/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.serv.dataimport.serv;

import at.tugraz.sss.conf.SSCoreConf;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.conf.api.SSCoreConfA;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportClientI;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportServerI;
import at.kc.tugraz.ss.serv.dataimport.conf.SSDataImportConf;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.*;
import at.kc.tugraz.ss.serv.dataimport.impl.SSDataImportImpl;
import at.kc.tugraz.ss.serv.jobs.evernote.conf.*;
import at.tugraz.sss.conf.*;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class SSDataImportServ extends SSServContainerI{
  
  public static final SSDataImportServ inst = new SSDataImportServ(SSDataImportClientI.class, SSDataImportServerI.class);
  
  protected SSDataImportServ(
    final Class servImplClientInteraceClass,
    final Class servImplServerInteraceClass){
    
    super(servImplClientInteraceClass, servImplServerInteraceClass);
  }
  
  @Override
  public SSServImplA getServImpl() throws SSErr{
    
    if(!conf.use){
      throw SSErr.get(SSErrE.servNotRunning);
    }
    
    if(servImpl != null){
      return servImpl;
    }
    
    synchronized(this){
      
      if(servImpl != null){
        return servImpl;
      }
      
      servImpl = new SSDataImportImpl(conf);
    }
    
    return servImpl;
  }
  
  @Override
  public SSServContainerI regServ() throws Exception{
    
    this.conf = SSCoreConf.instGet().getDataImport();
    
    SSServReg.inst.regServ(this);
    
    return this;
  }
  
  @Override
  public void initServ() throws Exception{
    
    final SSDataImportConf dataImportConf = (SSDataImportConf)conf;
    
    if(!dataImportConf.use){
      return;
    }
    
    if(!dataImportConf.initAtStartUp){
      return;
    }
  }
  
  @Override
  public void schedule() throws Exception{
    
    final SSDataImportConf dataImportConf = (SSDataImportConf)conf;
    
    if(
      !dataImportConf.use ||
      !dataImportConf.schedule){
      return;
    }
    
    if(
      SSObjU.isNull(dataImportConf.scheduleOps, dataImportConf.scheduleIntervals) ||
      dataImportConf.scheduleOps.isEmpty()                                        ||
      dataImportConf.scheduleIntervals.isEmpty()                                  ||
      dataImportConf.scheduleOps.size() != dataImportConf.scheduleIntervals.size()){
      
      SSLogU.warn("attempt to schedule with ops/intervals wrong");
      return;
    }
    
    if(dataImportConf.executeScheduleAtStartUp){
      
      for(String scheduleOp : dataImportConf.scheduleOps){
        
        if(SSStrU.equals(scheduleOp, SSVarNames.dataImportBitsAndPieces)){
          scheduleNowDataImportBitsAndPieces();
        }
        
        SSLogU.warn("attempt to schedule op at startup with no schedule task defined");
      }
    }
    
    for(int counter = 0; counter < dataImportConf.scheduleOps.size(); counter++){
      
      if(SSStrU.equals(dataImportConf.scheduleOps.get(counter), SSVarNames.dataImportBitsAndPieces)){
        scheduleAtFixedRateDataImportBitsAndPieces(
          SSDateU.getDatePlusMinutes(conf.scheduleIntervals.get(counter)),
          conf.scheduleIntervals.get(counter) * SSDateU.minuteInMilliSeconds);
      }
      
      SSLogU.warn("attempt to schedule op with no schedule task defined");
    }
  }
  
  @Override
  public SSCoreConfA getConfForCloudDeployment(
    final SSCoreConfA coreConfA,
    final List<Class> configuredServs) throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  private void scheduleNowDataImportBitsAndPieces(){
    
    try{
      
      final SSEvernoteConf evernoteConf = SSCoreConf.instGet().getEvernote();
      final SSServPar      servPar      = new SSServPar(null);
     
      for(int counter = 0; counter < SSCoreConf.instGet().getEvernote().getAuthTokens().size(); counter++){
        
        try{
          
          new SSDataImportBitsAndPiecesTask(
            new SSDataImportBitsAndPiecesPar(
              servPar,
              SSConf.systemUserUri,
              evernoteConf.getAuthTokens().get(counter),
              evernoteConf.getAuthEmails().get(counter),
              null,
              null,
              null,
              true, //importEvernote,
              false, //importEmail,
              true, //withUserRestriction,
              true)).handle(); //shouldCommit
          
        }catch(Exception error){
          SSLogU.err(error);
        }
      }
      
      for(int counter = 0; counter < evernoteConf.getEmailInEmails().size(); counter++){
        
        try{
          
          new SSDataImportBitsAndPiecesTask(
            new SSDataImportBitsAndPiecesPar(
              servPar,
              SSConf.systemUserUri,
              evernoteConf.getAuthTokens().get(counter),
              evernoteConf.getAuthEmails().get(counter),
              evernoteConf.getEmailInUsers().get(counter),
              evernoteConf.getEmailInPasswords().get(counter),
              evernoteConf.getEmailInEmails().get(counter),
              false, //importEvernote,
              true, //importEmail,
              true, //withUserRestriction,
              true)).handle(); //shouldCommit
          
        }catch(Exception error){
          SSLogU.err(error);
        }
      }
      
    }catch(Exception error){
      SSLogU.err(error);
    }
  }
  
  private void scheduleAtFixedRateDataImportBitsAndPieces(final Date startDate, final long timeBetween){
    
    try{
      
      final SSEvernoteConf evernoteConf = SSCoreConf.instGet().getEvernote();
      final SSServPar      servPar      = new SSServPar(null);
      
      for(int counter = 0; counter < SSCoreConf.instGet().getEvernote().getAuthTokens().size(); counter++){
        
        try{
          
          SSServReg.regScheduler(
            SSDateU.scheduleAtFixedRate(
              new SSDataImportBitsAndPiecesTask(
                new SSDataImportBitsAndPiecesPar(
                  servPar,
                  SSConf.systemUserUri,
                  evernoteConf.getAuthTokens().get(counter),
                  evernoteConf.getAuthEmails().get(counter),
                  null,
                  null,
                  null,
                  true, //importEvernote,
                  false, //importEmail,
                  true, //withUserRestriction,
                  true)), //shouldCommit
              startDate,
              timeBetween));
          
        }catch(Exception error){
          SSLogU.err(error);
        }
      }
      
      for(int counter = 0; counter < evernoteConf.getEmailInEmails().size(); counter++){
        
        try{
          
          SSServReg.regScheduler(
            SSDateU.scheduleAtFixedRate(
              new SSDataImportBitsAndPiecesTask(
                new SSDataImportBitsAndPiecesPar(
                  servPar,
                  SSConf.systemUserUri,
                  evernoteConf.getAuthTokens().get(counter),
                  evernoteConf.getAuthEmails().get(counter),
                  evernoteConf.getEmailInUsers().get(counter),
                  evernoteConf.getEmailInPasswords().get(counter),
                  evernoteConf.getEmailInEmails().get(counter),
                  false, //importEvernote,
                  true, //importEmail,
                  true, //withUserRestriction,
                  true)), //shouldCommit
              startDate,
              timeBetween));
          
        }catch(Exception error){
          SSLogU.err(error);
        }
      }
      
    }catch(Exception error){
      SSLogU.err(error);
    }
  }
}