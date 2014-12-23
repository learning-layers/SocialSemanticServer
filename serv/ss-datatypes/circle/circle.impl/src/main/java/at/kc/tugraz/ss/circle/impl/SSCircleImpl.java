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
package at.kc.tugraz.ss.circle.impl;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.circle.api.SSCircleClientI;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.impl.fct.activity.SSCircleActivityFct;
import at.kc.tugraz.ss.circle.impl.fct.misc.SSCircleMiscFct;
import at.kc.tugraz.ss.circle.impl.fct.sql.SSCircleSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.SSCircleE;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityCircleCreatePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityCircleURIPrivGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityEntitiesToCircleAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityEntityCirclesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityEntityToPrivCircleAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityEntityToPubCircleAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityUserCircleCreatePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityUserCircleGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityUserCirclesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityUserEntitiesToCircleAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityUserEntityCircleTypesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityUserEntityCirclesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityUserEntityMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityUserUsersToCircleAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityUsersToCircleAddPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSEntityUserCircleCreateRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSEntityUserCircleGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSEntityUserCirclesGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSEntityUserEntitiesToCircleAddRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSEntityUserUsersToCircleAddRet;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityUserCanPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityUserPublicSetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityUserEntityUsersGetPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSEntityUserEntityUsersGetRet;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntitiesUserGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSEntityUserSharePar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSEntitiesUserGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserPublicSetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSEntityUserShareRet;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.serv.caller.SSServCallerU;
import java.util.ArrayList;
import java.util.List;
import sss.serv.err.datatypes.SSErr;
import sss.serv.err.datatypes.SSErrE;

public class SSCircleImpl extends SSServImplWithDBA implements SSCircleClientI, SSCircleServerI{
  
  private final SSCircleSQLFct sqlFct;

  public SSCircleImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{
    super(conf, null, dbSQL);
    
    this.sqlFct = new SSCircleSQLFct(dbSQL);
  }
  
  @Override
  public void entityCircleCreate(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    final SSUri userFromOIDC = SSServCaller.checkKey(parA);
    
    if(userFromOIDC != null){
      parA.user = userFromOIDC;
    }
    
    final SSUri result = entityUserCircleCreate(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserCircleCreateRet.get(result, parA.op));
    
    SSCircleActivityFct.createCircle(new SSEntityUserCircleCreatePar(parA), result);
  }
  
  @Override
  public SSUri entityUserCircleCreate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUserCircleCreatePar par = new SSEntityUserCircleCreatePar(parA);
      
      SSCircleMiscFct.checkWhetherCircleIsGroupCircle (par.type);
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
      
      SSCircleMiscFct.shareUserEntityWithCircleByEntityHandlers(
        par.user,
        par.entities,
        circleUri);
      
      dbSQL.commit(par.shouldCommit);
      
