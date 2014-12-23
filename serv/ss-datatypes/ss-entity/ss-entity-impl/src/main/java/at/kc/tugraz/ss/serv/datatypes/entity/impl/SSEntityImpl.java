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

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityClientI;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityDescGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserDirectlyAdjoinedEntitiesRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.datatypes.datatypes.SSImage;
import at.kc.tugraz.ss.datatypes.datatypes.SSImageE;
import at.kc.tugraz.ss.datatypes.datatypes.SSLocation;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForDescriptionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForLabelsAndDescriptionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForLabelsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDownloadURIsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityEntitiesAttachedGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityExistsPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityFileAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityFilesGetPar;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityLocationsAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityLocationsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityReadGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityScreenShotsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityThumbAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityThumbsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCopyPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserEntitiesAttachPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserSubEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserParentEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityDescsGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserCopyRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserUpdateRet;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntityActivityFct;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.SSEntityUserRelationsGatherFct;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityDescriberI;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSEntityUpdaterI;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSUserRelationGathererI;
import at.kc.tugraz.ss.serv.serv.api.SSUsersResourcesGathererI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sss.serv.err.datatypes.SSErr;
import sss.serv.err.datatypes.SSErrE;

public class SSEntityImpl extends SSServImplWithDBA implements SSEntityClientI, SSEntityServerI, SSUserRelationGathererI, SSUsersResourcesGathererI{
  
  private final SSEntitySQLFct         sqlFct;
  
