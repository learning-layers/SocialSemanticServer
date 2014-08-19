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
package at.kc.tugraz.ss.serv.dataimport.impl;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.dataimport.conf.SSDataImportConf;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportEvernotePar;
import at.kc.tugraz.ss.serv.dataimport.impl.evernote.SSDataImportEvernoteHelper;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.db.serv.SSDBSQL;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplStartWithDBA;

public class SSDataImportEvernoteHandler extends SSServImplStartWithDBA{
  
  private final SSDataImportEvernotePar par;
  private final SSDataImportImpl        servImpl;
  
  public SSDataImportEvernoteHandler(
    final SSDataImportConf         conf,
    final SSDataImportEvernotePar  par,
    final SSDataImportImpl         servImpl) throws Exception{
    
    super(conf, null);
    
    this.par      = par;
    this.servImpl = servImpl;
  }
  
  @Override
  public void run(){
    
    try{
    
      SSLogU.info("start data import for evernote account " + par.authToken);                
      
      final SSDBSQLI                     dbSQL                   = (SSDBSQLI) SSDBSQL.inst.serv();
      final SSDataImportEvernoteHelper   dataImpEvernoteHelper   = new SSDataImportEvernoteHelper (dbSQL); 
        
      dbSQL.startTrans(par.shouldCommit);
      
      dataImpEvernoteHelper.setBasicEvernoteInfo  (par);
      dataImpEvernoteHelper.handleLinkedNotebooks ();
      dataImpEvernoteHelper.setSharedNotebooks    ();
      dataImpEvernoteHelper.handleNotebooks       (par);
      
      dbSQL.commit(par.shouldCommit);
      
      SSServA.removeClientRequ(par.op, SSStrU.toStr(par.user), servImpl);
      
      SSLogU.info("end data import for evernote account " + par.authToken);
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(!dbSQL.rollBack(par)){
        SSServErrReg.regErr(deadLockErr);
      }
      
    }catch(Exception error){
      dbSQL.rollBack(par);
      SSServErrReg.regErr(error);
    }finally{
      
      try{
        finalizeImpl();
      }catch(Exception error3){
        SSLogU.err(error3);
      }
    }
  }
  
  @Override
  protected void finalizeImpl() throws Exception{
    finalizeThread();
  }
}
