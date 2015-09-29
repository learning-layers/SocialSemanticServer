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
package at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.activity;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.tugraz.sss.serv.SSLogU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpEntity;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCirclesWithEntriesGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.SSLearnEpImpl;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import java.util.List;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSToolContextE;
import java.util.ArrayList;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSLearnEpActivityAndLogFct{
  
  private final SSActivityServerI activityServ;
  private final SSEvalServerI     evalServ;
  
  public SSLearnEpActivityAndLogFct(
    final SSActivityServerI activityServ,
    final SSEvalServerI     evalServ){
    
    this.activityServ = activityServ;
    this.evalServ     = evalServ;
  }
  
  public void addCircleToLearnEpVersion(
    final SSUri                        user,
    final SSUri                        learnEpVersion,
    final SSUri                        circle,
    final SSUri                        learnEp,
    final Boolean                      shouldCommit) throws Exception{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.addCircleToLearnEpVersion,
          learnEpVersion,
          null,
          SSUri.asListWithoutNullAndEmpty(circle, learnEp),
          null,
          null,
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.addCircleToLearnEpVersion,
          circle,
          null, //content
          SSUri.asListWithoutNullAndEmpty(learnEp), //entities
          null, //users
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addEntityToLearnEpVersion(
    final SSUri                        user,
    final SSUri                        learnEpVersion,
    final SSUri                        entity,
    final SSUri                        learnEpEntity,
    final SSUri                        learnEp,
    final Boolean                      shouldCommit) throws Exception{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.addEntityToLearnEpVersion,
          learnEpVersion,
          null,
          SSUri.asListWithoutNullAndEmpty(learnEpEntity, entity, learnEp),
          null,
          null,
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
     try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.addEntityToLearnEpVersion,
          entity,
          null, //content
          SSUri.asListWithoutNullAndEmpty(learnEp), //entities
          null, //users
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeLearnEpVersionCircle(
    final SSUri                           user,
    final SSUri                           learnEpVersion,
    final SSUri                           learnEp,
    final SSUri                           learnEpCircle,
    final Boolean                         shouldCommit) throws Exception{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.removeLearnEpVersionCircle,
          learnEpVersion,
          null,
          SSUri.asListWithoutNullAndEmpty(learnEpCircle, learnEp),
          null,
          null,
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.removeLearnEpVersionCircle,
          learnEpCircle,
          null, //content
          SSUri.asListWithoutNullAndEmpty(learnEp), //entities
          null, //users
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void handleRemoveLearnEpVersionCircleWithEntities(
    final SSUri                           user,
    final SSUri                           learnEpVersion,
    final SSUri                           learnEp,
    final SSEntity                        learnEpCircle,
    final Boolean                         shouldCommit) throws Exception{
    
    try{
      
      if(learnEpCircle.entries.isEmpty()){
        
        removeLearnEpVersionCircle(
          user, 
          learnEpVersion, 
          learnEp, 
          learnEpCircle.id, 
          shouldCommit);
        
      }else{
        
        removeLearnEpVersionCircleWithEntities(
          user, 
          learnEpVersion, 
          learnEp, 
          learnEpCircle, 
          shouldCommit);
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
  
  public void removeLearnEpVersionCircleWithEntities(
    final SSUri                           user,
    final SSUri                           learnEpVersion,
    final SSUri                           learnEp,
    final SSEntity                        learnEpCircle,
    final Boolean                         shouldCommit) throws Exception{
    
    final List<SSUri> uris = new ArrayList<>();
    
    uris.add(learnEpCircle.id);
    uris.add(learnEp);
    
    for(SSEntity learnEpEntity : learnEpCircle.entries){
      
      if(((SSLearnEpEntity) learnEpEntity).entity == null){
        continue;
      }
      
      SSUri.addDistinctWithoutNull(
        uris, 
        ((SSLearnEpEntity) learnEpEntity).entity.id);
    }
      
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.removeLearnEpVersionCircleWithEntitites,
          learnEpVersion, //entitiy
          null,
          uris, //entities
          null,
          null,
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.removeLearnEpVersionCircleWithEntitites,
          learnEpCircle.id,
          null, //content
          uris, //entities
          null, //users
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeLearnEpVersionEntity(
    final SSUri                           user,
    final SSUri                           learnEpVersion,
    final SSUri                           learnEpEntity,
    final SSUri                           entity,
    final SSUri                           learnEp,
    final Boolean                         shouldCommit) throws Exception{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.removeLearnEpVersionEntity,
          learnEpVersion,
          null,
          SSUri.asListWithoutNullAndEmpty(learnEpEntity, entity, learnEp),
          null,
          null,
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.removeLearnEpVersionEntity,
          entity,
          null, //content
          SSUri.asListWithoutNullAndEmpty(learnEp), //entities
          null, //users
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void shareLearnEp(
    final SSUri       user,
    final SSUri       learnEp,
    final List<SSUri> usersToShareWith,
    final Boolean     shouldCommit) throws Exception{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
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
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          user,
          SSToolContextE.episodeTab,
          SSEvalLogE.shareLearnEpWithUser,
          learnEp,
          null, //content
          null, //entities
          usersToShareWith, //users
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void copyLearnEp(
    final SSUri       user,
    final SSUri       learnEp,
    final List<SSUri> usersToCopyFor,
    final List<SSUri> includedEntities,
    final Boolean     shouldCommit) throws Exception{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.copyLearnEpForUsers,
          learnEp,
          SSUri.asListWithoutNullAndEmpty(usersToCopyFor), //users
          null,
          null,
          null,
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          user, 
          SSToolContextE.episodeTab, 
          SSEvalLogE.copyLearnEpForUser, 
          learnEp, 
          null, //content 
          includedEntities, //entities
          SSUri.asListWithoutNullAndEmpty(usersToCopyFor), 
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void changeLearnEpVersionCircleLabelAndDescription(
    final SSUri                           user,
    final SSEntity                        circleEntity,
    final SSLabel                         label,
    final SSTextComment                   description,
    final SSUri                           learnEpVersion,
    final SSUri                           learnEpCircle,
    final SSUri                           learnEp,
    final Boolean                         shouldCommit) throws Exception{
    
    try{
      
      if(
        label != null &&
        !SSStrU.equals(circleEntity.label, label)){
        
        activityServ.activityAdd(
          new SSActivityAddPar(
            user,
            SSActivityE.changeLearnEpVersionCircleLabel,
            learnEpVersion,
            null,
            SSUri.asListWithoutNullAndEmpty(learnEpCircle, learnEp),
            null,
            null,
            shouldCommit));
      }
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      if(
        description != null &&
        !SSStrU.equals(circleEntity.description, description)){
        
        activityServ.activityAdd(
          new SSActivityAddPar(
            user,
            SSActivityE.changeLearnEpVersionCircleDescription,
            learnEpVersion,
            null,
            SSUri.asListWithoutNullAndEmpty(learnEpCircle, learnEp),
            null,
            null,
            shouldCommit));
      }

    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      if(
        label != null &&
        !SSStrU.equals(circleEntity.label, label)){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            user,
            SSToolContextE.organizeArea,
            SSEvalLogE.changeLabel,
            learnEpCircle,
            SSStrU.toStr(label), //content
            SSUri.asListWithoutNullAndEmpty(learnEp), //entities
            null, //users
            shouldCommit));
      }
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      if(
        description != null &&
        !SSStrU.equals(circleEntity.description, description)){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            user,
            SSToolContextE.organizeArea,
            SSEvalLogE.changeDescription,
            learnEpCircle,
            SSStrU.toStr(description), //content
            SSUri.asListWithoutNullAndEmpty(learnEp), //entities
            null, //users
            shouldCommit));
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
  
  public void removeEntityFromLearnEpCircle(
    final SSUri   user,
    final SSUri   learnEp,
    final SSUri   learnEpVersion,
    final SSUri   learnEpEntity,
    final SSUri   entity,
    final SSUri   circle,
    final Boolean shouldCommit) throws Exception{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.removeEntityFromLearnEpCircle, //type
          learnEpVersion,
          null,
          SSUri.asListWithoutNullAndEmpty(learnEpEntity, entity, circle, learnEp),
          null, //comments
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.removeEntityFromLearnEpCircle,
          entity,
          null, //content
          SSUri.asListWithoutNullAndEmpty(learnEp, circle), //entities
          null, //users
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addEntityToLearnEpCircle(
    final SSUri   user,
    final SSUri   learnEp,
    final SSUri   learnEpVersion,
    final SSUri   learnEpEntity,
    final SSUri   entity,
    final SSUri   circle,
    final Boolean shouldCommit) throws Exception{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          user,
          SSActivityE.addEntityToLearnEpCircle, //type
          learnEpVersion, //entity
          null, //users
          SSUri.asListWithoutNullAndEmpty(circle, entity, learnEp), //entities
          null, //comments
          null, //creationTime
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.addEntityToLearnEpCircle,
          entity,
          null, //content
          SSUri.asListWithoutNullAndEmpty(learnEp, circle), //entities
          null, //users
          shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void handleEntityAddToOrRemoveFromLearnEpCircle(
    final SSLearnEpImpl  learnEpServ,
    final SSUri          user,
    final SSUri          learnEp,
    final SSUri          learnEpVersion,
    final SSUri          learnEpEntity,
    final SSUri          entity,
    final List<SSEntity> versionCirclesBefore,
    final Boolean        calledFromRemove, 
    final Boolean        calledFromAdd, 
    final Boolean        shouldCommit) throws Exception{
    
    try{
      
      final List<SSEntity> versionCirclesAfter     = new ArrayList<>();
      final List<SSUri>    entityCircleUrisBefore  = new ArrayList<>();
      final List<SSUri>    entityCircleUrisAfter   = new ArrayList<>();
      
      versionCirclesAfter.addAll(
        learnEpServ.learnEpVersionCirclesWithEntriesGet(
          new SSLearnEpVersionCirclesWithEntriesGetPar(
            user,
            learnEpVersion,
            false, //withUserRestriction
            false))); //invokeEntityHandlers
      
      for(SSEntity circle : versionCirclesBefore){
        
        if(SSStrU.contains(circle.entries, learnEpEntity)){
          SSUri.addDistinctWithoutNull(entityCircleUrisBefore, circle.id);
        }
      }
      
      for(SSEntity circle : versionCirclesAfter){
        
        if(SSStrU.contains(circle.entries, learnEpEntity)){
          SSUri.addDistinctWithoutNull(entityCircleUrisAfter, circle.id);
        }
      }
      
      Boolean entityRemovedFromCircle = false;
      
      for(SSUri circle : entityCircleUrisBefore){
        
        if(!SSStrU.contains(entityCircleUrisAfter, circle)){
          
          removeEntityFromLearnEpCircle(
            user,
            learnEp,
            learnEpVersion,
            learnEpEntity,
            entity,
            circle,
            shouldCommit);
          
          entityRemovedFromCircle = true;
        }
      }
      
      if(
        !entityRemovedFromCircle &&
        calledFromRemove){
            
        removeLearnEpVersionEntity(
          user,
          learnEpVersion,
          learnEpEntity,
          entity,
          learnEp,
          shouldCommit);
      }
      
      Boolean entityAddToCircle = false;
      
      for(SSUri circle : entityCircleUrisAfter){
        
        if(!SSStrU.contains(entityCircleUrisBefore, circle)){
          
          addEntityToLearnEpCircle(
            user,
            learnEp,
            learnEpVersion,
            learnEpEntity,
            entity,
            circle,
            shouldCommit);
          
          entityAddToCircle = true;
        }
      }
      
      if(
        !entityAddToCircle &&
        calledFromAdd){
        
        addEntityToLearnEpVersion(
          user,
          learnEpVersion,
          entity,
          learnEpEntity,
          learnEp,
          shouldCommit);
      }      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
