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

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreatePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitySharePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivURIGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubURIGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityShareRet;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityClientI;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForDescriptionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForLabelsAndDescriptionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForLabelsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityCopyPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityFromTypeAndLabelGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserParentEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserSubEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntitiesGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityCopyRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUpdateRet;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntityActivityFct;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntityUserRelationsGatherFct;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchOpE;
import at.kc.tugraz.ss.service.userevent.api.SSUEServerI;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddPar;
import at.tugraz.sss.serv.SSCircleContentChangedPar;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityAttatchmentsRemovePar;

public class SSEntityImpl 
extends SSServImplWithDBA
implements 
  SSEntityClientI, 
  SSEntityServerI,
  SSUserRelationGathererI,
  SSEntityHandlerImplI,
  SSUsersResourcesGathererI{
  
  private final SSEntitySQLFct         sqlFct;
  
  public SSEntityImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    sqlFct = new SSEntitySQLFct(this);
  }
  
  @Override
  public SSEntity getUserEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{

    return entity;
  }
  
  @Override
  public void circleContentChanged(final SSCircleContentChangedPar par) throws Exception{
    
  }
  
  @Override
  public void copyEntity(
    final SSUri user, 
    final List<SSUri> users, 
    final SSUri entity, 
    final List<SSUri> entitiesToExclude, 
    final SSEntityE   entityType) throws Exception{
  }

  @Override
  public List<SSUri> getSubEntities(
    final SSUri user, 
    final SSUri entity, 
    final SSEntityE type) throws Exception{
    
    return new ArrayList<>();
  }

  @Override
  public List<SSUri> getParentEntities(
    final SSUri user, 
    final SSUri entity, 
    final SSEntityE type) throws Exception{
    
    return new ArrayList<>();
  }
  
  @Override
  public void getUsersResources(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> usersResources) throws Exception{
    
    final List<SSEntityE> types = new ArrayList<>();
    
    types.add(SSEntityE.file);
    types.add(SSEntityE.entity);
    
    for(String user : allUsers){
      
      for(SSUri entity : sqlFct.getEntityURIs(SSUri.get(user), types)){
        
        if(usersResources.containsKey(user)){
          usersResources.get(user).add(entity);
        }else{
          
          final List<SSUri> resourceList = new ArrayList<>();
          
          resourceList.add(entity);
          
          usersResources.put(user, resourceList);
        }
      }
    }
    
    for(Map.Entry<String, List<SSUri>> resourcesPerUser : usersResources.entrySet()){
      SSStrU.distinctWithoutNull2(resourcesPerUser.getValue());
    }
  }
  
  @Override
  public void getUserRelations(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> userRelations) throws Exception{
    
    SSEntityUserRelationsGatherFct.getUserRelations(
      sqlFct, 
      allUsers, 
      userRelations);
  }
  
  @Override
  public void entityCopy(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSEntityCopyPar par = (SSEntityCopyPar) parA.getFromJSON(SSEntityCopyPar.class);
    
    sSCon.writeRetFullToClient(SSEntityCopyRet.get(entityCopy(par)));
    
    SSEntityActivityFct.copyEntityForUsers(par);
  }
  
  @Override
  public Boolean entityCopy(final SSEntityCopyPar par) throws Exception{
    
    try{   
      
      SSServCallerU.canUserEditEntity(par.user, par.entity);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSEntityE entityType = sqlFct.getEntity(par.entity).type;
        
      for(SSServContainerI serv : SSServReg.inst.getServsHandlingEntities()){
        ((SSEntityHandlerImplI) serv.serv()).copyEntity(
          par.user,
          par.users,
          par.entity,
          par.entitiesToExclude,
          entityType);
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
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entitiesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSEntitiesGetPar par = (SSEntitiesGetPar) parA.getFromJSON(SSEntitiesGetPar.class);
    
    sSCon.writeRetFullToClient(SSEntitiesGetRet.get(entitiesGet(par)));
  }
  
  @Override
  public List<SSEntity> entitiesGet(final SSEntitiesGetPar par) throws Exception{
    
    //TODO to be handled via entity handler like service overarching call; now its done with the help of access restrictions (i.e., circles)
    try{
      
      final List<SSEntity> entities = new ArrayList<>();
      
      if(
        par.user == null &&
        par.entities.isEmpty() &&
        par.types.isEmpty()){ //no information on what to query given
        
        return entities;
      }
        
      if(!par.entities.isEmpty()){
        
        SSStrU.distinctWithoutNull2(par.entities);
        
        for(SSUri entity : par.entities){
          
          SSEntity.addEntitiesDistinctWithoutNull(
            entities,
            entityGet(
              new SSEntityGetPar(
                null,
                null,
                par.user,
                entity,
                par.withUserRestriction,
                par.descPar)));
        }
        
        return entities;
      }
      
      if(!par.types.isEmpty()){
        
        for(SSEntity entity : sqlFct.getAccessibleEntityURIs(par.user, true, par.types)){
          
//      for(SSUri circle : sqlFct.getCircleURIsForUser(par.forUser, par.withSystemCircles)){
          
//        for(SSEntity entity : sqlFct.getEntitiesForCircle(circle, par.types)){
          
          SSEntity.addEntitiesDistinctWithoutNull(
            entities,
            entityGet(
              new SSEntityGetPar(
                null,
                null,
                par.user,
                entity.id,
                par.withUserRestriction, //withUserRestriction
                par.descPar))); //descPar
        }
      }
      
      return entities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSEntityGetPar par = (SSEntityGetPar) parA.getFromJSON(SSEntityGetPar.class);
    
    sSCon.writeRetFullToClient(SSEntityGetRet.get(entityGet(par)));
  }
  
  @Override
  public SSEntity entityGet(final SSEntityGetPar par) throws Exception{
    
    try{
      
      if(par.entity == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      SSEntity entity = sqlFct.getEntity(par.entity);
      
      if(entity == null){
        return null;
      }
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, entity.id)){
          return null;
        }
      }
        
      if(par.descPar != null){
      
        par.descPar.user                = par.user;
        par.descPar.withUserRestriction = par.withUserRestriction;
        
        if(par.descPar.setAttachedEntities){
          
          final List<SSUri>          attachedEntityURIs    = sqlFct.getAttachedEntityURIs(par.entity);
          final SSEntityDescriberPar attachedEntityDescPar = new SSEntityDescriberPar(null);
          
          attachedEntityDescPar.user                = par.user;
          attachedEntityDescPar.withUserRestriction = par.withUserRestriction;
        
          SSEntity.addEntitiesDistinctWithoutNull(
            entity.attachedEntities,
            entitiesGet(
              new SSEntitiesGetPar(
                null,
                null,
                par.user,
                attachedEntityURIs, //entities
                null, //types,
                attachedEntityDescPar, //descPar,
                par.withUserRestriction)));
        }
        
        if(par.descPar.setRead){
          entity.read = sqlFct.getEntityRead (par.user, entity.id);
        }
        
        for(SSServContainerI serv : SSServReg.inst.getServsHandlingEntities()){
          entity = ((SSEntityHandlerImplI) serv.serv()).getUserEntity(entity, par.descPar);
        }
      }
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSEntity entityFromTypeAndLabelGet(final SSEntityFromTypeAndLabelGetPar par) throws Exception{
    
    try{
      
      if(
        par.type == null ||
        par.label == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
        
      final SSEntity entity = sqlFct.getEntity(par.label, par.type);
      
      if(entity == null){
        return null;
      }
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, entity.id)){
          return null;
        }
      }
      
      return entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityUpdate(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSEntityUpdatePar par = (SSEntityUpdatePar) parA.getFromJSON(SSEntityUpdatePar.class);
    final SSUri             entityURI;
    
    if(
      par.entity == null &&
      par.type   == null){
      throw new SSErr(SSErrE.parameterMissing);
    }
        
    if(par.entity == null){
      
      switch(par.type){
        case placeholder:{
          
          par.entity = SSServCaller.vocURICreate();
          
          ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventAdd(
            new SSUEAddPar(
              null,
              null,
              par.user,
              par.entity,
              SSUEE.bnpPlaceholderAdd,
              null, //content
              par.creationTime,
              par.withUserRestriction,
              false)); //shouldCommit
          break;
        }
        
        default: throw new SSErr(SSErrE.entityTypeNotSupported);
      }
    }
    
    entityURI = entityUpdate(par);
      
    sSCon.writeRetFullToClient(SSEntityUpdateRet.get(entityURI));
  }
  
  @Override
  public SSUri entityUpdate(final SSEntityUpdatePar par) throws Exception{
    
    SSEntity entityToAttach;
    
    try{
      
      if(par.entity == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      SSServCallerU.canUserReadEntity(par.user, par.entity);
      
      if(par.type == null){
        par.type = SSEntityE.entity;
      }

      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addEntityIfNotExists(
        par.entity,
        par.type,
        par.label,
        par.description,
        par.user,
        par.creationTime);
      
      final SSUri privateCircleURI =
        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlePrivURIGet(
          new SSCirclePrivURIGetPar(
            null,
            null,
            par.user, 
            false)); //shouldCommit
      
      sqlFct.addEntityToCircleIfNotExists(
          privateCircleURI, 
          par.entity);
      
      for(SSUri entityURIToAttach : par.entitiesToAttach){
        
        entityToAttach = sqlFct.getEntity(entityURIToAttach);
        
        if(entityToAttach == null){
          
          sqlFct.addEntityIfNotExists(
            entityURIToAttach,
            SSEntityE.entity,
            null,
            null,
            par.user,
            null);
          
        }else{
          
          if(par.withUserRestriction){
            SSServCallerU.canUserReadEntity(par.user, entityURIToAttach);
          }
        }
        
        sqlFct.attachEntity(par.entity, entityURIToAttach);
      }
      
      if(par.read != null){
        
        sqlFct.setEntityRead(
          par.user, 
          par.entity,
          par.read);
      }
      
      if(
        par.setPublic != null &&
        par.setPublic){
        
        entityShare(
          new SSEntitySharePar(
            null,
            null,
            par.user,
            par.entity, //entity,
            null, //users,
            null, //circles,
            true, //setPublic,
            null, //comment,
            par.withUserRestriction,
            false)); //shouldCommit))
      }
      
      dbSQL.commit(par.shouldCommit);
      
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
  public List<SSEntity> entitiesForLabelsAndDescriptionsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntitiesForLabelsAndDescriptionsGetPar  par = new SSEntitiesForLabelsAndDescriptionsGetPar(parA);
      
      //TODO a lot of improvement and error handling here
      
      if(!par.requireds.isEmpty()){
        return sqlFct.getEntitiesForLabelsAndDescriptionsWithSQLLike(par.requireds, par.requireds, SSSearchOpE.and);
      }
      
      if(!par.eithers.isEmpty()){
        return sqlFct.getEntitiesForLabelsAndDescriptionsWithSQLLike(par.eithers, par.eithers, SSSearchOpE.or);
      }
      
      if(!par.absents.isEmpty()){
        throw new UnsupportedOperationException("absents not suppported yet");
      }
      
      return new ArrayList<>();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> entitiesForLabelsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntitiesForLabelsGetPar  par      = new SSEntitiesForLabelsGetPar(parA);

      //TODO a lot of improvment and error handling here
      
      if(!par.requireds.isEmpty()){
        return sqlFct.getEntitiesForLabelsAndDescriptionsWithSQLLike(par.requireds, new ArrayList<>(), SSSearchOpE.and);
        //TODO could also use public List<SSEntity> getEntitiesForLabelsAndDescriptions(final List<String> requireds,final List<String> absents,final List<String> eithers);
        //here, although it depends on the innodb_ft_min_token_size setting for InnoDB MYSQL tables then, and stopwords and so on
        //see http://dba.stackexchange.com/questions/51144/mysql-match-against-boolean-mode and http://dev.mysql.com/doc/refman/5.6/en/fulltext-stopwords.html
        //please consider that the actual version doesnt exploit MYSQL indexes on labels and descriptions
      }
      
      if(!par.eithers.isEmpty()){
        return sqlFct.getEntitiesForLabelsAndDescriptionsWithSQLLike(par.eithers, new ArrayList<>(), SSSearchOpE.or);
      }
      
      if(!par.absents.isEmpty()){
        throw new UnsupportedOperationException("absents not suppported yet");
      }
      
      return new ArrayList<>();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> entitiesForDescriptionsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntitiesForDescriptionsGetPar  par = new SSEntitiesForDescriptionsGetPar(parA);
      
      //TODO a lot of improvement and error handling here
      
      if(!par.requireds.isEmpty()){
        return sqlFct.getEntitiesForLabelsAndDescriptionsWithSQLLike(new ArrayList<>(), par.requireds, SSSearchOpE.and);
      }
      
      if(!par.eithers.isEmpty()){
        return sqlFct.getEntitiesForLabelsAndDescriptionsWithSQLLike(new ArrayList<>(), par.eithers, SSSearchOpE.or);
      }
      
      if(!par.absents.isEmpty()){
        throw new UnsupportedOperationException("absents not suppported yet");
      }
      
      return new ArrayList<>();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityRemove(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityRemovePar par = new SSEntityRemovePar(parA);
      
      sqlFct.deleteEntityIfExists(par.entity);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityRemove(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> entityUserParentEntitiesGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserParentEntitiesGetPar par = new SSEntityUserParentEntitiesGetPar(parA);

      SSServCallerU.canUserReadEntity(par.user, par.entity);
      
      final SSEntityE    entityType   = sqlFct.getEntity(par.entity).type;
      final List<SSUri>  entities     = new ArrayList<>();
      
      for(SSServContainerI serv : SSServReg.inst.getServsHandlingEntities()){
        entities.addAll(((SSEntityHandlerImplI) serv.serv()).getParentEntities(par.user, par.entity, entityType));
      }
      
      SSStrU.distinctWithoutNull2(entities);
      
      return entities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> entityUserSubEntitiesGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserSubEntitiesGetPar par = new SSEntityUserSubEntitiesGetPar(parA);
      final List<SSUri>                   subEntities = new ArrayList<>();
      final SSEntityE                     entityType;
      
      SSServCallerU.canUserReadEntity(par.user, par.entity);
      
      entityType = sqlFct.getEntity(par.entity).type;
      
      switch(entityType){
        
        case entity: {
          return new ArrayList<>();
        }
        
        default: {
          
          for(SSServContainerI serv : SSServReg.inst.getServsHandlingEntities()){
            
            subEntities.addAll(
              ((SSEntityHandlerImplI) serv.serv()).getSubEntities(
                par.user, 
                par.entity, 
                entityType));
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
  public SSUri entityAttachmentsRemove(final SSEntityAttatchmentsRemovePar par) throws Exception{
    
    try{

      if(par.withUserRestriction){
        SSServCallerU.canUserEditEntity   (par.user, par.entity);
        SSServCallerU.canUserReadEntities (par.user, par.attachments);
      }
      
      sqlFct.removeAttachments(par.entity, par.attachments);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityAttachmentsRemove(par);
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
  public void entityShare(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
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
  }
  
  @Override  
  public SSUri entityShare(final SSEntitySharePar par) throws Exception{
    
    try{
      
      if(
        par.users.isEmpty() &&
        par.circles.isEmpty() &&
        !par.setPublic){
        return par.entity;
      }
      
      if(par.withUserRestriction){
        
        if(SSObjU.isNull(par.user)){
          throw new SSErr(SSErrE.parameterMissing);
        }
        
        if(par.setPublic){
          SSServCallerU.canUserAllEntity(par.user, par.entity);
        }else{
          SSServCallerU.canUserEditEntity(par.user, par.entity);
        }
      }
      
      final List<SSEntity> entities =
        entitiesGet(
          new SSEntitiesGetPar(
            null,
            null,
            par.user,
            SSUri.asListWithoutNullAndEmpty(par.entity),
            null, //types,
            null, //descPar,
            par.withUserRestriction));
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(!par.users.isEmpty()){
        
        if(SSStrU.contains(par.users, par.user)){
          throw new SSErr(SSErrE.userCannotShareWithHimself);
        }
        
        SSServCallerU.checkWhetherUsersAreUsers(par.users);
        
        final SSUri           circleUri =
          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleCreate(
            new SSCircleCreatePar(
              null,
              null,
              SSVocConf.systemUserUri, //user
              par.user, //forUser,
              SSCircleE.group,
              null, //label
              null, //description
              true,  //isSystemCircle
              false, //withUserRestriction
              false)); //shouldCommit
        
        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleUsersAdd(
          new SSCircleUsersAddPar(
            null,
            null,
            par.user,
            circleUri, //circle
            par.users, //users
            false, //withUserRestriction
            false)); //shouldCommit
        
        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            null,
            null,
            par.user,
            circleUri, //circle
            SSUri.asListWithoutNullAndEmpty(par.entity),  //entities
            false, //withUserRestriction
            false)); //shouldCommit
        
        for(SSServContainerI serv : SSServReg.inst.getServsHandlingEntities()){
          
          ((SSEntityHandlerImplI) serv.serv()).circleContentChanged(
            new SSCircleContentChangedPar(
              par.user,
              circleUri,
              false, //isPublicCircle
              null,  //usersToAdd
              entities, //entitiesToAdd,
              par.users,  //usersToPushEntitiesTo
              null, //circleUsers
              null)); //circleEntities
        }
      }
        
      if(!par.circles.isEmpty()){

        for(SSUri circleURI : par.circles){
          
          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntitiesAdd(
            new SSCircleEntitiesAddPar(
              null, 
              null, 
              par.user,
              circleURI,  //circle
              SSUri.asListWithoutNullAndEmpty(par.entity),  //entities
              par.withUserRestriction, //withUserRestriction
              false)); //shouldCommit
          
          final SSEntityCircle circle =
            ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleGet(
              new SSCircleGetPar(
                null,
                null,
                par.user,
                circleURI, //circle
                SSEntityE.asListWithoutNullAndEmpty(), //entityTypesToIncludeOnly
                par.withUserRestriction, //withUserRestriction
                false)); //invokeEntityHandlers
          
          for(SSServContainerI serv : SSServReg.inst.getServsHandlingEntities()){
            
            ((SSEntityHandlerImplI) serv.serv()).circleContentChanged(
              new SSCircleContentChangedPar(
                par.user,
                circleURI,
                false,    //isPublicCircle
                null,         //usersToAdd
                entities, //entitiesToAdd,
                null,  //usersToPushEntitiesTo
                SSUri.getFromEntitites(circle.users), //circleUsers
                null)); //circleEntities
          }
        }
      }
      
      if(par.setPublic){
        
        final SSUri pubCircleURI = 
          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlePubURIGet(
            new SSCirclePubURIGetPar(
              null,
              null,
              par.user,
              false));
        
        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            null,
            null,
            par.user,
            pubCircleURI,  //circle
            SSUri.asListWithoutNullAndEmpty(par.entity),  //entities
            false, //withUserRestriction
            false)); //shouldCommit
        
        for(SSServContainerI serv : SSServReg.inst.getServsHandlingEntities()){
            
            ((SSEntityHandlerImplI) serv.serv()).circleContentChanged(
              new SSCircleContentChangedPar(
                par.user,
                pubCircleURI,
                true,     //isPublicCircle
                null,     //usersToAdd
                entities, //entitiesToAdd,
                null,     //usersToPushEntitiesTo
                null,    //circleUsers
                null));  //circleEntities
          }
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


//download section
//@Override
//  public List<SSUri> entityDownloadURIsGet(SSServPar parA) throws Exception{
//    
//    try{
//      final SSEntityDownloadURIsGetPar par = new SSEntityDownloadURIsGetPar(parA);
//      
//      return sqlFct.getDownloads(par.entity);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//public void addDownload(
//    final SSUri   entity, 
//    final SSUri   download) throws Exception{
//    
//    try{
//
//      final Map<String, String> inserts    = new HashMap<>();
//      final Map<String, String> uniqueKeys = new HashMap<>();
//      
//      insert(inserts, SSSQLVarNames.entityId,      entity);
//      insert(inserts, SSSQLVarNames.downloadId,     download);
//      
//      uniqueKey(uniqueKeys, SSSQLVarNames.entityId,    entity);
//      uniqueKey(uniqueKeys, SSSQLVarNames.downloadId,  download);
//      
//      dbSQL.insertIfNotExists(SSSQLVarNames.downloadsTable, inserts, uniqueKeys);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }

//public List<SSUri> getDownloads(final SSUri entity) throws Exception{
//    
//    ResultSet resultSet = null;
//    
//    try{
//      
//      final List<String>        columns           = new ArrayList<>();
//      final Map<String, String> wheres            = new HashMap<>();
//      
//      column(columns, SSSQLVarNames.downloadId);
//      
//      where(wheres, SSSQLVarNames.entityId, entity);
//      
//      resultSet = dbSQL.select(SSSQLVarNames.downloadsTable, columns, wheres, null, null, null);
//      
//      return getURIsFromResult(resultSet, SSSQLVarNames.downloadId);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }

//public List<SSUri>                     entityDownloadURIsGet                    (final SSServPar parA) throws Exception;