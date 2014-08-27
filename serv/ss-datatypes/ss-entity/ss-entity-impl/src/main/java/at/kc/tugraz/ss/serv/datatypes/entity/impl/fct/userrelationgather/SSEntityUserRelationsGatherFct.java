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
package at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.userrelationgather;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.sql.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.List;
import java.util.Map;

public class SSEntityUserRelationsGatherFct{
  
  public static void getUserRelations(
    final SSEntitySQLFct           sqlFct, 
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> userRelations) throws Exception{
    
    try{
      
      for(String user : allUsers){

        final SSUri userUri = SSUri.get(user);

        SSEntityUserRelationsGatherFct.addRelationsForCommentedEntities(
          sqlFct, 
          userRelations, 
          userUri);
        
        SSEntityUserRelationsGatherFct.addRelationsForUserCircles(
          sqlFct, 
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
    final SSEntitySQLFct            sqlFct, 
    final Map<String, List<SSUri>>  userRelations,
    final SSUri                     userUri) throws Exception{
    
    final String   userStr = SSStrU.toStr(userUri);
    SSEntityCircle entityCircle;
    
    for(SSUri entity : SSServCaller.entityEntitiesCommentedGet(userUri, userUri)){
      
      for(SSUri circleUri : sqlFct.getCircleURIsForEntity(entity)){
        
        entityCircle = sqlFct.getCircle(circleUri, true, false, false);
        
        if(userRelations.containsKey(userStr)){
          userRelations.get(userStr).addAll(entityCircle.users);
        }else{
          userRelations.put(userStr, entityCircle.users);
        }
      }
    }
  }
  
  private static void addRelationsForUserCircles(
    final SSEntitySQLFct            sqlFct,
    final Map<String, List<SSUri>>  userRelations,
    final SSUri                     userUri) throws Exception{
    
    final String userStr = SSStrU.toStr(userUri);
    
    for(SSEntityCircle circle : SSServCaller.entityUserCirclesGet(userUri, userUri, true)){
      
      if(userRelations.containsKey(userStr)){
        userRelations.get(userStr).addAll(circle.users);
      }else{
        userRelations.put(userStr, circle.users);
      }
    }
  }
}