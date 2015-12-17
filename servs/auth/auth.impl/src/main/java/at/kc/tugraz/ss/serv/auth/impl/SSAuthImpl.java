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

import at.tugraz.sss.serv.SSLogU;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSLabel;
import at.kc.tugraz.ss.serv.auth.api.SSAuthClientI;
import at.kc.tugraz.ss.serv.auth.api.SSAuthServerI;
import at.kc.tugraz.ss.serv.auth.conf.SSAuthConf;
import at.kc.tugraz.ss.serv.auth.impl.fct.csv.SSAuthMiscFct;
import at.kc.tugraz.ss.serv.auth.impl.fct.oidc.SSAuthOIDC;
import at.kc.tugraz.ss.serv.auth.impl.fct.sql.SSAuthSQLFct;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportServerI;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportSSSUsersFromCSVFilePar;
import at.tugraz.sss.serv.SSServPar; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthCheckCredPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthCheckKeyPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthRegisterUserPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthUsersFromCSVFileAddPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.ret.SSAuthCheckCredRet;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.ret.SSAuthRegisterUserRet;
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.kc.tugraz.ss.service.coll.api.SSCollServerI;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootAddPar;
import at.kc.tugraz.ss.service.user.api.SSUserServerI;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserAddPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserExistsPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserURIGetPar;
import at.tugraz.sss.serv.SSClientE;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServRetI; import at.tugraz.sss.serv.SSVarNames;

public class SSAuthImpl extends SSServImplWithDBA implements SSAuthClientI, SSAuthServerI{
  
  private final SSAuthSQLFct    sqlFct;
  private final List<String>    csvFileAuthKeys = new ArrayList<>();
  private final SSUserServerI   userServ;
  private final SSCollServerI   collServ;
  
  private static final Map<String, String> oidcAuthTokens = new HashMap<>();
  
  public SSAuthImpl(final SSAuthConf conf) throws SSErr {
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.getServImpl(), (SSDBNoSQLI) SSDBNoSQL.inst.getServImpl());
    
