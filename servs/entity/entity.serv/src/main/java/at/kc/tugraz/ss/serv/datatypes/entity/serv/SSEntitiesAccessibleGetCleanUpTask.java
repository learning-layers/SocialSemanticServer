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
package at.kc.tugraz.ss.serv.datatypes.entity.serv;

import at.tugraz.sss.serv.impl.api.SSEntityServerI;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.impl.api.SSServImplStartA;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.par.SSEntitiesAccessibleGetCleanUpPar;
import java.util.TimerTask;
import at.kc.tugraz.ss.conf.conf.SSVocConf;

public class SSEntitiesAccessibleGetCleanUpTask extends TimerTask {
  
  @Override
  public void run(){
    
    try{
      new Thread(new SSEntitiesAccessibleGetCleaner()).start();
    }catch(Exception error){
      SSServErrReg.regErr(error);
    }
  }
  
  protected class SSEntitiesAccessibleGetCleaner extends SSServImplStartA{
    
    public SSEntitiesAccessibleGetCleaner() throws Exception{
      super(null);
    }
    
    @Override
    public void run() {
      
      try{
        
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      entityServ.entitiesAccessibleGetCleanUp(new SSEntitiesAccessibleGetCleanUpPar(SSVocConf.systemUserUri));
      
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