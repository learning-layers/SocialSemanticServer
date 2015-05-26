/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
import at.tugraz.sss.serv.SSEntityCircle;
import java.util.HashMap;
import java.util.Map;

public class SSCircleGetRet extends SSServRetI{

  public SSEntityCircle circle = null;

  public SSEntityCircle getCircle() {
    return circle;
  }
  
  public static SSCircleGetRet get(
    final SSEntityCircle circle){
    
    return new SSCircleGetRet(circle);
  }
  
  private SSCircleGetRet(
    final SSEntityCircle circle){

    super(SSServOpE.circleGet);
    
    this.circle = circle;
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld  = new HashMap<>();
    
    ld.put(SSVarNames.circle, SSVarNames.sss + SSStrU.colon + SSEntityCircle.class.getName());
    
    return ld;
  }
}
