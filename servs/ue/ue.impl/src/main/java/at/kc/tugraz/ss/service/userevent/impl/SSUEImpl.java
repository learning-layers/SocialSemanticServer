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
 package at.kc.tugraz.ss.service.userevent.impl;

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;

import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.kc.tugraz.ss.service.userevent.api.*;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUECountGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEAddRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUECountGetRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEGetRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEsGetRet;
import at.kc.tugraz.ss.service.userevent.impl.fct.sql.SSUESQLFct;
import at.tugraz.sss.serv.SSClientE;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityContext;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import java.util.*;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServRetI; import at.tugraz.sss.serv.SSVarNames;

public class SSUEImpl 
extends SSServImplWithDBA 
implements 
  SSUEClientI, 
  SSUEServerI, 
  SSDescribeEntityI, 
  SSUsersResourcesGathererI{
  
  private final SSUESQLFct        sql;
  private final SSEntityServerI   entityServ;
  private final SSUserCommons  userCommons;
  
  public SSUEImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.getServImpl(), (SSDBNoSQLI) SSDBNoSQL.inst.getServImpl());
    
    this.sql          = new SSUESQLFct(dbSQL, SSVocConf.systemUserUri);
    this.entityServ   = (SSEntityServerI)   SSServReg.getServ(SSEntityServerI.class);
    this.userCommons  = new SSUserCommons();
  }
  
  @Override
  public void getUsersResources(
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      final List<SSUEE> ueTypes = new ArrayList<>();
      
      ueTypes.add(SSUEE.evernoteNotebookUpdate);
      ueTypes.add(SSUEE.evernoteNotebookFollow);
      ueTypes.add(SSUEE.evernoteNoteUpdate);
      ueTypes.add(SSUEE.evernoteNoteDelete);
      ueTypes.add(SSUEE.evernoteNoteShare);
      ueTypes.add(SSUEE.evernoteReminderDone);
      ueTypes.add(SSUEE.evernoteReminderCreate);
      ueTypes.add(SSUEE.evernoteResourceAdd);
      ueTypes.add(SSUEE.bnpPlaceholderAdd);
      
      final SSUEsGetPar uesGetPar =
        new SSUEsGetPar(
          null, //user
          null, //userEvents,
          null, //forUser
          null, //entity
          ueTypes, //types
          null, //startTime
          null, //endTime
          false, //withUserRestriction
          false); //invokeEntityHandlers
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        uesGetPar.user    = userID;
        uesGetPar.forUser = userID;
        
        for(SSEntity ue : userEventsGet(uesGetPar)){
          
          usersEntities.get(user).add(
            new SSEntityContext(
              ((SSUE) ue).entity.id,
              SSEntityE.userEvent,
              null,
              null));
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(par.setUEs){
        
        entity.userEvents.addAll(
          userEventsGet(
            new SSUEsGetPar(
              par.user,
              null, //userEvents
              null, //forUser,
              entity.id, //entity
              null, //types,
              null, //startTime,
              null, //endTime,
              par.withUserRestriction,
              false))); //invokeEntityHandlers
      }
      
      switch(entity.type){
        
        case userEvent:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          final SSUE ue =
            userEventGet(
              new SSUEGetPar(
                par.user,
                entity.id,
                par.withUserRestriction,
                false));
          
          if(ue == null){
            return entity;
          }
          
          return SSUE.get(
            ue,
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
  public SSServRetI userEventGet(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSUEGetPar par = (SSUEGetPar) parA.getFromJSON(SSUEGetPar.class);
      
      return SSUEGetRet.get(userEventGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
    
  @Override
  public SSUE userEventGet(final SSUEGetPar par) throws SSErr {
    
    try{
      
      final SSUEsGetPar uesGetPar =
        new SSUEsGetPar(
          par.user,
          SSUri.asListNotNull(par.userEvent),  //userEvents
          null, //forUser,
          null, //entity,
          null, //types,
          null, //startTime,
          null, //endTime,
          par.withUserRestriction,
          par.invokeEntityHandlers);
      
      uesGetPar.setFlags = par.setFlags;
      uesGetPar.setTags  = par.setTags;
      
      final List<SSEntity> ues = userEventsGet(uesGetPar);
      
      if(ues.isEmpty()){
        return null;
      }
      
      return (SSUE) ues.get(0);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI userEventsGet(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSUEsGetPar par = (SSUEsGetPar) parA.getFromJSON(SSUEsGetPar.class);
      
      return SSUEsGetRet.get(userEventsGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> userEventsGet(final SSUEsGetPar par) throws SSErr{

    try{
      
      final List<SSEntity> ues    = new ArrayList<>();
      final SSUEsGetFct    fct    = new SSUEsGetFct(entityServ, sql);
      SSUE                 ue;
      
      for(SSUri ueURI : fct.getUEsToFill(par)){
        
        ue = sql.getUE(ueURI);
        
        if(ue == null){
          continue;
        }
        
        fct.setUEEntity(par, ue);
        
        if(ue.entity == null){
          continue;
        }
        
        fct.setUEUser(par, ue);
        
        ues.add(ue);
      }

      return ues;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI userEventCountGet(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      
    userCommons.checkKeyAndSetUser(parA);
    
    final SSUECountGetPar par = (SSUECountGetPar) parA.getFromJSON(SSUECountGetPar.class);
    
    return SSUECountGetRet.get(userEventCountGet(par));
    
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Integer userEventCountGet(final SSUECountGetPar par) throws SSErr {

    //TODO dtheiler: count via db then: count(p.catId) as the_count 
    
    try{

      if(par.withUserRestriction){
        
        if(par.entity == null){
          throw SSErr.get(SSErrE.parameterMissing);
        }
        
        SSEntity entity =
          sql.getEntityTest(
            par.user,
            par.entity,
            par.withUserRestriction);
        
        if(entity == null){
          return -1;
        }
      }
      
      return sql.getUEURIs(
        par.forUser,
        par.entity,
        SSUEE.asListWithoutEmptyAndNull(par.type),
        par.startTime,
        par.endTime).size();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI userEventAdd(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSUEAddPar par = (SSUEAddPar) parA.getFromJSON(SSUEAddPar.class);
      
      return SSUEAddRet.get(userEventAdd(par));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri userEventAdd(final SSUEAddPar par) throws SSErr{
    
    try{

      if(par.entity == null){
        par.entity = SSUri.get(SSVocConf.sssUri);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri entity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.entity,
            null, //type,
            null, //label
            null,//description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(entity == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      final SSUri ueUri =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            SSVocConf.vocURICreate(),
            SSEntityE.userEvent, //type,
            null, //label
            null,//description,
            par.creationTime, //creationTime,
            null, //read,
            true, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(ueUri == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sql.addUE(
        ueUri,
        par.user,
        par.entity,
        par.type, 
        par.content);
      
      dbSQL.commit(par.shouldCommit);
      
      return ueUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return userEventAdd(par);
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

//  public static List<SSUE> filterUEs(
//    final List<SSUE> ues, 
//    final Long       startTime, 
//    final Long       endTime){
//    
//    final List<SSUE> uesAfterStartTimeExclusion = new ArrayList<SSUE>();
//    final List<SSUE> uesAfterEndTimeExclusion   = new ArrayList<SSUE>();
//    
//    if(startTime != null){
//      
//      for(SSUE ue : ues){
//      
//        if(ue.timestamp > startTime){
//          uesAfterStartTimeExclusion.add(ue);
//        }
//      }
//    }else{
//      uesAfterStartTimeExclusion.addAll(ues);
//    }
//    
//    if(endTime != null){
//      
//      for(SSUE ue : uesAfterStartTimeExclusion){
//      
//        if(ue.timestamp < endTime){
//          uesAfterEndTimeExclusion.add(ue);
//        }
//      }
//    }else{
//      uesAfterEndTimeExclusion.addAll(uesAfterStartTimeExclusion);
//    }
//    
//    return uesAfterEndTimeExclusion;
//  }