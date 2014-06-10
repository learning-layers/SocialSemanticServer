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

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserEntryAddPar;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.service.disc.api.*;
import at.kc.tugraz.ss.service.disc.datatypes.*;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserDiscURIsForTargetGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserRemovePar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsUserAllGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserDiscURIsForTargetGetRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserEntryAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserRemoveRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserWithEntriesRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscsUserAllGetRet;
import at.kc.tugraz.ss.service.disc.impl.fct.activity.SSDiscActivityFct;
import at.kc.tugraz.ss.service.disc.impl.fct.op.SSDiscUserEntryAddFct;
import at.kc.tugraz.ss.service.disc.impl.fct.sql.SSDiscSQLFct;
import java.util.*;

public class SSDiscImpl extends SSServImplWithDBA implements SSDiscClientI, SSDiscServerI, SSEntityHandlerImplI {

  private final SSDiscSQLFct sqlFct;

  public SSDiscImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception {

    super(conf, dbGraph, dbSQL);

    sqlFct = new SSDiscSQLFct(this);
  }

  /* SSEntityHandlerImplI */
  
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

    return false;
  }
  
  @Override
  public Boolean shareUserEntity(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          entityCircleUri,
    final SSEntityE   entityType) throws Exception{
    
    return false;
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
    final SSEntityE        entityType,
    final SSUri            userUri,
    final SSUri            entityUri,
    final SSLabel          label,
    final Long             creationTime,
    final List<String>     tags,
    final SSEntityA        overallRating,
    final List<SSUri>      discUris,
    final SSUri            author) throws Exception {

    if (SSEntityE.equals(entityType, SSEntityE.disc)) {

      return SSDiscDesc.get(
        entityUri,
        label,
        creationTime,
        tags,
        overallRating,
        author, 
        discUris);
    }

    if(SSEntityE.equals(entityType, SSEntityE.discEntry)) {

      return SSDiscEntryDesc.get(
        entityUri,
        label,
        creationTime,
        author, 
        overallRating, 
        tags, 
        discUris);
    }

    return SSEntityDesc.get(entityUri, label, creationTime, tags, overallRating, discUris, author);
  }

  /* SSDiscClientI */
  
  @Override
  public void discEntryAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    try{
      dbSQL.startTrans(true);
      
      parA.shouldCommit = false;
      
      final SSDiscUserEntryAddPar par = new SSDiscUserEntryAddPar(parA);
      final SSDiscUserEntryAddRet ret = discUserEntryAdd(par);
      
      SSDiscActivityFct.discEntryAdd(par, ret);
      
      dbSQL.commit(true);
      
      sSCon.writeRetFullToClient(ret);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public void discsAllGet(final SSSocketCon sSCon, final SSServPar par) throws Exception {

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSDiscsUserAllGetRet.get(discsUserAllGet(par), par.op));
  }

  @Override
  public void discWithEntriesGet(final SSSocketCon sSCon, final SSServPar par) throws Exception {

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSDiscUserWithEntriesRet.get(discUserWithEntriesGet(par), par.op));
  }
  
  @Override
  public void discRemove(final SSSocketCon sSCon, final SSServPar parA) throws Exception {

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSDiscUserRemoveRet.get(discUserRemove(parA), parA.op));
  }
  
  @Override
  public void discURIsForTargetGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSDiscUserDiscURIsForTargetGetRet.get(discUserDiscURIsForTargetGet(parA), parA.op));
  }

  /* SSDiscServerI */
  
  @Override
  public List<SSUri> discUserDiscURIsForTargetGet(final SSServPar parA) throws Exception {

    try{
      final SSDiscUserDiscURIsForTargetGetPar par = new SSDiscUserDiscURIsForTargetGetPar(parA);
      
      if(!SSServCaller.entityUserCanRead(par.user, par.entity)){
        return new ArrayList<SSUri>();
      }
    
      return sqlFct.getDiscURIs(par.user, par.entity);
    } catch (Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
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

  protected SSDiscUserEntryAddRet discUserEntryAdd(final SSDiscUserEntryAddPar par) throws Exception {
    
    try{
      SSUri discEntryUri = null;
      
      if(par.addNewDisc){
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
      }      
      
      if(par.entry != null){
        
        discEntryUri = 
          SSDiscUserEntryAddFct.addDiscEntry(
            sqlFct,
            par.user,
            par.disc,
            par.entry);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return SSDiscUserEntryAddRet.get(
        par.disc, 
        discEntryUri, 
        par.op);
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(par)){
        return discUserEntryAdd(par);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(par);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSDiscUserEntryAddRet discUserEntryAdd(final SSServPar parA) throws Exception{
    
    try{
      return discUserEntryAdd(new SSDiscUserEntryAddPar(parA));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSDisc> discsUserAllGet(final SSServPar parA) throws Exception{

    try{
      final SSDiscsUserAllGetPar par                 = new SSDiscsUserAllGetPar(parA);
      final List<SSDisc>         discsWithoutEntries = new ArrayList<SSDisc>();

      for(SSUri discUri : sqlFct.getDiscURIs(par.user)){
        discsWithoutEntries.add(sqlFct.getDiscWithoutEntries(discUri));
      }

      return discsWithoutEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSDisc discUserWithEntriesGet(final SSServPar parA) throws Exception{

    try{
      final SSDiscUserWithEntriesGetPar par = new SSDiscUserWithEntriesGetPar(parA);
      
      if(!SSServCaller.entityUserCanRead(par.user, par.disc)){
        throw new Exception("user cannot access disc");
      }
      
      return sqlFct.getDiscWithEntries(par.disc);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSDisc> discsUserWithEntriesGet(final SSServPar parA) throws Exception{

    try{
      final SSDiscsWithEntriesGetPar par              = new SSDiscsWithEntriesGetPar(parA);
      final List<SSDisc>             discsWithEntries = new ArrayList<SSDisc>();
      
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