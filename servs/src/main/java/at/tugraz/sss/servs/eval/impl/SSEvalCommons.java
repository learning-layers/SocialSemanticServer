/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

package at.tugraz.sss.servs.eval.impl;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.user.api.*;
import at.tugraz.sss.servs.user.datatype.*;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.user.impl.*;
import java.util.*;

public class SSEvalCommons {
  
  private final Map<String, String> userSubs = new HashMap<>();
  
  public String getOIDCSubForEmail(
    final SSServPar servPar, 
    final Object    email) throws SSErr{
    
    try{
      
      if(userSubs.containsKey(SSStrU.toStr(email))){
        return userSubs.get(SSStrU.toStr(email));
      }
      
      final SSUserServerI userServ = new SSUserImpl();
      final List<String>  emails   = new ArrayList<>();
      final List<String>  subs     = new ArrayList<>();
      
      emails.add(SSStrU.toStr(email));
      
      final List<SSEntity> users = 
        userServ.usersGet(
          new SSUsersGetPar(
            servPar,
            SSConf.systemUserUri,  //user
            null, //users
            emails, //emails
            false)); //invokeEntityHandlers);
      
      for(SSEntity user : users){
        subs.add(((SSUser) user).oidcSub);
      }
      
      if(SSStrU.isEmpty(subs.get(0))){
        
        userSubs.put(SSStrU.toStr(email), SSStrU.toStr(email));

        return SSStrU.toStr(email);
      }
      
      userSubs.put(SSStrU.toStr(email), subs.get(0));
      
      return subs.get(0);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<String> getOIDCSubForEmail(
    final SSServPar              servPar, 
    final List<? extends Object> emails) throws SSErr{
    
    try{
      
      final List<String> subs = new ArrayList<>();
      
      for(String email : SSStrU.toStr(emails)){
        subs.add(getOIDCSubForEmail(servPar, email));
      }
      
      return subs;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
