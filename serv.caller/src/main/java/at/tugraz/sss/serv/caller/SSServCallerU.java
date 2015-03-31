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
package at.tugraz.sss.serv.caller;

import at.tugraz.sss.serv.SSCircleRightE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import java.util.ArrayList;
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
    
  public static void checkWhetherUsersAreUsers(final List<SSUri> users) throws Exception{
    
    try{
      
      for(SSUri user : users){
        
        switch(SSServCaller.entityGet(user).type){
          
          case user: continue;
          default:   throw new SSErr(SSErrE.providedUserIsNotRegistered);
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addEntities(
    final SSUri       user, 
    final List<SSUri> entities) throws Exception{
    
    for(SSUri entity : entities){
      
      SSServCaller.entityAdd(
        user, 
        entity, 
        SSEntityE.entity, 
        null, 
        null, 
        null, 
        false);
    }
  }
  
  public static List<SSEntity> getEntities(
    final List<SSUri> entities) throws Exception{
    
    final List<SSEntity> result = new ArrayList<>();
    
    for(SSUri entity : entities){
      
      result.add(SSServCaller.entityGet(entity));
    }
    
    return result;
  }
  
  public static SSEntity canUserReadEntity(
    final SSUri user, 
    final SSUri entity) throws Exception{
    
    return SSServCaller.circleUserCan(user, entity, SSCircleRightE.read);
  }
  
  public static SSEntity canUserReadEntity(
    final SSUri   user,
    final SSUri   entity,
    final Boolean logErr) throws Exception{
    
    return SSServCaller.circleUserCan(user, entity, SSCircleRightE.read, logErr);
  }
  
  public static void canUserReadEntities(
    final SSUri       user,
    final List<SSUri> entities,
    final Boolean     logErr) throws Exception{
    
    for(SSUri entity : entities){
      SSServCaller.circleUserCan(user, entity, SSCircleRightE.read, logErr);
    }
  }
  
  public static void canUserReadEntities(
    final SSUri       user, 
    final List<SSUri> entities) throws Exception{
    
    for(SSUri entity : entities){
      SSServCaller.circleUserCan(user, entity, SSCircleRightE.read);
    }
  }

  public static SSEntity canUserEditEntity(
    final SSUri   user, 
    final SSUri   entity,
    final Boolean logErr) throws Exception{
    
    return SSServCaller.circleUserCan(user, entity, SSCircleRightE.edit, logErr);
  }
  
  public static SSEntity canUserEditEntity(
    final SSUri user, 
    final SSUri entity) throws Exception{
    
    return SSServCaller.circleUserCan(user, entity, SSCircleRightE.edit);
  }

  public static void canUserEditEntities(
    final SSUri       user, 
    final List<SSUri> entities,
    final Boolean     logErr) throws Exception{
    
    for(SSUri entity : entities){
      SSServCaller.circleUserCan(user, entity, SSCircleRightE.edit, logErr);
    }
  }
  
  public static void canUserEditEntities(
    final SSUri       user, 
    final List<SSUri> entities) throws Exception{
    
    for(SSUri entity : entities){
      SSServCaller.circleUserCan(user, entity, SSCircleRightE.edit);
    }
  }
  
  public static SSEntity canUserAllEntity(
    final SSUri   user, 
    final SSUri   entity,
    final Boolean logErr) throws Exception{
    
    return SSServCaller.circleUserCan(user, entity, SSCircleRightE.all, logErr);
  }
  
  public static SSEntity canUserAllEntity(
    final SSUri user, 
    final SSUri entity) throws Exception{
    
    return SSServCaller.circleUserCan(user, entity, SSCircleRightE.all);
  }

  public static void checkKey(){
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}