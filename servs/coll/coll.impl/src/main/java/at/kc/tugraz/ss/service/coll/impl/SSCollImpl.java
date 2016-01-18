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

import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.entity.api.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.entity.api.SSGetParentEntitiesI;
import at.tugraz.sss.serv.entity.api.SSDescribeEntityI;
import at.tugraz.sss.serv.entity.api.SSPushEntitiesToUsersI;
import at.tugraz.sss.serv.entity.api.SSGetSubEntitiesI;
import at.tugraz.sss.serv.entity.api.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSEntityContext;
import at.tugraz.sss.serv.datatype.SSQueryResultPage;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.ret.SSServRetI;
import at.tugraz.sss.serv.datatype.par.SSPushEntitiesToUsersPar;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.par.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.datatype.par.SSCirclePubURIGetPar;
import at.tugraz.sss.serv.datatype.par.SSCirclesGetPar;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.datatype.par.SSCircleEntitiesAddPar;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.par.SSEntitiesGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.datatype.*;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryDeletePar;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.kc.tugraz.ss.service.coll.api.*;
import at.kc.tugraz.ss.service.coll.datatypes.*;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollGetPar;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserParentGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserRootGetRet;
import at.kc.tugraz.ss.service.coll.impl.fct.sql.SSCollSQLFct;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesDeletePar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesDeleteRet;
import at.tugraz.sss.serv.entity.api.SSUserRelationGathererI;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
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
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.datatype.enums.SSWarnE;

