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
package at.tugraz.sss.servs.dataimport.impl;

import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSToolContextE;
import java.util.*;
import at.tugraz.sss.servs.eval.api.SSEvalServerI;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogE;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogPar;
import at.tugraz.sss.servs.eval.impl.*;

public class SSDataImportActAndLog {
  
  public void addTag(
    final SSServPar        servPar, 
    final SSToolContextE   toolContext, 
    final SSUri            entity,
    final String           tag, 
    final List<SSUri>      entities,
    final Long             creationTime, 
    final boolean          shouldCommit) throws SSErr{
    
    try{
      
      final SSEvalServerI evalServ = new SSEvalImpl();
    
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          servPar.user,
          toolContext,
          SSEvalLogE.addTag,
          entity, //entity
          tag, //content
          entities, //entities
          null, //users
          creationTime, //creationTime
          shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addNotebook(
    final SSServPar        servPar, 
    final SSUri            user,
    final SSToolContextE   toolContext, 
    final SSUri            notebook,
    final Long             creationTime, 
    final boolean          shouldCommit) throws SSErr{
    
    try{
      
      final SSEvalServerI evalServ = new SSEvalImpl();
    
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          toolContext,
          SSEvalLogE.addNotebook,
          notebook, //entity
          null, //content
          null, //entities
          null, //users
          creationTime, //creationTime
          shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addNote(
    final SSServPar        servPar, 
    final SSUri            user,
    final SSToolContextE   toolContext, 
    final SSUri            note,
    final List<SSUri>      entities,
    final Long             creationTime, 
    final boolean          shouldCommit) throws SSErr{
    
    try{
      
      final SSEvalServerI evalServ = new SSEvalImpl();
    
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          toolContext,
          SSEvalLogE.addNote,
          note, //entity
          null, //content
          entities, //entities
          null, //users
          creationTime, //creationTime
          shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addResource(
    final SSServPar        servPar, 
    final SSUri            user,
    final SSToolContextE   toolContext, 
    final SSUri            resource,
    final List<SSUri>      entities,
    final Long             creationTime, 
    final boolean          shouldCommit) throws SSErr{
    
    try{
      
      final SSEvalServerI evalServ = new SSEvalImpl();
      
      evalServ.evalLog(
        new SSEvalLogPar(
          servPar,
          user,
          toolContext,
          SSEvalLogE.addResource,
          resource, //entity
          null, //content
          entities, //entities
          null, //users
          creationTime, //creationTime
          shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}