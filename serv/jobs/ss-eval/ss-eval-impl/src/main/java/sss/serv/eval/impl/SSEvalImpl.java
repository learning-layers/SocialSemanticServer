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

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.activity.datatypes.SSActivity;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.SSCircleE;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSToolContextE;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSToolE;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.serv.caller.SSServCallerU;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import java.util.ArrayList;
import java.util.List;
import sss.serv.eval.api.SSEvalClientI;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.conf.SSEvalConf;
import sss.serv.eval.datatypes.par.SSEvalLogPar;
import sss.serv.eval.datatypes.ret.SSEvalLogRet;
import sss.serv.eval.impl.fct.sql.SSEvalSQLFct;

public class SSEvalImpl extends SSServImplWithDBA implements SSEvalClientI, SSEvalServerI{

  private final SSEvalSQLFct sqlFct;
  private final SSEvalConf   evalConf;

  public SSEvalImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{

    super(conf, null, dbSQL);

    sqlFct     = new SSEvalSQLFct(this);
    evalConf   = (SSEvalConf) conf;
  }

  @Override
  public void evalLog(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSEvalLogRet.get(evalLog(parA), parA.op));
  }

  @Override
  public Boolean evalLog(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEvalLogPar     par              = new SSEvalLogPar(parA);
      final List<SSEntity>   targetEntities   = new ArrayList<>();
      final List<SSEntity>   targetUsers      = new ArrayList<>();
      String                 logText          = new String();
      SSEntity               targetEntity     = null;
      SSActivity             activity         = null;
      SSEntity               originUser;
      SSCircleE              episodeSpace     = null;
      
      if(
        evalConf.tools.isEmpty()              ||
        !evalConf.tools.contains(SSToolE.bnp) ||
        par.type == null){
        return false;
      }
      
      if(par.forUser != null){
        originUser = SSServCaller.entityGet(par.forUser);
      }else{
        originUser = SSServCaller.entityGet(par.user);
      }
      
      if(par.entity != null){
        targetEntity = SSServCaller.entityGet(par.entity);
      }
      
      for(SSUri entity : par.entities){
        targetEntities.add(SSServCaller.entityGet(entity));
      }
      
      for(SSUri user : par.users){
        targetUsers.add(SSServCaller.entityGet(user));
      }
      
      switch(par.type){
        
        case clickBit:{
          
          if(targetEntity == null){
            break;
          }
          
          switch(targetEntity.type){
            
            case activity:{
              activity = SSServCaller.activityGet(par.user, targetEntity.id);
              
              targetEntities.addAll (activity.entities);
              targetUsers.addAll    (activity.users);
              break;
            }
          }          
          break;
        }
      }
      
      //timestamp;tool context;user label;log type;entity;entity type;entity label;content;tag type;entities' ids;entities' labels;users' labels
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
      }else{
        if(activity != null){
          logText += activity.activityType;
        }
      }
      
      logText += SSStrU.semiColon;
      
      // tag type
      switch(par.type){
        
        case addTag:{
          
          if(par.content != null){

            if(par.content.startsWith("*")){
              logText += 2;
            }else{
              logText += 1;
            }
          }
          
          break;
        }
      }
      
      logText += SSStrU.semiColon;
      
      // entities' ids
      for(SSEntity entity : targetEntities){
        logText += entity.id;
        logText += SSStrU.comma;
        
        switch(entity.type){
          
          case learnEp:
            
            episodeSpace = 
              SSServCaller.circleMostOpenCircleTypeGet(
                originUser.id, 
                originUser.id,
                entity.id, 
                true);
            break;
        }
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
      if(episodeSpace != null){
        logText += episodeSpace;
      }
      
      logText += SSStrU.semiColon;
      
      logText = SSStrU.replaceAllLineFeedsWithTextualRepr(logText);
      
      SSLogU.trace(logText, false);
      
      return true;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}