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
package at.tugraz.sss.serv.container.api;

import at.tugraz.sss.serv.impl.api.SSServImplA;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.datatype.SSErr;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class SSServContainerI {
 
  public        SSConfA                                         conf                            = null;
  protected     SSServImplA                                     servImpl                        = null;
  public        final Class                                     servImplClientInteraceClass;
  public        final Class                                     servImplServerInteraceClass;
  
//      ExecutorService          executor  = Executors.newFixedThreadPool(10);
////    Future<ReturnType> result = executor.submit(new CallableTask());
  
  protected SSServContainerI(
    final Class servImplClientInteraceClass,
    final Class servImplServerInteraceClass){
    
    this.servImplClientInteraceClass = servImplClientInteraceClass;
    this.servImplServerInteraceClass = servImplServerInteraceClass;
  }
  
  public    abstract void             initServ                  () throws SSErr;
  public    abstract void             schedule                  () throws SSErr;
  public    abstract SSServContainerI regServ                   (final SSConfA conf) throws SSErr;
  public    abstract SSServImplA      getServImpl               () throws SSErr;
  
  public List<String> publishClientOps() throws SSErr{
    
    final List<String> clientOps = new ArrayList<>();
    
    if(servImplClientInteraceClass == null){
      return clientOps;
    }
    
    final Method[]      methods   = servImplClientInteraceClass.getMethods();
    
    for(Method method : methods){
      clientOps.add(method.getName());
    }
    
    return clientOps;
  }
}

//  protected     SSErr                                           servImplCreationError           = null;
//  protected abstract SSServImplA      createServImplForThread   () throws SSErr;

//  private final ThreadLocal<SSServImplA> servImplsByServByThread = new ThreadLocal<SSServImplA>(){
//    
//    @Override
//    protected SSServImplA initialValue(){
//
//      try{
//        return createServImplForThread();
//      }catch (Exception error){
//        SSLogU.err(error);
//        servImplCreationError = SSErr.get(SSErrE.servImplCreationFailed, error);
//        return null;
//      }
//    }
//  };

//public SSServImplA getServImpl() throws SSErr{
//    
////    try{
//      
//      if(!conf.use){
//        throw SSErr.get(SSErrE.servNotRunning);
//      }
//      
//      final SSServImplA servTmp = servImplsByServByThread.get();
//      
//      if(servImplCreationError != null){
//        throw servImplCreationError;
//      }
//      
//      return servTmp;
////      SSLogU.err(error);
////      throw error;
//  }