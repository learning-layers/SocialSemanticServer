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
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityPublicSetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivURIGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityClientI;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForDescriptionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForLabelsAndDescriptionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForLabelsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDownloadURIsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityEntitiesAttachedGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityFileAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityFilesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityLocationsAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityLocationsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityReadGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityScreenShotsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityThumbAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityThumbsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityCopyPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserParentEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserSubEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntitiesGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityCopyRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserDirectlyAdjoinedEntitiesRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserUpdateRet;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntityActivityFct;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntityUserRelationsGatherFct;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchOpE;
import at.kc.tugraz.ss.service.userevent.api.SSUEServerI;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSImage;
import at.tugraz.sss.serv.SSImageE;
import at.tugraz.sss.serv.SSLocation;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSEntityUpdaterI;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddPar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityCircle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;

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
    
    sqlFct = new SSEntitySQLFct   (this);
  }
  
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSUri userUri, 
    final SSEntityE entityType, 
    final SSUri entityUri, 
    final Boolean removeUserTags, 
    final Boolean removeUserRatings, 
    final Boolean removeFromUserColls, 
    final Boolean removeUserLocations) throws Exception{

  }

  @Override
  public void setEntityPublic(
    final SSUri     userUri, 
    final SSUri     entityUri, 
    final SSEntityE entityType, 
    final SSUri     publicCircleUri) throws Exception{
    
  }

  @Override
  public void shareEntityWithUsers(
    final SSUri user, 
    final List<SSUri> users, 
    final SSUri entity, 
    final SSUri circle, 
    final SSEntityE entityType, 
    final Boolean saveActivity) throws Exception{
  }

  @Override
  public void addEntityToCircle(
    final SSUri user, 
    final SSUri circle, 
    final List<SSUri> circleUsers, 
    final SSUri entity, 
    final SSEntityE entityType) throws Exception{
  }

  @Override
  public void addUsersToCircle(
    final SSUri user, 
    final List<SSUri> users, 
    final SSEntityCircle circle) throws Exception{
    
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
  public Boolean entityReadGet(final SSEntityReadGetPar par) throws Exception{
  
    try{
      return sqlFct.getEntityRead(par.user, par.entity);
    }catch(Exception error){      
      SSServErrReg.regErrThrow(error);
      return null;
    }
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
        
      for(SSServContainerI serv : SSServReg.inst.getServsManagingEntities()){
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
      
      if(par.withUserRestriction){
        
        if(par.forUser == null){
          par.forUser = par.user;
        }
      }
      
      if(
        par.forUser == null &&
        par.entities.isEmpty() &&
        par.types.isEmpty()){ //no information on what to query given
        
        return entities;
      }
        
      if(!par.entities.isEmpty()){
        
        SSStrU.distinctWithoutNull2(par.entities);
        
        for(SSUri entity : par.entities){
          
          entities.add(
            entityGet(
              new SSEntityGetPar(
                null,
                null,
                par.user,
                entity,
                par.forUser,
                null, //label
                null, //type
                par.withUserRestriction, 
                par.invokeEntityHandlers, 
                par.descPar,
                par.logErr)));
        }
        
        return entities;
      }
      
      for(SSEntity entity : sqlFct.getAccessibleEntityURIs(par.forUser, true, par.types))
        
//      for(SSUri circle : sqlFct.getCircleURIsForUser(par.forUser, par.withSystemCircles)){
        
//        for(SSEntity entity : sqlFct.getEntitiesForCircle(circle, par.types)){
        
        //TODO check whether try around "withUserRestriction" is needed, as getAccessibleEntityURIs should provide only entities which are accessible to the user
        try{
          entities.add(
            entityGet(
              new SSEntityGetPar(
                null, 
                null, 
                par.user, 
                entity.id, 
                par.forUser, 
                null, //label
                null, //type
                par.withUserRestriction, //withUserRestriction
                par.invokeEntityHandlers, //invokeEntityHandlers 
                par.descPar,
                false))); //logErr
            
        }catch(Exception error){
          
          if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
            SSServErrReg.reset();
            continue;
          }
          
          throw error;
        }
//        }
////      }
      
      SSStrU.distinctWithoutNull2(entities);
      
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
      
      SSEntity entity = null;
      
      if(par.entity != null){
        
        entity = sqlFct.getEntity(par.entity);
        
        if(entity == null){
          return null;
        }
        
        if(par.withUserRestriction){
          SSServCallerU.canUserReadEntity(par.user, entity, par.logErr);
        }
        
      }else{
        
        if(!SSObjU.isNull(par.label, par.type)){
          
          entity = sqlFct.getEntity(par.label, par.type);
          
          if(entity == null){
            return null;
          }
          
          if(par.withUserRestriction){
            SSServCallerU.canUserReadEntity(par.user, entity, par.logErr);
          }
        }else{
          throw new SSErr(SSErrE.entityCouldntBeQueried);
        }
      }
      
      if(par.invokeEntityHandlers){
        
        setReadAndFileInformation(par.user, entity);
        
        par.descPar.user    = par.user;
        par.descPar.forUser = par.forUser;
        
        for(SSServContainerI serv : SSServReg.inst.getServsDescribingEntities()){
          entity = ((SSEntityDescriberI) serv.serv()).getUserEntity(entity, par.descPar);
        }
      }
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error, par.logErr);
      return null;
    }
  }
  
  @Override
  public void entityUpdate(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSEntityUpdatePar par = (SSEntityUpdatePar) parA.getFromJSON(SSEntityUpdatePar.class);
    
    sSCon.writeRetFullToClient(SSEntityUserUpdateRet.get(entityUpdate(par)));
  }
  
  @Override
  public SSUri entityUpdate(final SSEntityUpdatePar par) throws Exception{
    
    SSEntity entityToAttach;
    SSEntity entity;
    
    try{

      if(par.entity == null){
        
        if(par.uriAlternative != null){
          par.entity = par.uriAlternative;
        }else{
          par.entity = SSServCaller.vocURICreate();
        }
      }
      
      entity =
        entityGet(
          new SSEntityGetPar(
            null,
            null,
            par.user,
            par.entity,
            null, //forUser
            null, //label
            null, //type
            par.withUserRestriction,
            false, //invokeEntityHandlers,
            null, //descPar,
            true)); //logErr
      
      if(entity == null){
        
        if(par.type == null){
          par.type = SSEntityE.entity;
        }
        
        switch(par.type){
          case entity:
          case placeholder:{
            break;
          }
          
          default: throw new SSErr(SSErrE.entityTypeNotSupported);
        }
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
            par.user));
      
      ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          null,
          null,
          par.user,
          privateCircleURI,
          SSUri.asListWithoutNullAndEmpty(par.entity),
          false,
          false,
          false));
      
      if(entity == null){
        
        switch(par.type){
          case placeholder:{
            
            ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).uEAdd(
              new SSUEAddPar(
                null,
                null,
                par.user,
                par.entity,
                SSUEE.bnpPlaceholderAdd,
                SSStrU.empty,
                par.creationTime,
                false));
            
            break;
          }
        }
      }
      
      for(SSUri screenShot : par.screenShots){
        
        sqlFct.addImage(
          screenShot, 
          SSImageE.screenShot);
        
        sqlFct.attachEntity(par.entity, screenShot);
      }
      
      for(SSUri download : par.downloads){
        sqlFct.addDownload (par.entity, download);
      }
      
      for(SSUri image : par.images){
        
         sqlFct.addImage(
          image, 
          SSImageE.image);
         
        sqlFct.attachEntity(par.entity, image);
      }
      
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
            SSServCallerU.canUserEditEntity(par.user, entityToAttach);
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
      
      if(!par.comments.isEmpty()){
        
        if(!par.comments.isEmpty()){
          
          for(SSServContainerI serv : SSServReg.inst.getServsUpdatingEntities()){
            ((SSEntityUpdaterI) serv.serv()).updateEntity(par);
          }
          
//          SSServCaller.entityUpdate(
//          par.user, 
//          par.entity, 
//          null, 
//          null, 
//          par.comments, 
//          new ArrayList<>(), 
//          new ArrayList<>(), 
//          new ArrayList<>(), 
//          new ArrayList<>(), 
//          false);
        }
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
  
  private void setReadAndFileInformation(
    final SSUri    user, 
    final SSEntity entity) throws Exception{
    
    try{
      final List<SSUri> files = sqlFct.getFiles (entity.id);
      
      entity.read = sqlFct.getEntityRead (user, entity.id);
      
      if(!files.isEmpty()){
        entity.file = files.get(0);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void entityDirectlyAdjoinedEntitiesRemove(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserDirectlyAdjoinedEntitiesRemoveRet.get(entityUserDirectlyAdjoinedEntitiesRemove(parA)));
  }
  
  @Override
  public SSUri entityUserDirectlyAdjoinedEntitiesRemove(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par    = new SSEntityUserDirectlyAdjoinedEntitiesRemovePar(parA);
      final SSEntity                                      entity = sqlFct.getEntity(par.entity);
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSServContainerI serv : SSServReg.inst.getServsManagingEntities()){
        
        ((SSEntityHandlerImplI) serv.serv()).removeDirectlyAdjoinedEntitiesForUser(
          par.user,
          entity.type,
          entity.id,
          par.removeUserTags,
          par.removeUserRatings,
          par.removeFromUserColls,
          par.removeUserLocations);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityUserDirectlyAdjoinedEntitiesRemove(parA);
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
      
      for(SSServContainerI serv : SSServReg.inst.getServsManagingEntities()){
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
      final SSEntityE                     entityType;
      List<SSUri>                         entities;
      
      SSServCallerU.canUserReadEntity(par.user, par.entity);
      
      entityType = sqlFct.getEntity(par.entity).type;
      
      switch(entityType){
        
        case entity: {
          return new ArrayList<>();
        }
        
        default: {
          for(SSServContainerI serv : SSServReg.inst.getServsManagingEntities()){
            
            entities = ((SSEntityHandlerImplI) serv.serv()).getSubEntities(par.user, par.entity, entityType);
            
            if(entities != null){
              return entities;
            }
          }
          
          SSLogU.warn("entity couldnt be searched within by entity handlers");
          return new ArrayList<>();
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityThumbAdd(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityThumbAddPar par = new SSEntityThumbAddPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addThumb(par.entity, par.thumb);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.thumb;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityThumbAdd(parA);
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
  public List<SSUri> entityThumbsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityThumbsGetPar par = new SSEntityThumbsGetPar(parA);
      
      return sqlFct.getThumbs(par.entity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> entityEntitiesAttachedGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityEntitiesAttachedGetPar par = new SSEntityEntitiesAttachedGetPar(parA);
      
      return sqlFct.getAttachedEntities(par.entity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSLocation> entityLocationsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityLocationsGetPar par       = new SSEntityLocationsGetPar(parA);
      
      return sqlFct.getLocations(par.entity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
    
  @Override
  public SSUri entityLocationsAdd(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityLocationsAddPar par         = new SSEntityLocationsAddPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          par.entity, //entity,
          null, //uriAlternative,
          null, //type,
          null, //label,
          null, //description,
          null, //comments,
          null, //downloads,
          null, //screenShots,
          null, //images,
          null, //videos,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          true, //withUserRestriction,
          false)); //shouldCommit
      
      for(SSLocation location : par.locations){
        
        entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            par.user,
            location.id, //entity,
            null, //uriAlternative,
            SSEntityE.location, //type,
            null, //label,
            null, //description,
            null, //comments,
            null, //downloads,
            null, //screenShots,
            null, //images,
            null, //videos,
            null, //entitiesToAttach,
            null, //creationTime,
            null, //read,
            false, //withUserRestriction,
            false)); //shouldCommit
        
        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntityPublicSet(
          new SSCircleEntityPublicSetPar(
            null,
            null,
            par.user,
            location.id, //entity
            false, //withUserRestriction
            false)); //shouldCommit
        
        sqlFct.addLocation(
          location.id,
          par.entity,
          location);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityLocationsAdd(parA);
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
  public SSUri entityFileAdd(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityFileAddPar par = new SSEntityFileAddPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addFile(par.entity, par.file);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.file;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return entityFileAdd(parA);
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
  public List<SSUri> entityFilesGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityFilesGetPar par = new SSEntityFilesGetPar(parA);
      
      return sqlFct.getFiles(par.entity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSImage> entityScreenShotsGet(SSServPar parA) throws Exception{
    
    try{
      final SSEntityScreenShotsGetPar par = new SSEntityScreenShotsGetPar(parA);
      
      return
        SSImage.get(
          sqlFct.getImages(
            par.entity,
            SSImageE.screenShot));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSUri> entityDownloadURIsGet(SSServPar parA) throws Exception{
    
    try{
      final SSEntityDownloadURIsGetPar par = new SSEntityDownloadURIsGetPar(parA);
      
      return sqlFct.getDownloads(par.entity);
      
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