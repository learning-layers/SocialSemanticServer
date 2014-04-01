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
 package at.kc.tugraz.ss.serv.datatypes.location.impl;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.datatypes.location.api.SSLocationClientI;
import at.kc.tugraz.ss.serv.datatypes.location.api.SSLocationServerI;
import at.kc.tugraz.ss.serv.datatypes.location.datatypes.desc.SSLocationDesc;
import at.kc.tugraz.ss.serv.datatypes.location.datatypes.par.SSLocationsGetPar;
import at.kc.tugraz.ss.serv.datatypes.location.datatypes.par.SSLocationAddPar;
import at.kc.tugraz.ss.serv.datatypes.location.datatypes.par.SSLocationsUserRemovePar;
import at.kc.tugraz.ss.serv.datatypes.location.datatypes.ret.SSLocationsGetRet;
import at.kc.tugraz.ss.serv.datatypes.location.datatypes.ret.SSLocationAddRet;
import at.kc.tugraz.ss.serv.datatypes.location.impl.fct.sql.SSLocationSQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.util.List;

public class SSLocationImpl extends SSServImplWithDBA implements SSLocationClientI, SSLocationServerI, SSEntityHandlerImplI{
  
//  private final SSLocationGraphFct graphFct;
  private final SSLocationSQLFct   sqlFct;
  
  public SSLocationImpl(final SSServConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
//    graphFct  = new SSLocationGraphFct (this);
    sqlFct    = new SSLocationSQLFct   (this);
  }
  
  /* SSEntityHandlerImplI */
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSEntityEnum                                  entityType,
    final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par,
    final Boolean                                       shouldCommit) throws Exception{
    
    if(!par.removeUserLocations){
      return;
    }
    
    try{
      SSServCaller.locationsUserRemove(par.user, par.entityUri, shouldCommit);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
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
    
    if(!SSEntityEnum.equals(entityType, SSEntityEnum.location)){
      SSEntityDesc.get(entityUri, label, creationTime, tags, overallRating, discUris,author);
    }
    
     return SSLocationDesc.get(
        entityUri,
        label,
        creationTime, 
        author);
  }
  
  /****** SSLocationClientI ******/
  
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
  
  /****** SSLabelServerI ******/
  @Override
  public Boolean locationsUserRemove(final SSServPar parA) throws Exception{
    
    final SSLocationsUserRemovePar par = new SSLocationsUserRemovePar(parA);
    
    try{
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.deleteLocationAss(par.user, par.entityUri);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri locationAdd(SSServPar parI) throws Exception {
    
    SSLocationAddPar par            = new SSLocationAddPar(parI);
    Boolean          existsLocation;
    SSUri            locationUri;
    
    try{
      
      existsLocation = sqlFct.existsLocationString   (par.location);
      locationUri    = sqlFct.getOrCreateLocationUri (existsLocation, par.location);
      
      SSServCaller.addEntity(
        par.user,
        locationUri,
        SSLabelStr.get(par.location),
        SSEntityEnum.location);
      
      SSServCaller.addEntity(
        par.user,
        par.entityUri,
        SSLabelStr.get(SSStrU.empty),
        SSEntityEnum.entity);
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(!existsLocation){
        sqlFct.addLocation(locationUri);
      }
      
      if(!sqlFct.existsLocationAss(par.user, par.entityUri, locationUri)){
        sqlFct.addLocationAss(par.user, par.entityUri, locationUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entityUri;
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }    
  }
  
  @Override
  public List<String> locationsGet(SSServPar parI) throws Exception {
    
    SSLocationsGetPar par    = new SSLocationsGetPar(parI);
    
    try{
      return sqlFct.getLocationAsss(null, par.entityUri, null);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}