public class SSCollImpl
extends SSServImplWithDBA
implements
  SSCollClientI,
  SSCollServerI,
  SSDescribeEntityI,
  SSGetParentEntitiesI,
  SSGetSubEntitiesI,
  SSAddAffiliatedEntitiesToCircleI,
  SSPushEntitiesToUsersI,
  SSUserRelationGathererI,
  SSUsersResourcesGathererI{
  
  private final SSCollSQLFct     sql;
  private final SSEntityServerI  entityServ;
  private final SSEntityServerI  circleServ;
  private final SSUserCommons userCommons;
  
  public SSCollImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql         = new SSCollSQLFct(dbSQL, SSConf.systemUserUri);
    this.entityServ  = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
    this.circleServ  = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
    this.userCommons = new SSUserCommons();
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(par.setColls){
        
        switch(entity.type){
          
          case user:{
            
            entity.collsPage = new SSQueryResultPage(null);
            
            final SSColl rootColl =
              collRootGet(
                new SSCollUserRootGetPar(
                  servPar, 
                  entity.id,
                  par.withUserRestriction,
                  false));
            
            SSEntity collEntity;
            
            for(SSEntity coll : rootColl.entries){
              
              collEntity =
                sql.getEntityTest(
                  servPar, 
                  par.user,
                  coll.id,
                  par.withUserRestriction);
              
              if(collEntity == null){
                continue;
              }
              
              entity.collsPage.entities.add(collEntity);
            }
            
            break;
          }
        }
      }
      
      switch(entity.type){
        
        case coll:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          return SSColl.get(
            collGet(
              new SSCollGetPar(
                servPar, 
                par.user,
                entity.id,
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
  public void getUsersResources(
    final SSServPar servPar,
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      final SSCollsGetPar collsGetPar =
        new SSCollsGetPar(
          servPar, 
          null, //user
          false, //withUserRestriction,
          false); //invokeEntityHandlers)
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        collsGetPar.user = userID;
        
        for(SSEntity coll : collsGet(collsGetPar)){
          
          usersEntities.get(user).add(
            new SSEntityContext(
              coll.id,
              SSEntityE.coll,
              null,
              null));
          
          for(SSEntity collEntry : coll.entries){
            
            usersEntities.get(user).add(
              new SSEntityContext(
                collEntry.id,
                SSEntityE.coll,
                null,
                null));
          }
        }
      }
      
    }catch(Exception error){
      SSLogU.err(error);
    }
  }
  
  @Override
  public void getUserRelations(
    final SSServPar servPar, 
    final List<String>             allUsers,
    final Map<String, List<SSUri>> userRelations) throws SSErr{
    
    try{
      List<SSEntity>                 collUserCircles;
      List<SSEntity>                 collEntryUserCircles;
      List<SSEntity>                 allColls;
      SSCollEntry                    collEntry;
      
      for(String user : allUsers){
        
        final SSUri userUri = SSUri.get(user);
        
        allColls =
          collsGet(
            new SSCollsGetPar(
              servPar, 
              userUri,
              false, //withUserRestriction
              false)); //invokeEntityHandlers
        
        for(SSEntity coll : allColls){
          
          collUserCircles =
            circleServ.circlesGet(
              new SSCirclesGetPar(
                servPar, 
                userUri,
                userUri,
                coll.id,
                null, //entityTypesToIncludeOnly
                false, //setEntities
                true, //setUsers
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
              circleServ.circlesGet(
                new SSCirclesGetPar(
                  servPar, 
                  userUri,
                  userUri,
                  collEntry.id,
                  null, //entityTypesToIncludeOnly
                  false, //setEntities,
                  true, //setUsers
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
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public List<SSUri> getParentEntities(
    final SSServPar servPar, 
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws SSErr{
    
    try{
      return SSUri.getDistinctNotNullFromEntities(
        collsEntityIsInGet(
          new SSCollsUserEntityIsInGetPar(
            servPar, 
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
    final SSServPar servPar, 
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws SSErr{
    
    try{
      switch(type){
        
        case coll:{
          
          return SSCollMiscFct.getCollSubCollAndEntryURIs(
            servPar, 
            sql,
            sql.getCollWithEntries(
              servPar, 
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
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSServPar servPar, final SSAddAffiliatedEntitiesToCirclePar par) throws SSErr{
    
    try{
      
      final List<SSUri>    affiliatedURIs       = new ArrayList<>();
      final List<SSEntity> affiliatedEntities   = new ArrayList<>();
      
      for(SSEntity entityAdded : par.entities){
        
        switch(entityAdded.type){
          
          case coll:{
            
            if(SSStrU.contains(par.recursiveEntities, entityAdded)){
              continue;
            }else{
              SSUri.addDistinctWithoutNull(par.recursiveEntities, entityAdded.id);
            }
            
            if(sql.isCollSpecial(servPar, entityAdded.id)){
              
              if(
                SSStrU.equals(
                  par.circle,
                  circleServ.circlePubURIGet(
                    new SSCirclePubURIGetPar(
                      servPar, 
                      par.user,
                      false)))){
                
                throw SSErr.get(SSErrE.cannotSetSpecialCollectionPublic);
              }
              
              throw SSErr.get(SSErrE.cannotShareSpecialCollection);
            }
            
            for(SSUri collContentURI : SSCollMiscFct.getCollSubCollAndEntryURIs(servPar, sql, sql.getCollWithEntries(servPar, entityAdded.id))){
              
              if(SSStrU.contains(par.recursiveEntities, collContentURI)){
                continue;
              }
              
              SSUri.addDistinctWithoutNull(
                affiliatedURIs,
                collContentURI);
            }
          }
        }
      }
      
      if(affiliatedURIs.isEmpty()){
        return affiliatedEntities;
      }
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          servPar, 
          par.user,
          par.circle, //circle
          affiliatedURIs, //entities
          false, //withUserRestriction
          false)); //shouldCommit
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities,
        entityServ.entitiesGet(
          new SSEntitiesGetPar(
            servPar, 
            par.user,
            affiliatedURIs, //entities
            null, //descPar
            par.withUserRestriction)));
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities,
        SSServReg.inst.addAffiliatedEntitiesToCircle(
          servPar, 
          par.user,
          par.circle,
          affiliatedEntities, //entities
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
    final SSServPar servPar, 
    final SSPushEntitiesToUsersPar par) throws SSErr {
    
    try{
      
      SSColl rootColl;
      
      for(SSEntity entityToPush : par.entities){
        
        switch(entityToPush.type){
          
          case coll: {
            
            for(SSUri userToPushTo : par.users){
              
              rootColl =
                collRootGet(
                  new SSCollUserRootGetPar(
                    servPar, 
                    userToPushTo,
                    false, //withUserRestriction,
                    false)); //invokeEntityHandlers));
              
              if(
                sql.containsCollEntry (servPar, rootColl.id,        entityToPush.id) ||
                sql.ownsUserColl      (servPar, userToPushTo, entityToPush.id)){
                SSLogU.warn(SSWarnE.collAlreadySharedWithUser);
                continue;
              }
              
              if(SSCollMiscFct.ownsUserASubColl(servPar, sql, userToPushTo, entityToPush.id)){
                SSLogU.warn(SSWarnE.subCollAlreadySharedWithUser);
                continue;
              }
              
              sql.addCollToColl(
                servPar, 
                userToPushTo,
                rootColl.id,
                entityToPush.id,
                false,
                true);
            }
            
            break;
          }
          
          case uploadedFile:
          case entity:{
            
            SSUri sharedWithMeFilesCollUri;
            
            for(SSUri userToPushTo : par.users){
              
              sharedWithMeFilesCollUri = sql.getSpecialCollURI(servPar, userToPushTo);
              
              if(sql.containsCollEntry (servPar, sharedWithMeFilesCollUri, entityToPush.id)){
                continue;
              }
              
              sql.addCollEntry(servPar, sharedWithMeFilesCollUri, entityToPush.id);
              
              break;
            }
          }
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
//  @Override
//  public void entitiesSharedWithUsers(SSEntitiesSharedWithUsersPar par) throws SSErr {
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
////                  sqlFct.getCollUserURIs(entityToAdd.id),
////                  false,
////                  false));
////            }
//      }
//  }
  
  @Override
  public SSServRetI collGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCollGetPar par = (SSCollGetPar) parA.getFromClient(clientType, parA, SSCollGetPar.class);
      final SSCollGetRet ret = SSCollGetRet.get(collGet(par));
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSColl collGet(final SSCollGetPar par) throws SSErr{
    
    try{
      
      if(par.coll == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      SSColl coll = sql.getCollWithEntries(par, par.coll);
      
      if(coll == null){
        return null;
      }
      
      final SSEntityDescriberPar descPar;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(par.coll);
        
        descPar.setCircleTypes = true;
      }else{
        descPar = null;
      }
      
      final SSEntity collEntity =
        entityServ.entityGet(
          new SSEntityGetPar(
            par, 
            par.user,
            par.coll, //entity,
            par.withUserRestriction,
            descPar));
      
      if(collEntity == null){
        return null;
      }
      
      coll = SSColl.get(coll, collEntity);
      
      if(descPar != null){
        descPar.recursiveEntity = null;
      }
      
      final List<SSEntity> collEntries =
        entityServ.entitiesGet(
          new SSEntitiesGetPar(
            par, 
            par.user,
            SSUri.getDistinctNotNullFromEntities(coll.entries),  //entities
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
  public SSColl collParentGet(final SSCollUserParentGetPar par) throws SSErr{
    
    try{
      
      if(SSObjU.isNull(par.user, par.coll)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(!sql.ownsUserColl(
        par,
        par.user,
        par.coll)){
        
        throw SSErr.get(SSErrE.userDoesntOwnColl);
      }
      
      return collGet(
        new SSCollGetPar(
          par, 
          par.user,
          SSCollMiscFct.getDirectParentCollURIForUser(
            par, 
            sql,
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
  public SSServRetI collRootGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCollUserRootGetPar par = (SSCollUserRootGetPar) parA.getFromClient(clientType, parA, SSCollUserRootGetPar.class);
      
      return SSCollUserRootGetRet.get(collRootGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSColl collRootGet(final SSCollUserRootGetPar par) throws SSErr{
    
    try{
      
      if(SSObjU.isNull(par.user)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      return collGet(
        new SSCollGetPar(
          par,
          par.user,
          sql.getRootCollURI(
            par,
            par.user),
          par.withUserRestriction,
          par.invokeEntityHandlers));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> collsGet(final SSCollsGetPar par) throws SSErr{
    
    try{
      
      if(par.user == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<SSEntity> colls      = new ArrayList<>();
      final SSCollGetPar   collGetPar =
        new SSCollGetPar(
            par, 
          par.user,
          null, //coll
          par.withUserRestriction,
          par.invokeEntityHandlers);
      
      for(SSUri collURI : sql.getUserCollURIs(par, par.user)){
        
        collGetPar.coll = collURI;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          colls,
          collGet(collGetPar));
      }
      
      return colls;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI collHierarchyGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCollUserHierarchyGetPar par = (SSCollUserHierarchyGetPar) parA.getFromClient(clientType, parA, SSCollUserHierarchyGetPar.class);
      
      return SSCollUserHierarchyGetRet.get(collHierarchyGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> collHierarchyGet(final SSCollUserHierarchyGetPar par) throws SSErr{
    
    try{
      
      if(SSObjU.isNull(par.user, par.coll)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<SSEntity> colls = new ArrayList<>();
      
      if(par.withUserRestriction){
        
        final SSEntity coll =
          sql.getEntityTest(
            par, 
            par.user,
            par.coll,
            par.withUserRestriction);
        
        if(coll == null){
          return colls;
        }
      }
      
      final List<SSUri>  hierarchy      = new ArrayList<>();
      final SSUri        rootCollUri;
      SSUri              directPartentCollUri;
      
      rootCollUri          = 
        sql.getRootCollURI(
          par, 
          par.user);
      
      directPartentCollUri =
        SSCollMiscFct.getDirectParentCollURIForUser(
            par, 
          sql,
          par.user,
          par.coll);
      
      while(!SSStrU.equals(rootCollUri, directPartentCollUri)){
        
        hierarchy.add(directPartentCollUri);
        
        directPartentCollUri =
          SSCollMiscFct.getDirectParentCollURIForUser(
            par, 
            sql,
            par.user,
            directPartentCollUri);
      }
      
      hierarchy.add(rootCollUri);
      
      final SSCollGetPar collGetPar =
        new SSCollGetPar(
            par, 
          par.user,
          null, //collURI,
          par.withUserRestriction,
          par.invokeEntityHandlers);
      
      for(SSUri collURI : hierarchy){
        
        collGetPar.coll = collURI;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          colls,
          collGet(collGetPar));
      }
      
      return colls;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI collsEntityIsInGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCollsUserEntityIsInGetPar par = (SSCollsUserEntityIsInGetPar) parA.getFromClient(clientType, parA, SSCollsUserEntityIsInGetPar.class);
      
      return SSCollsUserEntityIsInGetRet.get(collsEntityIsInGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> collsEntityIsInGet(final SSCollsUserEntityIsInGetPar par) throws SSErr{
    
    try{
      
      final List<SSEntity> colls = new ArrayList<>();
      
      if(par.withUserRestriction){
        
        final SSEntity entity =
          sql.getEntityTest(
            par, 
            par.user,
            par.entity,
            par.withUserRestriction);
        
        if(entity == null){
          return colls;
        }
      }
      
      final List<SSUri>   userCollUris   = sql.getUserCollURIs(par, par.user);
      final List<SSUri>   entityCollUris = sql.getCollURIsContainingEntity(par, par.entity);
      final List<String>  commonCollUris = SSStrU.retainAll(SSStrU.toStr(entityCollUris), SSStrU.toStr(userCollUris));
      final SSCollGetPar  collGetPar =
        new SSCollGetPar(
            par, 
          par.user,
          null, //coll,
          par.withUserRestriction,
          par.invokeEntityHandlers);
      
      for(SSUri collURI : SSUri.get(commonCollUris)){
        
        collGetPar.coll = collURI;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          colls,
          collGet(collGetPar));
      }
      
      return colls;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean collRootAdd(final SSCollUserRootAddPar par) throws SSErr{
    
    try{
      
      if(SSObjU.isNull(par.user)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(sql.existsCollRootForUser(par, par.forUser)){
        return true;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri rootColl =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par, 
            par.forUser,
            SSConf.vocURICreate(), //entity
            SSEntityE.coll, //type,
            SSLabel.get(SSStrU.valueRoot), //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(rootColl == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return false;
      }
      
      sql.addColl(
            par, 
        rootColl);
      
      sql.addCollRoot(
            par, 
        rootColl,
        par.forUser);
      
      final SSUri sharedWithMeFilesCollUri =
        collEntryAdd(
          new SSCollUserEntryAddPar(
            par, 
            par.forUser,
            rootColl, //coll
            null, //entry
            SSLabel.get(SSStrU.valueSharedWithMeFiles), //label
            true, //addNewColl
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit
      
      if(sharedWithMeFilesCollUri == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return false;
      }
      
      sql.addCollSpecial(
            par, 
        sharedWithMeFilesCollUri,
        par.forUser);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return true;
    }catch(SSErr error){
      
      switch(error.code){

        case sqlDeadLock:{
          
          try{
            dbSQL.rollBack(par, par.shouldCommit);
            SSServErrReg.regErrThrow(error);
            return false;
          }catch(Exception error2){
            SSServErrReg.regErrThrow(error2);
            return false;
          }
        }
        
        default:{
          SSServErrReg.regErrThrow(error);
          return false;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public SSServRetI collEntryAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCollUserEntryAddPar par = (SSCollUserEntryAddPar) parA.getFromClient(clientType, parA, SSCollUserEntryAddPar.class);
      final SSServRetI            ret = SSCollUserEntryAddRet.get(collEntryAdd(par));
      
      SSCollActivityFct.addCollEntry(par);
      
      return ret;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri collEntryAdd(final SSCollUserEntryAddPar par) throws SSErr{
    
    try{
      
      if(SSObjU.isNull(par.coll)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        final SSEntity coll =
          sql.getEntityTest(
            par, 
            par.user,
            par.coll,
            par.withUserRestriction);
        
        if(coll == null){
          return null;
        }
        
        final SSEntity entry =
          sql.getEntityTest(
            par, 
            par.user,
            par.coll,
            par.withUserRestriction);
        
        if(entry == null){
          return null;
        }
      }
      
      if(par.addNewColl){
        
        if(par.label == null){
          throw SSErr.get(SSErrE.parameterMissing);
        }
        
        dbSQL.startTrans(par, par.shouldCommit);
        
        final SSUri newColl =
          SSCollEntryAddFct.addNewColl(
            circleServ,
            entityServ,
            sql,
            par);
        
        if(newColl == null){
          dbSQL.rollBack(par, par.shouldCommit);
          return null;
        }
        
        dbSQL.commit(par, par.shouldCommit);
        
        return newColl;
      }
      
      if(sql.containsCollEntry(par, par.coll, par.entry)){
        return par.entry;
      }
      
      if(sql.isColl(par, par.entry)){
        
        dbSQL.startTrans(par, par.shouldCommit);
        
        SSCollEntryAddFct.addPublicColl(
          sql,
          circleServ,
          par);
        
        dbSQL.commit(par, par.shouldCommit);
        
        return par.entry;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri entry =
        SSCollEntryAddFct.addCollEntry(
          circleServ,
          entityServ,
          sql,
          par);
      
      if(entry == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      return par.entry;
      
    }catch(SSErr error){
      
      switch(error.code){

        case sqlDeadLock:{
          
          try{
            dbSQL.rollBack(par, par.shouldCommit);
            SSServErrReg.regErrThrow(error);
            return null;
          }catch(Exception error2){
            SSServErrReg.regErrThrow(error2);
            return null;
          }
        }
        
        default:{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI collEntriesAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCollUserEntriesAddPar par = (SSCollUserEntriesAddPar) parA.getFromClient(clientType, parA, SSCollUserEntriesAddPar.class);
      final SSServRetI              ret = SSCollUserEntriesAddRet.get(collEntriesAdd(par));
      
      SSCollActivityFct.addCollEntries(par);
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> collEntriesAdd(final SSCollUserEntriesAddPar par) throws SSErr{
    
    try{
      
      final List<SSUri> addedEntries = new ArrayList<>();
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSCollUserEntryAddPar collEntryAddPar =
        new SSCollUserEntryAddPar(
            par, 
          par.user,
          par.coll, //coll
          null, //entry
          null, //label
          false, //addNewColl
          par.withUserRestriction, //withUserRestriction
          false); //shouldCommit
      
      for(int counter = 0; counter < par.entries.size(); counter++){
        
        collEntryAddPar.entry = par.entries.get(counter);
        collEntryAddPar.label = par.labels.get(counter);
        
        SSUri.addDistinctWithoutNull(
          addedEntries,
          collEntryAdd(collEntryAddPar));
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      return addedEntries;
    }catch(SSErr error){
      
      switch(error.code){

        case sqlDeadLock:{
          
          try{
            dbSQL.rollBack(par, par.shouldCommit);
            SSServErrReg.regErrThrow(error);
            return null;
          }catch(Exception error2){
            SSServErrReg.regErrThrow(error2);
            return null;
          }
        }
        
        default:{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri collEntryDelete(final SSCollUserEntryDeletePar par) throws SSErr{
    
    try{
      
      if(SSObjU.isNull(par.coll, par.entry)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        final SSEntity coll =
          sql.getEntityTest(
            par, 
            par.user,
            par.coll,
            par.withUserRestriction);
        
        if(coll == null){
          return null;
        }
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      if(sql.isColl(par, par.entry)){
        
        SSCollEntryDeleteFct.removeColl(
          sql,
          circleServ,
          par);
        
      }else{
        SSCollEntryDeleteFct.removeCollEntry(sql, par);
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      return par.entry;
    }catch(SSErr error){
      
      switch(error.code){

        case sqlDeadLock:{
          
          try{
            dbSQL.rollBack(par, par.shouldCommit);
            SSServErrReg.regErrThrow(error);
            return null;
          }catch(Exception error2){
            SSServErrReg.regErrThrow(error2);
            return null;
          }
        }
        
        default:{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI collEntriesDelete(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCollUserEntriesDeletePar par = (SSCollUserEntriesDeletePar) parA.getFromClient(clientType, parA, SSCollUserEntriesDeletePar.class);
      final SSServRetI                 ret = SSCollUserEntriesDeleteRet.get(collEntriesDelete(par));
      
      SSCollActivityFct.removeCollEntries(par);
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> collEntriesDelete(final SSCollUserEntriesDeletePar par) throws SSErr{
    
    try{
      
      final List<SSUri> deletedEntries = new ArrayList<>();
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSCollUserEntryDeletePar collEntryDeletePar =
        new SSCollUserEntryDeletePar(
            par, 
          par.user,
          par.coll,
          null,  //entry
          par.withUserRestriction,
          false);
      
      for(SSUri collEntryUri : par.entries){
        
        collEntryDeletePar.entry = collEntryUri;
        
        SSUri.addDistinctWithoutNull(
          deletedEntries,
          collEntryDelete(collEntryDeletePar));
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      return deletedEntries;
      
    }catch(SSErr error){
      
      switch(error.code){

        case sqlDeadLock:{
          
          try{
            dbSQL.rollBack(par, par.shouldCommit);
            SSServErrReg.regErrThrow(error);
            return null;
          }catch(Exception error2){
            SSServErrReg.regErrThrow(error2);
            return null;
          }
        }
        
        default:{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI collCumulatedTagsGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCollCumulatedTagsGetPar par = (SSCollCumulatedTagsGetPar) parA.getFromClient(clientType, parA, SSCollCumulatedTagsGetPar.class);
      
      return SSCollUserCumulatedTagsGetRet.get(collCumulatedTagsGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSTagFrequ> collCumulatedTagsGet(final SSCollCumulatedTagsGetPar par) throws SSErr{
    
    try{
      
      if(par.coll == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        final SSEntity coll =
          sql.getEntityTest(
            par, 
            par.user,
            par.coll,
            par.withUserRestriction);
        
        if(coll == null){
          return new ArrayList<>();
        }
      }
      
      final List<SSUri> entityURIs =
        SSCollMiscFct.getCollSubCollAndEntryURIs(
          par, 
          sql,
          sql.getCollWithEntries(par, par.coll));
      
      final SSTagServerI tagServ = (SSTagServerI) SSServReg.getServ(SSTagServerI.class);
      
      return tagServ.tagFrequsGet(
        new SSTagFrequsGetPar(
            par, 
          par.user,
          null, //forUser,
          entityURIs,
          null, //labels,
          null, //spaces,
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
//  public List<SSColl> collsUserCouldSubscribeGet(final SSServPar parA) throws SSErr{
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
//  public boolean collUserEntryChangePos(SSServPar parA) throws SSErr{
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
//      dbSQL.startTrans(par, par.shouldCommit);
//
//      sqlFct.updateCollEntriesPos(par.coll, collEntries, order);
//
//      dbSQL.commit(par, par.shouldCommit);
//
//      return true;
////    }catch(SSErr error){
//      
//      if(error.code == SSErrE.sqlDeadLock){
//
//        try{
//          dbSQL.rollBack(par, par.shouldCommit);
//          SSServErrReg.regErrThrow(error);
//          return null;
//        }catch(Exception error2){
//          SSServErrReg.regErrThrow(error2);
//          return null;
//        }
//      }
//      
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//@Override
//  public List<SSTagFrequ> collUserCumulatedTagsGet(final SSServPar parA) throws SSErr{
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