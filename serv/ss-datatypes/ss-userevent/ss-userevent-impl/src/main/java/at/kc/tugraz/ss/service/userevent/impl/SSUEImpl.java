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

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityDescriberI;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import at.kc.tugraz.ss.service.userevent.api.*;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
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
import java.util.*;
import sss.serv.err.datatypes.SSErrE;

public class SSUEImpl extends SSServImplWithDBA implements SSUEClientI, SSUEServerI, SSEntityHandlerImplI, SSEntityDescriberI{
  
//  private final SSUEGraphFct graphFct;
  private final SSUESQLFct   sqlFct;
  private final SSUEMiscFct  fct;
  
  public SSUEImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
//    graphFct = new SSUEGraphFct(this);
    this.sqlFct   = new SSUESQLFct  (this);
    this.fct      = new SSUEMiscFct ();
  }
  
  /* SSEntityHandlerImplI */
  
  @Override
  public Boolean copyUserEntity(
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
  public Boolean setUserEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri, 
    final SSEntityE   entityType,
    final SSUri          publicCircleUri) throws Exception{

    return false;
  }
  
  @Override
  public void shareUserEntity(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          entityCircleUri,
    final SSEntityE      entityType,
    final Boolean        saveActivity) throws Exception{
  }
  
  @Override
  public Boolean addEntityToCircle(
    final SSUri        userUri, 
    final SSUri        circleUri, 
    final SSUri        entityUri, 
    final SSEntityE entityType) throws Exception{
    
    return false;
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
  public SSEntity getDescForEntity(
    final SSEntityDescGetPar par,
    final SSEntity           desc) throws Exception{
    
    if(par.getUEs){
      
      desc.uEs.addAll(
        SSServCaller.uEsGet(
          par.user, 
          par.user, 
          par.entity, 
          null, 
          null,
          null));
    }
    
    return desc;
  }
  
  @Override
  public void uECountGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSUECountGetRet.get(uECountGet(parA), parA.op));
  }
  
  //TODO dtheiler: count via db then: count(p.catId) as the_count 
  @Override
  public Integer uECountGet(final SSServPar parA) throws Exception {
    
    try{
      
      final SSUECountGetPar par = new SSUECountGetPar(parA);
      
      return sqlFct.getUEs(
        par.forUser,
        par.entity,
        par.type,
        par.startTime,
        par.endTime).size();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void uEGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSUEGetRet.get(uEGet(parA), parA.op));
  }
    
  @Override
  public void uEsGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSUEsGetRet.get(uEsGet(parA), parA.op));
  }
  
  @Override
  public void uEAdd(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSUEAddRet.get(uEAdd(parA), parA.op));
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return uEsRemove(parA);
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
  public Boolean uEAddAtCreationTime(final SSServPar parA) throws Exception{
    
    try{
      final SSUEAddAtCreationTimePar par   = new SSUEAddAtCreationTimePar(parA);
      final SSUri                    ueUri = SSUEMiscFct.createUEUri();
    
      try{
        SSServCaller.entityUserCanRead(par.user, par.entity);
      }catch(Exception error){
        SSServErrReg.reset();
        SSLogU.warn("user is not allowed to add user event to entity");
        return false;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntityToPrivCircleAdd(
        par.user, 
        ueUri, 
        SSEntityE.userEvent, 
        null, 
        null, 
        par.creationTime, 
        false);
      
      SSServCaller.entityEntityToPrivCircleAdd(
        par.user,
        par.entity,
        SSEntityE.entity,
        null,
        null,
        null,
        false);
      
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return uEAddAtCreationTime(parA);
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
  public Boolean uEAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSUEAddPar par   = new SSUEAddPar(parA);
      final SSUri      ueUri = SSUEMiscFct.createUEUri();
      
      if(par.entity != null){

        try{
          SSServCaller.entityUserCanRead(par.user, par.entity);
        }catch(Exception error){
          SSServErrReg.reset();
          SSLogU.warn("user is not allowed to add user event to entity");
          return false;
        }
      }
      
      if(par.entity == null){
        par.entity = SSUri.get(SSVoc.sssUri);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
       SSServCaller.entityEntityToPrivCircleAdd(
        par.user,
        ueUri,
        SSEntityE.userEvent,
        null,
        null,
        null,
        false);
        
      SSServCaller.entityEntityToPrivCircleAdd(
        par.user, 
        par.entity, 
        SSEntityE.entity, 
        null, 
        null, 
        null, 
        false);

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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return uEAdd(parA);
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
  public SSUE uEGet(final SSServPar parA) throws Exception {
    
    try{
      final SSUEGetPar par = new SSUEGetPar(parA);
      
      return sqlFct.getUE(par.ue);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  //TODO dtheiler: restrict user event retrival for calling user
  @Override
  public List<SSUE> uEsGet(final SSServPar parA) throws Exception {
    
    try{
      
      final SSUEsGetPar par = new SSUEsGetPar(parA);
      
      return sqlFct.getUEs(
        par.forUser,
        par.entity,
        par.type,
        par.startTime,
        par.endTime);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}