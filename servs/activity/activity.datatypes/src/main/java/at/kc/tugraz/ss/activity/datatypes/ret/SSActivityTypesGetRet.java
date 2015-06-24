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
package at.kc.tugraz.ss.activity.datatypes.ret;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.tugraz.sss.serv.SSServRetI;
import at.tugraz.sss.serv.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSActivityTypesGetRet extends SSServRetI{
 
  public List<SSActivityE> types = new ArrayList<>();

  public List<String> getTypes() throws Exception {
    return SSStrU.toStr(types);
  }
  
  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld                 = new HashMap<>();
    final Map<String, Object> activityTpyesObj   = new HashMap<>();
    
    activityTpyesObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSActivityE.class.getName());
    activityTpyesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.types, activityTpyesObj);
    
    return ld;
  }
  
  public static SSActivityTypesGetRet get(
    final List<SSActivityE> types, 
    final SSServOpE           op){
    
    return new SSActivityTypesGetRet(types, op);
  }
  
  private SSActivityTypesGetRet(
    final List<SSActivityE> types, 
    final SSServOpE           op){

    super(op);
    
    if(types != null){
      this.types.addAll(types);
    }
  }
}