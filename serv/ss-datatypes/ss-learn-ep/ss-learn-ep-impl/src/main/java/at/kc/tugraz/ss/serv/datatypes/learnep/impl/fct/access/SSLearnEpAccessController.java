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

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import sss.serv.err.datatypes.SSErr;
import sss.serv.err.datatypes.SSErrE;

public class SSLearnEpAccessController{
  
  private static final Map<String, String>       lockedLearnEps               = new HashMap<>(); //user, learnEp
  private static final Map<String, Long>         lockedLearnEpsLockTimes      = new HashMap<>(); //learnEp, remaining milliseconds
  private static final ReentrantReadWriteLock    learnEpsLock                 = new ReentrantReadWriteLock();
  
  public static Boolean lock(
    final SSUri         user,
    final SSUri         learnEp) throws Exception{
    
    try{
      
      learnEpsLock.writeLock().lock();
      
      if(
        lockedLearnEps.containsKey(SSStrU.toStr(user)) &&
        SSStrU.equals(lockedLearnEps.get(SSStrU.toStr(user)), SSStrU.toStr(learnEp))){
        return true;
      }
      
      if(lockedLearnEps.containsValue(SSStrU.toStr(learnEp))){
        return false;
      }
      
      lockedLearnEps.put          (SSStrU.toStr(user), SSStrU.toStr(learnEp));
      lockedLearnEpsLockTimes.put (SSStrU.toStr(learnEp), SSDateU.dateAsLong());
      return true;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      learnEpsLock.writeLock().unlock();
    }
  }
  
  public static Boolean unLock(
    final SSUri         user, 
    final SSUri         learnEp) throws Exception{
    
    try{
      
      learnEpsLock.writeLock().lock();
      
      if(
        lockedLearnEps.containsKey(SSStrU.toStr(user)) &&
        SSStrU.equals(lockedLearnEps.get(SSStrU.toStr(user)), SSStrU.toStr(learnEp))){
        
        lockedLearnEps.remove          (SSStrU.toStr(user));
        lockedLearnEpsLockTimes.remove (SSStrU.toStr(learnEp));
        
        return true;
      }
      
      return false;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      learnEpsLock.writeLock().unlock();
    }
  }
  
  public static void checkHasLock(
    final SSUri         user, 
    final SSUri         learnEp) throws Exception{
    
    try{
      
      learnEpsLock.readLock().lock();
      
      if(
        lockedLearnEps.containsKey(SSStrU.toStr(user)) &&
        SSStrU.equals(lockedLearnEps.get(SSStrU.toStr(user)), SSStrU.toStr(learnEp))){
        return;
      }
      
      throw new SSErr(SSErrE.userNeedsLockOnEntity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      learnEpsLock.readLock().unlock();
    }
  }
  
  public static HashMap<String, Long> getLearnEpLockTimes() throws Exception{
    
    try{
      learnEpsLock.readLock().lock();
      
      return new HashMap<>(lockedLearnEpsLockTimes);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      learnEpsLock.readLock().unlock();
    }
  }
}