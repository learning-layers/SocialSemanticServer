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
package at.kc.tugraz.ss.serv.ss.auth.datatypes.ret;

import at.tugraz.sss.serv.SSLinkU;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServRetI;
import java.util.HashMap;
import java.util.Map;

public class SSAuthCheckCredRet extends SSServRetI{

  public SSUri  user = null;
  public String key  = null;
  
  public String getUser() {
    return SSStrU.removeTrailingSlash(user);
  } 
  
  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld = new HashMap<>();
    
    ld.put(SSVarNames.key,  SSLinkU.xsd     + SSStrU.valueString);
    ld.put(SSVarNames.user, SSVarNames.sss  + SSStrU.colon + SSUri.class.getName());
    
    return ld;
  }
  
  public static SSAuthCheckCredRet get(
    final String  key, 
    final SSUri   uri,
    final SSServOpE op){
    return new SSAuthCheckCredRet(key, uri, op);
  }

  private SSAuthCheckCredRet(
    final String  key,
    final SSUri   uri,
    final SSServOpE op){
    
    super(op);
    
    this.key = key;
    this.user = uri;
  }
}