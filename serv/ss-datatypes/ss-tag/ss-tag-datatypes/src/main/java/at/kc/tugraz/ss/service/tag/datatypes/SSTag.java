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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import java.util.*;

public class SSTag extends SSEntityA {

  public  SSUri               id           = null;
  public  SSUri               entity       = null;
  public  SSUri               user         = null;
  public  SSSpaceE            space        = null;
  public  SSTagLabel          label        = null;

  @Override
  public Object jsonLDDesc() {
  
    final Map<String, Object> ld = new HashMap<>();
    
    ld.put(SSVarU.id,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.entity,     SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.user,       SSVarU.sss + SSStrU.colon + SSUri.class.getName());    
    ld.put(SSVarU.space,      SSVarU.sss + SSStrU.colon + SSSpaceE.class.getName());    
    ld.put(SSVarU.label,      SSVarU.sss + SSStrU.colon + SSTagLabel.class.getName());    
    
    return ld;
  } 
    
  public static SSTag get(
    final SSUri       uri      ,
    final SSUri       resource ,
    final SSUri       user     ,
    final SSSpaceE    space    ,
    final SSTagLabel  label) throws Exception{
    
    return new SSTag(uri, resource, user, space, label);
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
  
  private SSTag(
    SSUri        uri,
    SSUri        entity,
    SSUri        user,
    SSSpaceE     space,
    SSTagLabel   label) throws Exception{
    
    super(label);
    
    this.id          = uri;
    this.entity      = entity;
    this.user        = user;
    this.space       = space;
    this.label       = label;
  }
  
  /* getters to allow for json enconding */
  
  public String getId() throws Exception{
    return SSStrU.removeTrailingSlash(id);
  }

  public String getEntity() throws Exception{
    return SSStrU.removeTrailingSlash(entity);
  }

  public String getUser() throws Exception{
    return SSStrU.removeTrailingSlash(user);
  }

  public String getSpace(){
    return SSSpaceE.toStr(space);
  }

  public String getLabel(){
    return SSStrU.toStr(label);
  }
}
