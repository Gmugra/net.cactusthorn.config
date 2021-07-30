# net.cactusthorn.config.zookeeper
The module provides loader for properties from [Apache ZooKeeper](https://zookeeper.apache.org)

Source URI format: `zookeeper://connectString/basePath[?optionalParameters]`
e.g.
-   `zookeeper://localhost:7777,localhost:8888/zk_test`
-   `zookeeper://localhost:7777/zk_test?sessionTimeoutMs=5000&connectionTimeoutMs=1000

Optional parameters:
-   sessionTimeoutMs - default value(if it not present in the URI) is 5000
-   connectionTimeoutMs - default value(if it not present in the URI) is 1000
-   blockUntilConnectedMaxWaitTimeMs - default value(if it not present in the URI) is 30000

## Installing
Download: [Maven Central Repository](https://search.maven.org/search?q=g:net.cactusthorn.config).   
Download: [GitHub Packages](https://github.com/Gmugra?tab=packages&repo_name=net.cactusthorn.config).

In order to use the library in a project, it's need to add the dependency to the pom.xml:
```xml
<dependency>
    <groupId>net.cactusthorn.config</groupId>
    <artifactId>config-zookeeper</artifactId>
    <version>0.70</version>
</dependency>
```
or with Gradle:
```groovy
api 'net.cactusthorn.config:config-zookeeper:0.70'
```

## LICENSE
net.cactusthorn.config is released under the BSD 3-Clause license. See [LICENSE](https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE) file included for the details.
