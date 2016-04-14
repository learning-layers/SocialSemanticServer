/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.common.impl;

import at.tugraz.sss.servs.common.api.*;
import at.tugraz.sss.servs.entity.datatype.*;
import java.lang.reflect.*;
import java.util.*;

public class SSClientServs {
  
  protected static final Map<SSServImplA, Class>  classForServ  = new HashMap<>();
  protected static final Map<String, SSServImplA> servForOp     = new HashMap<>();
  
  public void regServ(
    final SSServImplA serv,
    final Class       clientServClass) throws SSErr{
    
    try{
      
      synchronized(servForOp){
        
        final Method[] methods = clientServClass.getMethods();
        
        for(Method method : methods){
          servForOp.put(method.getName(), serv);
        }
      }
      
      synchronized(classForServ){
        classForServ.put(serv, clientServClass);
      }
       
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public SSServImplA getServ(
    final String op) throws SSErr{
    
    try{
      
      return servForOp.get(op);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public Class getClass(final SSServImplA serv) throws SSErr{
    
    try{
      
      return classForServ.get(serv);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

//  public void regServ(
//    final SSServContainerI servContainer) throws SSErr{
//    
//    try{
//      
//      synchronized(servsForClientI){
//        
//        if(servsForClientI.containsKey(servContainer.servImplClientInteraceClass)){
//          throw new Exception("serv server interface already registered");
//        }
//        
//        if(servContainer.servImplClientInteraceClass == null){
//          SSLogU.warn("service container has no service client interface", null);
//          return;
//        }
//        
//        servsForClientI.put(servContainer.servImplClientInteraceClass, servContainer);
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
  
//  public SSServImplA getClientServ(final Class clientServClass) throws SSErr{
//    
//    try{
//      
//      final SSServContainerI serv = servsForClientI.get(clientServClass);
//      
//      if(serv == null){
//        throw SSErr.get(SSErrE.servInvalid);
//      }
//      
//      return serv.getServImpl();
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }