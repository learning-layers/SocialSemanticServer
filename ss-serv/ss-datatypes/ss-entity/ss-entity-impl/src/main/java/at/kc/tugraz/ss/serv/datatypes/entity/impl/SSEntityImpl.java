/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityClientI;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircleTypeE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityRightTypeE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityAddAtCreationTimePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityAuthorGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityCreationTimeGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityLabelGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityLabelSetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityTypeGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityDescGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityLabelGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityLabelSetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityTypeGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserDirectlyAdjoinedEntitiesRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.sql.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircle;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityCircleUserAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserAllowedIsPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCircleCreatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCircleTypesForEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCirclesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserEntitiesToCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserEntityCirclesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserPublicSetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserSharePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserUsersToCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserCircleCreateRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserEntitiesToCircleAddRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserPublicSetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserShareRet;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.op.SSEntityUserCircleCreateFct;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.op.SSEntityUserPublicSetFct;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.op.SSEntityUserShareFct;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.user.api.SSUserGlobals;
import java.util.ArrayList;
import java.util.List;

public class SSEntityImpl extends SSServImplWithDBA implements SSEntityClientI, SSEntityServerI{
  
  private static SSUri publicCircleUri = null;
  
//  private final SSEntityGraphFct       graphFct;
  private final SSEntitySQLFct         sqlFct;
  
  public SSEntityImpl(final SSServConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
//    graphFct  = new SSEntityGraphFct (this);
    sqlFct    = new SSEntitySQLFct   (this);
  }
  
