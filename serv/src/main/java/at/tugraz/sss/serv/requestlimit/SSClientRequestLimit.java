/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.sss.serv.requestlimit;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.impl.api.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
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
