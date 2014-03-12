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
package at.kc.tugraz.socialserver.service.broadcast.datatypes.rets;

import at.kc.tugraz.socialserver.utils.SSLinkU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServRetI;
import java.util.HashMap;
import java.util.Map;

public class SSBroadcastServerTimeRet extends SSServRetI{

  public Long serverTime = -1L;

  public static SSBroadcastServerTimeRet get(Long serverTime, SSMethU op) {
    return new SSBroadcastServerTimeRet(serverTime, op);
  }

  private SSBroadcastServerTimeRet(Long  serverTime, SSMethU op) {
    
    super(op);
    
    this.serverTime      = serverTime;
  }
  
  @Override
  public Map<String, Object> jsonLDDesc() {
    
    Map<String, Object> ld = new HashMap<String, Object>();
    
    ld.put(SSVarU.serverTime, SSLinkU.xsd + SSStrU.valueLong);
    
    return ld;
  }
  
  public Long getServerTime() {
    return serverTime;
  }
}
