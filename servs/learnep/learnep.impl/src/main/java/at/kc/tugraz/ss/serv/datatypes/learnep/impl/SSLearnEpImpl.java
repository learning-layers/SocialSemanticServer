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
package at.kc.tugraz.ss.serv.datatypes.learnep.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesFromEntityEntitiesAdd;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpClientI;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.conf.SSLearnEpConf;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpCircle;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpEntity;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpTimelineState;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpLockHoldPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpLockRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpLockSetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityAddPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentSetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionTimelineStateGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionTimelineStateSetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpsGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpsLockHoldPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpCreateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpLockHoldRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpLockRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpLockSetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCircleAddRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionEntityAddRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCreateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCurrentGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCurrentSetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionTimelineStateGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCircleRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionEntityRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionTimelineStateSetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCircleUpdateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionEntityUpdateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionsGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpsGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpsLockHoldRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.access.SSLearnEpAccessController;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.activity.SSLearnEpActivityFct;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.misc.SSLearnEpMiscFct;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.sql.SSLearnEpSQLFct;
import at.tugraz.sss.serv.SSCircleContentAddedI;
import at.tugraz.sss.serv.SSCircleContentChangedPar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSCopyEntityI;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityCopiedI;
import at.tugraz.sss.serv.SSEntityCopiedPar;
import at.tugraz.sss.serv.SSEntityCopyPar;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServContainerI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;

