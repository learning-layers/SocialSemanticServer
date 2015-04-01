/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
* For a list of contributors see the AUTHORS file at the top-level directory of this distribution.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
 package at.kc.tugraz.ss.serv.modeling.ue.datatypes.rets;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSJSONLDU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSServRetI;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUETopicScore;
import java.util.*;

public class SSModelUEResourceDetailsRet extends SSServRetI{
  
  public  SSUri                             entity                      = null;
  public  SSUri                             recentArtifact              = null;
  public  String                            recentTopic                 = null;
	public  List<SSUri>                       relatedPersons              = null;
  public  List<SSUri>                       editors                     = null;
  public  List<SSUri>                       contributedEntities         = null;
  public  List<SSModelUETopicScore>         topicScores                 = null;
  public  List<String>                      mIs                         = null;

  public SSModelUEResourceDetailsRet(
    SSUri                         uri                                        ,
	  List<SSUri>                   relatedPersons                             ,
	  List<String>                  maturingIndicators                         ,
	  List<SSUri>                   editors                                    ,
	  SSUri                         recentArtifact                             ,
	  String                        recentTopic                                ,
	  List<SSUri>                   contributedResources                       ,
	  List<SSModelUETopicScore>     topicScores, 
    SSServOpE                       op){
    
    super(op);

    this.entity                                     = uri;
	  this.relatedPersons                             = relatedPersons;
	  this.mIs                                        = maturingIndicators;
	  this.editors                                    = editors;
	  this.recentArtifact                             = recentArtifact;
	  this.recentTopic                                = recentTopic;
	  this.contributedEntities                        = contributedResources;
	  this.topicScores                                = topicScores;    
  }
  
  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld                      = new HashMap<>();
    Map<String, Object> relatedPersonsObj       = new HashMap<>();
    Map<String, Object> editorsObj              = new HashMap<>();
    Map<String, Object> contributedResourcesObj = new HashMap<>();
    Map<String, Object> topicScoresObj          = new HashMap<>();
    Map<String, Object> mIObj                   = new HashMap<>();
    
    ld.put(SSVarU.entity,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.author,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.recentArtifact, SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.recentTopic,    SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    
    relatedPersonsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    relatedPersonsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.relatedPersons, relatedPersonsObj);
    
    editorsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    editorsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.editors, editorsObj);
    
    contributedResourcesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    contributedResourcesObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.contributedEntities, contributedResourcesObj);
    
    topicScoresObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSModelUETopicScore.class.getName());
    topicScoresObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.topicScores, topicScoresObj);
    
    mIObj.put(SSJSONLDU.id,        SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    mIObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.mIs, mIObj);
      
    return ld;
  }
  
  /* getters to allow for json enconding  */
  public String getEntity() throws Exception{
    return SSStrU.removeTrailingSlash(entity);
  }

  public List<String> getRelatedPersons() throws Exception{
    return SSStrU.removeTrailingSlash(relatedPersons);
  }

  public List<String> getEditors() throws Exception{
    return SSStrU.removeTrailingSlash(editors);
  }

  public String getRecentArtifact() throws Exception{
    return SSStrU.removeTrailingSlash(recentArtifact);
  }

  public String getRecentTopic(){
    return recentTopic;
  }

  public List<String> getContributedEntities() throws Exception{
    return SSStrU.removeTrailingSlash(contributedEntities);
  }

  public List<SSModelUETopicScore> getTopicScores(){
    return topicScores;
  }
  
  public List<String> getmIs(){
    return mIs;
  }
}