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
package at.kc.tugraz.ss.serv.datatypes.learnep.impl;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserPublicSetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserSharePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpClientI;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpCircle;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpCircleDesc;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpDesc;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpTimelineState;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpTimelineStateDesc;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersionDesc;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionAddCirclePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionAddEntityPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentSetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionGetTimelineStatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionRemoveCirclePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionRemoveEntityPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionSetTimelineStatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionUpdateCirclePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionUpdateEntityPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpsGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpCreateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionAddCircleRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionAddEntityRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCreateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCurrentGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCurrentSetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionGetTimelineStateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionRemoveCircleRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionRemoveEntityRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionSetTimelineStateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionUpdateCircleRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionUpdateEntityRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionsGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpsGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.sql.SSLearnEpSQLFct;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.util.ArrayList;
import java.util.List;

public class SSLearnEpImpl extends SSServImplWithDBA implements SSLearnEpClientI, SSLearnEpServerI, SSEntityHandlerImplI{
  
//  private final SSLearnEpGraphFct       graphFct;
  private final SSLearnEpSQLFct         sqlFct;
  
  public SSLearnEpImpl(final SSServConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
//    graphFct  = new SSLearnEpGraphFct (this);
    sqlFct    = new SSLearnEpSQLFct   (this);
  }
  
