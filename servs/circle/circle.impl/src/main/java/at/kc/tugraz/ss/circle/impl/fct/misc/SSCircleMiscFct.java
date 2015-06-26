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
package at.kc.tugraz.ss.circle.impl.fct.misc;

import at.kc.tugraz.ss.circle.datatypes.par.SSCircleGetPar;
import at.kc.tugraz.ss.circle.impl.SSCircleImpl;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.circle.impl.fct.sql.SSCircleSQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSCircleRightE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.SSEntityCircle;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;

public class SSCircleMiscFct{
  
  private static SSUri pubCircleUri = null;
  
  public static void checkWhetherUserIsAllowedToEditCircle(
    final SSCircleSQLFct sqlFct,
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
  
  public static void addEntitiesToCircleByEntityHandlers(
    final SSCircleSQLFct                     sqlFct,
    final SSUri                              userUri,
    final List<SSUri>                        entityUris,
    final SSUri                              circleUri) throws Exception{
    
    try{
      
      for(SSUri entityUri : entityUris){
        
        final SSEntityE entityType = 
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null, 
              null, 
              null, 
              entityUri, 
              null, 
              null, 
              null, 
              false, 
              false, 
              false)).type;
          
        switch(entityType){
          case entity: continue;
        }
        
        for(SSServContainerI serv : SSServReg.inst.getServsManagingEntities()){
          ((SSEntityHandlerImplI) serv.serv()).addEntityToCircle(
            userUri, 
            circleUri, 
            sqlFct.getUserURIsForCircle(circleUri),
            entityUri, 
            entityType);
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addUsersToCircleByEntityHandlers(
    final SSCircleImpl                       circleServ,
    final SSUri                              user,
    final List<SSUri>                        users,
    final SSUri                              circleUri) throws Exception{
    
    try{
      final SSEntityCircle circle = 
        circleServ.circleGet(
        new SSCircleGetPar(
          null,
          null,
          user,
          circleUri, 
          user,
          SSEntityE.asListWithoutNullAndEmpty(),
          false,
          true, 
          true));
        
        for(SSServContainerI serv : SSServReg.inst.getServsManagingEntities()){
          ((SSEntityHandlerImplI) serv.serv()).addUsersToCircle(user, users, circle);
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
  
  public static Boolean doesUserHaveRightInAnyCircleOfEntity(
    final SSCircleSQLFct sqlFct,
    final SSUri          user,
    final SSUri          entity,
    final SSCircleRightE accessRight,
    final Boolean        logErr) throws Exception{
    
    try{

      for(SSCircleE circleType : sqlFct.getCircleTypesCommonForUserAndEntity(user, entity)){
        
        if(doesCircleOfTypeHaveRight(circleType, accessRight)){
          return true;
        }
      }
      
      return false;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error, logErr);
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
    final SSCircleSQLFct sqlFct) throws Exception{
    
    if(pubCircleUri != null){
      return pubCircleUri;
    }
    
    pubCircleUri = addOrGetPubCircleURI(sqlFct);
    
    return pubCircleUri;
  }
  
  public static void checkWhetherUserIsInCircle(
    final SSCircleSQLFct sqlFct,
    final SSUri          user,
    final SSUri          circle,
    final Boolean        logErr) throws Exception{
    
    try{
      if(!isUserInCircle(sqlFct, user, circle)){
        throw new SSErr(SSErrE.userIsNotInCircle);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error, logErr);
    }
  }
  
  public static Boolean isUserInCircle(
    final SSCircleSQLFct sqlFct,
    final SSUri          user,
    final SSUri          circle) throws Exception{
    
    try{
      return SSStrU.contains(sqlFct.getUsersForCircle(circle), user);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static void checkWhetherCircleOfTypeHasRight(
    final SSCircleSQLFct sqlFct,
    final SSUri          circle,
    final SSCircleRightE accessRight,
    final Boolean        logErr) throws Exception{
    
    try{
      final SSCircleE circleType = sqlFct.getTypeForCircle(circle);
      
      if(!doesCircleOfTypeHaveRight(circleType, accessRight)){
        throw new SSErr(SSErrE.circleDoesntHaveQueriedRight);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error, logErr);
    }
  }
  
  public static SSUri addOrGetPrivCircleURI(
    final SSCircleSQLFct sqlFct,
    final SSUri          user) throws Exception{
    
    try{
      
      try{
        return sqlFct.getPrivCircleURI(user);
      }catch(SSErr error){
        
        switch(error.code){
          case entityDoesntExist: break;
          default: throw error;
        }
      }
      
      final SSUri circleUri  = SSServCaller.vocURICreate();
      
      SSServCaller.entityAdd(
        SSVocConf.systemUserUri,
        circleUri,
        SSEntityE.circle,
        null,
        null,
        null,
        false);
      
      sqlFct.addCircle(
        circleUri,
        SSCircleE.priv,
        true);
      
      sqlFct.addUserToCircleIfNotExists(
        circleUri,
        user);
      
      return circleUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static SSUri addOrGetPubCircleURI(
    final SSCircleSQLFct sqlFct) throws Exception{
    
    try{
      
      try{
        return sqlFct.getPubCircleURI();
      }catch(SSErr error){
        
        switch(error.code){
          case entityDoesntExist: break;
          default: throw error;
        }
      }
      
      final SSUri circleUri  = SSServCaller.vocURICreate();
      
      SSServCaller.entityAdd(
        SSVocConf.systemUserUri,
        circleUri,
        SSEntityE.circle,
        null,
        null,
        null,
        false);
      
      sqlFct.addCircle(
        circleUri,
        SSCircleE.pub,
        true);
      
      return circleUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static void checkWhetherUserCanForEntityType(
    final SSCircleSQLFct sqlFct,
    final SSUri          user, 
    final SSEntity       entity,
    final SSCircleRightE accessRight,
    final Boolean        logErr) throws Exception{
    
    try{
      switch(entity.type){
        case entity: return; //TODO dtheiler: break down general entity types so that checks on e.g. videos will be present
        case circle: {
          
          try{
            checkWhetherUserIsInCircle       (sqlFct, user,      entity.id,   logErr);
            checkWhetherCircleOfTypeHasRight (sqlFct, entity.id, accessRight, logErr);
          }catch(Exception error){
            throw new SSErr(SSErrE.userNotAllowedToAccessEntity);
          }
          
          break;
        }
        
        default:{
          if(!doesUserHaveRightInAnyCircleOfEntity(sqlFct, user, entity.id, accessRight, logErr)){
            throw new SSErr(SSErrE.userDoesntHaveRightInAnyCircleOfEntity);
          }
        }
      }
    }catch(SSErr error){
      SSServErrReg.regErrThrow(new SSErr(SSErrE.userNotAllowedToAccessEntity), logErr);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void setPublicByEntityHandlers(
    final SSUri        userUri,
    final SSUri        entityUri) throws Exception{
    
    try{
      
      final SSEntityE entityType =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
          new SSEntityGetPar(
            null,
            null,
            null,
            entityUri,
            null,
            null,
            null,
            false,
            false,
            false)).type;
      
      switch(entityType){
        case entity: return;
      }
      
      for(SSServContainerI serv : SSServReg.inst.getServsManagingEntities()){
        
        if(((SSEntityHandlerImplI) serv.serv()).setEntityPublic(
          userUri, 
          entityUri, 
          entityType, 
          pubCircleUri)){
          return;
        }
      }
          
      throw new Exception("entity couldnt be set to be public by entity handlers");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void shareEntityWithUsersByHandlers(
    final SSUri        userUri, 
    final List<SSUri>  userUris,
    final SSUri        entityUri,
    final SSUri        circleUri) throws Exception{
    
    try{
      
      final SSEntityE entityType =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
          new SSEntityGetPar(
            null,
            null,
            null,
            entityUri,
            null,
            null,
            null,
            false,
            false,
            false)).type;
      
      for(SSServContainerI serv : SSServReg.inst.getServsManagingEntities()){
        ((SSEntityHandlerImplI) serv.serv()).shareEntityWithUsers(
          userUri, 
          userUris,
          entityUri, 
          circleUri, 
          entityType, 
          true);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
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
}
