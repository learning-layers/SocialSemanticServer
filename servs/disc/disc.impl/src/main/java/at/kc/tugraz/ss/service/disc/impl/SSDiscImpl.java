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

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.tugraz.sss.serv.datatype.par.SSCircleAddEntitiesToCircleOfEntityPar;
import at.tugraz.sss.serv.datatype.par.SSCircleEntitiesAddPar;
import at.tugraz.sss.serv.datatype.par.SSEntitySharePar;
import at.tugraz.sss.serv.impl.api.SSEntityServerI;
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.tugraz.sss.serv.datatype.par.SSEntitiesGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAddPar;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.kc.tugraz.ss.service.disc.api.*;
import at.kc.tugraz.ss.service.disc.datatypes.*;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAcceptPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAddFromClientPar;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.conf.SSConfA;
import at.tugraz.sss.serv.impl.api.SSUserRelationGathererI;
import at.tugraz.sss.serv.impl.api.SSUsersResourcesGathererI;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryUpdatePar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscRemovePar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscTargetsAddPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUpdatePar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryAcceptRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryUpdateRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscRemoveRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscGetRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscTargetsAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUpdateRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscsGetRet;
import at.kc.tugraz.ss.service.disc.impl.fct.activity.SSDiscActAndLogFct;
import at.kc.tugraz.ss.service.disc.impl.fct.op.SSDiscUserEntryAddFct;
import at.tugraz.sss.serv.impl.api.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.datatype.par.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.datatype.par.SSCircleIsEntityPrivatePar;
import at.tugraz.sss.serv.datatype.par.SSCirclesGetPar;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.impl.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.par.SSEntityAttachEntitiesPar;
import at.tugraz.sss.serv.datatype.SSEntityContext;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.par.SSEntityEntitiesAttachedRemovePar;
import at.tugraz.sss.serv.datatype.SSErr;
import java.util.*;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.impl.api.SSGetParentEntitiesI;
import at.tugraz.sss.serv.impl.api.SSGetSubEntitiesI;
import at.tugraz.sss.serv.impl.api.SSPushEntitiesToUsersI;
import at.tugraz.sss.serv.datatype.par.SSPushEntitiesToUsersPar;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI;
import sss.serv.eval.api.SSEvalServerI;

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
  
  private final SSDiscSQLFct       sql;
  private final SSEntityServerI    entityServ;
  private final SSEntityServerI    circleServ;
  private final SSDiscActAndLogFct actAndLogFct;
  private final SSUserCommons   userCommons;
  
  public SSDiscImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql          = new SSDiscSQLFct(this, SSVocConf.systemUserUri);
    this.entityServ   = (SSEntityServerI)   SSServReg.getServ(SSEntityServerI.class);
    this.circleServ   = (SSEntityServerI)   SSServReg.getServ(SSEntityServerI.class);
    
    this.actAndLogFct =
      new SSDiscActAndLogFct(
        (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class),
        (SSEvalServerI)     SSServReg.getServ(SSEvalServerI.class));
    
    this.userCommons = new SSUserCommons();
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(par.setDiscs){
        
        switch(entity.type){
          
          case user:{
            
            entity.discs.addAll(
              discsGet(
                new SSDiscsGetPar(
                  par.user,
                  true, //setEntries
                  entity.id, //forUser
                  null, //discs
                  null, //targets
                  par.withUserRestriction,
                  false))); //invokeEntityHandlers
            
            break;
          }
          
          default:{
            
            entity.discs.addAll(
              discsGet(
                new SSDiscsGetPar(
                  par.user,
                  true, //setEntries
                  null, //forUser
                  null, //discs
                  SSUri.asListNotNull(entity.id), //targets
                  par.withUserRestriction,
                  false))); //invokeEntityHandlers
            
            break;
          }
        }
      }
      
      switch(entity.type){
        
        case disc:
        case qa:
        case chat: {
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          return SSDisc.get(
            discGet(
              new SSDiscGetPar(
                par.user,
                entity.id,
                true, //setEntries
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
    final List<String> allUsers,
    final Map<String, List<SSUri>> userRelations) throws SSErr{
    
    try{
      SSCirclesGetPar circlesGetPar;
      
      SSUri userUri;
      
      for(String user : allUsers){
        
        userUri = SSUri.get(user);
        
        circlesGetPar =
          new SSCirclesGetPar(
            userUri,
            userUri, //forUser
            null, //entity
            null, //entityTypesToIncludeOnly
            false, //setEntities,
            true, //setUsers
            false, //withUserRestriction
            true, //withSystemCircles
            false); //invokeEntityHandlers
        
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
          
          circlesGetPar.entity = disc.id;
          
          for(SSEntity circle : circleServ.circlesGet(circlesGetPar)){
            
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
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void getUsersResources(
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      final SSDiscsGetPar discsGetPar =
        new SSDiscsGetPar(
          null,
          false, //setEntries,
          null, //forUser,
          null, //discs,
          null, //target,
          true, //withUserRestriction,
          false); //invokeEntityHandlers
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        discsGetPar.user    = userID;
        discsGetPar.forUser = userID;
        
        for(SSEntity disc : discsGet(discsGetPar)){
          
          usersEntities.get(user).add(
            new SSEntityContext(
              disc.id,
              SSEntityE.disc,
              null,
              null));
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public List<SSUri> getSubEntities(
    final SSUri user,
    final SSUri entity,
    final SSEntityE type) throws SSErr{
    
    try{
      
      switch(type){
        case disc:
        case qa:
        case chat: {
          return sql.getDiscEntryURIs(entity);
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
    final SSEntityE type) throws SSErr{
    
    switch(type){
      
      case discEntry:
      case qaEntry:
      case chatEntry: {
        
        try{
          final List<SSUri>  userDiscUris = sql.getDiscURIs(user);
          final List<String> discUris     = new ArrayList<>();
          
          discUris.add(SSStrU.toStr(sql.getDiscForEntry(entity)));
          
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
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSAddAffiliatedEntitiesToCirclePar par) throws SSErr{
    
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
        SSServReg.inst.addAffiliatedEntitiesToCircle(
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
    final SSPushEntitiesToUsersPar par) throws SSErr {
    
    try{
      for(SSEntity entityToPush : par.entities){
        
        switch(entityToPush.type){
          case qa:
          case disc:
          case chat: {
            
            for(SSUri userToPushTo : par.users){
              
              if(sql.ownsUserDisc(userToPushTo, entityToPush.id)){
                continue;
              }
              
              sql.addDisc(entityToPush.id, userToPushTo);
            }
            
            break;
          }
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSServRetI discEntryAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
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
              par.withUserRestriction, //withUserRestriction,
              par.shouldCommit)); //shouldCommit
      }
      
      actAndLogFct.discEntryAdd(
        par.user,
        par.addNewDisc,
        par.targets,
        ret.disc,
        par.type,
        par.description,
        ret.entry,
        par.entry,
        par.entities,
        par.entityLabels,
        par.shouldCommit);
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSDiscEntryAddRet discEntryAdd(final SSDiscEntryAddPar par) throws SSErr{
    
    try{
      SSUri discEntryUri = null;
      
      final SSDiscUserEntryAddFct discEntryAddFct = new SSDiscUserEntryAddFct(circleServ, entityServ);
      
      if(par.addNewDisc){
        
        if(SSObjU.isNull(par.label, par.type)){
          throw SSErr.get(SSErrE.parameterMissing);
        }
        
        switch(par.type){
          case disc:
          case qa:
          case chat: break;
          default: throw new Exception("disc type not valid");
        }
      }
      
      if(!par.addNewDisc){
        
        if(SSObjU.isNull(par.entry)){
          throw SSErr.get(SSErrE.parameterMissing);
        }
        
        final SSEntity disc =
          sql.getEntityTest(
            par.user,
            par.disc,
            par.withUserRestriction);
        
        if(disc == null){
          return SSDiscEntryAddRet.get(null, null);
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(par.addNewDisc){
        
        par.disc =
          discEntryAddFct.addDisc(
            sql,
            par.user,
            par.type,
            par.label,
            par.description,
            par.withUserRestriction);
        
        if(par.disc == null){
          dbSQL.rollBack(par.shouldCommit);
          return SSDiscEntryAddRet.get(null, null);
        }
        
        discTargetsAdd(
          new SSDiscTargetsAddPar(
            par.user,
            par.disc,
            par.targets,
            par.withUserRestriction,
            false));
        
        //TODO move to client call
        par.disc =
          attachEntities(
            par.user,
            par.disc,
            par.entities,
            par.entityLabels,
            par.withUserRestriction);
        
        if(par.disc == null){
          dbSQL.rollBack(par.shouldCommit);
          return SSDiscEntryAddRet.get(null, null);
        }
      }
      
      if(par.entry != null){
        
        discEntryUri =
          discEntryAddFct.addDiscEntry(
            sql,
            par.user,
            par.disc,
            par.entry,
            par.withUserRestriction);
        
        if(discEntryUri == null){
          dbSQL.rollBack(par.shouldCommit);
          return SSDiscEntryAddRet.get(null, null);
        }
        
        //TODO move to client call
        discEntryUri =
          attachEntities(
            par.user,
            discEntryUri,
            par.entities,
            par.entityLabels,
            par.withUserRestriction);
        
        if(discEntryUri == null){
          dbSQL.rollBack(par.shouldCommit);
          return SSDiscEntryAddRet.get(null, null);
        }
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
  public SSServRetI discUpdate(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSDiscUpdatePar par = (SSDiscUpdatePar) parA.getFromJSON(SSDiscUpdatePar.class);
      final SSDiscUpdateRet ret = discUpdate(par);
      
      if(ret.disc != null){
        
        actAndLogFct.discUpdate(
          par.user,
          par.disc,
          par.label,
          par.content,
          par.shouldCommit);
      }
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSDiscUpdateRet discUpdate(final SSDiscUpdatePar par) throws SSErr{
    
    try{
      
      Boolean isAuthor = true;
      
      if(par.withUserRestriction){
        
        isAuthor = sql.isUserAuthor(par.user, par.disc, par.withUserRestriction);
        
        if(!isAuthor){
          par.label   = null;
          par.content = null;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      par.disc =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.disc,
            null, //type
            par.label,
            par.content,
            null, //creationTime
            par.read, //read
            false, //setPublic
            false, //createIfNotExists
            true, //withUserRestriction
            false)); //shouldCommit
      
      if(par.disc == null){
        dbSQL.rollBack(par.shouldCommit);
        return SSDiscUpdateRet.get(null);
      }
      
      if(
        par.read != null &&
        par.read){
        
        final SSEntityUpdatePar entityUpdatePar =
          new SSEntityUpdatePar(
            par.user,
            null, //entity
            null, //type
            null, //label)
            null, //description,
            null, //creationTime,
            par.read, //read
            false, //setPublic,
            false, //createIfNotExists,
            par.withUserRestriction,
            false); //shouldCommit)
        
        for(SSUri entry : sql.getDiscEntryURIs(par.disc)){
          
          entityUpdatePar.entity = entry;
          
          entityServ.entityUpdate(entityUpdatePar);
        }
      }
      
      if(isAuthor){
        
        par.disc =
          attachEntities(
            par.user,
            par.disc,
            par.entitiesToAttach,
            par.entityLabels,
            par.withUserRestriction);
        
        if(par.disc == null){
          dbSQL.rollBack(par.shouldCommit);
          return SSDiscUpdateRet.get(null);
        }
        
        par.disc =
          removeAttachedEntities(
            par.user,
            par.disc,
            par.entitiesToRemove,
            par.withUserRestriction);
        
        if(par.disc == null){
          dbSQL.rollBack(par.shouldCommit);
          return SSDiscUpdateRet.get(null);
        }
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return SSDiscUpdateRet.get(par.disc);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return discUpdate(par);
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
  public SSServRetI discEntryUpdate(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSDiscEntryUpdatePar par = (SSDiscEntryUpdatePar) parA.getFromJSON(SSDiscEntryUpdatePar.class);
      final SSDiscEntryUpdateRet ret = discEntryUpdate(par);
      
      if(ret.entry != null){
        
        actAndLogFct.discEntryUpdate(
          par.user,
          ret.entry,
          par.content,
          par.shouldCommit);
      }
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSDiscEntryUpdateRet discEntryUpdate(final SSDiscEntryUpdatePar par) throws SSErr{
    
    try{
      
      if(par.withUserRestriction){
        
        if(!sql.isUserAuthor(par.user, par.entry, par.withUserRestriction)){
          return SSDiscEntryUpdateRet.get(null, null);
        }
      }
      
      final SSUri discURI = sql.getDiscForEntry(par.entry);
      
      if(discURI == null){
        return SSDiscEntryUpdateRet.get(null, null);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(par.content != null){
        sql.updateEntryContent(par.entry, par.content);
      }
      
      par.entry =
        attachEntities(
          par.user,
          par.entry,
          par.entitiesToAttach,
          par.entityLabels,
          par.withUserRestriction);
      
      if(par.entry == null){
        dbSQL.rollBack(par.shouldCommit);
        return SSDiscEntryUpdateRet.get(null, null);
      }
      
      par.entry =
        removeAttachedEntities(
          par.user,
          par.entry,
          par.entitiesToRemove,
          par.withUserRestriction);
      
      if(par.entry == null){
        dbSQL.rollBack(par.shouldCommit);
        return SSDiscEntryUpdateRet.get(null, null);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return SSDiscEntryUpdateRet.get(
        discURI,
        par.entry);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return discEntryUpdate(par);
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
  public SSServRetI discEntryAccept(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSDiscEntryAcceptPar par = (SSDiscEntryAcceptPar) parA.getFromJSON(SSDiscEntryAcceptPar.class);
      
      return SSDiscEntryAcceptRet.get(discEntryAccept(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri discEntryAccept(final SSDiscEntryAcceptPar par) throws SSErr{
    
    try{
      
      final SSUri discURI = sql.getDiscForEntry(par.entry);
      
      if(discURI == null){
        return null;
      }
      
      if(par.withUserRestriction){
        
        final SSEntity entry =
          sql.getEntityTest(
            par.user,
            par.entry,
            par.withUserRestriction);
        
        if(entry == null){
          return null;
        }
        
        if(!sql.isUserAuthor(par.user, discURI, par.withUserRestriction)){
          return null;
        }
      }
      
      final SSDisc disc =
        discGet(
          new SSDiscGetPar(
            par.user,
            discURI,
            true, //setEntries
            par.withUserRestriction,
            false));
      
      for(SSEntity entry : disc.entries){
        if(((SSDiscEntry)entry).accepted){
          return null;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sql.acceptEntry(par.entry);
      
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
  public SSServRetI discGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSDiscGetPar par = (SSDiscGetPar) parA.getFromJSON(SSDiscGetPar.class);
      
      return SSDiscGetRet.get(discGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSDisc discGet(final SSDiscGetPar par) throws SSErr{
    
    try{
      
      SSDisc disc = sql.getDisc(par.disc, par.setEntries);
      
      if(disc == null){
        return null;
      }
      
      SSEntityDescriberPar       descPar;
      
      if(par.invokeEntityHandlers){
        
        descPar = new SSEntityDescriberPar(par.disc);
        
        descPar.setCircleTypes       = par.setCircleTypes;
        descPar.setLikes             = par.setLikes;
        descPar.setComments          = par.setComments;
        descPar.setTags              = par.setTags;
        descPar.setAttachedEntities  = par.setAttachedEntities;
        descPar.setRead              = par.setReads;
        
      }else{
        descPar = null;
      }
      
      final SSEntity discEntity =
        entityServ.entityGet(
          new SSEntityGetPar(
            par.user,
            par.disc,
            par.withUserRestriction, //withUserRestriction,
            descPar));
      
      if(discEntity == null){
        return null;
      }
      
      disc = SSDisc.get(disc, discEntity);
      
      if(par.invokeEntityHandlers){
        descPar                 = new SSEntityDescriberPar(null);
        descPar.setTags         = par.setTags;
      }else{
        descPar = null;
      }
      
      final List<SSEntity>  discTargets  = new ArrayList<>();
      SSEntityGetPar        entityGetPar =
        new SSEntityGetPar(
          par.user,
          null, //entity,
          par.withUserRestriction, //withUserRestriction,
          descPar);
      
      for(SSEntity target : disc.targets){
        
        entityGetPar.entity = target.id;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          discTargets,
          entityServ.entityGet(entityGetPar));
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
        descPar.setRead              = par.setReads;
      }else{
        descPar = null;
      }
      
      final List<SSEntity> discEntryEntities = new ArrayList<>();
      
      entityGetPar =
        new SSEntityGetPar(
          par.user,
          null, //entity
          par.withUserRestriction, //withUserRestriction,
          descPar);
      
      for(SSEntity entry : disc.entries){
        
        entityGetPar.entity = ((SSDiscEntry) entry).id;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          discEntryEntities,
          SSDiscEntry.get(
            (SSDiscEntry) entry,
            entityServ.entityGet(entityGetPar)));
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
  public SSServRetI discsGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSDiscsGetPar par = (SSDiscsGetPar) parA.getFromJSON(SSDiscsGetPar.class);
      
      return SSDiscsGetRet.get(discsGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> discsGet(final SSDiscsGetPar par) throws SSErr{
    
    try{
      
      if(par.user == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        if(!par.targets.isEmpty()){
          
          SSEntity targetEntity;
          
          for(SSUri target : par.targets){
            
            targetEntity =
              sql.getEntityTest(
                par.user,
                target,
                par.withUserRestriction);
            
            if(targetEntity == null){
              return new ArrayList<>();
            }
          }
        }
        
        if(!par.discs.isEmpty()){
          
          SSEntity discEntity;
          
          for(SSUri disc : par.discs){
            
            discEntity =
              sql.getEntityTest(
                par.user,
                disc,
                par.withUserRestriction);
            
            if(discEntity == null){
              return new ArrayList<>();
            }
          }
        }
      }
      
      final List<SSEntity> discs      = new ArrayList<>();
      final SSDiscGetPar   discGetPar =
        new SSDiscGetPar(
          par.user,
          null, //disc
          par.setEntries,
          par.withUserRestriction,
          par.invokeEntityHandlers);
      
      discGetPar.setEntries           = par.setEntries;
      discGetPar.setLikes             = par.setLikes;
      discGetPar.setCircleTypes       = par.setCircleTypes;
      discGetPar.setComments          = par.setComments;
      discGetPar.setTags              = par.setTags;
      discGetPar.setAttachedEntities  = par.setAttachedEntities;
      discGetPar.setReads             = par.setReads;
      
      if(!par.targets.isEmpty()){
        
        par.discs.clear();
        
        for(SSUri target : par.targets){
          
          SSUri.addDistinctWithoutNull(
            par.discs,
            sql.getDiscURIsForTarget(
              par.forUser,
              target));
        }
      }else{
        
        if(par.discs.isEmpty()){
          par.discs.addAll(sql.getDiscURIs(par.forUser));
        }
      }
      
      for(SSUri disc : par.discs){
        
        discGetPar.disc = disc;
        
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
  public SSServRetI discRemove(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSDiscRemovePar par = (SSDiscRemovePar) parA.getFromJSON(SSDiscRemovePar.class);
      
      return SSDiscRemoveRet.get(discRemove(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
//    SSDiscActivityFct.removeDisc(new SSDiscUserRemovePar(parA));
  }
  
  @Override
  public SSUri discRemove(final SSDiscRemovePar par) throws SSErr{
    
    try{
      
      final SSEntity disc =
        sql.getEntityTest(
          par.user,
          par.disc,
          par.withUserRestriction);
      
      if(disc == null){
        return null;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(circleServ.circleIsEntityPrivate(
        new SSCircleIsEntityPrivatePar(
          par.user,
          par.disc))){
        
        sql.deleteDisc(par.disc);
      }else{
        sql.unlinkDisc(par.user, par.disc);
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
  public SSServRetI discTargetsAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSDiscTargetsAddPar par        = (SSDiscTargetsAddPar) parA.getFromJSON(SSDiscTargetsAddPar.class);
      final SSUri               discussion = discTargetsAdd(par);
      final SSDiscTargetsAddRet ret        = SSDiscTargetsAddRet.get(discussion);
      
      actAndLogFct.discTargetsAdd(
        par.user,
        discussion,
        par.targets,
        par.shouldCommit);
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri discTargetsAdd(final SSDiscTargetsAddPar par) throws SSErr {
    
    try{
      
      if(par.targets.isEmpty()){
        return null;
      }
      
      final SSEntity disc =
        sql.getEntityTest(
          par.user,
          par.discussion,
          par.withUserRestriction);
      
      if(disc == null){
        return null;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSUri target;
      
      for(SSUri targetURI : par.targets){
        
        target =
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
              true, //creatIfNotExists
              par.withUserRestriction,
              false)); //shouldCommit)
        
        if(target == null){
          dbSQL.rollBack(par.shouldCommit);
          return null;
        }
      }
      
      sql.addDiscTargets(par.discussion, par.targets);
            
      circleServ.circleAddEntitiesToCirclesOfEntity(
        new SSCircleAddEntitiesToCircleOfEntityPar(
          par.user,
          par.discussion, //entity
          par.targets, //entities
          par.withUserRestriction,
          true, //invokeEntityHandlers,
          false)); //shouldCommit
      
      for(SSUri targetURI : par.targets){
        
        circleServ.circleAddEntitiesToCirclesOfEntity(
          new SSCircleAddEntitiesToCircleOfEntityPar(
            par.user,
            targetURI, //entity
            SSUri.asListNotNull(par.discussion), //entities
            par.withUserRestriction,
            true, //invokeEntityHandlers,
            false)); //shouldCommit
      }
      
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
    final SSUri        discUri) throws SSErr{
    
    try{
      
      final List<SSUri>  discContentUris = new ArrayList<>();
      final SSDisc       disc            = sql.getDisc(discUri, true);
      
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
  
  private SSUri attachEntities(
    final SSUri         user,
    final SSUri         entity,
    final List<SSUri>   entitiesToAttach,
    final List<SSLabel> entityLabels,
    final Boolean       withUserRestriction) throws SSErr{
    
    try{
      
      if(entitiesToAttach.isEmpty()){
        return entity;
      }
      
      final SSUri entityURI =
        entityServ.entityEntitiesAttach(
          new SSEntityAttachEntitiesPar(
            user,
            entity,
            entitiesToAttach,
            withUserRestriction,
            false));
      
      if(entityURI == null){
        return null;
      }
      
      if(
        entityLabels.isEmpty() ||
        entitiesToAttach.size() != entityLabels.size()){
        return entity;
      }
      
      SSUri attachedEntity;
      
      for(Integer counter = 0; counter < entitiesToAttach.size(); counter++){
        
        attachedEntity =
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
              true, //createIfNotExists
              withUserRestriction, //withUserRestriction
              false)); //shouldCommit
        
        if(attachedEntity == null){
          return null;
        }
      }
      
      return entityURI;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private SSUri removeAttachedEntities(
    final SSUri       user,
    final SSUri       entity,
    final List<SSUri> entitiesToRemove,
    final Boolean     withUserRestriction) throws SSErr{
    
    try{
      
      if(entitiesToRemove.isEmpty()){
        return entity;
      }
      
      final SSUri entityURI =
        entityServ.entityEntitiesAttachedRemove(
          new SSEntityEntitiesAttachedRemovePar(
            user,
            entity,
            entitiesToRemove,
            withUserRestriction,
            false));
      
      return entityURI;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}