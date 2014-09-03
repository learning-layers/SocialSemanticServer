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
 package at.kc.tugraz.ss.category.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import java.util.*;

public class SSCategory extends SSEntity{

  public  SSCategoryLabel     categoryLabel  = null;
  public  SSUri               entity         = null;
  public  SSUri               user           = null;
  public  SSSpaceE            space          = null;

  public static SSCategory get(
    final SSUri            id       ,
    final SSUri            entity   ,
    final SSUri            user     ,
    final SSSpaceE         space    ,
    final SSCategoryLabel  categoryLabel) throws Exception{
    
    return new SSCategory(id, entity, user, space, categoryLabel);
  }
  
  protected SSCategory(
    final SSUri             id,
    final SSUri             entity,
    final SSUri             user,
    final SSSpaceE          space,
    final SSCategoryLabel   categoryLabel) throws Exception{
    
    super(id, SSEntityE.category, SSLabel.get(SSStrU.toStr(categoryLabel)));
    
    this.entity        = entity;
    this.user          = user;
    this.space         = space;
    this.categoryLabel = categoryLabel;
  }
  
  @Override
  public Object jsonLDDesc() {
  
    final Map<String, Object> ld = (Map<String, Object>)super.jsonLDDesc();
    
    ld.put(SSVarU.entity,     SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.user,       SSVarU.sss + SSStrU.colon + SSUri.class.getName());    
    ld.put(SSVarU.space,      SSVarU.sss + SSStrU.colon + SSSpaceE.class.getName());
    ld.put(SSVarU.label,      SSVarU.sss + SSStrU.colon + SSCategoryLabel.class.getName());
    
    return ld;
  } 
  
  public static Map<String, List<String>> getTagLabelsPerEntities(final List<SSCategory> categorys) throws Exception{
    
    final Map<String, List<String>>     categorysPerEntity = new HashMap<>();
    List<String>                        categoryLabels;
    String                              entity;
    
    for(SSCategory userTag : categorys){
      
      entity = SSStrU.toStr(userTag.entity);
      
      if(categorysPerEntity.containsKey(entity)){
        
        categoryLabels = categorysPerEntity.get(entity);
        
        if(SSStrU.contains(categoryLabels, userTag.label)){
          continue;
        }
        
        categoryLabels.add(userTag.label.toString());
      }else{
        
        categoryLabels = new ArrayList<>();
        
        categoryLabels.add(SSStrU.toStr(userTag.label));
        
        categorysPerEntity.put(entity, categoryLabels);
      }
    }
    
    return categorysPerEntity;
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
    return SSStrU.toStr(categoryLabel);
  }
}


//  public static List<SSUri> getDistinctResources(final List<SSCategory> categories) throws Exception{
//    
//    final List<SSUri> result = new ArrayList<>();
//		
//		for(SSCategory category : categories){
//      SSUri.addDistinctWithoutNull(result, category.entity);
//		}
//		
//		return result;
//  }