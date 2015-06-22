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
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryEditPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryEntitiesForCategoriesGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryFrequsGetPar;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoriesPredefinedGetRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoriesGetRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoriesRemoveRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryAddRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryEditRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryEntitiesForCategoriesGetRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryFrequsGetRet;
import at.kc.tugraz.ss.category.impl.fct.activity.SSCategoryActivityFct;
import at.kc.tugraz.ss.category.impl.fct.misc.SSCategoryMiscFct;
import at.kc.tugraz.ss.category.impl.fct.sql.SSCategorySQLFct;
import at.kc.tugraz.ss.category.impl.fct.userrelationgatherer.SSCategoryUserRelationGathererFct;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivEntityAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubEntityAddPar;
import at.kc.tugraz.ss.circle.serv.SSCircleServ;
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
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;

public class SSCategoryImpl 
extends SSServImplWithDBA 
implements 
  SSCategoryClientI, 
  SSCategoryServerI, 
  SSEntityHandlerImplI, 
  SSEntityDescriberI, 
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
            userUri, 
            SSUri.asListWithoutNullAndEmpty(), 
            SSCategoryLabel.asListWithoutNullAndEmpty(), 
            null, 
            null));
        
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
  public Boolean copyEntity(
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
  public Boolean setEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri, 
    final SSEntityE      entityType,
    final SSUri          publicCircleUri) throws Exception{

    return false;
  }
  
  @Override
  public void shareEntityWithUsers(
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
    final List<SSUri>  circleUsers,
    final SSUri        entityUri, 
    final SSEntityE    entityType) throws Exception{
  }  
  
  @Override
  public void addUsersToCircle(
    final SSUri        user,
    final List<SSUri>  users,
    final SSEntityCircle        circle) throws Exception{
    
    
    
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
  public SSEntity getUserEntity(final SSEntityDescriberPar par) throws Exception{
    return par.entity;
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
      
      SSUri   categoryUri;
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSCategoryLabel label : par.labels){
      
        if(SSServCaller.entityExists(SSEntityE.category, SSLabel.get(SSStrU.toStr(label)))){
          categoryUri = SSServCaller.entityGet(SSEntityE.category, SSLabel.get(SSStrU.toStr(label))).id;
        }else{
          categoryUri = SSServCaller.vocURICreate();
        }
        
        ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
          new SSCirclePrivEntityAddPar(
            null,
            null,
            par.user,
            categoryUri,
            SSEntityE.category,
            SSLabel.get(SSStrU.toStr(label)),
            null,
            null,
            false));

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
    
    SSServCallerU.checkKey(parA);
    
    final SSUri categoryUri = categoryAdd((SSCategoryAddPar) parA.getFromJSON(SSCategoryAddPar.class));
    
    sSCon.writeRetFullToClient(SSCategoryAddRet.get(categoryUri));

//    SSTagActivityFct.addTag(new SSTagAddPar(parA), tagUri);
  }
  
  @Override
  public SSUri categoryAdd(final SSCategoryAddPar par) throws Exception {
    
    try{
      
      final Boolean          existsEntity      = SSServCaller.entityExists(par.entity);
      final SSUri            categoryUri;
      Boolean                existsCategory;
      
      if(existsEntity){
        
        switch(SSServCaller.entityGet(par.entity).type){
          case entity: break;
          default: SSServCallerU.canUserReadEntity(par.user, par.entity);
        }
      }
      
      existsCategory = SSServCaller.entityExists(SSEntityE.category, SSLabel.get(SSStrU.toStr(par.label)));
      
      if(existsCategory){
        categoryUri = SSServCaller.entityGet(SSEntityE.category, SSLabel.get(SSStrU.toStr(par.label))).id;
      }else{
        categoryUri = SSServCaller.vocURICreate();
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(existsEntity){
        
        if(SSStrU.equals(par.space, SSSpaceE.privateSpace)){
          
          ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
            new SSCirclePrivEntityAddPar(
              null,
              null,
              par.user,
              par.entity,
              SSEntityE.entity,
              null,
              null,
              null,
              false));
          
          ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
            new SSCirclePrivEntityAddPar(
              null,
              null,
              par.user,
              categoryUri,
              SSEntityE.category,
              SSLabel.get(SSStrU.toStr(par.label)),
              null,
              par.creationTime,
              false));
          
        }else{
          
          ((SSCircleServerI) SSCircleServ.inst.serv()).circlePubEntityAdd(
            new SSCirclePubEntityAddPar(
              null,
              null,
              par.user,
              categoryUri,
              false,
              SSEntityE.category,
              SSLabel.get(SSStrU.toStr(par.label)),
              null,
              par.creationTime));
        }
      }else{
        
        ((SSCircleServerI) SSCircleServ.inst.serv()).circlePubEntityAdd(
          new SSCirclePubEntityAddPar(
            null,
            null,
            par.user,
            par.entity,
            false,
            SSEntityE.entity,
            null,
            null,
            null));
        
        if(SSStrU.equals(par.space, SSSpaceE.privateSpace)){
          
          ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
            new SSCirclePrivEntityAddPar(
              null,
              null,
              par.user,
              categoryUri,
              SSEntityE.category,
              SSLabel.get(SSStrU.toStr(par.label)),
              null,
              par.creationTime,
              false));
          
        }else{
          
          ((SSCircleServerI) SSCircleServ.inst.serv()).circlePubEntityAdd(
            new SSCirclePubEntityAddPar(
              null,
              null,
              par.user,
              categoryUri,
              false,
              SSEntityE.category,
              SSLabel.get(SSStrU.toStr(par.label)),
              null,
              par.creationTime));
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
  public void categoryEdit(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
     sSCon.writeRetFullToClient(
      SSCategoryEditRet.get(
        categoryEdit((SSCategoryEditPar) parA.getFromJSON(SSCategoryEditPar.class))));
  }
  
  @Override
  public SSUri categoryEdit(final SSCategoryEditPar par) throws Exception {
    
    try{
      
      SSUri                  newCategoryUri = null;
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      final Boolean existsCategory;
      final SSUri   categoryUri;
      
      existsCategory = SSServCaller.entityExists(SSEntityE.tag, SSLabel.get(SSStrU.toStr(par.category)));
      
      if(existsCategory){
        categoryUri = SSServCaller.entityGet(SSEntityE.category, SSLabel.get(SSStrU.toStr(par.category))).id;
      }else{
        categoryUri = SSServCaller.vocURICreate();
      }
      
      final List<SSCategory> categories =
        categoriesGet(
          new SSCategoriesGetPar(
            null,
            null,
            par.user,
            par.user,
            SSUri.asListWithoutNullAndEmpty(par.entity),
            SSCategoryLabel.asListWithoutNullAndEmpty(par.category),
            null,
            null));
        
      for(SSCategory category : categories){
        
        categoriesRemove(
          new SSCategoriesRemovePar(
            null, 
            null,
            par.user,
            par.user, 
            category.entity,
            SSCategoryLabel.get(SSStrU.toStr(category.label)), 
            category.space,
            false, 
            false));
        
        newCategoryUri =
          categoryAdd(
            new SSCategoryAddPar(
              null, 
              null, 
              par.user, 
              category.entity, 
              par.label, 
              category.space, 
              null, 
              false));
      }
      
      if(newCategoryUri == null){
        return categoryUri;
      }else{
        return newCategoryUri;
      }
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return categoryEdit(par);
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
    
    sSCon.writeRetFullToClient(
      SSCategoriesRemoveRet.get(
        categoriesRemove(par)));
    
    SSCategoryActivityFct.removeCategories(par);
  }
  
  @Override
  public Boolean categoriesRemove(final SSCategoriesRemovePar par) throws Exception {
    
    try{
      
      SSUri categoryUri = null;
      
      if(par.label != null){
        
        if(SSServCaller.entityExists(SSEntityE.category, SSLabel.get(SSStrU.toStr(par.label)))){
          categoryUri = SSServCaller.entityGet(SSEntityE.category, SSLabel.get(SSStrU.toStr(par.label))).id;
        }else{
          categoryUri = SSServCaller.vocURICreate();
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
        throw new Exception("user cannot delete tags of other user");
      }
      
      if(par.forUser == null){
        par.forUser = par.user;
      }
      
      if(
        par.space    == null &&
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
      
      throw new Exception("reached not reachable code");
      
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
            par.startTime)),
        par.space);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> categoriesAdd(final SSCategoriesAddPar par) throws Exception {
    
    try{

      final List<SSUri>        categories  = new ArrayList<>();
      
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
  public void categoriesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
     sSCon.writeRetFullToClient(
      SSCategoriesGetRet.get(
        categoriesGet((SSCategoriesGetPar) parA.getFromJSON(SSCategoriesGetPar.class))));
  }
  
  @Override
  public List<SSCategory> categoriesGet(final SSCategoriesGetPar par) throws Exception {
    
    try{
      
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