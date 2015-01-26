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

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.jobs.evernote.conf.SSEvernoteConf;
import at.kc.tugraz.ss.serv.serv.api.SSServImplStartA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import java.util.TimerTask;

public class SSDataImportEvernoteTask extends TimerTask {
  
  @Override
  public void run(){
    
    try{
      
      final SSEvernoteConf evernoteConf = SSCoreConf.instGet().getEvernoteConf();
      
      for(int counter = 0; counter < evernoteConf.authTokens.size(); counter++){
        
        new Thread(
          new SSDataImportEvernoteUpdater(
            evernoteConf.authTokens.get(counter),
            evernoteConf.authEmails.get(counter))).start();
      }
    }catch(Exception error){
      SSServErrReg.regErr(error);
    }
  }
  
  protected class SSDataImportEvernoteUpdater extends SSServImplStartA{
    
    private final String authToken;
    private final String email;
    
    public SSDataImportEvernoteUpdater(
      final String authToken,
      final String email) throws Exception{
      
      super(null);
      
      this.authToken = authToken;
      this.email     = email;
    }
    
    @Override
    public void run() {
      
      try{
        
        SSServCaller.dataImportEvernote(
          SSVoc.systemUserUri,
          authToken,
          email,
          true);
            
      }catch(Exception error){
        SSLogU.warn("evernote import failed: " + authToken);
        SSServErrReg.reset();
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
      finalizeThread();
    }
  }
}