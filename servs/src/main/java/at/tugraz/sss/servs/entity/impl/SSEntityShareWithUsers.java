 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

package at.tugraz.sss.servs.entity.impl;

import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSCircle;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.user.api.SSUserServerI;
import at.tugraz.sss.servs.user.datatype.SSUserURIGetPar;
import at.tugraz.sss.servs.user.datatype.SSUsersPredefinedGetPar;
import at.tugraz.sss.servs.entity.api.*;
import at.tugraz.sss.servs.entity.datatype.SSCircleCreatePar;
import at.tugraz.sss.servs.entity.datatype.SSCircleEntitiesAddPar;
import at.tugraz.sss.servs.entity.datatype.SSCircleGetPar;
import at.tugraz.sss.servs.entity.datatype.SSCircleUsersAddPar;
import at.tugraz.sss.servs.entity.datatype.SSCircleE;
import at.tugraz.sss.servs.common.datatype.SSEntitiesSharedWithUsersPar;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.common.impl.*;
import at.tugraz.sss.servs.user.impl.*;
import java.util.ArrayList;
import java.util.List;

public class SSEntityShareWithUsers {
  
  private final SSEntityServerI           entityServ;
  private final SSUserServerI             userServ                = new SSUserImpl();
  private final SSUserCommons             userCommons             = new SSUserCommons();
  private final SSEntitiesSharedWithUsers entitiesSharedWithUsers = new SSEntitiesSharedWithUsers();
  private final SSCircleEntitiesAdded     circleEntitiesAdded     = new SSCircleEntitiesAdded();
  
  public SSEntityShareWithUsers(final SSEntityServerI entityServ){
    this.entityServ  = entityServ;
  }
  
  public void handle(
    final SSServPar   servPar,
    final SSUri       user,
    final SSCircleE   circleType,
    final SSEntity    entity,
    final List<SSUri> users,
    final boolean     withUserRestriction){
    
    try{
      
      if(SSStrU.contains(users, user)){
        throw SSErr.get(SSErrE.userCannotShareWithHimself);
      }
      
      final List<SSUri>   usersToUse = new ArrayList<>();
      final List<SSUri>   groupUsers = new ArrayList<>();
      final SSUri         groupUser =
        userServ.userURIGet(
          new SSUserURIGetPar(
            servPar,
            user,
            "internal.app@know-center.at"));
      
      if(SSStrU.contains(users, groupUser)){
        
        groupUsers.addAll(
          userServ.usersPredefinedGet(
            new SSUsersPredefinedGetPar(
              servPar,
              user,
              "internal.app@know-center.at")));
      }
      
      if(
        SSStrU.isEmpty(users) ||
        !userCommons.areUsersUsers(servPar, users)){
        return;
      }
      
      SSUri.addDistinctWithoutNull(usersToUse, users);
      SSUri.addDistinctWithoutNull(usersToUse, groupUsers);
      
      if(groupUser != null){
        SSStrU.remove(usersToUse, groupUser);
      }
      
      final SSUri circleUri =
        entityServ.circleCreate(
          new SSCircleCreatePar(
            servPar,
            user, //user
            circleType,
            null, //label
            null, //description
            true,  //isSystemCircle
            false, //withUserRestriction
            false)); //shouldCommit
      
      entityServ.circleUsersAdd(
        new SSCircleUsersAddPar(
          servPar,
          user,
          circleUri, //circle
          usersToUse, //users
          false, //withUserRestriction
          false)); //shouldCommit
      
      entityServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          servPar,
          user,
          circleUri, //circle
          SSUri.asListNotNull(entity.id),  //entities
          false, //withUserRestriction
          false)); //shouldCommit
      
      final SSCircle circle =
        entityServ.circleGet(
          new SSCircleGetPar(
            servPar,
            user,
            circleUri,
            null, //entityTypesToIncludeOnly,
            false, //setTags,
            null, //tagSpace,
            true, //setEntities,
            true, //setUsers
            false, //withUserRestriction,  //as it is a system circle
            false)); //invokeEntityHandlers));
      
      circleEntitiesAdded.circleEntitiesAdded(
        servPar,
        user,
        circle,
        circle.entities,
        withUserRestriction);
      
      entitiesSharedWithUsers.entitiesSharedWithUsers(
        servPar,
        new SSEntitiesSharedWithUsersPar(
          user,
          circle,
          withUserRestriction));
      
    }catch(Exception error){
      SSLogU.err(error);
    }
  }
}
