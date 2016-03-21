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
package at.tugraz.sss.servs.entity.impl;

import at.kc.tugraz.ss.category.api.SSCategoryServerI;
import at.kc.tugraz.ss.category.datatypes.SSCategoryLabel;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesAddPar;
import at.tugraz.sss.serv.datatype.par.SSCircleEntitiesAddPar;
import at.tugraz.sss.serv.datatype.par.SSEntitySharePar;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityShareRet;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityClientI;
import at.tugraz.sss.serv.datatype.par.SSEntitiesGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityDownloadsAddPar;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityRemovePar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.datatype.par.SSEntityCopyPar;
import at.tugraz.sss.serv.datatype.par.SSEntityFromTypeAndLabelGetPar;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntitiesGetRet;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityCopyRet;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityUpdateRet;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.par.SSServPar;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.entity.api.SSUserRelationGathererI;
import at.tugraz.sss.serv.entity.api.SSUsersResourcesGathererI;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import at.tugraz.sss.serv.entity.api.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.datatype.par.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.datatype.enums.SSCircleE;
import at.tugraz.sss.serv.entity.api.SSCopyEntityI;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.entity.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSEntityResultPages;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.util.SSIDU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.servs.common.impl.entity.SSEntityQueryCacheU;
import at.tugraz.sss.serv.datatype.par.SSEntitiesAccessibleGetCleanUpPar;
import at.tugraz.sss.serv.datatype.par.SSEntitiesAccessibleGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityEntitiesAttachedRemovePar;
import at.tugraz.sss.serv.datatype.par.SSEntityURIsGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUnpublicizePar;
import at.tugraz.sss.serv.datatype.ret.SSEntitiesAccessibleGetRet;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityTypesGetRet;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityUnpublicizeRet;
import java.util.Arrays;
import java.util.HashMap;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.par.SSCircleAddEntitiesToCircleOfEntityPar;
import at.tugraz.sss.serv.datatype.par.SSCircleCreateFromClientPar;
import at.tugraz.sss.serv.datatype.par.SSCircleCreatePar;
import at.tugraz.sss.serv.datatype.par.SSCircleEntitiesRemoveFromClientPar;
import at.tugraz.sss.serv.datatype.par.SSCircleEntitiesRemovePar;
import at.tugraz.sss.serv.datatype.par.SSCircleGetPar;
import at.tugraz.sss.serv.datatype.par.SSCircleIsEntityPrivatePar;
import at.tugraz.sss.serv.datatype.par.SSCircleIsEntityPublicPar;
import at.tugraz.sss.serv.datatype.par.SSCircleIsEntitySharedPar;
import at.tugraz.sss.serv.datatype.par.SSCircleRemovePar;
import at.tugraz.sss.serv.datatype.par.SSCircleTypeChangePar;
import at.tugraz.sss.serv.datatype.par.SSCircleTypesGetPar;
import at.tugraz.sss.serv.datatype.par.SSCircleUsersAddPar;
import at.tugraz.sss.serv.datatype.par.SSCircleUsersInvitePar;
import at.tugraz.sss.serv.datatype.par.SSCircleUsersRemovePar;
import at.tugraz.sss.serv.datatype.par.SSCirclesGetPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleCreateRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesAddRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleTypeChangeRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersAddRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersInviteRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersRemoveRet;
import at.tugraz.sss.serv.conf.SSConf;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.kc.tugraz.ss.service.user.api.SSUserServerI;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUsersGetPar;
import at.tugraz.sss.serv.datatype.par.SSCircleContentRemovedPar;
import at.tugraz.sss.serv.datatype.par.SSCirclePrivURIGetPar;
import at.tugraz.sss.serv.datatype.par.SSCirclePubURIGetPar;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.db.api.SSCoreSQL;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.datatype.par.SSEntityAttachEntitiesPar;
import at.tugraz.sss.serv.datatype.SSCircle;
import at.tugraz.sss.serv.datatype.SSEntityContext;
import at.tugraz.sss.serv.datatype.par.SSEntityCopiedPar;
import at.tugraz.sss.serv.datatype.par.SSEntityDownloadURIsGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityTypesGetPar;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.datatype.ret.SSServRetI;
import at.tugraz.sss.servs.entity.datatype.*;

