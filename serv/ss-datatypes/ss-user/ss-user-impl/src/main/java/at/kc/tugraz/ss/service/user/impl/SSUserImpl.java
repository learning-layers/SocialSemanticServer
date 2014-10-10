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
package at.kc.tugraz.ss.service.user.impl;

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import at.kc.tugraz.ss.service.user.api.*;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserAddPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserExistsPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserURIGetPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUsersGetPar;
import at.kc.tugraz.ss.service.user.datatypes.ret.SSUserAllRet;
import at.kc.tugraz.ss.service.user.impl.functions.sql.SSUserSQLFct;
import java.util.*;
import sss.serv.err.datatypes.SSErrE;

public class SSUserImpl extends SSServImplWithDBA implements SSUserClientI, SSUserServerI, SSEntityHandlerImplI{
  
//  private final SSUserGraphFct       graphFct;
  private final SSUserSQLFct         sqlFct;
  
  public SSUserImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, dbGraph, dbSQL);
    
//    graphFct = new SSUserGraphFct (this);
    sqlFct   = new SSUserSQLFct   (this);
  }
  
  @Override
  public Boolean copyUserEntity(
    final SSUri        user,
    final List<SSUri>  users,
    final SSUri        entity,
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception{
    
    return false;
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
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{

    return null;
  }
  
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSUri       userUri, 
    final SSEntityE   entityType,
    final SSUri       entityUri,
    final Boolean     removeUserTags,
    final Boolean     removeUserRatings,
    final Boolean     removeFromUserColls,
    final Boolean     removeUserLocations) throws Exception{
  }
  
  @Override
  public Boolean setUserEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri, 
    final SSEntityE      entityType,
    final SSUri          publicCircleUri) throws Exception{

    return false;
  }
  
  @Override
  public void shareUserEntity(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          entityCircleUri,
    final SSEntityE      entityType,
    final Boolean        saveActivity) throws Exception{
  }
  
  @Override
  public Boolean addEntityToCircle(
    final SSUri        userUri, 
    final SSUri        circleUri, 
    final SSUri        entityUri, 
    final SSEntityE entityType) throws Exception{
    
    return false;
  }  
  
  /* SSUserClientI  */

  @Override
  public void userAll(SSSocketCon sSCon, SSServPar par) throws Exception {
    
//      if(SSAuthEnum.isSame(SSAuthServ.inst().getAuthType(), SSAuthEnum.wikiAuth)){
//        returnObj.object =  new SSAuthWikiDbCon(new SSAuthWikiConf()).getUserList(); //TODO remove new SSAuthWikiConf() --> take it from config
//      }else{
        
    SSServCaller.checkKey(par);
    
    sSCon.writeRetFullToClient(SSUserAllRet.get(userAll(par), par.op));
//      }
  }

  /* SSUserServerI */
  @Override
  public Boolean userExists(final SSServPar parA) throws Exception{
    
    try{
      
      final SSUserExistsPar par = new SSUserExistsPar (parA);
      
      return sqlFct.existsUser(par.email);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri userURIGet(final SSServPar parA) throws Exception{
    
    try{
      final SSUserURIGetPar par = new SSUserURIGetPar (parA);
      
      return sqlFct.getUserURIForEmail(par.email);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUser> userAll(final SSServPar parA) throws Exception {
    return sqlFct.userAll();
  }

  @Override 
  public List<SSUser> usersGet(final SSServPar parA) throws Exception{
    
    try{
      final SSUsersGetPar par   = new SSUsersGetPar(parA);
      final List<SSUser>  users = new ArrayList<>();
      
      for(SSUri userUri : par.users){
        users.add(sqlFct.getUser(userUri));
      }
      
      return users;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public SSUri userAdd(final SSServPar parA) throws Exception{
    
    try{
      final SSUserAddPar  par      = new SSUserAddPar(parA);
      final SSUri         userUri;
      final SSLabel       tmpLabel;
      final String        tmpEmail;
      
      if(par.isSystemUser){
        userUri  = SSVoc.systemUserUri;
        tmpLabel = SSVoc.systemUserLabel;
        tmpEmail = SSVoc.systemUserEmail;
      }else{
        userUri  = SSServCaller.vocURICreate();
        tmpLabel = par.label;
        tmpEmail = par.email;        
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntityToPrivCircleAdd(
        SSVoc.systemUserUri,
        userUri,
        SSEntityE.user,
        tmpLabel,
        null,
        null,
        false);
      
      SSServCaller.entityEntitiesToCircleAdd(
        SSVoc.systemUserUri,
        SSServCaller.entityCircleURIPubGet(false),
        SSUri.asListWithoutNullAndEmpty(userUri),
        false);
      
      sqlFct.addUser(userUri, tmpEmail);
      
      dbSQL.commit(par.shouldCommit);
      
      return userUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return userAdd(parA);
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

//@Override
//  public String userNameFromUri(SSServPar parI) throws Exception {
//    
//    SSUserNameFromUriPar par = new SSUserNameFromUriPar (parI);
//    
//    String userUri;
//    
//    if(SSObjU.isNull(par.user)){
//      return null;
//    }
//    
//    userUri = SSStrU.removeTrailingSlash(SSStrU.toStr(par.user));
//    
//    return userUri.substring(userUri.lastIndexOf(SSStrU.slash) + 1, userUri.length());
//  }