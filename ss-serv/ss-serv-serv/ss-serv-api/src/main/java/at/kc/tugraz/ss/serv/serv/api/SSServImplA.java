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

public abstract class SSServImplA{

  protected SSConfA                        conf         = null;
  
  protected abstract void finalizeImpl      ()    throws Exception;
  
  protected SSServImplA(final SSConfA conf){
    this.conf = conf;
  }
  
  public void handleClientOp(
    final Class       clientInterfaceClass, 
    final SSSocketCon sSCon, 
    final SSServPar   par) throws Exception{
    
    if(clientInterfaceClass == null){
      SSServErrReg.regErr(new Exception("service op shouldnt be instantiated this way"));
      return;
    }
    
    clientInterfaceClass.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }

  public Object handleServerOp(
    final Class     serverInterfaceClass, 
    final SSServPar par) throws Exception{
    
    if(serverInterfaceClass == null){
      SSServErrReg.regErr(new Exception("service op shouldnt be instantiated this way"));
      return null;
    }
    
    return serverInterfaceClass.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }
}

  //  public static void addLogAndThrow(final Exception error) throws Exception{
  //
  //    if(error == null){
  //      SSLogU.logAndThrow(new Exception("error null"));
  //    }
  //
  //    SSLogU.logError(error);
  //
  //    servImplErrors.get().add(SSErrForClient.get(error));
  //
  //    throw error;
  //  }
  
  //  public static void log(final Exception error) throws Exception{
  //
  //    SSLogU.logError(error);
  //
  //    if(error == null){
  //      SSLogU.logAndThrow(new Exception("error null"));
  //    }
  //
  //    servImplErrors.get().add(SSErrForClient.get(error));
  //  }
  //
  //  public static void logAndThrow(final String logText) throws Exception{
  //
  //    Exception error = new Exception(logText);
  //
  //    servImplErrors.get().add(SSErrForClient.get(error));
  //
  //    throw error;
  //  }
  //
  //  public static void logAndThrow(final Exception error) throws Exception{
  //
  //    SSLogU.logError(error);
  //
  //    if(error == null){
  //      SSLogU.logAndThrow(new Exception("error null"));
  //    }
  //
  //    servImplErrors.get().add(SSErrForClient.get(error));
  //
  //    throw error;
  //  }
  //
  //  public static void logAndThrow(final Exception error, final String logText) throws Exception{
  //
  //    SSLogU.logError(error, logText);
  //
  //    if(error == null){
  //      SSLogU.logAndThrow(new Exception("error null"));
  //    }
  //
  //    error.printStackTrace();
  //
  //    servImplErrors.get().add(SSErrForClient.get(error));
  //
  //    throw error;
  //  }

  //  public List<String> recommendTagsWithLanguageModel(SSUri userUri, SSUri entityUri, Integer maxTags) throws Exception {
  //
  //    HashMap<String, Object> opPars = new HashMap<String, Object>();
  //
  //    opPars.put(SSVarU.user,           userUri);
  //    opPars.put(SSVarU.resource,       entityUri);
  //    opPars.put(SSVarU.numTags,        maxTags);
  //
  //    return (List<String>) SSServReg.callServServer(new SSServPar(SSMethU.recommLanguageModel, opPars));
  //  }