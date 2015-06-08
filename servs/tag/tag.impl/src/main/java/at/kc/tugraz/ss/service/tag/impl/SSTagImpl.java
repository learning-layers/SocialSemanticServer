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
package at.kc.tugraz.ss.service.tag.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivEntityAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubEntityAddPar;
import at.kc.tugraz.ss.circle.serv.SSCircleServ;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import at.kc.tugraz.ss.service.tag.api.*;
import at.kc.tugraz.ss.service.tag.datatypes.*;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagEditPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagEntitiesForTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagFrequsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsRemovePar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagAddRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagEditRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagEntitiesForTagsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagFrequsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsRemoveRet;
import at.kc.tugraz.ss.service.tag.impl.fct.activity.SSTagActivityFct;
import at.kc.tugraz.ss.service.tag.impl.fct.misc.SSTagMiscFct;
import at.kc.tugraz.ss.service.tag.impl.fct.sql.SSTagSQLFct;
import at.kc.tugraz.ss.service.tag.impl.fct.userrelationgatherer.SSTagUserRelationGathererFct;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.*;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;

public class SSTagImpl
extends SSServImplWithDBA
implements
  SSTagClientI,
  SSTagServerI,
  SSEntityHandlerImplI,
  SSEntityDescriberI,
  SSUserRelationGathererI,
  SSUsersResourcesGathererI{
  
  private final SSTagSQLFct sqlFct;
  
  public SSTagImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    sqlFct = new SSTagSQLFct (this);
  }
  
  @Override
  public void getUserRelations(
    final List<String>             allUsers,
    final Map<String, List<SSUri>> userRelations) throws Exception{
    
    final Map<String, List<SSUri>> usersPerTag    = new HashMap<>();
    final Map<String, List<SSUri>> usersPerEntity = new HashMap<>();
    List<SSTag>                    tags;
    
    for(String user : allUsers){
      
      final SSUri userUri = SSUri.get(user);
      
      tags =
        tagsGet(
          new SSTagsGetPar(
            null,
            null,
            userUri,
            userUri,
            SSUri.asListWithoutNullAndEmpty(),
            SSTagLabel.asListWithoutNullAndEmpty(),
            null,
            null));
      
      for(SSTag tag : tags){
        
        SSTagUserRelationGathererFct.addUserForTag     (tag, usersPerTag);
        SSTagUserRelationGathererFct.addUserForEntity  (tag, usersPerEntity);
      }
    }
    
    SSTagUserRelationGathererFct.addUserRelations(userRelations, usersPerTag);
    SSTagUserRelationGathererFct.addUserRelations(userRelations, usersPerEntity);
    
    for(Map.Entry<String, List<SSUri>> usersPerUser : userRelations.entrySet()){
      SSStrU.distinctWithoutNull2(usersPerUser.getValue());
    }
  }
  
  @Override
  public void getUsersResources(
    final List<String>             allUsers,
    final Map<String, List<SSUri>> usersResources) throws Exception{
    
    SSUri userUri;
    
    for(String user : allUsers){
      
      userUri = SSUri.get(user);
      
      for(SSTag tag :
        tagsGet(
          new SSTagsGetPar(
            null,
            null,
            userUri,
            userUri,
            SSUri.asListWithoutNullAndEmpty(),
            SSTagLabel.asListWithoutNullAndEmpty(),
            null,
            null))){
        
        if(usersResources.containsKey(user)){
          usersResources.get(user).add(tag.entity);
        }else{
          
          final List<SSUri> resourceList = new ArrayList<>();
          
          resourceList.add(tag.entity);
          
          usersResources.put(user, resourceList);
        }
      }
    }
    
    for(Map.Entry<String, List<SSUri>> resourcesPerUser : usersResources.entrySet()){
      SSStrU.distinctWithoutNull2(resourcesPerUser.getValue());
    }
  }
  
  @Override
  public Boolean copyEntity(
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
  public Boolean setEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri,
    final SSEntityE      entityType,
    final SSUri          publicCircleUri) throws Exception{
    
    return false;
  }
  
  @Override
  public void shareEntityWithUsers(
    final SSUri          userUri,
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri,
    final SSUri          entityCircleUri,
    final SSEntityE      entityType,
    final Boolean        saveActivity) throws Exception{
  }
  
  @Override
  public void addEntityToCircle(
    final SSUri        userUri,
    final SSUri        circleUri,
    final List<SSUri>  circleUsers,
    final SSUri        entityUri,
    final SSEntityE    entityType) throws Exception{
  }
  
  @Override
  public void addUsersToCircle(
    final SSUri        user,
    final List<SSUri>  users,
    final SSEntityCircle        circle) throws Exception{
    
    
    
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
    
    try{
      
      if(!removeUserTags){
        return;
      }
      
      tagsRemove(
        new SSTagsRemovePar(
          null,
          null,
          userUri,
          userUri,
          entityUri,
          null,
          null,
          true,
          false));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSEntity getUserEntity(final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setTags){
        
        par.entity.tags.addAll(
          SSStrU.toStr(
            tagsGet(
              new SSTagsGetPar(
                null,
                null,
                par.user,
                null,
                SSUri.asListWithoutNullAndEmpty(par.entity.id),
                SSTagLabel.asListWithoutNullAndEmpty(),
                null,
                null))));
      }
      
      return par.entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void tagEntitiesForTagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(
      SSTagEntitiesForTagsGetRet.get(
        tagEntitiesForTagsGet((SSTagEntitiesForTagsGetPar) parA.getFromJSON(SSTagEntitiesForTagsGetPar.class))),
      parA.op);
  }
  
  @Override
  public List<SSUri> tagEntitiesForTagsGet(final SSTagEntitiesForTagsGetPar par) throws Exception{
    
    //TODO dtheiler: use start time for this call as well
    try{
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(
        par.forUser != null &&
        !SSStrU.equals(par.user,  par.forUser)){
        par.space = SSSpaceE.sharedSpace;
      }
      
      if(par.space == null){
        return SSTagMiscFct.getEntitiesForTagsIfSpaceNotSet(sqlFct, par);
      }
      
      if(SSSpaceE.isShared(par.space)){
        return SSTagMiscFct.getEntitiesForTagsIfSpaceSet(sqlFct, par, par.forUser);
      }
      
      if(SSSpaceE.isPrivate(par.space)){
        return SSTagMiscFct.getEntitiesForTagsIfSpaceSet(sqlFct, par, par.user);
      }
      
      throw new Exception("reached not reachable code");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void tagAdd(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSTagAddPar par    = (SSTagAddPar) parA.getFromJSON(SSTagAddPar.class);
    final SSUri       tagUri = tagAdd(par);
    
    sSCon.writeRetFullToClient(SSTagAddRet.get(tagUri), parA.op);
    
    SSTagActivityFct.addTag(par, tagUri);
  }
  
  @Override
  public SSUri tagAdd(final SSTagAddPar par) throws Exception {
    
    try{
      
      final Boolean     existsTag;
      final Boolean     existsEntity = SSServCaller.entityExists(par.entity);
      final SSUri       tagUri;
      
      if(existsEntity){
        
        switch(SSServCaller.entityGet(par.entity).type){
          case entity: break;
          default: SSServCallerU.canUserReadEntity(par.user, par.entity);
        }
      }
      
      existsTag = SSServCaller.entityExists(SSEntityE.tag, SSLabel.get(SSStrU.toStr(par.label)));
      
      if(existsTag){
        tagUri = SSServCaller.entityGet(SSEntityE.tag, SSLabel.get(SSStrU.toStr(par.label))).id;
      }else{
        tagUri    = SSServCaller.vocURICreate();
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(existsEntity){
        
        if(SSStrU.equals(par.space, SSSpaceE.privateSpace)){
          
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
          
          ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
            new SSCirclePrivEntityAddPar(
              null,
              null,
              par.user,
              tagUri,
              SSEntityE.tag,
              SSLabel.get(SSStrU.toStr(par.label)),
              null,
              par.creationTime,
              false));
          
        }else{
          
          ((SSCircleServerI) SSCircleServ.inst.serv()).circlePubEntityAdd(
            new SSCirclePubEntityAddPar(
              null,
              null,
              par.user,
              tagUri,
              false,
              SSEntityE.tag,
              SSLabel.get(SSStrU.toStr(par.label)),
              null,
              par.creationTime));
        }
      }else{
        
        ((SSCircleServerI) SSCircleServ.inst.serv()).circlePubEntityAdd(
          new SSCirclePubEntityAddPar(
            null,
            null,
            par.user,
            par.entity,
            false,
            SSEntityE.entity,
            null,
            null,
            null));
        
        if(SSStrU.equals(par.space, SSSpaceE.privateSpace)){
          
          ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
            new SSCirclePrivEntityAddPar(
              null,
              null,
              par.user,
              tagUri,
              SSEntityE.tag,
              SSLabel.get(SSStrU.toStr(par.label)),
              null,
              par.creationTime,
              false));
          
        }else{
          ((SSCircleServerI) SSCircleServ.inst.serv()).circlePubEntityAdd(
            new SSCirclePubEntityAddPar(
              null,
              null,
              par.user,
              tagUri,
              false,
              SSEntityE.tag,
              SSLabel.get(SSStrU.toStr(par.label)),
              null,
              par.creationTime));
        }
      }
      
      sqlFct.addTagAssIfNotExists(
        tagUri,
        par.user,
        par.entity,
        par.space,
        par.creationTime);
      
      dbSQL.commit(par.shouldCommit);
      
      return tagUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return tagAdd(par);
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
  
  @Override
  public void tagEdit(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(
      SSTagEditRet.get(
        tagEdit((SSTagEditPar) parA.getFromJSON(SSTagEditPar.class))),
      parA.op);
  }
  
  @Override
  public SSUri tagEdit(final SSTagEditPar par) throws Exception {
    
    try{
      
      SSUri                  newTagUri = null;
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      final Boolean existsTag;
      final SSUri   tagUri;
      
      existsTag = SSServCaller.entityExists(SSEntityE.tag, SSLabel.get(SSStrU.toStr(par.tag)));
      
      if(existsTag){
        tagUri = SSServCaller.entityGet(SSEntityE.tag, SSLabel.get(SSStrU.toStr(par.tag))).id;
      }else{
        tagUri = SSServCaller.vocURICreate();
      }
      
      final List<SSTag> tags =
        tagsGet(
          new SSTagsGetPar(
            null,
            null,
            par.user,
            par.user,
            SSUri.asListWithoutNullAndEmpty(par.entity),
            SSTagLabel.asListWithoutNullAndEmpty(par.tag),
            null,
            null));
      
      for(SSTag tag : tags){
        
        tagsRemove(
          new SSTagsRemovePar(
            null,
            null,
            par.user,
            par.user,
            tag.entity,
            SSTagLabel.get(SSStrU.toStr(tag.label)),
            tag.space,
            false,
            false));
        
        newTagUri =
          tagAdd(
            new SSTagAddPar(
              null,
              null,
              par.user,
              tag.entity,
              par.label,
              tag.space,
              null,
              false));
      }
      
      if(newTagUri == null){
        return tagUri;
      }else{
        return newTagUri;
      }
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return tagEdit(par);
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
  
  @Override
  public void tagsRemove(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSTagsRemovePar par = (SSTagsRemovePar) parA.getFromJSON(SSTagsRemovePar.class);
    
    sSCon.writeRetFullToClient(
      SSTagsRemoveRet.get(
        tagsRemove(par)),
      parA.op);
    
    SSTagActivityFct.removeTags(par);
  }
  
  @Override
  public Boolean tagsRemove(final SSTagsRemovePar par) throws Exception {
    
    try{
      
      SSUri tagUri = null;
      
      if(par.label != null){
        
        if(SSServCaller.entityExists(SSEntityE.tag, SSLabel.get(SSStrU.toStr(par.label)))){
          tagUri = SSServCaller.entityGet(SSEntityE.tag,SSLabel.get(SSStrU.toStr(par.label))).id;
        }else{
          tagUri = SSServCaller.vocURICreate();
        }
      }
      
      if(!par.withUserRestriction){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeTagAsss(
          par.forUser,
          par.entity,
          tagUri,
          par.space);
        
        dbSQL.commit(par.shouldCommit);
        
        return true;
      }
      
      if(
        par.forUser != null &&
        !SSStrU.equals(par.forUser, par.user)){
        throw new Exception("user cannot delete tags of other user");
      }
      
      if(par.forUser == null){
        par.forUser = par.user;
      }
      
      if(
        par.space    == null &&
        par.entity == null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeTagAsss(par.forUser, null, tagUri, SSSpaceE.privateSpace);
        sqlFct.removeTagAsss(par.forUser, null, tagUri, SSSpaceE.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space    != null &&
        par.entity == null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeTagAsss(par.forUser, null, tagUri, par.space);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space    == null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeTagAsss (par.forUser, par.entity, tagUri, SSSpaceE.privateSpace);
        sqlFct.removeTagAsss (null,        par.entity, tagUri, SSSpaceE.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space    != null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeTagAsss(null, par.entity, tagUri, par.space);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      throw new Exception("reached not reachable code");
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return tagsRemove(par);
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
  
  @Override
  public void tagFrequsGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(
      SSTagFrequsGetRet.get(
        tagFrequsGet((SSTagFrequsGetPar) parA.getFromJSON(SSTagFrequsGetPar.class))),
      parA.op);
  }
  
  @Override
  public List<SSTagFrequ> tagFrequsGet(final SSTagFrequsGetPar par) throws Exception {
    
    try{
      
      if(
        !par.entities.isEmpty() &&
        par.useUsersEntities){
        throw new Exception("entities and useForUsersEntities set at the same time");
      }
      
      if(par.useUsersEntities){
        
        //TODO change: hack for bits and pieces
        final List<SSEntityE> types =
          SSEntityE.asListWithoutNullAndEmpty(
            SSEntityE.entity,
            SSEntityE.file,
            SSEntityE.evernoteResource,
            SSEntityE.evernoteNote,
            SSEntityE.evernoteNotebook,
            SSEntityE.placeholder);
        
        par.entities.addAll(
          SSUri.getFromEntitites(
            ((SSCircleServerI) SSCircleServ.inst.serv()).circleEntitiesGet(
              new SSCircleEntitiesGetPar(
                null,
                null,
                par.user,
                par.forUser,
                types,
                true,
                false,
                par.withUserRestriction))));
      }
      
      return SSTagMiscFct.getTagFrequsFromTags(
        tagsGet(
          new SSTagsGetPar(
            null,
            null,
            par.user,
            par.forUser,
            par.entities,
            par.labels,
            par.space,
            par.startTime)),
        par.space);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> tagsAdd(final SSTagsAddPar par) throws Exception {
    
    try{
      
      final List<SSUri>  tags   = new ArrayList<>();
      
      for(SSTagLabel tagLabel : par.labels) {
        
        tags.add(
          tagAdd(
            new SSTagAddPar(
              null,
              null,
              par.user,
              par.entity,
              tagLabel,
              par.space,
              par.creationTime,
              par.shouldCommit)));
      }
      
      SSStrU.distinctWithoutNull2(tags);
      
      return tags;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return tagsAdd(par);
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
  
  @Override
  public void tagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(
      SSTagsGetRet.get(
        tagsGet((SSTagsGetPar) parA.getFromJSON(SSTagsGetPar.class))),
      parA.op);
  }
  
  @Override
  public List<SSTag> tagsGet(final SSTagsGetPar par) throws Exception {
    
    try{
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(
        par.forUser != null &&
        !SSStrU.equals(par.user,  par.forUser)){
        par.space = SSSpaceE.sharedSpace;
      }
      
      if(par.space == null){
        return SSTagMiscFct.getTagsIfSpaceNotSet(sqlFct, par);
      }
      
      if(SSSpaceE.isPrivate(par.space)){
        return SSTagMiscFct.getTagsIfSpaceSet(sqlFct, par, par.user);
      }
      
      if(SSSpaceE.isShared(par.space)){
        return SSTagMiscFct.getTagsIfSpaceSet(sqlFct, par, par.forUser);
      }
      
      throw new Exception("reached not reachable code");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}