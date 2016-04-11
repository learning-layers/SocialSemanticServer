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

import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.*;
import at.tugraz.sss.serv.reg.SSServErrReg;

import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.mail.api.SSMailClientI;
import at.tugraz.sss.servs.mail.api.SSMailServerI;
import at.tugraz.sss.servs.mail.conf.SSMailConf;
import at.tugraz.sss.servs.mail.datatype.SSMailSendPar;
import at.tugraz.sss.servs.mail.datatype.SSMailsReceivePar;
import java.util.ArrayList;
import java.util.List;

public class SSMailImpl 
extends SSServImplA
implements
  SSMailClientI,
  SSMailServerI{
  
  private final SSDBSQLI                              dbSQL;
  private final SSDBNoSQLI                            dbNoSQL;
  private final SSMailConf                            mailConf;
  private final SSMailSQLFct                          sqlFct;
  
  public SSMailImpl(final SSConfA conf) throws SSErr{
    
    super(conf);
    
    this.dbSQL          = (SSDBSQLI)   SSServReg.getServ(SSDBSQLI.class);
    this.dbNoSQL        = (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class);
    this.mailConf       = (SSMailConf) conf;
    this.sqlFct         = new SSMailSQLFct(dbSQL);
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
          
          final SSMailReceiverGMAILIMAP gmailIMAPReceive = new SSMailReceiverGMAILIMAP(mailConf);
          
          SSEntity.addEntitiesDistinctWithoutNull(
            mails, 
            gmailIMAPReceive.receiveMails(
              par.fromUser, 
              par.fromPassword)); 
          
          break;
        }
        
        case kcDavMailIMAP:{
          
          final SSMailReceiverKCDavIMAP kcReceive = new SSMailReceiverKCDavIMAP(mailConf, sqlFct);
          
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

