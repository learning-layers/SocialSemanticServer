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
package at.tugraz.sss.servs.tag.impl;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.activity.api.SSActivityServerI;
import at.tugraz.sss.servs.activity.datatype.SSActivityE;
import at.tugraz.sss.servs.activity.datatype.SSActivityAddPar;
import at.tugraz.sss.servs.tag.datatype.SSTagLabel;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSToolContextE;
import at.tugraz.sss.servs.activity.impl.*;
import at.tugraz.sss.servs.eval.api.*;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogE;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogPar;
import at.tugraz.sss.servs.eval.impl.*;

public class SSTagActAndLogFct {
  
  public void addTag(
    final SSServPar        servPar,
    final SSUri            user,
    final SSUri            entity,
    final SSUri            tagURI,
    final SSTagLabel       label,
    final boolean          shouldCommit) throws SSErr{
    
    if(SSStrU.isEmpty(entity)){
      return;
    }
    
    try{
      
      final SSActivityServerI activityServ = new SSActivityImpl();
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.tagEntity,
          entity,
          null,
          SSUri.asListNotNull(tagURI),
          null,
          null,
          shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    if(SSStrU.isEmpty(label)){
      return;
    }
    
    try{
      
      final SSEvalServerI     evalServ     = new SSEvalImpl();
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.sss,
          SSEvalLogE.addTag,
          entity,  //entity
          SSStrU.toStr(label), //content,
          null, //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeTag(
    final SSServPar        servPar,
    final SSUri            user,
    final SSUri            entity,
    final SSTagLabel       label,
    final boolean          shouldCommit) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ = new SSActivityImpl();
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user,
          SSActivityE.removeTags,
          entity,
          null,
          null,
          null,
          null,
          shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    try{
      
      final SSEvalServerI     evalServ     = new SSEvalImpl();
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          SSToolContextE.sss,
          SSEvalLogE.removeTag,
          entity,  //entity
          SSStrU.toStr(label), //content,
          null, //entities
          null, //users
          null, //creationTime
          shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}