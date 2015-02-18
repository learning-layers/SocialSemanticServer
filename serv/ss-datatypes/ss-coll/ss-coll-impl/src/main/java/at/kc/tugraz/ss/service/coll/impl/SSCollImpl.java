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

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserWithEntriesPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryChangePosPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserWithEntriesPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryDeletePar;
import at.kc.tugraz.socialserver.utils.*;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.service.coll.api.*;
import at.kc.tugraz.ss.service.coll.datatypes.*;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserParentGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryChangePosRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryDeleteRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserParentGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserRootGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserWithEntriesRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserWithEntriesRet;
import at.kc.tugraz.ss.service.coll.impl.fct.sql.SSCollSQLFct;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesDeletePar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesDeleteRet;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSUserRelationGathererI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.serv.caller.SSServCallerU;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserCumulatedTagsGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserHierarchyGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserCouldSubscribeGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserEntityIsInGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserCumulatedTagsGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserHierarchyGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserCouldSubscribeGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserEntityIsInGetRet;
import at.kc.tugraz.ss.service.coll.impl.fct.activity.SSCollActivityFct;
import at.kc.tugraz.ss.service.coll.impl.fct.misc.SSCollMiscFct;
import static at.kc.tugraz.ss.service.coll.impl.fct.misc.SSCollMiscFct.getCollSubCollAndEntryURIs;
import at.kc.tugraz.ss.service.coll.impl.fct.op.SSCollEntryAddFct;
import at.kc.tugraz.ss.service.coll.impl.fct.op.SSCollEntryDeleteFct;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagFrequ;
import java.util.*;
import sss.serv.err.datatypes.SSErrE;
import sss.serv.err.datatypes.SSWarnE;

public class SSCollImpl extends SSServImplWithDBA implements SSCollClientI, SSCollServerI, SSEntityHandlerImplI, SSUserRelationGathererI{

  private final SSCollSQLFct sqlFct;

  public SSCollImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{

    super(conf, dbGraph, dbSQL);

    this.sqlFct = new SSCollSQLFct(dbSQL);
  }

