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
package at.tugraz.sss.servs.mail.impl;

import at.tugraz.sss.servs.mail.impl.kc.SSMailReceiverKCDavIMAP;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.servs.mail.SSMailClientI;
import at.tugraz.sss.servs.mail.SSMailServerI;
import at.tugraz.sss.servs.mail.conf.SSMailConf;
import at.tugraz.sss.servs.mail.datatype.par.SSMailSendPar;
import at.tugraz.sss.servs.mail.datatype.par.SSMailsReceivePar;
import at.tugraz.sss.servs.mail.impl.gmx.SSMailSenderGMXSMTP;
import at.tugraz.sss.servs.mail.impl.gmail.SSMailReceiverGMAILIMAP;
import java.util.ArrayList;
import java.util.List;

public class SSMailImpl 
extends SSServImplWithDBA
implements
  SSMailClientI,
  SSMailServerI{
  
  private   final SSMailSQLFct  sqlFct;
  private   final SSMailConf    mailConf;
  
  public SSMailImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.mailConf       = (SSMailConf) conf;
    this.sqlFct         = new SSMailSQLFct(this);
  }
  
  @Override
  public Boolean mailSend(final SSMailSendPar par) throws Exception{
    
    try{
      
      switch(mailConf.sendProvider){
        
        case gmxSMTP:{
          
          final SSMailSenderGMXSMTP gmxMailSend = new SSMailSenderGMXSMTP(mailConf);
          
          gmxMailSend.sendMail(
            par.fromEmail, 
            par.toEmail, 
            par.subject, 
            par.content);
          
          break;
        }
      }
    
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return mailSend(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> mailsReceive(final SSMailsReceivePar par) throws Exception{
    
    try{
      
      switch(mailConf.receiveProvider){
        
        case gmailIMAP:{
          
          final SSMailReceiverGMAILIMAP gmailIMAPReceive = new SSMailReceiverGMAILIMAP(mailConf);
          
          return gmailIMAPReceive.receiveMails(par.fromEmail, par.fromPassword); 
        }
        
        case kcDavMailIMAP:{
          
          final SSMailReceiverKCDavIMAP kcReceive = new SSMailReceiverKCDavIMAP(mailConf);
          
          return kcReceive.receiveMails(par.fromEmail, par.fromPassword); 
        }
      }
    
      return new ArrayList<>();
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return mailsReceive(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

