/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.auth.api.SSAuthClientI;
import at.kc.tugraz.ss.serv.auth.api.SSAuthServerI;
import at.kc.tugraz.ss.serv.auth.conf.SSAuthConf;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.enums.SSAuthEnum;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.ret.SSAuthCheckCredRet;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SSAuthImpl extends SSServImplMiscA implements SSAuthClientI, SSAuthServerI{
  
  //  private static final String DEFAULT_AGENT_KEY                        = "d4ed2b76cfcf9bad374ef96c9c7ab3b";
  //  private SSAuthWiki              wikiauth      = null;
  private final List<String>      defaultKeys;
//  private final Map<String, List<SSAuthEntity>>   keyMappings = new HashMap<String, List<SSAuthEntity>>();

  public SSAuthImpl(final SSAuthConf conf) throws Exception {
    
    super(conf);
    
    defaultKeys = new ArrayList<String>();
    
    defaultKeys.add("FischersFritzFischtFrischeFische");
    defaultKeys.add("681V454J1P3H4W3B367BB79615U184N22356I3E");
    defaultKeys.add("d4ed2b76cfcf9bad374ef96c9c7ab3b");
    
//    wikiauth  = new SSAuthWiki();
  }
  
  /****** SSServRegisterableImplI ******/
  
  @Override
  public List<SSMethU> publishClientOps() throws Exception{
    
    List<SSMethU> clientOps = new ArrayList<SSMethU>();
      
    Method[] methods = SSAuthClientI.class.getMethods();
    
    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }
    
    return clientOps;
  }
  
  @Override
  public List<SSMethU> publishServerOps() throws Exception{
    
    List<SSMethU> serverOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSAuthServerI.class.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }
    
    return serverOps;
  }
  
  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSAuthClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }
  
  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSAuthServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }
  
  /****** SSAuthServClientI ******/
  
  @Override
  public void authCheckCred(SSSocketCon sSCon, SSServPar par) throws Exception {
    sSCon.writeRetFullToClient(SSAuthCheckCredRet.get(authCheckCred(par), par.op));
  }

  /****** SSAuthServServerI ******/
  
  @Override
  public String authCheckCred(SSServPar parI) throws Exception {
    
    switch(SSAuthEnum.get(((SSAuthConf)conf).authType)){
      
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
        
      case noAuth:{
        return defaultKeys.get(1);
      }
    }
    
    return null;
  }

  @Override
  public void authCheckKey(SSServPar parA) throws Exception {
    
    if(SSStrU.containsNot(defaultKeys, parA.key)){
      SSServErrReg.regErrThrow(new Exception("Login key is wrong."));
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
}



//private static String key;
//private static Date keyDate;
//private static Map<String, String> passwords = new HashMap<String, String>();
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
//    List<String> tokenList = new ArrayList<String>();
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