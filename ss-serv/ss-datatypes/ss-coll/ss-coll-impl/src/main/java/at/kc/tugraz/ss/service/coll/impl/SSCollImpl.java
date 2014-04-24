/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package at.kc.tugraz.ss.service.coll.impl;

import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserWithEntriesPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryChangePosPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserWithEntriesPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryDeletePar;
import at.kc.tugraz.socialserver.utils.*;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.service.coll.api.*;
import at.kc.tugraz.ss.service.coll.datatypes.*;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityDescA;
import at.kc.tugraz.ss.datatypes.datatypes.SSTagLabel;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircleTypeE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
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
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollEntityInCircleTypeForUserIsPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollToCircleAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserCummulatedTagsGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserHierarchyGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserSetPublicPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserShareWithUserPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserCouldSubscribeGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserEntityIsInGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserCummulatedTagsGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserHierarchyGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserCouldSubscribeGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserEntityIsInGetRet;
import at.kc.tugraz.ss.service.coll.impl.fct.misc.SSCollMiscFct;
import at.kc.tugraz.ss.service.coll.impl.fct.op.SSCollEntryAddFct;
import at.kc.tugraz.ss.service.coll.impl.fct.op.SSCollEntryDeleteFct;
import at.kc.tugraz.ss.service.coll.impl.fct.op.SSCollUserShareWithUserFct;
import at.kc.tugraz.ss.service.coll.impl.fct.ue.SSCollUEFct;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagFrequ;
import at.kc.tugraz.ss.service.user.api.SSUserGlobals;
import java.util.*;

public class SSCollImpl extends SSServImplWithDBA implements SSCollClientI, SSCollServerI, SSEntityHandlerImplI{

  private final SSCollSQLFct sqlFct;

  public SSCollImpl(final SSServConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{

    super(conf, dbGraph, dbSQL);

    this.sqlFct = new SSCollSQLFct(dbSQL);
  }

  @Override
  public Boolean setUserEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri, 
    final SSEntityEnum   entityType,
    final SSUri          publicCircleUri) throws Exception{
    
    if(!SSEntityEnum.equals(entityType, SSEntityEnum.coll)){
      return false;
    }
    
    try{
      
      SSServCaller.collUserSetPublic(
        userUri,
        entityUri,
        publicCircleUri,
        false);
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public Boolean shareUserEntity(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          circleUri,
    final SSEntityEnum   entityType) throws Exception{

    if(!SSEntityEnum.equals(entityType, SSEntityEnum.coll)){
      return false;
    }
    
    try{
      
      for(SSUri userUriToShareWith : userUrisToShareWith){
        
        SSServCaller.collUserShareWithUser(
          userUri,
          userUriToShareWith,
          entityUri,
          circleUri,
          false);
      }
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean addEntityToCircle(
    final SSUri        userUri,
    final SSUri        circleUri,
    final SSUri        entityUri,
    final SSEntityEnum entityType) throws Exception{

    if(!SSEntityEnum.equals(entityType, SSEntityEnum.coll)){
      return false;
    }

    try{
      
      SSServCaller.collToCircleAdd(
        userUri,
        circleUri,
        entityUri,
        false);

      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSEntityEnum                                  entityType,
    final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par) throws Exception{

    if(!par.removeFromUserColls){
      return;
    }

    try{

      for(SSColl coll : SSServCaller.collsUserEntityIsInGet(par.user, par.entityUri)){
        
        SSServCaller.collUserEntryDelete(
          par.user, 
          par.entityUri, 
          coll.uri, 
          true, 
          false);
      }

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public SSEntityDescA getDescForEntity(
    final SSEntityEnum entityType,
    final SSUri userUri,
    final SSUri entityUri,
    final SSLabelStr label,
    final Long creationTime,
    final List<SSTag> tags,
    final SSRatingOverall overallRating,
    final List<SSUri> discUris,
    final SSUri author) throws Exception{

    if(!SSEntityEnum.equals(entityType, SSEntityEnum.coll)){
      return SSEntityDesc.get(entityUri, label, creationTime, tags, overallRating, discUris, author);
    }

    return SSCollDesc.get(
      entityUri,
      label,
      creationTime,
      tags,
      overallRating,
      discUris,
      author);
  }

  /* SSCollClientI */
  @Override
  public void collUserParentGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    SSColl collParent = collUserParentGet(parA);

    sSCon.writeRetFullToClient(SSCollUserParentGetRet.get(collParent, parA.op));
  }

  @Override
  public void collUserRootGet(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollUserRootGetRet.get(collUserRootGet(par), par.op));
  }

  @Override
  public void collUserEntryDelete(final SSSocketCon sSCon, final SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollUserEntryDeleteRet.get(collUserEntryDelete(par), par.op));
  }

  @Override
  public void collsUserCouldSubscribeGet(final SSSocketCon sSCon, final SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollsUserCouldSubscribeGetRet.get(collsUserCouldSubscribeGet(par), par.op));
  }

  @Override
  public void collUserEntriesDelete(final SSSocketCon sSCon, final SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollUserEntriesDeleteRet.get(collUserEntriesDelete(par), par.op));
  }

  @Override
  public void collUserEntryAdd(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollUserEntryAddRet.get(collUserEntryAdd(par), par.op));
  }

  @Override
  public void collUserEntriesAdd(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollUserEntriesAddRet.get(collUserEntriesAdd(par), par.op));
  }

