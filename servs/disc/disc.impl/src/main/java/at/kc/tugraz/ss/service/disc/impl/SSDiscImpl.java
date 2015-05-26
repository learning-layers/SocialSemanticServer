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
package at.kc.tugraz.ss.service.disc.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivEntityAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleTypesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.circle.serv.SSCircleServ;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserEntryAddPar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.kc.tugraz.ss.service.disc.api.*;
import at.kc.tugraz.ss.service.disc.datatypes.*;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryURIsGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserDiscURIsForTargetGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserRemovePar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsUserAllGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserDiscURIsForTargetGetRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserEntryAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserRemoveRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserWithEntriesRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscsUserAllGetRet;
import at.kc.tugraz.ss.service.disc.impl.fct.activity.SSDiscActivityFct;
import at.kc.tugraz.ss.service.disc.impl.fct.misc.SSDiscMiscFct;
import at.kc.tugraz.ss.service.disc.impl.fct.op.SSDiscUserEntryAddFct;
import at.kc.tugraz.ss.service.disc.impl.fct.sql.SSDiscSQLFct;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.*;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSWarnE;

public class SSDiscImpl 
extends SSServImplWithDBA 
implements 
  SSDiscClientI, 
  SSDiscServerI, 
  SSEntityHandlerImplI, 
  SSEntityDescriberI, 
  SSUserRelationGathererI,
  SSUsersResourcesGathererI{

  private final SSDiscSQLFct sqlFct;

  public SSDiscImpl(final SSConfA conf) throws Exception {

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    sqlFct = new SSDiscSQLFct(this);
  }

  @Override
  public void getUserRelations(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> userRelations) throws Exception{
    
    List<SSEntityCircle>           discUserCircles;
    List<SSDisc>                   allDiscs;
    
    for(String user : allUsers){
      
      final SSUri userUri = SSUri.get(user);
      
      allDiscs = SSServCaller.discsUserAllGet(userUri);
      
      for(SSDisc disc : allDiscs){
        
        discUserCircles =
          ((SSCircleServerI) SSCircleServ.inst.serv()).circlesGet(
            new SSCirclesGetPar(
              null,
              null,
              userUri,
              userUri,
              disc.id,
              SSEntityE.asListWithoutNullAndEmpty(),
              false,
              true,
              false));
        
        for(SSEntityCircle circle : discUserCircles){
          
          if(userRelations.containsKey(user)){
            userRelations.get(user).addAll(SSUri.getFromEntitites(circle.users));
          }else{
            userRelations.put(user, SSUri.getFromEntitites(circle.users));
          }
        }
      }
    }
    
    for(Map.Entry<String, List<SSUri>> usersPerUser : userRelations.entrySet()){
      SSStrU.distinctWithoutNull2(usersPerUser.getValue());
    }
  }
  
  @Override
  public void getUsersResources(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> usersResources) throws Exception{
    
    for(String user : allUsers){
      
      for(SSDisc disc : SSServCaller.discsUserAllGet(SSUri.get(user))){
        
        if(usersResources.containsKey(user)){
          usersResources.get(user).add(disc.id);
        }else{
          
          final List<SSUri> resourceList = new ArrayList<>();
          
          resourceList.add(disc.id);
          
          usersResources.put(user, resourceList);
        }
      }
    }
    
    for(Map.Entry<String, List<SSUri>> resourcesPerUser : usersResources.entrySet()){
      SSStrU.distinctWithoutNull2(resourcesPerUser.getValue());
    }
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
  public List<SSUri> getSubEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{

    if(
      !SSStrU.equals(type, SSEntityE.disc) &&
      !SSStrU.equals(type, SSEntityE.qa)   &&
      !SSStrU.equals(type, SSEntityE.chat)){
      return null;
    }
    
    try{
      return sqlFct.getDiscEntryURIs(entity);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> getParentEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    switch(type){
      
      case discEntry:
      case qaEntry:
      case chatEntry:{
        
        try{
          final List<String> userDiscUris = sqlFct.getDiscURIsForUser          (user);
          final List<String> discUris     = sqlFct.getDiscURIsContainingEntry  (entity);
          
          return SSUri.get(SSStrU.retainAll(discUris, userDiscUris));
        }catch(Exception error){
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
    }
    
    return new ArrayList<>();
  }

  @Override
  public Boolean setUserEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri, 
    final SSEntityE      entityType,
    final SSUri          publicCircleUri) throws Exception{

    return false;
  }
  
  @Override
  public void shareUserEntity(
    final SSUri          user, 
    final List<SSUri>    usersToShareWith,
    final SSUri          entity, 
    final SSUri          circle,
    final SSEntityE      entityType,
    final Boolean        saveActivity) throws Exception{
    
    switch(entityType){
      case disc:
      case chat:
      case qa:
       
        for(SSUri userToShareWith : usersToShareWith){
          
          if(sqlFct.ownsUserDisc(userToShareWith, entity)){
            SSLogU.warn(SSWarnE.discAlreadySharedWithUser);
            return;
          }
          
          sqlFct.addDisc(entity, userToShareWith);
          
          ((SSCircleServerI) SSCircleServ.inst.serv()).circleEntitiesAdd(
            new SSCircleEntitiesAddPar(
              null,
              null,
              user,
              circle,
              SSDiscMiscFct.getDiscContentURIs(sqlFct, entity),
              false,
              false,
              false));
          
          ((SSCircleServerI) SSCircleServ.inst.serv()).circleUsersAdd(
              new SSCircleUsersAddPar(
                null,
                null, 
                user, 
                circle, 
                sqlFct.getDiscUserURIs(entity), 
                false, 
                false));
        }
    }
  }
  
  @Override
  public void shareUserEntityWithCircle(
    final SSUri        user, 
    final SSUri        circle, 
    final SSUri        entity,
    final SSEntityE    type) throws Exception{
    
    switch(type){
      case qa:
      case disc:
      case chat:{
        
        ((SSCircleServerI) SSCircleServ.inst.serv()).circleEntitiesAdd(
            new SSCircleEntitiesAddPar(
              null,
              null,
              user,
              circle,
              SSDiscMiscFct.getDiscContentURIs(sqlFct, entity),
              false,
              false,
              false));
      }
    }
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
  public SSEntity getUserEntity(final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setDiscs){
        
        par.entity.discs.addAll(
          SSServCaller.discUserDiscURIsForTargetGet(
            par.user,
            par.entity.id));
      }
      
      return par.entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override 
  public List<SSUri> discEntryURIsGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSDiscEntryURIsGetPar par = new SSDiscEntryURIsGetPar(parA);
      
      return sqlFct.getDiscEntryURIs(par.disc);

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void discEntryAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSDiscUserEntryAddRet ret = discUserEntryAdd(parA);
    
    SSDiscActivityFct.discEntryAdd(SSDiscUserEntryAddPar.get(parA), ret);
    
    sSCon.writeRetFullToClient(ret, parA.op);
  }
  
  @Override
  public SSDiscUserEntryAddRet discUserEntryAdd(final SSServPar parA) throws Exception{
    
    try{
      final SSDiscUserEntryAddPar par          = SSDiscUserEntryAddPar.get(parA);
      SSUri                       discEntryUri = null;
      
      if(par.addNewDisc){
        
        SSDiscUserEntryAddFct.checkWhetherUserCanAddDisc(par);
        
        if(par.entity != null){
          
          ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
            new SSCirclePrivEntityAddPar(
              null,
              null,
              par.user,
              par.entity,
              SSEntityE.entity,
              null,
              null,
              null,
              false));
        }
      }
      
      if(!par.addNewDisc){
        SSDiscUserEntryAddFct.checkWhetherUserCanAddDiscEntry(par);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(par.addNewDisc){
        
        par.disc = SSServCaller.vocURICreate();
        
        SSDiscUserEntryAddFct.addDisc(
          sqlFct,
          par.disc,
          par.user,
          par.entity,
          par.type,
          par.label,
          par.description);
        
        if(!par.entities.isEmpty()){
        
          SSServCaller.entityUserEntitiesAttach(
            par.user, 
            par.disc, 
            par.entities,
            false);  
        }
      }   
      
      if(par.entry != null){
        
        discEntryUri = 
          SSDiscUserEntryAddFct.addDiscEntry(
            sqlFct,
            par.user,
            par.disc,
            par.entry);
        
        if(!par.entities.isEmpty()){
        
          SSServCaller.entityUserEntitiesAttach(
            par.user, 
            discEntryUri, 
            par.entities,
            false);  
        }
      }
      
      if(par.addNewDisc){
        
        SSServCaller.circleEntityShare(
          par.user,
          par.disc,
          par.users,
          par.circles,
          null,
          false);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return SSDiscUserEntryAddRet.get(
        par.disc, 
        discEntryUri, 
        par.op);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return discUserEntryAdd(parA);
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
  public void discsAllGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSDiscsUserAllGetRet.get(discsUserAllGet(parA), parA.op), parA.op);
  }
  
  @Override
  public List<SSDisc> discsUserAllGet(final SSServPar parA) throws Exception{

    try{
      final SSDiscsUserAllGetPar par                 = SSDiscsUserAllGetPar.get(parA);
      final List<SSDisc>         discsWithoutEntries = new ArrayList<>();
      SSDisc                     disc;
      
      for(SSUri discUri : sqlFct.getDiscURIs(par.user)){
        
        disc = sqlFct.getDiscWithoutEntries(discUri);
        
        disc.attachedEntities.addAll(
          SSServCaller.entityEntitiesAttachedGet(
            par.user,
            discUri));
        
        disc.circleTypes.addAll(
          ((SSCircleServerI) SSCircleServ.inst.serv()).circleTypesGet(
            new SSCircleTypesGetPar(
              null,
              null,
              par.user,
              par.user,
              disc.id,
              false)));
            
        discsWithoutEntries.add(disc);
      }
      
      return discsWithoutEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void discWithEntriesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSDiscUserWithEntriesRet.get(discUserWithEntriesGet(parA), parA.op), parA.op);
  }
  
  @Override
  public SSDisc discUserWithEntriesGet(final SSServPar parA) throws Exception{

    try{
      final SSDiscUserWithEntriesGetPar par = SSDiscUserWithEntriesGetPar.get(parA);
      SSDiscEntry                       discEntry;
      
      SSServCallerU.canUserReadEntity(par.user, par.disc);
      
      final SSDisc disc = sqlFct.getDiscWithEntries(par.disc);
      
      disc.attachedEntities.addAll(
        SSServCaller.entityEntitiesAttachedGet(
          par.user,
          disc.id));
      
      for(Object entry : disc.entries){
        
        discEntry = (SSDiscEntry) entry;
        
        discEntry.attachedEntities.addAll(
          SSServCaller.entityEntitiesAttachedGet(
            par.user,
            discEntry.id));
        
        discEntry.likes = 
          SSServCaller.likesUserGet(
            par.user, 
            null, 
            discEntry.id);
        
        if(par.includeComments){
         
          discEntry.comments.addAll(
            SSServCaller.commentsUserGet(
              par.user,
              null,
              discEntry.id));
        }
      }
      
      return disc;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void discRemove(final SSSocketCon sSCon, final SSServPar parA) throws Exception {

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSDiscUserRemoveRet.get(discUserRemove(parA), parA.op), parA.op);
    
//    SSDiscActivityFct.removeDisc(new SSDiscUserRemovePar(parA));
  }
  
  @Override
  public SSUri discUserRemove(final SSServPar parA) throws Exception {

    try {
      final SSDiscUserRemovePar par    = new SSDiscUserRemovePar(parA);

      SSServCallerU.canUserAllEntity(par.user, par.disc);
      
      dbSQL.startTrans(par.shouldCommit);
      
      switch(
        ((SSCircleServerI) SSCircleServ.inst.serv()).circleMostOpenCircleTypeGet(
          new SSCircleMostOpenCircleTypeGetPar(
            null, 
            null, 
            par.user, 
            par.user, 
            par.disc, 
            false))){
      
        case priv: sqlFct.deleteDisc(par.disc);          break;
        case group: 
        case pub: sqlFct.unlinkDisc(par.user, par.disc); break;
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.disc;
     }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return discUserRemove(parA);
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
  public void discURIsForTargetGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSDiscUserDiscURIsForTargetGetRet.get(discUserDiscURIsForTargetGet(parA), parA.op), parA.op);
  }
  
  @Override
  public List<SSUri> discUserDiscURIsForTargetGet(final SSServPar parA) throws Exception {

    try{
      final SSDiscUserDiscURIsForTargetGetPar par = SSDiscUserDiscURIsForTargetGetPar.get(parA);

      SSServCallerU.canUserReadEntity(par.user, par.entity);
      
      return sqlFct.getDiscURIs(par.user, par.entity);
    } catch (Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSDisc> discsUserWithEntriesGet(final SSServPar parA) throws Exception{

    try{
      final SSDiscsWithEntriesGetPar par              = new SSDiscsWithEntriesGetPar(parA);
      final List<SSDisc>             discsWithEntries = new ArrayList<>();
      
      for(SSDisc disc : SSServCaller.discsUserAllGet(par.user)){

        discsWithEntries.add(
          SSServCaller.discUserWithEntriesGet(
            par.user,
            disc.id,
            par.maxEntries,
            false));
      }

      return discsWithEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}