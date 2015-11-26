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
package at.kc.tugraz.ss.service.coll.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubURIGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryDeletePar;
import at.tugraz.sss.serv.*;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.kc.tugraz.ss.service.coll.api.*;
import at.kc.tugraz.ss.service.coll.datatypes.*;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollGetPar;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSConfA;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserParentGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserRootGetRet;
import at.kc.tugraz.ss.service.coll.impl.fct.sql.SSCollSQLFct;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesDeletePar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesDeleteRet;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollCumulatedTagsGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserHierarchyGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserEntityIsInGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserCumulatedTagsGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserHierarchyGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserEntityIsInGetRet;
import at.kc.tugraz.ss.service.coll.impl.fct.activity.SSCollActivityFct;
import at.kc.tugraz.ss.service.coll.impl.fct.misc.SSCollMiscFct;
import at.kc.tugraz.ss.service.coll.impl.fct.op.SSCollEntryAddFct;
import at.kc.tugraz.ss.service.coll.impl.fct.op.SSCollEntryDeleteFct;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagFrequ;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagFrequsGetPar;
import java.util.*;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSWarnE;

public class SSCollImpl 
extends SSServImplWithDBA 
implements 
  SSCollClientI, 
  SSCollServerI, 
  SSDescribeEntityI,
  SSGetParentEntitiesI, 
  SSGetSubEntitiesI,
  SSAddAffiliatedEntitiesToCircleI,
  SSPushEntitiesToUsersI,
  SSUserRelationGathererI,
  SSUsersResourcesGathererI{

  private final SSCollSQLFct    sqlFct;
  private final SSEntityServerI entityServ;
  private final SSCircleServerI circleServ;
  
  public SSCollImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    this.sqlFct     = new SSCollSQLFct(dbSQL);
    this.entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
    this.circleServ = (SSCircleServerI) SSServReg.getServ(SSCircleServerI.class);
  }

  @Override
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      switch(entity.type){
        
        case coll:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          return SSColl.get(
            collGet(
              new SSCollGetPar(
                par.user,
                entity.id,
                par.withUserRestriction,
                false)), //invokeEntityHandlers
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
  public void getUserRelations(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> userRelations) throws Exception{
    
    List<SSEntity>                 collUserCircles;
    List<SSEntity>                 collEntryUserCircles;
    List<SSEntity>                 allColls;
    SSCollEntry                    collEntry;
    
    for(String user : allUsers){
      
      final SSUri userUri = SSUri.get(user);
      
      allColls =
        collsGet(
          new SSCollsGetPar(
            userUri,
            false, //withUserRestriction
            false)); //invokeEntityHandlers
      
      for(SSEntity coll : allColls){
        
        collUserCircles =
          circleServ.circlesGet(
            new SSCirclesGetPar(
              userUri,
              userUri,
              coll.id,
              null, //entityTypesToIncludeOnly
              false, //setEntities
              true, //setUsers
              false, // withUserRestriction
              true, // withSystemCircles
              false)); //invokeEntityHandlers

        for(SSEntity circle : collUserCircles){
          
          if(userRelations.containsKey(user)){
            userRelations.get(user).addAll(SSUri.getDistinctNotNullFromEntities(circle.users));
          }else{
            userRelations.put(user, SSUri.getDistinctNotNullFromEntities(circle.users));
          }
        }
        
        for(Object entry : coll.entries){
          
          collEntry = (SSCollEntry) entry;
          
          collEntryUserCircles =
            circleServ.circlesGet(
              new SSCirclesGetPar(
                userUri,
                userUri,
                collEntry.id,
                null, //entityTypesToIncludeOnly
                false, //setEntities,
                true, //setUsers
                false, // withUserRestriction
                true, // withSystemCircles
                false)); //invokeEntityHandlers
          
          for(SSEntity circle : collEntryUserCircles){
            
            if(userRelations.containsKey(user)){
              userRelations.get(user).addAll(SSUri.getDistinctNotNullFromEntities(circle.users));
            }else{
              userRelations.put(user, SSUri.getDistinctNotNullFromEntities(circle.users));
            }
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
    final Map<String, List<SSEntityContext>> usersEntities) throws Exception{
    
    try{
      
      final SSCollsGetPar collsGetPar = 
        new SSCollsGetPar(
          null, //user
          false, //withUserRestriction, 
          false); //invokeEntityHandlers)
      
      SSUri userID;
        
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        collsGetPar.user = userID;
        
        for(SSEntity coll : collsGet(collsGetPar)){
          
          usersEntities.get(user).add(
            new SSEntityContext(
              coll.id,
              SSEntityE.coll,
              null,
              null));
          
          for(SSEntity collEntry : coll.entries){
            
            usersEntities.get(user).add(
              new SSEntityContext(
                collEntry.id,
                SSEntityE.coll,
                null,
                null));
          }
        }
      }
      
    }catch(Exception error){
      
      SSLogU.err(error);
      SSServErrReg.reset();
    }
  }
  
  @Override
  public List<SSUri> getParentEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    try{
      return SSUri.getDistinctNotNullFromEntities(
        collsEntityIsInGet(
          new SSCollsUserEntityIsInGetPar(
            user,
            entity,
            false, //withUserRestriction,
            false))); //invokeEntityHandlers));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> getSubEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    try{
      switch(type){
        
        case coll:{
          
          return SSCollMiscFct.getCollSubCollAndEntryURIs(
            sqlFct,
            sqlFct.getCollWithEntries(
              entity));
        }
        
        default: return new ArrayList<>();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSAddAffiliatedEntitiesToCirclePar par) throws Exception{
    
    try{
      
      final List<SSUri>    affiliatedURIs       = new ArrayList<>();
      final List<SSEntity> affiliatedEntities   = new ArrayList<>();
      
      for(SSEntity entityAdded : par.entities){
        
        switch(entityAdded.type){
          
          case coll:{
            
            if(SSStrU.contains(par.recursiveEntities, entityAdded)){
              continue;
            }else{
              SSUri.addDistinctWithoutNull(par.recursiveEntities, entityAdded.id);
            }
            
            if(sqlFct.isCollSpecial(entityAdded.id)){
              
              if(
                SSStrU.equals(
                  par.circle,
                  circleServ.circlePubURIGet(
                    new SSCirclePubURIGetPar(
                      par.user,
                      false)))){
              
                throw new SSErr(SSErrE.cannotSetSpecialCollectionPublic);
              }
              
              throw new SSErr(SSErrE.cannotShareSpecialCollection);
            }
            
            for(SSUri collContentURI : SSCollMiscFct.getCollSubCollAndEntryURIs(sqlFct, sqlFct.getCollWithEntries(entityAdded.id))){
            
              if(SSStrU.contains(par.recursiveEntities, collContentURI)){
                continue;
              }
              
              SSUri.addDistinctWithoutNull(
                affiliatedURIs,
                collContentURI);
            }
          }
        }
      }
      
      if(affiliatedURIs.isEmpty()){
        return affiliatedEntities;
      }
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          par.user,
          par.circle, //circle
          affiliatedURIs, //entities
          false, //withUserRestriction
          false)); //shouldCommit
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities,
        entityServ.entitiesGet(
          new SSEntitiesGetPar(
            par.user,
            affiliatedURIs, //entities
            null, //types,
            null, //descPar
            par.withUserRestriction)));
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities,
        SSServCallerU.handleAddAffiliatedEntitiesToCircle(
          par.user,
          par.circle,
          affiliatedEntities, //entities
          par.recursiveEntities, 
          par.withUserRestriction));
      
      return affiliatedEntities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void pushEntitiesToUsers(
    final SSPushEntitiesToUsersPar par) throws Exception {
    
    try{
      
      SSColl rootColl;
      
      for(SSEntity entityToPush : par.entities){
        
        switch(entityToPush.type){
          
          case coll: {
            
            for(SSUri userToPushTo : par.users){
              
              rootColl =
                collRootGet(
                  new SSCollUserRootGetPar(
                    userToPushTo,
                    false, //withUserRestriction,
                    false)); //invokeEntityHandlers));
              
              if(
                sqlFct.containsCollEntry (rootColl.id,        entityToPush.id) ||
                sqlFct.ownsUserColl      (userToPushTo, entityToPush.id)){
                SSLogU.warn(SSWarnE.collAlreadySharedWithUser);
                continue;
              }
              
              if(SSCollMiscFct.ownsUserASubColl(sqlFct, userToPushTo, entityToPush.id)){
                SSLogU.warn(SSWarnE.subCollAlreadySharedWithUser);
                continue;
              }
              
              sqlFct.addCollToColl(
                userToPushTo,
                rootColl.id,
                entityToPush.id,
                false,
                true);
            }
            
            break;
          }
          
          case uploadedFile:
          case entity:{
            
            SSUri sharedWithMeFilesCollUri;
            
            for(SSUri userToPushTo : par.users){
              
              sharedWithMeFilesCollUri = sqlFct.getSpecialCollURI(userToPushTo);
              
              if(sqlFct.containsCollEntry (sharedWithMeFilesCollUri, entityToPush.id)){
                continue;
              }
              
              sqlFct.addCollEntry(sharedWithMeFilesCollUri, entityToPush.id);
              
              break;
            }
          }
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
//  @Override
//  public void entitiesSharedWithUsers(SSEntitiesSharedWithUsersPar par) throws Exception {
//    
//    for(SSEntity entityShared : par.circle.entities){
//     
//      switch(entityShared.type){
//        case coll:{
////              circleServ.circleUsersAdd(
////                new SSCircleUsersAddPar(
////                  null,
////                  null,
////                  par.user,
////                  par.circle,
////                  sqlFct.getCollUserURIs(entityToAdd.id),
////                  false,
////                  false));
////            }
//      }
//  }

  @Override
  public void collGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSCollGetPar par = (SSCollGetPar) parA.getFromJSON(SSCollGetPar.class);
    
    sSCon.writeRetFullToClient(SSCollGetRet.get(collGet(par)));
  }
  
  @Override
  public SSColl collGet(final SSCollGetPar par) throws Exception{

    try{
      
      if(par.coll == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      SSColl coll = sqlFct.getCollWithEntries(par.coll);
      
      if(coll == null){
        return null;
      }
      
      final SSEntityDescriberPar descPar;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(par.coll);
        
        descPar.setCircleTypes = true;
      }else{
        descPar = null;
      }
      
      final SSEntity collEntity =
        entityServ.entityGet(
          new SSEntityGetPar(
            par.user,
            par.coll, //entity,
            par.withUserRestriction,
            descPar));
      
      if(collEntity == null){
        return null;
      }
      
      coll = SSColl.get(coll, collEntity); 
      
      if(descPar != null){
        descPar.recursiveEntity = null;
      }
      
      final List<SSEntity> collEntries =
        entityServ.entitiesGet(
          new SSEntitiesGetPar(
            par.user,
            SSUri.getDistinctNotNullFromEntities(coll.entries),  //entities
            null, //types,
            descPar, //descPar,
            par.withUserRestriction));

      coll.entries.clear();
      coll.entries.addAll(collEntries);
      
      return coll;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSColl collParentGet(final SSCollUserParentGetPar par) throws Exception{

    try{
      
      if(SSObjU.isNull(par.user, par.coll)){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(!sqlFct.ownsUserColl(par.user, par.coll)){
        throw new SSErr(SSErrE.userDoesntOwnColl);
      }
      
      return collGet(
        new SSCollGetPar(
          par.user,
          SSCollMiscFct.getDirectParentCollURIForUser(
            sqlFct,
            par.user,
            par.coll),
          par.withUserRestriction,
          par.invokeEntityHandlers));

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void collRootGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSCollUserRootGetPar par = (SSCollUserRootGetPar) parA.getFromJSON(SSCollUserRootGetPar.class);
    
    sSCon.writeRetFullToClient(SSCollUserRootGetRet.get(collRootGet(par)));
  }
  
  @Override
  public SSColl collRootGet(final SSCollUserRootGetPar par) throws Exception{

    try{

      if(SSObjU.isNull(par.user)){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      return collGet(
        new SSCollGetPar(
          par.user,
          sqlFct.getRootCollURI(par.user),
          par.withUserRestriction,
          par.invokeEntityHandlers));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> collsGet(final SSCollsGetPar par) throws Exception{

    try{
      
      if(par.user == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final List<SSEntity> colls      = new ArrayList<>();
      final SSCollGetPar   collGetPar =
        new SSCollGetPar(
          par.user,
          null, //coll
          par.withUserRestriction,
          par.invokeEntityHandlers);
        
      for(SSUri collURI : sqlFct.getUserCollURIs(par.user)){
        
        collGetPar.coll = collURI;
          
        SSEntity.addEntitiesDistinctWithoutNull(
          colls,
          collGet(collGetPar));
      }
      
      return colls;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void collHierarchyGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSCollUserHierarchyGetPar par = (SSCollUserHierarchyGetPar) parA.getFromJSON(SSCollUserHierarchyGetPar.class);
    
    sSCon.writeRetFullToClient(SSCollUserHierarchyGetRet.get(collHierarchyGet(par)));
  }
  
  @Override
  public List<SSEntity> collHierarchyGet(final SSCollUserHierarchyGetPar par) throws Exception{

    try{
      
      if(SSObjU.isNull(par.user, par.coll)){
        throw new SSErr(SSErrE.parameterMissing);
      }
            
      final List<SSEntity> colls = new ArrayList<>();      

      if(par.withUserRestriction){
       
        final SSEntity coll = 
          sqlFct.getEntityTest(
            par.user, 
            par.coll, 
            par.withUserRestriction);
          
        if(coll == null){
          return colls;
        }
      }
      
      final List<SSUri>  hierarchy      = new ArrayList<>();
      final SSUri        rootCollUri;
      SSUri              directPartentCollUri;

      rootCollUri          = sqlFct.getRootCollURI(par.user);
      directPartentCollUri = 
        SSCollMiscFct.getDirectParentCollURIForUser(
          sqlFct, 
          par.user, 
          par.coll);

      while(!SSStrU.equals(rootCollUri, directPartentCollUri)){

        hierarchy.add(directPartentCollUri);

        directPartentCollUri = 
          SSCollMiscFct.getDirectParentCollURIForUser(
            sqlFct, 
            par.user, 
            directPartentCollUri);
      }
      
      hierarchy.add(rootCollUri);
      
      final SSCollGetPar collGetPar =
        new SSCollGetPar(
          par.user,
          null, //collURI,
          par.withUserRestriction,
          par.invokeEntityHandlers);
      
      for(SSUri collURI : hierarchy){
        
        collGetPar.coll = collURI;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          colls,
          collGet(collGetPar));
      }
      
      return colls;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void collsEntityIsInGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSCollsUserEntityIsInGetPar par = (SSCollsUserEntityIsInGetPar) parA.getFromJSON(SSCollsUserEntityIsInGetPar.class);
    
    sSCon.writeRetFullToClient(SSCollsUserEntityIsInGetRet.get(collsEntityIsInGet(par)));
  }
  
  @Override
  public List<SSEntity> collsEntityIsInGet(final SSCollsUserEntityIsInGetPar par) throws Exception{

    try{
      
      final List<SSEntity> colls = new ArrayList<>();
      
      if(par.withUserRestriction){
        
        final SSEntity entity = 
          sqlFct.getEntityTest(
            par.user, 
            par.entity, 
            par.withUserRestriction);
        
        if(entity == null){
          return colls;
        }
      }
          
      final List<SSUri>   userCollUris   = sqlFct.getUserCollURIs(par.user);
      final List<SSUri>   entityCollUris = sqlFct.getCollURIsContainingEntity(par.entity);
      final List<String>  commonCollUris = SSStrU.retainAll(SSStrU.toStr(entityCollUris), SSStrU.toStr(userCollUris));
      final SSCollGetPar  collGetPar =
        new SSCollGetPar(
          par.user,
          null, //coll,
          par.withUserRestriction,
          par.invokeEntityHandlers);
      
      for(SSUri collURI : SSUri.get(commonCollUris)){
        
        collGetPar.coll = collURI;
          
        SSEntity.addEntitiesDistinctWithoutNull(
          colls,
          collGet(collGetPar));
      }
      
      return colls;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean collRootAdd(final SSCollUserRootAddPar par) throws Exception{

    try{

      if(SSObjU.isNull(par.user)){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(sqlFct.existsCollRootForUser(par.forUser)){
        return true;
      }

      dbSQL.startTrans(par.shouldCommit);

      final SSUri rootColl =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.forUser,
            SSServCaller.vocURICreate(), //entity
            SSEntityE.coll, //type,
            SSLabel.get(SSStrU.valueRoot), //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(rootColl == null){
        dbSQL.rollBack(par.shouldCommit);
        return false;
      }
      
      sqlFct.addColl(rootColl);

      sqlFct.addCollRoot(
        rootColl, 
        par.forUser);
      
      final SSUri sharedWithMeFilesCollUri =
        collEntryAdd(
          new SSCollUserEntryAddPar(
            par.forUser,
            rootColl, //coll
            null, //entry
            SSLabel.get(SSStrU.valueSharedWithMeFiles), //label
            true, //addNewColl
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit
      
      if(sharedWithMeFilesCollUri == null){
        dbSQL.rollBack(par.shouldCommit);
        return false;
      }
      
      sqlFct.addCollSpecial(
        sharedWithMeFilesCollUri,
        par.forUser);

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return collRootAdd(par);
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
  public void collEntryAdd(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSCollUserEntryAddPar par = (SSCollUserEntryAddPar) parA.getFromJSON(SSCollUserEntryAddPar.class);
    
    sSCon.writeRetFullToClient(SSCollUserEntryAddRet.get(collEntryAdd(par)));
    
    SSCollActivityFct.addCollEntry(par);
  }
  
  @Override
  public SSUri collEntryAdd(final SSCollUserEntryAddPar par) throws Exception{

    try{

      if(SSObjU.isNull(par.coll)){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        final SSEntity coll = 
          sqlFct.getEntityTest(
            par.user, 
            par.coll, 
            par.withUserRestriction);
        
        if(coll == null){
          return null;
        }
        
        final SSEntity entry = 
          sqlFct.getEntityTest(
            par.user, 
            par.coll, 
            par.withUserRestriction);
        
        if(entry == null){
          return null;
        }
      }
      
      if(par.addNewColl){
        
        if(par.label == null){
          throw new SSErr(SSErrE.parameterMissing);
        }
        
        dbSQL.startTrans(par.shouldCommit);

        final SSUri newColl =
          SSCollEntryAddFct.addNewColl(
            circleServ,
            entityServ,
            sqlFct,
            par);
        
        if(newColl == null){
          dbSQL.rollBack(par.shouldCommit);
          return null;
        }

        dbSQL.commit(par.shouldCommit);

        return newColl;
      }
      
      if(sqlFct.containsCollEntry(par.coll, par.entry)){
        return par.entry;
      }

      if(sqlFct.isColl(par.entry)){

        dbSQL.startTrans(par.shouldCommit);

        SSCollEntryAddFct.addPublicColl(
          sqlFct, 
          circleServ, 
          par);

        dbSQL.commit(par.shouldCommit);

        return par.entry;
      }

      dbSQL.startTrans(par.shouldCommit);

      final SSUri entry =
        SSCollEntryAddFct.addCollEntry(
          circleServ,
          entityServ,
          sqlFct,
          par);
      
      if(entry == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }

      dbSQL.commit(par.shouldCommit);

      return par.entry;

    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return collEntryAdd(par);
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
  public void collEntriesAdd(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSCollUserEntriesAddPar par = (SSCollUserEntriesAddPar) parA.getFromJSON(SSCollUserEntriesAddPar.class);
    
    sSCon.writeRetFullToClient(SSCollUserEntriesAddRet.get(collEntriesAdd(par)));
    
    SSCollActivityFct.addCollEntries(par);
  }
  
  @Override
  public List<SSUri> collEntriesAdd(final SSCollUserEntriesAddPar par) throws Exception{

    try{

      final List<SSUri> addedEntries = new ArrayList<>();
        
      dbSQL.startTrans(par.shouldCommit);

      final SSCollUserEntryAddPar collEntryAddPar =
        new SSCollUserEntryAddPar(
          par.user,
          par.coll, //coll
          null, //entry
          null, //label
          false, //addNewColl
          par.withUserRestriction, //withUserRestriction
          false); //shouldCommit
        
      for(int counter = 0; counter < par.entries.size(); counter++){

        collEntryAddPar.entry = par.entries.get(counter);
        collEntryAddPar.label = par.labels.get(counter);

        SSUri.addDistinctWithoutNull(
          addedEntries,
          collEntryAdd(collEntryAddPar));
      }
      
      dbSQL.commit(par.shouldCommit);

      return addedEntries;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return collEntriesAdd(par);
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
  public SSUri collEntryDelete(final SSCollUserEntryDeletePar par) throws Exception{

    try{

      if(SSObjU.isNull(par.coll, par.entry)){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        final SSEntity coll =
          sqlFct.getEntityTest(
            par.user, 
            par.coll, 
            par.withUserRestriction);
        
        if(coll == null){
          return null;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);

      if(sqlFct.isColl(par.entry)){
        
        SSCollEntryDeleteFct.removeColl(
          sqlFct, 
          circleServ, 
          par);
        
      }else{
        SSCollEntryDeleteFct.removeCollEntry(sqlFct, par);
      }

      dbSQL.commit(par.shouldCommit);

      return par.entry;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return collEntryDelete(par);
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
  public void collEntriesDelete(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSCollUserEntriesDeletePar par = (SSCollUserEntriesDeletePar) parA.getFromJSON(SSCollUserEntriesDeletePar.class);
    
    sSCon.writeRetFullToClient(SSCollUserEntriesDeleteRet.get(collEntriesDelete(par)));
    
    SSCollActivityFct.removeCollEntries(par);
  }
  
  @Override
  public List<SSUri> collEntriesDelete(final SSCollUserEntriesDeletePar par) throws Exception{

    try{

      final List<SSUri> deletedEntries = new ArrayList<>();
      
      dbSQL.startTrans(par.shouldCommit);

      final SSCollUserEntryDeletePar collEntryDeletePar =
        new SSCollUserEntryDeletePar(
          par.user,
          par.coll,
          null,  //entry
          par.withUserRestriction,
          false);
        
      for(SSUri collEntryUri : par.entries){
        
        collEntryDeletePar.entry = collEntryUri;
          
        SSUri.addDistinctWithoutNull(
          deletedEntries,
          collEntryDelete(collEntryDeletePar));
      }

      dbSQL.commit(par.shouldCommit);

      return deletedEntries;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return collEntriesDelete(par);
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
  public void collCumulatedTagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSCollCumulatedTagsGetPar par = (SSCollCumulatedTagsGetPar) parA.getFromJSON(SSCollCumulatedTagsGetPar.class);
    
    sSCon.writeRetFullToClient(SSCollUserCumulatedTagsGetRet.get(collCumulatedTagsGet(par)));
  }
  
  @Override
  public List<SSTagFrequ> collCumulatedTagsGet(final SSCollCumulatedTagsGetPar par) throws Exception{

    try{

      if(par.coll == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        final SSEntity coll = 
          sqlFct.getEntityTest(
            par.user, 
            par.coll, 
            par.withUserRestriction);
        
        if(coll == null){
          return new ArrayList<>();
        }
      }
      
      final List<SSUri> entityURIs = 
        SSCollMiscFct.getCollSubCollAndEntryURIs(
          sqlFct, 
          sqlFct.getCollWithEntries(par.coll));
      
      final SSTagServerI tagServ = (SSTagServerI) SSServReg.getServ(SSTagServerI.class);
      
      return tagServ.tagFrequsGet(
        new SSTagFrequsGetPar(
          par.user, 
          null, //forUser, 
          entityURIs, 
          null, //labels, 
          null, //spaces, 
          null, //circles
          null, //startTime, 
          false, //useUsersEntities,
          par.withUserRestriction));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

//  @Override
//  public List<SSColl> collsUserCouldSubscribeGet(final SSServPar parA) throws Exception{
//
//    try{
//      final SSCollsUserCouldSubscribeGetPar par          = new SSCollsUserCouldSubscribeGetPar(parA);
//      final List<String>                    userCollUris = sqlFct.getCollURIsForUser(par.user);
//      final List<SSColl>                    publicColls  = new ArrayList<>();
//
//      for(SSColl publicColl : sqlFct.getCollsPublic()){
//
//        if(!userCollUris.contains(SSStrU.toStr(publicColl.id))){
//          publicColls.add(publicColl);
//        }
//      }
//
//      return publicColls;
//
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//@Override
//  public Boolean collUserEntryChangePos(SSServPar parA) throws Exception{
//
//    try{
//      
//      final SSCollUserEntryChangePosPar par         = new SSCollUserEntryChangePosPar(parA);
//      final List<SSUri>                 collEntries = new ArrayList<>();
//      final List<Integer>               order       = new ArrayList<>();
//      Integer                           counter     = 0;
//      
//      SSServCallerU.canUserEditEntity(par.user, par.coll);
//      
//      while(counter < par.order.size()){
//        collEntries.add(SSUri.get(par.order.get(counter++)));
//        order.add(Integer.valueOf(par.order.get(counter++)));
//      }
//
//      dbSQL.startTrans(par.shouldCommit);
//
//      sqlFct.updateCollEntriesPos(par.coll, collEntries, order);
//
//      dbSQL.commit(par.shouldCommit);
//
//      return true;
//    }catch(Exception error){
//      
//      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
//        
//        if(dbSQL.rollBack(parA.shouldCommit)){
//          
//          SSServErrReg.reset();
//          
//          return collUserEntryChangePos(parA);
//        }else{
//          SSServErrReg.regErrThrow(error);
//          return null;
//        }
//      }
//      
//      dbSQL.rollBack(parA.shouldCommit);
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//@Override
//  public List<SSTagFrequ> collUserCumulatedTagsGet(final SSServPar parA) throws Exception{
//
//    try{
//      
//      final SSCollUserCumulatedTagsGetPar   par            = new SSCollUserCumulatedTagsGetPar(parA);
//      final Map<String, Integer>            tagLabelFrequs = new HashMap<>();
//      final List<SSTagFrequ>                tagFrequs      = new ArrayList<>();
//      SSColl                                coll;
//      SSCollEntry                           collEntry;
//      String                                tagLabel;
//
//      SSServCallerU.canUserReadEntity(par.user, par.coll);
//      
//      coll = sqlFct.getCollWithEntries(par.coll, new ArrayList<>());
//
//      for(SSTagFrequ tagFrequ : 
//        ((SSTagServerI) SSTagServ.inst.serv()).tagFrequsGet(
//          new SSTagFrequsGetPar(
//            null, 
//            null, 
//            par.user, 
//            null, //forUser
//            SSUri.asListWithoutNullAndEmpty(par.coll),  //entities
//            null,  //labels
//            null, //space
//            null, //startTime
//            false, //useUsersEntities
//            false))){ //withUserRestriction
//        
//        tagLabel = tagFrequ.label.toString();
//
//        if(tagLabelFrequs.containsKey(tagLabel)){
//          tagLabelFrequs.put(tagLabel, tagLabelFrequs.get(tagLabel) + tagFrequ.frequ);
//        }else{
//          tagLabelFrequs.put(tagLabel, tagFrequ.frequ);
//        }
//      }
//
//      for(Object entry : coll.entries){
//
//        collEntry = (SSCollEntry) entry;
//          
//        if(SSStrU.equals(collEntry.type, SSEntityE.coll)){
//
//          for(SSTagFrequ tagFrequ : SSServCaller.collUserCumulatedTagsGet(par.user, collEntry.id)){
//
//            tagLabel = tagFrequ.label.toString();
//
//            if(tagLabelFrequs.containsKey(tagLabel)){
//              tagLabelFrequs.put(tagLabel, tagLabelFrequs.get(tagLabel) + tagFrequ.frequ);
//            }else{
//              tagLabelFrequs.put(tagLabel, tagFrequ.frequ);
//            }
//          }
//
//        }else{
//
//          for(SSTagFrequ tagFrequ : 
//            ((SSTagServerI) SSTagServ.inst.serv()).tagFrequsGet(
//              new SSTagFrequsGetPar(
//                null,
//                null,
//                par.user,
//                null, //forUser
//                SSUri.asListWithoutNullAndEmpty(collEntry.id), //entities
//                null, //labels
//                null, //space
//                null, //startTime
//                false, //useUsersEntities
//                false))){ //withUserRestriction
//
//            tagLabel = tagFrequ.label.toString();
//
//            if(tagLabelFrequs.containsKey(tagLabel)){
//              tagLabelFrequs.put(tagLabel, tagLabelFrequs.get(tagLabel) + tagFrequ.frequ);
//            }else{
//              tagLabelFrequs.put(tagLabel, tagFrequ.frequ);
//            }
//          }
//        }
//      }
//
//      for(Map.Entry<String, Integer> entry : tagLabelFrequs.entrySet()){
//        
//        tagFrequs.add(
//          SSTagFrequ.get(
//            SSTagLabel.get(entry.getKey()),
//            null,
//            entry.getValue()));
//      }
//
//      return tagFrequs;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }