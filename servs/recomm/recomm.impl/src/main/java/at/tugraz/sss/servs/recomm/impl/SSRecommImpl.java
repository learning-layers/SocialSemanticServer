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
package at.tugraz.sss.servs.recomm.impl;

import at.tugraz.sss.serv.util.SSFileExtE;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.kc.tugraz.ss.recomm.api.SSRecommClientI;
import at.kc.tugraz.ss.recomm.api.SSRecommServerI;
import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.recomm.datatypes.SSRecommUserRealmEngine;
import at.kc.tugraz.ss.recomm.datatypes.SSResourceLikelihood;
import at.kc.tugraz.ss.recomm.datatypes.SSUserLikelihood;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommLoadUserRealmsPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommResourcesPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommTagsPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkEntitiesPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkUserRealmsFromCirclesPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkUserRealmsFromConfPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdatePar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUsersPar;
import at.kc.tugraz.ss.recomm.datatypes.ret.*;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.job.dataexport.api.SSDataExportServerI;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUserEntityTagsCategoriesTimestampsLinePar;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUsersEntitiesTagsCategoriesTimestampsFileFromCirclePar;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUsersEntitiesTagsCategoriesTimestampsFilePar;
import at.tugraz.sss.serv.conf.SSConf;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLikelihood;
import at.kc.tugraz.ss.service.user.api.SSUserServerI;
import at.kc.tugraz.ss.service.user.datatypes.*;
import at.kc.tugraz.ss.service.user.datatypes.pars.*;
import at.tugraz.sss.adapter.socket.*;
import at.tugraz.sss.serv.datatype.par.SSCirclesGetPar;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import engine.Algorithm;
import engine.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import engine.EntityRecommenderEngine;
import engine.TagRecommenderEvalEngine;
import java.io.*;
import java.util.*;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSRecommImpl 
extends 
  SSServImplWithDBA
