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
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserWithEntriesPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryChangePosPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserWithEntriesPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryDeletePar;
import at.tugraz.sss.serv.*;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.kc.tugraz.ss.service.coll.api.*;
import at.kc.tugraz.ss.service.coll.datatypes.*;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSConfA;
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
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
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
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagFrequ;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagFrequsGetPar;
import at.kc.tugraz.ss.service.tag.service.SSTagServ;
import java.util.*;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSWarnE;

public class SSCollImpl 
extends SSServImplWithDBA 
implements 
  SSCollClientI, 
  SSCollServerI, 
  SSEntityHandlerImplI, 
  SSUserRelationGathererI{

  private final SSCollSQLFct sqlFct;

  public SSCollImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    this.sqlFct = new SSCollSQLFct(dbSQL);
  }

  @Override
  public SSEntity getUserEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    return entity;
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
          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlesGet(
            new SSCirclesGetPar(
              null,
              null,
              userUri,
              userUri,
              coll.id,
              SSEntityE.asListWithoutNullAndEmpty(),
              false,
              true,
              false));

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
            ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlesGet(
              new SSCirclesGetPar(
                null,
                null,
                userUri,
                userUri,
                collEntry.id,
                SSEntityE.asListWithoutNullAndEmpty(),
                false,
                true,
                false));
          
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
  public void copyEntity(
    final SSUri        user,
    final List<SSUri>  users,
    final SSUri        entity,
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception{
    
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
  public void circleContentChanged(final SSCircleContentChangedPar par) throws Exception{
    
    SSUri rootColl;
    
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
                  getCollSubCollAndEntryURIs(sqlFct, sqlFct.getCollWithEntries(entityToAdd.id, new ArrayList<>())),
                  false,
                  false));

            }catch(Exception error){
              SSServErrReg.regErrThrow(error);
            }

            for(SSUri userToPushEntityTo : par.usersToPushEntitiesTo){

              rootColl = SSServCaller.collUserRootGet (userToPushEntityTo).id;

              if(
                sqlFct.containsCollEntry (rootColl,           entityToAdd.id) ||
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
                rootColl,
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
          
          case file:
          case entity:{

            SSUri sharedWithMeFilesCollUri;

            for(SSUri userToPushEntityTo : par.usersToPushEntitiesTo){

              sharedWithMeFilesCollUri = sqlFct.getCollSpecialURI (userToPushEntityTo);

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
  public void collParentGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

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
  public void collRootGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSCollUserRootGetRet.get(collUserRootGet(parA), parA.op));
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

    SSServCallerU.checkKey(parA);

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
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return collUserEntryDelete(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void collsCouldSubscribeGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSCollsUserCouldSubscribeGetRet.get(collsUserCouldSubscribeGet(parA), parA.op));
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

    SSServCallerU.checkKey(parA);

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
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return collUserEntriesDelete(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void collEntryAdd(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

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
      
      SSServCallerU.canUserReadEntity(par.user, par.entry);

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
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return collUserEntryAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void collEntriesAdd(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

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
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return collUserEntriesAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void collEntryChangePos(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

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
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return collUserEntryChangePos(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void collWithEntries(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSCollUserWithEntriesRet.get(collUserWithEntries(parA), parA.op));
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
  public void collsWithEntries(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSCollsUserWithEntriesRet.get(collsUserWithEntries(parA), parA.op));
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
  public void collHierarchyGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSCollUserHierarchyGetRet.get(collUserHierarchyGet(parA), parA.op));
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
        
        colls.add(
          SSColl.get(
            ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
              new SSEntityGetPar(
                null,
                null,
                null,
                collUri,  //entity
                null, //forUser
                null, //label
                null, //type
                false, //withUserRestriction
                false, //invokeEntityHandlers
                null, //descPar
                true)))); //logErr
      }

      return colls;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void collCumulatedTagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

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

      for(SSTagFrequ tagFrequ : 
        ((SSTagServerI) SSTagServ.inst.serv()).tagFrequsGet(
          new SSTagFrequsGetPar(
            null, 
            null, 
            par.user, 
            null, //forUser
            SSUri.asListWithoutNullAndEmpty(par.coll),  //entities
            null,  //labels
            null, //space
            null, //startTime
            false, //useUsersEntities
            false))){ //withUserRestriction
        
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

          for(SSTagFrequ tagFrequ : 
            ((SSTagServerI) SSTagServ.inst.serv()).tagFrequsGet(
              new SSTagFrequsGetPar(
                null,
                null,
                par.user,
                null, //forUser
                SSUri.asListWithoutNullAndEmpty(collEntry.id), //entities
                null, //labels
                null, //space
                null, //startTime
                false, //useUsersEntities
                false))){ //withUserRestriction

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

    SSServCallerU.checkKey(parA);

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

      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          rootCollUri,
          null, //uriAlternative,
          SSEntityE.coll, //type,
          SSLabel.get(SSStrU.valueRoot), //label
          null, //description,
          null, //comments,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      sqlFct.addColl (rootCollUri);

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
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return collUserRootAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
