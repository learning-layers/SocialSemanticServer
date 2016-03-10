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
package at.tugraz.sss.servs.entity.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.servs.common.impl.serv.SSServCommons;
import java.util.List;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSEntityActAndLog {
  
  private final SSServCommons servCommons = new SSServCommons();
    
  public void shareEntityWithCircles(
    final SSEntitySharePar par,
    final Boolean          shouldCommit) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ = servCommons.getActivityServ();
      
      if(activityServ != null){
        
        activityServ.activityAdd(
          new SSActivityAddPar(
            par,
            par.user,
            SSActivityE.shareEntityWithCircles,
            par.entity,
            null,
            SSUri.asListNotNull(par.circles),
            SSTextComment.asListWithoutNullAndEmpty(par.comment),
            null,
            shouldCommit));
      }
      
      final SSEvalServerI     evalServ     = servCommons.getEvalServ();
      
      if(evalServ != null){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            par,
            par.user,
            SSToolContextE.sss,
            SSEvalLogE.shareEntityWithCircles,
            par.entity,
            null, //content
            par.circles, //entities
            null, //users
            null, //creationTime
            shouldCommit));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void shareEntityWithUsers(
    final SSEntitySharePar par,
    final Boolean          shouldCommit) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ = servCommons.getActivityServ();
      
      if(activityServ != null){
        
        activityServ.activityAdd(
          new SSActivityAddPar(
            par,
            par.user,
            SSActivityE.shareEntityWithUsers,
            par.entity,
            par.users,
            SSUri.asListNotNull(),
            SSTextComment.asListWithoutNullAndEmpty(par.comment),
            null,
            shouldCommit));
      }
      
      final SSEvalServerI     evalServ     = servCommons.getEvalServ();
      
      if(evalServ != null){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            par,
            par.user,
            SSToolContextE.sss,
            SSEvalLogE.shareEntityWithUsers,
            par.entity,
            null, //content
            null, //entities
            par.users, //users
            null, //creationTime
            shouldCommit));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void copyEntity(
    final SSServPar     servPar,
    final SSUri         user,
    final SSUri         entity,
    final SSUri         targetEntity,
    final List<SSUri>   forUsers,
    final SSTextComment comment,
    final boolean       shouldCommit) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ = servCommons.getActivityServ();
      
      if(activityServ != null){
        
        activityServ.activityAdd(
          new SSActivityAddPar(
            servPar,
            user,
            SSActivityE.copyEntityForUsers,
            entity,
            forUsers,
            null,
            SSTextComment.asListWithoutNullAndEmpty(comment),
            null,
            shouldCommit));
      }
      
      final SSEvalServerI     evalServ     = servCommons.getEvalServ();
      
      if(evalServ != null){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            servPar,
            user,
            SSToolContextE.sss,
            SSEvalLogE.copyEntity,
            entity,  //entity
            null, //content,
            SSUri.asListNotNull(targetEntity), //entities
            forUsers, //users
            null, //creationTime
            shouldCommit));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void attachEntities(
    final SSServPar servPar,
    final SSUri       user,
    final SSUri       entity,
    final List<SSUri> entities,
    final boolean     shouldCommit) throws SSErr{
    
    if(
      entity == null ||
      entities.isEmpty()){
      return;
    }
    
    try{
      
      final SSEvalServerI     evalServ     = servCommons.getEvalServ();
      
      if(evalServ != null){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            servPar,
            user,
            SSToolContextE.sss,
            SSEvalLogE.attachEntities,
            entity, //entity
            null, //content
            entities, //entities
            null, //users
            null, //creationTime
            shouldCommit));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeEntities(
    final SSServPar   servPar,
    final SSUri       user,
    final SSUri       entity,
    final List<SSUri> entities,
    final boolean     shouldCommit) throws SSErr{
    
    if(
      entity == null ||
      entities.isEmpty()){
      return;
    }
    
    try{
      
      final SSEvalServerI     evalServ     = servCommons.getEvalServ();
      
      if(evalServ != null){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            servPar,
            user,
            SSToolContextE.sss,
            SSEvalLogE.removeEntities,
            entity, //entity
            null, //content
            entities, //entities
            null, //users
            null, //creationTime
            shouldCommit));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void createPlaceHolder(
    final SSServPar par,
    final SSUri     user,
    final SSUri     placeholder,
    final boolean   shouldCommit) throws SSErr{
    
    try{
      
      final SSEvalServerI     evalServ     = servCommons.getEvalServ();
      
      if(evalServ != null){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            par,
            user,
            SSToolContextE.organizeArea,
            SSEvalLogE.createPlaceholder,
            placeholder, //entity
            null, //content
            null, //entities
            null, //users
            null, //creationTime
            shouldCommit));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void createCircle(
    final SSCircleCreatePar par,
    final boolean           isPrivPubOrSystemGroupCircle,
    final SSUri             circleURI,
    final boolean           shouldCommit) throws SSErr{
    
    if(isPrivPubOrSystemGroupCircle){
      return;
    }
    
    if(par.isSystemCircle){
      return;
    }
    
    switch(par.circleType){
      
      case pub:
      case priv:{
        
        return;
      }
    }
    
    try{
      
      final SSActivityServerI activityServ = servCommons.getActivityServ();
      
      if(activityServ != null){
        
        activityServ.activityAdd(
          new SSActivityAddPar(
            par,
            par.user,
            SSActivityE.createCircle,
            circleURI,
            null, //users,
            null, //entities,
            null,
            null,
            shouldCommit));
      }
      
      final SSEvalServerI     evalServ     = servCommons.getEvalServ();
      
      if(evalServ != null){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            par,
            par.user,
            SSToolContextE.sss,
            SSEvalLogE.createCircle,
            circleURI,  //entity
            null, //content,
            null,
            null,
            null, //creationTime
            shouldCommit));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addEntitiesToCircle(
    final SSCircleEntitiesAddPar par,
    final boolean                isPrivPubOrSystemGroupCircle,
    final boolean                shouldCommit) throws SSErr{
    
    if(isPrivPubOrSystemGroupCircle){
      return;
    }
    
    try{
      
      final SSActivityServerI activityServ = servCommons.getActivityServ();
      
      if(activityServ != null){
        
        activityServ.activityAdd(
          new SSActivityAddPar(
            par, //servPar
            par.user, //user
            SSActivityE.addEntitiesToCircle, //type
            par.circle, //entity
            null, //users
            par.entities, //entities
            null, //comments
            null, //creationTime
            shouldCommit));
      }
      
      final SSEvalServerI     evalServ     = servCommons.getEvalServ();
      
      if(evalServ != null){
        evalServ.evalLog(
          new SSEvalLogPar(
            par,
            par.user,
            SSToolContextE.sss,
            SSEvalLogE.addCircleEntities,
            par.circle, //entity
            null, //content,
            par.entities, //entities
            null, //users
            null, //creationTime
            shouldCommit));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addUsersToCircle(
    final SSCircleUsersAddPar par,
    final boolean             isPrivPubOrSystemGroupCircle,
    final boolean             shouldCommit) throws SSErr{
    
    if(isPrivPubOrSystemGroupCircle){
      return;
    }
    
    try{
      
      final SSActivityServerI activityServ = servCommons.getActivityServ();
      
      if(activityServ != null){
        
        activityServ.activityAdd(
          new SSActivityAddPar(
            par,
            par.user,
            SSActivityE.addUsersToCircle,
            par.circle,
            par.users,
            null,
            null,
            null,
            shouldCommit));
      }
      
      final SSEvalServerI     evalServ     = servCommons.getEvalServ();
      
      if(evalServ != null){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            par,
            par.user,
            SSToolContextE.sss,
            SSEvalLogE.addCircleUsers,
            par.circle,  //entity
            null, //content,
            null, //entities
            par.users, //users
            null, //creationTime
            shouldCommit));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeCircle(
    final SSCircleRemovePar par,
    final boolean           isPrivPubOrSystemGroupCircle,
    final boolean           shouldCommit) throws SSErr{
    
    if(isPrivPubOrSystemGroupCircle){
      return;
    }
    
    try{
      
      final SSEvalServerI     evalServ     = servCommons.getEvalServ();
      
      if(evalServ != null){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            par,
            par.user,
            SSToolContextE.sss,
            SSEvalLogE.removeCircle,
            par.circle,  //entity
            null, //content,
            null, //entities
            null, //users
            null, //creationTime
            shouldCommit));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeUsersFromCircle(
    final SSCircleUsersRemovePar par,
    final boolean                isPrivPubOrSystemGroupCircle,
    final boolean                shouldCommit) throws SSErr{
    
    if(isPrivPubOrSystemGroupCircle){
      return;
    }
    
    try{
      
      final SSEvalServerI     evalServ     = servCommons.getEvalServ();
      
      if(evalServ != null){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            par,
            par.user,
            SSToolContextE.sss,
            SSEvalLogE.removeCircleUsers,
            par.circle,  //entity
            null, //content,
            null, //entities
            par.users, //users
            null, //creationTime
            shouldCommit));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeEntitiesFromCircle(
    final SSCircleEntitiesRemovePar par,
    final boolean                   isPrivPubOrSystemGroupCircle,
    final boolean                   shouldCommit) throws SSErr{
    
    if(isPrivPubOrSystemGroupCircle){
      return;
    }
    
    try{
      
      final SSEvalServerI     evalServ     = servCommons.getEvalServ();
      
      if(evalServ != null){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            par,
            par.user,
            SSToolContextE.sss,
            SSEvalLogE.removeCircleEntities,
            par.circle,  //entity
            null, //content,
            par.entities, //entities
            null, //users
            null, //creationTime
            shouldCommit));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}