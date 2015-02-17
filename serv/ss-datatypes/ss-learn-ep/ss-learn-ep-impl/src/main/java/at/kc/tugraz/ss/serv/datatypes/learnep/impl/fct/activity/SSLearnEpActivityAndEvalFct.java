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
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSToolContextE;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionAddCirclePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionAddEntityPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionRemoveCirclePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionRemoveEntityPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionUpdateCirclePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionUpdateEntityPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.List;
import sss.serv.err.datatypes.SSErr;
import sss.serv.eval.datatypes.SSEvalLogE;

public class SSLearnEpActivityAndEvalFct{

  public static void addCircleToLearnEpVersion(
    final SSLearnEpVersionAddCirclePar par, 
    final SSUri                        circle,
    final SSUri                        learnEp) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.addCircleToLearnEpVersion,
        par.learnEpVersion,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(circle),
        SSTextComment.asListWithoutNullAndEmpty(),
        null,
        false);
      
      try{
        
        if(!SSCoreConf.instGet().getEvalConf().use){
          return;
        }
        
        SSServCaller.evalLog(
          par.user,
          SSToolContextE.organizeArea,
          par.user,
          SSEvalLogE.addCircleToLearnEpVersion,
          circle,
          null,
          SSUri.asListWithoutNullAndEmpty(learnEp),
          SSUri.asListWithoutNullAndEmpty(),
          false);
        
      }catch(Exception error){
        SSServErrReg.reset();
        
        SSLogU.warn("addCircleToLearnEpVersion eval logs failed");
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

  public static void addEntityToLearnEpVersion(
    final SSLearnEpVersionAddEntityPar par, 
    final SSUri                        learnEpEntity,
    final SSUri                        learnEp) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.addEntityToLearnEpVersion,
        par.learnEpVersion,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(learnEpEntity, par.entity),
        SSTextComment.asListWithoutNullAndEmpty(),
        null,
        false);
      
      try{
        
        if(!SSCoreConf.instGet().getEvalConf().use){
          return;
        }
        
        SSServCaller.evalLog(
          par.user,
          SSToolContextE.organizeArea,
          par.user,
          SSEvalLogE.addEntityToLearnEpVersion,
          par.entity,
          null,
          SSUri.asListWithoutNullAndEmpty(learnEp),
          SSUri.asListWithoutNullAndEmpty(),
          false);
        
      }catch(Exception error){
        SSServErrReg.reset();
        
        SSLogU.warn("addEntityToLearnEpVersion eval logs failed");
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

  public static void removeLearnEpVersionCircle(
    final SSLearnEpVersionRemoveCirclePar par,
    final SSUri                           learnEpVersion,
    final SSUri                           learnEp) throws Exception{ 
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.removeLearnEpVersionCircle,
        learnEpVersion,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(par.learnEpCircle),
        SSTextComment.asListWithoutNullAndEmpty(),
        null,
        false);
      
      try{
        
        if(!SSCoreConf.instGet().getEvalConf().use){
          return;
        }
        
        SSServCaller.evalLog(
          par.user,
          SSToolContextE.organizeArea,
          par.user,
          SSEvalLogE.removeLearnEpVersionCircle,
          par.learnEpCircle,
          null,
          SSUri.asListWithoutNullAndEmpty(learnEp),
          SSUri.asListWithoutNullAndEmpty(),
          false);
        
      }catch(Exception error){
        SSServErrReg.reset();
        
        SSLogU.warn("removeLearnEpVersionCircle eval logs failed");
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

  public static void removeLearnEpVersionEntity(
    final SSLearnEpVersionRemoveEntityPar par,
    final SSUri                           learnEpVersion,
    final SSUri                           entity,
    final SSUri                           learnEp) throws Exception{
   
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.removeLearnEpVersionEntity,
        learnEpVersion, 
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(par.learnEpEntity, entity),
        SSTextComment.asListWithoutNullAndEmpty(),
        null,
        false);
      
      try{
        
        if(!SSCoreConf.instGet().getEvalConf().use){
          return;
        }
        
        SSServCaller.evalLog(
          par.user,
          SSToolContextE.organizeArea,
          par.user,
          SSEvalLogE.removeLearnEpVersionEntity,
          entity,
          null,
          SSUri.asListWithoutNullAndEmpty(learnEp),
          SSUri.asListWithoutNullAndEmpty(),
          false);
        
      }catch(Exception error){
        SSServErrReg.reset();
        
        SSLogU.warn("removeLearnEpVersionEntity eval logs failed");
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
  
  public static void copyLearnEp(
    final SSUri       user, 
    final List<SSUri> usersToShareWith, 
    final SSUri       learnEp){
   
    try{
      
      if(!SSCoreConf.instGet().getEvalConf().use){
        return;
      }
      
      SSServCaller.evalLog(
        user,
        SSToolContextE.episodeTab,
        user,
        SSEvalLogE.copyLearnEpForUser,
        learnEp,
        null,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(usersToShareWith),
        false);
      
    }catch(Exception error){
      SSServErrReg.reset();
      
      SSLogU.warn("copyLearnEp eval logs failed");
    }
  }

  public static void shareLearnEp(
    final SSUri       user,
    final SSUri       learnEp,
    final List<SSUri> usersToShareWith) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        user,
        SSActivityE.shareLearnEpWithUser,
        learnEp,
        SSUri.asListWithoutNullAndEmpty(usersToShareWith),
        SSUri.asListWithoutNullAndEmpty(),
        SSTextComment.asListWithoutNullAndEmpty(),
        null,
        false);
      
      try{
        
        if(!SSCoreConf.instGet().getEvalConf().use){
          return;
        }
        
        SSServCaller.evalLog(
          user,
          SSToolContextE.episodeTab,
          user,
          SSEvalLogE.shareLearnEpWithUser,
          learnEp,
          null,
          SSUri.asListWithoutNullAndEmpty(),
          SSUri.asListWithoutNullAndEmpty(usersToShareWith),
          false);
        
      }catch(Exception error){
        SSServErrReg.reset();
        
        SSLogU.warn("shareLearnEp eval logs failed");
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

  public static void handleLearnEpVersionUpdateEntity(
    final SSLearnEpVersionUpdateEntityPar par,
    final SSUri                           learnEpVersion) throws Exception{
    
    try{
      
      if(par.entity != null){
        
        SSServCaller.activityAdd(
          par.user,
          SSActivityE.changeEntityForLearnEpVersionEntity,
          learnEpVersion,
          SSUri.asListWithoutNullAndEmpty(),
          SSUri.asListWithoutNullAndEmpty(par.learnEpEntity, par.entity),
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          false);
      }
      
      if(
        par.x != null || 
        par.y != null){
       
        SSServCaller.activityAdd(
          par.user,
          SSActivityE.moveLearnEpVersionEntity,
          learnEpVersion,
          SSUri.asListWithoutNullAndEmpty(),
          SSUri.asListWithoutNullAndEmpty(par.learnEpEntity, par.entity),
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          false);
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
  
  public static void handleLearnEpVersionUpdateCircle(
    final SSLearnEpVersionUpdateCirclePar par,
    final SSUri                           learnEpVersion) throws Exception{
    
    try{
      
      if(par.label != null){
       
        SSServCaller.activityAdd(
          par.user,
          SSActivityE.changeLearnEpVersionCircleLabel,
          learnEpVersion,
          SSUri.asListWithoutNullAndEmpty(),
          SSUri.asListWithoutNullAndEmpty(par.learnEpCircle),
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          false);
        
        try{
          
          if(SSCoreConf.instGet().getEvalConf().use){
          
            SSServCaller.evalLog(
              par.user,
              SSToolContextE.organizeArea,
              par.user,
              SSEvalLogE.changeLabel,
              par.learnEpCircle,
              SSStrU.toStr(par.label),
              SSUri.asListWithoutNullAndEmpty(),
              SSUri.asListWithoutNullAndEmpty(),
              false);
          }
          
        }catch(Exception error){
          SSServErrReg.reset();
          
          SSLogU.warn("shareLearnEp eval logs failed");
        }
      }
      
      if(
        par.xC != null || 
        par.yC != null || 
        par.xR != null || 
        par.yR != null){
       
        SSServCaller.activityAdd(
          par.user,
          SSActivityE.moveLearnEpVersionCircle,
          learnEpVersion,
          SSUri.asListWithoutNullAndEmpty(),
          SSUri.asListWithoutNullAndEmpty(par.learnEpCircle),
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          false);
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
