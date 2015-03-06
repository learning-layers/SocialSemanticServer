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
package at.kc.tugraz.ss.service.tag.impl.fct.userrelationgatherer;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SSTagUserRelationGathererFct{
  
  public static void addUserForEntity(
    final SSTag                    tag,
    final Map<String, List<SSUri>> usersPerEntity) throws Exception{

    final String            entityStr = SSStrU.toStr(tag.entity);
    final List<SSUri>       usersForEntity;
    
    if(usersPerEntity.containsKey(entityStr)){
      
      usersForEntity = usersPerEntity.get(entityStr);
      
      if(!SSStrU.contains(usersForEntity, tag.user)){
        usersForEntity.add(tag.user);
      }
    }else{
      
      usersForEntity = new ArrayList<>();
      
      usersForEntity.add(tag.user);
      
      usersPerEntity.put(entityStr, usersForEntity);
    }
  }
  
  public static void addUserForTag(
    final SSTag                    tag,
    final Map<String, List<SSUri>> usersPerTag) throws Exception{
    
    final String            tagLabel = SSStrU.toStr(tag.label);
    final List<SSUri>       usersForTag;
    
    if(usersPerTag.containsKey(tagLabel)){
      
      usersForTag = usersPerTag.get(tagLabel);
      
      if(!SSStrU.contains(usersForTag, tag.user)){
        usersForTag.add(tag.user);
      }
    }else{
      
      usersForTag = new ArrayList<>();
      
      usersForTag.add(tag.user);
      
      usersPerTag.put(tagLabel, usersForTag);
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