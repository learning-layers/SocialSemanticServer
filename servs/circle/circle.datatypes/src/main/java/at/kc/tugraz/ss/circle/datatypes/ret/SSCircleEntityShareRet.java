/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.circle.datatypes.ret;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSServRetI;
import at.tugraz.sss.serv.SSUri;
import java.util.HashMap;
import java.util.Map;

public class SSCircleEntityShareRet extends SSServRetI{
  
  public SSUri entity = null;

  public static SSCircleEntityShareRet get(
    final SSUri   entity, 
    final SSServOpE op){
    
    return new SSCircleEntityShareRet(entity, op);
  }
  
  private SSCircleEntityShareRet(
    final SSUri   entity,
    final SSServOpE op){
    
    super(op);
    
    this.entity = entity;
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld         = new HashMap<>();
    
    ld.put(SSVarNames.entity, SSVarNames.sss + SSStrU.colon + SSUri.class.getName());
    
    return ld;
  }
  
  public String getEntity() throws Exception {
    return SSStrU.removeTrailingSlash(entity);
  }
}
