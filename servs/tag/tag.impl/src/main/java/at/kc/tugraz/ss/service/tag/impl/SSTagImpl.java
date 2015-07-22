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

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityFromTypeAndLabelGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
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
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.tag.api.*;
import at.kc.tugraz.ss.service.tag.datatypes.*;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagEntitiesForTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagFrequsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsRemovePar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagAddRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagEntitiesForTagsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagFrequsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsRemoveRet;
import at.kc.tugraz.ss.service.tag.impl.fct.activity.SSTagActivityFct;
import at.kc.tugraz.ss.service.tag.impl.fct.userrelationgatherer.SSTagUserRelationGathererFct;
import at.tugraz.sss.serv.SSCircleContentChangedPar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityA;
import at.tugraz.sss.serv.SSEntityCopiedPar;
import at.tugraz.sss.serv.SSEntityCopyPar;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import java.util.*;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.servs.common.impl.tagcategory.SSTagAndCategoryCommonMisc;
import at.tugraz.sss.servs.common.impl.tagcategory.SSTagAndCategoryCommonSQL;

public class SSTagImpl
extends SSServImplWithDBA
implements
  SSTagClientI,
  SSTagServerI,
  SSEntityHandlerImplI,
  SSUserRelationGathererI,
  SSUsersResourcesGathererI{
  
  final SSTagAndCategoryCommonSQL  sqlFct;
  final SSTagAndCategoryCommonMisc commonMiscFct;
  
  public SSTagImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    sqlFct        = new SSTagAndCategoryCommonSQL (dbSQL, SSEntityE.tag);
    commonMiscFct = new SSTagAndCategoryCommonMisc(dbSQL, SSEntityE.tag);
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
      
      for(SSEntity tagEntity : 
        tagsGet(
          new SSTagsGetPar(
            null,
            null,
            userUri,
            userUri, //forUser
            null, //entities
            null, //labels,
            null, //space
            null, //circles
            null, //startTime,
            false))){ //withUserRestriction){
        
        SSTagUserRelationGathererFct.addUserForTag     (tagEntity, usersPerTag);
        SSTagUserRelationGathererFct.addUserForEntity  (tagEntity, usersPerEntity);
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
      
      for(SSEntity tagEntity :
        tagsGet(
          new SSTagsGetPar(
            null,
            null,
            userUri,
            userUri, //forUser
            null, //entities
            null, //labels
            null, //space
            null, //circles
            null, //startTime,
            false))){ //withUserRestriction
        
        if(usersResources.containsKey(user)){
          usersResources.get(user).add(((SSTag)tagEntity).entity);
        }else{
          
          final List<SSUri> resourceList = new ArrayList<>();
          
          resourceList.add(((SSTag)tagEntity).entity);
          
          usersResources.put(user, resourceList);
        }
      }
    }
    
    for(Map.Entry<String, List<SSUri>> resourcesPerUser : usersResources.entrySet()){
      SSStrU.distinctWithoutNull2(resourcesPerUser.getValue());
    }
  }
  
  @Override
  public void copyEntity(
    final SSEntity                  entity,
    final SSEntityCopyPar           par) throws Exception{
    
  }
  
  @Override
  public void entityCopied(final SSEntityCopiedPar par) throws Exception{
    
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
  public void circleContentChanged(final SSCircleContentChangedPar par) throws Exception{
    
  } 
  
  @Override
  public SSEntity getUserEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setTags){
        
        entity.tags.addAll(
          tagsGet(
            new SSTagsGetPar(
              null,
              null,
              par.user,
              null, //forUser
              SSUri.asListWithoutNullAndEmpty(entity.id), //entities
              null, //labels
              null, //space
              null, //circle
              null, //startTime
              par.withUserRestriction))); //withUserRestriction
      }
      
      return entity;
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
              par.circle,
              par.creationTime,
              par.withUserRestriction,
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
  public void tagAdd(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSTagAddPar par    = (SSTagAddPar) parA.getFromJSON(SSTagAddPar.class);
    final SSUri       tagUri = tagAdd(par);
    
    sSCon.writeRetFullToClient(SSTagAddRet.get(tagUri));
    
    SSTagActivityFct.addTag(par, tagUri);
  }
  
  @Override
  public SSUri tagAdd(final SSTagAddPar par) throws Exception {
    
    try{
      
      final SSUri       tagUri;
      final SSEntity    circleEntity;
      final SSEntity    tagEntity = 
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
          new SSEntityFromTypeAndLabelGetPar(
            null,
            null,
            par.user,
            SSLabel.get(SSStrU.toStr(par.label)), //label,
            SSEntityE.tag, //type,
            par.withUserRestriction)); //withUserRestriction
      
      if(par.space == null){
        par.space = SSSpaceE.sharedSpace;
      }

      switch(par.space){
        case circleSpace:{
          
          if(par.circle == null){
            throw new SSErr(SSErrE.parameterMissing);
          }
          
          circleEntity =
            ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
              new SSEntityGetPar(
                null,
                null,
                par.user,
                par.circle,
                par.withUserRestriction,
                null)); //descPar
          
          if(circleEntity == null){
            return null;
          }
          
          break;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(tagEntity != null){
        tagUri = tagEntity.id;
      }else{
        tagUri = SSServCaller.vocURICreate();
      }
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          par.entity,
          null, //type,
          null, //label
          null, //description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          tagUri,
          SSEntityE.tag, //type,
          SSLabel.get(SSStrU.toStr(par.label)), //label
          null, //description,
          null, //entitiesToAttach,
          par.creationTime, //creationTime,
          null, //read,
          true, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
//      if(tagEntity == null){
//        sqlFct.addTagIfNotExists(
//          tagUri, 
//          false);
//      }
      
      sqlFct.addMetadataAssIfNotExists(
        tagUri,
        par.user,
        par.entity,
        par.space,
        par.circle,
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
  public void tagsRemove(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSTagsRemovePar par = (SSTagsRemovePar) parA.getFromJSON(SSTagsRemovePar.class);
    
    sSCon.writeRetFullToClient(SSTagsRemoveRet.get(tagsRemove(par)));
    
    SSTagActivityFct.removeTags(par);
  }
  
  @Override
  public Boolean tagsRemove(final SSTagsRemovePar par) throws Exception {
    
    try{
      
      SSUri tagUri = null;
      
      if(par.withUserRestriction){
        
        if(SSObjU.isNull(par.user, par.entity)){
          throw new SSErr(SSErrE.parameterMissing);
        }
      }
      
      if(par.label != null){
        
        final SSEntity tagEntity =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
            new SSEntityFromTypeAndLabelGetPar(
              null,
              null,
              par.user,
              SSLabel.get(SSStrU.toStr(par.label)), //label,
              SSEntityE.tag, //type,
              par.withUserRestriction)); //withUserRestriction
        
        if(tagEntity == null){
          return true;
        }else{
          tagUri = tagEntity.id;
        }
      }
      
      if(!par.withUserRestriction){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeMetadataAsss(
          par.forUser,
          par.entity,
          tagUri,
          par.space);
        
        dbSQL.commit(par.shouldCommit);
        
        return true;
      }
      
      //check whether user can access the entity
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
        new SSEntityGetPar(
          null,
          null,
          par.user,
          par.entity, //entity
          par.withUserRestriction, //withUserRestriction
          null));  //descPar
      
      if(
        par.space    == null &&
        par.entity == null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeMetadataAsss(par.user, null, tagUri, SSSpaceE.privateSpace);
        sqlFct.removeMetadataAsss(par.user, null, tagUri, SSSpaceE.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space    != null &&
        par.entity == null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeMetadataAsss(par.user, null, tagUri, par.space);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space    == null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeMetadataAsss (par.user, par.entity, tagUri, SSSpaceE.privateSpace);
        sqlFct.removeMetadataAsss (null,     par.entity, tagUri, SSSpaceE.sharedSpace);
        
        dbSQL.commit(par.shouldCommit);
        return true;
      }
      
      if(
        par.space    != null &&
        par.entity != null){
        
        dbSQL.startTrans(par.shouldCommit);
        
        sqlFct.removeMetadataAsss(null, par.entity, tagUri, par.space);
        
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
  public void tagEntitiesForTagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(
      SSTagEntitiesForTagsGetRet.get(
        tagEntitiesForTagsGet((SSTagEntitiesForTagsGetPar) parA.getFromJSON(SSTagEntitiesForTagsGetPar.class))));
  }
  
  @Override
  public List<SSUri> tagEntitiesForTagsGet(final SSTagEntitiesForTagsGetPar par) throws Exception{
    
    //TODO dtheiler: use start time for this call as well
    try{
      
      final List<SSUri> entityURIs = new ArrayList<>();
      
      if(par.user == null){
        throw new Exception("user null");
      }
      
      if(
        par.forUser != null &&
        !SSStrU.equals(par.user,  par.forUser)){
        par.space = SSSpaceE.sharedSpace;
      }
      
      if(par.space == null){
        
        entityURIs.addAll(
          commonMiscFct.getEntitiesForMetadataIfSpaceNotSet(
            par.user, 
            par.forUser, 
            SSStrU.toStr(par.labels)));
      }else{
        
        switch(par.space){
          
          case privateSpace:{
            entityURIs.addAll(
              commonMiscFct.getEntitiesForMetadataIfSpaceSet(
                par.user, 
                SSStrU.toStr(par.labels), 
                par.space, 
                par.user));
            break;
          }
          
          case sharedSpace:{
            entityURIs.addAll(
              commonMiscFct.getEntitiesForMetadataIfSpaceSet(
                par.user, 
                SSStrU.toStr(par.labels), 
                par.space, 
                par.forUser));
            break;
          }
        }
      } 
          
      return commonMiscFct.filterEntitiesUserCanAccess(
        entityURIs, 
        par.withUserRestriction, 
        par.user, 
        par.forUser);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void tagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSTagsGetPar par = (SSTagsGetPar) parA.getFromJSON(SSTagsGetPar.class);
    sSCon.writeRetFullToClient(SSTagsGetRet.get(tagsGet(par)));
  }
  
  @Override
  public List<SSEntity> tagsGet(final SSTagsGetPar par) throws Exception {
    
    try{
      
      final List<SSEntity>      tags   = new ArrayList<>();
      
      if(par.user == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(
        par.forUser != null &&
        !SSStrU.equals(par.user,  par.forUser)){
        par.space = SSSpaceE.sharedSpace;
      }
      
      if(par.space == null){
        tags.addAll(
          commonMiscFct.getMetadataIfSpaceNotSet(
            par.user, 
            par.forUser, 
            par.entities, 
            SSStrU.toStr(par.labels), 
            par.circles, 
            par.startTime));
        
      }else{
        switch(par.space){
          
          case privateSpace:{
            tags.addAll(
              commonMiscFct.getMetadataIfSpaceSet(
                par.user, 
                par.entities, 
                SSStrU.toStr(par.labels), 
                par.circles, 
                par.space, 
                par.startTime));
            break;
          }
          
          case sharedSpace:
          case circleSpace:{
            tags.addAll(
              commonMiscFct.getMetadataIfSpaceSet(
                par.forUser, 
                par.entities, 
                SSStrU.toStr(par.labels), 
                par.circles, 
                par.space, 
                par.startTime));
            break;
          }
        }
      }
      
      return commonMiscFct.filterMetadataByEntitiesUserCanAccess(
        tags, 
        par.withUserRestriction, 
        par.user, 
        par.forUser);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void tagFrequsGet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSTagFrequsGetPar par = (SSTagFrequsGetPar) parA.getFromJSON(SSTagFrequsGetPar.class);
    
    sSCon.writeRetFullToClient(SSTagFrequsGetRet.get(tagFrequsGet(par)));
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
        
        //TODO change: hack from bits and pieces
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
            ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
              new SSEntitiesGetPar(
                null,
                null,
                par.user,
                null, //entities
                types, //types
                null, //descPar
                par.withUserRestriction)))); //withUserRestriction
      }
      
      final List<SSTagFrequ> tagFrequs = new ArrayList<>();
      
      for(SSEntityA tagFrequ :
        commonMiscFct.getMetadataFrequsFromMetadata(
          tagsGet(
            new SSTagsGetPar(
              null,
              null,
              par.user,
              par.forUser,
              par.entities,
              par.labels,
              par.space,
              par.circles,
              par.startTime,
              par.withUserRestriction)),
          par.space)){
        
        tagFrequs.add((SSTagFrequ) tagFrequ);
      }
      
      return tagFrequs;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}


//@Override
//  public SSUri tagEdit(final SSTagEditPar par) throws Exception {
//    
//    try{
//      
//      SSUri                  newTagUri = null;
//      
//      if(par.user == null){
//        throw new Exception("user null");
//      }
//      
//      final SSUri    tagUri;
//      final SSEntity tagEntity = 
//        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
//          new SSEntityGetPar(
//            null,
//            null,
//            par.user,
//            null, //entity
//            null, //forUser,
//            SSLabel.get(SSStrU.toStr(par.label)), //label,
//            SSEntityE.tag, //type,
//            false, //withUserRestriction
//            false, //invokeEntityHandlers
//            null,  //descPar
//            true)); //logErr
//      
//      if(tagEntity != null){
//        tagUri = tagEntity.id;
//      }else{
//        tagUri = SSServCaller.vocURICreate();
//      }
//      
//      final List<SSTag> tags =
//        tagsGet(
//          new SSTagsGetPar(
//            null,
//            null,
//            par.user,
//            par.user,
//            SSUri.asListWithoutNullAndEmpty(par.entity),
//            SSTagLabel.asListWithoutNullAndEmpty(par.tag),
//            null,
//            null));
//      
//      for(SSTag tag : tags){
//        
//        tagsRemove(
//          new SSTagsRemovePar(
//            null,
//            null,
//            par.user,
//            par.user,
//            tag.entity,
//            SSTagLabel.get(SSStrU.toStr(tag.label)),
//            tag.space,
//            false,
//            false));
//        
//        newTagUri =
//          tagAdd(
//            new SSTagAddPar(
//              null,
//              null,
//              par.user,
//              tag.entity,
//              par.label,
//              tag.space,
//              null,
//              false));
//      }
//      
//      if(newTagUri == null){
//        return tagUri;
//      }else{
//        return newTagUri;
//      }
//      
//    }catch(Exception error){
//      
//      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
//        
//        if(dbSQL.rollBack(par.shouldCommit)){
//          
//          SSServErrReg.reset();
//          
//          return tagEdit(par);
//        }else{
//          SSServErrReg.regErrThrow(error);
//          return null;
//        }
//      }
//      
//      dbSQL.rollBack(par.shouldCommit);
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }