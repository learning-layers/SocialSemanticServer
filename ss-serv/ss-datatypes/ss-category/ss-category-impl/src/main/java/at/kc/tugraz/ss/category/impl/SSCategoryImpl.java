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
import at.kc.tugraz.ss.category.datatypes.par.SSCategorysAddAtCreationTimePar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategorysAddPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategorysRemovePar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategorysUserGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategorysUserRemovePar;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryUserFrequsGetRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategorysUserRemoveRet;
import at.kc.tugraz.ss.category.impl.fct.misc.SSCategoryMiscFct;
import at.kc.tugraz.ss.category.impl.fct.sql.SSCategorySQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
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
  
  public List<SSUri> searchWithKeywordWithin(
    final SSUri         userUri,
    final SSUri         entityUri,
    final String        keyword,
    final SSEntityE     entityType) throws Exception{
    
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
      
      SSServCaller.categorysUserRemove(
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
    final List<String>    categorys, 
    final SSEntityA       overallRating,
    final List<SSUri>     discUris,
    final SSUri           author) throws Exception{
    
    try{
      
      if(!SSEntityE.equals(entityType, SSEntityE.category)){
        
        return SSEntityDesc.get(
          entityUri, 
          label, 
          creationTime, 
          categorys, 
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
        categorys,
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
    
    sSCon.writeRetFullToClient(SSCategoryAddRet.get(categoryAdd(par)));
    
//    saveUECategoryAdd(par);
  }

  @Override
  public void categorysUserRemove(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSCategorysUserRemoveRet.get(categorysUserRemove(par)));
    
//    saveUECategoryDelete(par);
  }

  @Override
  public void categoryUserFrequsGet(SSSocketCon sSCon, SSServPar par) throws Exception {
       
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSCategoryUserFrequsGetRet.get(categoryUserFrequsGet(par)));
  }
  
  /* SSCategoryServerI */
  @Override
  public Boolean categoryAdd(final SSServPar parA) throws Exception {
    
    try{
      
      final SSCategoryAddPar par            = new SSCategoryAddPar(parA);
      final Boolean          existsCategory = sqlFct.existsCategoryLabel    (par.categoryLabel);
      final SSUri            categoryUri    = sqlFct.getOrCreateCategoryURI (existsCategory, par.categoryLabel);

      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        categoryUri,       
        SSLabel.get(SSCategoryLabel.toStr(par.categoryLabel)), 
        SSEntityE.category,
        false);
      
      SSServCaller.entityAdd(
        par.user,
        par.resource,
        SSLabel.get(SSUri.toStr(par.resource)),
        SSEntityE.entity,
        false);
      
      sqlFct.addCategoryAssIfNotExists(
        categoryUri,
        par.user,
        par.resource,
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
      final Boolean                        existsCategory = sqlFct.existsCategoryLabel   (par.categoryLabel);
      final SSUri                          categoryUri    = sqlFct.getOrCreateCategoryURI (existsCategory, par.categoryLabel); 

      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAddAtCreationTime(
        par.user,
        categoryUri,
        SSLabel.get(SSStrU.toString(par.categoryLabel)),
        par.creationTime,
        SSEntityE.category,
        false);
      
      SSServCaller.entityAdd(
        par.user,
        par.resource,
        SSLabel.get(par.resource.toString()),
        SSEntityE.entity,
        false);
      
      sqlFct.addCategoryAssIfNotExists(
        categoryUri, 
        par.user, 
        par.resource, 
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
  public Boolean categorysAdd(final SSServPar parA) throws Exception {
    
    try{

      final SSCategorysAddPar par    = new SSCategorysAddPar(parA);
      
      for(SSCategoryLabel categoryLabel : par.categoryLabels) {
        
        SSServCaller.categoryAdd(
          par.user, 
          par.resource, 
          categoryLabel, 
          par.space, 
          par.shouldCommit);
      }
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return categorysAdd(parA);
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
  public Boolean categorysAddAtCreationTime(final SSServPar parA) throws Exception {
    
    try{

      final SSCategorysAddAtCreationTimePar par    = new SSCategorysAddAtCreationTimePar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSCategoryLabel categoryLabel : par.categoryLabels) {
       
        SSServCaller.categoryAddAtCreationTime(
          par.user, 
          par.resource, 
          SSCategoryLabel.toStr(categoryLabel), 
          par.space, 
          par.creationTime, 
          false);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return categorysAddAtCreationTime(parA);
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
  public Boolean categorysRemove(final SSServPar parA) throws Exception{
  
    try{
      
      final SSCategorysRemovePar par = new SSCategorysRemovePar (parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.removeCategoryAsss (
        par.forUser, 
        par.entityUri, 
        par.categoryLabel, 
        par.space);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return categorysRemove(parA);
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
  public Boolean categorysUserRemove(final SSServPar parA) throws Exception {
    
    try{
      
      final SSCategorysUserRemovePar par = new SSCategorysUserRemovePar (parA);
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(
        par.space    == null &&
        par.resource == null){

        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeCategoryAsss(par.user, null, par.categoryLabel, SSSpaceE.privateSpace);
        sqlFct.removeCategoryAsss(par.user, null, par.categoryLabel, SSSpaceE.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
       if(
         par.space    != null &&
         par.resource == null){
         
         dbSQL.startTrans(par.shouldCommit);
         
         sqlFct.removeCategoryAsss(par.user, null, par.categoryLabel, par.space);
         
         dbSQL.commit(par.shouldCommit);
         return true;
       }
      
      if(
        par.space    == null &&
        par.resource != null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeCategoryAsss (par.user, par.resource, par.categoryLabel, SSSpaceE.privateSpace);
        sqlFct.removeCategoryAsss (null,     par.resource, par.categoryLabel, SSSpaceE.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space    != null &&
        par.resource != null){
        
        dbSQL.startTrans(par.shouldCommit);
      
        sqlFct.removeCategoryAsss(null, par.resource, par.categoryLabel, par.space);

        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      throw new Exception("reached not reachable code");
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return categorysUserRemove(parA);
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
        
        final List<SSUri> entityUris = new ArrayList<SSUri>();
        
        entityUris.addAll(
          sqlFct.getEntitiesForCategoryLabel(
          par.user,
          par.categoryLabel,
          SSSpaceE.privateSpace));
        
        entityUris.addAll(
          sqlFct.getEntitiesForCategoryLabel(
          null,
          par.categoryLabel,
          SSSpaceE.sharedSpace));
        
        return SSUri.distinct(entityUris);
      }
      
      if(SSSpaceE.isShared(par.space)){
        
        return sqlFct.getEntitiesForCategoryLabel(
          null,
          par.categoryLabel,
          par.space);
      }
        
      if(SSSpaceE.isPrivate(par.space)){
        
        return sqlFct.getEntitiesForCategoryLabel(
          par.user,
          par.categoryLabel,
          par.space);
      }
      
      throw new Exception("reached not reachable code");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSCategory> categorysUserGet(final SSServPar parA) throws Exception {
    
    final SSCategorysUserGetPar par  = new SSCategorysUserGetPar (parA);
    
    try{
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(par.space == null){
        
        final List<SSCategory>      categorys = new ArrayList<SSCategory>();
        
        categorys.addAll (sqlFct.getCategoryAsss(par.user, par.resource, par.categoryLabel, SSSpaceE.privateSpace));
        categorys.addAll (sqlFct.getCategoryAsss(null,     par.resource, par.categoryLabel, SSSpaceE.sharedSpace));
        
        return categorys;
      }
      
      if(SSSpaceE.isPrivate(par.space)){
        return sqlFct.getCategoryAsss(par.user, par.resource, par.categoryLabel, par.space);
      }
      
      if(SSSpaceE.isShared(par.space)){
        return sqlFct.getCategoryAsss(null, par.resource, par.categoryLabel, par.space);
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
    final List<SSCategory>            categorys;
    
    try{
      
      categorys = 
        SSServCaller.categorysUserGet(
          par.user,
          par.resource, 
          SSCategoryLabel.toStr(par.categoryLabel), 
          par.space);
      
      return SSCategoryMiscFct.getCategoryFrequsFromCategorys (categorys, par.space);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}