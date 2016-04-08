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
package at.tugraz.sss.servs.activity.impl;

import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.activity.api.SSActivityClientI;
import at.tugraz.sss.servs.activity.api.SSActivityServerI;
import at.tugraz.sss.servs.activity.datatype.SSActivity;
import at.tugraz.sss.servs.activity.datatype.SSActivityContent;
import at.tugraz.sss.servs.activity.datatype.SSActivityE;
import at.tugraz.sss.servs.activity.datatype.SSActivitiesGetPar;
import at.tugraz.sss.servs.activity.datatype.SSActivityAddPar;
import at.tugraz.sss.servs.activity.datatype.SSActivityContentAddPar;
import at.tugraz.sss.servs.activity.datatype.SSActivityContentsAddPar;
import at.tugraz.sss.servs.activity.datatype.SSActivityTypesGetPar;
import at.tugraz.sss.servs.activity.datatype.SSActivitiesGetRet;
import at.tugraz.sss.servs.activity.datatype.SSActivityTypesGetRet;
import at.tugraz.sss.servs.activity.datatype.SSActivityAddRet;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.entity.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.SSEntityContext;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.servs.common.impl.SSUserCommons;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.datatype.SSQueryResultPage;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI;
import at.tugraz.sss.serv.entity.api.SSUsersResourcesGathererI;
import java.util.Map;

public class SSActivityImpl
extends SSServImplWithDBA
implements
  SSActivityClientI,
  SSActivityServerI,
  SSDescribeEntityI,
  SSUsersResourcesGathererI{
  
  private final SSActivitySQLFct sql;
  private final SSUserCommons userCommons;
  
  public SSActivityImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql         = new SSActivitySQLFct(dbSQL);
    this.userCommons = new SSUserCommons();
  }
  
  @Override
  public void getUsersResources(
    final SSServPar                          servPar,
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      final SSActivitiesGetPar activitiesGetPar =
        new SSActivitiesGetPar(
          servPar,
          null, //user
          null, //activities,
          null, //types,
          null, //users,
          null, //entities,
          null, //circles,
          null, //startTime,
          null, //endTime,
          Integer.MAX_VALUE, //maxActivities
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
    }
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar            servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(par.setActivities){
        
        switch(entity.type){
          
          case user:{
            
            entity.activityPage =
              new SSQueryResultPage(
                activitiesGet(
                  new SSActivitiesGetPar(
                    servPar,
                    par.user,
                    null, //activities
                    null, //types
                    SSUri.asListNotNull(entity.id), //users
                    null, //entities
                    null, //circles
                    null, //startTime
                    null, //endTime
                    Integer.MAX_VALUE, //maxActivities
                    true, //includeOnlyLastActivities
                    par.withUserRestriction,
                    false))); //invokeEntityHandlers
            
            break;
          }
          
          case circle:{
            
            entity.activityPage =
              new SSQueryResultPage(
                activitiesGet(
                  new SSActivitiesGetPar(
                    servPar,
                    par.user,
                    null, //activities
                    null, //types
                    null, //users
                    null, //entities
                    SSUri.asListNotNull(entity.id), //circles
                    null, //startTime
                    null, //endTime
                    Integer.MAX_VALUE, //maxActivities
                    true, //includeOnlyLastActivities
                    par.withUserRestriction,
                    false))); //invokeEntityHandlers
            
            break;
          }
          
          default:{
            entity.activityPage =
              new SSQueryResultPage(
                activitiesGet(
                  new SSActivitiesGetPar(
                    servPar,
                    par.user,
                    null, //activities
                    null, //types
                    null, //users
                    SSUri.asListNotNull(entity.id), //entities
                    null, //circles
                    null, //startTime
                    null, //endTime
                    Integer.MAX_VALUE, //maxActivities
                    true, //includeOnlyLastActivities
                    par.withUserRestriction,
                    false))); //invokeEntityHandlers
            
            break;
          }
        }
      }
      
      switch(entity.type){
        
        case activity:{
          
          if(SSStrU.isEqual(entity, par.recursiveEntity)){
            return entity;
          }
          
          final List<SSEntity> activities =
            activitiesGet(
              new SSActivitiesGetPar(
                servPar,
                par.user,
                SSUri.asListNotNull(entity.id), //activities
                null, //types
                null, //users
                null, //entities
                null, //circles
                null, //startTime
                null, //endTime
                Integer.MAX_VALUE, //maxActivities
                false, //includeOnlyLastActivities
                par.withUserRestriction,
                false)); //invokeEntityHandlers
          
          if(!activities.isEmpty()){
            return SSActivity.get((SSActivity) activities.get(0), entity);
          }else{
            return entity;
          }
        }
        
        default: return entity;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI activityTypesGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSActivityTypesGetPar par = (SSActivityTypesGetPar) parA.getFromClient(clientType, parA, SSActivityTypesGetPar.class);
      
      return SSActivityTypesGetRet.get(activityTypesGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSActivityE> activityTypesGet(final SSActivityTypesGetPar parA) throws SSErr{
    
    try{
      return Arrays.asList(SSActivityE.values());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI activitiesGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSActivitiesGetPar par = (SSActivitiesGetPar) parA.getFromClient(clientType, parA, SSActivitiesGetPar.class);
      
      return SSActivitiesGetRet.get(activitiesGet(par), SSDateU.dateAsLong());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> activitiesGet(final SSActivitiesGetPar par) throws SSErr{
    
    try{
      final List<SSEntity>             activities           = new ArrayList<>();
      final List<SSUri>                activityURIsToQuery  = new ArrayList<>();
      final SSEntityServerI            entityServ           = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSActivitiesGetFct         fct                  = new SSActivitiesGetFct(entityServ, sql);
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
        
        activity = sql.getActivity(par, activityURI);
        
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
        
        activity.contents.addAll(sql.getActivityContents(par, activity.id));
        
        activities.add(activity);
        
        if(activities.size() > par.maxActivities){
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
  public SSServRetI activityAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSActivityAddPar par = (SSActivityAddPar) parA.getFromClient(clientType, parA, SSActivityAddPar.class);
      
      return SSActivityAddRet.get(activityAdd(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri activityAdd(final SSActivityAddPar par) throws SSErr{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
        
//        par.entity = SSUri.get(SSConf.sssUri);
      }
      
      if(!userCommons.areUsersUsers(par, par.users)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      SSUri                 entityEntity;
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri activity   =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par, 
            par.user,
            SSConf.vocURICreate(),
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      entityEntity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par, 
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      for(SSUri entity : par.entities){
        
        entityEntity =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              par, 
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
          dbSQL.rollBack(par, par.shouldCommit);
          return null;
        }
      }
      
      sql.addActivity(
        par, 
        par.user,
        activity,
        par.type,
        par.entity,
        par.users,
        par.entities,
        par.comments);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return activity;

    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri activityContentAdd(final SSActivityContentAddPar par) throws SSErr{
    
    try{
      
      final SSEntity activity =
        sql.getEntityTest(
          par, 
          SSConf.systemUserUri,
          par.user,
          par.activity,
          false); //withUserRestriction
      
      if(activity == null){
        return null;
      }
      
      sql.addActivityContent(
        par, 
        activity.id,
        par.contentType,
        par.content);
      
      return activity.id;
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
  
  @Override
  public void activityContentsAdd(final SSActivityContentsAddPar par) throws SSErr{
    
    try{
      
      final SSActivityContentAddPar activityContentAddPar =
        new SSActivityContentAddPar(
          par, 
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
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
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
//          if(SSStrU.isEqual(activitiesEntity.get(activityID), desc)){
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