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

import at.kc.tugraz.ss.message.api.SSMessageServerI;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportServerI;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportEvalLogFilePar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSToolE;
import at.tugraz.sss.serv.SSServPar; 
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import java.util.ArrayList;
import java.util.List;
import sss.serv.eval.api.SSEvalClientI;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.conf.SSEvalConf;
import sss.serv.eval.datatypes.SSEvalLogEntry;
import sss.serv.eval.datatypes.par.SSEvalAnalyzePar;
import sss.serv.eval.datatypes.par.SSEvalLogPar;
import sss.serv.eval.datatypes.ret.SSEvalLogRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpServerI;
import at.kc.tugraz.ss.service.disc.api.SSDiscServerI;
import at.tugraz.sss.serv.SSClientE;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServRetI; 
import at.tugraz.sss.servs.livingdocument.api.SSLivingDocServerI;
import java.util.Date;

public class SSEvalImpl 
extends SSServImplWithDBA 
implements 
  SSEvalClientI, 
  SSEvalServerI{

  private final SSEvalConf                evalConf;
  private final SSEvalLogKnowBrain        evalLogKnowBrain;
  private final SSEvalLogBNP              evalLogBNP;
  private final SSUserCommons          userCommons;

  public SSEvalImpl(final SSConfA conf) throws SSErr{

    super(conf, (SSDBSQLI) SSDBSQL.inst.getServImpl(), (SSDBNoSQLI) SSDBNoSQL.inst.getServImpl());

    evalConf         = (SSEvalConf) conf;
    evalLogKnowBrain = new SSEvalLogKnowBrain();
    evalLogBNP       = new SSEvalLogBNP();
    userCommons      = new SSUserCommons();
    
  }

  @Override
  public void evalAnalyze(final SSEvalAnalyzePar par) throws SSErr{
    
    try{
      final SSDataImportServerI dataImportServ = (SSDataImportServerI) SSServReg.getServ(SSDataImportServerI.class);
      final SSLearnEpServerI    learnEpServ    = (SSLearnEpServerI)    SSServReg.getServ(SSLearnEpServerI.class);
      final SSEntityServerI     entityServ     = (SSEntityServerI)     SSServReg.getServ(SSEntityServerI.class);
      final SSMessageServerI    messageServ    = (SSMessageServerI)    SSServReg.getServ(SSMessageServerI.class);
      final SSDiscServerI       discServ       = (SSDiscServerI)       SSServReg.getServ(SSDiscServerI.class);
      final SSLivingDocServerI  ldServ         = (SSLivingDocServerI)  SSServReg.getServ(SSLivingDocServerI.class);
      final Date                oct1           = new Date(Long.valueOf("1443679508904"));
      final Date                nov17          = new Date(Long.valueOf("1447752600000"));
      final SSEvalLogAnalyzer   analyzer       =
        new SSEvalLogAnalyzer(
          learnEpServ, 
          entityServ, 
          messageServ,
          discServ,
          ldServ,
          oct1.getTime(), //timeBeginStudy
          oct1.getTime()); //timeBeginLogAnalyze
      
      final List<SSEvalLogEntry> logEntries =
        dataImportServ.dataImportEvalLogFile(
          new SSDataImportEvalLogFilePar(
            par.user,
            SSFileU.dirWorkingData() + "sss-eval.log",
            oct1.getTime())); //startTime
      
      analyzer.setEpisodes ();
      
      System.out.println();
      System.out.println();
      System.out.println("##################################");
      System.out.println("##################################");
      System.out.println("Users");
      System.out.println("##################################");
      System.out.println("##################################");
      System.out.println();
        
      analyzer.analyzeUsers  (logEntries);
      
      System.out.println();
      System.out.println();
      System.out.println("##################################");
      System.out.println("##################################");
      System.out.println("Living Docs");
      System.out.println("##################################");
      System.out.println("##################################");
      System.out.println();
      
      analyzer.analyzeLDs(logEntries);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSServRetI evalLog(final SSClientE clientType, final SSServPar parA) throws SSErr{

    try{
      userCommons.checkKeyAndSetUser(parA);

      final SSEvalLogPar par = (SSEvalLogPar) parA.getFromJSON(SSEvalLogPar.class);

      return SSEvalLogRet.get(evalLog(par));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public Boolean evalLog(final SSEvalLogPar par) throws SSErr{
    
    try{
      
      if(
        evalConf.tools == null                ||
        evalConf.tools.isEmpty()              ||
        par.type == null){
        return false;
      }
      
      final List<SSEntity>   targetEntities = new ArrayList<>();
      final List<SSEntity>   targetUsers    = new ArrayList<>();
      SSEntity               targetEntity   = null;
      SSEntity               originUser;
      
      originUser = 
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
          new SSEntityGetPar(
            null,
            par.user,  //entity
            false, //withUserRestriction
            null)); //descPar
      
      if(par.entity != null){
        targetEntity =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              par.entity,  //entity
              false, //withUserRestriction
              null)); //descPar
      }
      
      if(!par.entities.isEmpty()){
      
        SSEntity.addEntitiesDistinctWithoutNull(
          targetEntities,
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
            new SSEntitiesGetPar(
              par.user,
              par.entities,  //entities
              null,  //descPar
              false))); //withUserRestriction
      }
      
      if(!par.users.isEmpty()){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          targetUsers,
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
            new SSEntitiesGetPar(
              par.user,
              par.users,  //entities
              null,  //descPar
              false))); //withUserRestriction
      }

      for(SSToolE tool : evalConf.tools){
        
        switch(tool){
          
          case knowbrain:{
            evalLogKnowBrain.log(
              par, 
              originUser, 
              targetEntity, 
              targetEntities, 
              targetUsers);
            
            break;
          }
          
          case bnp:{
            evalLogBNP.log(
              par, 
              originUser, 
              targetEntity, 
              targetEntities, 
              targetUsers);
            break; 
          }
        }
      }
      
      return true;
    }catch(Exception error){
      SSServErrReg.reset();
      SSLogU.warn("eval logging failed");
      return false;
    }
  }
}