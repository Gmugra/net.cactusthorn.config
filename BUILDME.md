
### Deploy as Release to GitHub Packages:

`mvn -Pgithub -Psources -DskipTests -Dchangelist= deploy`

### Deploy as SNAPSHOT to Maven.org:

`mvn -Possrh -Pjavadoc -Psources -DskipTests deploy`

### Deploy as Release to Maven.org:

`mvn -Possrh -Pjavadoc -Psources -DskipTests -Dchangelist= deploy`

### Generate JaCoCo Report

`mvn -Pcoverage clean verify`

where: target/site/jacoco/index.html

### Apply missing LICENSE header

`mvn com.mycila:license-maven-plugin:format`

Template: BSD-3-license-template.txt



