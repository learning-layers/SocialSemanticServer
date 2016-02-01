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
import at.kc.tugraz.ss.activity.datatypes.*;
import at.kc.tugraz.ss.activity.datatypes.enums.*;
import at.tugraz.sss.serv.util.SSLogU;
import at.kc.tugraz.ss.activity.datatypes.par.*;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpEntity;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCirclesWithEntriesGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.SSLearnEpImpl;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import java.util.List;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import at.tugraz.sss.serv.datatype.par.*;
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
    final SSServPar servPar,
    final SSUri                        user,
    final SSUri                        learnEpVersion,
    final SSUri                        circle,
    final SSUri                        learnEp,
    final boolean                      shouldCommit) throws SSErr{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.addCircleToLearnEpVersion,
          learnEpVersion,
          null,
          SSUri.asListNotNull(circle, learnEp),
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
  
  public void addEntityToLearnEpVersion(
    final SSServPar servPar,
    final SSUri                        user,
    final SSUri                        learnEpVersion,
    final SSUri                        entity,
    final SSUri                        learnEpEntity,
    final SSUri                        learnEp,
    final boolean                      shouldCommit) throws SSErr{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.addEntityToLearnEpVersion,
          learnEpVersion,
          null,
          SSUri.asListNotNull(learnEpEntity, entity, learnEp),
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
    final SSUri                           learnEpVersion,
    final SSUri                           learnEp,
    final SSUri                           learnEpCircle,
    final boolean                         shouldCommit) throws SSErr{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.removeLearnEpVersionCircle,
          learnEpVersion,
          null,
          SSUri.asListNotNull(learnEpCircle, learnEp),
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
    final SSServPar servPar,
    final SSUri                           user,
    final SSUri                           learnEpVersion,
    final SSUri                           learnEp,
    final SSEntity                        learnEpCircle,
    final boolean                         shouldCommit) throws SSErr{
    
    try{
      
      if(learnEpCircle.entries.isEmpty()){
        
        removeLearnEpVersionCircle(
          servPar,
          user,
          learnEpVersion,
          learnEp,
          learnEpCircle.id,
          shouldCommit);
        
      }else{
        
        removeLearnEpVersionCircleWithEntities(
          servPar,
          user,
          learnEpVersion,
          learnEp,
          learnEpCircle,
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
    final SSServPar servPar,
    final SSUri                           user,
    final SSUri                           learnEpVersion,
    final SSUri                           learnEp,
    final SSEntity                        learnEpCircle,
    final boolean                         shouldCommit) throws SSErr{
    
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
          servPar,
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
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.removeLearnEpVersionCircleWithEntitites,
          learnEpCircle.id,
          null, //content
          uris, //entities
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
  
  public void removeLearnEpVersionEntity(
    final SSServPar servPar,
    final SSUri                           user,
    final SSUri                           learnEpVersion,
    final SSUri                           learnEpEntity,
    final SSUri                           entity,
    final SSUri                           learnEp,
    final boolean                         shouldCommit) throws SSErr{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.removeLearnEpVersionEntity,
          learnEpVersion,
          null,
          SSUri.asListNotNull(learnEpEntity, entity, learnEp),
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
    final SSServPar servPar,
    final SSUri       user,
    final SSUri       learnEp,
    final List<SSUri> usersToShareWith,
    final boolean     shouldCommit) throws SSErr {
    
    try{
      
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
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.episodeTab,
          SSEvalLogE.shareLearnEpWithUser,
          learnEp,
          null, //content
          null, //entities
          usersToShareWith, //users
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
  
  public void copyLearnEp(
    final SSServPar servPar,
    final SSUri       user,
    final SSUri       learnEp,
    final List<SSUri> usersToCopyFor,
    final List<SSUri> includedEntities,
    final boolean     shouldCommit) throws SSErr{
    
    try{
      
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
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.episodeTab,
          SSEvalLogE.copyLearnEpForUser,
          learnEp,
          null, //content
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
    final SSUri                           learnEpVersion,
    final SSUri                           learnEpCircle,
    final SSUri                           learnEp,
    final boolean                         shouldCommit) throws SSErr{
    
    try{
      
      if(
        label != null &&
        !SSStrU.equals(circleEntity.label, label)){
        
        final SSUri activity =
          activityServ.activityAdd(
            new SSActivityAddPar(
              servPar,
              user,
              SSActivityE.changeLearnEpVersionCircleLabel,
              learnEpVersion,
              null,
              SSUri.asListNotNull(learnEpCircle, learnEp),
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
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default:{ SSServErrReg.regErrThrow(error); break;}
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
            servPar,
            user,
            SSActivityE.changeLearnEpVersionCircleDescription,
            learnEpVersion,
            null,
            SSUri.asListNotNull(learnEpCircle, learnEp),
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
  
  public void removeEntityFromLearnEpCircle(
    final SSServPar servPar,
    final SSUri   user,
    final SSUri   learnEp,
    final SSUri   learnEpVersion,
    final SSUri   learnEpEntity,
    final SSUri   entity,
    final SSUri   circle,
    final boolean shouldCommit) throws SSErr{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.removeEntityFromLearnEpCircle, //type
          learnEpVersion,
          null,
          SSUri.asListNotNull(learnEpEntity, entity, circle, learnEp),
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
  
  public void addEntityToLearnEpCircle(
    final SSServPar servPar,
    final SSUri   user,
    final SSUri   learnEp,
    final SSUri   learnEpVersion,
    final SSUri   learnEpEntity,
    final SSUri   entity,
    final SSUri   circle,
    final boolean shouldCommit) throws SSErr{
    
    try{
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.addEntityToLearnEpCircle, //type
          learnEpVersion, //entity
          null, //users
          SSUri.asListNotNull(circle, entity, learnEp), //entities
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
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.organizeArea,
          SSEvalLogE.addEntityToLearnEpCircle,
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
  
  public void handleEntityAddToOrRemoveFromLearnEpCircle(
    final SSServPar servPar,
    final SSLearnEpImpl  learnEpServ,
    final SSUri          user,
    final SSUri          learnEp,
    final SSUri          learnEpVersion,
    final SSUri          learnEpEntity,
    final SSUri          entity,
    final List<SSEntity> versionCirclesBefore,
    final boolean        calledFromRemove,
    final boolean        calledFromAdd,
    final boolean        shouldCommit) throws SSErr{
    
    try{
      
      final List<SSEntity> versionCirclesAfter     = new ArrayList<>();
      final List<SSUri>    entityCircleUrisBefore  = new ArrayList<>();
      final List<SSUri>    entityCircleUrisAfter   = new ArrayList<>();
      
      versionCirclesAfter.addAll(
        learnEpServ.learnEpVersionCirclesWithEntriesGet(
          new SSLearnEpVersionCirclesWithEntriesGetPar(
            servPar,
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
      
      boolean entityRemovedFromCircle = false;
      
      for(SSUri circle : entityCircleUrisBefore){
        
        if(!SSStrU.contains(entityCircleUrisAfter, circle)){
          
          removeEntityFromLearnEpCircle(
            servPar,
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
          servPar,
          user,
          learnEpVersion,
          learnEpEntity,
          entity,
          learnEp,
          shouldCommit);
      }
      
      boolean entityAddToCircle = false;
      
      for(SSUri circle : entityCircleUrisAfter){
        
        if(!SSStrU.contains(entityCircleUrisBefore, circle)){
          
          addEntityToLearnEpCircle(
            servPar,
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
          servPar,
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
