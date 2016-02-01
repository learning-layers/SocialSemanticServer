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

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.par.SSEntitiesGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityFromTypeAndLabelGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.enums.SSSpaceE;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.entity.api.SSUserRelationGathererI;
import at.tugraz.sss.serv.entity.api.SSUsersResourcesGathererI;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
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
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsAddRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsRemoveRet;
import at.kc.tugraz.ss.service.tag.impl.fct.userrelationgatherer.SSTagUserRelationGathererFct;
import at.tugraz.sss.serv.entity.api.SSCircleContentRemovedI;
import at.tugraz.sss.serv.datatype.par.SSCircleContentRemovedPar;
import at.tugraz.sss.serv.datatype.par.SSCirclePubURIGetPar;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.entity.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.api.SSEntityA;
import at.tugraz.sss.serv.datatype.SSEntityContext;
import at.tugraz.sss.serv.entity.api.SSEntityCopiedI;
import at.tugraz.sss.serv.datatype.par.SSEntityCopiedPar;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import java.util.*;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.datatype.enums.SSSearchOpE;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 
import at.tugraz.sss.servs.common.impl.metadata.SSTagAndCategoryCommonMisc;
import at.tugraz.sss.servs.common.impl.metadata.SSTagAndCategoryCommonSQL;
import sss.serv.eval.api.SSEvalServerI;

