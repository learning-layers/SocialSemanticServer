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
package at.kc.tugraz.sss.comment.impl.fct.userrelationgather;

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.List;
import java.util.Map;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;

public class SSCommentUserRelationGatherFct{
  
  public static void getUserRelations(
    final List<String>              allUsers,
    final Map<String, List<SSUri>>  userRelations) throws Exception{
    
    try{
      
      for(String user : allUsers){
        
        final SSUri userUri = SSUri.get(user);
        
        addRelationsForCommentedEntities(
          userRelations,
          userUri);
      }
      
      for(Map.Entry<String, List<SSUri>> usersPerUser : userRelations.entrySet()){
        SSStrU.distinctWithoutNull2(usersPerUser.getValue());
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private static void addRelationsForCommentedEntities(
    final Map<String, List<SSUri>>  userRelations,
    final SSUri                     userUri) throws Exception{
    
    final String userStr = SSStrU.toStr(userUri);
    
    for(SSUri entity : SSServCaller.commentEntitiesCommentedGet(userUri, userUri)){
      
      for(SSEntityCircle entityCircle : SSServCaller.circlesGet(userUri, userUri, entity, true, false)){
        
        if(userRelations.containsKey(userStr)){
          userRelations.get(userStr).addAll(SSUri.getFromEntitites(entityCircle.users));
        }else{
          userRelations.put(userStr, SSUri.getFromEntitites(entityCircle.users));
        }
      }
    }
  }
}
