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
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.category.api.SSCategoryClientI;
import at.kc.tugraz.ss.category.api.SSCategoryServerI;
import at.kc.tugraz.ss.category.datatypes.par.SSCategory;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryAddAtCreationTimePar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryAddPar;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryAddRet;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryDesc;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryFrequ;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryLabel;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryUserEntitiesForCategoryGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryUserFrequsGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesAddAtCreationTimePar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesAddPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesRemovePar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesUserGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesUserRemovePar;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryUserFrequsGetRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoriesUserRemoveRet;
import at.kc.tugraz.ss.category.impl.fct.misc.SSCategoryMiscFct;
import at.kc.tugraz.ss.category.impl.fct.sql.SSCategorySQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.*;

public class SSCategoryImpl extends SSServImplWithDBA implements SSCategoryClientI, SSCategoryServerI, SSEntityHandlerImplI{
  
  private final SSCategorySQLFct   sqlFct;
  
  public SSCategoryImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
    sqlFct = new SSCategorySQLFct   (this);
  }
  
  /* SSEntityHandlerImplI */

    @Override
  public SSUri copyUserEntity(
    final SSUri     user,
    final SSUri     forUser,
    final SSUri     entity,
    final SSEntityE entityType) throws Exception{
    
    return null;
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
  public Boolean shareUserEntity(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          entityCircleUri,
    final SSEntityE      entityType) throws Exception{
    
    return false;
  }
  
  @Override
  public Boolean addEntityToCircle(
    final SSUri        userUri, 
    final SSUri        circleUri, 
    final SSUri        entityUri, 
    final SSEntityE    entityType) throws Exception{
    
    return false;
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
    
    try{
      
      if(!removeUserTags){
        return;
      }
      
      SSServCaller.categoriesUserRemove(
        userUri, 
        entityUri, 
        null, 
        null, 
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSEntityDescA getDescForEntity(
    final SSEntityE       entityType,
    final SSUri           userUri, 
    final SSUri           entityUri, 
    final SSLabel         label,
    final Long            creationTime,
    final List<String>    categories, 
    final SSEntityA       overallRating,
    final List<SSUri>     discUris,
    final SSUri           author) throws Exception{
    
    try{
      
      if(!SSEntityE.equals(entityType, SSEntityE.category)){
        
        return SSEntityDesc.get(
          entityUri, 
          label, 
          creationTime, 
          categories, 
          overallRating, 
          discUris,
          author);
      }
      
      return SSCategoryDesc.get(
        entityUri,
        label,
        creationTime,
        author, 
        overallRating, 
        categories,
        discUris);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
    
  /* SSCategoryClientI */
  @Override
  public void categoryAdd(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSCategoryAddRet.get(categoryAdd(par), par.op));
    
//    saveUECategoryAdd(par);
  }

  @Override
  public void categoriesRemove(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSCategoriesUserRemoveRet.get(categoriesUserRemove(par), par.op));
    
//    saveUECategoryDelete(par);
  }

  @Override
  public void categoryFrequsGet(SSSocketCon sSCon, SSServPar par) throws Exception {
       
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSCategoryUserFrequsGetRet.get(categoryUserFrequsGet(par), par.op));
  }
  
  /* SSCategoryServerI */
  @Override
  public Boolean categoryAdd(final SSServPar parA) throws Exception {
    
    try{
      
      final SSCategoryAddPar par            = new SSCategoryAddPar(parA);
      final Boolean          existsCategory = sqlFct.existsCategoryLabel    (par.label);
      final SSUri            categoryUri    = sqlFct.getOrCreateCategoryURI (existsCategory, par.label);

      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        categoryUri,       
        SSLabel.get(SSStrU.toStr(par.label)), 
        SSEntityE.category,
        null,
        false);
      
      SSServCaller.entityAdd(
        par.user,
        par.entity,
        SSLabel.get(SSStrU.toStr(par.entity)),
        SSEntityE.entity,
        null,
        false);
      
      sqlFct.addCategoryAssIfNotExists(
        categoryUri,
        par.user,
        par.entity,
        par.space);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return categoryAdd(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean categoryAddAtCreationTime(final SSServPar parA) throws Exception {
    
    try{
      
      final SSCategoryAddAtCreationTimePar par            = new SSCategoryAddAtCreationTimePar(parA);
      final Boolean                        existsCategory = sqlFct.existsCategoryLabel   (par.label);
      final SSUri                          categoryUri    = sqlFct.getOrCreateCategoryURI (existsCategory, par.label); 

      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAddAtCreationTime(
        par.user,
        categoryUri,
        SSLabel.get(SSStrU.toStr(par.label)),
        par.creationTime,
        SSEntityE.category,
        null,
        false);
      
      SSServCaller.entityAdd(
        par.user,
        par.entity,
        SSLabel.get(par.entity.toString()),
        SSEntityE.entity,
        null,
        false);
      
      sqlFct.addCategoryAssIfNotExists(
        categoryUri, 
        par.user, 
        par.entity, 
        par.space);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return categoryAddAtCreationTime(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean categoriesAdd(final SSServPar parA) throws Exception {
    
    try{

      final SSCategoriesAddPar par    = new SSCategoriesAddPar(parA);
      
      for(SSCategoryLabel categoryLabel : par.labels) {
        
        SSServCaller.categoryAdd(
          par.user, 
          par.entity, 
          categoryLabel, 
          par.space, 
          par.shouldCommit);
      }
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return categoriesAdd(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean categoriesAddAtCreationTime(final SSServPar parA) throws Exception {
    
    try{

      final SSCategoriesAddAtCreationTimePar par    = new SSCategoriesAddAtCreationTimePar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSCategoryLabel categoryLabel : par.labels) {
       
        SSServCaller.categoryAddAtCreationTime(
          par.user, 
          par.entity, 
          SSStrU.toStr(categoryLabel), 
          par.space, 
          par.creationTime, 
          false);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return categoriesAddAtCreationTime(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
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
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return categoriesRemove(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
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
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return categoriesUserRemove(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> categoryUserEntitiesForCategoryGet(final SSServPar parA) throws Exception{
    
    final SSCategoryUserEntitiesForCategoryGetPar  par        = new SSCategoryUserEntitiesForCategoryGetPar(parA);
    
    try{
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(par.space == null){
        
        final List<SSUri> entityUris = new ArrayList<>();
        
        entityUris.addAll(
          sqlFct.getEntitiesForCategoryLabel(
          par.user,
          par.label,
          SSSpaceE.privateSpace));
        
        entityUris.addAll(
          sqlFct.getEntitiesForCategoryLabel(
          null,
          par.label,
          SSSpaceE.sharedSpace));
        
        SSStrU.distinctWithoutEmptyAndNull2(entityUris);
        
        return entityUris;
      }
      
      if(SSSpaceE.isShared(par.space)){
        
        return sqlFct.getEntitiesForCategoryLabel(
          null,
          par.label,
          par.space);
      }
        
      if(SSSpaceE.isPrivate(par.space)){
        
        return sqlFct.getEntitiesForCategoryLabel(
          par.user,
          par.label,
          par.space);
      }
      
      throw new Exception("reached not reachable code");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSCategory> categoriesUserGet(final SSServPar parA) throws Exception {
    
    final SSCategoriesUserGetPar par  = new SSCategoriesUserGetPar (parA);
    
    try{
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(par.space == null){
        
        final List<SSCategory>      categories = new ArrayList<>();
        
        categories.addAll (sqlFct.getCategoryAsss(par.user, par.entity, par.label, SSSpaceE.privateSpace));
        categories.addAll (sqlFct.getCategoryAsss(null,     par.entity, par.label, SSSpaceE.sharedSpace));
        
        return categories;
      }
      
      if(SSSpaceE.isPrivate(par.space)){
        return sqlFct.getCategoryAsss(par.user, par.entity, par.label, par.space);
      }
      
      if(SSSpaceE.isShared(par.space)){
        return sqlFct.getCategoryAsss(null, par.entity, par.label, par.space);
      }
      
      throw new Exception("reached not reachable code");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSCategoryFrequ> categoryUserFrequsGet(final SSServPar parA) throws Exception {
    
    final SSCategoryUserFrequsGetPar  par = new SSCategoryUserFrequsGetPar (parA);
    final List<SSCategory>            categories;
    
    try{
      
      categories = 
        SSServCaller.categoriesUserGet(
          par.user,
          par.entity, 
          SSStrU.toStr(par.label), 
          par.space);
      
      return SSCategoryMiscFct.getCategoryFrequsFromCategorys (categories, par.space);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}