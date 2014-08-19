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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServRetI;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSEntityDescsGetRet extends SSServRetI{

  public List<SSEntityDescA> descs = new ArrayList<>();

  public static SSEntityDescsGetRet get(
    final List<SSEntityDescA> entityDesc, 
    final SSMethU       op){
    
    return new SSEntityDescsGetRet(entityDesc, op);
  }
  
  private SSEntityDescsGetRet(
    final List<SSEntityDescA> descs, 
    final SSMethU       op){
    
    super(op);
    
    if(descs != null){
      this.descs.addAll(descs);
    }
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
        
    final Map<String, Object> ld         = new HashMap<>();
    final Map<String, Object> descsObj   = new HashMap<>();
    
    descsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSEntityDescA.class.getName());
    descsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.descs, descsObj);
    
    return ld;
  }
  
  public List<SSEntityDescA> getDescs() {
    return descs;
  }
}
