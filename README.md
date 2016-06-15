SocialSemanticServer
====================
## Towards A Framework for Social Semantic Network Data
<!-- [![BuildStatus](http://layers.dbis.rwth-aachen.de/jenkins/buildStatus/icon?job=SocialSemanticServer)](http://layers.dbis.rwth-aachen.de/jenkins/job/SocialSemanticServer/) -->

## Description
The main goal of the Social Semantic Server (SSS) is to establish a framework to provide services for informal learning by handling Social Semantic Network data. 
By creating an infrastructure that allows for social negotiation of semantic meaning and enabling meaningful learning, it will enable situated and contextualized learning in turn.

![alt tag](https://raw.githubusercontent.com/learning-layers/SocialSemanticServer/bba6324551551b41f43e3b630e2376ecde83c807/desc.jpg)

Please cite one of the [papers](https://github.com/learning-layers/SocialSemanticServer#references) if you use this software in one of your publications.

## Install and Deployment
* download desired release from release section
* extract `sss.package.zip`
* install `Java 8` or higher from [Java Site](https://java.com/en/download/index.jsp) (optionally install Apache Solr
* install `MySQL 5.6` or higher from [MySQL Site](http://www.mysql.com/downloads/)
 * have at least the following set in your `my.cnf` 
  * [client] 
    * default-character-set=utf8
  * [mysql] 
    * default-character-set=utf8
  * [mysqld] 
    * init-connect='SET NAMES utf8'
     * character-set-server = utf8
 * import `sss.package/sss.schema.sql` to setup `sss` scheme
* copy `sss.package/sss.conf.yaml` to `/sssWorkDir/` and adapt
* copy `sss.package/sss.dropwizard.conf.yaml` to `/sssWorkDir/` and adapt
* copy `sss.package/sss.adapter.rest.v3.dropwizard.jar` to `/sssWorkDir/`
* start sss via `/sssWorkDir/java -jar sss.adapter.rest.v3.dropwizard.jar server sss.dropwizard.conf.yaml`
* access REST API via requests to `http://yourhost:yourport/rest/{API}/{OP or ID}` 
 * `API`: REST resource to be targeted
 * `OP or ID`: path to the actual service call to be executed, e.g., GET to `http://localhost:9000/rest/entities/{entity}` gets information for a certain entity

## Documentation
* for REST API description please have a look at Swagger's JSON-based REST API documentation in releases (i.e. `sss.package/api-docs`)

## References
* S. Dennerlein, D. Kowald, E. Lex, D. Theiler, E. Lacic, T. Ley, V. Tomberg, A. Ruiz-Calleja [The Social Semantic Server: A Flexible Framework to Support Informal Learning at the Workplace](https://www.researchgate.net/publication/280920425_The_Social_Semantic_Server_A_Flexible_Framework_to_Support_Informal_Learning_at_the_Workplace#full-text), 2015. 15th International Conference on Knowledge Technologies and Data-driven Business (i-KNOW 2015), Graz, Austria
* A. Ruiz-Calleja, S. Dennerlein, V. Tomberg, K. Pata, T. Ley, D. Theiler and E. Lex [Supporting learning analytics for informal workplace learning with a social semantic infrastructure](https://www.researchgate.net/publication/282733837_Supporting_learning_analytics_for_informal_workplace_learning_with_a_social_semantic_infrastructure), 2015. European Conference on Technology Enhanced Learning 2015
* A. Ruiz-Calleja, S. Dennerlein, V. Tomberg, T. Ley, D. Theiler, E. Lex [Integrating data across workplace learning applications with a social semantic infrastructure](https://www.researchgate.net/publication/282733581_Integrating_data_across_workplace_learning_applications_with_a_social_semantic_infrastructure), 2015. 14th International Conference on Web-based Learning
* S. Dennerlein, D. Theiler, P. Marton, P. Santos Rodriguez, J. Cook, S. Lindstaedt and E. Lex [KnowBrain: An Online Social Knowledge Repository for Informal Workplace Learning](https://www.researchgate.net/publication/281819090_KnowBrain_An_Online_Social_Knowledge_Repository_for_Informal_Workplace_Learning#full-text), 2015. European Conference on Technology Enhanced Learning 2015
* D. Kowald, S. Dennerlein, D. Theiler, S. Walk and C. Trattner.: [The Social Semantic Server - A Framework to Provide Services on Social Semantic Network Data](https://www.researchgate.net/publication/256297004_The_Social_Semantic_Server_A_Framework_to_Provide_Services_on_Social_Semantic_Network_Data#full-text), 2013. In S. Lohmann (ed.), I-SEMANTICS (Posters & Demos) (p./pp. 50-54), : CEUR-WS.org.
* Dennerlein, S., Rella, M, Tomberg, V. Theiler, D., Treasure-Jones, T., Kerr, M., Ley, T., Al-Smadi, M. & Trattner, C. (2014). [Making Sense of Bits and Pieces: A Sensemaking Tool for Informal Workplace Learning](https://www.researchgate.net/publication/264861254_Making_Sense_of_Bits_and_Pieces_A_Sensemaking_Tool_for_Informal_Workplace_Learning#full-text), 2014. European Conference on Technology Enhanced Learning 2014

## Contact
* Dieter Theiler, Know-Center, Graz University of Technology, dtheiler@tugraz.at
