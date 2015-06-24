/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
 package at.kc.tugraz.ss.service.rating.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivEntityAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubEntityAddPar;
import at.kc.tugraz.ss.service.rating.impl.fct.userraltionsgathering.SSRatingUserRelationGathererFct;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingOverallGetPar;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingGetPar;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingSetPar;
import at.kc.tugraz.ss.service.rating.api.*;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import at.kc.tugraz.ss.service.rating.datatypes.SSRating;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.rating.datatypes.ret.SSRatingOverallGetRet;
import at.kc.tugraz.ss.service.rating.datatypes.ret.SSRatingGetRet;
import at.kc.tugraz.ss.service.rating.datatypes.ret.SSRatingSetRet;
import at.kc.tugraz.ss.service.rating.impl.fct.sql.SSRatingSQLFct;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingsRemovePar;
import at.kc.tugraz.ss.service.rating.impl.fct.activity.SSRatingActivityFct;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;

public class SSRatingImpl 
extends SSServImplWithDBA 
implements 
  SSRatingClientI, 
  SSRatingServerI, 
  SSEntityHandlerImplI, 
  SSEntityDescriberI, 
  SSUserRelationGathererI{
 
  private final SSRatingSQLFct   sqlFct;
    
  public SSRatingImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    sqlFct   = new SSRatingSQLFct   (this);
  }

  @Override
  public void getUserRelations(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> userRelations) throws Exception{
    
    final Map<String, List<SSUri>> usersPerEntity = new HashMap<>();
    List<SSRating>                 userRatings;
    
    for(String user : allUsers){
      
      final SSUri userUri = SSUri.get(user);
      
      userRatings = sqlFct.getUserRatings(userUri);
      
      for(SSRating rating : userRatings){
        SSRatingUserRelationGathererFct.addUserForEntity(rating, usersPerEntity);
      }
    }
    
    SSRatingUserRelationGathererFct.addUserRelations(userRelations, usersPerEntity);
    
    for(Map.Entry<String, List<SSUri>> usersPerUser : userRelations.entrySet()){
      SSStrU.distinctWithoutNull2(usersPerUser.getValue());
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
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSUri       userUri, 
    final SSEntityE   entityType,
    final SSUri       entityUri,
    final Boolean     removeUserTags,
    final Boolean     removeUserRatings,
    final Boolean     removeFromUserColls,
    final Boolean     removeUserLocations) throws Exception{
    
    if(!removeUserRatings){
      return;
    }
    
    try{
      
      ratingsRemove(
        new SSRatingsRemovePar(
          null, 
          null, 
          userUri, 
          entityUri, 
          false));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
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
  public SSEntity getUserEntity(final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setOverallRating){
        
        par.entity.overallRating =
          ratingOverallGet(
            new SSRatingOverallGetPar(
              null, 
              null, 
              par.user, 
              par.entity.id));
      }
      
      return par.entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void ratingSet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSRatingSetPar par = (SSRatingSetPar) parA.getFromJSON(SSRatingSetPar.class);
    
    sSCon.writeRetFullToClient(SSRatingSetRet.get(ratingSet(par)));
    
//    saveRatingUserSetUE(par);
    
    SSRatingActivityFct.rateEntity(par);
  }
  
  @Override
  public Boolean ratingSet(final SSRatingSetPar par) throws Exception {
    
    try{
      final Boolean            existsEntity = SSServCaller.entityExists(par.entity);
      final SSUri              ratingUri;
      
      if(existsEntity){
        
        switch(SSServCaller.entityGet(par.entity).type){
          case entity: break;
          default: SSServCallerU.canUserReadEntity(par.user, par.entity);
        }
      }
      
      if(sqlFct.hasUserRatedEntity(par.user, par.entity)){
        return true;
      }
      
      ratingUri = SSServCaller.vocURICreate();
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(!existsEntity){
        
        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlePrivEntityAdd(
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
        
        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlePubEntityAdd(
          new SSCirclePubEntityAddPar(
            null,
            null,
            par.user,
            par.entity,
            false,
            SSEntityE.entity,
            null,
            null,
            null));
        
      }else{
        
        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlePrivEntityAdd(
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
      }
      
      ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlePrivEntityAdd(
        new SSCirclePrivEntityAddPar(
          null,
          null,
          par.user,
          ratingUri,
          SSEntityE.rating,
          null,
          null,
          null,
          false));
      
      sqlFct.rateEntityByUser(
        ratingUri, 
        par.user,
        par.entity, 
        par.value);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return ratingSet(par);
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
  public void ratingGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSRatingGetPar par = (SSRatingGetPar) parA.getFromJSON(SSRatingGetPar.class);
    
    sSCon.writeRetFullToClient(SSRatingGetRet.get(ratingGet(par)));
  }
  
  @Override
  public Integer ratingGet(final SSRatingGetPar par) throws Exception {
    
    try{
      return sqlFct.getUserRating(par.user, par.entity);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void ratingOverallGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSRatingOverallGetPar par = (SSRatingOverallGetPar) parA.getFromJSON(SSRatingOverallGetPar.class);
    
    sSCon.writeRetFullToClient(SSRatingOverallGetRet.get(ratingOverallGet(par)));
  }
  
  @Override
  public SSRatingOverall ratingOverallGet(final SSRatingOverallGetPar par) throws Exception {

    try{
      return sqlFct.getOverallRating(par.entity);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean ratingsRemove(final SSRatingsRemovePar par) throws Exception{
    
    try{
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.deleteRatingAss(par.user, par.entity);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return ratingsRemove(par);
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
  
//  private void saveRatingUserSetUE(SSServPar parA) throws Exception {
//    
//    Map<String, Object> opPars = new HashMap<>();
//    SSRatingUserSetPar par = new SSRatingUserSetPar(parA);
//    
//    opPars.put(SSVarU.shouldCommit, true);
//    opPars.put(SSVarU.user,         par.user);
//    opPars.put(SSVarU.resource,     par.resource);
//    opPars.put(SSVarU.eventType,    SSUEEnum.rateEntity);
//    opPars.put(SSVarU.content,      String.valueOf(par.value));
//    
//    SSServReg.callServServer(new SSServPar(SSServOpE.uEAdd, opPars));
//  }
}