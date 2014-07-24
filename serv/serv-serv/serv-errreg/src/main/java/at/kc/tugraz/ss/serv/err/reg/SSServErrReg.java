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
package at.kc.tugraz.ss.serv.err.reg;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import java.util.ArrayList;
import java.util.List;

public class SSServErrReg {

  private static final ThreadLocal<List<SSErrForClient>> servImplErrors  = new ThreadLocal<List<SSErrForClient>>(){
    
    @Override protected List<SSErrForClient> initialValue() {
      
      try{
        return new ArrayList<>();
      }catch (Exception error){
        SSLogU.err(error);
        return null;
      }
    }
  };
  
  public static Boolean containsErr(final Class errClass){
    
    for(SSErrForClient error : servImplErrors.get()){
      
      if(error.exception.getClass() == errClass){
        return true;
      }
    }
    
    return false;
  }
    
  public static List<SSErrForClient> getServiceImplErrors(){
    return new ArrayList<>(servImplErrors.get());
  }
  
  public static void regErr(final Exception error){
    
    if(error == null){
      SSLogU.err(new Exception("error null"));
      return;
    }
    
    if(!SSStrU.contains(servImplErrors.get(), error)){
      SSLogU.err(error);
    }
    
    try{
      servImplErrors.get().add(SSErrForClient.get(error));
    }catch(Exception error1){
      
      SSLogU.err(error1);
      
      try{
        servImplErrors.get().add(SSErrForClient.get(error1));
      }catch(Exception error2){
        SSLogU.err(error2);
      }
    }
  }
  
  public static void regErr(final Exception error, final String logText){
    
    if(error == null){
      SSLogU.err(new Exception("error null"));
      return;
    }
    
    if(!containsErr(error.getClass())){
      SSLogU.err(error, logText);
    }
    
    try{
      servImplErrors.get().add(SSErrForClient.get(error));
    }catch(Exception error1){
      
      SSLogU.err(error1);
      
      try{
        servImplErrors.get().add(SSErrForClient.get(error1));
      }catch(Exception error2){
        SSLogU.err(error2);
      }
    }
  }
  
  public static void regErrThrow(final Exception error) throws Exception{
    
    if(error == null){
      SSLogU.err(new Exception("error null"));
      return;
    }
    
    if(!containsErr(error.getClass())){
      SSLogU.err(error);
    }
    
    try{
      servImplErrors.get().add(SSErrForClient.get(error));
    }catch(Exception error1){
      
      SSLogU.err(error1);
      
      try{
        servImplErrors.get().add(SSErrForClient.get(error1));
      }catch(Exception error2){
        SSLogU.err(error2);
      }
    }
    
    throw error;
  }
  
  public static void logServImplErrors(){
    
    for(SSErrForClient error : servImplErrors.get()){
      
      System.err.println(
        "error: "               + SSStrU.blank   +
        error.threadWhereThrown + SSStrU.blank   +
        error.classWhereThrown  + SSStrU.blank   +
        error.methodWhereThrown + SSStrU.blank   +
        error.lineWhereThrown   + SSStrU.blank   +
        error.className         + SSStrU.blank   +
        error.message           + SSStrU.blank);
    }
    
    servImplErrors.get().clear();
  }

  public static void reset(){
   servImplErrors.get().clear();
  }
}
