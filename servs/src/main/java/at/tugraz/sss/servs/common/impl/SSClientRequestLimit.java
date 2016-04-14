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

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.common.api.SSServImplA;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSClientE;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import java.util.*;

public class SSClientRequestLimit {
  
  protected static final Map<String, Integer>                         requsLimitsForClientOpsPerUser  = new HashMap<>();
  protected static final Map<String, Map<String, List<SSServImplA>>>  currentRequsForClientOpsPerUser = new HashMap<>();

  public void clear() {
    requsLimitsForClientOpsPerUser.clear();
    currentRequsForClientOpsPerUser.clear();
  }
  
  public void regClientRequest(
    final SSUri       user, 
    final SSServImplA servImpl,
    final String      op) throws SSErr{
    
    try{
    
      if(!requsLimitsForClientOpsPerUser.containsKey(op)){
        return;
      }
        
      Map<String, List<SSServImplA>> servImplsForUser;
      List<SSServImplA>              servImpls;
      
      synchronized(currentRequsForClientOpsPerUser){
        
        if(!currentRequsForClientOpsPerUser.containsKey(op)){
          
          servImplsForUser = new HashMap<>();
          
          currentRequsForClientOpsPerUser.put(op, servImplsForUser);
        }
        
        if(currentRequsForClientOpsPerUser.get(op).get(SSStrU.toStr(user)) == null){
          currentRequsForClientOpsPerUser.get(op).put(SSStrU.toStr(user), new ArrayList<>());
        }
        
        servImpls = currentRequsForClientOpsPerUser.get(op).get(SSStrU.toStr(user));
        
        if(
          servImpls.size() == requsLimitsForClientOpsPerUser.get(op)){
          throw SSErr.get(SSErrE.maxNumClientConsForOpReached);
        }
        
        servImpls.add(servImpl);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void unregClientRequest(
    final String        op,
    final SSUri         user,
    final SSServImplA   servImpl) throws SSErr{
    
    try{
      if(!requsLimitsForClientOpsPerUser.containsKey(op)){
        return;
      }
      
      synchronized(currentRequsForClientOpsPerUser){
        
        if(
          !currentRequsForClientOpsPerUser.containsKey(op) ||
          currentRequsForClientOpsPerUser.get(op).isEmpty() ||
          currentRequsForClientOpsPerUser.get(op).get(SSStrU.toStr(user)).isEmpty()){
          return;
        }
        
        currentRequsForClientOpsPerUser.get(op).get(SSStrU.toStr(user)).remove(servImpl);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regClientRequestLimit(
    final Class                servImplClientInteraceClass,
    final Map<String, Integer> maxRequsPerOps) throws SSErr{
    
    for(Map.Entry<String, Integer> maxRequestPerOp : maxRequsPerOps.entrySet()){
      
      try{
        servImplClientInteraceClass.getMethod(SSStrU.toStr(maxRequestPerOp.getKey()), SSClientE.class, SSServPar.class);
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
        return;
      }
      
      if(requsLimitsForClientOpsPerUser.containsKey(maxRequestPerOp.getKey())){
        SSServErrReg.regErrThrow(new Exception("client operation already registered for this service"));
        return;
      }
      
      requsLimitsForClientOpsPerUser.put(maxRequestPerOp.getKey(), maxRequestPerOp.getValue());
    }
  }
}
