 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

package at.tugraz.sss.servs.message.impl;

import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.activity.api.SSActivityServerI;
import at.tugraz.sss.servs.activity.datatype.SSActivityContent;
import at.tugraz.sss.servs.activity.datatype.SSActivityContentE;
import at.tugraz.sss.servs.activity.datatype.SSActivityE;
import at.tugraz.sss.servs.activity.datatype.SSActivityAddPar;
import at.tugraz.sss.servs.activity.datatype.SSActivityContentAddPar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.activity.impl.*;

public class SSMessageActAndLog{
  
  public void messageSend(
    final SSServPar servPar,
    final SSUri           user,
    final SSUri           forUser,
    final SSUri           message,
    final SSTextComment   content,
    final boolean         shouldCommit) throws SSErr{
    
    try{
      
      final SSActivityServerI actServ = new SSActivityImpl();
        
      final SSUri activity =
        actServ.activityAdd(
          new SSActivityAddPar(
            servPar,
            user,
            SSActivityE.messageSend,
            message,
            SSUri.asListNotNull(forUser),
            null,
            null,
            null,
            shouldCommit));
        
      actServ.activityContentAdd(
        new SSActivityContentAddPar(
          servPar,
          user, 
          activity, 
          SSActivityContentE.text, 
          SSActivityContent.get(content),
          shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}