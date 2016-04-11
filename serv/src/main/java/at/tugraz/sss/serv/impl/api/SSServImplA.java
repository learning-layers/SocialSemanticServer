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
package at.tugraz.sss.serv.impl.api;

import at.tugraz.sss.serv.conf.api.SSConfA;
  
public abstract class SSServImplA{

  protected final SSConfA conf;
  
  protected SSServImplA(
    final SSConfA      conf){
    
    this.conf   = conf;
  }
}

// protected static final ThreadLocal<List<SSServImplA>> servImplsUsedByThread = new ThreadLocal<List<SSServImplA>>(){
//    
//    @Override protected List<SSServImplA> initialValue(){
//      
//      try{
//        return new ArrayList<>();
//      }catch (Exception error){
//        SSLogU.err(error);
//        return null;
//      }
//    }
//  };

//public static void regServImplUsedByThread(final SSServImplA servImpl){
//    
//    List<SSServImplA> servImplUsedList = servImplsUsedByThread.get();
//    
//    if(servImplUsedList.contains(servImpl)){
//      return;
//    }
//    
//    servImplUsedList.add(servImpl);
//  }

  //  public static void addLogAndThrow(final Exception error) throws SSErr{
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
  
  //  public static void log(final Exception error) throws SSErr{
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
  //  public static void logAndThrow(final String logText) throws SSErr{
  //
  //    Exception error = new Exception(logText);
  //
  //    servImplErrors.get().add(SSErrForClient.get(error));
  //
  //    throw error;
  //  }
  //
  //  public static void logAndThrow(final Exception error) throws SSErr{
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
  //  public static void logAndThrow(final Exception error, final String logText) throws SSErr{
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

  //  public List<String> recommendTagsWithLanguageModel(SSUri userUri, SSUri entityUri, Integer maxTags) throws SSErr {
  //
  //    HashMap<String, Object> opPars = new HashMap<>();
  //
  //    opPars.put(SSVarU.user,           userUri);
  //    opPars.put(SSVarU.resource,       entityUri);
  //    opPars.put(SSVarU.numTags,        maxTags);
  //
  //    return (List<String>) SSServReg.callServServer(new SSServPar(SSVarNames.recommLanguageModel, opPars));
  //  }