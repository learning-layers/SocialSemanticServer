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

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.kc.tugraz.ss.category.api.SSCategoryClientI;
import at.kc.tugraz.ss.category.api.SSCategoryServerI;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesAddPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesPredefinedAddPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesPredefinedGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesRemovePar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesGetPar;
import at.kc.tugraz.ss.category.datatypes.SSCategory;
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
import at.kc.tugraz.ss.category.impl.fct.activity.SSCategoryActivityFct;
import at.kc.tugraz.ss.category.impl.fct.misc.SSCategoryMiscFct;
import at.kc.tugraz.ss.category.impl.fct.sql.SSCategorySQLFct;
import at.kc.tugraz.ss.category.impl.fct.userrelationgatherer.SSCategoryUserRelationGathererFct;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityFromTypeAndLabelGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSCircleContentChangedPar;
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
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;

public class SSCategoryImpl 
extends SSServImplWithDBA 
implements 
  SSCategoryClientI, 
  SSCategoryServerI, 
  SSEntityHandlerImplI, 
  SSUserRelationGathererI{
  
  private final SSCategorySQLFct   sqlFct;
  
  public SSCategoryImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    sqlFct = new SSCategorySQLFct   (this);
  }
  
  @Override
  public void getUserRelations(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> userRelations) throws Exception{
    
    final Map<String, List<SSUri>> usersPerCategory = new HashMap<>();
    final Map<String, List<SSUri>> usersPerEntity   = new HashMap<>();
    List<SSCategory>               categories;
    
    for(String user : allUsers){
      
      final SSUri userUri = SSUri.get(user);
      
      categories =
        categoriesGet(
          new SSCategoriesGetPar(
            null, 
            null,
            userUri, 
            userUri, //forUser
            null,  //entities
            null, //labels
            null, //space
            null, //startTime
            false)); //withUserRestriction
        
      for(SSCategory category : categories){
      
        SSCategoryUserRelationGathererFct.addUserForCategory     (category, usersPerCategory);
        SSCategoryUserRelationGathererFct.addUserForEntity       (category, usersPerEntity);
      }
    }
    
    SSCategoryUserRelationGathererFct.addUserRelations(userRelations, usersPerCategory);
    SSCategoryUserRelationGathererFct.addUserRelations(userRelations, usersPerEntity);
    
    for(Map.Entry<String, List<SSUri>> usersPerUser : userRelations.entrySet()){
      SSStrU.distinctWithoutNull2(usersPerUser.getValue());
    }
  }
  
  @Override
  public void copyEntity(
    final SSUri        user,
    final List<SSUri>  users,
    final SSUri        entity,
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception{
    
  }
  
  @Override
  public List<SSUri> getParentEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    return new ArrayList<>();
  }
  
  @Override
  public List<SSUri> getSubEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{

    return new ArrayList<>();
  }
  
  @Override
  public void circleContentChanged(final SSCircleContentChangedPar par) throws Exception{
    
  }
  
  @Override
  public SSEntity getUserEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    return entity;
  }
  
  @Override
  public List<SSUri> categoriesAdd(final SSCategoriesAddPar par) throws Exception {
    
    try{

      final List<SSUri> categories  = new ArrayList<>();
      
      for(SSCategoryLabel categoryLabel : par.labels) {
        
        categories.add(
          categoryAdd(
            new SSCategoryAddPar(
              null, 
              null, 
              par.user, 
              par.entity,
              categoryLabel, 
              par.space,
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
    
    final SSUri categoryUri = categoryAdd((SSCategoryAddPar) parA.getFromJSON(SSCategoryAddPar.class));
    
    sSCon.writeRetFullToClient(SSCategoryAddRet.get(categoryUri));
  }
  
  @Override
  public SSUri categoryAdd(final SSCategoryAddPar par) throws Exception {
    
    try{
      
      final SSUri            categoryUri;
      final SSEntity         categoryEntity;
      
      categoryEntity =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
          new SSEntityFromTypeAndLabelGetPar(
            null,
            null,
            par.user,
            SSLabel.get(SSStrU.toStr(par.label)), //label,
            SSEntityE.category, //type,
            par.withUserRestriction)); //withUserRestriction
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(categoryEntity != null){
        categoryUri = categoryEntity.id;
      }else{
        categoryUri = SSServCaller.vocURICreate();
      }
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          par.entity,
          null, //type,
          null, //label
          null, //description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          categoryUri,
          SSEntityE.category, //type,
          SSLabel.get(SSStrU.toStr(par.label)), //label
          null, //description,
          null, //entitiesToAttach,
          par.creationTime, //creationTime,
          null, //read,
          true, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      if(categoryEntity == null){
        sqlFct.addCategoryIfNotExists(
          categoryUri, 
          false);
      }
      
      sqlFct.addCategoryAssIfNotExists(
        categoryUri,
        par.user,
        par.entity,
        par.space,
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
    
    SSCategoryActivityFct.removeCategories(par, par.shouldCommit);
  }
  
  @Override
  public Boolean categoriesRemove(final SSCategoriesRemovePar par) throws Exception {
    
    try{
      
      SSUri categoryUri = null;
      
      if(par.withUserRestriction){
        
        if(par.entity == null){
          throw new SSErr(SSErrE.parameterMissing);
        }
      }
      
      if(par.label != null){
        
        final SSEntity categoryEntity =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
          new SSEntityFromTypeAndLabelGetPar(
            null,
            null,
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
        
        sqlFct.removeCategoryAsss(
          par.forUser,
          par.entity,
          categoryUri,
          par.space);
        
        dbSQL.commit(par.shouldCommit);
        
        return true;
      }
      
      if(
        par.forUser != null &&
        !SSStrU.equals(par.forUser, par.user)){
        throw new SSErr(SSErrE.userNotAllowedToRetrieveForOtherUser);
      }
      
      if(par.forUser == null){
        par.forUser = par.user;
      }
      
      //check whether (for)user can access the entity
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
        new SSEntityGetPar(
          null,
          null,
          par.user,
          par.entity, //entity
          par.forUser, //forUser,
          par.withUserRestriction, //withUserRestriction
          null));  //descPar
      
      if(
        par.space  == null &&
        par.entity == null){

        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeCategoryAsss(par.user, null, categoryUri, SSSpaceE.privateSpace);
        sqlFct.removeCategoryAsss(par.user, null, categoryUri, SSSpaceE.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
       if(
         par.space    != null &&
         par.entity == null){
         
         dbSQL.startTrans(par.shouldCommit);
         
         sqlFct.removeCategoryAsss(par.user, null, categoryUri, par.space);
         
         dbSQL.commit(par.shouldCommit);
         return true;
       }
      
      if(
        par.space    == null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeCategoryAsss (par.user, par.entity, categoryUri, SSSpaceE.privateSpace);
        sqlFct.removeCategoryAsss (null,     par.entity, categoryUri, SSSpaceE.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space    != null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
      
        sqlFct.removeCategoryAsss(null, par.entity, categoryUri, par.space);

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
    
    sSCon.writeRetFullToClient(
      SSCategoriesPredefinedGetRet.get(
        categoriesPredefinedGet(
          (SSCategoriesPredefinedGetPar) parA.getFromJSON(SSCategoriesPredefinedGetPar.class))));
  }  
  
  @Override 
  public List<String> categoriesPredefinedGet(final SSCategoriesPredefinedGetPar par) throws Exception {
    
    try{
      return sqlFct.getCategories(true);
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
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
          new SSEntityFromTypeAndLabelGetPar(
            null,
            null,
            par.user,
            SSLabel.get(SSStrU.toStr(label)), //label,
            SSEntityE.category, //type,
            par.withUserRestriction)); //withUserRestriction
        
        if(categoryEntity != null){
          continue;
        }
          
        categoryUri = SSServCaller.vocURICreate();

        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            par.user,
            categoryUri,
            SSEntityE.category, //type,
            SSLabel.get(SSStrU.toStr(label)), //label
            null, //description,
            null, //entitiesToAttach,
            null, //creationTime,
            null, //read,
            true, //setPublic
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)

        sqlFct.addCategoryIfNotExists(
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
    
    sSCon.writeRetFullToClient(
      SSCategoryEntitiesForCategoriesGetRet.get(
        categoryEntitiesForCategoriesGet((SSCategoryEntitiesForCategoriesGetPar) parA.getFromJSON(SSCategoryEntitiesForCategoriesGetPar.class))));
  }

  @Override
  public List<SSUri> categoryEntitiesForCategoriesGet(final SSCategoryEntitiesForCategoriesGetPar par) throws Exception{
    
    //TODO dtheiler: use start time for this call as well
    try{

      final List<SSUri> entityURIs = new ArrayList<>();
      final List<SSUri> result     = new ArrayList<>();
        
      if(par.user == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(
        par.forUser != null &&
        !SSStrU.equals(par.user,  par.forUser)){
        par.space = SSSpaceE.sharedSpace;
      }
      
      if(par.space == null){
        entityURIs.addAll(SSCategoryMiscFct.getEntitiesForCategoriesIfSpaceNotSet(sqlFct, par));
      }else{
        
        switch(par.space){
          
          case privateSpace:{
            entityURIs.addAll(SSCategoryMiscFct.getEntitiesForCategoriesIfSpaceSet(sqlFct, par, par.user));
            break;
          }
          
          case sharedSpace:{
            entityURIs.addAll(SSCategoryMiscFct.getEntitiesForCategoriesIfSpaceSet(sqlFct, par, par.forUser));
            break;
          }
        }
      }
      
      if(
        par.withUserRestriction &&
        !SSStrU.equals(par.user,  par.forUser)){
        
        for(SSUri entityURI : entityURIs){

          try{
            SSServCallerU.canUserReadEntity(par.user, entityURI);
            
            result.add(entityURI);
          }catch(Exception error){
            SSServErrReg.reset();
          }
        }
      }else{
        result.addAll(entityURIs);
      }
      
      return result;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void categoriesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
     sSCon.writeRetFullToClient(
      SSCategoriesGetRet.get(
        categoriesGet((SSCategoriesGetPar) parA.getFromJSON(SSCategoriesGetPar.class))));
  }
  
  @Override
  public List<SSCategory> categoriesGet(final SSCategoriesGetPar par) throws Exception {
    
    try{

      final List<SSCategory> categories = new ArrayList<>();
      final List<SSCategory> result     = new ArrayList<>();
      
      if(par.user == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(
        par.forUser != null &&
        !SSStrU.equals(par.user, par.forUser)){
        par.space = SSSpaceE.sharedSpace;
      }
      
      if(par.space == null){
        categories.addAll(SSCategoryMiscFct.getCategoriesIfSpaceNotSet(sqlFct, par));
      }else{
        
        switch(par.space){
        
          case privateSpace:{
            categories.addAll(SSCategoryMiscFct.getCategoriesIfSpaceSet(sqlFct, par, par.user));
            break;
          }
          
          case sharedSpace:{
            categories.addAll(SSCategoryMiscFct.getCategoriesIfSpaceSet(sqlFct, par, par.forUser));
            break;
          }
        }
      }
      
      if(
        par.withUserRestriction &&
        !SSStrU.equals(par.user, par.forUser)){
        
        for(SSCategory category : categories){
          
          try{
            SSServCallerU.canUserReadEntity(par.user, category.entity);
            
            result.add(category);
          }catch(Exception error){
            SSServErrReg.reset();
          }
        }
      }else{
        result.addAll(categories);
      }
        
      return result;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void categoryFrequsGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
       
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(
      SSCategoryFrequsGetRet.get(
        categoryFrequsGet((SSCategoryFrequsGetPar) parA.getFromJSON(SSCategoryFrequsGetPar.class))));
  }
  
  @Override
  public List<SSCategoryFrequ> categoryFrequsGet(final SSCategoryFrequsGetPar par) throws Exception {
    
    try{
      
      return SSCategoryMiscFct.getCategoryFrequsFromCategories(
        categoriesGet(
          new SSCategoriesGetPar(
            null,
            null,
            par.user,
            par.forUser,
            par.entities,
            par.labels,
            par.space,
            par.startTime,
            par.withUserRestriction)),
        par.space);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}