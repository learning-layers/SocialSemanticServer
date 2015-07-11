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
package at.tugraz.sss.servs.location.impl;

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.tugraz.sss.servs.location.datatype.par.SSLocationAddPar;
import at.tugraz.sss.servs.location.datatype.par.SSLocationsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSCircleContentChangedPar;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.servs.location.datatype.SSLocation;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.servs.location.api.SSLocationClientI;
import at.tugraz.sss.servs.location.api.SSLocationServerI;
import at.tugraz.sss.servs.location.impl.sql.SSLocationSQLFct;
import at.tugraz.sss.util.SSServCallerU;

public class SSLocationImpl 
extends SSServImplWithDBA 
implements 
  SSLocationClientI, 
  SSLocationServerI,
  SSEntityHandlerImplI{

  private final SSLocationSQLFct         sqlFct;
  
  public SSLocationImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
     sqlFct = new SSLocationSQLFct   (this);
  }

  @Override
  public SSEntity getUserEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws Exception{
    
    if(par.setLocations){
      
      entity.locations.addAll(
        locationsGet(
          new SSLocationsGetPar(
            null,
            null,
            par.user,
            entity.id,
            par.withUserRestriction))); //withUserRestriction
    }
      
     
    return entity;
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
    final SSUri     user,
    final SSUri     entity,
    final SSEntityE type) throws Exception{

    return null;
  }
  
  @Override
  public void circleContentChanged(final SSCircleContentChangedPar par) throws Exception{
  }
  
  @Override
  public void copyEntity(
    final SSUri       user,
    final List<SSUri> users,
    final SSUri       entity,
    final List<SSUri> entitiesToExclude,
    final SSEntityE   entityType) throws Exception{

  }

  @Override
  public List<SSLocation> locationsGet(final SSLocationsGetPar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        SSServCallerU.canUserReadEntity(par.user, par.entity);
      }
      
      return sqlFct.getLocations(par.entity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
    
  @Override
  public SSUri locationAdd(final SSLocationAddPar par) throws Exception{
    
    try{
      dbSQL.startTrans(par.shouldCommit);
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          par.entity, //entity,
          null, //type,
          null, //label,
          null, //description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          null, //setPublic
          par.withUserRestriction, //withUserRestriction,
          false)); //shouldCommit
      
      final SSLocation location =
        SSLocation.get(
          SSServCaller.vocURICreate(),
          par.latitude,
          par.longitude,
          par.accuracy);
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          location.id, //entity,
          SSEntityE.location, //type,
          null, //label,
          null, //description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          true, //setPublic
          par.withUserRestriction, //withUserRestriction,
          false)); //shouldCommit
      
      sqlFct.addLocation(
        location.id,
        par.entity,
        location);
      
      dbSQL.commit(par.shouldCommit);
      
      return location.id;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return locationAdd(par);
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
}