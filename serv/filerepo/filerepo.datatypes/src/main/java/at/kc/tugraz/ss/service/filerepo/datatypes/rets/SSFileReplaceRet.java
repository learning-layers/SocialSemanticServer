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
 package at.kc.tugraz.ss.service.filerepo.datatypes.rets;

import at.tugraz.sss.serv.SSMethU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSServRetI;
import at.tugraz.sss.serv.SSUri;
import java.util.HashMap;
import java.util.Map;

public class SSFileReplaceRet extends SSServRetI{
  
  public  SSUri         file;
  public  SSUri         user;

  public SSFileReplaceRet(
    final SSUri            uri, 
    final SSUri            user, 
    final SSMethU          op){
    
    super(op);
    
    this.file           = uri;
    this.user          = user;
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld = new HashMap<>();
    
    ld.put(SSVarU.file,    SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.user,    SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    
    return ld;
  }
  
  /* json getters */
  public String getFile() throws Exception{
    return SSStrU.removeTrailingSlash(file);
  }

  public String getUser() throws Exception{
    return SSStrU.removeTrailingSlash(user);
  }
}
