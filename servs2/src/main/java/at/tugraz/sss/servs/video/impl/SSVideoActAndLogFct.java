 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.servs.video.impl;

import at.tugraz.sss.servs.video.datatype.SSVideoUserAnnotationAddPar;
import at.tugraz.sss.servs.video.datatype.SSVideoUserAddPar;
import at.tugraz.sss.servs.video.datatype.SSVideoAnnotationsSetPar;
import at.tugraz.sss.servs.activity.api.SSActivityServerI;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import at.tugraz.sss.servs.eval.api.SSEvalServerI;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogE;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogPar;

public class SSVideoActAndLogFct {
  
  private final SSActivityServerI activityServ;
  private final SSEvalServerI     evalServ;
  
  public SSVideoActAndLogFct(
    final SSActivityServerI activityServ,
    final SSEvalServerI     evalServ){
    
    this.activityServ = activityServ;
    this.evalServ     = evalServ;
  }
  
  public void createVideo(
    final SSVideoUserAddPar par,
    final SSUri             video,
    final boolean           shouldCommit) throws SSErr{
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par,
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.createVideo,
          video, //entity
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
  
  public void createVideoAnnotation(
    final SSVideoUserAnnotationAddPar par,
    final SSUri                       annotation,
    final boolean                     shouldCommit) throws SSErr{
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par,
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.createVideoAnnotation,
          par.video, //entity
          null, //content
          SSUri.asListNotNull(annotation), //entities
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

  public void removeVideoAnnotation(
    final SSVideoAnnotationsSetPar    par,
    final SSUri                       annotation,
    final boolean                     shouldCommit) throws SSErr{
    
    try{
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par,
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.removeVideoAnnotation,
          par.video, //entity
          null, //content
          SSUri.asListNotNull(annotation), //entities
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