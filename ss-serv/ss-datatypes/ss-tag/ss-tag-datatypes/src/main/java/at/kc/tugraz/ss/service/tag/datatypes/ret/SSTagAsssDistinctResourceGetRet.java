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

package at.kc.tugraz.ss.service.tag.datatypes.ret;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServRetI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSTagAsssDistinctResourceGetRet extends SSServRetI{
  
  public List<SSTag> tags = new ArrayList<SSTag>();
  
  public static SSTagAsssDistinctResourceGetRet get(List<SSTag> tags, SSMethU op){
    return new SSTagAsssDistinctResourceGetRet(tags, op);
  }
  
  private SSTagAsssDistinctResourceGetRet(List<SSTag> tags, SSMethU op){
    
    super(op);
    
    if(tags != null){
      this.tags = tags;
    }
  }
  
  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld         = new HashMap<>();
    Map<String, Object> tagsObj    = new HashMap<>();
    
    tagsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSTag.class.getName());
    tagsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.tags, tagsObj);
    
    return ld;
  }
  
  /*************** getters to allow for json enconding ********************/
  public List<SSTag> getTags() {
    return tags;
  }
}
