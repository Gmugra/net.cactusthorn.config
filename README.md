# net.cactusthorn.config

The Java library with the goal of minimizing the code required to handle application configuration.

[![build](https://github.com/Gmugra/net.cactusthorn.config/actions/workflows/maven.yml/badge.svg)](https://github.com/Gmugra/net.cactusthorn.config/actions) [![Coverage Status](https://coveralls.io/repos/github/Gmugra/net.cactusthorn.config/badge.svg?branch=main)](https://coveralls.io/github/Gmugra/net.cactusthorn.config?branch=main) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/e95b3233c26d4b8ab31e1956c16fcc0d)](https://www.codacy.com/gh/Gmugra/net.cactusthorn.config/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Gmugra/net.cactusthorn.config&amp;utm_campaign=Badge_Grade) [![Maven Central with version prefix filter](https://img.shields.io/maven-central/v/net.cactusthorn.config/config-core/0.81)](https://search.maven.org/search?q=g:%22net.cactusthorn.config%22) [![GitHub](https://img.shields.io/github/license/Gmugra/net.cactusthorn.config)](https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE) [![Build by Maven](http://maven.apache.org/images/logos/maven-feather.png)](http://maven.apache.org)

## Table of Contents

-   [Motivation](#motivation)

-   [Features](#features)

-   [Basics](#basics)
    -   [Installing](#installing)
    -   [Basic usage](#basic-usage)
    -   [Annotations](#annotations)
    -   [`@Default` or Optional](#property-not-found--default-or-optional)
    -   [`@Config` annotation parameters](#config-annotation-parameters)
    -   [System properties and/or environment variables in `@Key` and/or `@Prefix`](#system-properties-andor-environment-variables-in-key-andor-prefix)

-   [The `ConfigFactory`](#the-configfactory)
    -   [Direct access to properties](#direct-access-to-properties)
    -   [`@Factory` annotation](#factory-annotation)
    -   [Manually added properties](#manually-added-properties)
    -   [Caching](#caching)
    -   [Global prefix](#global-prefix)

-   [Type conversion](#type-conversion)
    -   [Supported method return types](#supported-method-return-types)
    -   [Maps](#maps)
    -   [`java.util.Locale` format](#javautillocale-format)
    -   [`java.time.Instant` format](#javatimeinstant-format)
    -   [`java.time.Duration` formats](#javatimeduration-formats)
    -   [`java.time.Period` formats](#javatimeperiod-formats)
    -   [`net.cactusthorn.config.core.converter.bytesize.ByteSize` format](#netcactusthornconfigcoreconverterbytesizebytesize-format)
    -   [Custom converters](#custom-converters)
    -   [Parameterized custom converters](#parameterized-custom-converters)

-   [Loaders](#loaders)
    -   [Standard loaders](#standard-loaders)
    -   [Custom loaders](#custom-loaders)
    -   [Service-provider loading facility](#spi)
    -   [System properties and/or environment variables in sources URIs](#system-properties-andor-environment-variables-in-sources-uris)
    -   [Loading strategies](#loading-strategies)
    -   [Periodical auto reloading](#periodical-auto-reloading)
    -   [Reload event listeners](#reload-event-listeners)

-   [Interfaces](#interfaces)
    -   [Interfaces inheritance](#interfaces-inheritance)
    -   [`java.io.Serializable`](#javaioserializable)
    -   [`net.cactusthorn.config.core.Accessible`](#netcactusthornconfigcoreaccessible)
    -   [`net.cactusthorn.config.core.Reloadable`](#netcactusthornconfigcorereloadable)

-   [Miscellaneous](#miscellaneous)
    -   [Extras](#extras)
    -   [Profiles](#profiles)
    -   [Integration with DI containers](#integration-with-di-containers)
    -   [Eclipse IDE](#eclipse-ide)

-   [LICENSE](#license)


## Motivation
The inspiring idea for the project comes from [OWNER](https://github.com/lviggiano/owner). *OWNER* is a nice Java library for the same purpose, but it's not factually maintained anymore, and it's not really support "new" language features from Java 8+.

So, this project is providing library with similar with *OWNER* API, but
-   Based not on Reflection, but on compile-time Code Generation (Java Annotation Processing).
-   Required at least Java 11, as result it support "more fresh" language features.

## Features
-   Core is plain Java 11 without any external dependencies

-   Uses no reflection or runtime bytecode generation; generates plain Java source code.

-   Small (< 100KB) & lightweight core runtime part

-   Ready to use with [OSGi](https://www.osgi.org)

-   Supports multiple configuration sources: files, classpath, URLs, environment variables, system properties, META-INF/MANIFEST.MF, Apache ZooKeeper

-   Supports files in Multiple formats:
    -   [Java properties](https://en.wikipedia.org/wiki/.properties)
    -   [Java properties represented by the XML document](https://docs.oracle.com/javase/8/docs/api/java/util/Properties.html#loadFromXML-java.io.InputStream-)
    -   [OWNER user’s specified XML format](http://owner.aeonbits.org/docs/xml-support/)
    -   [TOML](https://toml.io/en/)
    -   [JSON](https://www.json.org/json-en.html)
    -   [YAML](https://yaml.org)
    -   [Hjson](https://hjson.github.io)

-   Supports multiple loading strategies (configuration sources fallback/merging)

-   Expandable with custom source loaders

-   Powerful type conversions: collections, maps, enums, etc.

-   Parameterized type converters

-   Expandable with custom type converters

-   Special support for `java.util.Optional`, `java.time.*`, byte-size settings (e.g. `10Mb`), [Jasypt](http://www.jasypt.org) password-based encryption

-   Caching

-   Seamless integration with DI containers

-   Thread-safe

-   Reloading, Periodical auto reloading, Reload event listeners

## Basics

### Installing
Download: [Maven Central Repository](https://search.maven.org/search?q=g:%22net.cactusthorn.config%22).

Download: [GitHub Packages](https://github.com/Gmugra?tab=packages&repo_name=net.cactusthorn.config).

In order to use the library in a project, it's need to add the dependency to the pom.xml:
```xml
<dependency>
    <groupId>net.cactusthorn.config</groupId>
    <artifactId>config-core</artifactId>
    <version>0.81</version>
</dependency>
```
It's also need to include the compiler used to convert annotated "source"-interfaces into the code:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
         <annotationProcessorPaths>
              <path>
                  <groupId>net.cactusthorn.config</groupId>
                  <artifactId>config-compiler</artifactId>
                  <version>0.81</version>
              </path>
         </annotationProcessorPaths>
    </configuration>
</plugin>
```
FYI: With this configuration, Maven will output the generated code into `target/generated-sources/annotations`.

Same with Gradle:
```groovy
api 'net.cactusthorn.config:config-core:0.81'
annotationProcessor 'net.cactusthorn.config:config-compiler:0.81'
```

### Basic usage
To access properties it's need to define a convenient Java interface, e.g. :
```java
@Config
@Prefix("app")
public interface MyConfig {

    @Default("unknown")
    String val();

    @Key("number")
    int intVal();

    Optional<URI> uri();

    @Disable(PREFIX)
    List<UUID> ids();

    @Split("[,:;]")
    @Default("DAYS;HOURS")
    Set<TimeUnit> units();

    @LocalDateParser({ "dd.MM.yyyy", "yyyy-MM-dd" })
    LocalDate date();
}
```
-   An interface must be annotated with `@Config`.
-   An interface must contain at least one method declaration (but methods declaration can be also in super interface(s)).
-   All methods must be without parameters

Based on the interface, the annotation processor will generate an implementation, that can be obtained using `ConfigFactory`:
```java
MyConfig myConfig =
    ConfigFactory.builder()
        .setLoadStrategy(LoadStrategy.MERGE)
        .addSource("file:~/myconfig.xml")
        .addSource("classpath:config/myconfig-owner.xml")
        .addSource("jar:file:path/to/some.jar!/path/to/myconfig.properties")
        .addSource("https://somewhere.com/myconfig.toml")
        .addSource("file:./myconfig.json")
        .addSource("file:./myconfig.yaml")
        .build()
        .create(MyConfig.class);
```
e.g. "myconfig.properties":
```properties
app.val=ABC
app.number=10
app.uri=http://java.sun.com/j2se/1.3/
ids=f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454,123e4567-e89b-12d3-a456-556642440000
app.units=DAYS:HOURS;MICROSECONDS
app.date=12.11.2005
```
e.g. "myconfig.xml" (properties style xml):
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
    <entry key="app.val">ABC</entry>
    <entry key="app.number">10</entry>
    <entry key="app.uri">http://java.sun.com/j2se/1.3/</entry>
    <entry key="ids">f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454,123e4567-e89b-12d3-a456-556642440000</entry>
    <entry key="app.units">DAYS;HOURS;MICROSECONDS</entry>
    <entry key="app.date">12.11.2005</entry>
</properties>
```
e.g. "myconfig-owner.xml" ([OWNER](http://owner.aeonbits.org/docs/xml-support/) xml format):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<app>
    <val>ABC</val>
    <number>10</number>
    <uri>http://java.sun.com/j2se/1.3/</uri>
    <units>DAYS;HOURS;MICROSECONDS</units>
    <date>12.11.2005</date>
</app>
```
e.g. "myconfig.toml" ([TOML](https://toml.io) format):
```toml
ids = ["f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454","123e4567-e89b-12d3-a456-556642440000"]

[app]
val = "ABC"
number = 10
uri = "http://java.sun.com/j2se/1.3/"
units = ["DAYS", "HOURS", "MICROSECONDS"]
date = 2005-11-12
```
e.g. "myconfig.json" ([JSON](https://www.json.org/json-en.html) format):
```json
{
    "ids" : ["f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454", "123e4567-e89b-12d3-a456-556642440000"],
    "app" : {
        "val" : "ABC",
        "number" : 10,
        "uri" : "http://java.sun.com/j2se/1.3/",
        "units" : ["DAYS", "HOURS", "MICROSECONDS"],
        "date" : "2005-11-12"
    }
}
```
e.g. "myconfig.yaml" ([YAML](https://yaml.org) format):
```yaml
ids:
  - f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454
  - 123e4567-e89b-12d3-a456-556642440000
app:
  val: ABC
  number: 10
  uri: http://java.sun.com/j2se/1.3/
  units:
    - DAYS
    - HOURS
    - MICROSECONDS
  date: '2005-11-12'
```

### Annotations

1.  `@Config`
    -   `@Target(TYPE)`
    -   The "source" interface must be annotated with this annotation.

2.  `@Factory`
    -   `@Target(TYPE)`
    -   The "factory" interface must be annotated with this annotation.

3.  `@Prefix`
    -   `@Target(TYPE)`
    -   Set global prefix for all property names

4.  `@Key`
    -   `@Target(METHOD)`
    -   Set property name for the method. If this annotation is not present method-name will be used as property name

5.  `@Default`
    -   `@Target(METHOD)`
    -   Set default value (if property will not found in sources, the default value will be used).
    -   Can't be used for methods with `Optional` return type.

6.  `@Disable`
    -   `@Target({TYPE, METHOD})`
    -   Disable "global"-level features for this method.

7.  `@Split`
    -   `@Target({TYPE, METHOD})`
    -   Set splitter regular expression for splitting value for collections, or key+value "entries" for maps.
    -   If this annotation is not present, default "splitter" is comma : `,`

8.  `@ConverterClass`
    -   `@Target({METHOD, ANNOTATION_TYPE})`
    -   apply custom converter implementation

9.  `@LocalDateParser`, `@LocalDateTimeParser`, `@LocalTimeParser`, `@ZonedDateTimeParser`, `@OffsetDateTimeParser`, `@OffsetTimeParser`, `@YearParser`, `@YearMonthParser`, `@MonthDayParser`
    -   `@Target(METHOD)`
    -   apply a parameterized by formats converter to the relevant java.time.* type

10. `@PBEDecryptor`
    -   `@Target(METHOD)`
    -   decrypt properties that were encrypted with [Jasypt](http://www.jasypt.org) Password-Based Encryption. FYI: [jasypt](https://github.com/Gmugra/net.cactusthorn.config/tree/main/jasypt)

### Property not found : `@Default` or `Optional`
There are three ways for dealing with properties that are not found in sources:
1.  If method return type is not `Optional` and the method do not annotated with `@Default`, the `ConfigFactory.create` method will throw runtime exception "property ... not found"

2.  If method return type is `Optional` ->  method will return `Optional.empty()`

3.  If method return type is not `Optional`, but the method do annotated with `@Default` -> method will return converted to return type default value.

FYI:
-   The `@Default` annotation can't be used with a method that returns `Optional`.
-   If method return type is List, Set, SortedSet, Map, SortedMap -> method will return empty collection or map.

### `@Config` annotation parameters
There are two *optional* parameters `sources` and `loadStrategy` which can be used to override these settings from [`ConfigFactory`](#the-configfactory).
e.g.
```java
@Config(sources = {"classpath:config/testconfig2.properties","nocache:system:properties"},
        loadStrategy = LoadStrategy.FIRST)
public interface ConfigOverride {

    String string();
}
```
1.  If `sources` parameter is present, all sources added in the [`ConfigFactory`](#the-configfactory) (using `ConfigFactory.Builder.addSource` methods) will be ignored.
2.  If `loadStrategy` parameter is present, it will be used instead of loadStrategy from [`ConfigFactory`](#the-configfactory).
3.  Manually added properties (which added using `ConfigFactory.Builder.setSource(Map<String, String> properties)` method) are highest priority anyway. These properties will be merged in any case.

### System properties and/or environment variables in `@Key` and/or `@Prefix`
The `@Key` and `@Prefix` annotations can refer to system properties or environment variables.

This feature makes it possible to store, for example, settings for different environments in a single configuration file. e.g. (TOML):
```toml
host = "https://www.google.com/"
port = 80

[dev]
host = "https://github.com/"
port = 90

[prod]
host = "https://www.wikipedia.org/"
port = 100
```

Syntax: {*name*} or {*name*:*default-value*}

e.g.
```java
@Config
public interface MyServer {
    @Key("{env}.host") URL host();
    @Key("{env}.port") int port();
}
```
or (with same result)
```java
@Config
@Prefix("{env}")
public interface MyServer {
    URL host();
    int port();
}
```
usage e.g.:
```console
java -Denv=dev -jar myapp.jar
```

FYI:
1.  If a system property or environment variable does not exist, specified default-value will be used.
2.  If a system property or environment variable does not exist, and default-value not specified, an empty string will be used as the value.
3.  After expanding, start & end points `.` will be dropped.
4.  After expanding, multiple points (e.g `...`) inside the key name will be substituted to single `.`.

| system property value | key config | resulting key |
| --- | --- | --- |
| dev | {env}.host | dev.host |
| | {env}.host | host |
| dev | server.{env}.host | server.dev.host |
| | server.{env}.host | server.host |
| dev | host.{env} | host.dev |
| | host.{env} | host |

| system property value | key config | resulting key |
| --- | --- | --- |
| dev | {env:test}.host | dev.host |
| | {env:test}.host | test.host |
| dev | server.{env:test}.host | server.dev.host |
| | server.{env:test}.host | server.test.host |
| dev | host.{env:test} | host.dev |
| | host.{env:test} | host.test |

## The `ConfigFactory`
The `ConfigFactory` class is thread-safe, but not stateless.
It stores loaded properties in the internal cache (see [Caching](#caching)), and also control auto reloading.
Therefore, it certainly makes sense to create and use one single instance of `ConfigFactory` for the whole application.

### Direct access to properties
It's possible to get loaded properties without define config-interface.
```java
ConfigHolder holder =
    ConfigFactory.builder()
        .setLoadStrategy(LoadStrategy.FIRST)
        .addSource("file:./myconfig.properties")
        .addSource("classpath:config/myconfig.properties", "system:properties")
        .build()
        .configHolder();

String val = holder.getString("app.val", "unknown");
int intVal = holder.getInt("app.number");
Optional<List<UUID>> ids = holder.getOptionalList(UUID::fromString, "ids", ",");
Set<TimeUnit> units = holder.getSet(TimeUnit::valueOf, "app.units", "[:;]", "DAYS:HOURS");
```

### `@Factory` annotation
There is one place where Java-reflection is used: `ConfigFactory.create` method.
`@Factory` annotation provides the ability to generate  "Factory"-class(es) which helps to avoid reflection completely.
```java
@Config
public interface MyConfig {
    String val();
}
```
```java
@Factory
public interface MyFactory {
    MyConfig createMyConfig();
}
```
```java
MyConfig myConfig = Factory_MyFactory.builder().addSource("file:./myconfig.properties").build().createMyConfig();
```
As you can see, based on the `MyFactory`-interface annotated by `@Factory`, the class `Factory_MyFactory` will be generated, which has same API with `ConfigFactory` but instead of `create`-method it provides "create"-methods for the interface annotated by `@Factory`.
Restrictions:
-   an interface annotated by `@Factory` must contains at least one method
-   an interface annotated by `@Factory` must contains only methods without parameters
-   all methods of an interface annotated by `@Factory` must return only types annotated by `@Config`


### Manually added properties
The `ConfigFactory.Builder` contains a method for adding properties manually: `setSource(Map<String, String> properties)`.
Manually added properties are highest priority always: loaded by URIs properties merged with manually added properties, independent of loading strategy.
In other words: the manually added properties will always override (sure, when the property keys are same) properties loaded by URI(s).

There is two major use-cases for the feature: unit-tests & console applications.
For console applications, it is convenient to provide command line arguments to the `ConfigFactory` using this feature.

### Caching
By default, `ConfigFactory` caches loaded properties using source-URI (after resolving system properties and/or environment variable in it) as a cache key.

To not cache properties related to the URI(s), use URI-prefix `nocache:` this will switch off caching for the URI.
e.g.
-   `nocache:system:properties`
-   `nocache:file:~/my.properties`

### Global Prefix
The `ConfigFactory.Builder` provide the method to set global prefix `setGlobalPrefix(String prefix)`, which will be used for all "config"-interfaces that will be created using the factory:
```properties
app.val=ABC
```
```java
@Config
public interface MyConfig {
    String val();
}
```
```java
MyConfig myConfig = ConfigFactory.builder().setGlobalPrefix("app").build().create(MyConfig.class);
```
This makes it possible to avoid `@Prefix` or/and `@Key` annotations, in case several "config"-interfaces are created based on the same source(s).
FYI:
-   Global-prefix is added before the prefix from the `@Prefix` annotation. They can be used together.
-   Global-prefix also support system properties and/or environment variables (like `@Prefix` and `@Key`)
-   Global-prefix can be switched off for the method or "config"-interface using `@Disable(Disable.Feature.GLOBAL_PREFIX)`


## Type conversion

### Supported method return types
The return type of the interface methods must either:
1.  Be a primitive type

2.  Have a public constructor that accepts a single `String` argument
    -   e.g. [StringBuilder](https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html#StringBuilder-java.lang.String-)

3.  Have a public static method named `valueOf` or `fromString` that accepts a single `String` argument
    -   e.g. [Integer.valueOf](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html#valueOf-java.lang.String-)
    -   e.g. [UUID.fromString](https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html#fromString-java.lang.String-)
    -   e.g. any `enum`
    -   If both methods are present then `valueOf` used unless the type is an `enum` in which case `fromString` used.

4.  Be
    -   `java.net.URL`
    -   `java.net.URI`
    -   `java.nio.file.Path`
    -   `java.util.Currency`
    -   `java.util.Locale`
    -   `java.util.regex.Pattern`
    -   `java.time.Instant`
    -   `java.time.Duration`
    -   `java.time.Period`
    -   `java.time.LocalDate`
    -   `java.time.LocalDateTime`
    -   `java.time.LocalTime`
    -   `java.time.ZonedDateTime`
    -   `java.time.OffsetDateTime`
    -   `java.time.OffsetTime`
    -   `java.time.Year`
    -   `java.time.YearMonth`
    -   `java.time.MonthDay`
    -   `java.time.ZoneId`
    -   `java.time.ZoneOffset`
    -   `net.cactusthorn.config.core.converter.bytesize.ByteSize`

5.  Be `List<T>`, `Set<T>` or `SortedSet<T>`, where **T** satisfies 2, 3 or 4 above. The resulting collection is read-only.

6.  Be `Map<K,V>` or `SortedMap<K,V>`, where
    -   **K** satisfies 2, 3 or 4 above.
    -   **V** satisfies 2, 3 or 4 above.
    -   The resulting map is read-only.

7.  Be `Optional<T>`, where **T** satisfies 2, 3 or 4 above

### Maps
Maps support is limited to two restrictions:
1.  custom converters are not supported for the *key*
2.  as key-value separator can be used only `|` (pipe character)

e.g. "myconfig.properties":
```java
map=A|10,BBB|20
map2=10000|10;20000|20
```
```java
@Config(sources="classpath:/myconfig.properties")
public interface ConfigMap {

    Map<String, Integer> map();

    @Split(";") Optional<Map<Integer, Byte>> map2();

    @Default("123e4567-e89b-12d3-a456-556642440000|https://github.com") Map<UUID, URL> map3();
}
```
FYI:
1.  In case of Maps, `@Split` annotation set splitter for key+value "entries" (default "splitter" is comma : `,`).
2.  In case of Maps, the annotations associated with converters( e.g. `@ConverterClass`, `@ZonedDateTimeParser` etc.) only affect the Map values.

### `java.util.Locale` format
The string must be well-formed BCP 47 language tag.
https://docs.oracle.com/javase/8/docs/api/java/util/Locale.html#forLanguageTag-java.lang.String-
e.g. `de-DE`

### `java.time.Instant` format
The string must represent a valid instant in [UTC](https://en.wikipedia.org/wiki/Coordinated_Universal_Time) and is parsed using [DateTimeFormatter.ISO_INSTANT](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_INSTANT)
e.g. `2011-12-03T10:15:30Z`

### `java.time.Duration` formats
1.  Standard *ISO 8601* format, as described in the [JavaDoc for java.time.Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#parse-java.lang.CharSequence-). e.g. `P2DT3H4M`

2.  "unit strings" format:
    1.  Bare numbers are taken to be in milliseconds: `10`

    2.  Strings are parsed as a number plus an optional unit string: `10ms`, `10 days`

    3.  The supported unit strings for duration are case sensitive and must be lowercase. Exactly these strings are supported:
        -   `ns`, `nano`, `nanos`, `nanosecond`, `nanoseconds`
        -   `us`, `µs`, `micro`, `micros`, `microsecond`, `microseconds`
        -   `ms`, `milli`, `millis`, `millisecond`, `milliseconds`
        -   `s`, `second`, `seconds`
        -   `m`, `minute`, `minutes`
        -   `h`, `hour`, `hours`
        -   `d`, `day`, `days`

### `java.time.Period` formats
1.  Standard *ISO 8601* format, as described in the [JavaDoc for java.time.Period](https://docs.oracle.com/javase/8/docs/api/java/time/Period.html#parse-java.lang.CharSequence-). e.g. `P1Y2M3W4D`

2.  "unit strings" format:
    1.  Bare numbers are taken to be in days: `10`

    2.  Strings are parsed as a number plus an optional unit string: `10y`, `10 days`

    3.  The supported unit strings for duration are case sensitive and must be lowercase. Exactly these strings are supported:
        -   `d`, `day`, `days`
        -   `w`, `week`, `weeks`
        -   `m`, `mo`, `month`, `months`
        -   `y`, `year`, `years`

### `net.cactusthorn.config.core.converter.bytesize.ByteSize` format
It based on [OWNER](http://owner.aeonbits.org/docs/type-conversion/) classes to represent data sizes.

usage:
```java
@Config
public interface MyByteSize {

    @Default("10 megabytes")
    net.cactusthorn.config.core.converter.bytesize.ByteSize size();
}
```
The supported unit strings for `ByteSize` are case sensitive and must be lowercase. Exactly these strings are supported:
-   `byte`, `bytes`, `b`
-   `kilobyte`, `kilobytes`, `k`, `ki`, `kib`
-   `kibibyte`, `kibibytes`, `kb`
-   `megabyte`, `megabytes`, `m`, `mi`, `mib`
-   `mebibyte`, `mebibytes`, `mb`
-   `gigabyte`, `gigabytes`, `g`, `gi`, `gib`
-   `gibibyte`, `gibibytes`, `gb`
-   `terabyte`, `terabytes`, `t`, `ti`, `tib`
-   `tebibyte`, `tebibytes`, `tb`
-   `petabyte`, `petabytes`, `p`, `pi`, `pib`
-   `pebibyte`, `pebibytes`, `pb`
-   `exabyte`, `exabytes`, `e`, `ei`, `eib`
-   `exbibyte`, `exbibytes`, `eb`
-   `zettabyte`, `zettabytes`, `z`, `zi`, `zib`
-   `zebibyte`, `zebibytes`, `zb`
-   `yottabyte`, `yottabytes`, `y`, `yi`, `yib`
-   `yobibyte`, `yobibytes`, `yb`

### Custom converters
If it's need to deal with class which is not supported "by default" (see [Supported method return types](#supported-method-return-types)), a custom converter can be implemented and used.
```java
public class MyClassConverter implements Converter<MyClass> {

    @Override public MyClass convert(String value, String[] parameters) {
        ...
    }
}
```
The `@ConverterClass` annotation allows to specify the `Converter`-implementation for the config-interface method:
```java
@Config public interface MyConfigWithConverter {

    @ConverterClass(MyClassConverter.class) @Default("some super default value") MyClass theValue();

    @ConverterClass(MyClassConverter.class) Optional<MyClass> mayBeValue();

    @ConverterClass(MyClassConverter.class) Optional<List<MyClass>> values();

    @ConverterClass(MyClassConverter.class) Optional<Map<Path, MyClass>> map();
}
```
FYI:
1.  `Converter`-implementation must be stateless and must have a default(no-argument) `public` constructor.
2.  `Converter`-implementation can be used instead "standard" converting, so it's possible to implement specific converting for supported by default types.
3.  `Converter`-implementation can be used for interface or abstract class
4.  In case of maps, `Converter`-implementation are using only for values(not for keys)


### Parameterized custom converters
Sometimes it's convenient to set several constant parameters for the custom converter.
For example, to provide format(s) with a converter for date-time types.
This can be achieved with converter-annotation for the custom-converter:
```java
@Retention(SOURCE)
@Target(METHOD)
@ConverterClass(MyClassConverter.class) //converter implementation
public @interface MySuperParser {

    String[] value() default "";
}
```
FYI:
-   the annotation must contains `String[] value() default ""` parameter, otherwise parameters will be ignored by compiler
-   the annotation can be made for any converter (even for converter which is, actually, not need parameters)

usage:
```java
@Config
public interface MyConfig {

    @MySuperParser({"param1", "param1"})
    MyClass myValue();
}
```

Several of these annotations shipped with the library:
-   `@LocalDateParser`
-   `@LocalDateTimeParser`
-   `@LocalTimeParser`
-   `@ZonedDateTimeParser`
-   `@OffsetDateTimeParser`
-   `@OffsetTimeParser`
-   `@YearParser`
-   `@YearMonthParser`
-   `@MonthDayParser`


## Loaders

### Standard loaders
1.  System properties: `system:properties`

2.  Environment variables: `system:env`

3.  properties file from class-path : `classpath:relative-path-to-name.properties[#charset]`
    -   Default charset (if URI fragment not present) is **UTF-8**
    -   e.g. `classpath:config/my.properties#ISO-5589-1`

4.  properties file from any URI convertable to URL: `whatever-what-supported.properties[#charset]`
    -   Default charset (if URI fragment not present) is **UTF-8**
    -   e.g. the file from the working directory: `file:./my.properties`
    -   e.g. Windows file: `file:///C:/my.properties`
    -   e.g. web: `https://raw.githubusercontent.com/Gmugra/net.cactusthorn.config/main/core/src/test/resources/test.properties`
    -   e.g. jar in file-system: `jar:file:path/to/some.jar!/path/to/your.properties`

5.  XML file from class-path : `classpath:relative-path-to-name.xml[#charset]`
    -   XML format: [properties.dtd](https://docs.oracle.com/javase/8/docs/api/java/util/Properties.html) or [OWNER](http://owner.aeonbits.org/docs/xml-support/)
    -   Default charset (if URI fragment not present) is **UTF-8**
    -   e.g. `classpath:config/my.xml#ISO-5589-1`

6.  XML file from any URI convertable to URL: `whatever-what-supported.xml[#charset]`
    -   XML format: [properties.dtd](https://docs.oracle.com/javase/8/docs/api/java/util/Properties.html) or [OWNER](http://owner.aeonbits.org/docs/xml-support/)
    -   Default charset (if URI fragment not present) is **UTF-8**
    -   e.g. `file:./my.xml`

7.  META-INF/MANIFEST.MF: `classpath:jar:manifest?attribute[=value]`
    -   The loader scans all JARs in classpath for META-INF/MANIFEST.MF files. First META-INF/MANIFEST.MF, which contain *attribute* (with optional value) from the URI will be used as source.
    -   e.g. MANIFEST.MF must contain attribute **Bundle-Name** with value **JUnit Jupiter API**: `classpath:jar:manifest?Bundle-Name=JUnit%20Jupiter%20API`
    -   e.g. MANIFEST.MF must contain attribute **exotic-unique-attribute** with any value: `classpath:jar:manifest?exotic-unique-attribute`

### Custom loaders
It's possible to implement custom loaders using `Loader` interface.
This makes it possible to load properties from specific sources (e.g. Database) or to support alternative configuration-file formats.
e.g.
```java
public final class SinglePropertyLoader implements Loader {

    @Override public boolean accept(URI uri) {

        return uri.toString().equals("single:property");
    }

    @Override public Map<String, String> load(URI uri, ClassLoader classLoader) {

        Map<String, String> result = new HashMap<>();
        result.put("key", "value");
        return result;
    }
}
```
```java
ConfigFactory factory =
    ConfigFactory.builder()
    .addLoader(SinglePropertyLoader.class)
    .addSource("single:property")
    .build();
```
FYI:
-   Custom loaders always have the highest priority if added using `ConfigFactory.Builder.addLoader` method: last added -> first used.
-   Custom loader implementation must be stateless and must have a default(no-argument) `public` constructor.

### SPI
[Service-provider loading facility](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html) (introduced in JDK 1.6) can be used to *automatically* add custom loader implementation to the `ConfigFactory`. Simple add file *META-INF\services\net.cactusthorn.config.core.loader.Loader* with full-class-name of custom-loader implementation(s) in the class path.
e.g.
-   [core module](https://github.com/Gmugra/net.cactusthorn.config/blob/main/core/src/main/resources/META-INF/services/net.cactusthorn.config.core.loader.Loader)
-   [tests module](https://github.com/Gmugra/net.cactusthorn.config/blob/main/tests/src/main/resources/META-INF/services/net.cactusthorn.config.core.loader.Loader)
-   [toml module](https://github.com/Gmugra/net.cactusthorn.config/blob/main/toml/src/main/resources/META-INF/services/net.cactusthorn.config.core.loader.Loader)

### System properties and/or environment variables in sources URIs
Syntax: {*name*} or {*name*:*default-value*}
e.g.
-   `file:/{config-path}/my.properties`
-   `classpath:{config-path:home}/my.properties#{charset}`

FYI:
-   If a system property or environment variable does not exist and *default-value* is not specified, an *empty string* will be used as the value.

Special use-case *user home directory*: The URIs with `file:~/` (e.g. `file:~/my.xml` or `jar:file:~/some.jar!/your.properties`) always correctly resolved to user home directory independent from OS.
-   e.g. in Windows, URI `file:~/my.xml` will be replaced to `file:///C:/Users/UserName/my.xml`.

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
-   **FIRST** - only the first (in the sequence of adding) existing and not empty source will be used.
-   **MERGE** - merging all properties from first added to last added.
-   **FIRST_KEYCASEINSENSITIVE** - same with **FIRST**, but property keys are case insensitive
-   **MERGE_KEYCASEINSENSITIVE** - same with **MERGE**, but property keys are case insensitive
-   **FIRST_KEYRELAXED** - same with **FIRST**, but property keys are "relaxed".
-   **MERGE_KEYRELAXED** - same with **MERGE**, but property keys are "relaxed".
-   Default strategy is **MERGE**

"Relaxed":
-   keys are case insensitive
-   `.`(dot), `-`(minus) and `_`(underscore) characters are ignored
-   For example: **person.first-name**, **person.firstName** and **PERSON_FIRSTNAME** can all be used interchangeably.

Warning: Manually added properties (which added using `ConfigFactory.Builder.setSource(Map<String, String> properties)` method) are highest priority always. So, loaded by URIs properties merged with manually added properties, independent of loading strategy.

### Periodical auto reloading
ConfigFactory can automatically reload configurations which extends [`net.cactusthorn.config.core.Reloadable`](#netcactusthornconfigcorereloadable) interface.
To activate auto-reloading need to set "periodInSeconds" using `autoReload` method:
```java
ConfigFactory factory =
    ConfigFactory.builder()
        .addSource("file:/myconfig.properties")
        .autoReload(5) //reload every 5 seconds
        .build();
```

Warning: If you do not call `autoReload` method, auto reloading will not work.

But, the source will be reloaded only if it *changed*.
`Loader`-implementation should implement `contentHashCode` method which return hash-code. (The method return value should be changed, when URI related content is changed).
If `Loader`-implementation do not support auto-reloading (which is default behavior) the method is returns always same value (e.g. `0`).
As result, for the moment, auto reloading only supported for:
-   `system:properties`
-   URIs with **file:** scheme (only files related URIs). FYI: file last-modified-time is used as hash-code.

Warning: Be careful, non-cached(`nocache:`) sources will always be reloaded, whether they are modified or not.

It is possible to disable auto reloading for the "config"-interface, even if it is activated:
```java
@Config
@Disable(Disable.Feature.AUTO_RELOAD)
public interface MyConfig extends Reloadable {
    String value();
}
```

> **Filesystems quirks**
> The date resolution vary from filesystem to filesystem.
> For instance, for Ext3, ReiserFS and HSF+ the date resolution is of 1 second.
> For FAT32 the date resolution for the last modified time is 2 seconds.
> For Ext4 the date resolution is in nanoseconds.

### Reload event listeners
It would be nice to know which properties has changed as result of reloading, so that you can e.g. re-configure only the affected services.
It's possible to achieve using "Reload event listeners" feature.
Example how to do it: [ListenerTest](https://github.com/Gmugra/net.cactusthorn.config/tree/main/tests/src/test/java/net/cactusthorn/config/tests/listener/ListenerTest.java)

## Interfaces

### Interfaces inheritance
Interfaces inheritance is supported.
e.g.
```java
interface MyRoot {

    @Key(rootVal) String value();
}
```
```java
@Config
interface MyConfig extends MyRoot {

    int intValue();
}
```
-   There is no limit to the number and "depth" of super-interfaces.
-   Interface level annotations (e.g. `@Prefix`) on super-interfaces will be ignored.

### `java.io.Serializable`
"config"-interface can extends (directly or over super-interface) `java.io.Serializable`.
In this case generated class will also get `private static final long serialVersionUID` attribute.
```java
@Config
public interface MyConfig extends java.io.Serializable {

    long serialVersionUID = 100L;

    String val();
}
```
The interface (as in the example before) can, optionally, contains `long serialVersionUID` constant.
If the constant is present, the value will be used for the `private static final long serialVersionUID` attribute in the generated class.
Otherwise generated class will be generated with `private static final long serialVersionUID = 0L`.

### `net.cactusthorn.config.core.Accessible`
"config"-interface can extends (directly or over super-interface) `net.cactusthorn.config.core.Accessible`.
In this case generated class will also get methods for this interface:
```java
    Set<String> keys();

    Object get(String key);

    Map<String, Object> asMap();
```

### `net.cactusthorn.config.core.Reloadable`
"config"-interface can extends (directly or over super-interface) `net.cactusthorn.config.core.Reloadable`.
In this case generated class will also get methods for this interface:
```java
    void reload();

    boolean autoReloadable();

    void addReloadListener(ReloadListener listener);
```
FYI: The method always reload *not cached* sources, even if they not changed (see [Caching](#caching))

## Miscellaneous

### Extras
"Extras" are optional extensions (converters and loaders) that need external dependencies and therefore can't be integrated into the core library.
-   **[jasypt](https://github.com/Gmugra/net.cactusthorn.config/tree/main/jasypt)** : provide `@PBEDecryptor` annotation which decrypt properties that were encrypted with [Jasypt](http://www.jasypt.org) Password-Based Encryption.
-   **[toml](https://github.com/Gmugra/net.cactusthorn.config/tree/main/toml)** : provide loaders for files in [TOML](https://toml.io) format
-   **[json](https://github.com/Gmugra/net.cactusthorn.config/tree/main/json)** : provide loaders for files in [JSON](https://www.json.org/json-en.html) format
-   **[yaml](https://github.com/Gmugra/net.cactusthorn.config/tree/main/yaml)** : provide loaders for files in [YAML](https://yaml.org) format
-   **[hjson](https://github.com/Gmugra/net.cactusthorn.config/tree/main/hjson)** : provide loaders for files in [Hjson](https://hjson.github.io) format
-   **[zookeeper](https://github.com/Gmugra/net.cactusthorn.config/tree/main/zookeeper)** : provide loader for properties from [Apache ZooKeeper](https://zookeeper.apache.org)

### Logging
The runtime part of the library is using [System.Logger](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.Logger.html).
This way, you can use any logging library you prefer:
-   Apache Log4j 2 -> org.apache.logging.log4j:log4j-jpl
-   SLF4J -> org.slf4j:slf4j-jdk-platform-logging

### Profiles
There is no specific support for profiles, but it is easy to achieve similar behavior using *System properties and/or environment variables* in sources URIs,
e.g.:
```java
ConfigFactory.builder()
    .addSource("file:~/myconfig-{myapp.profile}.properties")
    .addSource("file:./myconfig-{myapp.profile}.properties")
    .addSource("classpath:myconfig.properties")
    .build();
```
and get *profile* from, for example, system property:
```console
java -Dmyapp.profile=DEV -jar myapp.jar
```

### Integration with DI containers
Example with [Dagger 2](https://dagger.dev):
-   Config Interface: [AppInfo](https://github.com/Gmugra/net.cactusthorn.micro/blob/master/core/src/main/java/net/cactusthorn/micro/core/configuration/AppInfo.java)
-   Dagger 2 module: [CoreModule](https://github.com/Gmugra/net.cactusthorn.micro/blob/master/core/src/main/java/net/cactusthorn/micro/core/dagger/CoreModule.java)
-   Usage (DI over constructor): [Banner](https://github.com/Gmugra/net.cactusthorn.micro/blob/master/core/src/main/java/net/cactusthorn/micro/core/banner/Banner.java)
-   One more config Interface: [HikariConf](https://github.com/Gmugra/net.cactusthorn.micro/blob/master/db/src/main/java/net/cactusthorn/micro/db/configuration/HikariConf.java)
-   One more Dagger 2 module: [DatabaseModule](https://github.com/Gmugra/net.cactusthorn.micro/blob/master/db/src/main/java/net/cactusthorn/micro/db/dagger/DatabaseModule.java)

### Eclipse IDE
It does not have annotation-processing enabled by default. To get it, you must install *m2e-apt* from the eclipse marketplace: https://immutables.github.io/apt.html

## LICENSE
net.cactusthorn.config is released under the BSD 3-Clause license. See [LICENSE](https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE) file included for the details.
