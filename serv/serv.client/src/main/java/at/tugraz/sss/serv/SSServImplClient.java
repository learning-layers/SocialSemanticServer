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
package at.tugraz.sss.serv;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SSServImplClient extends SSServImplStartA implements Runnable{

  private final Boolean     useCloud;
  private final SSSocketCon clientCon;
  private SSServPar         par         = null;
  private SSServImplA       servImpl    = null;
  
  public SSServImplClient(
    final Socket  clientSocket,
    final Boolean useCloud) throws Exception{
    
    super(null, null);
    
    clientCon          = new SSSocketCon(clientSocket);
    this.useCloud      = useCloud;
  }
  
  @Override
  public void run(){
    
    try{
      
      par = 
        new SSServPar(
          clientCon, 
          clientCon.readMsgFullFromClient());
      
      SSLogU.info(par.clientJSONRequ);
      
      servImpl = 
        SSServReg.inst.callServViaClient(
          par,
          useCloud);
      
    }catch(Exception error){
      
      SSServErrReg.regErr(error, false);
      
      if(par == null){
        SSServErrReg.regErr(new Exception("couldnt get serv par"), true);
      }
      
      try{
        par.clientCon.writeErrorFullToClient(SSServErrReg.getServiceImplErrors(), par.op);
      }catch(Exception error2){
        SSServErrReg.regErr(error2, true);
      }
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
    
    finalizeThread(false);
    
    SSServReg.inst.unregClientRequest(par.op, par.user, servImpl);
  }

  public static void regServImplUsedByThread(final SSServImplA servImpl){
    
    List<SSServImplA> servImplUsedList = servImplsUsedByThread.get();
    
    if(servImplUsedList.contains(servImpl)){
      return;
    }
    
    servImplUsedList.add(servImpl);
  }
  
   @Override
   protected void finalizeThread(final Boolean log){
    
    final List<SSServImplA> usedServs = new ArrayList<>();
    
    try{
      servImplsUsedByThread.get().remove(this);

      usedServs.addAll(servImplsUsedByThread.get());

      for(SSServImplA servImpl : usedServs){
        servImpl.finalizeImpl();
      }

    }catch(Exception error){
      SSServErrReg.regErr(error);
    }finally{
      SSServErrReg.logServImplErrors();
    }
  }
}
