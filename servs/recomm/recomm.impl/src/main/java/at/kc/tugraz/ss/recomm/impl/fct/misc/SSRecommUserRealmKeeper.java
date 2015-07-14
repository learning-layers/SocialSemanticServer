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

import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.recomm.datatypes.SSRecommUserRealmEngine;
import at.kc.tugraz.ss.recomm.impl.fct.sql.SSRecommSQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityFromTypeAndLabelGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import engine.EntityRecommenderEngine;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SSRecommUserRealmKeeper{
  
  private static final Map<String, SSRecommUserRealmEngine> userRealmEngines   = new HashMap<>();
  private static final ReentrantReadWriteLock               engineLock         = new ReentrantReadWriteLock();  
  
  public static Collection<SSRecommUserRealmEngine> getUserRealmEngines(){
    return userRealmEngines.values();
  }
  
  public static SSRecommUserRealmEngine checkAddAndGetUserRealmEngine(
    final SSRecommConf   conf,
    final SSUri          user,
    String               realm,
    final Boolean        checkForUpdate,
    final SSRecommSQLFct sqlFct) throws Exception{
    
    try{
      
      engineLock.readLock().lock();
      
      if(user == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(realm == null){
        realm = ((SSRecommConf)conf).fileNameForRec;
      }
      
      final String userStr = SSStrU.toStr(user);
      
      if(checkForUpdate){
        
        if(
          SSStrU.equals  (realm,   conf.fileNameForRec) &&
          !SSStrU.equals (userStr, SSVocConf.systemUserUri)){
          throw new SSErr(SSErrE.userNotAllowedToRetrieveForOtherUser);
        }
      }else{
        
        if(SSStrU.equals(realm, conf.fileNameForRec)){
          return userRealmEngines.get(SSStrU.toStr(SSVocConf.systemUserUri));
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
          
          final SSRecommUserRealmEngine userRealmEngine  = SSRecommUserRealmEngine.get(new EntityRecommenderEngine(), realm);
          FileOutputStream              userRealmFileOut = null;
          
          //refactor this to loadSingleUserRealm
          try{
            userRealmFileOut =
              SSFileU.openOrCreateFileWithPathForWrite(
                SSFileU.dirWorkingDataCsv() + userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
            
            sqlFct.addUserRealm(user, realm);
            
            userRealmEngines.put(userStr, userRealmEngine);
          }catch(Exception error){
            SSLogU.warn("user realm engine creation of file failed");
          }finally{
            if(userRealmFileOut != null){
              userRealmFileOut.close();
            }
          }
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
  
  public static void setSSSRealmEngine(final SSRecommConf conf) throws Exception{
    
    FileOutputStream sssRealmFileOut = null;
    
    try{
      
      engineLock.writeLock().lock();
      
      if(!userRealmEngines.containsKey(SSStrU.toStr(SSVocConf.systemUserUri))){
        
        sssRealmFileOut =
          SSFileU.openOrCreateFileWithPathForWrite(
            SSFileU.dirWorkingDataCsv() + conf.fileNameForRec + SSStrU.dot + SSFileExtE.txt);
        
        userRealmEngines.put(
          SSStrU.toStr(SSVocConf.systemUserUri),
          SSRecommUserRealmEngine.get(
            new EntityRecommenderEngine(),
            conf.fileNameForRec));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(sssRealmFileOut != null){
        sssRealmFileOut.close();
      }
      
      if(engineLock.isWriteLockedByCurrentThread()){
        engineLock.writeLock().unlock();
      }
    }
  }
  
  public static void setUserRealmEnginesFromConf(
    final SSRecommConf        conf) throws Exception{
    
    FileOutputStream userRealmFileOut = null;
    String    realm;
    String    userLabel;
    SSEntity  userEntity;
    
    try{
      
      SSRecommUserRealmEngine userRealmEngine;
      
      engineLock.writeLock().lock();
      
      if(
        conf.recommTagsUserPerRealm == null ||
        conf.recommTagsUserPerRealm.isEmpty()){
        return;
      }
        
      for(String realmAndUser : conf.recommTagsUserPerRealm){
        realm     = SSStrU.split(realmAndUser, SSStrU.colon).get(0);
        userLabel = SSStrU.split(realmAndUser, SSStrU.colon).get(1);
        
        userEntity =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
            new SSEntityFromTypeAndLabelGetPar(
              null,
              null,
              SSVocConf.systemUserUri,
              SSLabel.get(userLabel),
              SSEntityE.user,
              false)); //withUserRestriction));
        
        if(userRealmEngines.containsKey(SSStrU.toStr(userEntity.id))){
          continue;
        }
        
        userRealmEngine =
          SSRecommUserRealmEngine.get(
            new EntityRecommenderEngine(),
            realm);
        
        //refactor this to loadSingleUserRealm
        try{
          userRealmFileOut =
            SSFileU.openOrCreateFileWithPathForWrite(
              SSFileU.dirWorkingDataCsv() + userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
          
          userRealmEngine.engine.loadFile(userRealmEngine.realm);
          
          userRealmEngines.put(SSStrU.toStr(userEntity.id), userRealmEngine);
        }catch(Exception error){
          SSLogU.warn("user realm engine creation of file failed");
        }finally{
          
          if(userRealmFileOut != null){
            userRealmFileOut.close();
            userRealmFileOut = null;
          }
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(engineLock.isWriteLockedByCurrentThread()){
        engineLock.writeLock().unlock();
      }
    }
  }
  
  public static void setAndLoadUserRealmEnginesFromDB(
    final Map<String, String> userRealms) throws Exception{
    
    FileOutputStream userRealmFileOut = null;
    
    try{
      
      SSRecommUserRealmEngine userRealmEngine;
      
      engineLock.writeLock().lock();
      
      for(Map.Entry<String, String> userRealm : userRealms.entrySet()){
        
        if(userRealmEngines.containsKey(userRealm.getKey())){
          continue;
        }
        
        userRealmEngine =
          SSRecommUserRealmEngine.get(
            new EntityRecommenderEngine(),
            userRealm.getValue());
        
        //refactor this to loadSingleUserRealm
        try{
          userRealmFileOut =
            SSFileU.openOrCreateFileWithPathForWrite(
              SSFileU.dirWorkingDataCsv() + userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
          
          userRealmEngine.engine.loadFile(userRealmEngine.realm);
          
          userRealmEngines.put(userRealm.getKey(), userRealmEngine);
        }catch(Exception error){
          SSLogU.warn("user realm engine creation of file failed");
        }finally{
          
          if(userRealmFileOut != null){
            userRealmFileOut.close();
            userRealmFileOut = null;
          }
        }
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