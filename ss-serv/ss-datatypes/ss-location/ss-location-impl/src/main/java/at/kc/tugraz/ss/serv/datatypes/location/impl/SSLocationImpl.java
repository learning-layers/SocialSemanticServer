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
 package at.kc.tugraz.ss.serv.datatypes.location.impl;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.location.api.SSLocationClientI;
import at.kc.tugraz.ss.serv.datatypes.location.api.SSLocationServerI;
import at.kc.tugraz.ss.serv.datatypes.location.datatypes.desc.SSLocationDesc;
import at.kc.tugraz.ss.serv.datatypes.location.datatypes.par.SSLocationsGetPar;
import at.kc.tugraz.ss.serv.datatypes.location.datatypes.par.SSLocationAddPar;
import at.kc.tugraz.ss.serv.datatypes.location.datatypes.par.SSLocationsUserRemovePar;
import at.kc.tugraz.ss.serv.datatypes.location.datatypes.ret.SSLocationsGetRet;
import at.kc.tugraz.ss.serv.datatypes.location.datatypes.ret.SSLocationAddRet;
import at.kc.tugraz.ss.serv.datatypes.location.impl.fct.sql.SSLocationSQLFct;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.List;

public class SSLocationImpl extends SSServImplWithDBA implements SSLocationClientI, SSLocationServerI, SSEntityHandlerImplI{
  
//  private final SSLocationGraphFct graphFct;
  private final SSLocationSQLFct   sqlFct;
  
  public SSLocationImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
//    graphFct  = new SSLocationGraphFct (this);
    sqlFct    = new SSLocationSQLFct   (this);
  }
  
  /* SSEntityHandlerImplI */
  
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
    
    if(!removeUserLocations){
      return;
    }
    
    try{
      
      SSServCaller.locationsUserRemove(
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
    
    if(!SSEntityE.equals(entityType, SSEntityE.location)){
      SSEntityDesc.get(entityUri, label, creationTime, tags, overallRating, discUris,author);
    }
    
     return SSLocationDesc.get(
        entityUri,
        label,
        creationTime, 
        author, 
        overallRating, 
        tags, 
        discUris);
  }
  
  /* SSLocationClientI */
  
  @Override
  public void locationAdd(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLocationAddRet.get(locationAdd(par), par.op));
  }
  
  @Override
  public void locationsGet(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSLocationsGetRet.get(locationsGet(par), par.op));
  }
  
  /* SSLabelServerI */
  @Override
  public Boolean locationsUserRemove(final SSServPar parA) throws Exception{
    
    try{
      
      final SSLocationsUserRemovePar par = new SSLocationsUserRemovePar(parA);
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.deleteLocationAss(par.user, par.entity);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return locationsUserRemove(parA);
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
  public SSUri locationAdd(final SSServPar parA) throws Exception {
    
    try{
      final SSLocationAddPar par               = new SSLocationAddPar(parA);
      final Boolean          existsLocation    = sqlFct.existsLocationString   (par.location);
      final SSUri            locationUri       = sqlFct.getOrCreateLocationURI (existsLocation, par.location);
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        locationUri,
        SSLabel.get(par.location),
        SSEntityE.location,
        null,
        false);
      
      SSServCaller.entityAdd(
        par.user,
        par.entity,
        SSLabel.get(SSStrU.empty),
        SSEntityE.entity,
        null,
        false);
      
      if(!existsLocation){
        sqlFct.addLocation(locationUri);
      }
      
      if(!sqlFct.existsLocationAss(par.user, par.entity, locationUri)){
        sqlFct.addLocationAss(par.user, par.entity, locationUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return locationAdd(parA);
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
  public List<String> locationsGet(SSServPar parI) throws Exception {
    
    SSLocationsGetPar par = new SSLocationsGetPar(parI);
    
    try{
      return sqlFct.getLocationAsss(null, par.entity, null);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}