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

import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.recomm.api.SSRecommClientI;
import at.kc.tugraz.ss.recomm.api.SSRecommServerI;
import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.recomm.datatypes.SSRecommUserRealmEngine;
import at.kc.tugraz.ss.recomm.datatypes.SSResourceLikelihood;
import at.kc.tugraz.ss.recomm.datatypes.SSUserLikelihood;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommResourcesPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommTagsPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkEntitiesPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkUserRealmsFromConfPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdatePar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUsersPar;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommResourcesRet;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommTagsRet;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommUpdateRet;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommUsersRet;
import at.kc.tugraz.ss.recomm.impl.fct.misc.SSRecommResourcesFct;
import at.kc.tugraz.ss.recomm.impl.fct.misc.SSRecommUpdateBulkUploader;
import at.kc.tugraz.ss.recomm.impl.fct.misc.SSRecommUserRealmKeeper;
import at.kc.tugraz.ss.recomm.impl.fct.sql.SSRecommSQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityFromTypeAndLabelGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLikelihood;
import at.kc.tugraz.ss.service.user.api.SSUserServerI;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserGetPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserURIGetPar;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import engine.Algorithm;
import engine.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import java.util.HashMap;

public class SSRecommImpl extends SSServImplWithDBA implements SSRecommClientI, SSRecommServerI{
  
  private final SSRecommConf   recommConf;
  private final SSRecommSQLFct sqlFct;
  
