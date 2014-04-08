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
package at.kc.tugraz.ss.serv.serv.api;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.ArrayList;
import java.util.List;

public abstract class SSServImplStartA extends SSServImplA implements Runnable{

  private static final ThreadLocal<List<SSServImplA>> servImplsUsedByThread = new ThreadLocal<List<SSServImplA>>(){
    
    @Override protected List<SSServImplA> initialValue() {
      
      try{
        return new ArrayList<SSServImplA>();
      }catch (Exception error){
        SSServErrReg.regErr(error);
        return null;
      }
    }
  };
  
  public SSServImplStartA(final SSServConfA conf){
    super(conf);
  }
  
  public static void regServImplUsedByThread(final SSServImplA servImpl){
    
    List<SSServImplA> servImplUsedList = servImplsUsedByThread.get();
    
    if(servImplUsedList.contains(servImpl)){
      return;
    }
    
    servImplUsedList.add(servImpl);
  }
  
  protected void finalizeThread(){
    
    List<SSServImplA> usedServs = new ArrayList<SSServImplA>();
    
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
  
  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Object handleServerOp(SSServPar parA) throws Exception{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<SSMethU> publishClientOps() throws Exception{
    return new ArrayList<SSMethU>();
  }

  @Override
  public List<SSMethU> publishServerOps() throws Exception{
    return new ArrayList<SSMethU>();
  }
}
