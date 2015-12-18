 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.serv.datatypes.entity.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreatePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSEntitiesSharedWithUsersPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import java.util.List;

public class SSEntityShareWithUsers {
  
  private final SSCircleServerI  circleServ;
  private final SSUserCommons userCommons;
  
  public SSEntityShareWithUsers(final SSCircleServerI circleServ){
    this.circleServ  = circleServ;
    this.userCommons = new SSUserCommons();
  }
  
  public void handle(
    final SSUri       user,
    final SSCircleE   circleType,
    final SSEntity    entity,
    final List<SSUri> users,
    final Boolean     withUserRestriction){
    
    try{
      
      if(SSStrU.contains(users, user)){
        throw SSErr.get(SSErrE.userCannotShareWithHimself);
      }
      
      if(!userCommons.areUsersUsers(users)){
        return;
      }
      
      final SSUri circleUri =
        circleServ.circleCreate(
          new SSCircleCreatePar(
            user, //user
            circleType,
            null, //label
            null, //description
            true,  //isSystemCircle
            false, //withUserRestriction
            false)); //shouldCommit
      
      circleServ.circleUsersAdd(
        new SSCircleUsersAddPar(
          user,
          circleUri, //circle
          users, //users
          false, //withUserRestriction
          false)); //shouldCommit
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          user,
          circleUri, //circle
          SSUri.asListNotNull(entity.id),  //entities
          false, //withUserRestriction
          false)); //shouldCommit
      
      final SSEntityCircle circle =
        circleServ.circleGet(
          new SSCircleGetPar(
            user,
            circleUri,
            null, //entityTypesToIncludeOnly,
            null, //setTags,
            null, //tagSpace,
            true, //setEntities,
            true, //setUsers
            false, //withUserRestriction,  //as it is a system circle
            false)); //invokeEntityHandlers));
      
      SSServReg.inst.circleEntitiesAdded(
        user,
        circle,
        circle.entities,
        withUserRestriction);
      
      SSServReg.inst.entitiesSharedWithUsers(
        new SSEntitiesSharedWithUsersPar(
          user,
          circle,
          withUserRestriction));
      
    }catch(Exception error){
      SSServErrReg.reset();
    }
  }
}
