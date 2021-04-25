
# net.cactusthorn.config

The Java library with the goal of minimizing the code required to handle application configuration.

[![Build Status](https://travis-ci.com/Gmugra/net.cactusthorn.config.svg?branch=main)](https://travis-ci.com/Gmugra/net.cactusthorn.config) [![Coverage Status](https://coveralls.io/repos/github/Gmugra/net.cactusthorn.config/badge.svg?branch=main)](https://coveralls.io/github/Gmugra/net.cactusthorn.config?branch=main) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/Gmugra/net.cactusthorn.config.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Gmugra/net.cactusthorn.config/context:java) [![GitHub](https://img.shields.io/github/license/Gmugra/net.cactusthorn.config)](https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE) [![Build by Maven](http://maven.apache.org/images/logos/maven-feather.png)](http://maven.apache.org)

## Motivation

The inspiring idea for the project comes from [OWNER](https://github.com/lviggiano/owner). *OWNER* is a nice Java library for the same purpose, but it's future is "gray", because it not actually maintened anymore. So, this project is providing similar with *OWNER* API, but
1. Based not on Reflection, but on compile-time Code Generation (Java Annotation Processing)
1. Required at least Java 8, as result it support "more fresh" language features e.g. `java.util.Optional`
1. There is not goal, to provide *all* features of *OWNER*

### Basic usage

To access properties you need to define a convenient Java interface, e.g. :
```java
package my.superapp;

import static net.cactusthorn.config.core.Disable.Feature.*
import net.cactusthorn.config.core.*

import java.util.concurrent.TimeUnit;
import java.util.Set;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Config 
@Prefix("app")
interface MyConfig {

    @Default("unknown")
    String val();
    
    @Key("number")
    int intVal();
    
    @Disable(PREFIX)
    List<UUID> ids();
    
    @Split("[:;]")
    @Default("DAYS:HOURS")
    Optional<Set<TimeUnit>> units();
}
```
Based on this interface annotation-processor will generate implementation.
And all what is need to get properties values, is to get the implementation using factory, e.g.:
```java
MyConfig myConfig =
    ConfigFactory.builder()
        .addSource("file:./myconfig.properties")
        .addSource("classpath:config/myconfig.properties", "system:properties")
        .build()
        .create(MyConfig.class);
```
The interface before is waiting "myconfig.properties":
```java
app.val=ABC
app.number=10
ids=f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454,123e4567-e89b-12d3-a456-556642440000
app.units=DAYS:HOURS;MICROSECONDS 
```

### Supported Return types
The return type of the interface methods must either:
1. Be a primitive type (except `char`)
1. Have a public constructor that accepts a single `String` argument
1. Have a public static method named `valueOf` or `fromString` that accepts a single `String` argument (e.g. [Integer.valueOf](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html#valueOf-java.lang.String-), [UUID.fromString](https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html#fromString-java.lang.String-))
   1. If both methods are present then `valueOf` used unless the type is an `enum` in which case `fromString` used.
1. Be `List<T>`, `Set<T>` or `SortedSet<T>`, where T satisfies 2 or 3 above. The resulting collection is read-only.
1. Be `Optional<T>`, where T satisfies 2, 3 or 4 above

### Annotations

1. `@Config`
   - `@Target(TYPE)`
   - The "source" interface must be annotated with this annotation.
1. `@Prefix`
   - `@Target(TYPE)`
   - Set global prefix for all property names
1. `@Key`
   - `@Target(METHOD)`
   - Set property name for the method. If this annotation is not present method-name will be used as property name
1. `@Default`
   - `@Target(METHOD)`
   - Set default value (if property will not found in sources, the default value will be used).
   - Can't be used for methods with `Optional` return type.
1. `@Disable`
   - `@Target(METHOD)`
   - Disable interface-level features for this method.
1. `@Split`
   - `@Target({TYPE,METHOD})`
   - Set splitter regular expression for splitting value for collections.
   - If this annotation is not present, default "splitter" is comma : `,`

### Property not found : `@Default` or `Optional`
There are three options for dealing with properties that are not found in sources:
1. If method return type is not `Optional` and the method do not annotated with `@Default`, ConfigFactory.create will throw *RuntimeException* "property ... not found"
2. If method return type is `Optional` ->  method will return `Optional.empty()`
3. If method return type is not `Optional`, but the method do annotated with `@Default` -> method will return converted to return type deafult value.

### Standard Loaders
1. System properties: `system:properties`
1. Environment variables: `system:env`
1. properties file from class-path : classpath:*relative-path-to-name*.properties[#charset]
   - Default charset (if URI fragment not present) is **UTF-8**
   - e.g. `classpath:config/my.properties#ISO-5589-1`
1. properties file from any URI convertable to URL: *whatever-what-supported*.properties[#charset]
   - Default charset (if URI fragment not present) is **UTF-8**
   - e.g. the file from the application folder: `file:./my.properties`
   - e.g. windows file: `file:///C:/my.properties`
   - e.g. web: `https://raw.githubusercontent.com/Gmugra/net.cactusthorn.config/main/core/src/test/resources/test.properties`
   - e.g. jar in file-system: `jar:file:path/to/some.jar!/path/to/your.properties`

### Loader interface
...

### System properties and/or environment variable in sources URIs

Syntax: {*name*}

e.g.
- `file:/{config-path}/my.properties`
- `classpath:{config-path}/my.properties#{charset}`

### Loading strategies
ConfigFactory saves the sequence in which the sources URIs were added.
```java
MyConfig myConfig =
    ConfigFactory.builder()
        .setLoadStrategy(LoadStrategy.FIRST)
        .addSource("file:/myconfig.properties", "classpath:config/myconfig.properties")
        .build()
        .create(MyConfig.class);
```
Loading strategies:
- **FIRST** - only the first (in the sequence of adding) existing and not empty source will be used.
- **MERGE** - merging all properties from first added to last added.
- Default strategy is **MERGE**

Manually added properties (which added using `ConfigFactory.Builder.setSource(Map<String, String> properties)` method) are highest priority always.
So, loaded by URIs properties merged with manually added properties, independent of loading strategy.

## FYI : Eclipse

It does not have annotation-processing enabled by default. To do this, you must install *m2e-apt* from the eclipse marketplace: https://immutables.github.io/apt.html

## LICENSE
net.cactusthorn.config is released under the BSD 3-Clause license. See LICENSE file included for the details.
