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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.category.api.SSCategoryClientI;
import at.kc.tugraz.ss.category.api.SSCategoryServerI;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesAddPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesPredefinedAddPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesPredefinedGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesRemovePar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesUserGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesUserRemovePar;
import at.kc.tugraz.ss.category.datatypes.SSCategory;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryAddPar;
import at.kc.tugraz.ss.category.datatypes.SSCategoryFrequ;
import at.kc.tugraz.ss.category.datatypes.SSCategoryLabel;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryUserEditPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryUserEntitiesForCategoriesGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryUserFrequsGetPar;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoriesPredefinedGetRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoriesUserGetRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoriesUserRemoveRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryAddRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryUserEditRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryUserEntitiesForCategoriesGetRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryUserFrequsGetRet;
import at.kc.tugraz.ss.category.impl.fct.activity.SSCategoryActivityFct;
import at.kc.tugraz.ss.category.impl.fct.misc.SSCategoryMiscFct;
import at.kc.tugraz.ss.category.impl.fct.sql.SSCategorySQLFct;
import at.kc.tugraz.ss.category.impl.fct.userrelationgatherer.SSCategoryUserRelationGathererFct;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityDescriberI;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSUserRelationGathererI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sss.serv.err.datatypes.SSErrE;

public class SSCategoryImpl extends SSServImplWithDBA implements SSCategoryClientI, SSCategoryServerI, SSEntityHandlerImplI, SSEntityDescriberI, SSUserRelationGathererI{
  
  private final SSCategorySQLFct   sqlFct;
  
