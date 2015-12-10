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
package at.kc.tugraz.ss.serv.datatypes.entity.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitySharePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivURIGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubURIGetPar;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityShareRet;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityClientI;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntityActAndLogFct;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityAttachEntitiesPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityDownloadURIsGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityDownloadsAddPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityRemovePar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSEntityCopyPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityFromTypeAndLabelGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityTypesGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUserParentEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUserSubEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntitiesGetRet;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityCopyRet;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityGetRet;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityUpdateRet;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntityActivityFct;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntityUserRelationsGatherFct;
import at.kc.tugraz.ss.service.userevent.api.SSUEServerI;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddPar;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSCopyEntityI;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSEntityResultPages;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSGetParentEntitiesI;
import at.tugraz.sss.serv.SSGetSubEntitiesI;
import at.tugraz.sss.serv.SSIDU;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.servs.common.impl.tagcategory.SSEntityQueryCacheU;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesAccessibleGetCleanUpPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesAccessibleGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityEntitiesAttachedRemovePar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityURIsGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUnpublicizePar;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntitiesAccessibleGetRet;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityTypesGetRet;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityUnpublicizeRet;
import java.util.Arrays;
import java.util.HashMap;
import sss.serv.eval.api.SSEvalServerI;
import sss.servs.entity.sql.SSEntitySQL;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSEntityContext;
import at.tugraz.sss.serv.SSStrU;

