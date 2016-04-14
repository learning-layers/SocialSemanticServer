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

package at.tugraz.sss.servs.learnep.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSServRetI; 

public class SSLearnEpLockHoldRet extends SSServRetI{

  public SSUri   learnEp       = null;
  public boolean lockedByUser  = false;
  public boolean locked        = false;
  public Long    remainingTime = null;

  public String getLearnEp(){
    return SSStrU.removeTrailingSlash(learnEp);
  }
  
  public static SSLearnEpLockHoldRet get(
    final SSUri   learnEp,
    final boolean locked,
    final boolean lockedByUser, 
    final Long    remainingTime){

    return new SSLearnEpLockHoldRet(learnEp, locked, lockedByUser, remainingTime);
  }
  
  private SSLearnEpLockHoldRet(
    final SSUri   learnEp,
    final boolean locked,
    final boolean lockedByUser,
    final Long    remainingTime){
    
    super(SSVarNames.learnEpsLockHold);
    
    this.learnEp       = learnEp;
    this.locked        = locked;
    this.lockedByUser  = lockedByUser;
    this.remainingTime = remainingTime;
  }
}
