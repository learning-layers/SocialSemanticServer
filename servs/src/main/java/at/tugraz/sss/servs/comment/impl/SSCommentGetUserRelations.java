/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.comment.impl;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.comment.datatype.SSCommentEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatype.SSCirclesGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.api.*;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.comment.api.*;

public class SSCommentGetUserRelations{
  
  private final SSCommentServerI commentServ;
  
  public SSCommentGetUserRelations(final SSCommentServerI commentServ){
    this.commentServ = commentServ; 
  }
  
  public void getUserRelations(
    final SSServPar servPar,
    final List<String>              allUsers,
    final Map<String, List<SSUri>>  userRelations) throws SSErr{
    
    try{
      
      for(String user : allUsers){
        
        final SSUri userUri = SSUri.get(user);
        
        addRelationsForCommentedEntities(
          servPar,
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
  
  private void addRelationsForCommentedEntities(
    final SSServPar servPar,
    final Map<String, List<SSUri>>  userRelations,
    final SSUri                     userUri) throws SSErr{
    
    try{
      final String userStr = SSStrU.toStr(userUri);
      
      for(SSUri entity :
        commentServ.commentEntitiesGet(
          new SSCommentEntitiesGetPar(
            servPar,
            userUri,
            false))){ //withUserRestriction
        
        for(SSEntity entityCircle :
          ((SSEntityServerI) commentServ).circlesGet(
            new SSCirclesGetPar(
              servPar,
              userUri,
              userUri,
              entity,
              null, //entityTypesToIncludeOnly
              false, //setEntities,
              true, //setUsers
              false, //withUserRestriction
              true, //withSystemCircles
              false))){ //invokeEntityHandlers
          
          if(userRelations.containsKey(userStr)){
            userRelations.get(userStr).addAll(SSUri.getDistinctNotNullFromEntities(entityCircle.users));
          }else{
            userRelations.put(userStr, SSUri.getDistinctNotNullFromEntities(entityCircle.users));
          }
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}