  /* SSEntityHandlerImplI */
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSEntityEnum                                  entityType,
    final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par,
    final Boolean                                       shouldCommit) throws Exception{
  }
  
  @Override
  public Boolean setUserEntityPublic(
    final SSEntityUserPublicSetPar par, 
    final SSEntityEnum             entityType) throws Exception{

    return false;
  }
  
  @Override
  public Boolean shareUserEntity(
    final SSEntityUserSharePar par,
    final SSEntityEnum         entityType) throws Exception{
    
    return false;
  }
  
  @Override
  public Boolean addEntityToCircle(
    final SSUri        userUri, 
    final SSUri        circleUri, 
    final SSUri        entityUri, 
    final SSEntityEnum entityType) throws Exception{
    
    return false;
  }
  
  @Override
  public SSEntityDescA getDescForEntity(
    final SSEntityEnum    entityType,
    final SSUri           userUri, 
    final SSUri           entityUri, 
    final SSLabelStr      label,
    final Long            creationTime,
    final List<SSTag>     tags, 
    final SSRatingOverall overallRating,
    final List<SSUri>     discUris,
    final SSUri           author) throws Exception{
    
    if(SSEntityEnum.equals(entityType, SSEntityEnum.learnEp)){
      return SSLearnEpDesc.get(
        entityUri,
        label,
        creationTime, 
        author);
    }
    
    if(SSEntityEnum.equals(entityType, SSEntityEnum.learnEpVersion)){
      return SSLearnEpVersionDesc.get(
        entityUri,
        label,
        creationTime, 
        author);
    }
    
    if(SSEntityEnum.equals(entityType, SSEntityEnum.learnEpCircle)){
      return SSLearnEpCircleDesc.get(
        entityUri,
        label,
        creationTime, 
        author);
    }
    
    if(SSEntityEnum.equals(entityType, SSEntityEnum.learnEpEntity)){
      return SSLearnEpEntityDesc.get(
        entityUri,
        label,
        creationTime, 
        author);
    }
    
    if(SSEntityEnum.equals(entityType, SSEntityEnum.learnEpTimelineState)){
      return SSLearnEpTimelineStateDesc.get(
        entityUri,
        label,
        creationTime, 
        author);
    }
    
    return SSEntityDesc.get(entityUri, label, creationTime, tags, overallRating, discUris, author);
  }
  
  /* SSLearnEpClientI */
  @Override
  public void learnEpsGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLearnEpsGetRet.get(learnEpsGet(par), par.op));
  }
  
  @Override
  public void learnEpVersionsGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionsGetRet.get(learnEpVersionsGet(par), par.op));
  }
  
  @Override
  public void learnEpVersionGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionGetRet.get(learnEpVersionGet(par), par.op));
  }
  
  @Override
  public void learnEpVersionCreate(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionCreateRet.get(learnEpVersionCreate(par), par.op));
  }
  
  @Override
  public void learnEpVersionAddCircle(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionAddCircleRet.get(learnEpVersionAddCircle(par), par.op));
  }
  
  @Override
  public void learnEpVersionAddEntity(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionAddEntityRet.get(learnEpVersionAddEntity(par), par.op));
  }
  
  @Override
  public void learnEpCreate(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLearnEpCreateRet.get(learnEpCreate(par), par.op));
  }
  
  @Override
  public void learnEpVersionUpdateCircle(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionUpdateCircleRet.get(learnEpVersionUpdateCircle(par), par.op));
  }
  
  @Override
  public void learnEpVersionUpdateEntity(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionUpdateEntityRet.get(learnEpVersionUpdateEntity(par), par.op));
  }
  
  @Override
  public void learnEpVersionRemoveCircle(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionRemoveCircleRet.get(learnEpVersionRemoveCircle(par), par.op));
  }
  
  @Override
  public void learnEpVersionRemoveEntity(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionRemoveEntityRet.get(learnEpVersionRemoveEntity(par), par.op));
  }
  
  @Override
  public void learnEpVersionSetTimelineState(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionSetTimelineStateRet.get(learnEpVersionSetTimelineState(par), par.op));
  }
  
  @Override
  public void learnEpVersionGetTimelineState(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionGetTimelineStateRet.get(learnEpVersionGetTimelineState(par), par.op));
  }
  
  @Override
  public void learnEpVersionCurrentGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionCurrentGetRet.get(learnEpVersionCurrentGet(par), par.op));
  }
  
  @Override
  public void learnEpVersionCurrentSet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionCurrentSetRet.get(learnEpVersionCurrentSet(par), par.op));
  }
  
  /* SSLearnEpServerI */
  @Override
  public SSUri learnEpVersionCurrentSet(final SSServPar parA) throws Exception {
    
    try{
      
      final SSLearnEpVersionCurrentSetPar par = new SSLearnEpVersionCurrentSetPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.setLearnEpCurrentVersion(par.user, par.learnEpVersionUri);
          
      dbSQL.commit(par.shouldCommit);
      
      return par.learnEpVersionUri;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return learnEpVersionCurrentSet(parA);
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
  public SSUri learnEpVersionSetTimelineState(final SSServPar parA) throws Exception {
    
    try{
      
      final SSLearnEpVersionSetTimelineStatePar par                     = new SSLearnEpVersionSetTimelineStatePar(parA);
      final SSUri                               learnEpTimelineStateUri = sqlFct.createLearnEpTimelineStateUri();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        learnEpTimelineStateUri,
        SSLabelStr.get(learnEpTimelineStateUri.toString()),
        SSEntityEnum.learnEpTimelineState,
        false);
      
      sqlFct.setLearnEpVersionTimelineState(
        learnEpTimelineStateUri, 
        par.learnEpVersionUri, 
        par.startTime, 
        par.endTime);
      
      dbSQL.commit(par.shouldCommit);
      
       return learnEpTimelineStateUri;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return learnEpVersionSetTimelineState(parA);
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
  public Boolean learnEpVersionRemoveCircle(final SSServPar parA) throws Exception {
    
    try{
      
      final SSLearnEpVersionRemoveCirclePar par = new SSLearnEpVersionRemoveCirclePar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.removeCircle(par.learnEpCircleUri);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return learnEpVersionRemoveCircle(parA);
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
  public Boolean learnEpVersionRemoveEntity(final SSServPar parA) throws Exception {
    
    try{
      
      final SSLearnEpVersionRemoveEntityPar par = new SSLearnEpVersionRemoveEntityPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.removeEntity(par.learnEpEntityUri);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return learnEpVersionRemoveEntity(parA);
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
  public Boolean learnEpVersionUpdateCircle(final SSServPar parA) throws Exception {
    
    try{
      
      final SSLearnEpVersionUpdateCirclePar par = new SSLearnEpVersionUpdateCirclePar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityLabelSet(
        par.learnEpCircleUri, 
        par.label, 
        false);
      
      sqlFct.updateCircle(
        par.learnEpCircleUri, 
        par.label, 
        par.xLabel,
        par.yLabel,
        par.xR, 
        par.yR, 
        par.xC, 
        par.yC);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return learnEpVersionUpdateCircle(parA);
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
  public Boolean learnEpVersionUpdateEntity(SSServPar parA) throws Exception {
    
    try{
      
      final SSLearnEpVersionUpdateEntityPar par = new SSLearnEpVersionUpdateEntityPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        par.entityUri,
        SSLabelStr.get(SSStrU.toString(par.entityUri)), 
        SSEntityEnum.entity,
        false);
      
      sqlFct.updateEntity(par.learnEpEntityUri, par.entityUri, par.x, par.y);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return learnEpVersionUpdateEntity(parA);
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
  public SSUri learnEpCreate(final SSServPar parA) throws Exception {
    
    try{
      final SSLearnEpCreatePar  par        = new SSLearnEpCreatePar(parA);
      final SSUri               learnEpUri = sqlFct.createLearnEpUri();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        learnEpUri,
        par.label,
        SSEntityEnum.learnEp,
        false);
      
      sqlFct.createLearnEp(learnEpUri, par.user, par.space);
      
      dbSQL.commit(par.shouldCommit);
      
      return learnEpUri;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return learnEpCreate(parA);
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
  public SSUri learnEpVersionCreate(final SSServPar parA) throws Exception {
    
    try{
      final SSLearnEpVersionCreatePar par                = new SSLearnEpVersionCreatePar(parA);
      final SSUri                     learnEpVersionUri  = sqlFct.createLearnEpVersionUri();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        learnEpVersionUri,
        SSLabelStr.get(learnEpVersionUri.toString()),
        SSEntityEnum.learnEpVersion,
        false);
      
      sqlFct.createLearnEpVersion(learnEpVersionUri, par.learnEpUri);
      
      dbSQL.commit(par.shouldCommit);
      
      return learnEpVersionUri;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return learnEpVersionCreate(parA);
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
  public SSUri learnEpVersionAddCircle(final SSServPar parA) throws Exception {
    
    try{
      final SSLearnEpVersionAddCirclePar par        = new SSLearnEpVersionAddCirclePar(parA);
      final SSUri                        circleUri  = sqlFct.createLearnEpCircleUri();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        circleUri,
        par.label,
        SSEntityEnum.learnEpCircle,
        false);
      
      sqlFct.addCircleToLearnEpVersion(
        circleUri,
        par.learnEpVersionUri,
        par.label,
        par.xLabel,
        par.yLabel,
        par.xR,
        par.yR,
        par.xC,
        par.yC);
      
      dbSQL.commit(par.shouldCommit);
      
      return circleUri;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return learnEpVersionAddCircle(parA);
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
  public SSUri learnEpVersionAddEntity(SSServPar parA) throws Exception {
    
    try{
      final SSLearnEpVersionAddEntityPar par               = new SSLearnEpVersionAddEntityPar(parA);
      final SSUri                        learnEpEntityUri  = sqlFct.createLearnEpEntityUri();

      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        learnEpEntityUri,
        SSLabelStr.get(learnEpEntityUri.toString()),
        SSEntityEnum.learnEpEntity,
        false);
      
      SSServCaller.entityAdd(
        par.user,
        par.entityUri,
        SSLabelStr.get(par.entityUri.toString()),
        SSEntityEnum.entity,
        false);
      
      sqlFct.addEntityToLearnEpVersion(
        learnEpEntityUri, 
        par.learnEpVersionUri, 
        par.entityUri, 
        par.x, 
        par.y);
      
      dbSQL.commit(par.shouldCommit);
      
      return learnEpEntityUri;
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return learnEpVersionAddEntity(parA);
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
  public SSLearnEpVersion learnEpVersionGet(SSServPar parI) throws Exception {
    
    SSLearnEpVersionGetPar par            = new SSLearnEpVersionGetPar(parI);
    SSLearnEpVersion       learnEpVersion = null;
    
    try{
      learnEpVersion           = sqlFct.getLearnEpVersion(par.learnEpVersionUri);
      learnEpVersion.timestamp = SSServCaller.entityCreationTimeGet(learnEpVersion.learnEpVersionUri).toString();
      
      for(SSLearnEpCircle circle : learnEpVersion.circles){
        circle.label = SSServCaller.entityLabelGet(circle.learnEpCircleUri);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    return learnEpVersion;
  }
  
  @Override
  public List<SSLearnEpVersion> learnEpVersionsGet(SSServPar parI) throws Exception {
    
    SSLearnEpVersionsGetPar  par             = new SSLearnEpVersionsGetPar(parI);
    List<SSLearnEpVersion>   learnEpVersions = null;
    
    try{
      
      learnEpVersions = sqlFct.getLearnEpVersions(par.learnEpUri);
      
      for(SSLearnEpVersion learnEpVersion: learnEpVersions){
        
        learnEpVersion.timestamp = SSServCaller.entityCreationTimeGet(learnEpVersion.learnEpVersionUri).toString();
        
        for(SSLearnEpCircle circle : learnEpVersion.circles){
          circle.label = SSServCaller.entityLabelGet(circle.learnEpCircleUri);
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    return learnEpVersions;
  }
  
  @Override
  public List<SSLearnEp> learnEpsGet(SSServPar parI) throws Exception {
    
    SSLearnEpsGetPar par      = new SSLearnEpsGetPar(parI);
    List<SSLearnEp>  learnEps = new ArrayList<SSLearnEp>();
    
    try{
      
      learnEps = sqlFct.getLearnEpsForUser(par.user);
      
      for(SSLearnEp learnEp : learnEps){
        learnEp.label = SSServCaller.entityLabelGet(learnEp.learnEpUri);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    return learnEps;
  }
  
  @Override
  public SSLearnEpVersion learnEpVersionCurrentGet(SSServPar parI) throws Exception {
    
    SSLearnEpVersionCurrentGetPar par    = new SSLearnEpVersionCurrentGetPar(parI);
    SSLearnEpVersion              result = null;

    try{
      result = SSServCaller.getLearnEpVersion(par.user, sqlFct.getLearnEpCurrentVersionUri(par.user));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    return result;
  }
  
  @Override
  public SSLearnEpTimelineState learnEpVersionGetTimelineState(SSServPar parI) throws Exception {
    
    SSLearnEpVersionGetTimelineStatePar par    = new SSLearnEpVersionGetTimelineStatePar(parI);
    SSLearnEpTimelineState              result = null;
    
    try{
      result = sqlFct.getLearnEpVersionTimelineState(par.learnEpVersionUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    return result;
  }
}