  public SSCategoryImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
    sqlFct = new SSCategorySQLFct   (this);
  }
  
  @Override
  public void getUserRelations(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> userRelations) throws Exception{
    
    final List<String>             labels           = new ArrayList<>();
    final List<SSUri>              entities         = new ArrayList<>();
    final Map<String, List<SSUri>> usersPerCategory = new HashMap<>();
    final Map<String, List<SSUri>> usersPerEntity   = new HashMap<>();
    List<SSCategory>               categories;
    
    for(String user : allUsers){
      
      final SSUri userUri = SSUri.get(user);
      
      categories =
        SSServCaller.categoriesUserGet(
          userUri,
          userUri,
          entities,
          labels,
          null,
          null);
      
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
  public Boolean copyUserEntity(
    final SSUri        user,
    final List<SSUri>  users,
    final SSUri        entity,
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception{
    
    return false;
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

    return null;
  }
  
  @Override
  public Boolean setUserEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri, 
    final SSEntityE      entityType,
    final SSUri          publicCircleUri) throws Exception{

    return false;
  }
  
  @Override
  public void shareUserEntity(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          entityCircleUri,
    final SSEntityE      entityType,
    final Boolean        saveActivity) throws Exception{
  }
  
  @Override
  public void addEntityToCircle(
    final SSUri        userUri, 
    final SSUri        circleUri, 
    final SSUri        entityUri, 
    final SSEntityE    entityType) throws Exception{
  }  
  
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSUri       userUri, 
    final SSEntityE   entityType,
    final SSUri       entityUri,
    final Boolean     removeUserTags,
    final Boolean     removeUserRatings,
    final Boolean     removeFromUserColls,
    final Boolean     removeUserLocations) throws Exception{
    
  }
  
  @Override
  public SSEntity getDescForEntity(
    final SSEntityDescGetPar par,
    final SSEntity           desc) throws Exception{
    
//    if(par.getTags){
//      
//      desc.tags.addAll(
//        SSStrU.toStr(
//          SSServCaller.tagsUserGet(
//            par.user, 
//            par.user,
//            SSUri.asListWithoutNullAndEmpty(par.entity), 
//            new ArrayList<String>(), 
//            null, 
//            null)));
//    }
    
    return desc;
  }
  
  @Override
  public void categoriesPredefinedGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSCategoriesPredefinedGetRet.get(categoriesPredefinedGet(parA), parA.op));
  }  
  
  @Override 
  public List<String> categoriesPredefinedGet(final SSServPar parA) throws Exception {
    
    try{
      final SSCategoriesPredefinedGetPar par = new SSCategoriesPredefinedGetPar(parA);
      
      return sqlFct.getCategories(true);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean categoriesPredefinedAdd(final SSServPar parA) throws Exception {
    
    try{
      
      final SSCategoriesPredefinedAddPar par            = new SSCategoriesPredefinedAddPar(parA);
      Boolean                            existsCategory;
      SSUri                              categoryUri;
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSCategoryLabel label : par.labels){
      
        existsCategory = sqlFct.existsCategoryLabel    (label);
        categoryUri    = sqlFct.getOrCreateCategoryURI (existsCategory, label);

        SSServCaller.entityEntityToPrivCircleAdd(
          par.user, 
          categoryUri, 
          SSEntityE.category, 
          SSLabel.get(SSStrU.toStr(label)), 
          null, 
          null, 
          false);

        sqlFct.addCategoryIfNotExists(
          categoryUri,
          true);
      }
      
      dbSQL.commit(par.shouldCommit);
            
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return categoriesPredefinedAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void categoryEntitiesForCategoriesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSCategoryUserEntitiesForCategoriesGetRet.get(categoryUserEntitiesForCategoriesGet(parA), parA.op));
  }

  @Override
  public List<SSUri> categoryUserEntitiesForCategoriesGet(final SSServPar parA) throws Exception{
    
    //TODO dtheiler: use start time for this call as well
    try{
      final SSCategoryUserEntitiesForCategoriesGetPar par = new SSCategoryUserEntitiesForCategoriesGetPar(parA);
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(
        par.forUser != null &&
        !SSStrU.equals(par.user,  par.forUser)){
        par.space = SSSpaceE.sharedSpace;
      }
      
      if(par.space == null){
        return SSCategoryMiscFct.getEntitiesForCategoriesIfSpaceNotSet(sqlFct, par);
      }
      
      if(SSSpaceE.isShared(par.space)){
        return SSCategoryMiscFct.getEntitiesForCategoriesIfSpaceSet(sqlFct, par, par.forUser);
      }
      
      if(SSSpaceE.isPrivate(par.space)){
        return SSCategoryMiscFct.getEntitiesForCategoriesIfSpaceSet(sqlFct, par, par.user);
      }
      
      throw new Exception("reached not reachable code");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void categoryAdd(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    final SSUri tagUri = categoryAdd(parA);
    
    sSCon.writeRetFullToClient(SSCategoryAddRet.get(tagUri, parA.op));

//    SSTagActivityFct.addTag(new SSTagAddPar(parA), tagUri);
  }
  
  @Override
  public SSUri categoryAdd(final SSServPar parA) throws Exception {
    
    try{
      
      final SSCategoryAddPar par               = new SSCategoryAddPar(parA);
      final Boolean          existsEntity      = SSServCaller.entityExists(par.entity);
      final Boolean          existsCategory;
      final SSUri            categoryUri;
      
      if(existsEntity){
        
        switch(SSServCaller.entityGet(par.entity).type){
          case entity: break;
          default: SSServCaller.entityUserCanRead(par.user, par.entity);
        }
      }
      
      existsCategory = sqlFct.existsCategoryLabel    (par.label);
      categoryUri    = sqlFct.getOrCreateCategoryURI (existsCategory, par.label);
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(existsEntity){
        
        if(SSStrU.equals(par.space, SSSpaceE.privateSpace)){
          
          SSServCaller.entityEntityToPrivCircleAdd(
            par.user,
            par.entity,
            SSEntityE.entity,
            null,
            null,
            null,
            false);
          
          SSServCaller.entityEntityToPrivCircleAdd(
            par.user,
            categoryUri,
            SSEntityE.category,
            SSLabel.get(SSStrU.toStr(par.label)),
            null,
            par.creationTime,
            false);
          
        }else{
          
          SSServCaller.entityEntityToPubCircleAdd(
            par.user,
            categoryUri,
            SSEntityE.category,
            SSLabel.get(SSStrU.toStr(par.label)),
            null,
            par.creationTime,
            false);
        }
      }else{
        
        SSServCaller.entityEntityToPubCircleAdd(
          par.user,
          par.entity,
          SSEntityE.entity,
          null,
          null,
          null,
          false);
        
        if(SSStrU.equals(par.space, SSSpaceE.privateSpace)){
          
          SSServCaller.entityEntityToPrivCircleAdd(
            par.user,
            categoryUri,
            SSEntityE.category,
            SSLabel.get(SSStrU.toStr(par.label)),
            null,
            par.creationTime,
            false);
          
        }else{
          SSServCaller.entityEntityToPubCircleAdd(
            par.user,
            categoryUri,
            SSEntityE.category,
            SSLabel.get(SSStrU.toStr(par.label)),
            null,
            par.creationTime,
            false);
        }
      }
      
      if(!existsCategory){
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return categoryAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void categoryEdit(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSCategoryUserEditRet.get(categoryUserEdit(parA), parA.op));
  }
  
  @Override
  public SSUri categoryUserEdit(final SSServPar parA) throws Exception {
    
    try{
      
      final SSCategoryUserEditPar par            = new SSCategoryUserEditPar (parA);
      SSUri                       newCategoryUri = null;
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      final List<SSCategory> categories =
        SSServCaller.categoriesUserGet(
          par.user,
          par.user,
          SSUri.asListWithoutNullAndEmpty(),
          SSStrU.toStrWithoutEmptyAndNull(SSServCaller.entityGet(par.category).label),
          null,
          null);
      
      for(SSCategory category : categories){
        
        SSServCaller.tagsRemove(
          par.user,
          category.entity,
          SSStrU.toStr(category.label),
          category.space,
          false);
        
        newCategoryUri =
          SSServCaller.tagAdd(
            par.user,
            category.entity,
            SSStrU.toStr(par.label),
            category.space,
            null,
            false);
      }
      
      if(newCategoryUri == null){
        return par.category;
      }else{
        return newCategoryUri;
      }
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return categoryUserEdit(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void categoriesRemove(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSCategoriesUserRemoveRet.get(categoriesUserRemove(parA), parA.op));
    
    SSCategoryActivityFct.removeCategories(new SSCategoriesUserRemovePar(parA));
  }
  
  @Override
  public Boolean categoriesUserRemove(final SSServPar parA) throws Exception {
    
    try{
      
      final SSCategoriesUserRemovePar par = new SSCategoriesUserRemovePar (parA);
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(
        par.space    == null &&
        par.entity == null){

        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeCategoryAsss(par.user, null, par.label, SSSpaceE.privateSpace);
        sqlFct.removeCategoryAsss(par.user, null, par.label, SSSpaceE.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
       if(
         par.space    != null &&
         par.entity == null){
         
         dbSQL.startTrans(par.shouldCommit);
         
         sqlFct.removeCategoryAsss(par.user, null, par.label, par.space);
         
         dbSQL.commit(par.shouldCommit);
         return true;
       }
      
      if(
        par.space    == null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeCategoryAsss (par.user, par.entity, par.label, SSSpaceE.privateSpace);
        sqlFct.removeCategoryAsss (null,     par.entity, par.label, SSSpaceE.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space    != null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
      
        sqlFct.removeCategoryAsss(null, par.entity, par.label, par.space);

        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      throw new Exception("reached not reachable code");
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return categoriesUserRemove(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void categoryFrequsGet(SSSocketCon sSCon, SSServPar par) throws Exception {
       
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSCategoryUserFrequsGetRet.get(categoryUserFrequsGet(par), par.op));
  }
  
  @Override
  public List<SSCategoryFrequ> categoryUserFrequsGet(final SSServPar parA) throws Exception {
    
    try{
      
      final SSCategoryUserFrequsGetPar par = new SSCategoryUserFrequsGetPar (parA);
      
      return SSCategoryMiscFct.getCategoryFrequsFromCategories(
        SSServCaller.categoriesUserGet(
          par.user,
          par.forUser,
          par.entities,
          SSStrU.toStrWithoutEmptyAndNull(par.labels),
          par.space,
          par.startTime),
        par.space);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> categoriesAdd(final SSServPar parA) throws Exception {
    
    try{

      final SSCategoriesAddPar par         = new SSCategoriesAddPar(parA);
      final List<SSUri>        categories  = new ArrayList<>();
      
      for(SSCategoryLabel categoryLabel : par.labels) {
        
        categories.add(
          SSServCaller.categoryAdd(
            par.user,
            par.entity,
            SSStrU.toStr(categoryLabel),
            par.space,
            par.creationTime,
            par.shouldCommit));
      }
      
      SSStrU.distinctWithoutNull2(categories);
      
      return categories;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return categoriesAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean categoriesRemove(final SSServPar parA) throws Exception{
  
    try{
      
      final SSCategoriesRemovePar par = new SSCategoriesRemovePar (parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.removeCategoryAsss (
        par.forUser, 
        par.entity, 
        par.label, 
        par.space);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return categoriesRemove(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void categoriesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSCategoriesUserGetRet.get(categoriesUserGet(parA), parA.op));
  }
  
  @Override
  public List<SSCategory> categoriesUserGet(final SSServPar parA) throws Exception {
    
    try{
      
      final SSCategoriesUserGetPar par       = new SSCategoriesUserGetPar (parA);
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(
        par.forUser != null &&
        !SSStrU.equals(par.user,  par.forUser)){
        par.space = SSSpaceE.sharedSpace;
      }
      
      if(par.space == null){
        return SSCategoryMiscFct.getCategoriesIfSpaceNotSet(sqlFct, par);
      }
      
      if(SSSpaceE.isPrivate(par.space)){
        return SSCategoryMiscFct.getCategoriesIfSpaceSet(sqlFct, par, par.user);
      }
      
      if(SSSpaceE.isShared(par.space)){
        return SSCategoryMiscFct.getCategoriesIfSpaceSet(sqlFct, par, par.forUser);
      }
      
      throw new Exception("reached not reachable code");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}