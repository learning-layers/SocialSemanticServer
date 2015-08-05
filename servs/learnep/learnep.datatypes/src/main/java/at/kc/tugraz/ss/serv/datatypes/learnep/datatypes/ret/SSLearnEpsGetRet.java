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
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSJSONLDU;
import at.tugraz.sss.serv.SSServRetI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSLearnEpsGetRet extends SSServRetI{

  public List<SSEntity> learnEps = new ArrayList<>();

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld           = new HashMap<>();
    Map<String, Object> learnEpsObj  = new HashMap<>();
    
    learnEpsObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSLearnEp.class.getName());
    learnEpsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.learnEps, learnEpsObj);
    
    return ld;
  }
  
  public static SSLearnEpsGetRet get(final List<SSEntity> learnEp){
    return new SSLearnEpsGetRet(learnEp);
  }
  
  private SSLearnEpsGetRet(
    final List<SSEntity> learnEps){
    
    super(SSServOpE.learnEpsGet);
    
    SSEntity.addEntitiesDistinctWithoutNull(this.learnEps, learnEps);
  }
}