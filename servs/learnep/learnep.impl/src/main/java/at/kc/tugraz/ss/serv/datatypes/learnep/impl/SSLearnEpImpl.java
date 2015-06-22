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

import at.kc.tugraz.socialserver.service.broadcast.datatypes.enums.SSBroadcastEnum;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivEntityAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleTypesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.circle.serv.SSCircleServ;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpClientI;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.conf.SSLearnEpConf;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpEntity;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpTimelineState;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpLockHoldPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpLockRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpLockSetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpUserCopyForUserPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionAddCirclePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionAddEntityPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentSetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionGetTimelineStatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionRemoveCirclePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionRemoveEntityPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionSetTimelineStatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionUpdateCirclePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionUpdateEntityPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpsGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpsLockHoldPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpCreateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpLockHoldRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpLockRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpLockSetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionAddCircleRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionAddEntityRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCreateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCurrentGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCurrentSetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionGetTimelineStateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionRemoveCircleRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionRemoveEntityRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionSetTimelineStateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionUpdateCircleRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionUpdateEntityRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionsGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpsGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpsLockHoldRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.access.SSLearnEpAccessController;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.activity.SSLearnEpActivityFct;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.misc.SSLearnEpMiscFct;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.sql.SSLearnEpSQLFct;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;

