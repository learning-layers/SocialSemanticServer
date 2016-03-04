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
package at.tugraz.sss.servs.learnep.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.SSActivity;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivitiesGetPar;
import at.kc.tugraz.ss.category.datatypes.*;
import at.tugraz.sss.serv.datatype.par.SSCircleEntitiesAddPar;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.par.SSEntitiesGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpClientI;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.conf.*;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpCircle;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpDailySummary;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpEntity;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpTimelineState;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.*;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.*;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.entity.api.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.datatype.par.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.datatype.par.SSCircleAddEntitiesToCircleOfEntityPar;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.entity.api.SSCopyEntityI;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.entity.api.SSDescribeEntityI;
import at.tugraz.sss.serv.entity.api.SSEntitiesSharedWithUsersI;
import at.tugraz.sss.serv.datatype.par.SSEntitiesSharedWithUsersPar;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSEntityContext;
import at.tugraz.sss.serv.datatype.par.SSEntityCopyPar;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.entity.api.SSPushEntitiesToUsersI;
import at.tugraz.sss.serv.datatype.par.SSPushEntitiesToUsersPar;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 
import at.tugraz.sss.serv.entity.api.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.datatype.par.SSEntityCopiedPar;

public class SSLearnEpImpl
extends SSServImplWithDBA
implements
  SSLearnEpClientI,
  SSLearnEpServerI,
  SSDescribeEntityI,
  SSAddAffiliatedEntitiesToCircleI,
  SSPushEntitiesToUsersI,
  SSEntitiesSharedWithUsersI,
  SSCopyEntityI,
  SSUsersResourcesGathererI{
  
  private final SSLearnEpActAndLog         actAndLog      = new SSLearnEpActAndLog();
  private final SSUserCommons              userCommons    = new SSUserCommons(); 
  private final SSLearnEpCat               cat            = new SSLearnEpCat();          
  private final SSLearnEpCommons           commons        = new SSLearnEpCommons();     
  private final SSLearnEpSummaryCommons    summaryCommons = new SSLearnEpSummaryCommons();
  private final SSLearnEpSQL               sql;
  
  public SSLearnEpImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql = new SSLearnEpSQL(dbSQL);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar            servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    switch(entity.type){
      
      case learnEp:{
        
        if(SSStrU.isEqual(entity, par.recursiveEntity)){
          return entity;
        }
        
        return SSLearnEp.get(
          learnEpGet(
            new SSLearnEpGetPar(
              servPar,
              par.user,
              entity.id,
              par.withUserRestriction,
              false)),
          entity);
      }
      
      default: return entity;
    }
  }
  
  @Override
  public void getUsersResources(
    final SSServPar servPar,
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      final SSLearnEpsGetPar learnEpsGetPar =
        new SSLearnEpsGetPar(
          servPar,
          null, //user
          null, //forUser
          false, //withUserRestriction,
          false); //invokeEntityHandlers)
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        learnEpsGetPar.user    = userID;
        learnEpsGetPar.forUser = userID;
        
        for(SSEntity learnEpEntity : learnEpsGet(learnEpsGetPar)){
          
          usersEntities.get(user).add(
            new SSEntityContext(
              learnEpEntity.id,
              SSEntityE.learnEp,
              null,
              null));
          
          for(SSEntity versionEntity : ((SSLearnEp) learnEpEntity).entries){
            
            for(SSEntity entity : ((SSLearnEpVersion) versionEntity).learnEpEntities){
              
              usersEntities.get(user).add(
                new SSEntityContext(
                  ((SSLearnEpEntity) entity).entity.id,
                  SSEntityE.learnEp,
                  null,
                  null));
            }
          }
        }
      }
      
    }catch(Exception error){
      SSLogU.err(error);
    }
  }
  
  @Override
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSServPar servPar, final SSAddAffiliatedEntitiesToCirclePar par) throws SSErr{
    
    try{
      final SSEntityServerI entityServ         = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final List<SSUri>     affiliatedURIs     = new ArrayList<>();
      final List<SSEntity>  affiliatedEntities = new ArrayList<>();
      
      for(SSEntity entityAdded : par.entities){
        
        switch(entityAdded.type){
          
          case learnEp:{
            
            if(SSStrU.contains(par.recursiveEntities, entityAdded)){
              continue;
            }else{
              SSUri.addDistinctWithoutNull(par.recursiveEntities, entityAdded.id);
            }
            
            for(SSUri learnEpContentURI : getLearnEpAffiliatedURIs(servPar, entityAdded.id)){
              
              if(SSStrU.contains(par.recursiveEntities, learnEpContentURI)){
                continue;
              }
              
              SSUri.addDistinctWithoutNull(
                affiliatedURIs,
                learnEpContentURI);
            }
          }
        }
      }
      
      if(affiliatedURIs.isEmpty()){
        return affiliatedEntities;
      }
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities,
        entityServ.entitiesGet(
          new SSEntitiesGetPar(
            servPar,
            par.user,
            affiliatedURIs,
            null, //descPar
            par.withUserRestriction)));
      
      entityServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          servPar,
          par.user,
          par.circle,
          affiliatedURIs,
          false, //withUserRestriction
          false)); //shouldCommit
      
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
    final SSServPar                servPar,
    final SSPushEntitiesToUsersPar par) throws SSErr {
    
    try{
      for(SSEntity entityToPush : par.entities){
        
        switch(entityToPush.type){
          
          case learnEp: {
            
            for(SSUri userToPushTo : par.users){
              sql.addLearnEp(servPar, entityToPush.id, userToPushTo);
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
  public void entitiesSharedWithUsers(
    final SSServPar servPar,
    final SSEntitiesSharedWithUsersPar par) throws SSErr {
    
    for(SSEntity entityShared : par.circle.entities){
      
      switch(entityShared.type){
        
        case learnEp:{
          
          actAndLog.shareLearnEp(
            servPar,
            par.user,
            entityShared.id,
            SSUri.getDistinctNotNullFromEntities(par.circle.users),
            false);
          
          break;
        }
      }
    }
  }
  
  @Override
  public void copyEntity(
    final SSServPar servPar,
    final SSEntity                  entity,
    final SSEntityCopyPar           par) throws SSErr{
    
    try{
      
      if(!SSStrU.isEqual(entity.type, SSEntityE.learnEp)){
        return;
      }
      
      final SSEntityServerI entityServ       = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final List<SSEntity>  copiedEntities   = new ArrayList<>();
      final List<SSEntity>  entitiesIncluded = new ArrayList<>();
      SSUri                 copyVersionUri;
      SSUri                 copyLearnEpUri;
      SSEntityCopiedPar     entityCopiedPar;
      
      final SSLearnEp learnEp =
        learnEpGet(
          new SSLearnEpGetPar(
            servPar,
            par.user,
            entity.id,
            par.withUserRestriction,
            true)); //invokeEntityHandlers
      
      for(SSUri forUser : par.forUsers){
        
        copiedEntities.clear();
        
        copyLearnEpUri =
          learnEpCreate(
            new SSLearnEpCreatePar(
              servPar,
              forUser,
              learnEp.label,
              learnEp.description,
              false,
              false));
        
        for(SSEntity version : learnEp.entries){
          
          copyVersionUri =
            learnEpVersionCreate(
              new SSLearnEpVersionCreatePar(
                servPar,
                forUser,
                copyLearnEpUri,
                false,
                false));
          
          for(SSEntity circle : ((SSLearnEpVersion) version).learnEpCircles){
            
            if(SSStrU.contains(par.entitiesToExclude, circle.id)){
              continue;
            }
            
            entitiesIncluded.add(circle);
            
            learnEpVersionCircleAdd(
              new SSLearnEpVersionCircleAddPar(
                servPar,
                forUser,
                copyVersionUri,
                circle.label,
                ((SSLearnEpCircle) circle).xLabel,
                ((SSLearnEpCircle) circle).yLabel,
                ((SSLearnEpCircle) circle).xR,
                ((SSLearnEpCircle) circle).yR,
                ((SSLearnEpCircle) circle).xC,
                ((SSLearnEpCircle) circle).yC,
                false,
                false));
          }
          
          for(SSEntity learnEpEntity : ((SSLearnEpVersion) version).learnEpEntities){
            
            if(
              SSStrU.contains(par.entitiesToExclude, learnEpEntity.id) ||
              SSStrU.contains(par.entitiesToExclude, ((SSLearnEpEntity) learnEpEntity).entity)){
              continue;
            }
            
            entitiesIncluded.add(((SSLearnEpEntity) learnEpEntity).entity);
            
            learnEpVersionEntityAdd(
              new SSLearnEpVersionEntityAddPar(
                servPar,
                forUser,
                copyVersionUri,
                ((SSLearnEpEntity) learnEpEntity).entity.id,
                ((SSLearnEpEntity) learnEpEntity).x,
                ((SSLearnEpEntity) learnEpEntity).y,
                false,
                false));
            
            copiedEntities.add(((SSLearnEpEntity) learnEpEntity).entity);
          }
        }
        
        entityCopiedPar =
          new SSEntityCopiedPar(
            par.user,
            forUser,
            learnEp, //entity
            copiedEntities, //entities
            copyLearnEpUri, //targetEntity
            par.withUserRestriction);
        
        SSServReg.inst.entityCopied(servPar, entityCopiedPar);
        
        entityServ.circleAddEntitiesToCirclesOfEntity(
          new SSCircleAddEntitiesToCircleOfEntityPar(
            servPar,
            par.user,
            copyLearnEpUri,
            getLearnEpAffiliatedURIs(servPar, copyLearnEpUri), //entities
            par.withUserRestriction,
            true, //invokeEntityHandlers,
            false)); //shouldCommit
      }
      
      actAndLog.copyLearnEp(
        servPar,
        par.user,
        learnEp.id,  //learnEp
        par.forUsers, //usersToCopyFor
        SSUri.getDistinctNotNullFromEntities(entitiesIncluded), //includedEntities
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSServRetI learnEpsGet(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpsGetPar par = (SSLearnEpsGetPar) parA.getFromClient(clientType, parA, SSLearnEpsGetPar.class);
      
      return SSLearnEpsGetRet.get(learnEpsGet(par));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> learnEpsGet(final SSLearnEpsGetPar par) throws SSErr{
    
    try{
      
      if(par.user == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        if(!SSStrU.isEqual(par.forUser, par.user)){
          par.forUser = par.user;
        }
      }
      
      final List<SSUri> learnEpURIs = new ArrayList<>();
      
      if(par.forUser == null){
        learnEpURIs.addAll(sql.getLearnEpURIs(par));
      }else{
        learnEpURIs.addAll(sql.getLearnEpURIs(par, par.forUser));
      }
      
      final List<SSEntity>    learnEps      = new ArrayList<>();
      final SSLearnEpGetPar   learnEpGetPar =
        new SSLearnEpGetPar(
          par, 
          par.user,
          null,
          par.withUserRestriction, //withUserRestriction,
          par.invokeEntityHandlers);
      
      learnEpGetPar.setRead             = par.setRead;
      learnEpGetPar.setCircleTypes      = par.setCircleTypes;
      learnEpGetPar.setAttachedEntities = par.setAttachedEntities;
      
      for(SSUri learnEpURI : learnEpURIs){
        
        learnEpGetPar.learnEp = learnEpURI;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          learnEps,
          learnEpGet(learnEpGetPar));
      }
      
      return learnEps;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSLearnEp learnEpGet(final SSLearnEpGetPar par) throws SSErr{
    
    try{
      
      final SSEntityServerI entityServ  = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      SSLearnEp             learnEp     = sql.getLearnEp(par, par.learnEp);
      
      if(learnEp == null){
        return null;
      }
      
      SSEntityDescriberPar descPar;
      
      if(par.invokeEntityHandlers){
        descPar                     = new SSEntityDescriberPar(par.learnEp);
        descPar.setRead             = par.setRead;
        descPar.setCircleTypes      = par.setCircleTypes;
        descPar.setAttachedEntities = par.setAttachedEntities;
      }else{
        descPar = null;
      }
      
      final SSEntity learnEpEntity =
        entityServ.entityGet(
          new SSEntityGetPar(
            par, 
            par.user,
            par.learnEp,
            par.withUserRestriction,
            descPar));
      
      if(learnEpEntity == null){
        return null;
      }
      
      learnEp =
        SSLearnEp.get(
          learnEp,
          learnEpEntity);
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(learnEp.id);
      }else{
        descPar = null;
      }
      
      final SSEntitiesGetPar entitiesGetPar =
        new SSEntitiesGetPar(
          par, 
          par.user,
          sql.getLearnEpUserURIs(par, learnEp.id), //entities
          descPar, //descPar
          par.withUserRestriction);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        learnEp.users,
        entityServ.entitiesGet(entitiesGetPar));
      
      final SSLearnEpVersionsGetPar learnEpVersionsGetPar =
        new SSLearnEpVersionsGetPar(
          par, 
          par.user,
          learnEp.id, //learnEp
          null, //learnEpVersions,
          par.withUserRestriction, //withUserRestriction
          par.invokeEntityHandlers);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        learnEp.entries,
        learnEpVersionsGet(learnEpVersionsGetPar));
      
      if(!((SSLearnEpConf) conf).useEpisodeLocking){
        learnEp.locked       = false;
        learnEp.lockedByUser = false;
      }else{
        
        learnEp.locked       = SSLearnEpAccessController.isLocked (learnEp.id);
        learnEp.lockedByUser = SSLearnEpAccessController.hasLock  (par.user, learnEp.id);
      }
      
      return learnEp;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI learnEpVersionsGet(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpVersionsGetPar par = (SSLearnEpVersionsGetPar) parA.getFromClient(clientType, parA, SSLearnEpVersionsGetPar.class);
      
      return SSLearnEpVersionsGetRet.get(learnEpVersionsGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> learnEpVersionsGet(final SSLearnEpVersionsGetPar par) throws SSErr{
    
    try{
      
      final SSEntityServerI         entityServ   = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSLearnEpVersionsGetFct fct          = new SSLearnEpVersionsGetFct(entityServ, sql);
      final List<SSEntity>          versions     = new ArrayList<>();
      final SSEntityGetPar          entityGetPar =
        new SSEntityGetPar(
          par, 
          par.user,
          null, //entity
          par.withUserRestriction, //withUserRestriction
          null); // descPar
      
      SSLearnEpVersion              version;
      SSEntity                      versionEntity;
      SSEntityDescriberPar          descPar;
      
      for(SSUri versionUri : fct.getLearnEpVersionURIsToFill(par)){
        
        version =
          sql.getLearnEpVersion(
            par, 
            versionUri,
            false, //setCircles
            false); //setEntities
        
        if(version == null){
          continue;
        }
        
        if(par.invokeEntityHandlers){
          descPar = new SSEntityDescriberPar(versionUri);
        }else{
          descPar = null;
        }
        
        entityGetPar.entity  = versionUri;
        entityGetPar.descPar = descPar;
        
        versionEntity = entityServ.entityGet(entityGetPar);
        
        if(versionEntity == null){
          continue;
        }
        
        version =
          SSLearnEpVersion.get(
            version,
            versionEntity);
        
        fct.setLearnEpVersionCircles       (par, version);
        fct.setLearnEpVersionEntities      (par, version);
        
        versions.add(version);
      }
      
      return versions;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI learnEpVersionGet(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpVersionGetPar par = (SSLearnEpVersionGetPar) parA.getFromClient(clientType, parA, SSLearnEpVersionGetPar.class);
      
      return SSLearnEpVersionGetRet.get(learnEpVersionGet(par));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSLearnEpVersion learnEpVersionGet(final SSLearnEpVersionGetPar par) throws SSErr{
    
    try{
      
      final List<SSEntity> learnEpVersions =
        learnEpVersionsGet(
          new SSLearnEpVersionsGetPar(
            par,
            par.user,
            null, //learnEp
            SSUri.asListNotNull(par.learnEpVersion), //learnEpVersions
            par.withUserRestriction,
            par.invokeEntityHandlers));
      
      if(learnEpVersions.isEmpty()){
        return null;
      }
      
      return (SSLearnEpVersion) learnEpVersions.get(0);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSLearnEpDailySummaryGetRet learnEpDailySummaryGet(final SSLearnEpDailySummaryGetPar par) throws SSErr{
    
    try{
      final SSActivityServerI           activityServ = (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class);
      final SSLearnEpDailySummaryGetRet result       = new SSLearnEpDailySummaryGetRet();
      final List<SSActivityE>           actTypes     = new ArrayList<>();
      final List<SSEntity>              acts         = new ArrayList<>();
      
      actTypes.add(SSActivityE.shareLearnEpWithUser);
      actTypes.add(SSActivityE.copyLearnEpForUsers);
      
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
            false, //includeOnlyLastActivities,
            false, //withUserRestriction,
            false))); //invokeEntityHandlers
      
      SSActivity            act;
      SSLearnEpDailySummary dailySummary;
      
      for(SSEntity actEntity : acts){
        
        act = (SSActivity) actEntity;
        
        switch(act.activityType){
          
          case shareLearnEpWithUser:{
            
            summaryCommons.shareLearnEpWithUser(act, result.summaries);
            break;
          }
          
          case copyLearnEpForUsers:{
            
            summaryCommons.copyLearnEpForUser(act, result.summaries);
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
  public SSServRetI learnEpCircleEntityStructureGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
     try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpCircleEntityStructureGetPar par = (SSLearnEpCircleEntityStructureGetPar) parA.getFromClient(clientType, parA, SSLearnEpCircleEntityStructureGetPar.class);
      
      return learnEpCircleEntityStructureGet(par);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSLearnEpCircleEntityStructureGetRet learnEpCircleEntityStructureGet(final SSLearnEpCircleEntityStructureGetPar par) throws SSErr{
    
    try{
      
      final List<SSUri> versions = sql.getLearnEpVersionURIs(par, par.learnEp);

      if(versions.isEmpty()){
        return new SSLearnEpCircleEntityStructureGetRet(new ArrayList<>(), new ArrayList<>());
      }
      
      final SSLearnEpCirclesWithEntriesGetRet circlesAndOrphans =
        learnEpVersionCirclesWithEntriesGet(
          new SSLearnEpVersionCirclesWithEntriesGetPar(
            par,
            par.user,
            versions.get(0),
            par.withUserRestriction,
            par.invokeEntityHandlers));
      
      return new SSLearnEpCircleEntityStructureGetRet(
        circlesAndOrphans.circles, 
        circlesAndOrphans.orphans);
      
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
  public SSServRetI learnEpRemove(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpRemovePar par = (SSLearnEpRemovePar) parA.getFromClient(clientType, parA, SSLearnEpRemovePar.class);
      
      return SSLearnEpRemoveRet.get(learnEpRemove(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri learnEpRemove(final SSLearnEpRemovePar par) throws SSErr{
    
    try{
      
      final SSEntity learnEp =
        sql.getEntityTest(
          par, 
          SSConf.systemUserUri,
          par.user,
          par.learnEp,
          par.withUserRestriction);
      
      if(learnEp == null){
        return null;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.removeLearnEpForUser(par, par.user, par.learnEp);
      
      for(SSUri version : sql.getLearnEpVersionURIs(par, par.learnEp)){
        sql.deleteCurrentEpVersion (par, par.user, version);
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.removeLearnEp(
        par, 
        par.shouldCommit);
      
      return par.learnEp;
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
  public SSServRetI learnEpVersionCreate(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpVersionCreatePar par = (SSLearnEpVersionCreatePar) parA.getFromClient(clientType, parA, SSLearnEpVersionCreatePar.class);
      
      SSLearnEpAccessController.checkHasLock(
        par, 
        ((SSLearnEpConf) conf),
        par.user,
        par.learnEp);
      
      return SSLearnEpVersionCreateRet.get(learnEpVersionCreate(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri learnEpVersionCreate(final SSLearnEpVersionCreatePar par) throws SSErr{
    
    try{
      
      final SSEntityServerI entityServ  = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEntity        learnEp     =
        sql.getEntityTest(
          par, 
          SSConf.systemUserUri,
          par.user,
          par.learnEp,
          par.withUserRestriction);
      
      if(learnEp == null){
        return null;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri learnEpVersion =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par, 
            par.user,
            SSConf.vocURICreate(),
            SSEntityE.learnEpVersion, //type,
            null, //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //creatIfNotExists
            false, //withUserRestriction
            false)); //shouldCommit)
      
      if(learnEpVersion == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sql.createLearnEpVersion(
        par, 
        learnEpVersion,
        par.learnEp);
      
      entityServ.circleAddEntitiesToCirclesOfEntity(
        new SSCircleAddEntitiesToCircleOfEntityPar(
          par,
          par.user,
          par.learnEp,
          SSUri.asListNotNull(learnEpVersion), //entities
          false, //withUserRestriction
          false, //invokeEntityHandlers,
          false)); //shouldCommit
      
      dbSQL.commit(par, par.shouldCommit);
      
      return learnEpVersion;
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
  public SSServRetI learnEpVersionCircleAdd(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpVersionCircleAddPar par          = (SSLearnEpVersionCircleAddPar) parA.getFromClient(clientType, parA, SSLearnEpVersionCircleAddPar.class);
      final SSUri                        learnEp      = sql.getLearnEpForVersion(par, par.learnEpVersion);
      
      SSLearnEpAccessController.checkHasLock(
        par, 
        ((SSLearnEpConf) conf),
        par.user,
        learnEp);
      
      final SSUri                        newCircle            = learnEpVersionCircleAdd(par);
      final SSLearnEpVersionCircleAddRet ret                  = SSLearnEpVersionCircleAddRet.get(newCircle);
      
      if(newCircle == null){
        return ret;
      }
        
      actAndLog.addCircleToLearnEpVersion(
        par,
        par.user,
        par.learnEpVersion,
        newCircle,
        learnEp,
        par.shouldCommit);
      
      final List<SSEntity> learnEpCirclesAfter = commons.getLearnEpCirclesWithEntries (this, par, par.user, par.learnEpVersion);
      List<SSUri>          entityURIs;
      
      for(SSEntity learnEpCircleAfter : learnEpCirclesAfter){
        
        if(!SSStrU.isEqual(newCircle, learnEpCircleAfter)){
          continue;
        }
        
        entityURIs = commons.getEntityURIsFromLearnEpCircle((SSLearnEpCircle) learnEpCircleAfter);
        
        cat.addCategory(
          par,
          par.user,
          entityURIs, //entities
          SSCategoryLabel.get(SSStrU.toStr(par.label)), //label
          par.shouldCommit);
        
        for(SSUri entityURI : entityURIs){
          
          actAndLog.addLearnEpEntityToCircle(
            par,
            par.user,
            learnEp, //learnEp
            par.learnEpVersion, //learnEpVersion
            entityURI, //entity
            newCircle, //circle
            par.shouldCommit);
        }
        
        break;
      }
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri learnEpVersionCircleAdd(final SSLearnEpVersionCircleAddPar par) throws SSErr{
    
    try{
      
      final SSEntityServerI entityServ     = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEntity        learnEpVersion =
        sql.getEntityTest(
          par, 
          SSConf.systemUserUri,
          par.user,
          par.learnEpVersion,
          par.withUserRestriction);
      
      if(learnEpVersion == null){
        return null;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri circle =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par, 
            par.user,
            SSConf.vocURICreate(),
            SSEntityE.learnEpCircle, //type,
            par.label, //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            false, //withUserRestriction
            false)); //shouldCommit)
      
      if(circle == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sql.addCircleToLearnEpVersion(
        par, 
        circle,
        par.learnEpVersion,
        par.label,
        par.xLabel,
        par.yLabel,
        par.xR,
        par.yR,
        par.xC,
        par.yC);
      
      entityServ.circleAddEntitiesToCirclesOfEntity(
        new SSCircleAddEntitiesToCircleOfEntityPar(
          par, 
          par.user,
          par.learnEpVersion,
          SSUri.asListNotNull(circle), //entities
          false, //withUserRestriction
          false, //invokeEntityHandlers,
          false)); //shouldCommit
      
      dbSQL.commit(par, par.shouldCommit);
      
      return circle;
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
  public SSServRetI learnEpVersionEntityAdd(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpVersionEntityAddPar par     = (SSLearnEpVersionEntityAddPar) parA.getFromClient(clientType, parA, SSLearnEpVersionEntityAddPar.class);
      final SSUri                        learnEp = sql.getLearnEpForVersion(par, par.learnEpVersion);
      
      SSLearnEpAccessController.checkHasLock(
        par, 
        ((SSLearnEpConf) conf),
        par.user,
        learnEp);
      
      final List<SSEntity>               learnEpCirclesBefore          = commons.getLearnEpCirclesWithEntries  (this, par, par.user, par.learnEpVersion);
      final SSUri                        learnEpEntity                 = learnEpVersionEntityAdd    (par);
      final SSLearnEpVersionEntityAddRet ret                           = SSLearnEpVersionEntityAddRet.get(learnEpEntity);
      
      if(learnEpEntity == null){
        return ret;
      }
      
      final List<SSEntity> learnEpCirclesAfter           = commons.getLearnEpCirclesWithEntries            (this, par, par.user, par.learnEpVersion);
      final List<SSUri>    learnEpEntityCircleURIsBefore = commons.getCircleURIsForLearnEpEntity(learnEpCirclesBefore, learnEpEntity);
      final List<SSUri>    learnEpEntityCircleURIsAfter  = commons.getCircleURIsForLearnEpEntity(learnEpCirclesAfter,  learnEpEntity);
      
      cat.handleAddLearnEpEntity(
        par, 
        par.user,
        par.entity,
        learnEpEntityCircleURIsBefore, 
        learnEpEntityCircleURIsAfter, 
        par.shouldCommit);
      
      actAndLog.handleAddLearnEpEntity(
        par, 
        par.user, 
        learnEp, 
        par.learnEpVersion, 
        learnEpEntity, 
        par.entity, 
        learnEpEntityCircleURIsBefore, 
        learnEpEntityCircleURIsAfter, 
        true, //calledFromAdd,
        par.shouldCommit);
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri learnEpVersionEntityAdd(final SSLearnEpVersionEntityAddPar par) throws SSErr{
    
    try{
      
      final SSEntityServerI entityServ     = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEntity        learnEpVersion =
        sql.getEntityTest(
          par, 
          SSConf.systemUserUri,
          par.user,
          par.learnEpVersion,
          par.withUserRestriction);
      
      if(learnEpVersion == null){
        return null;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri entity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par, 
            par.user,
            par.entity,
            null, //type,
            null, //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(entity == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      final SSUri learnEpEntity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par, 
            par.user,
            SSConf.vocURICreate(),
            SSEntityE.learnEpEntity, //type,
            null, //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            false, //withUserRestriction
            false)); //shouldCommit)
      
      if(learnEpEntity == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sql.addEntityToLearnEpVersion(
        par, 
        learnEpEntity,
        learnEpVersion.id,
        entity,
        par.x,
        par.y);
      
      //DONE replace this parts with circleContentedChanged:
      //0. first split / refactor circleContentChanged to. circleEntitiesAdded, circleUsersAdded, sharedWithUsers
      //1. dont retrieve entities for learn ep entity here anymore
      //2. extend circlesFromEntityEntitiesAdd to call "circleContentChanged" upon each circle to be touched
      //3. for each entity targeted in "circleContentChanged" call "circleContentChanged" for its affiliated entities
      //4. make sure to avoid recursion on entites via list keeping all entities for "circleContentChanged" was called so far
      //5. call this process whereever "circlesFromEntityEntitiesAdd" is/can be called
      //      entities.addAll(miscFct.getLearnEpEntityAttachedEntities(par.user, par.entity, par.withUserRestriction));
      //      circleServ.circlesFromEntityEntitiesAdd(
//        new SSCirclesFromEntityEntitiesAdd(
//          null,
//          null,
//          par.user,
//          par.learnEpVersion,
//          entities,
//          false));
      
      entityServ.circleAddEntitiesToCirclesOfEntity(
        new SSCircleAddEntitiesToCircleOfEntityPar(
          par, 
          par.user,
          par.learnEpVersion,
          SSUri.asListNotNull(learnEpEntity), //entities
          false, //withUserRestriction
          false, //invokeEntityHandlers,
          false)); //shouldCommit
            
      entityServ.circleAddEntitiesToCirclesOfEntity(
        new SSCircleAddEntitiesToCircleOfEntityPar(
          par, 
          par.user,
          par.learnEpVersion,
          SSUri.asListNotNull(entity), //entities
          par.withUserRestriction, //withUserRestriction
          true, //invokeEntityHandlers,
          false)); //shouldCommit
      
      dbSQL.commit(par, par.shouldCommit);
      
      return learnEpEntity;
      
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
  public SSServRetI learnEpCreate(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpCreatePar par = (SSLearnEpCreatePar) parA.getFromClient(clientType, parA, SSLearnEpCreatePar.class);

      return SSLearnEpCreateRet.get(learnEpCreate(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri learnEpCreate(final SSLearnEpCreatePar par) throws SSErr{
    
    try{
      
      final SSEntityServerI entityServ       = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri learnEp =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par, 
            par.user,
            SSConf.vocURICreate(),
            SSEntityE.learnEp, //type,
            par.label, //label
            par.description,//description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            false, //withUserRestriction
            false)); //shouldCommit)
      
      if(learnEp == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sql.createLearnEp(par, learnEp, par.user);
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.createLearnEp(
        par, 
        learnEp, 
        par.shouldCommit);
      
      return learnEp;
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
  public SSServRetI learnEpVersionCircleUpdate(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSEntityServerI                 entityServ      = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSLearnEpVersionCircleUpdatePar par             = (SSLearnEpVersionCircleUpdatePar) parA.getFromClient(clientType, parA, SSLearnEpVersionCircleUpdatePar.class);
      final SSUri                           learnEp         = sql.getLearnEpForCircle       (par, par.learnEpCircle);
      final SSUri                           learnEpVersion  = sql.getLearnEpVersionForCircle(par, par.learnEpCircle);
      final SSEntity                        circleEntity    =
        entityServ.entityGet(
          new SSEntityGetPar(
            par, 
            par.user,
            par.learnEpCircle,
            false,
            null));
      
      SSLearnEpAccessController.checkHasLock(
        par, 
        ((SSLearnEpConf) conf),
        par.user,
        learnEp);
      
      final List<SSEntity>                  learnEpCirclesBefore = commons.getLearnEpCirclesWithEntries  (this, par, par.user, learnEpVersion);
      final boolean                         worked               = learnEpVersionCircleUpdate(par);
      final SSLearnEpVersionCircleUpdateRet ret                  = SSLearnEpVersionCircleUpdateRet.get(worked);
      
      if(!worked){
        return ret;
      }
      
      final List<SSEntity> learnEpCirclesAfter = commons.getLearnEpCirclesWithEntries  (this, par, par.user, learnEpVersion);
      
      cat.handleLearnEpCircleLabelUpdate(
        par, 
        par.user,
        circleEntity.id, //learnEpCircle
        learnEpCirclesBefore,  //learnEpCirclesBefore
        circleEntity.label,  //originalLabel
        par.label,  //newLabel
        par.shouldCommit);
      
      actAndLog.changeLearnEpVersionCircleLabelAndDescription(
        par,
        par.user,
        circleEntity,
        par.label,
        par.description,
        learnEpVersion,
        par.learnEpCircle,
        learnEp,
        par.shouldCommit);
      
      List<SSUri> learnEpEntityCircleURIsBefore;
      List<SSUri> learnEpEntityCircleURIsAfter;
      
      for(SSEntity learnEpCircleBefore : learnEpCirclesBefore){
        
        if(!SSStrU.isEqual(learnEpCircleBefore, par.learnEpCircle)){
          continue;
        }
        
        for(SSEntity learnEpEntity : learnEpCircleBefore.entries){
          
          learnEpEntityCircleURIsBefore = commons.getCircleURIsForLearnEpEntity(learnEpCirclesBefore, learnEpEntity.id);
          learnEpEntityCircleURIsAfter  = commons.getCircleURIsForLearnEpEntity(learnEpCirclesAfter,  learnEpEntity.id);
          
          cat.handleAddLearnEpEntity(
            par,
            par.user,
            ((SSLearnEpEntity) learnEpEntity).entity.id,
            learnEpEntityCircleURIsBefore,
            learnEpEntityCircleURIsAfter,
            par.shouldCommit);
          
          cat.handleRemoveLearnEpEntity(
            par,
            par.user,
            ((SSLearnEpEntity) learnEpEntity).entity.id,
            learnEpEntityCircleURIsBefore,
            learnEpEntityCircleURIsAfter,
            par.shouldCommit);
          
          actAndLog.handleAddLearnEpEntity(
            par, 
            par.user, 
            learnEp, 
            learnEpVersion, 
            learnEpEntity.id, 
            ((SSLearnEpEntity) learnEpEntity).entity.id, 
            learnEpEntityCircleURIsBefore, 
            learnEpEntityCircleURIsAfter, 
            false, //calledFromAdd
            par.shouldCommit); //shouldCommit
          
          actAndLog.handleRemoveLearnEpEntity(
            par, 
            par.user, 
            learnEp, 
            learnEpVersion, 
            learnEpEntity.id, 
            ((SSLearnEpEntity) learnEpEntity).entity.id, 
            learnEpEntityCircleURIsBefore, 
            learnEpEntityCircleURIsAfter, 
            false, //calledFromRemove
            par.shouldCommit); //shouldCommit
        }
        
        break;
      }
      
      for(SSEntity learnEpCircleAfter : learnEpCirclesAfter){
        
        if(!SSStrU.isEqual(learnEpCircleAfter, par.learnEpCircle)){
          continue;
        }
        
        for(SSEntity learnEpEntity : learnEpCircleAfter.entries){
          
          learnEpEntityCircleURIsBefore = commons.getCircleURIsForLearnEpEntity(learnEpCirclesBefore, learnEpEntity.id);
          learnEpEntityCircleURIsAfter  = commons.getCircleURIsForLearnEpEntity(learnEpCirclesAfter,  learnEpEntity.id);
          
          cat.handleAddLearnEpEntity(
            par,
            par.user,
            ((SSLearnEpEntity) learnEpEntity).entity.id,
            learnEpEntityCircleURIsBefore,
            learnEpEntityCircleURIsAfter,
            par.shouldCommit);
          
          cat.handleRemoveLearnEpEntity(
            par,
            par.user,
            ((SSLearnEpEntity) learnEpEntity).entity.id,
            learnEpEntityCircleURIsBefore,
            learnEpEntityCircleURIsAfter,
            par.shouldCommit);
          
          actAndLog.handleAddLearnEpEntity(
            par, 
            par.user, 
            learnEp, 
            learnEpVersion, 
            learnEpEntity.id, 
            ((SSLearnEpEntity) learnEpEntity).entity.id, 
            learnEpEntityCircleURIsBefore, 
            learnEpEntityCircleURIsAfter, 
            false, //calledFromAdd
            par.shouldCommit); //shouldCommit
          
          actAndLog.handleRemoveLearnEpEntity(
            par, 
            par.user, 
            learnEp, 
            learnEpVersion, 
            learnEpEntity.id, 
            ((SSLearnEpEntity) learnEpEntity).entity.id, 
            learnEpEntityCircleURIsBefore, 
            learnEpEntityCircleURIsAfter, 
            false, //calledFromRemove
            par.shouldCommit); //shouldCommit
        }
        
        break;
      }
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean learnEpVersionCircleUpdate(final SSLearnEpVersionCircleUpdatePar par) throws SSErr{
    
    try{
      
      final SSEntityServerI entityServ       = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSEntityUpdatePar entityUpdatePar =
        new SSEntityUpdatePar(
          par,
          par.user,
          par.learnEpCircle,
          SSEntityE.learnEpCircle, //type,
          par.label, //label
          par.description,//description,
          null, //creationTime,
          null, //read,
          false, //setPublic
          false, //createIfNotExists
          par.withUserRestriction, //withUserRestriction
          false); //shouldCommit)
      
      final SSUri circle = entityServ.entityUpdate(entityUpdatePar);
      
      if(circle == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return false;
      }
      
      sql.updateCircle(
        par, 
        circle,
        par.xLabel,
        par.yLabel,
        par.xR,
        par.yR,
        par.xC,
        par.yC);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public SSServRetI learnEpVersionEntityUpdate(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpVersionEntityUpdatePar par            = (SSLearnEpVersionEntityUpdatePar) parA.getFromClient(clientType, parA, SSLearnEpVersionEntityUpdatePar.class);
      final SSUri                           learnEp        = sql.getLearnEpForEntity        (par, par.learnEpEntity);
      final SSUri                           learnEpVersion = sql.getLearnEpVersionForEntity (par, par.learnEpEntity);
      final SSUri                           entity         = sql.getEntity                  (par, learnEpVersion, par.learnEpEntity);
      
      SSLearnEpAccessController.checkHasLock(
        par, 
        ((SSLearnEpConf) conf),
        par.user,
        learnEp);
      
      final List<SSEntity>                  learnEpCirclesBefore          = commons.getLearnEpCirclesWithEntries  (this, par, par.user, learnEpVersion);
      final List<SSUri>                     learnEpEntityCircleURIsBefore = commons.getCircleURIsForLearnEpEntity(learnEpCirclesBefore, par.learnEpEntity);
      final boolean                         worked                        = learnEpVersionEntityUpdate (par);
      final SSLearnEpVersionEntityUpdateRet ret                           = SSLearnEpVersionEntityUpdateRet.get(worked);
      
      if(!worked){
        return ret;
      }
      
      final List<SSEntity> learnEpCirclesAfter           = commons.getLearnEpCirclesWithEntries            (this, par, par.user, learnEpVersion);
      final List<SSUri>    learnEpEntityCircleURIsAfter  = commons.getCircleURIsForLearnEpEntity(learnEpCirclesAfter,  par.learnEpEntity);
      
      cat.handleRemoveLearnEpEntity(
        par, 
        par.user, 
        entity, 
        learnEpEntityCircleURIsBefore, 
        learnEpEntityCircleURIsAfter, 
        par.shouldCommit);
      
      cat.handleAddLearnEpEntity(
        par, 
        par.user, 
        entity, 
        learnEpEntityCircleURIsBefore, 
        learnEpEntityCircleURIsAfter, 
        par.shouldCommit);
      
      actAndLog.handleRemoveLearnEpEntity(
        par, 
        par.user, 
        learnEp, 
        learnEpVersion, 
        par.learnEpEntity, 
        entity, 
        learnEpEntityCircleURIsBefore,
        learnEpEntityCircleURIsAfter, 
        false, //calledFromRemove, 
        par.shouldCommit);
      
      actAndLog.handleAddLearnEpEntity(
        par, 
        par.user, 
        learnEp, 
        learnEpVersion, 
        par.learnEpEntity, 
        entity, 
        learnEpEntityCircleURIsBefore, 
        learnEpEntityCircleURIsAfter, 
        false, //calledFromAdd,
        par.shouldCommit);
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean learnEpVersionEntityUpdate(final SSLearnEpVersionEntityUpdatePar par) throws SSErr{
    
    try{
      
      final SSEntity learnEpEntity =
        sql.getEntityTest(
          par, 
          SSConf.systemUserUri,
          par.user,
          par.learnEpEntity,
          par.withUserRestriction);
      
      if(learnEpEntity == null){
        return false;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.updateEntity(
        par, 
        learnEpEntity.id,
        par.x,
        par.y);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public SSServRetI learnEpVersionCircleRemove(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpVersionCircleRemovePar par              = (SSLearnEpVersionCircleRemovePar) parA.getFromClient(clientType, parA, SSLearnEpVersionCircleRemovePar.class);
      final SSUri                           learnEp          = sql.getLearnEpForCircle        (par, par.learnEpCircle);
      final SSUri                           learnEpVersion   = sql.getLearnEpVersionForCircle (par, par.learnEpCircle);
      
      SSLearnEpAccessController.checkHasLock(
        par, 
        ((SSLearnEpConf) conf),
        par.user,
        learnEp);
      
      final List<SSEntity>                  learnEpCirclesBefore = commons.getLearnEpCirclesWithEntries  (this, par, par.user, learnEpVersion);
      final boolean                         worked               = learnEpVersionCircleRemove(par);
      final SSLearnEpVersionCircleRemoveRet ret                  = SSLearnEpVersionCircleRemoveRet.get(worked);
      
      if(!worked){
        return ret;
      }
        
      List<SSUri> entityURIs;
        
      for(SSEntity learnEpCircleBefore : learnEpCirclesBefore){
        
        if(!SSStrU.isEqual(par.learnEpCircle, learnEpCircleBefore)){
          continue;
        }
        
        entityURIs = commons.getEntityURIsFromLearnEpCircle((SSLearnEpCircle) learnEpCircleBefore);
        
        cat.removeCategories(
          par, 
          par.user, 
          entityURIs,  //entities
          SSCategoryLabel.get(SSStrU.toStr(learnEpCircleBefore.label)),  //categoryLabel
          par.shouldCommit);
        
        actAndLog.handleRemoveLearnEpVersionCircleWithEntities(
          par,
          par.user,
          learnEpVersion,
          learnEp,
          learnEpCircleBefore,
          entityURIs,
          par.shouldCommit);
        
        break;
      }
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean learnEpVersionCircleRemove(final SSLearnEpVersionCircleRemovePar par) throws SSErr{
    
    try{
      
      final SSEntity learnEpCircle =
        sql.getEntityTest(
          par, 
          SSConf.systemUserUri,
          par.user,
          par.learnEpCircle,
          par.withUserRestriction);
      
      if(learnEpCircle == null){
        return false;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.deleteCircle(par, learnEpCircle.id);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public SSServRetI learnEpVersionEntityRemove(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpVersionEntityRemovePar par            = (SSLearnEpVersionEntityRemovePar) parA.getFromClient(clientType, parA, SSLearnEpVersionEntityRemovePar.class);
      final SSUri                           learnEpVersion = sql.getLearnEpVersionForEntity       (par, par.learnEpEntity);
      final SSUri                           learnEp        = sql.getLearnEpForVersion             (par, learnEpVersion);
      final SSUri                           entity         = sql.getEntity                        (par, learnEpVersion, par.learnEpEntity);
      
      SSLearnEpAccessController.checkHasLock(
        par, 
        ((SSLearnEpConf) conf),
        par.user,
        learnEp);
      
      final List<SSEntity>                   learnEpCirclesBefore          = commons.getLearnEpCirclesWithEntries(this, par, par.user, learnEpVersion);
      final List<SSUri>                      learnEpEntityCircleURIsBefore = commons.getCircleURIsForLearnEpEntity(learnEpCirclesBefore, par.learnEpEntity);
      final boolean                          worked                        = learnEpVersionEntityRemove(par);
      final SSLearnEpVersionEntityRemoveRet  ret                           = SSLearnEpVersionEntityRemoveRet.get(worked);
      
      if(!worked){
        return ret;
      }
      
      final List<SSEntity> learnEpCirclesAfter          = commons.getLearnEpCirclesWithEntries            (this, par, par.user, learnEpVersion);
      final List<SSUri>    learnEpEntityCircleURIsAfter = commons.getCircleURIsForLearnEpEntity(learnEpCirclesAfter, par.learnEpEntity);
      
      cat.handleRemoveLearnEpEntity(
        par, 
        par.user, 
        entity, 
        learnEpEntityCircleURIsBefore, 
        learnEpEntityCircleURIsAfter, 
        par.shouldCommit);
      
      actAndLog.handleRemoveLearnEpEntity(
        par, 
        par.user, 
        learnEp, 
        learnEpVersion, 
        par.learnEpEntity, 
        entity, 
        learnEpEntityCircleURIsBefore,
        learnEpEntityCircleURIsAfter, 
        true, //calledFromRemove, 
        par.shouldCommit);

      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean learnEpVersionEntityRemove(final SSLearnEpVersionEntityRemovePar par) throws SSErr{
    
    try{
      
      final SSEntity learnEpEntity =
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          par.user,
          par.learnEpEntity,
          par.withUserRestriction);
      
      if(learnEpEntity == null){
        return false;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.deleteEntity(par, learnEpEntity.id);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return true;
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public SSServRetI learnEpTimelineStateSet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpTimelineStateSetPar par = (SSLearnEpTimelineStateSetPar) parA.getFromClient(clientType, parA, SSLearnEpTimelineStateSetPar.class);
      
      return SSLearnEpTimelineStateSetRet.get(learnEpTimelineStateSet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri learnEpTimelineStateSet(final SSLearnEpTimelineStateSetPar par) throws SSErr{
    
    try{
      
      final SSEntityServerI        entityServ    = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSLearnEpTimelineState timelineState =
        learnEpTimelineStateGet(
          new SSLearnEpTimelineStateGetPar(
            par,
            par.user,
            par.withUserRestriction));
      
      final SSUri timelineStateURI;
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      if(timelineState == null){
        
        timelineStateURI =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              par,
              par.user,
              SSConf.vocURICreate(),
              SSEntityE.learnEpTimelineState, //type,
              null, //label
              null,//description,
              null, //creationTime,
              null, //read,
              false, //setPublic
              true, //createIfNotExists
              false, //withUserRestriction
              false)); //shouldCommit)
        
        if(timelineStateURI == null){
          dbSQL.rollBack(par, par.shouldCommit);
          return null;
        }
        
        sql.createTimelineState(
          par,
          par.user,
          timelineStateURI,
          par.startTime,
          par.endTime);
        
      }else{
        timelineStateURI = timelineState.id;
        
        sql.updateTimelineState(
          par,
          timelineStateURI,
          par.startTime,
          par.endTime);
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      return timelineStateURI;
      
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
  public SSLearnEpCirclesWithEntriesGetRet learnEpVersionCirclesWithEntriesGet(final SSLearnEpVersionCirclesWithEntriesGetPar par) throws SSErr{
    
    try{
      
      final List<SSEntity>   entitiesInCircles = new ArrayList<>();
      final List<SSEntity>   allEntities       = new ArrayList<>();
      final List<SSEntity>   circles           = new ArrayList<>();
      final List<SSEntity>   orphans           = new ArrayList<>();
      final SSLearnEpVersion version =
        learnEpVersionGet(
          new SSLearnEpVersionGetPar(
            par, 
            par.user,
            par.learnEpVersion,
            false, //withUserRestriction
            par.invokeEntityHandlers)); //invokeEntityHandlers
      
      if(version == null){
        return new SSLearnEpCirclesWithEntriesGetRet(circles, orphans);
      }
      
      SSLearnEpCircle circle;
      SSLearnEpEntity entity;
      
      double first, firstDivisor, second, secondDivisor, firstResult, secondResult, result;
      
      for(SSEntity circleEntity : version.learnEpCircles){
        
        circle = (SSLearnEpCircle) circleEntity;
        
        for(SSEntity entityEntity : version.learnEpEntities){
          
          entity = (SSLearnEpEntity) entityEntity;
          
          SSEntity.addEntitiesDistinctWithoutNull(
            allEntities, 
            entity);
          
          //(xEntity - xCircle)^2 / rxCircle^2 + same for y <= 1 then the entiy is wihtin the circle
          
          first         = Math.pow(Math.subtractExact(entity.x.longValue() + 25, circle.xC.longValue()), 2);
          firstDivisor  = Math.pow(circle.xR.longValue(), 2);
          second        = Math.pow(Math.subtractExact(entity.y.longValue() + 25, circle.yC.longValue()), 2);
          secondDivisor = Math.pow(circle.yR.longValue(), 2);
          
          if(
            Double.doubleToRawLongBits(firstDivisor)  == 0 || 
            Double.doubleToRawLongBits(secondDivisor) == 0){
            continue;
          }
          
          firstResult  = first / firstDivisor;
          secondResult = second / secondDivisor;
          result       = firstResult + secondResult;
          
          if(result > 1){
            continue;
          }
          
          SSEntity.addEntitiesDistinctWithoutNull(
            entitiesInCircles, 
            entity);
            
          SSEntity.addEntitiesDistinctWithoutNull(
            circle.entries,
            entity);
        }
      }
      
      SSEntity.addEntitiesDistinctWithoutNull(
        circles,
        version.learnEpCircles);
      
      for(SSEntity entityFromAll : allEntities){
        
        if(SSStrU.contains(entitiesInCircles, entityFromAll)){
          continue;
        }
        
        SSEntity.addEntitiesDistinctWithoutNull(orphans, entityFromAll);
      }
      
      return new SSLearnEpCirclesWithEntriesGetRet(circles, orphans);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI learnEpTimelineStateGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpTimelineStateGetPar par = (SSLearnEpTimelineStateGetPar) parA.getFromClient(clientType, parA, SSLearnEpTimelineStateGetPar.class);
      
      return SSLearnEpTimelineStateGetRet.get(learnEpTimelineStateGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSLearnEpTimelineState learnEpTimelineStateGet(final SSLearnEpTimelineStateGetPar par) throws SSErr{
    
    try{
      
      return sql.getTimelineState(par, par.user);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI learnEpVersionCurrentGet(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpVersionCurrentGetPar par = (SSLearnEpVersionCurrentGetPar) parA.getFromClient(clientType, parA, SSLearnEpVersionCurrentGetPar.class);
      
      return SSLearnEpVersionCurrentGetRet.get(learnEpVersionCurrentGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSLearnEpVersion learnEpVersionCurrentGet(final SSLearnEpVersionCurrentGetPar par) throws SSErr{
    
    try{
      
      return learnEpVersionGet(
        new SSLearnEpVersionGetPar(
          par, 
          par.user,
          sql.getLearnEpCurrentVersionURI(par, par.user),
          par.withUserRestriction,
          par.invokeEntityHandlers));
      
    }catch(SSErr error){
      
      switch(error.code){
        case learnEpCurrentVersionNotSet:{
          SSServErrReg.regErrThrow(error, false);
          return null;
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
  public SSServRetI learnEpVersionCurrentSet(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpVersionCurrentSetPar par = (SSLearnEpVersionCurrentSetPar) parA.getFromClient(clientType, parA, SSLearnEpVersionCurrentSetPar.class);
      
      return SSLearnEpVersionCurrentSetRet.get(learnEpVersionCurrentSet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri learnEpVersionCurrentSet(final SSLearnEpVersionCurrentSetPar par) throws SSErr{
    
    try{
      
      final SSEntity learnEpVerison =
        sql.getEntityTest(
          par, 
          SSConf.systemUserUri,
          par.user,
          par.learnEpVersion,
          par.withUserRestriction);
      
      if(learnEpVerison == null){
        return null;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.setLearnEpCurrentVersion(par, par.user, par.learnEpVersion);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return par.learnEpVersion;
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
  public SSServRetI learnEpsLockHold(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpsLockHoldPar par = (SSLearnEpsLockHoldPar) parA.getFromClient(clientType, parA, SSLearnEpsLockHoldPar.class);
      
      return SSLearnEpsLockHoldRet.get(learnEpsLockHold(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSLearnEpLockHoldRet> learnEpsLockHold(final SSLearnEpsLockHoldPar par) throws SSErr{
    
    try{
      final List<SSLearnEpLockHoldRet> locks = new ArrayList<>();
      final SSLearnEpLockHoldPar       learnEpLockHoldPar;
      
      if(((SSLearnEpConf) conf).useEpisodeLocking){
        
        learnEpLockHoldPar =
          new SSLearnEpLockHoldPar(
            par, 
            par.user,
            null,
            true);
        
        if(!par.learnEps.isEmpty()){
          
          for(SSUri learnEp : par.learnEps){
            
            learnEpLockHoldPar.learnEp = learnEp;
            
            locks.add(learnEpLockHold(learnEpLockHoldPar));
          }
        }else{
          
          for(SSUri learnEp : sql.getLearnEpURIs(par, par.user)){
            
            learnEpLockHoldPar.learnEp = learnEp;
            
            locks.add(learnEpLockHold(learnEpLockHoldPar));
          }
        }
        
        return locks;
        
      }else{
        return locks;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSLearnEpLockHoldRet learnEpLockHold(final SSLearnEpLockHoldPar par) throws SSErr{
    
    try{
      final SSLearnEpLockHoldRet ret;
      
      if(((SSLearnEpConf) conf).useEpisodeLocking){
        
        ret =
          SSLearnEpLockHoldRet.get(
            par.learnEp,
            SSLearnEpAccessController.isLocked(par.learnEp),
            SSLearnEpAccessController.hasLock(
              par.user,
              par.learnEp),
            SSLearnEpAccessController.getRemainingTime(par.learnEp));
        
      }else{
        
        ret =
          SSLearnEpLockHoldRet.get(
            par.learnEp,
            false,
            false,
            0L);
      }
      
      return ret;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI learnEpLockSet(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpLockSetPar par = (SSLearnEpLockSetPar) parA.getFromClient(clientType, parA, SSLearnEpLockSetPar.class);
      
      return SSLearnEpLockSetRet.get(learnEpLockSet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean learnEpLockSet(final SSLearnEpLockSetPar par) throws SSErr{
    
    try{
      if(par.withUserRestriction){
        
        final SSEntity learnEp =
          sql.getEntityTest(
            par, 
            SSConf.systemUserUri,
            par.user,
            par.learnEp,
            par.withUserRestriction);
        
        if(learnEp == null){
          return false;
        }
        
        if(par.forUser == null){
          par.forUser = par.user;
        }
      }else{
        
        if(par.forUser == null){
          throw SSErr.get(SSErrE.parameterMissing);
        }
      }
      
      boolean lockResult = false;
      
      if(((SSLearnEpConf) conf).useEpisodeLocking){
        
        lockResult = SSLearnEpAccessController.lock(par.forUser, par.learnEp);
      }
      
      return lockResult;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public SSServRetI learnEpLockRemove(SSClientE clientType, SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLearnEpLockRemovePar par = (SSLearnEpLockRemovePar) parA.getFromClient(clientType, parA, SSLearnEpLockRemovePar.class);
      
      return SSLearnEpLockRemoveRet.get(learnEpLockRemove(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean learnEpLockRemove(final SSLearnEpLockRemovePar par) throws SSErr{
    
    try{
      boolean unLockResult = false;
      
      if(!((SSLearnEpConf) conf).useEpisodeLocking){
        return unLockResult;
      }
      
      if(par.withUserRestriction){
        
        final SSEntity learnEp =
          sql.getEntityTest(
            par, 
            SSConf.systemUserUri,
            par.user,
            par.learnEp,
            par.withUserRestriction);
        
        if(learnEp == null){
          return false;
        }
        
        if(par.forUser == null){
          par.forUser = par.user;
        }
        
        if(!SSLearnEpAccessController.hasLock(par.forUser, par.learnEp)){
          return unLockResult;
        }
      }
      
      unLockResult = SSLearnEpAccessController.unLock(par.learnEp);
      
      return unLockResult;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  private List<SSUri> getLearnEpAffiliatedURIs(
    final SSServPar servPar,
    final SSUri           learnEp) throws SSErr{
    
    try{
      
      final List<SSUri>  learnEpContentUris = new ArrayList<>();
      SSLearnEpVersion   learnEpVersion;
      
      learnEpContentUris.add(learnEp);
      
      for(SSUri learnEpVersionUri : sql.getLearnEpVersionURIs(servPar, learnEp)){
        
        learnEpContentUris.add(learnEpVersionUri);
        
        learnEpVersion =
          sql.getLearnEpVersion(
            servPar, 
            learnEpVersionUri,
            true, //setCircles,
            true); //setEntities,
        
        for(SSEntity circle : learnEpVersion.learnEpCircles){
          learnEpContentUris.add(circle.id);
        }
        
        for(SSEntity learnEpEntity : learnEpVersion.learnEpEntities){
          
          learnEpContentUris.add   (learnEpEntity.id);
          learnEpContentUris.add   (((SSLearnEpEntity) learnEpEntity).entity.id);
          
//          learnEpContentUris.addAll(
//            getLearnEpEntityAttachedEntities(
//              user,
//              ((SSLearnEpEntity) learnEpEntity).entity.id,
//              withUserRestriction));
        }
      }
      
      SSStrU.distinctWithoutNull2(learnEpContentUris);
      
      return learnEpContentUris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

//  private List<SSUri> getLearnEpEntityAttachedEntities(
//    final SSUri   user,
//    final SSUri   entity,
//    final boolean withUserRestriction) throws SSErr{
//
//    try{
//      final List<SSEntity> attachedEntities = new ArrayList<>();
//
//      SSEntity.addEntitiesDistinctWithoutNull(
//        attachedEntities,
//        ((SSFileServerI) SSServReg.getServ(SSFileServerI.class)).filesGet(
//          new SSEntityFilesGetPar(
//            user,
//            entity,
//            withUserRestriction, //withUserRestcrition
//            false)));   //invokeEntityHandlers
//
//      SSEntity.addEntitiesDistinctWithoutNull(
//        attachedEntities,
//        ((SSImageServerI) SSServReg.getServ(SSImageServerI.class)).imagesGet(
//          new SSImagesGetPar(
//            user,
//            entity, //entity
//            SSImageE.thumb, //imageType
//            withUserRestriction)));
//
//      return SSUri.getDistinctNotNullFromEntities(attachedEntities);
//
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }