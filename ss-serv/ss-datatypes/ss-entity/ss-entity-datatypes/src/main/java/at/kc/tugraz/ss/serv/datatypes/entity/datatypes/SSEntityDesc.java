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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityDescA;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSEntityDesc extends SSEntityDescA{

  public List<SSTag>     tags          = new ArrayList<SSTag>();
  public SSRatingOverall overallRating = null;
  public List<SSUri>     discs         = new ArrayList<SSUri>();
  
  public static SSEntityDesc get(
    final SSUri           entityUri, 
    final SSLabelStr      entityLabel, 
    final Long            entityCreationTime, 
    final List<SSTag>     tags,
    final SSRatingOverall overallRating,
    final List<SSUri>     discs,
    final SSUri           author){
    
    return new SSEntityDesc(entityUri, entityLabel, entityCreationTime, tags, overallRating, discs, author);
  }
  
  protected SSEntityDesc(
    final SSUri           entityUri,
    final SSLabelStr      entityLabel,
    final Long            creationTime,
    final List<SSTag>     tags, 
    final SSRatingOverall overallRating,
    final List<SSUri>     discs,
    final SSUri           author){
    
    super(entityUri, entityLabel, creationTime, SSEntityEnum.entity, SSEntityEnum.entityDesc, author);
    
    if(tags != null){
      this.tags.addAll(tags);
    }
    
    if(discs != null){
      this.discs.addAll(discs);
    }
    
    this.overallRating = overallRating;
    this.author        = author;
  }
  
  @Override
  public Object jsonLDDesc(){
    
    Map<String, Object> jsonLDDesc = (Map<String, Object>) super.jsonLDDesc();
    Map<String, Object> tagsObj    = new HashMap<String, Object>();
    Map<String, Object> discsObj   = new HashMap<String, Object>();
    
    tagsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSTag.class.getName());
    tagsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    jsonLDDesc.put(SSVarU.tags,      tagsObj);
    
    discsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    discsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    jsonLDDesc.put(SSVarU.discs,      discsObj);
    
    jsonLDDesc.put(SSVarU.overallRating,  SSVarU.sss + SSStrU.colon + SSRatingOverall.class.getName());
    
    return jsonLDDesc;
  }
  
  /*************** getters to allow for json enconding ********************/
  public List<SSTag> getTags(){
    return tags;
  }

  public SSRatingOverall getOverallRating(){
    return overallRating;
  }

  public List<String> getDiscs(){
    return SSUri.toStrWithoutSlash(discs);
  }
}
