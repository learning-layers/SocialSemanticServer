/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.disc.impl;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.activity.api.SSActivityServerI;
import at.tugraz.sss.servs.activity.datatype.SSActivityE;
import at.tugraz.sss.servs.activity.datatype.SSActivityAddPar;
import at.tugraz.sss.servs.disc.datatype.*;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSToolContextE;
import at.tugraz.sss.servs.activity.impl.*;
import java.util.List;
import at.tugraz.sss.servs.eval.api.SSEvalServerI;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogE;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogPar;
import at.tugraz.sss.servs.eval.impl.*;

public class SSDiscActAndLog{
  
  public void addDiscEntry(
    final SSServPar     servPar,
    final SSUri         user,
    final boolean       addNewDisc,
    final SSUri         disc,
    final SSEntityE     discType,
    final SSTextComment discDescription,
    final SSUri         entry,
    final SSTextComment entryContent,
    final List<SSLabel> entityLabels,
    final boolean       shouldCommit) throws SSErr{
    
    try{
        
      final SSActivityServerI actServ = new SSActivityImpl();
      
      if(entry != null){
        
        actServ.activityAdd(
          new SSActivityAddPar(
            servPar,
            user,
            SSActivityE.addDiscEntry,
            disc, //entity
            null, //users,
            SSUri.asListNotNull(entry), //entities
            SSTextComment.asListWithoutNullAndEmpty(entryContent), //comment
            null,
            shouldCommit));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      final SSEvalServerI     evalServ = new SSEvalImpl();
      
      if(addNewDisc){
        
        if(
          SSStrU.isEqual(discType, SSEntityE.qa) &&
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
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addDiscussionTargets(
    final SSDiscTargetsAddPar   par,
    final boolean               shouldCommit) throws SSErr{
    
    try{
      
      final SSActivityServerI actServ  = new SSActivityImpl();
      
      for(SSUri target : par.targets){
        
        actServ.activityAdd(
          new SSActivityAddPar(
            par,
            par.user,
            SSActivityE.discussEntity,
            target, //entity
            null, //users
            SSUri.asListNotNull(par.discussion), //entities
            null, //comments
            null,
            shouldCommit));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      final SSEvalServerI     evalServ = new SSEvalImpl();
            
      evalServ.evalLog(
        new SSEvalLogPar(
          par,
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.addDiscTargets,
          par.discussion, // entity
          null, //content
          par.targets, //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void updateDisc(
    final SSDiscUpdatePar par, 
    final boolean         shouldCommit) throws SSErr{
    
     try{
      
       final SSEvalServerI     evalServ = new SSEvalImpl();
             
       if(par.content != null){
         
         evalServ.evalLog(
           new SSEvalLogPar(
             par,
             par.user,
             SSToolContextE.sss,
             SSEvalLogE.changeDiscContent,
             par.disc, // entity
             SSStrU.toStr(par.content), //content
             null, //entities
             null, //users
             null, //creationTime
             shouldCommit));
       }
       
       if(par.label != null){
        
         evalServ.evalLog(
           new SSEvalLogPar(
             par,
             par.user,
             SSToolContextE.sss,
             SSEvalLogE.changeLabel,
             par.disc, // entity
             SSStrU.toStr(par.label), //content
             null, //entities
             null, //users
             null, //creationTime
             shouldCommit));
       }
       
       if(
         par.read != null &&
         par.read){
         
         evalServ.evalLog(
           new SSEvalLogPar(
             par,
             par.user,
             SSToolContextE.sss,
             SSEvalLogE.read,
             par.disc, // entity
             SSStrU.toStr(par.label), //content
             null, //entities
             null, //users
             null, //creationTime
             shouldCommit));
       }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void updateDiscEntry(
    final SSDiscEntryUpdatePar par, 
    boolean                    shouldCommit) throws SSErr{
    
     try{

       final SSEvalServerI     evalServ = new SSEvalImpl();
       
       if(par.content != null){
         
         evalServ.evalLog(
           new SSEvalLogPar(
             par,
             par.user,
             SSToolContextE.sss,
             SSEvalLogE.changeDiscEntryContent,
             par.entry, // entity
             SSStrU.toStr(par.content), //content
             null, //entities
             null, //users
             null, //creationTime
             shouldCommit));
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
//        !SSStrU.isEqual(circleEntity.label, label)){
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
//        !SSStrU.isEqual(circleEntity.description, description)){
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