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
package at.kc.tugraz.sss.flag.datatypes.ret;


import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSServRetI; import at.tugraz.sss.serv.SSVarNames;
import java.util.HashMap;
import java.util.Map;

public class SSFlagsSetRet extends SSServRetI{
  
  public Boolean worked = null;
  
  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld         = new HashMap<>();
    
    ld.put(SSVarNames.worked, SSVarNames.xsd + SSStrU.colon + SSStrU.valueBoolean);
    
    return ld;
  }
  
  public static SSFlagsSetRet get(
    final Boolean  worked){
    
    return new SSFlagsSetRet(worked);
  }
  
  private SSFlagsSetRet(
    final Boolean  worked) {
    
    super(SSVarNames.flagsSet);
    
    this.worked = worked;
  }
}
