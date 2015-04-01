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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SSServA{
  
  public                 SSConfA                                      servConf                        = null;
  protected        final Class                                        servImplClientInteraceClass;
  protected        final Class                                        servImplServerInteraceClass;
  protected              Exception                                    servImplCreationError           = null;
  private   static final Map<SSServOpE, SSServA>                        servs                           = new EnumMap<>(SSServOpE.class);
  private   static final Map<SSServOpE, SSServA>                        servsForClientOps               = new EnumMap<>(SSServOpE.class);
  private   static final Map<SSServOpE, SSServA>                        servsForServerOps               = new EnumMap<>(SSServOpE.class);
  private   static final List<SSServA>                                servsForUpdatingEntities        = new ArrayList<>();
  private   static final List<SSServA>                                servsForManagingEntities        = new ArrayList<>();
  private   static final List<SSServA>                                servsForDescribingEntities      = new ArrayList<>();
  private   static final List<SSServA>                                servsForGatheringUserRelations  = new ArrayList<>();
  private   static final List<SSServA>                                servsForGatheringUsersResources = new ArrayList<>();
  protected static final Map<SSServOpE, Integer>                        maxRequsForClientOpsPerUser     = new HashMap<>();
  private   static final Map<SSServOpE, Map<String, List<SSServImplA>>> actualRequsForClientOpsForUser  = new HashMap<>();
  
  private static void addClientRequ(
    final SSServOpE     op,
    final String      user,
    final SSServImplA servImpl) throws Exception{
    
    try{
      if(servImpl instanceof SSServImplWithDBA){
        
        if(((SSServImplWithDBA)servImpl).dbSQL.getActive() > ((SSServImplWithDBA)servImpl).dbSQL.getMaxActive() - 30){
          throw new SSErr(SSErrE.maxNumDBConsReached);
        }
      }
      
      if(!maxRequsForClientOpsPerUser.containsKey(op)){
        return;
      }
      
      Map<String, List<SSServImplA>> servImplsForUser;
      List<SSServImplA>              servImpls;
      
      synchronized(actualRequsForClientOpsForUser){
        
        if(
          !actualRequsForClientOpsForUser.containsKey(op) ||
          actualRequsForClientOpsForUser.get(op).get(user) == null){
          
          servImplsForUser = new HashMap<>();
          servImpls        = new ArrayList<>();
          
          servImplsForUser.put(user, servImpls);
          
          actualRequsForClientOpsForUser.put(op, servImplsForUser);
        }else{
          
          servImpls = actualRequsForClientOpsForUser.get(op).get(user);
          
          if(
            servImpls.size() == maxRequsForClientOpsPerUser.get(op)){
            throw new SSErr(SSErrE.maxNumClientConsForOpReached);
          }
        }
        
        servImpls.add(servImpl);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void removeClientRequ(
    final SSServOpE     op,
    final String      user,
    final SSServImplA servImpl) throws Exception{
    
    try{
      if(!maxRequsForClientOpsPerUser.containsKey(op)){
        return;
      }
      
      synchronized(actualRequsForClientOpsForUser){
        
        if(
          !actualRequsForClientOpsForUser.containsKey(op) ||
          actualRequsForClientOpsForUser.get(op).isEmpty() ||
          actualRequsForClientOpsForUser.get(op).get(user).isEmpty()){
          return;
        }
        
        actualRequsForClientOpsForUser.get(op).get(user).remove(servImpl);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected SSServA(
    final Class servImplClientInteraceClass,
    final Class servImplServerInteraceClass){
    
    this.servImplClientInteraceClass = servImplClientInteraceClass;
    this.servImplServerInteraceClass = servImplServerInteraceClass;
  }
  
  private final ThreadLocal<SSServImplA> servImplsByServByThread = new ThreadLocal<SSServImplA>(){
    
    @Override protected SSServImplA initialValue() {
      
      try{
        return createServImplForThread();
      }catch (Exception error){
        SSServErrReg.regErr(error);
        servImplCreationError = error;
        return null;
      }
    }
  };
  
  protected abstract SSServImplA  createServImplForThread   () throws Exception;
  public    abstract void         initServ                  () throws Exception;
  public    abstract void         schedule                  () throws Exception;
  public    abstract SSCoreConfA  getConfForCloudDeployment (final SSCoreConfA coreConfA, final List<Class> configuredServs) throws Exception;
  
  protected SSCoreConfA getConfForCloudDeployment(
    final Class       servI,
    final SSCoreConfA coreConf,
    final List<Class> configuredServs) throws Exception{
    
    if(configuredServs.contains(servI)){
      return coreConf;
    }
    
    for(SSServA serv : servs.values()){
      
      if(servI.isInstance(serv)){
        
        configuredServs.add(servI);
        
        return serv.getConfForCloudDeployment(coreConf, configuredServs);
      }
    }
    
    throw new Exception("service not registered");
  }
  
  private void regServOps() throws Exception{
    
    try{
      
      synchronized(servsForClientOps){
        
        for(SSServOpE op : this.publishClientOps()){
          
          if(servsForClientOps.containsKey(op)){
            throw new Exception("op for client service already registered");
          }
          
          servsForClientOps.put(op, this);
        }
      }
      
      synchronized(servsForServerOps){
        
        for(SSServOpE op : this.publishServerOps()){
          
          if(servsForServerOps.containsKey(op)){
            throw new Exception("op for server service already registered");
          }
          
          servsForServerOps.put(op, this);
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected void regServForUpdatingEntities() throws Exception{
    
    try{
      
      if(!servConf.use){
        return;
      }
      
      synchronized(servsForUpdatingEntities){
        
        if(servsForUpdatingEntities.contains(this)){
          throw new Exception("service for updating entities already registered");
        }
        
        servsForUpdatingEntities.add(this);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected void regServForManagingEntities() throws Exception{
    
    try{
      
      if(!servConf.use){
        return;
      }
      
      synchronized(servsForManagingEntities){
        
        if(servsForManagingEntities.contains(this)){
          throw new Exception("service already registered");
        }
        
        servsForManagingEntities.add(this);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected void regServForDescribingEntities() throws Exception{
    
    try{
      
      if(!servConf.use){
        return;
      }
      
      synchronized(servsForDescribingEntities){
        
        if(servsForDescribingEntities.contains(this)){
          throw new Exception("service for describing entities already registered");
        }
        
        servsForDescribingEntities.add(this);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected void regServForGatheringUserRelations() throws Exception{
    
    try{
      
      if(!servConf.use){
        return;
      }
      
      synchronized(servsForGatheringUserRelations){
        
        if(servsForGatheringUserRelations.contains(this)){
          throw new Exception("service for gathering user relations already registered");
        }
        
        servsForGatheringUserRelations.add(this);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected void regServForGatheringUsersResources() throws Exception{
    
    try{
      
      if(!servConf.use){
        return;
      }
      
      synchronized(servsForGatheringUsersResources){
        
        if(servsForGatheringUsersResources.contains(this)){
          throw new Exception("service for gathering users resources already registered");
        }
        
        servsForGatheringUsersResources.add(this);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public SSServA regServ(
    final SSConfA conf) throws Exception{
    
    try{
      
      this.servConf = conf;
      
      synchronized(servs){
        
        for(SSServOpE op : this.publishClientOps()){
          
          if(servs.containsKey(op)){
            throw new Exception("op for service already registered");
          }
          
          servs.put(op, this);
        }
      }
      
      if(!servConf.use){
        return null;
      }
      
      regServOps();
      
      return this;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSServImplA serv() throws Exception{
    
    try{
      
      if(!servConf.use){
        return null;
      }
      
      final SSServImplA servTmp = servImplsByServByThread.get();
      
      if(servImplCreationError != null){
        throw servImplCreationError;
      }
      
      SSServImplStartA.regServImplUsedByThread (servTmp);
      
      return servTmp;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static void callServViaClient(
    final SSSocketCon sSCon,
    final SSServPar   parA,
    final Boolean     useCloud) throws Exception{
    
    try{
      
      final SSServA     serv     = getClientServAvailableOnMachine(parA);
      final SSServImplA servImpl = serv.serv();
      
      addClientRequ(parA.op, SSStrU.toStr(parA.user), servImpl);
      
      servImpl.handleClientOp(serv.servImplClientInteraceClass, sSCon, parA);
      
    }catch(Exception error){
      
      if(!SSServErrReg.containsErr(SSErrE.noClientServiceForOpAvailableOnMachine)){
        throw error;
      }
      
      if(useCloud){
        
        deployServNode(
          sSCon,
          parA,
          getClientServAvailableOnNodes(parA));
        
        return;
      }
      
      throw error;
    }
  }
  
  private static void deployServNode(
    final SSSocketCon sSCon,
    final SSServPar   parA,
    final SSServA     serv) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user, parA.user);
    opPars.put(SSVarU.serv, serv);
    
    sSCon.writeRetFullToClient((SSServRetI) callServViaServer(new SSServPar(SSServOpE.cloudPublishService, opPars)));
  }
  
  private static SSServA getClientServAvailableOnNodes(
    final SSServPar par) throws Exception{
    
    try{
      final SSServA serv = servs.get(par.op);
      
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
  
  private static SSServA getClientServAvailableOnMachine(
    final SSServPar par) throws Exception{
    
    try{
      final SSServA serv = servsForClientOps.get(par.op);
      
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
  
  public static Object callServViaServer(final SSServPar par) throws Exception{
    
    SSServA serv;
    
    try{
      
      serv = servsForServerOps.get(par.op);
      
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
  
  public static List<SSServA> getServsUpdatingEntities(){
    return new ArrayList<>(servsForUpdatingEntities);
  }
  
  public static List<SSServA> getServsManagingEntities(){
    return new ArrayList<>(servsForManagingEntities);
  }
  
  public static List<SSServA> getServsDescribingEntities(){
    return new ArrayList<>(servsForDescribingEntities);
  }
  
  public static List<SSServA> getServsGatheringUserRelations(){
    return new ArrayList<>(servsForGatheringUserRelations);
  }
  
  public static List<SSServA> getServsGatheringUsersResources(){
    return new ArrayList<>(servsForGatheringUsersResources);
  }
  
  private List<SSServOpE> publishClientOps() throws Exception{
    
    final List<SSServOpE> clientOps = new ArrayList<>();
    
    if(servImplClientInteraceClass == null){
      return clientOps;
    }
    
    final Method[]      methods   = servImplClientInteraceClass.getMethods();
    
    for(Method method : methods){
      clientOps.add(SSServOpE.get(method.getName()));
    }
    
    return clientOps;
  }
  
  private List<SSServOpE> publishServerOps() throws Exception{
    
    final List<SSServOpE> serverOps = new ArrayList<>();
    
    if(servImplServerInteraceClass == null){
      return serverOps;
    }
    
    final Method[]      methods   = servImplServerInteraceClass.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSServOpE.get(method.getName()));
    }
    
    return serverOps;
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
