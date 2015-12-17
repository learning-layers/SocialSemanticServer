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

import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.kc.tugraz.ss.serv.auth.api.SSAuthServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthCheckKeyPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar; 
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSWarnE;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesGetPar;
import java.util.List;

public class SSServCallerU{

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
  
  public static Boolean areUsersUsers(
    final List<SSUri> users){
    
    try{
      
      if(users.isEmpty()){
        return true;
      }
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      final List<SSEntity> entities =
        entityServ.entitiesGet(
          new SSEntitiesGetPar(
            SSVocConf.systemUserUri,
            users, //entities
            null,    //descPar
            false)); //withUserRestriction
      
      for(SSEntity entity : entities){
        
        switch(entity.type){
          
          case user: continue;
          default:   throw new Exception();
        }
      }
      
      return true;
    }catch(Exception error){
      SSLogU.warn(SSWarnE.userNotRegistered, error);
      SSServErrReg.reset();
      return false;
    }
  }
  
  public static void checkKey(final SSServPar parA) throws SSErr{
    
    try{
      final SSAuthServerI authServ = (SSAuthServerI) SSServReg.getServ(SSAuthServerI.class);
      final SSUri         user     =
        authServ.authCheckKey(
          new SSAuthCheckKeyPar(
            parA.key));
      
      if(user != null){
        parA.user = user;
      }
      
      if(
        user      == null &&
        parA.user == null){
        throw new Exception();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(SSErrE.authNoUserForKey, error);
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
