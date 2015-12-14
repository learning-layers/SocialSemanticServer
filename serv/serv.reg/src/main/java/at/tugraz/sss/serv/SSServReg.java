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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSServReg{
  
  public static final SSServReg                            inst                                         = new SSServReg();
  public static final Map<SSServOpE,     SSServContainerI> servs                                        = new EnumMap<>(SSServOpE.class);
  public static final Map<SSServOpE,     SSServContainerI> servsForClientOps                            = new EnumMap<>(SSServOpE.class);
  public static final Map<SSServOpE,     SSServContainerI> servsForServerOps                            = new EnumMap<>(SSServOpE.class);
  public static final Map<Class,         SSServContainerI> servsForServerI                              = new HashMap<>();
  public static final Map<Class,         SSServContainerI> servsForClientI                              = new HashMap<>();
  public static final List<SSServContainerI>               servsForGatheringUsersResources              = new ArrayList<>();
  public static final List<SSServContainerI>               servsForGatheringUserRelations               = new ArrayList<>();
  public static final List<SSServContainerI>               servsHandlingCircleContentRemoved            = new ArrayList<>();
  public static final List<SSServContainerI>               servsHandlingEntityCopied                    = new ArrayList<>();
  public static final List<SSServContainerI>               servsHandlingGetSubEntities                  = new ArrayList<>();
  public static final List<SSServContainerI>               servsHandlingGetParentEntities               = new ArrayList<>();
  public static final List<SSServContainerI>               servsHandlingCopyEntity                      = new ArrayList<>();
  public static final List<SSServContainerI>               servsHandlingDescribeEntity                  = new ArrayList<>();
  public static final List<SSServContainerI>               servsHandlingAddAffiliatedEntitiesToCircle   = new ArrayList<>();
  public static final List<SSServContainerI>               servsHandlingPushEntitiesToUsers             = new ArrayList<>();
  public static final List<SSServContainerI>               servsHandlingEntitiesSharedWithUsers         = new ArrayList<>();
  
  public static final Map<SSServOpE, Integer>                         requsLimitsForClientOpsPerUser  = new EnumMap<>(SSServOpE.class);
  public static final Map<SSServOpE, Map<String, List<SSServImplA>>>  currentRequsForClientOpsPerUser = new EnumMap<>(SSServOpE.class);
  
  public static SSServImplA getClientServ(final Class clientServClass) throws SSErr{
    
    try{
      
      final SSServContainerI serv = servsForClientI.get(clientServClass);
      
      if(serv == null){
        throw SSErr.get(SSErrE.noClientServiceForOpAvailableOnMachine);
      }
      
      return serv.serv();
    
    }catch(SSErr error){
      
      switch(error.code){
        
        case servClientOpNotAvailable:{
          throw error;
        }
        
        default: {
          SSServErrReg.regErrThrow(error);
        }
      }
        
      return null;
      
      
//      if(!SSServErrReg.containsErr(SSErrE.noClientServiceForOpAvailableOnMachine)){
//        throw error;
//      }
      
//      if(useCloud){
//
//        deployServNode(
//          par,
//          getClientServAvailableOnNodes(par));
//
//        return null; //TODO to be tested
//      }
    }
  }
  
  public SSServRetI getAndCallClientServForSocketClient(
    final SSServPar    par) throws Exception{
    
    try{
      
      final SSServContainerI serv = servsForClientOps.get(par.op);
      
      if(serv == null){
        throw SSErr.get(SSErrE.noClientServiceForOpAvailableOnMachine);
      }
      
      final SSServImplA servImpl = serv.serv();
      
      if(requsLimitsForClientOpsPerUser.containsKey(par.op)){
        
        Map<String, List<SSServImplA>> servImplsForUser;
        List<SSServImplA>              servImpls;
        
        synchronized(currentRequsForClientOpsPerUser){
          
          if(!currentRequsForClientOpsPerUser.containsKey(par.op)){
            
            servImplsForUser = new HashMap<>();
            
            currentRequsForClientOpsPerUser.put(par.op, servImplsForUser);
          }
          
          if(currentRequsForClientOpsPerUser.get(par.op).get(SSStrU.toStr(par.user)) == null){
            currentRequsForClientOpsPerUser.get(par.op).put(SSStrU.toStr(par.user), new ArrayList<>());
          }
          
          servImpls = currentRequsForClientOpsPerUser.get(par.op).get(SSStrU.toStr(par.user));
          
          if(
            servImpls.size() == requsLimitsForClientOpsPerUser.get(par.op)){
            throw SSErr.get(SSErrE.maxNumClientConsForOpReached);
          }
          
          servImpls.add(servImpl);
        }
      }
      
      return servImpl.handleSocketeClientOp(
        serv.servImplClientInteraceClass,
        par);
      
    }catch(Exception error){
      
      if(error instanceof SSErr){
        
        switch(((SSErr)error).code){
          
          case servClientOpNotAvailable:{
            throw error;
          }
          
          default: {
            SSServErrReg.regErrThrow(error);
          }
        }
        
        return null;
      }else{
        throw error;
      }
      
//      if(!SSServErrReg.containsErr(SSErrE.noClientServiceForOpAvailableOnMachine)){
//        throw error;
//      }
      
//      if(useCloud){
//
//        deployServNode(
//          par,
//          getClientServAvailableOnNodes(par));
//
//        return null; //TODO to be tested
//      }
      
    }
  }
  
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
  
  public void regClientRequestLimit(
    final Class                   servImplClientInteraceClass,
    final Map<SSServOpE, Integer> maxRequsPerOps) throws Exception{
    
    for(Map.Entry<SSServOpE, Integer> maxRequestPerOp : maxRequsPerOps.entrySet()){
      
      try{
        servImplClientInteraceClass.getMethod(SSStrU.toStr(maxRequestPerOp.getKey()), SSClientE.class, SSServPar.class);
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
  
  public void regServForHandlingDescribeEntity(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingDescribeEntity){
        
        if(servsHandlingDescribeEntity.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingDescribeEntity.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingPushEntitiesToUsers(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingPushEntitiesToUsers){
        
        if(servsHandlingPushEntitiesToUsers.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingPushEntitiesToUsers.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingEntitiesSharedWithUsers(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingEntitiesSharedWithUsers){
        
        if(servsHandlingEntitiesSharedWithUsers.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingEntitiesSharedWithUsers.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingAddAffiliatedEntitiesToCircle(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingAddAffiliatedEntitiesToCircle){
        
        if(servsHandlingAddAffiliatedEntitiesToCircle.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingAddAffiliatedEntitiesToCircle.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingCopyEntity(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingCopyEntity){
        
        if(servsHandlingCopyEntity.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingCopyEntity.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingGetParentEntities(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingGetParentEntities){
        
        if(servsHandlingGetParentEntities.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingGetParentEntities.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingGetSubEntities(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingGetSubEntities){
        
        if(servsHandlingGetSubEntities.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingGetSubEntities.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingEntityCopied(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingEntityCopied){
        
        if(servsHandlingEntityCopied.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingEntityCopied.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForHandlingCircleContentRemoved(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsHandlingCircleContentRemoved){
        
        if(servsHandlingCircleContentRemoved.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsHandlingCircleContentRemoved.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForGatheringUserRelations(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsForGatheringUserRelations){
        
        if(servsForGatheringUserRelations.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForGatheringUserRelations.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void regServForGatheringUsersResources(
    final SSServContainerI servContainer) throws Exception{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsForGatheringUsersResources){
        
        if(servsForGatheringUsersResources.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForGatheringUsersResources.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
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
      
      regServOps       (servContainer);
      regServServerI   (servContainer);
      regServClientI   (servContainer);
      
      return servContainer;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static SSServImplA getServ(final Class servServerI) throws SSErr{
    
    SSServContainerI serv;
    
    try{
      
      serv = servsForServerI.get(servServerI);
      
      if(serv == null){
        throw SSErr.get(SSErrE.servServerOpNotAvailable);
      }
      
      return serv.serv();
      
    }catch(SSErr error){
      
      switch(error.code){
        
        case servServerOpNotAvailable:{ 
          throw error;
        }
        
        default: {
          SSServErrReg.regErrThrow(error);
        }
      }
      
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
  
  private void regServClientI(
    final SSServContainerI servContainer) throws Exception{
   
    try{
      
      synchronized(servsForClientI){
        
        if(servsForClientI.containsKey(servContainer.servImplClientInteraceClass)){
          throw new Exception("serv server interface already registered");
        }
        
        if(servContainer.servImplClientInteraceClass == null){
          SSLogU.warn("servContainer has no serv client impl");
          return;
        }
        
        servsForClientI.put(servContainer.servImplClientInteraceClass, servContainer);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void regServServerI(
    final SSServContainerI servContainer) throws Exception{
   
    try{
      
      synchronized(servsForServerI){
        
        if(servsForServerI.containsKey(servContainer.servImplServerInteraceClass)){
          throw new Exception("serv server interface already registered");
        }
        
        if(servContainer.servImplServerInteraceClass == null){
          SSLogU.warn("servContainer has no serv server impl");
          return;
        }
        
        servsForServerI.put(servContainer.servImplServerInteraceClass, servContainer);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSServContainerI> getServsHandlingAddAffiliatedEntitiesToCircle(){
    return new ArrayList<>(servsHandlingAddAffiliatedEntitiesToCircle);
  }
  
  public List<SSServContainerI> getServsHandlingEntitiesSharedWithUsers(){
    return new ArrayList<>(servsHandlingEntitiesSharedWithUsers);
  }
    
  public List<SSServContainerI> getServsHandlingPushEntitiesToUsers(){
    return new ArrayList<>(servsHandlingPushEntitiesToUsers);
  }
  
  public List<SSServContainerI> getServsHandlingDescribeEntity(){
    return new ArrayList<>(servsHandlingDescribeEntity);
  }
    
  public List<SSServContainerI> getServsHandlingCopyEntity(){
    return new ArrayList<>(servsHandlingCopyEntity);
  }
  
  public List<SSServContainerI> getServsHandlingGetParentEntities(){
    return new ArrayList<>(servsHandlingGetParentEntities);
  }
  
  public List<SSServContainerI> getServsHandlingGetSubEntities(){
    return new ArrayList<>(servsHandlingGetSubEntities);
  }
  
  public List<SSServContainerI> getServsHandlingEntityCopied(){
    return new ArrayList<>(servsHandlingEntityCopied);
  }
  
  public List<SSServContainerI> getServsHandlingCircleContentRemoved(){
    return new ArrayList<>(servsHandlingCircleContentRemoved);
  }
    
  public List<SSServContainerI> getServsGatheringUserRelations(){
    return new ArrayList<>(servsForGatheringUserRelations);
  }
  
  public List<SSServContainerI> getServsGatheringUsersResources(){
    return new ArrayList<>(servsForGatheringUsersResources);
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
//          SSServErrReg.regErrThrow(SSErr.get(SSErrE.maxNumDBConsReached);
//        }
//      }


//  private void deployServNode(
//    final SSServPar          par,
//    final SSServContainerI   serv) throws Exception{
//    
//    final Map<String, Object> opPars = new HashMap<>();
//    
//    opPars.put(SSVarNames.user, par.user);
//    opPars.put(SSVarNames.serv, serv);
//    
//    par.clientCon.writeRetFullToClient((SSServRetI) callServViaServer(new SSServPar(SSServOpE.cloudPublishService, opPars)));
//  }
  
//  private SSServContainerI getClientServAvailableOnNodes(
//    final SSServPar par) throws Exception{
//    
//    try{
//      final SSServContainerI serv = servs.get(par.op);
//      
//      if(serv == null){
//        throw SSErr.get(SSErrE.noClientServiceForOpAvailableOnNodes);
//      }
//      
//      return serv;
//    }catch(SSErr error){
//      
//      switch(error.code){
//        case noClientServiceForOpAvailableOnNodes: throw error;
//        default: SSServErrReg.regErrThrow(error);
//      }
//      
//      return null;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//  public Object callServViaServer(final SSServPar par) throws Exception{
//    
//    SSServContainerI serv;
//    
//    try{
//      
//      serv = servsForServerOps.get(par.op);
//      
//      if(serv == null){
//        throw SSErr.get(SSErrE.servServerOpNotAvailable);
//      }
//      
//      return serv.serv().handleServerOp(serv.servImplServerInteraceClass, par);
//    }catch(SSErr error){
//      
//      switch(error.code){
//        
//        case servServerOpNotAvailable:{
//          throw error;
//        }
//        
//        default: {
//          SSServErrReg.regErrThrow(error);
//        }
//      }
//      
//      return null;
//    }catch(Exception error){
//      throw error;
//    }
//  }