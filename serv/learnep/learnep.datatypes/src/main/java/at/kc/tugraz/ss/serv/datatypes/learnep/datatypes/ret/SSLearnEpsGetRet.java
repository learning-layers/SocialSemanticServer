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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret;

import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.tugraz.sss.serv.SSMethU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSJSONLDU;
import at.tugraz.sss.serv.SSServRetI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSLearnEpsGetRet extends SSServRetI{

  public List<SSLearnEp> learnEps = null;

  public static SSLearnEpsGetRet get(List<SSLearnEp> learnEp, SSMethU op){
    return new SSLearnEpsGetRet(learnEp, op);
  }
  
  private SSLearnEpsGetRet(List<SSLearnEp> learnEps, SSMethU op){
    
    super(op);
    
    this.learnEps = learnEps;
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld           = new HashMap<>();
    Map<String, Object> learnEpsObj  = new HashMap<>();
    
    learnEpsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSLearnEp.class.getName());
    learnEpsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.learnEps, learnEpsObj);
    
    return ld;
  }
  
  public List<SSLearnEp> getLearnEp() {
    return learnEps;
  }
}
