/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.service.disc.datatypes;

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

public class SSDiscDesc extends SSEntityDescA{

  public List<SSTag>     tags          = new ArrayList<SSTag>();
  public SSRatingOverall overallRating = null;
  
  public SSDiscDesc(
    final SSUri           entityUri, 
    final SSLabelStr      entityLabel, 
    final Long            creationTime, 
    final List<SSTag>     tags,
    final SSRatingOverall overallRating,
    final SSUri           author){
    
    super(entityUri, entityLabel, creationTime, SSEntityEnum.disc, SSEntityEnum.discDesc, author);
    
    if(tags != null){
      this.tags.addAll(tags);
    }

    this.overallRating = overallRating;
  }
  
  public static SSDiscDesc get(
    final SSUri           entityUri, 
    final SSLabelStr      entityLabel, 
    final Long            entityCreationTime, 
    final List<SSTag>     tags,
    final SSRatingOverall overallRating,
    final SSUri           author){
    
    return new SSDiscDesc(entityUri, entityLabel, entityCreationTime, tags, overallRating, author);
  }
  
@Override
  public Object jsonLDDesc(){
    
    Map<String, Object> jsonLDDesc = (Map<String, Object>) super.jsonLDDesc();
    Map<String, Object> tagsObj    = new HashMap<String, Object>();
    
    tagsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSTag.class.getName());
    tagsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    jsonLDDesc.put(SSVarU.tags,      tagsObj);
    
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
}