  public SSRecommImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    recommConf = ((SSRecommConf)conf);
    sqlFct     = new SSRecommSQLFct(dbSQL);
  }
  
  @Override
  public void recommUsers(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSRecommUsersPar par = (SSRecommUsersPar) parA.getFromJSON(SSRecommUsersPar.class);
    
    sSCon.writeRetFullToClient(SSRecommUsersRet.get(recommUsers(par)));
  }
  
  @Override
  public List<SSUserLikelihood> recommUsers(final SSRecommUsersPar par) throws Exception{
    
    try{
      final SSRecommUserRealmEngine userRealmEngine =
        SSRecommUserRealmKeeper.checkAddAndGetUserRealmEngine(
          (SSRecommConf)conf,
          par.user,
          par.realm,
          false,
          sqlFct);
      
      if(!par.ignoreAccessRights){
        
        if(par.withUserRestriction){
          
          if(
            par.forUser != null &&
            !SSStrU.equals(par.user, par.forUser)){
            throw new SSErr(SSErrE.userNotAllowedToRetrieveForOtherUser);
          }
          
          if(par.entity != null){
            if(!SSServCallerU.canUserRead(par.user, par.entity)){
              return new ArrayList<>();
            }
          }
        }
      }
      
      final List<SSUserLikelihood> users  = new ArrayList<>();
      final SSEntityDescriberPar   descPar;
      SSEntity                     entity;
      Integer                      userCounter = 0;

      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar();
      }else{
        descPar = null;
      }
      
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
        
        if(par.ignoreAccessRights){
          
          users.add(
            SSUserLikelihood.get(
              SSEntity.get(
                SSUri.get(userWithLikelihood.getKey()),
                SSEntityE.user),
              userWithLikelihood.getValue()));
        }else{
          
          entity =
            ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
              new SSEntityGetPar(
                null,
                null,
                par.user,
                SSUri.get(userWithLikelihood.getKey()), //entity
                par.withUserRestriction, //withUserRestriction,
                descPar)); //descPar
          
          if(entity == null){
            continue;
          }
          
          users.add(
            SSUserLikelihood.get(
              entity,
              userWithLikelihood.getValue()));
        }
        
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
    
    final SSRecommTagsPar par = (SSRecommTagsPar) parA.getFromJSON(SSRecommTagsPar.class);
    
    sSCon.writeRetFullToClient(SSRecommTagsRet.get(recommTags(par)));
  }
  
  @Override
  public List<SSTagLikelihood> recommTags(final SSRecommTagsPar par) throws Exception{
    
    try{
      
      final SSRecommUserRealmEngine userRealmEngine =
        SSRecommUserRealmKeeper.checkAddAndGetUserRealmEngine(
          (SSRecommConf) conf,
          par.user,
          par.realm,
          false,
          sqlFct);
      
      if(par.ignoreAccessRights){
        
        if(SSStrU.equals(par.realm, ((SSRecommConf)conf).fileNameForRec)){
          throw new SSErr(SSErrE.parameterMissing);
        }
      }
      
      if(!par.ignoreAccessRights){
        
        if(par.withUserRestriction){
          
          if(
            par.forUser != null &&
            !SSStrU.equals(par.user, par.forUser)){
            throw new SSErr(SSErrE.userNotAllowedToRetrieveForOtherUser);
          }
          
          if(par.entity != null){
            if(!SSServCallerU.canUserRead(par.user, par.entity)){
              return new ArrayList<>();
            }
          }
        }
      }
      
      final List<SSTagLikelihood>  tags  = new ArrayList<>();
      Algorithm                    algo = recommConf.recommTagAlgorithm;

      if(
        recommConf.recommTagsAlgoPerRealm != null &&
        !recommConf.recommTagsAlgoPerRealm.isEmpty()){
       
        for(String realmAndAlgo : recommConf.recommTagsAlgoPerRealm){
          
          if(SSStrU.equals(SSStrU.split(realmAndAlgo, SSStrU.colon).get(0), userRealmEngine.realm)){
            algo = Algorithm.valueOf(SSStrU.split(realmAndAlgo, SSStrU.colon).get(1));
            break;
          }
        }
      }
      
      //Tags for user and resource: getEntitiesWithLikelihood(forUser,  entity,  null, 10, false, null, EntityType.TAG);  // BLLac+MPr
      //Tags for user:              getEntitiesWithLikelihood(forUser,  null,    null, 10, false, null, EntityType.TAG);  // BLL
      //Tags for resource:          getEntitiesWithLikelihood(null,     entity,  null, 10, false, null, EntityType.TAG);  // MPr
      //Tags MostPopular:           getEntitiesWithLikelihood(null,     null,    null, 10, false, null, EntityType.TAG);  // MP
      
      final Map<String, Double> tagsWithLikelihood =
        userRealmEngine.engine.getEntitiesWithLikelihood(
          SSStrU.toStr(par.forUser),
          SSStrU.toStr(par.entity),
          par.categories,
          par.maxTags,
          !par.includeOwn, //filterOwn
          algo,
          EntityType.TAG); //entity type to recommend
      
      for(Map.Entry<String, Double> tag : tagsWithLikelihood.entrySet()){
        
        tags.add(
          SSTagLikelihood.get(
            SSTagLabel.get(tag.getKey()),
            tag.getValue()));
      }
      
      return tags;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void recommResources(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSRecommResourcesPar par = (SSRecommResourcesPar) parA.getFromJSON(SSRecommResourcesPar.class);
    
    sSCon.writeRetFullToClient(SSRecommResourcesRet.get(recommResources(par)));
  }
  
  @Override
  public List<SSResourceLikelihood> recommResources(final SSRecommResourcesPar par) throws Exception{
    
    try{
      
      final SSRecommUserRealmEngine userRealmEngine = 
        SSRecommUserRealmKeeper.checkAddAndGetUserRealmEngine(
          (SSRecommConf)conf,
          par.user, 
          par.realm, 
          false,
          sqlFct);
        
      if(par.ignoreAccessRights){
        
        if(SSStrU.equals(par.realm, ((SSRecommConf)conf).fileNameForRec)){
          throw new SSErr(SSErrE.parameterMissing);
        }
      }
      
      if(!par.ignoreAccessRights){
        
        if(par.withUserRestriction){
          
          if(
            par.forUser != null &&
            !SSStrU.equals(par.user, par.forUser)){
            throw new SSErr(SSErrE.userNotAllowedToRetrieveForOtherUser);
          }
          
          if(par.entity != null){
            if(!SSServCallerU.canUserRead(par.user, par.entity)){
              return new ArrayList<>();
            }
          }
        }
      }
      
      final List<SSResourceLikelihood>  resources      = new ArrayList<>();
      final SSEntityDescriberPar        descPar;
      Integer                           entityCounter = 0;
      SSEntity                          entity;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar();
        
        descPar.setCircleTypes = par.setCircleTypes;
          
      }else{
        descPar = null;
      }
      
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

        if(par.ignoreAccessRights){
          
          resources.add(
            SSResourceLikelihood.get(
              SSEntity.get(
                SSUri.get(entityWithLikelihood.getKey()),
                SSEntityE.entity),
              entityWithLikelihood.getValue()));
        }else{
          
          entity =
            ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
              new SSEntityGetPar(
                null,
                null,
                par.user,
                SSUri.get(entityWithLikelihood.getKey()), //entity
                par.withUserRestriction, //withUserRestriction,
                descPar)); //descPar
          
          if(
            entity == null ||
            !SSRecommResourcesFct.handleType   (par, entity)){
            continue;
          }
          
          resources.add(
            SSResourceLikelihood.get(
              entity,
              entityWithLikelihood.getValue()));
        }

        if((++entityCounter) >= par.maxResources){
          break;
        }
      }
      
      return resources;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void recommLoadUserRealms (final SSServPar parA) throws Exception{
    
    try{
      
      SSRecommUserRealmKeeper.setUserRealmEnginesFromConf       (recommConf);
      SSRecommUserRealmKeeper.setAndLoadUserRealmEnginesFromDB  (sqlFct.getUserRealms());
      SSRecommUserRealmKeeper.setSSSRealmEngine                 (recommConf);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void recommUpdateBulk(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSRecommUpdateBulkPar par = (SSRecommUpdateBulkPar) parA.getFromJSON(SSRecommUpdateBulkPar.class);
    
    par.sSCon = sSCon;
    
    new Thread(new SSRecommUpdateBulkUploader(recommConf, par)).start();
  }
  
  @Override
  public void recommUpdateBulk(final SSRecommUpdateBulkPar par) throws Exception{
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSRecommUserRealmEngine userRealmEngine =
        SSRecommUserRealmKeeper.checkAddAndGetUserRealmEngine(
          (SSRecommConf)conf,
          par.user,
          par.realm,
          true,
          sqlFct);
      
      SSServCaller.dataExportUsersEntitiesTagsCategoriesTimestampsFile(
        par.user,
        SSUri.asListWithoutNullAndEmpty(),
        true,
        recommConf.usePrivateTagsToo,
        true,
        userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
      
      userRealmEngine.engine.loadFile(userRealmEngine.realm);
      
      dbSQL.commit(par.shouldCommit);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          recommUpdateBulk(par);
        }else{
          SSServErrReg.regErrThrow(error);
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void recommUpdateBulkUserRealmsFromConf(final SSRecommUpdateBulkUserRealmsFromConfPar par) throws Exception{
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(
        ((SSRecommConf)conf).recommTagsUserPerRealm == null ||
        ((SSRecommConf)conf).recommTagsUserPerRealm.isEmpty()){
        return;
      }
      
      final Map<String, List<String>> usersForRealms = new HashMap<>();
      final List<SSUri>               userURIs       = new ArrayList<>();
      String                          realm;
      String                          userEmail;
      SSRecommUserRealmEngine         userRealmEngine;
      
      for(String realmAndUser : ((SSRecommConf)conf).recommTagsUserPerRealm){
        
        realm     = SSStrU.split(realmAndUser, SSStrU.colon).get(0);
        userEmail = SSStrU.split(realmAndUser, SSStrU.colon).get(1);
        
        if(usersForRealms.containsKey(realm)){
          
          if(usersForRealms.get(realm).contains(userEmail)){
            continue;
          }
        }else{
          usersForRealms.put(realm, new ArrayList<>());
        }
        
        usersForRealms.get(realm).add(userEmail);
      }
      
      for(Map.Entry<String, List<String>> usersForRealm : usersForRealms.entrySet()){
        
        userURIs.clear();
        
        for(String user : usersForRealm.getValue()){
          
          userURIs.add(
            ((SSUserServerI) SSServReg.getServ(SSUserServerI.class)).userURIGet(
              new SSUserURIGetPar(null, null, par.user, user)));
        }
          
        SSServCaller.dataExportUsersEntitiesTagsCategoriesTimestampsFile(
          par.user,
          userURIs,
          true,
          recommConf.usePrivateTagsToo,
          true,
          usersForRealm.getKey() + SSStrU.dot + SSFileExtE.txt);
        
        for(SSUri userURI : userURIs){
          
          userRealmEngine =
            SSRecommUserRealmKeeper.checkAddAndGetUserRealmEngine(
              (SSRecommConf)conf,
              userURI,
              usersForRealm.getKey(),
              true,
              sqlFct);
          
          userRealmEngine.engine.loadFile(userRealmEngine.realm);
        }
      }
      
      dbSQL.commit(par.shouldCommit);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          recommUpdateBulkUserRealmsFromConf(par);
        }else{
          SSServErrReg.regErrThrow(error);
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
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
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSRecommUserRealmEngine userRealmEngine = 
        SSRecommUserRealmKeeper.checkAddAndGetUserRealmEngine(
          (SSRecommConf)conf,
          par.user, 
          par.realm,
          true,
          sqlFct);
      
      SSServCaller.dataExportUserEntityTagsCategoriesTimestampsLine(
        par.user,
        par.forUser,
        par.entity,
        par.tags,
        par.categories,
        userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
      
      userRealmEngine.engine.loadFile(userRealmEngine.realm);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return recommUpdate(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
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
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSRecommUserRealmEngine userRealmEngine = 
        SSRecommUserRealmKeeper.checkAddAndGetUserRealmEngine(
          (SSRecommConf)conf,
          par.user, 
          par.realm, 
          true,
          sqlFct);
      
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
        
        SSServCaller.dataExportUserEntityTagsCategoriesTimestampsLine(
          par.user,
          par.forUser,
          par.entities.get(counter),
          tags,
          categories,
          userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
      }
      
      userRealmEngine.engine.loadFile(userRealmEngine.realm);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return recommUpdateBulkEntities(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}