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
package at.tugraz.sss.util;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCanAccessPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.serv.auth.api.SSAuthServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthCheckKeyPar;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.SSCircleRightE;
import at.tugraz.sss.serv.SSEntitiesSharedWithUsersI;
import at.tugraz.sss.serv.SSEntitiesSharedWithUsersPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSEntityCopiedI;
import at.tugraz.sss.serv.SSEntityCopiedPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSPushEntitiesToUsersI;
import at.tugraz.sss.serv.SSPushEntitiesToUsersPar;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSUri;
import java.util.ArrayList;
import java.util.List;

public class SSServCallerU{
  
  public static void handleEntityCopied(
    final SSUri          user,
    final SSUri          targetUser,
    final SSEntity       entity,
    final List<SSEntity> entities,
    final SSUri          targetEntity,
    final Boolean        withUserRestriction) throws Exception {
    
    try{
      
      final SSEntityCopiedPar entityCopiedPar =
        new SSEntityCopiedPar(
          user,
          targetUser, //targetUser
          entity, //entity
          entities, //entities
          targetEntity, //targetEntity
          withUserRestriction);
      
      for(SSServContainerI entityHandler : SSServReg.inst.getServsHandlingEntityCopied()){
        ((SSEntityCopiedI) entityHandler.serv()).entityCopied(entityCopiedPar);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void handleEntitiesSharedWithUsers(final SSEntitiesSharedWithUsersPar par) throws Exception{  
        
    try{
      
      for(SSServContainerI serv : SSServReg.inst.getServsHandlingEntitiesSharedWithUsers()){
        ((SSEntitiesSharedWithUsersI) serv.serv()).entitiesSharedWithUsers(par);
      }      
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void handleCirclesFromEntityEntitiesAdd(
    final SSUri          user,
    final SSUri          entity,
    final List<SSUri>    entityURIs,
    final Boolean        withUserRestriction) throws Exception{
    
    try{
      final SSCircleServerI circleServ              = (SSCircleServerI) SSServReg.getServ(SSCircleServerI.class);
      final SSEntityServerI entityServ              = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final List<SSEntity>  entities                =
        entityServ.entitiesGet(
          new SSEntitiesGetPar(
            user,
            entityURIs,
            null,
            null,
            withUserRestriction));
      
      for(SSEntity entityUserCircle :
        circleServ.circlesGet(
          new SSCirclesGetPar(
            user,
            entity,
            null, //entityTypesToIncludeOnly
            false, //withUserRestriction
            true,  //withSystemCircles
            false))){ //invokeEntityHandlers
        
        handleCircleEntitiesAdd(
          user,
          (SSEntityCircle) entityUserCircle,
          entities,
          withUserRestriction);
        
//      circleServ.circleEntitiesAdd(
//        new SSCircleEntitiesAddPar(
//          user,
//          entityUserCircle.id,
//          entities,  //entities
//          false,  //withUserRestriction
//          false)); //shouldCommit
//    }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void handleCircleEntitiesAdd(
    final SSUri          user, 
    final SSEntityCircle circle,
    final List<SSEntity> entities,
    final Boolean        withUserRestriction) throws Exception{
    
    try{
      
      final List<SSEntity>  entitiesToPushToUsers   = new ArrayList<>();
      final List<SSEntity>  addedAffiliatedEntities =
        handleAddAffiliatedEntitiesToCircle(
          user, 
          circle.id, 
          entities, 
          new ArrayList<>(),
          withUserRestriction);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entitiesToPushToUsers,
        entities);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entitiesToPushToUsers,
        addedAffiliatedEntities);
      
      handlePushEntitiesToUsers(
        new SSPushEntitiesToUsersPar(
          user,
          entitiesToPushToUsers,
          SSUri.getDistinctNotNullFromEntities(circle.users),
          withUserRestriction));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
   
  public static void handleCircleUsersAdd(
    final SSUri          user, 
    final SSEntityCircle circle,
    final List<SSUri>    users, 
    final Boolean        withUserRestriction) throws Exception {
    
    try{
      
      final List<SSEntity>  entitiesToPushToUsers   = new ArrayList<>();
      final List<SSEntity>  addedAffiliatedEntities =
        handleAddAffiliatedEntitiesToCircle(
            user,
            circle.id,
            circle.entities,
            new ArrayList<>(),
            withUserRestriction);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entitiesToPushToUsers,
        circle.entities);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entitiesToPushToUsers,
        addedAffiliatedEntities);
      
      handlePushEntitiesToUsers(
        new SSPushEntitiesToUsersPar(
          user,
          entitiesToPushToUsers,
          users,
          withUserRestriction));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static List<SSEntity> handleAddAffiliatedEntitiesToCircle(
    final SSUri             user,
    final SSUri             circle,
    final List<SSEntity>    entities,
    final List<SSUri>       recursiveEntities,
    final Boolean           withUserRestriction) throws Exception{
    
    try{
      final List<SSEntity>                     addedAffiliatedEntities = new ArrayList<>();
      
      if(
        entities == null || 
        entities.isEmpty()){
        return addedAffiliatedEntities;
      }
      
      final SSAddAffiliatedEntitiesToCirclePar par =
        new SSAddAffiliatedEntitiesToCirclePar(
          user,
          circle,
          entities,
          recursiveEntities,
          withUserRestriction);
      
      for(SSServContainerI serv : SSServReg.inst.getServsHandlingAddAffiliatedEntitiesToCircle()){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          addedAffiliatedEntities,
          ((SSAddAffiliatedEntitiesToCircleI) serv.serv()).addAffiliatedEntitiesToCircle(par));
      }
      
      return addedAffiliatedEntities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private static void handlePushEntitiesToUsers(final SSPushEntitiesToUsersPar par) throws Exception{
    
    try{
      
      for(SSServContainerI serv : SSServReg.inst.getServsHandlingPushEntitiesToUsers()){
        ((SSPushEntitiesToUsersI) serv.serv()).pushEntitiesToUsers(par);
      }      
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static Boolean areUsersUsers(final List<SSUri> users) throws Exception{
    
    try{
      
      if(users.isEmpty()){
        return true;
      }
      
      final List<SSEntity> entities =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            null,
            users, //entities
            null, //types
            null,    //descPar
            false)); //withUserRestriction
      
      for(SSEntity entity : entities){
        
        switch(entity.type){
          
          case user: continue;
          default:   throw new SSErr(SSErrE.userNotRegistered);
        }
      }
      
      return true;
    }catch(Exception error){
      SSServErrReg.reset();
      return false;
    }
  }
  
  public static void checkKey(final SSServPar parA) throws Exception{
    
    final SSUri user = 
      ((SSAuthServerI) SSServReg.getServ(SSAuthServerI.class)).authCheckKey(
        new SSAuthCheckKeyPar(
          parA.key));
    
    if(user != null){
      parA.user = user;
    }
    
    if(
      user      == null &&
      parA.user == null){
      SSServErrReg.regErrThrow(new SSErr(SSErrE.authNoUserForKey));
    }
  }
  
  public static Boolean canUserRead(
    final SSUri user, 
    final SSUri entityURI) throws Exception{
    
    try{
      ((SSCircleServerI)SSServReg.getServ(SSCircleServerI.class)).circleCanAccess(
        new SSCircleCanAccessPar(
          user, 
          entityURI, 
          SSCircleRightE.read));
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
        SSServErrReg.reset();
        return false;
      }

      throw error;
    }
  }
  
  public static Boolean canUserRead(
    final SSUri       user, 
    final List<SSUri> entityURIs) throws Exception{
    
    try{
      
      for(SSUri entityURI : entityURIs){
      
        ((SSCircleServerI)SSServReg.getServ(SSCircleServerI.class)).circleCanAccess(
          new SSCircleCanAccessPar(
            user, 
            entityURI, 
            SSCircleRightE.read));
      }
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
        SSServErrReg.reset();
        return false;
      }

      throw error;
    }
  }
  
  public static Boolean canUserAll(
    final SSUri   user,
    final SSUri   entityURI) throws Exception{
    
   try{
      ((SSCircleServerI)SSServReg.getServ(SSCircleServerI.class)).circleCanAccess(
        new SSCircleCanAccessPar(
          user, 
          entityURI, 
          SSCircleRightE.all));
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
        SSServErrReg.reset();
        return false;
      }

      throw error;
    }
  }
  
//  public static void canUserReadEntity(
//    final SSUri user,
//    final SSUri entityURI) throws Exception{
//    
//    ((SSCircleServerI)SSServReg.getServ(SSCircleServerI.class)).circleCanAccess(
//      new SSCircleCanAccessPar(
//        null, 
//        null, 
//        user, 
//        entityURI, 
//        SSCircleRightE.read));
//  }
//  
//  public static void canUserReadEntities(
//    final SSUri       user,
//    final List<SSUri> entityURIs) throws Exception{
//    
//    for(SSUri entityURI : entityURIs){
//      
//      ((SSCircleServerI)SSServReg.getServ(SSCircleServerI.class)).circleCanAccess(
//        new SSCircleCanAccessPar(
//          null,
//          null,
//          user,
//          entityURI,
//          SSCircleRightE.read));
//    }
//  }
  
//  public static void canUserAllEntity(
//    final SSUri   user,
//    final SSUri   entityURI) throws Exception{
//    
//    ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleCanAccess(
//      new SSCircleCanAccessPar(
//        null,
//        null,
//        user,
//        entityURI,
//        SSCircleRightE.all));
//  }
}
