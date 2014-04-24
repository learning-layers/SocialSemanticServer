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
package at.kc.tugraz.ss.service.tag.impl;

import at.kc.tugraz.ss.datatypes.datatypes.SSTagLabel;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.tag.api.*;
import at.kc.tugraz.ss.service.tag.datatypes.*;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagAddAtCreationTimePar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagUserFrequsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagUserEntitiesForTagGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsUserGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsUserRemovePar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddAtCreationTimePar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsRemovePar;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagAddRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagUserFrequsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsUserRemoveRet;
import at.kc.tugraz.ss.service.tag.impl.fct.SSTagFct;
import at.kc.tugraz.ss.service.tag.impl.fct.sql.SSTagSQLFct;
import java.util.*;

public class SSTagImpl extends SSServImplWithDBA implements SSTagClientI, SSTagServerI, SSEntityHandlerImplI{
  
//  private final SSTagGraphFct graphFct;
  private final SSTagSQLFct   sqlFct;
  
  public SSTagImpl(final SSServConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
//    graphFct  = new SSTagGraphFct (this);
    sqlFct    = new SSTagSQLFct   (this);
  }
  
  /* SSEntityHandlerImplI */
  
  @Override
  public Boolean setUserEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri, 
    final SSEntityEnum   entityType,
    final SSUri          publicCircleUri) throws Exception{

    return false;
  }
  
  @Override
  public Boolean shareUserEntity(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          entityCircleUri,
    final SSEntityEnum   entityType) throws Exception{
    
    return false;
  }
  
  @Override
  public Boolean addEntityToCircle(
    final SSUri        userUri, 
    final SSUri        circleUri, 
    final SSUri        entityUri, 
    final SSEntityEnum entityType) throws Exception{
    
    return false;
  }  
  
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSEntityEnum                                  entityType,
    final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par) throws Exception{
    
    try{
      
      if(!par.removeUserTags){
        return;
      }
      
      SSServCaller.tagsUserRemove(
        par.user, 
        par.entityUri, 
        null, 
        null, 
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
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
    
    try{
      
      if(!SSEntityEnum.equals(entityType, SSEntityEnum.tag)){
        return SSEntityDesc.get(entityUri, label, creationTime, tags, overallRating, discUris, author);
      }
      
      return SSTagDesc.get(
        entityUri,
        label,
        creationTime,
        author);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
    
  /* SSTagClientI */
  @Override
  public void tagAdd(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSTagAddRet.get(tagAdd(par), par.op));
    
//    saveUETagAdd(par);
  }

  @Override
  public void tagsUserRemove(SSSocketCon sSCon, SSServPar par) throws Exception {
    
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSTagsUserRemoveRet.get(tagsUserRemove(par), par.op));
    
//    saveUETagDelete(par);
  }

  @Override
  public void tagUserFrequsGet(SSSocketCon sSCon, SSServPar par) throws Exception {
       
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSTagUserFrequsGetRet.get(tagUserFrequsGet(par), par.op));
  }
  
  /* SSTagServerI */
  @Override
  public Boolean tagAdd(final SSServPar parA) throws Exception {
    
    try{
      
      final SSTagAddPar par       = new SSTagAddPar(parA);
      final Boolean     existsTag = sqlFct.existsTagLabel    (par.tagString);
      final SSUri       tagUri    = sqlFct.getOrCreateTagUri (existsTag, par.tagString);

      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        tagUri,       
        SSLabelStr.get(SSTagLabel.toStr(par.tagString)), 
        SSEntityEnum.tag,
        false);

      SSServCaller.entityAdd(
        par.user,
        par.resource, 
        SSLabelStr.get(SSUri.toStr(par.resource)),
        SSEntityEnum.entity,
        false);
      
      if(!sqlFct.existsTagAss(par.user, par.resource, tagUri, par.space)){
        sqlFct.addTagAss(tagUri, par.user, par.resource, par.space);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return tagAdd(parA);
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
  public Boolean tagAddAtCreationTime(final SSServPar parA) throws Exception {
    
    try{
      
      final SSTagAddAtCreationTimePar par       = new SSTagAddAtCreationTimePar(parA);
      final Boolean                   existsTag = sqlFct.existsTagLabel   (par.tagString);
      final SSUri                     tagUri    = sqlFct.getOrCreateTagUri (existsTag, par.tagString); 

      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAddAtCreationTime(
        par.user,
        tagUri,
        SSLabelStr.get(SSStrU.toString(par.tagString)),
        par.creationTime,
        SSEntityEnum.tag,
        false);
      
      SSServCaller.entityAdd(
        par.user,
        par.resource,
        SSLabelStr.get(par.resource.toString()),
        SSEntityEnum.entity,
        false);
      
      sqlFct.addTagAss(tagUri, par.user, par.resource, par.space);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return tagAddAtCreationTime(parA);
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
  public Boolean tagsAdd(final SSServPar parA) throws Exception {
    
    try{

      final SSTagsAddPar par    = new SSTagsAddPar(parA);
      
      for(SSTagLabel tagString : par.tagStrings) {
        SSServCaller.tagAdd(par.user, par.resource, tagString, par.space, par.shouldCommit);
      }
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return tagsAdd(parA);
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
  public Boolean tagsAddAtCreationTime(final SSServPar parA) throws Exception {
    
    try{

      final SSTagsAddAtCreationTimePar par    = new SSTagsAddAtCreationTimePar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSTagLabel tagString : par.tagStrings) {
        SSServCaller.tagAddAtCreationTime(
          par.user, 
          par.resource, 
          tagString, 
          par.space, 
          par.creationTime, 
          false);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return tagsAddAtCreationTime(parA);
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
  public Boolean tagsRemove(final SSServPar parA) throws Exception{
  
    try{
      
      final SSTagsRemovePar par = new SSTagsRemovePar (parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.removeTagAsss (
        par.forUser, 
        par.entityUri, 
        par.tagLabel, 
        par.space);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return tagsRemove(parA);
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
  public Boolean tagsUserRemove(final SSServPar parA) throws Exception {
    
    try{
      
      final SSTagsUserRemovePar par = new SSTagsUserRemovePar (parA);
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(
        par.space    == null &&
        par.resource == null){

        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeTagAsss(par.user, null, par.tagString, SSSpaceEnum.privateSpace);
        sqlFct.removeTagAsss(par.user, null, par.tagString, SSSpaceEnum.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
       if(
         par.space    != null &&
         par.resource == null){
         
         dbSQL.startTrans(par.shouldCommit);
         
         sqlFct.removeTagAsss(par.user, null, par.tagString, par.space);
         
         dbSQL.commit(par.shouldCommit);
         return true;
       }
      
      if(
        par.space    == null &&
        par.resource != null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeTagAsss (par.user, par.resource, par.tagString, SSSpaceEnum.privateSpace);
        sqlFct.removeTagAsss (null,     par.resource, par.tagString, SSSpaceEnum.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space    != null &&
        par.resource != null){
        
        dbSQL.startTrans(par.shouldCommit);
      
        sqlFct.removeTagAsss(null, par.resource, par.tagString, par.space);

        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      throw new Exception("reached not reachable code");
      
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return tagsUserRemove(parA);
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
  public List<SSUri> tagUserEntitiesForTagGet(final SSServPar parA) throws Exception{
    
    final SSTagUserEntitiesForTagGetPar  par        = new SSTagUserEntitiesForTagGetPar(parA);
    
    try{
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(par.space == null){
        
        final List<SSUri> entityUris = new ArrayList<SSUri>();
        
        entityUris.addAll(
          sqlFct.getEntitiesForTagLabel(
          par.user,
          par.tagString,
          SSSpaceEnum.privateSpace));
        
        entityUris.addAll(
          sqlFct.getEntitiesForTagLabel(
          null,
          par.tagString,
          SSSpaceEnum.sharedSpace));
        
        return SSUri.distinct(entityUris);
      }
      
      if(SSSpaceEnum.isShared(par.space)){
        
        return sqlFct.getEntitiesForTagLabel(
          null,
          par.tagString,
          par.space);
      }
        
      if(SSSpaceEnum.isPrivate(par.space)){
        
        return sqlFct.getEntitiesForTagLabel(
          par.user,
          par.tagString,
          par.space);
      }
      
      throw new Exception("reached not reachable code");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSTag> tagsUserGet(final SSServPar parA) throws Exception {
    
    final SSTagsUserGetPar par  = new SSTagsUserGetPar (parA);
    
    try{
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(par.space == null){
        
        final List<SSTag>      tags = new ArrayList<SSTag>();
        
        tags.addAll (sqlFct.getTagAsss(par.user, par.resource, par.tagString, SSSpaceEnum.privateSpace));
        tags.addAll (sqlFct.getTagAsss(null,     par.resource, par.tagString, SSSpaceEnum.sharedSpace));
        
        return tags;
      }
      
      if(SSSpaceEnum.isPrivate(par.space)){
        return sqlFct.getTagAsss(par.user, par.resource, par.tagString, par.space);
      }
      
      if(SSSpaceEnum.isShared(par.space)){
        return sqlFct.getTagAsss(null, par.resource, par.tagString, par.space);
      }
      
      throw new Exception("reached not reachable code");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSTagFrequ> tagUserFrequsGet(final SSServPar parA) throws Exception {
    
    final SSTagUserFrequsGetPar  par = new SSTagUserFrequsGetPar (parA);
    final List<SSTag>            tags;
    
    try{
      
      tags = SSServCaller.tagsUserGet(par.user, par.resource, par.tagString, par.space);
      
      return SSTagFct.getTagFrequsFromTags (tags, par.space);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}