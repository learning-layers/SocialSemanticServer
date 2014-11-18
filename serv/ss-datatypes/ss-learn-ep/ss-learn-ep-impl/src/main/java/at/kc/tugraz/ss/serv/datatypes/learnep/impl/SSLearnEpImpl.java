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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpClientI;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpEntity;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpTimelineState;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpUserCopyForUserPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpUserShareWithUserPar;
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
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpCreateRet;
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
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.activity.SSLearnEpActivityFct;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.misc.SSLearnEpMiscFct;
import at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.sql.SSLearnEpSQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.api.SSUsersResourcesGathererI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sss.serv.err.datatypes.SSErrE;

public class SSLearnEpImpl extends SSServImplWithDBA implements SSLearnEpClientI, SSLearnEpServerI, SSEntityHandlerImplI, SSUsersResourcesGathererI{

//  private final SSLearnEpGraphFct       graphFct;
  private final SSLearnEpSQLFct sqlFct;

  public SSLearnEpImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{

    super(conf, dbGraph, dbSQL);

//    graphFct  = new SSLearnEpGraphFct (this);
    sqlFct = new SSLearnEpSQLFct(this);
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
  public Boolean setUserEntityPublic(
    final SSUri userUri,
    final SSUri entityUri,
    final SSEntityE entityType,
    final SSUri publicCircleUri) throws Exception{

    return false;
  }

  @Override
  public void shareUserEntity(
    final SSUri         user,
    final List<SSUri>   usersToShareWith,
    final SSUri         entity,
    final SSUri         circle,
    final SSEntityE     entityType,
    final Boolean       saveActivity) throws Exception{
    
    try{
      
      switch(entityType){
        
        case learnEp:
          for(SSUri userUriToShareWith : usersToShareWith){
            
            SSServCaller.learnEpUserShareWithUser(
              user,
              userUriToShareWith,
              entity,
              circle,
              saveActivity,
              false);
          }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public Boolean copyUserEntity(
    final SSUri user,
    final List<SSUri> users,
    final SSUri entity,
    final List<SSUri> entitiesToExclude,
    final SSEntityE entityType) throws Exception{

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
  public void shareUserEntityWithCircle(
    final SSUri userUri,
    final SSUri circleUri,
    final SSUri entityUri,
    final SSEntityE entityType) throws Exception{
  }

  @Override
  public void learnEpsGet(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSLearnEpsGetRet.get(learnEpsGet(par), par.op));
  }

  @Override
  public List<SSLearnEp> learnEpsGet(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpsGetPar par      = new SSLearnEpsGetPar(parA);
      final List<SSLearnEp>  learnEps = sqlFct.getLearnEps(par.user);

      for(SSLearnEp learnEp : learnEps){

        learnEp.circleTypes.addAll(
          SSServCaller.entityUserEntityCircleTypesGet(
            par.user,
            learnEp.id));
      }

      return learnEps;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void learnEpVersionsGet(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSLearnEpVersionsGetRet.get(learnEpVersionsGet(par), par.op));
  }
  
  @Override
  public List<SSLearnEpVersion> learnEpVersionsGet(SSServPar parI) throws Exception{

    try{
      final SSLearnEpVersionsGetPar par    = new SSLearnEpVersionsGetPar(parI);
      final List<SSLearnEpVersion>  result = new ArrayList<>();
      SSLearnEpVersion              learnEpVersion;
      
      SSServCaller.entityUserCanRead(par.user, par.learnEp);
      
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
  public void learnEpVersionGet(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSLearnEpVersionGetRet.get(learnEpVersionGet(par), par.op));
  }
  
   @Override
  public SSLearnEpVersion learnEpVersionGet(final SSServPar parA) throws Exception{

    try{

      final SSLearnEpVersionGetPar par = new SSLearnEpVersionGetPar(parA);

      SSServCaller.entityUserCanRead(par.user, par.learnEpVersion);
      
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
  public void learnEpVersionCreate(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    final SSUri result = learnEpVersionCreate(parA);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionCreateRet.get(result, parA.op));
    
    SSLearnEpActivityFct.createLearnEpVersion(new SSLearnEpVersionCreatePar(parA), result);
  }
  
  @Override
  public SSUri learnEpVersionCreate(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpVersionCreatePar par               = new SSLearnEpVersionCreatePar(parA);
      final SSUri                     learnEpVersionUri = sqlFct.createLearnEpVersionUri();

      SSServCaller.entityUserCanEdit(par.user, par.learnEp);
      
      dbSQL.startTrans(par.shouldCommit);

      SSServCaller.entityEntityToPrivCircleAdd(
        par.user, 
        learnEpVersionUri, 
        SSEntityE.learnEpVersion, 
        null, 
        null, 
        null, 
        false);

      for(SSEntityCircle entityUserCircle : SSServCaller.entityUserEntityCirclesGet(par.user, par.learnEp, true)){

        SSServCaller.entityEntitiesToCircleAdd(
          par.user,
          entityUserCircle.id,
          SSUri.asListWithoutNullAndEmpty(learnEpVersionUri),
          false);
      }

      sqlFct.createLearnEpVersion(
        learnEpVersionUri,
        par.learnEp);

      dbSQL.commit(par.shouldCommit);

      return learnEpVersionUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return learnEpVersionCreate(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionAddCircle(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);
    
    final SSUri circle = learnEpVersionAddCircle(parA);
      
    sSCon.writeRetFullToClient(SSLearnEpVersionAddCircleRet.get(circle, parA.op));
    
    SSLearnEpActivityFct.addCircleToLearnEpVersion(new SSLearnEpVersionAddCirclePar(parA), circle);
  }
  
  @Override
  public SSUri learnEpVersionAddCircle(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpVersionAddCirclePar par = new SSLearnEpVersionAddCirclePar(parA);
      final SSUri circleUri = sqlFct.createLearnEpCircleUri();

      SSServCaller.entityUserCanEdit(par.user, par.learnEpVersion);
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntityToPrivCircleAdd(
        par.user,
        circleUri,
        SSEntityE.learnEpCircle,
        par.label,
        null,
        null,
        false);
            
      for(SSEntityCircle entityUserCircle : SSServCaller.entityUserEntityCirclesGet(par.user, par.learnEpVersion, true)){

        SSServCaller.entityEntitiesToCircleAdd(
          par.user,
          entityUserCircle.id,
          SSUri.asListWithoutNullAndEmpty(circleUri),
          false);
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

      dbSQL.commit(par.shouldCommit);

      return circleUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return learnEpVersionAddCircle(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionAddEntity(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    final SSUri entity = learnEpVersionAddEntity(parA);
    
    sSCon.writeRetFullToClient(SSLearnEpVersionAddEntityRet.get(entity, parA.op));
    
    SSLearnEpActivityFct.addEntityToLearnEpVersion(new SSLearnEpVersionAddEntityPar(parA), entity);
  }
  
  @Override
  public SSUri learnEpVersionAddEntity(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpVersionAddEntityPar par              = new SSLearnEpVersionAddEntityPar(parA);
      final SSUri                        learnEpEntityUri = sqlFct.createLearnEpEntityUri();

      SSServCaller.entityUserCanEdit(par.user, par.learnEpVersion);
      SSServCaller.entityUserCanEdit(par.user, par.entity);

      dbSQL.startTrans(par.shouldCommit);

      SSServCaller.entityEntityToPrivCircleAdd(
        par.user,
        learnEpEntityUri,
        SSEntityE.learnEpEntity,
        null,
        null,
        null,
        false);
           
      for(SSEntityCircle entityUserCircle : SSServCaller.entityUserEntityCirclesGet(par.user, par.learnEpVersion, true)){
        
        SSServCaller.entityEntitiesToCircleAdd(
          par.user,
          entityUserCircle.id,
          SSUri.asListWithoutNullAndEmpty(learnEpEntityUri, par.entity),
          false);
      }        
      
      sqlFct.addEntityToLearnEpVersion(
        learnEpEntityUri,
        par.learnEpVersion,
        par.entity,
        par.x,
        par.y);

      dbSQL.commit(par.shouldCommit);

      return learnEpEntityUri;

    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return learnEpVersionAddEntity(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpCreate(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    final SSUri learnEp = learnEpCreate(parA);
    
    sSCon.writeRetFullToClient(SSLearnEpCreateRet.get(learnEp, parA.op));
    
    SSLearnEpActivityFct.createLearnEp(new SSLearnEpCreatePar(parA), learnEp);
  }
  
  @Override
  public SSUri learnEpCreate(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpCreatePar par        = new SSLearnEpCreatePar(parA);
      final SSUri              learnEpUri = sqlFct.createLearnEpUri();

      dbSQL.startTrans(par.shouldCommit);

      SSServCaller.entityEntityToPrivCircleAdd(
        par.user, 
        learnEpUri, 
        SSEntityE.learnEp,  
        par.label, 
        par.description, 
        null, 
        false);
        
      sqlFct.createLearnEp(learnEpUri, par.user);

      dbSQL.commit(par.shouldCommit);

      return learnEpUri;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return learnEpCreate(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionUpdateCircle(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionUpdateCircleRet.get(learnEpVersionUpdateCircle(parA), parA.op));
    
    SSLearnEpActivityFct.updateLearnEpVersionCircle(new SSLearnEpVersionUpdateCirclePar(parA));
  }
  
  @Override
  public Boolean learnEpVersionUpdateCircle(final SSServPar parA) throws Exception{

    try{

      final SSLearnEpVersionUpdateCirclePar par = new SSLearnEpVersionUpdateCirclePar(parA);

      SSServCaller.entityUserCanEdit(par.user, par.learnEpCircle);
      
      dbSQL.startTrans(par.shouldCommit);

      SSServCaller.entityEntityToPrivCircleAdd(
        par.user, 
        par.learnEpCircle, 
        SSEntityE.learnEpCircle,
        par.label, 
        null,
        null,
        false);

      sqlFct.updateCircle(
        par.learnEpCircle,
        par.xLabel,
        par.yLabel,
        par.xR,
        par.yR,
        par.xC,
        par.yC);

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return learnEpVersionUpdateCircle(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionUpdateEntity(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionUpdateEntityRet.get(learnEpVersionUpdateEntity(parA), parA.op));
    
    SSLearnEpActivityFct.updateLearnEpVersionEntity(new SSLearnEpVersionUpdateEntityPar(parA));
  }
  
  @Override
  public Boolean learnEpVersionUpdateEntity(SSServPar parA) throws Exception{
    
    try{
      
      final SSLearnEpVersionUpdateEntityPar par = new SSLearnEpVersionUpdateEntityPar(parA);

      SSServCaller.entityUserCanEdit(par.user, par.learnEpEntity);
      
      if(par.entity != null){
        SSServCaller.entityUserCanEdit(par.user, par.entity);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(par.entity != null){
        
        for(SSEntityCircle entityUserCircle : SSServCaller.entityUserEntityCirclesGet(par.user, par.learnEpEntity, true)){

          SSServCaller.entityEntitiesToCircleAdd(
            par.user,
            entityUserCircle.id,
            SSUri.asListWithoutNullAndEmpty(par.entity),
            false);
        }
      }
      
      sqlFct.updateEntity(
        par.learnEpEntity,
        par.entity,
        par.x,
        par.y);

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return learnEpVersionUpdateEntity(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionRemoveCircle(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionRemoveCircleRet.get(learnEpVersionRemoveCircle(parA), parA.op));
    
    SSLearnEpActivityFct.removeLearnEpVersionCircle(new SSLearnEpVersionRemoveCirclePar(parA));
  }
  
  @Override
  public Boolean learnEpVersionRemoveCircle(final SSServPar parA) throws Exception{

    try{

      final SSLearnEpVersionRemoveCirclePar par = new SSLearnEpVersionRemoveCirclePar(parA);

      SSServCaller.entityUserCanEdit(par.user, par.learnEpCircle);
      
      dbSQL.startTrans(par.shouldCommit);

      SSServCaller.entityRemove(par.learnEpCircle, false);

      dbSQL.commit(par.shouldCommit);

      return true;
   }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return learnEpVersionRemoveCircle(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionRemoveEntity(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionRemoveEntityRet.get(learnEpVersionRemoveEntity(parA), parA.op));
    
    SSLearnEpActivityFct.removeLearnEpVersionEntity(new SSLearnEpVersionRemoveEntityPar(parA));
  }
  
  @Override
  public Boolean learnEpVersionRemoveEntity(final SSServPar parA) throws Exception{

    try{

      final SSLearnEpVersionRemoveEntityPar par = new SSLearnEpVersionRemoveEntityPar(parA);

      SSServCaller.entityUserCanEdit(par.user, par.learnEpEntity);
      
      dbSQL.startTrans(par.shouldCommit);

      SSServCaller.entityRemove(par.learnEpEntity, false);

      dbSQL.commit(par.shouldCommit);

      return true;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return learnEpVersionRemoveEntity(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionSetTimelineState(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionSetTimelineStateRet.get(learnEpVersionSetTimelineState(parA), parA.op));
    
    SSServA.removeClientRequ(parA.op, SSStrU.toStr(parA.user), this);
  }
  
  @Override
  public SSUri learnEpVersionSetTimelineState(final SSServPar parA) throws Exception{

    try{

      final SSLearnEpVersionSetTimelineStatePar par                     = new SSLearnEpVersionSetTimelineStatePar(parA);
      final SSUri                               learnEpTimelineStateUri = sqlFct.createLearnEpTimelineStateUri();

      SSServCaller.entityUserCanEdit(par.user, par.learnEpVersion);
      
      dbSQL.startTrans(par.shouldCommit);

      SSServCaller.entityEntityToPrivCircleAdd(
        par.user, 
        learnEpTimelineStateUri, 
        SSEntityE.learnEpTimelineState, 
        null, 
        null, 
        null, 
        false);
        
      for(SSEntityCircle entityUserCircle : SSServCaller.entityUserEntityCirclesGet(par.user, par.learnEpVersion, true)){

        SSServCaller.entityEntitiesToCircleAdd(
          par.user,
          entityUserCircle.id,
          SSUri.asListWithoutNullAndEmpty(learnEpTimelineStateUri),
          false);
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return learnEpVersionSetTimelineState(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionGetTimelineState(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionGetTimelineStateRet.get(learnEpVersionGetTimelineState(parA), parA.op));
    
    SSServA.removeClientRequ(parA.op, SSStrU.toStr(parA.user), this);
  }
  
   @Override
  public SSLearnEpTimelineState learnEpVersionGetTimelineState(
    final SSServPar parA) throws Exception{

    try{

      final SSLearnEpVersionGetTimelineStatePar par = new SSLearnEpVersionGetTimelineStatePar(parA);

      SSServCaller.entityUserCanRead(par.user, par.learnEpVersion);
      
      return sqlFct.getLearnEpVersionTimelineState(par.learnEpVersion);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionCurrentGet(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSLearnEpVersionCurrentGetRet.get(learnEpVersionCurrentGet(par), par.op));
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
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void learnEpVersionCurrentSet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSLearnEpVersionCurrentSetRet.get(learnEpVersionCurrentSet(parA), parA.op));
    
    SSLearnEpActivityFct.setCurrentLearnEpVersion(new SSLearnEpVersionCurrentSetPar(parA));
  }

  @Override
  public SSUri learnEpVersionCurrentSet(final SSServPar parA) throws Exception{

    try{

      final SSLearnEpVersionCurrentSetPar par = new SSLearnEpVersionCurrentSetPar(parA);

      SSServCaller.entityUserCanEdit(par.user, par.learnEpVersion);
      
      dbSQL.startTrans(par.shouldCommit);

      sqlFct.setLearnEpCurrentVersion(par.user, par.learnEpVersion);

      dbSQL.commit(par.shouldCommit);

      return par.learnEpVersion;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return learnEpVersionCurrentSet(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSUri learnEpUserShareWithUser(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpUserShareWithUserPar par = new SSLearnEpUserShareWithUserPar(parA);

      SSServCaller.entityUserCanEdit(par.user, par.entity);
      
      if(sqlFct.ownsUserLearnEp(par.forUser, par.entity)){
        throw new Exception("learn ep is already shared with user");
      }

      dbSQL.startTrans(par.shouldCommit);

      SSLearnEpMiscFct.shareLearnEpWithUser(
        sqlFct,
        par.user,
        par.forUser,
        par.entity,
        par.circle);

      SSLearnEpActivityFct.shareLearnEp(par);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;

    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return learnEpUserShareWithUser(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSUri learnEpUserCopyForUser(final SSServPar parA) throws Exception{

    try{
      final SSLearnEpUserCopyForUserPar par = new SSLearnEpUserCopyForUserPar(parA);

      SSServCaller.entityUserCanEdit(par.user, par.entity);
      
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
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return learnEpUserCopyForUser(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
