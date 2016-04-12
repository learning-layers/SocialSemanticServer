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
package at.tugraz.sss.servs.disc.impl;

import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.util.SSObjU;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.activity.api.SSActivityServerI;
import at.tugraz.sss.servs.activity.datatype.SSActivity;
import at.tugraz.sss.servs.activity.datatype.SSActivityE;
import at.tugraz.sss.servs.activity.datatype.SSActivitiesGetPar;
import at.tugraz.sss.servs.entity.datatype.SSCircleAddEntitiesToCircleOfEntityPar;
import at.tugraz.sss.servs.entity.datatype.SSCircleEntitiesAddPar;
import at.tugraz.sss.servs.entity.datatype.SSEntitySharePar;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.entity.datatype.SSEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityUpdatePar;
import at.tugraz.sss.servs.disc.datatype.SSDiscsGetPar;
import at.tugraz.sss.servs.disc.datatype.SSDiscGetPar;
import at.tugraz.sss.servs.disc.datatype.SSDiscEntryAddPar;
import at.tugraz.sss.servs.disc.api.*;
import at.tugraz.sss.servs.disc.datatype.SSDiscDailySummaryGetPar;
import at.tugraz.sss.servs.disc.datatype.SSDiscEntryAcceptPar;
import at.tugraz.sss.servs.disc.datatype.SSDiscEntryAddFromClientPar;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.disc.datatype.*;
import at.tugraz.sss.servs.common.api.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.servs.common.datatype.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.servs.entity.datatype.SSCircleIsEntityPrivatePar;
import at.tugraz.sss.servs.entity.datatype.SSCirclesGetPar;
import at.tugraz.sss.servs.entity.datatype.SSClientE;
import at.tugraz.sss.servs.common.api.SSDescribeEntityI;
import at.tugraz.sss.servs.entity.datatype.SSEntityAttachEntitiesPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityContext;
import at.tugraz.sss.servs.common.datatype.SSEntityDescriberPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityEntitiesAttachedRemovePar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import java.util.*;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.common.api.SSGetParentEntitiesI;
import at.tugraz.sss.servs.common.api.SSGetSubEntitiesI;
import at.tugraz.sss.servs.common.api.SSPushEntitiesToUsersI;
import at.tugraz.sss.servs.common.datatype.SSPushEntitiesToUsersPar;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import at.tugraz.sss.servs.entity.datatype.SSServRetI;
import at.tugraz.sss.servs.activity.impl.*;
import at.tugraz.sss.servs.entity.impl.SSEntityImpl;
import at.tugraz.sss.servs.common.api.SSGetUsersResourcesI;
import at.tugraz.sss.servs.common.api.SSGetUserRelationsI;
import at.tugraz.sss.servs.conf.*;

