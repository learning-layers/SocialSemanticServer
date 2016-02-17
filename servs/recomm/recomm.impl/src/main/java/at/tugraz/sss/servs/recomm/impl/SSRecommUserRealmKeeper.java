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
package at.tugraz.sss.servs.recomm.impl;

import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.kc.tugraz.ss.recomm.datatypes.SSRecommUserRealmEngine;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.util.SSFileExtE;
import at.tugraz.sss.serv.util.SSFileU;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.reg.SSServErrReg;
import engine.EngineInterface;
import engine.EntityRecommenderEngine;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SSRecommUserRealmKeeper{
  
  private final Map<String, List<SSRecommUserRealmEngine>> userRealmEngines   = new HashMap<>();
  private final ReentrantReadWriteLock                     engineLock         = new ReentrantReadWriteLock();  

  public SSRecommUserRealmEngine checkAddAndGetUserRealmEngine(
    final SSServPar       servPar,
    final SSRecommConf    conf,
    final SSUri           user,
    String                realm,
    final boolean         checkForUpdate,
    final EngineInterface engine, 
    final SSRecommSQL     sqlFct,
    final boolean         storeToDB) throws SSErr{
    
    try{
      
      engineLock.readLock().lock();
      
      if(user == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(realm == null){
        realm = ((SSRecommConf)conf).fileNameForRec;
      }
      
      final String userStr = SSStrU.toStr(user);
      
      if(checkForUpdate){
        
        if(
          SSStrU.isEqual  (realm,   conf.fileNameForRec) &&
          !SSStrU.isEqual (userStr, SSConf.systemUserUri)){
          
          throw SSErr.get(SSErrE.userNotAllowedToRetrieveForOtherUser);
        }
      }else{
        
        if(SSStrU.isEqual(realm, conf.fileNameForRec)){
          final List<SSRecommUserRealmEngine> systemUserRealmEngines = userRealmEngines.get(SSStrU.toStr(SSConf.systemUserUri));
          
          return systemUserRealmEngines.get(0);
        }
      }
      
      if(userRealmEngines.containsKey(userStr)){
        
        for(SSRecommUserRealmEngine userRealmEngine : userRealmEngines.get(userStr)){
        
          if(SSStrU.isEqual(userRealmEngine.realm, realm)){
            return userRealmEngine;
          }
        }
      }
        
      if(checkForUpdate){
        
        engineLock.readLock().unlock();
        
        engineLock.writeLock().lock();
        
        final SSRecommUserRealmEngine userRealmEngine  = SSRecommUserRealmEngine.get(engine, realm);
        
        //refactor this to loadSingleUserRealm
        
        if(storeToDB){
          sqlFct.addUserRealm(servPar, user, realm);
        }
        
        if(!userRealmEngines.containsKey(userStr)){
          userRealmEngines.put(userStr, new ArrayList());
        }
        
        userRealmEngines.get(userStr).add(userRealmEngine);
        
        return userRealmEngine;
        
      }else{
        throw new Exception("realm to recommend from doesnt exist");
      }
      
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
  
  public void setAndLoadUserRealmEnginesFromDB(
    final String                    workDirDataCsvPath,
    final Map<String, List<String>> usersRealms) throws SSErr{
    
    FileOutputStream userRealmFileOut = null;
    
    try{
      
      SSRecommUserRealmEngine userRealmEngine;
      
      engineLock.writeLock().lock();
      
      for(Map.Entry<String, List<String>> userRealms : usersRealms.entrySet()){
        
        for(String userRealm : userRealms.getValue()){
          
          if(!userRealmEngines.containsKey(userRealms.getKey())){
            userRealmEngines.put(userRealms.getKey(), new ArrayList<>());
          }
          
          userRealmEngine =
            SSRecommUserRealmEngine.get(
              new EntityRecommenderEngine(),
              userRealm);
          
          //refactor this to loadSingleUserRealm
          try{
            userRealmFileOut =
              SSFileU.openOrCreateFileWithPathForWrite(
                workDirDataCsvPath + userRealm + SSStrU.dot + SSFileExtE.txt);
            
            userRealmEngine.engine.loadFile(workDirDataCsvPath, userRealm + SSStrU.dot + SSFileExtE.txt);
            
            userRealmEngines.get(userRealms.getKey()).add(userRealmEngine);
          }catch(Exception error){
            SSLogU.warn("user realm engine creation of file failed", error);
          }finally{
            
            if(userRealmFileOut != null){
              userRealmFileOut.close();
              userRealmFileOut = null;
            }
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

//public static void setUserRealmEnginesFromConf(
//    final SSRecommConf        conf) throws SSErr{
//    
//    FileOutputStream userRealmFileOut = null;
//    String    realm;
//    SSUri     userURI;
//    String    userEmail;
//    
//    try{
//      
//      SSRecommUserRealmEngine userRealmEngine;
//      
//      engineLock.writeLock().lock();
//      
//      if(
//        conf.recommTagsUserPerRealm == null ||
//        conf.recommTagsUserPerRealm.isEmpty()){
//        return;
//      }
//        
//      for(String realmAndUser : conf.recommTagsUserPerRealm){
//        
//        realm     = SSStrU.split(realmAndUser, SSStrU.colon).get(0);
//        userEmail = SSStrU.split(realmAndUser, SSStrU.colon).get(1);
//        userURI   =
//          ((SSUserServerI) SSServReg.getServ(SSUserServerI.class)).userURIGet(
//            new SSUserURIGetPar(
//              null, 
//              null, 
//              SSConf.systemUserUri, 
//              userEmail));
//        
//        if(!userRealmEngines.containsKey(SSStrU.toStr(userURI))){
//          userRealmEngines.put(SSStrU.toStr(userURI), new ArrayList<>());
//        }
//        
//        userRealmEngine =
//          SSRecommUserRealmEngine.get(
//            new EntityRecommenderEngine(),
//            realm);
//        
//        //refactor this to loadSingleUserRealm
//        try{
//          userRealmFileOut =
//            SSFileU.openOrCreateFileWithPathForWrite(
//              SSFileU.dirWorkingDataCsv() + realm + SSStrU.dot + SSFileExtE.txt);
//          
//          userRealmEngine.engine.loadFile(realm);
//          
//          userRealmEngines.get(SSStrU.toStr(userURI)).add(userRealmEngine);
//        }catch(Exception error){
//          SSLogU.warn("user realm engine creation of file failed");
//        }finally{
//          
//          if(userRealmFileOut != null){
//            userRealmFileOut.close();
//            userRealmFileOut = null;
//          }
//        }
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      
//      if(engineLock.isWriteLockedByCurrentThread()){
//        engineLock.writeLock().unlock();
//      }
//    }
//  }

//public static void setSSSRealmEngine(final SSRecommConf conf) throws SSErr{
//    
//    FileOutputStream sssRealmFileOut = null;
//    
//    try{
//      
//      engineLock.writeLock().lock();
//      
//      if(!userRealmEngines.containsKey(SSStrU.toStr(SSConf.systemUserUri))){
//        
//        userRealmEngines.put(SSStrU.toStr(SSConf.systemUserUri), new ArrayList());
//        
//        sssRealmFileOut =
//          SSFileU.openOrCreateFileWithPathForWrite(
//            SSFileU.dirWorkingDataCsv() + conf.fileNameForRec + SSStrU.dot + SSFileExtE.txt);
//        
//        userRealmEngines.get(
//          SSStrU.toStr(SSConf.systemUserUri)).add(
//          SSRecommUserRealmEngine.get(
//            new EntityRecommenderEngine(),
//            conf.fileNameForRec));
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      
//      if(sssRealmFileOut != null){
//        sssRealmFileOut.close();
//      }
//      
//      if(engineLock.isWriteLockedByCurrentThread()){
//        engineLock.writeLock().unlock();
//      }
//    }
//  }

//  public static Collection<SSRecommUserRealmEngine> getUserRealmEngines(){
//    return userRealmEngines.values();
//  }