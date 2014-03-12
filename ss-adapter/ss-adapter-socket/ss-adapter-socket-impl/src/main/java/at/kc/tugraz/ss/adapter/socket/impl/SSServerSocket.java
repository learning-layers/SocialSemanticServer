/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.adapter.socket.impl;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplStartA;
import java.net.ServerSocket;
import java.net.Socket;

public class SSServerSocket implements Runnable{
  
  private int                     port       = -1;
  private boolean                 run        = true;
  
  public SSServerSocket(int port) {
    this.port = port;
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
      SSServErrReg.regErr(error, "accepting client socket didnt work");
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
      
      super(null);
      
      sScon = new SSSocketCon(clientSocket);
    }
    
    @Override
    public void run(){
      
      try{
        clientMsg = sScon.readMsgFullFromClient();
        
        SSLogU.info(clientMsg);
        
        par       = new SSServPar(clientMsg);
        
//        SSLogU.info("server enter: " + par.op);
        
        SSServA.callServViaClient(sScon, par);
        
      }catch(Exception error1){
        
        SSServErrReg.regErr(error1);
        
        try{
          
          if(par == null){
            SSServErrReg.regErrThrow(new Exception("couldnt get serv par"));
          }
          
          sScon.writeErrorFullToClient(SSServErrReg.getServiceImplErrors(), par.op);
        }catch(Exception error2){
          SSServErrReg.regErr(error2, "couldnt write error response");
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
      finalizeThread();
    }
  }
}