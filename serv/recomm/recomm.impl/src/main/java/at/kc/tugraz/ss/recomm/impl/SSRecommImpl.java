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
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommResourcesPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdatePar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommTagsPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUsersPar;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommResourcesRet;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommTagsRet;
import at.kc.tugraz.ss.recomm.impl.fct.misc.SSRecommFct;
import at.kc.tugraz.ss.recomm.impl.fct.misc.SSRecommResourcesFct;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.serv.caller.SSServCallerU;
import engine.Algorithm;
import engine.EngineInterface;
import engine.EntityRecommenderEngine;
import engine.EntityType;
import java.util.HashMap;
import java.util.Map;

public class SSRecommImpl extends SSServImplMiscA implements SSRecommClientI, SSRecommServerI{
  
  private static final EngineInterface               regEngine       = new EntityRecommenderEngine();
  private        final SSRecommConf                  recommConf;
  
  public SSRecommImpl(final SSConfA conf) throws Exception{
    super(conf);
    
    recommConf = ((SSRecommConf)conf);
  }
  
  @Override
  public void recommUsers(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSRecommTagsRet.get(recommTags(parA), parA.op));
  }
  
  @Override
  public Map<SSEntity, Double> recommUsers(final SSServPar parA) throws Exception{
    
    try{
      final SSRecommUsersPar       par           = new SSRecommUsersPar(parA);
      final Map<SSEntity, Double>  users         = new HashMap<>();
      Integer                      userCounter   = 0;
      
      SSRecommFct.checkPar(par.user, par.forUser);
      
      //Users for resource (Tags): getEntitiesWithLikelihood(null,    entity, null, 10, false, null /* LD: Algorithm.USERTAGCB */, EntityType.USER); // CBtags
      //Users for user:            getEntitiesWithLikelihood(forUser, null,   null, 10, false, null,                               EntityType.USER); // CF
      //Users MostPopular:         getEntitiesWithLikelihood(null,    null,   null, 10, false, null,                               EntityType.USER); // MP
      
      final Map<String, Double> usersWithLikelihood =
        regEngine.getEntitiesWithLikelihood(
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
      
      
      if(par.user == null){
        throw new Exception("user cannot be null");
      }
      
      if(
        par.forUser != null &&
        !SSStrU.equals(par.user, par.forUser)){
        throw new Exception("user cannot retrieve tag recommendations for other users");
      }
      
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
      return regEngine.getEntitiesWithLikelihood(
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
      
      SSRecommFct.checkPar(par.user, par.forUser);
      
      //Resources for user (Tags): getEntitiesWithLikelihood(forUser, null,   null, 10, false, Algorithm.RESOURCETAGCB /* LD */,   EntityType.RESOURCE); // CBtags
      //Resources for user (CF):   getEntitiesWithLikelihood(forUser, null,   null, 10, false, null /* or Algorithm.RESOURCECF */, EntityType.RESOURCE); // CF
      //Resources for resource:    getEntitiesWithLikelihood(null,    entity, null, 10, false, null,                               EntityType.RESOURCE); // CF
      //Resources MostPopular:     getEntitiesWithLikelihood(null,    null,   null, 10, false, null,                               EntityType.RESOURCE); // MP
      
      final Map<String, Double> entitiesWithLikelihood =
        regEngine.getEntitiesWithLikelihood(
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
  public void recommUpdate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSRecommUpdatePar par = new SSRecommUpdatePar(parA);
      
      SSServCaller.dataExportUserEntityTagCategoryTimestamps(
        par.user, 
        true, 
        recommConf.usePrivateTagsToo, 
        true, 
        recommConf.fileNameForRec);
      
      regEngine.loadFile(
        SSStrU.removeTrailingString(
          recommConf.fileNameForRec,
          SSStrU.dot + SSFileExtE.txt.toString()));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}