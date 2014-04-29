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
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSTagLabel;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircleTypeE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityRightTypeE;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.sql.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.user.api.SSUserGlobals;
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
          isSystemCircle  (sqlFct, circleUri) ||
          !isGroupCircle  (sqlFct, circleUri) ||
          !isUserInCircle (sqlFct, userUri, circleUri)){
          
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
  
  private static Boolean isUserInCircle(
    final SSEntitySQLFct sqlFct, 
    final SSUri          userUri, 
    final SSUri          circleUri) throws Exception{
  
    try{
      return SSUri.contains (sqlFct.getUserCircleURIs(userUri), circleUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private static Boolean isGroupCircle(
    final SSEntitySQLFct sqlFct, 
    final SSUri          circleUri) throws Exception{
    
    try{
      
      return SSEntityCircleTypeE.equals(sqlFct.getCircleType(circleUri), SSEntityCircleTypeE.group);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static Boolean isSystemCircle(
    final SSEntitySQLFct sqlFct, 
    final SSUri          circleUri) throws Exception{
    
    try{
      return SSUri.equals(sqlFct.getEntityAuthor(circleUri), SSUri.get(SSUserGlobals.systemUserURI));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  private static Boolean isGroupCircle(
    final SSEntityCircleTypeE circleType){
    
    return SSEntityCircleTypeE.equals(circleType, SSEntityCircleTypeE.group);
  }

  public static void checkWhetherUsersExist(
    final List<SSUri> userUris) throws Exception{
    
    try{
      if(userUris == null){
        throw new Exception("pars null");
      }
      
      for(SSUri userUri: userUris){
        
        if(!SSServCaller.userExists(userUri)){
          throw new Exception("user doesnt exist");
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void checkWhetherCircleIsGroupCircle(
    final SSEntityCircleTypeE circleType) throws Exception{
    
    try{
      
      if(!isGroupCircle(circleType)){
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
      
      if(SSObjU.isNull(userUri, entityUri)){
        throw new Exception("pars null");
      }
      
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
      
      if(SSObjU.isNull(userUri, entityUri)){
        throw new Exception("pars null");
      }
      
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
      
      if(SSObjU.isNull(userUri, entityUris)){
        throw new Exception("pars null");
      }
      
      for(SSUri entityUri : entityUris){
        
        if(!SSServCaller.entityUserCanEdit(userUri, entityUri)){
          throw new Exception("user cannot edit entity");
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addEntityToCircle(
    final SSEntitySQLFct              sqlFct, 
    final SSUri                       entityUri, 
    final SSUri                       circleUri) throws Exception{
  
    try{
      
      if(SSObjU.isNull(entityUri, circleUri)){
        throw new Exception("pars null");
      }
      
      sqlFct.addEntityToCircle(circleUri, entityUri);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addEntitiesToCircle(
    final SSEntitySQLFct              sqlFct, 
    final List<SSUri>                 entityUris, 
    final SSUri                       circleUri) throws Exception{
    
    try{
      
      if(SSObjU.isNull(entityUris, circleUri)){
        throw new Exception("pars null");
      }
      
      for(SSUri entityUri : entityUris){
        sqlFct.addEntityToCircle(circleUri, entityUri);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addUserToCircle(
    final SSEntitySQLFct              sqlFct,
    final SSUri                       userUri,
    final SSUri                       circleUri) throws Exception{
    
    try{
      sqlFct.addUserToCircle(circleUri, userUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addUsersToCircle(
    final SSEntitySQLFct              sqlFct,
    final List<SSUri>                 userUris,
    final SSUri                       circleUri) throws Exception{
    
    try{
      
      if(SSObjU.isNull(userUris, circleUri)){
        throw new Exception("pars null");
      }
      
      for(SSUri userUri : userUris){
        sqlFct.addUserToCircle(circleUri, userUri);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static SSUri createCircleWithRights(
    final SSEntitySQLFct          sqlFct,
    final SSUri                   circleAuthor,
    final SSEntityCircleTypeE     circleType,
    final SSLabelStr              circleLabel) throws Exception{
    
    try{
      
      if(SSObjU.isNull(circleType, circleAuthor, circleLabel)){
        throw new Exception("pars null");
      }
      
      final SSUri circleUri = sqlFct.createCircleURI();
      
      SSServCaller.entityAdd(
        circleAuthor,
        circleUri,
        circleLabel,
        SSEntityEnum.circle,
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
  
  public static void updateEntities(
    final SSUri       userUri, 
    final List<SSUri> entityUris) throws Exception{
  
    try{
      
      if(SSObjU.isNull(userUri, entityUris)){
        throw new Exception("pars null");
      }
      
      for(SSUri entityUri : entityUris){
        
        SSServCaller.entityAdd(
          userUri,
          entityUri,
          SSLabelStr.get(SSUri.toStr(entityUri)),
          SSEntityEnum.entity,
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
      
      if(SSObjU.isNull(userUri, userUris, entityUri, circleUri)){
        throw new Exception("pars null");
      }
            
      final SSEntityEnum entityType = SSServCaller.entityTypeGet(entityUri);
      
      if(SSEntityEnum.equals(entityType, SSEntityEnum.entity)){
        return;
      }
      
      for(SSServA serv : SSServA.getServsManagingEntities()){
        
        if(((SSEntityHandlerImplI) serv.serv()).shareUserEntity(userUri, userUris, entityUri, circleUri, entityType)){
          return;
        }
      }
      
      throw new Exception("entity couldnt be shared through entity handlers");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void setPublicByEntityHandlers(
    final SSUri        userUri,
    final SSUri        entityUri,
    final SSUri        publicCircleUri) throws Exception{
    
    try{
      
      if(SSObjU.isNull(userUri, entityUri)){
        throw new Exception("pars null");
      }
      
      final SSEntityEnum entityType = SSServCaller.entityTypeGet(entityUri);
      
      if(SSEntityEnum.equals(entityType, SSEntityEnum.entity)){
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
      
      if(SSObjU.isNull(userUri, entityUris, circleUri)){
        throw new Exception("pars null");
      }
      
      Boolean handledEntity;
      
      for(SSUri entityUri : entityUris){
        
        final SSEntityEnum entityType = SSServCaller.entityTypeGet(entityUri);
        
        if(SSEntityEnum.equals(entityType, SSEntityEnum.entity)){
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
  
  public static List<SSEntityRightTypeE> getCircleRights(
    final SSEntityCircleTypeE circleType) throws Exception{
    
    try{
      
      final List<SSEntityRightTypeE> circleRights = new ArrayList<SSEntityRightTypeE>();
      
      if(SSObjU.isNull(circleType)){
        throw new Exception("pars null");
      }
      
      switch(circleType){
        case priv: circleRights.add(SSEntityRightTypeE.all);  break;
        case pub:  circleRights.add(SSEntityRightTypeE.read); break;
        default:   circleRights.add(SSEntityRightTypeE.read); circleRights.add(SSEntityRightTypeE.edit);
      }
      
      return circleRights;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public static List<SSUri> searchWithTagWithinByEntityHandlers(
    final SSUri      userUri, 
    final SSUri      entityUri, 
    final SSTagLabel tag) throws Exception{
    
    try{
      
      if(SSObjU.isNull(userUri, entityUri, tag)){
        throw new Exception("pars null");
      }
      
      final SSEntityEnum entityType = SSServCaller.entityTypeGet(entityUri);
      List<SSUri>        entityUris;
      
      if(SSEntityEnum.equals(entityType, SSEntityEnum.entity)){
        return new ArrayList<SSUri>();
      }
      
      for(SSServA serv : SSServA.getServsManagingEntities()){
        
        entityUris = ((SSEntityHandlerImplI) serv.serv()).searchWithTagWithin(userUri, entityUri, tag, entityType);
        
        if(!SSObjU.isNull(entityUris)){
          return entityUris;
        }
      }
      
      throw new Exception("entity couldnt be searched within by entity handlers");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
