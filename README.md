SocialSemanticServer
====================
##Towards A Framework for Social Semantic Network Data
[![BuildStatus](http://layers.dbis.rwth-aachen.de/jenkins/buildStatus/icon?job=SocialSemanticServer)](http://layers.dbis.rwth-aachen.de/jenkins/job/SocialSemanticServer/)

## Description
The main goal of the Social Semantic Server (SSS) is to establish an informal learning service framework for handling Social Semantic Network data. 
By creating an infrastructure that allows for social negotiation of semantic meaning and enabling meaningful learning, it will enable situated and contextualized learning in turn.

![alt tag](https://raw.githubusercontent.com/learning-layers/SocialSemanticServer/bba6324551551b41f43e3b630e2376ecde83c807/desc.jpg)

Please cite [the paper](https://github.com/learning-layers/SocialSemanticServer#references) if you use this software in one of your publications.

## Download
The source-code can be directly checked-out through this repository. It contains a Maven project to edit and build it.

## How-to-use
### Set-up
Please have a look at the [Learning Layers Open Developer Library](http://developer.learning-layers.eu/documentation/social-semantic-server/) for set up and below or in project [SSS Client Side](https://github.com/learning-layers/SocialSemanticServerClientSide/) for REST API access. In order to deploy and run your own server instance of the SSS, please follow instructions below. 
####Apache Maven
* install from [Maven Site](http://maven.apache.org/download.cg)
* check out the Maven installation guide at [Maven Guides](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)

####Apache Solr
install Solr following instructions at [Solr Wiki](http://wiki.apache.org/solr/SolrInstall)
####MySQL database
* install MySQL from [MySQL Site](http://www.mysql.com/downloads/)
* run `SSS/ss/src/main/resources/conf/sss_schema.sql` to setup MySQL database

####SSS and REST adapter
download the Social Semantic Server containing its REST adapter as well from github
####Code import
import the SSS project as Maven project into e.g. Netbeans or Eclipse
####Logging
* modify `log4j.properties` in `SSS/ss/src/main/resources/conf/` for your needs
* customize `log4j.properties` in `SSS/ss-adapter/ss-adapter-rest/src/main/resources`

####Configuration
* modify `ss-conf.yaml` in `SSS/ss/src/main/resources/conf/` to your needs
* modify / provide `ss-adapter-rest-conf.yaml` in `CatalinaBase/conf/` from `SSS/ss-adapter/ss-adapter-rest/src/main/resources`

####Run
* run `mvn clean install` on project `SSS/ss-root` in order to get `SSS/ss/target/ss-app/ss.jar` and `SSS/ss-adapter/ss-adapter-rest/target/ss-adapter-rest-X.X-SNAPSHOT.war` and to install needed local jars provided in `SSS/ss-3rdpartylibs/libs/`
* copy directory `SSS/ss/target/ss-app/` to where you plan to run the SSS
* for configuration and logging please see chapter Logging and Configuration
* run `ss.jar` with `runinit.bat` or `.sh`
* deploy `ss-adapter-rest.war` after renaming `SSS/ss-adapter/ss-adapter-rest/target/ss-adapter-rest-X.X-SNAPSHOT.war` to the webapps folder of Catalina Tomcat

####SSS client-side libraries
* download the [SSS Client Side](https://github.com/learning-layers/SocialSemanticServerClientSide/) libraries from github
* link Javascript projects `JSUtilities`, `SSClientInterfaceGlobals` and `SSSClientInterfaceREST` in your application to have access to SSS server-side operations via its REST interface

####SSS plain REST API access
* To access the REST API via POST requests call `http://your-sss-host:your-port/ss-adapter-rest/rest/SSAdapterRest/yourOp/` where your `your-sss-host` represents the host running the REST API and `yourOp` represents one operation out of the ones provided by SSS (see client-side project). The API generally expects JSON strings as input (mime type `application/json`).
* Return values are JSON strings (except for, e.g. file downloading), e.g. `{"op":"collWithEntries", "error":false, "errorMsg":null, "opValue":{"author":"http://dt.ll/user/hugo/",...}}")`. 
* Additionally, all properties/variables returned by any API call will contain a JSON-LD description.
* Following key-value pairs are returned (except for non-JSON returns) normally:
 * _op_ the operation returning the result
 * _"opValue"_ opValue will always be replaced by the op value provided for the op parameter and contains the actual result for the API request - the value of the operation's return
 * _error_ whether an error occured server-side
 * _errorMsg_ if error, "errorMsg" gets set to respective exception messages thrown
 * _errorClassNames_ if error, class names of errors
 * _errorClassesWhereThrown_ if error, class names where the errors got thrown
 * _errorMethodsWhereThrown_ if error, method names where the errors got thrown
 * _errorLinesWhereThrown_ if error, line numbers where the errors got thrown
 * _errorThreadsWhereThrown_ if error, id's of threads where the errors got thrown

## References
* D. Kowald, S. Dennerlein, D. Theiler, S. Walk and C. Trattner.: [The Social Semantic Server - A Framework to Provide Services on Social Semantic Network Data](http://ceur-ws.org/Vol-1026/paper11.pdf), 2013. In S. Lohmann (ed.), I-SEMANTICS (Posters & Demos) (p./pp. 50-54), : CEUR-WS.org.

## Contact
* Dieter Theiler, Know-Center, Graz University of Technology, dtheiler@tugraz.at
