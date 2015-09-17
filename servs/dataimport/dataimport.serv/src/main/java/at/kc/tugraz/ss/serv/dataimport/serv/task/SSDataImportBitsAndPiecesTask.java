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

import at.tugraz.sss.serv.SSLogU;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportServerI;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportBitsAndPiecesPar;
import at.kc.tugraz.ss.serv.jobs.evernote.conf.SSEvernoteConf;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSServImplStartA;
import java.util.TimerTask;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;

public class SSDataImportBitsAndPiecesTask extends TimerTask {
  
  public SSDataImportBitsAndPiecesTask() throws Exception{
  }
  
  @Override
  public void run(){
    
    try{
      
      final SSEvernoteConf evernoteConf = SSCoreConf.instGet().getEvernote();
      
      if(evernoteConf.authTokens == null){
        return;
      }
      
      String emailInEmail    = null;
      String emailInPassword = null;
      
      for(int counter = 0; counter < evernoteConf.authTokens.size(); counter++){
        
        if(evernoteConf.emailInUsers != null){
          emailInEmail    = evernoteConf.emailInUsers.get(counter);
          emailInPassword = evernoteConf.emailInPasswords.get(counter);
        }
        
        new Thread(
          new SSDataImportBitsAndPiecesUpdater(
            new SSDataImportBitsAndPiecesPar(
              SSVocConf.systemUserUri,
              evernoteConf.authTokens.get(counter),
              evernoteConf.authEmails.get(counter),
              emailInEmail,
              emailInPassword,
              true, //withUserRestriction,
              true))).start();
      }
      
    }catch(Exception error){
      SSServErrReg.regErr(error);
    }
  }
  
  protected class SSDataImportBitsAndPiecesUpdater extends SSServImplStartA{
    
    private final SSDataImportBitsAndPiecesPar par;
    
    public SSDataImportBitsAndPiecesUpdater(
      final SSDataImportBitsAndPiecesPar par) throws Exception{
      
      super(null);
      
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
          finalizeImpl();
        }catch(Exception error2){
          SSLogU.err(error2);
        }
      }
    }
    
    @Override
    protected void finalizeImpl() throws Exception{
      finalizeThread(true);
    }
  }
}