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

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.kc.tugraz.ss.circle.api.SSCircleClientI;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.impl.fct.serv.SSCircleServFct;
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
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesRemoveFromClientPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityUsersGetPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntityUsersGetRet;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubURIGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesFromEntityEntitiesAdd;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSCircleContentRemovedI;
import at.tugraz.sss.serv.SSCircleContentRemovedPar;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSCopyEntityI;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityCopyPar;
import at.tugraz.sss.serv.SSEntityDescriberPar;
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
import at.tugraz.sss.serv.SSToolContextE;
import at.tugraz.sss.serv.SSWarnE;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSCircleImpl
extends SSServImplWithDBA
implements
  SSCircleClientI,
  SSCircleServerI,
  SSDescribeEntityI,
  SSCopyEntityI{
  
  private static SSUri             pubCircleUri = null;
  private final  SSCircleSQLFct    sqlFct;
  private final  SSCircleMiscFct   miscFct;
  private final  SSActivityServerI activityServ;
  private final  SSEvalServerI     evalServ;
  
  public SSCircleImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct       = new SSCircleSQLFct  (dbSQL);
    this.miscFct      = new SSCircleMiscFct (this, sqlFct);
    this.activityServ = (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
    this.evalServ     = (SSEvalServerI)     SSServReg.getServ(SSEvalServerI.class);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setCircles){
        
        entity.circles.addAll(
          circlesGet(
            new SSCirclesGetPar(
              par.user, //user
              entity.id, //entity
              null, //entityTypesToIncludeOnly
              par.withUserRestriction, //withUserRestriction
              false,  //withSystemCircles
              false))); //invokeEntityHandlers
      }
      
      if(par.setCircleTypes){
        
        entity.circleTypes.addAll(
          circleTypesGet(
            new SSCircleTypesGetPar(
              par.user,
              entity.id,
              par.withUserRestriction)));
      }
      
      switch(entity.type){
        
        case circle:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          final SSEntityCircle circle =
            circleGet(
              new SSCircleGetPar(
                par.user,
                entity.id,                    //circle
                par.entityTypesToIncludeOnly, //entityTypesToIncludeOnly
                false,  //setTags
                null, //tagSpace
                par.withUserRestriction,  //withUserRestriction
                false));                   //invokeEntityHandlers
          
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
  public void copyEntity(
    final SSEntity                  entity,
    final SSEntityCopyPar           par) throws Exception{
    
    try{
      
      switch(entity.type){
        case circle: break;
        default: return;
      }
      
      if(par.withUserRestriction){
        
        if(!sqlFct.isGroupCircle(entity.id)){
          return;
        }
        
        if(par.targetEntity != null){
           
          if(!sqlFct.isGroupCircle(par.targetEntity)){
            return;
          }
        }
      }
      
      final SSEntityCircle circle =
        circleGet(
          new SSCircleGetPar(
            par.user,
            entity.id,
            null, //entityTypesToIncludeOnly
            false,  //setTags
            null, //tagSpace
            par.withUserRestriction,
            true)); //invokeEntityHandlers
      
      if(par.targetEntity == null){
        miscFct.copyCircleToNewCircle(par, circle);
      }else{
        miscFct.copyCircleToExistingCircle(par, circle);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void circleEntitiesRemove(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSCircleEntitiesRemoveFromClientPar par    = (SSCircleEntitiesRemoveFromClientPar) parA.getFromJSON(SSCircleEntitiesRemoveFromClientPar.class);
    final List<SSUri>                         result;
    
    if(!sqlFct.isGroupCircle(par.circle)){
      result = new ArrayList<>();
    }else{
      result = circleEntitiesRemove(par);
    }
    
    if(!result.isEmpty()){
      
      for(SSServContainerI serv : SSServReg.inst.getServsHandlingCircleContentRemoved()){
        
        ((SSCircleContentRemovedI) serv.serv()).circleContentRemoved(
          new SSCircleContentRemovedPar(
            par.user,
            par.circle, //circle
            result, //entities
            par.removeCircleSpecificMetadata, //removeCircleSpecificMetadata
            par.withUserRestriction, //withUserRestriction
            par.shouldCommit)); //shouldCommit
      }
    }
    
    sSCon.writeRetFullToClient(SSCircleEntitiesRemoveRet.get(result));
    
    evalServ.evalLog(
      new SSEvalLogPar(
        par.user,
        SSToolContextE.sss,
        SSEvalLogE.circleEntitiesRemove,
        par.circle,  //entity
        null, //content,
        par.entities, //entities
        null, //users
        par.shouldCommit));
  }
  
  @Override
  public List<SSUri> circleEntitiesRemove(final SSCircleEntitiesRemovePar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.circle)){
          return new ArrayList<>();
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
  public void circleUsersRemove(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSCircleUsersRemovePar par    = (SSCircleUsersRemovePar) parA.getFromJSON(SSCircleUsersRemovePar.class);
    final List<SSUri>            result;
    
    if(!sqlFct.isGroupCircle(par.circle)){
      result = new ArrayList<>();
    }else{
      result = circleUsersRemove(par);
    }
    
    sSCon.writeRetFullToClient(SSCircleUsersRemoveRet.get(result));
    
    evalServ.evalLog(
      new SSEvalLogPar(
        par.user,
        SSToolContextE.sss,
        SSEvalLogE.circleUsersRemove,
        par.circle,  //entity
        null, //content,
        null, //entities
        par.users, //users
        par.shouldCommit));
  }
  
  @Override
  public List<SSUri> circleUsersRemove(final SSCircleUsersRemovePar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.circle)){
          return new ArrayList<>();
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri user : par.users){
        sqlFct.removeUser(par.circle, user);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.users;
   }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleUsersRemove(par);
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
          par.user, 
          circleURI, 
          par.entities, 
          par.withUserRestriction, //withUserRestriction
          par.shouldCommit)); //shouldCommit
    }
    
    if(!par.users.isEmpty()){
      circleUsersAdd(
        new SSCircleUsersAddPar(
          par.user, 
          circleURI, 
          par.users, 
          par.withUserRestriction, //withUserRestriction, 
          par.shouldCommit)); //shouldCommit));
    }
    
    final SSEntityCircle circle =
      circleGet(
        new SSCircleGetPar(
          par.user,
          circleURI, //circle
          null, //entityTypesToIncludeOnly,
          false,  //setTags
          null, //tagSpace
          par.withUserRestriction, //withUserRestriction,
          false)); //invokeEntityHandlers
    
    SSServCallerU.handleCircleEntitiesAdd(
      par.user, 
      circle,
      circle.entities, 
      par.withUserRestriction);
     
    sSCon.writeRetFullToClient(SSCircleCreateRet.get(circleURI));
    
    activityServ.activityAdd(
      new SSActivityAddPar(
        par.user,
        SSActivityE.createCircle,
        circleURI,
        par.users, //users,
        par.entities, //entities,
        null,
        null,
        par.shouldCommit));
    
    evalServ.evalLog(
      new SSEvalLogPar(
        par.user,
        SSToolContextE.sss,
        SSEvalLogE.circleCreate,
        circleURI,  //entity
        null, //content,
        par.entities,
        par.users,
        par.shouldCommit));
  }
  
  @Override
  public SSUri circleCreate(final SSCircleCreatePar par) throws Exception{
    
    try{
      
      final SSUri circleUri = SSServCaller.vocURICreate();
      
      if(par.withUserRestriction){
        
        if(par.isSystemCircle){
          throw new SSErr(SSErrE.notAllowedToCreateCircle);
        }
        
//        if(
//          par.forUser != null &&
//          !SSStrU.equals(par.user,  par.forUser)){
//          
//          throw new SSErr(SSErrE.notAllowedToCreateCircle);
//        }
        
        switch(par.circleType){
          case group:{
            break;
          }
          default: throw new SSErr(SSErrE.notAllowedToCreateCircle);
        }
      }
      
//      if(par.forUser == null){
//        par.forUser = par.user;
//      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
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
      
      miscFct.addCircle(
        circleUri, 
        par.circleType, 
        par.isSystemCircle, 
        par.user); //par.forUser
      
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
  public void circleRemove(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSCircleRemovePar par       = (SSCircleRemovePar) parA.getFromJSON(SSCircleRemovePar.class);
    final SSUri             circleURI;
    
    if(!sqlFct.isGroupCircle(par.circle)){
      circleURI = null;
    }else{
      circleURI = circleRemove(par);
    }
    
    sSCon.writeRetFullToClient(SSCircleRemoveRet.get(circleURI));
    
    evalServ.evalLog(
      new SSEvalLogPar(
        par.user,
        SSToolContextE.sss,
        SSEvalLogE.circleRemove,
        par.circle,  //entity
        null, //content,
        null, //entities
        null, //users
        par.shouldCommit));
  }
  
  @Override
  public SSUri circleRemove(final SSCircleRemovePar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.circle)){
          return null;
        }
      }
        
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.removeCircle(par.circle);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.circle;
   }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return circleRemove(par);
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
    final SSUri               circleURI;
    
    if(!sqlFct.isGroupCircle(par.circle)){
      circleURI = null;
    }else{
      circleURI = circleUsersAdd(par);
    }
    
    final SSEntityCircle circle    = 
      circleGet(
        new SSCircleGetPar(
          par.user,
          circleURI, //circle
          null, //entityTypesToIncludeOnly,
          false,  //setTags
          null, //tagSpace
          par.withUserRestriction, //withUserRestriction,
          true)); //invokeEntityHandlers))
    
    SSServCallerU.handleCircleUsersAdd(
      par.user, 
      circle,
      par.users, 
      par.withUserRestriction);
    
    sSCon.writeRetFullToClient(SSCircleUsersAddRet.get(circleURI));
    
    activityServ.activityAdd(
      new SSActivityAddPar(
        par.user,
        SSActivityE.addUsersToCircle,
        par.circle,
        par.users,
        null,
        null,
        null,
        par.shouldCommit));
    
    evalServ.evalLog(
      new SSEvalLogPar(
        par.user,
        SSToolContextE.sss,
        SSEvalLogE.circleUsersAdd,
        par.circle,  //entity
        null, //content,
        null, //entities
        par.users, //users
        par.shouldCommit));
  }
  
  @Override
  public SSUri circleUsersAdd(final SSCircleUsersAddPar par) throws Exception{
    
    try{

      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.circle)){
          return par.circle;
        }
      }
      
      if(!SSServCallerU.areUsersUsers(par.users)){
        return par.circle;
      }
        
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
    final SSUri                  cicleURI;
    
    if(!sqlFct.isGroupCircle(par.circle)){
      cicleURI = null;
    }else{
      cicleURI = circleEntitiesAdd(par);
    }
    
    final SSEntityCircle circle =
      circleGet(
        new SSCircleGetPar(
          par.user,
          par.circle, //circle
          null, //entityTypesToIncludeOnly,
          false,  //setTags
          null, //tagSpace
          par.withUserRestriction, //withUserRestriction,
          false)); //invokeEntityHandlers
    
    SSCircleServFct.addTags(
      par.user, 
      par.tags, 
      par.entities, 
      par.circle);
    
    SSCircleServFct.addCategories(
      par.user, 
      par.categories, 
      par.entities, 
      par.circle);
    
    final List<SSEntity> entities = 
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
        new SSEntitiesGetPar(
          par.user, 
          par.entities, 
          null, //types, 
          null, //descPar, 
          par.withUserRestriction)); //withUserRestriction, 
    
    SSServCallerU.handleCircleEntitiesAdd(
      par.user, 
      circle,
      entities, 
      par.withUserRestriction);
    
    sSCon.writeRetFullToClient(SSCircleEntitiesAddRet.get(cicleURI));
    
    activityServ.activityAdd(
      new SSActivityAddPar(
        par.user,
        SSActivityE.addEntitiesToCircle,
        par.circle,
        SSUri.asListWithoutNullAndEmpty(),
        par.entities,
        null,
        null,
        par.shouldCommit));
    
    evalServ.evalLog(
      new SSEvalLogPar(
        par.user,
        SSToolContextE.sss,
        SSEvalLogE.circleEntitiesAdd,
        par.circle,  //entity
        null, //content,
        par.entities,
        null, //users
        par.shouldCommit));
    
    evalServ.evalLog(
      new SSEvalLogPar(
        par.user,
        SSToolContextE.sss,
        SSEvalLogE.circleEntitiesAddTagsAdd,
        par.circle,  //entity
        SSStrU.toCommaSeparatedStrNotNull(par.tags), //content,
        par.entities,
        null, //users
        par.shouldCommit));
    
    evalServ.evalLog(
      new SSEvalLogPar(
        par.user,
        SSToolContextE.sss,
        SSEvalLogE.circleEntitiesAddCategoriesAdd,
        par.circle,  //entity
        SSStrU.toCommaSeparatedStrNotNull(par.categories), //content,
        par.entities,
        null, //users
        par.shouldCommit));
  }
  
  @Override
  public SSUri circleEntitiesAdd(final SSCircleEntitiesAddPar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.circle)){
          return par.circle;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri entity : par.entities){
        
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
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
            par.user,
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
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        if(par.user == null){
          throw new SSErr(SSErrE.parameterMissing);
        }
        
        return sqlFct.getCircleTypesCommonForUserAndEntity(par.user, par.entity);
      }else{
        return sqlFct.getCircleTypesForEntity(par.entity);
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
      final List<SSEntity> circleEntities;
      final List<SSEntity> circleUsers;
      
      if(par.withUserRestriction){
        
        if(
          sqlFct.isSystemCircle(par.circle) &&
          !SSStrU.equals(par.circle, pubCircleUri)){
          
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
              par.user,
              par.circle,
              par.withUserRestriction, //withUserRestriction,
              null))); //descPar
      
      SSEntityDescriberPar descPar;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(null);
        
        descPar.setOverallRating = true;
        descPar.setTags          = par.setTags;
        descPar.space            = par.tagSpace;
        descPar.circle           = par.circle;
      }else{
        descPar = null;
      }
      
      circleEntities =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            par.user,
            SSUri.getDistinctNotNullFromEntities(circle.entities),
            null, //types,
            descPar, //descPar
            par.withUserRestriction)); //withUserRestriction
      
      circle.entities.clear();
      circle.entities.addAll(circleEntities);
      
      descPar = new SSEntityDescriberPar(null);
      
      circleUsers =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            par.user,
            SSUri.getDistinctNotNullFromEntities(circle.users),
            null, //types,
            descPar, //descPar
            par.withUserRestriction)); //withUserRestriction
      
      circle.users.clear();
      circle.users.addAll(circleUsers);
      
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
      
      for(SSUri circleURI : circleUris){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          circles,
          circleGet(
            new SSCircleGetPar(
              par.user,
              circleURI,
              par.entityTypesToIncludeOnly,
              false,  //setTags
              null, //tagSpace
              par.withUserRestriction,
              par.invokeEntityHandlers)));
      }
      
      return circles;
      
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
      
      miscFct.addCircle(
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
      
      miscFct.addCircle(
        tmpPublicCircleURI,
        SSCircleE.pub, 
        true, 
        SSVocConf.systemUserUri);
      
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
      
      if(SSStrU.equals(par.user, SSVocConf.systemUserUri)){
        return;
      }
      
      if(
        !miscFct.canUserForEntityType(
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
          userUris, //entities
          null, //types
          null, //descPar
          false));  //withUserRestriction
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void circlesFromEntityEntitiesAdd(final SSCirclesFromEntityEntitiesAdd par) throws Exception{
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSEntity entityUserCircle :
        circlesGet(
          new SSCirclesGetPar(
            par.user,
            par.entity,
            null, //entityTypesToIncludeOnly
            false, //withUserRestriction
            true,  //withSystemCircles
            false))){ //invokeEntityHandlers
        
        circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            par.user,
            entityUserCircle.id,
            par.entities,  //entities
            false,  //withUserRestriction
            false)); //shouldCommit
      }
      
      dbSQL.commit(par.shouldCommit);
    
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          circlesFromEntityEntitiesAdd(par);
        }else{
          SSServErrReg.regErrThrow(error);
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
  }
}