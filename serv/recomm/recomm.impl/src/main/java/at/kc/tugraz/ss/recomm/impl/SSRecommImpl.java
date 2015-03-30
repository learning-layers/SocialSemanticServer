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
package at.kc.tugraz.ss.recomm.impl;

import at.kc.tugraz.socialserver.utils.SSFileExtE;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.recomm.api.SSRecommClientI;
import at.kc.tugraz.ss.recomm.api.SSRecommServerI;
import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.recomm.datatypes.SSRecommUserRealmEngine;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommResourcesPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommTagsPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkEntitiesPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdatePar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUsersPar;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommResourcesRet;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommTagsRet;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommUpdateRet;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommUsersRet;
import at.kc.tugraz.ss.recomm.impl.fct.misc.SSRecommFct;
import at.kc.tugraz.ss.recomm.impl.fct.misc.SSRecommResourcesFct;
import at.kc.tugraz.ss.recomm.impl.fct.misc.SSRecommUpdateBulkUploader;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.serv.caller.SSServCallerU;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import engine.Algorithm;
import engine.EntityRecommenderEngine;
import engine.EntityType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSRecommImpl extends SSServImplMiscA implements SSRecommClientI, SSRecommServerI{
  
  private static String                                     sssRealm       = null;

  public static void setSssRealm(String sssRealm){
    SSRecommImpl.sssRealm = sssRealm;
  }
  private static final Map<String, SSRecommUserRealmEngine> userEngines    = new HashMap<>();
  
  private        final SSRecommConf                  recommConf;
  
  public SSRecommImpl(final SSConfA conf) throws Exception{
    super(conf);
    
    recommConf = ((SSRecommConf)conf);
    
    if(sssRealm == null){
      sssRealm = recommConf.fileNameForRec;
    }
    
    if(!userEngines.containsKey(SSStrU.toStr(SSVoc.systemUserUri))){
      userEngines.put(SSStrU.toStr(SSVoc.systemUserUri), SSRecommUserRealmEngine.get(new EntityRecommenderEngine(), sssRealm));
    }
  }
  
  public static String getSssRealm(){
    return sssRealm;
  }
  
  @Override
  public void recommUsers(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSRecommUsersRet.get(recommUsers(parA), parA.op));
  }
  
  @Override
  public Map<SSEntity, Double> recommUsers(final SSServPar parA) throws Exception{
    
    try{
      final SSRecommUsersPar       par           = new SSRecommUsersPar(parA);
      final Map<SSEntity, Double>  users         = new HashMap<>();
      Integer                      userCounter   = 0;
      
      SSRecommFct.checkPar(par.user, par.forUser, par.realm);
      
      final SSRecommUserRealmEngine userRealmEngine = 
        checkAddAndGetUserRealmEngine(par.user, par.realm, false);
      
      //Users for resource (Tags): getEntitiesWithLikelihood(null,    entity, null, 10, false, null /* LD: Algorithm.USERTAGCB */, EntityType.USER); // CBtags
      //Users for user:            getEntitiesWithLikelihood(forUser, null,   null, 10, false, null,                               EntityType.USER); // CF
      //Users MostPopular:         getEntitiesWithLikelihood(null,    null,   null, 10, false, null,                               EntityType.USER); // MP
      
      final Map<String, Double> usersWithLikelihood =
        userRealmEngine.engine.getEntitiesWithLikelihood(
          SSStrU.toStr(par.forUser),
          SSStrU.toStr(par.entity),
          par.categories,
          100,
          null, //filterOwn
          recommConf.recommUserAlgorithm, //algo
          EntityType.USER);  //entity type to recommend

      for(Map.Entry<String, Double> userWithLikelihood : usersWithLikelihood.entrySet()){
        
        users.put(
          SSRecommFct.handleAccess(par.user, SSUri.get(userWithLikelihood.getKey())),
          userWithLikelihood.getValue());
        
        if((++userCounter) >= par.maxUsers){
          break;
        }
      }
      
      return users;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void recommTags(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSRecommTagsRet.get(recommTags(parA), parA.op));
  }
  
  @Override
  public Map<String, Double> recommTags(final SSServPar parA) throws Exception{
    
    try{
      final SSRecommTagsPar par  = new SSRecommTagsPar(parA);
      final SSEntity        forUserEntity;
      Algorithm             algo = recommConf.recommTagAlgorithm;
      
      SSRecommFct.checkPar(par.user, par.forUser, par.realm);
      
      final SSRecommUserRealmEngine userRealmEngine = 
        checkAddAndGetUserRealmEngine(par.user, par.realm, false);
      
      if(
        recommConf.recommTagsGroups != null &&
        !recommConf.recommTagsGroups.isEmpty()){
       
        forUserEntity = SSServCaller.entityGet(par.forUser);
        
        for(String tagRecommGroup : recommConf.recommTagsGroups){
          
          if(SSStrU.equals(SSStrU.split(tagRecommGroup, SSStrU.colon).get(0), forUserEntity.label)){
            algo = Algorithm.valueOf(SSStrU.split(tagRecommGroup, SSStrU.colon).get(1));
            break;
          }
        }
      }
      
      //Tags for user and resource: getEntitiesWithLikelihood(forUser,  entity,  null, 10, false, null, EntityType.TAG);  // BLLac+MPr
      //Tags for user:              getEntitiesWithLikelihood(forUser,  null,    null, 10, false, null, EntityType.TAG);  // BLL
      //Tags for resource:          getEntitiesWithLikelihood(null,     entity,  null, 10, false, null, EntityType.TAG);  // MPr
      //Tags MostPopular:           getEntitiesWithLikelihood(null,     null,    null, 10, false, null, EntityType.TAG);  // MP
      return userRealmEngine.engine.getEntitiesWithLikelihood(
        SSStrU.toStr(par.forUser), 
        SSStrU.toStr(par.entity),
        par.categories,
        par.maxTags,
        !par.includeOwn, //filterOwn
        algo,
        EntityType.TAG);  //entity type to recommend
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void recommResources(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSRecommResourcesRet.get(recommResources(parA), parA.op));
  }
  
  @Override
  public Map<SSEntity, Double> recommResources(final SSServPar parA) throws Exception{
    
    try{
      final SSRecommResourcesPar   par           = new SSRecommResourcesPar(parA);
      final Map<SSEntity, Double>  entities      = new HashMap<>();
      Integer                      entityCounter = 0;
      SSEntity                     entity;
      
      SSRecommFct.checkPar(par.user, par.forUser, par.realm);
      
      final SSRecommUserRealmEngine userRealmEngine = 
        checkAddAndGetUserRealmEngine(par.user, par.realm, false);
      
      //Resources for user (Tags): getEntitiesWithLikelihood(forUser, null,   null, 10, false, Algorithm.RESOURCETAGCB /* LD */,   EntityType.RESOURCE); // CBtags
      //Resources for user (CF):   getEntitiesWithLikelihood(forUser, null,   null, 10, false, null /* or Algorithm.RESOURCECF */, EntityType.RESOURCE); // CF
      //Resources for resource:    getEntitiesWithLikelihood(null,    entity, null, 10, false, null,                               EntityType.RESOURCE); // CF
      //Resources MostPopular:     getEntitiesWithLikelihood(null,    null,   null, 10, false, null,                               EntityType.RESOURCE); // MP
      
      final Map<String, Double> entitiesWithLikelihood =
        userRealmEngine.engine.getEntitiesWithLikelihood(
          SSStrU.toStr(par.forUser),
          SSStrU.toStr(par.entity),
          par.categories,
          100,
          !par.includeOwn, //filterOwn
          recommConf.recommResourceAlgorithm, //algo
          EntityType.RESOURCE);  //entity type to recommend

      for(Map.Entry<String, Double> entityWithLikelihood : entitiesWithLikelihood.entrySet()){
        
        entity = 
          SSRecommFct.handleAccess(
            par.user, 
            SSUri.get(entityWithLikelihood.getKey()));
        
        if(
          entity == null ||
          !SSRecommResourcesFct.handleType   (par, entity)){
          continue;
        }
        
        SSRecommResourcesFct.addCircleTypes(par, entity);
       
        entities.put(
          entity,
          entityWithLikelihood.getValue());
        
        if((++entityCounter) >= par.maxResources){
          break;
        }
      }
      
      return entities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void recommUpdateBulk(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSRecommUpdateBulkPar par = new SSRecommUpdateBulkPar(parA);
    
    checkAddAndGetUserRealmEngine(par.user, par.realm, true);
    
    new Thread(new SSRecommUpdateBulkUploader(recommConf, sSCon, par)).start();
  }
  
  @Override
  public void recommUpdateBulk(final SSServPar parA) throws Exception{
    
    try{
      
      final SSRecommUpdateBulkPar par = new SSRecommUpdateBulkPar(parA);
      
      final SSRecommUserRealmEngine userRealmEngine = 
        checkAddAndGetUserRealmEngine(par.user, par.realm, true);
        
      SSServCaller.dataExportUserEntityTagCategoryTimestamps(
        par.user,
        true, 
        recommConf.usePrivateTagsToo, 
        true, 
        userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
      
      userRealmEngine.engine.loadFile(userRealmEngine.realm);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void recommUpdate(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSRecommUpdateRet.get(recommUpdate(parA), parA.op));
  }
  
  @Override
  public Boolean recommUpdate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSRecommUpdatePar par = new SSRecommUpdatePar(parA);
      
      final SSRecommUserRealmEngine userRealmEngine = 
        checkAddAndGetUserRealmEngine(par.user, par.realm, true);
      
      SSServCaller.dataExportAddTagsCategoriesTimestampsForUserEntity(
        par.user,
        par.forUser,
        par.entity,
        par.tags,
        par.categories,
        userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
      
      userRealmEngine.engine.loadFile(userRealmEngine.realm);
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      
      return null;
    }
  }
  
    @Override
  public void recommUpdateBulkEntities(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSRecommUpdateRet.get(recommUpdateBulkEntities(parA), parA.op));
  }
  
  @Override
  public Boolean recommUpdateBulkEntities(final SSServPar parA) throws Exception{
    
    try{
      
      final SSRecommUpdateBulkEntitiesPar par = new SSRecommUpdateBulkEntitiesPar(parA);

      List<String> tags;
      List<String> categories;
      
      final SSRecommUserRealmEngine userRealmEngine = 
        checkAddAndGetUserRealmEngine(par.user, par.realm, true);
      
      if(
        !par.tags.isEmpty() &&
        par.tags.size() != par.entities.size()){
        
        throw new Exception("tag list size differs from entity list size");
      }
      
      if(
        !par.categories.isEmpty() &&
        par.categories.size() != par.entities.size()){
        
        throw new Exception("category list size differs from entity list size");
      }
      
      for(int counter = 0; counter < par.entities.size(); counter++){

        if(!par.tags.isEmpty()){
          tags = par.tags.get(counter);
        }else{
          tags = new ArrayList<>();
        }
        
        if(!par.categories.isEmpty()){
          categories = par.categories.get(counter);
        }else{
          categories = new ArrayList<>();
        }
        
        SSServCaller.dataExportAddTagsCategoriesTimestampsForUserEntity(
          par.user,
          par.forUser,
          par.entities.get(counter),
          tags,
          categories,
          userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
      }
      
      userRealmEngine.engine.loadFile(userRealmEngine.realm);
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private SSRecommUserRealmEngine checkAddAndGetUserRealmEngine(
    final SSUri   user,
    final String  realm,
    final Boolean checkForUpdate) throws Exception{
    
    try{
      
      final String userStr = SSStrU.toStr(user);
      
      if(checkForUpdate){
        
        if(SSStrU.equals(user, SSVoc.systemUserUri)){
          
          if(realm == null){
            throw new Exception("realm has to be set");
          }
        }else{
          
          if(
            realm == null ||
            SSStrU.equals(realm, sssRealm)){
            
            throw new Exception("realm has to be set");
          }
        }
      }else{
        
        if(
          realm == null ||
          SSStrU.equals(realm, sssRealm)){
          
          return userEngines.get(SSStrU.toStr(SSVoc.systemUserUri));
        }
      }
      
      if(userEngines.containsKey(userStr)){
        
        if(!SSStrU.equals(userEngines.get(userStr).realm, realm)){
          throw new Exception("user already defined recomm realm: " + userEngines.get(userStr).realm + " re-use this!");
        }
      }else{
        
        if(checkForUpdate){
          userEngines.put(userStr, SSRecommUserRealmEngine.get(new EntityRecommenderEngine(), realm));
        }else{
          throw new Exception("realm to recommend from doesnt exist");
        }
      }
      
      return userEngines.get(userStr);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}