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
package at.kc.tugraz.ss.activity.impl;

import at.kc.tugraz.ss.activity.datatypes.SSActivity;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivitiesGetPar;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.misc.SSEntityFiller;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSEntitiesGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import java.util.ArrayList;
import java.util.List;

public class SSActivitiesGetFct {

  private final SSEntityFiller        entityFiller = new SSEntityFiller();
  private final SSEntityServerI       entityServ;
  private final SSActivitySQLFct      sql;
  
  public SSActivitiesGetFct(
    final SSEntityServerI  entityServ, 
    final SSActivitySQLFct sql){
    
    this.entityServ = entityServ;
    this.sql        = sql;
  }
  
  public void setEntitiesToQuery(
    final SSActivitiesGetPar   par,
    final SSEntityDescriberPar descPar,
    final List<SSUri>          entityURIsToQuery) throws Exception{
    
    try{
      
      if(par.entities.isEmpty()){
        return;
      }
      
      final SSEntitiesGetPar entitiesGetPar =
        new SSEntitiesGetPar(
          par.user,
          par.entities, //entities
          descPar, //descPar,
          par.withUserRestriction);
      
      final List<SSEntity> entities = entityServ.entitiesGet(entitiesGetPar);
      
      for(SSEntity entity : entities){
        
        entityFiller.addFilledEntity(entity.id, entity);
        
        entityURIsToQuery.add(entity.id);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void setCirclesToQuery(
    final SSActivitiesGetPar   par,
    final SSEntityDescriberPar descPar,
    final List<SSUri>          entityURIsToQuery) throws Exception{
    
    try{
      
      if(par.circles.isEmpty()){
        return;
      }
      
      final SSEntitiesGetPar entitiesGetPar =
        new SSEntitiesGetPar(
          par.user,
          par.circles, //entities
          descPar, //descPar,
          par.withUserRestriction);
      
      for(SSEntity circle : entityServ.entitiesGet(entitiesGetPar)){
        
        entityFiller.addFilledEntity(circle.id, circle);
        
        entityURIsToQuery.add(circle.id);
        
        for(SSEntity circleEntity : circle.entities){
          
          entityFiller.addFilledEntity(circleEntity.id, circleEntity);
          
          entityURIsToQuery.add(circleEntity.id);
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void setActivityURIsToFill(
    final SSActivitiesGetPar   par,
    final SSEntityDescriberPar descPar,
    final List<SSUri>          activityURIsToQuery) throws Exception{
  
    try{
      
      if(!par.activities.isEmpty()){
        activityURIsToQuery.addAll(par.activities);
        return;
      }
      
      final List<SSUri> entityURIsToQuery = new ArrayList<>();
      
      setEntitiesToQuery(
        par,
        descPar,
        entityURIsToQuery);
      
      setCirclesToQuery(
        par,
        descPar,
        entityURIsToQuery);
      
      SSStrU.distinctWithoutEmptyAndNull2(entityURIsToQuery);
      
      //      entityURIsToQuery.addAll(sqlFct.getAccessibleURIs(par.user));
      
      activityURIsToQuery.addAll(
        sql.getActivityURIs(
          par.users,
          entityURIsToQuery,
          par.types,
          par.startTime,
          par.endTime,
          true,
          1000,
          par.includeOnlyLastActivities));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public SSEntity getActivityEntity(
    final SSActivitiesGetPar   par,
    final SSEntityDescriberPar descPar, 
    final SSEntity             activityEntity) throws Exception{
    
    try{
      
      if(activityEntity == null){
        SSLogU.warn("entity for activity null");
        return null;
      }
      
      final SSEntity filledActivityEntity;
      
      if(entityFiller.containsFilledEntity(activityEntity)){
        filledActivityEntity = entityFiller.getFilledEntity(activityEntity);
      }else{
        
        final SSEntityGetPar entityGetPar =
          new SSEntityGetPar(
            par.user,
            activityEntity.id, //entity,
            par.withUserRestriction, //withUserRestriction,
            descPar); //descPar
        
        filledActivityEntity = entityServ.entityGet(entityGetPar);
        
        entityFiller.addFilledEntity(activityEntity.id, filledActivityEntity);
      }
      
      return filledActivityEntity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public void setActivityEntities(
    final SSActivity            activity,
    final SSActivitiesGetPar    par,
    final SSEntityDescriberPar  descPar) throws Exception{
     
    try{
    
      final SSEntityGetPar entityGetPar = 
        new SSEntityGetPar(
          par.user, 
          null,  //entity
          par.withUserRestriction,
          descPar);
      
      SSEntity activityEntity;
      
      for(SSUri entity : sql.getActivityEntities (activity.id)){
        
        if(entityFiller.containsFilledEntity(entity)){
          SSEntity.addEntitiesDistinctWithoutNull(activity.entities, entityFiller.getFilledEntity(entity));
          continue;
        }
        
        entityGetPar.entity = entity;
        activityEntity      = entityServ.entityGet(entityGetPar);
        
        entityFiller.addFilledEntity(entity, activityEntity);
        
        SSEntity.addEntitiesDistinctWithoutNull(activity.entities, activityEntity);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void setActivityUsers(
    final SSActivity            activity,
    final SSActivitiesGetPar    par,
    final SSEntityDescriberPar  descPar) throws Exception{
     
    try{
    
      final SSEntityGetPar entityGetPar = 
        new SSEntityGetPar(
          par.user, 
          null,  //entity
          par.withUserRestriction,
          descPar);
      
      SSEntity activityUser;
      
      for(SSUri user : sql.getActivityUsers (activity.id)){
        
        if(entityFiller.containsFilledEntity(user)){
          SSEntity.addEntitiesDistinctWithoutNull(activity.users, entityFiller.getFilledEntity(user));
          continue;
        }
        
        entityGetPar.entity = user;
        activityUser        = entityServ.entityGet(entityGetPar);
        
        entityFiller.addFilledEntity(user, activityUser);
        
        SSEntity.addEntitiesDistinctWithoutNull(activity.users, activityUser);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
