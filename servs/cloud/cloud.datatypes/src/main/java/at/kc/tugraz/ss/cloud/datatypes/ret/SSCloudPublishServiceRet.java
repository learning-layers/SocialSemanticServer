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
package at.kc.tugraz.ss.cloud.datatypes.ret;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSServRetI;
import java.util.HashMap;
import java.util.Map;

public class SSCloudPublishServiceRet extends SSServRetI{

  public final Boolean useDifferentServiceNode = true;
  public       String  sssNodeHost             = null;
  public       Integer sssNodePort             = null;

  public static SSCloudPublishServiceRet get(
    final String  sssNodeHost,
    final Integer sssNodePort, 
    final SSServOpE op){
    
    return new SSCloudPublishServiceRet(sssNodeHost, sssNodePort, op);
  }
  
  private SSCloudPublishServiceRet(
    final String  sssNodeHost,
    final Integer sssNodePort,
    final SSServOpE op){

    super(op);
    
    this.sssNodeHost = sssNodeHost;
    this.sssNodePort = sssNodePort;
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld            = new HashMap<>();
    
    ld.put(SSVarNames.sssNodeHost, SSVarNames.xsd + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarNames.sssNodePort, SSVarNames.xsd + SSStrU.colon + SSStrU.valueInteger);
    
    return ld;
  }
  
  /* json getters */
  public Boolean getUseDifferentServiceNode(){
    return useDifferentServiceNode;
  }
  
  public String getSssNodeHost(){
    return sssNodeHost;
  }

  public int getSssNodePort(){
    return sssNodePort;
  }
}