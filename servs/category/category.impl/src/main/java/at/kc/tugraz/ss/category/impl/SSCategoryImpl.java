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
package at.kc.tugraz.ss.category.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.kc.tugraz.ss.category.api.SSCategoryClientI;
import at.kc.tugraz.ss.category.api.SSCategoryServerI;
import at.kc.tugraz.ss.category.datatypes.SSCategory;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesAddPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesPredefinedAddPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesPredefinedGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesRemovePar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryAddPar;
import at.kc.tugraz.ss.category.datatypes.SSCategoryFrequ;
import at.kc.tugraz.ss.category.datatypes.SSCategoryLabel;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryEntitiesForCategoriesGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryFrequsGetPar;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoriesPredefinedGetRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoriesGetRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoriesRemoveRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryAddRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryEntitiesForCategoriesGetRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryFrequsGetRet;
import at.kc.tugraz.ss.category.impl.fct.userrelationgatherer.SSCategoryUserRelationGathererFct;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubURIGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityFromTypeAndLabelGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSCircleContentRemovedI;
import at.tugraz.sss.serv.SSCircleContentRemovedPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityA;
import at.tugraz.sss.serv.SSEntityContext;
import at.tugraz.sss.serv.SSEntityCopiedI;
import at.tugraz.sss.serv.SSEntityCopiedPar;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSSearchOpE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSToolContextE;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.servs.common.impl.tagcategory.SSTagAndCategoryCommonMisc;
import at.tugraz.sss.servs.common.impl.tagcategory.SSTagAndCategoryCommonSQL;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSCategoryImpl
extends SSServImplWithDBA
implements
  SSCategoryClientI,
  SSCategoryServerI,
  SSDescribeEntityI,
  SSEntityCopiedI,
  SSCircleContentRemovedI,
  SSUserRelationGathererI,
  SSUsersResourcesGathererI{
  
  final SSTagAndCategoryCommonSQL  sql;
  final SSTagAndCategoryCommonMisc commonMiscFct;
  final SSEntityServerI            entityServ;
  final SSActivityServerI          activityServ;
  final SSEvalServerI              evalServ;
  
  public SSCategoryImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sql           = new SSTagAndCategoryCommonSQL (dbSQL, SSVocConf.systemUserUri, SSEntityE.category);
    this.commonMiscFct = new SSTagAndCategoryCommonMisc(dbSQL, SSEntityE.category);
    
    this.activityServ  = (SSActivityServerI) SSServReg.getServ (SSActivityServerI.class);
    this.evalServ      = (SSEvalServerI)     SSServReg.getServ (SSEvalServerI.class);
    this.entityServ    = (SSEntityServerI)   SSServReg.getServ (SSEntityServerI.class);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(par.setCategories){
        
        entity.categories.addAll(
          categoriesGet(
            new SSCategoriesGetPar(
              par.user,
              null, //forUser,
              SSUri.asListNotNull(entity.id),
              null, //labels,
              null, //labelSearchOp
              null, //spaces,
              null,
              null, //startTime,
              par.withUserRestriction))); //withUserRestriction
      }
      
      return entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void getUserRelations(
    final List<String>             allUsers,
    final Map<String, List<SSUri>> userRelations) throws SSErr{
    
    try{
      final Map<String, List<SSUri>> usersPerCategory = new HashMap<>();
      final Map<String, List<SSUri>> usersPerEntity   = new HashMap<>();
      
      for(String user : allUsers){
        
        final SSUri userUri = SSUri.get(user);
        
        for(SSEntity category :
          categoriesGet(
            new SSCategoriesGetPar(
              userUri,
              userUri, //forUser
              null,  //entities
              null, //labels
              null, //labelSearchOp
              null, //spaces
              null, //circles
              null, //startTime
              false))){ //withUserRestriction){
          
          SSCategoryUserRelationGathererFct.addUserForCategory     (category, usersPerCategory);
          SSCategoryUserRelationGathererFct.addUserForEntity       (category, usersPerEntity);
        }
      }
      
      SSCategoryUserRelationGathererFct.addUserRelations(userRelations, usersPerCategory);
      SSCategoryUserRelationGathererFct.addUserRelations(userRelations, usersPerEntity);
      
      for(Map.Entry<String, List<SSUri>> usersPerUser : userRelations.entrySet()){
        SSStrU.distinctWithoutNull2(usersPerUser.getValue());
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void getUsersResources(
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      final SSCategoriesGetPar categoriesGetPar =
        new SSCategoriesGetPar(
          null, //user
          null, //forUser
          null, //entities
          null, //labels
          null, //labelSearchOp,
          null, //spaces
          null, //circles
          null, //startTime,
          false);  //withUserRestriction
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        categoriesGetPar.user    = userID;
        categoriesGetPar.forUser = userID;
        
        for(SSEntity categoryEntity : categoriesGet(categoriesGetPar)){
          
          usersEntities.get(user).add(
            new SSEntityContext(
              ((SSCategory) categoryEntity).entity,
              SSEntityE.category,
              SSStrU.toStr(((SSCategory) categoryEntity).categoryLabel),
              ((SSCategory) categoryEntity).creationTime));
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void circleContentRemoved(
    final SSCircleContentRemovedPar par) throws SSErr {
    
    if(!par.removeCircleSpecificMetadata){
      return;
    }
    
    for(SSUri entity : par.entities){
      
      categoriesRemove(
        new SSCategoriesRemovePar(
          par.user,
          null, //forUser
          entity,
          null, //label,
          SSSpaceE.circleSpace, //space,
          par.circle,
          par.withUserRestriction,
          false));
    }
    
  }
  
  @Override
  public void entityCopied(final SSEntityCopiedPar par) throws SSErr{
    
    try{
      if(!par.includeMetadataSpecificToEntityAndItsEntities){
        return;
      }
      
      switch(par.entity.type){
        
        case circle:{
          
          for(SSEntity category :
            categoriesGet(
              new SSCategoriesGetPar(
                par.user,
                null, //forUser
                SSUri.getDistinctNotNullFromEntities(par.entities), //entities
                null, //labels,
                null, //labelSearchOp
                SSSpaceE.asListWithoutNull(SSSpaceE.circleSpace), //spaces
                SSUri.getDistinctNotNullFromEntities(par.entity), //circles
                null, //startTime,
                par.withUserRestriction))){
            
            if(par.targetUser != null){ //user copied for others users
              
              categoryAdd(
                new SSCategoryAddPar(
                  par.targetUser,  //user
                  ((SSCategory)category).entity, //entity
                  ((SSCategory)category).categoryLabel, //label
                  ((SSCategory)category).space, //space
                  par.targetEntity, //circle
                  category.creationTime, //creationTime
                  par.withUserRestriction, //withUserRestriction
                  false)); //shouldCommmit
              
            }else{ //user merged into other circle
              
              categoryAdd(
                new SSCategoryAddPar(
                  ((SSCategory)category).user,  //user
                  ((SSCategory)category).entity, //entity
                  ((SSCategory)category).categoryLabel, //label
                  ((SSCategory)category).space, //space
                  par.targetEntity, //circle
                  category.creationTime, //creationTime
                  par.withUserRestriction, //withUserRestriction
                  false)); //shouldCommmit
            }
          }
          
          break;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public List<SSUri> categoriesAdd(final SSCategoriesAddPar par) throws SSErr {
    
    try{
      
      final List<SSUri>      categories     = new ArrayList<>();
      final SSCategoryAddPar categoryAddPar =
        new SSCategoryAddPar(
          par.user,
          par.entity,
          null, //label
          par.space,
          par.circle,
          par.creationTime,
          par.withUserRestriction,
          par.shouldCommit);
      
      for(SSCategoryLabel categoryLabel : par.labels){
        
        categoryAddPar.label = categoryLabel;
        
        SSUri.addDistinctWithoutNull(categories, categoryAdd(categoryAddPar));
      }
      
      return categories;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return categoriesAdd(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void categoryAdd(SSSocketCon sSCon, SSServPar parA) throws SSErr {
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSCategoryAddPar par = (SSCategoryAddPar) parA.getFromJSON(SSCategoryAddPar.class);
      
      final SSUri categoryUri = categoryAdd(par);
      
      sSCon.writeRetFullToClient(SSCategoryAddRet.get(categoryUri));
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          par.user,
          SSActivityE.addCategory,
          par.entity,
          null,
          SSUri.asListNotNull(categoryUri),
          null,
          null,
          par.shouldCommit));
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.categoryAdd,
          par.entity,  //entity
          SSStrU.toStr(par.label), //content,
          null, //entities
          null, //users
          par.shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSUri categoryAdd(final SSCategoryAddPar par) throws SSErr {
    
    try{
      final SSUri            categoryUri;
      final SSEntity         categoryEntity =
        entityServ.entityFromTypeAndLabelGet(
          new SSEntityFromTypeAndLabelGetPar(
            par.user,
            SSLabel.get(SSStrU.toStr(par.label)), //label,
            SSEntityE.category, //type,
            par.withUserRestriction)); //withUserRestriction
      
      if(par.circle == null){
        par.circle =
          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlePubURIGet(
            new SSCirclePubURIGetPar(
              null,
              false));
      }else{
        par.space = SSSpaceE.circleSpace;
      }
      
      if(par.space == null){
        par.space = SSSpaceE.sharedSpace;
      }
      
      switch(par.space){
        
        case circleSpace:{
          
          final SSEntity circle =
            sql.getEntityTest(
              par.user,
              par.circle,
              par.withUserRestriction);
          
          if(circle == null){
            return null;
          }
          
          break;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      par.entity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.entity, //entity
            null, //type,
            null, //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit
      
      if(par.entity == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      if(categoryEntity == null){
        
        categoryUri =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              par.user,
              SSServCaller.vocURICreate(),
              SSEntityE.category, //type,
              SSLabel.get(SSStrU.toStr(par.label)), //label
              null, //description,
              par.creationTime, //creationTime,
              null, //read,
              true, //setPublic
              true, //createIfNotExists
              par.withUserRestriction, //withUserRestriction
              false)); //shouldCommit)
        
        if(categoryUri == null){
          dbSQL.rollBack(par.shouldCommit);
          return null;
        }
        
        sql.addMetadataIfNotExists(
          categoryUri, //metadataURI
          false); //isPredefined
        
      }else{
        categoryUri = categoryEntity.id;
      }
      
      sql.addMetadataAssIfNotExists1(
        categoryUri,
        par.user,
        par.entity,
        par.space,
        par.circle,
        par.creationTime);
      
      dbSQL.commit(par.shouldCommit);
      
      return categoryUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return categoryAdd(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void categoriesRemove(SSSocketCon sSCon, SSServPar parA) throws SSErr {
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSCategoriesRemovePar par = (SSCategoriesRemovePar) parA.getFromJSON(SSCategoriesRemovePar.class);
      
      sSCon.writeRetFullToClient(SSCategoriesRemoveRet.get(categoriesRemove(par)));
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          par.user,
          SSActivityE.removeCategories,
          par.entity,
          null,
          null,
          null,
          null,
          par.shouldCommit));
      
      evalServ.evalLog(
        new SSEvalLogPar(
          par.user,
          SSToolContextE.sss,
          SSEvalLogE.categoriesRemove,
          par.entity,  //entity
          SSStrU.toStr(par.label), //content,
          null, //entities
          null, //users
          par.shouldCommit));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public Boolean categoriesRemove(final SSCategoriesRemovePar par) throws SSErr {
    
    try{
      
      if(SSObjU.isNull(par.user)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.entity != null){
        
        final SSEntity entity =
          sql.getEntityTest(
            par.user,
            par.entity,
            par.withUserRestriction);
        
        if(entity == null){
          return false;
        }
      }
      
      if(par.withUserRestriction){
        
        if(SSObjU.isNull(par.entity)){
          throw SSErr.get(SSErrE.parameterMissing);
        }
        
        if(par.circle != null){
          
          final SSEntity circle =
            sql.getEntityTest(
              par.user,
              par.circle,
              par.withUserRestriction);
          
          if(circle == null){
            return false;
          }
        }
        
        if(!SSStrU.equals(par.space, SSSpaceE.circleSpace)){
          par.circle = null;
        }
      }
      
      SSUri categoryUri = null;
      
      if(par.label != null){
        
        final SSEntity categoryEntity =
          entityServ.entityFromTypeAndLabelGet(
            new SSEntityFromTypeAndLabelGetPar(
              par.user,
              SSLabel.get(SSStrU.toStr(par.label)), //label,
              SSEntityE.category, //type,
              par.withUserRestriction)); //withUserRestriction
        
        if(categoryEntity == null){
          return true;
        }else{
          categoryUri = categoryEntity.id;
        }
      }
      
      if(!par.withUserRestriction){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sql.removeMetadataAsss(
          par.forUser,
          par.entity,
          categoryUri,
          par.space,
          par.circle);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space  == null &&
        par.entity == null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sql.removeMetadataAsss(par.user, null, categoryUri, SSSpaceE.privateSpace, par.circle);
        sql.removeMetadataAsss(par.user, null, categoryUri, SSSpaceE.sharedSpace,  par.circle);
        sql.removeMetadataAsss(par.user, null, categoryUri, SSSpaceE.circleSpace,  par.circle);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space  != null &&
        par.entity == null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sql.removeMetadataAsss(par.user, null, categoryUri, par.space, par.circle);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space    == null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sql.removeMetadataAsss (par.user, par.entity, categoryUri, SSSpaceE.privateSpace, par.circle);
        sql.removeMetadataAsss (null,     par.entity, categoryUri, SSSpaceE.sharedSpace,  par.circle);
        sql.removeMetadataAsss (null,     par.entity, categoryUri, SSSpaceE.circleSpace,  par.circle);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space    != null &&
        par.entity   != null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sql.removeMetadataAsss(null, par.entity, categoryUri, par.space, par.circle);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      throw SSErr.get(SSErrE.codeUnreachable);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return categoriesRemove(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return false;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public void categoriesPredefinedGet(final SSSocketCon sSCon, final SSServPar parA) throws SSErr {
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSCategoriesPredefinedGetPar par = (SSCategoriesPredefinedGetPar) parA.getFromJSON(SSCategoriesPredefinedGetPar.class);
      
      sSCon.writeRetFullToClient(SSCategoriesPredefinedGetRet.get(categoriesPredefinedGet(par)));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public List<String> categoriesPredefinedGet(final SSCategoriesPredefinedGetPar par) throws SSErr {
    
    try{
      return sql.getMetadata(true);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean categoriesPredefinedAdd(final SSCategoriesPredefinedAddPar par) throws SSErr {
    
    try{
      
      SSEntity categoryEntity;
      SSUri    categoryUri;
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSCategoryLabel label : par.labels){
        
        categoryEntity =
          entityServ.entityFromTypeAndLabelGet(
            new SSEntityFromTypeAndLabelGetPar(
              par.user,
              SSLabel.get(SSStrU.toStr(label)), //label,
              SSEntityE.category, //type,
              par.withUserRestriction)); //withUserRestriction
        
        if(categoryEntity != null){
          continue;
        }
        
        categoryUri =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              par.user,
              SSServCaller.vocURICreate(),
              SSEntityE.category, //type,
              SSLabel.get(SSStrU.toStr(label)), //label
              null, //description,
              null, //creationTime,
              null, //read,
              true, //setPublic
              true, //createIfNotExists
              par.withUserRestriction, //withUserRestriction
              false)); //shouldCommit)
        
        if(categoryUri == null){
          dbSQL.rollBack(par.shouldCommit);
          return false;
        }
        
        sql.addMetadataIfNotExists(
          categoryUri, //metadataURI
          true); //isPredefined
      }
      
      dbSQL.commit(par.shouldCommit);
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return categoriesPredefinedAdd(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return false;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public void categoryEntitiesForCategoriesGet(final SSSocketCon sSCon, final SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSCategoryEntitiesForCategoriesGetPar par = (SSCategoryEntitiesForCategoriesGetPar) parA.getFromJSON(SSCategoryEntitiesForCategoriesGetPar.class);
      
      sSCon.writeRetFullToClient(SSCategoryEntitiesForCategoriesGetRet.get(categoryEntitiesForCategoriesGet(par)));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public List<SSUri> categoryEntitiesForCategoriesGet(final SSCategoryEntitiesForCategoriesGetPar par) throws SSErr{
    
    try{
      
      final List<SSEntity> categoryAsss =
        categoriesGet(
          new SSCategoriesGetPar(
            par.user,
            par.forUser,
            par.entities,
            par.labels,
            par.labelSearchOp,
            par.spaces,
            par.circles,
            par.startTime,
            par.withUserRestriction));
      
      return commonMiscFct.getEntitiesFromMetadataDistinctNotNull(categoryAsss);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void categoriesGet(final SSSocketCon sSCon, final SSServPar parA) throws SSErr {
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSCategoriesGetPar par = (SSCategoriesGetPar) parA.getFromJSON(SSCategoriesGetPar.class);
      
      sSCon.writeRetFullToClient(SSCategoriesGetRet.get(categoriesGet(par)));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public List<SSEntity> categoriesGet(final SSCategoriesGetPar par) throws SSErr {
    
    try{
      
      if(par.user == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        if(
          par.forUser != null &&
          !SSStrU.equals(par.user, par.forUser)){
          throw SSErr.get(SSErrE.userNotAllowedToRetrieveForOtherUser);
        }
      }
      
      final List<SSEntity> categories =
        commonMiscFct.getMetadata(
          par.user,
          par.forUser,
          par.entities,
          SSStrU.toStr(par.labels),
          par.labelSearchOp,
          par.spaces,
          par.circles,
          par.startTime);
      
      return commonMiscFct.filterMetadataByEntitiesUserCanAccess(
        categories,
        par.withUserRestriction,
        par.user,
        par.forUser);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void categoryFrequsGet(SSSocketCon sSCon, SSServPar parA) throws SSErr {
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSCategoryFrequsGetPar par = (SSCategoryFrequsGetPar) parA.getFromJSON(SSCategoryFrequsGetPar.class);
      
      sSCon.writeRetFullToClient(SSCategoryFrequsGetRet.get(categoryFrequsGet(par)));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public List<SSCategoryFrequ> categoryFrequsGet(final SSCategoryFrequsGetPar par) throws SSErr{
    
    try{
      
      final List<SSCategoryFrequ> categoryFrequs = new ArrayList<>();
      
      for(SSEntityA categoryFrequ :
        commonMiscFct.getMetadataFrequsFromMetadata(
          categoriesGet(
            new SSCategoriesGetPar(
              par.user,
              par.forUser,
              par.entities,
              par.labels,
              SSSearchOpE.or,
              par.spaces,
              par.circles,
              par.startTime,
              par.withUserRestriction)))){
        
        categoryFrequs.add((SSCategoryFrequ) categoryFrequ);
      }
      
      return categoryFrequs;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}