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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityClientI;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleRightE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityAddAtCreationTimePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityLabelSetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityDescGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityLabelSetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserDirectlyAdjoinedEntitiesRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.sql.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserAllowedIsPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityCircleCreatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityEntitiesToCircleAddPar;
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
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.user.api.SSUserGlobals;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitySearchWithKeywordWithinPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserSharePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserShareRet;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import java.util.ArrayList;
import java.util.List;

public class SSEntityImpl extends SSServImplWithDBA implements SSEntityClientI, SSEntityServerI{
  
  private static SSUri publicCircleUri = null;
  
//  private final SSEntityGraphFct       graphFct;
  private final SSEntitySQLFct         sqlFct;
  
  public SSEntityImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
//    graphFct  = new SSEntityGraphFct (this);
    sqlFct    = new SSEntitySQLFct   (this);
  }
  
  /* SSEntityClientI */
  @Override
  public void entityUserShare(final SSSocketCon sSCon, final SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSEntityUserShareRet.get(entityUserShare(par), par.op));
  }
  
  @Override
  public void entityDescGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSEntityDescGetRet.get(entityDescGet(par), par.op));
  }
  
  @Override
  public void entityUserGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserGetRet.get(entityUserGet(parA), parA.op));
  }
  
  @Override
  public void entityLabelSet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSEntityLabelSetRet.get(entityLabelSet(par), par.op));
  }
  
  @Override
  public void entityUserDirectlyAdjoinedEntitiesRemove(final SSSocketCon sSCon, final SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSEntityUserDirectlyAdjoinedEntitiesRemoveRet.get(entityUserDirectlyAdjoinedEntitiesRemove(par), par.op));
  }
  
  @Override
  public void entityUserCircleCreate(final SSSocketCon sSCon, final SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSEntityUserCircleCreateRet.get(entityUserCircleCreate(par), par.op));
  }
  
  @Override
  public void entityUserEntitiesToCircleAdd(final SSSocketCon sSCon, final SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSEntityUserEntitiesToCircleAddRet.get(entityUserEntitiesToCircleAdd(par), par.op));
  }
  
  @Override
  public void entityUserUsersToCircleAdd(final SSSocketCon sSCon, final SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSEntityUserUsersToCircleAddRet.get(entityUserUsersToCircleAdd(par), par.op));
  }
  
    @Override
  public void entityUserCirclesGet(final SSSocketCon sSCon, final SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSEntityUserCirclesGetRet.get(entityUserCirclesGet(par), par.op));
  }
  
  @Override
  public void entityUserPublicSet(final SSSocketCon sSCon, final SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSEntityUserPublicSetRet.get(entityUserPublicSet(par), par.op));
  }
  
  @Override
  public void entityUserEntityUsersGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserEntityUsersGetRet.get(entityUserEntityUsersGet(parA)));
  }
  
  /* SSEntityServerI */
  
  @Override
  public SSEntityDescA entityDescGet(final SSServPar parA) throws Exception{
    
    final SSEntityDescGetPar  par           = new SSEntityDescGetPar(parA);
    List<String>              tags          = new ArrayList<String>();
    List<SSUri>               discUris      = null;
    SSRatingOverall           overallRating = null;
    final SSEntity            entity;
    
    try{
      
      entity = sqlFct.getEntity(par.entityUri);
      
      if(par.getOverallRating){
        overallRating = SSServCaller.ratingOverallGet   (par.user, par.entityUri);
      }
      
      if(par.getDiscUris){
        discUris = SSServCaller.discUrisUserForTargetGet  (par.user, par.entityUri);
      }
      
      if(par.getTags){
        tags = SSEntityA.toStr(SSServCaller.tagsUserGet(par.user, par.entityUri, null, null));
      }
      
      return SSEntityMiscFct.getDescForEntityByEntityHandlers(
        par.user,
        entity,
        tags,
        overallRating,
        discUris);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSEntity entityUserGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserGetPar par = new SSEntityUserGetPar(parA);
      
      SSEntityMiscFct.checkWhetherUserCanReadEntity(par.user, par.entityUri);
      
      return sqlFct.getEntity(par.entityUri);
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSEntity entityGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityGetPar par = new SSEntityGetPar(parA);
      
      return sqlFct.getEntity(par.entityUri);
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityUserDirectlyAdjoinedEntitiesRemove(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par    = new SSEntityUserDirectlyAdjoinedEntitiesRemovePar(parA);
      final SSEntity                                      entity = sqlFct.getEntity(par.entityUri);
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSEntityMiscFct.removeUserEntityDirectlyAdjoinedEntitiesByEntityHandlers(entity, par);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entityUri;
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
  public SSUri entityLabelSet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityLabelSetPar par = new SSEntityLabelSetPar(parA);
      
      sqlFct.updateEntityLabelIfExists(par.entityUri, par.label);
      
      return par.entityUri;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityLabelSet(parA);
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
  public SSUri entityAdd(final SSServPar parA) throws Exception {
    
    final SSEntityAddPar par = new SSEntityAddPar(parA);
    
    try{

      sqlFct.addEntityIfNotExists(
        par.entityUri, 
        par.label, 
        par.entityType, 
        par.user);
      
      return par.entityUri;
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
  public SSUri entityAddAtCreationTime(final SSServPar parA) throws Exception {
    
    try{
      
      final SSEntityAddAtCreationTimePar par = new SSEntityAddAtCreationTimePar(parA);

      sqlFct.addEntityAtCreationTimeIfNotExists(
        par.entityUri, 
        par.label, 
        par.creationTime, 
        par.entityType, 
        par.user);
      
      return par.entityUri;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityAddAtCreationTime(parA);
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
      
      sqlFct.deleteEntityIfExists(par.entityUri);
      
      return par.entityUri;
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
  public SSUri entityCirclePublicAdd(final SSServPar parA) throws Exception{
    
    try{
      
      dbSQL.startTrans(parA.shouldCommit);
      
      publicCircleUri = sqlFct.addOrGetPublicCircle();
      
      dbSQL.commit(parA.shouldCommit);
      
      return publicCircleUri;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return entityCirclePublicAdd(parA);
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
  public SSUri entityCircleURIPublicGet(final SSServPar parA) throws Exception{
    
    try{
      
      if(publicCircleUri == null){
        throw new Exception("public circle not initialized");
      }
      
      return publicCircleUri;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntityCircle> entityUserCirclesGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUserCirclesGetPar par     = new SSEntityUserCirclesGetPar(parA);
      final List<SSEntityCircle>            circles = new ArrayList<SSEntityCircle>();
      SSEntityCircle                        circle;
      
      for(SSUri circleUri : sqlFct.getCircleURIsForUser(par.user)){

        if(
          !par.withSystemGeneratedCircles &&
          sqlFct.isSystemCircle(circleUri)){
          continue;
        }
        
        circle              = sqlFct.getCircle                (circleUri);
        circle.circleRights = SSEntityMiscFct.getCircleRights (circle.circleType);
        circle.userUris     = sqlFct.getCircleUserURIs        (circleUri);
        circle.entityUris   = sqlFct.getCircleEntityURIs      (circleUri);
          
        circles.add(circle);
      }
      
      return circles;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityUserCircleCreate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUserCircleCreatePar par = new SSEntityUserCircleCreatePar(parA);
      
      SSEntityMiscFct.checkWhetherCircleIsGroupCircle (par.circleType);
      SSEntityMiscFct.checkWhetherUserCanEditEntities (par.user, par.entityUris);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri circleUri =
        SSServCaller.entityCircleCreate(
          par.user,
          par.entityUris,
          par.userUris,
          par.circleType,
          par.label,
          par.user,
          false);
      
      SSEntityMiscFct.addEntitiesToCircleByEntityHandlers(
        par.user,
        par.entityUris,
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
  public SSUri entityCircleCreate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityCircleCreatePar par = new SSEntityCircleCreatePar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri circleUri = 
        SSEntityMiscFct.createCircle(
          sqlFct, 
          par.circleAuthor, 
          par.circleType, 
          par.label);
      
      SSServCaller.entityEntitiesToCircleAdd(
        par.user, 
        circleUri,    
        par.entityUris, 
        false);
      
      sqlFct.addUserToCircleIfNotExists(
        circleUri, 
        par.user);
      
      SSServCaller.entityUsersToCircleAdd(
        par.user, 
        circleUri,   
        par.userUris,  
        false);
      
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
      
      SSEntityMiscFct.checkWhetherEntitiesExist(sqlFct, par.userUris, SSEntityE.user);
        
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri userUri : par.userUris){
        sqlFct.addUserToCircleIfNotExists(par.circleUri, userUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circleUri;
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
  public SSUri entityUserUsersToCircleAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUserUsersToCircleAddPar par = new SSEntityUserUsersToCircleAddPar(parA);
      
      SSEntityMiscFct.checkWhetherUserIsAllowedToEditCircle (sqlFct, par.user, par.circleUri);
        
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityUsersToCircleAdd(
        par.user, 
        par.circleUri, 
        par.userUris, 
        false);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circleUri;
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
  public SSCircleE entityMostOpenCircleTypeGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityMostOpenCircleTypeGetPar par                = new SSEntityMostOpenCircleTypeGetPar(parA);
      SSCircleE                    mostOpenCircleType = SSCircleE.priv;
      
      
      for(SSCircleE circleType : sqlFct.getCircleTypesForEntity(par.entityUri)){
        
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
      
      return sqlFct.getCircleTypesCommonForUserAndEntity(par.user, par.entityUri);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override 
  public List<SSEntityCircle> entityUserEntityCirclesGet(final SSServPar parA) throws Exception{
    
    try{

      final SSEntityUserEntityCirclesGetPar par = new SSEntityUserEntityCirclesGetPar(parA);
      
      return sqlFct.getCirclesCommonForUserAndEntity(par.user, par.entityUri);
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
      
      SSEntityMiscFct.updateEntities     (par.user, par.entityUris);
      
      for(SSUri entityUri : par.entityUris){
        sqlFct.addEntityToCircleIfNotExists(par.circleUri, entityUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circleUri;
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
  public SSUri entityUserEntitiesToCircleAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUserEntitiesToCircleAddPar par = new SSEntityUserEntitiesToCircleAddPar(parA);
      
      SSEntityMiscFct.checkWhetherUserIsAllowedToEditCircle (sqlFct,   par.user, par.circleUri);
      SSEntityMiscFct.checkWhetherUserCanEditEntities       (par.user, par.entityUris);
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntitiesToCircleAdd(
        par.user, 
        par.circleUri, 
        par.entityUris, 
        false);
      
      SSEntityMiscFct.addEntitiesToCircleByEntityHandlers(
        par.user, 
        par.entityUris,
        par.circleUri);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circleUri;
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
  public Boolean entityUserAllowedIs(final SSServPar parA) throws Exception{
   
    final SSEntityUserAllowedIsPar par = new SSEntityUserAllowedIsPar(parA);
    
    try{
      
      switch(SSServCaller.entityGet(par.user, par.entityUri).type){
        case entity: return true;
      }
      
      for(SSCircleE circleType : sqlFct.getCircleTypesCommonForUserAndEntity(par.user, par.entityUri)){
       
        switch(circleType){
          case priv: return true;
          case pub:{
            
            if(SSCircleRightE.equals(par.accessRight, SSCircleRightE.read)){ 
              return true;
            }
            
            continue;
          }
          default:{
            if(
              SSCircleRightE.equals(par.accessRight, SSCircleRightE.read) ||
              SSCircleRightE.equals(par.accessRight, SSCircleRightE.edit)){ 
              return true;
            }
          }
        }
      }
      
      return false;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override 
  public SSUri entityUserPublicSet(final SSServPar parA) throws Exception{
    
    try{

      final SSEntityUserPublicSetPar par = new SSEntityUserPublicSetPar(parA);
      
      SSServCaller.entityUserCanEdit(par.user, par.entityUri);
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.addEntityToCircleIfNotExists        (publicCircleUri, par.entityUri);
      SSEntityMiscFct.setPublicByEntityHandlers  (par.user,        par.entityUri, publicCircleUri);

      dbSQL.commit(par.shouldCommit);
      
      return par.entityUri;
      
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
  public SSUri entityUserShare(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserSharePar par = new SSEntityUserSharePar(parA);
      
      SSEntityMiscFct.checkWhetherUserCanEditEntity          (par.user, par.entityUri);
      SSEntityMiscFct.checkWhetherUserWantsToShareWithHimself(par.user, par.userUris);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri circleUri =
        SSServCaller.entityCircleCreate(
          par.user,
          par.entityUri,
          par.userUris,
          SSCircleE.group,
          SSLabel.get(SSUri.toStr(par.user) + SSStrU.underline + SSUri.toStr(par.entityUri)),
          SSUserGlobals.systemUser,
          false);
      
      for(SSUri userUriToShareWith : par.userUris){
        
        SSServCaller.collUserShareWithUser(
          par.user,
          userUriToShareWith,
          par.entityUri,
          circleUri,
          false);
      }

      SSEntityMiscFct.shareByEntityHandlers(
        par.user,
        par.userUris,
        par.entityUri,
        circleUri);
      
      SSEntityMiscFct.saveActivity(
        par,
        SSActivityE.share,
        par.userUris,
        par.entityUri,
        new ArrayList<SSUri>(),
        par.comment);
      
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
  public List<SSEntity> entityUserEntityUsersGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserEntityUsersGetPar par             = new SSEntityUserEntityUsersGetPar(parA);
      final List<SSUri>                   userUris        = new ArrayList<SSUri>();
      final List<SSUri>                   userCircleUris  = sqlFct.getCircleURIsForUser   (par.user);
      final List<SSEntity>                users           = new ArrayList<SSEntity>();
      
      for(SSUri circleUri : sqlFct.getCircleURIsForEntity(par.entityUri)){
        
        switch(sqlFct.getCircleType(circleUri)){
          
          case pub:{
            
            for(SSUri userUri : sqlFct.getCircleUserURIs(circleUri)){
              
              if(!SSUri.contains(userUris, userUri)){
                userUris.add(userUri);
              }
            }
          }
          
          case group:{
            
            if(!SSUri.contains(userCircleUris, circleUri)){
              continue;
            }
            
            for(SSUri userUri : sqlFct.getCircleUserURIs(circleUri)){
              
              if(!SSUri.contains(userUris, userUri)){
                userUris.add(userUri);
              }
            }
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
  public List<SSUri> entitySearchWithKeywordWithin(final SSServPar parA) throws Exception{
    
    try{
      final SSEntitySearchWithKeywordWithinPar par = new SSEntitySearchWithKeywordWithinPar(parA);
      
      SSEntityMiscFct.checkWhetherUserCanEditEntity(par.user, par.entityUri);
      
      return SSEntityMiscFct.searchWithKeywordWithinByEntityHandlers(
        par.user, 
        par.entityUri, 
        par.keyword);
      
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