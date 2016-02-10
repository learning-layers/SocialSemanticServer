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
package at.kc.tugraz.ss.service.disc.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.tugraz.sss.serv.util.SSLogU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import at.tugraz.sss.serv.datatype.par.*;
import java.util.List;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSDiscActAndLogFct{
  
  private final SSActivityServerI activityServ;
  private final SSEvalServerI     evalServ;
  
  public SSDiscActAndLogFct(
    final SSActivityServerI activityServ,
    final SSEvalServerI     evalServ){
    
    this.activityServ = activityServ;
    this.evalServ     = evalServ;
  }
  
  public void discEntryAdd(
    final SSServPar     servPar,
    final SSUri         user,
    final boolean       addNewDisc,
    final List<SSUri>   targets,
    final SSUri         disc,
    final SSEntityE     discType,
    final SSTextComment discDescription,
    final SSUri         entry,
    final SSTextComment entryContent,
    final List<SSUri>   entities,
    final List<SSLabel> entityLabels,
    final boolean       shouldCommit) throws SSErr{
    
    try{
      
      if(addNewDisc){
        
        for(SSUri target : targets){
          
          activityServ.activityAdd(
            new SSActivityAddPar(
              servPar,
              user,
              SSActivityE.discussEntity,
              target, //entity
              null, //users
              SSUri.asListNotNull(disc), //entities
              null, //comments
              null,
              shouldCommit));
        }
      }
      
      if(entry != null){
        
        activityServ.activityAdd(
          new SSActivityAddPar(
            servPar,
            user,
            SSActivityE.addDiscEntry,
            disc,
            null, //users,
            SSUri.asListNotNull(entry), //entities
            SSTextComment.asListWithoutNullAndEmpty(entryContent), //comment
            null,
            shouldCommit));
      }
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default: {
          SSServErrReg.regErrThrow(error);
          break;
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      if(addNewDisc){
        
        for(SSUri target : targets){
          
          evalServ.evalLog(
            new SSEvalLogPar(
              servPar,
              user,
              SSToolContextE.sss,
              SSEvalLogE.discussEntity,
              target, //entity
              null, //content
              SSUri.asListNotNull(disc), //entities
              null, //users
              null, //creationTime
              shouldCommit));
        }
        
        if(
          SSStrU.equals(discType, SSEntityE.qa) &&
          discDescription != null){
          
          evalServ.evalLog(
            new SSEvalLogPar(
              servPar,
              user,
              SSToolContextE.sss,
              SSEvalLogE.createDisc,
              disc, //entity
              SSStrU.toStr(discDescription), //content
              null, //entities
              null, //users
              null, //creationTime
              shouldCommit));
        }
      }
      
      if(entry != null){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            servPar,
            user,
            SSToolContextE.sss,
            SSEvalLogE.addDiscEntry,
            disc, //entity
            SSStrU.toStr(entryContent), //content
            SSUri.asListNotNull(entry), //entities
            null, //users
            null, //creationTime
            shouldCommit));
      }
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default: {
          SSServErrReg.regErrThrow(error);
          break;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void discTargetsAdd(
    final SSServPar     servPar,
    final SSUri       user,
    final SSUri       disc,
    final List<SSUri> targets,
    final boolean     shouldCommit) throws SSErr{
    
    if(disc == null){
      return;
    }
    
    try{
      
      for(SSUri target : targets){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            servPar,
            user,
            SSToolContextE.sss,
            SSEvalLogE.discussEntity,
            target, //entity
            null, //content
            SSUri.asListNotNull(disc), //entities
            null, //users
            null, //creationTime
            shouldCommit));
      }
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default: {
          SSServErrReg.regErrThrow(error);
          break;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}

//try{
//
//      if(
//        label != null &&
//        !SSStrU.equals(circleEntity.label, label)){
//
//        evalServ.evalLog(
//          new SSEvalLogPar(
//            user,
//            SSToolContextE.organizeArea,
//            SSEvalLogE.changeLabel,
//            learnEpCircle,
//            SSStrU.toStr(label), //content
//            SSUri.asListWithoutNullAndEmpty(learnEp), //entities
//            null, //users
//            shouldCommit));
//      }
//
//    }catch(SSErr error){
//
//      switch(error.code){
//        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
//        default: SSServErrReg.regErrThrow(error);
//      }
//
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//
//    try{
//
//      if(
//        description != null &&
//        !SSStrU.equals(circleEntity.description, description)){
//
//        evalServ.evalLog(
//          new SSEvalLogPar(
//            user,
//            SSToolContextE.organizeArea,
//            SSEvalLogE.changeDescription,
//            learnEpCircle,
//            SSStrU.toStr(description), //content
//            SSUri.asListWithoutNullAndEmpty(learnEp), //entities
//            null, //users
//            shouldCommit));
//      }
//
//    }catch(SSErr error){
//
//      switch(error.code){
//        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
//        default: SSServErrReg.regErrThrow(error);
//      }
//
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }