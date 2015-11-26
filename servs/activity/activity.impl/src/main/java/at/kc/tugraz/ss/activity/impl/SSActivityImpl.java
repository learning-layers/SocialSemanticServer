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
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityTypesGetPar;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivitiesGetRet;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivityTypesGetRet;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivityAddRet;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
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
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityContext;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import java.util.Map;

public class SSActivityImpl
extends SSServImplWithDBA
implements
  SSActivityClientI,
  SSActivityServerI,
  SSDescribeEntityI,
  SSUsersResourcesGathererI{
  
  private final SSActivitySQLFct sqlFct;
  
  public SSActivityImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct = new SSActivitySQLFct(dbSQL);
  }
  
  @Override
  public void getUsersResources(
    final Map<String, List<SSEntityContext>> usersEntities) throws Exception{
    
    try{
      
      final SSActivitiesGetPar activitiesGetPar =
        new SSActivitiesGetPar(
          null, //user
          null, //activities,
          null, //types,
          null, //users,
          null, //entities,
          null, //circles,
          null, //startTime,
          null, //endTime,
          false, //includeOnlyLastActivities,
          false, //withUserRestriction,
          false); //invokeEntityHandlers);
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        activitiesGetPar.user = userID;
        activitiesGetPar.users.add(userID);
        
        for(SSEntity activity : activitiesGet(activitiesGetPar)){
          
          usersEntities.get(user).add(
            new SSEntityContext(
              ((SSActivity) activity).entity.id,
              SSEntityE.activity,
              null,
              null));
          
          for(SSEntity entity : activity.entities){
            
            usersEntities.get(user).add(
              new SSEntityContext(
                entity.id,
                SSEntityE.activity,
                null,
                null));
          }
        }
      }
      
    }catch(Exception error){
      SSLogU.err(error);
      SSServErrReg.reset();
    }
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws Exception{
    
    switch(entity.type){
      
      case activity:{
        
        if(SSStrU.equals(entity, par.recursiveEntity)){
          return entity;
        }
        
        final List<SSEntity> activities =
          activitiesGet(
            new SSActivitiesGetPar(
              par.user,
              SSUri.asListWithoutNullAndEmpty(entity.id), //activities
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              par.withUserRestriction,
              false));
        
        if(activities.isEmpty()){
          throw new Exception("activity doesnt exist");
        }
        
        return SSActivity.get((SSActivity) activities.get(0), entity);
      }
      
      default: return entity;
    }
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
    
    final SSActivitiesGetPar par = (SSActivitiesGetPar) parA.getFromJSON(SSActivitiesGetPar.class);
    
    sSCon.writeRetFullToClient(SSActivitiesGetRet.get(activitiesGet(par), SSDateU.dateAsLong()));
  }
  
  @Override
  public List<SSEntity> activitiesGet(final SSActivitiesGetPar par) throws Exception{
    
    try{
      final List<SSEntity>             activities           = new ArrayList<>();
      final List<SSUri>                activityURIsToQuery  = new ArrayList<>();
      final SSEntityServerI            entityServ           = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSActivitiesGetFct         fct                  = new SSActivitiesGetFct(entityServ, sqlFct);
      SSActivity                       activity;
      SSEntityDescriberPar             descPar;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(null);
      }else{
        descPar = null;
      }
      
      fct.setActivityURIsToFill(
        par,
        descPar,
        activityURIsToQuery);
      
      for(SSUri activityURI : activityURIsToQuery){
        
        activity = sqlFct.getActivity(activityURI);
        
        if(activity == null){
          continue;
        }
        
        if(par.invokeEntityHandlers){
          descPar = new SSEntityDescriberPar(activity.id);
        }else{
          descPar = null;
        }
        
        activity.entity = 
          fct.getActivityEntity(
            par, 
            descPar, 
            activity.entity);
        
        if(activity.entity == null){
          continue;
        }
        
        fct.setActivityEntities(
          activity, 
          par, 
          descPar);
        
        fct.setActivityUsers(
          activity, 
          par, 
          descPar);
        
        activity.contents.addAll(sqlFct.getActivityContents(activity.id));
        
        activities.add(activity);
        
        if(activities.size() > 30){
          break;
        }
      }
      
      return activities;

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

      if(par.entity == null){
        throw new SSErr(SSErrE.parameterMissing);
        
//        par.entity = SSUri.get(SSVocConf.sssUri);
      }
      
      if(!SSServCallerU.areUsersUsers(par.users)){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      SSUri                 entityEntity;
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri activity   =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            SSServCaller.vocURICreate(),
            SSEntityE.activity,
            SSLabel.get(SSStrU.toStr(par.type)), //label,
            null, //description,
            null, //creationTime,
            null, //read,
            true, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit))
      
      if(activity == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      entityEntity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.entity,
            null, //type
            null, //label,
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit))
      
      if(entityEntity == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      for(SSUri entity : par.entities){
        
        entityEntity =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              par.user,
              entity,
              null, //type
              null, //label,
              null, //description,
              null, //creationTime,
              null, //read,
              false, //setPublic
              true, //createIfNotExists
              par.withUserRestriction, //withUserRestriction,
              false)); //shouldCommit))
        
        if(entityEntity == null){
          dbSQL.rollBack(par.shouldCommit);
          return null;
        }
      }
      
      sqlFct.addActivity(
        par.user,
        activity,
        par.type,
        par.entity,
        par.users,
        par.entities,
        par.comments);
      
      dbSQL.commit(par.shouldCommit);
      
      return activity;
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
      
      final SSEntity activity = 
        sqlFct.getEntityTest(
          par.user, 
          par.activity, 
          false); //withUserRestriction
      
      if(activity == null){
        return null;
      }
      
      sqlFct.addActivityContent(
        activity.id,
        par.contentType,
        par.content);
      
      return activity.id;
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
      
      final SSActivityContentAddPar activityContentAddPar =
        new SSActivityContentAddPar(
          par.user,
          par.activity,
          par.contentType,
          null, //content
          false);
      
      for(SSActivityContent content : par.contents){
        
        activityContentAddPar.content = content;
        
        activityContentAdd(activityContentAddPar);
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
}


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