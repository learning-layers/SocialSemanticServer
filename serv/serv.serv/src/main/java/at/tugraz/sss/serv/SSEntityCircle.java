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
package at.tugraz.sss.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSEntityCircle extends SSEntity{
 
  public SSCircleE                      circleType     = null;
  public List<SSCircleRightE>           accessRights   = new ArrayList<>();
  public Boolean                        isSystemCircle = null;
  
  public static SSEntityCircle get(
    final SSUri                          id,
    final SSLabel                        label, 
    final SSCircleE                      circleType,
    final Boolean                        isSystemCircle) throws Exception{
    
    return new SSEntityCircle(id, label, circleType, isSystemCircle);
  }

  protected SSEntityCircle(
    final SSUri                           id,
    final SSLabel                         label,
    final SSCircleE                       circleType, 
    final Boolean                         isSystemCircle) throws Exception{
    
    super(id, SSEntityE.circle, label);
    
    this.circleType     = circleType;
    this.isSystemCircle = isSystemCircle;
  }

  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld              = (Map<String, Object>) super.jsonLDDesc();
    final Map<String, Object> accessRightsObj = new HashMap<>();
    
    ld.put(SSVarU.circleType,      SSVarU.sss + SSStrU.colon + SSCircleE.class.getName());
    ld.put(SSVarU.isSystemCircle,  SSVarU.xsd + SSStrU.colon + SSStrU.valueBoolean);
    
    accessRightsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSCircleRightE.class.getName());
    accessRightsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.accessRights, accessRightsObj);
    
    return ld;
  }

  /* json getters  */
  public String getCircleType(){
    return SSStrU.toStr(circleType);
  }

  public List<String> getAccessRights() throws Exception{
    return SSStrU.toStr(accessRights);
  }
}
