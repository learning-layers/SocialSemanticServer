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

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.tugraz.sss.serv.SSLogU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityAddPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleUpdatePar;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import java.util.List;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;

public class SSLearnEpActivityFct{
  
  public static void addCircleToLearnEpVersion(
    final SSLearnEpVersionCircleAddPar par,
    final SSUri                        circle,
    final SSUri                        learnEp) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          par.user,
          SSActivityE.addCircleToLearnEpVersion,
          par.learnEpVersion,
          SSUri.asListWithoutNullAndEmpty(),
          SSUri.asListWithoutNullAndEmpty(circle, learnEp),
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          false));
      
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
    final SSLearnEpVersionEntityAddPar par,
    final SSUri                        learnEpEntity,
    final SSUri                        learnEp) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          par.user,
          SSActivityE.addEntityToLearnEpVersion,
          par.learnEpVersion,
          SSUri.asListWithoutNullAndEmpty(),
          SSUri.asListWithoutNullAndEmpty(learnEpEntity, par.entity, learnEp),
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          false));
      
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
    final SSLearnEpVersionCircleRemovePar par,
    final SSUri                           learnEpVersion,
    final SSUri                           learnEp) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          par.user,
          SSActivityE.removeLearnEpVersionCircle,
          learnEpVersion,
          SSUri.asListWithoutNullAndEmpty(),
          SSUri.asListWithoutNullAndEmpty(par.learnEpCircle, learnEp),
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          false));
      
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
    final SSLearnEpVersionEntityRemovePar par,
    final SSUri                           learnEpVersion,
    final SSUri                           entity,
    final SSUri                           learnEp) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          par.user,
          SSActivityE.removeLearnEpVersionEntity,
          learnEpVersion,
          SSUri.asListWithoutNullAndEmpty(),
          SSUri.asListWithoutNullAndEmpty(par.learnEpEntity, entity, learnEp),
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          false));
      
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
    final SSUri       user,
    final SSUri       learnEp,
    final List<SSUri> usersToShareWith) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.shareLearnEpWithUser,
          learnEp,
          SSUri.asListWithoutNullAndEmpty(usersToShareWith),
          null,
          null,
          null,
          false));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void copyLearnEp(
    final SSUri       user,
    final SSUri       learnEp,
    final List<SSUri> usersToCopyFor) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.copyLearnEpForUsers,
          learnEp,
          SSUri.asListWithoutNullAndEmpty(usersToCopyFor),
          null,
          null,
          null,
          false));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void handleLearnEpVersionUpdateCircle(
    final SSLearnEpVersionCircleUpdatePar par,
    final SSUri                           learnEpVersion,
    final SSUri                           learnEp) throws Exception{
    
    try{
      
      if(par.label != null){
        
        ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
          new SSActivityAddPar(
            par.user,
            SSActivityE.changeLearnEpVersionCircleLabel,
            learnEpVersion,
            SSUri.asListWithoutNullAndEmpty(),
            SSUri.asListWithoutNullAndEmpty(par.learnEpCircle, learnEp),
            SSTextComment.asListWithoutNullAndEmpty(),
            null,
            false));
      }
      
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
