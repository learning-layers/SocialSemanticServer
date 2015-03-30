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

import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityDescriberI;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSUserRelationGathererI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.serv.caller.SSServCallerU;
import at.kc.tugraz.ss.service.tag.api.*;
import at.kc.tugraz.ss.service.tag.datatypes.*;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagUserEditPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagUserEntitiesForTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagUserFrequsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsUserGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsUserRemovePar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsRemovePar;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagAddRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagUserEditRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagUserEntitiesForTagsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagUserFrequsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsUserGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsUserRemoveRet;
import at.kc.tugraz.ss.service.tag.impl.fct.activity.SSTagActivityFct;
import at.kc.tugraz.ss.service.tag.impl.fct.misc.SSTagMiscFct;
import at.kc.tugraz.ss.service.tag.impl.fct.sql.SSTagSQLFct;
import at.kc.tugraz.ss.service.tag.impl.fct.userrelationgatherer.SSTagUserRelationGathererFct;
import java.util.*;
import sss.serv.err.datatypes.SSErrE;

public class SSTagImpl 
extends SSServImplWithDBA 
implements 
  SSTagClientI, 
  SSTagServerI, 
  SSEntityHandlerImplI, 
  SSEntityDescriberI, 
  SSUserRelationGathererI{
  
  private final SSTagSQLFct sqlFct;
  
  public SSTagImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
    sqlFct = new SSTagSQLFct (this);
  }
  
  @Override
  public void getUserRelations(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> userRelations) throws Exception{
    
    final List<String>             labels         = new ArrayList<>();
    final List<SSUri>              entities       = new ArrayList<>();
    final Map<String, List<SSUri>> usersPerTag    = new HashMap<>();
    final Map<String, List<SSUri>> usersPerEntity = new HashMap<>();
    List<SSTag>                    tags;
    
    for(String user : allUsers){
      
      final SSUri userUri = SSUri.get(user);
      
      tags =
        SSServCaller.tagsUserGet(
          userUri,
          userUri,
          entities,
          labels,
          null,
          null);
      
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

    return false;
  }
  
  @Override
  public void shareUserEntity(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          entityCircleUri,
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
    
    try{
      
      if(!removeUserTags){
        return;
      }
      
      SSServCaller.tagsUserRemove(
        userUri, 
        entityUri, 
        null, 
        null, 
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSEntity getUserEntity(
    final SSUri              user,
    final SSEntity           entity) throws Exception{
    
    switch(entity.type){
      case tag:
//        return SSServCaller.videoUserGet(user, entity.id);
    }
    
    return entity;
  }
  
  @Override
  public SSEntity getDescForEntity(
    final SSEntityDescGetPar par,
    final SSEntity           desc) throws Exception{
    
    if(par.getTags){
      
      desc.tags.addAll(
        SSStrU.toStr(
          SSServCaller.tagsUserGet(
            par.user, 
            null,
            SSUri.asListWithoutNullAndEmpty(par.entity), 
            new ArrayList<>(), 
            null, 
            null)));
    }
    
    return desc;
  }
    
  @Override
  public void tagEntitiesForTagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    final SSUri userFromKey = SSServCaller.checkKey(parA);
    
    if(userFromKey != null){
      parA.user = userFromKey;
    }
    
    sSCon.writeRetFullToClient(SSTagUserEntitiesForTagsGetRet.get(tagUserEntitiesForTagsGet(parA), parA.op));
  }

  @Override
  public List<SSUri> tagUserEntitiesForTagsGet(final SSServPar parA) throws Exception{
    
    //TODO dtheiler: use start time for this call as well
    try{
      final SSTagUserEntitiesForTagsGetPar par = new SSTagUserEntitiesForTagsGetPar(parA);
      
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
    
    final SSUri userFromKey = SSServCaller.checkKey(parA);
    
    if(userFromKey != null){
      parA.user = userFromKey;
    }
    
    final SSUri tagUri = tagAdd(parA);
    
    sSCon.writeRetFullToClient(SSTagAddRet.get(tagUri, parA.op));

    SSTagActivityFct.addTag(new SSTagAddPar(parA), tagUri);
  }
  
  @Override
  public SSUri tagAdd(final SSServPar parA) throws Exception {
    
    try{
      
      final SSTagAddPar par          = new SSTagAddPar(parA);
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
          
          SSServCaller.entityEntityToPrivCircleAdd(
            par.user,
            par.entity,
            SSEntityE.entity,
            null,
            null,
            null,
            false);
          
          SSServCaller.entityEntityToPrivCircleAdd(
            par.user,
            tagUri,
            SSEntityE.tag,
            SSLabel.get(SSStrU.toStr(par.label)),
            null,
            par.creationTime,
            false);
          
        }else{
          
          SSServCaller.entityEntityToPubCircleAdd(
            par.user,
            tagUri,
            SSEntityE.tag,
            SSLabel.get(SSStrU.toStr(par.label)),
            null,
            par.creationTime,
            false);
        }
      }else{
        
        SSServCaller.entityEntityToPubCircleAdd(
          par.user,
          par.entity,
          SSEntityE.entity,
          null,
          null,
          null,
          false);
        
        if(SSStrU.equals(par.space, SSSpaceE.privateSpace)){
          
          SSServCaller.entityEntityToPrivCircleAdd(
            par.user,
            tagUri,
            SSEntityE.tag,
            SSLabel.get(SSStrU.toStr(par.label)),
            null,
            par.creationTime,
            false);
          
        }else{
          SSServCaller.entityEntityToPubCircleAdd(
            par.user,
            tagUri,
            SSEntityE.tag,
            SSLabel.get(SSStrU.toStr(par.label)),
            null,
            par.creationTime,
            false);
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
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return tagAdd(parA);
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
  public void tagEdit(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    final SSUri userFromKey = SSServCaller.checkKey(parA);
    
    if(userFromKey != null){
      parA.user = userFromKey;
    }
    
    sSCon.writeRetFullToClient(SSTagUserEditRet.get(tagUserEdit(parA), parA.op));
  }
  
  @Override
  public SSUri tagUserEdit(final SSServPar parA) throws Exception {
    
    try{
      
      final SSTagUserEditPar par       = new SSTagUserEditPar (parA);
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
        SSServCaller.tagsUserGet(
          par.user,
          par.user,
          SSUri.asListWithoutNullAndEmpty(par.entity),
          SSStrU.toStrWithoutEmptyAndNull(par.tag),
          null,
          null);
      
      for(SSTag tag : tags){
        
        SSServCaller.tagsRemove(
          par.user,
          tag.entity,
          SSStrU.toStr(tag.label),
          tag.space,
          false);
        
        newTagUri =
          SSServCaller.tagAdd(
            par.user,
            tag.entity,
            SSStrU.toStr(par.label),
            tag.space,
            null,
            false);
      }
      
      if(newTagUri == null){
        return tagUri;
      }else{
        return newTagUri;
      }
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return tagUserEdit(parA);
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
  public void tagsRemove(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    final SSUri userFromKey = SSServCaller.checkKey(parA);
    
    if(userFromKey != null){
      parA.user = userFromKey;
    }
    
    sSCon.writeRetFullToClient(SSTagsUserRemoveRet.get(tagsUserRemove(parA), parA.op));
    
    SSTagActivityFct.removeTags(new SSTagsUserRemovePar(parA));
  }
  
  @Override
  public Boolean tagsUserRemove(final SSServPar parA) throws Exception {
    
    try{
      
      final SSTagsUserRemovePar par = new SSTagsUserRemovePar (parA);
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      SSUri tagUri = null;
      
      if(par.label != null){
        
        if(SSServCaller.entityExists(SSEntityE.tag, SSLabel.get(SSStrU.toStr(par.label)))){
          tagUri = SSServCaller.entityGet(SSEntityE.tag,SSLabel.get(SSStrU.toStr(par.label))).id;
        }else{
          tagUri = SSServCaller.vocURICreate();
        }
      }
      
      
      if(
        par.space  == null &&
        par.entity == null){

        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeTagAsss(par.user, null, tagUri, SSSpaceE.privateSpace);
        sqlFct.removeTagAsss(par.user, null, tagUri, SSSpaceE.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
       if(
         par.space  != null &&
         par.entity == null){
         
         dbSQL.startTrans(par.shouldCommit);
         
         sqlFct.removeTagAsss(par.user, null, tagUri, par.space);
         
         dbSQL.commit(par.shouldCommit);
         return true;
       }
      
      if(
        par.space  == null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeTagAsss (par.user, par.entity, tagUri, SSSpaceE.privateSpace);
        sqlFct.removeTagAsss (null,     par.entity, tagUri, SSSpaceE.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space  != null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
      
        sqlFct.removeTagAsss(null, par.entity, tagUri, par.space);

        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      throw new Exception("reached not reachable code");
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return tagsUserRemove(parA);
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
  public void tagFrequsGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
       
    final SSUri userFromKey = SSServCaller.checkKey(parA);
    
    if(userFromKey != null){
      parA.user = userFromKey;
    }
    
    sSCon.writeRetFullToClient(SSTagUserFrequsGetRet.get(tagUserFrequsGet(parA), parA.op));
  }
  
  @Override
  public List<SSTagFrequ> tagUserFrequsGet(final SSServPar parA) throws Exception {
    
    try{
      
      final SSTagUserFrequsGetPar par = new SSTagUserFrequsGetPar (parA);
      
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
            SSServCaller.circleEntitiesGet(
              par.user,
              par.forUser,             //forUser
              types,                   //types
              true,                    //withSystemCircles
              par.withUserRestriction, //withUserRestriction
              false)));                //invokeEntityHandlers
      }
      
      return SSTagMiscFct.getTagFrequsFromTags(
        SSServCaller.tagsUserGet(
          par.user,
          par.forUser,
          par.entities,
          SSStrU.toStrWithoutEmptyAndNull(par.labels),
          par.space,
          par.startTime),
        par.space);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> tagsAdd(final SSServPar parA) throws Exception {
    
    try{

      final SSTagsAddPar par    = new SSTagsAddPar(parA);
      final List<SSUri>  tags   = new ArrayList<>();
      
      for(SSTagLabel tagLabel : par.labels) {
        
        tags.add(
          SSServCaller.tagAdd(
            par.user,
            par.entity,
            SSStrU.toStr(tagLabel),
            par.space,
            par.creationTime,
            par.shouldCommit));
      }
      
      SSStrU.distinctWithoutNull2(tags);
      
      return tags;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return tagsAdd(parA);
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
  public Boolean tagsRemove(final SSServPar parA) throws Exception{
  
    try{
      
      final SSTagsRemovePar par = new SSTagsRemovePar (parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSUri tagUri = null;
      
      if(par.label != null){
        
        if(SSServCaller.entityExists(SSEntityE.tag, SSLabel.get(SSStrU.toStr(par.label)))){
          tagUri = SSServCaller.entityGet(SSEntityE.tag,SSLabel.get(SSStrU.toStr(par.label))).id;
        }else{
          tagUri = SSServCaller.vocURICreate();
        }
      }
      
      sqlFct.removeTagAsss(
        par.forUser, 
        par.entity, 
        tagUri, 
        par.space);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return tagsRemove(parA);
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
  public void tagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    final SSUri userFromKey = SSServCaller.checkKey(parA);
    
    if(userFromKey != null){
      parA.user = userFromKey;
    }
    
    sSCon.writeRetFullToClient(SSTagsUserGetRet.get(tagsUserGet(parA), parA.op));
  }
  
  @Override
  public List<SSTag> tagsUserGet(final SSServPar parA) throws Exception {
    
    try{
      
      final SSTagsUserGetPar par       = new SSTagsUserGetPar (parA);
      
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