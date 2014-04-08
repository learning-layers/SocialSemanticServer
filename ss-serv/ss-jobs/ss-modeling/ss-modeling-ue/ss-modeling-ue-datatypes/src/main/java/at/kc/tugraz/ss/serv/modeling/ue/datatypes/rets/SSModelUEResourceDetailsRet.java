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

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTagLabel;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServRetI;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUETopicScore;
import java.util.*;

public class SSModelUEResourceDetailsRet extends SSServRetI{
  
  public  SSUri                             uri                                        = null;
  public  SSUri                             recentArtifact                             = null;
  public  SSTagLabel                       recentTopic                                = null;
	public  List<SSUri>                       relatedPersons                             = null;
  public  List<SSUri>                       editors                                    = null;
  public  List<SSUri>                       contributedResources                       = null;
  public  List<SSModelUETopicScore>         topicScores                                = null;
  public  List<String>                      maturingIndicators                         = null;
  
  public SSModelUEResourceDetailsRet(
    SSUri                         uri                                        ,
	  List<SSUri>                   relatedPersons                             ,
	  List<String>                  maturingIndicators                         ,
	  List<SSUri>                   editors                                    ,
	  SSUri                         recentArtifact                             ,
	  SSTagLabel                   recentTopic                                ,
	  List<SSUri>                   contributedResources                       ,
	  List<SSModelUETopicScore>     topicScores, 
    SSMethU                       op){
    
    super(op);

    this.uri                                        = uri;
	  this.relatedPersons                             = relatedPersons;
	  this.maturingIndicators                         = maturingIndicators;
	  this.editors                                    = editors;
	  this.recentArtifact                             = recentArtifact;
	  this.recentTopic                                = recentTopic;
	  this.contributedResources                       = contributedResources;
	  this.topicScores                                = topicScores;    
  }
  
  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld                      = new HashMap<String, Object>();
    Map<String, Object> relatedPersonsObj       = new HashMap<String, Object>();
    Map<String, Object> editorsObj              = new HashMap<String, Object>();
    Map<String, Object> contributedResourcesObj = new HashMap<String, Object>();
    Map<String, Object> topicScoresObj          = new HashMap<String, Object>();
    Map<String, Object> mIObj                   = new HashMap<String, Object>();
    
    ld.put(SSVarU.uri,            SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.author,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.recentArtifact, SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.recentTopic,    SSVarU.sss + SSStrU.colon + SSTagLabel.class.getName());
    
    relatedPersonsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    relatedPersonsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.relatedPersons, relatedPersonsObj);
    
    editorsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    editorsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.editors, editorsObj);
    
    contributedResourcesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    contributedResourcesObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.contributedResources, contributedResourcesObj);
    
    topicScoresObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSModelUETopicScore.class.getName());
    topicScoresObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.topicScores, topicScoresObj);
    
    mIObj.put(SSJSONLDU.id,        SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    mIObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.topicScores, mIObj);
      
    return ld;
  }
  
  /*************** getters to allow for json enconding ********************/
  public String getUri() throws Exception{
    return SSUri.toStrWithoutSlash(uri);
  }

  public List<String> getRelatedPersons(){
    return SSUri.toDistinctStringArray(relatedPersons);
  }

  public List<String> getMaturingIndicators(){
    return maturingIndicators;
  }

  public List<String> getEditors(){
    return SSUri.toDistinctStringArray(editors);
  }

  public String getRecentArtifact() throws Exception{
    return SSUri.toStrWithoutSlash(recentArtifact);
  }

  public String getRecentTopic(){
    return SSTagLabel.toStr(recentTopic);
  }

  public List<String> getContributedResources(){
    return SSUri.toDistinctStringArray(contributedResources);
  }

  public List<SSModelUETopicScore> getTopicScores(){
    return topicScores;
  }
}