public class SSEntityImpl 
extends SSServImplWithDBA
implements 
  SSEntityClientI, 
  SSEntityServerI,
  SSUserRelationGathererI,
  SSDescribeEntityI,
  SSAddAffiliatedEntitiesToCircleI,
  SSUsersResourcesGathererI{
  
  protected static final Map<String, SSEntityResultPages> accessibleEntitiesPagesCache = new HashMap<>();
  
  private final SSEntitySQL          sql;
  private final SSCircleServerI      circleServ;
  private final SSEntityActAndLogFct actAndLogFct;
  
  public SSEntityImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sql             = new SSEntitySQL  (dbSQL, SSVocConf.systemUserUri);
    this.circleServ      = (SSCircleServerI) SSServReg.getServ(SSCircleServerI.class);
    
    this.actAndLogFct = 
      new SSEntityActAndLogFct(
        (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class), 
        (SSEvalServerI)     SSServReg.getServ(SSEvalServerI.class));
  }
  
  @Override
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSAddAffiliatedEntitiesToCirclePar par) throws SSErr{
    
    try{
      
      final List<SSUri>    affiliatedURIs     = new ArrayList<>();
      final List<SSEntity> affiliatedEntities = new ArrayList<>();
      
      for(SSEntity entityAdded : par.entities){
        
        for(SSUri attachedEntity : sql.getAttachedEntities(entityAdded.id)){
          
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
            par.user, 
            affiliatedURIs, //entities
            null, //descPar
            par.withUserRestriction)));
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          par.user,
          par.circle, //circle
          affiliatedURIs, //entities
          false, //withUserRestriction
          false)); //shouldCommit
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities,
        SSServCallerU.handleAddAffiliatedEntitiesToCircle(
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
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws SSErr{

    if(par.setAttachedEntities){
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entity.attachedEntities,
        entitiesGet(
          new SSEntitiesGetPar(
            par.user,
            sql.getAttachedEntities(entity.id), //entities
            null, //descPar,
            par.withUserRestriction)));
    }
    
    if(par.setRead){
      entity.read = sql.getEntityRead (par.user, entity.id);
    }
    
    return entity;
  }
  
  @Override
  public void getUsersResources(
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      final List<SSEntityE> types = new ArrayList<>();
      
      types.add(SSEntityE.placeholder);
      types.add(SSEntityE.file);
      types.add(SSEntityE.uploadedFile);
      types.add(SSEntityE.entity);
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        for(SSUri entity : 
          sql.getAccessibleURIs(
            userID, 
            types, 
            null,//authors
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
    final List<String>             allUsers,
    final Map<String, List<SSUri>> userRelations) throws SSErr{
    
    try{
      SSEntityUserRelationsGatherFct.getUserRelations(
        allUsers,
        userRelations);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void entityCopy(final SSSocketCon sSCon, final SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSEntityCopyPar par    = (SSEntityCopyPar) parA.getFromJSON(SSEntityCopyPar.class);
      final Boolean         worked = entityCopy(par);
      
      sSCon.writeRetFullToClient(SSEntityCopyRet.get(worked));
      
      if(worked){
        
        actAndLogFct.entityCopy(
          par.user,
          par.entity,
          par.targetEntity,
          par.forUsers,
          par.comment,
          par.shouldCommit);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public Boolean entityCopy(final SSEntityCopyPar par) throws SSErr{
    
    try{   
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSEntity entity =
        entityGet(
          new SSEntityGetPar(
            par.user,
            par.entity,
            par.withUserRestriction,
            null));
      
      if(entity == null){
        return false;
      }
      
      for(SSServContainerI serv : SSServReg.inst.getServsHandlingCopyEntity()){
        ((SSCopyEntityI) serv.serv()).copyEntity(entity, par);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityCopy(par);
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
        throw SSErr.get(SSErrE.queryPageUnavailable);
      }
      
      return SSEntitiesAccessibleGetRet.get(
        entitiesGet(
          new SSEntitiesGetPar(
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
  public void entitiesAccessibleGet(final SSSocketCon sSCon, final SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSEntitiesAccessibleGetPar par = (SSEntitiesAccessibleGetPar) parA.getFromJSON(SSEntitiesAccessibleGetPar.class);
      
      sSCon.writeRetFullToClient(entitiesAccessibleGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSEntitiesAccessibleGetRet entitiesAccessibleGet (final SSEntitiesAccessibleGetPar par) throws SSErr{
    
    try{
      
      if(par.pagesID   != null &&
        par.pageNumber != null){
      
        return handleAccessibleEntitiesPageRequest(par);
      }

      final List<List<SSUri>>       pages                         = new ArrayList<>();
      final List<SSUri>             page                          = new ArrayList<>();
      final String                  pagesID                       = SSIDU.uniqueID();
      
      final List<SSUri> entityURIs =
        entityURIsGet(
          new SSEntityURIsGetPar(
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
        
        if(page.size() == 10){
          pages.add(new ArrayList<>(page));
          page.clear();
        }
        
        page.add(entityURI);
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
  
  @Override
  public List<SSUri> entityURIsGet (final SSEntityURIsGetPar par) throws SSErr{
    
    try{
      
      if(par.getAccessible){
        
        return sql.getAccessibleURIs(
          par.user,
          par.types,
          par.authors,
          par.startTime,
          par.endTime);
      }
      
      if(
        !par.types.isEmpty() ||
        !par.authors.isEmpty()){
        
        return sql.getEntityURIs(
          par.entities,
          par.types,
          par.authors, 
          par.startTime, 
          par.endTime);
      }
      
      return par.entities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
    
  @Override
  public void entitiesGet(final SSSocketCon sSCon, final SSServPar parA) throws SSErr {
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSEntitiesGetPar par = (SSEntitiesGetPar) parA.getFromJSON(SSEntitiesGetPar.class);
      
      sSCon.writeRetFullToClient(SSEntitiesGetRet.get(entitiesGet(par)));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public List<SSEntity> entitiesGet(final SSEntitiesGetPar par) throws SSErr{
    
    try{
      
      final List<SSUri> entityURIs = 
        entityURIsGet(
          new SSEntityURIsGetPar(
            par.user,
            par.entities,
            false, //getAccessible
            par.types,
            par.authors, 
            null, //startTime
            null)); //endTime
        
      final SSEntityGetPar entityGetPar =
        new SSEntityGetPar(
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
  public void entityGet(final SSSocketCon sSCon, final SSServPar parA) throws SSErr {
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSEntityGetPar par = (SSEntityGetPar) parA.getFromJSON(SSEntityGetPar.class);
      
      sSCon.writeRetFullToClient(SSEntityGetRet.get(entityGet(par)));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSEntity entityGet(final SSEntityGetPar par) throws SSErr{
    
    try{
      
      SSEntity entity =
        sql.getEntityTest(
          par.user,
          par.entity,
          par.withUserRestriction);
      
      if(entity == null){
        return null;
      }
      
      if(
        par.descPar   == null &&
        entity.author != null){
        entity.author = sql.getEntityTest(null, entity.author.id, false);
      }
      
      if(par.descPar != null){
      
        par.descPar.user                = par.user;
        par.descPar.withUserRestriction = par.withUserRestriction;
        
        for(SSServContainerI serv : SSServReg.inst.getServsHandlingDescribeEntity()){
          entity = ((SSDescribeEntityI) serv.serv()).describeEntity(entity, par.descPar);
        }
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
          par.user, 
          SSUri.getDistinctNotNullFromEntities(sql.getEntities(par.label, par.type)), 
          null, // descPar, 
          par.withUserRestriction));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityUpdate(final SSSocketCon sSCon, final SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSEntityUpdatePar par = (SSEntityUpdatePar) parA.getFromJSON(SSEntityUpdatePar.class);
      final SSUri             entityURI;
      Boolean                 isPlaceholderAdd = false;
      
      par.fromClient = true;
      
      if(
        par.entity == null &&
        par.type   == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.entity == null){
        
        switch(par.type){
          case placeholder:{
            
            par.entity = SSServCaller.vocURICreate();
            
            isPlaceholderAdd = true;
            break;
          }
          
          default: throw SSErr.get(SSErrE.entityTypeNotSupported);
        }
      }
      
      entityURI = entityUpdate(par);
      
      sSCon.writeRetFullToClient(SSEntityUpdateRet.get(entityURI));
      
      if(isPlaceholderAdd){
        
        ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventAdd(
          new SSUEAddPar(
            par.user,
            par.entity,
            SSUEE.bnpPlaceholderAdd,
            null, //content
            par.creationTime,
            par.withUserRestriction,
            par.shouldCommit)); //shouldCommit
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
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
          null, //user
          par.entity, //entity
          false); //withUserRestriction
      
      if(
        entity != null &&
        par.withUserRestriction){
        
        entity = 
          sql.getEntityTest(
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
        
      dbSQL.startTrans(par.shouldCommit);
      
      if(entity == null){
        
        if(par.type == null){
          par.type = SSEntityE.entity;
        }
      }
      
      sql.addEntityIfNotExists(
        par.entity,
        par.type,
        par.label,
        par.description,
        par.user,
        par.creationTime);
      
      if(entity == null){
        
        final SSUri privateCircleURI =
          circleServ.circlePrivURIGet(
            new SSCirclePrivURIGetPar(
              par.user,
              false)); //shouldCommit
        
        sql.addEntityToCircleIfNotExists(
          privateCircleURI,
          par.entity);
      }
      
      if(par.read != null){
        
        sql.setEntityRead(
          par.user, 
          par.entity,
          par.read);
      }
      
      if(
        par.setPublic != null &&
        par.setPublic){
          
        final SSUri entityURI =
          entityShare(
            new SSEntitySharePar(
              par.user,
              par.entity, //entity,
              null, //users,
              null, //circles,
              true, //setPublic,
              null, //comment,
              par.withUserRestriction,
              false)); //shouldCommit))
        
        if(entityURI == null){
          dbSQL.rollBack(par.shouldCommit);
          return null;
        }
      }
      
      dbSQL.commit(par.shouldCommit);
      
      actAndLogFct.entityUpdate(
        par.user,
        par.fromClient,
        entity,
        par.label,
        par.description,
        par.shouldCommit);
        
      return par.entity;
      
   }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityUpdate(par);
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
  public SSUri entityRemove(final SSEntityRemovePar par) throws SSErr{
    
    try{
      
      sql.deleteEntityIfExists(par.entity);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityRemove(par);
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
  public List<SSUri> entityUserParentEntitiesGet(final SSEntityUserParentEntitiesGetPar par) throws SSErr{
    
    try{
      final List<SSUri>  entities     = new ArrayList<>();
      final SSEntity     entity       = 
        sql.getEntityTest(
          par.user, 
          par.entity, 
          par.withUserRestriction);
      
      if(entity == null){
        return entities;
      }
      
      for(SSServContainerI serv : SSServReg.inst.getServsHandlingGetParentEntities()){
        entities.addAll(((SSGetParentEntitiesI) serv.serv()).getParentEntities(par.user, par.entity, entity.type));
      }
      
      SSStrU.distinctWithoutNull2(entities);
      
      return entities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> entityUserSubEntitiesGet(final SSEntityUserSubEntitiesGetPar par) throws SSErr{
    
    try{
      final List<SSUri>                   subEntities = new ArrayList<>();
      final SSEntity                      entity      =
        sql.getEntityTest(
          par.user,
          par.entity,
          par.withUserRestriction);
      
      if(entity == null){
        return subEntities;
      }
      
      
      switch(entity.type){
        
        case entity: {
          return subEntities;
        }
        
        default: {
          
          for(SSServContainerI serv : SSServReg.inst.getServsHandlingGetSubEntities()){
            
            subEntities.addAll(
              ((SSGetSubEntitiesI) serv.serv()).getSubEntities(
                par.user, 
                par.entity, 
                entity.type));
          }
          
          return subEntities;
        }
      }
      
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
          par.user, 
          par.entity, 
          par.withUserRestriction);
        
      if(entity == null){
        return null;
      }
      
      SSUri entityToAttachURI;
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri entityToAttach : par.entities){
        
        entityToAttachURI =
          entityUpdate(
            new SSEntityUpdatePar(
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
          dbSQL.rollBack(par.shouldCommit);
          return null;
        }
      }
      
      sql.attachEntities(par.entity, par.entities);
      
      SSServCallerU.handleCirclesFromEntityGetEntitiesAdd(
        circleServ, 
        this,
        par.user,
        par.entity,
        par.entities, //entities
        par.withUserRestriction,
        true); //invokeEntityHandlers);
      
      dbSQL.commit(par.shouldCommit);
      
      actAndLogFct.attachEntities(
        par.user, 
        par.entity,
        par.entities, 
        par.shouldCommit);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityEntitiesAttach(par);
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
  public SSUri entityEntitiesAttachedRemove(final SSEntityEntitiesAttachedRemovePar par) throws SSErr{
    
    try{

      final SSEntity entity =
        sql.getEntityTest(
          par.user,
          par.entity,
          par.withUserRestriction);
      
      if(entity == null){
        return null;
      }

      dbSQL.startTrans(par.shouldCommit);
      
      sql.removeAttachedEntities(par.entity, par.entities);
     
      dbSQL.commit(par.shouldCommit);
      
      actAndLogFct.removeEntities(
        par.user, 
        par.entity, 
        par.entities, 
        par.shouldCommit);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityEntitiesAttachedRemove(par);
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
  public SSUri entityDownloadsAdd(final SSEntityDownloadsAddPar par) throws SSErr{
    
    try{

      final SSEntity entity =
        sql.getEntityTest(
          par.user,
          par.entity,
          par.withUserRestriction);
      
      if(entity == null){
        return null;
      }
      
      SSUri downloadURI;
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri download : par.downloads){
        
        downloadURI =
          entityUpdate(
            new SSEntityUpdatePar(
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
          dbSQL.rollBack(par.shouldCommit);
          return null;
        }
      }
      
      sql.addDownloads(par.entity, par.downloads);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityDownloadsAdd(par);
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
  public List<SSUri> entityDownloadsGet(final SSEntityDownloadURIsGetPar par) throws SSErr{
    
    try{
      
      final List<SSUri> downloads = new ArrayList<>();
      
      SSEntity entity =
        sql.getEntityTest(
          par.user,
          par.entity,
          par.withUserRestriction);
      
      if(entity == null){
        return downloads;
      }
      
      SSEntity downloadEntity;
      
      for(SSUri download : sql.getDownloads(par.entity)){
        
        downloadEntity = 
          sql.getEntityTest(
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
  public void entityShare(final SSSocketCon sSCon, final SSServPar parA) throws SSErr {
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSEntitySharePar par = (SSEntitySharePar) parA.getFromJSON(SSEntitySharePar.class);
      
      final SSUri entityURI = entityShare(par);
      
      sSCon.writeRetFullToClient(SSEntityShareRet.get(entityURI));
      
      if(!par.users.isEmpty()){
        SSEntityActivityFct.shareEntityWithUsers(par);
      }
      
      if(!par.circles.isEmpty()){
        SSEntityActivityFct.shareEntityWithCircles(par);
      }
      
      if(par.setPublic){
        SSEntityActivityFct.setEntityPublic(par);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
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
          par.user,
          par.entity, 
          par.withUserRestriction);
      
      if(entity == null){
        return null;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(!par.users.isEmpty()){
        
        new SSEntityShareWithUsers(circleServ).handle(
          par.user, 
          SSCircleE.group,
          entity,
          par.users, 
          par.withUserRestriction);
      }
        
      if(!par.circles.isEmpty()){
        
        new SSEntityShareWithCircles(circleServ).handle(
          par.user,
          entity,
          par.circles,
          par.withUserRestriction);
      }
      
      if(par.setPublic){
        
        new SSEntitySetPublic(circleServ).handle(
          par.user,
          entity,
          par.withUserRestriction);
      }
     
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityShare(par);
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
  public void entityUnpublicize(final SSSocketCon sSCon, final SSServPar parA) throws SSErr {
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSEntityUnpublicizePar par = (SSEntityUnpublicizePar) parA.getFromJSON(SSEntityUnpublicizePar.class);
      
      final SSUri entityURI = entityUnpublicize(par);
      
      sSCon.writeRetFullToClient(SSEntityUnpublicizeRet.get(entityURI));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override  
  public SSUri entityUnpublicize(final SSEntityUnpublicizePar par) throws SSErr{
    
    try{
      
      if(par.withUserRestriction){
        
        if(par.user == null){
          throw SSErr.get(SSErrE.parameterMissing);
        }
        
        if(!sql.isUserAuthor(par.user, par.entity, par.withUserRestriction)){
          return null;
        }
      }
      
      final SSEntity entity = 
        sql.getEntityTest(
          par.user,
          par.entity, 
          par.withUserRestriction);
      
      if(entity == null){
        return null;
      }

      final SSUri pubCircleURI = 
        circleServ.circlePubURIGet(
          new SSCirclePubURIGetPar(
            par.user,
            par.shouldCommit));
      
      dbSQL.startTrans(par.shouldCommit);
      
      sql.removeEntityFromCircle(
        pubCircleURI,
        par.entity);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityUnpublicize(par);
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
  public void entityTypesGet(final SSSocketCon sSCon, final SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSEntityTypesGetPar par = (SSEntityTypesGetPar) parA.getFromJSON(SSEntityTypesGetPar.class);
      
      sSCon.writeRetFullToClient(SSEntityTypesGetRet.get(entityTypesGet(par)));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
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
}

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