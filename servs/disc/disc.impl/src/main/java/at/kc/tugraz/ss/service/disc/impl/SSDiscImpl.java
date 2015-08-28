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
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitySharePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityAttachEntitiesPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAddPar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.kc.tugraz.ss.service.disc.api.*;
import at.kc.tugraz.ss.service.disc.datatypes.*;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAcceptPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAddFromClientPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryURIsGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscRemovePar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscTargetsAddPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryAcceptRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscRemoveRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscGetRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscTargetsAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscsGetRet;
import at.kc.tugraz.ss.service.disc.impl.fct.activity.SSDiscActivityFct;
import at.kc.tugraz.ss.service.disc.impl.fct.op.SSDiscUserEntryAddFct;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import java.util.*;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSGetParentEntitiesI;
import at.tugraz.sss.serv.SSGetSubEntitiesI;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSPushEntitiesToUsersI;
import at.tugraz.sss.serv.SSPushEntitiesToUsersPar;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;

public class SSDiscImpl
  extends SSServImplWithDBA
  implements
  SSDiscClientI,
  SSDiscServerI,
  SSDescribeEntityI,
  SSAddAffiliatedEntitiesToCircleI,
  SSPushEntitiesToUsersI,
  SSGetSubEntitiesI, 
  SSGetParentEntitiesI,
  SSUserRelationGathererI,
  SSUsersResourcesGathererI{

  private final SSDiscSQLFct     sqlFct;
  private final SSEntityServerI  entityServ;
  private final SSCircleServerI  circleServ;

  public SSDiscImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    this.sqlFct     = new SSDiscSQLFct(this);
    this.entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
    this.circleServ = (SSCircleServerI) SSServReg.getServ(SSCircleServerI.class);
  }

  @Override
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{

    try{
      
      if(par.setDiscs){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          entity.discs,
          discsGet(
            new SSDiscsGetPar(
              par.user,
              true,
              null, //forUser
              null, //discs
              SSUri.asListWithoutNullAndEmpty(entity.id), //targets
              par.withUserRestriction,
              false))); //invokeEntityHandlers
      }
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void getUserRelations(
    final List<String> allUsers,
    final Map<String, List<SSUri>> userRelations) throws Exception{

    List<SSEntity> discUserCircles;

    for(String user : allUsers){
      
      final SSUri userUri = SSUri.get(user);
      
      for(SSEntity disc :
        discsGet(
          new SSDiscsGetPar(
            userUri,
            false, //setEntries,
            userUri, //forUser,
            null, //discs,
            null, //target,
            false, //withUserRestriction,
            false))){ //invokeEntityHandlers);){

        discUserCircles = 
          circleServ.circlesGet(
            new SSCirclesGetPar(
              userUri,
              userUri, //forUser
              disc.id,
              null, //entityTypesToIncludeOnly
              false, //withUserRestriction
              true, //withSystemCircles
              false)); //invokeEntityHandlers

        for(SSEntity circle : discUserCircles){

          if(userRelations.containsKey(user)){
            userRelations.get(user).addAll(SSUri.getDistinctNotNullFromEntities(circle.users));
          }else{
            userRelations.put(user, SSUri.getDistinctNotNullFromEntities(circle.users));
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

      for(SSEntity disc :
        discsGet(
          new SSDiscsGetPar(
            SSUri.get(user),
            false, //setEntries,
            SSUri.get(user), //forUser,
            null, //discs,
            null, //target,
            true, //withUserRestriction,
            false))){ //invokeEntityHandlers)

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
  public List<SSUri> getSubEntities(
    final SSUri user,
    final SSUri entity,
    final SSEntityE type) throws Exception{
    
    try{
      
      switch(type){
        case disc:
        case qa:
        case chat: {
          return sqlFct.getDiscEntryURIs(entity);
        }
        
        default: return null;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSUri> getParentEntities(
    final SSUri     user,
    final SSUri     entity,
    final SSEntityE type) throws Exception{
    
    switch(type){
      
      case discEntry:
      case qaEntry:
      case chatEntry: {
        
        try{
          final List<SSUri>  userDiscUris = sqlFct.getDiscURIs(user);
          final List<String> discUris     = new ArrayList<>();
          
          discUris.add(SSStrU.toStr(sqlFct.getDiscURIContainingEntry(entity)));
          
          return SSUri.get(SSStrU.retainAll(discUris, SSStrU.toStr(userDiscUris)));
        }catch(Exception error){
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
    }
    
    return new ArrayList<>();
  }
  
  @Override
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSAddAffiliatedEntitiesToCirclePar par) throws Exception{
    
    try{
      
      final List<SSUri>    affiliatedURIs     = new ArrayList<>();
      final List<SSEntity> affiliatedEntities = new ArrayList<>();
      
      for(SSEntity entityAdded : par.entities){
        
        switch(entityAdded.type){
          case disc:
          case chat:
          case qa:{
            
            if(SSStrU.contains(par.recursiveEntities, entityAdded)){
              continue;
            }else{
              SSUri.addDistinctWithoutNull(par.recursiveEntities, entityAdded.id);
            }
            
            for(SSUri discContentURI : getDiscAffiliatedURIs(entityAdded.id)){
            
              if(SSStrU.contains(par.recursiveEntities, discContentURI)){
                continue;
              }
              
              SSUri.addDistinctWithoutNull(
                affiliatedURIs,
                discContentURI);
            }
          }
        }
      }
        
      if(!affiliatedURIs.isEmpty()){
      
        SSEntity.addEntitiesDistinctWithoutNull(
          affiliatedEntities,
          entityServ.entitiesGet(
            new SSEntitiesGetPar(
              par.user,
              affiliatedURIs,
              null, //types,
              null, //descPar
              par.withUserRestriction)));
      }
      
      for(SSEntity disc :
        discsGet(
          new SSDiscsGetPar(
            par.user,
            false, //setEntries
            null,  //forUser
            null, //discs
            SSUri.getDistinctNotNullFromEntities(par.entities), //targets
            par.withUserRestriction,
            false))){ //invokeEntityHandlers
        
        if(SSStrU.contains(par.recursiveEntities, disc.id)){
          continue;
        }
          
        SSUri.addDistinctWithoutNull(
          affiliatedURIs,
          disc.id);
        
        SSEntity.addEntitiesDistinctWithoutNull(
          affiliatedEntities,
          disc);
      }
      
      if(affiliatedURIs.isEmpty()){
        return affiliatedEntities;
      }
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          par.user,
          par.circle,
          affiliatedURIs,
          false, //withUserRestriction
          false)); //shouldCommit
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities, 
        SSServCallerU.handleAddAffiliatedEntitiesToCircle(
          par.user, 
          par.circle, 
          affiliatedEntities,
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
      for(SSEntity entityToPush : par.entities){
        
        switch(entityToPush.type){
          case qa:
          case disc:
          case chat: {
            
            for(SSUri userToPushTo : par.users){
              
              if(sqlFct.ownsUserDisc(userToPushTo, entityToPush.id)){
                continue;
              }
              
              sqlFct.addDisc(entityToPush.id, userToPushTo);
            }
            
            break;
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
////                  sqlFct.getDiscUserURIs(entityToAdd.id),
////                  false,
////                  false));
////            }
//      }
//  }

  @Override
  public List<SSUri> discEntryURIsGet(final SSDiscEntryURIsGetPar par) throws Exception{

    try{
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.disc)){
          return new ArrayList<>();
        }
      }
      
      return sqlFct.getDiscEntryURIs(par.disc);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void discEntryAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSDiscEntryAddFromClientPar par = (SSDiscEntryAddFromClientPar) parA.getFromJSON(SSDiscEntryAddFromClientPar.class);
    final SSDiscEntryAddRet           ret = discEntryAdd(par);

    if(par.addNewDisc){
      
      if(
        !par.users.isEmpty() ||
        !par.circles.isEmpty())
        
        entityServ.entityShare(
          new SSEntitySharePar(
            par.user,
            ret.disc,
            par.users, //users
            par.circles, //circles
            false, //setPublic,
            null, //comment,
            true, //withUserRestriction,
            true)); //shouldCommit
    }
    
    sSCon.writeRetFullToClient(ret);
    
    SSDiscActivityFct.discEntryAdd(par, ret, true);
  }

  @Override
  public SSDiscEntryAddRet discEntryAdd(final SSDiscEntryAddPar par) throws Exception{

    try{
      SSUri discEntryUri = null;

      final SSDiscUserEntryAddFct discEntryAddFct = new SSDiscUserEntryAddFct(entityServ);
      
      if(par.addNewDisc){
        discEntryAddFct.checkWhetherUserCanAddDisc(par);
      }
      
      if(!par.addNewDisc){
        
        if(SSObjU.isNull(par.entry)){
          throw new SSErr(SSErrE.parameterMissing);
        }
        
        if(!SSServCallerU.canUserRead(par.user, par.disc)){
          return SSDiscEntryAddRet.get(null, null);
        }
      }

      dbSQL.startTrans(par.shouldCommit);

      if(par.addNewDisc){

        par.disc = SSServCaller.vocURICreate();

        discEntryAddFct.addDisc(
          sqlFct,
          par.disc,
          par.user,
          par.targets,
          par.type,
          par.label,
          par.description,
          par.withUserRestriction);

        discTargetsAdd(
          new SSDiscTargetsAddPar(
            par.user, 
            par.disc, 
            par.targets, 
            par.withUserRestriction, 
            false));
        
        //TODO move to client call
        attachEntities(
          par.user, 
          par.disc, 
          par.entities, 
          par.entityLabels,
          par.withUserRestriction);
      }

      if(par.entry != null){

        discEntryUri = 
          discEntryAddFct.addDiscEntry(
            sqlFct,
            par.user,
            par.disc,
            par.entry,
            par.withUserRestriction);

        //TODO move to client call
        attachEntities(
          par.user, 
          discEntryUri, 
          par.entities, 
          par.entityLabels, 
          par.withUserRestriction);
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
  public void discEntryAccept(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSDiscEntryAcceptPar par = (SSDiscEntryAcceptPar) parA.getFromJSON(SSDiscEntryAcceptPar.class);

    sSCon.writeRetFullToClient(SSDiscEntryAcceptRet.get(discEntryAccept(par)));
  }
  
  @Override
  public SSUri discEntryAccept(final SSDiscEntryAcceptPar par) throws Exception{

    try{
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.entry)){
          return null;
        }
      }

      final SSUri   discURI = sqlFct.getDiscURIContainingEntry(par.entry);
      final SSDisc  disc    = 
        discGet(
          new SSDiscGetPar(
            par.user, 
            discURI, 
            true,  //setEntries
            par.withUserRestriction, 
            false));
      
      if(disc == null){
        return null;
      }
      
      if(par.withUserRestriction){
      
        if(!SSStrU.equals(disc.author, par.user)){
          return null;
        }
      }

      for(SSEntity entry : disc.entries){
        if(((SSDiscEntry)entry).accepted){
          return null;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.acceptEntry(par.entry);

      dbSQL.commit(par.shouldCommit);

      return par.entry;

    }catch(Exception error){

      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){

        if(dbSQL.rollBack(par.shouldCommit)){

          SSServErrReg.reset();

          return discEntryAccept(par);
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
  public void discGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSDiscGetPar par = (SSDiscGetPar) parA.getFromJSON(SSDiscGetPar.class);

    sSCon.writeRetFullToClient(SSDiscGetRet.get(discGet(par)));
  }

  @Override
  public SSDisc discGet(final SSDiscGetPar par) throws Exception{

    try{
      final List<SSEntity>       discEntryEntities = new ArrayList<>();
      final List<SSEntity>       discTargets       = new ArrayList<>();
      SSEntityDescriberPar       descPar;
      
      if(par.invokeEntityHandlers){
        
        descPar = new SSEntityDescriberPar(par.disc);
        
        descPar.setCircleTypes       = par.setCircleTypes;
        descPar.setLikes             = par.setLikes;
        descPar.setComments          = par.setComments;
        descPar.setTags              = par.setTags;
        descPar.setAttachedEntities  = par.setAttachedEntities;
        
      }else{
        descPar = null;
      }
      
      final SSDisc disc =
        SSDisc.get(
          sqlFct.getDisc(par.disc, par.setEntries),
          entityServ.entityGet(
            new SSEntityGetPar(
              par.user,
              par.disc,
              par.withUserRestriction, //withUserRestriction,
              descPar)));
      
      for(SSEntity target : disc.targets){
        
        if(par.invokeEntityHandlers){
          descPar                 = new SSEntityDescriberPar(null);
          descPar.setTags         = par.setTags;
        }else{
          descPar = null;
        }
        
        SSEntity.addEntitiesDistinctWithoutNull(
          discTargets,
          entityServ.entityGet(
            new SSEntityGetPar(
              par.user,
              target.id,
              par.withUserRestriction, //withUserRestriction,
              descPar)));
      }
      
      disc.targets.clear();
      disc.targets.addAll(discTargets);
      
      if(par.invokeEntityHandlers){
        descPar                      = new SSEntityDescriberPar(null);
        descPar.setCircleTypes       = par.setCircleTypes;
        descPar.setLikes             = par.setLikes;
        descPar.setComments          = par.setComments;
        descPar.setTags              = par.setTags;
        descPar.setAttachedEntities  = par.setAttachedEntities;
      }else{
        descPar = null;
      }
        
      for(SSEntity entry : disc.entries){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          discEntryEntities,
          SSDiscEntry.get(
            (SSDiscEntry) entry,
            entityServ.entityGet(
              new SSEntityGetPar(
                par.user,
                ((SSDiscEntry) entry).id,
                par.withUserRestriction, //withUserRestriction,
                descPar))));
      }

      disc.entries.clear();
      disc.entries.addAll(discEntryEntities);
      
      return disc;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void discsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSDiscsGetPar par = (SSDiscsGetPar) parA.getFromJSON(SSDiscsGetPar.class);

    sSCon.writeRetFullToClient(SSDiscsGetRet.get(discsGet(par)));
  }
  
  @Override
  public List<SSEntity> discsGet(final SSDiscsGetPar par) throws Exception{

    try{
      
      if(par.user == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        if(!par.targets.isEmpty()){
          
          if(!SSServCallerU.canUserRead(par.user, par.targets)){
            return new ArrayList<>();
          }
        }
        
        if(!par.discs.isEmpty()){
          
          if(!SSServCallerU.canUserRead(par.user, par.discs)){
            return new ArrayList<>();
          }
        }
        
        if(
          par.forUser != null &&
          !SSStrU.equals(par.user,  par.forUser)){
          return new ArrayList<>();
        }
      }
      
      final List<SSEntity> discs     = new ArrayList<>();
      final SSDiscGetPar   discGetPar =
        new SSDiscGetPar(
          par.user,
          null,
          par.setEntries,
          par.withUserRestriction,
          par.invokeEntityHandlers);
      
      discGetPar.setEntries           = par.setEntries;
      discGetPar.setLikes             = par.setLikes;
      discGetPar.setCircleTypes       = par.setCircleTypes;
      discGetPar.setComments          = par.setComments;
      discGetPar.setTags              = par.setTags;
      discGetPar.setAttachedEntities  = par.setAttachedEntities;
      
      if(!par.targets.isEmpty()){
        
        par.discs.clear();
        
        for(SSUri targetURI : par.targets){
          
          SSUri.addDistinctWithoutNull(
            par.discs,
            sqlFct.getDiscURIsForTarget(
              par.forUser, 
              targetURI));
        }
      }else{
        
        if(par.discs.isEmpty()){
          par.discs.addAll(sqlFct.getDiscURIs(par.forUser));
        }
      }
      
      for(SSUri discURI : par.discs){
        
        discGetPar.disc = discURI;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          discs,
          discGet(discGetPar));
      } 
       
      return discs;

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
      dbSQL.startTrans(par.shouldCommit);
      
      switch(
        circleServ.circleMostOpenCircleTypeGet(
          new SSCircleMostOpenCircleTypeGetPar(
            par.user,
            par.disc,
            false))){
        
        case priv:{
          
          if(!SSServCallerU.canUserAll(par.user, par.disc)){
            return null;
          }
          
          sqlFct.deleteDisc(par.disc);
          break;
        }
        
        case group:
        case pub:{
          
          if(!SSServCallerU.canUserRead(par.user, par.disc)){
            return null;
          }
          
          sqlFct.unlinkDisc(par.user, par.disc);
          break;
        }
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
  public void discTargetsAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSDiscTargetsAddPar par = (SSDiscTargetsAddPar) parA.getFromJSON(SSDiscTargetsAddPar.class);

    sSCon.writeRetFullToClient(SSDiscTargetsAddRet.get(discTargetsAdd(par)));
  }
  
  @Override
  public SSUri discTargetsAdd(final SSDiscTargetsAddPar par) throws Exception {
    
    try{
      
      if(par.withUserRestriction){
        
        if(
          !SSServCallerU.canUserRead(par.user, par.discussion)){
          return null;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri targetURI :  par.targets){
        
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            targetURI,
            null, //type,
            null, //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      }
        
      sqlFct.addDiscTargets(par.discussion, par.targets);
      
      SSServCallerU.handleCirclesFromEntityGetEntitiesAdd(
        par.user,
        par.discussion,
        par.targets, //entities
        par.withUserRestriction);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.discussion;
    }catch(Exception error){

      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){

        if(dbSQL.rollBack(par.shouldCommit)){

          SSServErrReg.reset();

          return discTargetsAdd(par);
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
  
  private List<SSUri> getDiscAffiliatedURIs(
    final SSUri        discUri) throws Exception{

    try{

      final List<SSUri>  discContentUris = new ArrayList<>();
      final SSDisc       disc            = sqlFct.getDisc(discUri, true);

      discContentUris.add   (discUri);
      discContentUris.addAll(SSUri.getDistinctNotNullFromEntities(disc.targets));
      
      for(SSEntity entry : disc.entries){
        discContentUris.add(entry.id);
      }
      
      return discContentUris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private void attachEntities(
    final SSUri         user,
    final SSUri         entity,
    final List<SSUri>   entitiesToAttach,
    final List<SSLabel> entityLabels,
    final Boolean       withUserRestriction) throws Exception{
    
    if(entitiesToAttach.isEmpty()){
      return;
    }
    
    try{
      
      entityServ.entityEntitiesAttach(
        new SSEntityAttachEntitiesPar(
          user,
          entity,
          entitiesToAttach,
          withUserRestriction,
          false));
      
      if(
        entityLabels.isEmpty() ||
        entitiesToAttach.size() != entityLabels.size()){
        return;
      }
      
      for(Integer counter = 0; counter < entitiesToAttach.size(); counter++){
        
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            user,
            entitiesToAttach.get(counter), //entity
            null, //type
            entityLabels.get(counter), //label,
            null, //description,
            null, //creationTime
            null, //read,
            false, //setPublic
            withUserRestriction, //withUserRestriction
            false)); //shouldCommit
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
