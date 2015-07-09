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

import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.circle.impl.fct.sql.SSCircleSQLFct;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSCircleRightE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSServErrReg;

public class SSCircleMiscFct{
  
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
    final SSCircleRightE accessRight) throws Exception{
    
    try{
      final List<SSCircleE> circleTypes = sqlFct.getCircleTypesCommonForUserAndEntity(user, entity);
      
      for(SSCircleE circleType : circleTypes){
        
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
  
  private static Boolean doesCircleOfTypeHaveRight(
    final SSCircleE      circleType,
    final SSCircleRightE accessRight) throws Exception{
    
    try{
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
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static Boolean isUserInCircle(
    final SSCircleSQLFct sqlFct,
    final SSUri          user,
    final SSUri          circle) throws Exception{
    
    try{
      final List<SSEntity> usersForCircle = sqlFct.getUsersForCircle(circle);
      
      return SSStrU.contains(usersForCircle, user);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static Boolean hasCircleOfTypeRight(
    final SSCircleSQLFct sqlFct,
    final SSUri          circle,
    final SSCircleRightE accessRight) throws Exception{
    
    try{
      return doesCircleOfTypeHaveRight(sqlFct.getTypeForCircle(circle), accessRight);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static Boolean canUserForEntityType(
    final SSCircleSQLFct sqlFct,
    final SSUri          user, 
    final SSEntity       entity,
    final SSCircleRightE accessRight) throws Exception{
    
    try{
      switch(entity.type){
        case entity: return true; //TODO dtheiler: break down general entity types so that checks on e.g. videos will be present
        case circle: {
          
          if(
            isUserInCircle       (sqlFct, user,      entity.id) &&
            hasCircleOfTypeRight (sqlFct, entity.id, accessRight)){
            return true;
          }
          
          return false;
        }
        
        default:{
          return doesUserHaveRightInAnyCircleOfEntity(sqlFct, user, entity.id, accessRight);
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public static void addCircle(
    final SSCircleSQLFct sqlFct, 
    final SSUri          circleUri,
    final SSCircleE      circleType,
    final Boolean        isSystemCircle,
    final SSUri          userToAdd) throws Exception{
    
    try{
      sqlFct.addCircle(
        circleUri,
        circleType,
        isSystemCircle);
      
      sqlFct.addUserToCircleIfNotExists(
        circleUri,
        userToAdd);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