      return circleUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return entityUserCircleCreate(parA);
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
  public SSUri entityCircleCreate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityCircleCreatePar par        = new SSEntityCircleCreatePar(parA);
      final SSUri                   circleUri  = SSServCaller.vocURICreate();
      final SSCircleE               circleType = SSCircleE.group;
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user, 
        circleUri,
        SSEntityE.circle, 
        par.label, 
        par.description, 
        null, 
        false);
      
      switch(circleType){
        case priv:
        case group:
        case pub: sqlFct.addCircle(circleUri, circleType, par.isSystemCircle); break;
        default: throw new Exception("circle type " + circleType + "currently not supported");
      }
      
      if(!par.entities.isEmpty()){
      
        SSServCaller.entityEntitiesToCircleAdd(
          par.user, 
          circleUri,    
          par.entities, 
          false);
      }
      
      if(!par.users.isEmpty()){
        
        SSServCaller.entityUsersToCircleAdd(
          par.user, 
          circleUri,   
          par.users,  
          false);
      }
      
      sqlFct.addUserToCircleIfNotExists(
        circleUri, 
        par.user);
      
      dbSQL.commit(par.shouldCommit);
      
      return circleUri;
   }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return entityCircleCreate(parA);
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
  public SSUri entityUsersToCircleAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUsersToCircleAddPar par = new SSEntityUsersToCircleAddPar(parA);
      
      SSServCallerU.checkWhetherUsersAreUsers(par.users);

      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri userUri : par.users){
        sqlFct.addUserToCircleIfNotExists(par.circle, userUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return entityUsersToCircleAdd(parA);
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
  public SSCircleE entityUserEntityMostOpenCircleTypeGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntityUserEntityMostOpenCircleTypeGetPar par                = new SSEntityUserEntityMostOpenCircleTypeGetPar(parA);
      SSCircleE                                        mostOpenCircleType = SSCircleE.priv;
      
      for(SSCircleE circleType : sqlFct.getCircleTypesCommonForUserAndEntity(par.user, par.entity)){
        
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
      
      SSServCallerU.addEntities(par.user, par.entities);
      
      for(SSUri entityUri : par.entities){
        sqlFct.addEntityToCircleIfNotExists(par.circle, entityUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return entityEntitiesToCircleAdd(parA);
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
  public SSUri entityCircleURIPrivGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityCircleURIPrivGetPar par = new SSEntityCircleURIPrivGetPar(parA);
      final SSUri                       privCircleUri;
      
      dbSQL.startTrans(parA.shouldCommit);
      
      privCircleUri = SSCircleMiscFct.addOrGetPrivCircleURI(sqlFct, par.user);
      
      dbSQL.commit(parA.shouldCommit);
      
      return privCircleUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return entityCircleURIPrivGet(parA);
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
  public SSUri entityCircleURIPubGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSUri circleUri;
      
      dbSQL.startTrans(parA.shouldCommit);
      
      circleUri = sqlFct.getPubCircleURI();
      
      dbSQL.commit(parA.shouldCommit);
      
      return circleUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return entityCircleURIPubGet(parA);
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
  public List<SSEntityCircle> entityEntityCirclesGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityEntityCirclesGetPar       par        = new SSEntityEntityCirclesGetPar(parA);
      final List<SSEntityCircle>              circles    = new ArrayList<>();
      
      for(SSUri circleUri : sqlFct.getCircleURIsForEntity(par.entity)){
        
        circles.add(
          sqlFct.getCircle(
            circleUri,
            false,
            true,
            false));
      }
      
      return circles;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityUsersToCircleAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    final SSUri userFromOIDC = SSServCaller.checkKey(parA);
    
    if(userFromOIDC != null){
      parA.user = userFromOIDC;
    }

    final SSUri result = entityUserUsersToCircleAdd(parA);
    
    sSCon.writeRetFullToClient(SSEntityUserUsersToCircleAddRet.get(result, parA.op));
    
    SSCircleActivityFct.addUsersToCircle(new SSEntityUserUsersToCircleAddPar(parA));
  }
  
  @Override
  public SSUri entityUserUsersToCircleAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUserUsersToCircleAddPar par = new SSEntityUserUsersToCircleAddPar(parA);
      
      SSCircleMiscFct.checkWhetherUserIsAllowedToEditCircle (sqlFct, par.user, par.circle);
        
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityUsersToCircleAdd(
        par.user, 
        par.circle, 
        par.users, 
        false);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return entityUserUsersToCircleAdd(parA);
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
  public void entityUserCirclesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    final SSUri userFromOIDC = SSServCaller.checkKey(parA);
    
    if(userFromOIDC != null){
      parA.user = userFromOIDC;
    }

    sSCon.writeRetFullToClient(SSEntityUserCirclesGetRet.get(entityUserCirclesGet(parA), parA.op));
  }
  
  @Override
  public void entityCircleGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    final SSUri userFromOIDC = SSServCaller.checkKey(parA);
    
    if(userFromOIDC != null){
      parA.user = userFromOIDC;
    }

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
        
        switch(otherUserCircle.circleType){
          case pub:
          case group:
            otherUserCircle.entities = sqlFct.getCircleEntityURIs(otherUserCircle.id);
            otherUserCircle.users    = sqlFct.getCircleEntityURIs(otherUserCircle.id);
            
            return otherUserCircle;
          default:
            throw new SSErr(SSErrE.userAccessedOtherUsersPrivateGroup);
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
          
          if(SSServErrReg.containsErr(SSErrE.userAccessedOtherUsersPrivateGroup)){
            SSServErrReg.reset();
            continue;
          }
          
          throw error;
        }
      }
      
      return circles;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityEntitiesToCircleAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    final SSUri userFromOIDC = SSServCaller.checkKey(parA);
    
    if(userFromOIDC != null){
      parA.user = userFromOIDC;
    }

    sSCon.writeRetFullToClient(SSEntityUserEntitiesToCircleAddRet.get(entityUserEntitiesToCircleAdd(parA), parA.op));
    
    SSCircleActivityFct.addEntitiesToCircle(new SSEntityUserEntitiesToCircleAddPar(parA));
  }
  
  @Override
  public SSUri entityUserEntitiesToCircleAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityUserEntitiesToCircleAddPar par = new SSEntityUserEntitiesToCircleAddPar(parA);
      
      SSCircleMiscFct.checkWhetherUserIsAllowedToEditCircle (sqlFct,   par.user, par.circle);
      SSServCaller.entityUserCanEdit                        (par.user, par.entities);
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCallerU.addEntities(
        par.user, 
        par.entities);
      
      SSServCaller.entityEntitiesToCircleAdd(
        par.user, 
        par.circle, 
        par.entities, 
        false);
      
      SSCircleMiscFct.shareUserEntityWithCircleByEntityHandlers(
        par.user, 
        par.entities,
        par.circle);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return entityUserEntitiesToCircleAdd(parA);
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
  public void entityEntityToPrivCircleAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityEntityToPrivCircleAddPar par = new SSEntityEntityToPrivCircleAddPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user, 
        par.entity, 
        par.type, 
        par.label, 
        par.description, 
        par.creationTime, 
        false);
      
      SSServCaller.entityEntitiesToCircleAdd(
        par.user,
        SSCircleMiscFct.addOrGetPrivCircleURI (sqlFct, par.user),
        SSUri.asListWithoutNullAndEmpty       (par.entity), 
        false);
      
      dbSQL.commit(par.shouldCommit);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          entityEntityToPrivCircleAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void entityEntityToPubCircleAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSEntityEntityToPubCircleAddPar par = new SSEntityEntityToPubCircleAddPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user, 
        par.entity, 
        par.type, 
        par.label, 
        par.description, 
        par.creationTime, 
        false);

      SSServCaller.entityEntitiesToCircleAdd(
        par.user,
        SSCircleMiscFct.addOrGetPubCircleURI (sqlFct),
        SSUri.asListWithoutNullAndEmpty      (par.entity), 
        false);
      
      dbSQL.commit(par.shouldCommit);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          entityEntityToPrivCircleAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSEntity entityUserCan(final SSServPar parA) throws Exception{
   
    try{

      final SSEntityUserCanPar par    = new SSEntityUserCanPar(parA);
      final SSEntity           entity;
      
      try{
        entity = SSServCaller.entityGet(par.entity);
      }catch(Exception error){
        
        if(SSServErrReg.containsErr(SSErrE.entityDoesntExist)){
          SSServErrReg.reset();
          return null;
        }
        
        throw error;
      }
      
      SSCircleMiscFct.checkWhetherUserCanForEntityType(
        sqlFct,
        par.user,
        entity,
        par.accessRight);
      
      return entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entityPublicSet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSEntityUserPublicSetRet.get(entityUserPublicSet(parA), parA.op));
    
     SSCircleActivityFct.setEntityPublic(new SSEntityUserPublicSetPar(parA));
  }
  
  @Override 
  public SSUri entityUserPublicSet(final SSServPar parA) throws Exception{
    
    try{

      final SSEntityUserPublicSetPar par = new SSEntityUserPublicSetPar(parA);
      
      SSServCaller.entityUserCanEdit(par.user, par.entity);
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.addEntityToCircleIfNotExists(
        SSCircleMiscFct.getPubCircleURI(sqlFct), 
        par.entity);
      
      SSCircleMiscFct.setPublicByEntityHandlers(
        par.user,        
        par.entity);

      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return entityUserPublicSet(parA);
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
      
      return SSServCallerU.getEntities(userUris);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void entitiesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    final SSUri userFromOIDC = SSServCaller.checkKey(parA);
    
    if(userFromOIDC != null){
      parA.user = userFromOIDC;
    }
    
    sSCon.writeRetFullToClient(SSEntitiesUserGetRet.get(entitiesUserGet(parA), parA.op));
  }
  
  @Override
  public List<SSEntity> entitiesUserGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEntitiesUserGetPar par      = new SSEntitiesUserGetPar(parA);
      final List<SSEntity>       entities = new ArrayList<>();

      for(SSEntityCircle circle : SSServCaller.entityUserCirclesGet(par.user, par.forUser, true)){

        for(SSUri entity : circle.entities){
          
          try{
            SSServCaller.entityUserCanRead(par.user, entity);
          }catch(Exception error){
            continue;
          }
          
          entities.add(SSServCaller.entityUserGet(par.user, entity, par.forUser));
        }
      }
      
      return entities;
    }catch(Exception error){
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
      
      if(
        par.users.isEmpty() &&
        par.circles.isEmpty()){
        return par.entity;
      }
      
      SSServCaller.entityUserCanEdit                            (par.user, par.entity);

      if(!par.users.isEmpty()){
        
        SSCircleMiscFct.checkWhetherUserWantsToShareWithHimself (par.user, par.users);
        SSServCallerU.checkWhetherUsersAreUsers                 (par.users);
        
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
        
        SSCircleMiscFct.shareByEntityHandlers(
          par.user,
          par.users,
          par.entity,
          circleUri,
          par.saveActivity);
        
        SSCircleActivityFct.shareEntityWithUsers(par);
      }
      
      if(!par.circles.isEmpty()){

        dbSQL.startTrans(par.shouldCommit);
        
        for(SSUri circle : par.circles){
          
          SSServCaller.entityUserEntitiesToCircleAdd(
            par.user, 
            circle, 
            SSUri.asListWithoutNullAndEmpty(par.entity), 
            false);
          
//          switch(entityType){
//            case qa:
//              SSEntityMiscFct.shareByEntityHandlers(
//                par.user,
//                sqlFct.getUserURIsForCircle(circle),
//                par.entity,
//                circle,
//                false);
//              break;
//            default:break;
//          }
        }
        
        SSCircleActivityFct.shareEntityWithCircles(par);
      }
     
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return entityUserShare(parA);
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
}