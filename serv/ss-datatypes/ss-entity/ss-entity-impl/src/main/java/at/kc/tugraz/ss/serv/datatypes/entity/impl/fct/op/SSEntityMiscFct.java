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
package at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.op;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleRightE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.sql.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityDescriberI;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSEntityUpdaterI;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.ArrayList;
import java.util.List;
import sss.serv.err.datatypes.SSErr;
import sss.serv.err.datatypes.SSErrE;

public class SSEntityMiscFct{
  
  private static SSUri pubCircleUri = null;
  
  public static void checkWhetherUserIsAllowedToEditCircle(
    final SSEntitySQLFct sqlFct, 
    final SSUri          userUri,
    final SSUri          circleUri) throws Exception{
    
    try{
      
      try{
        if(
          sqlFct.isSystemCircle  (circleUri) ||
          !sqlFct.isGroupCircle  (circleUri) ||
          !sqlFct.isUserInCircle (userUri, circleUri)){
          
          throw new Exception("user is not allowed to edit circle");
        }
      }catch(Exception error){
        SSLogU.err(error);
        throw new Exception("user is not allowed to edit circle");
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void checkWhetherEntitiesHaveType(
    final SSEntitySQLFct sqlFct, 
    final List<SSUri>    entityUris,
    final SSEntityE      entityType) throws Exception{
    
    try{
      for(SSUri entityUri: entityUris){
        
        if(!SSStrU.equals(sqlFct.getEntity(entityUri).type, entityType)){
          throw new Exception("entity doesnt exist");
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void checkWhetherCircleIsGroupCircle(
    final SSCircleE circleType) throws Exception{
    
    try{
      
      if(!SSCircleE.isGroupCircle(circleType)){
        throw new Exception("circle is no group circle");
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static SSUri createCircle(
    final SSEntitySQLFct          sqlFct,
    final SSUri                   user,
    final SSCircleE               circleType,
    final SSLabel                 circleLabel,
    final SSTextComment           description,
    final Boolean                 isSystemCircle) throws Exception{
    
    try{
      
      final SSUri circleUri = sqlFct.createCircleURI();

      sqlFct.addEntityIfNotExists(
        circleUri, 
        SSEntityE.circle, 
        circleLabel, 
        description, 
        user,
        null);
      
      switch(circleType){
        case priv: 
        case group:
        case pub: sqlFct.addCircle(circleUri, circleType, isSystemCircle); break;
        default: throw new Exception("circle type " + circleType + "currently not supported");
      }
      
      return circleUri;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static void addEntities(
    final SSEntitySQLFct sqlFct, 
    final SSUri          userUri, 
    final List<SSUri>    entities) throws Exception{
  
    try{
      
      for(SSUri entity : entities){

        sqlFct.addEntityIfNotExists(
          entity, 
          SSEntityE.entity, 
          null, 
          null, 
          userUri, 
          null);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  } 
  
  public static void shareByEntityHandlers(
    final SSUri        userUri, 
    final List<SSUri>  userUris,
    final SSUri        entityUri,
    final SSUri        circleUri,
    final Boolean      saveActivity) throws Exception{
    
    try{
      
      final SSEntityE entityType = SSServCaller.entityGet(entityUri).type;
      
      for(SSServA serv : SSServA.getServsManagingEntities()){
        ((SSEntityHandlerImplI) serv.serv()).shareUserEntity(userUri, userUris, entityUri, circleUri, entityType, saveActivity);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void setPublicByEntityHandlers(
    final SSUri        userUri,
    final SSUri        entityUri,
    final SSUri        publicCircleUri) throws Exception{
    
    try{
      
      final SSEntityE entityType = SSServCaller.entityGet(entityUri).type;
      
      if(SSStrU.equals(entityType, SSEntityE.entity)){
        return;
      }
      
      for(SSServA serv : SSServA.getServsManagingEntities()){
        
        if(((SSEntityHandlerImplI) serv.serv()).setUserEntityPublic(userUri, entityUri, entityType, publicCircleUri)){
          return;
        }
      }
          
      throw new Exception("entity couldnt be set to be public by entity handlers");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addEntitiesToCircleByEntityHandlers(
    final SSUri                              userUri,
    final List<SSUri>                        entityUris,
    final SSUri                              circleUri) throws Exception{
    
    try{
      
      Boolean handledEntity;
      
      for(SSUri entityUri : entityUris){
        
        final SSEntityE entityType = SSServCaller.entityGet(entityUri).type;
        
        if(SSStrU.equals(entityType, SSEntityE.entity)){
          continue;
        }
        
        handledEntity = false;
        
        for(SSServA serv : SSServA.getServsManagingEntities()){
          
          if(((SSEntityHandlerImplI) serv.serv()).addEntityToCircle(userUri, circleUri, entityUri, entityType)){
            handledEntity = true;
            break;
          }
        }
        
        if(!handledEntity){
          throw new Exception("entity couldnt be added to circle by entity handlers");
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static List<SSCircleRightE> getCircleRights(
    final SSCircleE circleType) throws Exception{
    
    try{
      
      final List<SSCircleRightE> circleRights = new ArrayList<>();
      
      if(SSObjU.isNull(circleType)){
        throw new Exception("pars null");
      }
      
      switch(circleType){
        case priv: circleRights.add(SSCircleRightE.all);  break;
        case pub:  circleRights.add(SSCircleRightE.read); break;
        default:   circleRights.add(SSCircleRightE.read); circleRights.add(SSCircleRightE.edit);
      }
      
      return circleRights;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public static List<SSUri> getParentEntitiesByEntityHandlers(
    final SSUri      user, 
    final SSUri      entity) throws Exception{
    
    try{
      
      final SSEntityE    type        = SSServCaller.entityGet(entity).type;
      final List<SSUri>  entities    = new ArrayList<>();
      
      for(SSServA serv : SSServA.getServsManagingEntities()){
        entities.addAll(((SSEntityHandlerImplI) serv.serv()).getParentEntities(user, entity, type));
      }
      
      return entities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static List<SSUri> getSubEntitiesByEntityHandlers(
    final SSUri      user, 
    final SSUri      entity) throws Exception{
    
    try{
      
      final SSEntityE    type = SSServCaller.entityGet(entity).type;
      List<SSUri>        entityUris;
      
      if(SSStrU.equals(type, SSEntityE.entity)){
        return new ArrayList<>();
      }
      
      for(SSServA serv : SSServA.getServsManagingEntities()){
        
        entityUris = ((SSEntityHandlerImplI) serv.serv()).getSubEntities(user, entity, type);
        
        if(!SSObjU.isNull(entityUris)){
          return entityUris;
        }
      }
      
      SSLogU.warn("entity couldnt be searched within by entity handlers");
      
      return new ArrayList<>();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public static void updateEntityByEntityHandlers(
    final SSEntityUpdatePar par) throws Exception{
    
    try{
      
      for(SSServA serv : SSServA.getServsUpdatingEntities()){
        ((SSEntityUpdaterI) serv.serv()).updateEntity(par);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static SSEntity getDescForEntityByEntityHandlers(
    final SSEntityDescGetPar par,
    SSEntity                 desc) throws Exception{
    
    try{
      
      for(SSServA serv : SSServA.getServsDescribingEntities()){
        
        desc = ((SSEntityDescriberI) serv.serv()).getDescForEntity(
          par,
          desc);
      }
      
      return desc;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public static void checkWhetherUserWantsToShareWithHimself(
    final SSUri       userUri, 
    final List<SSUri> userUrisToShareWith) throws Exception{
    
    try{
      
      if(SSObjU.isNull(userUri, userUrisToShareWith)){
        throw new Exception("pars null");
      }
      
      if(SSStrU.contains(userUrisToShareWith, userUri)){
        throw new Exception("user cannot share with himself");
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void removeUserEntityDirectlyAdjoinedEntitiesByEntityHandlers(
    final SSEntity                                      entity,
    final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par) throws Exception{
    
    try{
      for(SSServA serv : SSServA.getServsManagingEntities()){
        
        ((SSEntityHandlerImplI) serv.serv()).removeDirectlyAdjoinedEntitiesForUser(
          par.user,
          entity.type,
          entity.id,
          par.removeUserTags,
          par.removeUserRatings,
          par.removeFromUserColls,
          par.removeUserLocations);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void copyEntityByEntityHandlers(
    final SSUri        user, 
    final SSUri        entity, 
    final List<SSUri>  entitiesToExclude,
    final List<SSUri>  users) throws Exception{
    
    try{
      
      final SSEntityE type = SSServCaller.entityGet(entity).type;
      
      if(SSStrU.equals(type, SSEntityE.entity)){
        SSLogU.warn("entity couldnt be copied by entity handlers");
        return;
      }
      
      for(SSServA serv : SSServA.getServsManagingEntities()){
        
        if(((SSEntityHandlerImplI) serv.serv()).copyUserEntity(user, users, entity, entitiesToExclude, type)){
         return; 
        }
      }
      
      SSLogU.warn("entity couldnt be copied by entity handlers");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void checkWhetherUsersAreUsers(final List<SSUri> users) throws Exception{
    
    try{
      
      for(SSUri user : users){
        
        if(!SSStrU.equals(SSServCaller.entityGet(user).type, SSEntityE.user)){
          throw new SSErr(SSErrE.providedUserIsNotRegistered);
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void checkWhetherUserHasRightInAnyCircleOfEntity(
    final SSEntitySQLFct sqlFct,
    final SSUri          user,
    final SSUri          entity,
    final SSCircleRightE accessRight) throws Exception{
    
    try{
      
      if(!doesUserHaveRightInAnyCircleOfEntity(sqlFct, user, entity, accessRight)){
        throw new SSErr(SSErrE.userDoesntHaveRightInAnyCircleOfEntity);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static Boolean doesUserHaveRightInAnyCircleOfEntity(
    final SSEntitySQLFct sqlFct, 
    final SSUri          user, 
    final SSUri          entity,
    final SSCircleRightE accessRight) throws Exception{
    
    try{
      
      for(SSCircleE circleType : sqlFct.getCircleTypesCommonForUserAndEntity(user, entity)){
        
        if(doesCircleOfTypeHaveRight(circleType, accessRight)){
          return true;
        }
      }
      
      return false;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static Boolean doesCircleOfTypeHaveRight(
    final SSCircleE      circleType, 
    final SSCircleRightE accessRight) throws Exception{
    
    switch(circleType){
      case priv: return true;
      case pub:{
        
        if(SSCircleRightE.equals(accessRight, SSCircleRightE.read)){
          return true;
        }
        
        break;
      }
      
      default:{
        
        if(
          SSCircleRightE.equals(accessRight, SSCircleRightE.read) ||
          SSCircleRightE.equals(accessRight, SSCircleRightE.edit)){
          return true;
        }
        
        break;
      }
    }
    
    return false;
  }

  public static SSUri getPubCircleURI(
    final SSEntitySQLFct sqlFct) throws Exception{
    
    if(pubCircleUri != null){
      return pubCircleUri;
    }
    
    pubCircleUri = sqlFct.addOrGetPubCircleURI();
    
    return pubCircleUri;
  }

  public static void checkWhetherUserIsInCircle(
    final SSEntitySQLFct sqlFct,
    final SSUri          user,
    final SSUri          circle) throws Exception{
    
    try{
      if(!isUserInCircle(sqlFct, user, circle)){
        throw new SSErr(SSErrE.userIsNotInCircle);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
    
  public static Boolean isUserInCircle(
    final SSEntitySQLFct sqlFct,
    final SSUri          user,
    final SSUri          circle) throws Exception{
    
    try{
      
      return SSStrU.contains(
        sqlFct.getUserURIsForCircle(
          circle),
        user);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static void checkWhetherCircleOfTypeHasRight(
    final SSEntitySQLFct sqlFct,
    final SSUri          circle,
    final SSCircleRightE accessRight) throws Exception{
    
    try{
      final SSCircleE circleType = sqlFct.getTypeForCircle(circle);
      
      if(!doesCircleOfTypeHaveRight(circleType, accessRight)){
        throw new SSErr(SSErrE.circleDoesntHaveQueriedRight);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}