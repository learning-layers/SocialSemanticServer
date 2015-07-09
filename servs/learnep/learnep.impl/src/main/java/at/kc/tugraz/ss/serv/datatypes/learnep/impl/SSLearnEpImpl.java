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
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleTypesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
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
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpCopyForUserPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityAddPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentSetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionTimelineStateGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionTimelineStateSetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityUpdatePar;
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
import at.tugraz.sss.serv.SSCircleContentChangedPar;
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
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSImageE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.servs.image.api.SSImageServerI;
import at.tugraz.sss.servs.image.datatype.par.SSImagesGetPar;

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
  public SSEntity getUserEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    return entity;
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
  public void circleContentChanged(final SSCircleContentChangedPar par) throws Exception{
    
    try{
      for(SSEntity entityToAdd : par.entitiesToAdd){
        
        switch(entityToAdd.type){
          
          case learnEp:{
            
            for(SSUri userToPushEntityTo : par.usersToPushEntitiesTo){
              
              if(sqlFct.ownsUserLearnEp(userToPushEntityTo, entityToAdd.id)){
                continue;
              }
              
              sqlFct.addLearnEp(entityToAdd.id, userToPushEntityTo);
            }
              
            ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntitiesAdd(
              new SSCircleEntitiesAddPar(
                null,
                null,
                par.user,
                par.circle,
                SSLearnEpMiscFct.getLearnEpContentURIs(par.user, sqlFct, entityToAdd.id),
                false,
                false));
            
            if(!par.usersToPushEntitiesTo.isEmpty()){
              
              ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleUsersAdd(
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
    final SSUri       user,
    final List<SSUri> users,
    final SSUri       entity,
    final List<SSUri> entitiesToExclude,
    final SSEntityE   entityType) throws Exception{

    try{

      if(!SSStrU.equals(entityType, SSEntityE.learnEp)){
        return;
      }

      for(SSUri forUser : users){

        learnEpCopyForUser(
          new SSLearnEpCopyForUserPar(
            null, 
            null, 
            user, 
            entity, 
            forUser, 
            entitiesToExclude, 
            false));
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
  public List<SSLearnEp> learnEpsGet(final SSLearnEpsGetPar par) throws Exception{

    try{
      final List<SSLearnEp>  learnEps = sqlFct.getLearnEps(par.user);

      for(SSLearnEp learnEp : learnEps){

        learnEp.circleTypes.addAll(
          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleTypesGet(
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
         
         learnEp.users.addAll(
           ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
             new SSEntitiesGetPar(
               null,
               null,
               par.user,
               sqlFct.getLearnEpUserURIs(learnEp.id),
               null, //forUser,
               null, //types,
               null, //descPar
               false))); //withUserRestriction
         
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

    final SSLearnEpVersionsGetPar par = (SSLearnEpVersionsGetPar) parA.getFromJSON(SSLearnEpVersionsGetPar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionsGetRet.get(learnEpVersionsGet(par)));
  }
  
  @Override
  public List<SSLearnEpVersion> learnEpVersionsGet(final SSLearnEpVersionsGetPar par) throws Exception{

    try{
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
        
        final SSEntityDescriberPar descPar = new SSEntityDescriberPar();
        
        descPar.setOverallRating = true;
        descPar.setTags          = true;
        
        for(SSLearnEpEntity learnEpEntity : learnEpVersion.learnEpEntities){
          
          learnEpEntity.entity =
            ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
              new SSEntityGetPar(
                null,
                null,
                par.user,
                learnEpEntity.entity.id,
                null, //forUser,
                false, //withUserRestriction
                descPar, //descPar
                true)); //logErr
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

    final SSLearnEpVersionGetPar par = (SSLearnEpVersionGetPar) parA.getFromJSON(SSLearnEpVersionGetPar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionGetRet.get(learnEpVersionGet(par)));
  }
  
   @Override
  public SSLearnEpVersion learnEpVersionGet(final SSLearnEpVersionGetPar par) throws Exception{

    try{

      SSServCallerU.canUserReadEntity(par.user, par.learnEpVersion);
      
      final SSLearnEpVersion learnEpVersion =
        sqlFct.getLearnEpVersion(
          par.learnEpVersion,
          false);
      
      for(SSLearnEpEntity learnEpEntity : learnEpVersion.learnEpEntities){
        
        learnEpEntity.entity =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              learnEpEntity.entity.id,
              null, //forUser,
              false, //withUserRestriction
              true, //invokeEntityHandlers,
              new SSEntityDescriberPar(
                true, //setTags,
                true, //setOverallRating,
                false, //setDiscs,
                false, //setUEs,
                false, //setThumb,
                false, //setFlags,
                false), //setCircles //descPar,
              true)); //logErr
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

    final SSLearnEpRemovePar par = (SSLearnEpRemovePar) parA.getFromJSON(SSLearnEpRemovePar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpRemoveRet.get(learnEpRemove(par)));
  }
  
  @Override
  public SSUri learnEpRemove(final SSLearnEpRemovePar par) throws Exception{

    try{
      SSServCallerU.canUserEditEntity(par.user, par.learnEp);
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.removeLearnEpForUser(par.user, par.learnEp);
      sqlFct.deleteCurrentEpVersion(par.user);
      
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

      SSServCallerU.canUserEditEntity(par.user, par.learnEp);
      
      SSLearnEpAccessController.checkHasLock(learnEpConf, par.user, par.learnEp);
      
      dbSQL.startTrans(par.shouldCommit);

      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
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

      for(SSEntityCircle entityUserCircle : 
        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlesGet(
          new SSCirclesGetPar(
            null, 
            null, 
            par.user, 
            null, //forUser
            par.learnEp, //entity
            null,  //entityTypesToIncludeOnly
            false, //withUserRestriction
            true, //withSystemCircles
            false))){ //invokeEntityHandlers

        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntitiesAdd(
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
      
      SSServCallerU.canUserEditEntity        (par.user,    par.learnEpVersion);

      learnEp = sqlFct.getLearnEpForVersion(par.learnEpVersion);
      
      SSLearnEpAccessController.checkHasLock (learnEpConf, par.user, learnEp);
      
      dbSQL.startTrans(par.shouldCommit);
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
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
      
      for(SSEntityCircle entityUserCircle : 
        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlesGet(
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

        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntitiesAdd(
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
      final List<SSUri>                  filesAndThumbs   = new ArrayList<>();
      final List<SSUri>                  entities         = new ArrayList<>();
      final SSUri                        learnEp;

      SSServCallerU.canUserEditEntity        (par.user,    par.learnEpVersion);
      SSServCallerU.canUserEditEntity        (par.user,    par.entity);

      learnEp = sqlFct.getLearnEpForVersion(par.learnEpVersion);
      
      SSLearnEpAccessController.checkHasLock (learnEpConf, par.user, learnEp);

      dbSQL.startTrans(par.shouldCommit);
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
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
            
      //TODO replace this parts with circleContentedChanged
      for(SSUri file : SSServCaller.entityFilesGet(par.user, par.entity)){
        filesAndThumbs.add(file);
      }
      
      for(SSUri thumb :
        ((SSImageServerI) SSServReg.getServ(SSImageServerI.class)).imagesGet(
          new SSImagesGetPar(
            null,
            null,
            par.user,
            par.entity, //entity, 
            SSImageE.thumb, //imageType,
            false))){ //withUserRestriction
        
        filesAndThumbs.add(thumb);
      }
      
      entities.add   (learnEpEntityUri);
      entities.add   (par.entity);
      entities.addAll(filesAndThumbs);
        
      for(SSEntityCircle entityUserCircle : 
        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlesGet(
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

        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            null, 
            null, 
            par.user, 
            entityUserCircle.id, 
            entities, 
            false,
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
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
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

      SSServCallerU.canUserEditEntity        (par.user,    par.learnEpCircle);

      final SSUri learnEpVersion = sqlFct.getLearnEpVersionForCircle  (par.learnEpCircle);
      final SSUri learnEp        = sqlFct.getLearnEpForVersion        (learnEpVersion);
      
      SSLearnEpAccessController.checkHasLock (learnEpConf, par.user, learnEp);
      
      dbSQL.startTrans(par.shouldCommit);
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
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
          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlesGet(
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
          
          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntitiesAdd(
            new SSCircleEntitiesAddPar(
              null,
              null,
              par.user,
              entityUserCircle.id,
              SSUri.asListWithoutNullAndEmpty(par.entity),
              false,
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

      SSServCallerU.canUserEditEntity        (par.user,    par.learnEpCircle);

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
      SSServCallerU.canUserEditEntity        (par.user,    par.learnEpEntity);

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

      SSServCallerU.canUserEditEntity(par.user, par.learnEpVersion);
      
      dbSQL.startTrans(par.shouldCommit);
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
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
      
      for(SSEntityCircle entityUserCircle :
        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlesGet(
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

        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntitiesAdd(
          new SSCircleEntitiesAddPar(
            null, 
            null, 
            par.user, 
            entityUserCircle.id, 
            SSUri.asListWithoutNullAndEmpty(learnEpTimelineStateUri), 
            false,
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

    final SSLearnEpVersionCurrentGetPar par = (SSLearnEpVersionCurrentGetPar) parA.getFromJSON(SSLearnEpVersionCurrentGetPar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionCurrentGetRet.get(learnEpVersionCurrentGet(par)));
  }
  
  @Override
  public SSLearnEpVersion learnEpVersionCurrentGet(final SSLearnEpVersionCurrentGetPar par) throws Exception{
    
    try{
      
      final SSLearnEpVersion learnEpVersion =
        sqlFct.getLearnEpVersion(
          sqlFct.getLearnEpCurrentVersionURI(par.user),
          false);
      
      final SSEntityDescriberPar descPar = new SSEntityDescriberPar();
      
      descPar.setTags          = true;
      descPar.setOverallRating = true;
        
      for(SSLearnEpEntity learnEpEntity : learnEpVersion.learnEpEntities){
        
        learnEpEntity.entity =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              learnEpEntity.entity.id,
              null, //forUser,
              false, //withUserRestriction
              descPar)); //descPar
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

    final SSLearnEpVersionCurrentSetPar par = (SSLearnEpVersionCurrentSetPar) parA.getFromJSON(SSLearnEpVersionCurrentSetPar.class);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionCurrentSetRet.get(learnEpVersionCurrentSet(par)));
    
//    SSLearnEpActivityFct.setCurrentLearnEpVersion(new SSLearnEpVersionCurrentSetPar(parA));
  }

  @Override
  public SSUri learnEpVersionCurrentSet(final SSLearnEpVersionCurrentSetPar par) throws Exception{

    try{

      SSServCallerU.canUserEditEntity(par.user, par.learnEpVersion);
      
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
  public SSUri learnEpCopyForUser(final SSLearnEpCopyForUserPar par) throws Exception{

    try{
      SSServCallerU.canUserEditEntity(par.user, par.entity);
      
      dbSQL.startTrans(par.shouldCommit);

      SSLearnEpMiscFct.copyLearnEpForUser(
        this,
        sqlFct,
        par.user,
        par.forUser,
        par.entitiesToExclude,
        par.entity);

      dbSQL.commit(par.shouldCommit);

      return par.entity;

    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return learnEpCopyForUser(par);
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
          
          for(SSUri learnEp : sqlFct.getLearnEpURIs(par.user)){
            
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
