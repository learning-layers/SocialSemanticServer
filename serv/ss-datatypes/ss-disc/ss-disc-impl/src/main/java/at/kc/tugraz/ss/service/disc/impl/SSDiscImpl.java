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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserEntryAddPar;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.service.disc.api.*;
import at.kc.tugraz.ss.service.disc.datatypes.*;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSDiscUserShareWithUserPar;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityDescriberI;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
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
import java.util.*;

public class SSDiscImpl extends SSServImplWithDBA implements SSDiscClientI, SSDiscServerI, SSEntityHandlerImplI, SSEntityDescriberI {

  private final SSDiscSQLFct sqlFct;

  public SSDiscImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception {

    super(conf, dbGraph, dbSQL);

    sqlFct = new SSDiscSQLFct(this);
  }

  /* SSEntityHandlerImplI */
  
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
    
    if(
      !SSStrU.equals(type, SSEntityE.discEntry) &&
      !SSStrU.equals(type, SSEntityE.qaEntry)   &&
      !SSStrU.equals(type, SSEntityE.chatEntry)){
      return new ArrayList<>();
    }
      
    try{
      final List<String> userDiscUris = sqlFct.getDiscURIsForUser          (user);
      final List<String> discUris     = sqlFct.getDiscURIsContainingEntry  (entity);
      
      return SSUri.get(SSStrU.retainAll(discUris, userDiscUris));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
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
  public Boolean shareUserEntity(
    final SSUri          user, 
    final List<SSUri>    usersToShareWith,
    final SSUri          entity, 
    final SSUri          circle,
    final SSEntityE      entityType) throws Exception{
    
    if(
      !SSStrU.equals(entityType, SSEntityE.disc) ||
      !SSStrU.equals(entityType, SSEntityE.chat) ||
      !SSStrU.equals(entityType, SSEntityE.qa)){
      return false;
    }
    
    for(SSUri userToShareWith : usersToShareWith){
    
      SSServCaller.discUserShareWithUser(
        user, 
        userToShareWith, 
        entity, 
        circle, 
        false);
    }
    
    return true;
  }
  
  @Override
  public Boolean addEntityToCircle(
    final SSUri        userUri, 
    final SSUri        circleUri, 
    final SSUri        entityUri, 
    final SSEntityE entityType) throws Exception{
    
    return false;
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
  public SSEntityDescA getDescForEntity(
    final SSEntityDescGetPar par,
    final SSEntityDescA      entityDesc) throws Exception{
    
    if(par.getDiscs){
      
      entityDesc.discs.addAll(
        SSServCaller.discUserDiscURIsForTargetGet(
          par.user, 
          par.entity));
    }
    
    return entityDesc;
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
    
    SSServCaller.checkKey(parA);
    
    try{
      
      final SSDiscUserEntryAddRet ret = discUserEntryAdd(parA);
      
      SSDiscActivityFct.discEntryAdd(new SSDiscUserEntryAddPar(parA), ret);
      
      sSCon.writeRetFullToClient(ret);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSDiscUserEntryAddRet discUserEntryAdd(final SSServPar parA) throws Exception{
    
    try{
      final SSDiscUserEntryAddPar par          = new SSDiscUserEntryAddPar(parA);
      SSUri                       discEntryUri = null;
      
      if(par.addNewDisc){
        
        if(par.entity != null){
          
          SSServCaller.entityAdd(
            par.user,
            par.entity, 
            null, 
            SSEntityE.entity,
            null,
            false);
        }
        
        SSDiscUserEntryAddFct.checkWhetherUserCanAddDisc(par);
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
          par.explanation);
        
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
      
      if(
        par.addNewDisc &&
        !par.users.isEmpty()){
        
        SSServCaller.entityUserShare(
          par.user,
          par.disc,
          par.users,
          null,
          false);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return SSDiscUserEntryAddRet.get(
        par.disc, 
        discEntryUri, 
        par.op);
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return discUserEntryAdd(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void discsAllGet(final SSSocketCon sSCon, final SSServPar par) throws Exception {

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSDiscsUserAllGetRet.get(discsUserAllGet(par), par.op));
  }
  
  @Override
  public List<SSDisc> discsUserAllGet(final SSServPar parA) throws Exception{

    try{
      final SSDiscsUserAllGetPar par                 = new SSDiscsUserAllGetPar(parA);
      final List<SSDisc>         discsWithoutEntries = new ArrayList<>();
      SSDisc                     disc;
      
      for(SSUri discUri : sqlFct.getDiscURIs(par.user)){
        
        disc = sqlFct.getDiscWithoutEntries(discUri);
        
        disc.attachedEntities.addAll(
          SSServCaller.entityEntitiesAttachedGet(
            par.user, 
            discUri));
        
        discsWithoutEntries.add(disc);
      }

      return discsWithoutEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void discWithEntriesGet(final SSSocketCon sSCon, final SSServPar par) throws Exception {

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSDiscUserWithEntriesRet.get(discUserWithEntriesGet(par), par.op));
  }
  
  @Override
  public SSDisc discUserWithEntriesGet(final SSServPar parA) throws Exception{

    try{
      final SSDiscUserWithEntriesGetPar par = new SSDiscUserWithEntriesGetPar(parA);
      
      if(!SSServCaller.entityUserCanRead(par.user, par.disc)){
        throw new Exception("user cannot access disc");
      }
      
      final SSDisc disc = sqlFct.getDiscWithEntries(par.disc);
      
      disc.attachedEntities.addAll(
        SSServCaller.entityEntitiesAttachedGet(
          par.user, 
          disc.id));
      
      return disc;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void discRemove(final SSSocketCon sSCon, final SSServPar parA) throws Exception {

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSDiscUserRemoveRet.get(discUserRemove(parA), parA.op));
    
    SSDiscActivityFct.removeDisc(new SSDiscUserRemovePar(parA));
  }
  
  @Override
  public SSUri discUserRemove(final SSServPar parA) throws Exception {

    try {
      final SSDiscUserRemovePar par    = new SSDiscUserRemovePar(parA);
      
      if(!SSServCaller.entityUserCanAll(par.user, par.disc)){
        throw new Exception("user cannot remove disc");
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      switch(SSServCaller.entityMostOpenCircleTypeGet(par.disc)){
      
        case priv: sqlFct.deleteDisc(par.disc);          break;
        case group: 
        case pub: sqlFct.unlinkDisc(par.user, par.disc); break;
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.disc;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return discUserRemove(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void discURIsForTargetGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSDiscUserDiscURIsForTargetGetRet.get(discUserDiscURIsForTargetGet(parA), parA.op));
  }
  
  @Override
  public List<SSUri> discUserDiscURIsForTargetGet(final SSServPar parA) throws Exception {

    try{
      final SSDiscUserDiscURIsForTargetGetPar par = new SSDiscUserDiscURIsForTargetGetPar(parA);
      
      if(!SSServCaller.entityUserCanRead(par.user, par.entity)){
        throw new Exception("user cannot access target");
      }
    
      return sqlFct.getDiscURIs(par.user, par.entity);
    } catch (Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSUri discUserShareWithUser(final SSServPar parA) throws Exception {
 
    try{
      final SSDiscUserShareWithUserPar par = new SSDiscUserShareWithUserPar(parA);

      if(!SSServCaller.entityUserCanEdit(par.user, par.entity)){
        throw new Exception("user cannot share this entity");
      }

      if(sqlFct.ownsUserDisc(par.forUser, par.entity)){
        throw new Exception("disc is already shared with user");
      }

      dbSQL.startTrans(par.shouldCommit);

      SSDiscMiscFct.shareDiscWithUser(
        sqlFct,
        par.user,
        par.forUser,
        par.entity,
        par.circle);

      dbSQL.commit(par.shouldCommit);

      return par.entity;

    }catch(SSSQLDeadLockErr deadLockErr){

      if(dbSQL.rollBack(parA)){
        return discUserShareWithUser(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }

    }catch(Exception error){
      dbSQL.rollBack(parA);
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
            par.maxEntries));
      }

      return discsWithEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}