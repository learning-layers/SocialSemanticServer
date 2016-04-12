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
package at.tugraz.sss.servs.friend.impl;

import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.activity.api.SSActivityServerI;
import at.tugraz.sss.servs.activity.datatype.SSActivityE;
import at.tugraz.sss.servs.activity.datatype.SSActivityAddPar;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.friend.datatype.SSFriendAddPar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.activity.impl.*;

public class SSFriendActAndLog{
  
  public static void addFriend(
    final SSServPar      servPar,
    final SSFriendAddPar par) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ = new SSActivityImpl();
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          par.user,
          SSActivityE.friendAdd,
          par.user,
          SSUri.asListNotNull(par.friend),
          SSUri.asListNotNull(),
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          false));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
