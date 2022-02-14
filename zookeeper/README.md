# net.cactusthorn.config.zookeeper
The module provides simple loader for properties from [Apache ZooKeeper](https://zookeeper.apache.org)

Source URI format: `zookeeper://connectString/basePath[?optionalParameters]`
e.g.
-   `zookeeper://localhost:7777,localhost:8888/zk_test`
-   `zookeeper://localhost:7777/zk_test?sessionTimeoutMs=5000&connectionTimeoutMs=1000`

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
    <version>0.80</version>
</dependency>
```
or with Gradle:
```groovy
api 'net.cactusthorn.config:config-zookeeper:0.80'
```

## OSGi
This module-JAR is ready to use OSGi-bundle.
You must also install the *config-core* and [org.apache.curator:curator-framework](https://curator.apache.org/index.html) in the OSGi container, which are the OSGi-bundles themselves.

FYI: *curator-framework* required a lot of other OSGi-bundles:
-   com.fasterxml.jackson.core:jackson-annotations
-   com.fasterxml.jackson.core:jackson-core
-   com.fasterxml.jackson.core:jackson-databind
-   com.google.guava:failureaccess
-   com.google.guava:guava
-   org.slf4j:slf4j-api
-   ch.qos.logback:logback-classic + ch.qos.logback:logback-core (it could be other implementation of slf4j)
-   org.apache.zookeeper:zookeeper
-   org.apache.curator:curator-client

## LICENSE
net.cactusthorn.config is released under the BSD 3-Clause license. See [LICENSE](https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE) file included for the details.
