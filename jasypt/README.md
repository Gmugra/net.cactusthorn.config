# net.cactusthorn.config.jasypt
The module provides a parameterized converter to decrypt properties that were encrypted with [Jasypt](http://www.jasypt.org) Password-Based Encryption.

### Example
e.g. 
my.properties:
```
encrypted=U79blAyCnFylcjX5wpCl/TVDHmy+MSSw
```
interface:
```java
import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.extras.jasypt.PBEDecryptor;

@Config(sources = {"classpath:my.properties"})
public interface ConfigPBE {

    @PBEDecryptor("pbepassword") // "pbepassword" is name of the System Property with password for decrypting
    String encrypted();
}
```
usage:
```
java -Dpbepassword=megapass -jar myapp.jar
```

### Installing
Download: [Maven Central Repository](https://search.maven.org/search?q=g:net.cactusthorn.config).   
Download: [GitHub Packages](https://github.com/Gmugra?tab=packages&repo_name=net.cactusthorn.config).

In order to use the library in a project, it's need to add the dependency to the pom.xml:
```xml
<dependency>
    <groupId>net.cactusthorn.config</groupId>
    <artifactId>jasypt</artifactId>
    <version>0.30</version>
</dependency>
```
or with Gradle:
```
compile 'net.cactusthorn.config:jasypt:0.30'
```

## LICENSE
net.cactusthorn.config is released under the BSD 3-Clause license. See [LICENSE](https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE) file included for the details.



