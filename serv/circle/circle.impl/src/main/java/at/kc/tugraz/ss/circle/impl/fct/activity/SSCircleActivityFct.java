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
package at.kc.tugraz.ss.circle.impl.fct.activity;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreatePar;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityPublicSetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitySharePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.ArrayList;
import java.util.List;
import sss.serv.err.datatypes.SSErr;

public class SSCircleActivityFct{
  
  public static void createCircle(
    final SSCircleCreatePar par,
    final SSUri             circle) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.createCircle,
        circle,
        par.users,
        par.entities,
        SSTextComment.asListWithoutNullAndEmpty(),
        null,
        false);
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addEntitiesToCircle(
    final SSCircleEntitiesAddPar par) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.addEntitiesToCircle,
        par.circle,
        new ArrayList<>(),
        par.entities,
        SSTextComment.asListWithoutNullAndEmpty(),
        null,
        false);
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addUsersToCircle(
    final SSCircleUsersAddPar par) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.addUsersToCircle,
        par.circle,
        par.users,
        SSUri.asListWithoutNullAndEmpty(),
        SSTextComment.asListWithoutNullAndEmpty(),
        null,
        false);
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void setEntityPublic(
    final SSCircleEntityPublicSetPar par) throws Exception{
    
     try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.setEntityPublic,
        par.entity,
        new ArrayList<>(),
        SSUri.asListWithoutNullAndEmpty(),
        SSTextComment.asListWithoutNullAndEmpty(),
        null,
        false);
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void shareEntityWithUsers(
    final SSCircleEntitySharePar par,
    final SSUri                  circle) throws Exception{
    
    if(!par.saveActivity){
      return;
    }
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.shareEntityWithUsers,
        par.entity,
        par.users,
        SSUri.asListWithoutNullAndEmpty(),
        SSTextComment.asListWithoutNullAndEmpty(par.comment),
        null,
        false);
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void shareEntityWithCircles(
    final SSCircleEntitySharePar par) throws Exception{
    
    if(!par.saveActivity){
      return;
    }
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.shareEntityWithCircles,
        par.entity,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(par.circles),
        SSTextComment.asListWithoutNullAndEmpty(par.comment),
        null,
        false);
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
