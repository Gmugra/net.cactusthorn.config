# net.cactusthorn.config.hjson
The module provides loaders for files in [Hjson](https://hjson.github.io) format

1.  Hjson file from class-path : `classpath:relative-path-to-name.hjson[#charset]`
    -   Default charset (if URI fragment not present) is **UTF-8**
    -   e.g. `classpath:config/my.hjson#ISO-5589-1`

2.  Hjson file from any URI convertable to URL: `whatever-what-supported.hjson[#charset]`
    -   Default charset (if URI fragment not present) is **UTF-8**
    -   e.g. the file from the working directory: `file:./my.hjson`
    -   e.g. Windows file: `file:///C:/my.hjson`
    -   e.g. web: `https://raw.githubusercontent.com/Gmugra/net.cactusthorn.config/main/core/src/test/resources/test.hjson`

## Restrictions
1.  root must be object (not array)

2.  arrays in array are **not supported**. e.g.:
```hjson
{
  id:
  [
    f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454
    [
      123e4567-e89b-12d3-a456-556642440000
    ]
  ]
}
```

3.  object in array are **not supported**. e.g.:
```hjson
{
  ids:
  [
    f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454
    {
      aaa: 123e4567-e89b-12d3-a456-556642440000
    }
  ]
}
```

4.  arrays are converter in comma `,` separated string.

## Example
e.g. 
my.hjson:
```hjson
{
  // this is comment
  id:
  [
    f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454
    123e4567-e89b-12d3-a456-556642440000
  ]
  database:
  {
    skipit: null
    enabled: true
    ports:
    [
      8000
      8001
      null
      8002
    ]
    temp_targets:
    {
      cpu: 79.5
      case: 72
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
the source interface for my.hjson:
```java
@Config(sources={"classpath:my.hjson"})
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
    <artifactId>config-hjson</artifactId>
    <version>0.80</version>
</dependency>
```
or with Gradle:
```groovy
api 'net.cactusthorn.config:config-hjson:0.80'
```

## OSGi
The module is using [hjson-java](https://github.com/hjson/hjson-java) JAR which is not OSGi-bundle.
This is why there is a special bundle-JAR which includes *hjson-java* (and it's runtime dependencies) and can be used in the OSGi container:
```xml
<dependency>
    <groupId>net.cactusthorn.config</groupId>
    <artifactId>config-hjson</artifactId>
    <version>0.80</version>
    <classifier>bundle</classifier>
</dependency>
```
You must also install the *config-core* into the OSGi container, which is the OSGi-bundle itself.

## LICENSE
net.cactusthorn.config is released under the BSD 3-Clause license. See [LICENSE](https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE) file included for the details.
