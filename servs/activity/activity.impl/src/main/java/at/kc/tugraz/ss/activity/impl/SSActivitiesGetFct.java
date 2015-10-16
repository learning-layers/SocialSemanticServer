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
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSActivitiesGetFct {

  private final SSEntityServerI       entityServ;
  private final SSActivitySQLFct      sqlFct;
  private final Map<String, SSEntity> filledEntities = new HashMap<>();
  
  public SSActivitiesGetFct(
    final SSEntityServerI  entityServ, 
    final SSActivitySQLFct sqlFct){
    
    this.entityServ = entityServ;
    this.sqlFct     = sqlFct;
  }
  
  public void addFilledEntity(
    final SSUri    id, 
    final SSEntity entity) throws Exception{
    
    try{
      
      if(id == null){
        return;
      }
      
      if(!filledEntities.containsKey(id.toString())){
        filledEntities.put(id.toString(), entity);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
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
          null, //types,
          descPar, //descPar,
          par.withUserRestriction);
      
      final List<SSEntity> entities = entityServ.entitiesGet(entitiesGetPar);
      
      for(SSEntity entity : entities){
        
        addFilledEntity(entity.id, entity);
        
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
          null, //types,
          descPar, //descPar,
          par.withUserRestriction);
      
      for(SSEntity circle : entityServ.entitiesGet(entitiesGetPar)){
        
        addFilledEntity(circle.id, circle);
        
        entityURIsToQuery.add(circle.id);
        
        for(SSEntity circleEntity : circle.entities){
          
          addFilledEntity(circleEntity.id, circleEntity);
          
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
        sqlFct.getActivityURIs(
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
  
  public Boolean containsFilledEntity(final SSUri entityURI) throws Exception {
    
    try{
      
      if(entityURI == null){
        return false;
      }
    
      return filledEntities.containsKey(entityURI.toString());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public Boolean containsFilledEntity(final SSEntity entity) throws Exception {
    
    try{
      
      if(entity == null){
        return false;
      }
    
      return filledEntities.containsKey(entity.id.toString());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSEntity getFilledEntity(final SSEntity entity) throws Exception{
    
    try{
      
      if(entity == null){
        return null;
      }
      
      return filledEntities.getOrDefault(entity.id.toString(), null);

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSEntity getFilledEntity(final SSUri entityURI) throws Exception{
    
    try{
      
      if(entityURI == null){
        return null;
      }
      
      return filledEntities.getOrDefault(entityURI.toString(), null);

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
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
      
      if(containsFilledEntity(activityEntity)){
        
        filledActivityEntity = getFilledEntity(activityEntity);
      }else{
        
        final SSEntityGetPar entityGetPar =
          new SSEntityGetPar(
            par.user,
            activityEntity.id, //entity,
            par.withUserRestriction, //withUserRestriction,
            descPar); //descPar
        
        filledActivityEntity = entityServ.entityGet(entityGetPar);
        
        filledEntities.put(activityEntity.id.toString(), filledActivityEntity);
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
      
      for(SSUri entity : sqlFct.getActivityEntities (activity.id)){
        
        if(containsFilledEntity(entity)){
          SSEntity.addEntitiesDistinctWithoutNull(activity.entities, getFilledEntity(entity));
          continue;
        }
        
        entityGetPar.entity = entity;
        activityEntity      = entityServ.entityGet(entityGetPar);
        
        addFilledEntity(entity, activityEntity);
        
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
      
      for(SSUri user : sqlFct.getActivityUsers (activity.id)){
        
        if(containsFilledEntity(user)){
          SSEntity.addEntitiesDistinctWithoutNull(activity.users, getFilledEntity(user));
          continue;
        }
        
        entityGetPar.entity = user;
        activityUser        = entityServ.entityGet(entityGetPar);
        
        addFilledEntity(user, activityUser);
        
        SSEntity.addEntitiesDistinctWithoutNull(activity.users, activityUser);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
