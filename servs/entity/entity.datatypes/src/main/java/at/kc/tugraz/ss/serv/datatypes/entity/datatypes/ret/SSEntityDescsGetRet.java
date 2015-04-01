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

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSEntityA;
import at.tugraz.sss.serv.SSServRetI;
import at.tugraz.sss.serv.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSEntityDescsGetRet extends SSServRetI{

  public List<SSEntityA> descs = new ArrayList<>();

  public static SSEntityDescsGetRet get(
    final List<SSEntityA> descs, 
    final SSServOpE         op){
    
    return new SSEntityDescsGetRet(descs, op);
  }
  
  private SSEntityDescsGetRet(
    final List<SSEntityA> descs, 
    final SSServOpE         op){
    
    super(op);
    
    if(descs != null){
      this.descs.addAll(descs);
    }
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
        
    final Map<String, Object> ld         = new HashMap<>();
    final Map<String, Object> descsObj   = new HashMap<>();
    
    descsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSEntityA.class.getName());
    descsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.descs, descsObj);
    
    return ld;
  }
}