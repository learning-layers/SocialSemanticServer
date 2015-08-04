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
package sss.serv.eval.impl;

import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSStrU;
import java.util.List;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSEvalLogKnowBrain {

  public void log(
    final SSEvalLogPar     par,
    final SSEntity         originUser,
    final SSEntity         targetEntity,
    final List<SSEntity>   targetEntities,
    final List<SSEntity>   targetUsers){
    
    try{
      
      String                 logText             = new String();
      
      //timestamp;tool context;user label;log type;entity;entity type;entity label;content;tag type;entities' ids;entities' labels;users' labels;episodespace;selected bits measure;not selected entities' ids;not selected entities' labels
      //time stamp
      logText += SSDateU.dateAsLong();
      logText += SSStrU.semiColon;
      
      //tool context
      if(par.toolContext != null){
        logText += par.toolContext;
      }
      
      logText += SSStrU.semiColon;
      
      //user
      if(originUser != null){
        logText += originUser.label;
      }
      
      logText += SSStrU.semiColon;
      
      // log type
      logText += par.type;
      logText += SSStrU.semiColon;
      
      // entity
      if(targetEntity != null){
        logText += targetEntity.id;
      }
      
      logText += SSStrU.semiColon;
      
      // entity type
      if(targetEntity != null){
        logText += targetEntity.type;
      }
      
      logText += SSStrU.semiColon;
      
      // entity label
      if(targetEntity != null){
        logText += targetEntity.label;
      }
      
      logText += SSStrU.semiColon;
      
      // content
      if(par.content != null){
        logText += par.content;
      }
      
      logText += SSStrU.semiColon;

      // tag type
      logText += SSStrU.semiColon;
      
      // entities' ids
      for(SSEntity entity : targetEntities){
        logText += entity.id;
        logText += SSStrU.comma;
      }
      
      logText += SSStrU.semiColon;
      
      // entities' labels
      for(SSEntity entity : targetEntities){
        logText += entity.label;
        logText += SSStrU.comma;
      }
      
      logText += SSStrU.semiColon;
      
      // users' labels
      for(SSEntity user : targetUsers){
        logText += user.label;
        logText += SSStrU.comma;
      }
      
      logText += SSStrU.semiColon;
      
      // episode space
      logText += SSStrU.semiColon;
      
      //selected bits measure
      logText += SSStrU.semiColon;
      
      //not selected entities' ids
      logText += SSStrU.semiColon;
      
      //not selected entities' labels
      logText += SSStrU.semiColon;
      
      logText = SSStrU.replaceAllLineFeedsWithTextualRepr(logText);
      
      SSLogU.trace(logText, false);
      
    }catch(Exception error){
      SSServErrReg.reset();
      SSLogU.warn("eval logging failed");
    }
  }
}
