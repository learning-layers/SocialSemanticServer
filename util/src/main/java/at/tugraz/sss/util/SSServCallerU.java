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
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.tugraz.sss.serv.SSCircleRightE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.caller.SSServCaller;
import java.util.List;

public class SSServCallerU{
  
  public static void checkKey(final SSServPar parA) throws Exception{
  
    final SSUri user = SSServCaller.checkKey(parA);
    
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
          null, 
          null, 
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
            null, 
            null, 
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
  
  public static Boolean canUserEdit(
    final SSUri   user,
    final SSUri   entityURI) throws Exception{
    
    try{
      ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleCanAccess(
        new SSCircleCanAccessPar(
          null,
          null,
          user,
          entityURI,
          SSCircleRightE.edit));
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
        SSServErrReg.reset();
        return false;
      }
      
      throw error;
    }
  }
  
  public static void canUserReadEntity(
    final SSUri user,
    final SSUri entityURI) throws Exception{
    
    ((SSCircleServerI)SSServReg.getServ(SSCircleServerI.class)).circleCanAccess(
      new SSCircleCanAccessPar(
        null, 
        null, 
        user, 
        entityURI, 
        SSCircleRightE.read));
  }
  
  public static void canUserReadEntities(
    final SSUri       user,
    final List<SSUri> entityURIs) throws Exception{
    
    for(SSUri entityURI : entityURIs){
      
      ((SSCircleServerI)SSServReg.getServ(SSCircleServerI.class)).circleCanAccess(
        new SSCircleCanAccessPar(
          null,
          null,
          user,
          entityURI,
          SSCircleRightE.read));
    }
  }
  
  public static void canUserEditEntity(
    final SSUri   user, 
    final SSUri   entityURI) throws Exception{
    
    ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleCanAccess(
      new SSCircleCanAccessPar(
        null,
        null,
        user,
        entityURI,
        SSCircleRightE.edit));
  }
  
  public static void canUserEditEntities(
    final SSUri       user, 
    final List<SSUri> entityURIs) throws Exception{
    
    for(SSUri entityURI : entityURIs){
      
      ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleCanAccess(
        new SSCircleCanAccessPar(
          null,
          null,
          user,
          entityURI,
          SSCircleRightE.edit)); 
    }
  }
  
  public static void canUserAllEntity(
    final SSUri   user,
    final SSUri   entityURI) throws Exception{
    
    ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleCanAccess(
      new SSCircleCanAccessPar(
        null,
        null,
        user,
        entityURI,
        SSCircleRightE.all));
  }
  
  public static void checkWhetherUsersAreUsers(final List<SSUri> users) throws Exception{
    
    try{
      
      if(users.isEmpty()){
        return;
      }
      
      final List<SSEntity> entities =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            null,
            null,
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
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}