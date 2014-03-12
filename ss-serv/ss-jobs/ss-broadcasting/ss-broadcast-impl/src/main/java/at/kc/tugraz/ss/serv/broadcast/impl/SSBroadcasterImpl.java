/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
 package at.kc.tugraz.ss.serv.broadcast.impl;

import at.kc.tugraz.socialserver.service.broadcast.api.*;
import at.kc.tugraz.socialserver.service.broadcast.conf.*;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.SSBroadcast;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.pars.SSBroadcastUpdatePar;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.rets.SSBroadcastServerTimeRet;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.rets.SSBroadcastUpdateRet;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.rets.SSBroadcastUpdateTimeGetRet;
import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SSBroadcasterImpl extends SSServImplMiscA implements SSBroadcasterClientI, SSBroadcasterServerI{

  // TODO singleton?
  private List<SSBroadcast>             updateList  = Collections.synchronizedList(new ArrayList<SSBroadcast>());
  private int                           lifetime    = 10000;
  
  public SSBroadcasterImpl(final SSBroadcasterConf conf) throws Exception{
    super(conf);
  }
  
  /****** SSServRegisterableImplI ******/
  
  @Override
  public List<SSMethU> publishClientOps() throws Exception{
    
    List<SSMethU> clientOps = new ArrayList<SSMethU>();
      
    Method[] methods = SSBroadcasterClientI.class.getMethods();
    
    for(Method method :methods){
      clientOps.add(SSMethU.get(method.getName()));
    }
    
    return clientOps;
  }
  
  @Override
  public List<SSMethU> publishServerOps() throws Exception{
    
    List<SSMethU> serverOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSBroadcasterServerI.class.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }
    
    return serverOps;
  }
  
  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSBroadcasterClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }
  
  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSBroadcasterServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }
  
  /****** SSBroadcasterClientI ******/
  
  @Override
  public void broadcastUpdate(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSBroadcastUpdateRet.get(broadcastUpdate(par), par.op));
  }

  @Override
  public void broadcastUpdateTimeGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSBroadcastUpdateTimeGetRet.get(broadcastUpdateTimeGet(par), par.op));
  }

  @Override
  public void broadcastServerTime(SSSocketCon sSCon, SSServPar par) throws Exception {

    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSBroadcastServerTimeRet.get(broadcastServerTime(par), par.op));
  }
  
  /****** SSBroadcasterServerI ******/

  @Override
  public boolean broadcastUpdate(SSServPar parI) throws Exception {
    
    SSBroadcastUpdatePar par = new SSBroadcastUpdatePar(parI);
    
    updateList.add(SSBroadcast.get(par.type, par.resource, par.user));
    
    return true;
  }

  @Override
  public List<SSBroadcast> broadcastUpdateTimeGet(SSServPar par) throws Exception {
    
    long              currentTimeMillis = System.currentTimeMillis();
    List<SSBroadcast> objectArray;
      
    for (int i = 0; i < updateList.size(); i++) {
      if (updateList.get(i).timestamp < currentTimeMillis - lifetime) {
        updateList.remove(updateList.get(i));
        i--;
      }
    }
    
    objectArray  = new ArrayList<SSBroadcast>(updateList.size());
    
    for (SSBroadcast object : updateList) {
      objectArray.add(object);
    }

    return objectArray;
  }

  @Override
  public Long broadcastServerTime(SSServPar par) throws Exception {
    return SSDateU.dateAsNano();
  }
}