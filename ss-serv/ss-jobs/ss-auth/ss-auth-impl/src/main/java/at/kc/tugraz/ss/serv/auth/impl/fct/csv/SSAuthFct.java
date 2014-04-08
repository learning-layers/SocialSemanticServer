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
package at.kc.tugraz.ss.serv.auth.impl.fct.csv;

import at.kc.tugraz.socialserver.utils.SSEncodingU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

public class SSAuthFct{
  
  public static String checkPasswordAndGetUserKey(
    final Map<String, String> passwordPerUser, 
    final Map<String, String> keyPerUser, 
    final String              userName,
    final String              password) throws Exception{
    
    try{
      
      if(
        !SSStrU.equals(password, passwordPerUser.get(userName)) ||
        keyPerUser.get(userName) == null){
        throw new Exception("user not registered");
      }
      
      return keyPerUser.get(userName);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static String generateKey(final String toDigest) throws Exception{

    try{
      if(SSStrU.isEmpty(toDigest)){
        throw new Exception("to digest string not valid");
      }

      final MessageDigest digest = MessageDigest.getInstance(SSEncodingU.md5);

      digest.update(toDigest.getBytes());

      return digest.digest().toString();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public static void checkKey(
    final List<String> keys, 
    final String       key) throws Exception {
    
    if(SSStrU.containsNot(keys, key)){
      SSServErrReg.regErrThrow(new Exception("Login key is wrong."));
    }
  }
}