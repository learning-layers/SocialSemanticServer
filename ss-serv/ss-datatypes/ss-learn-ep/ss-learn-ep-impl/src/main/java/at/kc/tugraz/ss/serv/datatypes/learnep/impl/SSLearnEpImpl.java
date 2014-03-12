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

import at.kc.tugraz.socialserver.utils.SSMethU;
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
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.lang.reflect.Method;
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
  
  /****** SSEntityHandlerImplI ******/
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSEntityEnum                                  entityType,
    final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par,
    final Boolean                                       shouldCommit) throws Exception{
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
  
  /****** SSServRegisterableImplI ******/
  
  @Override
  public List<SSMethU> publishClientOps() throws Exception{
    
    List<SSMethU> clientOps = new ArrayList<SSMethU>();
      
    Method[] methods = SSLearnEpClientI.class.getMethods();
    
    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }
    
    return clientOps;
  }
  
  @Override
  public List<SSMethU> publishServerOps() throws Exception{
    
    List<SSMethU> serverOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSLearnEpServerI.class.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }
    
    return serverOps;
  }
  
  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSLearnEpClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }
  
  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSLearnEpServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }
  
  /****** SSLearnEpClientI ******/
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
  
  /****** SSLearnEpServerI *********************************************/
  /*********************************************************************/
  @Override
  public SSUri learnEpVersionCurrentSet(SSServPar parI) throws Exception {
    
    SSLearnEpVersionCurrentSetPar par = new SSLearnEpVersionCurrentSetPar(parI);
    
    try{
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.setLearnEpCurrentVersion(par.user, par.learnEpVersionUri);
          
      dbSQL.commit(par.shouldCommit);
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }

    return par.learnEpVersionUri;
  }
   
  @Override
  public SSUri learnEpVersionSetTimelineState(SSServPar parI) throws Exception {
    
    SSLearnEpVersionSetTimelineStatePar par                     = new SSLearnEpVersionSetTimelineStatePar(parI);
    SSUri                               learnEpTimelineStateUri = null;
    
    try{
      
      learnEpTimelineStateUri = sqlFct.createLearnEpTimelineStateUri();
      
      SSServCaller.addEntity(
        par.user,
        learnEpTimelineStateUri,
        SSLabelStr.get(learnEpTimelineStateUri.toString()),
        SSEntityEnum.learnEpTimelineState);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.setLearnEpVersionTimelineState(learnEpTimelineStateUri, par.learnEpVersionUri, par.startTime, par.endTime);
      
      dbSQL.commit(par.shouldCommit);
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
    
    return learnEpTimelineStateUri;
  }
  
  @Override
  public Boolean learnEpVersionRemoveCircle(SSServPar parI) throws Exception {
    
    SSLearnEpVersionRemoveCirclePar par = new SSLearnEpVersionRemoveCirclePar(parI);
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.removeCircle(par.learnEpCircleUri);
      
      dbSQL.commit(par.shouldCommit);
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
    
    return true;
  }
  
  @Override
  public Boolean learnEpVersionRemoveEntity(SSServPar parI) throws Exception {
    
    SSLearnEpVersionRemoveEntityPar par = new SSLearnEpVersionRemoveEntityPar(parI);
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.removeEntity(par.learnEpEntityUri);
      
      dbSQL.commit(par.shouldCommit);
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
    
    return true;
  }
  
  @Override
  public Boolean learnEpVersionUpdateCircle(SSServPar parI) throws Exception {
    
    SSLearnEpVersionUpdateCirclePar par = new SSLearnEpVersionUpdateCirclePar(parI);
    
    try{
      SSServCaller.setEntityLabel(
        par.learnEpCircleUri, 
        par.label, 
        par.shouldCommit);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.updateCircle(par.learnEpCircleUri, par.label, par.xLabel, par.yLabel, par.xR, par.yR, par.xC, par.yC);
      
      dbSQL.commit(par.shouldCommit);
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
    
    return true;
  }
  
  @Override
  public Boolean learnEpVersionUpdateEntity(SSServPar parI) throws Exception {
    
    SSLearnEpVersionUpdateEntityPar par = new SSLearnEpVersionUpdateEntityPar(parI);
    
    try{
      SSServCaller.addEntity(
        par.user,
        par.entityUri, 
        SSLabelStr.get(SSStrU.toString(par.entityUri)), 
        SSEntityEnum.entity);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.updateEntity(par.learnEpEntityUri, par.entityUri, par.x, par.y);
      
      dbSQL.commit(par.shouldCommit);
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
    
    return true;
  }
  
  @Override
  public SSUri learnEpCreate(SSServPar parI) throws Exception {
    
    SSLearnEpCreatePar  par        = new SSLearnEpCreatePar(parI);
    SSUri               learnEpUri = null;
    
    try{
      learnEpUri = sqlFct.createLearnEpUri();
      
      SSServCaller.addEntity(
        par.user,
        learnEpUri,
        par.label,
        SSEntityEnum.learnEp);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.createLearnEp(learnEpUri, par.user, par.space);
      
      dbSQL.commit(par.shouldCommit);
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
    
    return learnEpUri;
  }
  
  @Override
  public SSUri learnEpVersionCreate(SSServPar parI) throws Exception {
    
    SSLearnEpVersionCreatePar par               = new SSLearnEpVersionCreatePar(parI);
    SSUri                     learnEpVersionUri = null;
    
    try{
      
      learnEpVersionUri = sqlFct.createLearnEpVersionUri();
      
      SSServCaller.addEntity(
        par.user,
        learnEpVersionUri,
        SSLabelStr.get(learnEpVersionUri.toString()),
        SSEntityEnum.learnEpVersion);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.createLearnEpVersion(learnEpVersionUri, par.learnEpUri);
      
      dbSQL.commit(par.shouldCommit);
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
    
    return learnEpVersionUri;
  }
  
  @Override
  public SSUri learnEpVersionAddCircle(SSServPar parI) throws Exception {
    
    SSLearnEpVersionAddCirclePar par       = new SSLearnEpVersionAddCirclePar(parI);
    SSUri                        circleUri = null;
    
    try{
      circleUri = sqlFct.createLearnEpCircleUri();
      
      SSServCaller.addEntity(
        par.user,
        circleUri,
        par.label,
        SSEntityEnum.learnEpCircle);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addCircleToLearnEpVersion(circleUri, par.learnEpVersionUri, par.label, par.xLabel, par.yLabel, par.xR, par.yR, par.xC, par.yC);
      
      dbSQL.commit(par.shouldCommit);
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
    
    return circleUri;
  }
  
  @Override
  public SSUri learnEpVersionAddEntity(SSServPar parI) throws Exception {
    
    SSLearnEpVersionAddEntityPar par              = new SSLearnEpVersionAddEntityPar(parI);
    SSUri                        learnEpEntityUri = null;
    
    try{
      
      learnEpEntityUri = sqlFct.createLearnEpEntityUri();
      
      SSServCaller.addEntity(
        par.user,
        learnEpEntityUri,
        SSLabelStr.get(learnEpEntityUri.toString()),
        SSEntityEnum.learnEpEntity);
      
      SSServCaller.addEntity(
        par.user,
        par.entityUri,
        SSLabelStr.get(par.entityUri.toString()),
        SSEntityEnum.entity);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addEntityToLearnEpVersion(learnEpEntityUri, par.learnEpVersionUri, par.entityUri, par.x, par.y);
      
      dbSQL.commit(par.shouldCommit);
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
    
    return learnEpEntityUri;
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