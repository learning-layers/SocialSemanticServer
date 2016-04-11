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
package at.tugraz.sss.serv.reg;

import at.tugraz.sss.serv.conf.*;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.impl.api.SSServImplA;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.requestlimit.*;
import java.util.*;

public class SSServReg{

  public static final SSServReg                            inst                                            = new SSServReg();
  
  protected static final Map<String,        SSServContainerI> servsForClientOps                            = new HashMap<>();
  protected static final Map<Class,         SSServContainerI> servsForServerI                              = new HashMap<>();
  protected static final Map<Class,         SSServContainerI> servsForClientI                              = new HashMap<>();
  protected static final SSSchedules                          schedules                                    = new SSSchedules();
  protected static final SSClientRequestLimit                 clientRequestLimit                           = new SSClientRequestLimit();
    
  public static void destroy() throws SSErr{

    schedules.clear();
    
    servsForClientOps.clear();
    servsForServerI.clear();
    servsForClientI.clear();
    
    clientRequestLimit.clear();
  }
  
  public static SSServImplA getClientServ(final Class clientServClass) throws SSErr{
    
    try{
      
      final SSServContainerI serv = servsForClientI.get(clientServClass);
      
      if(serv == null){
        throw SSErr.get(SSErrE.servInvalid);
      }
      
      return serv.getServImpl();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSServContainerI getClientServContainer(
    final String op) throws SSErr{
    
    try{
      
      final SSServContainerI serv = servsForClientOps.get(op);
      
      if(serv == null){
        throw SSErr.get(SSErrE.servInvalid);
      }
      
      return serv;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
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
//        return null;
//      }
  }
      
  public SSServContainerI regServ(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
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
        throw SSErr.get(SSErrE.servInvalid);
      }
      
      return serv.getServImpl();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private void regServOps(
    final SSServContainerI servContainer) throws SSErr{
   
    try{
      
      synchronized(servsForClientOps){
        
        for(String op : servContainer.publishClientOps()){
          
          if(servsForClientOps.containsKey(op)){
            throw new Exception("op for client service already registered");
          }
          
          servsForClientOps.put(op, servContainer);
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void regServClientI(
    final SSServContainerI servContainer) throws SSErr{
   
    try{
      
      synchronized(servsForClientI){
        
        if(servsForClientI.containsKey(servContainer.servImplClientInteraceClass)){
          throw new Exception("serv server interface already registered");
        }
        
        if(servContainer.servImplClientInteraceClass == null){
          SSLogU.warn("service container has no service client interface", null);
          return;
        }
        
        servsForClientI.put(servContainer.servImplClientInteraceClass, servContainer);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void regServServerI(
    final SSServContainerI servContainer) throws SSErr{
   
    try{
      
      synchronized(servsForServerI){
        
        if(servsForServerI.containsKey(servContainer.servImplServerInteraceClass)){
          throw new Exception("serv server interface already registered");
        }
        
        if(servContainer.servImplServerInteraceClass == null){
          throw SSErr.get(SSErrE.servContainerHasNoServerInterface);
        }
        
        servsForServerI.put(servContainer.servImplServerInteraceClass, servContainer);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}

//  public List<SSServContainerI> getServsHandlingAddAffiliatedEntitiesToCircle(){
//    return new ArrayList<>(servsHandlingAddAffiliatedEntitiesToCircle);
//  }
//  
//  public List<SSServContainerI> getServsHandlingEntitiesSharedWithUsers(){
//    return new ArrayList<>(servsHandlingEntitiesSharedWithUsers);
//  }
//    
//  public List<SSServContainerI> getServsHandlingPushEntitiesToUsers(){
//    return new ArrayList<>(servsHandlingPushEntitiesToUsers);
//  }
//  
//  public List<SSServContainerI> getServsHandlingDescribeEntity(){
//    return new ArrayList<>(servsHandlingDescribeEntity);
//  }
//    
//  public List<SSServContainerI> getServsHandlingCopyEntity(){
//    return new ArrayList<>(servsHandlingCopyEntity);
//  }
//  
//  public List<SSServContainerI> getServsHandlingGetParentEntities(){
//    return new ArrayList<>(servsHandlingGetParentEntities);
//  }
//  
//  public List<SSServContainerI> getServsHandlingGetSubEntities(){
//    return new ArrayList<>(servsHandlingGetSubEntities);
//  }
//  
//  public List<SSServContainerI> getServsHandlingEntityCopied(){
//    return new ArrayList<>(servsHandlingEntityCopied);
//  }
//  
//  public List<SSServContainerI> getServsHandlingCircleContentRemoved(){
//    return new ArrayList<>(servsHandlingCircleContentRemoved);
//  }
//    
//  public List<SSServContainerI> getServsGatheringUserRelations(){
//    return new ArrayList<>(servsForGatheringUserRelations);
//  }
//  
//  public List<SSServContainerI> getServsGatheringUsersResources(){
//    return new ArrayList<>(servsForGatheringUsersResources);
//  }

//  public static SSServA servForEntityType(SSEntityEnum entityType) throws SSErr{
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
//            SSVarNames.toStr(op)"policy file serving not supported anymore"); //sScon.writeStringToClient("<?xml version=\"1.0\" ?><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"" + sScon.port + "\"/></cross-domain-policy>");
//          }



//      if(servImpl instanceof SSServImplWithDBA){
//
//        if(((SSServImplWithDBA)servImpl).dbSQL.getActive() > ((SSServImplWithDBA)servImpl).dbSQL.getMaxActive() - 30){
//          SSServErrReg.regErrThrow(SSErr.get(SSErrE.maxNumDBConsReached);
//        }
//      }


//  private void deployServNode(
//    final SSServPar          par,
//    final SSServContainerI   serv) throws SSErr{
//    
//    final Map<String, Object> opPars = new HashMap<>();
//    
//    opPars.put(SSVarNames.user, par.user);
//    opPars.put(SSVarNames.serv, serv);
//    
//    par.clientCon.writeRetFullToClient((SSServRetI) callServViaServer(new SSServPar(SSVarNames.cloudPublishService, opPars)));
//  }
  
//  private SSServContainerI getClientServAvailableOnNodes(
//    final SSServPar par) throws SSErr{
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