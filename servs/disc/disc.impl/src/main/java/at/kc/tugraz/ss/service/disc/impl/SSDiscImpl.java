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
package at.kc.tugraz.ss.service.disc.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivEntityAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleTypesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAddPar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.kc.tugraz.ss.service.disc.api.*;
import at.kc.tugraz.ss.service.disc.datatypes.*;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryURIsGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscURIsForTargetGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscRemovePar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsAllGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscURIsForTargetGetRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscRemoveRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscWithEntriesRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscsAllGetRet;
import at.kc.tugraz.ss.service.disc.impl.fct.activity.SSDiscActivityFct;
import at.kc.tugraz.ss.service.disc.impl.fct.misc.SSDiscMiscFct;
import at.kc.tugraz.ss.service.disc.impl.fct.op.SSDiscUserEntryAddFct;
import at.kc.tugraz.ss.service.disc.impl.fct.sql.SSDiscSQLFct;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.*;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSWarnE;

public class SSDiscImpl
  extends SSServImplWithDBA
  implements
  SSDiscClientI,
  SSDiscServerI,
  SSEntityHandlerImplI,
  SSEntityDescriberI,
  SSUserRelationGathererI,
  SSUsersResourcesGathererI{

  private final SSDiscSQLFct sqlFct;

  public SSDiscImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    sqlFct = new SSDiscSQLFct(this);
  }

  @Override
  public void getUserRelations(
    final List<String> allUsers,
    final Map<String, List<SSUri>> userRelations) throws Exception{

    List<SSEntityCircle> discUserCircles;
    List<SSDisc> allDiscs;

    for(String user : allUsers){

      final SSUri userUri = SSUri.get(user);

      allDiscs
        = discsAllGet(
          new SSDiscsAllGetPar(
            null,
            null,
            userUri));

      for(SSDisc disc : allDiscs){

        discUserCircles
          = ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlesGet(
            new SSCirclesGetPar(
              null,
              null,
              userUri,
              userUri,
              disc.id,
              SSEntityE.asListWithoutNullAndEmpty(),
              false,
              true,
              false));

        for(SSEntityCircle circle : discUserCircles){

          if(userRelations.containsKey(user)){
            userRelations.get(user).addAll(SSUri.getFromEntitites(circle.users));
          }else{
            userRelations.put(user, SSUri.getFromEntitites(circle.users));
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
    final List<String> allUsers,
    final Map<String, List<SSUri>> usersResources) throws Exception{

    for(String user : allUsers){

      for(SSDisc disc
        : discsAllGet(
          new SSDiscsAllGetPar(
            null,
            null,
            SSUri.get(user)))){

        if(usersResources.containsKey(user)){
          usersResources.get(user).add(disc.id);
        }else{

          final List<SSUri> resourceList = new ArrayList<>();

          resourceList.add(disc.id);

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
    final SSUri user,
    final List<SSUri> users,
    final SSUri entity,
    final List<SSUri> entitiesToExclude,
    final SSEntityE entityType) throws Exception{

    return false;
  }

  @Override
  public List<SSUri> getSubEntities(
    final SSUri user,
    final SSUri entity,
    final SSEntityE type) throws Exception{

    if(!SSStrU.equals(type, SSEntityE.disc)
      && !SSStrU.equals(type, SSEntityE.qa)
      && !SSStrU.equals(type, SSEntityE.chat)){
      return null;
    }

    try{
      return sqlFct.getDiscEntryURIs(entity);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSUri> getParentEntities(
    final SSUri user,
    final SSUri entity,
    final SSEntityE type) throws Exception{

    switch(type){

      case discEntry:
      case qaEntry:
      case chatEntry: {

        try{
          final List<String> userDiscUris = sqlFct.getDiscURIsForUser(user);
          final List<String> discUris = sqlFct.getDiscURIsContainingEntry(entity);

          return SSUri.get(SSStrU.retainAll(discUris, userDiscUris));
        }catch(Exception error){
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
    }

    return new ArrayList<>();
  }

  @Override
  public Boolean setEntityPublic(
    final SSUri userUri,
    final SSUri entityUri,
    final SSEntityE entityType,
    final SSUri publicCircleUri) throws Exception{

    return false;
  }

  @Override
  public void shareEntityWithUsers(
    final SSUri user,
    final List<SSUri> usersToShareWith,
    final SSUri entity,
    final SSUri circle,
    final SSEntityE entityType,
    final Boolean saveActivity) throws Exception{

    switch(entityType){
      case disc:
      case chat:
      case qa:

        for(SSUri userToShareWith : usersToShareWith){

          if(sqlFct.ownsUserDisc(userToShareWith, entity)){
            SSLogU.warn(SSWarnE.discAlreadySharedWithUser);
            return;
          }

          sqlFct.addDisc(entity, userToShareWith);

          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntitiesAdd(
            new SSCircleEntitiesAddPar(
              null,
              null,
              user,
              circle,
              SSDiscMiscFct.getDiscContentURIs(sqlFct, entity),
              false,
              false,
              false));

          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleUsersAdd(
            new SSCircleUsersAddPar(
              null,
              null,
              user,
              circle,
              sqlFct.getDiscUserURIs(entity),
              false,
              false,
              false));
        }
    }
  }

  @Override
  public void addEntityToCircle(
    final SSUri user,
    final SSUri circle,
    final List<SSUri> circleUsers,
    final SSUri entity,
    final SSEntityE type) throws Exception{

    switch(type){
      case qa:
      case disc:
      case chat: {

        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            null,
            null,
            user,
            circle,
            SSDiscMiscFct.getDiscContentURIs(sqlFct, entity),
            false,
            false,
            false));

        for(SSUri circleUser : circleUsers){

          if(sqlFct.ownsUserDisc(circleUser, entity)){
            continue;
          }

          sqlFct.addDisc(entity, circleUser);
        }
      }
    }
  }

  @Override
  public void addUsersToCircle(
    final SSUri user,
    final List<SSUri> users,
    final SSEntityCircle circle) throws Exception{

    for(SSEntity circleEntity : circle.entities){

      switch(circleEntity.type){
        case qa:
        case disc:
        case chat: {

          for(SSUri userToAdd : users){

            if(sqlFct.ownsUserDisc(userToAdd, circleEntity.id)){
              continue;
            }

            sqlFct.addDisc(circleEntity.id, userToAdd);
          }
        }
      }
    }
  }

  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSUri userUri,
    final SSEntityE entityType,
    final SSUri entityUri,
    final Boolean removeUserTags,
    final Boolean removeUserRatings,
    final Boolean removeFromUserColls,
    final Boolean removeUserLocations) throws Exception{
  }

  @Override
  public SSEntity getUserEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{

    try{

      if(par.setDiscs){

        entity.discs.addAll(
          discURIsForTargetGet(
            new SSDiscURIsForTargetGetPar(
              null, 
              null, 
              par.user, 
              entity.id)));
      }

      return entity;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSUri> discEntryURIsGet(final SSDiscEntryURIsGetPar par) throws Exception{

    try{
      return sqlFct.getDiscEntryURIs(par.disc);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void discEntryAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSDiscEntryAddPar par = (SSDiscEntryAddPar) parA.getFromJSON(SSDiscEntryAddPar.class);
    final SSDiscEntryAddRet ret = discEntryAdd(par);

    SSDiscActivityFct.discEntryAdd(par, ret);

    sSCon.writeRetFullToClient(ret);
  }

  @Override
  public SSDiscEntryAddRet discEntryAdd(final SSDiscEntryAddPar par) throws Exception{

    try{
      SSUri discEntryUri = null;

      if(par.addNewDisc){

        SSDiscUserEntryAddFct.checkWhetherUserCanAddDisc(par);

        if(par.entity != null){

          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlePrivEntityAdd(
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
        }
      }

      if(!par.addNewDisc){
        SSDiscUserEntryAddFct.checkWhetherUserCanAddDiscEntry(par);
      }

      dbSQL.startTrans(par.shouldCommit);

      if(par.addNewDisc){

        par.disc = SSServCaller.vocURICreate();

        SSDiscUserEntryAddFct.addDisc(
          sqlFct,
          par.disc,
          par.user,
          par.entity,
          par.type,
          par.label,
          par.description);

        attachEntities(
          par.user, 
          par.disc, 
          par.entities, 
          par.entityLabels);
      }

      if(par.entry != null){

        discEntryUri = 
          SSDiscUserEntryAddFct.addDiscEntry(
            sqlFct,
            par.user,
            par.disc,
            par.entry);

        attachEntities(
          par.user, 
          discEntryUri, 
          par.entities, 
          par.entityLabels);
      }

      if(par.addNewDisc){

        SSServCaller.circleEntityShare(
          par.user,
          par.disc,
          par.users,
          par.circles,
          null,
          false);
      }

      dbSQL.commit(par.shouldCommit);

      return SSDiscEntryAddRet.get(
        par.disc,
        discEntryUri);

    }catch(Exception error){

      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){

        if(dbSQL.rollBack(par.shouldCommit)){

          SSServErrReg.reset();

          return discEntryAdd(par);
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
  public void discsAllGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSDiscsAllGetPar par = (SSDiscsAllGetPar) parA.getFromJSON(SSDiscsAllGetPar.class);

    sSCon.writeRetFullToClient(SSDiscsAllGetRet.get(discsAllGet(par)));
  }

  @Override
  public List<SSDisc> discsAllGet(final SSDiscsAllGetPar par) throws Exception{

    try{
      final List<SSDisc> discsWithoutEntries = new ArrayList<>();
      SSDisc disc;

      for(SSUri discUri : sqlFct.getDiscURIs(par.user)){

        disc = sqlFct.getDiscWithoutEntries(discUri);

        disc.attachedEntities.addAll(
          SSServCaller.entityEntitiesAttachedGet(
            par.user,
            discUri));

        disc.circleTypes.addAll(
          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleTypesGet(
            new SSCircleTypesGetPar(
              null,
              null,
              par.user,
              par.user,
              disc.id,
              false)));

        discsWithoutEntries.add(disc);
      }

      return discsWithoutEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void discWithEntriesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSDiscWithEntriesGetPar par = (SSDiscWithEntriesGetPar) parA.getFromJSON(SSDiscWithEntriesGetPar.class);

    sSCon.writeRetFullToClient(SSDiscWithEntriesRet.get(discWithEntriesGet(par)));
  }

  @Override
  public SSDisc discWithEntriesGet(final SSDiscWithEntriesGetPar par) throws Exception{

    try{
      SSDiscEntry discEntry;

      SSServCallerU.canUserReadEntity(par.user, par.disc);

      final SSDisc disc = sqlFct.getDiscWithEntries(par.disc);

      disc.attachedEntities.addAll(
        SSServCaller.entityEntitiesAttachedGet(
          par.user,
          disc.id));

      for(Object entry : disc.entries){

        discEntry = (SSDiscEntry) entry;

        discEntry.attachedEntities.addAll(
          SSServCaller.entityEntitiesAttachedGet(
            par.user,
            discEntry.id));

        discEntry.likes
          = SSServCaller.likesUserGet(
            par.user,
            null,
            discEntry.id);

        if(par.includeComments){

          discEntry.comments.addAll(
            SSServCaller.commentsUserGet(
              par.user,
              null,
              discEntry.id));
        }
      }

      return disc;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void discRemove(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSDiscRemovePar par = (SSDiscRemovePar) parA.getFromJSON(SSDiscRemovePar.class);

    sSCon.writeRetFullToClient(SSDiscRemoveRet.get(discRemove(par)));

//    SSDiscActivityFct.removeDisc(new SSDiscUserRemovePar(parA));
  }

  @Override
  public SSUri discRemove(final SSDiscRemovePar par) throws Exception{

    try{
      SSServCallerU.canUserAllEntity(par.user, par.disc);

      dbSQL.startTrans(par.shouldCommit);

      switch(((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleMostOpenCircleTypeGet(
        new SSCircleMostOpenCircleTypeGetPar(
          null,
          null,
          par.user,
          par.user,
          par.disc,
          false))){

          case priv:
            sqlFct.deleteDisc(par.disc);
            break;
          case group:
          case pub:
            sqlFct.unlinkDisc(par.user, par.disc);
            break;
        }

        dbSQL.commit(par.shouldCommit);

        return par.disc;
    }catch(Exception error){

      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){

        if(dbSQL.rollBack(par.shouldCommit)){

          SSServErrReg.reset();

          return discRemove(par);
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
  public void discURIsForTargetGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSDiscURIsForTargetGetPar par = (SSDiscURIsForTargetGetPar) parA.getFromJSON(SSDiscURIsForTargetGetPar.class);

    sSCon.writeRetFullToClient(SSDiscURIsForTargetGetRet.get(discURIsForTargetGet(par)));
  }

  @Override
  public List<SSUri> discURIsForTargetGet(final SSDiscURIsForTargetGetPar par) throws Exception{

    try{
      SSServCallerU.canUserReadEntity(par.user, par.entity);

      return sqlFct.getDiscURIs(par.user, par.entity);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSDisc> discsWithEntriesGet(final SSDiscsWithEntriesGetPar par) throws Exception{

    try{
      final List<SSDisc> discsWithEntries = new ArrayList<>();

      for(SSDisc disc : discsAllGet(new SSDiscsAllGetPar(null, null, par.user))){

        discsWithEntries.add(
          discWithEntriesGet(
            new SSDiscWithEntriesGetPar(
              null,
              null,
              par.user,
              disc.id,
              par.maxEntries,
              false)));
      }

      return discsWithEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private void attachEntities(
    final SSUri         user,
    final SSUri         entity,
    final List<SSUri>   entitiesToAttach,
    final List<SSLabel> entityLabels) throws Exception{
    
    if(entitiesToAttach.isEmpty()){
      return;
    }
    
    try{
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          user,
          entity,
          null, //uriAlternative
          null, //type
          null, //label,
          null, //description,
          SSTextComment.asListWithoutNullAndEmpty(), //comments,
          SSUri.asListWithoutNullAndEmpty(), //downloads,
          SSUri.asListWithoutNullAndEmpty(), //screenShots,
          SSUri.asListWithoutNullAndEmpty(), //images,
          SSUri.asListWithoutNullAndEmpty(), //videos,
          entitiesToAttach,  //entitiesToAttach
          null, //creationTime
          null, //read,
          true, //withUserRestriction
          false)); //shouldCommit
      
      if(
        entityLabels.isEmpty() ||
        entitiesToAttach.size() != entityLabels.size()){
        return;
      }
      
      for(Integer counter = 0; counter < entitiesToAttach.size(); counter++){
        
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            user,
            entitiesToAttach.get(counter), //entity
            null, //uriAlternative
            null, //type
            entityLabels.get(counter), //label,
            null, //description,
            SSTextComment.asListWithoutNullAndEmpty(), //comments,
            SSUri.asListWithoutNullAndEmpty(), //downloads,
            SSUri.asListWithoutNullAndEmpty(), //screenShots,
            SSUri.asListWithoutNullAndEmpty(), //images,
            SSUri.asListWithoutNullAndEmpty(), //videos,
            SSUri.asListWithoutNullAndEmpty(), //entitiesToAttach
            null, //creationTime
            null, //read,
            false, //withUserRestriction
            false)); //shouldCommit
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
