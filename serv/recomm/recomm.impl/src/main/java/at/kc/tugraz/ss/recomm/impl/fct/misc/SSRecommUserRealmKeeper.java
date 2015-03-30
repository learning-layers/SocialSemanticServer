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
package at.kc.tugraz.ss.recomm.impl.fct.misc;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.recomm.datatypes.SSRecommUserRealmEngine;
import at.kc.tugraz.ss.recomm.impl.fct.sql.SSRecommSQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import engine.EntityRecommenderEngine;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SSRecommUserRealmKeeper{
  
  private static String                                     sssRealm            = null;
  private static final Map<String, SSRecommUserRealmEngine> userRealmEngines    = new HashMap<>();

  public static Collection<SSRecommUserRealmEngine> getUserRealmEngines(){
    
    return userRealmEngines.values();
  }
  
  private static final ReentrantReadWriteLock               engineLock     = new ReentrantReadWriteLock();
  
  public static String getSssRealm() throws Exception{
    
    try{
      engineLock.readLock().lock();
      
      return sssRealm;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      engineLock.readLock().unlock();
    }
  }
  
  public static SSRecommUserRealmEngine checkAddAndGetUserRealmEngine(
    final SSUri          user,
    final String         realm,
    final Boolean        checkForUpdate, 
    final SSRecommSQLFct sqlFct) throws Exception{
    
    try{
      
      engineLock.readLock().lock();
      
      final String userStr = SSStrU.toStr(user);
      
      if(checkForUpdate){
        
        if(SSStrU.equals(user, SSVoc.systemUserUri)){
          
          if(realm == null){
            throw new Exception("realm has to be set");
          }
        }else{
          
          if(
            realm == null ||
            SSStrU.equals(realm, SSRecommUserRealmKeeper.getSssRealm())){
            
            throw new Exception("realm has to be set");
          }
        }
      }else{
        
        if(
          realm == null ||
          SSStrU.equals(realm, SSRecommUserRealmKeeper.getSssRealm())){
          
          return SSRecommUserRealmKeeper.userRealmEngines.get(SSStrU.toStr(SSVoc.systemUserUri));
        }
      }
      
      if(userRealmEngines.containsKey(userStr)){
        
        if(!SSStrU.equals(userRealmEngines.get(userStr).realm, realm)){
          throw new Exception("user already defined recomm realm: '" + userRealmEngines.get(userStr).realm + "'; re-use this!");
        }
      }else{
        
        if(checkForUpdate){
          
          engineLock.readLock().unlock();
          
          engineLock.writeLock().lock();
          
          userRealmEngines.put(userStr, SSRecommUserRealmEngine.get(new EntityRecommenderEngine(), realm));
          
          sqlFct.addUserRealm(user, realm);
          
        }else{
          throw new Exception("realm to recommend from doesnt exist");
        }
      }
      
      return userRealmEngines.get(userStr);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      if(engineLock.isWriteLockedByCurrentThread()){
        engineLock.writeLock().unlock();
      }else{
        engineLock.readLock().unlock();
      }
    }
  }
  
  public static void setSssRealmAndEngine(final String sssRealmValue) throws Exception{
    
    try{
      
      engineLock.writeLock().lock();
      
      if(sssRealm == null){
        sssRealm = sssRealmValue;
      }
      
      if(!userRealmEngines.containsKey(SSStrU.toStr(SSVoc.systemUserUri))){
        
        userRealmEngines.put(
          SSStrU.toStr(SSVoc.systemUserUri),
          SSRecommUserRealmEngine.get(
            new EntityRecommenderEngine(),
            sssRealm));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(engineLock.isWriteLockedByCurrentThread()){
        engineLock.writeLock().unlock();
      }
    }
  }

  public static void setUserRealmEngines(
    final Map<String, String> userRealms) throws Exception{
    
    try{
      
      engineLock.writeLock().lock();
      
      for(Map.Entry<String, String> userRealm : userRealms.entrySet()){
        
        if(userRealmEngines.containsKey(userRealm.getKey())){
          continue;
        }
        
        userRealmEngines.put(
          userRealm.getKey(),
          SSRecommUserRealmEngine.get(
            new EntityRecommenderEngine(),
            userRealm.getValue()));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(engineLock.isWriteLockedByCurrentThread()){
        engineLock.writeLock().unlock();
      }
    }
  }
}
