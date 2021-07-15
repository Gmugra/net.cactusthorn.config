# net.cactusthorn.config.yaml
The module provides loaders for files in [YAML](https://yaml.org) format

1.  YAML file from class-path : `classpath:relative-path-to-name.yaml[#charset]`
    -   Default charset (if URI fragment not present) is **UTF-8**
    -   e.g. `classpath:config/my.yaml#ISO-5589-1`

2.  YAML file from any URI convertable to URL: `whatever-what-supported.yaml[#charset]`
    -   Default charset (if URI fragment not present) is **UTF-8**
    -   e.g. the file from the working directory: `file:./my.yaml`
    -   e.g. Windows file: `file:///C:/my.yaml`

## Restrictions
1.  arrays in array are **not supported**.
2.  map in array are **not supported**.
3.  arrays are converter in comma `,` separated string.

## Example
e.g. 
my.yaml:
```yaml
id:
  - f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454
  - 123e4567-e89b-12d3-a456-556642440000
database:
  enabled: true
  ports:
    - 8000
    - 8001
    - 8002
  temp_targets:
    cpu: 79.5
    case: 72
```
the file, internally, factually "converted" in the next "properties" file:
```properties
id=f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454,123e4567-e89b-12d3-a456-556642440000
database.enabled=true
database.ports=8000,8001,8002
database.temp_targets.cpu=79.5
database.temp_targets.case=72.0
```
the source interface for my.yaml:
```java
@Config(sources={"classpath:my.yaml"})
@Prefix("database")
public interface ConfigYaml {

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
    <artifactId>config-yaml</artifactId>
    <version>0.60</version>
</dependency>
```
or with Gradle:
```groovy
api 'net.cactusthorn.config:config-yaml:0.60'
```

## LICENSE
net.cactusthorn.config is released under the BSD 3-Clause license. See [LICENSE](https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE) file included for the details.
