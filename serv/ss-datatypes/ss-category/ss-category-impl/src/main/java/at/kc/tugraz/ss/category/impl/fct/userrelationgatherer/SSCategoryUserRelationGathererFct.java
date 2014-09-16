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
package at.kc.tugraz.ss.category.impl.fct.userrelationgatherer;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.category.datatypes.SSCategory;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SSCategoryUserRelationGathererFct{
  
  public static void addUserForEntity(
    final SSCategory               category,
    final Map<String, List<SSUri>> usersPerEntity) throws Exception{

    final String            entityStr = SSStrU.toStr(category.entity);
    final List<SSUri>       usersForEntity;
    
    if(usersPerEntity.containsKey(entityStr)){
      
      usersForEntity = usersPerEntity.get(entityStr);
      
      if(!SSStrU.contains(usersForEntity, category.user)){
        usersForEntity.add(category.user);
      }
    }else{
      
      usersForEntity = new ArrayList<>();
      
      usersForEntity.add(category.user);
      
      usersPerEntity.put(entityStr, usersForEntity);
    }
  }
  
  public static void addUserForCategory(
    final SSCategory               category,
    final Map<String, List<SSUri>> usersPerCategory) throws Exception{
    
    final String            categoryLabel = SSStrU.toStr(category.label);
    final List<SSUri>       usersForCategory;
    
    if(usersPerCategory.containsKey(categoryLabel)){
      
      usersForCategory = usersPerCategory.get(categoryLabel);
      
      if(!SSStrU.contains(usersForCategory, category.user)){
        usersForCategory.add(category.user);
      }
    }else{
      
      usersForCategory = new ArrayList<>();
      
      usersForCategory.add(category.user);
      
      usersPerCategory.put(categoryLabel, usersForCategory);
    }
  }

  public static void addUserRelations(
    final Map<String, List<SSUri>> userRelations, 
    final Map<String, List<SSUri>> usersPerKey) throws Exception{
    
    String userStr;
    
    for(Map.Entry<String, List<SSUri>> entry : usersPerKey.entrySet()){
      
      for(SSUri user : entry.getValue()){

        userStr = SSStrU.toStr(user);
        
        if(userRelations.containsKey(userStr)){
          userRelations.get(userStr).addAll(entry.getValue());
        }else{
          userRelations.put(userStr, entry.getValue());
        }
      }
    }
  }
}