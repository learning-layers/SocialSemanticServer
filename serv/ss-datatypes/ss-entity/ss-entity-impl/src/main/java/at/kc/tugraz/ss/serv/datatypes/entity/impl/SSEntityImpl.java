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

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityClientI;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityDescGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserDirectlyAdjoinedEntitiesRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.sql.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.err.SSEntityUserAccessedOtherUsersPrivateGroupErr;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForDescriptionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForLabelsAndDescriptionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesForLabelsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCanPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityCircleCreatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityCircleURIPrivGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityCommentsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityEntitiesAttachedGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityEntitiesCommentedGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityEntitiesToCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityEntityToPrivCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityExistsPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityFileAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityFilesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCircleCreatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserEntityCircleTypesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCirclesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserEntitiesToCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserEntityCirclesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserEntityUsersGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserPublicSetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserUsersToCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUsersToCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserCircleCreateRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserCirclesGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserEntitiesToCircleAddRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserEntityUsersGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserPublicSetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserUsersToCircleAddRet;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.op.SSEntityMiscFct;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityThumbAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityThumbsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCircleGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCommentsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCopyPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserEntitiesAttachPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserSubEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserParentEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserSharePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityDescsGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserCircleGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserShareRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserUpdateRet;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.activity.SSEntityActivityFct;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.op.SSEntityUserCanFct;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.userrelationgather.SSEntityUserRelationsGatherFct;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSUserRelationGathererI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sss.serv.err.datatypes.SSErr;
import sss.serv.err.datatypes.SSErrE;

public class SSEntityImpl extends SSServImplWithDBA implements SSEntityClientI, SSEntityServerI, SSUserRelationGathererI{
  
  private final SSEntitySQLFct         sqlFct;
  
