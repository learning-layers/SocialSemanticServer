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
package at.tugraz.sss.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSServA implements SSServRegI{
  
  public static final SSServA inst = new SSServA();
  
  @Override
  public void regClientRequest(
    final SSServOpE     op,
    final SSUri         user,
    final SSServImplA   servImpl) throws Exception{
    
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
  
  @Override
  public void unregClientRequest(
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
  
  @Override
  public void regClientRequestLimit(
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
  
  @Override
  public void regServForUpdatingEntities(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsForUpdatingEntities){
        
        if(servsForUpdatingEntities.contains(servContainer)){
          throw new Exception("service for updating entities already registered");
        }
        
        servsForUpdatingEntities.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void regServForManagingEntities(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsForManagingEntities){
        
        if(servsForManagingEntities.contains(servContainer)){
          throw new Exception("service already registered");
        }
        
        servsForManagingEntities.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void regServForDescribingEntities(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsForDescribingEntities){
        
        if(servsForDescribingEntities.contains(servContainer)){
          throw new Exception("service for describing entities already registered");
        }
        
        servsForDescribingEntities.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void regServForGatheringUserRelations(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsForGatheringUserRelations){
        
        if(servsForGatheringUserRelations.contains(servContainer)){
          throw new Exception("service for gathering user relations already registered");
        }
        
        servsForGatheringUserRelations.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void regServForGatheringUsersResources(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsForGatheringUsersResources){
        
        if(servsForGatheringUsersResources.contains(servContainer)){
          throw new Exception("service for gathering users resources already registered");
        }
        
        servsForGatheringUsersResources.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSServContainerI regServ(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      synchronized(servs){
        
        for(SSServOpE op : servContainer.publishClientOps()){
          
          if(servs.containsKey(op)){
            throw new Exception("op for service already registered");
          }
          
          servs.put(op, servContainer);
        }
      }
      
      if(!servContainer.conf.use){
        return null;
      }
      
      regServOps(servContainer);
      
      return servContainer;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServImplA callServViaClient(
    final SSSocketCon  sSCon,
    final SSServParI   par,
    final Boolean      useCloud) throws Exception{
    
    try{
      
      final SSServContainerI     serv     = getClientServAvailableOnMachine(par);
      final SSServImplA          servImpl = serv.serv();
      
      servImpl.handleClientOp(
        serv.servImplClientInteraceClass, 
        sSCon, 
        par);
      
      return servImpl;
      
    }catch(Exception error){
      
      if(!SSServErrReg.containsErr(SSErrE.noClientServiceForOpAvailableOnMachine)){
        throw error;
      }
      
      if(useCloud){
        
        deployServNode(
          sSCon,
          par,
          getClientServAvailableOnNodes(par));
        
        return null; //TODO to be tested
      }
      
      throw error;
    }
  }
  
  @Override
  public Object callServViaServer(final SSServParI par) throws Exception{
    
    SSServContainerI serv;
    
    try{
      
      serv = servsForServerOps.get(par.getOpE());
      
      if(serv == null){
        throw new SSErr(SSErrE.notServerServiceForOpAvailable);
      }
      
      return serv.serv().handleServerOp(serv.servImplServerInteraceClass, par);
    }catch(SSErr error){
      
      switch(error.code){
        
        case notServerServiceForOpAvailable:{
          throw error;
        }
        
        default: {
          SSServErrReg.regErrThrow(error);
        }
      }
      
      return null;
    }catch(Exception error){
      throw error;
    }
  }
  
  @Override
  public List<SSServContainerI> getServsUpdatingEntities(){
    return new ArrayList<>(servsForUpdatingEntities);
  }
  
  @Override
  public List<SSServContainerI> getServsManagingEntities(){
    return new ArrayList<>(servsForManagingEntities);
  }
  
  @Override
  public List<SSServContainerI> getServsDescribingEntities(){
    return new ArrayList<>(servsForDescribingEntities);
  }
  
  @Override
  public List<SSServContainerI> getServsGatheringUserRelations(){
    return new ArrayList<>(servsForGatheringUserRelations);
  }
  
  @Override
  public List<SSServContainerI> getServsGatheringUsersResources(){
    return new ArrayList<>(servsForGatheringUsersResources);
  }
  
  private void deployServNode(
    final SSSocketCon        sSCon,
    final SSServParI         par,
    final SSServContainerI   serv) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user, par.getUserUri());
    opPars.put(SSVarU.serv, serv);
    
    sSCon.writeRetFullToClient(callServViaServer(new SSServPar(SSServOpE.cloudPublishService, opPars)), par.getOpE());
  }
  
  private SSServContainerI getClientServAvailableOnNodes(
    final SSServParI par) throws Exception{
    
    try{
      final SSServContainerI serv = servs.get(par.getOpE());
      
      if(serv == null){
        throw new SSErr(SSErrE.noClientServiceForOpAvailableOnNodes);
      }
      
      return serv;
    }catch(SSErr error){
      
      switch(error.code){
        case noClientServiceForOpAvailableOnNodes: throw error;
        default: SSServErrReg.regErrThrow(error);
      }
      
      return null;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private SSServContainerI getClientServAvailableOnMachine(
    final SSServParI par) throws Exception{
    
    try{
      final SSServContainerI serv = servsForClientOps.get(par.getOpE());
      
      if(serv == null){
        throw new SSErr(SSErrE.noClientServiceForOpAvailableOnMachine);
      }
      
      return serv;
    }catch(SSErr error){
      
      switch(error.code){
        case noClientServiceForOpAvailableOnMachine: throw error;
        default: SSServErrReg.regErrThrow(error);
      }
      
      return null;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private void regServOps(
    final SSServContainerI servContainer) throws Exception{
    try{
      
      synchronized(servsForClientOps){
        
        for(SSServOpE op : servContainer.publishClientOps()){
          
          if(servsForClientOps.containsKey(op)){
            throw new Exception("op for client service already registered");
          }
          
          servsForClientOps.put(op, servContainer);
        }
      }
      
      synchronized(servsForServerOps){
        
        for(SSServOpE op : servContainer.publishServerOps()){
          
          if(servsForServerOps.containsKey(op)){
            throw new Exception("op for server service already registered");
          }
          
          servsForServerOps.put(op, servContainer);
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
  }
}

//  public static SSServA servForEntityType(SSEntityEnum entityType) throws Exception{
//
//    if(SSObjU.isNull (entityType)){
//      SSLogU.logAndThrow(new Exception("entityType null"));
//    }
//
//    if(!servsForEntityManaging.containsKey(entityType.toString())){
//      SSLogU.logAndThrow(new Exception("no serv available for handling entity issues"));
//    }
//
//    return (SSServA) servsForEntityManaging.get(entityType.toString());
//  }

//          if(SSStrU.contains(requestBuffer.toString(), SSRegistryServ.policyFile)){
//            SSServOpE.toStr(op)"policy file serving not supported anymore"); //sScon.writeStringToClient("<?xml version=\"1.0\" ?><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"" + sScon.port + "\"/></cross-domain-policy>");
//          }



//      if(servImpl instanceof SSServImplWithDBA){
//        
//        if(((SSServImplWithDBA)servImpl).dbSQL.getActive() > ((SSServImplWithDBA)servImpl).dbSQL.getMaxActive() - 30){
//          throw new SSErr(SSErrE.maxNumDBConsReached);
//        }
//      }