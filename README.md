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
	
## SSS deployment from release packages
* follow instructions for Java 8, Apache Tomcat 7, [Apache Solr 4.9] and MySQL 5.6 in chapters below
* download desired release from within this repository (i.e. `https://github.com/learning-layers/SocialSemanticServer/releases`)
* extract `sss.package.zip`
* SSS deployment with REST API
 * copy `sss.package/sss.conf.yaml` to `/sssWorkDir/`
 * adapt conf to your needs 
 * copy `sss.package/sss.adapter.rest.v3.war` to `Catalina Base/webapps`
 * start Tomcat
* access Swagger UI from `http://localhost:8080/sss.adapter.rest.v3/`
* access the REST API via requests to `http://localhost:8080/sss.adapter.rest.v3/rest/{API}/{OP or ID}` 
 * `API`: REST resource to be targeted
 * `OP or ID`: path to the actual service call to be executed, e.g., GET to `http://localhost:8080/sss.adapter.rest.v3/rest/entities/{entity}` gets information for a certain entity 

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
* run `mvn clean install` on project `SocialSemanticServer` to have `adapter/adapter.rest/adapter.rest.v3/target/adapter.rest.v3.1.0-SNAPSHOT.war`
* rename `adapter.rest.v3.1.0-SNAPSHOT.war` to `sss.adapter.rest.v3.war` and copy to `Tomcat webapps` directory

## References
* S. Dennerlein, D. Kowald, E. Lex, D. Theiler, E. Lacic, T. Ley, V. Tomberg, A. Ruiz-Calleja [The Social Semantic Server: A Flexible Framework to Support Informal Learning at the Workplace](https://www.researchgate.net/publication/280920425_The_Social_Semantic_Server_A_Flexible_Framework_to_Support_Informal_Learning_at_the_Workplace#full-text), 2015. 15th International Conference on Knowledge Technologies and Data-driven Business (i-KNOW 2015), Graz, Austria
* A. Ruiz-Calleja, S. Dennerlein, V. Tomberg, K. Pata, T. Ley, D. Theiler and E. Lex [Supporting learning analytics for informal workplace learning with a social semantic infrastructure](https://www.researchgate.net/publication/282733837_Supporting_learning_analytics_for_informal_workplace_learning_with_a_social_semantic_infrastructure), 2015. European Conference on Technology Enhanced Learning 2015
* A. Ruiz-Calleja, S. Dennerlein, V. Tomberg, T. Ley, D. Theiler, E. Lex [Integrating data across workplace learning applications with a social semantic infrastructure](https://www.researchgate.net/publication/282733581_Integrating_data_across_workplace_learning_applications_with_a_social_semantic_infrastructure), 2015. 14th International Conference on Web-based Learning
* S. Dennerlein, D. Theiler, P. Marton, P. Santos Rodriguez, J. Cook, S. Lindstaedt and E. Lex [KnowBrain: An Online Social Knowledge Repository for Informal Workplace Learning](https://www.researchgate.net/publication/281819090_KnowBrain_An_Online_Social_Knowledge_Repository_for_Informal_Workplace_Learning#full-text), 2015. European Conference on Technology Enhanced Learning 2015
* D. Kowald, S. Dennerlein, D. Theiler, S. Walk and C. Trattner.: [The Social Semantic Server - A Framework to Provide Services on Social Semantic Network Data](https://www.researchgate.net/publication/256297004_The_Social_Semantic_Server_A_Framework_to_Provide_Services_on_Social_Semantic_Network_Data#full-text), 2013. In S. Lohmann (ed.), I-SEMANTICS (Posters & Demos) (p./pp. 50-54), : CEUR-WS.org.
* Dennerlein, S., Rella, M, Tomberg, V. Theiler, D., Treasure-Jones, T., Kerr, M., Ley, T., Al-Smadi, M. & Trattner, C. (2014). [Making Sense of Bits and Pieces: A Sensemaking Tool for Informal Workplace Learning](https://www.researchgate.net/publication/264861254_Making_Sense_of_Bits_and_Pieces_A_Sensemaking_Tool_for_Informal_Workplace_Learning#full-text), 2014. European Conference on Technology Enhanced Learning 2014

## Contact
* Dieter Theiler, Know-Center, Graz University of Technology, dtheiler#tugraz.at