public class SSLearnEpImpl
extends SSServImplWithDBA
implements
  SSLearnEpClientI,
  SSLearnEpServerI,
  SSDescribeEntityI,
  SSCircleContentAddedI,
  SSCopyEntityI,
  SSUsersResourcesGathererI{
  
  private final SSLearnEpSQLFct  sqlFct;
  private final SSLearnEpConf    learnEpConf;
  private final SSLearnEpMiscFct miscFct;
  private final SSEntityServerI  entityServ;
  private final SSCircleServerI  circleServ;
  
  public SSLearnEpImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    learnEpConf     = (SSLearnEpConf) conf;
    this.sqlFct     = new SSLearnEpSQLFct(this);
    this.miscFct    = new SSLearnEpMiscFct();
    this.entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
    this.circleServ = (SSCircleServerI) SSServReg.getServ(SSCircleServerI.class);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws Exception{
    
    switch(entity.type){
    
      case learnEp:{
        
        return SSLearnEp.get(
          learnEpGet(
            new SSLearnEpGetPar(
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
    final List<String>             allUsers,
    final Map<String, List<SSUri>> usersResources) throws Exception{
    
    for(String user : allUsers){
      
      for(SSUri learnEp : sqlFct.getLearnEpURIsForUser(SSUri.get(user))){
        
        for(SSUri versionUri : sqlFct.getLearnEpVersionURIs(learnEp)){
          
          for(SSEntity entity : sqlFct.getLearnEpVersion(versionUri).learnEpEntities){
            
            if(usersResources.containsKey(user)){
              usersResources.get(user).add(((SSLearnEpEntity)entity).entity.id);
            }else{
              
              final List<SSUri> resourceList = new ArrayList<>();
              
              resourceList.add(((SSLearnEpEntity)entity).entity.id);
              
              usersResources.put(user, resourceList);
            }
          }
        }
      }
    }
    
    for(Map.Entry<String, List<SSUri>> resourcesPerUser : usersResources.entrySet()){
      SSStrU.distinctWithoutNull2(resourcesPerUser.getValue());
    }
  }
  
  @Override
  public void circleContentAdded(final SSCircleContentChangedPar par) throws Exception{
    
    try{
      
      final List<SSUri>    learnEpContentURIs = new ArrayList<>();
      final List<SSEntity> entitiesToAdd      = new ArrayList<>();
      
      for(SSEntity entityToAdd : par.entitiesToAdd){
        
        switch(entityToAdd.type){
          
          case learnEp:{
            
            if(SSStrU.contains(par.recursiveEntities, entityToAdd)){
              continue;
            }else{
              SSUri.addDistinctWithoutNull(par.recursiveEntities, entityToAdd.id);
            }
            
            for(SSUri userToPushEntityTo : par.usersToPushEntitiesTo){
              
              if(sqlFct.ownsUserLearnEp(userToPushEntityTo, entityToAdd.id)){
                continue;
              }
              
              sqlFct.addLearnEp(entityToAdd.id, userToPushEntityTo);
            }
            
            learnEpContentURIs.clear();
            
            learnEpContentURIs.addAll(miscFct.getLearnEpContentURIs(par.user, sqlFct, entityToAdd.id));
              
            circleServ.circleEntitiesAdd(
              new SSCircleEntitiesAddPar(
                null,
                null,
                par.user,
                par.circle,
                learnEpContentURIs,
                false,
                false));
            
            if(!par.usersToPushEntitiesTo.isEmpty()){
              
              circleServ.circleUsersAdd(
                new SSCircleUsersAddPar(
                  null,
                  null,
                  par.user,
                  par.circle,
                  sqlFct.getLearnEpUserURIs(entityToAdd.id),
                  false,
                  false));
              
              SSLearnEpActivityFct.shareLearnEp(
                par.user,
                entityToAdd.id,
                par.usersToPushEntitiesTo);
            }
            
            //call circleContentAddAgain for all those entities added from within the learn ep
            entitiesToAdd.clear();
            
            for(SSUri learnEpContentURI : learnEpContentURIs){
              
              if(SSStrU.contains(par.recursiveEntities, learnEpContentURI)){
                continue;
              }
              
              entitiesToAdd.add(
                entityServ.entityGet(
                  new SSEntityGetPar(
                    null, 
                    null, 
                    par.user, 
                    learnEpContentURI, 
                    true, 
                    null)));
                
              par.recursiveEntities.add(learnEpContentURI);
            }
            
            if(!entitiesToAdd.isEmpty()){
              
              for(SSServContainerI serv : SSServReg.inst.getServsHandlingCircleContentAdded()){

                ((SSCircleContentAddedI) serv.serv()).circleContentAdded(
                  new SSCircleContentChangedPar(
                    SSUri.getDistinctNotNullFromEntities(entitiesToAdd), //recursiveEntitiesToAdd
                    par.user,
                    par.circle,
                    par.isCirclePublic, //isPublicCircle
                    par.usersToAdd,  //usersToAdd
                    entitiesToAdd, //entitiesToAdd,
                    par.usersToPushEntitiesTo,  //usersToPushEntitiesTo
                    par.circleUsers, //circleUsers
                    par.circleEntities)); //circleEntities
              }
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
  public void copyEntity(
    final SSEntity                  entity,
    final SSEntityCopyPar           par) throws Exception{
    
    try{
      
      if(!SSStrU.equals(entity.type, SSEntityE.learnEp)){
        return;
      }
      
      final List<SSEntity> copiedEntities = new ArrayList<>();
      SSUri                copyVersionUri;
      SSUri                copyLearnEpUri;
      SSEntityCopiedPar    entityCopiedPar;
      
      final SSLearnEp learnEp = 
        learnEpGet(
          new SSLearnEpGetPar(
            par.user, 
            entity.id, 
            par.withUserRestriction, 
            true)); //invokeEntityHandlers

      for(SSUri forUser : par.forUsers){
        
        copiedEntities.clear();
          
        copyLearnEpUri =
          learnEpCreate(
            new SSLearnEpCreatePar(
              null,
              null,
              forUser,
              learnEp.label,
              learnEp.description,
              false));
        
        for(SSEntity version : learnEp.entries){
          
          copyVersionUri =
            learnEpVersionCreate(
              new SSLearnEpVersionCreatePar(
                null,
                null,
                forUser,
                copyLearnEpUri,
                false));
          
          for(SSEntity circle : ((SSLearnEpVersion) version).learnEpCircles){
            
            if(SSStrU.contains(par.entitiesToExclude, circle.id)){
              continue;
            }
            
            learnEpVersionCircleAdd(
              new SSLearnEpVersionCircleAddPar(
                null,
                null,
                forUser,
                copyVersionUri,
                circle.label,
                ((SSLearnEpCircle) circle).xLabel,
                ((SSLearnEpCircle) circle).yLabel,
                ((SSLearnEpCircle) circle).xR,
                ((SSLearnEpCircle) circle).yR,
                ((SSLearnEpCircle) circle).xC,
                ((SSLearnEpCircle) circle).yC,
                false));
          }
          
          for(SSEntity learnEpEntity : ((SSLearnEpVersion) version).learnEpEntities){
            
            if(
              SSStrU.contains(par.entitiesToExclude, learnEpEntity.id) ||
              SSStrU.contains(par.entitiesToExclude, ((SSLearnEpEntity) learnEpEntity).entity)){
              continue;
            }
            
            learnEpVersionEntityAdd(
              new SSLearnEpVersionEntityAddPar(
                null,
                null,
                forUser,
                copyVersionUri,
                ((SSLearnEpEntity) learnEpEntity).entity.id,
                ((SSLearnEpEntity) learnEpEntity).x,
                ((SSLearnEpEntity) learnEpEntity).y,
                false));
            
            copiedEntities.add(((SSLearnEpEntity) learnEpEntity).entity);
          }
          
          if(((SSLearnEpVersion) version).learnEpTimelineState != null){
            
            learnEpVersionTimelineStateSet(
              new SSLearnEpVersionTimelineStateSetPar(
                null,
                null,
                forUser,
                copyVersionUri,
                ((SSLearnEpVersion) version).learnEpTimelineState.startTime,
                ((SSLearnEpVersion) version).learnEpTimelineState.endTime,
                false));
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
        
        for(SSServContainerI entityHandler : SSServReg.inst.getServsHandlingEntityCopied()){
          ((SSEntityCopiedI) entityHandler.serv()).entityCopied(entityCopiedPar);
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public void learnEpsGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpsGetPar par = (SSLearnEpsGetPar) parA.getFromJSON(SSLearnEpsGetPar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpsGetRet.get(learnEpsGet(par)));
  }

  @Override
  public SSLearnEp learnEpGet(final SSLearnEpGetPar par) throws Exception{

    try{
      
      if(par.withUserRestriction){
       
        if(!SSServCallerU.canUserRead(par.user, par.learnEp)){
          return null;
        }
      }
      
      final SSLearnEp             learnEp; 
      SSEntityDescriberPar descPar;
      
      if(par.invokeEntityHandlers){
        descPar                  = new SSEntityDescriberPar(par.learnEp);
        descPar.setRead          = par.setRead;
        descPar.setCircleTypes   = par.setCircleTypes;
      }else{
        descPar = null;
      }
        
      learnEp =
        SSLearnEp.get(
          sqlFct.getLearnEp(par.learnEp),
          entityServ.entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              par.learnEp,
              par.withUserRestriction,
              descPar)));
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(null);
      }else{
        descPar = null;
      }

      SSEntity.addEntitiesDistinctWithoutNull(
        learnEp.users,
        entityServ.entitiesGet(
          new SSEntitiesGetPar(
            null,
            null,
            par.user,
            sqlFct.getLearnEpUserURIs(learnEp.id),
            null, //types,
            descPar, //descPar
            par.withUserRestriction))); //withUserRestriction
      
      SSEntity.addEntitiesDistinctWithoutNull(
        learnEp.entries,
        learnEpVersionsGet(
          new SSLearnEpVersionsGetPar(
            null,
            null,
            par.user,
            learnEp.id,
            par.withUserRestriction,
            par.invokeEntityHandlers)));
        
      if(!learnEpConf.useEpisodeLocking){
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
  public List<SSEntity> learnEpsGet(final SSLearnEpsGetPar par) throws Exception{

    try{
      
      if(par.withUserRestriction){
        
        if(par.user == null){
          throw new SSErr(SSErrE.parameterMissing);
        }
      }
      
      final List<SSEntity> learnEps = new ArrayList<>();
      SSLearnEpGetPar      learnEpGetPar;
        
      for(SSUri learnEpURI : sqlFct.getLearnEpURIsForUser(par.user)){

        learnEpGetPar =
          new SSLearnEpGetPar(
            par.user,
            learnEpURI,
            par.withUserRestriction,
            par.invokeEntityHandlers);
        
        learnEpGetPar.setRead        = par.setRead;
        learnEpGetPar.setCircleTypes = par.setCircleTypes;
        
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
  public void learnEpVersionsGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpVersionsGetPar par = (SSLearnEpVersionsGetPar) parA.getFromJSON(SSLearnEpVersionsGetPar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionsGetRet.get(learnEpVersionsGet(par)));
  }
  
  @Override
  public List<SSEntity> learnEpVersionsGet(final SSLearnEpVersionsGetPar par) throws Exception{

    try{

      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.learnEp)){
          return new ArrayList<>();
        }
      }

      final List<SSEntity> learnEpVersions        = new ArrayList<>();
      
      for(SSUri learnEpVersionUri : sqlFct.getLearnEpVersionURIs(par.learnEp)){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          learnEpVersions,
          learnEpVersionGet(
            new SSLearnEpVersionGetPar(
              null,
              null,
              par.user,
              learnEpVersionUri,
              par.withUserRestriction,
              par.invokeEntityHandlers)));
      }
      
      return learnEpVersions;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpVersionGetPar par = (SSLearnEpVersionGetPar) parA.getFromJSON(SSLearnEpVersionGetPar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionGetRet.get(learnEpVersionGet(par)));
  }
  
   @Override
  public SSLearnEpVersion learnEpVersionGet(final SSLearnEpVersionGetPar par) throws Exception{

    try{

      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.learnEpVersion)){
          return null;
        }
      }
      
      final List<SSEntity> learnEpVersionEntities = new ArrayList<>();
      final List<SSEntity> learnEpVersionCircles  = new ArrayList<>();
      SSEntityDescriberPar descPar;
      SSLearnEpVersion     learnEpVersion;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(par.learnEpVersion);
      }else{
        descPar = null;
      }
      
      learnEpVersion =
        SSLearnEpVersion.get(
          sqlFct.getLearnEpVersion(par.learnEpVersion),
          entityServ.entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              par.learnEpVersion,
              par.withUserRestriction,
              descPar)));
      
      for(SSEntity learnEpEntity : learnEpVersion.learnEpEntities){
        
        descPar = null;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          learnEpVersionEntities,
          SSLearnEpEntity.get(
            (SSLearnEpEntity) learnEpEntity,
            entityServ.entityGet(
              new SSEntityGetPar(
                null,
                null,
                par.user,
                learnEpEntity.id,
                par.withUserRestriction,
                descPar)))); //descPar
      }
      
      learnEpVersion.learnEpEntities.clear();
      learnEpVersion.learnEpEntities.addAll(learnEpVersionEntities);
      
      for(SSEntity learnEpVersionEntity : learnEpVersionEntities){
        
        if(par.invokeEntityHandlers){
          descPar = new SSEntityDescriberPar(((SSLearnEpEntity)learnEpVersionEntity).entity.id);
          
          descPar.setOverallRating = true;
          descPar.setTags          = true;
        }else{
          descPar = null;
        }
        
        ((SSLearnEpEntity)learnEpVersionEntity).entity =
          entityServ.entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              ((SSLearnEpEntity)learnEpVersionEntity).entity.id,
              par.withUserRestriction,
              descPar));
      }
      
      for(SSEntity learnEpCircle : learnEpVersion.learnEpCircles){
        
        descPar = null;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          learnEpVersionCircles,
          SSLearnEpCircle.get(
            (SSLearnEpCircle) learnEpCircle,
            entityServ.entityGet(
              new SSEntityGetPar(
                null,
                null,
                par.user,
                learnEpCircle.id,
                par.withUserRestriction,
                descPar)))); //descPar
      }
      
      learnEpVersion.learnEpCircles.clear();
      learnEpVersion.learnEpCircles.addAll(learnEpVersionCircles);
      
      return learnEpVersion;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpRemove(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpRemovePar par = (SSLearnEpRemovePar) parA.getFromJSON(SSLearnEpRemovePar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpRemoveRet.get(learnEpRemove(par)));
  }
  
  @Override
  public SSUri learnEpRemove(final SSLearnEpRemovePar par) throws Exception{

    try{
      
      if(par.withUserRestriction){
      
        if(!SSServCallerU.canUserEdit(par.user, par.learnEp)){
          return null;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.removeLearnEpForUser   (par.user, par.learnEp);
      sqlFct.deleteCurrentEpVersion (par.user);
      
      dbSQL.commit(par.shouldCommit);

      return par.learnEp;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpRemove(par);
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
  public void learnEpVersionCreate(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpVersionCreatePar par = (SSLearnEpVersionCreatePar) parA.getFromJSON(SSLearnEpVersionCreatePar.class);
    
    final SSUri result = learnEpVersionCreate(par);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionCreateRet.get(result));
    
//    SSLearnEpActivityFct.createLearnEpVersion(new SSLearnEpVersionCreatePar(parA), result);
  }
  
  @Override
  public SSUri learnEpVersionCreate(final SSLearnEpVersionCreatePar par) throws Exception{

    try{
      final SSUri                     learnEpVersionUri = SSServCaller.vocURICreate();

      if(par.withUserRestriction){
      
        if(!SSServCallerU.canUserEdit(par.user, par.learnEp)){
          return null;
        }
      }
      
      SSLearnEpAccessController.checkHasLock(learnEpConf, par.user, par.learnEp);
      
      dbSQL.startTrans(par.shouldCommit);

      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          learnEpVersionUri,
          SSEntityE.learnEpVersion, //type,
          null, //label
          null, //description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          false, //withUserRestriction
          false)); //shouldCommit)

      for(SSEntity entityUserCircle : 
        circleServ.circlesGet(
          new SSCirclesGetPar(
            null, 
            null, 
            par.user, 
            par.learnEp, //entity
            null,  //entityTypesToIncludeOnly
            false, //withUserRestriction
            true, //withSystemCircles
            false))){ //invokeEntityHandlers

        circleServ.circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            null, 
            null, 
            par.user, 
            entityUserCircle.id, 
            SSUri.asListWithoutNullAndEmpty(learnEpVersionUri), 
            false,
            false));
      }

      sqlFct.createLearnEpVersion(
        learnEpVersionUri,
        par.learnEp);

      dbSQL.commit(par.shouldCommit);

      return learnEpVersionUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionCreate(par);
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
  public void learnEpVersionCircleAdd(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);
    
    final SSLearnEpVersionCircleAddPar par = (SSLearnEpVersionCircleAddPar) parA.getFromJSON(SSLearnEpVersionCircleAddPar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionCircleAddRet.get(learnEpVersionCircleAdd(par)));
  }
  
  @Override
  public SSUri learnEpVersionCircleAdd(final SSLearnEpVersionCircleAddPar par) throws Exception{

    try{
      final SSUri                        circleUri  = SSServCaller.vocURICreate();
      final SSUri                        learnEp;
      
      if(par.withUserRestriction){
      
        if(!SSServCallerU.canUserEdit(par.user, par.learnEpVersion)){
          return null;
        }
      }

      learnEp = sqlFct.getLearnEpForVersion(par.learnEpVersion);
      
      SSLearnEpAccessController.checkHasLock (learnEpConf, par.user, learnEp);
      
      dbSQL.startTrans(par.shouldCommit);
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          circleUri,
          SSEntityE.learnEpCircle, //type,
          par.label, //label
          null, //description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          false, //withUserRestriction
          false)); //shouldCommit)
      
      for(SSEntity entityUserCircle : 
        circleServ.circlesGet(
          new SSCirclesGetPar(
            null, 
            null, 
            par.user, 
            par.learnEpVersion, 
            null, 
            false, 
            true, 
            false))){

        circleServ.circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            null, 
            null, 
            par.user, 
            entityUserCircle.id, 
            SSUri.asListWithoutNullAndEmpty(circleUri), 
            false,
            false));
      }
      
      sqlFct.addCircleToLearnEpVersion(
        circleUri,
        par.learnEpVersion,
        par.label,
        par.xLabel,
        par.yLabel,
        par.xR,
        par.yR,
        par.xC,
        par.yC);
      
      SSLearnEpActivityFct.addCircleToLearnEpVersion(par, circleUri, learnEp);

      dbSQL.commit(par.shouldCommit);

      return circleUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionCircleAdd(par);
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
  public void learnEpVersionEntityAdd(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpVersionEntityAddPar par = (SSLearnEpVersionEntityAddPar) parA.getFromJSON(SSLearnEpVersionEntityAddPar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionEntityAddRet.get(learnEpVersionEntityAdd(par)));
  }
  
  @Override
  public SSUri learnEpVersionEntityAdd(final SSLearnEpVersionEntityAddPar par) throws Exception{

    try{
      final SSUri                        learnEpEntityUri = SSServCaller.vocURICreate();
      final List<SSUri>                  entities         = new ArrayList<>();
      final SSUri                        learnEp;

      if(par.withUserRestriction){
      
        if(
          !SSServCallerU.canUserEdit(par.user, par.learnEpVersion) ||
          !SSServCallerU.canUserEdit(par.user, par.entity)){
          
          return null;
        }
      }

      learnEp = sqlFct.getLearnEpForVersion(par.learnEpVersion);
      
      SSLearnEpAccessController.checkHasLock (learnEpConf, par.user, learnEp);

      dbSQL.startTrans(par.shouldCommit);
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          learnEpEntityUri,
          SSEntityE.learnEpEntity, //type,
          null, //label
          null, //description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          false, //withUserRestriction
          false)); //shouldCommit)
            
      //TODO replace this parts with circleContentedChanged:
      //0. first split / refactor circleContentChanged to. circleEntitiesAdded, circleUsersAdded, sharedWithUsers
      //1. dont retrieve entities for learn ep entity here anymore
      //2. extend circlesFromEntityEntitiesAdd to call "circleContentChanged" upon each circle to be touched
      //3. for each entity targeted in "circleContentChanged" call "circleContentChanged" for its affiliated entities
      //4. make sure to avoid recursion on entites via list keeping all entities for "circleContentChanged" was called so far
      //5. call this process whereever "circlesFromEntityEntitiesAdd" is/can be called
      
      entities.add   (learnEpEntityUri);
      entities.add   (par.entity);
      entities.addAll(miscFct.getLearnEpEntityAttachedEntities(par.user, par.entity));
      
      circleServ.circlesFromEntityEntitiesAdd(
        new SSCirclesFromEntityEntitiesAdd(
          null, 
          null, 
          par.user, 
          par.learnEpVersion, 
          entities, 
          false));
     
      sqlFct.addEntityToLearnEpVersion(
        learnEpEntityUri,
        par.learnEpVersion,
        par.entity,
        par.x,
        par.y);

      SSLearnEpActivityFct.addEntityToLearnEpVersion(par, learnEpEntityUri, learnEp);
      
      dbSQL.commit(par.shouldCommit);

      return learnEpEntityUri;

    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionEntityAdd(par);
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
  public void learnEpCreate(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpCreatePar par = (SSLearnEpCreatePar) parA.getFromJSON(SSLearnEpCreatePar.class);

    final SSUri learnEp = learnEpCreate(par);
    
    sSCon.writeRetFullToClient(SSLearnEpCreateRet.get(learnEp));
    
//    SSLearnEpActivityFct.createLearnEp(new SSLearnEpCreatePar(parA), learnEp);
  }
  
  @Override
  public SSUri learnEpCreate(final SSLearnEpCreatePar par) throws Exception{

    try{
      final SSUri              learnEpUri = SSServCaller.vocURICreate();
      
      dbSQL.startTrans(par.shouldCommit);
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          learnEpUri,
          SSEntityE.learnEp, //type,
          par.label, //label
          par.description,//description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          false, //withUserRestriction
          false)); //shouldCommit)
            
      sqlFct.createLearnEp(learnEpUri, par.user);
      
      dbSQL.commit(par.shouldCommit);

      return learnEpUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpCreate(par);
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
  public void learnEpVersionCircleUpdate(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpVersionCircleUpdatePar par = (SSLearnEpVersionCircleUpdatePar) parA.getFromJSON(SSLearnEpVersionCircleUpdatePar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionCircleUpdateRet.get(learnEpVersionCircleUpdate(par)));
  }
  
  @Override
  public Boolean learnEpVersionCircleUpdate(final SSLearnEpVersionCircleUpdatePar par) throws Exception{

    try{

      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserEdit(par.user, par.learnEpCircle)){
          return null;
        }
      }

      final SSUri learnEpVersion = sqlFct.getLearnEpVersionForCircle  (par.learnEpCircle);
      final SSUri learnEp        = sqlFct.getLearnEpForVersion        (learnEpVersion);
      
      SSLearnEpAccessController.checkHasLock (learnEpConf, par.user, learnEp);
      
      dbSQL.startTrans(par.shouldCommit);
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          par.learnEpCircle,
          SSEntityE.learnEpCircle, //type,
          par.label, //label
          null,//description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          false, //withUserRestriction
          false)); //shouldCommit)
      
      sqlFct.updateCircle(
        par.learnEpCircle,
        par.xLabel,
        par.yLabel,
        par.xR,
        par.yR,
        par.xC,
        par.yC);

      SSLearnEpActivityFct.handleLearnEpVersionUpdateCircle(par, learnEpVersion, learnEp);
      
      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionCircleUpdate(par);
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
  public void learnEpVersionEntityUpdate(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpVersionEntityUpdatePar par = (SSLearnEpVersionEntityUpdatePar) parA.getFromJSON(SSLearnEpVersionEntityUpdatePar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionEntityUpdateRet.get(learnEpVersionEntityUpdate(par)));
  }
  
  @Override
  public Boolean learnEpVersionEntityUpdate(final SSLearnEpVersionEntityUpdatePar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        
        if(
          !SSServCallerU.canUserEdit(par.user, par.learnEpEntity) ||
          !SSServCallerU.canUserEdit(par.user, par.entity)){
          return null;
        }
      }
      
      final SSUri learnEpVersion     = sqlFct.getLearnEpVersionForEntity(par.learnEpEntity);
      final SSUri learnEp            = sqlFct.getLearnEpForVersion      (learnEpVersion);
      
      SSLearnEpAccessController.checkHasLock (learnEpConf, par.user, learnEp);
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(par.entity != null){
        
        circleServ.circlesFromEntityEntitiesAdd(
          new SSCirclesFromEntityEntitiesAdd(
            null,
            null,
            par.user,
            par.learnEpEntity,
            SSUri.asListWithoutNullAndEmpty(par.entity),
            false));
      }
      
      sqlFct.updateEntity(
        par.learnEpEntity,
        par.entity,
        par.x,
        par.y);

      if(
        par.entity        == null &&
        par.learnEpEntity != null){
        
        par.entity = sqlFct.getEntity(learnEpVersion, par.learnEpEntity);
      }
      
      SSLearnEpActivityFct.handleLearnEpVersionUpdateEntity(par, learnEpVersion, learnEp);
      
      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionEntityUpdate(par);
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
  public void learnEpVersionCircleRemove(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpVersionCircleRemovePar par = (SSLearnEpVersionCircleRemovePar) parA.getFromJSON(SSLearnEpVersionCircleRemovePar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionCircleRemoveRet.get(learnEpVersionCircleRemove(par)));
  }
  
  @Override
  public Boolean learnEpVersionCircleRemove(final SSLearnEpVersionCircleRemovePar par) throws Exception{

    try{

      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserEdit(par.user, par.learnEpCircle)){
          return null;
        }
      }

      final SSUri learnEpVersion     = sqlFct.getLearnEpVersionForCircle(par.learnEpCircle);
      final SSUri learnEp            = sqlFct.getLearnEpForVersion      (learnEpVersion);
        
      SSLearnEpAccessController.checkHasLock (learnEpConf, par.user, learnEp);
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.deleteCircle(par.learnEpCircle);
      
      SSLearnEpActivityFct.removeLearnEpVersionCircle(par, learnEpVersion, learnEp);
      
      dbSQL.commit(par.shouldCommit);

      return true;
   }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionCircleRemove(par);
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
  public void learnEpVersionEntityRemove(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpVersionEntityRemovePar par = (SSLearnEpVersionEntityRemovePar) parA.getFromJSON(SSLearnEpVersionEntityRemovePar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionEntityRemoveRet.get(learnEpVersionEntityRemove(par)));
  }
  
  @Override
  public Boolean learnEpVersionEntityRemove(final SSLearnEpVersionEntityRemovePar par) throws Exception{

    try{
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserEdit(par.user, par.learnEpEntity)){
          return null;
        }
      }

      final SSUri learnEpVersion       = sqlFct.getLearnEpVersionForEntity       (par.learnEpEntity);
      final SSUri learnEp              = sqlFct.getLearnEpForVersion             (learnEpVersion);
      final SSUri entity               = sqlFct.getEntity                        (learnEpVersion, par.learnEpEntity);
      
      SSLearnEpAccessController.checkHasLock (learnEpConf, par.user, learnEp);
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.deleteEntity(par.learnEpEntity);
      
      SSLearnEpActivityFct.removeLearnEpVersionEntity(par, learnEpVersion, entity, learnEp);

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionEntityRemove(par);
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
  public void learnEpVersionTimelineStateSet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpVersionTimelineStateSetPar par = (SSLearnEpVersionTimelineStateSetPar) parA.getFromJSON(SSLearnEpVersionTimelineStateSetPar.class);
      
    sSCon.writeRetFullToClient(SSLearnEpVersionTimelineStateSetRet.get(learnEpVersionTimelineStateSet(par)));
  }
  
  @Override
  public SSUri learnEpVersionTimelineStateSet(final SSLearnEpVersionTimelineStateSetPar par) throws Exception{

    try{

      final SSUri                               learnEpTimelineStateUri = SSServCaller.vocURICreate();

      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserEdit(par.user, par.learnEpVersion)){
          return null;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          learnEpTimelineStateUri,
          SSEntityE.learnEpTimelineState, //type,
          null, //label
          null,//description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          false, //withUserRestriction
          false)); //shouldCommit)
      
      circleServ.circlesFromEntityEntitiesAdd(
        new SSCirclesFromEntityEntitiesAdd(
          null,
          null,
          par.user,
          par.learnEpVersion,
          SSUri.asListWithoutNullAndEmpty(learnEpTimelineStateUri),
          false));

      sqlFct.setLearnEpVersionTimelineState(
        learnEpTimelineStateUri,
        par.learnEpVersion,
        par.startTime,
        par.endTime);

      dbSQL.commit(par.shouldCommit);

      return learnEpTimelineStateUri;

    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionTimelineStateSet(par);
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
  public void learnEpVersionTimelineStateGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpVersionTimelineStateGetPar par = (SSLearnEpVersionTimelineStateGetPar) parA.getFromJSON(SSLearnEpVersionTimelineStateGetPar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionTimelineStateGetRet.get(learnEpVersionTimelineStateGet(par)));
  }
  
  @Override
  public SSLearnEpTimelineState learnEpVersionTimelineStateGet(final SSLearnEpVersionTimelineStateGetPar par) throws Exception{
    try{
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.learnEpVersion)){
          return null;
        }
      }
      
      return sqlFct.getLearnEpVersionTimelineState(par.learnEpVersion);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionCurrentGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpVersionCurrentGetPar par = (SSLearnEpVersionCurrentGetPar) parA.getFromJSON(SSLearnEpVersionCurrentGetPar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionCurrentGetRet.get(learnEpVersionCurrentGet(par)));
  }
  
  @Override
  public SSLearnEpVersion learnEpVersionCurrentGet(final SSLearnEpVersionCurrentGetPar par) throws Exception{
    
    try{
      
      return learnEpVersionGet(
        new SSLearnEpVersionGetPar(
          null, 
          null, 
          par.user, 
          sqlFct.getLearnEpVersion(sqlFct.getLearnEpCurrentVersionURI(par.user)).id,
          par.withUserRestriction, 
          par.invokeEntityHandlers));

    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.learnEpCurrentVersionNotSet)){
        SSServErrReg.regErrThrow(error, false);
      }else{
        SSServErrReg.regErrThrow(error);
      }
      
      return null;
    }
  }

  @Override
  public void learnEpVersionCurrentSet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpVersionCurrentSetPar par = (SSLearnEpVersionCurrentSetPar) parA.getFromJSON(SSLearnEpVersionCurrentSetPar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionCurrentSetRet.get(learnEpVersionCurrentSet(par)));
    
