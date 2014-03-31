/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserWithEntriesPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserSharePar;
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
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSAccessRightsCircleTypeE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSAccessRightsRightTypeE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserParentGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryChangePosRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryDeleteRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserParentGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserRootGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserShareRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserWithEntriesRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserWithEntriesRet;
import at.kc.tugraz.ss.service.coll.impl.fct.sql.SSCollSQLFct;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesDeletePar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesDeleteRet;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollEntityPrivateForUserIsPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollEntitySharedOrFollowedForUserIsPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserCummulatedTagsGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserHierarchyGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserSpaceGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserCouldSubscribeGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserEntityIsInGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserCummulatedTagsGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserHierarchyGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserCouldSubscribeGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserEntityIsInGetRet;
import at.kc.tugraz.ss.service.coll.impl.fct.op.SSCollEntryAddFct;
import at.kc.tugraz.ss.service.coll.impl.fct.op.SSCollEntryDeleteFct;
import at.kc.tugraz.ss.service.coll.impl.fct.ue.SSCollUEFct;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagFrequ;
import java.lang.reflect.Method;
import java.util.*;

public class SSCollImpl extends SSServImplWithDBA implements SSCollClientI, SSCollServerI, SSEntityHandlerImplI{

  private final SSCollSQLFct         sqlFct;
  private final SSCollEntryAddFct    collEntryAddFct;
  private final SSCollEntryDeleteFct collEntryDeleteFct;

