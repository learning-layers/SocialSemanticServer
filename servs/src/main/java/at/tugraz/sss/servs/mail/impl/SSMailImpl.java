/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.mail.impl;

import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSDateU;
import at.tugraz.sss.servs.util.SSObjU;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.common.impl.SSSchedules;
import at.tugraz.sss.servs.entity.datatype.SSWarnE;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.entity.impl.*;
import at.tugraz.sss.servs.mail.api.SSMailClientI;
import at.tugraz.sss.servs.mail.api.SSMailServerI;
import at.tugraz.sss.servs.mail.conf.SSMailConf;
import at.tugraz.sss.servs.mail.datatype.SSMailSendPar;
import at.tugraz.sss.servs.mail.datatype.SSMailsReceivePar;
import java.util.ArrayList;
import java.util.List;

public class SSMailImpl 
extends SSEntityImpl
implements
  SSMailClientI,
  SSMailServerI{
  
  private final SSMailSQLFct sql       = new SSMailSQLFct(dbSQL);
  private final SSMailConf   mailConf;
  
  public SSMailImpl(){

    super(SSCoreConf.instGet().getMail());
    
    this.mailConf = (SSMailConf) conf;
  }
  
  @Override
  public void schedule() throws SSErr{
    
    if(
      !mailConf.use ||
      !mailConf.schedule){
      return;
    }
    
    if(
      SSObjU.isNull(mailConf.scheduleOps, mailConf.scheduleIntervals)   ||
      mailConf.scheduleOps.isEmpty()                                        ||
      mailConf.scheduleIntervals.isEmpty()                                  ||
      mailConf.scheduleOps.size() != mailConf.scheduleIntervals.size()){
      
      SSLogU.warn(SSWarnE.scheduleConfigInvalid, null);
      return;
    }
    
    if(mailConf.executeScheduleAtStartUp){
      
      for(String scheduleOp : mailConf.scheduleOps){
        
        if(SSStrU.isEqual(scheduleOp, SSVarNames.mailSend)){
          new SSSchedules().regScheduler(SSDateU.scheduleNow(new SSMailSendTask()));
        }
      }
    }
  }
  
  @Override
  public boolean mailSend(final SSMailSendPar par) throws SSErr{
    
    try{
      
      switch(mailConf.sendProvider){
        
        case kcDavMailSMTP:{
          
          new SSMailSenderKCDavSMTP().sendMail(
            mailConf,
            par.fromEmail,
            par.toEmail,
            par.subject,
            par.content);
          
          break;
        }
        
        case gmxSMTP:{
          
          new SSMailSenderGMXSMTP().sendMail(
            mailConf,
            par.fromEmail,
            par.toEmail,
            par.subject,
            par.content);
          
          break;
        }
        
        default: throw new UnsupportedOperationException();
      }
    
      return true;
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public List<SSEntity> mailsReceive(final SSMailsReceivePar par) throws SSErr{
    
    try{
      
      final List<SSEntity> mails = new ArrayList<>();
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      switch(mailConf.receiveProvider){
        
        case gmailIMAP:{
          
          final SSMailReceiverGMAILIMAP gmailIMAPReceive = new SSMailReceiverGMAILIMAP();
          
          SSEntity.addEntitiesDistinctWithoutNull(
            mails, 
            gmailIMAPReceive.receiveMails(
              par.fromUser, 
              par.fromPassword)); 
          
          break;
        }
        
        case kcDavMailIMAP:{
          
          final SSMailReceiverKCDavIMAP kcReceive = new SSMailReceiverKCDavIMAP(this, sql);
          
          SSEntity.addEntitiesDistinctWithoutNull(
            mails, 
            kcReceive.receiveMails(par));
          
          break;
        }
      }
    
      dbSQL.commit(par, par.shouldCommit);
      
      return mails;
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

