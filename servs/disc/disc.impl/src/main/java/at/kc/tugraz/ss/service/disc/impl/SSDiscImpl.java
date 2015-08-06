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
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAddFromClientPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryURIsGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscRemovePar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscRemoveRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscGetRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscsGetRet;
import at.kc.tugraz.ss.service.disc.impl.fct.activity.SSDiscActivityFct;
import at.kc.tugraz.ss.service.disc.impl.fct.misc.SSDiscMiscFct;
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
import at.tugraz.sss.serv.SSPushEntitiesToUsersI;
import at.tugraz.sss.serv.SSPushEntitiesToUsersPar;
import at.tugraz.sss.serv.SSServContainerI;
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
              entity.id, //target
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
              null,
              null,
              userUri,
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
            
            affiliatedURIs.clear();
            
            for(SSUri discContentURI : SSDiscMiscFct.getDiscContentURIs(sqlFct, entityAdded.id)){
            
              if(SSStrU.contains(par.recursiveEntities, discContentURI)){
                continue;
              }
              
              SSUri.addDistinctWithoutNull(
                affiliatedURIs,
                discContentURI);
            }
            
            SSEntity.addEntitiesDistinctWithoutNull(
              affiliatedEntities,
              entityServ.entitiesGet(
                new SSEntitiesGetPar(
                  null,
                  null,
                  par.user,
                  affiliatedURIs,
                  null, //types,
                  null, //descPar
                  par.withUserRestriction)));
            
            circleServ.circleEntitiesAdd(
              new SSCircleEntitiesAddPar(
                null,
                null,
                par.user,
                par.circle,
                affiliatedURIs,
                par.withUserRestriction, //withUserRestriction
                false)); //shouldCommit
            
            break;
          }
        }
      }
      
      if(affiliatedEntities.isEmpty()){
        return affiliatedEntities;
      }
      
      par.entities.clear();
      par.entities.addAll(affiliatedEntities);
      
      for(SSServContainerI serv : SSServReg.inst.getServsHandlingAddAffiliatedEntitiesToCircle()){
        ((SSAddAffiliatedEntitiesToCircleI) serv.serv()).addAffiliatedEntitiesToCircle(par);
      }
      
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
            null,
            null,
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

        if(par.entity != null){
      
          entityServ.entityUpdate(
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
              true, //withUserRestriction
              false)); //shouldCommit)
        }
      }

      if(!par.addNewDisc){
        discEntryAddFct.checkWhetherUserCanAddDiscEntry(par);
      }

      dbSQL.startTrans(par.shouldCommit);

      if(par.addNewDisc){

        par.disc = SSServCaller.vocURICreate();

        discEntryAddFct.addDisc(
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
          discEntryAddFct.addDiscEntry(
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
  public void discGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSDiscGetPar par = (SSDiscGetPar) parA.getFromJSON(SSDiscGetPar.class);

    sSCon.writeRetFullToClient(SSDiscGetRet.get(discGet(par)));
  }

  @Override
  public SSDisc discGet(final SSDiscGetPar par) throws Exception{

    try{
      final List<SSDiscEntry>    discEntryEntities = new ArrayList<>();
      SSDiscEntry                discEntry;
      SSEntityDescriberPar       descPar;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(par.disc);
        
        descPar.setCircleTypes = par.setCircleTypes;
        descPar.setLikes       = par.setLikes;
        descPar.setComments    = par.setComments;
      }else{
        descPar = null;
      }
      
      final SSDisc               disc =
        SSDisc.get(
          sqlFct.getDisc(par.disc, par.setEntries),
          entityServ.entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              par.disc,
              par.withUserRestriction, //withUserRestriction,
              descPar)));
      
      if(par.invokeEntityHandlers){
        descPar                 = new SSEntityDescriberPar(null);
        descPar.setCircleTypes  = par.setCircleTypes;
        descPar.setLikes        = par.setLikes;
        descPar.setComments     = par.setComments;
      }else{
        descPar = null;
      }
        
      for(SSEntity entry : disc.entries){
        
        discEntry =
          SSDiscEntry.get(
            (SSDiscEntry) entry,
            entityServ.entityGet(
              new SSEntityGetPar(
                null,
                null,
                par.user,
                ((SSDiscEntry) entry).id,
                par.withUserRestriction, //withUserRestriction,
                descPar)));
        
        discEntryEntities.add(discEntry);
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
        
        if(par.target != null){
          
          if(!SSServCallerU.canUserRead(par.user, par.target)){
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
      
      discGetPar.setEntries      = par.setEntries;
      discGetPar.setLikes        = par.setLikes;
      discGetPar.setCircleTypes  = par.setCircleTypes;
      discGetPar.setComments     = par.setComments;
      
      if(par.target != null){
      
        par.discs.clear();
        
        par.discs.addAll(sqlFct.getDiscURIsForTarget(par.forUser, par.target));
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
      SSServCallerU.canUserAllEntity(par.user, par.disc);
      
      dbSQL.startTrans(par.shouldCommit);
      
      switch(circleServ.circleMostOpenCircleTypeGet(
        new SSCircleMostOpenCircleTypeGetPar(
          null,
          null,
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

  private void attachEntities(
    final SSUri         user,
    final SSUri         entity,
    final List<SSUri>   entitiesToAttach,
    final List<SSLabel> entityLabels) throws Exception{
    
    if(entitiesToAttach.isEmpty()){
      return;
    }
    
    try{
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          user,
          entity,
          null, //type
          null, //label,
          null, //description,
          entitiesToAttach,  //entitiesToAttach
          null, //creationTime
          null, //read,
          false, //setPublic
          true, //withUserRestriction
          false)); //shouldCommit
      
      if(
        entityLabels.isEmpty() ||
        entitiesToAttach.size() != entityLabels.size()){
        return;
      }
      
      for(Integer counter = 0; counter < entitiesToAttach.size(); counter++){
        
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            user,
            entitiesToAttach.get(counter), //entity
            null, //type
            entityLabels.get(counter), //label,
            null, //description,
            null, //entitiesToAttach
            null, //creationTime
            null, //read,
            false, //setPublic
            false, //withUserRestriction
            false)); //shouldCommit
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