  public SSCollImpl(final SSServConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{

    super(conf, dbGraph, dbSQL);

    this.sqlFct              = new SSCollSQLFct          (dbSQL);
    this.collEntryAddFct     = new SSCollEntryAddFct     (dbSQL);
    this.collEntryDeleteFct  = new SSCollEntryDeleteFct  (dbSQL);
  }
  
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSEntityEnum                                  entityType,
    final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par,
    final Boolean                                       shouldCommit) throws Exception{
    
    if(!par.removeFromUserColls){
      return;
    }
    
    try{
      final List<SSColl> collsUserEntityIsInGet = SSServCaller.collsUserEntityIsInGet(par.user, par.entityUri);
      
      for(SSColl coll : collsUserEntityIsInGet){
        SSServCaller.collUserEntryDelete(par.user, par.entityUri, coll.uri, true, shouldCommit);
      }
      
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

  /* SSServRegisterableImplI */
  @Override
  public List<SSMethU> publishClientOps() throws Exception{

    List<SSMethU> clientOps = new ArrayList<SSMethU>();

    Method[] methods = SSCollClientI.class.getMethods();

    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }

    return clientOps;
  }

  @Override
  public List<SSMethU> publishServerOps() throws Exception{

    List<SSMethU> serverOps = new ArrayList<SSMethU>();

    Method[] methods = SSCollServerI.class.getMethods();

    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }

    return serverOps;
  }

  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSCollClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }

  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSCollServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }

  /* SSCollClientI */
  @Override
  public void collUserParentGet(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    SSColl collParent = collUserParentGet(par);

    sSCon.writeRetFullToClient(SSCollUserParentGetRet.get(collParent, par.op));
  }

  @Override
  public void collUserRootGet(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    SSColl collRoot = collUserRootGet(par);

    sSCon.writeRetFullToClient(SSCollUserRootGetRet.get(collRoot, par.op));
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
  public void collUserShare(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSCollUserShareRet.get(collUserShare(par), par.op));
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
    final SSUri rootCollUri;

    try{

      if(sqlFct.existsUserRootColl(par.user)){
        return true;
      }

      rootCollUri = sqlFct.createCollURI();

      SSServCaller.addEntity(
        par.user,
        rootCollUri,
        SSLabelStr.get(SSStrU.valueRoot),
        SSEntityEnum.coll);

      dbSQL.startTrans(par.shouldCommit);

      sqlFct.createColl     (rootCollUri);
      
      SSServCaller.accessRightsUserCircleCreate(
        par.user, 
        rootCollUri,
        null, 
        SSAccessRightsCircleTypeE.priv, 
        SSLabelStr.get(SSUri.toStr(par.user) + SSStrU.underline + SSStrU.valueRoot), 
        false);
      
      sqlFct.addUserRootColl(rootCollUri, par.user);

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSUri collUserEntryAdd(final SSServPar parA) throws Exception{

    final SSCollUserEntryAddPar par = new SSCollUserEntryAddPar(parA);

    try{

      if(sqlFct.containsEntry(par.coll, par.collEntry)){
        return par.collEntry;
      }
      
      if(!SSServCaller.accessRightsUserAllowedIs(par.user, par.coll, null, SSAccessRightsRightTypeE.edit)){
        throw new Exception("user cannot add to this coll");
      }
      
      if(par.addNewColl){
        return collEntryAddFct.addNewColl(par);
      }
      
      if(sqlFct.isColl(par.collEntry)){
        return collEntryAddFct.addExistingColl(par);
      }
      
      collEntryAddFct.addCollEntry(par);
      
      SSCollUEFct.collUserEntryAdd(par);
      
      return par.collEntry;
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public Boolean collUserEntriesAdd(final SSServPar parA) throws Exception{

    final SSCollUserEntriesAddPar par = new SSCollUserEntriesAddPar(parA);

    try{

      for(int counter = 0; counter < par.entries.size(); counter++){

        dbSQL.startTrans(par.shouldCommit);

        SSServCaller.collUserEntryAdd(
          par.user,
          par.coll,
          par.entries.get     (counter),
          par.entryLabels.get (counter),
          par.circleUris.get  (counter),
          -1,
          false,
          par.saveUE,
          false);

        dbSQL.commit(par.shouldCommit);
      }

      return true;
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean collUserEntryDelete(final SSServPar parA) throws Exception{

    final SSCollUserEntryDeletePar par = new SSCollUserEntryDeletePar(parA);

    try{

      if(!SSServCaller.accessRightsUserAllowedIs(par.user, par.coll, null, SSAccessRightsRightTypeE.edit)){
        throw new Exception("user cannot delete from this coll");
      }
      
      if(!SSServCaller.accessRightsUserAllowedIs(par.user, par.collEntry, null, SSAccessRightsRightTypeE.read)){
        throw new Exception("user cannot delete this coll entry");
      }
      
      if(sqlFct.isColl(par.collEntry)){ //to remove coll entry is coll itself
        return collEntryDeleteFct.removeColl(par);
      }else{ //to remove coll entry is NO collection

        dbSQL.startTrans(par.shouldCommit);

        for(SSUri circleUri : SSServCaller.accessRightsEntityCirclesURIsGet(par.coll)){
          
          SSServCaller.accessRightsUserEntitiesFromCircleRemove(
            par.user,
            circleUri,
            par.collEntry,
            false);
        }
        
        sqlFct.removeEntryFromColl(par.coll, par.collEntry);

        dbSQL.commit(par.shouldCommit);
      }

      SSCollUEFct.collUserEntryDelete(par);
      
      return true;
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean collUserEntriesDelete(final SSServPar parA) throws Exception{

    final SSCollUserEntriesDeletePar par = new SSCollUserEntriesDeletePar(parA);

    try{

      for(SSUri collEntryUri : par.collEntries){

        dbSQL.startTrans(par.shouldCommit);

        SSServCaller.collUserEntryDelete(par.user, collEntryUri, par.coll, par.saveUE, false);

        dbSQL.commit(par.shouldCommit);
      }
      
      return true;
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  //TODO dtheiler: re-implement this method
  @Override
  public Boolean collUserShare(final SSServPar parA) throws Exception{

    final SSCollUserSharePar par         = new SSCollUserSharePar(parA);

    try{
      final SSColl userColl = sqlFct.getUserColl(par.user, par.coll);

      if(SSSpaceEnum.isSharedOrFollow(userColl.space)){
        throw new Exception("coll already shared / followed");
      }
    
      if(
        sqlFct.ownsUserASubColl(par.user, par.coll, SSSpaceEnum.sharedSpace) ||
        sqlFct.ownsUserASubColl(par.user, par.coll, SSSpaceEnum.followSpace)){
        throw new Exception("a sub coll is already shared / followed");
      }
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.shareCollAndSubColls(par.coll);

      dbSQL.commit(par.shouldCommit);
      
      SSCollUEFct.collUserShareColl(par);
      return true;
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public Boolean collUserEntryChangePos(SSServPar parA) throws Exception{

    final SSCollUserEntryChangePosPar  par = new SSCollUserEntryChangePosPar(parA);
    final List<SSUri>                  collEntries = new ArrayList<SSUri>();
    final List<Integer>                order = new ArrayList<Integer>();
    Integer counter = 0;

    try{

      if(!SSServCaller.accessRightsUserAllowedIs(par.user, par.coll, null, SSAccessRightsRightTypeE.edit)){
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
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSColl collUserWithEntries(final SSServPar parA) throws Exception{

    final SSCollUserWithEntriesPar par = new SSCollUserWithEntriesPar(parA);

    try{
      
      if(!SSServCaller.accessRightsUserAllowedIs(par.user, par.coll, null, SSAccessRightsRightTypeE.read)){
        throw new Exception("user cannot change the order of entities in this coll");
      }
                  
      return sqlFct.getUserCollWithEntries(par.user, par.coll, par.sort);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSColl> collsUserWithEntries(SSServPar parA) throws Exception{

    final SSCollsUserWithEntriesPar par                  = new SSCollsUserWithEntriesPar(parA);
    final List<SSColl>              userCollsWithEntries = new ArrayList<SSColl>();

    try{

      for(String collUri : sqlFct.getAllUserCollURIs(par.user)){
        userCollsWithEntries.add(sqlFct.getUserCollWithEntries(par.user, SSUri.get(collUri), true));
      }

      return userCollsWithEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSColl collUserRootGet(SSServPar parA) throws Exception{

    SSCollUserRootGetPar par = new SSCollUserRootGetPar(parA);

    try{
      
      return sqlFct.getUserCollWithEntries(par.user, sqlFct.getUserRootCollURI(par.user), true);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSColl collUserParentGet(SSServPar parA) throws Exception{

    SSCollUserParentGetPar par = new SSCollUserParentGetPar(parA);

    try{
      
      if(!SSServCaller.accessRightsUserAllowedIs(par.user, par.coll, null, SSAccessRightsRightTypeE.read)){
        throw new Exception("user cannot change the order of entities in this coll");
      }
            
      return sqlFct.getUserCollWithEntries(par.user, sqlFct.getUserDirectParentCollURI(par.user, par.coll), true);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  //TODO dtheiler: rewrite this method
  @Override
  public SSSpaceEnum collUserSpaceGet(SSServPar parA) throws Exception{

    SSCollUserSpaceGetPar par = new SSCollUserSpaceGetPar(parA);

    try{
      return sqlFct.getUserColl(par.user, par.collUri).space;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSColl> collUserHierarchyGet(SSServPar parA) throws Exception{

    final SSCollUserHierarchyGetPar par           = new SSCollUserHierarchyGetPar(parA);
    final List<SSUri>               hierarchy     = new ArrayList<SSUri>();
    final List<SSColl>              colls         = new ArrayList<SSColl>();
    final SSUri                     rootCollUri;
    SSUri directPartentCollUri;
    
    try{
      
      if(!SSServCaller.accessRightsUserAllowedIs(par.user, par.collUri, null, SSAccessRightsRightTypeE.read)){
        throw new Exception("user cannot change the order of entities in this coll");
      }
            
      rootCollUri          = sqlFct.getUserRootCollURI         (par.user);
      directPartentCollUri = sqlFct.getUserDirectParentCollURI (par.user, par.collUri);
      
      while(SSUri.isNotSame(rootCollUri, directPartentCollUri)){
        
        hierarchy.add(directPartentCollUri);
        
        directPartentCollUri = sqlFct.getUserDirectParentCollURI (par.user, directPartentCollUri);
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
   
    final SSCollUserCummulatedTagsGetPar par            = new SSCollUserCummulatedTagsGetPar(parA);
    final Map<String, Integer>           tagLabelFrequs = new HashMap<String, Integer>();
    final List<SSTagFrequ>               tagFrequs      = new ArrayList<SSTagFrequ>();
    SSColl                               coll;
    String      tagLabel;

    try{
      
      if(!SSServCaller.accessRightsUserAllowedIs(par.user, par.collUri, null, SSAccessRightsRightTypeE.read)){
        throw new Exception("user cannot change the order of entities in this coll");
      }
            
      coll = sqlFct.getUserCollWithEntries(par.user, par.collUri, false);
      
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

  //TODO dtheiler: check this method
  @Override
  public Boolean collEntityPrivateForUserIs(final SSServPar parA) throws Exception{

    final SSCollEntityPrivateForUserIsPar          par = new SSCollEntityPrivateForUserIsPar(parA);
    final List<String>                             collUris;
    final List<String>                             userCollUris;
    
    try{
      
      userCollUris = sqlFct.getAllUserCollURIs          (par.user);
      collUris     = sqlFct.getCollUrisContainingEntity (par.entityUri);
        
      for(String collUri : SSStrU.retainAll(collUris, userCollUris)){

        if(SSSpaceEnum.isPrivate(sqlFct.getUserColl(par.user, SSUri.get(collUri)).space)){
          return true;
        }
      }
      
      return false;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  //TODO dtheiler: check this method
  @Override
  public Boolean collEntitySharedOrFollowedForUserIs(final SSServPar parA) throws Exception{

    final SSCollEntitySharedOrFollowedForUserIsPar par = new SSCollEntitySharedOrFollowedForUserIsPar(parA);
    final List<String>                             collUris;
    final List<String>                             userCollUris;
    
    try{
      
      userCollUris = sqlFct.getAllUserCollURIs          (par.user);
      collUris     = sqlFct.getCollUrisContainingEntity (par.entityUri);
      
      for(String collUri : SSStrU.retainAll(collUris, userCollUris)){

        if(SSSpaceEnum.isSharedOrFollow(sqlFct.getUserColl(par.user, SSUri.get(collUri)).space)){
          return true;
        }
      }
        
      return false;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  //TODO dtheiler: check this method
  @Override
  public List<SSColl> collsUserEntityIsInGet(final SSServPar parA) throws Exception{
    
    final SSCollsUserEntityIsInGetPar par      = new SSCollsUserEntityIsInGetPar(parA);
    final List<SSColl>                colls    = new ArrayList<SSColl>();
    final List<String>                collUris;
    final List<String>                userCollUris;
    
    try{
      
      userCollUris = sqlFct.getAllUserCollURIs          (par.user);
      collUris     = sqlFct.getCollUrisContainingEntity (par.entityUri);
       
      for(String collUri : SSStrU.retainAll(collUris, userCollUris)){
        colls.add(sqlFct.getUserColl(par.user, SSUri.get(collUri)));
      }
      
      return colls;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  //TODO dtheiler: check this method
  @Override
  public List<SSColl> collsUserCouldSubscribeGet(final SSServPar parA) throws Exception{
   
    try{
      final SSCollsUserCouldSubscribeGetPar par               = new SSCollsUserCouldSubscribeGetPar(parA);
      final List<SSColl>                    colls             = new ArrayList<SSColl>();
      final List<SSUri>                     sharedCollURIs    = sqlFct.getAllSharedCollURIs();
      final List<String>                    userCollURIs      = sqlFct.getAllUserCollURIs(par.user);
      
      for(SSUri sharedCollURI : sharedCollURIs){
        
        if(
          userCollURIs.contains  (SSUri.toStr(sharedCollURI)) ||
          sqlFct.ownsUserASubColl(par.user, sharedCollURI)){
          continue;
        }
        
        colls.add(
          sqlFct.getColl(
            sharedCollURI,
            SSSpaceEnum.sharedSpace));
      }
      
      return colls;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}