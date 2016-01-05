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
package at.kc.tugraz.ss.serv.dataimport.serv.task;

import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportServerI;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportBitsAndPiecesPar;
import at.kc.tugraz.ss.serv.jobs.evernote.conf.SSEvernoteConf;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.impl.api.SSServImplStartA;
import java.util.TimerTask;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.reg.*;

public class SSDataImportBitsAndPiecesTask extends TimerTask {
  
  public SSDataImportBitsAndPiecesTask() throws Exception{
    SSServReg.regTimerTask(this);
  }
  
  @Override
  public void run(){
    
    try{
      
      final SSEvernoteConf evernoteConf = SSCoreConf.instGet().getEvernote();
      
      if(evernoteConf.getAuthTokens().isEmpty()){
        return;
      }
      
      for(int counter = 0; counter < evernoteConf.getAuthTokens().size(); counter++){
        
        Thread thread =
          new Thread(
            new SSDataImportBitsAndPiecesUpdater(
              new SSDataImportBitsAndPiecesPar(
                SSConf.systemUserUri,
                evernoteConf.getAuthTokens().get(counter),
                evernoteConf.getAuthEmails().get(counter),
                null,
                null,
                null,
                true, //importEvernote,
                false, //importEmail,
                true, //withUserRestriction,
                true)));
        
        thread.start();
        
        SSServReg.regTimerThread(thread);
      }
      
      new Thread(new SSDataImportBitsAndPiecesEmailUpdater()).start();
      
    }catch(Exception error){
      SSServErrReg.regErr(error);
    }
  }
  
  protected class SSDataImportBitsAndPiecesEmailUpdater implements Runnable{
    
    public SSDataImportBitsAndPiecesEmailUpdater() throws Exception{
    }
    
    @Override
    public void run() {
      
      try{
        
        final SSEvernoteConf evernoteConf = SSCoreConf.instGet().getEvernote();
        
        if(evernoteConf.getEmailInEmails().isEmpty()){
          return;
        }
        
        String emailInUser     = null;
        String emailInPassword = null;
        String emailInEmail    = null;
        
        for(int counter = 0; counter < evernoteConf.getEmailInEmails().size(); counter++){
          
          emailInUser     = evernoteConf.getEmailInUsers().get(counter);
          emailInPassword = evernoteConf.getEmailInPasswords().get(counter);
          emailInEmail    = evernoteConf.getEmailInEmails().get(counter);
          
          try{
            ((SSDataImportServerI) SSServReg.getServ(SSDataImportServerI.class)).dataImportBitsAndPieces(
              new SSDataImportBitsAndPiecesPar(
                SSConf.systemUserUri,
                evernoteConf.getAuthTokens().get(counter),
                evernoteConf.getAuthEmails().get(counter),
                emailInUser,
                emailInPassword,
                emailInEmail,
                false, //importEvernote,
                true, //importEmail,
                true, //withUserRestriction,
                true));
          }catch(Exception error){
            SSServErrReg.reset();
          }
        }
        
      }catch(Exception error){
        SSServErrReg.regErr(error);
      }finally{
        
        try{
//          finalizeImpl();
        }catch(Exception error2){
          SSLogU.err(error2);
        }
      }
    }
  }
  
  protected class SSDataImportBitsAndPiecesUpdater implements Runnable{
    
    private final SSDataImportBitsAndPiecesPar par;
    
    public SSDataImportBitsAndPiecesUpdater(
      final SSDataImportBitsAndPiecesPar par) throws Exception{
      
      this.par = par;
    }
    
    @Override
    public void run() {
      
      try{
        
        ((SSDataImportServerI) SSServReg.getServ(SSDataImportServerI.class)).dataImportBitsAndPieces(par);
        
      }catch(Exception error){
        SSServErrReg.regErr(error);
      }finally{
        
        try{
//          finalizeImpl();
        }catch(Exception error2){
          SSLogU.err(error2);
        }
      }
    }
  }
}