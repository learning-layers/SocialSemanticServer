SocialSemanticServer
====================
## Towards A Framework for Social Semantic Network Data
<!-- [![BuildStatus](http://layers.dbis.rwth-aachen.de/jenkins/buildStatus/icon?job=SocialSemanticServer)](http://layers.dbis.rwth-aachen.de/jenkins/job/SocialSemanticServer/) -->

## Description
The main goal of the Social Semantic Server (SSS) is to establish a framework to provide services for informal learning by handling Social Semantic Network data. 
By creating an infrastructure that allows for social negotiation of semantic meaning and enabling meaningful learning, it will enable situated and contextualized learning in turn.

![alt tag](https://raw.githubusercontent.com/learning-layers/SocialSemanticServer/bba6324551551b41f43e3b630e2376ecde83c807/desc.jpg)

Please cite [the paper](https://github.com/learning-layers/SocialSemanticServer#references) if you use this software in one of your publications.

## Download
The source-code can be directly checked-out through this repository. It contains a Maven project to edit and build it.

## Documentation
* Please find the documentation of the SSS REST APIs in:
 * `ss-package` from respective release from within this repository in folder `documentation`
 * `http://{your-host}:{your-port}/ss-adapter-rest/swagger/generated/document.html`
 * `SSS/ss-adapter/ss-adapter-rest/src/main/webapp/swagger/generated/document.html`
 
## SSS for deployment
* follow instructions for Java 8, Apache Tomcat 7, Apache Solr 4.9 and MySQL 5.6 in chapters below
* download `ss-package` from respective release from within this repository
* adjust `ss-package/ss-app/ss-conf.yaml` and `ss-package/ss-app/log4j.properties`
* copy `ss-package/ss-app/` to custom SSS's execution dir
* copy `ss-package/ss-adapter-rest.war` to `tomcat webapps` dir
* adjust and copy `ss-package/ss-adapter-rest-conf.yaml` to `tomcat conf` dir
* run `runit.sh / .bat`
 
## SSS for development

### Java 8
* please use Java 8 or higher from [Java Site](https://java.com/en/download/index.jsp)

### Apache Maven 3
* please use Maven 3 or higher from [Maven Site](http://maven.apache.org/download.cg)
* check out the Maven installation guide at [Maven Guides](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)

### Apache Tomcat 7
* please use Tomcat 7 or higher from [Tomcat Site](http://tomcat.apache.org/download-70.cgi)

### Apache Solr 4.9
* this guide was derived from [Apache Solr Reference Guide](http://tweedo.com/mirror/apache/lucene/solr/ref-guide/apache-solr-ref-guide-4.9.pdf)
* download Solr from, e.g. [Solr Mirror](http://mirror2.klaus-uwe.me/apache/lucene/solr/4.9.0/)
* decompress solr package
* rename folder for convenience to `solrPackage`
* copy to `solr home dir` (e.g.: `/solr/`)
 * `/solrPackage/example/solr/`
* copy to `solr home lib` dir (e.g.: `/solr/lib/`)
 * `/solrPackage/contrib/`
 * `/solrPackage/dist/`
 * for certain bug-fixes for PDF extraction and indexing 
  * upgrade in `/solrPackage/contrib/extraction/`
   * `pdfbox-1.8.4.jar` to `pdfbox-1.8.6.jar`
   * `tika-core-1.5.jar` to tika-core-1.6.jar`
   * `tika-parsers-1.5.jar` to `tika-parsers-1.6.jar`
   * `tika-xmp-1.5.jar` to `tika-xmp-1.6.jar`
* replace `schema.xml` and `solrconfig.xml` in `solr home core's conf` dir (e.g.: `/solr/collection1/conf`) with: 
 * `SSS/ss/src/main/resources/conf/solr_schema.xml`
 * `SSS/ss/src/main/resources/conf/solr_solrconfig.xml`
* adjust `solrconfig.xml` to have directives pointing to `solr home lib subfolders`, e.g.:
 * `<lib dir="/solr/lib/contrib/extraction/lib" regex=".*\.jar" />`
 * `<lib dir="/solr/lib/dist/" regex="solr-cell-\d.*\.jar" />`
 * `<lib dir="/solr/lib/contrib/clustering/lib/" regex=".*\.jar" />`
 * `<lib dir="/solr/lib/dist/" regex="solr-clustering-\d.*\.jar" />`
 * `<lib dir="/solr/lib/contrib/langid/lib/" regex=".*\.jar" />`
 * `<lib dir="/solr/lib/dist/" regex="solr-langid-\d.*\.jar" />`
 * `<lib dir="/solr/lib/contrib/velocity/lib" regex=".*\.jar" />`
 * `<lib dir="/solr/lib/dist/" regex="solr-velocity-\d.*\.jar" />`
* set user `tomcat7` as owner for `solr home dir`
* stop tomcat
* edit tomcat's `catalina.sh` (e.g.: `/usr/share/tomcat7/bin/catalina.sh`) to point to `solr home dir` 
 * `export JAVA_OPTS="$JAVA_OPTS -Dsolr.solr.home=/solr"`
* copy to `tomcat lib` dir: (e.g.: `/usr/share/tomcat7/lib`)
 * `/solrPackage/example/lib/ext/` contents
 * `/solrPackage/example/resources/log4j.properties`
* adjust `log4j.properties` to your needs
* copy to `tomcat webapps` dir (e.g.: `/var/lib/tomcat7/webapps/`)
 * `/solrPackage/example/webapps/solr.war`
* start tomcat

### MySQL 5.6
* please use MySQL 5.6 or higher from [MySQL Site](http://www.mysql.com/downloads/)
* have at least the following set in your `my.cnf` 
 * [client] 
   * default-character-set=utf8
 * [mysql] 
   * default-character-set=utf8
 * [mysqld] 
   * init-connect='SET NAMES utf8'
    * character-set-server = utf8
* either import `SSS/ss/src/main/resources/conf/sss_schema.sql` to setup `sss` scheme or apply respective database migration script, e.g. `SSS/ss/src/main/resources/conf/sss_schema_upgrade_6.0.0_6.0.1.sql`

### SSS and REST adapter
* download SSS containing its REST adapter from this repository
* import SSS as Maven project into, e.g. Netbeans or Eclipse

### Logging and Configuration
* adjust `SSS/ss/src/main/resources/conf/log4j.properties` and copy to `SSS/ss/`
* adjust `SSS/ss/src/main/resources/conf/ss-conf.yaml` and copy to `SSS/ss/`
* adjust `SSS/ss-adapter/ss-adapter-rest/src/main/resources/ss-adapter-rest-conf.yaml` and copy to `tomcat conf` dir

### Run
* run `mvn clean install` on project `SSS/ss-root` to have 
 * `SSS/ss/target/ss-app/`
 * `SSS/ss-adapter/ss-adapter-rest/target/ss-adapter-rest-X.X-SNAPSHOT.war`
* execute `SSS/ss/src/main/java/at/kc/tugraz/ss/main/SSMain.java` with VM options `-Dlog4j.configuration=file:log4j.properties`
* rename `ss-adapter-rest-X.X-SNAPSHOT.war` to `ss-adapter-rest.war` and copy to `tomcat webapps` dir

## SSS client-side libraries
* download [SSS Client Side](https://github.com/learning-layers/SocialSemanticServerClientSide/) libs
* link Javascript projects `JSUtilities`, `SSClientInterfaceGlobals` and `SSSClientInterfaceREST` in your application to have access to SSS server-side operations via its REST interface

## SSS plain REST API access
* access the REST APIs via POST requests to `http://{your-sss-host}:{your-port}/ss-adapter-rest/{API}/{yourOp}/` 
 * `your-sss-host` and `your-port` represents the host and port running the REST APIs
 * `API` stands for the name of the REST API to be targeted:
  * `SSAdapterRest` for all REST operations except for file handling
  * `SSAdapterRESTFile` for REST operations dealing with files not using any multipart form parameters as input
  * `SSAdapterRESTFileDownload` for REST operations dealing with file downloading
  * `SSAdapterRESTFileUpload` for REST operations dealing with file uploading
  * `SSAdapterRESTFileReplace` for REST operations dealing with file replacing
 * `yourOp` represents the operation out of the ones provided by SSS (see documentation section and / or client-side SSS JS project)
* generally JSON strings are expected as input (mime type `application/json`)
* return values are JSON strings (except for, e.g. file downloading), e.g. `{"op":"collWithEntries", "error":false, "errorMsg":null, "opValue":{"author":"http://dt.ll/user/hugo/",...}}")`
* additionally, all properties/variables returned by any API call will contain a JSON-LD description
* following key-value pairs are returned (except for non-JSON returns) normally beside the actual return value described in the server-side REST API documentation
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