public class SSDiscImpl
extends SSEntityImpl
implements
  SSDiscClientI,
  SSDiscServerI,
  SSDescribeEntityI,
  SSAddAffiliatedEntitiesToCircleI,
  SSPushEntitiesToUsersI,
  SSGetSubEntitiesI,
  SSGetParentEntitiesI,
  SSGetUserRelationsI,
  SSGetUsersResourcesI{
  
  private final SSDiscActAndLog                       actAndLog       = new SSDiscActAndLog();
  private final SSDiscSummaryCommons                  summaryCommons  = new SSDiscSummaryCommons();
  private final SSDiscSQL                             sql             = new SSDiscSQL(dbSQL);
  
  public SSDiscImpl(){
    super(SSCoreConf.instGet().getDisc());
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar            servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(par.setDiscs){
        
        switch(entity.type){
          
          case user:{
            
            entity.discs.addAll(
              discsGet(
                new SSDiscsGetPar(
                  servPar, 
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
                  servPar, 
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
          
          if(SSStrU.isEqual(entity, par.recursiveEntity)){
            return entity;
          }
          
          return SSDisc.get(
            discGet(
              new SSDiscGetPar(
                servPar, 
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
    final SSServPar servPar, 
    final List<String> allUsers,
    final Map<String, List<SSUri>> userRelations) throws SSErr{
    
    try{
      SSCirclesGetPar       circlesGetPar;
      
      SSUri userUri;
      
      for(String user : allUsers){
        
        userUri = SSUri.get(user);
        
        circlesGetPar =
          new SSCirclesGetPar(
            servPar, 
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
              servPar, 
              userUri,
              false, //setEntries,
              userUri, //forUser,
              null, //discs,
              null, //target,
              false, //withUserRestriction,
              false))){ //invokeEntityHandlers);){
          
          circlesGetPar.entity = disc.id;
          
          for(SSEntity circle : circlesGet(circlesGetPar)){
            
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
    final SSServPar                          servPar,
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      for(String user : usersEntities.keySet()){
        
        for(SSEntity disc :
          discsGet(
            new SSDiscsGetPar(
              servPar,
              SSUri.get(user), //user
              true, //setEntries,
              SSUri.get(user), //forUser,
              null, //discs,
              null, //target,
              true, //withUserRestriction,
              false))){ //invokeEntityHandlers
        
          usersEntities.get(user).add(
            new SSEntityContext(
              disc.id,
              SSEntityE.disc,
              null,
              null));
          
          for(SSEntity target : ((SSDisc) disc).targets){
            
            usersEntities.get(user).add(
              new SSEntityContext(
                target.id,
                SSEntityE.disc,
                null,
                null));
          }
          
          for(SSEntity dicscEntry : disc.entries){
            
            usersEntities.get(user).add(
              new SSEntityContext(
                dicscEntry.id,
                SSEntityE.disc,
                null,
                null));
          }
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public List<SSUri> getSubEntities(
    final SSServPar servPar, 
    final SSUri user,
    final SSUri entity,
    final SSEntityE type) throws SSErr{
    
    try{
      
      switch(type){
        case disc:
        case qa:
        case chat: {
          return sql.getDiscEntryURIs(servPar, entity);
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
    final SSServPar servPar, 
    final SSUri     user,
    final SSUri     entity,
    final SSEntityE type) throws SSErr{
    
    switch(type){
      
      case discEntry:
      case qaEntry:
      case chatEntry: {
        
        try{
          final List<SSUri>  userDiscUris = sql.getDiscURIs(servPar, user);
          final List<String> discUris     = new ArrayList<>();
          
          discUris.add(SSStrU.toStr(sql.getDiscForEntry(servPar, entity)));
          
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
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSServPar servPar, final SSAddAffiliatedEntitiesToCirclePar par) throws SSErr{
    
    try{
      final List<SSUri>     affiliatedURIs     = new ArrayList<>();
      final List<SSEntity>  affiliatedEntities = new ArrayList<>();
      
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
            
            for(SSUri discContentURI : getDiscAffiliatedURIs(servPar, entityAdded.id)){
              
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
          entitiesGet(
            new SSEntitiesGetPar(
              servPar, 
              par.user,
              affiliatedURIs,
              null, //descPar
              par.withUserRestriction)));
      }
      
      for(SSEntity disc :
        discsGet(
          new SSDiscsGetPar(
            servPar, 
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
      
      circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          servPar, 
          par.user,
          par.circle,
          affiliatedURIs,
          false, //withUserRestriction
          false)); //shouldCommit
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities,
        addAffiliatedEntitiesToCircle.addAffiliatedEntitiesToCircle(
          servPar, 
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
    final SSServPar                servPar, 
    final SSPushEntitiesToUsersPar par) throws SSErr {
    
    try{
      for(SSEntity entityToPush : par.entities){
        
        switch(entityToPush.type){
          
          case qa:
          case disc:
          case chat: {
            
            for(SSUri userToPushTo : par.users){
              
              if(sql.ownsUserDisc(servPar, userToPushTo, entityToPush.id)){
                continue;
              }
              
              sql.addDisc(
                servPar, 
                entityToPush.id, //disc
                userToPushTo); //user
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
  public SSDiscDailySummaryGetRet discDailySummaryGet(final SSDiscDailySummaryGetPar par) throws SSErr{
    
    try{
      final SSActivityServerI           activityServ = new SSActivityImpl();
      final SSDiscDailySummaryGetRet    result       = new SSDiscDailySummaryGetRet();
      final List<SSActivityE>           actTypes     = new ArrayList<>();
      final List<SSEntity>              acts         = new ArrayList<>();
      
      actTypes.add(SSActivityE.addDiscEntry);
      actTypes.add(SSActivityE.discussEntity);
      
      acts.addAll(
        activityServ.activitiesGet(
          new SSActivitiesGetPar(
            par,
            par.user,
            null, //activities,
            actTypes, //types,
            null, //users,
            null, //entities,
            null, //circles,
            par.startTime, //startTime,
            null, //endTime,
            Integer.MAX_VALUE, //maxActivities
            false, //includeOnlyLastActivities,
            false, //withUserRestriction,
            false))); //invokeEntityHandlers
      
      SSActivity act;
      
      for(SSEntity actEntity : acts){
        
        act = (SSActivity) actEntity;
        
        switch(act.activityType){
          
          case addDiscEntry:{
            
            summaryCommons.addDiscEntry(
              this,
              par, 
              sql, 
              act, 
              result.summaries);
            break;
          }
          
          case discussEntity:{
            
            summaryCommons.discEntity(
              par, 
              sql, 
              act, 
              result.summaries);
            break;
          }
        }
      }

      return result;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI discEntryAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSDiscEntryAddFromClientPar par        = (SSDiscEntryAddFromClientPar) parA.getFromClient(clientType, parA, SSDiscEntryAddFromClientPar.class);
      final SSDiscEntryAddRet           ret        = discEntryAdd(par);
      
      if(par.addNewDisc){
        
        if(
          !par.users.isEmpty() ||
          !par.circles.isEmpty())
          
          entityShare(
            new SSEntitySharePar(
              par,
              par.user,
              ret.disc,
              par.users, //users
              par.circles, //circles
              false, //setPublic,
              null, //comment,
              par.withUserRestriction, //withUserRestriction,
              par.shouldCommit)); //shouldCommit
      }
      
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
            par, 
            SSConf.systemUserUri,
            par.user,
            par.disc,
            par.withUserRestriction);
        
        if(disc == null){
          return SSDiscEntryAddRet.get(null, null);
        }
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      if(par.addNewDisc){
        
        par.disc =
          addDisc(
            par,
            par.user,
            par.type,
            par.label,
            par.description,
            par.withUserRestriction);
        
        if(par.disc == null){
          dbSQL.rollBack(par, par.shouldCommit);
          return SSDiscEntryAddRet.get(null, null);
        }
        
        discTargetsAdd(
          new SSDiscTargetsAddPar(
            par, 
            par.user,
            par.disc,
            par.targets,
            par.withUserRestriction,
            false));
        
        //TODO move to client call
        par.disc =
          attachEntities(
            par,
            par.user,
            par.disc,
            par.entities,
            par.entityLabels,
            par.withUserRestriction);
        
        if(par.disc == null){
          dbSQL.rollBack(par, par.shouldCommit);
          return SSDiscEntryAddRet.get(null, null);
        }
      }
      
      if(par.entry != null){
        
        discEntryUri =
          addDiscEntry(
            par,
            par.user,
            par.disc,
            par.entry,
            par.withUserRestriction);
        
        if(discEntryUri == null){
          dbSQL.rollBack(par, par.shouldCommit);
          return SSDiscEntryAddRet.get(null, null);
        }
        
        //TODO move to client call
        discEntryUri =
          attachEntities(
            par,
            par.user,
            discEntryUri,
            par.entities,
            par.entityLabels,
            par.withUserRestriction);
        
        if(discEntryUri == null){
          dbSQL.rollBack(par, par.shouldCommit);
          return SSDiscEntryAddRet.get(null, null);
        }
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.addDiscEntry(
        par,  
        par.user,
        par.addNewDisc,
        par.disc,
        par.type,
        par.description,
        discEntryUri,
        par.entry,
        par.entityLabels,
        par.shouldCommit);
        
      return SSDiscEntryAddRet.get(
        par.disc,
        discEntryUri);
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI discUpdate(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSDiscUpdatePar par = (SSDiscUpdatePar) parA.getFromClient(clientType, parA, SSDiscUpdatePar.class);
      final SSDiscUpdateRet ret = discUpdate(par);
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSDiscUpdateRet discUpdate(final SSDiscUpdatePar par) throws SSErr{
    
    try{
      
      boolean               isAuthor   = true;
      SSEntityUpdatePar     entityUpdatePar;
      
      if(par.withUserRestriction){
        
        isAuthor = 
          sql.isUserAuthor(
            par, 
            par.user, 
            par.disc);
        
        if(!isAuthor){
          par.label   = null;
          par.content = null;
        }
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      entityUpdatePar =
        new SSEntityUpdatePar(
          par,
          par.user,
          par.disc,
          null, //type
          par.label,
          par.content, //description
          null, //creationTime
          null, //read
          false, //setPublic
          false, //createIfNotExists
          true, //withUserRestriction
          false); //shouldCommit
      
      par.disc = entityUpdate(entityUpdatePar);
      
      if(par.disc == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return SSDiscUpdateRet.get(null);
      }
      
      if(
        par.read != null &&
        par.read){
        
        entityUpdatePar =
          new SSEntityUpdatePar(
            par, 
            par.user,
            null, //entity
            null, //type
            null, //label
            null, //description,
            null, //creationTime,
            par.read, //read
            false, //setPublic,
            false, //createIfNotExists,
            par.withUserRestriction,
            false); //shouldCommit)
        
        for(SSUri entry : sql.getDiscEntryURIs(par, par.disc)){
          
          entityUpdatePar.entity = entry;
          
          entityUpdate(entityUpdatePar);
        }
      }
      
      if(isAuthor){
        
        par.disc =
          attachEntities(
            par, 
            par.user,
            par.disc,
            par.entitiesToAttach,
            par.entityLabels,
            par.withUserRestriction);
        
        if(par.disc == null){
          dbSQL.rollBack(par, par.shouldCommit);
          return SSDiscUpdateRet.get(null);
        }
        
        par.disc =
          removeAttachedEntities(
            par, 
            par.user,
            par.disc,
            par.entitiesToRemove,
            par.withUserRestriction);
        
        if(par.disc == null){
          dbSQL.rollBack(par, par.shouldCommit);
          return SSDiscUpdateRet.get(null);
        }
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.updateDisc(par, par.shouldCommit);
      
      return SSDiscUpdateRet.get(par.disc);
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI discEntryUpdate(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSDiscEntryUpdatePar par = (SSDiscEntryUpdatePar) parA.getFromClient(clientType, parA, SSDiscEntryUpdatePar.class);
      
      return discEntryUpdate(par);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSDiscEntryUpdateRet discEntryUpdate(final SSDiscEntryUpdatePar par) throws SSErr{
    
    try{
      
      if(par.withUserRestriction){
        
        if(!sql.isUserAuthor(par, par.user, par.entry)){
          return SSDiscEntryUpdateRet.get(null, null);
        }
      }
      
      final SSUri discURI = sql.getDiscForEntry(par, par.entry);
      
      if(discURI == null){
        return SSDiscEntryUpdateRet.get(null, null);
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      if(par.content != null){
        sql.updateEntryContent(par, par.entry, par.content);
      }
      
      par.entry =
        attachEntities(
          par, 
          par.user,
          par.entry,
          par.entitiesToAttach,
          par.entityLabels,
          par.withUserRestriction);
      
      if(par.entry == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return SSDiscEntryUpdateRet.get(null, null);
      }
      
      par.entry =
        removeAttachedEntities(
          par, 
          par.user,
          par.entry,
          par.entitiesToRemove,
          par.withUserRestriction);
      
      if(par.entry == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return SSDiscEntryUpdateRet.get(null, null);
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.updateDiscEntry(par, par.shouldCommit);
      
      return SSDiscEntryUpdateRet.get(
        discURI,
        par.entry);
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI discEntryAccept(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSDiscEntryAcceptPar par = (SSDiscEntryAcceptPar) parA.getFromClient(clientType, parA, SSDiscEntryAcceptPar.class);
      
      return SSDiscEntryAcceptRet.get(discEntryAccept(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri discEntryAccept(final SSDiscEntryAcceptPar par) throws SSErr{
    
    try{
      
      final SSUri discURI = sql.getDiscForEntry(par, par.entry);
      
      if(discURI == null){
        return null;
      }
      
      if(par.withUserRestriction){
        
        final SSEntity entry =
          sql.getEntityTest(
            par, 
            SSConf.systemUserUri,
            par.user,
            par.entry,
            par.withUserRestriction);
        
        if(entry == null){
          return null;
        }
        
        if(!sql.isUserAuthor(par, par.user, discURI)){
          return null;
        }
      }
      
      final SSDisc disc =
        discGet(
          new SSDiscGetPar(
            par, 
            par.user,
            discURI,
            true, //setEntries
            par.withUserRestriction,
            false));
      
      for(SSEntity entry : disc.entries){
        if(((SSDiscEntry) entry).accepted){
          return null;
        }
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.acceptEntry(par, par.entry);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return par.entry;
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI discGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSDiscGetPar par = (SSDiscGetPar) parA.getFromClient(clientType, parA, SSDiscGetPar.class);
      
      return SSDiscGetRet.get(discGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSDisc discGet(final SSDiscGetPar par) throws SSErr{
    
    try{
      
      SSDisc                disc       = sql.getDisc(par, par.disc, par.setEntries);
      
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
        entityGet(
          new SSEntityGetPar(
            par, 
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
          par, 
          par.user,
          null, //entity,
          par.withUserRestriction, //withUserRestriction,
          descPar);
      
      for(SSEntity target : disc.targets){
        
        entityGetPar.entity = target.id;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          discTargets,
          entityGet(entityGetPar));
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
          par, 
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
            entityGet(entityGetPar)));
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
      
      final SSDiscsGetPar par = (SSDiscsGetPar) parA.getFromClient(clientType, parA, SSDiscsGetPar.class);
      
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
                par, 
                SSConf.systemUserUri,
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
                par, 
                SSConf.systemUserUri,
                par.user,
                disc,
                par.withUserRestriction);
            
            if(discEntity == null){
              return new ArrayList<>();
            }
          }
        }
      }
      
      final List<SSUri>    discURIsToQuery = new ArrayList<>();
      final List<SSEntity> discs           = new ArrayList<>();
      final SSDiscGetPar   discGetPar =
        new SSDiscGetPar(
          par, 
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
        
        for(SSUri target : par.targets){
          
          SSUri.addDistinctWithoutNull(
            discURIsToQuery,
            sql.getDiscURIsForTarget(
              par, 
              par.forUser,
              target));
        }
      }else{
        
        if(par.discs.isEmpty()){
          discURIsToQuery.addAll(sql.getDiscURIs(par, par.forUser));
        }
      }
      
      for(SSUri disc : discURIsToQuery){
        
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
      
      final SSDiscRemovePar par = (SSDiscRemovePar) parA.getFromClient(clientType, parA, SSDiscRemovePar.class);
      
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
      
      final SSEntity        disc       =
        sql.getEntityTest(
          par, 
          SSConf.systemUserUri,
          par.user,
          par.disc,
          par.withUserRestriction);
      
      if(disc == null){
        return null;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      if(circleIsEntityPrivate(
        new SSCircleIsEntityPrivatePar(
          par, 
          par.user,
          par.disc))){
        
        sql.deleteDisc(par, par.disc);
      }else{
        sql.unlinkDisc(par, par.user, par.disc);
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      return par.disc;
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI discTargetsAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSDiscTargetsAddPar par        = (SSDiscTargetsAddPar) parA.getFromClient(clientType, parA, SSDiscTargetsAddPar.class);
      final SSUri               discussion = discTargetsAdd(par);
      
      return SSDiscTargetsAddRet.get(discussion);
      
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
          par, 
          SSConf.systemUserUri,
          par.user,
          par.discussion,
          par.withUserRestriction);
      
      if(disc == null){
        return null;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      SSUri target;
      
      for(SSUri targetURI : par.targets){
        
        target =
          entityUpdate(
            new SSEntityUpdatePar(
              par, 
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
          dbSQL.rollBack(par, par.shouldCommit);
          return null;
        }
      }
      
      sql.addDiscTargets(par, par.discussion, par.targets);
            
      circleAddEntitiesToCirclesOfEntity(
        new SSCircleAddEntitiesToCircleOfEntityPar(
          par, 
          par.user,
          par.discussion, //entity
          par.targets, //entities
          par.withUserRestriction,
          true, //invokeEntityHandlers,
          false)); //shouldCommit
      
      for(SSUri targetURI : par.targets){
        
        circleAddEntitiesToCirclesOfEntity(
          new SSCircleAddEntitiesToCircleOfEntityPar(
            par, 
            par.user,
            targetURI, //entity
            SSUri.asListNotNull(par.discussion), //entities
            par.withUserRestriction,
            true, //invokeEntityHandlers,
            false)); //shouldCommit
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.addDiscussionTargets(
        par,
        par.shouldCommit);
       
      return par.discussion;
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private List<SSUri> getDiscAffiliatedURIs(
    final SSServPar    servPar,
    final SSUri        discUri) throws SSErr{
    
    try{
      
      final List<SSUri>  discContentUris = new ArrayList<>();
      final SSDisc       disc            = sql.getDisc(servPar, discUri, true);
      
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
    final SSServPar     servPar,
    final SSUri         user,
    final SSUri         entity,
    final List<SSUri>   entitiesToAttach,
    final List<SSLabel> entityLabels,
    final boolean       withUserRestriction) throws SSErr{
    
    try{
      
      if(entitiesToAttach.isEmpty()){
        return entity;
      }
      
      final SSUri entityURI =
        entityEntitiesAttach(
          new SSEntityAttachEntitiesPar(
            servPar, 
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
          entityUpdate(
            new SSEntityUpdatePar(
              servPar, 
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
    final SSServPar   servPar,
    final SSUri       user,
    final SSUri       entity,
    final List<SSUri> entitiesToRemove,
    final boolean     withUserRestriction) throws SSErr{
    
    try{

      if(entitiesToRemove.isEmpty()){
        return entity;
      }
      
      final SSUri entityURI =
        entityEntitiesAttachedRemove(
          new SSEntityEntitiesAttachedRemovePar(
            servPar,
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
  
  public SSUri addDisc(
    final SSServPar     servPar,
    final SSUri         userUri, 
    final SSEntityE     discType, 
    final SSLabel       discLabel,
    final SSTextComment description,
    final boolean       withUserRestriction) throws SSErr{
    
    try{
      final SSUri           disc       =
        entityUpdate(
          new SSEntityUpdatePar(
            servPar,
            userUri,
            SSConf.vocURICreate(),
            discType, //type,
            discLabel, //label
            description, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(disc == null){
        return null;
      }
      
      sql.createDisc(
        servPar,
        userUri, 
        disc);
      
      return disc;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public SSUri addDiscEntry(
    final SSServPar     servPar,
    final SSUri         userUri,
    final SSUri         discUri, 
    final SSTextComment content,
    final boolean       withUserRestriction) throws SSErr{
    
    try{
      final SSEntityE       discType   =
        entityGet(
          new SSEntityGetPar(
            servPar,
            null,
            discUri,  //entity
            false, //withUserRestriction
            null)).type; //descPar
      
      SSEntityE discEntryType = null;
      
      switch(discType){
        case disc: discEntryType = SSEntityE.discEntry;   break;
        case qa:   discEntryType = SSEntityE.qaEntry;     break;
        case chat: discEntryType = SSEntityE.chatEntry;   break;
        default: throw new Exception("disc type not valid");
      }
      
      final SSUri discEntry =
        entityUpdate(
          new SSEntityUpdatePar(
            servPar,
            userUri,
            SSConf.vocURICreate(),
            discEntryType, //type,
            null, //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            false, //withUserRestriction
            false)); //shouldCommit)
      
      if(discEntry == null){
        return null;
      }
      
      sql.addDiscEntry(
        servPar,
        discEntry,
        discUri,
        content);
      
      circleAddEntitiesToCirclesOfEntity(
        new SSCircleAddEntitiesToCircleOfEntityPar(
          servPar,
          userUri,
          discUri,
          SSUri.asListNotNull(discEntry), //entities
          withUserRestriction,
          false, //invokeEntityHandlers,
          false)); //shouldCommit
      
      return discEntry;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}