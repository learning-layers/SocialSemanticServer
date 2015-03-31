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
package at.kc.tugraz.ss.main;

import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServA;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplStartA;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSSystemVersionGetRet;
import java.net.ServerSocket;
import java.net.Socket;

public class SSServerSocket implements Runnable{
  
  private int                     port       = -1;
  private boolean                 run        = true;
  
  public SSServerSocket() throws Exception {
    this.port = SSCoreConf.instGet().getSs().port;
  }
  
  @Override
  public void run(){
    
    try {
      SSLogU.info("Starting server on port " + port);
      
      ServerSocket server = new ServerSocket(port);
      
      while(run){
        new Thread(new SSServerSocketHandler(server.accept())).start();
      }
    }catch(Exception error){
      SSServErrReg.regErr(error, true);
    }
  }
  
  public void setRun(boolean v){
    run = v;
  }
  
  private class SSServerSocketHandler extends SSServImplStartA{
    
    private SSSocketCon sScon       = null;
    private SSServPar   par         = null;
    private String      clientMsg;
    
    public SSServerSocketHandler(Socket clientSocket) throws Exception{
      
      super(null, null);
      
      sScon = new SSSocketCon(clientSocket);
    }
    
    @Override
    public void run(){
      
      try{
        clientMsg = sScon.readMsgFullFromClient();
        
        SSLogU.info(clientMsg);
        
        par       = new SSServPar(clientMsg);
        
        switch(par.op){
          
          case systemVersionGet:
            sScon.writeRetFullToClient(
              new SSSystemVersionGetRet(
                SSCoreConf.instGet().getSs().version, 
                par.op));
            return;
        }
        
        SSServA.callServViaClient(
          sScon, 
          par, 
          SSCoreConf.instGet().getCloud().use);
        
      }catch(Exception error1){
        
        SSServErrReg.regErr(error1, false);
        
        try{
          
          if(par == null){
            SSServErrReg.regErrThrow(new Exception("couldnt get serv par"), true);
          }
          
          sScon.writeErrorFullToClient(SSServErrReg.getServiceImplErrors(), par.op);
        }catch(Exception error2){
          SSServErrReg.regErr(error2, true);
        }
      }finally{
        try{
          finalizeImpl();
          
//          SSLogU.info("server leave: " + par.op);
        }catch(Exception error3){
          SSLogU.err(error3);
        }
      }
    }
    
    @Override
    protected void finalizeImpl() throws Exception{
      finalizeThread(false);
    }
  }
}