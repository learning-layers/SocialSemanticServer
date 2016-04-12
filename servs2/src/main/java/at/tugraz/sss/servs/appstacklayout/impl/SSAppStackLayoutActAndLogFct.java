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
package at.tugraz.sss.servs.appstacklayout.impl;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.appstacklayout.datatype.SSAppStackLayoutCreatePar;
import at.tugraz.sss.servs.appstacklayout.datatype.SSAppStackLayoutDeletePar;
import at.tugraz.sss.servs.appstacklayout.datatype.SSAppStackLayoutUpdatePar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSToolContextE;
import at.tugraz.sss.servs.eval.api.SSEvalServerI;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogE;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogPar;
import at.tugraz.sss.servs.eval.impl.*;

public class SSAppStackLayoutActAndLogFct {
  
  public void createAppStackLayout(
    final SSAppStackLayoutCreatePar par, 
    final SSUri                     appStackLayout, 
    final boolean                   shouldCommit) throws SSErr{
    
    try{
      final SSEvalServerI evalServ = new SSEvalImpl();
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par,
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.createAppStackLayout,
          appStackLayout, //entity
          null, //content
          null, //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeAppStackLayout(
    final SSAppStackLayoutDeletePar par, 
    final SSUri                     appStackLayout, 
    final boolean                   shouldCommit) throws SSErr{
    
    try{
      
      final SSEvalServerI evalServ = new SSEvalImpl();
        
      evalServ.evalLog(
        new SSEvalLogPar(
          par,
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.removeAppStackLayout,
          appStackLayout, //entity
          null, //content
          null, //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void updateAppStackLayout(
    final SSAppStackLayoutUpdatePar par, 
    final boolean                   shouldCommit) throws SSErr{
    
    try{
      
      final SSEvalServerI evalServ = new SSEvalImpl();
      
      if(par.app != null){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            par,
            par.user,
            SSToolContextE.sss,
            SSEvalLogE.updateAppStackLayoutApp,
            par.stack, //entity
            null, //content
            SSUri.asListNotNull(par.app), //entities
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
            par.stack, //entity
            SSStrU.toStr(par.label), //content
            null, //entities
            null, //users
            null, //creationTime
            shouldCommit));
      }
      
      if(par.description != null){

        evalServ.evalLog(
          new SSEvalLogPar(
            par,
            par.user,
            SSToolContextE.sss,
            SSEvalLogE.changeDescription,
            par.stack, //entity
            SSStrU.toStr(par.description), //content
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