  public SSEntityImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, null, dbSQL);
    
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
      
      for(SSUri entity : sqlFct.getEntities(SSUri.get(user), types)){
        
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
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserCopyRet.get(entityUserCopy(parA), parA.op));
    
    SSEntityActivityFct.copyEntityForUsers(new SSEntityUserCopyPar(parA));
  }
  
  @Override
  public Boolean entityUserCopy(final SSServPar parA) throws Exception{
    
    try{   
      
      final SSEntityUserCopyPar par = new SSEntityUserCopyPar(parA);
      
      SSServCaller.entityUserCanEdit(par.user, par.entity);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSEntityE entityType = SSServCaller.entityGet(par.entity).type;
      
      switch(entityType){
        case entity:{
          SSLogU.warn("entity couldnt be copied by entity handlers");
          break;
        }
        
        default:{
          
          for(SSServA serv : SSServA.getServsManagingEntities()){
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
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
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserUpdateRet.get(entityUserUpdate(parA), parA.op));
    
    SSEntityActivityFct.entityUpdate(new SSEntityUserUpdatePar(parA));
  }
  
  @Override
  public SSUri entityUserUpdate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUserUpdatePar par = new SSEntityUserUpdatePar(parA);

      SSServCaller.entityUserCanEdit(par.user, par.entity);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addEntityIfNotExists(
        par.entity, 
        SSEntityE.entity, 
        par.label, 
        par.description, 
        par.user, 
        null);
      
      if(par.read){
        sqlFct.entityRead(par.user, par.entity);
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
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
      
      for(SSServA serv : SSServA.getServsUpdatingEntities()){
        ((SSEntityUpdaterI) serv.serv()).updateEntity(par);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
     }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
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
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityDescsGetRet.get(entityDescsGet(parA), parA.op));
    
    SSServA.removeClientRequ(parA.op, SSStrU.toStr(parA.user), this);
  }
  
  @Override
  public List<SSEntityA> entityDescsGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityDescsGetPar  par      = new SSEntityDescsGetPar(parA);
      final List<SSEntityA>      entities = new ArrayList<>();
      
      for(SSUri entityUri : par.entities){
        
        if(
          !par.types.isEmpty() &&
          !SSStrU.contains(par.types, sqlFct.getEntity (entityUri).type)){
          continue;
        }
        
        entities.add(
          SSServCaller.entityDescGet(
            par.user,
            entityUri,
            par.getTags,
            par.getOverallRating,
            par.getDiscs,
            par.getUEs,
            par.getThumb,
            par.getFlags,
            false));
      }
      
      if(par.entities.isEmpty()){
        
        if(par.types.isEmpty()){
          throw new Exception("either types or entites must be set");
        }
        
        for(SSUri entityUri : sqlFct.getEntities(par.user, par.types)){
          
          entities.add(
            SSServCaller.entityDescGet(
              par.user,
              entityUri,
              par.getTags,
              par.getOverallRating,
              par.getDiscs,
              par.getUEs,
              par.getThumb,
              par.getFlags,
              false));
        }
      }
      
      return entities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityDescGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityDescGetRet.get(entityDescGet(parA), parA.op));
    
    SSServA.removeClientRequ(parA.op, SSStrU.toStr(parA.user), this);
  }
  
  @Override
  public SSEntity entityDescGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityDescGetPar  par    = new SSEntityDescGetPar(parA);
      SSEntity                  entity;
      final List<SSUri>         files;
      
      entity = sqlFct.getEntity (par.entity);
      files  = sqlFct.getFiles  (par.entity);
      
      if(!files.isEmpty()){
        entity.file = files.get(0);
      }
        
      for(SSServA serv : SSServA.getServsDescribingEntities()){
        entity = ((SSEntityDescriberI) serv.serv()).getDescForEntity(par, entity);
      }
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    final SSUri userFromOIDC = SSServCaller.checkKey(parA);
    
    if(userFromOIDC != null){
      parA.user = userFromOIDC;
    }
    
    sSCon.writeRetFullToClient(SSEntityUserGetRet.get(entityUserGet(parA), parA.op));
  }
  
  @Override
  public SSEntity entityUserGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserGetPar par = new SSEntityUserGetPar(parA);
      SSEntity                 entity;
      
      SSServCaller.entityUserCanRead(par.user, par.entity);
      
      entity = sqlFct.getEntity(par.entity);
      
      switch(entity.type){
        
        case circle:{
          return SSServCaller.circleGet(
            par.user,
            par.forUser,
            par.entity,
            false,
            true);
        }
        
        default:{
          for(SSServA serv : SSServA.getServsDescribingEntities()){
            entity = ((SSEntityDescriberI) serv.serv()).getUserEntity(par.user, entity);
          }
          
          return entity;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityDirectlyAdjoinedEntitiesRemove(final SSSocketCon sSCon, final SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSEntityUserDirectlyAdjoinedEntitiesRemoveRet.get(entityUserDirectlyAdjoinedEntitiesRemove(par), par.op));
  }
  
  @Override
  public SSUri entityUserDirectlyAdjoinedEntitiesRemove(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par    = new SSEntityUserDirectlyAdjoinedEntitiesRemovePar(parA);
      final SSEntity                                      entity = sqlFct.getEntity(par.entity);
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSServA serv : SSServA.getServsManagingEntities()){
        
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
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
      
      return sqlFct.getEntitiesForLabelsAndDescriptions(
        SSStrU.distinctWithoutEmptyAndNull(par.requireds),
        SSStrU.distinctWithoutEmptyAndNull(par.absents),
        SSStrU.distinctWithoutEmptyAndNull(par.eithers));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> entitiesForLabelsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntitiesForLabelsGetPar  par      = new SSEntitiesForLabelsGetPar(parA);

      return sqlFct.getEntitiesForLabels(
        SSStrU.distinctWithoutEmptyAndNull(par.requireds),
        SSStrU.distinctWithoutEmptyAndNull(par.absents),
        SSStrU.distinctWithoutEmptyAndNull(par.eithers));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> entitiesForDescriptionsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntitiesForDescriptionsGetPar  par      = new SSEntitiesForDescriptionsGetPar(parA);
      
      return sqlFct.getEntitiesForDescriptions(
        SSStrU.distinctWithoutEmptyAndNull(par.requireds),
        SSStrU.distinctWithoutEmptyAndNull(par.absents),
        SSStrU.distinctWithoutEmptyAndNull(par.eithers));
      
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
  public SSUri entityAdd(final SSServPar parA) throws Exception {
    
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
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

      SSServCaller.entityUserCanRead(par.user, par.entity);
      
      final SSEntityE    entityType   = SSServCaller.entityGet(par.entity).type;
      final List<SSUri>  entities     = new ArrayList<>();
      
      for(SSServA serv : SSServA.getServsManagingEntities()){
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
      
      SSServCaller.entityUserCanRead(par.user, par.entity);
      
      entityType = SSServCaller.entityGet(par.entity).type;
      
      switch(entityType){
        
        case entity: {
          return new ArrayList<>();
        }
        
        default: {
          for(SSServA serv : SSServA.getServsManagingEntities()){
            
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
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
      
      SSServCaller.entityUserCanEdit(par.user, par.entity);
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri entity : par.entities){
        
        SSServCaller.entityUserCanRead(par.user, entity);
        
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
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