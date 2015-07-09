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

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.activity.datatypes.SSActivity;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityGetPar;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentGetPar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSToolE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;
import sss.serv.eval.api.SSEvalClientI;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.conf.SSEvalConf;
import sss.serv.eval.datatypes.par.SSEvalLogPar;
import sss.serv.eval.datatypes.ret.SSEvalLogRet;
import sss.serv.eval.impl.fct.sql.SSEvalSQLFct;

public class SSEvalImpl 
extends SSServImplWithDBA 
implements 
  SSEvalClientI, 
  SSEvalServerI{

  private final SSEvalSQLFct sqlFct;
  private final SSEvalConf   evalConf;

  public SSEvalImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    sqlFct     = new SSEvalSQLFct(this);
    evalConf   = (SSEvalConf) conf;
  }

  @Override
  public void evalLog(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSEvalLogRet.get(evalLog(parA), parA.op));
  }

  @Override
  public Boolean evalLog(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEvalLogPar     par                 = new SSEvalLogPar(parA);
      final List<SSEntity>   targetEntities      = new ArrayList<>();
      final List<SSEntity>   targetUsers         = new ArrayList<>();
      final List<SSEntity>   notSelectedEntities = new ArrayList<>();
      String                 logText             = new String();
      String                 selectBitsMeasure   = SSStrU.empty;
      SSEntity               targetEntity        = null;
      SSActivity             activity            = null;
      SSCircleE              episodeSpace        = null;
      SSEntity               originUser;
      
      if(
        evalConf.tools.isEmpty()              ||
        !evalConf.tools.contains(SSToolE.bnp) ||
        par.type == null){
        return false;
      }

      if(par.forUser != null){
        originUser =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              null,
              par.forUser,  //entity
              null, //forUser
              false, //withUserRestriction
              null)); //descPar
        
      }else{
        originUser =           
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              null,
              par.user,  //entity
              null, //forUser
              false, //withUserRestriction
              null)); //descPar
      }
      
      if(par.entity != null){
        targetEntity = 
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              null,
              par.entity,  //entity
              null, //forUser
              false, //withUserRestriction
              null)); //descPar
      }
      
      for(SSUri entity : par.entities){
        targetEntities.add(
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              null,
              entity,  //entity
              null, //forUser
              false, //withUserRestriction
              null))); //descPar
      }
      
      for(SSUri user : par.users){
        targetUsers.add(
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              null,
              user,  //entity
              null, //forUser
              false, //withUserRestriction
              null))); //descPar
      }
      
      if(targetEntity != null){
        
        switch(targetEntity.type){
          
          case learnEp:
            
            episodeSpace = 
              ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleMostOpenCircleTypeGet(
                new SSCircleMostOpenCircleTypeGetPar(
                  null,
                  null,
                  originUser.id,
                  originUser.id,
                  targetEntity.id,
                  true));
              
            break;
        }
      }
      
      switch(par.type){
        
        case clickBit:{
          
          if(targetEntity == null){
            break;
          }
          
          switch(targetEntity.type){
            
            case activity:{
              
              activity = 
                ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityGet(
                  new SSActivityGetPar(
                    null, 
                    null, 
                    par.user, 
                    targetEntity.id));
              
              targetEntities.addAll (activity.entities);
              targetUsers.addAll    (activity.users);
              break;
            }
          }          
          break;
        }
      }
      
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
        
        if(episodeSpace != null){
          continue;
        }
        
        switch(entity.type){
          
          case learnEp:
            
            episodeSpace = 
              ((SSCircleServerI)SSServReg.getServ(SSCircleServerI.class)).circleMostOpenCircleTypeGet(
                new SSCircleMostOpenCircleTypeGetPar(
                  null,
                  null,
                  originUser.id,
                  originUser.id,
                  targetEntity.id,
                  true));
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
      
      switch(par.type){
        
        case copyLearnEpForUser:{

          if(
            episodeSpace  == null ||
            targetEntity  == null ||
            targetEntities.isEmpty()){
            break;
          }
          
          try{
          
            final SSLearnEpVersion learnEpVersion = 
              ((SSLearnEpServerI)SSServReg.getServ(SSLearnEpServerI.class)).learnEpVersionCurrentGet(
                new SSLearnEpVersionCurrentGetPar(null, null, originUser.id));

            final Integer itemCount = 
              learnEpVersion.learnEpCircles.size() + 
              learnEpVersion.learnEpEntities.size();
            
            selectBitsMeasure = targetEntities.size() + SSStrU.slash + itemCount;
            
            for(SSEntity circle : learnEpVersion.learnEpCircles){
              
              if(!SSStrU.contains(targetEntities, circle)){
                notSelectedEntities.add(circle);
              }
            }
            
            for(SSEntity entity : learnEpVersion.learnEpEntities){
              
              if(!SSStrU.contains(targetEntities, entity)){
                notSelectedEntities.add(entity);
              }
            }
          }catch(Exception error){
            SSServErrReg.reset();
            SSLogU.warn("learn ep version couldnt be retrieved");
          }
          
          break;
        }
      }

      //selected bits measure 
      logText += selectBitsMeasure;
      logText += SSStrU.semiColon;
      
      //not selected entities' ids
      for(SSEntity entity : notSelectedEntities){
        logText += entity.id;
        logText += SSStrU.comma;
      }
      
      logText += SSStrU.semiColon;
      
      //not selected entities' labels
      for(SSEntity entity : notSelectedEntities){
        logText += entity.label;
        logText += SSStrU.comma;
      }
      
      logText += SSStrU.semiColon;
      
      logText = SSStrU.replaceAllLineFeedsWithTextualRepr(logText);
      
      SSLogU.trace(logText, false);
      
      return true;
      
    }catch(Exception error){
      SSServErrReg.reset();
      SSLogU.warn("eval logging failed");
      return false;
    }
  }
}