  /* SSEntityClientI */
  @Override
  public void entityTypeGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSEntityTypeGetRet.get(entityTypeGet(par), par.op));
  }
  
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
  public void entityLabelGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSEntityLabelGetRet.get(entityLabelGet(par), par.op));
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
  public void entityUserPublicSet(final SSSocketCon sSCon, final SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSEntityUserPublicSetRet.get(entityUserPublicSet(par), par.op));
  }
  
  /* SSEntityServerI */
  
  @Override
  public SSEntityDescA entityDescGet(final SSServPar parA) throws Exception{
    
    final SSEntityDescGetPar  par           = new SSEntityDescGetPar(parA);
    List<SSTag>               tags          = null;
    List<SSUri>               discUris      = null;
    SSRatingOverall           overallRating = null;
    SSEntityDescA             result        = null;
    final SSEntity            entity;
    
    try{
      
      entity = sqlFct.getEntity(par.entityUri);
      
      if(par.getOverallRating){
        overallRating     = SSServCaller.ratingOverallGet   (par.user, par.entityUri);
      }
      
      if(par.getDiscUris){
        discUris          = SSServCaller.discUrisUserForTargetGet  (par.user, par.entityUri);
      }
      
      if(par.getTags){
        tags = SSServCaller.tagsUserGet(par.user,     par.entityUri, null, null);
      }
      
      if(SSEntityEnum.equals(entity.type, SSEntityEnum.entity)){
        
        result = SSEntityDesc.get(
          par.entityUri,
          entity.label,
          entity.creationTime,
          tags, 
          overallRating,
          discUris,
          entity.author);
        
      }else{
        
        for(SSServA serv : SSServA.getServsManagingEntities()){
        
          result = ((SSEntityHandlerImplI) serv.serv()).getDescForEntity(
            entity.type,
            par.user, 
            par.entityUri, 
            entity.label, 
            entity.creationTime, 
            tags,
            overallRating, 
            discUris, 
            entity.author);
        
          if(!SSEntityEnum.equals(result.entityDescType, SSEntityEnum.entityDesc)){
            break;
          }
        }
      }
      
      return result;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityUserDirectlyAdjoinedEntitiesRemove(final SSServPar parA) throws Exception{
    
    final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par = new SSEntityUserDirectlyAdjoinedEntitiesRemovePar(parA);
    
    try{
      final SSEntity entity = sqlFct.getEntity(par.entityUri);
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSServA serv : SSServA.getServsManagingEntities()){
        
        ((SSEntityHandlerImplI) serv.serv()).removeDirectlyAdjoinedEntitiesForUser(
          entity.type,
          par,
          false);
      }
      
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
      
      sqlFct.entityLabelSet(par.entityUri, par.label);
      
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
      
      sqlFct.entityDeleteIgnore(par.entityUri);
      
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
  public SSLabelStr entityLabelGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityLabelGetPar par = new SSEntityLabelGetPar(parA);
      
      return sqlFct.entityLabelGet(par.entityUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityAuthorGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityAuthorGetPar par = new SSEntityAuthorGetPar(parA);

      return sqlFct.getEntityAuthor(par.entityUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Long entityCreationTimeGet(final SSServPar parA) throws Exception {
    
    try{
      
      final SSEntityCreationTimeGetPar par    = new SSEntityCreationTimeGetPar(parA);

      return sqlFct.getEntityCreationTime(par.entityUri);

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSEntityEnum entityTypeGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityTypeGetPar par = new SSEntityTypeGetPar (parA);
      
      return sqlFct.getEntityType(par.entityUri);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri entityCirclePublicAdd(final SSServPar parA) throws Exception{
    
    try{
      
      dbSQL.startTrans(parA.shouldCommit);
      
      publicCircleUri = sqlFct.addPublicCircle();
      
      SSServCaller.entityAdd(
        SSUri.get(SSUserGlobals.systemUserURI), 
        publicCircleUri, 
        SSLabelStr.get(publicCircleUri.toString()),
        SSEntityEnum.circle, 
        false);
      
      sqlFct.addCircleRight(publicCircleUri, SSEntityRightTypeE.read);
      sqlFct.addCircleRight(publicCircleUri, SSEntityRightTypeE.addMetadata);
      sqlFct.addCircleRight(publicCircleUri, SSEntityRightTypeE.addEntityToCircle);
      
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
  public List<SSCircle> entityUserCirclesGet(final SSServPar parA) throws Exception{
    
    final SSEntityUserCirclesGetPar par = new SSEntityUserCirclesGetPar(parA);
    
    try{
      final List<SSUri>    circleUris = sqlFct.getUserCircleURIs(par.user);
      final List<SSCircle> circles    = new ArrayList<SSCircle>();
      SSCircle             circle;
      
      for(SSUri circleUri : circleUris){
        
        circle              = sqlFct.getCircle            (circleUri);
        circle.circleRights = sqlFct.getCircleRights      (circleUri);
        circle.userUris     = sqlFct.getCircleUserUris    (circleUri);
        circle.entityUris   = sqlFct.getCircleEntityUris  (circleUri);
          
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
    
    final SSEntityUserCircleCreatePar par = new SSEntityUserCircleCreatePar(parA);
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSEntityUserCircleCreateFct.updateEntities(par);
      
      final SSUri circleUri = SSEntityUserCircleCreateFct.createCircleWithRights(sqlFct, par);
      
      SSEntityUserCircleCreateFct.addEntitiesToCircle(sqlFct, par, circleUri);
      SSEntityUserCircleCreateFct.addUsersToCircle   (sqlFct, par, circleUri);
      
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
  public SSUri entityUserUsersToCircleAdd(final SSServPar parA) throws Exception{
    
    final SSEntityUserUsersToCircleAddPar par = new SSEntityUserUsersToCircleAddPar(parA);
    
    try{
      
      if(!sqlFct.hasCircleRight(par.circleUri, SSEntityRightTypeE.addUserToCircle)){
        throw new Exception("circle has not enough rights to add user to it");
      }

      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri userUri : par.userUris){
        sqlFct.addUserToCircle(par.circleUri, userUri);
      }
      
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
  public void entityCircleUserAdd(final SSServPar parA) throws Exception{
    
    final SSEntityCircleUserAddPar par = new SSEntityCircleUserAddPar(parA);
  
    try{
      sqlFct.addUserToCircle(par.circleUri, par.user);
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        entityCircleUserAdd(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSEntityCircleTypeE entityMostOpenCircleTypeGet(final SSServPar parA) throws Exception{
    
    final SSEntityMostOpenCircleTypeGetPar par                = new SSEntityMostOpenCircleTypeGetPar(parA);
    SSEntityCircleTypeE                    mostOpenCircleType = null;
    
    try{
      
      final List<SSUri> entityCircleUris = sqlFct.getEntityCircleURIs(par.entityUri);
      
      for(SSUri circleUri : entityCircleUris){
        
        switch(sqlFct.getCircleType(circleUri)){
          
          case pub:{
            return SSEntityCircleTypeE.pub;
          }
          
          case priv:{
            
            if(
              mostOpenCircleType != null || 
              mostOpenCircleType != SSEntityCircleTypeE.group){ 
              
              mostOpenCircleType = SSEntityCircleTypeE.priv;
            }
            
            break;
          }   
          
          default:{
            mostOpenCircleType = SSEntityCircleTypeE.group;
            break;
          }
        }
      }
      
      return mostOpenCircleType;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntityCircleTypeE> entityUserCircleTypesForEntityGet(final SSServPar parA) throws Exception{
    
    final SSEntityUserCircleTypesForEntityGetPar par         = new SSEntityUserCircleTypesForEntityGetPar(parA);
    final List<SSEntityCircleTypeE>              circleTypes = new ArrayList<SSEntityCircleTypeE>();
    SSEntityCircleTypeE                          circleType;
    
    try{
      final List<SSUri> entityCircleUris = sqlFct.getEntityCircleURIs(par.entityUri);
      final List<SSUri> userCircleUris   = sqlFct.getUserCircleURIs  (par.user);
      
      for(SSUri circleUri : entityCircleUris){
        
        if(!SSUri.contains(userCircleUris, circleUri)){
          continue;
        }
        
        circleType = sqlFct.getCircleType(circleUri);
        
        if(!SSEntityCircleTypeE.contains(circleTypes, circleType)){
          circleTypes.add(circleType);
        }
      }
      
      return circleTypes;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override 
  public List<SSCircle> entityUserEntityCirclesGet(final SSServPar parA) throws Exception{
    
    final SSEntityUserEntityCirclesGetPar par               = new SSEntityUserEntityCirclesGetPar(parA);
    final List<SSCircle>                        userEntityCircles = new ArrayList<SSCircle>();
    
    try{
      final List<SSUri> userCircleURIs   = sqlFct.getUserCircleURIs   (par.user);
      final List<SSUri> entityCircleURIs = sqlFct.getEntityCircleURIs (par.entityUri);
      
      for(SSUri entityCircleUri : entityCircleURIs){
        
        if(!SSUri.contains(userCircleURIs, entityCircleUri)){
          continue;
        }
        
        userEntityCircles.add(sqlFct.getCircle(entityCircleUri));
      }

      return userEntityCircles;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean entityUserEntitiesToCircleAdd(final SSServPar parA) throws Exception{
    
    final SSEntityUserEntitiesToCircleAddPar par = new SSEntityUserEntitiesToCircleAddPar(parA);
    
    try{
      final List<SSUri> userCircleURIs = sqlFct.getUserCircleURIs(par.user);
      
      if(!SSUri.contains(userCircleURIs, par.circleUri)){
        throw new Exception("user doesnt have access to circle");
      }
      
      if(!sqlFct.hasCircleRight(par.circleUri, SSEntityRightTypeE.addEntityToCircle)){
        throw new Exception("circle has not enough rights to add entity to it");
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri entityUri : par.entityUris){
        
        SSServCaller.entityAdd(
          par.user,
          entityUri,
          SSLabelStr.get(SSUri.toStr(entityUri)),
          SSEntityEnum.entity,
          false);
      }
      
      for(SSUri entityUri : par.entityUris){
        sqlFct.addEntityToCircle(par.circleUri, entityUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
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
      
      if(SSEntityEnum.equals(SSServCaller.entityTypeGet(par.entityUri), SSEntityEnum.entity)){
        return true;
      }
      
      final List<SSUri> entityCircleUris = sqlFct.getEntityCircleURIs(par.entityUri);
      
      if(entityCircleUris.isEmpty()){
        return true;
      }
      
      for(SSUri userCircleUri : sqlFct.getUserCircleURIs(par.user)){
        
        if(SSUri.contains(entityCircleUris, userCircleUri)){
          
          if(sqlFct.hasCircleRight(userCircleUri, par.accessRight)){
            return true;
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
    
    final SSEntityUserPublicSetPar par = new SSEntityUserPublicSetPar(parA);
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);

      SSEntityUserPublicSetFct.addEntityToPublicCircle   (sqlFct, par, publicCircleUri);
      SSEntityUserPublicSetFct.setPublicByEntityHandlers (par);

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
    
    final SSEntityUserSharePar par = new SSEntityUserSharePar(parA);
    
    try{
      
      for(SSUri userUri: par.userUris){
        
        if(!SSServCaller.userExists(userUri)){
          throw new Exception("cannot share with unknown user");
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(par.entityCircleUri == null){
        par.entityCircleUri = SSEntityUserShareFct.createNewCircleAndShare(par);
      }else{
        SSEntityUserShareFct.useCircleAndShare(par);
      }
      
      SSEntityUserShareFct.shareByEntityHandlers(par);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entityCircleUri;
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