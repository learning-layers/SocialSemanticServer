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
package at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.access;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleIsEntitySharedPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.conf.SSLearnEpConf;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;

public class SSLearnEpAccessController{
  
  private static final Map<String, String>       lockedLearnEps               = new HashMap<>(); //learnEp, user
  private static final Map<String, Long>         lockedLearnEpsLockTimes      = new HashMap<>(); //learnEp, time the episode got locked
  private static final ReentrantReadWriteLock    learnEpsLock                 = new ReentrantReadWriteLock();
  
  public static Boolean lock(
    final SSUri         user,
    final SSUri         learnEp) throws Exception{
    
    try{
      
      learnEpsLock.writeLock().lock();
      
      if(
        lockedLearnEps.containsKey(SSStrU.toStr(learnEp)) &&
        SSStrU.equals(lockedLearnEps.get(SSStrU.toStr(learnEp)), SSStrU.toStr(user))){
        return true;
      }
      
      if(lockedLearnEps.containsKey(SSStrU.toStr(learnEp))){
        return false;
      }
      
      lockedLearnEps.put          (SSStrU.toStr(learnEp), SSStrU.toStr(user));
      lockedLearnEpsLockTimes.put (SSStrU.toStr(learnEp), SSDateU.dateAsLong());
      return true;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      
      if(learnEpsLock.isWriteLockedByCurrentThread()){
        learnEpsLock.writeLock().unlock();
      }
    }
  }
  
  public static Boolean unLock(
    final SSUri         learnEp) throws Exception{
    
    try{
      
      learnEpsLock.writeLock().lock();
      
      if(lockedLearnEps.containsKey(SSStrU.toStr(learnEp))){

        lockedLearnEps.remove          (SSStrU.toStr(learnEp));
        lockedLearnEpsLockTimes.remove (SSStrU.toStr(learnEp));
        
        return true;
      }
      
      return false;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      if(learnEpsLock.isWriteLockedByCurrentThread()){
        learnEpsLock.writeLock().unlock();
      }
    }
  }
  
  public static Boolean isLocked(final SSUri learnEp) throws Exception{
    
    try{
      
      learnEpsLock.readLock().lock();
      
      return lockedLearnEps.containsKey(SSStrU.toStr(learnEp));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      learnEpsLock.readLock().unlock();
    }
  }
  
  public static Boolean hasLock(
    final SSUri user, 
    final SSUri learnEp) throws Exception{
    
    try{
      
      learnEpsLock.readLock().lock();
      
      if(
        lockedLearnEps.containsKey(SSStrU.toStr(learnEp)) &&
        SSStrU.equals(lockedLearnEps.get(SSStrU.toStr(learnEp)), SSStrU.toStr(user))){
        return true;
      }
      
      return false;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      learnEpsLock.readLock().unlock();
    }
  }
  
  public static void checkHasLock(
    final SSLearnEpConf learnEpConf,
    final SSUri         user,
    final SSUri         learnEp) throws Exception{
    
    try{
      
      final SSCircleServerI           circleServ        = (SSCircleServerI) SSServReg.getServ(SSCircleServerI.class);
      final SSCircleIsEntitySharedPar isEntitySharedPar =
        new SSCircleIsEntitySharedPar(
          user,
          null, //forUser
          learnEp); //entity
      
      if(
        !learnEpConf.useEpisodeLocking ||
        !circleServ.circleIsEntityShared(isEntitySharedPar)){
        return;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return;
    }
    
    try{
      
      learnEpsLock.readLock().lock();
      
      if(!hasLock(user, learnEp)){
        throw SSErr.get(SSErrE.userNeedsLockOnEntity);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      learnEpsLock.readLock().unlock();
    }
  }
  
  public static Long getPassedTime(final SSUri learnEp) throws Exception{
    
    try{
      learnEpsLock.readLock().lock();
      
      if(isLocked(learnEp)){
        return SSDateU.dateAsLong() - lockedLearnEpsLockTimes.get(SSStrU.toStr(learnEp));
      }else{
        return 0L;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      learnEpsLock.readLock().unlock();
    }
  }
  
  public static Long getRemainingTime(final SSUri learnEp) throws Exception{
    return (SSDateU.minuteInMilliSeconds * 5) - SSLearnEpAccessController.getPassedTime(learnEp);
  }
  
  public static List<String> getLockedLearnEps() throws Exception{
    
    try{
      learnEpsLock.readLock().lock();
      
      return new ArrayList<>(lockedLearnEps.keySet());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      learnEpsLock.readLock().unlock();
    }
  }
}