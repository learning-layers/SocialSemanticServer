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
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.datatypes.err.SSClientServNotAvailableErr;
import at.kc.tugraz.ss.serv.serv.datatypes.err.SSServerServNotAvailableErr;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class SSServA{

  public    static final String                          policyFile                = "<policy-file-request/>";
  public                 SSServConfA                         servConf                  = null;
  protected              Exception                       servImplCreationError     = null;
  private   static final Map<SSMethU, SSServA>           servsForClientCalls       = new EnumMap<SSMethU,      SSServA>(SSMethU.class);
  private   static final Map<SSMethU, SSServA>           servsForServerCalls       = new EnumMap<SSMethU,      SSServA>(SSMethU.class);
  private   static final Map<SSEntityEnum, SSServA>      servsForManagingEntities  = new EnumMap<SSEntityEnum, SSServA>(SSEntityEnum.class);
 
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
  
  protected abstract SSServImplA createServImplForThread() throws Exception;
  protected abstract void        initServSpecificStuff()   throws Exception;
   
  private void regServOps() throws Exception{

    SSServImplA servImpl;
    
    try{
      
      servImpl = serv();
      
      synchronized(servsForServerCalls){
        
        for(SSMethU op : servImpl.publishClientOps()){
          
          if(servsForClientCalls.containsKey(op)){
            SSServErrReg.regErrThrow(new Exception("op for client service already registered"));
            return;
          }
          
          servsForClientCalls.put(op, this);
        }
        
        for(SSMethU op : servImpl.publishServerOps()){
          
          if(servsForServerCalls.containsKey(op)){
            SSServErrReg.regErrThrow(new Exception("op for server service already registered"));
            return;
          }
          
          servsForServerCalls.put(op, this);
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected void regServForManagingEntities(final SSEntityEnum entityType) throws Exception{
    
    if(!servConf.use){
      return;
    }
    
    if(entityType == null){
      SSServErrReg.regErrThrow(new Exception("entityType is null"));
      return;
    }
    
    synchronized(servsForManagingEntities){
      
      if(servsForManagingEntities.containsKey(entityType)){
        SSServErrReg.regErrThrow(new Exception("entityType for service already registered"));
        return;
      }
      
      servsForManagingEntities.put(entityType, this);
    }
  }
  
  private Boolean shallServBeUsed() throws Exception{
    
    try{
      if(servConf == null){
        throw new Exception("servConf null");
      }

      return servConf.use;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public synchronized SSServA initServ(SSServConfA conf) throws Exception{
    
    this.servConf = conf;
    
    try{
      
      if(!shallServBeUsed()){
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
    
    if(!shallServBeUsed()){
      return null;
    }
    
    SSServImplA servTmp;
    
    try{
      servTmp = servImplsByServByThread.get();

      if(servImplCreationError != null){
        SSServErrReg.regErrThrow(servImplCreationError);
        return null;
      }

      SSServImplStartA.regServImplUsedByThread (servTmp);
      
      return servTmp;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static void callServViaClient(final SSSocketCon sSCon, final SSServPar par) throws Exception{
    
    SSServA serv;
    
    try{
      
      serv = servsForClientCalls.get(par.op);
      
      if(serv == null){
        SSServErrReg.regErrThrow(new SSClientServNotAvailableErr(SSMethU.toStr(par.op)));
        return;
      }

      serv.serv().handleClientOp(sSCon, par);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static Object callServViaServer(final SSServPar par) throws Exception{
    
    SSServA serv;
    
    try{

      serv = servsForServerCalls.get(par.op);
      
      if(serv == null){
        SSServErrReg.regErrThrow(new SSServerServNotAvailableErr(SSMethU.toStr(par.op)));
        return null;
      }

      return serv.serv().handleServerOp(par);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static List<SSServA> getServsManagingEntities(){
    return new ArrayList<SSServA>(servsForManagingEntities.values());
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
