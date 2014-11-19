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

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.auth.api.SSAuthClientI;
import at.kc.tugraz.ss.serv.auth.api.SSAuthServerI;
import at.kc.tugraz.ss.serv.auth.conf.SSAuthConf;
import at.kc.tugraz.ss.serv.auth.impl.fct.csv.SSAuthMiscFct;
import at.kc.tugraz.ss.serv.auth.impl.fct.oidc.SSAuthOIDC;
import at.kc.tugraz.ss.serv.auth.impl.fct.sql.SSAuthSQLFct;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthCheckCredPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthRegisterUserPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthUsersFromCSVFileAddPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.ret.SSAuthCheckCredRet;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sss.serv.err.datatypes.SSErr;
import sss.serv.err.datatypes.SSErrE;

public class SSAuthImpl extends SSServImplWithDBA implements SSAuthClientI, SSAuthServerI{
  
  private final SSAuthSQLFct    sqlFct;
  private final String          noAuthKey       = "1234";
  private final List<String>    csvFileAuthKeys = new ArrayList<>();
  
  public SSAuthImpl(final SSAuthConf conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception {
    
    super(conf, dbGraph, dbSQL);
    
    this.sqlFct = new SSAuthSQLFct(dbSQL);
    
//    wikiauth  = new SSAuthWiki();
  }
  
  /* SSAuthServClientI */
  
  @Override
  public void authCheckCred(SSSocketCon sSCon, SSServPar par) throws Exception {
    sSCon.writeRetFullToClient(authCheckCred(par));
  }
  
  /* SSAuthServServerI */
  
  //TODO dtheiler: create transactions here as well
  
  @Override
  public void authUsersFromCSVFileAdd(final SSServPar parA) throws Exception {
    
    try{
      final SSAuthUsersFromCSVFileAddPar par                          = new SSAuthUsersFromCSVFileAddPar(parA);
      final Map<String, String>          passwordsForUsersFromCSVFile = new HashMap<>();
      
      try{
        passwordsForUsersFromCSVFile.putAll(SSServCaller.dataImportSSSUsersFromCSVFile(((SSAuthConf)conf).fileName));
      }catch(SSErr error){
        
        switch(error.code){
          case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); return;
          default: SSServErrReg.regErrThrow(error);
        }
      }      
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(Map.Entry<String, String> passwordForUser : passwordsForUsersFromCSVFile.entrySet()){

        SSServCaller.authRegisterUser(
          SSVoc.systemUserUri,
          SSLabel.get(passwordForUser.getKey()),
          passwordForUser.getKey() + SSVoc.systemEmailPostFix,
          passwordForUser.getValue(),
          false,
          false);
      }
      
      dbSQL.commit(par.shouldCommit);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override 
  public SSUri authRegisterUser(final SSServPar parA) throws Exception{
    
    try{
      
      final SSAuthRegisterUserPar par      = new SSAuthRegisterUserPar(parA);
      final SSUri                 userUri;
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(!SSServCaller.userExists(SSVoc.systemUserUri, par.email)){
        
        userUri =
          SSServCaller.userAdd(
            SSVoc.systemUserUri,
            par.label,
            par.email,
            par.isSystemUser,
            false);
      }else{
        
        userUri = SSServCaller.userURIGet(SSVoc.systemUserUri, par.email);
        
        sqlFct.removeKey(userUri);
      }
      
      sqlFct.addKey(
        userUri,
        SSAuthMiscFct.genKey(par.email, par.password));
      
      try{
        SSServCaller.collUserRootAdd (userUri, false);
      }catch(SSErr error){
        
        switch(error.code){
          case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
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
  public SSAuthCheckCredRet authCheckCred(final SSServPar parA) throws Exception {
    
    try{
      final SSAuthCheckCredPar par = new SSAuthCheckCredPar(parA);

      switch(((SSAuthConf)conf).authType){

        case noAuth:{

          final SSUri userUri;
          
          if(!SSServCaller.userExists(SSVoc.systemUserUri, SSStrU.toStr(par.label) + SSVoc.systemEmailPostFix)){
            
            userUri =
              SSServCaller.userAdd(
                SSVoc.systemUserUri,
                par.label,
                SSStrU.toStr(par.label) + SSVoc.systemEmailPostFix,
                false,
                true);
            
            try{
              SSServCaller.collUserRootAdd (userUri, true);
            }catch(SSErr error){
              
              switch(error.code){
                case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
                default: SSServErrReg.regErrThrow(error);
              }
            }
          }else{
            
            userUri = 
              SSServCaller.userURIGet(
                SSVoc.systemUserUri, 
                SSStrU.toStr(par.label) + SSVoc.systemEmailPostFix);
          }
          
          return SSAuthCheckCredRet.get(
            noAuthKey,
            userUri,
            SSMethU.authCheckCred);
        }

        case csvFileAuth:{

          final SSUri userUri;
          
          if(!SSServCaller.userExists(SSVoc.systemUserUri, par.label + SSVoc.systemEmailPostFix)){
            throw new SSErr(SSErrE.userIsNotRegistered);
          }
          
          userUri = SSServCaller.userURIGet(SSVoc.systemUserUri, par.label + SSVoc.systemEmailPostFix);

          return SSAuthCheckCredRet.get(
            SSAuthMiscFct.checkAndGetKey(
              sqlFct,
              userUri,
              par.label + SSVoc.systemEmailPostFix,
              par.password),
            userUri, 
            SSMethU.authCheckCred);
        }
        
        case oidc:{
          final String email = SSAuthOIDC.getOIDCUserEmail(par.key);
          SSUri        userUri;
          
          if(!SSServCaller.userExists(SSVoc.systemUserUri, email)){
            
            userUri =
              SSServCaller.userAdd(
                SSVoc.systemUserUri,
                SSLabel.get(email),
                email,
                false,
                true);
            
            try{
              SSServCaller.collUserRootAdd (userUri, true);
            }catch(SSErr error){
              
              switch(error.code){
                case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
                default: SSServErrReg.regErrThrow(error);
              }
            }
          }else{
            
            userUri = 
              SSServCaller.userURIGet(
                SSVoc.systemUserUri, 
                email);
          }

          return SSAuthCheckCredRet.get(
            par.key,
            userUri,
            SSMethU.authCheckCred);
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
  public void authCheckKey(final SSServPar parA) throws Exception {
    
    try{
      
      switch(((SSAuthConf)conf).authType){
        
        case noAuth: break;
        
        case csvFileAuth:{
          
          if(csvFileAuthKeys != null){
            csvFileAuthKeys.addAll(sqlFct.getKeys());
          }
          
          if(
            noAuthKey.equals(parA.key) ||
            !csvFileAuthKeys.contains(parA.key)){
            throw new SSErr(SSErrE.userKeyWrong);
          }
          
          break;
        }
        
        case oidc:{
          SSServCaller.authCheckCred(SSVoc.systemUserUri, parA.key);
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}


//  @Override
//  public List<String> authKeyList(SSServerPar par) throws Exception {
//    return keylist;
//  }
//
//  @Override
//  public SSAuthEnum authAuthType(SSServerPar par) throws Exception {
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