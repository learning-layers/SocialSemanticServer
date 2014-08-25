SocialSemanticServer
====================
## Towards A Framework for Social Semantic Network Data
[![BuildStatus](http://layers.dbis.rwth-aachen.de/jenkins/buildStatus/icon?job=SocialSemanticServer)](http://layers.dbis.rwth-aachen.de/jenkins/job/SocialSemanticServer/)

## Description
The main goal of the Social Semantic Server (SSS) is to establish a framework to provide services for informal learning by handling Social Semantic Network data. 
By creating an infrastructure that allows for social negotiation of semantic meaning and enabling meaningful learning, it will enable situated and contextualized learning in turn.

![alt tag](https://raw.githubusercontent.com/learning-layers/SocialSemanticServer/bba6324551551b41f43e3b630e2376ecde83c807/desc.jpg)

Please cite [the paper](https://github.com/learning-layers/SocialSemanticServer#references) if you use this software in one of your publications.

## Download
The source-code can be directly checked-out through this repository. It contains a Maven project to edit and build it.

## How-to-use SSS for development
### Set-up
* In order to deploy and run your own server instance of the SSS, please follow instructions below. For REST-API access, please have a look at at project [SSS Client Side](https://github.com/learning-layers/SocialSemanticServerClientSide/) as well.

#### Java 8
* please use Java 8 or higher from (Java Site)[https://java.com/en/download/index.jsp]

#### Apache Maven 3
* please use Maven 3 or higher from [Maven Site](http://maven.apache.org/download.cg)
* check out the Maven installation guide at [Maven Guides](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)

#### Apache Tomcat 7
* please use Tomcat 7 or higher from [Tomcat Site](http://tomcat.apache.org/download-70.cgi)

#### Apache Solr 4.9
* this guide was derived form [Apache Solr Reference Guide](http://tweedo.com/mirror/apache/lucene/solr/ref-guide/apache-solr-ref-guide-4.9.pdf)
* download Solr from http://mirror2.klaus-uwe.me/apache/lucene/solr/4.9.0/ 
* decompress solr package
* rename folder for convenience to `solrPackage`
* copy to `solr home dir` (e.g.: /solr/)
 * /solrPackage/example/solr/ 
* copy to `solr home lib` dir (e.g.: /solr/lib/)
 * /solrPackage/contrib/
 * /solrPackage/dist/
* replace `schema.xml` and `solrconfig.xml` in `solr home core's conf` dir with (e.g.: /solr/collection1/conf)
 * SSS/ss/src/main/resources/conf/solr_schema.xml
 * SSS/ss/src/main/resources/conf/solr_solrconfig.xml
* adjust `solrconfig.xml` to have directives pointing to solr home lib subfolders, e.g.:
 * <lib dir="/solr/lib/contrib/extraction/lib" regex=".*\.jar" />
 * <lib dir="/solr/lib/dist/" regex="solr-cell-\d.*\.jar" />
 * <lib dir="/solr/lib/contrib/clustering/lib/" regex=".*\.jar" />
 * <lib dir="/solr/lib/dist/" regex="solr-clustering-\d.*\.jar" />
 * <lib dir="/solr/lib/contrib/langid/lib/" regex=".*\.jar" />
 * <lib dir="/solr/lib/dist/" regex="solr-langid-\d.*\.jar" />
 * <lib dir="/solr/lib/contrib/velocity/lib" regex=".*\.jar" />
 * <lib dir="/solr/lib/dist/" regex="solr-velocity-\d.*\.jar" />
* set user `tomcat7` as owner for `solr home dir`
* stop tomcat
* edit tomcat's `catalina.sh` to point to your `solr home dir` (e.g.: /usr/share/tomcat7/bin/catalina.sh)
 * export JAVA_OPTS="$JAVA_OPTS -Dsolr.solr.home=/solr"
* copy to `tomcat lib` dir: (e.g.: /usr/share/tomcat7/lib)
 * /solrPackage/example/lib/ext/
 * /solrPackage/example/resources/log4j.properties
* adjust `log4j.properties` to your needs
* copy to `tomcat webapps` dir (e.g.: /var/lib/tomcat7/webapps/)
 * /solrPackage/example/webapps/solr.war
* start tomcat

#### MySQL 5.6
* please use MySQL 5.6 or higher from [MySQL Site](http://www.mysql.com/downloads/)
* have at least the following set in your `my.cnf` 
 * [client] 
   * default-character-set=utf8
 * [mysql] 
   * default-character-set=utf8
 * [mysqld] 
   * init-connect='SET NAMES utf8'
    * character-set-server = utf8
* run `SSS/ss/src/main/resources/conf/sss_schema.sql` to setup MySQL database schema

#### SSS and REST adapter
* download SSS containing its REST adapter from this repository

#### Code import
* import the SSS project as Maven project into, e.g. Netbeans or Eclipse

#### Logging
* modify `log4j.properties` in `SSS/ss/src/main/resources/conf/` for your needs
* customize `log4j.properties` in `SSS/ss-adapter/ss-adapter-rest/src/main/resources`

#### Configuration
* adjust `SSS/ss/src/main/resources/conf/ss-conf.yaml`
* adjust and copy to `tomcat conf` dir (e.g. /var/lib/tomcat7/conf/) 
 * `SSS/ss-adapter/ss-adapter-rest/src/main/resources/ss-adapter-rest-conf.yaml`

#### Run
* run `mvn clean install` on project `SSS/ss-root` to have 
 * `SSS/ss/target/ss-app`
 * `SSS/ss-adapter/ss-adapter-rest/target/ss-adapter-rest-X.X-SNAPSHOT.war`
* copy directory `SSS/ss/target/ss-app/` to your SSS execution dir
* for configuration and logging please see chapter Logging and Configuration
* run `ss.jar` with `runinit.bat` or `.sh`
* deploy `SSS/ss-adapter/ss-adapter-rest/target/ss-adapter-rest-X.X-SNAPSHOT.war` 
 * rename `SSS/ss-adapter/ss-adapter-rest/target/ss-adapter-rest-X.X-SNAPSHOT.war` to `ss-adapter-rest.war` 
 * copy `ss-adapter-rest.war` to 'tomcat webapps' dir

## How-to-use SSS from releases for deployment only
* follow instructions for Java 8, Apache Tomcat 7, Apache Solr 4.9 and MySQL 5.6 in chapters above
* download `ss-package` from respective release from within this repository
* adjust `ss-package/ss-app/ss-conf.yaml` and `ss-package/ss-app/log4j.properties`
* copy `ss-package/ss-app/` to SSS's execution dir
* copy `ss-adapter-rest.war` to `tomcat webapps` dir
* adjust and copy `ss-adapter-rest-conf.yaml` to `tomcat conf` dir

## SSS client-side libraries
* download [SSS Client Side](https://github.com/learning-layers/SocialSemanticServerClientSide/) libs
* link Javascript projects `JSUtilities`, `SSClientInterfaceGlobals` and `SSSClientInterfaceREST` in your application to have access to SSS server-side operations via its REST interface

## SSS plain REST API access
* access the REST API via POST requests call `http://your-sss-host:your-port/ss-adapter-rest/rest/SSAdapterRest/yourOp/` 
 * `your-sss-host` represents the host running the REST API
 * `yourOp` represents one operation out of the ones provided by SSS (see client-side project)
* generally JSON strings are expected as input (mime type `application/json`)
* return values are JSON strings (except for, e.g. file downloading), e.g. `{"op":"collWithEntries", "error":false, "errorMsg":null, "opValue":{"author":"http://dt.ll/user/hugo/",...}}")`
* additionally, all properties/variables returned by any API call will contain a JSON-LD description
* following key-value pairs are returned (except for non-JSON returns) normally
 * `op` the operation returning the result
 * `"opValue"` opValue will always be replaced by the op value provided for the op parameter and contains the actual result for the API request - the value of the operation's return
 * `error` whether an error occured server-side
 * `errorMsg` if error, "errorMsg" gets set to respective exception messages thrown
 * `errorClassNames` if error, class names of errors
 * `errorClassesWhereThrown` if error, class names where the errors got thrown
 * `errorMethodsWhereThrown` if error, method names where the errors got thrown
 * `errorLinesWhereThrown` if error, line numbers where the errors got thrown
 * `errorThreadsWhereThrown` if error, id's of threads where the errors got thrown
 
## References
* D. Kowald, S. Dennerlein, D. Theiler, S. Walk and C. Trattner.: [The Social Semantic Server - A Framework to Provide Services on Social Semantic Network Data](http://ceur-ws.org/Vol-1026/paper11.pdf), 2013. In S. Lohmann (ed.), I-SEMANTICS (Posters & Demos) (p./pp. 50-54), : CEUR-WS.org.
* Dennerlein, S., Rella, M, Tomberg, V. Theiler, D., Treasure-Jones, T., Kerr, M., Ley, T., Al-Smadi, M. & Trattner, C. (2014). Making Sense of Bits and Pieces: A Sensemaking Tool for Informal Workplace Learning. In: Proceedings of EC-TEL 2014. In press.

## Contact
* Dieter Theiler, Know-Center, Graz University of Technology, dtheiler@tugraz.at
