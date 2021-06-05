# net.cactusthorn.config.toml
The module provides loaders for files in [TOML](https://toml.io) format

1.  TOML file from class-path : `classpath:relative-path-to-name.toml[#charset]`
    -   Default charset (if URI fragment not present) is **UTF-8**
    -   e.g. `classpath:config/my.toml#ISO-5589-1`

2.  TOML file from any URI convertable to URL: `whatever-what-supported.toml[#charset]`
    -   Default charset (if URI fragment not present) is **UTF-8**
    -   e.g. the file from the working directory: `file:./my.toml`
    -   e.g. Windows file: `file:///C:/my.toml`
    -   e.g. web: `https://raw.githubusercontent.com/Gmugra/net.cactusthorn.config/main/core/src/test/resources/test.toml`

## Restrictions
1.  arrays in array (e.g. `data = [ ["delta", "phi"], [3.14] ]`) are **not supported**.
2.  tables in array (e.g. `points = [{x = 1, y = 2}, { x = 7, y = 8}, {x = 2, y = 4}]`) are **not supported**.
3.  arrays are converter in comma `,` separated string.

## Example
e.g. 
my.toml:
```toml
# This is a TOML document

title = "TOML Example"

[owner]
name = "Tom Preston-Werner"

[database]
enabled = true
ports = [ 8000, 8001, 8002 ]
temp_targets = { cpu = 79.5, case = 72.0 }

[servers]

[servers.alpha]
ip = "10.0.0.1"
role = "frontend"

[servers.beta]
ip = "10.0.0.2"
role = "backend"
```
the file, internally, factually "converted" in the next "properties" file:
```properties
title=TOML Example
owner.name=Tom Preston-Werner
database.enabled=true
database.ports=8000,8001,8002
database.temp_targets.cpu=79.5
database.temp_targets.case=72.0
servers.alpha.ip=10.0.0.1
servers.alpha.role=frontend
servers.beta.ip=10.0.0.2
servers.beta.role=backend
```
the source interface for my.toml:
```java
@Config(sources={"classpath:my.toml"})
public interface ConfigToml {

    @Key("title") String title();

    @Key("owner.name") String name();

    @Key("database.enabled") boolean enabled();

    @Key("database.ports") List<Integer> ports();

    @Key("database.temp_targets.cpu") double ttcpu();

    @Key("database.temp_targets.case") double ttcase();

    @Key("servers.alpha.ip") String aip();

    @Key("servers.alpha.role") String arole();

    @Key("servers.beta.ip") String bip();

    @Key("servers.beta.role") String brole();
}
```

## Installing
Download: [Maven Central Repository](https://search.maven.org/search?q=g:net.cactusthorn.config).   
Download: [GitHub Packages](https://github.com/Gmugra?tab=packages&repo_name=net.cactusthorn.config).

In order to use the library in a project, it's need to add the dependency to the pom.xml:
```xml
<dependency>
    <groupId>net.cactusthorn.config</groupId>
    <artifactId>config-toml</artifactId>
    <version>0.40</version>
</dependency>
```
or with Gradle:
```groovy
compile 'net.cactusthorn.config:config-toml:0.40'
```

## LICENSE
net.cactusthorn.config is released under the BSD 3-Clause license. See [LICENSE](https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE) file included for the details.