  @Override
  public void collUserEntryChangePos(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollUserEntryChangePosRet.get(collUserEntryChangePos(par), par.op));
  }

  @Override
  public void collUserWithEntries(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollUserWithEntriesRet.get(collUserWithEntries(par), par.op));
  }

  @Override
  public void collsUserWithEntries(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollsUserWithEntriesRet.get(collsUserWithEntries(par), par.op));
  }

  @Override
  public void collUserHierarchyGet(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollUserHierarchyGetRet.get(collUserHierarchyGet(par), par.op));
  }

  @Override
  public void collUserCumulatedTagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSCollUserCummulatedTagsGetRet.get(collUserCumulatedTagsGet(parA), parA.op));
  }

  @Override
  public void collsUserEntityIsInGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSCollsUserEntityIsInGetRet.get(collsUserEntityIsInGet(parA), parA.op));
  }

  /*  SSCollServerI */
  @Override
  public Boolean collUserRootAdd(final SSServPar parA) throws Exception{

    final SSCollUserRootAddPar par = new SSCollUserRootAddPar(parA);
    final SSUri                rootCollUri;

    try{

      if(sqlFct.existsUserRootColl(par.user)){
        return true;
      }

      dbSQL.startTrans(par.shouldCommit);

      rootCollUri = sqlFct.createCollURI();

      SSServCaller.entityAdd(
        par.user,
        rootCollUri,
        SSLabelStr.get(SSStrU.valueRoot),
        SSEntityEnum.coll,
        false);

      sqlFct.createColl(rootCollUri);

      SSServCaller.entityCircleCreate(
        par.user,
        rootCollUri,
        new ArrayList<SSUri>(),
        SSEntityCircleTypeE.priv,
        SSLabelStr.get(SSUri.toStr(par.user) + SSStrU.underline + SSStrU.valueRoot),
        SSUri.get(SSUserGlobals.systemUserURI),
        false);

      SSServCaller.entityUsersToCircleAdd(
        par.user,
        SSServCaller.entityCircleURIPublicGet(),
        par.user,
        false);

      sqlFct.addUserRootColl(rootCollUri, par.user);

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(SSSQLDeadLockErr deadLockErr){

      if(dbSQL.rollBack(parA)){
        return collUserRootAdd(parA);
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
  public Boolean collUserEntriesAdd(final SSServPar parA) throws Exception{

    final SSCollUserEntriesAddPar par = new SSCollUserEntriesAddPar(parA);

    try{

      dbSQL.startTrans(par.shouldCommit);

      for(int counter = 0; counter < par.entries.size(); counter++){

        SSServCaller.collUserEntryAdd(
          par.user,
          par.coll,
          par.entries.get(counter),
          par.entryLabels.get(counter),
          false,
          par.saveUE,
          false);
      }

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(SSSQLDeadLockErr deadLockErr){

      if(dbSQL.rollBack(parA)){
        return collUserEntriesAdd(parA);
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
  public Boolean collUserEntryDelete(final SSServPar parA) throws Exception{

    final SSCollUserEntryDeletePar par = new SSCollUserEntryDeletePar(parA);

    try{

      if(!SSServCaller.entityUserCanEdit(par.user, par.coll)){
        throw new Exception("user cannot delete from this coll");
      }

      if(!SSServCaller.entityUserCanRead(par.user, par.collEntry)){
        throw new Exception("user cannot delete this coll entry");
      }

      dbSQL.startTrans(par.shouldCommit);

      if(sqlFct.isColl(par.collEntry)){
        SSCollEntryDeleteFct.removeColl(sqlFct, par);
      }else{
        SSCollEntryDeleteFct.removeCollEntry(sqlFct, par);
      }

      SSCollUEFct.collUserEntryDelete(par);

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(SSSQLDeadLockErr deadLockErr){

      if(dbSQL.rollBack(parA)){
        return collUserEntryDelete(parA);
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
  public Boolean collUserEntriesDelete(final SSServPar parA) throws Exception{

    final SSCollUserEntriesDeletePar par = new SSCollUserEntriesDeletePar(parA);

    try{

      dbSQL.startTrans(par.shouldCommit);

      for(SSUri collEntryUri : par.collEntries){
        SSServCaller.collUserEntryDelete(par.user, collEntryUri, par.coll, par.saveUE, false);
      }

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(SSSQLDeadLockErr deadLockErr){

      if(dbSQL.rollBack(parA)){
        return collUserEntriesDelete(parA);
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
  public SSUri collUserEntryAdd(final SSServPar parA) throws Exception{

    final SSCollUserEntryAddPar par = new SSCollUserEntryAddPar(parA);

    try{

      if(!SSServCaller.entityUserCanEdit(par.user, par.coll)){
        throw new Exception("user cannot add to this coll");
      }

      if(par.addNewColl){

        dbSQL.startTrans(par.shouldCommit);

        SSCollEntryAddFct.addNewColl(sqlFct, par);

        dbSQL.commit(par.shouldCommit);

        return par.collEntry;
      }

      if(par.collEntry == null){
        throw new Exception("no coll entry provided");
      }

      if(sqlFct.containsEntry(par.coll, par.collEntry)){
        return par.collEntry;
      }

      if(sqlFct.isColl(par.collEntry)){

        dbSQL.startTrans(par.shouldCommit);

        SSCollEntryAddFct.addPublicColl(sqlFct, par);

        dbSQL.commit(par.shouldCommit);

        return par.collEntry;
      }

      dbSQL.startTrans(par.shouldCommit);

      SSCollEntryAddFct.addCollEntry(sqlFct, par);

      dbSQL.commit(par.shouldCommit);

      return par.collEntry;

    }catch(SSSQLDeadLockErr deadLockErr){

      if(dbSQL.rollBack(parA)){
        return collUserEntryAdd(parA);
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
  public SSUri collUserShareWithUser(final SSServPar parA) throws Exception{

    try{
      final SSCollUserShareWithUserPar par = new SSCollUserShareWithUserPar(parA);

      if(!SSServCaller.entityUserCanEdit(par.user, par.collUri)){
        throw new Exception("user cannot share this coll");
      }

      final SSUri rootCollUri = SSServCaller.collUserRootGet(par.userUriToShareWith).uri;

      if(rootCollUri == null){
        throw new Exception("user to share coll with doesnt exist");
      }

      if(!sqlFct.isColl(par.collUri)){
        throw new Exception("to share coll isnt a coll");
      }

      if(
        sqlFct.containsEntry (rootCollUri, par.collUri) ||
        sqlFct.ownsUserColl  (par.userUriToShareWith, par.collUri)){
        throw new Exception("coll is already shared with user");
      }

      if(sqlFct.ownsUserASubColl(par.userUriToShareWith, par.collUri)){
        throw new Exception("a sub coll is already shared with user");
      }

      dbSQL.startTrans(par.shouldCommit);

      SSCollUserShareWithUserFct.addCollToRootColl                           (sqlFct, par, rootCollUri);
      SSCollMiscFct.addCollAndSubCollsWithEntriesToCircle(
        sqlFct, 
        par.user, 
        sqlFct.getCollWithEntries(
          par.collUri, 
          new ArrayList<SSEntityCircleTypeE>()), 
        par.collCircleUri);
      
      SSCollUserShareWithUserFct.addCollUsersToSharedCircle                   (sqlFct, par);

      dbSQL.commit(par.shouldCommit);

      return par.collUri;

    }catch(SSSQLDeadLockErr deadLockErr){

      if(dbSQL.rollBack(parA)){
        return collUserShareWithUser(parA);
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
  public SSUri collUserSetPublic(final SSServPar parA) throws Exception{

    final SSCollUserSetPublicPar par = new SSCollUserSetPublicPar(parA);

    try{

      if(!SSServCaller.entityUserCanAll(par.user, par.collUri)){
        throw new Exception("user is not allowed to set coll public");
      }

      dbSQL.startTrans(par.shouldCommit);

      SSCollMiscFct.addCollAndSubCollsWithEntriesToCircle(
        sqlFct,
        par.user,
        sqlFct.getCollWithEntries(
          par.collUri, 
          new ArrayList<SSEntityCircleTypeE>()),
        par.publicCircleUri);

      dbSQL.commit(par.shouldCommit);

      return par.collUri;
    }catch(SSSQLDeadLockErr deadLockErr){

      if(dbSQL.rollBack(parA)){
        return collUserSetPublic(parA);
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
  public Boolean collUserEntryChangePos(SSServPar parA) throws Exception{

    final SSCollUserEntryChangePosPar par         = new SSCollUserEntryChangePosPar(parA);
    final List<SSUri>                 collEntries = new ArrayList<SSUri>();
    final List<Integer>               order       = new ArrayList<Integer>();
    Integer                           counter     = 0;

    try{

      if(!SSServCaller.entityUserCanEdit(par.user, par.coll)){
        throw new Exception("user cannot change the order of entities in this coll");
      }

      while(counter < par.order.size()){
        collEntries.add(SSUri.get(par.order.get(counter++)));
        order.add(Integer.valueOf(par.order.get(counter++)));
      }

      dbSQL.startTrans(par.shouldCommit);

      sqlFct.changeCollEntriesPos(par.coll, collEntries, order);

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(SSSQLDeadLockErr deadLockErr){

      if(dbSQL.rollBack(parA)){
        return collUserEntryChangePos(parA);
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
  public SSColl collUserWithEntries(final SSServPar parA) throws Exception{

    try{
      final SSCollUserWithEntriesPar par = new SSCollUserWithEntriesPar(parA);

      if(!SSServCaller.entityUserCanRead(par.user, par.coll)){
        throw new Exception("user cannot access this collection");
      }

      return SSCollMiscFct.getCollWithEntriesWithCircleTypes(sqlFct, par.user, par.coll);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSColl> collsUserWithEntries(SSServPar parA) throws Exception{

    try{
      
      final SSCollsUserWithEntriesPar par                  = new SSCollsUserWithEntriesPar(parA);
      final List<SSColl>              userCollsWithEntries = new ArrayList<SSColl>();
      
      for(String collUri : sqlFct.getAllUserCollURIs(par.user)){

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
  public SSColl collUserRootGet(final SSServPar parA) throws Exception{

    final SSCollUserRootGetPar par = new SSCollUserRootGetPar(parA);

    try{

      return SSCollMiscFct.getCollWithEntriesWithCircleTypes(
        sqlFct,
        par.user,
        sqlFct.getUserRootCollURI(par.user));

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSColl collUserParentGet(SSServPar parA) throws Exception{

    final SSCollUserParentGetPar par = new SSCollUserParentGetPar(parA);

    try{

      if(!SSServCaller.entityUserCanRead(par.user, par.coll)){
        throw new Exception("user cannot change the order of entities in this coll");
      }

      return SSCollMiscFct.getCollWithEntriesWithCircleTypes(
        sqlFct,
        par.user,
        sqlFct.getUserDirectParentCollURI(par.user, par.coll));

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSColl> collUserHierarchyGet(final SSServPar parA) throws Exception{

    try{
      
      final SSCollUserHierarchyGetPar par            = new SSCollUserHierarchyGetPar(parA);
      final List<SSUri>               hierarchy      = new ArrayList<SSUri>();
      final List<SSColl>              colls          = new ArrayList<SSColl>();
      final SSUri                     rootCollUri;
      SSUri                           directPartentCollUri;

      if(!SSServCaller.entityUserCanRead(par.user, par.collUri)){
        throw new Exception("user access this collection");
      }

      rootCollUri          = sqlFct.getUserRootCollURI         (par.user);
      directPartentCollUri = sqlFct.getUserDirectParentCollURI (par.user, par.collUri);

      while(!SSUri.isSame(rootCollUri, directPartentCollUri)){

        hierarchy.add(directPartentCollUri);

        directPartentCollUri = sqlFct.getUserDirectParentCollURI(par.user, directPartentCollUri);
      }

      hierarchy.add(rootCollUri);

      for(SSUri collUri : hierarchy){

        colls.add(
          SSColl.get(
            collUri,
            null,
            null,
            SSStrU.toString(SSServCaller.entityLabelGet(collUri)),
            null));
      }

      return colls;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSTagFrequ> collUserCumulatedTagsGet(final SSServPar parA) throws Exception{

    try{
      
      final SSCollUserCummulatedTagsGetPar  par            = new SSCollUserCummulatedTagsGetPar(parA);
      final Map<String, Integer>            tagLabelFrequs = new HashMap<String, Integer>();
      final List<SSTagFrequ>                tagFrequs      = new ArrayList<SSTagFrequ>();
      SSColl                                coll;
      String                                tagLabel;
      
      if(!SSServCaller.entityUserCanRead(par.user, par.collUri)){
        throw new Exception("user cannot access this collection");
      }

      coll = sqlFct.getCollWithEntries(par.collUri, new ArrayList<SSEntityCircleTypeE>());

      for(SSTagFrequ tagFrequ : SSServCaller.tagUserFrequsGet(par.user, par.collUri, null, null)){

        tagLabel = tagFrequ.label.toString();

        if(tagLabelFrequs.containsKey(tagLabel)){
          tagLabelFrequs.put(tagLabel, tagLabelFrequs.get(tagLabel) + tagFrequ.frequ);
        }else{
          tagLabelFrequs.put(tagLabel, tagFrequ.frequ);
        }
      }

      for(SSCollEntry collEntry : coll.entries){

        if(SSEntityEnum.equals(collEntry.entityType, SSEntityEnum.coll)){

          for(SSTagFrequ tagFrequ : SSServCaller.collUserCumulatedTagsGet(par.user, collEntry.uri)){

            tagLabel = tagFrequ.label.toString();

            if(tagLabelFrequs.containsKey(tagLabel)){
              tagLabelFrequs.put(tagLabel, tagLabelFrequs.get(tagLabel) + tagFrequ.frequ);
            }else{
              tagLabelFrequs.put(tagLabel, tagFrequ.frequ);
            }
          }

        }else{

          for(SSTagFrequ tagFrequ : SSServCaller.tagUserFrequsGet(par.user, collEntry.uri, null, null)){

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

        tagFrequs.add(SSTagFrequ.get(
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

  //TODO dtheiler: implement this method in entity service based on circles, not colls anymore
  @Override
  public Boolean collEntityInCircleTypeForUserIs(final SSServPar parA) throws Exception{

    try{
      
      final SSCollEntityInCircleTypeForUserIsPar par = new SSCollEntityInCircleTypeForUserIsPar(parA);
      final List<String>                         collUris;
      final List<String>                         userCollUris;
      
      if(!SSServCaller.entityUserCanRead(par.user, par.entityUri)){
        throw new Exception("user cannot access entity");
      }

      userCollUris = sqlFct.getAllUserCollURIs(par.user);
      collUris     = sqlFct.getCollUrisContainingEntity(par.entityUri);

      for(String collUri : SSStrU.retainAll(collUris, userCollUris)){

        if(SSEntityCircleTypeE.contains(
          SSServCaller.entityUserEntityCircleTypesGet(
            par.user,
            SSUri.get(collUri)), par.circleType)){

          return true;
        }
      }

      return false;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSColl> collsUserEntityIsInGet(final SSServPar parA) throws Exception{

    final SSCollsUserEntityIsInGetPar par           = new SSCollsUserEntityIsInGetPar(parA);
    final List<SSColl>                colls         = new ArrayList<SSColl>();
    final List<String>                collUris;
    final List<String>                userCollUris;

    try{

      if(!SSServCaller.entityUserCanRead(par.user, par.entityUri)){
        throw new Exception("user cannot access entity");
      }

      userCollUris = sqlFct.getAllUserCollURIs(par.user);
      collUris     = sqlFct.getCollUrisContainingEntity(par.entityUri);

      for(String coll : SSStrU.retainAll(collUris, userCollUris)){
        colls.add(sqlFct.getCollWithEntries(SSUri.get(coll), new ArrayList<SSEntityCircleTypeE>()));
      }

      return colls;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSColl> collsUserCouldSubscribeGet(final SSServPar parA) throws Exception{

    try{
      final SSCollsUserCouldSubscribeGetPar par          = new SSCollsUserCouldSubscribeGetPar(parA);
      final List<String>                    userCollUris = sqlFct.getAllUserCollURIs(par.user);
      final List<SSColl>                    publicColls  = new ArrayList<SSColl>();

      for(SSColl publicColl : sqlFct.getAllPublicColls()){

        if(!userCollUris.contains(SSUri.toStr(publicColl.uri))){
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
  public SSUri collToCircleAdd(final SSServPar parA) throws Exception{

    try{
      final SSCollToCircleAddPar par = new SSCollToCircleAddPar(parA);

      if(!SSServCaller.entityUserCanRead(par.user, par.collUri)){
        throw new Exception("user is not allowed to add collection to circle");
      }

      dbSQL.startTrans(par.shouldCommit);

      SSCollMiscFct.addCollAndSubCollsWithEntriesToCircle(
        sqlFct,
        par.user,
        sqlFct.getCollWithEntries(
          par.collUri, 
          new ArrayList<SSEntityCircleTypeE>()),
        par.collCircleUri);

      dbSQL.commit(par.shouldCommit);

      return par.collUri;
    }catch(SSSQLDeadLockErr deadLockErr){

      if(dbSQL.rollBack(parA)){
        return collToCircleAdd(parA);
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
}
