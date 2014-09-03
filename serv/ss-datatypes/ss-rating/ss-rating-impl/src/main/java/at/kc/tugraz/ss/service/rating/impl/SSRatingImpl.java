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
 package at.kc.tugraz.ss.service.rating.impl;

import at.kc.tugraz.ss.service.rating.impl.fct.userraltionsgathering.SSRatingUserRelationGathererFct;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingOverallGetPar;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingUserGetPar;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingUserSetPar;
import at.kc.tugraz.ss.service.rating.api.*;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityDescriberI;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSUserRelationGathererI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.rating.datatypes.SSRating;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.rating.datatypes.ret.SSRatingOverallGetRet;
import at.kc.tugraz.ss.service.rating.datatypes.ret.SSRatingUserGetRet;
import at.kc.tugraz.ss.service.rating.datatypes.ret.SSRatingUserSetRet;
import at.kc.tugraz.ss.service.rating.impl.fct.sql.SSRatingSQLFct;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingsUserRemovePar;
import at.kc.tugraz.ss.service.rating.impl.fct.activity.SSRatingActivityFct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sss.serv.err.datatypes.SSErrE;

public class SSRatingImpl extends SSServImplWithDBA implements SSRatingClientI, SSRatingServerI, SSEntityHandlerImplI, SSEntityDescriberI, SSUserRelationGathererI{
 
  private final SSRatingSQLFct   sqlFct;
//  private final SSRatingGraphFct graphFct;
    
  public SSRatingImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
    sqlFct   = new SSRatingSQLFct   (this);
//    graphFct = new SSRatingGraphFct (this);
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
      SSServCaller.ratingsUserRemove(
        userUri, 
        entityUri, 
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
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
  public SSEntity getDescForEntity(
    final SSEntityDescGetPar par,
    final SSEntity           desc) throws Exception{
    
    if(par.getOverallRating){
      
      desc.overallRating = 
        SSServCaller.ratingOverallGet(
          par.user, 
          par.entity);
    }
    
    return desc;
  }
  
  @Override
  public void ratingSet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSRatingUserSetRet.get(ratingUserSet(parA), parA.op));
    
//    saveRatingUserSetUE(par);
    
    SSRatingActivityFct.rateEntity(new SSRatingUserSetPar(parA));
  }
  
  @Override
  public Boolean ratingUserSet(SSServPar parA) throws Exception {
    
    try{
      final SSRatingUserSetPar par       = new SSRatingUserSetPar(parA);
    
      SSServCaller.entityUserCanRead(par.user, par.entity);
      
      if(sqlFct.hasUserRatedEntity(par.user, par.entity)){
        return true;
      }
      
      final SSUri ratingUri = sqlFct.createRatingUri();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntityToPrivCircleAdd(
        par.user,
        par.entity,
        SSEntityE.entity,
        SSLabel.get(par.entity),
        null,
        null,
        false);
      
      SSServCaller.entityEntityToPrivCircleAdd(
        par.user, 
        ratingUri, 
        SSEntityE.rating, 
        null, 
        null, 
        null, 
        false);
      
      sqlFct.rateEntityByUser(
        ratingUri, 
        par.user,
        par.entity, 
        par.value);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return ratingUserSet(parA);
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
  public void ratingUserGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSRatingUserGetRet.get(ratingUserGet(par), par.op));
  }
  
  @Override
  public Integer ratingUserGet(SSServPar parI) throws Exception {
    
    SSRatingUserGetPar par    = new SSRatingUserGetPar(parI);
    Integer            result = 0;
    
    try{
      result = sqlFct.getUserRating(par.user, par.entity);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    return result;
  }

  @Override
  public void ratingOverallGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSRatingOverallGetRet.get(ratingOverallGet(par), par.op));
  }
  
  @Override
  public SSRatingOverall ratingOverallGet(SSServPar parI) throws Exception {
    
    SSRatingOverallGetPar par     = new SSRatingOverallGetPar(parI);
    SSRatingOverall       result  = null;
    
    try{
      result = sqlFct.getOverallRating(par.entity);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    return result;
  }
  
  @Override
  public Boolean ratingsUserRemove(final SSServPar parA) throws Exception{
    
    try{
      
      final SSRatingsUserRemovePar par = new SSRatingsUserRemovePar(parA);
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.deleteRatingAss(par.user, par.entity);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return ratingsUserRemove(parA);
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
//    SSServReg.callServServer(new SSServPar(SSMethU.uEAdd, opPars));
//  }
}