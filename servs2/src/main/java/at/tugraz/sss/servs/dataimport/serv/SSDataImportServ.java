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
package at.tugraz.sss.servs.dataimport.serv;

import at.tugraz.sss.serv.conf.*;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.conf.api.SSCoreConfA;
import at.tugraz.sss.servs.dataimport.api.SSDataImportClientI;
import at.tugraz.sss.servs.dataimport.api.SSDataImportServerI;
import at.tugraz.sss.servs.dataimport.datatype.*;
import at.tugraz.sss.servs.dataimport.impl.SSDataImportImpl;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import at.tugraz.sss.servs.conf.SSCoreConf;
import at.tugraz.sss.servs.dataimport.conf.*;
import at.tugraz.sss.servs.evernote.conf.*;
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
      
      servImpl = new SSDataImportImpl((SSDataImportConf)conf);
    }
    
    return servImpl;
  }
  
  @Override
  public SSServContainerI regServ(final SSConfA conf) throws SSErr{
    
    this.conf = conf;
    
    SSServReg.inst.regServ(this);
    
    return this;
  }
  
  @Override
  public void initServ() throws SSErr{
    
    final SSDataImportConf dataImportConf = (SSDataImportConf)conf;
    
    if(!dataImportConf.use){
      return;
    }
    
    if(!dataImportConf.initAtStartUp){
      return;
    }
  }
  
  @Override
  public void schedule() throws SSErr{
    
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
      
      SSLogU.warn(SSWarnE.scheduleConfigInvalid, null);
      return;
    }
    
    final SSEvernoteConf evernoteConf = SSCoreConf.instGet().getEvernote();
    final SSServPar      servPar      = new SSServPar(null);
    Date                 startDate;
    
    for(int scheduleOpsCounter = 0; scheduleOpsCounter < dataImportConf.scheduleOps.size(); scheduleOpsCounter++){
      
      if(SSStrU.isEqual(dataImportConf.scheduleOps.get(scheduleOpsCounter), SSVarNames.dataImportMediaWikiUser)){
        new SSSchedules().regScheduler(SSDateU.scheduleNow(new SSDataImportMediaWikiUserTask()));
        continue;
      }
      
      if(SSStrU.isEqual(dataImportConf.scheduleOps.get(scheduleOpsCounter), SSVarNames.dataImportBitsAndPieces)){
        
        if(dataImportConf.executeScheduleAtStartUp){
          startDate = new Date();
        }else{
          startDate = SSDateU.getDatePlusMinutes(conf.scheduleIntervals.get(scheduleOpsCounter));
        }
        
        for(int counter = 0; counter < evernoteConf.getAuthTokens().size(); counter++){
          
          try{
            
            new SSSchedules().regScheduler(
              SSDateU.scheduleWithFixedDelay(
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
                conf.scheduleIntervals.get(scheduleOpsCounter) * SSDateU.minuteInMilliSeconds));
            
          }catch(Exception error){
            SSLogU.err(error);
          }
        }
        
        for(int counter = 0; counter < evernoteConf.getEmailInEmails().size(); counter++){
          
          try{
            
            new SSSchedules().regScheduler(
              SSDateU.scheduleWithFixedDelay(
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
                conf.scheduleIntervals.get(scheduleOpsCounter) * SSDateU.minuteInMilliSeconds));
            
          }catch(Exception error){
            SSLogU.err(error);
          }
        }
      }
    }
  }
}