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

import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.serv.entity.api.SSUsersResourcesGathererI;

import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import at.tugraz.sss.conf.SSConf;
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
import at.tugraz.sss.serv.datatype.enums.SSClientE;

import at.tugraz.sss.serv.db.api.SSDBNoSQLI;

import at.tugraz.sss.serv.entity.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.SSEntityContext;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import java.util.*;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.par.SSServPar;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI;

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
    
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql          = new SSUESQLFct(dbSQL, SSConf.systemUserUri);
    this.entityServ   = (SSEntityServerI)   SSServReg.getServ(SSEntityServerI.class);
    this.userCommons  = new SSUserCommons();
  }
  
  @Override
  public void getUsersResources(
    final SSServPar servPar,
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
          servPar,
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
    final SSServPar servPar,
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(par.setUEs){
        
        entity.userEvents.addAll(
          userEventsGet(
            new SSUEsGetPar(
              servPar,
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
                servPar,
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
      
      final SSUEGetPar par = (SSUEGetPar) parA.getFromClient(clientType, parA, SSUEGetPar.class);
      
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
          par,
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
      
      final SSUEsGetPar par = (SSUEsGetPar) parA.getFromClient(clientType, parA, SSUEsGetPar.class);
      
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
        
        ue = sql.getUE(par, ueURI);
        
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
    
    final SSUECountGetPar par = (SSUECountGetPar) parA.getFromClient(clientType, parA, SSUECountGetPar.class);
    
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
            par,
            par.user,
            par.entity,
            par.withUserRestriction);
        
        if(entity == null){
          return -1;
        }
      }
      
      return sql.getUEURIs(
        par, 
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
      
      final SSUEAddPar par = (SSUEAddPar) parA.getFromClient(clientType, parA, SSUEAddPar.class);
      
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
        par.entity = SSUri.get(SSConf.sssUri);
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri entity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      final SSUri ueUri =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
            par.user,
            SSConf.vocURICreate(),
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sql.addUE(
        par, 
        ueUri,
        par.user,
        par.entity,
        par.type, 
        par.content);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return ueUri;
    }catch(SSErr error){
      
      switch(error.code){

        case sqlDeadLock:{
          
          try{
            dbSQL.rollBack(par, par.shouldCommit);
            SSServErrReg.regErrThrow(error);
            return null;
          }catch(Exception error2){
            SSServErrReg.regErrThrow(error2);
            return null;
          }
        }
        
        default:{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
    }catch(Exception error){
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