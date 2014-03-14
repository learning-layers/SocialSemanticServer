/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

package at.kc.tugraz.ss.service.disc.datatypes.ret;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServRetI;
import at.kc.tugraz.ss.service.disc.datatypes.SSDisc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSDiscsUserAllGetRet extends SSServRetI{

  public List<SSDisc> discs = new ArrayList<SSDisc>();

  public static SSDiscsUserAllGetRet get(List<SSDisc> discs, SSMethU op){
    return new SSDiscsUserAllGetRet(discs, op);
  }
  
  private SSDiscsUserAllGetRet(List<SSDisc> discs, SSMethU op){
    
    super(op);
    
    this.discs.addAll(discs);
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    Map<String, Object> ld         = new HashMap<String, Object>();
    Map<String, Object> discsObj   = new HashMap<String, Object>();
    
    discsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSDisc.class.getName());
    discsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.discs, discsObj);
    
    return ld;
  }
  
  public List<SSDisc> getDiscs() {
    return discs;
  }
}
