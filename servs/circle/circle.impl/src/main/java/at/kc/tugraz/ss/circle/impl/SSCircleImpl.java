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
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivEntityAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubEntityAddPar;
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
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUserCanPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityPublicSetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityUsersGetPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntityUsersGetRet;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityPublicSetRet;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitySharePar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntityShareRet;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;

public class SSCircleImpl 
extends SSServImplWithDBA 
implements 
  SSCircleClientI, 
  SSCircleServerI{
  
  private final SSCircleSQLFct sqlFct;

  public SSCircleImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct = new SSCircleSQLFct(dbSQL);
  }
  
  @Override
  public void circleEntitiesRemove(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSCircleEntitiesRemoveRet.get(circleEntitiesRemove(parA), parA.op), parA.op);
  }
  
  @Override
  public List<SSUri> circleEntitiesRemove(final SSServPar parA) throws Exception{
    
    try{
      
      final SSCircleEntitiesRemovePar par        = SSCircleEntitiesRemovePar.get(parA);
      
      if(par.withUserRestriction){
        SSServCallerU.canUserEditEntity(par.user, par.circle);
        
        if(sqlFct.isSystemCircle(par.circle)){
          throw new SSErr(SSErrE.userNotAllowedToAccessCircle);
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri entity : par.entities){
        sqlFct.removeEntity(par.circle, entity);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entities;
   }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleEntitiesRemove(parA);
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
  public void circleCreate(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSCircleCreatePar par = (SSCircleCreatePar) parA.getFromJSON(SSCircleCreatePar.class);
    
    final SSUri result = circleCreate(par);
    
    sSCon.writeRetFullToClient(SSCircleCreateRet.get(result), parA.op);
    
    SSCircleActivityFct.createCircle(par, result);
  }
  
  @Override
  public SSUri circleCreate(final SSCircleCreatePar par) throws Exception{
    
    try{
      
      final SSUri             circleUri  = SSServCaller.vocURICreate();
      
      if(par.withUserRestriction){
        SSServCallerU.canUserEditEntities (par.user, par.entities);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        circleUri,
        SSEntityE.circle, 
        par.label, 
        par.description, 
        null, 
        false);
      
      sqlFct.addCircle(
        circleUri, 
        SSCircleE.group, 
        par.isSystemCircle);
      
      sqlFct.addUserToCircleIfNotExists(
        circleUri, 
        par.user);
      
      if(!par.entities.isEmpty()){
        
        circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            null,
            null,
            par.user,
            circleUri, 
            par.entities, 
            false,
            par.invokeEntityHandlers,
            false));
      }      
      
      if(!par.users.isEmpty()){
        
        circleUsersAdd(
          new SSCircleUsersAddPar(
            null,
            null,
            par.user,
            circleUri,
            par.users,
            false,
            false));
      }
      
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
      
    final SSUri result = circleUsersAdd(par);
    
    sSCon.writeRetFullToClient(SSCircleUsersAddRet.get(result), parA.op);
    
    SSCircleActivityFct.addUsersToCircle(par);
  }
  
  @Override
  public SSUri circleUsersAdd(final SSCircleUsersAddPar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        SSCircleMiscFct.checkWhetherUserIsAllowedToEditCircle (sqlFct, par.user, par.circle);
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

    final SSCircleEntitiesAddPar par = (SSCircleEntitiesAddPar) parA.getFromJSON(SSCircleEntitiesAddPar.class);
    
    sSCon.writeRetFullToClient(SSCircleEntitiesAddRet.get(circleEntitiesAdd(par)), parA.op);
    
    SSCircleActivityFct.addEntitiesToCircle(par);
  }
  
  @Override
  public SSUri circleEntitiesAdd(final SSCircleEntitiesAddPar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        SSCircleMiscFct.checkWhetherUserIsAllowedToEditCircle (sqlFct,   par.user, par.circle);
        SSServCallerU.canUserEditEntities                     (par.user, par.entities);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCallerU.addEntities(
        par.user,
        par.entities);
      
      for(SSUri entityUri : par.entities){
        sqlFct.addEntityToCircleIfNotExists(par.circle, entityUri);
      }
      
      if(par.invokeEntityHandlers){
        
        SSCircleMiscFct.shareEntityWithCircleByHandlers(
          par.user, 
          par.entities,
          par.circle);
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
      
      SSCircleE                              mostOpenCircleType = SSCircleE.priv;
      
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
    
    sSCon.writeRetFullToClient(SSCircleGetRet.get(circleGet(par)), parA.op);
  }
  
  @Override
  public SSEntityCircle circleGet(final SSCircleGetPar par) throws Exception{
    
    try{
      final List<SSEntity>  entities = new ArrayList<>();
      final List<SSEntity>  users    = new ArrayList<>();
      final SSEntityCircle  circle;
      
      if(par.withUserRestriction){
        
        if(par.forUser == null){
          par.forUser = par.user;
        }
        
        if(
          par.withSystemCircles ||
          sqlFct.isSystemCircle(par.circle)){
          
          throw new Exception("user cannot access system circle");
        }
        
        SSServCallerU.canUserReadEntity(par.forUser, par.circle);
      }
      
      circle = 
        sqlFct.getCircle(
          par.circle, 
          true, 
          true, 
          true, 
          par.entityTypesToIncludeOnly);
      
      if(par.invokeEntityHandlers){
        
        for(SSEntity entity : circle.entities){
          
          entities.add(
            SSServCaller.entityDescGet(
              par.user,
              entity.id, 
              false, //getTags 
              true,  //getOverallRating
              false, 
              false, 
              false, 
              false, 
              false));
        }
        
        circle.entities.clear();
        circle.entities.addAll(entities);
        
        for(SSEntity user : circle.users){
          
          users.add(
            SSServCaller.entityDescGet(
              par.user, 
              user.id, 
              false, 
              false, 
              false, 
              false, 
              false, 
              false, 
              false));
        }
        
        circle.users.clear();
        circle.users.addAll(users);
      }
      
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
   
   sSCon.writeRetFullToClient(SSCirclesGetRet.get(circlesGet(par)), parA.op);
  }
  
  @Override
  public List<SSEntityCircle> circlesGet(final SSCirclesGetPar par) throws Exception{
    
    try{
      
      final List<SSEntityCircle>            circles           = new ArrayList<>();
      final List<SSUri>                     circleUris        = new ArrayList<>();
      
      if(par.withUserRestriction){
        
        if(par.forUser == null){
          par.forUser = par.user;
        }
        
        if(par.withSystemCircles){
          throw new Exception("user cannot access system circles");
        }
        
        if(par.entity != null){
          SSServCallerU.canUserReadEntity(par.forUser, par.entity);
        }
      }
      
      if(!SSObjU.isNull(par.forUser, par.entity)){
        
        for(SSEntityCircle circle : 
          sqlFct.getCirclesCommonForUserAndEntity(
            par.forUser,
            par.entity,
            par.withSystemCircles)){
          
          circleUris.add(circle.id);
        }
        
      }else{
        
        if(
          par.forUser == null &&
          par.entity  == null){
          
          circleUris.addAll(sqlFct.getCircleURIs(par.withSystemCircles));
        }else{
          
          if(par.forUser != null){
            circleUris.addAll(sqlFct.getCircleURIsForUser(par.forUser, par.withSystemCircles));
          }
          
          if(par.entity != null){
            circleUris.addAll(sqlFct.getCircleURIsForEntity(par.entity, par.withSystemCircles));
          }
        }
      }
      
      if(par.withUserRestriction){
        
        for(SSUri circleUri : circleUris){
          
          try{
            SSServCallerU.canUserReadEntity(par.forUser, circleUri);
          }catch(Exception error){
            SSServErrReg.reset();
            continue;
          }
          
          circles.add(
            circleGet(
              new SSCircleGetPar(
                null, 
                null,
                par.forUser,
                circleUri, 
                null,
                par.entityTypesToIncludeOnly,
                true, 
                par.withSystemCircles,
                par.invokeEntityHandlers)));
        }
      }else{
        
        for(SSUri circleUri : circleUris){
          
          circles.add(
            circleGet(
              new SSCircleGetPar(
                null,
                null,
                par.user,
                circleUri,
                null,
                par.entityTypesToIncludeOnly,
                false,
                par.withSystemCircles,
                par.invokeEntityHandlers)));
        }
      }

      return circles;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circleEntitiesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(
      SSCircleEntitiesGetRet.get(
        circleEntitiesGet((SSCircleEntitiesGetPar) parA.getFromJSON(SSCircleEntitiesGetPar.class))), 
      parA.op);
  }
  
  @Override
  public List<SSEntity> circleEntitiesGet(final SSCircleEntitiesGetPar par) throws Exception{
    
    //TODO to be refactored exhaustively (e.g. introduce circle parameter and user par.withUserRestriction together with par.forUser)
    try{
      final List<SSEntity>         entities = new ArrayList<>();

      if(par.withUserRestriction){
        
        if(par.forUser == null){
          par.forUser = par.user;
        }
      }
      
      for(SSEntity entity : sqlFct.getCircleEntityURIsForUser(par.forUser, par.withSystemCircles, par.types))
        
//      for(SSUri circle : sqlFct.getCircleURIsForUser(par.forUser, par.withSystemCircles)){
        
//        for(SSEntity entity : sqlFct.getEntitiesForCircle(circle, par.types)){
        
        try{
          entities.add(SSServCaller.entityUserGet(par.user, entity.id, par.forUser, false));
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
  public SSUri circlePrivURIGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSCirclePrivURIGetPar par = new SSCirclePrivURIGetPar(parA);
      final SSUri                       privCircleUri;
      
      dbSQL.startTrans(parA.shouldCommit);
      
      privCircleUri = SSCircleMiscFct.addOrGetPrivCircleURI(sqlFct, par.user);
      
      dbSQL.commit(parA.shouldCommit);
      
      return privCircleUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circlePrivURIGet(parA);
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
  public SSUri circlePubURIGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSUri circleUri;
      
      dbSQL.startTrans(parA.shouldCommit);
      
      circleUri = SSCircleMiscFct.addOrGetPubCircleURI(sqlFct);
      
      dbSQL.commit(parA.shouldCommit);
      
      return circleUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circlePubURIGet(parA);
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
  public void circlePrivEntityAdd(final SSCirclePrivEntityAddPar par) throws Exception{
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user, 
        par.entity, 
        par.type, 
        par.label, 
        par.description, 
        par.creationTime, 
        false);
      
      circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          null, 
          null,
          par.user, 
          SSCircleMiscFct.addOrGetPrivCircleURI (sqlFct, par.user),
          SSUri.asListWithoutNullAndEmpty(par.entity),
          false, 
          false,
          false));
      
      dbSQL.commit(par.shouldCommit);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          circlePrivEntityAdd(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void circlePubEntityAdd(final SSCirclePubEntityAddPar par) throws Exception{
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user, 
        par.entity, 
        par.type, 
        par.label, 
        par.description, 
        par.creationTime, 
        false);

      circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          null,
          null,
          par.user,
          SSCircleMiscFct.addOrGetPubCircleURI (sqlFct),
          SSUri.asListWithoutNullAndEmpty      (par.entity),
          false,
          false,
          false));
      
      dbSQL.commit(par.shouldCommit);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          circlePubEntityAdd(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSEntity circleUserCan(final SSServPar parA) throws Exception{
   
    try{

      final SSCircleUserCanPar par    = new SSCircleUserCanPar(parA);
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
        par.accessRight,
        par.logErr);
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error, parA.logErr);
      return null;
    }
  }
  
  @Override
  public void circleEntityPublicSet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSCircleEntityPublicSetRet.get(circleEntityPublicSet(parA), parA.op), parA.op);
    
    SSCircleActivityFct.setEntityPublic(new SSCircleEntityPublicSetPar(parA));
  }
  
  @Override 
  public SSUri circleEntityPublicSet(final SSServPar parA) throws Exception{
    
    try{

      final SSCircleEntityPublicSetPar par = new SSCircleEntityPublicSetPar(parA);
      
      SSServCallerU.canUserAllEntity(par.user, par.entity);
      
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
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleEntityPublicSet(parA);
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
  public void circleEntityUsersGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSCircleEntityUsersGetRet.get(circleEntityUsersGet(parA), parA.op), parA.op);
  }
  
  @Override
  public List<SSEntity> circleEntityUsersGet(final SSServPar parA) throws Exception{
    //to be integrated with withUserRestriction
    try{
      final SSCircleEntityUsersGetPar     par             = new SSCircleEntityUsersGetPar(parA);
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
      
      return SSServCallerU.getEntities(userUris);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circleEntityShare(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSCircleEntityShareRet.get(circleEntityShare(parA), parA.op), parA.op);
  }
  
  @Override  
  public SSUri circleEntityShare(final SSServPar parA) throws Exception{
    
    try{
      final SSCircleEntitySharePar par = new SSCircleEntitySharePar(parA);
      
      if(
        par.users.isEmpty() &&
        par.circles.isEmpty()){
        return par.entity;
      }
      
      SSServCallerU.canUserEditEntity                            (par.user, par.entity);

      if(!par.users.isEmpty()){
        
        SSCircleMiscFct.checkWhetherUserWantsToShareWithHimself (par.user, par.users);
        SSServCallerU.checkWhetherUsersAreUsers                 (par.users);
        
        dbSQL.startTrans(par.shouldCommit);
        
        final SSUri circleUri =
          circleCreate(
            new SSCircleCreatePar(
              null,
              null,
              par.user, //user
              null, //label
              SSUri.asListWithoutNullAndEmpty(par.entity), //entities
              par.users, //users
              null, //description
              true,  //isSystemCircle
              false, //withUserRestriction
              false, //invokeEntityHandlers
              false)); //shouldCommit
        
//            null, //entities
//            null, //users
//            null, //label
//            null, //description
//            null, //isSystemCircle
//            null, //shouldCommit
//            null, //withUserRestriction
//            null); //invokeEntityHandlers
        
        SSCircleMiscFct.shareEntityWithUsersByHandlers(
          par.user,
          par.users,
          par.entity,
          circleUri);
        
        SSCircleActivityFct.shareEntityWithUsers(par, circleUri);
      }
      
      if(!par.circles.isEmpty()){

        dbSQL.startTrans(par.shouldCommit);
        
        for(SSUri circle : par.circles){
          
          circleEntitiesAdd(
            new SSCircleEntitiesAddPar(
              null, 
              null, 
              par.user, 
              circle, 
              SSUri.asListWithoutNullAndEmpty(par.entity), 
              true, 
              true, 
              false));
          
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
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleEntityShare(parA);
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
}