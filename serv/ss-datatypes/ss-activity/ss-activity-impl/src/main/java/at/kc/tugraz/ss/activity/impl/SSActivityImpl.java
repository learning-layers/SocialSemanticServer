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

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.activity.api.SSActivityClientI;
import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.SSActivity;
import at.kc.tugraz.ss.activity.datatypes.SSActivityContent;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivitiesUserGetPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityContentAddPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityContentsAddPar;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivitiesUserGetRet;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivityTypesGetRet;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivityUserAddRet;
import at.kc.tugraz.ss.activity.impl.fct.sql.SSActivitySQLFct;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import sss.serv.err.datatypes.SSErrE;

public class SSActivityImpl extends SSServImplWithDBA implements SSActivityClientI, SSActivityServerI, SSEntityHandlerImplI{

  private final SSActivitySQLFct sqlFct;

  public SSActivityImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{

    super(conf, dbGraph, dbSQL);

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
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSActivityTypesGetRet.get(activityTypesGet(parA), parA.op));
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
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSActivitiesUserGetRet.get(activitiesUserGet(parA), parA.op));
  }
  
  @Override
  public List<SSActivity> activitiesUserGet(final SSServPar parA) throws Exception{
    
    try{
      final SSActivitiesUserGetPar par         = new SSActivitiesUserGetPar(parA);
      final List<SSUri>            entities    = new ArrayList<>();
      
      if(!par.entities.isEmpty()){
        
        for(SSUri entity : par.entities){
          
          try{
            SSServCaller.entityUserCanRead(par.user, entity);
          }catch(Exception error){
            SSServErrReg.reset();
            SSLogU.warn("user cannot access entity for activities");
            continue;
          }
          
          entities.add(entity);
        }
      }
      
      if(!par.circles.isEmpty()){
        
        for(SSUri circle : par.circles){
          
          try{
            SSServCaller.entityUserCanRead(par.user, circle);
          }catch(Exception error){
            SSServErrReg.reset();
            SSLogU.warn("user cannot access circle for activities");
            continue;
          }
          
          entities.addAll(
            SSServCaller.circleGet(
              par.user, 
              par.user, 
              circle, 
              false,
              true).entities);
        }
      }

      SSStrU.distinctWithoutNull2(entities);
      
      return sqlFct.getActivities(
          par.users,
          entities,
          par.types,
          par.startTime,
          par.endTime);

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  
  @Override
  public void activityAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSActivityUserAddRet.get(activityAdd(parA), parA.op));
  }
  
  @Override
  public SSUri activityAdd(final SSServPar parA) throws Exception{

    try{
      final SSActivityAddPar par = new SSActivityAddPar(parA);
      
      final SSUri activityUri = SSServCaller.vocURICreate();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntityToPrivCircleAdd(
        par.user, 
        activityUri, 
        SSEntityE.activity, 
        SSLabel.get(SSStrU.toStr(par.type)), 
        null, 
        par.creationTime, 
        false);
      
      //TODO dhteiler: remove possibly same user par.user from par.users
      
      sqlFct.addActivity(
        par.user,
        activityUri, 
        par.type, 
        par.users, 
        par.entities, 
        par.comments);
      
      dbSQL.commit(par.shouldCommit);
      
      return activityUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return activityAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSUri activityContentAdd(final SSServPar parA) throws Exception{
    
    try{
      final SSActivityContentAddPar par = new SSActivityContentAddPar(parA);
      
      sqlFct.addActivityContent(
        par.user, 
        par.activity, 
        par.contentType, 
        par.content);
      
      return par.activity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return activityContentAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          activityContentsAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
        }
        
        return;
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
    }
  }
}