public class SSLearnEpImpl 
extends SSServImplWithDBA 
implements 
  SSLearnEpClientI, 
  SSLearnEpServerI,
  SSEntityHandlerImplI,
  SSUsersResourcesGathererI{

  private final SSLearnEpSQLFct sqlFct;
  private final SSLearnEpConf   learnEpConf;

  public SSLearnEpImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

//    graphFct  = new SSLearnEpGraphFct (this);
    sqlFct        = new SSLearnEpSQLFct(this);
    learnEpConf   = (SSLearnEpConf) conf;
  }

  @Override
  public void getUsersResources(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> usersResources) throws Exception{
    
    for(String user : allUsers){
      
      for(SSUri learnEp : sqlFct.getLearnEpURIs(SSUri.get(user))){
        
        for(SSUri versionUri : sqlFct.getLearnEpVersionURIs(learnEp)){
          
          for(SSLearnEpEntity entity : sqlFct.getLearnEpVersion(versionUri, false).learnEpEntities){
            
            if(usersResources.containsKey(user)){
              usersResources.get(user).add(entity.entity.id);
            }else{
              
              final List<SSUri> resourceList = new ArrayList<>();
              
              resourceList.add(entity.entity.id);
              
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
  public List<SSUri> getParentEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    return new ArrayList<>();
  }
    
  @Override
  public List<SSUri> getSubEntities(
    final SSUri     user,
    final SSUri     entity,
    final SSEntityE type) throws Exception{

    return null;
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
  public Boolean setEntityPublic(
    final SSUri userUri,
    final SSUri entityUri,
    final SSEntityE entityType,
    final SSUri publicCircleUri) throws Exception{

    return false;
  }

  @Override
  public void shareEntityWithUsers(
    final SSUri         user,
    final List<SSUri>   usersToShareWith,
    final SSUri         entity,
    final SSUri         circle,
    final SSEntityE     entityType,
    final Boolean       saveActivity) throws Exception{
    
    try{
      
      switch(entityType){
        
        case learnEp:{
          
          for(SSUri userUriToShareWith : usersToShareWith){
            
            if(sqlFct.ownsUserLearnEp(userUriToShareWith, entity)){
              SSLogU.warn("learn ep is already shared with user");
              continue;
            }
            
            sqlFct.addLearnEp(entity, userUriToShareWith);

            ((SSCircleServerI) SSCircleServ.inst.serv()).circleUsersAdd(
              new SSCircleUsersAddPar(
                null,
                null,
                user,
                circle,
                sqlFct.getLearnEpUserURIs(entity),
                false,
                false,
                false));
            
            ((SSCircleServerI) SSCircleServ.inst.serv()).circleEntitiesAdd(
              new SSCircleEntitiesAddPar(
                null, 
                null, 
                user, 
                circle, 
                SSLearnEpMiscFct.getLearnEpContentURIs(user, sqlFct, entity), 
                false, 
                false,
                false));
          }
          
          SSLearnEpActivityFct.shareLearnEp(
            user, 
            entity, 
            usersToShareWith);
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public Boolean copyEntity(
    final SSUri       user,
    final List<SSUri> users,
    final SSUri       entity,
    final List<SSUri> entitiesToExclude,
    final SSEntityE   entityType) throws Exception{

    try{

      if(!SSStrU.equals(entityType, SSEntityE.learnEp)){
        return false;
      }

      for(SSUri forUser : users){

        SSServCaller.learnEpUserCopyForUser(
          user,
          forUser,
          entity,
          entitiesToExclude,
          false);
      }

      return true;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void addEntityToCircle(
    final SSUri        userUri,
    final SSUri        circleUri,
    final List<SSUri>  circleUsers,
    final SSUri        entityUri,
    final SSEntityE    entityType) throws Exception{
  }

  @Override
  public void addUsersToCircle(
    final SSUri        user,
    final List<SSUri>  users,
    final SSEntityCircle        circle) throws Exception{
    
    
    
  }
  
  @Override
  public void learnEpsGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpsGetRet.get(learnEpsGet(parA), parA.op));
  }

  @Override
  public List<SSLearnEp> learnEpsGet(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpsGetPar par      = new SSLearnEpsGetPar(parA);
      final List<SSLearnEp>  learnEps = sqlFct.getLearnEps(par.user);

      for(SSLearnEp learnEp : learnEps){

        learnEp.circleTypes.addAll(
          ((SSCircleServerI) SSCircleServ.inst.serv()).circleTypesGet(
            new SSCircleTypesGetPar(
              null, 
              null, 
              par.user,
              par.user,
              learnEp.id, 
              true)));
        
         learnEp.read = 
           SSServCaller.entityReadGet(
             par.user, 
             learnEp.id);
         
         for(SSUri user : sqlFct.getLearnEpUserURIs(learnEp.id)){
           
           learnEp.users.add(
             SSServCaller.entityDescGet(
               par.user, 
               user, 
               false,
               false, 
               false, 
               false, 
               false, 
               false, 
               false));
         }
         
         if(!learnEpConf.useEpisodeLocking){
           learnEp.locked       = false;
           learnEp.lockedByUser = false;
         }else{
           
           learnEp.locked       = SSLearnEpAccessController.isLocked (learnEp.id);
           learnEp.lockedByUser = SSLearnEpAccessController.hasLock  (par.user, learnEp.id);
         }
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

    sSCon.writeRetFullToClient(SSLearnEpVersionsGetRet.get(learnEpVersionsGet(parA), parA.op));
  }
  
  @Override
  public List<SSLearnEpVersion> learnEpVersionsGet(SSServPar parI) throws Exception{

    try{
      final SSLearnEpVersionsGetPar par    = new SSLearnEpVersionsGetPar(parI);
      final List<SSLearnEpVersion>  result = new ArrayList<>();
      SSLearnEpVersion              learnEpVersion;
      
      SSServCallerU.canUserReadEntity(par.user, par.learnEp);
      
      for(SSUri learnEpVersionUri : sqlFct.getLearnEpVersionURIs(par.learnEp)){
        
//        learnEpVersion =
//          SSLearnEpVersion.get(
//            sqlFct.getLearnEpVersion(
//              learnEpVersionUri,
//              false),
//            SSServCaller.entityDescGet(
//              par.user,
//              learnEpVersionUri,
//              true,
//              true,
//              false,
//              false,
//              false,
//              false));
        
        //        for(SSLearnEpCircle learnEpEntity : learnEpVersion.learnEpCircles){
//          
//          learnEpEntity =
//            SSLearnEpEntity.get(
//              learnEpEntity,
//              SSServCaller.entityDescGet(
//                par.user,
//                learnEpEntity.id,
//                true,
//                true,
//                false,
//                false,
//                false,
//                false));
//        }
        
        learnEpVersion =
          sqlFct.getLearnEpVersion(
            learnEpVersionUri,
            false);
        
        for(SSLearnEpEntity learnEpEntity : learnEpVersion.learnEpEntities){
          
          learnEpEntity.entity = 
            SSServCaller.entityDescGet(
                par.user,
                learnEpEntity.entity.id,
                true,
                true,
                false,
                false,
                false,
                false,
                false);
        }

        result.add(learnEpVersion);
      }

      return result;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionGetRet.get(learnEpVersionGet(parA), parA.op));
  }
  
   @Override
  public SSLearnEpVersion learnEpVersionGet(final SSServPar parA) throws Exception{

    try{

      final SSLearnEpVersionGetPar par = new SSLearnEpVersionGetPar(parA);

      SSServCallerU.canUserReadEntity(par.user, par.learnEpVersion);
      
      final SSLearnEpVersion learnEpVersion =
        sqlFct.getLearnEpVersion(
          par.learnEpVersion,
          false);
      
      for(SSLearnEpEntity learnEpEntity : learnEpVersion.learnEpEntities){
        
        learnEpEntity.entity =
          SSServCaller.entityDescGet(
            par.user,
            learnEpEntity.entity.id,
            true,
            true,
            false,
            false,
            false,
            false,
            false);
      }
        
      return learnEpVersion;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpRemove(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpRemoveRet.get(learnEpRemove(parA), parA.op));
  }
  
  @Override
  public SSUri learnEpRemove(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpRemovePar par               = new SSLearnEpRemovePar(parA);

      SSServCallerU.canUserEditEntity(par.user, par.learnEp);
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.removeLearnEpForUser(par.user, par.learnEp);
      sqlFct.deleteCurrentEpVersion(par.user);
      
      dbSQL.commit(par.shouldCommit);

      return par.learnEp;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpRemove(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void learnEpVersionCreate(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSUri result = learnEpVersionCreate(parA);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionCreateRet.get(result, parA.op));
    
//    SSLearnEpActivityFct.createLearnEpVersion(new SSLearnEpVersionCreatePar(parA), result);
  }
  
  @Override
  public SSUri learnEpVersionCreate(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpVersionCreatePar par               = new SSLearnEpVersionCreatePar(parA);
      final SSUri                     learnEpVersionUri = SSServCaller.vocURICreate();

      SSServCallerU.canUserEditEntity(par.user, par.learnEp);
      
      SSLearnEpAccessController.checkHasLock(learnEpConf, par.user, par.learnEp);
      
      dbSQL.startTrans(par.shouldCommit);

      ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
        new SSCirclePrivEntityAddPar(
          null,
          null,
          par.user,
          learnEpVersionUri,
          SSEntityE.learnEpVersion,
          null,
          null,
          null,
          false));

      for(SSEntityCircle entityUserCircle : 
        ((SSCircleServerI) SSCircleServ.inst.serv()).circlesGet(
          new SSCirclesGetPar(
            null, 
            null, 
            par.user, 
            null,
            par.learnEp, 
            SSEntityE.asListWithoutNullAndEmpty(), 
            false, 
            true, 
            false))){

        ((SSCircleServerI) SSCircleServ.inst.serv()).circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            null, 
            null, 
            par.user, 
            entityUserCircle.id, 
            SSUri.asListWithoutNullAndEmpty(learnEpVersionUri), 
            false,
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
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionCreate(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionAddCircle(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionAddCircleRet.get(learnEpVersionAddCircle(parA), parA.op));
  }
  
  @Override
  public SSUri learnEpVersionAddCircle(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpVersionAddCirclePar par        = new SSLearnEpVersionAddCirclePar(parA);
      final SSUri                        circleUri  = SSServCaller.vocURICreate();
      final SSUri                        learnEp;
      
      SSServCallerU.canUserEditEntity        (par.user,    par.learnEpVersion);

      learnEp = sqlFct.getLearnEpForVersion(par.learnEpVersion);
      
      SSLearnEpAccessController.checkHasLock (learnEpConf, par.user, learnEp);
      
      dbSQL.startTrans(par.shouldCommit);
      
      ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
        new SSCirclePrivEntityAddPar(
          null,
          null,
          par.user,
          circleUri,
          SSEntityE.learnEpCircle,
          par.label,
          null,
          null,
          false));
            
      for(SSEntityCircle entityUserCircle : 
        ((SSCircleServerI) SSCircleServ.inst.serv()).circlesGet(
          new SSCirclesGetPar(
            null, 
            null, 
            par.user, 
            null,
            par.learnEpVersion, 
            SSEntityE.asListWithoutNullAndEmpty(), 
            false, 
            true, 
            false))){

        ((SSCircleServerI) SSCircleServ.inst.serv()).circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            null, 
            null, 
            par.user, 
            entityUserCircle.id, 
            SSUri.asListWithoutNullAndEmpty(circleUri), 
            false,
            true, 
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
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionAddCircle(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionAddEntity(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionAddEntityRet.get(learnEpVersionAddEntity(parA), parA.op));
  }
  
  @Override
  public SSUri learnEpVersionAddEntity(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpVersionAddEntityPar par              = new SSLearnEpVersionAddEntityPar(parA);
      final SSUri                        learnEpEntityUri = SSServCaller.vocURICreate();
      final List<SSUri>                  filesAndThumbs   = new ArrayList<>();
      final List<SSUri>                  entities         = new ArrayList<>();
      final SSUri                        learnEp;

      SSServCallerU.canUserEditEntity        (par.user,    par.learnEpVersion);
      SSServCallerU.canUserEditEntity        (par.user,    par.entity);

      learnEp = sqlFct.getLearnEpForVersion(par.learnEpVersion);
      
      SSLearnEpAccessController.checkHasLock (learnEpConf, par.user, learnEp);

      dbSQL.startTrans(par.shouldCommit);

      ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
        new SSCirclePrivEntityAddPar(
          null,
          null,
          par.user,
          learnEpEntityUri,
          SSEntityE.learnEpEntity,
          null,
          null,
          null,
          false));
           
      for(SSUri file : SSServCaller.entityFilesGet(par.user, par.entity)){
        filesAndThumbs.add(file);
      }
      
      for(SSUri thumb : SSServCaller.entityThumbsGet(par.user, par.entity)){
        filesAndThumbs.add(thumb);
      }
      
      entities.add   (learnEpEntityUri);
      entities.add   (par.entity);
      entities.addAll(filesAndThumbs);
        
      for(SSEntityCircle entityUserCircle : 
        ((SSCircleServerI) SSCircleServ.inst.serv()).circlesGet(
          new SSCirclesGetPar(
            null, 
            null, 
            par.user, 
            null,
            par.learnEpVersion, 
            SSEntityE.asListWithoutNullAndEmpty(), 
            false, 
            true, 
            false))){

        ((SSCircleServerI) SSCircleServ.inst.serv()).circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            null, 
            null, 
            par.user, 
            entityUserCircle.id, 
            entities, 
            false,
            true, 
            false));
      }        
      
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
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionAddEntity(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpCreate(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSUri learnEp = learnEpCreate(parA);
    
    sSCon.writeRetFullToClient(SSLearnEpCreateRet.get(learnEp, parA.op));
    
//    SSLearnEpActivityFct.createLearnEp(new SSLearnEpCreatePar(parA), learnEp);
  }
  
  @Override
  public SSUri learnEpCreate(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpCreatePar par        = new SSLearnEpCreatePar(parA);
      final SSUri              learnEpUri = SSServCaller.vocURICreate();
      
      dbSQL.startTrans(par.shouldCommit);
      
      ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
        new SSCirclePrivEntityAddPar(
          null,
          null,
          par.user,
          learnEpUri,
          SSEntityE.learnEp,
          par.label,
          par.description,
          null,
          false));
      
      sqlFct.createLearnEp(learnEpUri, par.user);
      
      dbSQL.commit(par.shouldCommit);

      return learnEpUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpCreate(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionUpdateCircle(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionUpdateCircleRet.get(learnEpVersionUpdateCircle(parA), parA.op));
  }
  
  @Override
  public Boolean learnEpVersionUpdateCircle(final SSServPar parA) throws Exception{

    try{

      final SSLearnEpVersionUpdateCirclePar par = new SSLearnEpVersionUpdateCirclePar(parA);

      SSServCallerU.canUserEditEntity        (par.user,    par.learnEpCircle);

      final SSUri learnEpVersion = sqlFct.getLearnEpVersionForCircle  (par.learnEpCircle);
      final SSUri learnEp        = sqlFct.getLearnEpForVersion        (learnEpVersion);
      
      SSLearnEpAccessController.checkHasLock (learnEpConf, par.user, learnEp);
      
      dbSQL.startTrans(par.shouldCommit);

      ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
        new SSCirclePrivEntityAddPar(
          null,
          null,
          par.user,
          par.learnEpCircle,
          SSEntityE.learnEpCircle,
          par.label,
          null,
          null,
          false));

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
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionUpdateCircle(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionUpdateEntity(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionUpdateEntityRet.get(learnEpVersionUpdateEntity(parA), parA.op));
  }
  
  @Override
  public Boolean learnEpVersionUpdateEntity(SSServPar parA) throws Exception{
    
    try{
      
      final SSLearnEpVersionUpdateEntityPar par = new SSLearnEpVersionUpdateEntityPar(parA);

      SSServCallerU.canUserEditEntity        (par.user, par.learnEpEntity);
      
      if(par.entity != null){
        SSServCallerU.canUserEditEntity(par.user, par.entity);
      }
            
      final SSUri learnEpVersion     = sqlFct.getLearnEpVersionForEntity(par.learnEpEntity);
      final SSUri learnEp            = sqlFct.getLearnEpForVersion      (learnEpVersion);
      
      SSLearnEpAccessController.checkHasLock (learnEpConf, par.user, learnEp);
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(par.entity != null){
        
        for(SSEntityCircle entityUserCircle :
          ((SSCircleServerI) SSCircleServ.inst.serv()).circlesGet(
            new SSCirclesGetPar(
              null,
              null,
              par.user,
              null,
              par.learnEpEntity,
              SSEntityE.asListWithoutNullAndEmpty(),
              false,
              true,
              false))){
          
          ((SSCircleServerI) SSCircleServ.inst.serv()).circleEntitiesAdd(
            new SSCircleEntitiesAddPar(
              null,
              null,
              par.user,
              entityUserCircle.id,
              SSUri.asListWithoutNullAndEmpty(par.entity),
              false,
              true,
              false));
        }
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
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionUpdateEntity(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionRemoveCircle(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionRemoveCircleRet.get(learnEpVersionRemoveCircle(parA), parA.op));
  }
  
  @Override
  public Boolean learnEpVersionRemoveCircle(final SSServPar parA) throws Exception{

    try{

      final SSLearnEpVersionRemoveCirclePar par = new SSLearnEpVersionRemoveCirclePar(parA);

      SSServCallerU.canUserEditEntity        (par.user,    par.learnEpCircle);

      final SSUri learnEpVersion     = sqlFct.getLearnEpVersionForCircle(par.learnEpCircle);
      final SSUri learnEp            = sqlFct.getLearnEpForVersion      (learnEpVersion);
        
      SSLearnEpAccessController.checkHasLock (learnEpConf, par.user, learnEp);
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.deleteCircle(par.learnEpCircle);
      
//      SSServCaller.entityRemove(par.learnEpCircle, false);

      SSLearnEpActivityFct.removeLearnEpVersionCircle(par, learnEpVersion, learnEp);
      
      dbSQL.commit(par.shouldCommit);

      return true;
   }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionRemoveCircle(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionRemoveEntity(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionRemoveEntityRet.get(learnEpVersionRemoveEntity(parA), parA.op));
  }
  
  @Override
  public Boolean learnEpVersionRemoveEntity(final SSServPar parA) throws Exception{

    try{

      final SSLearnEpVersionRemoveEntityPar par = new SSLearnEpVersionRemoveEntityPar(parA);

      SSServCallerU.canUserEditEntity        (par.user,    par.learnEpEntity);

      final SSUri learnEpVersion       = sqlFct.getLearnEpVersionForEntity       (par.learnEpEntity);
      final SSUri learnEp              = sqlFct.getLearnEpForVersion             (learnEpVersion);
      final SSUri entity               = sqlFct.getEntity                        (learnEpVersion, par.learnEpEntity);
      
      SSLearnEpAccessController.checkHasLock (learnEpConf, par.user, learnEp);
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.deleteEntity(par.learnEpEntity);
//      SSServCaller.entityRemove(par.learnEpEntity, false);
      
      SSLearnEpActivityFct.removeLearnEpVersionEntity(par, learnEpVersion, entity, learnEp);

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionRemoveEntity(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionSetTimelineState(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionSetTimelineStateRet.get(learnEpVersionSetTimelineState(parA), parA.op));
  }
  
  @Override
  public SSUri learnEpVersionSetTimelineState(final SSServPar parA) throws Exception{

    try{

      final SSLearnEpVersionSetTimelineStatePar par                     = new SSLearnEpVersionSetTimelineStatePar(parA);
      final SSUri                               learnEpTimelineStateUri = SSServCaller.vocURICreate();

      SSServCallerU.canUserEditEntity(par.user, par.learnEpVersion);
      
      dbSQL.startTrans(par.shouldCommit);
      
      ((SSCircleServerI) SSCircleServ.inst.serv()).circlePrivEntityAdd(
        new SSCirclePrivEntityAddPar(
          null,
          null,
          par.user,
          learnEpTimelineStateUri,
          SSEntityE.learnEpTimelineState,
          null,
          null,
          null,
          false));
      
      for(SSEntityCircle entityUserCircle :
        ((SSCircleServerI) SSCircleServ.inst.serv()).circlesGet(
          new SSCirclesGetPar(
            null,
            null,
            par.user,
            null,
            par.learnEpVersion,
            SSEntityE.asListWithoutNullAndEmpty(),
            false,
            true,
            false))){

        ((SSCircleServerI) SSCircleServ.inst.serv()).circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            null, 
            null, 
            par.user, 
            entityUserCircle.id, 
            SSUri.asListWithoutNullAndEmpty(learnEpTimelineStateUri), 
            false,
            true, 
            false));
      }

      sqlFct.setLearnEpVersionTimelineState(
        learnEpTimelineStateUri,
        par.learnEpVersion,
        par.startTime,
        par.endTime);

      dbSQL.commit(par.shouldCommit);

      return learnEpTimelineStateUri;

    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionSetTimelineState(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionGetTimelineState(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionGetTimelineStateRet.get(learnEpVersionGetTimelineState(parA), parA.op));
  }
  
  @Override
  public SSLearnEpTimelineState learnEpVersionGetTimelineState(
    final SSServPar parA) throws Exception{

    try{

      final SSLearnEpVersionGetTimelineStatePar par = new SSLearnEpVersionGetTimelineStatePar(parA);

      SSServCallerU.canUserReadEntity(par.user, par.learnEpVersion);
      
      return sqlFct.getLearnEpVersionTimelineState(par.learnEpVersion);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionCurrentGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionCurrentGetRet.get(learnEpVersionCurrentGet(parA), parA.op));
  }
  
  @Override
  public SSLearnEpVersion learnEpVersionCurrentGet(SSServPar parI) throws Exception{
    
    try{
      
      final SSLearnEpVersionCurrentGetPar par = new SSLearnEpVersionCurrentGetPar(parI);
      
      final SSLearnEpVersion learnEpVersion =
        sqlFct.getLearnEpVersion(
          sqlFct.getLearnEpCurrentVersionURI(par.user),
          false);
      
      for(SSLearnEpEntity learnEpEntity : learnEpVersion.learnEpEntities){
        
        learnEpEntity.entity =
          SSServCaller.entityDescGet(
            par.user,
            learnEpEntity.entity.id,
            true,
            true,
            false,
            false,
            false,
            false,
            false);
      }
      
      return learnEpVersion;
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

    sSCon.writeRetFullToClient(SSLearnEpVersionCurrentSetRet.get(learnEpVersionCurrentSet(parA), parA.op));
    
//    SSLearnEpActivityFct.setCurrentLearnEpVersion(new SSLearnEpVersionCurrentSetPar(parA));
  }

  @Override
  public SSUri learnEpVersionCurrentSet(final SSServPar parA) throws Exception{

    try{

      final SSLearnEpVersionCurrentSetPar par = new SSLearnEpVersionCurrentSetPar(parA);

      SSServCallerU.canUserEditEntity(par.user, par.learnEpVersion);
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.setLearnEpCurrentVersion(par.user, par.learnEpVersion);

      dbSQL.commit(par.shouldCommit);

      return par.learnEpVersion;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpVersionCurrentSet(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSUri learnEpUserCopyForUser(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpUserCopyForUserPar par = new SSLearnEpUserCopyForUserPar(parA);

      SSServCallerU.canUserEditEntity(par.user, par.entity);
      
      dbSQL.startTrans(par.shouldCommit);

      SSLearnEpMiscFct.copyLearnEpForUser(
        sqlFct,
        par.user,
        par.forUser,
        par.entitiesToExclude,
        par.entity);

      dbSQL.commit(par.shouldCommit);

      return par.entity;

    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpUserCopyForUser(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Deprecated
  @Override
  public void learnEpLockHold(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(learnEpLockHold(parA));
  }
  
  @Override
  public SSLearnEpLockHoldRet learnEpLockHold(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpLockHoldPar par = new SSLearnEpLockHoldPar(parA);
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
            SSLearnEpAccessController.getRemainingTime(par.learnEp),
            parA.op);
        
      }else{
        
        ret =
          SSLearnEpLockHoldRet.get(
            par.learnEp,
            false,
            false,
            0L,
            parA.op);
      }    
      
      return ret;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void learnEpsLockHold(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpsLockHoldRet.get(learnEpsLockHold(parA), parA.op));
  }
  
  @Override
  public List<SSLearnEpLockHoldRet> learnEpsLockHold(final SSServPar parA) throws Exception{
    
    try{
      final SSLearnEpsLockHoldPar      par = new SSLearnEpsLockHoldPar(parA);
      final List<SSLearnEpLockHoldRet> locks = new ArrayList<>();
      
      if(learnEpConf.useEpisodeLocking){
        
        if(!par.learnEps.isEmpty()){
          
          for(SSUri learnEp : par.learnEps){
            
            locks.add(
              SSServCaller.learnEpLockHold(
                par.user,
                learnEp,
                true));
          }
        }else{
          
          for(SSUri learnEp : sqlFct.getLearnEpURIs(par.user)){
            
            locks.add(
              SSServCaller.learnEpLockHold(
                par.user,
                learnEp,
                true));
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
  public void learnEpLockSet(SSSocketCon sSCon, SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpLockSetRet.get(learnEpLockSet(parA), parA.op));
  }
  
  @Override
  public Boolean learnEpLockSet(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpLockSetPar par = new SSLearnEpLockSetPar(parA);

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
          
          try{
            
            SSServCaller.broadcastAdd(
              null,
              par.learnEp,
              SSBroadcastEnum.learnEpLockSet,
              SSDateU.dateAsLong());
            
          }catch(SSErr error){
            
            switch(error.code){
              case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
              default: SSServErrReg.regErrThrow(error);
            }
          }
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

    sSCon.writeRetFullToClient(SSLearnEpLockRemoveRet.get(learnEpLockRemove(parA), parA.op));
  }
  
  @Override
  public Boolean learnEpLockRemove(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpLockRemovePar par = new SSLearnEpLockRemovePar(parA);

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
        
        try{
          
          SSServCaller.broadcastAdd(
            null,
            par.learnEp,
            SSBroadcastEnum.learnEpLockRemoved,
            null);
          
        }catch(SSErr error){
          
          switch(error.code){
            case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
            default: SSServErrReg.regErrThrow(error);
          }
        }
      }
      
      return unLockResult;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
