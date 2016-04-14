 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.user.impl;

import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSCircleUsersAddPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityUpdatePar;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.conf.SSConfA;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.user.api.*;
import at.tugraz.sss.servs.user.datatype.SSUser;
import at.tugraz.sss.servs.user.datatype.*;
import at.tugraz.sss.servs.user.datatype.SSUserEntityUsersGetRet;
import at.tugraz.sss.servs.user.datatype.SSUsersGetRet;
import at.tugraz.sss.servs.entity.datatype.SSCirclePubURIGetPar;
import at.tugraz.sss.servs.entity.datatype.SSClientE;
import at.tugraz.sss.servs.common.api.SSDescribeEntityI;
import at.tugraz.sss.servs.common.datatype.SSEntityDescriberPar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import java.util.*;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import at.tugraz.sss.servs.entity.datatype.SSServRetI; 
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.entity.impl.*;
import at.tugraz.sss.servs.user.conf.*;

public class SSUserImpl
extends SSEntityImpl
implements
  SSUserClientI,
  SSUserServerI,
  SSDescribeEntityI{
  
  private final SSUserSQL sql = new SSUserSQL(dbSQL);
  
  public SSUserImpl(){
    super(SSCoreConf.instGet().getUser());
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(!SSStrU.isEqual(entity.author, SSConf.systemUserUri)){
        
        if(entity.author != null){
          
          final List<SSEntity> authors =
            usersGet(
              new SSUsersGetPar(
                servPar,
                par.user,
                SSUri.asListNotNull(entity.author.id),
                null, //emails
                false)); //invokeEntityHandlers))
          
          if(!authors.isEmpty()){
            entity.author = authors.get(0);
          }
        }
      }
      
      switch(entity.type){
        
        case user:{
          
          if(SSStrU.isEqual(entity, par.recursiveEntity)){
            return entity;
          }
          
          final List<SSEntity> users =
            usersGet(
              new SSUsersGetPar(
                servPar,
                par.user,
                SSUri.asListNotNull(entity.id),
                null, //emails
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
      final List<SSEntity>  users      = new ArrayList<>();
      final List<SSUri>     userURIs   = new ArrayList<>();
      SSUser                userToGet;
      SSEntity              userEntityToGet;
      SSEntityDescriberPar  descPar;
      
      for(String email : par.emails){
        
        SSUri.addDistinctWithoutNull(
          userURIs,
          sql.getUserURIForEmail(par, email));
      }
      
      if(!par.users.isEmpty()){
        
        for(SSUri userURI : sql.getUserURIs(par, par.users)){

          SSUri.addDistinctWithoutNull(
            userURIs,
            userURI);
        }
      }
      
      if(userURIs.isEmpty()){
        
        for(SSUri userURI : sql.getUserURIs(par, userURIs)){

          SSUri.addDistinctWithoutNull(
            userURIs,
            userURI);
        }
      }
      
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
          entityGet(
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
  public List<SSUri> usersPredefinedGet(final SSUsersPredefinedGetPar par) throws SSErr{
    
    try{
      
      if(par.group == null){
        return new ArrayList<>();
      }
            
      if(((SSUserConf) conf).usersPerGroup.isEmpty()){
        return new ArrayList<>();
      }
      
      final List<String> emails   = new ArrayList<>();
      final List<SSUri>  userURIs = new ArrayList<>();
      String             groupStr;
      String             userStr;
      
      for(String usersForGroup :((SSUserConf) conf).usersPerGroup){
        
        groupStr     = SSStrU.split(usersForGroup, SSStrU.colon).get(0);
        userStr     = SSStrU.split(usersForGroup, SSStrU.colon).get(1);
        
        if(!SSStrU.isEqual(par.group, groupStr)){
          continue;
        }
        
        emails.clear();
        emails.addAll(SSStrU.splitDistinctWithoutEmptyAndNull(userStr, SSStrU.comma));
        
        for(String email : emails){
          SSUri.addDistinctWithoutNull(userURIs, sql.getUserURIForEmail(par, email));
        }
      }
      
      return userURIs;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri userUpdate (final SSUserUpdatePar par) throws SSErr{
    
    try{
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.updateUser(
        par, 
        par.forUser, 
        par.oidcSub);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return par.forUser;
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
  public SSUri userAdd(final SSUserAddPar par) throws SSErr{
    
    try{
      
      if(par.withUserRestriction){
        throw SSErr.get(SSErrE.userCannotAddUser);
      }
      
      final SSLabel         tmpLabel;
      final String          tmpEmail;
      SSUri                 publicCircleURI;
      SSUri                 userUri;
      
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
        entityUpdate(
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
        circlePubURIGet(
          new SSCirclePubURIGetPar(
            par,
            par.user,
            false));
      
      if(publicCircleURI == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sql.addUser(
        par, 
        userUri, 
        tmpEmail.toLowerCase(), 
        par.oidcSub);
      
      publicCircleURI =
        circleUsersAdd(
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
            SSConf.systemUserUri,
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
            null, //emails
            false))){ //invokeEntityHandlers
        
        entity =
          sql.getEntityTest(
            par,
            SSConf.systemUserUri,
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
