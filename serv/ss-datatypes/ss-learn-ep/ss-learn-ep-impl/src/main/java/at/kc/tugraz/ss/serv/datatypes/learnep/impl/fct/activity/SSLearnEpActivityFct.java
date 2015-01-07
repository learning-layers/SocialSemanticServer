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
package at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.activity;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionAddCirclePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionAddEntityPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentSetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionRemoveCirclePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionRemoveEntityPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionUpdateCirclePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionUpdateEntityPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.ArrayList;
import java.util.List;
import sss.serv.err.datatypes.SSErr;

public class SSLearnEpActivityFct{

  public static void createLearnEpVersion(
    final SSLearnEpVersionCreatePar par,
    final SSUri                     learnEpVersion) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.createLearnEpVersion,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(learnEpVersion),
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

  public static void addCircleToLearnEpVersion(
    final SSLearnEpVersionAddCirclePar par, 
    final SSUri                        circle) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.addCircleToLearnEpVersion,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(circle),
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

  public static void addEntityToLearnEpVersion(
    final SSLearnEpVersionAddEntityPar par, 
    final SSUri                        entity) throws Exception{
    
    try{
      
      final List<SSUri> eventEntities = new ArrayList<>();
      
      eventEntities.add (par.entity);
      eventEntities.add( entity);
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.addEntityToLearnEpVersion,
        SSUri.asListWithoutNullAndEmpty(),
        eventEntities,
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

  public static void createLearnEp(
    final SSLearnEpCreatePar par, 
    final SSUri              learnEp) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.createLearnEp,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(learnEp),
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

  public static void updateLearnEpVersionCircle(
    final SSLearnEpVersionUpdateCirclePar par) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.updateLearnEpVersionCircle,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(par.learnEpCircle),
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

  public static void updateLearnEpVersionEntity(
    final SSLearnEpVersionUpdateEntityPar par) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.updateLearnEpVersionEntity,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(par.learnEpEntity),
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

  public static void removeLearnEpVersionCircle(
    final SSLearnEpVersionRemoveCirclePar par) throws Exception{ 
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.removeLearnEpVersionCircle,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(par.learnEpCircle),
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

  public static void removeLearnEpVersionEntity(
    final SSLearnEpVersionRemoveEntityPar par) throws Exception{
   
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.removeLearnEpVersionEntity,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(par.learnEpEntity),
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

  public static void setCurrentLearnEpVersion(
    final SSLearnEpVersionCurrentSetPar par) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.setCurrentLearnEpVersion,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(par.learnEpVersion),
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

  public static void shareLearnEp(
    final SSUri user,
    final SSUri forUser,
    final SSUri learnEp) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        user,
        SSActivityE.shareLearnEpWithUser,
        SSUri.asListWithoutNullAndEmpty(forUser),
        SSUri.asListWithoutNullAndEmpty(learnEp),
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
}
