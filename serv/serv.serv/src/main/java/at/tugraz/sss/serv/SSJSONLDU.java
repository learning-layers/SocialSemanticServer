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
package at.tugraz.sss.serv;

import at.tugraz.sss.serv.SSLinkU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import java.util.HashMap;
import java.util.Map;

public class SSJSONLDU {

  public static final String id        = "@id";
  public static final String type      = "@type";
  public static final String context   = "@context";
  public static final String container = "@container";
  public static final String set       = "@set";

  private static String jsonLDUri; 
  private static final String  app   = "ll";
  private static final String  space = "sss";
  
  public static void init(
    final String jsonLDServUri){
    
    jsonLDUri = jsonLDServUri;
//    app       = application;
//    space     = appSpace;
  }
  
  public static Map<String, Object> jsonLDContext() throws Exception{
    return jsonLDContextBasic();
  }

  public static Map<String, Object> jsonLDContext(
    final Map<String, Object> jsonLDDesc) throws Exception {
    
    final Map<String, Object> jsonLDContext = jsonLDContextBasic();
    
    for(Map.Entry<String, Object> entry : jsonLDDesc.entrySet()){
      jsonLDContext.put(entry.getKey(), entry.getValue());
    }
    
    return jsonLDContext;
  }
  
  private static Map<String, Object> jsonLDContextBasic() throws Exception{
   
    final Map<String, Object> jsonLDContext = new HashMap<>();
      
    jsonLDContext.put(SSVarU.sss,      jsonLDUri + SSMethU.jsonLD.toString() + SSStrU.slash);
    jsonLDContext.put(SSVarU.xsd,      SSLinkU.xsd);
    jsonLDContext.put(SSVarU.op,       SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    jsonLDContext.put(SSVarU.error,    SSVarU.xsd + SSStrU.colon + SSStrU.valueBoolean);
    jsonLDContext.put(SSVarU.errorMsg, SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    jsonLDContext.put(SSVarU.app,      app);
    jsonLDContext.put(SSVarU.space,    space);

    return jsonLDContext;
  }
}