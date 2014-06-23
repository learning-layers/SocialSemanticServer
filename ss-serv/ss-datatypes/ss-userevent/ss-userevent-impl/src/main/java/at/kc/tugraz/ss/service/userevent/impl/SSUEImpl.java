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

import at.kc.tugraz.socialserver.utils.SSLinkU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.userevent.api.*;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEDesc;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddAtCreationTimePar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEAddRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEGetRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEsGetRet;
import at.kc.tugraz.ss.service.userevent.impl.fct.misc.SSUEMiscFct;
import at.kc.tugraz.ss.service.userevent.impl.fct.sql.SSUESQLFct;
import java.util.*;

public class SSUEImpl extends SSServImplWithDBA implements SSUEClientI, SSUEServerI, SSEntityHandlerImplI{
  
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
  public SSUri copyUserEntity(
    final SSUri        user,
    final SSUri        forUser,
    final SSUri        entity,
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception{
    
    return null;
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
  public Boolean shareUserEntity(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          entityCircleUri,
    final SSEntityE   entityType) throws Exception{
    
    return false;
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
  public SSEntityDescA getDescForEntity(
    final SSEntityE       entityType, 
    final SSUri           userUri, 
    final SSUri           entityUri, 
    final SSLabel         label,
    final Long            creationTime,
    final List<String>    tags, 
    final SSEntityA       overallRating,
    final List<SSUri>     discUris,
    final SSUri           author) throws Exception{
    
    if(!SSEntityE.equals(entityType, SSEntityE.userEvent)){
      
      return SSEntityDesc.get(
        entityUri,
        label,
        creationTime,
        tags,
        overallRating,
        discUris,
        author);
    }
    
    return SSUEDesc.get(
      entityUri,
      label,
      creationTime,
      author,
      overallRating,
      tags,
      discUris);
  }
  
  /* SSUserEventClientI  */
  
  @Override
  public void uEGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSUEGetRet.get(uEGet(par), par.op));
  }
    
  @Override
  public void uEsGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSUEsGetRet.get(uEsGet(par), par.op));
  }
  
  @Override
  public void uEAdd(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSUEAddRet.get(uEAdd(par), par.op));
  }
  
  /* SSUserEventServerI */
  @Override
  public Boolean uEAddAtCreationTime(final SSServPar parA) throws Exception{
    
    SSUEAddAtCreationTimePar par   = new SSUEAddAtCreationTimePar(parA);
    SSUri                    ueUri;
    
    try{
      ueUri = SSUEMiscFct.createUEUri();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAddAtCreationTime(
        par.user,
        ueUri,
        SSLabel.get(ueUri.toString()),
        par.creationTime,
        SSEntityE.userEvent,
        null,
        false);
      
      SSServCaller.entityAdd(
        par.user,
        par.entity,
        SSLabel.get(par.entity.toString()),
        SSEntityE.entity,
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
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return uEAddAtCreationTime(parA);
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
  public Boolean uEAdd(final SSServPar parA) throws Exception{
    
    final SSUEAddPar par   = new SSUEAddPar(parA);
    SSUri            ueUri;
    
    try{
      if(par.entity == null){
        par.entity = SSUri.get(SSLinkU.dummyUri);
      }
      
      if(SSStrU.isEmpty(par.content)){
        par.content = SSStrU.empty;
      }
      
      ueUri = SSUEMiscFct.createUEUri();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        par.entity,
        SSLabel.get(par.entity.toString()),
        SSEntityE.entity,
        null,
        false);
      
      SSServCaller.entityAdd(
        par.user,
        ueUri,
        SSLabel.get(ueUri.toString()),
        SSEntityE.userEvent,
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
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return uEAdd(parA);
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
  public SSUE uEGet(SSServPar parI) throws Exception {
    
    SSUEGetPar par     = new SSUEGetPar(parI);
    SSUE       result  = null;
    
    try{
      result = sqlFct.getUE(par.ue);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    return result;
  }

  //TODO dtheiler: restrict user event retrival for calling user
  @Override
  public List<SSUE> uEsGet(final SSServPar parA) throws Exception {
    
    final SSUEsGetPar par = new SSUEsGetPar(parA);
    
    try{
      
      return sqlFct.getUEs(
        par.forUser,
        par.entity,
        par.type,
        par.startTime,
        par.endTime);
      
//      return SSUEMiscFct.filterUEs(uEs, par.startTime, par.endTime);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}