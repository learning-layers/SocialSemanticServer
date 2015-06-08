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
 package at.kc.tugraz.ss.service.userevent.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivEntityAddPar;
import at.kc.tugraz.ss.circle.serv.SSCircleServ;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.userevent.api.*;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddAtCreationTimePar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUECountGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsRemovePar;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEAddRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUECountGetRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEGetRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEsGetRet;
import at.kc.tugraz.ss.service.userevent.impl.fct.misc.SSUEMiscFct;
import at.kc.tugraz.ss.service.userevent.impl.fct.sql.SSUESQLFct;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.*;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;

public class SSUEImpl 
extends SSServImplWithDBA 
implements 
  SSUEClientI, 
  SSUEServerI, 
  SSEntityHandlerImplI, 
  SSEntityDescriberI, 
  SSUsersResourcesGathererI{
  
  private final SSUESQLFct   sqlFct;
  private final SSUEMiscFct  fct;
  
  public SSUEImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct   = new SSUESQLFct  (this);
    this.fct      = new SSUEMiscFct ();
  }
  
  @Override
  public void getUsersResources(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> usersResources) throws Exception{
    
    final List<SSUEE> ueTypes = new ArrayList<>();
    
    SSUri userUri;
    
    ueTypes.add(SSUEE.evernoteNotebookUpdate);
    ueTypes.add(SSUEE.evernoteNotebookFollow);
    ueTypes.add(SSUEE.evernoteNoteUpdate);
    ueTypes.add(SSUEE.evernoteNoteDelete);
    ueTypes.add(SSUEE.evernoteNoteShare);
    ueTypes.add(SSUEE.evernoteReminderDone);
    ueTypes.add(SSUEE.evernoteReminderCreate);
    ueTypes.add(SSUEE.evernoteResourceAdd);
      
    for(String user : allUsers){
      
      userUri = SSUri.get(user);
      
      for(SSUE ue : SSServCaller.uEsGet(userUri, userUri, null, ueTypes, null, null)){
        
        if(usersResources.containsKey(user)){
          usersResources.get(user).add(ue.entity);
        }else{
          
          final List<SSUri> resourceList = new ArrayList<>();
          
          resourceList.add(ue.entity);
          
          usersResources.put(user, resourceList);
        }
      }
    }
    
    for(Map.Entry<String, List<SSUri>> resourcesPerUser : usersResources.entrySet()){
      SSStrU.distinctWithoutNull2(resourcesPerUser.getValue());
    }
  }
  
  @Override
  public Boolean copyEntity(
    final SSUri        user,
    final List<SSUri>  users,
    final SSUri        entity,
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception{
    
    return false;
  }
  
  @Override
  public List<SSUri> getParentEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    return new ArrayList<>();
  }
  
  @Override
  public List<SSUri> getSubEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{

    return null;
  }
  
  @Override
  public Boolean setEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri, 
    final SSEntityE   entityType,
    final SSUri          publicCircleUri) throws Exception{

    return false;
  }
  
  @Override
  public void shareEntityWithUsers(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          entityCircleUri,
    final SSEntityE      entityType,
    final Boolean        saveActivity) throws Exception{
  }
  
  @Override
  public void addEntityToCircle(
    final SSUri        userUri, 
    final SSUri        circleUri, 
    final List<SSUri>  circleUsers,
    final SSUri        entityUri, 
    final SSEntityE    entityType) throws Exception{
  }  
  
  @Override
  public void addUsersToCircle(
    final SSUri        user,
    final List<SSUri>  users,
    final SSEntityCircle        circle) throws Exception{
    
    
    
  }
  
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSUri       userUri, 
    final SSEntityE   entityType,
    final SSUri       entityUri,
    final Boolean     removeUserTags,
    final Boolean     removeUserRatings,
    final Boolean     removeFromUserColls,
    final Boolean     removeUserLocations) throws Exception{
  }
  
  @Override
  public SSEntity getUserEntity(final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setUEs){
        
        par.entity.uEs.addAll(
          SSServCaller.uEsGet(
            par.user,
            par.user,
            par.entity.id,
            null,
            null,
            null));
      }
      
      return par.entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void uECountGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSUECountGetRet.get(uECountGet(parA), parA.op), parA.op);
  }
  
  //TODO dtheiler: count via db then: count(p.catId) as the_count 
  @Override
  public Integer uECountGet(final SSServPar parA) throws Exception {
    
    try{
      
      final SSUECountGetPar par = SSUECountGetPar.get(parA);
      
      return sqlFct.getUEs(
        par.forUser,
        par.entity,
        SSUEE.asListWithoutEmptyAndNull(par.type),
        par.startTime,
        par.endTime).size();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void uEGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSUEGetRet.get(uEGet(parA), parA.op), parA.op);
  }
    
  @Override
  public void uEsGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSUEsGetRet.get(uEsGet(parA), parA.op), parA.op);
  }
  
  @Override
  public void uEAdd(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSUEAddRet.get(uEAdd(parA), parA.op), parA.op);
  }
  
  @Override
  public Boolean uEsRemove (final SSServPar parA) throws Exception{
    
    try{
      final SSUEsRemovePar par   = new SSUEsRemovePar(parA);
    
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUE ue : sqlFct.getUEs(par.user, par.entity, null, null, null)){
        SSServCaller.entityRemove(ue.id, false);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return uEsRemove(parA);
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
  public Boolean uEAddAtCreationTime(final SSServPar parA) throws Exception{
    
    try{
      final SSUEAddAtCreationTimePar par   = new SSUEAddAtCreationTimePar(parA);
      final SSUri                    ueUri = SSServCaller.vocURICreate();
    
      try{
        SSServCallerU.canUserReadEntity(par.user, par.entity);
      }catch(Exception error){
        SSServErrReg.reset();
        SSLogU.warn("user is not allowed to add user event to entity");
        return false;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
        new SSCirclePrivEntityAddPar(
          null,
          null,
          par.user,
          ueUri,
          SSEntityE.userEvent,
          null,
          null,
          par.creationTime,
          false));
      
      ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
          new SSCirclePrivEntityAddPar(
            null,
            null,
        par.user,
        par.entity,
        SSEntityE.entity,
        null,
        null,
        null,
        false));
      
      sqlFct.addUE(
        ueUri, 
        par.user, 
        par.entity, 
        par.type, 
        par.content);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return uEAddAtCreationTime(parA);
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
  public Boolean uEAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSUEAddPar par   = SSUEAddPar.get(parA);
      final SSUri      ueUri = SSServCaller.vocURICreate();
      
      if(par.entity != null){

        try{
          SSServCallerU.canUserReadEntity(par.user, par.entity);
        }catch(Exception error){
          SSServErrReg.reset();
          SSLogU.warn("user is not allowed to add user event to entity");
          return false;
        }
      }
      
      if(par.entity == null){
        par.entity = SSUri.get(SSVocConf.sssUri);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
       ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
          new SSCirclePrivEntityAddPar(
            null,
            null,
        par.user,
        ueUri,
        SSEntityE.userEvent,
        null,
        null,
        null,
        false));
        
      ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
          new SSCirclePrivEntityAddPar(
            null,
            null,
        par.user, 
        par.entity, 
        SSEntityE.entity, 
        null, 
        null, 
        null, 
        false));

      sqlFct.addUE(
        ueUri,
        par.user,
        par.entity,
        par.type,
        par.content);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return uEAdd(parA);
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
  public SSUE uEGet(final SSServPar parA) throws Exception {
    
    try{
      final SSUEGetPar par = SSUEGetPar.get(parA);
      
      return sqlFct.getUE(par.uE);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  //TODO dtheiler: restrict user event retrival for calling user
  @Override
  public List<SSUE> uEsGet(final SSServPar parA) throws Exception {
    
    try{
      
      final SSUEsGetPar par = SSUEsGetPar.get(parA);
      
      return sqlFct.getUEs(
        par.forUser,
        par.entity,
        par.types,
        par.startTime,
        par.endTime);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}