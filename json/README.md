# net.cactusthorn.config.json
The module provides loaders for files in [JSON](https://www.json.org/json-en.html) format

1.  JSON file from class-path : `classpath:relative-path-to-name.json[#charset]`
    -   Default charset (if URI fragment not present) is **UTF-8**
    -   e.g. `classpath:config/my.json#ISO-5589-1`

2.  JSON file from any URI convertable to URL: `whatever-what-supported.json[#charset]`
    -   Default charset (if URI fragment not present) is **UTF-8**
    -   e.g. the file from the working directory: `file:./my.json`
    -   e.g. Windows file: `file:///C:/my.json`

## Restrictions
1.  root must be object (not array)
2.  arrays in array (e.g. `"data" : [ ["delta", "phi"], [3.14] ]`) are **not supported**.
3.  object in array (e.g. `"points" : [{"x" = 1, "y" = 2}, { "x" = 7, "y" = 8}, {"x" = 2, "y" = 4}]`) are **not supported**.
4.  arrays are converter in comma `,` separated string.

## Example
e.g. 
my.json:
```json
{
    "id" : ["f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454", "123e4567-e89b-12d3-a456-556642440000"],
    "database" : {
        "enabled" : true,
        "ports" : [8000, 8001, 8002],
        "temp_targets" : {
            "cpu" : 79.5,
            "case" : 72.0
        }
    }
}
```
the file, internally, factually "converted" in the next "properties" file:
```properties
id=f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454,123e4567-e89b-12d3-a456-556642440000
database.enabled=true
database.ports=8000,8001,8002
database.temp_targets.cpu=79.5
database.temp_targets.case=72.0
```
the source interface for my.json:
```java
@Config(sources={"classpath:my.json"})
@Prefix("database")
public interface ConfigJson {

    @Disable(Feature.PREFIX) List<UUID> id();

    boolean enabled();

    Set<Integer> ports();

    @Key("temp_targets.cpu") double ttcpu();

    @Key("temp_targets.case") double ttcase();
}
```

## Installing
Download: [Maven Central Repository](https://search.maven.org/search?q=g:net.cactusthorn.config).   
Download: [GitHub Packages](https://github.com/Gmugra?tab=packages&repo_name=net.cactusthorn.config).

In order to use the library in a project, it's need to add the dependency to the pom.xml:
```xml
<dependency>
    <groupId>net.cactusthorn.config</groupId>
    <artifactId>config-json</artifactId>
    <version>0.81</version>
</dependency>
```
or with Gradle:
```groovy
api 'net.cactusthorn.config:config-json:0.81'
```

## OSGi
This module-JAR is ready to use OSGi-bundle.
But, you must also install the *config-core* and [gson](https://github.com/google/gson) in the OSGi container, which are the OSGi-bundles themselves.

## LICENSE
net.cactusthorn.config is released under the BSD 3-Clause license. See [LICENSE](https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE) file included for the details.