    this.sqlFct   = new SSAuthSQLFct(dbSQL);
    this.userServ = (SSUserServerI) SSServReg.getServ(SSUserServerI.class);
    this.collServ = (SSCollServerI) SSServReg.getServ(SSCollServerI.class);
      
//    wikiauth  = new SSAuthWiki();
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
              par.user,
              ((SSAuthConf)conf).fileName)));
        
      }catch(SSErr error){
        
        switch(error.code){
          case servServerNotAvailable: SSLogU.warn(error.getMessage()); return;
          default: SSServErrReg.regErrThrow(error);
        }
      }      
      
      dbSQL.startTrans(par.shouldCommit);
      
      String email;
      
      for(Map.Entry<String, String> passwordForUser : passwordsForUsersFromCSVFile.entrySet()){

        if(passwordForUser.getKey().contains("@")){
          email = passwordForUser.getKey();
        }else{
          email = passwordForUser.getKey() + SSStrU.at + SSVocConf.systemEmailPostFix;
        }
        
        authRegisterUser(
          new SSAuthRegisterUserPar(
            email, 
            passwordForUser.getValue(), 
            SSLabel.get(passwordForUser.getKey()), 
            true, //updatePassword, 
            false, //isSystemUser, 
            false, //withUserRestriction, 
            false)); //shouldCommit)
      }
      
      dbSQL.commit(par.shouldCommit);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSServRetI authRegisterUser(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      final SSAuthRegisterUserPar par = (SSAuthRegisterUserPar) parA.getFromJSON(SSAuthRegisterUserPar.class);
      
      return SSAuthRegisterUserRet.get(authRegisterUser(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override 
  public SSUri authRegisterUser(final SSAuthRegisterUserPar par) throws SSErr{
    
    try{
      
      if(par.email == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final SSUri     userUri;
      final Boolean   userExists;
      
      userExists = 
        userServ.userExists(
          new SSUserExistsPar(
            SSVocConf.systemUserUri,
            par.email));
        
      dbSQL.startTrans(par.shouldCommit);
      
      if(!userExists){
        
        userUri = 
          userServ.userAdd(
            new SSUserAddPar(
              SSVocConf.systemUserUri,
              false,
              par.label,
              par.email,
              par.isSystemUser, 
              false));
        
        sqlFct.removeKey(userUri);
        
        sqlFct.addKey(
          userUri,
          SSAuthMiscFct.genKey(par.email, par.password));
        
      }else{
        
        userUri =
          userServ.userURIGet(
            new SSUserURIGetPar(
              SSVocConf.systemUserUri,
              par.email));
        
        if(par.updatePassword){
          
          sqlFct.removeKey(userUri);
          
          sqlFct.addKey(
            userUri,
            SSAuthMiscFct.genKey(par.email, par.password));
        }
      }
      
      try{
        
        collServ.collRootAdd(
          new SSCollUserRootAddPar(
            SSVocConf.systemUserUri,
            userUri, 
            false));
        
      }catch(SSErr error){
        
        switch(error.code){
          case servServerNotAvailable: SSLogU.warn(error.getMessage()); break;
          default: SSServErrReg.regErrThrow(error);
        }
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return userUri;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
 
  @Override
  public SSServRetI authCheckCred(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      
      final SSAuthCheckCredPar par = (SSAuthCheckCredPar) parA.getFromJSON(SSAuthCheckCredPar.class);
      
      return authCheckCred(par);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSAuthCheckCredRet authCheckCred(final SSAuthCheckCredPar par) throws SSErr {
    
    try{

      switch(((SSAuthConf)conf).authType){

//        case noAuth:{
//
//          final String email = SSStrU.toStr(par.label) + SSStrU.at + SSVocConf.systemEmailPostFix;
//          final SSUri  userUri;
//          
//          if(!userServ.userExists(
//            new SSUserExistsPar(
//              null,
//              null,
//              SSVocConf.systemUserUri,
//              email))){
//                
//            userUri =
//              userServ.userAdd(
//                new SSUserAddPar(
//                  null, 
//                  null, 
//                  SSVocConf.systemUserUri, 
//                  true, 
//                  par.label, 
//                  email, 
//                  false, //isSystemUser
//                  false)); //withUserRestriction
//            
//            try{
//              
//              collServ.collRootAdd(
//                new SSCollUserRootAddPar(
//                  SSVocConf.systemUserUri,
//                  userUri,
//                  true));
//              
//            }catch(SSErr error){
//              
//              switch(error.code){
//                case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
//                default: SSServErrReg.regErrThrow(error);
//              }
//            }
//          }else{
//            
//           userUri = 
//             userServ.userURIGet(
//               new SSUserURIGetPar(
//                 null, 
//                 null, 
//                 SSVocConf.systemUserUri, 
//                 email));
//          }
//          
//          return SSAuthCheckCredRet.get(
//            SSAuthConf.noAuthKey,
//            userUri,
//            SSVarNames.authCheckCred);
//        }

        case csvFileAuth:{

          final SSUri userUri;
          
          if(par.key != null){
            
            userUri = sqlFct.getUserForKey(par.key);
            
            return SSAuthCheckCredRet.get(
              par.key,
              userUri,
              SSVarNames.authCheckCred);
            
          }else{

            String email = SSStrU.toStr(par.label);

            if(!email.contains("@")){
             email = email + SSStrU.at + SSVocConf.systemEmailPostFix;
            }

            if(!userServ.userExists(
              new SSUserExistsPar(
                SSVocConf.systemUserUri,
                email))){
              
              throw SSErr.get(SSErrE.userNotRegistered);
            }
            
            userUri = 
             userServ.userURIGet(
               new SSUserURIGetPar(
                 SSVocConf.systemUserUri, 
                 email));
            
            return SSAuthCheckCredRet.get(
              SSAuthMiscFct.checkAndGetKey(
                sqlFct,
                userUri,
                email,
                par.password),
              userUri,
              SSVarNames.authCheckCred);
          }
        }
        
        case oidc:{
          
          SSUri        userUri;
          
          if(oidcAuthTokens.containsKey(par.key)){
            userUri = SSUri.get(oidcAuthTokens.get(par.key));
          }else{
          
            final String email = SSAuthOIDC.getOIDCUserEmail(par.key);
          
            if(!userServ.userExists(
              new SSUserExistsPar(
                SSVocConf.systemUserUri,
                email))){

              //TODO use authRegisterUser
              userUri = 
                userServ.userAdd(
                  new SSUserAddPar(
                    SSVocConf.systemUserUri, 
                    true, 
                    SSLabel.get(email), 
                    email, 
                    false, //isSystemUser
                    false)); //withUserRestriction

              try{
                collServ.collRootAdd(
                  new SSCollUserRootAddPar(
                    SSVocConf.systemUserUri,
                    userUri,
                    true));
              }catch(SSErr error){

                switch(error.code){
                  case servServerNotAvailable: SSLogU.warn(error.getMessage()); break;
                  default: SSServErrReg.regErrThrow(error);
                }
              }
            }else{

              userUri = 
               userServ.userURIGet(
                 new SSUserURIGetPar(
                   SSVocConf.systemUserUri, 
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

          return SSAuthCheckCredRet.get(
            par.key,
            userUri,
            SSVarNames.authCheckCred);
        }

        default: 
          throw new UnsupportedOperationException();
        
//      case wikiAuth:{
//        // TODO get SSAuthWikiConf
//        boolean authUser = wikiauth.authUser(par.user, par.pass, new SSAuthWikiConf());
//
//        if (authUser) {
//
//          if (SSStrU.containsNot(keylist, alternateKeys[0])) {
//            keylist.add(alternateKeys[0]);
//          }
//
//          return alternateKeys[0];
//        }else{
//          Exception ile = new Exception();
//
//          throw ile;
//        }
//      }
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
            csvFileAuthKeys.addAll(sqlFct.getKeys());
          }
          
          if(!csvFileAuthKeys.contains(par.key)){
            throw SSErr.get(SSErrE.userKeyWrong);
          }
          
          return authCheckCred(new SSAuthCheckCredPar(par.key)).user;
        }
        
        case oidc:{
          return authCheckCred(new SSAuthCheckCredPar(par.key)).user;
        }
        
        default: throw SSErr.get(SSErrE.authNoUserForKey);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}


//  @Override
//  public List<String> authKeyList(SSServerPar par) throws SSErr {
//    return keylist;
//  }
//
//  @Override
//  public SSAuthEnum authAuthType(SSServerPar par) throws SSErr {
//    return conf.getAuthTypeEnum();
//  }


//private static String key;
//private static Date keyDate;
//private static Map<String, String> passwords = new HashMap<>();
//private static String credPath = "/home/nweber/workspace/Dem1Files/";
//private MessageDigest md5;
//  @Deprecated
//  private void createKey() {
//    Date date = new Date();
//
//    /* Berechnung */
//    try {
//      md5 = MessageDigest.getInstance("MD5");
//    } catch (NoSuchAlgorithmException e) {
//      log.error(e.getMessage(), e);
//    }
//    md5.reset();
//    md5.update(date.toGMTString().getBytes());
//    byte[] result = md5.digest();
//
//    /* Ausgabe */
//    StringBuilder hexString = new StringBuilder();
//    for (int i = 0; i < result.length; i++) {
//      hexString.append(Integer.toHexString(0xFF & result[i]));
//    }
//    if (log.isDebugEnabled()) {
//      log.debug("MD5: " + hexString.toString());
//    }
//    this.key = hexString.toString();
//    this.keyDate = date;
//
//  }
//
//  @Deprecated
//  private static void initPasswords() {
//    String[] userpass;
//    List outList = new ArrayList();
//    try {
//
//      boolean exists = (new File(credPath + "passwords.txt")).exists();
//      BufferedReader in = null;
//      if (exists) {
//        in = new BufferedReader(new FileReader(credPath + "passwords.txt"));
//      } else {
//        log.error("ERROR: PASSWORD FILE MISSING !!!!");
//      }
//
//      if (in == null) {
//        return;
//      }
//
//      String str;
//      while ((str = in.readLine()) != null) {
//        userpass = str.split(":");
//        passwords.put(userpass[0], userpass[1]);
//      }
//      in.close();
//
//    } catch (IOException e) {
//      log.error(e.getMessage(), e);
//    }
//  }
//
//	public String addUserCred(String user, String pass, String token)
//	{
//		if(checkToken(token))
//		{
//			try {
//			    BufferedWriter out = new BufferedWriter(new FileWriter(credPath+"passwords.txt",true));
//			    	out.newLine();
//			    	out.write(user+":"+pass);
//			    			    		    
//			    out.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return getKey();
//		}
//		return null;
//	}

//  private boolean checkToken(String token) {
//    boolean found = false;
//    List<String> tokenList = new ArrayList<>();
//    try {
//
//      boolean exists = (new File(credPath + "tokens.txt")).exists();
//      BufferedReader in = null;
//      if (exists) {
//        in = new BufferedReader(new FileReader(credPath + "tokens.txt"));
//      } else {
//        log.error("ERROR: TOKEN FILE MISSING !!!!");
//      }
//
//      if (in == null) {
//        return false;
//      }
//
//      String str;
//      while ((str = in.readLine()) != null) {
//        tokenList.add(str);
//      }
//      in.close();
//
//    } catch (IOException e) {
//      log.error(e.getMessage(), e);
//    }
//
//    if (tokenList.contains(token)) {
//      if (log.isDebugEnabled()) {
//        log.debug("TOKEN FOUND");
//      }
//      found = true;
//      tokenList.remove(token);
//    }
//    try {
//      BufferedWriter out = new BufferedWriter(new FileWriter(credPath + "tokens.txt"));
//      for (String tokenString : tokenList) {
//        out.write(tokenString);
//        out.newLine();
//
//      }
//
//      out.close();
//    } catch (IOException e) {
//      log.error(e.getMessage(), e);
//    }
//
//    return found;
//  }

//  public void createTokenFile(int number, int length) {
//    try {
//      BufferedWriter out = new BufferedWriter(new FileWriter(credPath + "tokens.txt"));
//      for (int i = 0; i < number; i++) {
//        out.write(getToken(length));
//        out.newLine();
//      }
//      out.close();
//    } catch (IOException e) {
//      log.error(e.getMessage(), e);
//    }
//  }

//  private String getToken(int length) {
//    Random random = new Random();
//    int intRand = random.nextInt();
//    Date date = new Date();
//    long time = date.getTime();
//
//    long seed = intRand + time;
//
//
//    try {
//      md5 = MessageDigest.getInstance("MD5");
//    } catch (NoSuchAlgorithmException e) {
//      log.error(e.getMessage(), e);
//    }
//    md5.reset();
//    md5.update(String.valueOf(seed).getBytes());
//    byte[] result = md5.digest();
//
//    /* Ausgabe */
//    StringBuilder hexString = new StringBuilder();
//    for (int i = 0; i < result.length; i++) {
//      hexString.append(Integer.toHexString(0xFF & result[i]));
//    }
//    if (log.isDebugEnabled()) {
//      log.debug("MD5: " + hexString.toString());
//    }
//    this.key = hexString.toString();
//    return key.substring(2, 2 + length);
//  }

//  private static final String DEFAULT_AGENT_KEY                        = "d4ed2b76cfcf9bad374ef96c9c7ab3b";
  //  private SSAuthWiki              wikiauth      = null;