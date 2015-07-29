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
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
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
import at.kc.tugraz.ss.service.userevent.impl.fct.misc.SSUEMiscFct;
import at.kc.tugraz.ss.service.userevent.impl.fct.sql.SSUESQLFct;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import java.util.*;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;

public class SSUEImpl 
extends SSServImplWithDBA 
implements 
  SSUEClientI, 
  SSUEServerI, 
  SSDescribeEntityI, 
  SSUsersResourcesGathererI{
  
  private final SSUESQLFct   sqlFct;
  private final SSUEMiscFct  fct;
  
  public SSUEImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct   = new SSUESQLFct  (this);
    this.fct      = new SSUEMiscFct ();
  }
  
  @Override
  public void getUsersResources(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> usersResources) throws Exception{
    
    final List<SSUEE> ueTypes = new ArrayList<>();
    
    SSUri userUri;
    
    ueTypes.add(SSUEE.evernoteNotebookUpdate);
    ueTypes.add(SSUEE.evernoteNotebookFollow);
    ueTypes.add(SSUEE.evernoteNoteUpdate);
    ueTypes.add(SSUEE.evernoteNoteDelete);
    ueTypes.add(SSUEE.evernoteNoteShare);
    ueTypes.add(SSUEE.evernoteReminderDone);
    ueTypes.add(SSUEE.evernoteReminderCreate);
    ueTypes.add(SSUEE.evernoteResourceAdd);
      
    for(String user : allUsers){
      
      userUri = SSUri.get(user);
      
      for(SSEntity ue : 
        userEventsGet(
          new SSUEsGetPar(
            null, 
            null, 
            userUri, 
            userUri, 
            null, 
            ueTypes,
            null, 
            null, 
            false, 
            false))){
        
        if(usersResources.containsKey(user)){
          usersResources.get(user).add(((SSUE)ue).entity.id);
        }else{
          
          final List<SSUri> resourceList = new ArrayList<>();
          
          resourceList.add(((SSUE)ue).entity.id);
          
          usersResources.put(user, resourceList);
        }
      }
    }
    
    for(Map.Entry<String, List<SSUri>> resourcesPerUser : usersResources.entrySet()){
      SSStrU.distinctWithoutNull2(resourcesPerUser.getValue());
    }
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setUEs){
        
        entity.userEvents.addAll(
          userEventsGet(
            new SSUEsGetPar(
              null,
              null,
              par.user,
              null, //forUser,
              entity.id,
              null, //types,
              null, //startTime,
              null, //endTime,
              par.withUserRestriction,
              false))); //invokeEntityHandlers
      }
      
      switch(entity.type){
        
        case userEvent:{
          
          return SSUE.get(
            userEventGet(
              new SSUEGetPar(
                null,
                null,
                par.user,
                entity.id,
                par.withUserRestriction,
                false)),
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
  public void userEventGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSUEGetPar par = (SSUEGetPar) parA.getFromJSON(SSUEGetPar.class);
    
    sSCon.writeRetFullToClient(SSUEGetRet.get(userEventGet(par)));
  }
    
  @Override
  public SSUE userEventGet(final SSUEGetPar par) throws Exception {
    
    try{
      
      final SSUE userEvent =
        SSUE.get(
          sqlFct.getUE(par.userEvent),
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              par.userEvent,
              par.withUserRestriction,
              null))); //descPar
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, userEvent.entity.id)){
          return null;
        }
      }
        
      final SSEntityDescriberPar descPar;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(null);
      }else{
        descPar = null;
      }
      
      userEvent.entity =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
          new SSEntityGetPar(
            null,
            null,
            par.user,
            userEvent.entity.id,
            par.withUserRestriction,
            descPar));
      
      userEvent.user =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
          new SSEntityGetPar(
            null,
            null,
            par.user,
            userEvent.user.id,
            par.withUserRestriction,
            descPar));
      
      return userEvent;
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void userEventsGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSUEsGetPar par = (SSUEsGetPar) parA.getFromJSON(SSUEsGetPar.class);
      
    sSCon.writeRetFullToClient(SSUEsGetRet.get(userEventsGet(par)));
  }
  
  @Override
  public List<SSEntity> userEventsGet(final SSUEsGetPar par) throws Exception {

    try{
      
      if(par.withUserRestriction){
        
        if(par.entity != null){
          if(!SSServCallerU.canUserRead(par.user, par.user)){
            return new ArrayList<>();
          }
        }
      }
      
      final List<SSEntity> userEvents    = new ArrayList<>();
      final List<SSUri>    userEventURIs =
        sqlFct.getUEURIs(
          par.forUser,
          par.entity,
          par.types,
          par.startTime,
          par.endTime);
      
      for(SSUri userEventURI : userEventURIs){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          userEvents, 
          userEventGet(
            new SSUEGetPar(
              null, 
              null, 
              par.user, 
              userEventURI, 
              par.withUserRestriction, 
              par.invokeEntityHandlers)));
      }

      return userEvents;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void userEventCountGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSUECountGetPar par = (SSUECountGetPar) parA.getFromJSON(SSUECountGetPar.class);
    
    sSCon.writeRetFullToClient(SSUECountGetRet.get(userEventCountGet(par)));
  }
  
  @Override
  public Integer userEventCountGet(final SSUECountGetPar par) throws Exception {

    //TODO dtheiler: count via db then: count(p.catId) as the_count 
    
    try{

      if(par.withUserRestriction){
        
        if(par.entity == null){
          throw new SSErr(SSErrE.parameterMissing);
        }
        
        if(!SSServCallerU.canUserRead(par.user, par.entity)){
          return -1;
        }
      }
      
      return sqlFct.getUEURIs(
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
  public void userEventAdd(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSUEAddPar par = (SSUEAddPar) parA.getFromJSON(SSUEAddPar.class);
    
    sSCon.writeRetFullToClient(SSUEAddRet.get(userEventAdd(par)));
  }
  
  @Override
  public SSUri userEventAdd(final SSUEAddPar par) throws Exception{
    
    try{
      final SSUri ueUri = SSServCaller.vocURICreate();
      
      if(par.entity == null){
        par.entity = SSUri.get(SSVocConf.sssUri);
      }else{

        if(!SSServCallerU.canUserRead(par.user, par.entity)){
          return null;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          ueUri,
          SSEntityE.userEvent, //type,
          null, //label
          null,//description,
          null, //entitiesToAttach,
          par.creationTime, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          par.entity,
          null, //type,
          null, //label
          null,//description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      sqlFct.addUE(
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