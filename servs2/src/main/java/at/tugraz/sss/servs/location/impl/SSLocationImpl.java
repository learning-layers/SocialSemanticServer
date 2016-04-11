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

import at.tugraz.sss.serv.errreg.SSServErrReg;
import at.tugraz.sss.serv.conf.*;
import at.tugraz.sss.servs.location.datatype.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.servs.common.api.*;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.entity.impl.*;
import at.tugraz.sss.servs.location.api.*;

public class SSLocationImpl 
extends SSEntityImpl 
implements 
  SSLocationServerI,
  SSDescribeEntityI{

  private final SSLocationSQLFct sql = new SSLocationSQLFct(dbSQL);
  
  public SSLocationImpl() throws SSErr{
    super(SSCoreConf.instGet().getLocation());
  }

  @Override
  public SSEntity describeEntity(
    final SSServPar            servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(par.setLocations){
        
        entity.locations.addAll(
          locationsGet(
            new SSLocationsGetPar(
              servPar,
              par.user,
              entity.id,
              par.withUserRestriction, //withUserRestriction
              false))); //invokeEntityHandlers
      }
      
      switch(entity.type){
        
        case location:{
          
          if(SSStrU.isEqual(entity, par.recursiveEntity)){
            return entity;
          }
          
          return SSLocation.get(
            locationGet(
              new SSLocationGetPar(
                servPar,
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
  public SSLocation locationGet(final SSLocationGetPar par) throws SSErr{
    
    try{
      
      if(par.location == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      return sql.getLocation(par, par.location);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> locationsGet(final SSLocationsGetPar par) throws SSErr{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final SSEntity entity = 
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          par.user, 
          par.entity, 
          par.withUserRestriction);
      
      if(entity == null){
        return new ArrayList<>();
      }
      
     final List<SSUri>      locationURIs    = sql.getLocationURIs(par, par.entity);
     final List<SSEntity>   locations       = new ArrayList<>();
     final SSLocationGetPar locationsGetPar =
       new SSLocationGetPar(
         par,
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
  public SSUri locationAdd(final SSLocationAddPar par) throws SSErr{
    
    try{
      
      final SSUri      entity;
      final SSUri      locationURI;
      final SSLocation location;
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      entity =
        entityUpdate(
          new SSEntityUpdatePar(
            par,
            par.user,
            par.entity, //entity,
            null, //type,
            null, //label,
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists,
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit
      
      if(entity == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
        
      location =
        SSLocation.get(
          SSConf.vocURICreate(),
          par.latitude,
          par.longitude,
          par.accuracy);
      
      locationURI =
        entityUpdate(
          new SSEntityUpdatePar(
            par,
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sql.addLocation(
        par,
        location.id,
        par.entity,
        location);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return locationURI;
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}