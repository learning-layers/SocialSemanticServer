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
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
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
  SSCircleContentAddedI,
  SSUserRelationGathererI{

  private final SSCollSQLFct sqlFct;

  public SSCollImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    this.sqlFct = new SSCollSQLFct(dbSQL);
  }

  @Override
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    switch(entity.type){
      
      case coll:{
        
        return SSColl.get(
          collGet(
            new SSCollGetPar(
              null, 
              null, 
              par.user, 
              entity.id, 
              par.withUserRestriction, 
              false)), //invokeEntityHandlers
          entity);
      }
      
      default: return entity;
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
            null,
            null,
            userUri,
            false, //withUserRestriction
            false)); //invokeEntityHandlers
      
      for(SSEntity coll : allColls){
        
        collUserCircles =
          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlesGet(
            new SSCirclesGetPar(
              null,
              null,
              userUri,
              coll.id,
              null, //entityTypesToIncludeOnly
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
            ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlesGet(
              new SSCirclesGetPar(
                null,
                null,
                userUri,
                collEntry.id,
                null, //entityTypesToIncludeOnly
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
  public List<SSUri> getParentEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    try{
      return SSUri.getDistinctNotNullFromEntities(
        collsEntityIsInGet(
          new SSCollsUserEntityIsInGetPar(
            null,
            null,
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
  public void circleContentAdded(final SSCircleContentChangedPar par) throws Exception{
    
    SSColl rootColl;
    
    try{
    
      for(SSEntity entityToAdd : par.entitiesToAdd){

        switch(entityToAdd.type){

          case coll:{

            if(sqlFct.isCollSpecial(entityToAdd.id)){

              if(par.isCirclePublic){
                throw new SSErr(SSErrE.cannotSetSpecialCollectionPublic);
              }

              throw new SSErr(SSErrE.cannotShareSpecialCollection);
            }

            try{

              ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntitiesAdd(
                new SSCircleEntitiesAddPar(
                  null,
                  null,
                  par.user,
                  par.circle,
                  SSCollMiscFct.getCollSubCollAndEntryURIs(sqlFct, sqlFct.getCollWithEntries(entityToAdd.id)),
                  false,
                  false));

            }catch(Exception error){
              SSServErrReg.regErrThrow(error);
            }

            for(SSUri userToPushEntityTo : par.usersToPushEntitiesTo){

              rootColl = 
                collRootGet(
                  new SSCollUserRootGetPar(
                    null, 
                    null, 
                    userToPushEntityTo, 
                    false, //withUserRestriction, 
                    false)); //invokeEntityHandlers));

              if(
                sqlFct.containsCollEntry (rootColl.id,        entityToAdd.id) ||
                sqlFct.ownsUserColl      (userToPushEntityTo, entityToAdd.id)){
                SSLogU.warn(SSWarnE.collAlreadySharedWithUser);
                continue;
              }

              if(SSCollMiscFct.ownsUserASubColl(sqlFct, userToPushEntityTo, entityToAdd.id)){
                SSLogU.warn(SSWarnE.subCollAlreadySharedWithUser);
                continue;
              }

              sqlFct.addCollToColl(
                userToPushEntityTo,
                rootColl.id,
                entityToAdd.id,
                false,
                true);
            }
            
            if(!par.usersToPushEntitiesTo.isEmpty()){
              
              ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleUsersAdd(
                new SSCircleUsersAddPar(
                  null,
                  null,
                  par.user,
                  par.circle,
                  sqlFct.getCollUserURIs(entityToAdd.id),
                  false,
                  false));
            }
            
            break;
          }
          
          default:{
//          case file:
//          case entity:{

            SSUri sharedWithMeFilesCollUri;

            for(SSUri userToPushEntityTo : par.usersToPushEntitiesTo){

              sharedWithMeFilesCollUri = sqlFct.getSpecialCollURI(userToPushEntityTo);

              if(sqlFct.containsCollEntry (sharedWithMeFilesCollUri, entityToAdd.id)){
                continue;
              }

              sqlFct.addCollEntry(sharedWithMeFilesCollUri, entityToAdd.id);

              break;
            }
          }
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }    

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
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.coll)){
          return null;
        }
      }
      
      final SSEntityDescriberPar descPar;
      final List<SSEntity>       collEntries;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(par.coll);
        
        descPar.setCircleTypes = true;
      }else{
        descPar = null;
      }
      
      final SSColl coll = 
        SSColl.get(
          sqlFct.getCollWithEntries(par.coll), 
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null, 
              null, 
              par.user, 
              par.coll, //entity, 
              par.withUserRestriction, 
              descPar))); //descPar
      
      if(descPar != null){
        descPar.recursiveEntity = null;
      }
      
      collEntries =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            null,
            null,
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
          null,
          null,
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
          null,
          null,
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
      
      final List<SSEntity> colls = new ArrayList<>();
      
      for(SSUri collURI : sqlFct.getUserCollURIs(par.user)){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          colls,
          collGet(
            new SSCollGetPar(
              null,
              null,
              par.user,
              collURI,
              par.withUserRestriction,
              par.invokeEntityHandlers)));
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
            
      final List<SSEntity> colls          = new ArrayList<>();      

      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.coll)){
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
      
      for(SSUri collURI : hierarchy){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          colls,
          collGet(
            new SSCollGetPar(
              null,
              null,
              par.user,
              collURI,
              par.withUserRestriction,
              par.invokeEntityHandlers)));
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
      
      final List<SSEntity> colls         = new ArrayList<>();
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.entity)){
          return colls;
        }
      }

      final List<SSUri>                userCollUris   = sqlFct.getUserCollURIs(par.user);
      final List<SSUri>                entityCollUris = sqlFct.getCollURIsContainingEntity(par.entity);
      final List<String>               commonCollUris = SSStrU.retainAll(SSStrU.toStr(entityCollUris), SSStrU.toStr(userCollUris));

      for(SSUri collURI : SSUri.get(commonCollUris)){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          colls,
          collGet(
            new SSCollGetPar(
              null,
              null,
              par.user,
              collURI,
              par.withUserRestriction,
              par.invokeEntityHandlers)));
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

      final SSUri rootCollUri = SSServCaller.vocURICreate();

      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.forUser,
          rootCollUri,
          SSEntityE.coll, //type,
          SSLabel.get(SSStrU.valueRoot), //label
          null, //description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      sqlFct.addColl (rootCollUri);

      sqlFct.addCollRoot(
        rootCollUri, 
        par.forUser);
      
      final SSUri sharedWithMeFilesCollUri =
        collEntryAdd(
          new SSCollUserEntryAddPar(
            null,
            null,
            par.forUser,
            rootCollUri, //coll
            null, //entry
            SSLabel.get(SSStrU.valueSharedWithMeFiles), //label
            true, //addNewColl
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit
      
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
        
        if(!SSServCallerU.canUserRead(par.user, par.coll)){
          return null;
        }
        
        if(par.entry != null){
          
          if(!SSServCallerU.canUserRead(par.user, par.entry)){
            return null;
          }
        }
      }
      
      if(par.addNewColl){

        dbSQL.startTrans(par.shouldCommit);

        SSCollEntryAddFct.addNewColl(sqlFct, par);

        dbSQL.commit(par.shouldCommit);

        return par.entry;
      }
      
      if(sqlFct.containsCollEntry(par.coll, par.entry)){
        return par.entry;
      }

      if(sqlFct.isColl(par.entry)){

        dbSQL.startTrans(par.shouldCommit);

        SSCollEntryAddFct.addPublicColl(sqlFct, par);

        dbSQL.commit(par.shouldCommit);

        return par.entry;
      }

      dbSQL.startTrans(par.shouldCommit);

      SSCollEntryAddFct.addCollEntry(sqlFct, par);

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

      for(int counter = 0; counter < par.entries.size(); counter++){

        SSUri.addDistinctWithoutNull(
          addedEntries,
          collEntryAdd(
            new SSCollUserEntryAddPar(
              null,
              null,
              par.user,
              par.coll, //coll
              par.entries.get(counter), //entry
              par.labels.get(counter), //label
              false, //addNewColl
              par.withUserRestriction, //withUserRestriction
              false))); //shouldCommit
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
        
        if(
          !SSServCallerU.canUserEdit(par.user, par.coll) ||
          !SSServCallerU.canUserRead(par.user, par.entry)){
          return null;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);

      if(sqlFct.isColl(par.entry)){
        
        SSCollEntryDeleteFct.removeColl(sqlFct, par);
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

      for(SSUri collEntryUri : par.entries){
        
        SSUri.addDistinctWithoutNull(
          deletedEntries,
          collEntryDelete(
            new SSCollUserEntryDeletePar(
              null,
              null,
              par.user, 
              par.coll, 
              collEntryUri, 
              par.withUserRestriction, 
              false)));
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
        
        if(!SSServCallerU.canUserRead(par.user, par.coll)){
          return new ArrayList<>();
        }
      }
      
      final List<SSUri> entityURIs = 
        SSCollMiscFct.getCollSubCollAndEntryURIs(
          sqlFct, 
          sqlFct.getCollWithEntries(par.coll));
      
      return ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagFrequsGet(
        new SSTagFrequsGetPar(
          null, 
          null, 
          par.user, 
          null, //forUser, 
          entityURIs, 
          null, //labels, 
          null, //space, 
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