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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSServClientOpRequsTracker{

  private static final Map<SSServOpE, Integer>                         requsLimitsForClientOpsPerUser  = new EnumMap<>(SSServOpE.class);
  private static final Map<SSServOpE, Map<String, List<SSServImplA>>>  currentRequsForClientOpsPerUser = new EnumMap<>(SSServOpE.class);
  
  public static void addClientRequ(
    final SSServOpE   op,
    final SSUri       user,
    final SSServImplA servImpl) throws Exception{
    
    if(!requsLimitsForClientOpsPerUser.containsKey(op)){
      return;
    }
    
    Map<String, List<SSServImplA>> servImplsForUser;
    List<SSServImplA>              servImpls;
    
    synchronized(currentRequsForClientOpsPerUser){
      
      if(
        !currentRequsForClientOpsPerUser.containsKey(op) ||
        currentRequsForClientOpsPerUser.get(op).get(SSStrU.toStr(user)) == null){
        
        servImplsForUser = new HashMap<>();
        servImpls        = new ArrayList<>();
        
        servImplsForUser.put(SSStrU.toStr(user), servImpls);
        
        currentRequsForClientOpsPerUser.put(op, servImplsForUser);
      }else{
        
        servImpls = currentRequsForClientOpsPerUser.get(op).get(SSStrU.toStr(user));
        
        if(
          servImpls.size() == requsLimitsForClientOpsPerUser.get(op)){
          throw new SSErr(SSErrE.maxNumClientConsForOpReached);
        }
      }
      
      servImpls.add(servImpl);
    }
  }
  
  public static void removeClientRequ(
    final SSServOpE     op,
    final SSUri         user,
    final SSServImplA   servImpl) throws Exception{
    
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
  
  public static void regClientRequestLimit(
    final Class                   servImplClientInteraceClass,
    final Map<SSServOpE, Integer> maxRequsPerOps) throws Exception{
    
    for(Map.Entry<SSServOpE, Integer> maxRequestPerOp : maxRequsPerOps.entrySet()){
      
      try{
        servImplClientInteraceClass.getMethod(SSStrU.toStr(maxRequestPerOp.getKey()));
      }catch(Exception error){
        SSServErrReg.regErrThrow(new Exception("client operation to register not available for this service"));
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