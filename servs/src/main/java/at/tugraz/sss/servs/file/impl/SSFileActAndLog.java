/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

package at.tugraz.sss.servs.file.impl;

import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.activity.api.SSActivityServerI;
import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.activity.datatype.SSActivityAddPar;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import java.util.List;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.activity.datatype.*;
import at.tugraz.sss.servs.activity.impl.*;

public class SSFileActAndLog{
  
  public static void shareFileWithUser(
    final SSServPar   servPar,
    final SSUri       user,
    final SSUri       entity,
    final List<SSUri> usersToShareWith) throws SSErr{
    
    try{
      
      final SSActivityServerI activityServ = new SSActivityImpl();
      
      activityServ.activityAdd(
        new SSActivityAddPar(
          servPar,
          user, 
          SSActivityE.shareFileWithUsers, 
          entity, 
          SSUri.asListNotNull(usersToShareWith), 
          SSUri.asListNotNull(), 
          SSTextComment.asListWithoutNullAndEmpty(), 
          null, 
          false));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}