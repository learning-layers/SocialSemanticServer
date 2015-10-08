SocialSemanticServer
====================
## Towards A Framework for Social Semantic Network Data
<!-- [![BuildStatus](http://layers.dbis.rwth-aachen.de/jenkins/buildStatus/icon?job=SocialSemanticServer)](http://layers.dbis.rwth-aachen.de/jenkins/job/SocialSemanticServer/) -->

## Description
The main goal of the Social Semantic Server (SSS) is to establish a framework to provide services for informal learning by handling Social Semantic Network data. 
By creating an infrastructure that allows for social negotiation of semantic meaning and enabling meaningful learning, it will enable situated and contextualized learning in turn.

![alt tag](https://raw.githubusercontent.com/learning-layers/SocialSemanticServer/bba6324551551b41f43e3b630e2376ecde83c807/desc.jpg)

Please cite one of the [papers](https://github.com/learning-layers/SocialSemanticServer#references) if you use this software in one of your publications.

## Download
The source-code can be directly checked-out through this repository. It contains a Maven project to edit and build it.

## Documentation
* for REST API description please have a look at Swagger's JSON-based REST API documentation in releases (i.e. `sss.package/api-docs`)
* for Swagger UI styled documention from Swagger's JSON files please deploy and access SSS's REST API (see below)
	
## SSS deployment from release packages
* follow instructions for Java 8, Apache Tomcat 7, [Apache Solr 4.9] and MySQL 5.6 in chapters below
* download desired release from within this repository (i.e. `https://github.com/learning-layers/SocialSemanticServer/releases`)
* extract `sss.package.zip`
* REST API Tomcat deployment
 * copy `sss.package/sss.adapter.rest.v2.conf.yaml` to `Catalina Base/conf`
 * adapt conf property `sss` 
  * set `host` and `port` to the location at which SSS will be running
 * copy `sss.package/sss.adapter.rest.v2.war` to `Catalina Base/webapps`
 * start Tomcat
* SSS deployment
 * copy folder `sss.package/sss.app` to your desired destination (execution directory) and jump into
 * adapt `log4j.properties` to your needs
 * adapt `sss.conf.yaml` to your needs
   * make sure `host` and `port` properties of `sss` in `sss.conf.yaml` match the same attributes in `sss.adapter.rest.v2.conf.yaml`
   * make sure to set property `authType` of `auth` in `sss.conf.yaml` accordingly (either `csvFileAuth`or `oidc`)
     * for `csvFileAuth` make sure to have `users.csv` (in your execution directory) filled with combinations of users' emails and passwords (i.e. "email@email.com;password")
 * start SSS with `java -jar -Dlog4j.configuration=file:log4j.properties ./sss.jar`
* access Swagger UI from `http://localhost:8080/sss.adapter.rest.v2/`
* access the REST API via requests to `http://localhost:8080/sss.adapter.rest.v2/{API}/{API}/{OP or ID}` 
 * `API`: REST resource to be targeted
 * `OP or ID`: path to the actual service call to be executed, e.g., GET to `http://localhost:8080/sss.adapter.rest.v2/entities/entities/{entity}` gets information for a certain entity 

## SSS service usage via Swagger UI
* to login use either GET or POST calls in `.../sss.adapter.rest.v2/#!/auth`
 * for GET (i.e. `OIDC` authentication) set your OIDC token to be sent via input field on top saying `add auth key to be sent in header`
 * for POST (i.e. `CSV` file based authentication) use provided template (click `Model Schema` tab in the `Data Type` column)
   * fill out `label`and `password` with your email address and password
* to use any other service operation 
 * make sure to have the login key returned (from either `auth` service call in attribute `key`) put to the input field on top saying `add auth key to be sent in header`
 * when using, e.g., `.../sss.adapter.rest.v2/#!/recomm/recommUsersForEntity` (i.e. `/recomm/recomm/users/entity/{entity}`), setting required parameters can be done either with encoded URIs (i.e. `encode('http://google.com')` or with IDs directly (as long as the ID is one from SSS's realm (i.e. created in / added to SSS)))

## SSS requirements

### Java 8
* please use Java 8 or higher from [Java Site](https://java.com/en/download/index.jsp)

### Apache Maven 3
* please use Maven 3 or higher from [Maven Site](http://maven.apache.org/download.cg)
* check out the Maven installation guide at [Maven Guides](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)

### Apache Tomcat 7
* please use Tomcat 7 or higher from [Tomcat Site](http://tomcat.apache.org/download-70.cgi)

### Apache Solr 4.9
* this guide was derived from [Apache Solr Reference Guide](http://tweedo.com/mirror/apache/lucene/solr/ref-guide/apache-solr-ref-guide-4.9.pdf)
* download Solr from, e.g., [Solr Mirror](http://mirror2.klaus-uwe.me/apache/lucene/solr/4.9.0/)
* decompress Solr package
* rename folder for convenience to `solrPackage`
* copy to `Solr Home` (e.g.: `/solr/`)
 * `/solrPackage/example/solr/`
* copy to `Solr Home` `Lib` directory (e.g.: `/solr/lib/`)
 * `/solrPackage/contrib/`
 * `/solrPackage/dist/`
 * for certain bug-fixes for PDF extraction and indexing 
  * upgrade in `Solr Home` `lib/contrib/extraction/`
    * `pdfbox-1.8.4.jar` to `pdfbox-1.8.6.jar`
    * `tika-core-1.5.jar` to tika-core-1.6.jar`
    * `tika-parsers-1.5.jar` to `tika-parsers-1.6.jar`
    * `tika-xmp-1.5.jar` to `tika-xmp-1.6.jar`
* replace `schema.xml` and `solrconfig.xml` in `Solr Home` `Core's Conf` directory (e.g.: `/solr/collection1/conf`) with: 
 * `sss.package/sss.app/solr_schema.xml`
 * `sss.package/sss.app/solr_solrconfig.xml`
* adjust `solrconfig.xml` to have directives pointing to `Solr Home` `Lib` subfolders, e.g.:
 * `<lib dir="/solr/lib/contrib/extraction/lib" regex=".*\.jar" />`
 * `<lib dir="/solr/lib/dist/" regex="solr-cell-\d.*\.jar" />`
 * `<lib dir="/solr/lib/contrib/clustering/lib/" regex=".*\.jar" />`
 * `<lib dir="/solr/lib/dist/" regex="solr-clustering-\d.*\.jar" />`
 * `<lib dir="/solr/lib/contrib/langid/lib/" regex=".*\.jar" />`
 * `<lib dir="/solr/lib/dist/" regex="solr-langid-\d.*\.jar" />`
 * `<lib dir="/solr/lib/contrib/velocity/lib" regex=".*\.jar" />`
 * `<lib dir="/solr/lib/dist/" regex="solr-velocity-\d.*\.jar" />`
* set user `tomcat7` as owner for `Solr Home` directory
* stop Tomcat
* edit Tomcat's `catalina.sh` (e.g.: `/usr/share/tomcat7/bin/catalina.sh`) to point to `Solr Home` directory
 * `export JAVA_OPTS="$JAVA_OPTS -Dsolr.solr.home=/solr"`
* copy to `Tomcat` `Lib` dir: (e.g.: `/usr/share/tomcat7/lib`)
 * `/solrPackage/example/lib/ext/` contents
 * `/solrPackage/example/resources/log4j.properties`
* adjust `log4j.properties` to your needs
* copy to `Tomcat Webapps` directory (e.g.: `/var/lib/tomcat7/webapps/`)
 * `/solrPackage/example/webapps/solr.war`
* start Tomcat

### MySQL 5.6
* please use `MySQL 5.6` or higher from [MySQL Site](http://www.mysql.com/downloads/)
* have at least the following set in your `my.cnf` 
 * [client] 
   * default-character-set=utf8
 * [mysql] 
   * default-character-set=utf8
 * [mysqld] 
   * init-connect='SET NAMES utf8'
    * character-set-server = utf8
* either import `sss.package/sss.app/sss_schema.sql` to setup `sss` scheme or apply respective database migration script, e.g., `sss.package/sss.app/sss_schema_upgrade_6.0.0_6.0.1.sql`

## SSS deployment from source code
* run `mvn clean install` on project `sss.root` to have 
 * `sss/target/sss.app/`
 * `adapter/adapter.rest/adapter.rest.vX/target/adapter.rest.vX.X-SNAPSHOT.war`
* run `sss/target/sss.app/sss.jar` with VM options `-Dlog4j.configuration=file:log4j.properties`
* rename `adapter.rest.vX.X-SNAPSHOT.war` to `sss.adapter.rest-vX.war` and copy to `Tomcat webapps` directory

## SSS documentation from source code
* for apiVersion `v2` 
 * adapt `swagger-maven-plugin` in `adapter/adapter.rest/adapter.rest.v2/pom.xml`
    * property `basePath` to `http://tomcatHost:tomcatPort/sss.adapter.rest.v2`
    * property `swaggerUIDocBasePath` to `http://tomcatHost:tomcatPort/sss.adapter.rest.v2/api-docs`
 * adapt Swagger property `url` of object `SwaggerUi` in `adapter/adapter.rest/adapter.rest.v2/src/main/webapp/swagger/index.html` to `http://tomcatHost:tomcatPort/sss.adapter.rest.v2/api-docs` 
* build and deploy Web project
 * `adapter/adapter.rest/adapter.rest.v2`
* access Swagger docs
 * `http://tomcatHost:tomcatPort/sss.adapter.rest.v2/index.html`
 
## References
* S. Dennerlein, D. Kowald, E. Lex, D. Theiler, E. Lacic, T. Ley, V. Tomberg, A. Ruiz-Calleja [The Social Semantic Server: A Flexible Framework to Support Informal Learning at the Workplace](https://www.researchgate.net/publication/280920425_The_Social_Semantic_Server_A_Flexible_Framework_to_Support_Informal_Learning_at_the_Workplace#full-text), 2015. 15th International Conference on Knowledge Technologies and Data-driven Business (i-KNOW 2015), Graz, Austria
* D. Kowald, S. Dennerlein, D. Theiler, S. Walk and C. Trattner.: [The Social Semantic Server - A Framework to Provide Services on Social Semantic Network Data](http://ceur-ws.org/Vol-1026/paper11.pdf), 2013. In S. Lohmann (ed.), I-SEMANTICS (Posters & Demos) (p./pp. 50-54), : CEUR-WS.org.
* Dennerlein, S., Rella, M, Tomberg, V. Theiler, D., Treasure-Jones, T., Kerr, M., Ley, T., Al-Smadi, M. & Trattner, C. (2014). Making Sense of Bits and Pieces: A Sensemaking Tool for Informal Workplace Learning. In: Proceedings of EC-TEL 2014. In press.

## Contact
* Dieter Theiler, Know-Center, Graz University of Technology, dtheiler@tugraz.at
