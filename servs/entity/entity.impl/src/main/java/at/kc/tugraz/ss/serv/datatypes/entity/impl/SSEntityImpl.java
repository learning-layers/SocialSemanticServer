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

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityClientI;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForDescriptionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForLabelsAndDescriptionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForLabelsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDownloadURIsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityEntitiesAttachedGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityExistsPar;
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
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCopyPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserEntitiesAttachPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserParentEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserSubEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityDescGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityDescsGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserAddRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserCopyRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserDirectlyAdjoinedEntitiesRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserUpdateRet;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntityActivityFct;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntityUserRelationsGatherFct;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchOpE;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSEntityA;
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
import at.tugraz.sss.serv.caller.SSServCallerU;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;

public class SSEntityImpl extends SSServImplWithDBA implements SSEntityClientI, SSEntityServerI, SSUserRelationGathererI, SSUsersResourcesGathererI{
  
  private final SSEntitySQLFct         sqlFct;
  
  public SSEntityImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbSQL);
    
    sqlFct = new SSEntitySQLFct   (this);
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
  public Boolean entityReadGet(final SSServPar parA) throws Exception{
  
    try{
      final SSEntityReadGetPar par = new SSEntityReadGetPar(parA);
      
      return sqlFct.getEntityRead(par.user, par.entity);
    }catch(Exception error){      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityCopy(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserCopyRet.get(entityUserCopy(parA), parA.op), parA.op);
    
    SSEntityActivityFct.copyEntityForUsers(new SSEntityUserCopyPar(parA));
  }
  
  @Override
  public Boolean entityUserCopy(final SSServPar parA) throws Exception{
    
    try{   
      
      final SSEntityUserCopyPar par = new SSEntityUserCopyPar(parA);
      
      SSServCallerU.canUserEditEntity(par.user, par.entity);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSEntityE entityType = SSServCaller.entityGet(par.entity).type;
      
      switch(entityType){
        case entity:{
          SSLogU.warn("entity couldnt be copied by entity handlers");
          break;
        }
        
        default:{
          
          for(SSServContainerI serv : SSServReg.inst.getServsManagingEntities()){
            if(((SSEntityHandlerImplI) serv.serv()).copyUserEntity(par.user, par.users, par.entity, par.entitiesToExclude, entityType)){
              break;
            }
          }
        }
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return entityUserCopy(parA);
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
  public void entityUpdate(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserUpdateRet.get(entityUserUpdate(parA), parA.op), parA.op);

//    SSEntityActivityFct.entityUpdate(new SSEntityUserUpdatePar(parA));
  }
  
  @Override
  public SSUri entityUserUpdate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUserUpdatePar par = new SSEntityUserUpdatePar(parA);

      SSServCallerU.canUserEditEntity(par.user, par.entity);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addEntityIfNotExists(
        par.entity, 
        SSEntityE.entity, 
        par.label, 
        par.description, 
        par.user, 
        null);
      
      if(par.read != null){
        
        sqlFct.setEntityRead(
          par.user, 
          par.entity,
          par.read);
      }
      
      if(!par.comments.isEmpty()){
        SSServCaller.entityUpdate(
          par.user, 
          par.entity, 
          null, 
          null, 
          par.comments, 
          new ArrayList<>(), 
          new ArrayList<>(), 
          new ArrayList<>(), 
          new ArrayList<>(), 
          false);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
      
   }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return entityUserUpdate(parA);
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
  public SSUri entityUpdate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUpdatePar par = new SSEntityUpdatePar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addEntityIfNotExists(
        par.entity,
        null,
        par.label,
        par.description,
        par.user,
        null);
      
      for(SSUri screenShot : par.screenShots){
        
        sqlFct.addImage(
          screenShot, 
          SSImageE.screenShot);
        
        sqlFct.attachEntity(par.entity, screenShot);
      }
      
      for(SSUri download : par.downloads){
        sqlFct.addDownload     (par.entity, download);
      }
      
      for(SSUri image : par.images){
        
         sqlFct.addImage(
          image, 
          SSImageE.image);
         
        sqlFct.attachEntity(par.entity, image);
      }
      
      for(SSServContainerI serv : SSServReg.inst.getServsUpdatingEntities()){
        ((SSEntityUpdaterI) serv.serv()).updateEntity(par);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
     }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return entityUpdate(parA);
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
  public void entityDescsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityDescsGetRet.get(entityDescsGet(parA), parA.op), parA.op);
  }
  
  @Override
  public List<SSEntityA> entityDescsGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityDescsGetPar  par      = new SSEntityDescsGetPar(parA);
      final List<SSEntityA>      entities = new ArrayList<>();
      
      final SSEntityDescGetPar entityDescGetPar = 
        new SSEntityDescGetPar(
          par.user,
          null, 
          par.getTags, 
          par.getOverallRating, 
          par.getDiscs, 
          par.getUEs, 
          par.getThumb, 
          par.getFlags, 
          false);
      
      for(SSEntity entity : sqlFct.getEntities(par.entities, par.types)){
        
        entityDescGetPar.entity = entity.id;
        
        setReadAndFileInformation(par.user, entity);
        
        for(SSServContainerI serv : SSServReg.inst.getServsDescribingEntities()){
          entity = ((SSEntityDescriberI) serv.serv()).getDescForEntity(entityDescGetPar, entity);
        }
        
        entities.add(entity);
      }
        
      return entities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityDescGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityDescGetRet.get(entityDescGet(parA), parA.op), parA.op);
  }
  
  @Override
  public SSEntity entityDescGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityDescGetPar  par    = new SSEntityDescGetPar(parA);
      SSEntity                  entity = sqlFct.getEntity     (par.entity);
      
      setReadAndFileInformation(par.user, entity);
      
      for(SSServContainerI serv : SSServReg.inst.getServsDescribingEntities()){
        entity = ((SSEntityDescriberI) serv.serv()).getDescForEntity(par, entity);
      }
      
      return entity;
      
    }catch(Exception error){
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
  public void entityGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserGetRet.get(entityUserGet(parA), parA.op), parA.op);
  }
  
  @Override
  public SSEntity entityUserGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserGetPar par = new SSEntityUserGetPar(parA);
      SSEntity                 entity;
      
      SSServCallerU.canUserReadEntity(par.user, par.entity, par.logErr);
      
      entity = sqlFct.getEntity(par.entity);
      
      switch(entity.type){
        
        case circle:{
          return SSServCaller.circleGet(
            par.user,
            par.forUser,
            par.entity,
            SSEntityE.asListWithoutNullAndEmpty(), //entityTypesToIncludeOnly
            false,
            true,
            false);
        }
        
        default:{
          for(SSServContainerI serv : SSServReg.inst.getServsDescribingEntities()){
            entity = ((SSEntityDescriberI) serv.serv()).getUserEntity(par.user, entity);
          }
          
          return entity;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error, parA.logErr);
      return null;
    }
  }
  
  @Override
  public void entityDirectlyAdjoinedEntitiesRemove(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserDirectlyAdjoinedEntitiesRemoveRet.get(entityUserDirectlyAdjoinedEntitiesRemove(parA), parA.op), parA.op);
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
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return entityUserDirectlyAdjoinedEntitiesRemove(parA);
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
  public SSEntity entityGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityGetPar par = new SSEntityGetPar(parA);
      
      if(par.entity != null){
        return sqlFct.getEntity(par.entity);
      }
      
      if(!SSObjU.isNull(par.label, par.type)){
        return sqlFct.getEntity(par.label, par.type);  
      }
      
      throw new SSErr(SSErrE.entityCouldntBeQueried);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean entityExists(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityExistsPar par = new SSEntityExistsPar(parA);
      
      if(par.entity != null){
        sqlFct.getEntity(par.entity);
        return true;
      }
      
      if(!SSObjU.isNull(par.label, par.type)){
        sqlFct.getEntity(par.label, par.type);
        return true;
      }
      
      throw new SSErr(SSErrE.entityCouldntBeQueried);
      
    }catch(SSErr error){
      
      if(SSServErrReg.containsErr(SSErrE.entityDoesntExist)){
        SSServErrReg.reset();
        return false;
      }
      
      throw error;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void entityAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserAddRet.get(entityUserAdd(parA), parA.op), parA.op);
  }
  
  @Override
  public SSUri entityUserAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUserAddPar par = new SSEntityUserAddPar(parA);
      final SSUri              entityUri;
      
      if(par.link != null){
        entityUri = par.link;
      }else{
        entityUri = SSServCaller.vocURICreate();
      }

      if(par.type == null){
        par.type = SSEntityE.placeholder;
      }
      
      switch(par.type){
        case entity:
        case placeholder:
          break;
                
        default: throw new SSErr(SSErrE.entityTypeNotSupported);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntityToPrivCircleAdd(
        par.user,
        entityUri,
        par.type,
        par.label,
        par.description,
        par.creationTime,
        false);
      
      SSServCaller.uEAddAtCreationTime(
        par.user,
        entityUri,
        SSUEE.bnpPlaceholderAdd,
        SSStrU.empty,
        par.creationTime,
        false);
      
      dbSQL.commit(par.shouldCommit);
      
      return entityUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return entityUserAdd(parA);
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
  public SSUri entityAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityAddPar par = new SSEntityAddPar(parA);

      sqlFct.addEntityIfNotExists(
        par.entity, 
        par.type, 
        par.label,
        par.description, 
        par.user, 
        par.creationTime);

      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return entityAdd(parA);
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
  public SSUri entityRemove(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityRemovePar par = new SSEntityRemovePar(parA);
      
      sqlFct.deleteEntityIfExists(par.entity);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return entityRemove(parA);
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
  public List<SSUri> entityUserParentEntitiesGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserParentEntitiesGetPar par = new SSEntityUserParentEntitiesGetPar(parA);

      SSServCallerU.canUserReadEntity(par.user, par.entity);
      
      final SSEntityE    entityType   = SSServCaller.entityGet(par.entity).type;
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
      
      entityType = SSServCaller.entityGet(par.entity).type;
      
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
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return entityThumbAdd(parA);
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
  public List<SSUri> entityUserEntitiesAttach(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserEntitiesAttachPar par = new SSEntityUserEntitiesAttachPar(parA);
      
      SSServCallerU.canUserEditEntity(par.user, par.entity);
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri entity : par.entities){
        
        SSServCallerU.canUserReadEntity(par.user, entity);
        
        sqlFct.addEntityIfNotExists(
          entity, 
          SSEntityE.entity,  
          null, 
          null, 
          par.user, 
          null);
        
        sqlFct.attachEntity(par.entity, entity);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entities;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return entityUserEntitiesAttach(parA);
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
      
      for(SSLocation location : par.locations){
        
        SSServCaller.entityEntityToPubCircleAdd(
          par.user,
          location.id,
          SSEntityE.location,
          null,
          null,
          null,
          false);
        
        sqlFct.addLocation(
          location.id,
          par.entity,
          location);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return entityLocationsAdd(parA);
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
  public SSUri entityFileAdd(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityFileAddPar par = new SSEntityFileAddPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addFile(par.entity, par.file);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.file;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return entityFileAdd(parA);
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