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
package at.kc.tugraz.ss.service.tag.datatypes;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSLabel;
import java.util.*;

public class SSTag extends SSEntity{

  public  SSTagLabel          tagLabel     = null;
  public  SSUri               entity       = null;
  public  SSUri               user         = null;
  public  SSSpaceE            space        = null;

  public static SSTag get(
    final SSUri       id,
    final SSUri       entity,
    final SSUri       user,
    final SSSpaceE    space,
    final SSTagLabel  tagLabel) throws Exception{
    
    return new SSTag(id, entity, user, space, tagLabel);
  }
  
  protected SSTag(
    final SSUri       id,
    final SSUri       entity,
    final SSUri       user,
    final SSSpaceE    space,
    final SSTagLabel  tagLabel) throws Exception{
    
    super(id, SSEntityE.tag, SSLabel.get(SSStrU.toStr(tagLabel)));
    
    this.val         = SSStrU.toStr(tagLabel);
    this.entity      = entity;
    this.user        = user;
    this.space       = space;
    this.tagLabel    = tagLabel;
  }
  
  @Override
  public Object jsonLDDesc() {
  
    final Map<String, Object> ld = (Map<String, Object>)super.jsonLDDesc();
    
    ld.put(SSVarNames.entity,     SSVarNames.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarNames.user,       SSVarNames.sss + SSStrU.colon + SSUri.class.getName());    
    ld.put(SSVarNames.space,      SSVarNames.sss + SSStrU.colon + SSSpaceE.class.getName());    
    ld.put(SSVarNames.label,      SSVarNames.sss + SSStrU.colon + SSTagLabel.class.getName());    
    
    return ld;
  } 
  
  public static Map<String, List<String>> getTagLabelsPerEntities(final List<SSTag> tags) throws Exception{
    
    final Map<String, List<String>>     tagsPerEntity = new HashMap<>();
    List<String>                        tagLabels;
    String                              entity;
    
    for(SSTag userTag : tags){
      
      entity = SSStrU.toStr(userTag.entity);
      
      if(tagsPerEntity.containsKey(entity)){
        
        tagLabels = tagsPerEntity.get(entity);
        
        if(SSStrU.contains(tagLabels, userTag.label)){
          continue;
        }
        
        tagLabels.add(userTag.label.toString());
      }else{
        
        tagLabels = new ArrayList<>();
        
        tagLabels.add(SSStrU.toStr(userTag.label));
        
        tagsPerEntity.put(entity, tagLabels);
      }
    }
    
    return tagsPerEntity;
  }
  
  /* json getters */
  
  public String getEntity() throws Exception{
    return SSStrU.removeTrailingSlash(entity);
  }

  public String getUser() throws Exception{
    return SSStrU.removeTrailingSlash(user);
  }

  public String getSpace(){
    return SSStrU.toStr(space);
  }

  @Override
  public String getLabel(){
    return SSStrU.toStr(tagLabel);
  }
  
  public String getTagLabel(){
    return SSStrU.toStr(tagLabel);
  }
}


//  public static List<SSUri> getDistinctResources(
//    final List<SSTag> tags) throws Exception{
//    
//    final List<SSEntityA> result = new ArrayList<>();
//		
//		for(SSTag tag : tags){
//      SSUri.addDistinct(result, tag.entity);
//		}
//
//    
//  }
//  