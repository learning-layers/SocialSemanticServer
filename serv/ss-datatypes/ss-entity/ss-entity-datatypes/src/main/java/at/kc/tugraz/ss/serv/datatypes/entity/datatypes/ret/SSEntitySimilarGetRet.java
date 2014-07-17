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
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntitySimilar;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSEntitySimilarGetRet extends SSServRetI{

  public List<SSEntitySimilar> entities = new ArrayList<>();

  public static SSEntitySimilarGetRet get(
    final List<SSEntitySimilar> entities, 
    final SSMethU               op){
    
    return new SSEntitySimilarGetRet(entities, op);
  }
  
  private SSEntitySimilarGetRet(
    final List<SSEntitySimilar> entities, 
    final SSMethU               op){

    super(op);
    
    if(entities != null){
      this.entities.addAll(entities);
    }
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld           = new HashMap<>();
    final Map<String, Object> entitiesObj   = new HashMap<>();
    
    entitiesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSEntitySimilar.class.getName());
    entitiesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.entities, entitiesObj);
    
    return ld;
  }
  
  public List<SSEntitySimilar> getEntities() {
    return entities;
  }
}
