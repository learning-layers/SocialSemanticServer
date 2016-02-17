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
package at.kc.tugraz.ss.serv.auth.impl;

import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.kc.tugraz.ss.serv.auth.api.SSAuthClientI;
import at.kc.tugraz.ss.serv.auth.api.SSAuthServerI;
import at.kc.tugraz.ss.serv.auth.conf.SSAuthConf;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportServerI;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportSSSUsersFromCSVFilePar;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthCheckCredPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthCheckKeyPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthRegisterUserPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthUsersFromCSVFileAddPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.ret.SSAuthCheckCredRet;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.ret.SSAuthRegisterUserRet;
import at.tugraz.sss.conf.SSConf;
import at.kc.tugraz.ss.service.coll.api.SSCollServerI;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootAddPar;
import at.kc.tugraz.ss.service.user.api.SSUserServerI;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserAddPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserExistsPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserURIGetPar;
import at.tugraz.sss.conf.*;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 
import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.http.*;
import com.nimbusds.openid.connect.sdk.*;
import java.io.*;
import java.net.*;
import java.security.*;

public class SSAuthImpl 
extends 
  SSServImplWithDBA 
implements 
  SSAuthClientI, 
  SSAuthServerI{
  
  private final List<String>    csvFileAuthKeys = new ArrayList<>();
  private final SSAuthSQL       sql;
  
  private static final Map<String, String> oidcAuthTokens = new HashMap<>();
  
  public SSAuthImpl(final SSAuthConf conf) throws SSErr {
    
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql = new SSAuthSQL(dbSQL);
  }
  
  @Override
  public void authUsersFromCSVFileAdd(final SSAuthUsersFromCSVFileAddPar par) throws SSErr {
    
    try{
      final Map<String, String>          passwordsForUsersFromCSVFile = new HashMap<>();
      final SSDataImportServerI          dataImportServ               = (SSDataImportServerI) SSServReg.getServ(SSDataImportServerI.class);
      
      try{
        
        passwordsForUsersFromCSVFile.putAll(
          dataImportServ.dataImportSSSUsersFromCSVFile(
            new SSDataImportSSSUsersFromCSVFilePar(
              par,
              par.user,
              conf.getSssWorkDir() + SSFileU.fileNameUsersCsv)));
        
      }catch(SSErr error){
        
        switch(error.code){
          case servInvalid: SSLogU.warn(error); return;
          default: {
            SSServErrReg.regErrThrow(error);
            break;
          }
        }
      }      
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      String email;
      
      for(Map.Entry<String, String> passwordForUser : passwordsForUsersFromCSVFile.entrySet()){

        if(passwordForUser.getKey().contains(SSStrU.at)){
          email = passwordForUser.getKey();
        }else{
          email = passwordForUser.getKey() + SSStrU.at + SSConf.systemEmailPostFix;
        }
        
        authRegisterUser(
          new SSAuthRegisterUserPar(
            par,
            email, 
            passwordForUser.getValue(), 
            SSLabel.get(passwordForUser.getKey()), 
            true, //updatePassword, 
            false, //isSystemUser, 
            false, //withUserRestriction, 
            false)); //shouldCommit)
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSServRetI authRegisterUser(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      final SSAuthRegisterUserPar par = (SSAuthRegisterUserPar) parA.getFromClient(clientType, parA, SSAuthRegisterUserPar.class);
      
      return SSAuthRegisterUserRet.get(authRegisterUser(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override 
  public SSUri authRegisterUser(final SSAuthRegisterUserPar par) throws SSErr{
    
    try{
      final SSUserServerI userServ = (SSUserServerI) SSServReg.getServ(SSUserServerI.class);
      
      if(par.email == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final SSUri     userUri;
      final boolean   userExists;
      
      userExists = 
        userServ.userExists(
          new SSUserExistsPar(
            par,
            SSConf.systemUserUri,
            par.email));
        
      dbSQL.startTrans(par, par.shouldCommit);
      
      if(!userExists){
        
        userUri = 
          userServ.userAdd(
            new SSUserAddPar(
              par,
              SSConf.systemUserUri,
              false,
              par.label,
              par.email,
              par.isSystemUser, 
              false));
        
        sql.removeKey(par, userUri);
        
        sql.addKey(
          par,
          userUri,
          genKey(par.email, par.password));
        
      }else{
        
        userUri =
          userServ.userURIGet(
            new SSUserURIGetPar(
              par,
              SSConf.systemUserUri,
              par.email));
        
        if(par.updatePassword){
          
          sql.removeKey(par, userUri);
          
          sql.addKey(
            par,
            userUri,
            genKey(par.email, par.password));
        }
      }
      
      addRootColl(par, userUri);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return userUri;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
 
  @Override
  public SSServRetI authCheckCred(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      
      final SSAuthCheckCredPar par = (SSAuthCheckCredPar) parA.getFromClient(clientType, parA, SSAuthCheckCredPar.class);
      
      return authCheckCred(par);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSAuthCheckCredRet authCheckCred(final SSAuthCheckCredPar par) throws SSErr {
    
    try{

      final SSUserServerI userServ = (SSUserServerI) SSServReg.getServ(SSUserServerI.class);
      
      switch(((SSAuthConf)conf).authType){

        case csvFileAuth:{

          final SSUri userUri;
          
          if(par.key != null){
            
            userUri = sql.getUserForKey(par, par.key);
            
            return new SSAuthCheckCredRet(
              par.key,
              userUri);
            
          }else{

            String email = SSStrU.toStr(par.label);

            if(!email.contains("@")){
             email = email + SSStrU.at + SSConf.systemEmailPostFix;
            }

            if(!userServ.userExists(
              new SSUserExistsPar(
                par,
                SSConf.systemUserUri,
                email))){
              
              throw SSErr.get(SSErrE.userNotRegistered);
            }
            
            userUri =
              userServ.userURIGet(
                new SSUserURIGetPar(
                  par,
                  SSConf.systemUserUri,
                  email));
            
            return new SSAuthCheckCredRet(
              checkAndGetKey(
                par,
                userUri,
                email,
                par.password),
              userUri);
          }
        }
        
        case oidc:{
          
          SSUri userUri;
          
          if(oidcAuthTokens.containsKey(par.key)){
            userUri = SSUri.get(oidcAuthTokens.get(par.key));
          }else{
          
            final String email = getOIDCUserEmail(par.key);
          
            if(!userServ.userExists(
              new SSUserExistsPar(
                par,
                SSConf.systemUserUri,
                email))){

              //TODO use authRegisterUser
              userUri = 
                userServ.userAdd(
                  new SSUserAddPar(
                    par,
                    SSConf.systemUserUri, 
                    true, 
                    SSLabel.get(email), 
                    email, 
                    false, //isSystemUser
                    false)); //withUserRestriction

              addRootColl(par, userUri);
              
            }else{

              userUri = 
               userServ.userURIGet(
                 new SSUserURIGetPar(
                   par,
                   SSConf.systemUserUri, 
                   email));
            }
            
            final String userStr = SSStrU.toStr(userUri);
            
            if(oidcAuthTokens.containsValue(userStr)){
              
              for(Map.Entry<String, String> entrySet : oidcAuthTokens.entrySet()){
                
                if(SSStrU.equals(entrySet.getValue(), userStr)){
                  oidcAuthTokens.remove(entrySet.getKey());
                  break;
                }
              }
            }
            
            oidcAuthTokens.put(par.key, userStr);
          }

          return new SSAuthCheckCredRet(
            par.key,
            userUri);
        }

        default:{
          throw new UnsupportedOperationException();
        }

      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri authCheckKey(final SSAuthCheckKeyPar par) throws SSErr {
    
    try{
      
      switch(((SSAuthConf)conf).authType){
        
//        case noAuth: return null;
        
        case csvFileAuth:{
          
          if(csvFileAuthKeys != null){
            csvFileAuthKeys.addAll(sql.getKeys(par));
          }
          
          if(!csvFileAuthKeys.contains(par.key)){
            throw SSErr.get(SSErrE.userKeyWrong);
          }
          
          return authCheckCred(new SSAuthCheckCredPar(par, par.key)).user;
        }
        
        case oidc:{
          return authCheckCred(new SSAuthCheckCredPar(par, par.key)).user;
        }
        
        default: throw SSErr.get(SSErrE.authNoUserForKey);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private String getOIDCUserEmail(final String authToken) throws SSErr{
    
    // send request to OpenID Connect user info endpoint to retrieve complete user information
    // in exchange for access token.
    final HTTPRequest      hrq;
    final HTTPResponse     hrs;
    final UserInfoResponse userInfoResponse;
    
    try{
      try{
        //      URI userinfoEndpointUri = new URI((String)((JSONObject) fetchOidcProviderConfig().get("config")).get("userinfo_endpoint"));
        hrq                     = new HTTPRequest(HTTPRequest.Method.GET, new URL(SSCoreConf.instGet().getAuth().oidcUserEndPointURI));  //userinfoEndpointUri.toURL()
        hrq.setAuthorization("Bearer "+ authToken);
        
        //TODO: process all error cases that can happen (in particular invalid tokens)
        hrs = hrq.send();
        
      } catch (IOException error) {
        SSServErrReg.regErrThrow(SSErrE.authCouldntConnectToOIDC, error);
        return null;
      }
      
      // process response from OpenID Connect user info endpoint
      
      try {
        userInfoResponse = UserInfoResponse.parse(hrs);
      } catch (ParseException error) {
        SSServErrReg.regErrThrow(SSErrE.authCouldntParseOIDCUserInfoResponse, error);
        return null;
      }
      
      // failed request for OpenID Connect user info will result in no agent being returned.
      if (userInfoResponse instanceof UserInfoErrorResponse) {
        
        if(
          ((UserInfoErrorResponse) userInfoResponse).getErrorObject()                  != null &&
          ((UserInfoErrorResponse) userInfoResponse).getErrorObject().getDescription() != null){
          
          throw SSErr.get(SSErrE.authOIDCUserInfoRequestFailed); // "Cause: " + ((UserInfoErrorResponse) userInfoResponse).getErrorObject().getDescription());
        }else{
          throw SSErr.get(SSErrE.authOIDCUserInfoRequestFailed);
        }
      }
      
      return (String) ((UserInfoSuccessResponse)userInfoResponse).getUserInfo().toJSONObject().get(SSVarNames.email);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private void addRootColl(
    final SSServPar servPar,
    final SSUri     userURI) throws SSErr {
    
    try{
      
      final SSCollServerI collServ = (SSCollServerI) SSServReg.getServ(SSCollServerI.class);
      
      collServ.collRootAdd(
        new SSCollUserRootAddPar(
          servPar,
          SSConf.systemUserUri,
          userURI,
          false));
      
    }catch(SSErr error){
      
      switch(error.code){
        
        case servInvalid:{
          SSLogU.warn(error);
          return;
        }
      }
      
      SSServErrReg.regErrThrow(error);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private String genKey(
    final String email, 
    final String password) throws SSErr{
    
    try{
      
      if(email == null){
        SSServErrReg.regErrThrow(SSErrE.parameterMissing);
        return null;
      }
      
      final String toDigest = email.toLowerCase() + password;
      
      if(SSStrU.isEmpty(toDigest)){
        throw new Exception("to digest string not valid");
      }
      
      final MessageDigest digest = MessageDigest.getInstance(SSEncodingU.md5.toString());
      
      byte[] hash = digest.digest(toDigest.getBytes());
      
      //converting byte array to Hexadecimal String
      final StringBuilder sb = new StringBuilder(2 * hash.length);
      
      for(byte b : hash){
        sb.append(String.format("%02x", b&0xff));
      }
      
      return sb.toString();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private String checkAndGetKey(
    final SSServPar    servPar, 
    final SSUri        userUri,
    final String       email,
    final String       pass) throws SSErr{
    
    try{
      
      if(
        !sql.hasKey(servPar, userUri)){
        throw SSErr.get(SSErrE.userNotRegistered);
      }
       
      final String key = sql.getKey(servPar, userUri);
      
      if(!key.equals(genKey(email, pass))){
        throw SSErr.get(SSErrE.userKeyWrong);
      }
      
      return key;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}