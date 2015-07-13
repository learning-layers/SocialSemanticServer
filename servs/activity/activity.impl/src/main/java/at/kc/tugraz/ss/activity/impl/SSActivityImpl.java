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
package at.kc.tugraz.ss.activity.impl;

import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.activity.api.SSActivityClientI;
import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.SSActivity;
import at.kc.tugraz.ss.activity.datatypes.SSActivityContent;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivitiesGetPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityContentAddPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityContentsAddPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityGetPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityTypesGetPar;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivitiesGetRet;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivityTypesGetRet;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivityAddRet;
import at.kc.tugraz.ss.activity.impl.fct.sql.SSActivitySQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSCircleContentChangedPar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;

public class SSActivityImpl
extends SSServImplWithDBA
implements
  SSActivityClientI,
  SSActivityServerI,
  SSEntityHandlerImplI{
  
  private final SSActivitySQLFct sqlFct;
  
  public SSActivityImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct = new SSActivitySQLFct(dbSQL);
  }
  
  @Override
  public SSEntity getUserEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws Exception{
    
    switch(entity.type){
      
      case activity:{

        return SSActivity.get(
          activityGet(
            new SSActivityGetPar(
              null,
              null,
              par.user,
              entity.id, 
              false)), //invokeEntityHandlers
          entity);
      }
      
      default: return entity;
    }
  }
  
  @Override
  public void copyEntity(
    final SSUri        user,
    final List<SSUri>  users,
    final SSUri        entity,
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception{
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
  public void circleContentChanged(final SSCircleContentChangedPar par) throws Exception{
    
  }
  
  @Override
  public void activityTypesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSActivityTypesGetPar par = (SSActivityTypesGetPar) parA.getFromJSON(SSActivityTypesGetPar.class);
    
    sSCon.writeRetFullToClient(SSActivityTypesGetRet.get(activityTypesGet(par)));
  }
  
  @Override
  public List<SSActivityE> activityTypesGet(final SSActivityTypesGetPar parA) throws Exception{
    
    try{
      return Arrays.asList(SSActivityE.values());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void activitiesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSActivitiesGetPar   par        = (SSActivitiesGetPar) parA.getFromJSON(SSActivitiesGetPar.class);
    
    sSCon.writeRetFullToClient(SSActivitiesGetRet.get(activitiesGet(par), SSDateU.dateAsLong()));
  }
  
  @Override
  public List<SSEntity> activitiesGet(final SSActivitiesGetPar par) throws Exception{
    
    try{
      final List<SSEntity>             activities         = new ArrayList<>();
      final List<SSEntity>             entitiesToQuery    = new ArrayList<>();
      final List<SSUri>                activityURIs       = new ArrayList<>();
      
      if(!par.entities.isEmpty()){
        SSEntity.addEntitiesDistinctWithoutNull(
          entitiesToQuery,
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
            new SSEntitiesGetPar(
              null,
              null,
              par.user,
              par.entities, //entities
              null, //types,
              null, //descPar,
              par.withUserRestriction)));
      }
      
      if(!par.circles.isEmpty()){
      
        for(SSEntity circle :
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
            new SSEntitiesGetPar(
              null,
              null,
              par.user,
              par.circles, //entities
              null, //types,
              new SSEntityDescriberPar(), //descPar,
              par.withUserRestriction))){

          SSEntity.addEntitiesDistinctWithoutNull(
            entitiesToQuery,
            circle);

          SSEntity.addEntitiesDistinctWithoutNull(
            entitiesToQuery,
            circle.entities);
        }
      }
      
      activityURIs.addAll(
        sqlFct.getActivityURIs(
          par.users,
          SSUri.getFromEntitites(entitiesToQuery),
          par.types,
          par.startTime,
          par.endTime,
          true,
          1000,
          par.includeOnlyLastActivities));
      
      for(SSUri activityURI : activityURIs){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          activities,
          activityGet(
            new SSActivityGetPar(
              null,
              null,
              par.user,
              activityURI,
              par.invokeEntityHandlers)));
      }
      
      return activities;
        
//      final List<SSUri>                descURIs           = new ArrayList<>();
//      final Map<String, List<SSUri>>   activitiesUsers    = new HashMap<>();
//      final Map<String, List<SSUri>>   activitiesEntities = new HashMap<>();
//      final Map<String, SSUri>         activitiesEntity   = new HashMap<>();
//      final List<SSEntity>             descs              = new ArrayList<>();
//      String                           activityID;
//      for(SSActivity activity : activities){
//
//        result.add(activity);
//
//        activityID = SSStrU.toStr(activity);
//
//        activitiesUsers.put   (SSStrU.toStr(activity), new ArrayList<>());
//        activitiesEntities.put(SSStrU.toStr(activity), new ArrayList<>());
//
//        if(activity.entity != null){
//          activitiesEntity.put (activityID, activity.entity.id);
//          descURIs.add         (activity.entity.id);
//        }
//
//        activitiesUsers.get(activityID).addAll(sqlFct.getActivityUsers(activity.id));
//        descURIs.addAll                       (activitiesUsers.get(activityID));
//
//        activitiesEntities.get(activityID).addAll(sqlFct.getActivityEntities(activity.id));
//        descURIs.addAll                          (activitiesEntities.get(activityID));
//
//        activity.contents.addAll(sqlFct.getActivityContents(activity.id));
//      }
//
//      SSStrU.distinctWithoutEmptyAndNull2(descURIs);
//
//      SSEntity.addEntitiesDistinctWithoutNull(
//        descs,
//        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
//          new SSEntitiesGetPar(
//            null,
//            null,
//            par.user,
//            descURIs,
//            null, //forUser,
//            null, //types,
//            null, //descPar
//            par.withUserRestriction))); //withUserRestriction,
//
//      if(descs.isEmpty()){
//        return result;
//      }
//
//      for(SSActivity activity : result){
//
//        activityID = SSStrU.toStr(activity);
//
//        for(SSEntity desc : descs){
//
//          if(SSStrU.equals(activitiesEntity.get(activityID), desc)){
//            activity.entity = desc;
//            continue;
//          }
//
//          if(SSStrU.contains(activitiesUsers.get(activityID), desc)){
//            activity.users.add(desc);
//            continue;
//          }
//
//          if(SSStrU.contains(activitiesEntities.get(activityID), desc)){
//            activity.entities.add(desc);
//          }
//        }
//      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void activityAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSActivityAddPar par = (SSActivityAddPar) parA.getFromJSON(SSActivityAddPar.class);
    
    sSCon.writeRetFullToClient(SSActivityAddRet.get(activityAdd(par)));
  }
  
  @Override
  public SSUri activityAdd(final SSActivityAddPar par) throws Exception{
    
    try{
      final SSUri activityUri = SSServCaller.vocURICreate();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCallerU.checkWhetherUsersAreUsers(par.users);
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          activityUri,
          SSEntityE.activity,
          SSLabel.get(SSStrU.toStr(par.type)), //label,
          null, //description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          true, //setPublic
          par.withUserRestriction, //withUserRestriction,
          false)); //shouldCommit))
      
      if(par.entity != null){
        
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            par.user,
            par.entity,
            null, //type
            null, //label,
            null, //description,
            null, //entitiesToAttach,
            null, //creationTime,
            null, //read,
            false, //setPublic
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit))
      }
      
      for(SSUri entity : par.entities){
        
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            par.user,
            entity,
            null, //type
            null, //label,
            null, //description,
            null, //entitiesToAttach,
            null, //creationTime,
            null, //read,
            false, //setPublic
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit))
      }
      
      sqlFct.addActivity(
        par.user,
        activityUri,
        par.type,
        par.entity,
        par.users,
        par.entities,
        par.comments);
      
      dbSQL.commit(par.shouldCommit);
      
      return activityUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return activityAdd(par);
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
  public SSUri activityContentAdd(final SSActivityContentAddPar par) throws Exception{
    
    try{
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          par.activity,
          null, //type
          null, //label,
          null, //description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction,
          false)); //shouldCommit))
      
      sqlFct.addActivityContent(
        par.activity,
        par.contentType,
        par.content);
      
      return par.activity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return activityContentAdd(par);
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
  public void activityContentsAdd(final SSActivityContentsAddPar par) throws Exception{
    
    try{
      
      for(SSActivityContent content : par.contents){
        
        activityContentAdd(
          new SSActivityContentAddPar(
            null,
            null,
            par.user,
            par.activity,
            par.contentType,
            content,
            false));
      }
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          activityContentsAdd(par);
        }else{
          SSServErrReg.regErrThrow(error);
        }
        
        return;
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSActivity activityGet(final SSActivityGetPar par) throws Exception{
    
    try{
      
      final SSActivity            activity;
      final SSEntityDescriberPar  descPar;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar();
      }else{
        descPar = null;
      }
      
      activity =
        SSActivity.get(
          sqlFct.getActivity(par.activity),
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              par.activity, //entity
              par.withUserRestriction, //withUserRestriction,
              descPar))); //descPar
      
      if(activity.entity != null){
        
        activity.entity =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              activity.entity.id,
              par.withUserRestriction, //withUserRestriction,
              descPar)); //descPar,
      }
      
      final List<SSUri>    activityUserURIs        = sqlFct.getActivityUsers(activity.id);
      final List<SSEntity> activityUserEntities = new ArrayList<>();
      
      activityUserEntities.addAll(
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
        new SSEntitiesGetPar(
          null,
          null,
          par.user,
          activityUserURIs,  //entities
          null, //types,
          descPar, //descPar,
          par.withUserRestriction)));
      
      activity.users.clear();
      activity.users.addAll(activityUserEntities);
      
      activity.contents.addAll(sqlFct.getActivityContents(activity.id));
      
      final List<SSUri>    activityEntityURIs      = sqlFct.getActivityEntities(activity.id);
      final List<SSEntity> activityEntityEntities  = new ArrayList<>();
      
      activityEntityEntities.addAll(
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            null,
            null,
            par.user,
            activityEntityURIs,
            null, //types,
            descPar, //descPar
            par.withUserRestriction)));
      
      activity.entities.clear();
      activity.entities.addAll(activityEntityEntities);
      
      return activity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}