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
package at.tugraz.sss.servs.like.impl;

import at.tugraz.sss.serv.util.SSLogU;
import at.kc.tugraz.ss.like.datatypes.par.*;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import at.tugraz.sss.serv.reg.*;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSLikeActAndLog{
  
  public void setLike(
    final SSLikeUserSetPar par,
    final boolean          shouldCommit) throws SSErr{
    
    try{
      
      final SSEvalServerI evalServ = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
      
      if(par.value == 1){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            par,
            par.user,
            SSToolContextE.sss,
            SSEvalLogE.likeEntity,
            par.entity, //entity
            null, //content
            null, //entities
            null, //users
            null, //creationTime
            shouldCommit));
      }
      
      if(par.value == -1){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            par,
            par.user,
            SSToolContextE.sss,
            SSEvalLogE.dislikeEntity,
            par.entity, //entity
            null, //content
            null, //entities
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