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

import at.tugraz.sss.serv.SSMethU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSServRetI;
import at.tugraz.sss.serv.SSEntityCircle;
import java.util.HashMap;
import java.util.Map;

public class SSCircleGetRet extends SSServRetI{

  public SSEntityCircle circle = null;

  public static SSCircleGetRet get(
    final SSEntityCircle circle, 
    final SSMethU        op){
    
    return new SSCircleGetRet(circle, op);
  }
  
  private SSCircleGetRet(
    final SSEntityCircle circle, 
    final SSMethU        op){

    super(op);
    
    this.circle = circle;
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld  = new HashMap<>();
    
    ld.put(SSVarU.circle, SSVarU.sss + SSStrU.colon + SSEntityCircle.class.getName());
    
    return ld;
  }
  
  public SSEntityCircle getCircle() {
    return circle;
  }
}
