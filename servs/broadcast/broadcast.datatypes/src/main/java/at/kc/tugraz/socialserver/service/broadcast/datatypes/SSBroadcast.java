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
package at.kc.tugraz.socialserver.service.broadcast.datatypes;

import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.enums.SSBroadcastEnum;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSJSONLDPropI;
import java.util.HashMap;
import java.util.Map;

public class SSBroadcast implements SSJSONLDPropI{

  public SSUri             entity    = null;
  public SSBroadcastEnum   type      = null;
  public SSUri             user      = null;
  public Object            content   = null;
  public Long              timestamp = -1L;

  public static SSBroadcast get(
    final SSBroadcastEnum    type, 
    final SSUri              entity, 
    final SSUri              user,
    final Object             content) {
    
    return new SSBroadcast(type, entity, user, content);
  }

  private SSBroadcast(
    final SSBroadcastEnum    type, 
    final SSUri              entity, 
    final SSUri              user,
    final Object             content){
    
    this.type      = type;
    this.entity    = entity;
    this.user      = user;
    this.timestamp = System.currentTimeMillis();
    this.content   = content;
  }
  
  public SSBroadcast(){}
  
  @Override
  public Object jsonLDDesc() {
    
    Map<String, Object> ld = new HashMap<>();
    
    ld.put(SSVarNames.entity,      SSVarNames.sss  + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarNames.type,        SSVarNames.sss  + SSStrU.colon + SSBroadcastEnum.class.getName());
    ld.put(SSVarNames.user,        SSVarNames.sss  + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarNames.timestamp,   SSVarNames.xsd  + SSStrU.colon + SSStrU.valueLong);
    ld.put(SSVarNames.content,     SSVarNames.xsd  + SSStrU.colon + SSStrU.valueBoolean);
    
    return ld;
  }
}
