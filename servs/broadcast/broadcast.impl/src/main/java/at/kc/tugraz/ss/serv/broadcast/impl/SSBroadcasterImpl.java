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
package at.kc.tugraz.ss.serv.broadcast.impl;

import at.kc.tugraz.socialserver.service.broadcast.api.*;
import at.kc.tugraz.socialserver.service.broadcast.conf.*;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.SSBroadcast;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.pars.SSBroadcastAddPar;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.rets.SSBroadcastServerTimeRet;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.rets.SSBroadcastAddRet;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.rets.SSBroadcastsGetRet;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSServPar;

import at.tugraz.sss.serv.SSServImplMiscA;
import at.tugraz.sss.serv.caller.SSServCallerU;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SSBroadcasterImpl extends SSServImplMiscA implements SSBroadcasterClientI, SSBroadcasterServerI{

  private static final List<SSBroadcast>      updateList     = new ArrayList<>();
  private static final ReentrantReadWriteLock updateListLock = new ReentrantReadWriteLock();
  
  public SSBroadcasterImpl(final SSBroadcasterConf conf) throws Exception{
    super(conf);
  }
  
  @Override
  public void broadcastAdd(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSBroadcastAddRet.get(broadcastAdd(parA), parA.op));
  }

  @Override
  public void broadcastsGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSBroadcastsGetRet.get(broadcastsGet(parA), parA.op));
  }

  @Override
  public void broadcastServerTime(SSSocketCon sSCon, SSServPar parA) throws Exception {

    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSBroadcastServerTimeRet.get(broadcastServerTime(parA), parA.op));
  }
  
  @Override
  public Boolean broadcastAdd(final SSServPar parA) throws Exception {
    
    try{
      final SSBroadcastAddPar par = new SSBroadcastAddPar(parA);
      
      updateListLock.writeLock().lock();
      
      updateList.add(
        SSBroadcast.get(
          par.type, 
          par.entity, 
          par.user,
          par.content));
      
      return true;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      updateListLock.writeLock().unlock();
    }
  }
  
  @Override
  public void broadcastUpdate(final SSServPar parA) throws Exception {
    
    try{
      final long currentTime = SSDateU.dateAsLong();
      
      updateListLock.writeLock().lock();
      
      for(int counter = updateList.size() - 1; counter >= 0; counter--){
        
        if(updateList.get(counter).timestamp < currentTime - (SSDateU.minuteInMilliSeconds * 4 /*lifeTime*/)) {
          updateList.remove(updateList.get(counter));
//          counter--;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      updateListLock.writeLock().unlock();
    }
  }

  @Override
  public List<SSBroadcast> broadcastsGet(final SSServPar parA) throws Exception {
    
    try{
      
      updateListLock.readLock().lock();
       
      return new ArrayList<>(updateList);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      updateListLock.readLock().unlock();
    }
  }

  @Override
  public Long broadcastServerTime(SSServPar parA) throws Exception {
    return SSDateU.dateAsNano();
  }
}