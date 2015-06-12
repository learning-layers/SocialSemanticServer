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
import at.tugraz.sss.serv.SSJSONLDU;
import at.tugraz.sss.serv.SSUri;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCirclesGetRet extends SSServRetI{

  public List<SSEntityCircle> circles = new ArrayList<>();

   public List<SSEntityCircle> getCircles() {
    return circles;
  }
   
  public static SSCirclesGetRet get(
    final List<SSEntityCircle> circles){
    
    return new SSCirclesGetRet(circles);
  }
  
  private SSCirclesGetRet(
    final List<SSEntityCircle> circles){

    super(SSServOpE.circlesGet);
    
    SSEntityCircle.addDistinctWithoutNull(this.circles, circles);
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld           = new HashMap<>();
    final Map<String, Object> circlesObj   = new HashMap<>();
    
    circlesObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSEntityCircle.class.getName());
    circlesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.circles, circlesObj);
    
    return ld;
  }
}