public class SSTagImpl
extends SSServImplWithDBA
implements
  SSTagClientI,
  SSTagServerI,
  SSDescribeEntityI,
  SSEntityCopiedI,
  SSUserRelationGathererI,
  SSCircleContentRemovedI,
  SSUsersResourcesGathererI{
  
  private final SSTagActAndLogFct          actAndLogFct;
  private final SSTagAndCategoryCommonSQL  sql;
  private final SSTagAndCategoryCommonMisc commonMiscFct;
  private final SSEntityServerI            entityServ;
  private final SSUserCommons           userCommons;
  
  public SSTagImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql           = new SSTagAndCategoryCommonSQL (dbSQL, SSConf.systemUserUri, SSEntityE.tag);
    this.commonMiscFct = new SSTagAndCategoryCommonMisc(dbSQL, SSEntityE.tag);
    
    this.actAndLogFct =
      new SSTagActAndLogFct(
        (SSActivityServerI) SSServReg.getServ (SSActivityServerI.class),
        (SSEvalServerI)     SSServReg.getServ (SSEvalServerI.class));
    
    this.entityServ  = (SSEntityServerI)   SSServReg.getServ (SSEntityServerI.class);
    this.userCommons = new SSUserCommons();
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(par.setTags){
        
        entity.tags.addAll(
          tagsGet(
            new SSTagsGetPar(
              servPar,
              par.user,
              null, //forUser
              SSUri.asListNotNull(entity.id), //entities
              null, //labels
              null, //labelSearchOp,
              SSSpaceE.asListWithoutNull(par.space), //spaces
              SSUri.asListNotNull(par.circle), //circles
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
  public void getUserRelations(
    final SSServPar servPar,
    final List<String>             allUsers,
    final Map<String, List<SSUri>> userRelations) throws SSErr{
    
    try{
      
      final Map<String, List<SSUri>> usersPerTag    = new HashMap<>();
      final Map<String, List<SSUri>> usersPerEntity = new HashMap<>();
      List<SSTag>                    tags;
      
      for(String user : allUsers){
        
        final SSUri userUri = SSUri.get(user);
        
        for(SSEntity tagEntity :
          tagsGet(
            new SSTagsGetPar(
              servPar,
              userUri,
              userUri, //forUser
              null, //entities
              null, //labels,
              null, //labelSearchOp,
              null, //spaces
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
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void getUsersResources(
    final SSServPar servPar,
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      final SSTagsGetPar tagsGetPar =
        new SSTagsGetPar(
              servPar,
          null, //user
          null, //forUser
          null, //entities
          null, //labels
          null, //labelSearchOp,
          null, //spaces
          null, //circles
          null, //startTime,
          false); //withUserRestriction
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        tagsGetPar.user    = userID;
        tagsGetPar.forUser = userID;
        
        for(SSEntity tagEntity : tagsGet(tagsGetPar)){
          
          usersEntities.get(user).add(
            new SSEntityContext(
              ((SSTag) tagEntity).entity,
              SSEntityE.tag,
              SSStrU.toStr(((SSTag) tagEntity).tagLabel),
              ((SSTag) tagEntity).creationTime));
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void circleContentRemoved(
    final SSServPar servPar,
    final SSCircleContentRemovedPar par) throws SSErr {
    
    if(!par.removeCircleSpecificMetadata){
      return;
    }
    
    for(SSUri entity : par.entities){
      
      tagsRemove(
        new SSTagsRemovePar(
              servPar,
          par.user,
          null, //forUser
          entity,
          null, //label,
          SSSpaceE.circleSpace, //space,
          par.circle,
          par.withUserRestriction,
          false));
    }
  }
  
  @Override
  public void entityCopied(
    final SSServPar servPar, 
    final SSEntityCopiedPar par) throws SSErr{
    
    try{
      
      if(!par.includeMetadataSpecificToEntityAndItsEntities){
        return;
      }
      
      switch(par.entity.type){
        
        case circle:{
          
          for(SSEntity tag :
            tagsGet(
              new SSTagsGetPar(
                servPar,
                par.user,
                null, //forUser
                SSUri.getDistinctNotNullFromEntities(par.entities), //entities
                null, //labels
                null, //labelSearchOp,
                SSSpaceE.asListWithoutNull(SSSpaceE.circleSpace), //spaces
                SSUri.getDistinctNotNullFromEntities(par.entity), //circles
                null, //startTime,
                par.withUserRestriction))){
            
            if(par.targetUser != null){
              
              tagAdd(
                new SSTagAddPar(
                servPar,
                  par.targetUser,  //user
                  ((SSTag)tag).entity, //entity
                  ((SSTag)tag).tagLabel, //label
                  ((SSTag)tag).space, //space
                  par.targetEntity, //circle
                  tag.creationTime, //creationTime
                  par.withUserRestriction, //withUserRestriction
                  false)); //shouldCommmit
            }else{
              
              tagAdd(
                new SSTagAddPar(
                servPar,
                  ((SSTag)tag).user, //user
                  ((SSTag)tag).entity, //entity
                  ((SSTag)tag).tagLabel, //label
                  ((SSTag)tag).space, //space
                  par.targetEntity, //circle
                  tag.creationTime, //creationTime
                  par.withUserRestriction, //withUserRestriction
                  false)); //shouldCommmit
            }
          }
          break;
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSServRetI tagsAdd(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSTagsAddPar par     = (SSTagsAddPar) parA.getFromClient(clientType, parA, SSTagsAddPar.class);
      final List<SSUri>  tagURIs = tagsAdd(par);
      final SSTagsAddRet ret     = SSTagsAddRet.get(tagURIs);
      
      actAndLogFct.tagsAdd(
        par,
        par.user,
        par.entities,
        tagURIs,
        par.labels,
        par.shouldCommit);
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> tagsAdd(final SSTagsAddPar par) throws SSErr {
    
    try{
      
      final List<SSUri> tags = new ArrayList<>();
      
      for(SSTagLabel tagLabel : par.labels) {
        
        for(SSUri entity : par.entities){
          
          SSUri.addDistinctWithoutNull(
            tags,
            tagAdd(
              new SSTagAddPar(
                par,
                par.user,
                entity,
                tagLabel,
                par.space,
                par.circle,
                par.creationTime,
                par.withUserRestriction,
                par.shouldCommit)));
        }
      }
      
      SSStrU.distinctWithoutNull2(tags);
      
      return tags;
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
  
  @Override
  public SSServRetI tagAdd(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSTagAddPar par    = (SSTagAddPar) parA.getFromClient(clientType, parA, SSTagAddPar.class);
      final SSUri       tagURI = tagAdd(par);
      final SSTagAddRet ret    = SSTagAddRet.get(tagURI);
      
      if(tagURI != null){
        
        actAndLogFct.tagsAdd(
          par,
          par.user,
          SSUri.asListNotNull(par.entity),
          SSUri.asListNotNull(tagURI),
          SSTagLabel.asListNotEmpty(par.label),
          par.shouldCommit);
      }
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri tagAdd(final SSTagAddPar par) throws SSErr {
    
    try{
      
      final SSUri             tagUri;
      final List<SSEntity>    tagEntities =
        entityServ.entityFromTypeAndLabelGet(
          new SSEntityFromTypeAndLabelGetPar(
                par,
            par.user,
            SSLabel.get(SSStrU.toStr(par.label)), //label,
            SSEntityE.tag, //type,
            par.withUserRestriction)); //withUserRestriction
      
      if(SSObjU.isNull(par.circle)){
        
        par.circle =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).circlePubURIGet(
            new SSCirclePubURIGetPar(
                par,
              null,
              false));
      }else{
        par.space = SSSpaceE.circleSpace;
      }
      
      if(par.space == null){
        par.space = SSSpaceE.sharedSpace;
      }
      
      switch(par.space){
        
        case circleSpace:{
          
          final SSEntity circle =
            sql.getEntityTest(
                par,
              par.user,
              par.circle,
              par.withUserRestriction);
          
          if(circle == null){
            return null;
          }
          
          break;
        }
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      par.entity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
                par,
            par.user,
            par.entity,
            null, //type,
            null, //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(par.entity == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      if(tagEntities.isEmpty()){
        
        tagUri =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
                par,
              par.user,
              SSConf.vocURICreate(),
              SSEntityE.tag, //type,
              SSLabel.get(SSStrU.toStr(par.label)), //label
              null, //description,
              par.creationTime, //creationTime,
              null, //read,
              true, //setPublic
              true, //createIfNotExists
              par.withUserRestriction, //withUserRestriction
              false)); //shouldCommit)
        
        if(tagUri == null){
          dbSQL.rollBack(par, par.shouldCommit);
          return null;
        }
      }else{
        tagUri = tagEntities.get(0).id;
      }
      
      sql.addMetadataAssIfNotExists1(
                par,
        tagUri,
        par.user,
        par.entity,
        par.space,
        par.circle,
        par.creationTime);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return tagUri;
      
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
  
  @Override
  public SSServRetI tagsRemove(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSTagsRemovePar par    = (SSTagsRemovePar) parA.getFromClient(clientType, parA, SSTagsRemovePar.class);
      final boolean         worked = tagsRemove(par);
      final SSTagsRemoveRet ret    = SSTagsRemoveRet.get(worked);
      
      if(worked){
        
        actAndLogFct.tagsRemove(
          par,
          par.user,
          par.entity,
          par.label,
          par.shouldCommit);
      }
      
      return ret;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean tagsRemove(final SSTagsRemovePar par) throws SSErr {
    
    try{
      
      if(SSObjU.isNull(par.user)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.entity != null){
        
        final SSEntity entity =
          sql.getEntityTest(
            par,
            par.user,
            par.entity,
            par.withUserRestriction);
        
        if(entity == null){
          return false;
        }
      }
      
      if(par.withUserRestriction){
        
        if(SSObjU.isNull(par.entity)){
          throw SSErr.get(SSErrE.parameterMissing);
        }
        
        if(par.circle != null){
          
          final SSEntity circle =
            sql.getEntityTest(
                par,
              par.user,
              par.circle,
              par.withUserRestriction);
          
          if(circle == null){
            return false;
          }
        }
        
        if(!SSStrU.equals(par.space, SSSpaceE.circleSpace)){
          par.circle = null;
        }
      }
      
      SSUri tagUri = null;
      
      if(par.label != null){
        
        final List<SSEntity> tagEntities =
          entityServ.entityFromTypeAndLabelGet(
            new SSEntityFromTypeAndLabelGetPar(
                par,
              par.user,
              SSLabel.get(SSStrU.toStr(par.label)), //label,
              SSEntityE.tag, //type,
              par.withUserRestriction)); //withUserRestriction
        
        if(tagEntities.isEmpty()){
          return true;
        }else{
          tagUri = tagEntities.get(0).id;
        }
      }
      
      if(!par.withUserRestriction){
        
        dbSQL.startTrans(par, par.shouldCommit);
        
        sql.removeMetadataAsss(
                par,
          par.forUser,
          par.entity,
          tagUri,
          par.space,
          par.circle);
        
        dbSQL.commit(par, par.shouldCommit);
        
        return true;
      }
      
      if(
        par.space  == null &&
        par.entity == null){
        
        dbSQL.startTrans(par, par.shouldCommit);
        
        sql.removeMetadataAsss(par, par.user, null, tagUri, SSSpaceE.privateSpace, par.circle);
        sql.removeMetadataAsss(par, par.user, null, tagUri, SSSpaceE.sharedSpace,  par.circle);
        sql.removeMetadataAsss(par, par.user, null, tagUri, SSSpaceE.circleSpace,  par.circle);
        
        dbSQL.commit(par, par.shouldCommit);
        return true;
      }
      
      if(
        par.space  != null &&
        par.entity == null){
        
        dbSQL.startTrans(par, par.shouldCommit);
        
        sql.removeMetadataAsss(par, par.user, null, tagUri, par.space, par.circle);
        
        dbSQL.commit(par, par.shouldCommit);
        return true;
      }
      
      if(
        par.space  == null &&
        par.entity != null){
        
        dbSQL.startTrans(par, par.shouldCommit);
        
        sql.removeMetadataAsss (par, par.user, par.entity, tagUri, SSSpaceE.privateSpace, par.circle);
        sql.removeMetadataAsss (par, null,     par.entity, tagUri, SSSpaceE.sharedSpace,  par.circle);
        sql.removeMetadataAsss (par, null,     par.entity, tagUri, SSSpaceE.circleSpace,  par.circle);
        
        dbSQL.commit(par, par.shouldCommit);
        return true;
      }
      
      if(
        par.space  != null &&
        par.entity != null){
        
        dbSQL.startTrans(par, par.shouldCommit);
        
        sql.removeMetadataAsss(par, null, par.entity, tagUri, par.space, par.circle);
        
        dbSQL.commit(par, par.shouldCommit);
        return true;
      }
      
      throw new Exception("reached not reachable code");
      
    }catch(SSErr error){
      
      switch(error.code){

        case sqlDeadLock:{
          
          try{
            dbSQL.rollBack(par, par.shouldCommit);
            SSServErrReg.regErrThrow(error);
            return false;
          }catch(Exception error2){
            SSServErrReg.regErrThrow(error2);
            return false;
          }
        }
        
        default:{
          SSServErrReg.regErrThrow(error);
          return false;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public SSServRetI tagEntitiesForTagsGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSTagEntitiesForTagsGetPar par = (SSTagEntitiesForTagsGetPar) parA.getFromClient(clientType, parA, SSTagEntitiesForTagsGetPar.class);
      
      return SSTagEntitiesForTagsGetRet.get(tagEntitiesForTagsGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> tagEntitiesForTagsGet(final SSTagEntitiesForTagsGetPar par) throws SSErr{
    
    try{
      
      final List<SSEntity> tagAsss =
        tagsGet(
          new SSTagsGetPar(
            par, 
            par.user,
            par.forUser,
            par.entities,
            par.labels,
            par.labelSearchOp,
            par.spaces,
            par.circles,
            par.startTime,
            par.withUserRestriction));
      
      return commonMiscFct.getEntitiesFromMetadataDistinctNotNull(par, tagAsss);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI tagsGet(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSTagsGetPar par = (SSTagsGetPar) parA.getFromClient(clientType, parA, SSTagsGetPar.class);
      
      return SSTagsGetRet.get(tagsGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> tagsGet(final SSTagsGetPar par) throws SSErr {
    
    try{
      
      if(par.user == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        if(
          par.forUser != null &&
          !SSStrU.equals(par.user, par.forUser)){
          throw SSErr.get(SSErrE.userNotAllowedToRetrieveForOtherUser);
        }
      }
      
      final List<SSEntity> tags =
        commonMiscFct.getMetadata(
          par, 
          par.user,
          par.forUser,
          par.entities,
          SSStrU.toStr(par.labels),
          par.labelSearchOp,
          par.spaces,
          par.circles,
          par.startTime);
      
      return commonMiscFct.filterMetadataByEntitiesUserCanAccess(
        par, 
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
  public SSServRetI tagFrequsGet(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSTagFrequsGetPar par = (SSTagFrequsGetPar) parA.getFromClient(clientType, parA, SSTagFrequsGetPar.class);
      
      return SSTagFrequsGetRet.get(tagFrequsGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSTagFrequ> tagFrequsGet(final SSTagFrequsGetPar par) throws SSErr {
    
    try{
      
      if(
        !par.entities.isEmpty() &&
        par.useUsersEntities){
        throw new Exception("entities and useForUsersEntities set at the same time");
      }
      
      if(par.useUsersEntities){
        
        //TODO change: hack from bits and pieces
        final List<SSEntityE> types =
          SSEntityE.asListNotNull(
            SSEntityE.entity,
            SSEntityE.uploadedFile,
            SSEntityE.evernoteResource,
            SSEntityE.evernoteNote,
            SSEntityE.evernoteNotebook,
            SSEntityE.placeholder);
        
        final SSEntitiesGetPar entitiesGetPar =
          new SSEntitiesGetPar(
            par, 
            par.user,
            null, //entities
            null, //descPar
            par.withUserRestriction);
        
        entitiesGetPar.types.addAll(types);
        
        par.entities.addAll(
          SSUri.getDistinctNotNullFromEntities(
            entityServ.entitiesGet(entitiesGetPar))); //withUserRestriction
      }
      
      final List<SSTagFrequ> tagFrequs = new ArrayList<>();
      
      for(SSEntityA tagFrequ :
        commonMiscFct.getMetadataFrequsFromMetadata(
          par,
          tagsGet(
            new SSTagsGetPar(
              par, 
              par.user,
              par.forUser,
              par.entities,
              par.labels,
              SSSearchOpE.or, //labelSearchOp
              par.spaces,
              par.circles,
              par.startTime,
              par.withUserRestriction)))){
        
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
//  public SSUri tagEdit(final SSTagEditPar par) throws SSErr {
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
//        entityServ.entityGet(
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
//        tagUri = SSConf.vocURICreate();
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
//      dbSQL.rollBack(par, par.shouldCommit);
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//@Override
//  public List<SSUri> tagEntitiesForTagsGet(final SSTagEntitiesForTagsGetPar par) throws SSErr{
//
//    //TODO dtheiler: use start time for this call as well
//    try{
//
//      final List<SSUri> entityURIs = new ArrayList<>();
//
//      if(par.user == null){
//        throw SSErr.get(SSErrE.parameterMissing);
//      }
//
//      if(par.withUserRestriction){
//
//        if(
//          par.forUser != null &&
//          !SSStrU.equals(par.user, par.forUser)){
//          throw new SSErr(SSErrE.userNotAllowedToRetrieveForOtherUser);
//        }
//      }
//
//      if(par.spaces.isEmpty()){
//
//        SSUri.addDistinctWithoutNull(
//          entityURIs,
//          commonMiscFct.getEntitiesForMetadataIfSpaceNotSet(
//            par.user,
//            par.forUser,
//            SSStrU.toStr(par.labels)));
//      }else{
//
//        for(SSSpaceE space : par.spaces){
//
//          switch(space){
//
//            case privateSpace:{
//
//              SSUri.addDistinctWithoutNull(
//                entityURIs,
//                commonMiscFct.getEntitiesForMetadataIfSpaceSet(
//                  par.user,
//                  SSStrU.toStr(par.labels),
//                  space,
//                  par.user));
//
//              break;
//            }
//
//            case sharedSpace:{
//
//              SSUri.addDistinctWithoutNull(
//                entityURIs,
//                commonMiscFct.getEntitiesForMetadataIfSpaceSet(
//                  par.user,
//                  SSStrU.toStr(par.labels),
//                  space,
//                  par.forUser));
//              break;
//            }
//          }
//        }
//      }
//
//      return commonMiscFct.filterEntitiesUserCanAccess(
//        entityURIs,
//        par.withUserRestriction,
//        par.user,
//        par.forUser);
//
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }