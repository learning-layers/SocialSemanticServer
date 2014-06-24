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
package at.kc.tugraz.ss.serv.serv.api;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.conf.api.SSCoreConfA;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.datatypes.SSServRetI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.datatypes.err.SSClientServForOpNotAvailableOnMachineErr;
import at.kc.tugraz.ss.serv.serv.datatypes.err.SSClientServForOpNotAvailableOnNodesErr;
import at.kc.tugraz.ss.serv.serv.datatypes.err.SSServerServNotAvailableErr;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SSServA{

  public    static final String                          policyFile                    = "<policy-file-request/>";
  public                 SSConfA                         servConf                      = null;
  protected        final Class                           servImplClientInteraceClass;
  protected        final Class                           servImplServerInteraceClass;
  protected              Exception                       servImplCreationError         = null;
  private   static final Map<SSMethU, SSServA>           servs                         = new EnumMap<>(SSMethU.class);
  private   static final Map<SSMethU, SSServA>           servsForClientOps             = new EnumMap<>(SSMethU.class);
  private   static final Map<SSMethU, SSServA>           servsForServerOps             = new EnumMap<>(SSMethU.class);
  private   static final Map<SSEntityE, SSServA>         servsForManagingEntities      = new EnumMap<>(SSEntityE.class);
  private   static final List<SSServA>                   servsForDescribingEntities    = new ArrayList<>();
 
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
  protected abstract void         initServSpecificStuff     () throws Exception;
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
  
  private void regServ() throws Exception{
    
    try{
      
      synchronized(servs){
        
        for(SSMethU op : this.publishClientOps()){
          
          if(servs.containsKey(op)){
            throw new Exception("op for service already registered");
          }
          
          servs.put(op, this);
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void regServOps() throws Exception{

    try{
      
      synchronized(servsForServerOps){
        
        for(SSMethU op : this.publishClientOps()){
          
          if(servsForClientOps.containsKey(op)){
            throw new Exception("op for client service already registered");
          }
          
          servsForClientOps.put(op, this);
        }
        
        for(SSMethU op : this.publishServerOps()){
          
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
  
  protected void regServForManagingEntities(final List<SSEntityE> entityTypes) throws Exception{
    
    try{
     
      if(!servConf.use){
        return;
      }
      
      synchronized(servsForManagingEntities){

        for(SSEntityE entityType : entityTypes){
         
          if(servsForManagingEntities.containsKey(entityType)){
            throw new Exception("entityType for service already registered");
          }

          servsForManagingEntities.put(entityType, this);
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected void regServForManagingEntities(final SSEntityE entityType) throws Exception{
    
    try{
     
      if(!servConf.use){
        return;
      }

      if(entityType == null){
        throw new Exception("entityType is null");
      }
      
      synchronized(servsForManagingEntities){
        
        if(servsForManagingEntities.containsKey(entityType)){
          throw new Exception("entityType for service already registered");
        }
        
        servsForManagingEntities.put(entityType, this);
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
  
  public synchronized SSServA initServ(
    final SSConfA conf) throws Exception{
    
    try{
      
      this.servConf = conf;
      
      regServ();
      
      if(!servConf.use){
        return null;
      }
      
      regServOps           ();
      initServSpecificStuff();
      
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
      
      try{
        
        final SSServA serv = getClientServAvailableOnMachine(parA);
        
        serv.serv().handleClientOp(serv.servImplClientInteraceClass, sSCon, parA);
        
      }catch(SSClientServForOpNotAvailableOnMachineErr error){

        if(useCloud){
          
          deployServNode(
            sSCon,
            parA, 
            getClientServAvailableOnNodes(parA));
          
          return;
        }
        
        throw error;
      }    
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private static void deployServNode(
    final SSSocketCon sSCon, 
    final SSServPar   parA,
    final SSServA     serv) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarU.user, parA.user);
    opPars.put(SSVarU.serv, serv);
    
    sSCon.writeRetFullToClient((SSServRetI) callServViaServer(new SSServPar(SSMethU.cloudPublishService, opPars)), false);
  }
  
  private static SSServA getClientServAvailableOnNodes(
    final SSServPar par) throws Exception{
    
    try{
      final SSServA serv = servs.get(par.op);
      
      if(serv == null){
        throw new SSClientServForOpNotAvailableOnNodesErr(SSMethU.toStr(par.op));
      }
      
      return serv;
    }catch(SSClientServForOpNotAvailableOnNodesErr error){
      throw error;
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
        throw new SSClientServForOpNotAvailableOnMachineErr(SSMethU.toStr(par.op));
      }
      
      return serv;
    }catch(SSClientServForOpNotAvailableOnMachineErr error){
      throw error;
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
        throw new SSServerServNotAvailableErr(SSMethU.toStr(par.op));
      }

      return serv.serv().handleServerOp(serv.servImplServerInteraceClass, par);
    }catch(SSServerServNotAvailableErr error){
      throw error;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static List<SSServA> getServsManagingEntities(){
    return new ArrayList<>(servsForManagingEntities.values());
  }
  
  public static List<SSServA> getServsDescribingEntities(){
    return new ArrayList<>(servsForDescribingEntities);
  }
  
  private List<SSMethU> publishClientOps() throws Exception{
    
    final List<SSMethU> clientOps = new ArrayList<>();
    
    if(servImplClientInteraceClass == null){
      return clientOps;
    }
    
    final Method[]      methods   = servImplClientInteraceClass.getMethods();
    
    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }
    
    return clientOps;
  }
  
  private List<SSMethU> publishServerOps() throws Exception{
    
    final List<SSMethU> serverOps = new ArrayList<>();
    
    if(servImplServerInteraceClass == null){
      return serverOps;
    }
    
    final Method[]      methods   = servImplServerInteraceClass.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
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
//            SSMethU.toStr(op)"policy file serving not supported anymore"); //sScon.writeStringToClient("<?xml version=\"1.0\" ?><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"" + sScon.port + "\"/></cross-domain-policy>");
//          }
