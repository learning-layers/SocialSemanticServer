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

import at.tugraz.sss.serv.datatype.par.SSCircleUsersAddPar;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import at.tugraz.sss.conf.SSConf;
import at.kc.tugraz.ss.service.user.api.*;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserAddPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserEntityUsersGetPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserExistsPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserURIGetPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserURIsGetPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUsersGetPar;
import at.kc.tugraz.ss.service.user.datatypes.ret.SSUserEntityUsersGetRet;
import at.kc.tugraz.ss.service.user.datatypes.ret.SSUsersGetRet;
import at.kc.tugraz.ss.service.user.impl.functions.sql.SSUserSQLFct;
import at.tugraz.sss.serv.datatype.par.SSCirclePubURIGetPar;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.entity.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import java.util.*;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 

public class SSUserImpl
extends SSServImplWithDBA
implements
  SSUserClientI,
  SSUserServerI,
  SSDescribeEntityI{
  
  private final SSUserSQLFct      sql;
  private final SSEntityServerI   entityServ;
  private final SSEntityServerI   circleServ;
  private final SSUserCommons  userCommons;
  
  public SSUserImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql          = new SSUserSQLFct(dbSQL, SSConf.systemUserUri);
    this.entityServ   = (SSEntityServerI)   SSServReg.getServ(SSEntityServerI.class);
    this.circleServ   = (SSEntityServerI)   SSServReg.getServ(SSEntityServerI.class);
    this.userCommons  = new SSUserCommons();
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(!SSStrU.equals(entity.author, SSConf.systemUserUri)){
        
        if(entity.author != null){
          
          final List<SSEntity> authors =
            usersGet(
              new SSUsersGetPar(
                servPar,
                par.user,
                SSUri.asListNotNull(entity.author.id),
                false)); //invokeEntityHandlers))
          
          if(!authors.isEmpty()){
            entity.author = authors.get(0);
          }
        }
      }
      
      switch(entity.type){
        
        case user:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          final List<SSEntity> users =
            usersGet(
              new SSUsersGetPar(
                servPar,
                par.user,
                SSUri.asListNotNull(entity.id),
                false)); //invokeEntityHandlers))
          
          if(!users.isEmpty()){
            
            return SSUser.get(
              (SSUser) users.get(0),
              entity);
          }else{
            return entity;
          }
        }
        
        default: return entity;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean userExists(final SSUserExistsPar par) throws SSErr{
    
    try{
      
      if(par.email == null){
        return false;
      }
      
      return sql.existsUser(par, par.email.toLowerCase());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public SSUri userURIGet(final SSUserURIGetPar par) throws SSErr{
    
    try{
      
      if(par.email == null){
        return null;
      }
      
      return sql.getUserURIForEmail(par, par.email.toLowerCase());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  
  @Override
  public List<SSUri> userURIsGet(final SSUserURIsGetPar par) throws SSErr{
    
    try{
      
      final List<SSUri> uris = new ArrayList<>();
      
      for(String email : par.emails){
        
        if(email == null){
          uris.add(null);
          continue;
        }
        
        uris.add(sql.getUserURIForEmail(par, email.toLowerCase()));
      }
      
      return uris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI usersGet(SSClientE clientType, SSServPar parA) throws SSErr {
    
//      if(SSAuthEnum.isSame(SSAuthServ.inst().getAuthType(), SSAuthEnum.wikiAuth)){
//        returnObj.object =  new SSAuthWikiDbCon(new SSAuthWikiConf()).getUserList(); //TODO remove new SSAuthWikiConf() --> take it from config
//      }else{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSUsersGetPar par = (SSUsersGetPar) parA.getFromClient(clientType, parA, SSUsersGetPar.class);
      
      return new SSUsersGetRet(usersGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
//      }
  }
  
  @Override
  public List<SSEntity> usersGet(final SSUsersGetPar par) throws SSErr{
    
    try{
      
      final List<SSUri>    userURIs   = sql.getUserURIs(par, par.users);
      final List<SSEntity> users      = new ArrayList<>();
      SSUser               userToGet;
      SSEntity             userEntityToGet;
      SSEntityDescriberPar descPar;
      
      for(SSUri userURI : userURIs){
        
        userToGet = sql.getUser(par, userURI);
        
        if(userToGet == null){
          continue;
        }
        
        if(par.invokeEntityHandlers){
          descPar = new SSEntityDescriberPar(userToGet.id);
          
          descPar.setProfilePicture = par.setProfilePicture;
          descPar.setFriends        = par.setFriends;
          descPar.setThumb          = par.setThumb;
          descPar.setMessages       = par.setMessages;
          descPar.setActivities     = par.setActivities;
          descPar.setCircles        = par.setCircles;
          descPar.setDiscs          = par.setDiscs;
          descPar.setColls          = par.setColls;
          descPar.setTags           = par.setTags;
          
        }else{
          descPar = null;
        }
        
        userEntityToGet =
          entityServ.entityGet(
            new SSEntityGetPar(
              par,
              par.user,
              userToGet.id,
              par.withUserRestriction,
              descPar));
        
        if(userEntityToGet == null){
          continue;
        }
        
        userToGet =
          SSUser.get(
            userToGet,
            userEntityToGet);
        
        if(
          par.invokeEntityHandlers &&
          par.setFriends){
          
          userToGet.friend = SSStrU.contains(userToGet.friends, par.user);
        }
        
        users.add(userToGet);
      }
      
      return users;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri userAdd(final SSUserAddPar par) throws SSErr{
    
    try{
      
      if(par.withUserRestriction){
        throw SSErr.get(SSErrE.userCannotAddUser);
      }
      
      final SSLabel       tmpLabel;
      final String        tmpEmail;
      SSUri               publicCircleURI;
      SSUri               userUri;
      
      if(par.isSystemUser){
        userUri  = SSConf.systemUserUri;
        tmpLabel = SSLabel.get(SSConf.systemUserLabel);
        tmpEmail = SSConf.systemUserEmail;
      }else{
        userUri  = SSConf.vocURICreate();
        tmpLabel = par.label;
        tmpEmail = par.email;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      userUri =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
            SSConf.systemUserUri,
            userUri,
            SSEntityE.user, //type,
            tmpLabel, //label
            null,//description,
            null, //creationTime,
            null, //read,
            true, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(userUri == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      publicCircleURI =
        circleServ.circlePubURIGet(
          new SSCirclePubURIGetPar(
            par,
            par.user,
            false));
      
      if(publicCircleURI == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sql.addUser(par, userUri, tmpEmail.toLowerCase());
      
      publicCircleURI =
        circleServ.circleUsersAdd(
          new SSCircleUsersAddPar(
            par,
            SSConf.systemUserUri,
            publicCircleURI, //circle
            SSUri.asListNotNull(userUri), //users
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit
      
      if(publicCircleURI == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      return userUri;
      
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
  public SSServRetI userEntityUsersGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSUserEntityUsersGetPar par = (SSUserEntityUsersGetPar) parA.getFromClient(clientType, parA, SSUserEntityUsersGetPar.class);
      
      return SSUserEntityUsersGetRet.get(userEntityUsersGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> userEntityUsersGet(final SSUserEntityUsersGetPar par) throws SSErr{
    
    try{
      
      final List<SSEntity> users = new ArrayList<>();
      
      if(!sql.existsEntity(par, par.entity)){
        return users;
      }
      
      SSEntity entity;
      
      if(par.withUserRestriction){
        
        entity =
          sql.getEntityTest(
            par,
            par.user,
            par.entity,
            par.withUserRestriction);
        
        if(entity == null){
          return users;
        }
      }
      
      for(SSEntity user :
        usersGet(
          new SSUsersGetPar(
            par,
            par.user,
            null, //users
            false))){ //invokeEntityHandlers
        
        entity =
          sql.getEntityTest(
            par,
            user.id,
            par.entity,
            par.withUserRestriction);
        
        if(entity == null){
          continue;
        }
        
        users.add(user);
      }
      
      return users;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

//  private void setUserThumb(
//    final SSUri          callingUser,
//    final SSUser         user,
//    final SSImageServerI imageServ,
//    final boolean        withUserRestriction) throws SSErr{
//
//    try{
//
//      for(SSEntity thumb :
//        imageServ.imagesGet(
//          new SSImagesGetPar(
//            callingUser,
//            user.id,
//            SSImageE.thumb,
//            withUserRestriction))){
//
//        user.thumb = thumb;
//        break;
//      }
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
