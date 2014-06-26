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
package at.kc.tugraz.ss.serv.datatypes.location.datatypes.ret;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.ss.serv.datatypes.SSServRetI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSLocationsGetRet extends SSServRetI{

  public List<String> locations = new ArrayList<>();

  public static SSLocationsGetRet get(List<String> locations, SSMethU op){
    return new SSLocationsGetRet(locations, op);
  }
  
  private SSLocationsGetRet(List<String> locations, SSMethU op){
    
    super(op);
    
    if(locations != null){
      this.locations.addAll(locations);
    }
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld            = new HashMap<>();
    Map<String, Object> locationsObj  = new HashMap<>();
    
    locationsObj.put(SSJSONLDU.id,        SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    locationsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.locations, locationsObj);
    
    return ld;
  }
  
  public List<String> getLocations() {
    return locations;
  }
}
