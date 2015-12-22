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
package at.kc.tugraz.ss.service.search.service.task;

import at.kc.tugraz.ss.service.search.api.SSSearchServerI;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchCleanUpPar;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.impl.api.SSServImplStartA;
import at.tugraz.sss.serv.reg.*;
import java.util.TimerTask;
import at.tugraz.sss.conf.SSConf;

public class SSSearchResultPagesCacheCleanerTask extends TimerTask {
  
  @Override
  public void run(){
    
    try{
      new Thread(new SSSearchResultPagesCacheCleaner()).start();
    }catch(Exception error){
      SSServErrReg
        .regErr(error);
    }
  }
  
  protected class SSSearchResultPagesCacheCleaner extends SSServImplStartA implements Runnable{
    
    public SSSearchResultPagesCacheCleaner() throws Exception{
      super(null);
    }
    
    @Override
    public void run() {
      
      try{
        
        final SSSearchServerI searchServ = (SSSearchServerI) SSServReg.getServ(SSSearchServerI.class);
        
        searchServ.searchCleanUp(new SSSearchCleanUpPar(SSConf.systemUserUri));
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
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