public class SSEntityImpl
extends SSServImplWithDBA
implements
  SSEntityClientI,
  SSEntityServerI,
  SSUserRelationGathererI,
  SSDescribeEntityI,
  SSAddAffiliatedEntitiesToCircleI,
  SSUsersResourcesGathererI,
  SSCopyEntityI{
  
  protected static final Map<String, SSEntityResultPages> accessibleEntitiesPagesCache = new HashMap<>();
  
  private static SSUri             pubCircleUri = null;
  private final  SSEntityActAndLog actAndLog    = new SSEntityActAndLog();
  private final  SSUserCommons     userCommons  = new SSUserCommons();
  private final  SSCoreSQL         sql;
  
  public SSEntityImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql = new SSCoreSQL(dbSQL);
  }
  
  @Override
  public void copyEntity(
    final SSServPar servPar,
    final SSEntity                  entity,
    final SSEntityCopyPar           par) throws SSErr{
    
    try{
      
      switch(entity.type){
        case circle: break;
        default: return;
      }
      
      if(par.withUserRestriction){
        
        if(isPrivPubOrSystemGroupCircle(servPar, entity.id)){ //former: !sql.isGroupOrPubCircleCircle(servPar, entity.id)){
          return;
        }
        
        if(par.targetEntity != null){
          
          if(isPrivPubOrSystemGroupCircle(servPar, par.targetEntity)){ //former: !sql.isGroupOrPubCircleCircle(servPar, par.targetEntity)){
            return;
          }
        }
      }
      
      final SSCircle circle =
        circleGet(
          new SSCircleGetPar(
            servPar,
            par.user,
            entity.id,
            null, //entityTypesToIncludeOnly
            false,  //setTags
            null, //tagSpace
            true, //setEntities
            true, //setUsers,
            par.withUserRestriction,
            true)); //invokeEntityHandlers
      
      if(par.targetEntity == null){
        copyCircleToNewCircle(par, circle);
      }else{
        copyCircleToExistingCircle(par, circle);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public List<SSEntity> addAffiliatedEntitiesToCircle(
    final SSServPar servPar,
    final SSAddAffiliatedEntitiesToCirclePar par) throws SSErr{
    
    try{
      
      final List<SSUri>    affiliatedURIs     = new ArrayList<>();
      final List<SSEntity> affiliatedEntities = new ArrayList<>();
      
      for(SSEntity entityAdded : par.entities){
        
        for(SSUri attachedEntity : sql.getAttachedEntities(servPar, entityAdded.id)){
          
          if(SSStrU.contains(par.recursiveEntities, attachedEntity)){
            continue;
          }
          
          SSUri.addDistinctWithoutNull(
            affiliatedURIs,
            attachedEntity);
        }
      }
      
      if(affiliatedURIs.isEmpty()){
        return affiliatedEntities;
      }
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities,
        entitiesGet(
          new SSEntitiesGetPar(
            servPar,
            par.user,
            affiliatedURIs, //entities
            null, //descPar
            par.withUserRestriction)));
      
      circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          servPar,
          par.user,
          par.circle, //circle
          affiliatedURIs, //entities
          false, //withUserRestriction
          false)); //shouldCommit
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities,
        SSServReg.inst.addAffiliatedEntitiesToCircle(
          servPar,
          par.user,
          par.circle,
          affiliatedEntities, //entities
          par.recursiveEntities,
          par.withUserRestriction));
      
      return affiliatedEntities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    if(par.setCircles){
      
      switch(entity.type){
        
        case user:{
          
          entity.circles.addAll(
            circlesGet(
              new SSCirclesGetPar(
                servPar,
                par.user, //user
                entity.id, //forUser
                null, //entity
                null, //entityTypesToIncludeOnly
                true, //setEntities,
                true, //setUsers,
                par.withUserRestriction, //withUserRestriction
                false,  //withSystemCircles
                false))); //invokeEntityHandlers
          
          break;
        }
        
        default:{
          
          entity.circles.addAll(
            circlesGet(
              new SSCirclesGetPar(
                servPar,
                par.user, //user
                null, //forUser
                entity.id, //entity
                null, //entityTypesToIncludeOnly
                true, //setEntities,
                true, //setUsers,
                par.withUserRestriction, //withUserRestriction
                false,  //withSystemCircles
                false))); //invokeEntityHandlers
          
          break;
        }
      }
    }
    
    if(par.setCircleTypes){
      
      entity.circleTypes.addAll(
        circleTypesGet(
          new SSCircleTypesGetPar(
            servPar,
            par.user,
            entity.id,
            par.withUserRestriction)));
    }
    
    if(par.setAttachedEntities){
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entity.attachedEntities,
        entitiesGet(
          new SSEntitiesGetPar(
            servPar,
            par.user,
            sql.getAttachedEntities(servPar, entity.id), //entities
            null, //descPar,
            par.withUserRestriction)));
    }
    
    if(par.setRead){
      entity.read = sql.getEntityRead (servPar, par.user, entity.id);
    }
    
    switch(entity.type){
      
      case circle:{
        
        if(SSStrU.isEqual(entity, par.recursiveEntity)){
          return entity;
        }
        
        final SSCircle circle =
          circleGet(
            new SSCircleGetPar(
              servPar,
              par.user,
              entity.id,                    //circle
              par.entityTypesToIncludeOnly, //entityTypesToIncludeOnly
              false,  //setTags
              null, //tagSpace
              true, //setEntities
              true, //setUsers
              par.withUserRestriction,  //withUserRestriction
              false));                   //invokeEntityHandlers
        
        return SSCircle.get(circle, entity);
      }
    }
    
    return entity;
  }
  
  @Override
  public void getUsersResources(
    final SSServPar                          servPar,
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      final List<SSEntityE> types = new ArrayList<>();
      
      types.add(SSEntityE.evernoteNote);
      types.add(SSEntityE.evernoteNotebook);
      types.add(SSEntityE.evernoteResource);
      types.add(SSEntityE.placeholder);
      types.add(SSEntityE.uploadedFile);
      types.add(SSEntityE.entity);
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        for(SSUri entity :
          sql.getAccessibleURIs(
            servPar,
            SSConf.systemUserUri,
            userID,
            types,
            null, //startTime
            null)){ //endTime
          
          usersEntities.get(user).add(
            new SSEntityContext(
              entity,
              SSEntityE.entity,
              null,
              null));
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void getUserRelations(
    final SSServPar servPar,
    final List<String>             allUsers,
    final Map<String, List<SSUri>> userRelations) throws SSErr{
    
    try{
      SSEntityUserRelationsGatherFct.getUserRelations(
        servPar,
        allUsers,
        userRelations);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSServRetI entityCopy(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSEntityCopyPar par    = (SSEntityCopyPar) parA.getFromClient(clientType, parA, SSEntityCopyPar.class);
      final boolean         worked = entityCopy(par);
      final SSEntityCopyRet ret    = SSEntityCopyRet.get(worked);
      
      return ret;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean entityCopy(final SSEntityCopyPar par) throws SSErr{
    
    try{
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSEntity entity =
        entityGet(
          new SSEntityGetPar(
            par,
            par.user,
            par.entity,
            par.withUserRestriction,
            null));
      
      if(entity == null){
        return false;
      }
      
      SSServReg.inst.copyEntity(par, entity);
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.copyEntity(
        par,
        par.user,
        par.entity,
        par.targetEntity,
        par.forUsers,
        par.comment,
        par.shouldCommit);
      
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
  
  private SSEntitiesAccessibleGetRet handleAccessibleEntitiesPageRequest(
    final SSEntitiesAccessibleGetPar par) throws SSErr{
    
    try{
      
      final SSEntityResultPages     pages = accessibleEntitiesPagesCache.get(par.pagesID);
      final List<SSUri>             page;
      
      if(pages == null){
        throw SSErr.get(SSErrE.queryResultOutDated);
      }
      
      try{
        page = pages.pages.get(par.pageNumber - 1);
      }catch(Exception error){
        SSServErrReg.regErrThrow(SSErrE.queryPageInvalid, error);
        return null;
      }
      
      return SSEntitiesAccessibleGetRet.get(
        entitiesGet(
          new SSEntitiesGetPar(
            par,
            par.user,
            page, //entities
            par.descPar, //descPar,
            par.withUserRestriction)),
        par.pagesID,
        par.pageNumber,
        pages.pages.size());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entitiesAccessibleGetCleanUp(final SSEntitiesAccessibleGetCleanUpPar par) throws SSErr{
    
    try{
      SSEntityQueryCacheU.entityQueryCacheClean(accessibleEntitiesPagesCache);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSServRetI entitiesAccessibleGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSEntitiesAccessibleGetPar par = (SSEntitiesAccessibleGetPar) parA.getFromClient(clientType, parA, SSEntitiesAccessibleGetPar.class);
      
      return entitiesAccessibleGet(par);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSEntitiesAccessibleGetRet entitiesAccessibleGet (final SSEntitiesAccessibleGetPar par) throws SSErr{
    
    try{
      
      if(par.pagesID != null){
        return handleAccessibleEntitiesPageRequest(par);
      }
      
      final List<List<SSUri>>       pages                         = new ArrayList<>();
      final List<SSUri>             page                          = new ArrayList<>();
      final String                  pagesID                       = SSIDU.uniqueID();
      
      final List<SSUri> entityURIs =
        entityURIsGet(
          new SSEntityURIsGetPar(
            par,
            par.user,
            null, //entities
            true, //getAccessible
            par.types, //types
            par.authors, //authors
            par.startTime, //startTime
            par.endTime)); //endTime
      
      if(entityURIs.isEmpty()){
        
        return SSEntitiesAccessibleGetRet.get(
          new ArrayList<>(),
          null,
          0,
          0);
      }
      
      for(SSUri entityURI : entityURIs){//entityServ.entitiesGet(entitiesGetPar)){
        
        if(
          page.size() == par.pageSize &&
          par.pageSize != 0){
          
          pages.add(new ArrayList<>(page));
          page.clear();
        }
        
        page.add(entityURI);
      }
      
      if(
        !page.isEmpty() ||
        par.pageSize == 0){
        pages.add(new ArrayList<>(page));
      }
      
      accessibleEntitiesPagesCache.put(
        pagesID,
        SSEntityResultPages.get(
          pages,
          SSDateU.dateAsLong(),
          pagesID));
      
      return SSEntitiesAccessibleGetRet.get(
        entitiesGet(
          new SSEntitiesGetPar(
            par,
            par.user,
            pages.get(0),
            par.descPar,
            false)),
        pagesID,
        1,
        pages.size());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  
//  if(
//        authors != null &&
//        !authors.isEmpty()){
//        
//        final MultivaluedMap<String, String> whereAuthors = new MultivaluedHashMap<>();
//        
//        for(SSUri author : authors){
//          where(whereAuthors, SSEntitySQLTableE.entity,        SSSQLVarNames.author, author);
//        }
//
//        wheres.add(whereAuthors);
//      }
  
  
  @Override
  public List<SSUri> entityURIsGet (final SSEntityURIsGetPar par) throws SSErr{
    
    try{
      
      if(
        !par.getAccessible  &&
        par.types.isEmpty() &&
        par.authors.isEmpty()){
        
        return par.entities;
      }
      
      final List<SSUri> result = new ArrayList<>();
      
      if(par.getAccessible){
        
        result.addAll(
          sql.getAccessibleURIs(
            par,
            SSConf.systemUserUri,
            par.user,
            par.types,
            par.startTime,
            par.endTime));
        
        return sql.filterEntitiesByAllAuthors(
          par, 
          result, 
          par.authors);
      }
      
      if(
        !par.types.isEmpty() ||
        !par.authors.isEmpty()){
        
        result.addAll(
          sql.getEntityURIs(
            par,
            par.entities,
            par.types,
            par.startTime,
            par.endTime));
        
        return sql.filterEntitiesByAllAuthors(
          par, 
          result, 
          par.authors);
      }
      
      return par.entities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI entitiesGet(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSEntitiesGetPar par = (SSEntitiesGetPar) parA.getFromClient(clientType, parA, SSEntitiesGetPar.class);
      
      return SSEntitiesGetRet.get(entitiesGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> entitiesGet(final SSEntitiesGetPar par) throws SSErr{
    
    try{
      
      final List<SSUri> entityURIs =
        entityURIsGet(
          new SSEntityURIsGetPar(
            par,
            par.user,
            par.entities,
            false, //getAccessible
            par.types,
            par.authors,
            null, //startTime
            null)); //endTime
      
      final SSEntityGetPar entityGetPar =
        new SSEntityGetPar(
          par,
          par.user,
          null, //entity
          par.withUserRestriction, //withUserRestriction
          par.descPar); //descPar
      
      final List<SSEntity> result = new ArrayList<>();
      
      for(SSUri entityURI : entityURIs){
        
        entityGetPar.entity = entityURI;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          result,
          entityGet(entityGetPar));
      }
      
      return result;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI entityGet(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSEntityGetPar par = (SSEntityGetPar) parA.getFromClient(clientType, parA, SSEntityGetPar.class);
      
      return SSEntityGetRet.get(entityGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSEntity entityGet(final SSEntityGetPar par) throws SSErr{
    
    try{
      
      SSEntity entity =
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          par.user,
          par.entity,
          par.withUserRestriction);
      
      if(entity == null){
        return null;
      }
      
      if(
        par.descPar   == null &&
        entity.author != null){
        
        entity.author = 
          sql.getEntityTest(
            par, 
            SSConf.systemUserUri,
            null, 
            entity.author.id, 
            false);
      }
      
      if(par.descPar != null){
        
        par.descPar.user                = par.user;
        par.descPar.withUserRestriction = par.withUserRestriction;
        
        entity = 
          SSServReg.inst.describeEntity(
            par, 
            par.user, 
            entity, 
            par.descPar, 
            par.withUserRestriction);
      }
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> entityFromTypeAndLabelGet(final SSEntityFromTypeAndLabelGetPar par) throws SSErr{
    
    try{
      
      if(SSObjU.isNull(par.type, par.label)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      return entitiesGet(
        new SSEntitiesGetPar(
          par,
          par.user,
          SSUri.getDistinctNotNullFromEntities(sql.getEntities(par, par.label, par.type)),
          null, // descPar,
          par.withUserRestriction));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI entityUpdate(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSEntityUpdatePar par = (SSEntityUpdatePar) parA.getFromClient(clientType, parA, SSEntityUpdatePar.class);
      final SSUri             entityURI;
      boolean                 isPlaceholderAdd = false;
      
      if(
        par.entity == null &&
        par.type   == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.entity == null){
        
        switch(par.type){
          
          case placeholder:{
            
            par.entity = SSConf.vocURICreate();
            
            isPlaceholderAdd = true;
            break;
          }
          
          default: throw SSErr.get(SSErrE.entityTypeNotSupported);
        }
      }
      
      entityURI = entityUpdate(par);
      
      final SSEntityUpdateRet ret = SSEntityUpdateRet.get(entityURI);
      
      if(isPlaceholderAdd){
        
        actAndLog.createPlaceHolder(
          par,
          par.user,
          par.entity, 
          par.shouldCommit);
      }
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityUpdate(final SSEntityUpdatePar par) throws SSErr{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      SSEntity entity =
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          null, //user
          par.entity, //entity
          false); //withUserRestriction
      
      if(
        entity != null &&
        par.withUserRestriction &&
        !par.addUserToAdditionalAuthors){
        
        entity =
          sql.getEntityTest(
            par,
            SSConf.systemUserUri,
            par.user, //user
            par.entity, //entity
            true); //withUserRestriction
        
        if(entity == null){
          throw SSErr.get(SSErrE.userNotAllowedToAccessEntity);
        }
      }
      
      if(
        entity == null &&
        !par.createIfNotExists){
        
        return null;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      SSEntityE type = par.type;
      
      if(
        entity == null &&
        type   == null){
        
        type = SSEntityE.entity;
      }
      
      sql.addEntityIfNotExists(
        par,
        par.entity,
        type,
        par.label,
        par.description,
        par.user,
        par.creationTime);
      
      if(
        entity == null ||
        par.addUserToAdditionalAuthors){
        
        final SSUri privateCircleURI =
          circlePrivURIGet(
            new SSCirclePrivURIGetPar(
              par,
              par.user,
              false)); //shouldCommit
        
        sql.addEntityToCircleIfNotExists(
          par,
          privateCircleURI,
          par.entity);
        
        sql.addUserToAdditionalAuthors(
          par, 
          par.user,
          par.entity, 
          par.creationTime);
      }
      
      if(par.read != null){
        
        sql.setEntityRead(
          par,
          par.user,
          par.entity,
          par.read);
      }
      
      if(par.setPublic){
        
        final SSUri entityURI =
          entityShare(
            new SSEntitySharePar(
              par,
              par.user,
              par.entity, //entity,
              null, //users,
              null, //circles,
              true, //setPublic,
              null, //comment,
              par.withUserRestriction,
              false)); //shouldCommit))
        
        if(entityURI == null){
          dbSQL.rollBack(par, par.shouldCommit);
          return null;
        }
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
//      actAndLogFct.entityUpdate(
//        par,
//        par.user,
//        par.storeLogs,
//        entity,
//        par.label,
//        par.description,
//        par.shouldCommit);
      
      return par.entity;
      
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
  public SSUri entityRemove(final SSEntityRemovePar par) throws SSErr{
    
    try{
      
      sql.deleteEntityIfExists(par, par.entity);
      
      return par.entity;
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
  public SSServRetI entityEntitiesAttach(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSEntityAttachEntitiesPar par = (SSEntityAttachEntitiesPar) parA.getFromClient(clientType, parA, SSEntityAttachEntitiesPar.class);
      
      return new SSEntityAttachEntitiesRet(entityEntitiesAttach(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityEntitiesAttach(final SSEntityAttachEntitiesPar par) throws SSErr{
    
    try{
      
      final SSEntity entity =
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          par.user,
          par.entity,
          par.withUserRestriction);
      
      if(entity == null){
        return null;
      }
      
      SSUri entityToAttachURI;
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      for(SSUri entityToAttach : par.entities){
        
        entityToAttachURI =
          entityUpdate(
            new SSEntityUpdatePar(
              par,
              par.user,
              entityToAttach,
              SSEntityE.entity,
              null, //label,
              null, //description,
              null, //creationTime,
              null, //read,
              false, //setPublic,
              true, //createIfNotExists
              par.withUserRestriction, //withUserRestriction,
              false)); //shouldCommit
        
        if(entityToAttachURI == null){
          dbSQL.rollBack(par, par.shouldCommit);
          return null;
        }
      }
      
      sql.attachEntities(par, par.entity, par.entities);
      
      circleAddEntitiesToCirclesOfEntity(
        new SSCircleAddEntitiesToCircleOfEntityPar(
          par,
          par.user,
          par.entity,
          par.entities,
          par.withUserRestriction,
          true, //invokeEntityHandlers,
          false)); //shouldCommit
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.attachEntities(
        par,
        par.user,
        par.entity,
        par.entities,
        par.shouldCommit);
      
      return par.entity;
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
  public SSUri entityEntitiesAttachedRemove(final SSEntityEntitiesAttachedRemovePar par) throws SSErr{
    
    try{
      
      final SSEntity entity =
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          par.user,
          par.entity,
          par.withUserRestriction);
      
      if(entity == null){
        return null;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.removeAttachedEntities(par, par.entity, par.entities);
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.removeEntities(
        par,
        par.user,
        par.entity,
        par.entities,
        par.shouldCommit);
      
      return par.entity;
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
  public SSUri entityDownloadsAdd(final SSEntityDownloadsAddPar par) throws SSErr{
    
    try{
      
      final SSEntity entity =
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          par.user,
          par.entity,
          par.withUserRestriction);
      
      if(entity == null){
        return null;
      }
      
      SSUri downloadURI;
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      for(SSUri download : par.downloads){
        
        downloadURI =
          entityUpdate(
            new SSEntityUpdatePar(
              par,
              par.user,
              download,
              SSEntityE.entity,
              null, //label,
              null, //description,
              null, //creationTime,
              null, //read,
              false, //setPublic,
              true, //createIfNotExists
              par.withUserRestriction, //withUserRestriction,
              false)); //shouldCommit
        
        if(downloadURI == null){
          dbSQL.rollBack(par, par.shouldCommit);
          return null;
        }
      }
      
      sql.addDownloads(par, par.entity, par.downloads);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return par.entity;
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
  public List<SSUri> entityDownloadsGet(final SSEntityDownloadURIsGetPar par) throws SSErr{
    
    try{
      
      final List<SSUri> downloads = new ArrayList<>();
      
      SSEntity entity =
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          par.user,
          par.entity,
          par.withUserRestriction);
      
      if(entity == null){
        return downloads;
      }
      
      SSEntity downloadEntity;
      
      for(SSUri download : sql.getDownloads(par, par.entity)){
        
        downloadEntity =
          sql.getEntityTest(
            par,
            SSConf.systemUserUri,
            par.user,
            download,
            par.withUserRestriction);
        
        if(downloadEntity == null){
          continue;
        }
        
        SSUri.addDistinctWithoutNull(
          downloads,
          downloadEntity.id);
      }
      
      return downloads;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI entityShare(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSEntitySharePar par       = (SSEntitySharePar) parA.getFromClient(clientType, parA, SSEntitySharePar.class);
      final SSUri            entityURI = entityShare(par);
      final SSEntityShareRet ret       = SSEntityShareRet.get(entityURI);
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityShare(final SSEntitySharePar par) throws SSErr{
    
    try{
      
      if(
        par.users.isEmpty()   &&
        par.circles.isEmpty() &&
        !par.setPublic){
        
        return par.entity;
      }
      
      if(par.withUserRestriction){
        
        if(par.user == null){
          throw SSErr.get(SSErrE.parameterMissing);
        }
      }
      
      final SSEntity entity =
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          par.user,
          par.entity,
          par.withUserRestriction);
      
      if(entity == null){
        return null;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      if(!par.users.isEmpty()){
        
        new SSEntityShareWithUsers(this).handle(
          par,
          par.user,
          SSCircleE.group,
          entity,
          par.users,
          par.withUserRestriction);
      }
      
      if(!par.circles.isEmpty()){
        
        new SSEntityShareWithCircles(this).handle(
          par,
          par.user,
          entity,
          par.circles,
          par.withUserRestriction);
      }
      
      if(par.setPublic){
        
        new SSEntitySetPublic(this).handle(
          par,
          par.user,
          entity,
          par.withUserRestriction);
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      if(!par.users.isEmpty()){
        
        actAndLog.shareEntityWithUsers(
          par, 
          par.shouldCommit);
      }
      
      if(!par.circles.isEmpty()){
        
        actAndLog.shareEntityWithCircles(
          par, 
          par.shouldCommit);
      }
      
      return par.entity;
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
  public SSServRetI entityUnpublicize(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSEntityUnpublicizePar par = (SSEntityUnpublicizePar) parA.getFromClient(clientType, parA, SSEntityUnpublicizePar.class);
      
      final SSUri entityURI = entityUnpublicize(par);
      
      return SSEntityUnpublicizeRet.get(entityURI);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityUnpublicize(final SSEntityUnpublicizePar par) throws SSErr{
    
    try{
      
      if(par.withUserRestriction){
        
        if(par.user == null){
          throw SSErr.get(SSErrE.parameterMissing);
        }
        
        if(!sql.isUserAuthor(par, par.user, par.entity)){
          return null;
        }
      }
      
      final SSEntity entity =
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          par.user,
          par.entity,
          par.withUserRestriction);
      
      if(entity == null){
        return null;
      }
      
      final SSUri pubCircleURI =
        circlePubURIGet(
          new SSCirclePubURIGetPar(
            par,
            par.user,
            par.shouldCommit));
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.removeEntityFromCircle(
        par,
        pubCircleURI,
        par.entity);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return par.entity;
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
  public SSServRetI entityTypesGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSEntityTypesGetPar par = (SSEntityTypesGetPar) parA.getFromClient(clientType, parA, SSEntityTypesGetPar.class);
      
      return SSEntityTypesGetRet.get(entityTypesGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntityE> entityTypesGet(final SSEntityTypesGetPar par) throws SSErr{
    
    try{
      return Arrays.asList(SSEntityE.values());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI circleEntitiesRemove(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCircleEntitiesRemoveFromClientPar par    = (SSCircleEntitiesRemoveFromClientPar) parA.getFromClient(clientType, parA, SSCircleEntitiesRemoveFromClientPar.class);
      final List<SSUri>                         result;
      
      
      if(isPrivPubOrSystemGroupCircle(parA, par.circle)){ //former: !sql.isGroupOrPubCircleCircle(par, par.circle)){
        result = new ArrayList<>();
      }else{
        result = circleEntitiesRemove(par);
      }
      
      if(!result.isEmpty()){
        
        final SSCircleContentRemovedPar circleContentRemovedPar =
          new SSCircleContentRemovedPar(
            par.user,
            par.circle, //circle
            result, //entities
            par.removeCircleSpecificMetadata, //removeCircleSpecificMetadata
            par.withUserRestriction, //withUserRestriction
            par.shouldCommit);
        
        SSServReg.inst.circleContentRemoved(par, circleContentRemovedPar);
      }
      
      return SSCircleEntitiesRemoveRet.get(result);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> circleEntitiesRemove(final SSCircleEntitiesRemovePar par) throws SSErr{
    
    try{
      
      final SSEntity circle =
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          par.user,
          par.circle,
          par.withUserRestriction);
      
      if(circle == null){
        return new ArrayList<>();
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      for(SSUri entity : par.entities){
        sql.removeEntityFromCircle(par, par.circle, entity);
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.removeEntitiesFromCircle(
        par, 
        isPrivPubOrSystemGroupCircle(par, par.circle),
        par.shouldCommit);
      
      return par.entities;
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
  public SSServRetI circleUsersRemove(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCircleUsersRemovePar par    = (SSCircleUsersRemovePar) parA.getFromClient(clientType, parA, SSCircleUsersRemovePar.class);
      final List<SSUri>            result;
      
      if(isPrivPubOrSystemGroupCircle(parA, par.circle)){ //former: !sql.isGroupOrPubCircleCircle(par, par.circle)){
        result = new ArrayList<>();
      }else{
        result = circleUsersRemove(par);
      }
      
      return SSCircleUsersRemoveRet.get(result);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> circleUsersRemove(final SSCircleUsersRemovePar par) throws SSErr{
    
    try{
      
      final SSEntity circle =
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          par.user,
          par.circle,
          par.withUserRestriction);
      
      if(circle == null){
        return new ArrayList<>();
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      for(SSUri user : par.users){
        sql.removeUser(par, par.circle, user);
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.removeUsersFromCircle(
        par, 
        isPrivPubOrSystemGroupCircle(par, par.circle),
        par.shouldCommit);
      
      return par.users;
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
  public SSServRetI circleCreate(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCircleCreateFromClientPar par       = (SSCircleCreateFromClientPar) parA.getFromClient(clientType, parA, SSCircleCreateFromClientPar.class);
      final SSUri                       circleURI = circleCreate(par);
      
      if(!par.entities.isEmpty()){
        circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            par,
            par.user,
            circleURI,
            par.entities,
            par.withUserRestriction, //withUserRestriction
            par.shouldCommit)); //shouldCommit
      }
      
      if(!par.users.isEmpty()){
        circleUsersAdd(
          new SSCircleUsersAddPar(
            par,
            par.user,
            circleURI,
            par.users,
            par.withUserRestriction, //withUserRestriction,
            par.shouldCommit)); //shouldCommit));
      }
      
      if(!par.invitees.isEmpty()){
        circleUsersInvite(
          new SSCircleUsersInvitePar(
            par,
            par.user,
            circleURI,
            par.invitees,
            par.withUserRestriction,
            par.shouldCommit));
      }
      
      final SSCircle circle =
        circleGet(
          new SSCircleGetPar(
            par,
            par.user,
            circleURI, //circle
            null, //entityTypesToIncludeOnly,
            false,  //setTags
            null, //tagSpace
            true, //setEntities,
            true, //setUsers,
            par.withUserRestriction, //withUserRestriction,
            false)); //invokeEntityHandlers
      
      SSServReg.inst.circleEntitiesAdded(
        par,
        par.user,
        circle,
        circle.entities,
        par.withUserRestriction);
      
      return SSCircleCreateRet.get(circleURI);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circleCreate(final SSCircleCreatePar par) throws SSErr{
    
    try{
      
      if(par.withUserRestriction){
        
        if(par.isSystemCircle){
          throw SSErr.get(SSErrE.notAllowedToCreateCircle);
        }
        
        switch(par.circleType){
          
          case pubCircle:
          case group:{
            break;
          }
          
          default: throw SSErr.get(SSErrE.notAllowedToCreateCircle);
        }
      }
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSUri           circleUri;
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      circleUri =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
            par.user,
            SSConf.vocURICreate(),
            SSEntityE.circle,
            par.label,
            par.description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit
      
      if(circleUri == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      addCircle(
        par,
        circleUri,
        par.circleType,
        par.isSystemCircle,
        par.user); //par.forUser
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.createCircle(
        par, 
        isPrivPubOrSystemGroupCircle(par, circleUri),
        circleUri, 
        par.shouldCommit);
      
      return circleUri;
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
  public SSServRetI circleRemove(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCircleRemovePar par       = (SSCircleRemovePar) parA.getFromClient(clientType, parA, SSCircleRemovePar.class);
      final SSUri             circleURI;
      
            //former: 
//      if(
//        sql.isGroupOrPubCircleCircle(par, par.circle) &&
//        sql.isUserAuthor(par, par.user, par.circle, par.withUserRestriction)){
//        circleURI = circleRemove(par);
//      }else{
//        circleURI = null;
//      }
      
      if(isPrivPubOrSystemGroupCircle(parA, par.circle)){
        circleURI = null;
      }else{
        
        if(!sql.isUserAuthor(par, par.user, par.circle)){
          circleURI = null;
        }else{
          circleURI = circleRemove(par);
        }
      }
      
      return SSCircleRemoveRet.get(circleURI);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circleRemove(final SSCircleRemovePar par) throws SSErr{
    
    try{
      
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
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.removeCircle(par, par.circle);
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.removeCircle(
        par, 
        isPrivPubOrSystemGroupCircle(par, par.circle),
        par.shouldCommit);
        
      return par.circle;
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
  public SSServRetI circleUsersAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCircleUsersAddPar par = (SSCircleUsersAddPar) parA.getFromClient(clientType, parA, SSCircleUsersAddPar.class);
      final SSUri               circleURI;
      
      if(isPrivPubOrSystemGroupCircle(parA, par.circle)){ //former: !sql.isGroupOrPubCircleCircle(par, par.circle)){
        circleURI = null;
      }else{
        circleURI = circleUsersAdd(par);
      }
      
      final SSCircle circle =
        circleGet(
          new SSCircleGetPar(
            par,
            par.user,
            circleURI, //circle
            null, //entityTypesToIncludeOnly,
            false,  //setTags
            null, //tagSpace
            true, //setEntities
            true, //setUsers,
            par.withUserRestriction, //withUserRestriction,
            true)); //invokeEntityHandlers))
      
      SSServReg.inst.circleUsersAdded(
        par,
        par.user,
        circle,
        par.users,
        par.withUserRestriction);
      
      return SSCircleUsersAddRet.get(circleURI);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circleUsersAdd(final SSCircleUsersAddPar par) throws SSErr{
    
    try{
      
      SSEntity circle =
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          null,
          par.circle,
          false); //withUserRestriction
      
      if(
        circle == null ||
        par.users.isEmpty()){
        return null;
      }
       
      if(par.withUserRestriction){
        
        circle =
          sql.getEntityTest(
            par,
            SSConf.systemUserUri,
            par.user,
            par.circle,
            true); //withUserRestriction
        
        if(circle == null){
          
          final List<String>   invitedUsers  = sql.getInvitedUsers(par, par.circle);
          final SSUserServerI  userServ      = (SSUserServerI) SSServReg.getServ(SSUserServerI.class);
          final SSUsersGetPar  usersGetPar   =
            new SSUsersGetPar(
              par,
              par.user,
              par.users,
              null, //emails
              false); //invokeEntityHandlers
          
          for(SSEntity userEntity : userServ.usersGet(usersGetPar)){
            
            if(!SSStrU.contains(invitedUsers, ((SSUser) userEntity).email)){
              return null;
            }
          }
        }
      }
      
      if(
        SSStrU.isEmpty(par.users) ||
        !userCommons.areUsersUsers(par, par.users)){
        return null;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      for(SSUri userUri : par.users){
        sql.addUserToCircleIfNotExists(par, par.circle, userUri);
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.addUsersToCircle(
        par, 
        isPrivPubOrSystemGroupCircle(par, par.circle),
        par.shouldCommit);
      
      return par.circle;
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
  public SSServRetI circleEntitiesAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCircleEntitiesAddPar par      = (SSCircleEntitiesAddPar) parA.getFromClient(clientType, parA, SSCircleEntitiesAddPar.class);
      final SSUri                  cicleURI;
      
      if(isPrivPubOrSystemGroupCircle(parA, par.circle)){ //former: sql.isGroupOrPubCircleCircle(par, par.circle)){
        cicleURI = null;
      }else{
        cicleURI = circleEntitiesAdd(par);
      }
      
      final SSCircle circle =
        circleGet(
          new SSCircleGetPar(
            par,
            par.user,
            par.circle, //circle
            null, //entityTypesToIncludeOnly,
            false,  //setTags
            null, //tagSpace
            false, //setEntities
            true, //setUsers,
            par.withUserRestriction, //withUserRestriction,
            false)); //invokeEntityHandlers
      
      addTags(
        par,
        par.user,
        par.tags,
        par.entities,
        par.circle);
      
      addCategories(
        par,
        par.user,
        par.categories,
        par.entities,
        par.circle);
      
      final List<SSEntity> entities =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            par,
            par.user,
            par.entities,
            null, //descPar,
            par.withUserRestriction)); //withUserRestriction,
      
      SSServReg.inst.circleEntitiesAdded(
        par,
        par.user,
        circle,
        entities,
        par.withUserRestriction);
      
      final SSCircleEntitiesAddRet ret = SSCircleEntitiesAddRet.get(cicleURI);
      
//      evalServ.evalLog(
//        new SSEvalLogPar(
//          par,
//          par.user,
//          SSToolContextE.sss,
//          SSEvalLogE.circleEntitiesAddTagsAdd,
//          par.circle,  //entity
//          SSStrU.toCommaSeparatedStrNotNull(par.tags), //content,
//          par.entities,
//          null, //users
//          null, //creationTime
//          par.shouldCommit));
//      
//      evalServ.evalLog(
//        new SSEvalLogPar(
//          par,
//          par.user,
//          SSToolContextE.sss,
//          SSEvalLogE.circleEntitiesAddCategoriesAdd,
//          par.circle,  //entity
//          SSStrU.toCommaSeparatedStrNotNull(par.categories), //content,
//          par.entities,
//          null, //users
//          null, //creationTime
//          par.shouldCommit));
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circleEntitiesAdd(final SSCircleEntitiesAddPar par) throws SSErr{
    
    try{
      
      SSEntity circle =
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          par.user,
          par.circle,
          par.withUserRestriction);
      
      if(circle == null){
        return null;
      }
      
      final SSEntityServerI   entityServ      = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEntityUpdatePar entityUpdatePar =
        new SSEntityUpdatePar(
          par,
          par.user,
          null, // entity
          null, //type,
          null, //label,
          null, //description,
          null, //creationTime,
          null, //read,
          false, //setPublic
          true, //createIfNotExists
          par.withUserRestriction, //withUserRestriction
          false); //shouldCommit
      
      SSUri entityURI;
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      for(SSUri entityToAdd : par.entities){
        
        entityUpdatePar.entity = entityToAdd;
        entityURI              = entityServ.entityUpdate(entityUpdatePar);
        
        if(entityURI == null){
          dbSQL.rollBack(par, par.shouldCommit);
          return null;
        }
        
        sql.addEntityToCircleIfNotExists(
          par,
          par.circle,
          entityToAdd);
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.addEntitiesToCircle(
        par, 
        isPrivPubOrSystemGroupCircle(par, par.circle),
        par.shouldCommit);
      
      return par.circle;
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
  public List<SSCircleE> circleTypesGet(final SSCircleTypesGetPar par) throws SSErr{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        if(par.user == null){
          throw SSErr.get(SSErrE.parameterMissing);
        }
        
        return sql.getCircleTypesCommonForUserAndEntity(par, par.user, par.entity);
      }else{
        return sql.getCircleTypesForEntity(par, par.entity);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean circleIsEntityPrivate(final SSCircleIsEntityPrivatePar par) throws SSErr{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      SSEntity entity =
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          par.user,
          par.entity,
          false);
      
      if(entity == null){
        return false;
      }
      
      final List<SSCircleE> circleTypes = sql.getCircleTypesCommonForUserAndEntity(par, par.user, par.entity);
      
      if(
        circleTypes.size() < 1 ||
        circleTypes.size() > 1){
        return false;
      }
      
      return SSStrU.isEqual(circleTypes.get(0), SSCircleE.priv);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public boolean circleIsEntityShared(final SSCircleIsEntitySharedPar par) throws SSErr{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.forUser == null){
        return SSStrU.contains(sql.getCircleTypesForEntity(par, par.entity), SSCircleE.group);
      }else{
        return SSStrU.contains(sql.getCircleTypesCommonForUserAndEntity(par, par.forUser, par.entity), SSCircleE.group);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public boolean circleIsEntityPublic(final SSCircleIsEntityPublicPar par) throws SSErr{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      SSEntity entity =
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          null,
          par.entity,
          false);
      
      if(entity == null){
        return true;
      }
      
      return SSStrU.contains(sql.getCircleTypesForEntity(par, par.entity), SSCircleE.pub);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public SSServRetI circleGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCircleGetPar par = (SSCircleGetPar) parA.getFromClient(clientType, parA, SSCircleGetPar.class);
      
      return SSCircleGetRet.get(circleGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSCircle circleGet(final SSCircleGetPar par) throws SSErr{
    
    try{
      
      if(par.withUserRestriction){
        
        if(
          sql.isSystemCircle(par, par.circle) &&
          !SSStrU.isEqual(par.circle, pubCircleUri)){
          
          SSLogU.warn(SSErrE.userNotAllowToAccessSystemCircle, null);
          return null;
        }
      }
      
      SSCircle       circle;
      SSEntityDescriberPar descPar;
      
      circle =
        sql.getCircle(
          par,
          par.circle,
          par.setUsers, //withUsers
          par.setEntities, //withEntities
          true, //withCircleRights
          par.entityTypesToIncludeOnly);
      
      if(circle == null){
        return null;
      }
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(par.circle);
        
        descPar.setProfilePicture = par.setProfilePicture;
        descPar.setThumb          = par.setThumb;
      }else{
        descPar = null;
      }
      
      final SSEntityServerI entityServ   = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEntity        circleEntity =
        entityServ.entityGet(
          new SSEntityGetPar(
            par,
            par.user,
            par.circle,
            par.withUserRestriction, //withUserRestriction,
            descPar)); //descPar
      
      if(circleEntity == null){
        return null;
      }
      
      circle =
        SSCircle.get(
          circle,
          circleEntity);
      
      if(par.invokeEntityHandlers){
        
        descPar = new SSEntityDescriberPar(par.circle);
        
        descPar.setOverallRating = true;
        descPar.setTags          = par.setTags;
        descPar.space            = par.tagSpace;
        
        if(par.tagSpace != null){
          
          switch(par.tagSpace){
            case circleSpace:{
              descPar.circle = par.circle;
              break;
            }
          }
        }
        
      }else{
        descPar = null;
      }
      
      if(!circle.entities.isEmpty()){
        
        final List<SSEntity> circleEntities =
          entityServ.entitiesGet(
            new SSEntitiesGetPar(
              par,
              par.user,
              SSUri.getDistinctNotNullFromEntities(circle.entities),
              descPar, //descPar
              par.withUserRestriction)); //withUserRestriction
        
        circle.entities.clear();
        circle.entities.addAll(circleEntities);
      }
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(par.circle);
      }else{
        descPar = null;
      }
      
      if(!circle.users.isEmpty()){
        
        final List<SSEntity> circleUsers =
          entityServ.entitiesGet(
            new SSEntitiesGetPar(
              par,
              par.user,
              SSUri.getDistinctNotNullFromEntities(circle.users),
              descPar, //descPar
              par.withUserRestriction)); //withUserRestriction
        
        circle.users.clear();
        circle.users.addAll(circleUsers);
      }
      
      return circle;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI circlesGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCirclesGetPar par = (SSCirclesGetPar) parA.getFromClient(clientType, parA, SSCirclesGetPar.class);
      
      return SSCirclesGetRet.get(circlesGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> circlesGet(final SSCirclesGetPar par) throws SSErr{
    
    try{
      
      final List<SSEntity>            circles           = new ArrayList<>();
      final List<SSUri>               circleUris        = new ArrayList<>();
      
      if(par.withUserRestriction){
        
        if(
          par.forUser == null &&
          par.entity  == null){
          
          par.forUser = par.user;
        }
        
        if(par.withSystemCircles){
          SSLogU.warn(SSErrE.userNotAllowToAccessSystemCircle, null);
          return circles;
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
      
      if(!SSObjU.isNull(par.forUser, par.entity)){
        
        circleUris.addAll(
          sql.getCircleURIsCommonForUserAndEntity(
            par,
            par.forUser,
            par.entity,
            par.withSystemCircles));
      }else{
        
        if(
          par.forUser   == null &&
          par.entity    == null){
          
          circleUris.addAll(sql.getCircleURIs(par, par.withSystemCircles));
        }else{
          
          if(par.forUser != null){
            circleUris.addAll(sql.getCircleURIsForUser(par, par.forUser, par.withSystemCircles));
          }
          
          if(par.entity != null){
            circleUris.addAll(sql.getCircleURIsForEntity(par, par.entity, par.withSystemCircles));
          }
        }
      }
      
      final SSCircleGetPar circleGetPar =
        new SSCircleGetPar(
          par,
          par.user,
          null, //circle
          par.entityTypesToIncludeOnly,
          par.setTags,  //setTags
          null, //tagSpace
          par.setEntities, //setEntities,
          par.setUsers, //setUsers
          par.withUserRestriction,
          par.invokeEntityHandlers);
      
      circleGetPar.setProfilePicture = par.setProfilePicture;
      circleGetPar.setThumb          = par.setThumb;
      
      for(SSUri circleURI : circleUris){
        
        circleGetPar.circle = circleURI;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          circles,
          circleGet(circleGetPar));
      }
      
      return circles;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circlePrivURIGet(final SSCirclePrivURIGetPar par) throws SSErr{
    
    try{
      
      SSUri circleURI;
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      circleURI = sql.getPrivCircleURI(par, par.user);
      
      if(circleURI != null){
        return circleURI;
      }
      
      circleURI = SSConf.vocURICreate();
      
      sql.addEntityIfNotExists(
        par,
        circleURI,
        SSEntityE.circle,
        null, //label
        null, //description
        SSConf.systemUserUri, //author
        null);
      
      addCircle(
        par,
        circleURI,
        SSCircleE.priv,
        true,
        par.user);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return circleURI;
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
  public SSUri circlePubURIGet(final SSCirclePubURIGetPar par) throws SSErr{
    
    try{
      
      final SSUri tmpPublicCircleURI;
      
      if(pubCircleUri != null){
        return pubCircleUri;
      }
      
      pubCircleUri = sql.getPubCircleURI(par);
      
      if(pubCircleUri != null){
        return pubCircleUri;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      tmpPublicCircleURI = SSConf.vocURICreate();
      
      sql.addEntityIfNotExists(
        par,
        tmpPublicCircleURI,
        SSEntityE.circle,
        null, //label,
        null, //description,
        SSConf.systemUserUri,
        null);//creationTime);
      
      addCircle(
        par,
        tmpPublicCircleURI,
        SSCircleE.pub,
        true,
        SSConf.systemUserUri);
      
      dbSQL.commit(par, par.shouldCommit);
      
      pubCircleUri = tmpPublicCircleURI;
      
      return pubCircleUri;
      
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
  public SSServRetI circleUsersInvite(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCircleUsersInvitePar par = (SSCircleUsersInvitePar) parA.getFromClient(clientType, parA, SSCircleUsersInvitePar.class);
      
      return SSCircleUsersInviteRet.get(circleUsersInvite(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circleUsersInvite(final SSCircleUsersInvitePar par) throws SSErr{
    
    try{
      
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
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.inviteUsers(par, par.circle, par.emails);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return par.circle;
      
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
  public SSServRetI circleTypeChange(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCircleTypeChangePar par = (SSCircleTypeChangePar) parA.getFromClient(clientType, parA, SSCircleTypeChangePar.class);
      final SSUri                 circleURI;
      
      //former in server method: 
//      if(par.withUserRestriction){
//        
//        if(
//          !sql.isGroupOrPubCircleCircle(par, par.circle) ||
//          !sql.isUserAuthor(par, par.user, par.circle, par.withUserRestriction)){
//          return null;
//        }
//        
//        
//        switch(par.type){
//          
//          case group:
//          case pubCircle:{
//            break;
//          }
//          
//          default: return null;
//        }
//      }
      
      if(isPrivPubOrSystemGroupCircle(parA, par.circle)){
        circleURI = null;
      }else{
        circleURI = circleTypeChange(par);
      }
      
      return SSCircleTypeChangeRet.get(circleURI);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circleTypeChange(final SSCircleTypeChangePar par) throws SSErr {
    
    try{
      
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
      
      if(sql.isSystemCircle(par, par.circle)){
        return null;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.changeCircleType(par, par.circle, par.type);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return par.circle;
      
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
  public void circleAddEntitiesToCirclesOfEntity(final SSCircleAddEntitiesToCircleOfEntityPar par) throws SSErr{
    
    try{
      
      if(
        par.entityURIs == null ||
        par.entityURIs.isEmpty()){
        return;
      }
      
      final List<SSEntity> entities = new ArrayList<>();
      
      if(par.invokeEntityHandlers){
        
        final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
        
        entities.addAll(
          entityServ.entitiesGet(
            new SSEntitiesGetPar(
              par,
              par.user,
              par.entityURIs,
              null,
              par.withUserRestriction)));
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      for(SSEntity circle :
        circlesGet(
          new SSCirclesGetPar(
            par,
            par.user, //user
            null, //forUser
            par.entity, //entity
            null, //entityTypesToIncludeOnly
            false, //setEntities,
            par.invokeEntityHandlers, //setUsers,
            false, //withUserRestriction
            true,  //withSystemCircles
            false))){ //invokeEntityHandlers
        
        circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            par,
            par.user,
            circle.id,
            par.entityURIs,
            false, //withUserRestriction
            false));
        
        if(par.invokeEntityHandlers){
          
          SSServReg.inst.circleEntitiesAdded(
            par,
            par.user,
            (SSCircle) circle,
            entities,
            par.withUserRestriction);
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
  
  public boolean isUserInCircle(
    final SSServPar servPar,
    final SSUri          user,
    final SSUri          circle) throws SSErr{
    
    try{
      final List<SSEntity> usersForCircle = sql.getUsersForCircle(servPar, circle);
      
      return SSStrU.contains(usersForCircle, user);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  public void addCircle(
    final SSServPar servPar,
    final SSUri          circleUri,
    final SSCircleE      circleType,
    final boolean        isSystemCircle,
    final SSUri          userToAdd) throws SSErr{
    
    try{
      sql.addCircle(
        servPar,
        circleUri,
        circleType,
        isSystemCircle);
      
      sql.addUserToCircleIfNotExists(
        servPar,
        circleUri,
        userToAdd);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void copyCircleToNewCircle(
    final SSEntityCopyPar par,
    final SSCircle  circle) throws SSErr{
    
    try{
      
      SSLabel              label;
      SSUri                copyCircleURI;
      SSCircle       newCircle;
      String               labelStr;
      SSEntityCopiedPar    entityCopiedPar;
      
      for(SSUri forUser : par.forUsers){
        
        if(par.label != null){
          label = par.label;
        }else{
          label = circle.label;
        }
        
        if(par.appendUserNameToLabel){
          
          labelStr =
            SSStrU.toStr(label) +
            SSStrU.underline    +
            SSStrU.toStr(
              ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
                new SSEntityGetPar(
                  par,
                  par.user, //user
                  forUser, //entity
                  par.withUserRestriction,  //withUserRestriction
                  null)).label); //descPar
          
          label = SSLabel.get(labelStr);
        }
        
        copyCircleURI =
          circleCreate(
            new SSCircleCreatePar(
              par,
              forUser,
              circle.circleType,
              label,
              circle.description,
              circle.isSystemCircle,
              false, //withUserRestriction
              false)); //shouldCommit
        
        if(par.includeEntities){
          
          circleEntitiesAdd(
            new SSCircleEntitiesAddPar(
              par,
              forUser,
              copyCircleURI,
              SSUri.getDistinctNotNullFromEntities(circle.entities),
              false, //withUserRestriction
              false)); //shouldCommit
          
          entityCopiedPar =
            new SSEntityCopiedPar(
              par.user,
              forUser, //forUser
              circle, //entity
              circle.entities, //entities
              copyCircleURI, //targetEntity
              par.withUserRestriction);
          
          SSServReg.inst.entityCopied(par, entityCopiedPar);
        }
        
        if(par.includeUsers){
          
          circleUsersAdd(
            new SSCircleUsersAddPar(
              par,
              forUser,
              copyCircleURI,
              SSUri.getDistinctNotNullFromEntities(circle.users),
              false, //withUserRestriction,
              false)); //shouldCommit
          
          entityCopiedPar =
            new SSEntityCopiedPar(
              par.user,
              forUser, //forUser
              circle, //entity
              circle.users, //entities
              copyCircleURI, //targetEntity
              par.withUserRestriction);
          
          SSServReg.inst.entityCopied(par, entityCopiedPar);
        }
        
        if(par.includeOriginUser){
          
          circleUsersAdd(
            new SSCircleUsersAddPar(
              par,
              forUser,
              copyCircleURI,
              SSUri.asListNotNull(par.user),
              false, //withUserRestriction,
              false)); //shouldCommit
          
          final List<SSEntity> originUsers = new ArrayList<>();
          
          originUsers.add(
            ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
              new SSEntityGetPar(
                par,
                par.user, //user
                par.user, //entity
                par.withUserRestriction,  //withUserRestriction
                null))); //descPar
          
          entityCopiedPar =
            new SSEntityCopiedPar(
              par.user,
              forUser, //forUser
              circle, //entity
              originUsers, //entities
              copyCircleURI, //targetEntity
              par.withUserRestriction);
          
          SSServReg.inst.entityCopied(par, entityCopiedPar);
        }
        
        newCircle =
          circleGet(
            new SSCircleGetPar(
              par,
              forUser,
              copyCircleURI,
              null, //entityTypesToIncludeOnly
              false,  //setTags
              null, //tagSpace
              true, //setEntities,
              true, //setUsers
              par.withUserRestriction,
              false)); //invokeEntityHandlers
        
        SSServReg.inst.circleEntitiesAdded(
          par,
          par.user,
          newCircle,
          newCircle.entities,
          par.withUserRestriction);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void copyCircleToExistingCircle(
    final SSEntityCopyPar           par,
    final SSEntity                  circle) throws SSErr{
    
    try{
      
      SSEntityCopiedPar    entityCopiedPar;
      SSCircle       targetCircle =
        circleGet(
          new SSCircleGetPar(
            par,
            par.user,
            par.targetEntity,
            null, //entityTypesToIncludeOnly
            false,  //setTags
            null, //tagSpace
            true, //setEntities,
            true, //setUsers
            par.withUserRestriction,
            false)); //invokeEntityHandlers
      
      if(par.includeEntities){
        
        circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            par,
            par.user,
            targetCircle.id,
            SSUri.getDistinctNotNullFromEntities(circle.entities),
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit
        
        entityCopiedPar =
          new SSEntityCopiedPar(
            par.user,
            null, //forUser
            circle, //entity
            circle.entities, //entities
            targetCircle.id, //targetEntity
            par.withUserRestriction);
        
        entityCopiedPar.includeMetadataSpecificToEntityAndItsEntities = true;
        
        SSServReg.inst.entityCopied(par, entityCopiedPar);
      }
      
      if(par.includeUsers){
        
        circleUsersAdd(
          new SSCircleUsersAddPar(
            par,
            par.user,
            targetCircle.id,
            SSUri.getDistinctNotNullFromEntities(circle.users),
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit
        
        entityCopiedPar =
          new SSEntityCopiedPar(
            par.user,
            null, //forUser,
            circle, //entity
            circle.users, //entities
            targetCircle.id,  //targetEntity
            par.withUserRestriction);
        
        SSServReg.inst.entityCopied(par, entityCopiedPar);
      }
      
      if(par.includeOriginUser){
        
        circleUsersAdd(
          new SSCircleUsersAddPar(
            par,
            par.user,
            par.targetEntity, //circle
            SSUri.asListNotNull(par.user), //users
            false, //withUserRestriction,
            false)); //shouldCommit
        
        final List<SSEntity> originUsers = new ArrayList<>();
        
        originUsers.add(
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              par,
              par.user, //user
              par.user, //entity
              par.withUserRestriction,  //withUserRestriction
              null))); //descPar
        
        entityCopiedPar =
          new SSEntityCopiedPar(
            par.user, //user
            null, //targetUser
            circle, //entity
            originUsers, //entities
            par.targetEntity, //targetEntity
            par.withUserRestriction);
        
        SSServReg.inst.entityCopied(par, entityCopiedPar);
      }
      
      targetCircle =
        circleGet(
          new SSCircleGetPar(
            par,
            par.user,
            par.targetEntity,
            null, //entityTypesToIncludeOnly
            false,  //setTags
            null, //tagSpace
            true, //setEntities,
            true, //setUsers
            par.withUserRestriction,
            false)); //invokeEntityHandlers
      
      SSServReg.inst.circleEntitiesAdded(
        par,
        par.user,
        targetCircle,
        targetCircle.entities,
        par.withUserRestriction);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addTags(
    final SSServPar       servPar,
    final SSUri           user,
    final List<String>    tags,
    final List<SSUri>     entities,
    final SSUri           circleAsSpace) throws SSErr{
    
    try{
      
      if(tags.isEmpty()){
        return;
      }
      
      ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagsAdd(
        new SSTagsAddPar(
          servPar,
          user,
          SSTagLabel.get(tags), //labels
          entities, //entity
          null, //space
          circleAsSpace, //circle
          null, //creationTime,
          true, //withUserRestriction
          false)); //shouldCommit)
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addCategories(
    final SSServPar servPar,
    final SSUri           user,
    final List<String>    categories,
    final List<SSUri>     entities,
    final SSUri           circleAsSpace) throws SSErr{
    
    try{
      
      if(categories.isEmpty()){
        return;
      }
      
      for(SSUri entity : entities){
        
        ((SSCategoryServerI) SSServReg.getServ(SSCategoryServerI.class)).categoriesAdd(
          new SSCategoriesAddPar(
            servPar,
            user,
            SSCategoryLabel.get(categories), //labels
            entity, //file
            null, //space
            circleAsSpace, //circle
            null, //creationTime,
            true, //withUserRestriction
            false)); //shouldCommit)
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private boolean isPrivPubOrSystemGroupCircle(
    final SSServPar servPar, 
    final SSUri     circleURI) throws SSErr{
    
    try{
      
      final SSCircle circle =
        sql.getCircle(
          servPar,
          circleURI,
          false,  //withUsers
          false, //withEntities
          false, //withCircleRights,
          null); //entityTypesToIncludeOnly
      
      if(
        circle == null ||
        circle.isSystemCircle){
        return true;
      }
      
      switch(circle.circleType){
        
        case pub:
        case priv:{
          return true;
        }
      }
      
      return false;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
}

//  public void checkWhetherCircleIsGroupCircle(
//    final SSCircleE circleType) throws SSErr{
//
//    try{
//
//      if(!SSCircleE.isGroupCircle(circleType)){
//        throw new Exception("circle is no group circle");
//      }
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }


//    public boolean hasCircleOfTypeRight(
//    final SSUri          circle,
//    final SSCircleRightE accessRight) throws SSErr{
//
//    try{
//      return doesCircleOfTypeHaveRight(sql.getTypeForCircle(circle), accessRight);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//  public boolean doesUserHaveRightInAnyCircleOfEntity(
//    final SSUri          user,
//    final SSUri          entity,
//    final SSCircleRightE accessRight) throws SSErr{
//
//    try{
//      final List<SSCircleE> circleTypes = sql.getCircleTypesCommonForUserAndEntity(user, entity);
//
//      for(SSCircleE circleType : circleTypes){
//
//        if(doesCircleOfTypeHaveRight(circleType, accessRight)){
//          return true;
//        }
//      }
//
//      return false;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//  private boolean doesCircleOfTypeHaveRight(
//    final SSCircleE      circleType,
//    final SSCircleRightE accessRight) throws SSErr{
//
//    try{
//      switch(circleType){
//        case priv: return true;
//        case pub:{
//
//          if(SSCircleRightE.equals(accessRight, SSCircleRightE.read)){
//            return true;
//          }
//
//          break;
//        }
//
//        default:{
//
//          if(
//            SSCircleRightE.equals(accessRight, SSCircleRightE.read) ||
//            SSCircleRightE.equals(accessRight, SSCircleRightE.edit)){
//            return true;
//          }
//
//          break;
//        }
//      }
//
//      return false;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//  public boolean canUserForEntityType(
//    final SSUri          user,
//    final SSEntity       entity) throws SSErr{
//
//    try{
//      switch(entity.type){
//        case entity: return true; //dtheiler: break down general entity types so that checks on e.g. videos will be present
//        case circle: {
//
//          if(isUserInCircle       (user, entity.id)){
////            &&
////            hasCircleOfTypeRight (entity.id, accessRight)
//            return true;
//          }
//
//          return false;
//        }
//
//        default:{
//          return !sql.getCirclesCommonForUserAndEntity(user, entity.id).isEmpty();
////          return doesUserHaveRightInAnyCircleOfEntity(user, entity.id, accessRight);
//        }
//      }
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//  @Override
//  public void circleCanAccess(final SSCircleCanAccessPar par) throws SSErr{
//
//    try{
//
//      if(par.entityURI == null){
//        throw SSErr.get(SSErrE.parameterMissing);
//      }
//
//      if(!sqlFct.existsEntity(par.entityURI)){
//        return;
//      }
//
//      if(SSStrU.isEqual(par.user, SSConf.systemUserUri)){
//        return;
//      }
//
//      if(
//        !canUserForEntityType(
//          par.user,
//          sqlFct.getEntity(par.entityURI))){
//
//        throw new SSErr(SSErrE.userNotAllowedToAccessEntity);
//      }
//
//    }catch(SSErr error){
//      SSServErrReg.regErrThrow(error, false);
//    }
//  }

//  @Override
//  public void circlesFromEntityEntitiesAdd(final SSCirclesFromEntityEntitiesAdd par) throws SSErr{
//
//    try{
//
//      dbSQL.startTrans(par, par.shouldCommit);
//
//      for(SSEntity entityUserCircle :
//        circlesGet(
//          new SSCirclesGetPar(
//            par.user,
//            par.entity,
//            null, //entityTypesToIncludeOnly
//            false, //withUserRestriction
//            true,  //withSystemCircles
//            false))){ //invokeEntityHandlers
//
//        circleEntitiesAdd(
//          new SSCircleEntitiesAddPar(
//            par.user,
//            entityUserCircle.id,
//            par.entities,  //entities
//            false,  //withUserRestriction
//            false)); //shouldCommit
//      }
//
//      dbSQL.commit(par, par.shouldCommit);
//
//    }catch(Exception error){
//
//      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
//
//        if(dbSQL.rollBack(par.shouldCommit)){
//
//          SSServErrReg.reset();
//
//          circlesFromEntityEntitiesAdd(par);
//        }else{
//          SSServErrReg.regErrThrow(error);
//        }
//      }
//
//      dbSQL.rollBack(par, par.shouldCommit);
//      SSServErrReg.regErrThrow(error);
//    }
//  }


//@Override
//  public List<SSEntity> circleEntityUsersGet(final SSCircleEntityUsersGetPar par) throws SSErr{
//
////TODO to be integrated with withUserRestriction
//    try{
//
//      final List<SSUri>                   userUris        = new ArrayList<>();
//      final List<SSUri>                   userCircleUris  = sqlFct.getCircleURIsForUser   (par.user, true);
//
//      for(SSUri circleUri : sqlFct.getCircleURIsForEntity(par.entity, true)){
//
//        switch(sqlFct.getTypeForCircle(circleUri)){
//
//          case priv:{
//
//            if(!SSStrU.contains(userCircleUris, circleUri)){
//              continue;
//            }
//
//            if(!SSStrU.contains(userUris, par.user)){
//              userUris.add(par.user);
//            }
//
//            break;
//          }
//
//          case group:{
//
//            if(!SSStrU.contains(userCircleUris, circleUri)){
//              continue;
//            }
//
//            SSUri.addDistinctWithoutNull(
//              userUris,
//              SSUri.getDistinctNotNullFromEntities(sqlFct.getUsersForCircle(circleUri)));
//
//            break;
//          }
//
//          case pub:{
//
//            SSUri.addDistinctWithoutNull(
//              userUris,
//              SSUri.getDistinctNotNullFromEntities(sqlFct.getUsersForCircle(circleUri)));
//
//            break;
//          }
//        }
//      }
//
//      return ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
//        new SSEntitiesGetPar(
//          null,
//          userUris, //entities
//          null, //types
//          null, //descPar
//          false));  //withUserRestriction
//
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//  public static boolean canUserRead(
//    final SSUri user,
//    final SSUri entityURI) throws SSErr{
//
//    try{
//      ((SSEntityServerI)SSServReg.getServ(SSEntityServerI.class)).circleCanAccess(
//        new SSCircleCanAccessPar(
//          user,
//          entityURI,
//          SSCircleRightE.read));
//
//      return true;
//    }catch(Exception error){
//
//      if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
//        SSServErrReg.reset();
//        return false;
//      }
//
//      throw error;
//    }
//  }
//
//  public static boolean canUserRead(
//    final SSUri       user,
//    final List<SSUri> entityURIs) throws SSErr{
//
//    try{
//
//      for(SSUri entityURI : entityURIs){
//
//        ((SSEntityServerI)SSServReg.getServ(SSEntityServerI.class)).circleCanAccess(
//          new SSCircleCanAccessPar(
//            user,
//            entityURI,
//            SSCircleRightE.read));
//      }
//
//      return true;
//    }catch(Exception error){
//
//      if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
//        SSServErrReg.reset();
//        return false;
//      }
//
//      throw error;
//    }
//  }

//  public static boolean canUserAll(
//    final SSUri   user,
//    final SSUri   entityURI) throws SSErr{
//
//   try{
//      ((SSEntityServerI)SSServReg.getServ(SSEntityServerI.class)).circleCanAccess(
//        new SSCircleCanAccessPar(
//          user,
//          entityURI,
//          SSCircleRightE.all));
//
//      return true;
//    }catch(Exception error){
//
//      if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
//        SSServErrReg.reset();
//        return false;
//      }
//
//      throw error;
//    }
//  }

//  public static void canUserReadEntity(
//    final SSUri user,
//    final SSUri entityURI) throws SSErr{
//
//    ((SSEntityServerI)SSServReg.getServ(SSEntityServerI.class)).circleCanAccess(
//      new SSCircleCanAccessPar(
//        null,
//        null,
//        user,
//        entityURI,
//        SSCircleRightE.read));
//  }
//
//  public static void canUserReadEntities(
//    final SSUri       user,
//    final List<SSUri> entityURIs) throws SSErr{
//
//    for(SSUri entityURI : entityURIs){
//
//      ((SSEntityServerI)SSServReg.getServ(SSEntityServerI.class)).circleCanAccess(
//        new SSCircleCanAccessPar(
//          null,
//          null,
//          user,
//          entityURI,
//          SSCircleRightE.read));
//    }
//  }

//  public static void canUserAllEntity(
//    final SSUri   user,
//    final SSUri   entityURI) throws SSErr{
//
//    ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).circleCanAccess(
//      new SSCircleCanAccessPar(
//        null,
//        null,
//        user,
//        entityURI,
//        SSCircleRightE.all));
//  }

//    prefixUri = SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.coll.toString());
//
//    if (SSStrU.startsWith(par.entityUri.toString(), prefixUri.toString())) {
//      return SSEntityEnum.coll.toString();
//    }

//    prefixUri = SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.disc.toString());
//
//    if (SSStrU.startsWith(par.entityUri.toString(), prefixUri.toString())) {
//      return SSEntityEnum.disc.toString();
//    }

//    prefixUri = SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.file.toString());
//
//    if (SSStrU.startsWith(par.entityUri.toString(), prefixUri.toString())) {
//      return SSEntityEnum.file.toString();
//    }

//    prefixUri = SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.user.toString());
//
//    if (SSStrU.startsWith(par.entityUri.toString(), prefixUri.toString())) {
//
//			List<User> allUsers = ModelGC.SOCKET.getAllUsers();
//
//			for(User user : allUsers){
//
//				if(GM.isSame(uri, user.getId())){
//
//					return SSEntityEnum.user;
//				}
//			}
//
//      return SSEntityEnum.user.toString();
//    }

//    return SSEntityEnum.entity.toString();