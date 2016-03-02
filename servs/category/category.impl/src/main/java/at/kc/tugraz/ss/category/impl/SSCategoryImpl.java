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
import at.tugraz.sss.serv.util.*;
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
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.par.SSEntityFromTypeAndLabelGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.entity.api.SSCircleContentRemovedI;
import at.tugraz.sss.serv.datatype.par.SSCircleContentRemovedPar;
import at.tugraz.sss.serv.datatype.par.SSCirclePubURIGetPar;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.enums.SSSpaceE;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.entity.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.api.SSEntityA;
import at.tugraz.sss.serv.datatype.SSEntityContext;
import at.tugraz.sss.serv.entity.api.SSEntityCopiedI;
import at.tugraz.sss.serv.datatype.par.SSEntityCopiedPar;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.entity.api.SSUserRelationGathererI;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.datatype.enums.SSSearchOpE;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.par.SSServPar;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI;
import at.tugraz.sss.serv.entity.api.SSUsersResourcesGathererI;
import at.tugraz.sss.servs.common.impl.metadata.SSTagAndCategoryCommonMisc;
import at.tugraz.sss.servs.common.impl.metadata.SSTagAndCategoryCommonSQL;
import sss.serv.eval.api.SSEvalServerI;


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
  
  private final SSTagAndCategoryCommonSQL  sql;
  private final SSTagAndCategoryCommonMisc commonMiscFct;
  private final SSEntityServerI            entityServ;
  private final SSActivityServerI          activityServ;
  private final SSEvalServerI              evalServ;
  private final SSUserCommons              userCommons;
  private final SSCategoryActAndLogFct    actAndLogFct;
  
  public SSCategoryImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql           = new SSTagAndCategoryCommonSQL (dbSQL, SSEntityE.category);
    this.commonMiscFct = new SSTagAndCategoryCommonMisc(dbSQL, SSEntityE.category);
    
    this.activityServ  = (SSActivityServerI) SSServReg.getServ (SSActivityServerI.class);
    this.evalServ      = (SSEvalServerI)     SSServReg.getServ (SSEvalServerI.class);
    this.entityServ    = (SSEntityServerI)   SSServReg.getServ (SSEntityServerI.class);
    this.userCommons   = new SSUserCommons();
    this.actAndLogFct   =
      new SSCategoryActAndLogFct(
        activityServ,
        evalServ);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(par.setCategories){
        
        entity.categories.addAll(
          categoriesGet(
            new SSCategoriesGetPar(
              servPar,
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
    final SSServPar servPar,
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
              servPar,
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
    final SSServPar servPar,
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      final SSCategoriesGetPar categoriesGetPar =
        new SSCategoriesGetPar(
          servPar, 
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
    final SSServPar servPar,
    final SSCircleContentRemovedPar par) throws SSErr {
    
    if(!par.removeCircleSpecificMetadata){
      return;
    }
    
    for(SSUri entity : par.entities){
      
      categoriesRemove(
        new SSCategoriesRemovePar(
          servPar,
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
  public void entityCopied(
    final SSServPar servPar,
    final SSEntityCopiedPar par) throws SSErr{
    
    try{
      if(!par.includeMetadataSpecificToEntityAndItsEntities){
        return;
      }
      
      switch(par.entity.type){
        
        case circle:{
          
          for(SSEntity category :
            categoriesGet(
              new SSCategoriesGetPar(
                servPar, 
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
                  servPar, 
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
                  servPar,
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
          par, 
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
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI categoryAdd(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCategoryAddPar par = (SSCategoryAddPar) parA.getFromClient(clientType, parA, SSCategoryAddPar.class);
      
      final SSUri            categoryUri = categoryAdd(par);
      final SSCategoryAddRet ret         = SSCategoryAddRet.get(categoryUri);
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri categoryAdd(final SSCategoryAddPar par) throws SSErr {
    
    try{
      final SSUri            categoryUri;
      final List<SSEntity>   categoryEntities =
        entityServ.entityFromTypeAndLabelGet(
          new SSEntityFromTypeAndLabelGetPar(
            par, 
            par.user,
            SSLabel.get(SSStrU.toStr(par.label)), //label,
            SSEntityE.category, //type,
            par.withUserRestriction)); //withUserRestriction
      
      if(par.circle == null){
        
        par.circle =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).circlePubURIGet(
            new SSCirclePubURIGetPar(
              par, 
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
              par, 
              SSConf.systemUserUri,
              par.user,
              par.circle,
              par.withUserRestriction);
          
          if(circle == null){
            return null;
          }
          
          break;
        }
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      par.entity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par, 
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      if(categoryEntities.isEmpty()){
        
        categoryUri =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              par, 
              par.user,
              SSConf.vocURICreate(),
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
          dbSQL.rollBack(par, par.shouldCommit);
          return null;
        }
        
        sql.addMetadataIfNotExists(
          par, 
          categoryUri, //metadataURI
          false); //isPredefined
        
      }else{
        categoryUri = categoryEntities.get(0).id;
      }
      
      sql.addMetadataAssIfNotExists1(
        par, 
        categoryUri,
        par.user,
        par.entity,
        par.space,
        par.circle,
        par.creationTime);
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLogFct.addCategory(
        par, 
        categoryUri,
        par.label, 
        par.shouldCommit);
      
      return categoryUri;
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI categoriesRemove(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCategoriesRemovePar par = (SSCategoriesRemovePar) parA.getFromClient(clientType, parA, SSCategoriesRemovePar.class);
      
      return SSCategoriesRemoveRet.get(categoriesRemove(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean categoriesRemove(final SSCategoriesRemovePar par) throws SSErr {
    
    try{
      
      if(SSObjU.isNull(par.user)){
        throw SSErr.get(SSErrE.parameterMissing);
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
              par, 
              SSConf.systemUserUri,
              par.user,
              par.circle,
              par.withUserRestriction);
          
          if(circle == null){
            return false;
          }
        }
        
        if(!SSStrU.isEqual(par.space, SSSpaceE.circleSpace)){
          par.circle = null;
        }
      }
      
      final List<SSUri> categoryURIs = new ArrayList<>();
      
      if(par.label != null){
        
        final List<SSEntity> categoryEntities =
          entityServ.entityFromTypeAndLabelGet(
            new SSEntityFromTypeAndLabelGetPar(
              par, 
              par.user,
              SSLabel.get(SSStrU.toStr(par.label)), //label,
              SSEntityE.category, //type,
              par.withUserRestriction)); //withUserRestriction
        
        if(categoryEntities.isEmpty()){
          return true;
        }else{
          categoryURIs.addAll(SSUri.getDistinctNotNullFromEntities(categoryEntities));
        }
      }
      
      if(!par.withUserRestriction){
        
        dbSQL.startTrans(par, par.shouldCommit);
        
        for(SSUri categoryURI: categoryURIs){
          
          sql.removeMetadataAsss(
            par,
            par.forUser,
            par.entity,
            categoryURI,
            par.space,
            par.circle);
        }
        
        dbSQL.commit(par, par.shouldCommit);
        
        actAndLogFct.removeCategories(par, par.shouldCommit);
        
        return true;
      }
      
      if(
        par.space  == null &&
        par.entity == null){
        
        dbSQL.startTrans(par, par.shouldCommit);
        
        for(SSUri categoryURI: categoryURIs){
         
          sql.removeMetadataAsss(par, par.user, null, categoryURI, SSSpaceE.privateSpace, par.circle);
          sql.removeMetadataAsss(par, par.user, null, categoryURI, SSSpaceE.sharedSpace,  par.circle);
          sql.removeMetadataAsss(par, par.user, null, categoryURI, SSSpaceE.circleSpace,  par.circle);
        }
        
        dbSQL.commit(par, par.shouldCommit);
        
        actAndLogFct.removeCategories(par, par.shouldCommit);
        
        return true;
      }
      
      if(
        par.space  != null &&
        par.entity == null){
        
        dbSQL.startTrans(par, par.shouldCommit);
        
        for(SSUri categoryURI: categoryURIs){
          sql.removeMetadataAsss(par, par.user, null, categoryURI, par.space, par.circle);
        }
        
        dbSQL.commit(par, par.shouldCommit);
        
        actAndLogFct.removeCategories(par, par.shouldCommit);
        
        return true;
      }
      
      if(
        par.space    == null &&
        par.entity != null){
        
        dbSQL.startTrans(par, par.shouldCommit);
        
        for(SSUri categoryURI: categoryURIs){
          
          sql.removeMetadataAsss (par, par.user, par.entity, categoryURI, SSSpaceE.privateSpace, par.circle);
          sql.removeMetadataAsss (par, null,     par.entity, categoryURI, SSSpaceE.sharedSpace,  par.circle);
          sql.removeMetadataAsss (par, null,     par.entity, categoryURI, SSSpaceE.circleSpace,  par.circle);
        }
        
        dbSQL.commit(par, par.shouldCommit);
        
        actAndLogFct.removeCategories(par, par.shouldCommit);
        
        return true;
      }
      
      if(
        par.space    != null &&
        par.entity   != null){
        
        dbSQL.startTrans(par, par.shouldCommit);
        
        for(SSUri categoryURI: categoryURIs){
          sql.removeMetadataAsss(par, null, par.entity, categoryURI, par.space, par.circle);
        }
        
        dbSQL.commit(par, par.shouldCommit);
        
        actAndLogFct.removeCategories(par, par.shouldCommit);
        
        return true;
      }
      
      throw SSErr.get(SSErrE.codeUnreachable);
      
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
  public SSServRetI categoriesPredefinedGet(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCategoriesPredefinedGetPar par = (SSCategoriesPredefinedGetPar) parA.getFromClient(clientType, parA, SSCategoriesPredefinedGetPar.class);

      return SSCategoriesPredefinedGetRet.get(categoriesPredefinedGet(par));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<String> categoriesPredefinedGet(final SSCategoriesPredefinedGetPar par) throws SSErr {
    
    try{
      return sql.getMetadata(par, true);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean categoriesPredefinedAdd(final SSCategoriesPredefinedAddPar par) throws SSErr {
    
    try{
      
      final List<SSEntity> categoryEntities = new ArrayList<>();
      SSUri                categoryUri;
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      for(SSCategoryLabel label : par.labels){
        
        categoryEntities.clear();
        
        categoryEntities.addAll(
          entityServ.entityFromTypeAndLabelGet(
            new SSEntityFromTypeAndLabelGetPar(
              par, 
              par.user,
              SSLabel.get(SSStrU.toStr(label)), //label,
              SSEntityE.category, //type,
              par.withUserRestriction))); //withUserRestriction
        
        if(!categoryEntities.isEmpty()){
          continue;
        }
        
        categoryUri =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              par, 
              par.user,
              SSConf.vocURICreate(),
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
          dbSQL.rollBack(par, par.shouldCommit);
          return false;
        }
        
        sql.addMetadataIfNotExists(
          par, 
          categoryUri, //metadataURI
          true); //isPredefined
      }
      
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
  public SSServRetI categoryEntitiesForCategoriesGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCategoryEntitiesForCategoriesGetPar par = (SSCategoryEntitiesForCategoriesGetPar) parA.getFromClient(clientType, parA, SSCategoryEntitiesForCategoriesGetPar.class);
      
      return SSCategoryEntitiesForCategoriesGetRet.get(categoryEntitiesForCategoriesGet(par));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> categoryEntitiesForCategoriesGet(final SSCategoryEntitiesForCategoriesGetPar par) throws SSErr{
    
    try{
      
      final List<SSEntity> categoryAsss =
        categoriesGet(
          new SSCategoriesGetPar(
            par, 
            par.user,
            par.forUser,
            par.entities,
            par.labels,
            par.labelSearchOp,
            par.spaces,
            par.circles,
            par.startTime,
            par.withUserRestriction));
      
      return commonMiscFct.getEntitiesFromMetadataDistinctNotNull(par, categoryAsss);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI categoriesGet(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCategoriesGetPar par = (SSCategoriesGetPar) parA.getFromClient(clientType, parA, SSCategoriesGetPar.class);
      
      return SSCategoriesGetRet.get(categoriesGet(par));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
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
          !SSStrU.isEqual(par.user, par.forUser)){
          throw SSErr.get(SSErrE.userNotAllowedToRetrieveForOtherUser);
        }
      }
      
      final List<SSEntity> categories =
        commonMiscFct.getMetadata(
          par, 
          par.user,
          par.forUser,
          par.entities,
          SSStrU.toStr(par.labels),
          par.labelSearchOp,
          par.spaces,
          par.circles,
          par.startTime);
      
      return commonMiscFct.filterMetadataByEntitiesUserCanAccess(
        par, 
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
  public SSServRetI categoryFrequsGet(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCategoryFrequsGetPar par = (SSCategoryFrequsGetPar) parA.getFromClient(clientType, parA, SSCategoryFrequsGetPar.class);
      
      return SSCategoryFrequsGetRet.get(categoryFrequsGet(par));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSCategoryFrequ> categoryFrequsGet(final SSCategoryFrequsGetPar par) throws SSErr{
    
    try{
      
      final List<SSCategoryFrequ> categoryFrequs = new ArrayList<>();
      
      for(SSEntityA categoryFrequ :
        commonMiscFct.getMetadataFrequsFromMetadata(
          par, 
          categoriesGet(
            new SSCategoriesGetPar(
              par, 
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