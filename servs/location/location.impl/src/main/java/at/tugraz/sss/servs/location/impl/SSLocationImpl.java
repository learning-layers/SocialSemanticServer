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
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.tugraz.sss.servs.location.datatype.par.SSLocationAddPar;
import at.tugraz.sss.servs.location.datatype.par.SSLocationsGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServImplWithDBA;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.servs.location.datatype.SSLocation;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.servs.location.api.SSLocationClientI;
import at.tugraz.sss.servs.location.api.SSLocationServerI;
import at.tugraz.sss.servs.location.datatype.par.SSLocationGetPar;
import at.tugraz.sss.servs.location.impl.sql.SSLocationSQLFct;

public class SSLocationImpl 
extends SSServImplWithDBA 
implements 
  SSLocationClientI, 
  SSLocationServerI,
  SSDescribeEntityI{

  private final SSLocationSQLFct sql;
  private final SSEntityServerI  entityServ;
  
  public SSLocationImpl(final SSConfA conf) throws SSErr{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
     sql             = new SSLocationSQLFct   (dbSQL, SSVocConf.systemUserUri);
     this.entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
  }

  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setLocations){
        
        entity.locations.addAll(
          locationsGet(
            new SSLocationsGetPar(
              par.user,
              entity.id,
              par.withUserRestriction, //withUserRestriction
              false))); //invokeEntityHandlers
      }
      
      switch(entity.type){
        
        case location:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          return SSLocation.get(
            locationGet(
              new SSLocationGetPar(
                par.user,
                entity.id,
                par.withUserRestriction,
                false)), //invokeEntityHandlers
            entity);
        }
        
        default: return entity;
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSLocation locationGet(final SSLocationGetPar par) throws Exception{
    
    try{
      
      if(par.location == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      return sql.getLocation(par.location);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> locationsGet(final SSLocationsGetPar par) throws Exception{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final SSEntity entity = 
        sql.getEntityTest(
          par.user, 
          par.entity, 
          par.withUserRestriction);
      
      if(entity == null){
        return new ArrayList<>();
      }
      
     final List<SSUri>      locationURIs    = sql.getLocationURIs(par.entity);
     final List<SSEntity>   locations       = new ArrayList<>();
     final SSLocationGetPar locationsGetPar =
       new SSLocationGetPar(
         par.user,
         null, //location,
         par.withUserRestriction,
         par.invokeEntityHandlers);
       
     for(SSUri locationURI : locationURIs){

       locationsGetPar.location = locationURI;
       
       SSEntity.addEntitiesDistinctWithoutNull(
         locations,
         locationGet(locationsGetPar));
     }
     
     return locations;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
    
  @Override
  public SSUri locationAdd(final SSLocationAddPar par) throws Exception{
    
    try{
      
      final SSUri      entity;
      final SSUri      locationURI;
      final SSLocation location;
      
      dbSQL.startTrans(par.shouldCommit);
      
      entity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.entity, //entity,
            null, //type,
            null, //label,
            null, //description,
            null, //creationTime,
            null, //read,
            null, //setPublic
            true, //createIfNotExists,
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit
      
      if(entity == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
        
      location =
        SSLocation.get(
          SSVocConf.vocURICreate(),
          par.latitude,
          par.longitude,
          par.accuracy);
      
      locationURI =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            location.id, //entity,
            SSEntityE.location, //type,
            null, //label,
            null, //description,
            null, //creationTime,
            null, //read,
            true, //setPublic
            true, //createIfNotExists,
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit
      
      if(locationURI == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sql.addLocation(
        location.id,
        par.entity,
        location);
      
      dbSQL.commit(par.shouldCommit);
      
      return locationURI;
      
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