implements 
  SSRecommClientI, 
  SSRecommServerI{
  
  private final SSUserCommons               userCommons      = new SSUserCommons();
  private final SSRecommCommons             recommCommons    = new SSRecommCommons();
  private final SSRecommTagCommons          recommTagCommons = new SSRecommTagCommons();
  private final SSRecommUserRealmKeeper     userRealmKeeper  = new SSRecommUserRealmKeeper();
  private final SSRecommSQL                 sql;
  
  public SSRecommImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql = new SSRecommSQL(dbSQL);
  }
  
  @Override
  public SSServRetI recommUsers(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);

      final SSRecommUsersPar par = (SSRecommUsersPar) parA.getFromClient(clientType, parA, SSRecommUsersPar.class);

      return SSRecommUsersRet.get(recommUsers(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUserLikelihood> recommUsers(final SSRecommUsersPar par) throws SSErr{
    
    try{
      final SSRecommUserRealmEngine userRealmEngine =
        userRealmKeeper.checkAddAndGetUserRealmEngine(
          par,
          (SSRecommConf)conf,
          par.user, //user
          par.realm, //realm
          false, //checkForUpdate
          null, //engine
          sql, //sqlFct
          false); //storeToDB
      
      if(!par.ignoreAccessRights){
        
        if(par.withUserRestriction){
          
          if(
            par.forUser != null &&
            !SSStrU.isEqual(par.user, par.forUser)){
            throw SSErr.get(SSErrE.userNotAllowedToRetrieveForOtherUser);
          }
          
          if(par.entity != null){
            
            final SSEntity entity =
              sql.getEntityTest(
                par,
                SSConf.systemUserUri,
                par.user,
                par.entity,
                par.withUserRestriction);
            
            if(entity == null){
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
        descPar = new SSEntityDescriberPar(null);
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
          ((SSRecommConf) conf).recommUserAlgorithm, //algo
          EntityType.USER);  //entity type to recommend

      final SSEntityServerI entityServ   = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class); 
      final SSEntityGetPar  entityGetPar = 
        new SSEntityGetPar(
          par,
          par.user,
          null, //entity
          par.withUserRestriction, //withUserRestriction,
          descPar); //descPar
        
      for(Map.Entry<String, Double> userWithLikelihood : usersWithLikelihood.entrySet()){
        
        if(!par.ignoreAccessRights){
          
          entityGetPar.entity = SSUri.get(userWithLikelihood.getKey());
          entity              = entityServ.entityGet(entityGetPar);
          
          if(entity == null){
            continue;
          }
          
          users.add(
            SSUserLikelihood.get(
              entity,
              userWithLikelihood.getValue()));
          
        }else{
          
          users.add(
            SSUserLikelihood.get(
              SSEntity.get(
                SSUri.get(userWithLikelihood.getKey()),
                SSEntityE.user),
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
  public SSServRetI recommTags(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSRecommTagsPar       par    = (SSRecommTagsPar) parA.getFromClient(clientType, parA, SSRecommTagsPar.class);
      final List<SSTagLikelihood> result = recommTags(par);
      final SSRecommTagsRet       ret    = SSRecommTagsRet.get(result);
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSTagLikelihood> recommTags(final SSRecommTagsPar par) throws SSErr{
    
    try{
      
      String realmToUse = null;
      
      try{
        
        if(par.realm != null){
        
          SSUri.get(par.realm);

          final String realmStr = SSStrU.removeTrailingSlash(par.realm);

          realmToUse = realmStr.substring(realmStr.lastIndexOf(SSStrU.slash) + 1, realmStr.length());
        }
        
      }catch(Exception error){
        
        SSLogU.warn("realm " + par.realm + " invalid; set to default", error);
        
        realmToUse = null;
      }
      
      final SSRecommUserRealmEngine userRealmEngine =
        userRealmKeeper.checkAddAndGetUserRealmEngine(
          par,
          (SSRecommConf) conf,
          par.user,
          realmToUse, //realm
          false, //checkForUpdate
          null, //engine
          sql, //sqlFct
          false); //storeToDB
      
      if(!recommTagCommons.checkAccessRights(par, (SSRecommConf) conf, realmToUse)){
        return new ArrayList<>();
      }
      
      final List<Map<String, Double>>    tagsPerEntity             = new ArrayList<>();
      final Map<String, Double>          tagsWithSummedLikelihoods = new HashMap<>();
      final List<SSTagLikelihood>        tags                      = new ArrayList<>();
      final Set<String>                  entitiesCategories        = new HashSet<>();
      final SSUser                       user;
      final Algorithm                    algo;
      List<String>                       entityCategories;
      
      user             = recommCommons.getUser(par, par.user);
      algo             = recommTagCommons.getRecommTagsAlgo((SSRecommConf) conf, user);
      
      if(par.entities.isEmpty()){
        
        tagsWithSummedLikelihoods.putAll(
          userRealmEngine.engine.getEntitiesWithLikelihood(
            SSStrU.toStr(par.forUser),
            null, //entity
            null, //categories,
            par.maxTags,
            !par.includeOwn, //filterOwn
            algo,
            EntityType.TAG)); //entity type to recommend
      }
      
      for(SSUri entity : par.entities){
        
        entityCategories = recommTagCommons.provideCategoryInputForRecommTags(par, entity, algo);
        
        entitiesCategories.addAll(entityCategories);
        
        tagsPerEntity.add(
          userRealmEngine.engine.getEntitiesWithLikelihood(
            SSStrU.toStr(par.forUser),
            SSStrU.toStr(entity),
            entityCategories,
            par.maxTags,
            !par.includeOwn, //filterOwn
            algo,
            EntityType.TAG)); //entity type to recommend
      }
      
      
      for(Map<String, Double> tagsWithLikelihood : tagsPerEntity){
        
        for(Map.Entry<String, Double> tag : tagsWithLikelihood.entrySet()){
          
          if(tagsWithSummedLikelihoods.containsKey(tag.getKey())){
            tagsWithSummedLikelihoods.put(tag.getKey(), tagsWithSummedLikelihoods.get(tag.getKey()) + tag.getValue());
          }else{
            tagsWithSummedLikelihoods.put(tag.getKey(), tag.getValue());
          }
        }
      }
      
      for(Map.Entry<String, Double> tag : tagsWithSummedLikelihoods.entrySet()){
        
        tags.add(
          SSTagLikelihood.get(
            SSTagLabel.get(tag.getKey()),
            tag.getValue()));
      }
      
      recommTagCommons.evalLog(
        par,
        tags,
        algo,
        entitiesCategories,
        par.shouldCommit);
      
      return tags;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI recommResources(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSRecommResourcesPar par = (SSRecommResourcesPar) parA.getFromClient(clientType, parA, SSRecommResourcesPar.class);
      
      return SSRecommResourcesRet.get(recommResources(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSResourceLikelihood> recommResources(final SSRecommResourcesPar par) throws SSErr{
    
    try{
      
      final SSRecommUserRealmEngine userRealmEngine = 
        userRealmKeeper.checkAddAndGetUserRealmEngine(
          par,
          (SSRecommConf)conf,
          par.user, 
          par.realm, //realm
          false, //checkForUpdate
          null, //engine
          sql, //sqlFct
          false); //storeToDB
        
      if(par.ignoreAccessRights){
        
        if(SSStrU.isEqual(par.realm, ((SSRecommConf)conf).fileNameForRec)){
          throw SSErr.get(SSErrE.parameterMissing);
        }
      }
      
      if(!par.ignoreAccessRights){
        
        if(par.withUserRestriction){
          
          if(
            par.forUser != null &&
            !SSStrU.isEqual(par.user, par.forUser)){
            throw SSErr.get(SSErrE.userNotAllowedToRetrieveForOtherUser);
          }
          
          if(par.entity != null){
            
            final SSEntity entity =
              sql.getEntityTest(
                par,
                SSConf.systemUserUri,
                par.user,
                par.entity,
                par.withUserRestriction);
            
            if(entity == null){
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
        descPar = new SSEntityDescriberPar(null);
        
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
          ((SSRecommConf) conf).recommResourceAlgorithm, //algo
          EntityType.RESOURCE);  //entity type to recommend

      final SSEntityServerI entityServ   = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEntityGetPar  entityGetPar = 
        new SSEntityGetPar(
          par,
          par.user,
          null, //entity
          par.withUserRestriction, //withUserRestriction,
          descPar);
      
      for(Map.Entry<String, Double> entityWithLikelihood : entitiesWithLikelihood.entrySet()){

        if(!par.ignoreAccessRights){
          
          entityGetPar.entity = SSUri.get(entityWithLikelihood.getKey());
          entity              = entityServ.entityGet(entityGetPar);
          
          if(
            entity == null ||
            !checkEntityType   (par, entity)){
            continue;
          }
          
          resources.add(
            SSResourceLikelihood.get(
              entity,
              entityWithLikelihood.getValue()));
          
        }else{
          
          resources.add(
            SSResourceLikelihood.get(
              SSEntity.get(
                SSUri.get(entityWithLikelihood.getKey()),
                SSEntityE.entity),
              entityWithLikelihood.getValue()));
        }

        if((++entityCounter) >= par.maxResources){
          break;
        }
      }
      
      evalLogResources(
        par, 
        resources, 
        ((SSRecommConf) conf).recommResourceAlgorithm, 
        par.shouldCommit);
      
      return resources;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void recommLoadUserRealms(final SSRecommLoadUserRealmsPar par) throws SSErr{
    
    try{
      
//      SSRecommUserRealmKeeper.setUserRealmEnginesFromConf       (recommConf);
      userRealmKeeper.setAndLoadUserRealmEnginesFromDB(
        SSConf.getSssWorkDirDataCsv(), 
        sql.getUserRealms(par));
      
//      SSRecommUserRealmKeeper.setSSSRealmEngine                 (recommConf);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSServRetI recommUpdateBulk(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    FileOutputStream fileOutputStream = null;
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSRecommUpdateBulkPar par = (SSRecommUpdateBulkPar) parA.getFromClient(clientType, parA, SSRecommUpdateBulkPar.class);
      
      final String                      dataCSVPath        = SSConf.getSssWorkDirDataCsv();
      final SSRecommUpdateBulkRet       result             = SSRecommUpdateBulkRet.get(true);
      byte[]                            fileChunk         = null;
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSRecommUserRealmEngine userRealmEngine =
        userRealmKeeper.checkAddAndGetUserRealmEngine(
          par,
          ((SSRecommConf) conf),
          par.user,
          par.realm,
          true, //checkForUpdate
          new EntityRecommenderEngine(), //engine
          sql,
          true);
      
      fileOutputStream = 
        SSFileU.openOrCreateFileWithPathForWrite (dataCSVPath + userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
      
      switch(par.clientType){
        
        case socket:{
          final DataInputStream             dataInputStream    = new DataInputStream   (par.clientSocket.getInputStream());
          final OutputStreamWriter          outputStreamWriter = new OutputStreamWriter(par.clientSocket.getOutputStream());
          final SSSocketAdapterU            socketAdapterU     = new SSSocketAdapterU  ();
          
          socketAdapterU.writeRetFullToClient(outputStreamWriter, result);
          
          while(true){
            
            fileChunk = SSSocketU.readByteChunk(dataInputStream);
            
            if(fileChunk.length != 0){
              
              fileOutputStream.write        (fileChunk);
              fileOutputStream.flush        ();
              continue;
            }
            
            break;
          }
          
          break;
        }
        
        case rest:{
          break;
        }
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      return result;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      if(fileOutputStream != null){
        
        try{
          fileOutputStream.close();
        }catch(IOException ex){
          SSLogU.err(ex);
        }
      }
    }
  }
  
  @Override
  public void recommUpdateBulk(final SSRecommUpdateBulkPar par) throws SSErr{
    
    try{
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSRecommUserRealmEngine userRealmEngine =
        userRealmKeeper.checkAddAndGetUserRealmEngine(
          par,
          (SSRecommConf)conf,
          par.user,
          par.realm,//realm
          true, //checkForUpdate
          new EntityRecommenderEngine(), //engine
          sql, //sqlFct
          false); //storeToDB

      final SSDataExportServerI                                      dataExportServ = (SSDataExportServerI) SSServReg.getServ(SSDataExportServerI.class);
      final SSDataExportUsersEntitiesTagsCategoriesTimestampsFilePar dataExportPar  =
        new SSDataExportUsersEntitiesTagsCategoriesTimestampsFilePar(
          par,
          par.user,
          SSUri.asListNotNull(),
          userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
     
        dataExportServ.dataExportUsersEntitiesTagsCategoriesTimestampsFile(dataExportPar);
     
      userRealmEngine.engine.loadFile(SSConf.getSssWorkDirDataCsv(), userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
      
      dbSQL.commit(par, par.shouldCommit);
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void recommUpdateBulkUserRealmsFromConf(final SSRecommUpdateBulkUserRealmsFromConfPar par) throws SSErr{
    
    try{
      
      dbSQL.startTrans(par, par.shouldCommit);
      
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
      
      final SSDataExportServerI dataExportServ = (SSDataExportServerI) SSServReg.getServ(SSDataExportServerI.class);
      
      
      
      for(Map.Entry<String, List<String>> usersForRealm : usersForRealms.entrySet()){
        
        userURIs.clear();
        
        for(String user : usersForRealm.getValue()){
          
          userURIs.add(
            ((SSUserServerI) SSServReg.getServ(SSUserServerI.class)).userURIGet(
              new SSUserURIGetPar(
                par,
                par.user, 
                user)));
        }
          
        dataExportServ.dataExportUsersEntitiesTagsCategoriesTimestampsFile(
          new SSDataExportUsersEntitiesTagsCategoriesTimestampsFilePar(
            par,
            par.user,
            userURIs,
            usersForRealm.getKey() + SSStrU.dot + SSFileExtE.txt)); //fileName
        
        for(SSUri userURI : userURIs){
          
          userRealmEngine =
            userRealmKeeper.checkAddAndGetUserRealmEngine(
              par,
              (SSRecommConf)conf,
              userURI,
              usersForRealm.getKey(), //realm
              true, //checkForUpdate
              new EntityRecommenderEngine(), //engine
              sql, //sqlFct
              false); //storeToDB
          
          userRealmEngine.engine.loadFile(SSConf.getSssWorkDirDataCsv(), userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
        }
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void recommUpdateBulkUserRealmsFromCircles(final SSRecommUpdateBulkUserRealmsFromCirclesPar par) throws SSErr{
    
    //TODO works for tag recommendations only
    try{
      
      if(
        ((SSRecommConf)conf).recommTagsUsersToSetRealmsFromCircles == null ||
        ((SSRecommConf)conf).recommTagsUsersToSetRealmsFromCircles.isEmpty()){
        return;
      }
      
      final SSUserServerI             userServ       = (SSUserServerI)       SSServReg.getServ(SSUserServerI.class);
      final SSEntityServerI           circleServ     = (SSEntityServerI)     SSServReg.getServ(SSEntityServerI.class);
      final SSDataExportServerI       dataExportServ = (SSDataExportServerI) SSServReg.getServ(SSDataExportServerI.class);
      final Map<String, List<String>> usersForRealms = new HashMap<>();
      SSUri                           userURI;
      String                          circleIDStr;
      String                          userURIStr;
      
      
      final SSUserURIGetPar userURIGetPar = 
        new SSUserURIGetPar(
          par,
          par.user,
          null); //email
      
      final SSCirclesGetPar circlesGetPar =
        new SSCirclesGetPar(
          par,
          null, //user
          null, //forUser
          null, //entity,
          null, //entityTypesToIncludeOnly,
          false, //setEntities,
          false, //setUsers
          false, //withUserRestriction,
          false, //withSystemCircles,
          false); //invokeEntityHandlers
      
      for(String userEmail : ((SSRecommConf)conf).recommTagsUsersToSetRealmsFromCircles){
        
        userURIGetPar.email    = userEmail;
        userURI                = userServ.userURIGet(userURIGetPar);
        userURIStr             = SSStrU.toStr(userURI);
        circlesGetPar.user     = userURI;
        circlesGetPar.forUser  = userURI;
          
        for(SSEntity circle : circleServ.circlesGet(circlesGetPar)){
          
          circleIDStr = SSStrU.removeTrailingSlash(circle.id);
          circleIDStr = circleIDStr.substring(circleIDStr.lastIndexOf(SSStrU.slash) + 1, circleIDStr.length());
          
          if(usersForRealms.containsKey(circleIDStr)){
            
            if(usersForRealms.get(circleIDStr).contains(userURIStr)){
              continue;
            }
          }else{
            usersForRealms.put(circleIDStr, new ArrayList<>());
          }
          
          usersForRealms.get(circleIDStr).add(userURIStr);
        }
      }
      
      final SSDataExportUsersEntitiesTagsCategoriesTimestampsFileFromCirclePar dataExportPar = 
        new SSDataExportUsersEntitiesTagsCategoriesTimestampsFileFromCirclePar(
          par,
            par.user,
            null, //circle
            null, //fileName
            false); //withUserRestriction
      
      SSRecommUserRealmEngine userRealmEngine;
      
      for(Map.Entry<String, List<String>> usersForRealm : usersForRealms.entrySet()){

        dataExportPar.circle   = SSUri.get(usersForRealm.getKey(), SSConf.sssUri);
        dataExportPar.fileName = usersForRealm.getKey() + SSStrU.dot + SSFileExtE.txt;
        
        dataExportServ.dataExportUsersEntitiesTagsCategoriesTimestampsFileFromCircle(dataExportPar);
          
        for(SSUri user : SSUri.get(usersForRealm.getValue())){
          
          userRealmEngine =
            userRealmKeeper.checkAddAndGetUserRealmEngine(
              par,
              (SSRecommConf) conf, //conf
              user, //user
              usersForRealm.getKey(), //realm
              true, //checkForUpdate
              new TagRecommenderEvalEngine(), //engine
              sql,
              false);
          
          userRealmEngine.engine.loadFile(SSConf.getSssWorkDirDataCsv(), userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
        }
      }
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSServRetI recommUpdate(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSRecommUpdatePar par = (SSRecommUpdatePar) parA.getFromClient(clientType, parA, SSRecommUpdatePar.class);
      
      return SSRecommUpdateRet.get(recommUpdate(par), parA.op);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean recommUpdate(final SSRecommUpdatePar par) throws SSErr{
    
    try{
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSRecommUserRealmEngine userRealmEngine =
        userRealmKeeper.checkAddAndGetUserRealmEngine(
          par,
          (SSRecommConf)conf,
          par.user,
          par.realm, //realm
          true, //checkForUpdate
          new EntityRecommenderEngine(), //engine
          sql, //sqlFct
          true); //storeToDB
  
      final SSDataExportServerI dataExportServ = (SSDataExportServerI) SSServReg.getServ(SSDataExportServerI.class);
      
      dataExportServ.dataExportUserEntityTagsCategoriesTimestampsLine(
        new SSDataExportUserEntityTagsCategoriesTimestampsLinePar(
          par,
          par.user,
          par.forUser,
          par.entity,
          par.tags,
          par.categories,
          userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt));
      
      userRealmEngine.engine.loadFile(SSConf.getSssWorkDirDataCsv(), userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public SSServRetI recommUpdateBulkEntities(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSRecommUpdateBulkEntitiesPar par = (SSRecommUpdateBulkEntitiesPar) parA.getFromClient(clientType, parA, SSRecommUpdateBulkEntitiesPar.class);
      
      return SSRecommUpdateRet.get(recommUpdateBulkEntities(par), parA.op);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean recommUpdateBulkEntities(final SSRecommUpdateBulkEntitiesPar par) throws SSErr{
    
    try{
      
      final SSDataExportServerI dataExportServ = (SSDataExportServerI) SSServReg.getServ(SSDataExportServerI.class);
      List<String>              tags;
      List<String>              categories;
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSRecommUserRealmEngine userRealmEngine = 
        userRealmKeeper.checkAddAndGetUserRealmEngine(
          par,
          (SSRecommConf)conf,
          par.user, 
          par.realm,//realm
          true, //checkForUpdate
          new EntityRecommenderEngine(), //engine
          sql, //sqlFct
          true); //storeToDB
      
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
        
        dataExportServ.dataExportUserEntityTagsCategoriesTimestampsLine(
          new SSDataExportUserEntityTagsCategoriesTimestampsLinePar(
            par,
            par.user,
            par.forUser,
            par.entities.get(counter),
            tags,
            categories,
            userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt));
      }
      
      userRealmEngine.engine.loadFile(SSConf.getSssWorkDirDataCsv(), userRealmEngine.realm + SSStrU.dot + SSFileExtE.txt);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  private void evalLogResources(
    final SSRecommResourcesPar       par, 
    final List<SSResourceLikelihood> resources,
    final Algorithm                  algo,
    final boolean                    shouldCommit) throws SSErr{
    
    try{
     
      final SSEvalServerI evalServ   = (SSEvalServerI) SSServReg.getServ(SSEvalServerI.class);
      final SSEvalLogPar  evalLogPar =
        new SSEvalLogPar(
          par,
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.recommResources,
          par.entity, // entity
          null, //content
          null, //entities
          SSUri.asListNotNull(par.forUser), //users
          new Date().getTime(), //creationTime
          shouldCommit);
      
      evalLogPar.query  = SSStrU.empty;
      evalLogPar.result = SSStrU.empty;
      
      final String categories = 
        SSStrU.toCommaSeparatedStrNotNull(
          SSStrU.escapeColonSemiColonComma(par.categories));
      
      final String realm = 
        SSStrU.escapeColonSemiColonComma(par.realm);
      
      evalLogPar.query += SSVarNames.algo                              + SSStrU.colon + algo                                                     + evalLogPar.creationTime;
      evalLogPar.query += SSVarNames.forEntity                         + SSStrU.colon + par.entity                                               + evalLogPar.creationTime;
      evalLogPar.query += SSVarNames.forUser                           + SSStrU.colon + par.forUser                                              + evalLogPar.creationTime;
      evalLogPar.query += SSVarNames.typesToRecommOnly                 + SSStrU.colon + SSStrU.toCommaSeparatedStrNotNull(par.typesToRecommOnly) + evalLogPar.creationTime;
      evalLogPar.query += SSVarNames.categories                        + SSStrU.colon + categories                                               + evalLogPar.creationTime;
      evalLogPar.query += SSVarNames.includeOwn                        + SSStrU.colon + par.includeOwn                                           + evalLogPar.creationTime;
      evalLogPar.query += SSVarNames.maxResources                      + SSStrU.colon + par.maxResources                                         + evalLogPar.creationTime;
      evalLogPar.query += SSVarNames.realm                             + SSStrU.colon + realm                                                    + evalLogPar.creationTime;
      evalLogPar.query += SSVarNames.ignoreAccessRights                + SSStrU.colon + par.ignoreAccessRights                                   + evalLogPar.creationTime;
      
      for(SSResourceLikelihood resource : resources){
        evalLogPar.result += SSStrU.escapeColonSemiColonComma(resource.resource.label) + SSStrU.colon + resource.likelihood + SSStrU.comma;
      }
      
      evalServ.evalLog(evalLogPar);
      
    }catch(SSErr error){
      
      switch(error.code){
        case servInvalid: SSLogU.warn(error); break;
        default: {
          SSServErrReg.regErrThrow(error);
          break;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private boolean checkEntityType(
    final SSRecommResourcesPar par,
    final SSEntity             entity) throws SSErr{
    
    try{
      //be very specific what to recommend; dont recommend entities without type
      switch(entity.type){
        case entity: return false;
      }
      
      if(
        !par.typesToRecommOnly.isEmpty() &&
        !SSStrU.contains(par.typesToRecommOnly, entity.type)){
        return false;
      }
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
}