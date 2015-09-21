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
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityFromTypeAndLabelGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
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
  SSUserRelationGathererI{
  
  final SSTagAndCategoryCommonSQL  sqlFct;
  final SSTagAndCategoryCommonMisc commonMiscFct;
  final SSEntityServerI            entityServ;
  final SSActivityServerI          activityServ;
  final SSEvalServerI              evalServ;
  
  public SSCategoryImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct        = new SSTagAndCategoryCommonSQL (dbSQL, SSEntityE.category);
    this.commonMiscFct = new SSTagAndCategoryCommonMisc(dbSQL, SSEntityE.category);
    
    this.activityServ  = (SSActivityServerI) SSServReg.getServ (SSActivityServerI.class);
    this.evalServ      = (SSEvalServerI)     SSServReg.getServ (SSEvalServerI.class);
    this.entityServ    = (SSEntityServerI)   SSServReg.getServ (SSEntityServerI.class);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setCategories){
        
        entity.categories.addAll(
          categoriesGet(
            new SSCategoriesGetPar(
              par.user,
              null, //forUser,
              SSUri.asListWithoutNullAndEmpty(entity.id),
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
    final Map<String, List<SSUri>> userRelations) throws Exception{
    
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
  public void circleContentRemoved(
    final SSCircleContentRemovedPar par) throws Exception {
    
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
  public void entityCopied(final SSEntityCopiedPar par) throws Exception{
    
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
  public List<SSUri> categoriesAdd(final SSCategoriesAddPar par) throws Exception {
    
    try{

      final List<SSUri> categories  = new ArrayList<>();
      
      for(SSCategoryLabel categoryLabel : par.labels) {
        
        categories.add(
          categoryAdd(
            new SSCategoryAddPar(
              par.user, 
              par.entity,
              categoryLabel, 
              par.space,
              par.circle,
              par.creationTime,
              par.withUserRestriction,
              par.shouldCommit)));
      }
      
      SSStrU.distinctWithoutNull2(categories);
      
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
  public void categoryAdd(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
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
        SSUri.asListWithoutNullAndEmpty(categoryUri),
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
  }
  
  @Override
  public SSUri categoryAdd(final SSCategoryAddPar par) throws Exception {
    
    try{
      
      final SSUri            categoryUri;
      final SSEntity         circleEntity;
      final SSEntity         categoryEntity =
        entityServ.entityFromTypeAndLabelGet(
          new SSEntityFromTypeAndLabelGetPar(
            par.user,
            SSLabel.get(SSStrU.toStr(par.label)), //label,
            SSEntityE.category, //type,
            par.withUserRestriction)); //withUserRestriction
      
      if(par.circle != null){
        par.space = SSSpaceE.circleSpace;
      }else{
        par.circle =
          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlePubURIGet(
            new SSCirclePubURIGetPar(
              null, 
              false));
      }
      
      if(par.space == null){
        par.space = SSSpaceE.sharedSpace;
      }
      
      switch(par.space){
        case circleSpace:{
          
          circleEntity =
            entityServ.entityGet(
              new SSEntityGetPar(
                par.user,
                par.circle,
                par.withUserRestriction,
                null)); //descPar
          
          if(circleEntity == null){
            return null;
          }
          
          break;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(categoryEntity != null){
        categoryUri = categoryEntity.id;
      }else{
        categoryUri = SSServCaller.vocURICreate();
        
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            categoryUri,
            SSEntityE.category, //type,
            SSLabel.get(SSStrU.toStr(par.label)), //label
            null, //description,
            par.creationTime, //creationTime,
            null, //read,
            true, //setPublic
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      }
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          par.user,
          par.entity,
          null, //type,
          null, //label
          null, //description,
          null, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      if(categoryEntity == null){
        sqlFct.addMetadataIfNotExists(
          categoryUri, 
          false);
      }
      
      sqlFct.addMetadataAssIfNotExists1(
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
  public void categoriesRemove(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
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
  }
  
  @Override
  public Boolean categoriesRemove(final SSCategoriesRemovePar par) throws Exception {
    
    try{
      
      SSUri categoryUri = null;
      
      if(par.withUserRestriction){
        
        if(SSObjU.isNull(par.user, par.entity)){
          throw new SSErr(SSErrE.parameterMissing);
        }
        
        if(par.circle != null){
          
          if(!SSServCallerU.canUserRead(par.user, par.circle)){
            return false;
          }
        }
        
        if(!SSStrU.equals(par.space, SSSpaceE.circleSpace)){
          par.circle = null;
        }
      }
      
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
        
        sqlFct.removeMetadataAsss(
          par.forUser,
          par.entity,
          categoryUri,
          par.space,
          par.circle);
        
        dbSQL.commit(par.shouldCommit);
        
        return true;
      }
      
      //check whether user can access the entity
      entityServ.entityGet(
        new SSEntityGetPar(
          par.user,
          par.entity, //entity
          par.withUserRestriction, //withUserRestriction
          null));  //descPar
      
      if(
        par.space  == null &&
        par.entity == null){

        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeMetadataAsss(par.user, null, categoryUri, SSSpaceE.privateSpace, par.circle);
        sqlFct.removeMetadataAsss(par.user, null, categoryUri, SSSpaceE.sharedSpace, par.circle);
        sqlFct.removeMetadataAsss(par.user, null, categoryUri, SSSpaceE.circleSpace, par.circle);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
       if(
         par.space    != null &&
         par.entity == null){
         
         dbSQL.startTrans(par.shouldCommit);
         
         sqlFct.removeMetadataAsss(par.user, null, categoryUri, par.space, par.circle);
         
         dbSQL.commit(par.shouldCommit);
         return true;
       }
      
      if(
        par.space    == null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeMetadataAsss (par.user, par.entity, categoryUri, SSSpaceE.privateSpace, par.circle);
        sqlFct.removeMetadataAsss (null,     par.entity, categoryUri, SSSpaceE.sharedSpace, par.circle);
        sqlFct.removeMetadataAsss (null,     par.entity, categoryUri, SSSpaceE.circleSpace, par.circle);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space    != null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
      
        sqlFct.removeMetadataAsss(null, par.entity, categoryUri, par.space, par.circle);

        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      throw new SSErr(SSErrE.codeUnreachable);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return categoriesRemove(par);
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
  public void categoriesPredefinedGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSCategoriesPredefinedGetPar par = (SSCategoriesPredefinedGetPar) parA.getFromJSON(SSCategoriesPredefinedGetPar.class);
    
    sSCon.writeRetFullToClient(SSCategoriesPredefinedGetRet.get(categoriesPredefinedGet(par)));
  }  
  
  @Override 
  public List<String> categoriesPredefinedGet(final SSCategoriesPredefinedGetPar par) throws Exception {
    
    try{
      return sqlFct.getMetadata(true);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean categoriesPredefinedAdd(final SSCategoriesPredefinedAddPar par) throws Exception {
    
    try{
      
      SSEntity categoryEntity;
      SSUri   categoryUri;
      
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
          
        categoryUri = SSServCaller.vocURICreate();

        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            categoryUri,
            SSEntityE.category, //type,
            SSLabel.get(SSStrU.toStr(label)), //label
            null, //description,
            null, //creationTime,
            null, //read,
            true, //setPublic
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)

        sqlFct.addMetadataIfNotExists(
          categoryUri,
          true);
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
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void categoryEntitiesForCategoriesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSCategoryEntitiesForCategoriesGetPar par = (SSCategoryEntitiesForCategoriesGetPar) parA.getFromJSON(SSCategoryEntitiesForCategoriesGetPar.class);
    
    sSCon.writeRetFullToClient(SSCategoryEntitiesForCategoriesGetRet.get(categoryEntitiesForCategoriesGet(par)));
  }

  @Override
  public List<SSUri> categoryEntitiesForCategoriesGet(final SSCategoryEntitiesForCategoriesGetPar par) throws Exception{
    
    try{

      final List<SSEntity> tagAsss =
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
      
      return SSTag.getEntitiesFromTagsDistinctNotNull(tagAsss);

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void categoriesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSCategoriesGetPar par = (SSCategoriesGetPar) parA.getFromJSON(SSCategoriesGetPar.class);
     
    sSCon.writeRetFullToClient(SSCategoriesGetRet.get(categoriesGet(par)));
  }
  
  @Override
  public List<SSEntity> categoriesGet(final SSCategoriesGetPar par) throws Exception {
    
    try{

      final List<SSEntity> categories = new ArrayList<>();
      
      if(par.user == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        if(
          par.forUser != null &&
          !SSStrU.equals(par.user, par.forUser)){
          throw new SSErr(SSErrE.userNotAllowedToRetrieveForOtherUser);
        }
      }
      
      if(par.spaces.isEmpty()){
        categories.addAll(
          commonMiscFct.getMetadataIfSpaceNotSet(
            par.user, 
            par.forUser,
            par.entities, 
            SSStrU.toStr(par.labels),
            par.labelSearchOp,
            par.circles, 
            par.startTime));
        
      }else{
        
        for(SSSpaceE space : par.spaces){
        
          switch(space){
            
            case privateSpace:{
              categories.addAll(
                commonMiscFct.getMetadataIfSpaceSet(
                  par.user,
                  par.entities,
                  SSStrU.toStr(par.labels),
                  par.labelSearchOp,
                  par.circles,
                  space,
                  par.startTime));
              break;
            }
            
            case sharedSpace:
            case circleSpace:{
              categories.addAll(
                commonMiscFct.getMetadataIfSpaceSet(
                  par.forUser,
                  par.entities,
                  SSStrU.toStr(par.labels),
                  par.labelSearchOp,
                  par.circles,
                  space,
                  par.startTime));
              break;
            }
          }
        }
      }
      
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
  public void categoryFrequsGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
       
    SSServCallerU.checkKey(parA);
    
    final SSCategoryFrequsGetPar par = (SSCategoryFrequsGetPar) parA.getFromJSON(SSCategoryFrequsGetPar.class);
    
    sSCon.writeRetFullToClient(SSCategoryFrequsGetRet.get(categoryFrequsGet(par)));
  }
  
  @Override
  public List<SSCategoryFrequ> categoryFrequsGet(final SSCategoryFrequsGetPar par) throws Exception {
    
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