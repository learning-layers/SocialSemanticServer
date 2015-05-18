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
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.activity.api.SSActivityClientI;
import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.SSActivity;
import at.kc.tugraz.ss.activity.datatypes.SSActivityContent;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivitiesUserGetPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityContentAddPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityContentsAddPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityGetPar;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivitiesUserGetRet;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivityTypesGetRet;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivityUserAddRet;
import at.kc.tugraz.ss.activity.impl.fct.sql.SSActivitySQLFct;
import at.kc.tugraz.ss.service.user.api.SSUserServerI;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUsersGetPar;
import at.kc.tugraz.ss.service.user.service.SSUserServ;
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
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;

public class SSActivityImpl extends SSServImplWithDBA implements SSActivityClientI, SSActivityServerI, SSEntityHandlerImplI{
  
  private final SSActivitySQLFct sqlFct;
  
  public SSActivityImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct = new SSActivitySQLFct(dbSQL);
  }
  
  @Override
  public Boolean copyUserEntity(
    final SSUri        user,
    final List<SSUri>  users,
    final SSUri        entity,
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception{
    
    return false;
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
  public Boolean setUserEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri,
    final SSEntityE      entityType,
    final SSUri          publicCircleUri) throws Exception{
    
    return null;
  }
  
  @Override
  public void shareUserEntity(
    final SSUri          userUri,
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri,
    final SSUri          circleUri,
    final SSEntityE      entityType,
    final Boolean        saveActivity) throws Exception{
  }
  
  @Override
  public void shareUserEntityWithCircle(
    final SSUri        userUri,
    final SSUri        circleUri,
    final SSUri        entityUri,
    final SSEntityE entityType) throws Exception{
    
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
  }
  
  @Override
  public void activityTypesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSActivityTypesGetRet.get(activityTypesGet(parA), parA.op), parA.op);
  }
  
  @Override
  public List<SSActivityE> activityTypesGet(final SSServPar parA) throws Exception{
    
    try{
      final SSActivitiesUserGetPar par = new SSActivitiesUserGetPar(parA);
      
      return Arrays.asList(SSActivityE.values());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  
  @Override
  public void activitiesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSActivitiesUserGetRet.get(activitiesUserGet(parA), SSDateU.dateAsLong(), parA.op), parA.op);
  }
  
  @Override
  public List<SSActivity> activitiesUserGet(final SSServPar parA) throws Exception{
    
    try{
      final SSActivitiesUserGetPar     par                = new SSActivitiesUserGetPar(parA);
      final List<SSActivity>           result             = new ArrayList<>();
      final List<SSUri>                entitiesToQuery    = new ArrayList<>();
      final List<SSUri>                descURIs           = new ArrayList<>();
      final Map<String, List<SSUri>>   activitiesUsers    = new HashMap<>();
      final Map<String, List<SSUri>>   activitiesEntities = new HashMap<>();
      final Map<String, SSUri>         activitiesEntity   = new HashMap<>();
      String                           activityID;
      
      
      if(!par.entities.isEmpty()){
        
        for(SSUri entity : par.entities){
          
          try{
            SSServCallerU.canUserReadEntity(par.user, entity);
          }catch(Exception error){
            SSServErrReg.reset();
            SSLogU.warn("user cannot access entity for activities");
            continue;
          }
          
          entitiesToQuery.add(entity);
        }
      }
      
      if(!par.circles.isEmpty()){
        
        for(SSUri circle : par.circles){
          
          try{
            SSServCallerU.canUserReadEntity(par.user, circle);
          }catch(Exception error){
            SSServErrReg.reset();
            SSLogU.warn("user cannot access circle for activities");
            continue;
          }
          
          for(SSEntity circleEntity : SSServCaller.circleGet(
            par.user, //user
            par.user, //forUser
            circle,   //circle
            SSEntityE.asListWithoutNullAndEmpty(), //entityTypesToIncludeOnly
            false,    //withSystemCircles
            true, //withUserRestriction
            false).entities){ //invokeEntityHandlers
            
            entitiesToQuery.add(circleEntity.id);
          }
          
          entitiesToQuery.add(circle);
        }
      }
      
      SSStrU.distinctWithoutNull2(entitiesToQuery);
      
      for(
        SSActivity activity :
        sqlFct.getActivities(
          par.users,
          entitiesToQuery,
          par.types,
          par.startTime,
          par.endTime,
          true,
          1000,
          par.includeOnlyLastActivities)){
        
        activityID = SSStrU.toStr(activity);
        
        activitiesUsers.put   (SSStrU.toStr(activity), new ArrayList<>());
        activitiesEntities.put(SSStrU.toStr(activity), new ArrayList<>());
        
        if(activity.entity == null){
          SSLogU.warn("activity entity null for activity type: " + activity.activityType);
          continue;
        }
        
        try{
          SSServCallerU.canUserReadEntity(par.user, activity.entity.id);
        }catch(Exception error){
          
          if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
            SSServErrReg.reset();
            SSLogU.debug("activity not accessible by user");
            continue;
          }
          
          throw error;
        }
        
        result.add(activity);
        
        activitiesEntity.put(activityID, activity.entity.id);
        descURIs.add(activity.entity.id);
        
        activitiesUsers.get(activityID).addAll(sqlFct.getActivityUsers(activity.id));
        descURIs.addAll(activitiesUsers.get(activityID));
        
        for(SSUri activityEntityURI : sqlFct.getActivityEntities(activity.id)){
          
          try{
            SSServCallerU.canUserReadEntity(par.user, activityEntityURI);
          }catch(Exception error){
            
            if(SSServErrReg.containsErr(SSErrE.userNotAllowedToAccessEntity)){
              SSServErrReg.reset();
              SSLogU.debug("activity entity not accessible by user");
              continue;
            }
            
            throw error;
          }
          
          activitiesEntities.get(activityID).add(activityEntityURI);
          descURIs.add(activityEntityURI);
        }
        
        activity.contents.addAll(sqlFct.getActivityContents(activity.id));
      }
      
      SSStrU.distinctWithoutEmptyAndNull2(descURIs);
      
      final List<SSEntity> descs = new ArrayList<>();
      
      try{
        descs.addAll(
          SSServCaller.entityDescsGet(
            par.user,
            descURIs,
            SSEntityE.asListWithoutNullAndEmpty(),
            false,
            false,
            false,
            false,
            false,
            false));
        
      }catch(Exception error){
        SSLogU.warn("information for activities couldnt be filled up");
        SSServErrReg.reset();
      }
      
      if(descs.isEmpty()){
        return result;
      }
      
      for(SSActivity activity : result){
        
        activityID = SSStrU.toStr(activity);
        
        for(SSEntity desc : descs){
          
          if(SSStrU.equals(activitiesEntity.get(activityID), desc)){
            activity.entity = desc;
            continue;
          }
          
          if(SSStrU.contains(activitiesUsers.get(activityID), desc)){
            activity.users.add(desc);
            continue;
          }
          
          if(SSStrU.contains(activitiesEntities.get(activityID), desc)){
            activity.entities.add(desc);
          }
        }
      }
      
      return result;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void activityAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSActivityUserAddRet.get(activityAdd(parA), parA.op), parA.op);
  }
  
  @Override
  public SSUri activityAdd(final SSServPar parA) throws Exception{
    
    try{
      final SSActivityAddPar par         = new SSActivityAddPar(parA);
      final SSUri            activityUri = SSServCaller.vocURICreate();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        activityUri,
        SSEntityE.activity,
        SSLabel.get(SSStrU.toStr(par.type)),
        null,
        null,
        false);
      
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
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return activityAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri activityContentAdd(final SSServPar parA) throws Exception{
    
    try{
      final SSActivityContentAddPar par = new SSActivityContentAddPar(parA);
      
      sqlFct.addActivityContent(
        par.activity,
        par.contentType,
        par.content);
      
      return par.activity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return activityContentAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void activityContentsAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSActivityContentsAddPar par = new SSActivityContentsAddPar(parA);
      
      for(SSActivityContent content : par.contents){
        
        SSServCaller.activityContentAdd(
          par.user,
          par.activity,
          par.contentType,
          content,
          false);
      }
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          activityContentsAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
        }
        
        return;
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSActivity activityGet(final SSServPar parA) throws Exception{
    
    try{
      final SSActivityGetPar par      = new SSActivityGetPar(parA);
      final SSActivity       activity = sqlFct.getActivity(par.activity);
      
      if(activity.entity != null){
        
        try{
          
          activity.entity =
            SSServCaller.entityDescGet(
              par.user,
              activity.entity.id,
              false,
              false,
              false,
              false,
              false,
              false,
              false);
          
        }catch(Exception error){
          SSLogU.warn("information for activity entity couldnt be retrieved");
          SSServErrReg.reset();
        }
      }
      
      final List<SSUser> users = 
        ((SSUserServerI) SSUserServ.inst.serv()).usersGet(
          new SSUsersGetPar(
            null, 
            null, 
            par.user, 
            sqlFct.getActivityUsers(activity.id), 
            false));
      
      activity.users.addAll(users);
              
      activity.contents.addAll(sqlFct.getActivityContents(activity.id));
      
      for(SSUri activityEntity : sqlFct.getActivityEntities(activity.id)){
        
        try{
          
          activity.entities.add(
            SSServCaller.entityDescGet(
              par.user,
              activityEntity,
              false,
              false,
              false,
              false,
              false,
              false,
              false));
          
        }catch(Exception error){
          SSLogU.warn("information for activity entity couldnt be retrieved");
          SSServErrReg.reset();
        }
      }
      
      return activity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}