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
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.serv.auth.api.SSAuthServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthCheckKeyPar;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntitiesSharedWithUsersI;
import at.tugraz.sss.serv.SSEntitiesSharedWithUsersPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSEntityCopiedI;
import at.tugraz.sss.serv.SSEntityCopiedPar;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSPushEntitiesToUsersI;
import at.tugraz.sss.serv.SSPushEntitiesToUsersPar;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesGetPar;
import java.util.ArrayList;
import java.util.List;

public class SSServCallerU{

  public static SSEntity describeEntity(
    final SSUri                user,
    final SSEntity             entity,
    final SSEntityDescriberPar descPar,
    final Boolean              withUserRestriction) throws Exception{
    
    try{
      
      if(entity == null){
        return null;
      }
      
      if(descPar == null){
        return entity;
      }
        
      descPar.user                = user;
      descPar.withUserRestriction = withUserRestriction;
      
      SSEntity describedEntity = entity;
      
      for(SSServContainerI serv : SSServReg.inst.getServsHandlingDescribeEntity()){
        describedEntity = ((SSDescribeEntityI) serv.serv()).describeEntity(describedEntity, descPar);
      }

      return describedEntity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static List<SSEntity> filterExistingAndAccessibleEntities(
    final SSUri       user,
    final List<SSUri> entities,
    final Boolean     withUserRestriction) throws Exception{
    
    try{
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      return entityServ.entitiesGet(
        new SSEntitiesGetPar(
          user,
          entities,
          null, //descPar,
          withUserRestriction));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
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
  
  public static void handleCirclesFromEntityGetEntitiesAdd(
    final SSCircleServerI       circleServ,
    final SSEntityServerI       entityServ,
    final SSUri                 user,
    final SSUri                 entity,
    final List<SSUri>           entityURIs,
    final Boolean               withUserRestriction,
    final Boolean               invokeEntityHandlers) throws Exception{
    
    try{
      
      if(
        entityURIs == null || 
        entityURIs.isEmpty()){
        return;
      }

      final List<SSEntity> entities = new ArrayList<>();
        
      if(invokeEntityHandlers){
     
        entities.addAll(
          entityServ.entitiesGet(
            new SSEntitiesGetPar(
              user,
              entityURIs,
              null,
              withUserRestriction)));
      }
      
      for(SSEntity circle :
        circleServ.circlesGet(
          new SSCirclesGetPar(
            user, //user
            null, //forUser
            entity, //entity
            null, //entityTypesToIncludeOnly
            false, //setEntities, 
            invokeEntityHandlers, //setUsers,
            false, //withUserRestriction
            true,  //withSystemCircles
            false))){ //invokeEntityHandlers
        
        circleServ.circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            user,
            circle.id,
            entityURIs,
            false, //withUserRestriction
            false));
        
        if(invokeEntityHandlers){
          
          handleCircleEntitiesAdded(
            user,
            (SSEntityCircle) circle,
            entities,
            withUserRestriction);
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void handleCircleEntitiesAdded(
    final SSUri          user, 
    final SSEntityCircle circle,
    final List<SSEntity> entities,
    final Boolean        withUserRestriction) throws Exception{
    
    try{
      
      if(
        entities == null ||
        entities.isEmpty()){
        return;
      }
      
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
   
  public static void handleCircleUsersAdded(
    final SSUri          user, 
    final SSEntityCircle circle,
    final List<SSUri>    users, 
    final Boolean        withUserRestriction) throws Exception {
    
    try{
      
      if(
        users == null ||
        users.isEmpty()){
        return;
      }
      
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
  
  public static Boolean areUsersUsers(
      final List<SSUri> users) throws Exception{
    
    try{
      
      if(users.isEmpty()){
        return true;
      }
      
      final List<SSEntity> entities =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            SSVocConf.systemUserUri,
            users, //entities
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
  
//  public static Boolean canUserRead(
//    final SSUri user, 
//    final SSUri entityURI) throws Exception{
//    
//    try{
//      ((SSCircleServerI)SSServReg.getServ(SSCircleServerI.class)).circleCanAccess(
//        new SSCircleCanAccessPar(
//          user, 
//          entityURI, 
//          SSCircleRightE.read));
//      
//      return true;
//    }catch(Exception error){
//      
//      if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
//        SSServErrReg.reset();
//        return false;
//      }
//
//      throw error;
//    }
//  }
//  
//  public static Boolean canUserRead(
//    final SSUri       user, 
//    final List<SSUri> entityURIs) throws Exception{
//    
//    try{
//      
//      for(SSUri entityURI : entityURIs){
//      
//        ((SSCircleServerI)SSServReg.getServ(SSCircleServerI.class)).circleCanAccess(
//          new SSCircleCanAccessPar(
//            user, 
//            entityURI, 
//            SSCircleRightE.read));
//      }
//      
//      return true;
//    }catch(Exception error){
//      
//      if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
//        SSServErrReg.reset();
//        return false;
//      }
//
//      throw error;
//    }
//  }
  
//  public static Boolean canUserAll(
//    final SSUri   user,
//    final SSUri   entityURI) throws Exception{
//    
//   try{
//      ((SSCircleServerI)SSServReg.getServ(SSCircleServerI.class)).circleCanAccess(
//        new SSCircleCanAccessPar(
//          user, 
//          entityURI, 
//          SSCircleRightE.all));
//      
//      return true;
//    }catch(Exception error){
//      
//      if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
//        SSServErrReg.reset();
//        return false;
//      }
//
//      throw error;
//    }
//  }
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
