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
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSPushEntitiesToUsersPar;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;

public class SSEntityShareWithUsers {
  
  private final SSCircleServerI circleServ;
  
  public SSEntityShareWithUsers(final SSCircleServerI circleServ){
    this.circleServ = circleServ;
  }
  
  public void handle(
    final SSUri       user,
    final SSEntity    entity,
    final List<SSUri> users,
    final Boolean     withUserRestriction){
    
    try{
      
      if(SSStrU.contains(users, user)){
        throw new SSErr(SSErrE.userCannotShareWithHimself);
      }
      
      SSServCallerU.checkWhetherUsersAreUsers(users);
      
      final SSUri circleUri =
        circleServ.circleCreate(
          new SSCircleCreatePar(
            null,
            null,
            user, //user
            SSCircleE.group,
            null, //label
            null, //description
            true,  //isSystemCircle
            false, //withUserRestriction
            false)); //shouldCommit
      
      circleServ.circleUsersAdd(
        new SSCircleUsersAddPar(
          null,
          null,
          user,
          circleUri, //circle
          users, //users
          false, //withUserRestriction
          false)); //shouldCommit
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          null,
          null,
          user,
          circleUri, //circle
          SSUri.asListWithoutNullAndEmpty(entity.id),  //entities
          false, //withUserRestriction
          false)); //shouldCommit

      final SSEntityCircle circle = 
        circleServ.circleGet(
          new SSCircleGetPar(
            null, 
            null, 
            user, 
            circleUri, 
            null, //entityTypesToIncludeOnly, 
            null, //setTags, 
            null, //tagSpace, 
            withUserRestriction, 
            false)); //invokeEntityHandlers));
          
      SSServCallerU.handleCircleEntitiesAdd(
        user,
        circle,
        circle.entities,
        withUserRestriction);
      
    }catch(Exception error){
      SSServErrReg.reset();
    }
  }
}
