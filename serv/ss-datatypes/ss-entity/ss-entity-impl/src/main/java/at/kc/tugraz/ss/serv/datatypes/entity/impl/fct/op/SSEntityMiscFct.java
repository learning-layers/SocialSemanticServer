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
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleRightE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.sql.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityDescriberI;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.ArrayList;
import java.util.List;

public class SSEntityMiscFct{
  
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
  
  public static void checkWhetherEntitiesExist(
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

  public static void checkWhetherUserCanEditEntity(
    final SSUri userUri, 
    final SSUri entityUri) throws Exception{
    
    try{
      
      if(!SSServCaller.entityUserCanEdit(userUri, entityUri)){
        throw new Exception("user cannot edit entity");
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void checkWhetherUserCanReadEntity(
    final SSUri userUri, 
    final SSUri entityUri) throws Exception{
    
    try{
      
      if(!SSServCaller.entityUserCanRead(userUri, entityUri)){
        throw new Exception("user cannot read");
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void checkWhetherUserCanEditEntities(
    final SSUri       userUri, 
    final List<SSUri> entityUris) throws Exception{
    
    try{
      
      for(SSUri entityUri : entityUris){
        
        if(!SSServCaller.entityUserCanEdit(userUri, entityUri)){
          throw new Exception("user cannot edit entity");
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static SSUri createCircle(
    final SSEntitySQLFct          sqlFct,
    final SSUri                   circleAuthor,
    final SSCircleE               circleType,
    final SSLabel                 circleLabel,
    final SSTextComment           description) throws Exception{
    
    try{
      
      final SSUri circleUri = sqlFct.createCircleURI();
      
      SSServCaller.entityAdd(
        circleAuthor,
        circleUri,
        circleLabel,
        SSEntityE.circle,
        description,
        false);
      
      switch(circleType){
        case priv:
        case group:
        case pub: sqlFct.addCircle(circleUri, circleType); break;
        default: throw new Exception("circle type " + circleType + "currently not supported");
      }
      
      return circleUri;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static void addEntities(
    final SSUri       userUri, 
    final List<SSUri> entityUris) throws Exception{
  
    try{
      
      for(SSUri entityUri : entityUris){
        
        SSServCaller.entityAdd(
          userUri,
          entityUri,
          SSLabel.get(entityUri),
          SSEntityE.entity,
          null,
          false);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  } 
  
  public static void shareByEntityHandlers(
    final SSUri        userUri, 
    final List<SSUri>  userUris,
    final SSUri        entityUri,
    final SSUri        circleUri) throws Exception{
    
    try{
      
      final SSEntityE entityType = SSServCaller.entityGet(entityUri).type;
      
      for(SSServA serv : SSServA.getServsManagingEntities()){
        
        if(((SSEntityHandlerImplI) serv.serv()).shareUserEntity(userUri, userUris, entityUri, circleUri, entityType)){
          return;
        }
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
          throw new Exception("entity couldnt not be added to circle by entity handlers");
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

  public static SSEntityDescA getDescForEntityByEntityHandlers(
    final SSEntityDescGetPar par,
    SSEntityDescA            entityDesc) throws Exception{
    
    try{
      
      for(SSServA serv : SSServA.getServsDescribingEntities()){
        
        entityDesc = ((SSEntityDescriberI) serv.serv()).getDescForEntity(
          par,
          entityDesc);
      }
      
      return entityDesc;
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
}