  @Override
  public void getUserRelations(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> userRelations) throws Exception{
    
    List<SSEntityCircle>           collUserCircles;
    List<SSEntityCircle>           collEntryUserCircles;
    List<SSColl>                   allColls;
    SSCollEntry                    collEntry;
    
    for(String user : allUsers){
      
      final SSUri userUri = SSUri.get(user);
      
      allColls = SSServCaller.collsUserWithEntries(userUri);
      
      for(SSColl coll : allColls){
        
        collUserCircles =
          SSServCaller.circlesGet(userUri, userUri, coll.id, true, false, false);
        
        for(SSEntityCircle circle : collUserCircles){
          
          if(userRelations.containsKey(user)){
            userRelations.get(user).addAll(SSUri.getFromEntitites(circle.users));
          }else{
            userRelations.put(user, SSUri.getFromEntitites(circle.users));
          }
        }
        
        for(Object entry : coll.entries){
          
          collEntry = (SSCollEntry) entry;
          
          collEntryUserCircles =
            SSServCaller.circlesGet(userUri, userUri, collEntry.id, true, false, false);
          
          for(SSEntityCircle circle : collEntryUserCircles){
            
            if(userRelations.containsKey(user)){
              userRelations.get(user).addAll(SSUri.getFromEntitites(circle.users));
            }else{
              userRelations.put(user, SSUri.getFromEntitites(circle.users));
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
    
    try{
      final List<String> userCollUris = sqlFct.getCollURIsForUser          (user);
      final List<String> collUris     = sqlFct.getCollURIsContainingEntity (entity);
      
      return SSUri.get(SSStrU.retainAll(collUris, userCollUris));
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
    
    if(!SSStrU.equals(type, SSEntityE.coll)){
      return null;
    }
    
    try{
      
      return SSCollMiscFct.getCollSubCollAndEntryURIs(
        sqlFct,
        sqlFct.getCollWithEntries(
          entity,
          new ArrayList<>()));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
      
  @Override
  public Boolean setUserEntityPublic(
    final SSUri          user,
    final SSUri          entity, 
    final SSEntityE      type,
    final SSUri          circle) throws Exception{
    
    switch(type){
      
      case coll:{
        
        if(sqlFct.isCollSpecial(entity)){
          throw new Exception("cannot set special collection public");
        }
        
        SSServCaller.circleEntitiesAdd(
          user,
          circle,
          SSCollMiscFct.getCollSubCollAndEntryURIs(sqlFct, sqlFct.getCollWithEntries(entity, new ArrayList<>())),
          false,
          false,
          false);
        
        return true;
      }
      
      default: return false;
    }
  }

  @Override
  public void shareUserEntity(
    final SSUri          user, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entity, 
    final SSUri          circle,
    final SSEntityE      type,
    final Boolean        saveActivity) throws Exception{
    
    try{
      
      switch(type){
        
        case coll:{
          
          SSUri rootColl;
          
          if(sqlFct.isCollSpecial(entity)){
            SSLogU.warn(SSWarnE.cannotShareSpecialCollection);
            return; 
          }
          
          for(SSUri userUriToShareWith : userUrisToShareWith){

            rootColl = SSServCaller.collUserRootGet (userUriToShareWith).id;
            
            if(
              sqlFct.containsCollEntry (rootColl, entity) ||
              sqlFct.ownsUserColl      (userUriToShareWith, entity)){
              SSLogU.warn(SSWarnE.collAlreadySharedWithUser);
              continue;
            }
            
            if(SSCollMiscFct.ownsUserASubColl(sqlFct, userUriToShareWith, entity)){
              SSLogU.warn(SSWarnE.subCollAlreadySharedWithUser);
              continue;
            }
            
            sqlFct.addCollToColl(
              userUriToShareWith,
              rootColl,
              entity,
              false,
              true);
            
            SSServCaller.circleEntitiesAdd(
              user,
              circle,
              SSCollMiscFct.getCollSubCollAndEntryURIs(sqlFct, sqlFct.getCollWithEntries(entity, new ArrayList<>())),
              false,
              false,
              false);
            
            SSServCaller.circleUsersAdd(
              user,
              circle,
              sqlFct.getCollUserURIs(entity),
              false,
              false);
          }
          
          break;
        }
        
        case file:
        case entity:{
          
          SSUri sharedWithMeFilesCollUri;
          
          for(SSUri userUriToShareWith : userUrisToShareWith){
            
            sharedWithMeFilesCollUri = sqlFct.getCollSpecialURI (userUriToShareWith);
            
            if(sqlFct.containsCollEntry (sharedWithMeFilesCollUri, entity)){
              SSLogU.warn(SSWarnE.entityAlreadySharedWithUser);
              continue;
            }
            
            SSCollMiscFct.shareEntityWithUser(
              sqlFct,
              sharedWithMeFilesCollUri,
              entity);
          }
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void shareUserEntityWithCircle(
    final SSUri        user,
    final SSUri        circle,
    final SSUri        entity,
    final SSEntityE    type) throws Exception{

    switch(type){
      
      case coll:{
        
        try{
          
          SSServCaller.circleEntitiesAdd(
            user,
            circle,
            getCollSubCollAndEntryURIs(sqlFct, sqlFct.getCollWithEntries(entity, new ArrayList<>())),
            false, 
            false, 
            false);
          
        }catch(Exception error){
          SSServErrReg.regErrThrow(error);
        }
      }
    }
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

    if(!removeFromUserColls){
      return;
    }

    try{

      for(SSColl coll : SSServCaller.collsUserEntityIsInGet(userUri, entityUri)){
        
        SSServCaller.collUserEntryDelete(
          userUri, 
          entityUri,
          coll.id, 
          false);
      }

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public void collParentGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    SSColl collParent = collUserParentGet(parA);

    sSCon.writeRetFullToClient(SSCollUserParentGetRet.get(collParent, parA.op));
  }
  
  @Override
  public SSColl collUserParentGet(SSServPar parA) throws Exception{

    final SSCollUserParentGetPar par = new SSCollUserParentGetPar(parA);

    try{

      SSServCallerU.canUserReadEntity(par.user, par.coll);
      
      return SSCollMiscFct.getCollWithEntriesWithCircleTypes(
        sqlFct,
        par.user,
        SSCollMiscFct.getDirectParentCollURIForUser(sqlFct, par.user, par.coll));

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void collRootGet(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollUserRootGetRet.get(collUserRootGet(par), par.op));
  }
  
  @Override
  public SSColl collUserRootGet(final SSServPar parA) throws Exception{

    final SSCollUserRootGetPar par = new SSCollUserRootGetPar(parA);

    try{

      return SSCollMiscFct.getCollWithEntriesWithCircleTypes(
        sqlFct,
        par.user,
        sqlFct.getRootCollURIForUser(par.user));

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void collEntryDelete(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSCollUserEntryDeleteRet.get(collUserEntryDelete(parA), parA.op));
    
    SSCollActivityFct.removeCollEntry(new SSCollUserEntryDeletePar(parA));
  }
  
  @Override
  public Boolean collUserEntryDelete(final SSServPar parA) throws Exception{

    final SSCollUserEntryDeletePar par = new SSCollUserEntryDeletePar(parA);

    try{

      SSServCallerU.canUserEditEntity(par.user, par.coll);
      SSServCallerU.canUserReadEntity(par.user, par.entry);
      
      dbSQL.startTrans(par.shouldCommit);

      if(sqlFct.isColl(par.entry)){
        
        SSCollEntryDeleteFct.removeColl(sqlFct, par);
      }else{
        SSCollEntryDeleteFct.removeCollEntry(sqlFct, par);
      }

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return collUserEntryDelete(parA);
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
  public void collsCouldSubscribeGet(final SSSocketCon sSCon, final SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollsUserCouldSubscribeGetRet.get(collsUserCouldSubscribeGet(par), par.op));
  }
  
   @Override
  public List<SSColl> collsUserCouldSubscribeGet(final SSServPar parA) throws Exception{

    try{
      final SSCollsUserCouldSubscribeGetPar par          = new SSCollsUserCouldSubscribeGetPar(parA);
      final List<String>                    userCollUris = sqlFct.getCollURIsForUser(par.user);
      final List<SSColl>                    publicColls  = new ArrayList<>();

      for(SSColl publicColl : sqlFct.getCollsPublic()){

        if(!userCollUris.contains(SSStrU.toStr(publicColl.id))){
          publicColls.add(publicColl);
        }
      }

      return publicColls;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void collEntriesDelete(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSCollUserEntriesDeleteRet.get(collUserEntriesDelete(parA), parA.op));
    
    SSCollActivityFct.removeCollEntries(new SSCollUserEntriesDeletePar(parA));
  }
  
  @Override
  public Boolean collUserEntriesDelete(final SSServPar parA) throws Exception{

    final SSCollUserEntriesDeletePar par = new SSCollUserEntriesDeletePar(parA);

    try{

      dbSQL.startTrans(par.shouldCommit);

      for(SSUri collEntryUri : par.entries){
        SSServCaller.collUserEntryDelete(par.user, collEntryUri, par.coll, false);
      }

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return collUserEntriesDelete(parA);
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
  public void collEntryAdd(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSCollUserEntryAddRet.get(collUserEntryAdd(parA), parA.op));
    
    SSCollActivityFct.addCollEntry(new SSCollUserEntryAddPar(parA));
  }
  
  @Override
  public SSUri collUserEntryAdd(final SSServPar parA) throws Exception{

    final SSCollUserEntryAddPar par = new SSCollUserEntryAddPar(parA);

    try{

      SSServCallerU.canUserEditEntity(par.user, par.coll);
      
      if(par.addNewColl){

        dbSQL.startTrans(par.shouldCommit);

        SSCollEntryAddFct.addNewColl(sqlFct, par);

        dbSQL.commit(par.shouldCommit);

        return par.entry;
      }

      final Boolean existsEntry = SSServCaller.entityExists(par.entry);
      
      if(existsEntry){
        
        switch(SSServCaller.entityGet(par.entry).type){
          case entity: break;
          default: SSServCallerU.canUserReadEntity(par.user, par.entry);
        }
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return collUserEntryAdd(parA);
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
  public void collEntriesAdd(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSCollUserEntriesAddRet.get(collUserEntriesAdd(parA), parA.op));
    
    SSCollActivityFct.addCollEntries(new SSCollUserEntriesAddPar(parA));
  }
  
  @Override
  public Boolean collUserEntriesAdd(final SSServPar parA) throws Exception{

    try{
      
      final SSCollUserEntriesAddPar par = new SSCollUserEntriesAddPar(parA);

      dbSQL.startTrans(par.shouldCommit);

      for(int counter = 0; counter < par.entries.size(); counter++){

        SSServCaller.collUserEntryAdd(
          par.user,
          par.coll,
          par.entries.get(counter),
          par.labels.get(counter),
          false,
          false);
      }

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return collUserEntriesAdd(parA);
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
  public void collEntryChangePos(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSCollUserEntryChangePosRet.get(collUserEntryChangePos(parA), parA.op));
    
//    SSCollActivityFct.changeCollEntryPos(new SSCollUserEntryChangePosPar(parA));
  }
  
  @Override
  public Boolean collUserEntryChangePos(SSServPar parA) throws Exception{

    try{
      
      final SSCollUserEntryChangePosPar par         = new SSCollUserEntryChangePosPar(parA);
      final List<SSUri>                 collEntries = new ArrayList<>();
      final List<Integer>               order       = new ArrayList<>();
      Integer                           counter     = 0;
      
      SSServCallerU.canUserEditEntity(par.user, par.coll);
      
      while(counter < par.order.size()){
        collEntries.add(SSUri.get(par.order.get(counter++)));
        order.add(Integer.valueOf(par.order.get(counter++)));
      }

      dbSQL.startTrans(par.shouldCommit);

      sqlFct.updateCollEntriesPos(par.coll, collEntries, order);

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return collUserEntryChangePos(parA);
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
  public void collWithEntries(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollUserWithEntriesRet.get(collUserWithEntries(par), par.op));
  }
  
  @Override
  public SSColl collUserWithEntries(final SSServPar parA) throws Exception{

    try{
      final SSCollUserWithEntriesPar par = new SSCollUserWithEntriesPar(parA);

      SSServCallerU.canUserReadEntity(par.user, par.coll);
      
      return SSCollMiscFct.getCollWithEntriesWithCircleTypes(sqlFct, par.user, par.coll);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void collsWithEntries(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollsUserWithEntriesRet.get(collsUserWithEntries(par), par.op));
  }
  
  @Override
  public List<SSColl> collsUserWithEntries(SSServPar parA) throws Exception{

    try{
      
      final SSCollsUserWithEntriesPar par                  = new SSCollsUserWithEntriesPar(parA);
      final List<SSColl>              userCollsWithEntries = new ArrayList<>();
      
      for(String collUri : sqlFct.getCollURIsForUser(par.user)){

        userCollsWithEntries.add(
          SSCollMiscFct.getCollWithEntriesWithCircleTypes(
            sqlFct,
            par.user,
            SSUri.get(collUri)));
      }

      return userCollsWithEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void collHierarchyGet(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollUserHierarchyGetRet.get(collUserHierarchyGet(par), par.op));
  }
  
  @Override
  public List<SSColl> collUserHierarchyGet(final SSServPar parA) throws Exception{

    try{
      
      final SSCollUserHierarchyGetPar par            = new SSCollUserHierarchyGetPar(parA);
      final List<SSUri>               hierarchy      = new ArrayList<>();
      final List<SSColl>              colls          = new ArrayList<>();
      final SSUri                     rootCollUri;
      SSUri                           directPartentCollUri;

      SSServCallerU.canUserReadEntity(par.user, par.coll);

      rootCollUri          = sqlFct.getRootCollURIForUser               (par.user);
      directPartentCollUri = SSCollMiscFct.getDirectParentCollURIForUser(sqlFct, par.user, par.coll);

      while(!SSStrU.equals(rootCollUri, directPartentCollUri)){

        hierarchy.add(directPartentCollUri);

        directPartentCollUri = SSCollMiscFct.getDirectParentCollURIForUser(sqlFct, par.user, directPartentCollUri);
      }

      hierarchy.add(rootCollUri);

      for(SSUri collUri : hierarchy){
        colls.add(SSColl.get(SSServCaller.entityGet(collUri)));
      }

      return colls;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void collCumulatedTagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSCollUserCumulatedTagsGetRet.get(collUserCumulatedTagsGet(parA), parA.op));
  }
  
   @Override
  public List<SSTagFrequ> collUserCumulatedTagsGet(final SSServPar parA) throws Exception{

    try{
      
      final SSCollUserCumulatedTagsGetPar   par            = new SSCollUserCumulatedTagsGetPar(parA);
      final Map<String, Integer>            tagLabelFrequs = new HashMap<>();
      final List<SSTagFrequ>                tagFrequs      = new ArrayList<>();
      SSColl                                coll;
      SSCollEntry                           collEntry;
      String                                tagLabel;

      SSServCallerU.canUserReadEntity(par.user, par.coll);
      
      coll = sqlFct.getCollWithEntries(par.coll, new ArrayList<>());

      for(SSTagFrequ tagFrequ : SSServCaller.tagUserFrequsGet(par.user, null, SSUri.asListWithoutNullAndEmpty(par.coll), new ArrayList<>(), null, null, false)){

        tagLabel = tagFrequ.label.toString();

        if(tagLabelFrequs.containsKey(tagLabel)){
          tagLabelFrequs.put(tagLabel, tagLabelFrequs.get(tagLabel) + tagFrequ.frequ);
        }else{
          tagLabelFrequs.put(tagLabel, tagFrequ.frequ);
        }
      }

      for(Object entry : coll.entries){

        collEntry = (SSCollEntry) entry;
          
        if(SSStrU.equals(collEntry.type, SSEntityE.coll)){

          for(SSTagFrequ tagFrequ : SSServCaller.collUserCumulatedTagsGet(par.user, collEntry.id)){

            tagLabel = tagFrequ.label.toString();

            if(tagLabelFrequs.containsKey(tagLabel)){
              tagLabelFrequs.put(tagLabel, tagLabelFrequs.get(tagLabel) + tagFrequ.frequ);
            }else{
              tagLabelFrequs.put(tagLabel, tagFrequ.frequ);
            }
          }

        }else{

          for(SSTagFrequ tagFrequ : SSServCaller.tagUserFrequsGet(par.user, null, SSUri.asListWithoutNullAndEmpty(collEntry.id), new ArrayList<>(), null, null, false)){

            tagLabel = tagFrequ.label.toString();

            if(tagLabelFrequs.containsKey(tagLabel)){
              tagLabelFrequs.put(tagLabel, tagLabelFrequs.get(tagLabel) + tagFrequ.frequ);
            }else{
              tagLabelFrequs.put(tagLabel, tagFrequ.frequ);
            }
          }
        }
      }

      for(Map.Entry<String, Integer> entry : tagLabelFrequs.entrySet()){
        
        tagFrequs.add(
          SSTagFrequ.get(
            SSTagLabel.get(entry.getKey()),
            null,
            entry.getValue()));
      }

      return tagFrequs;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void collsEntityIsInGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSCollsUserEntityIsInGetRet.get(collsUserEntityIsInGet(parA), parA.op));
  }
  
  @Override
  public List<SSColl> collsUserEntityIsInGet(final SSServPar parA) throws Exception{

    final SSCollsUserEntityIsInGetPar par           = new SSCollsUserEntityIsInGetPar(parA);
    final List<SSColl>                colls         = new ArrayList<>();
    final List<String>                collUris;
    final List<String>                userCollUris;

    try{

      SSServCallerU.canUserReadEntity(par.user, par.entity);
      
      userCollUris = sqlFct.getCollURIsForUser(par.user);
      collUris     = sqlFct.getCollURIsContainingEntity(par.entity);

      for(String coll : SSStrU.retainAll(collUris, userCollUris)){
        colls.add(SSCollMiscFct.getCollWithEntriesWithCircleTypes(sqlFct, par.user, SSUri.get(coll)));
      }

      return colls;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public Boolean collUserRootAdd(final SSServPar parA) throws Exception{

    try{
      
      final SSCollUserRootAddPar par = new SSCollUserRootAddPar(parA);

      if(sqlFct.existsCollRootForUser(par.user)){
        return true;
      }

      dbSQL.startTrans(par.shouldCommit);

      final SSUri rootCollUri = SSServCaller.vocURICreate();

      SSServCaller.entityEntityToPrivCircleAdd(
        par.user,
        rootCollUri, 
        SSEntityE.coll,
        SSLabel.get(SSStrU.valueRoot), 
        null,
        null,
        false);
      
      sqlFct.addColl (rootCollUri);

      SSServCaller.circleUsersAdd(
        par.user, 
        SSServCaller.circlePubURIGet(false), 
        par.user, 
        false, 
        false);
      
      sqlFct.addCollRoot(
        rootCollUri, 
        par.user);
      
      final SSUri sharedWithMeFilesCollUri =
        SSServCaller.collUserEntryAdd(
          par.user,
          rootCollUri,
          null,
          SSLabel.get(SSStrU.valueSharedWithMeFiles),
          true,
          false);
      
      sqlFct.addCollSpecial(
        sharedWithMeFilesCollUri,
        par.user);

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return collUserRootAdd(parA);
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
}
