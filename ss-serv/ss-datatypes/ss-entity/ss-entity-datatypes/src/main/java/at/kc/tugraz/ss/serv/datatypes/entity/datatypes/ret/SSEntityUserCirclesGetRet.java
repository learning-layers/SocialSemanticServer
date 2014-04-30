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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServRetI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSEntityUserCirclesGetRet extends SSServRetI{

  public List<SSEntityCircle> circles = new ArrayList<SSEntityCircle>();

  public static SSEntityUserCirclesGetRet get(
    final List<SSEntityCircle> circles, 
    final SSMethU        op){
    
    return new SSEntityUserCirclesGetRet(circles, op);
  }
  
  private SSEntityUserCirclesGetRet(
    final List<SSEntityCircle> circles, 
    final SSMethU        op){

    super(op);
    
    if(circles != null){
      this.circles.addAll(circles);
    }
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld           = new HashMap<String, Object>();
    final Map<String, Object> circlesObj   = new HashMap<String, Object>();
    
    circlesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSEntityCircle.class.getName());
    circlesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.circles, circlesObj);
    
    return ld;
  }
  
  public List<SSEntityCircle> getCircles() {
    return circles;
  }
}
