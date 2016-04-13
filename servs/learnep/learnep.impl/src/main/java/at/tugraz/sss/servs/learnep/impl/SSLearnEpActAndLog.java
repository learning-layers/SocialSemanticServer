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
package at.tugraz.sss.servs.learnep.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.*;
import at.kc.tugraz.ss.activity.datatypes.enums.*;
import at.tugraz.sss.serv.util.SSLogU;
import at.kc.tugraz.ss.activity.datatypes.par.*;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import java.util.List;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.reg.*;
import java.util.ArrayList;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSLearnEpActAndLog{
  
  public void addCircleToLearnEpVersion(
    final SSServPar servPar,
    final SSUri                        user,
//    final SSUri                        learnEpVersion,
    final SSUri                        circle,
    final SSUri                        learnEp,
    final boolean                      shouldCommit) throws SSErr{

    try{
      final SSActivityServerI activityServ = (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
       
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.addCircleToLearnEpVersion,
          learnEp,
          null,
          SSUri.asListNotNull(circle),
          null,
          null,
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break; }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      final SSEvalServerI evalServ = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
        
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.addCircleToLearnEpVersion,
          circle,
          null, //content
          SSUri.asListNotNull(learnEp), //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break; }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addLearnEpEntity(
    final SSServPar servPar,
    final SSUri                        user,
//    final SSUri                        learnEpVersion,
    final SSUri                        entity,
    final SSUri                        learnEpEntity,
    final SSUri                        learnEp,
    final boolean                      shouldCommit) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ = (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.addEntityToLearnEpVersion,
          learnEp,
          null,
          SSUri.asListNotNull(learnEpEntity, entity),
          null,
          null,
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break; }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      final SSEvalServerI evalServ = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.addEntityToLearnEpVersion,
          entity,
          null, //content
          SSUri.asListNotNull(learnEp), //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeLearnEpVersionCircle(
    final SSServPar servPar,
    final SSUri                           user,
//    final SSUri                           learnEpVersion,
    final SSUri                           learnEp,
    final SSUri                           learnEpCircle,
    final boolean                         shouldCommit) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ = (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.removeLearnEpVersionCircle,
          learnEp,
          null,
          SSUri.asListNotNull(learnEpCircle),
          null,
          null,
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      final SSEvalServerI evalServ = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.removeLearnEpVersionCircle,
          learnEpCircle,
          null, //content
          SSUri.asListNotNull(learnEp), //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void handleRemoveLearnEpVersionCircleWithEntities(
    final SSServPar                       servPar,
    final SSUri                           user,
//    final SSUri                           learnEpVersion,
    final SSUri                           learnEp,
    final SSEntity                        learnEpCircle,
    final List<SSUri>                     entities,
    final boolean                         shouldCommit) throws SSErr{
    
    try{
      
      if(entities.isEmpty()){
        
        removeLearnEpVersionCircle(
          servPar,
          user,
//          learnEpVersion,
          learnEp,
          learnEpCircle.id,
          shouldCommit);
        
      }else{
        
        removeLearnEpVersionCircleWithEntities(
          servPar,
          user,
//          learnEpVersion,
          learnEp,
          learnEpCircle,
          entities,
          shouldCommit);
      }
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeLearnEpVersionCircleWithEntities(
    final SSServPar                       servPar,
    final SSUri                           user,
//    final SSUri                           learnEpVersion,
    final SSUri                           learnEp,
    final SSEntity                        learnEpCircle,
    final List<SSUri>                     entities,
    final boolean                         shouldCommit) throws SSErr{
    
    final List<SSUri>       entitiesForActAndLog = new ArrayList<>();
    
//    SSUri.addDistinctWithoutNull(
//      entitiesForActAndLog,
//      learnEp);
    
    SSUri.addDistinctWithoutNull(
      entitiesForActAndLog,
      learnEpCircle.id);
    
    SSUri.addDistinctWithoutNull(
      entitiesForActAndLog,
      entities);
    
    try{
      
      final SSActivityServerI activityServ         = (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.removeLearnEpVersionCircleWithEntitites,
          learnEp, //entitiy
          null,
          entitiesForActAndLog, //entities
          null,
          null,
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      final SSEvalServerI evalServ = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.removeLearnEpVersionCircleWithEntitites,
          learnEpCircle.id,
          null, //content
          entitiesForActAndLog, //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeLearnEpEntity(
    final SSServPar servPar,
    final SSUri                           user,
//    final SSUri                           learnEpVersion,
    final SSUri                           learnEpEntity,
    final SSUri                           entity,
    final SSUri                           learnEp,
    final boolean                         shouldCommit) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ         = (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.removeLearnEpVersionEntity,
          learnEp,
          null,
          SSUri.asListNotNull(learnEpEntity, entity),
          null,
          null,
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      final SSEvalServerI evalServ = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.removeLearnEpVersionEntity,
          entity,
          null, //content
          SSUri.asListNotNull(learnEp), //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void shareLearnEp(
    final SSServPar   servPar,
    final SSUri       user,
    final SSUri       learnEp,
    final List<SSUri> usersToShareWith,
    final boolean     shouldCommit) throws SSErr {
    
    try{
      
      final SSActivityServerI activityServ         = (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.shareLearnEpWithUser,
          learnEp,
          usersToShareWith,
          null,
          null,
          null,
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void copyLearnEp(
    final SSServPar   servPar,
    final SSUri       user,
    final SSUri       learnEp,
    final SSUri       copiedLearnEp,
    final List<SSUri> usersToCopyFor,
    final List<SSUri> includedEntities,
    final boolean     shouldCommit) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ         = (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.copyLearnEpForUsers,
          learnEp,
          SSUri.asListNotNull(usersToCopyFor), //users
          null,
          null,
          null,
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      final SSEvalServerI evalServ = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.episodeTab,
          SSEvalLogE.copyLearnEpForUser,
          learnEp,
          SSStrU.toStr(copiedLearnEp), //content
          includedEntities, //entities
          SSUri.asListNotNull(usersToCopyFor),
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void changeLearnEpVersionCircleLabelAndDescription(
    final SSServPar                       servPar,
    final SSUri                           user,
    final SSEntity                        circleEntity,
    final SSLabel                         label,
    final SSTextComment                   description,
//    final SSUri                           learnEpVersion,
    final SSUri                           learnEpCircle,
    final SSUri                           learnEp,
    final boolean                         shouldCommit) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ = (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
        
      if(
        label != null &&
        !SSStrU.isEqual(circleEntity.label, label)){
        
        final SSUri activity =
          activityServ.activityAdd(
            new SSActivityAddPar(
              servPar,
              user,
              SSActivityE.changeLearnEpVersionCircleLabel,
              learnEp,
              null,
              SSUri.asListNotNull(learnEpCircle),
              null,
              null,
              shouldCommit));
        
        final List<SSActivityContent> contents = new ArrayList<>();
        
        contents.add(SSActivityContent.get(circleEntity.label));
        contents.add(SSActivityContent.get(label));
        
        activityServ.activityContentsAdd(
          new SSActivityContentsAddPar(
            servPar,
            user,
            activity, SSActivityContentE.text,
            contents,
            shouldCommit));
      }
      
      if(
        description != null &&
        !SSStrU.isEqual(circleEntity.description, description)){
        
        activityServ.activityAdd(
          new SSActivityAddPar(
            servPar,
            user,
            SSActivityE.changeLearnEpVersionCircleDescription,
            learnEp,
            null,
            SSUri.asListNotNull(learnEpCircle),
            null,
            null,
            shouldCommit));
      }
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void removeLearnEpEntityFromCircle(
    final SSServPar servPar,
    final SSUri   user,
    final SSUri   learnEp,
//    final SSUri   learnEpVersion,
    final SSUri   learnEpEntity,
    final SSUri   entity,
    final SSUri   circle,
    final boolean shouldCommit) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ         = (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.removeEntityFromLearnEpCircle, //type
          learnEp,
          null,
          SSUri.asListNotNull(learnEpEntity, entity, circle),
          null, //comments
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      final SSEvalServerI evalServ = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.removeEntityFromLearnEpCircle,
          entity,
          null, //content
          SSUri.asListNotNull(learnEp, circle), //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addLearnEpEntityToCircle(
    final SSServPar servPar,
    final SSUri     user,
    final SSUri     learnEp,
//    final SSUri     learnEpVersion,
    final SSUri     entity,
    final SSUri     learnEpCircle,
    final boolean   shouldCommit) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ         = (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.addEntityToLearnEpCircle, //type
          learnEp, //entity
          null, //users
          SSUri.asListNotNull(learnEpCircle, entity), //entities
          null, //comments
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      final SSEvalServerI evalServ = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.addEntityToLearnEpCircle,
          entity, //entity
          null, //content
          SSUri.asListNotNull(learnEp, learnEpCircle), //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void handleRemoveLearnEpEntity(
    final SSServPar   servPar,
    final SSUri       user, 
    final SSUri       learnEp,
//    final SSUri       learnEpVersion,
    final SSUri       learnEpEntity,
    final SSUri       entity,
    final List<SSUri> learnEpEntityCircleURIsBefore, 
    final List<SSUri> learnEpEntityCircleURIsAfter, 
    final boolean     calledFromRemove,
    final boolean     shouldCommit) throws SSErr{
    
    try{
      boolean entityRemovedFromCircle = false;
      
      for(SSUri circle : learnEpEntityCircleURIsBefore){
        
        if(SSStrU.contains(learnEpEntityCircleURIsAfter, circle)){
          continue;
        }
        
        removeLearnEpEntityFromCircle(
          servPar,
          user,
          learnEp,
//          learnEpVersion,
          learnEpEntity,
          entity,
          circle,
          shouldCommit);
        
        entityRemovedFromCircle = true;
      }
      
      if(
        !entityRemovedFromCircle &&
        calledFromRemove){
        
        removeLearnEpEntity(
          servPar,
          user,
//          learnEpVersion,
          learnEpEntity,
          entity,
          learnEp,
          shouldCommit);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void handleAddLearnEpEntity(
    final SSServPar   servPar,
    final SSUri       user,
    final SSUri       learnEp,
//    final SSUri       learnEpVersion,
    final SSUri       learnEpEntity,
    final SSUri       entity,
    final List<SSUri> learnEpEntityCircleURIsBefore,
    final List<SSUri> learnEpEntityCircleURIsAfter,
    final boolean     calledFromAdd,
    final boolean     shouldCommit) throws SSErr{
    
    try{
      
      boolean entityAddToCircle = false;
      
      for(SSUri learnEpCircleAfter : learnEpEntityCircleURIsAfter){
        
        if(SSStrU.contains(learnEpEntityCircleURIsBefore, learnEpCircleAfter)){
          continue;
        }
        
        addLearnEpEntityToCircle(
          servPar,
          user,
          learnEp, //learnEp
//          learnEpVersion, //learnEpVersion
          entity, //entity
          learnEpCircleAfter, //learnEpCircle
          shouldCommit);
        
        entityAddToCircle = true;
      }
      
      if(
        !entityAddToCircle &&
        calledFromAdd){
        
        addLearnEpEntity(
          servPar,
          user,
//          learnEpVersion,
          entity,
          learnEpEntity,
          learnEp,
          shouldCommit);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeLearnEp(
    final SSLearnEpRemovePar par,
    final boolean            shouldCommit) throws SSErr{
    
    try{
      
      final SSEvalServerI evalServ = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par,
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.removeLearnEp,
          par.learnEp, //entity
          null, //content
          null, //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void createLearnEp(
    final SSLearnEpCreatePar par,
    final SSUri              learnEp,
    final boolean            shouldCommit) throws SSErr{
    
    try{
      
      final SSEvalServerI evalServ = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par,
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.createLearnEp,
          learnEp, //entity
          null, //content
          null, //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}