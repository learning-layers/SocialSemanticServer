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

import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.kc.tugraz.ss.circle.api.SSCircleClientI;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.impl.fct.activity.SSCircleActivityFct;
import at.kc.tugraz.ss.circle.impl.fct.misc.SSCircleMiscFct;
import at.kc.tugraz.ss.circle.impl.fct.sql.SSCircleSQLFct;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreatePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivURIGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleTypesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleCreateRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCirclesGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesAddRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersAddRet;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCanAccessPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreateFromClientPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityUsersGetPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntityUsersGetRet;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubURIGetPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSCircleContentChangedPar;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSWarnE;

public class SSCircleImpl 
extends SSServImplWithDBA 
implements 
  SSCircleClientI, 
  SSCircleServerI, 
  SSEntityHandlerImplI{
  
  private final  SSCircleSQLFct sqlFct;
  private static SSUri          pubCircleUri = null;

  public SSCircleImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct = new SSCircleSQLFct(dbSQL);
  }
  
  @Override
  public SSEntity getUserEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setCircles){
            
        entity.circles.addAll(
          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlesGet(
            new SSCirclesGetPar(
              null,
              null,
              par.user, //user
              entity.id, //entity
              null, //entityTypesToIncludeOnly
              par.withUserRestriction, //withUserRestriction
              false,  //withSystemCircles
              true))); //invokeEntityHandlers
      }
      
      switch(entity.type){
        
        case circle:{
          
          final SSEntityCircle circle =
            circleGet(
              new SSCircleGetPar(
                null,
                null,
                par.user,
                entity.id,                    //circle
                par.entityTypesToIncludeOnly, //entityTypesToIncludeOnly
                par.withUserRestriction,  //withUserRestriction
                true));                   //invokeEntityHandlers
          
          return SSEntityCircle.get(circle, entity);
        }
      }
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circleContentChanged(final SSCircleContentChangedPar par) throws Exception{
  }
  
  @Override
  public void copyEntity(
    final SSUri        user,
    final List<SSUri>  users,
    final SSUri        entity,
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception{
    
  }
  
  @Override
  public List<SSUri> getSubEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    return new ArrayList<>();
  }
  
  @Override
  public List<SSUri> getParentEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    return new ArrayList<>();
  }
  
  @Override
  public void circleEntitiesRemove(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
     final SSCircleEntitiesRemovePar par = (SSCircleEntitiesRemovePar) parA.getFromJSON(SSCircleEntitiesRemovePar.class);
     
    sSCon.writeRetFullToClient(SSCircleEntitiesRemoveRet.get(circleEntitiesRemove(par)));
  }
  
  @Override
  public List<SSUri> circleEntitiesRemove(final SSCircleEntitiesRemovePar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        SSCircleMiscFct.checkWhetherUserIsAllowedToEditCircle(sqlFct, par.user, par.circle);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri entity : par.entities){
        sqlFct.removeEntity(par.circle, entity);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entities;
   }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleEntitiesRemove(par);
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
  public void circleCreate(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSCircleCreateFromClientPar par       = (SSCircleCreateFromClientPar) parA.getFromJSON(SSCircleCreateFromClientPar.class);
    final SSUri                       circleURI = circleCreate(par);

    if(!par.entities.isEmpty()){
      circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          null, 
          null, 
          par.user, 
          circleURI, 
          par.entities, 
          par.withUserRestriction, //withUserRestriction
          true)); //shouldCommit
    }
    
    if(!par.users.isEmpty()){
      circleUsersAdd(
        new SSCircleUsersAddPar(
          null, 
          null, 
          par.user, 
          circleURI, 
          par.users, 
          par.withUserRestriction, //withUserRestriction, 
          true)); //shouldCommit)); 
    }
    
    if(
      !par.entities.isEmpty() &&
      !par.users.isEmpty()){
      
      final SSEntityCircle circle =
        circleGet(
          new SSCircleGetPar(
            null,
            null,
            par.user,
            circleURI, //circle
            null, //entityTypesToIncludeOnly,
            par.withUserRestriction, //withUserRestriction,
            true)); //invokeEntityHandlers
      
      for(SSServContainerI serv : SSServReg.inst.getServsManagingEntities()){
        
        ((SSEntityHandlerImplI) serv.serv()).circleContentChanged(
          new SSCircleContentChangedPar(
            par.user,
            circleURI, //circle
            false, //isPublicCircle
            par.users,  //usersToAdd
            circle.entities, //entitiesToAdd,
            null,  //usersToPushEntitiesTo
            SSUri.getFromEntitites(circle.users), //circleUsers
            circle.entities)); //circleEntities
      }
    }
    
    sSCon.writeRetFullToClient(SSCircleCreateRet.get(circleURI));
    
    SSCircleActivityFct.createCircle(par, circleURI);
  }
  
  @Override
  public SSUri circleCreate(final SSCircleCreatePar par) throws Exception{
    
    try{
      
      final SSUri circleUri = SSServCaller.vocURICreate();
      
      if(par.withUserRestriction){
        
        if(par.isSystemCircle){
          throw new SSErr(SSErrE.notAllowedToCreateCircle);
        }
        
        if(
          par.forUser != null &&
          !SSStrU.equals(par.user,  par.forUser)){
          
          throw new SSErr(SSErrE.notAllowedToCreateCircle);
        }
        
        switch(par.circleType){
          case group:{
            break;
          }
          default: throw new SSErr(SSErrE.notAllowedToCreateCircle);
        }
      }
      
      if(par.forUser == null){
        par.forUser = par.user;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null, 
          null, 
          par.user, 
          circleUri,
          SSEntityE.circle, 
          par.label, 
          par.description, 
          null, //entitiesToAttach, 
          null, //creationTime, 
          null, //read,
          null, //setPublic
          par.withUserRestriction, //withUserRestriction, 
          false)); //shouldCommit))
      
      SSCircleMiscFct.addCircle(
        sqlFct,
        circleUri, 
        par.circleType, 
        par.isSystemCircle, 
        par.forUser);
      
      dbSQL.commit(par.shouldCommit);
      
      return circleUri;
   }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleCreate(par);
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
  public void circleUsersAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSCircleUsersAddPar par = (SSCircleUsersAddPar) parA.getFromJSON(SSCircleUsersAddPar.class);
      
    final SSUri          circleURI = circleUsersAdd(par);
    final SSEntityCircle circle    = 
      circleGet(
        new SSCircleGetPar(
          null, 
          null, 
          par.user, 
          circleURI, //circle
          null, //entityTypesToIncludeOnly, 
          par.withUserRestriction, //withUserRestriction, 
          true)); //invokeEntityHandlers))
    
    for(SSServContainerI serv : SSServReg.inst.getServsManagingEntities()){
      
      ((SSEntityHandlerImplI) serv.serv()).circleContentChanged(
        new SSCircleContentChangedPar(
          par.user,
          circleURI, //circle
          false, //isPublicCircle
          par.users,  //usersToAdd
          null, //entitiesToAdd,
          null,  //usersToPushEntitiesTo
          null, //circleUsers
          circle.entities)); //circleEntities
    }
    
    sSCon.writeRetFullToClient(SSCircleUsersAddRet.get(circleURI));
    
    SSCircleActivityFct.addUsersToCircle(par);
  }
  
  @Override
  public SSUri circleUsersAdd(final SSCircleUsersAddPar par) throws Exception{
    
    try{

      if(par.withUserRestriction){
        SSCircleMiscFct.checkWhetherUserIsAllowedToEditCircle(sqlFct, par.user, par.circle);
      }
      
      SSServCallerU.checkWhetherUsersAreUsers(par.users);
        
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri userUri : par.users){
        sqlFct.addUserToCircleIfNotExists(par.circle, userUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleUsersAdd(par);
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
  public void circleEntitiesAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSCircleEntitiesAddPar par      = (SSCircleEntitiesAddPar) parA.getFromJSON(SSCircleEntitiesAddPar.class);
    final SSUri                  cicleURI =  circleEntitiesAdd(par);
    
    final SSEntityCircle circle =
      circleGet(
        new SSCircleGetPar(
          null,
          null,
          par.user,
          par.circle, //circle
          null, //entityTypesToIncludeOnly,
          par.withUserRestriction, //withUserRestriction,
          true)); //invokeEntityHandlers))
    
    final List<SSEntity> entities = 
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
        new SSEntitiesGetPar(
          null, 
          null, 
          par.user, 
          par.entities, 
          null, //forUser, 
          null, //types, 
          null, //descPar, 
          par.withUserRestriction)); //withUserRestriction, 
    
    for(SSServContainerI serv : SSServReg.inst.getServsManagingEntities()){
      
      ((SSEntityHandlerImplI) serv.serv()).circleContentChanged(
        new SSCircleContentChangedPar(
          par.user,
          cicleURI, //circle
          false, //isPublicCircle
          null,  //usersToAdd
          entities, //entitiesToAdd,
          null,  //usersToPushEntitiesTo
          SSUri.getFromEntitites(circle.users), //circleUsers
          null)); //circleEntities
    }
    
    sSCon.writeRetFullToClient(SSCircleEntitiesAddRet.get(cicleURI));
    
    SSCircleActivityFct.addEntitiesToCircle(par);
  }
  
  @Override
  public SSUri circleEntitiesAdd(final SSCircleEntitiesAddPar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        
        SSCircleMiscFct.checkWhetherUserIsAllowedToEditCircle(
          sqlFct,   
          par.user, 
          par.circle);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri entity : par.entities){
        
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
            null, 
            null, 
            par.user, 
            entity, 
            null, //type, 
            null, //label, 
            null, //description, 
            null, //entitiesToAttach,
            null, //creationTime, 
            null, //read, 
            null, //setPublic
            par.withUserRestriction, //withUserRestriction 
            false)); //shouldCommit
        
        sqlFct.addEntityToCircleIfNotExists(
          par.circle, 
          entity);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleEntitiesAdd(par);
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
  public SSCircleE circleMostOpenCircleTypeGet(final SSCircleMostOpenCircleTypeGetPar par) throws Exception{
    
    try{
      
      SSCircleE    mostOpenCircleType = SSCircleE.priv;
      
      for(SSCircleE circleType :
        circleTypesGet(
          new SSCircleTypesGetPar(
            null,
            null,
            par.user,
            par.forUser,
            par.entity,
            par.withUserRestriction))){
        
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
  public List<SSCircleE> circleTypesGet(final SSCircleTypesGetPar par) throws Exception{
    
    try{
      
      if(par.entity == null){
        throw new Exception("entity to retrieve circle types for is null");
      }
      
      if(
        par.withUserRestriction &&
        par.forUser != null &&
        !SSStrU.equals(par.forUser, par.user)){
        throw new Exception("user cannot retrieve circle types for other user");
      }
      
      if(par.forUser == null){
        return sqlFct.getCircleTypesForEntity(par.entity);
      }else{
        return sqlFct.getCircleTypesCommonForUserAndEntity(par.forUser, par.entity);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circleGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSCircleGetPar par = (SSCircleGetPar) parA.getFromJSON(SSCircleGetPar.class);
    
    sSCon.writeRetFullToClient(SSCircleGetRet.get(circleGet(par)));
  }
  
  @Override
  public SSEntityCircle circleGet(final SSCircleGetPar par) throws Exception{
    
    try{
      final SSEntityCircle circle;
      
      if(par.withUserRestriction){
        
        if(sqlFct.isSystemCircle(par.circle)){
          SSLogU.warn(SSErrE.userNotAllowToAccessSystemCircle);
          return null;
        }
        
        if(!SSServCallerU.canUserRead(par.user, par.circle)){
          return null;
        }
      }
      
      circle =
        SSEntityCircle.get(
          sqlFct.getCircle(
            par.circle,
            true, //withUsers
            true, //withEntities
            true, //withCircleRights
            par.entityTypesToIncludeOnly),
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              par.circle,
              null, //forUser,
              par.withUserRestriction, //withUserRestriction,
              null))); //descPar
      
      if(!par.invokeEntityHandlers){
        return circle;
      }
        
      final SSEntityDescriberPar descPar = new SSEntityDescriberPar();
      
      descPar.setOverallRating = true;
      
      circle.entities.clear();
      circle.entities.addAll(
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            null,
            null,
            par.user,
            SSUri.getFromEntitites(circle.entities),
            null, //forUser,
            null, //types,
            descPar, //descPar
            par.withUserRestriction))); //withUserRestriction
      
      circle.users.clear();
      circle.users.addAll(
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            null,
            null,
            par.user,
            SSUri.getFromEntitites(circle.users),
            null, //forUser,
            null, //types,
            descPar, //descPar
            par.withUserRestriction))); //withUserRestriction
      
      return circle;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circlesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

   SSServCallerU.checkKey(parA);

   final SSCirclesGetPar par = (SSCirclesGetPar) parA.getFromJSON(SSCirclesGetPar.class);
   
   sSCon.writeRetFullToClient(SSCirclesGetRet.get(circlesGet(par)));
  }
  
  @Override
  public List<SSEntity> circlesGet(final SSCirclesGetPar par) throws Exception{
    
    try{
      
      final List<SSEntity>            circles           = new ArrayList<>();
      final List<SSUri>               circleUris        = new ArrayList<>();
      
      if(par.withUserRestriction){
        
        if(par.withSystemCircles){
          SSLogU.warn(SSErrE.userNotAllowToAccessSystemCircle);
          return circles;
        }
        
        if(par.entity != null){
          
          if(!SSServCallerU.canUserRead(par.user, par.entity)){
            return circles;
          }
        }
      }
      
      if(!SSObjU.isNull(par.user, par.entity)){
        
        circleUris.addAll(
          sqlFct.getCircleURIsCommonForUserAndEntity(
            par.user,
            par.entity,
            par.withSystemCircles));
      }else{
        
        if(
          par.user    == null &&
          par.entity  == null){
          
          circleUris.addAll(sqlFct.getCircleURIs(par.withSystemCircles));
        }else{
          
          if(par.user != null){
            circleUris.addAll(sqlFct.getCircleURIsForUser(par.user, par.withSystemCircles));
          }
          
          if(par.entity != null){
            circleUris.addAll(sqlFct.getCircleURIsForEntity(par.entity, par.withSystemCircles));
          }
        }
      }
      
      if(!par.invokeEntityHandlers){
        
        for(SSUri circleURI : circleUris){
          
          SSEntity.addEntitiesDistinctWithoutNull(
            circles, 
            circleGet(
              new SSCircleGetPar(
                null,
                null,
                par.user,
                circleURI,
                par.entityTypesToIncludeOnly,
                par.withUserRestriction,
                par.invokeEntityHandlers)));
        }
        
        return circles;
      }
      
      final SSEntityDescriberPar descPar = new SSEntityDescriberPar();
      
      descPar.entityTypesToIncludeOnly.addAll(par.entityTypesToIncludeOnly);
      
      return ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
        new SSEntitiesGetPar(
          null,
          null,
          par.user,
          circleUris,  //entities
          null, //forUser,
          null, //types,
          descPar, //descPar,
          par.withUserRestriction));// withUserRestriction
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri circlePrivURIGet(final SSCirclePrivURIGetPar par) throws Exception{
    
    try{
      
      SSUri circleURI;
      
      dbSQL.startTrans(par.shouldCommit);
      
      circleURI = sqlFct.getPrivCircleURI(par.user);
      
      if(circleURI != null){
        return circleURI;
      }
      
      circleURI = SSServCaller.vocURICreate();
      
      sqlFct.addEntity(
        circleURI, 
        SSEntityE.circle, 
        SSVocConf.systemUserUri);
      
      SSCircleMiscFct.addCircle(
        sqlFct,
        circleURI, 
        SSCircleE.priv, 
        true, 
        par.user);
      
      dbSQL.commit(par.shouldCommit);
      
      return circleURI;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circlePrivURIGet(par);
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
  public SSUri circlePubURIGet(final SSCirclePubURIGetPar par) throws Exception{
    
    try{
      
      final SSUri tmpPublicCircleURI;
      
      if(pubCircleUri != null){
        return pubCircleUri;
      }
      
      pubCircleUri = sqlFct.getPubCircleURI();
      
      if(pubCircleUri != null){
        return pubCircleUri;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      tmpPublicCircleURI = SSServCaller.vocURICreate();
      
      sqlFct.addEntity(
        tmpPublicCircleURI, 
        SSEntityE.circle, 
        SSVocConf.systemUserUri);
      
      SSCircleMiscFct.addCircle(
        sqlFct, 
        tmpPublicCircleURI,
        SSCircleE.pub, 
        true, 
        par.user);
      
      dbSQL.commit(par.shouldCommit);
      
      pubCircleUri = tmpPublicCircleURI;
      
      return pubCircleUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circlePubURIGet(par);
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
  public void circleCanAccess(final SSCircleCanAccessPar par) throws Exception{
    
    try{
      
      if(par.entityURI == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(!sqlFct.existsEntity(par.entityURI)){
        return;
      }
      
      if(!SSCircleMiscFct.canUserForEntityType(
        sqlFct,
        par.user,
        sqlFct.getEntity(par.entityURI),
        par.accessRight)){
        
        SSLogU.warn(SSWarnE.userNotAllowedToAccessEntity);
        throw new SSErr(SSErrE.userNotAllowedToAccessEntity);
      }
      
    }catch(SSErr error){
      SSServErrReg.regErrThrow(error, false);
    }
  }
  
  @Override
  public void circleEntityUsersGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSCircleEntityUsersGetPar par = (SSCircleEntityUsersGetPar) parA.getFromJSON(SSCircleEntityUsersGetPar.class);
    
    sSCon.writeRetFullToClient(SSCircleEntityUsersGetRet.get(circleEntityUsersGet(par)));
  }
  
  @Override
  public List<SSEntity> circleEntityUsersGet(final SSCircleEntityUsersGetPar par) throws Exception{
    
//TODO to be integrated with withUserRestriction
    try{
      final List<SSUri>                   userUris        = new ArrayList<>();
      final List<SSUri>                   userCircleUris  = sqlFct.getCircleURIsForUser   (par.user, true);
      
      for(SSUri circleUri : sqlFct.getCircleURIsForEntity(par.entity, true)){
        
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
            
            for(SSEntity user : sqlFct.getUsersForCircle(circleUri)){
              
              if(!SSStrU.contains(userUris, user.id)){
                userUris.add(user.id);
              }
            }
            
            break;
          }
          
          case group:{
            
            if(!SSStrU.contains(userCircleUris, circleUri)){
              continue;
            }
            
            for(SSEntity user : sqlFct.getUsersForCircle(circleUri)){
              
              if(!SSStrU.contains(userUris, user.id)){
                userUris.add(user.id);
              }
            }
            
            break;
          }
        }
      }
      
      return ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
        new SSEntitiesGetPar(
          null,
          null,
          null,
          userUris, //entities
          null,     //forUser
          null, //types
          null, //descPar
          false));  //withUserRestriction
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}