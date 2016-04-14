/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.common.impl;

import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.user.datatype.SSUsersGetPar;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.auth.datatype.SSAuthCheckKeyPar;
import at.tugraz.sss.servs.auth.impl.*;
import at.tugraz.sss.servs.user.impl.*;
import java.util.List;

public class SSUserCommons {
  
  public void checkKeyAndSetUser(final SSServPar servPar) throws SSErr{
    
    try{
      final SSUri user = new SSAuthImpl().authCheckKey(new SSAuthCheckKeyPar(servPar, servPar.key));
      
      if(user != null){
        servPar.user = user;
      }
      
      if(
        user      == null &&
        servPar.user == null){
        throw SSErr.get(SSErrE.authNoUserForKey);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
    
  public boolean areUsersUsers(
    final SSServPar   servPar,
    final List<SSUri> userURIs) throws SSErr{
    
    try{
      
      if(
        userURIs == null ||
        userURIs.isEmpty()){
        return true;
      }
      
      final List<SSEntity> users =
        new SSUserImpl().usersGet(
          new SSUsersGetPar(
            servPar,
            SSConf.systemUserUri, 
            userURIs,  //userURIs
            null, //emails
            false)); //invokeEntityHandlers
      
      return users.size() == userURIs.size();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
}