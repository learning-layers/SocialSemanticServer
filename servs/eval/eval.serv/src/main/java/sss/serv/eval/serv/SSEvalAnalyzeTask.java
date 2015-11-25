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
package sss.serv.eval.serv;

import at.tugraz.sss.serv.SSLogU;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSServImplStartA;
import java.util.TimerTask;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.par.SSEvalAnalyzePar;

public class SSEvalAnalyzeTask extends TimerTask {
  
  public SSEvalAnalyzeTask() throws Exception{
  }
  
  @Override
  public void run(){
    
    try{
      
      new Thread(new SSEvalAnalyzer()).start();
      
    }catch(Exception error){
      SSServErrReg.regErr(error);
    }
  }
  
  protected class SSEvalAnalyzer extends SSServImplStartA{
    
    public SSEvalAnalyzer() throws Exception{
      super(null);
    }
    
    @Override
    public void run() {
      
      try{
        
        final SSEvalServerI evalServ = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
        
        evalServ.evalAnalyze(new SSEvalAnalyzePar(SSVocConf.systemUserUri));
        
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