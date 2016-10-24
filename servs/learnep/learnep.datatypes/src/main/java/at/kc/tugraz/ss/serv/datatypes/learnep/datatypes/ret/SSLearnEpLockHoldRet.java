/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServRetI;
import java.util.HashMap;
import java.util.Map;

public class SSLearnEpLockHoldRet extends SSServRetI{

  public SSUri   learnEp       = null;
  public Boolean lockedByUser  = null;
  public Boolean locked        = false;
  public Long    remainingTime = null;

  public String getLearnEp(){
    return SSStrU.removeTrailingSlash(learnEp);
  }
  
  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld = new HashMap<>();
    
    ld.put(SSVarNames.learnEp,       SSVarNames.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarNames.locked,        SSVarNames.xsd + SSStrU.colon + SSStrU.valueBoolean);
    ld.put(SSVarNames.lockedByUser,  SSVarNames.xsd + SSStrU.colon + SSStrU.valueBoolean);
    ld.put(SSVarNames.remainingTime, SSVarNames.xsd + SSStrU.colon + SSStrU.valueLong);
    
    return ld;
  }
  
  public static SSLearnEpLockHoldRet get(
    final SSUri   learnEp,
    final Boolean locked,
    final Boolean lockedByUser, 
    final Long    remainingTime){

    return new SSLearnEpLockHoldRet(learnEp, locked, lockedByUser, remainingTime);
  }
  
  private SSLearnEpLockHoldRet(
    final SSUri   learnEp,
    final Boolean locked,
    final Boolean lockedByUser,
    final Long    remainingTime){
    
    super(SSServOpE.learnEpsLockHold);
    
    this.learnEp       = learnEp;
    this.locked        = locked;
    this.lockedByUser  = lockedByUser;
    this.remainingTime = remainingTime;
  }
}