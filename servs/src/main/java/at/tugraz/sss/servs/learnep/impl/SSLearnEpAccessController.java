/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.learnep.impl;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.api.*;
import at.tugraz.sss.servs.learnep.conf.SSLearnEpConf;
import at.tugraz.sss.servs.entity.datatype.SSCircleIsEntitySharedPar;
import at.tugraz.sss.servs.util.SSDateU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.common.impl.SSServErrReg;

public class SSLearnEpAccessController{
  
  private static final Map<String, String>       lockedLearnEps               = new HashMap<>(); //learnEp, user
  private static final Map<String, Long>         lockedLearnEpsLockTimes      = new HashMap<>(); //learnEp, time the episode got locked
  private static final ReentrantReadWriteLock    learnEpsLock                 = new ReentrantReadWriteLock();
  
  private final SSEntityServerI entityServ;
  
  public SSLearnEpAccessController(final SSEntityServerI entityServ){
    this.entityServ = entityServ;
  }
  
  public boolean lock(
    final SSUri         user,
    final SSUri         learnEp) throws SSErr{
    
    try{
      
      learnEpsLock.writeLock().lock();
      
      if(
        lockedLearnEps.containsKey(SSStrU.toStr(learnEp)) &&
        SSStrU.isEqual(lockedLearnEps.get(SSStrU.toStr(learnEp)), SSStrU.toStr(user))){
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
  
  public boolean unLock(
    final SSUri         learnEp) throws SSErr{
    
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
  
  public boolean isLocked(final SSUri learnEp) throws SSErr{
    
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
  
  public boolean hasLock(
    final SSUri user, 
    final SSUri learnEp) throws SSErr{
    
    try{
      
      learnEpsLock.readLock().lock();
      
      if(
        lockedLearnEps.containsKey(SSStrU.toStr(learnEp)) &&
        SSStrU.isEqual(lockedLearnEps.get(SSStrU.toStr(learnEp)), SSStrU.toStr(user))){
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
  
  public void checkHasLock(
    final SSServPar     servPar,
    final SSLearnEpConf learnEpConf,
    final SSUri         user,
    final SSUri         learnEp) throws SSErr{
    
    try{
      
      final SSCircleIsEntitySharedPar isEntitySharedPar =
        new SSCircleIsEntitySharedPar(
          servPar,
          user,
          null, //forUser
          learnEp); //entity
      
      if(
        !learnEpConf.useEpisodeLocking ||
        !entityServ.circleIsEntityShared(isEntitySharedPar)){
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
  
  public Long getPassedTime(final SSUri learnEp) throws SSErr{
    
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
  
  public Long getRemainingTime(final SSUri learnEp) throws SSErr{
    return (SSDateU.minuteInMilliSeconds * 5) - getPassedTime(learnEp);
  }
  
  public List<String> getLockedLearnEps() throws SSErr{
    
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