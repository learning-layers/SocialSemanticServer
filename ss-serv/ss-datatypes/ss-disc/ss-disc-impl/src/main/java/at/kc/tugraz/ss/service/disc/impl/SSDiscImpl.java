/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsWithEntriesPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserWithEntriesPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAddPar;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.service.disc.api.*;
import at.kc.tugraz.ss.service.disc.datatypes.*;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUrisForTargetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserRemovePar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserWithEntriesRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscsAllRet;
import at.kc.tugraz.ss.service.disc.impl.fct.sql.SSDiscSQLFct;
import at.kc.tugraz.ss.service.disc.impl.fct.ue.SSDiscUEFct;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.lang.reflect.Method;
import java.util.*;

public class SSDiscImpl extends SSServImplWithDBA implements SSDiscClientI, SSDiscServerI, SSEntityHandlerImplI{
  
//  private final SSDiscGraphFct graphFct;
  private final SSDiscSQLFct   sqlFct;
  
  public SSDiscImpl(final SSServConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
//    graphFct = new SSDiscGraphFct (this);
    sqlFct   = new SSDiscSQLFct   (this);
  }
    
  /* SSEntityHandlerImplI */

  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSEntityEnum                                  entityType,
    final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par,
    final Boolean                                       shouldCommit) throws Exception{
  }
  
  @Override
  public SSEntityDescA getDescForEntity(
    final SSEntityEnum    entityType,
    final SSUri           userUri, 
    final SSUri           entityUri, 
    final SSLabelStr      label,
    final Long            creationTime,
    final List<SSTag>     tags, 
    final SSRatingOverall overallRating,
    final List<SSUri>     discUris,
    final SSUri           author) throws Exception{
    
    if(SSEntityEnum.equals(entityType, SSEntityEnum.disc)){

      return SSDiscDesc.get(
        entityUri,
        label,
        creationTime, 
        tags, 
        overallRating, 
        author);
    }
    
    if(SSEntityEnum.equals(entityType, SSEntityEnum.discEntry)){
      
      return SSDiscEntryDesc.get(
        entityUri,
        label,
        creationTime,
        author);
    }
    
    return SSEntityDesc.get(entityUri, label, creationTime, tags, overallRating, discUris, author);
  }
  
  /* SSServRegisterableImplI */
  @Override
  public List<SSMethU> publishClientOps() throws Exception{
    
    List<SSMethU> clientOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSDiscClientI.class.getMethods();
    
    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }
    
    return clientOps;
  }
  
  @Override
  public List<SSMethU> publishServerOps() throws Exception{
    
    List<SSMethU> serverOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSDiscServerI.class.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }
    
    return serverOps;
  }
  
  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSDiscClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }
  
  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSDiscServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }
  
  /* SSDiscClientI */
  @Override
  public void discEntryAdd(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(discEntryAdd(par));
  }

  @Override
  public void discsAll(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSDiscsAllRet.get(discsAll(par), par.op));
  }
  
  @Override
  public void discUserWithEntriesGet(final SSSocketCon sSCon, final SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSDiscUserWithEntriesRet.get(discUserWithEntriesGet(par), par.op));
  }
  
  /* SSDiscServerI */
  @Override 
  public List<SSUri> discUrisForTarget(final SSServPar parA) throws Exception{
   
    final SSDiscUrisForTargetPar par          =  new SSDiscUrisForTargetPar(parA);
    
    try{
      return sqlFct.getDiscUrisForTarget(par.entityUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri discUserRemove(final SSServPar parA) throws Exception{
    
    final SSDiscUserRemovePar par    = new SSDiscUserRemovePar(parA);

    try{
      final SSUri author = SSServCaller.entityAuthorGet(par.user, par.discUri);
      
      if(!SSUri.equals(author, par.user)){
        throw new Exception("user is not author of disc");
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.deleteDisc(par.discUri);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.discUri;
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSDiscEntryAddRet discEntryAdd(final SSServPar parA) throws Exception {
    
    final SSDiscEntryAddPar par          =  new SSDiscEntryAddPar(parA);
    SSUri                   discEntryUri = null;
    SSLabelStr              discLabel    = null;
    SSUri                   discUri;
    
    try{
      
      discUri = par.disc;
        
      if(par.addNewDisc){
        
        discUri   = sqlFct.createDiscUri();
        
        SSServCaller.addEntity(
          par.user,
          par.target,
          SSLabelStr.get(par.target.toString()),
          SSEntityEnum.entity);
        
        discLabel = SSServCaller.entityLabelGet(par.target);
        
        SSServCaller.addEntity(
          par.user,
          discUri,
          discLabel,
          SSEntityEnum.disc);
      }
      
      if(par.content != null){
        
        discEntryUri = sqlFct.createDiscEntryUri();
        
        SSServCaller.addEntity(
          par.user,
          discEntryUri,
          SSLabelStr.get(discEntryUri.toString()),
          SSEntityEnum.discEntry);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(par.addNewDisc){
        sqlFct.createDisc (discUri, par.user, par.target, discLabel);
      }
      
      if(par.content != null){
        sqlFct.addDiscEntry (discEntryUri, par.user, discUri, par.content);
      }
      
      dbSQL.commit(par.shouldCommit);
      
//      SSServCaller.broadCastUpdate(par.user, disc, SSBroadcastEnum.suDiscssion, true);    
      
      if(par.addNewDisc){
        SSDiscUEFct.discCreate(par, discUri);
      }else{
        SSDiscUEFct.discEntryAdd(par, discEntryUri);
      }
      
      return SSDiscEntryAddRet.get(discUri, discEntryUri, par.op);
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSDisc> discsAll(SSServPar parA) throws Exception {
    
    List<SSDisc> discsWithoutEntries = new ArrayList<SSDisc>();
    
    try{
      
      discsWithoutEntries = sqlFct.getDiscsWithoutEntries();
      
      for(SSDisc disc : discsWithoutEntries){
        disc.label = SSServCaller.entityLabelGet(disc.uri);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    return discsWithoutEntries;
  }
 
  @Override
  public SSDisc discUserWithEntriesGet(SSServPar parA) throws Exception{
    
    final SSDiscUserWithEntriesPar par  = new SSDiscUserWithEntriesPar(parA);
    
    try{
      return sqlFct.getDiscWithEntries  (par.disc);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSDisc> discsUserWithEntriesGet(final SSServPar parA) throws Exception {
    
    final SSDiscsWithEntriesPar par              = new SSDiscsWithEntriesPar(parA);
    final List<SSDisc>          discsWithEntries = new ArrayList<SSDisc>();
    
    try{
      
      for(SSDisc disc : SSServCaller.getAllDiscsWithoutEntries(par.user)){
        
        discsWithEntries.add(
          SSServCaller.discUserWithEntriesGet(
            par.user, 
            disc.uri, 
            par.maxDiscEntries));
      }
      
      return discsWithEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}