  public SSEntityImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
    sqlFct    = new SSEntitySQLFct   (this);
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
  public void entityUserCan(final SSServPar parA) throws Exception{
   
    try{

      final SSEntityUserCanPar par    = new SSEntityUserCanPar(parA);
      final SSEntity           entity;
      
      try{
        entity = sqlFct.getEntity(par.entity);
      }catch(Exception error){
        
        if(SSServErrReg.containsErr(SSErrE.entityDoesntExist)){
          SSServErrReg.reset();
          return;
        }
        
        throw error;
      }
      
      SSEntityUserCanFct.checkWhetherUserCanForEntityType(
        sqlFct, 
        par.user, 
        entity, 
        par.accessRight);
      
      SSEntityMiscFct.checkWhetherUserHasRightInAnyCircleOfEntity(
        sqlFct, 
        par.user, 
        par.entity, 
        par.accessRight);
      
    }catch(SSErr error){
      SSServErrReg.regErrThrow (new SSErr(SSErrE.userNotAllowedToAccessEntity));
    }catch(Exception error){      
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void entityCopy(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    SSEntityClientFct.entityUserCopy(sSCon, parA, this);
  }
  
  @Override 
  public Boolean entityUserCopy(final SSServPar parA) throws Exception{
    
    try{   
      
      final SSEntityUserCopyPar par = new SSEntityUserCopyPar(parA);
      
      SSServCaller.entityUserCanEdit(par.user, par.entity);
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSEntityMiscFct.copyEntityByEntityHandlers(
        par.user,
        par.entity,
        par.entitiesToExclude,
        par.users);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityUserCopy(parA);
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
  public void entityShare(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserShareRet.get(entityUserShare(parA), parA.op));
  }
  
  @Override  
  public SSUri entityUserShare(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserSharePar par = new SSEntityUserSharePar(parA);
        
      SSServCaller.entityUserCanEdit                          (par.user, par.entity);
      SSEntityMiscFct.checkWhetherUserWantsToShareWithHimself (par.user, par.users);
      SSEntityMiscFct.checkWhetherUsersAreUsers               (par.users);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri circleUri =
        SSServCaller.entityCircleCreate(
          par.user,
          SSUri.asListWithoutNullAndEmpty(par.entity),
          par.users,
          null,
          null,
          true,
          false);
      
      SSEntityMiscFct.shareByEntityHandlers(
        par.user,
        par.users,
        par.entity,
        circleUri,
        par.saveActivity);
      
      SSEntityActivityFct.shareEntityWithUsers(par);
      
      dbSQL.commit(par.shouldCommit);
      
      return circleUri;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityUserShare(parA);
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
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityUserUpdate(parA);
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
  public SSUri entityUpdate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUpdatePar par = new SSEntityUpdatePar(parA);
      SSUri                   commentUri;
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addEntityIfNotExists(
        par.entity,
        null,
        par.label,
        par.description,
        par.user,
        null);
      
      if(!par.comments.isEmpty()){
        
        for(SSTextComment content : par.comments){
          
          commentUri = SSServCaller.vocURICreate();
          
          sqlFct.addEntityIfNotExists(
            commentUri,
            SSEntityE.comment,
            par.label,
            par.description,
            par.user,
            null);
          
          sqlFct.createComment (commentUri, content);
          sqlFct.addComment    (par.entity, commentUri);
        }
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityUpdate(parA);
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
  public void entityDescsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityDescsGetRet.get(entityDescsGet(parA), parA.op));
    
    SSServA.removeClientRequ(parA.op, SSStrU.toStr(parA.user), this);
  }
  
  @Override
  public List<SSEntityDescA> entityDescsGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityDescsGetPar  par      = new SSEntityDescsGetPar(parA);
      final List<SSEntityDescA>  entities = new ArrayList<>();
      
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
            par.getFlags));
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
              par.getFlags));
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
  public SSEntityDescA entityDescGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityDescGetPar  par    = new SSEntityDescGetPar(parA);
      final SSEntity            entity;
      final List<SSUri>         files;
      final SSUri               file;
      
      entity = sqlFct.getEntity (par.entity);
      files  = sqlFct.getFiles  (par.entity);
      
      if(files.isEmpty()){
        file = null;
      }else{
        file = files.get(0);
      }
      
      return SSEntityMiscFct.getDescForEntityByEntityHandlers(
        par,
        SSEntityDesc.get(
          entity.id,
          entity.type,
          entity.label,
          entity.creationTime,
          new ArrayList<>(),
          null,
          new ArrayList<>(),
          entity.author,
          new ArrayList<>(),
          null, 
          file,
          entity.description,
          new ArrayList<>(),
          SSServCaller.entityCommentsGet(par.user, null, entity.id)));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserGetRet.get(entityUserGet(parA), parA.op));
  }
  
  @Override
  public SSEntity entityUserGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserGetPar par = new SSEntityUserGetPar(parA);

      SSServCaller.entityUserCanRead(par.user, par.entity);
      
      return sqlFct.getEntity(par.entity);
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
      
      SSEntityMiscFct.removeUserEntityDirectlyAdjoinedEntitiesByEntityHandlers(entity, par);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityUserDirectlyAdjoinedEntitiesRemove(parA);
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
  public void entityCircleCreate(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);
    
    final SSUri result = entityUserCircleCreate(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserCircleCreateRet.get(result, parA.op));
    
    SSEntityActivityFct.createCircle(new SSEntityUserCircleCreatePar(parA), result);
  }
  
  @Override
  public SSUri entityUserCircleCreate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUserCircleCreatePar par = new SSEntityUserCircleCreatePar(parA);
      
      SSEntityMiscFct.checkWhetherCircleIsGroupCircle (par.type);
      SSServCaller.entityUserCanEdit                  (par.user, par.entities);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri circleUri =
        SSServCaller.entityCircleCreate(
          par.user,
          par.entities,
          par.users,
          par.label,
          par.description,
          par.isSystemCircle,
          false);
      
      SSEntityMiscFct.addEntitiesToCircleByEntityHandlers(
        par.user,
        par.entities,
        circleUri);
      
      dbSQL.commit(par.shouldCommit);
      
      return circleUri;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityUserCircleCreate(parA);
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
  public void entityEntitiesToCircleAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSEntityUserEntitiesToCircleAddRet.get(entityUserEntitiesToCircleAdd(parA), parA.op));
    
    SSEntityActivityFct.addEntitiesToCircle(new SSEntityUserEntitiesToCircleAddPar(parA));
  }
  
  @Override
  public SSUri entityUserEntitiesToCircleAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUserEntitiesToCircleAddPar par = new SSEntityUserEntitiesToCircleAddPar(parA);
      
      SSEntityMiscFct.checkWhetherUserIsAllowedToEditCircle (sqlFct,   par.user, par.circle);
      SSServCaller.entityUserCanEdit                        (par.user, par.entities);
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSEntityMiscFct.addEntities(
        sqlFct, 
        par.user, 
        par.entities);
      
      SSServCaller.entityEntitiesToCircleAdd(
        par.user, 
        par.circle, 
        par.entities, 
        false);
      
      SSEntityMiscFct.addEntitiesToCircleByEntityHandlers(
        par.user, 
        par.entities,
        par.circle);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityUserEntitiesToCircleAdd(parA);
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
  public void entityEntityToPrivCircleAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityEntityToPrivCircleAddPar par = new SSEntityEntityToPrivCircleAddPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addEntityIfNotExists(
        par.entity,
        par.type,
        par.label,
        par.description,
        par.user,
        par.creationTime);
      
      SSServCaller.entityEntitiesToCircleAdd(
        par.user,
        sqlFct.addOrGetPrivCircleURI    (par.user),
        SSUri.asListWithoutNullAndEmpty (par.entity), 
        false);
      
      dbSQL.commit(par.shouldCommit);
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        entityEntityToPrivCircleAdd(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void entityUsersToCircleAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    final SSUri result = entityUserUsersToCircleAdd(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserUsersToCircleAddRet.get(result, parA.op));
    
    SSEntityActivityFct.addUsersToCircle(new SSEntityUserUsersToCircleAddPar(parA));
  }
  
  @Override
  public SSUri entityUserUsersToCircleAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUserUsersToCircleAddPar par = new SSEntityUserUsersToCircleAddPar(parA);
      
      SSEntityMiscFct.checkWhetherUserIsAllowedToEditCircle (sqlFct, par.user, par.circle);
        
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityUsersToCircleAdd(
        par.user, 
        par.circle, 
        par.users, 
        false);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityUserUsersToCircleAdd(parA);
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
  public void entityUserCirclesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSEntityUserCirclesGetRet.get(entityUserCirclesGet(parA), parA.op));
  }
  
  @Override
  public void entityCircleGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSEntityUserCircleGetRet.get(entityUserCircleGet(parA), parA.op));
  }    
  
  @Override
  public SSEntityCircle entityUserCircleGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUserCircleGetPar par = new SSEntityUserCircleGetPar(parA);
      final SSEntityCircle           otherUserCircle;
      
      if(
        !par.withSystemCircles &&
        sqlFct.isSystemCircle(par.circle)){
        throw new Exception("user cannot access circle");
      }
      
      if(
        par.forUser == null ||
        SSStrU.equals(par.user, par.forUser)){
        
        SSServCaller.entityUserCanRead(par.user, par.circle);
        
        return sqlFct.getCircle(par.circle, true, true, true);
      }else{
        
        otherUserCircle = sqlFct.getCircle(par.circle, false, false, true);
        
        switch(otherUserCircle.type){
          case pub:
          case group:
            otherUserCircle.entities = sqlFct.getCircleEntityURIs(otherUserCircle.id);
            otherUserCircle.users    = sqlFct.getCircleEntityURIs(otherUserCircle.id);
            
            return otherUserCircle;
          default:
            throw new SSEntityUserAccessedOtherUsersPrivateGroupErr();
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntityCircle> entityUserCirclesGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUserCirclesGetPar       par        = new SSEntityUserCirclesGetPar(parA);
      final List<SSEntityCircle>            circles    = new ArrayList<>();
      final List<SSUri>                     circleUris = new ArrayList<>();
      
      if(par.forUser == null){
        circleUris.addAll(sqlFct.getCircleURIsForUser(par.user,    par.withSystemCircles));
      }else{
        circleUris.addAll(sqlFct.getCircleURIsForUser(par.forUser, par.withSystemCircles));
      }
      
      for(SSUri circleUri : circleUris){
        
        try{
          circles.add(
            SSServCaller.entityUserCircleGet(
              par.user, 
              par.forUser,
              circleUri,
              par.withSystemCircles));
        }catch(Exception error){
          
          if(SSServErrReg.containsErr(SSEntityUserAccessedOtherUsersPrivateGroupErr.class)){
            SSServErrReg.reset();
          }
        }
      }
      
      return circles;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityPublicSet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSEntityUserPublicSetRet.get(entityUserPublicSet(parA), parA.op));
    
     SSEntityActivityFct.setEntityPublic(new SSEntityUserPublicSetPar(parA));
  }
  
   @Override 
  public SSUri entityUserPublicSet(final SSServPar parA) throws Exception{
    
    try{

      final SSEntityUserPublicSetPar par = new SSEntityUserPublicSetPar(parA);
      
      SSServCaller.entityUserCanEdit(par.user, par.entity);
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.addEntityToCircleIfNotExists(
        SSEntityMiscFct.getPubCircleURI(sqlFct), 
        par.entity);
      
      SSEntityMiscFct.setPublicByEntityHandlers(
        par.user,        
        par.entity, 
        SSEntityMiscFct.getPubCircleURI(sqlFct));

      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityUserPublicSet(parA);
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
  public void entityEntityUsersGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserEntityUsersGetRet.get(entityUserEntityUsersGet(parA), parA.op));
  }
  
  @Override
  public List<SSEntity> entityUserEntityUsersGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserEntityUsersGetPar par             = new SSEntityUserEntityUsersGetPar(parA);
      final List<SSUri>                   userUris        = new ArrayList<>();
      final List<SSUri>                   userCircleUris  = sqlFct.getCircleURIsForUser   (par.user, true);
      final List<SSEntity>                users           = new ArrayList<>();
      
      for(SSUri circleUri : sqlFct.getCircleURIsForEntity(par.entity)){
        
        switch(sqlFct.getTypeForCircle(circleUri)){
          
          case priv:{
            
            if(!SSStrU.contains(userCircleUris, circleUri)){
              continue;
            }
            
            if(!SSStrU.contains(userUris, par.user)){
              userUris.add(par.user);
            }
            
            break;
          }
          
          case pub:{
            
            for(SSUri userUri : sqlFct.getUserURIsForCircle(circleUri)){
              
              if(!SSStrU.contains(userUris, userUri)){
                userUris.add(userUri);
              }
            }
            
            break;
          }
          
          case group:{
            
            if(!SSStrU.contains(userCircleUris, circleUri)){
              continue;
            }
            
            for(SSUri userUri : sqlFct.getUserURIsForCircle(circleUri)){
              
              if(!SSStrU.contains(userUris, userUri)){
                userUris.add(userUri);
              }
            }
            
            break;
          }
        }
      }
      
      for(SSUri userUri : userUris){
        users.add(sqlFct.getEntity(userUri));
      }
      
      return users;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> entitiesForLabelsAndDescriptionsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntitiesForLabelsAndDescriptionsGetPar  par = new SSEntitiesForLabelsAndDescriptionsGetPar(parA);
      
      return sqlFct.getEntitiesForLabelsAndDescriptions(SSStrU.distinctWithoutEmptyAndNull(par.keywords));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> entitiesForLabelsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntitiesForLabelsGetPar  par      = new SSEntitiesForLabelsGetPar(parA);

      return sqlFct.getEntitiesForLabels(SSStrU.distinctWithoutEmptyAndNull(par.keywords));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> entitiesForDescriptionsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntitiesForDescriptionsGetPar  par      = new SSEntitiesForDescriptionsGetPar(parA);
      
      return sqlFct.getEntitiesForDescriptions(SSStrU.distinctWithoutEmptyAndNull(par.keywords));
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
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityAdd(parA);
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
  public SSUri entityRemove(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityRemovePar par = new SSEntityRemovePar(parA);
      
      sqlFct.deleteEntityIfExists(par.entity);
      
      return par.entity;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityRemove(parA);
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
  public SSUri entityCircleURIPrivGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityCircleURIPrivGetPar par = new SSEntityCircleURIPrivGetPar(parA);
      final SSUri                       privCircleUri;
      
      dbSQL.startTrans(parA.shouldCommit);
      
      privCircleUri = sqlFct.addOrGetPrivCircleURI(par.user);
      
      dbSQL.commit(parA.shouldCommit);
      
      return privCircleUri;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityCircleURIPrivGet(parA);
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
  public SSUri entityCircleURIPubGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSUri circleUri;
      
      dbSQL.startTrans(parA.shouldCommit);
      
      circleUri = SSEntityMiscFct.getPubCircleURI(sqlFct);

      dbSQL.commit(parA.shouldCommit);
      
      return circleUri;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityCircleCreate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityCircleCreatePar par = new SSEntityCircleCreatePar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri circleUri = 
        SSEntityMiscFct.createCircle(
          sqlFct, 
          par.user, 
          SSCircleE.group,
          par.label, 
          par.description,
          par.isSystemCircle);
      
      SSServCaller.entityEntitiesToCircleAdd(
        par.user, 
        circleUri,    
        par.entities, 
        false);
      
      SSServCaller.entityUsersToCircleAdd(
        par.user, 
        circleUri,   
        par.users,  
        false);
      
      sqlFct.addUserToCircleIfNotExists(
        circleUri, 
        par.user);
      
      dbSQL.commit(par.shouldCommit);
      
      return circleUri;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityCircleCreate(parA);
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
  public SSUri entityUsersToCircleAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUsersToCircleAddPar par = new SSEntityUsersToCircleAddPar(parA);
      
      SSEntityMiscFct.checkWhetherUsersAreUsers(par.users);

      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri userUri : par.users){
        sqlFct.addUserToCircleIfNotExists(par.circle, userUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityUsersToCircleAdd(parA);
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
  public SSCircleE entityMostOpenCircleTypeGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityMostOpenCircleTypeGetPar par                = new SSEntityMostOpenCircleTypeGetPar(parA);
      SSCircleE                              mostOpenCircleType = SSCircleE.priv;
      
      for(SSCircleE circleType : sqlFct.getCircleTypesForEntity(par.entity)){
        
        switch(circleType){
          
          case pub:  return SSCircleE.pub;
          case priv: continue;
          default:   mostOpenCircleType = SSCircleE.group;
        }
      }
      
      return mostOpenCircleType;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSCircleE> entityUserEntityCircleTypesGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserEntityCircleTypesGetPar par         = new SSEntityUserEntityCircleTypesGetPar(parA);
      
      return sqlFct.getCircleTypesCommonForUserAndEntity(par.user, par.entity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override 
  public List<SSEntityCircle> entityUserEntityCirclesGet(final SSServPar parA) throws Exception{
    
    try{

      final SSEntityUserEntityCirclesGetPar par = new SSEntityUserEntityCirclesGetPar(parA);
      
      return sqlFct.getCirclesCommonForUserAndEntity(
        par.user, 
        par.entity, 
        par.withSystemCircles);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityEntitiesToCircleAdd(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityEntitiesToCircleAddPar par = new SSEntityEntitiesToCircleAddPar(parA);
            
      dbSQL.startTrans(par.shouldCommit);
      
      SSEntityMiscFct.addEntities     (sqlFct, par.user, par.entities);
      
      for(SSUri entityUri : par.entities){
        sqlFct.addEntityToCircleIfNotExists(par.circle, entityUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityEntitiesToCircleAdd(parA);
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
  public List<SSUri> entityUserParentEntitiesGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserParentEntitiesGetPar par = new SSEntityUserParentEntitiesGetPar(parA);

      SSServCaller.entityUserCanRead(par.user, par.entity);
      
      return SSEntityMiscFct.getParentEntitiesByEntityHandlers(
        par.user, 
        par.entity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> entityUserSubEntitiesGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserSubEntitiesGetPar par = new SSEntityUserSubEntitiesGetPar(parA);
      
      SSServCaller.entityUserCanRead(par.user, par.entity);
      
      return SSEntityMiscFct.getSubEntitiesByEntityHandlers(
        par.user, 
        par.entity);
      
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
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityThumbAdd(parA);
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
  public List<SSUri> entityThumbsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityThumbsGetPar par = new SSEntityThumbsGetPar(parA);
      
      return sqlFct.getThumbs(par.entity);
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
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
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityUserEntitiesAttach(parA);
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
  public SSUri entityFileAdd(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityFileAddPar par = new SSEntityFileAddPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addFile(par.entity, par.file);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.file;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityThumbAdd(parA);
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
  public List<SSUri> entityFilesGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityFilesGetPar par = new SSEntityFilesGetPar(parA);
      
      return sqlFct.getFiles(par.entity);
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSTextComment> entityUserCommentsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserCommentsGetPar par = new SSEntityUserCommentsGetPar(parA);
      
      if(par.entity != null){
        SSServCaller.entityUserCanRead(par.user, par.entity);
      }
      
      if(
        par.forUser != null && 
        !SSStrU.equals(par.user, par.forUser)){
        throw new Exception("user cannot access comments for user " + par.forUser);
      }
      
      return sqlFct.getComments(par.entity, par.forUser);
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSTextComment> entityCommentsGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityCommentsGetPar par = new SSEntityCommentsGetPar(parA);
      
      return sqlFct.getComments(par.entity, par.forUser);
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> entityEntitiesCommentedGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityEntitiesCommentedGetPar par = new SSEntityEntitiesCommentedGetPar(parA);
      
      return sqlFct.getEntityURIsCommented(par.forUser);
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
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