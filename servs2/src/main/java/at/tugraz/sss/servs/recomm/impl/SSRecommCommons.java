/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.servs.recomm.impl;

import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.user.api.*;
import at.tugraz.sss.servs.user.datatype.*;
import at.tugraz.sss.servs.user.impl.*;
import java.util.*;

public class SSRecommCommons {
  
  public SSUser getUser(
    final SSServPar servPar, 
    final SSUri     user) throws SSErr{
    
    try{
      
      final SSUserServerI  userServ = new SSUserImpl();
      final List<SSEntity> users    =
        userServ.usersGet(
          new SSUsersGetPar(
            servPar,
            user,
            SSUri.asListNotNull(user), //users
            null, //emals
            false)); //invokeEntityHandlers
      
      if(users.isEmpty()){
        SSServErrReg.regErrThrow(SSErrE.userNotRegistered);
        return null;
      }
      
      return (SSUser) users.get(0);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}