//    SSLearnEpActivityFct.setCurrentLearnEpVersion(new SSLearnEpVersionCurrentSetPar(parA));
  }

  @Override
  public SSUri learnEpVersionCurrentSet(final SSLearnEpVersionCurrentSetPar par) throws Exception{

    try{

      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserEdit(par.user, par.learnEpVersion)){
          return null;
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.setLearnEpCurrentVersion(par.user, par.learnEpVersion);

      dbSQL.commit(par.shouldCommit);

      return par.learnEpVersion;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionCurrentSet(par);
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
  public void learnEpsLockHold(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpsLockHoldPar par = (SSLearnEpsLockHoldPar) parA.getFromJSON(SSLearnEpsLockHoldPar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpsLockHoldRet.get(learnEpsLockHold(par)));
  }
  
  @Override
  public List<SSLearnEpLockHoldRet> learnEpsLockHold(final SSLearnEpsLockHoldPar par) throws Exception{
    
    try{
      final List<SSLearnEpLockHoldRet> locks = new ArrayList<>();
      
      if(learnEpConf.useEpisodeLocking){
        
        if(!par.learnEps.isEmpty()){
          
          for(SSUri learnEp : par.learnEps){
            
            locks.add(
              learnEpLockHold(
                new SSLearnEpLockHoldPar(
                  null, 
                  null, 
                  par.user, 
                  learnEp, 
                  true)));
          }
        }else{
          
          for(SSUri learnEp : sqlFct.getLearnEpURIsForUser(par.user)){
            
            locks.add(
              learnEpLockHold(
                new SSLearnEpLockHoldPar(
                  null, 
                  null, 
                  par.user, 
                  learnEp, 
                  true)));
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
  public SSLearnEpLockHoldRet learnEpLockHold(final SSLearnEpLockHoldPar par) throws Exception{

    try{
      final SSLearnEpLockHoldRet ret;

      if(par.withUserRestriction){
        SSServCallerU.canUserEditEntity(par.user, par.learnEp);
      }
      
      if(learnEpConf.useEpisodeLocking){
        
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
  public void learnEpLockSet(SSSocketCon sSCon, SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);

    final SSLearnEpLockSetPar par = (SSLearnEpLockSetPar) parA.getFromJSON(SSLearnEpLockSetPar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpLockSetRet.get(learnEpLockSet(par)));
  }
  
  @Override
  public Boolean learnEpLockSet(final SSLearnEpLockSetPar par) throws Exception{

    try{
      if(par.withUserRestriction){
        
        SSServCallerU.canUserEditEntity(par.user, par.learnEp);
        
        if(par.forUser == null){
          par.forUser = par.user;
        }
      }else{
        
        if(par.forUser == null){
          throw new Exception("forUser null");
        }
      }
      
      Boolean lockResult = false;
      
      if(learnEpConf.useEpisodeLocking){
      
        lockResult = SSLearnEpAccessController.lock(par.forUser, par.learnEp);
        
        if(lockResult){
          
//          try{
            
//            SSServCaller.broadcastAdd(
//              null,
//              par.learnEp,
//              SSBroadcastEnum.learnEpLockSet,
//              SSDateU.dateAsLong());
            
//          }catch(SSErr error){
//            
//            switch(error.code){
//              case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
//              default: SSServErrReg.regErrThrow(error);
//            }
//          }
        }
      }

      return lockResult;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void learnEpLockRemove(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSLearnEpLockRemovePar par = (SSLearnEpLockRemovePar) parA.getFromJSON(SSLearnEpLockRemovePar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpLockRemoveRet.get(learnEpLockRemove(par)));
  }
  
  @Override
  public Boolean learnEpLockRemove(final SSLearnEpLockRemovePar par) throws Exception{

    try{
      Boolean unLockResult = false;
      
      if(!learnEpConf.useEpisodeLocking){
        return unLockResult;
      }
      
      if(par.withUserRestriction){
        
        SSServCallerU.canUserEditEntity(par.user, par.learnEp);
        
        if(par.forUser == null){
          par.forUser = par.user;
        }
        
        if(!SSLearnEpAccessController.hasLock(par.forUser, par.learnEp)){
          return unLockResult;
        }
      }

      unLockResult = SSLearnEpAccessController.unLock(par.learnEp);

      if(unLockResult){
        
//        try{
          
//          SSServCaller.broadcastAdd(
//            null,
//            par.learnEp,
//            SSBroadcastEnum.learnEpLockRemoved,
//            null);
          
//        }catch(SSErr error){
//          
//          switch(error.code){
//            case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
//            default: SSServErrReg.regErrThrow(error);
//          }
//        }
      }
      
      return unLockResult;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
