/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.util;

import at.tugraz.sss.servs.entity.datatype.SSErr;
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
  
  public static Map<String, Object> jsonLDContext() throws SSErr{
    return jsonLDContextBasic();
  }

  public static Map<String, Object> jsonLDContext(
    final Map<String, Object> jsonLDDesc) throws SSErr {
    
    final Map<String, Object> jsonLDContext = jsonLDContextBasic();
    
    for(Map.Entry<String, Object> entry : jsonLDDesc.entrySet()){
      jsonLDContext.put(entry.getKey(), entry.getValue());
    }
    
    return jsonLDContext;
  }
  
  private static Map<String, Object> jsonLDContextBasic() throws SSErr{
   
    final Map<String, Object> jsonLDContext = new HashMap<>();
      
    jsonLDContext.put(SSVarNames.sss,      jsonLDUri + SSVarNames.jsonLD + SSStrU.slash);
    jsonLDContext.put(SSVarNames.xsd,      SSLinkU.xsd);
    jsonLDContext.put(SSVarNames.op,       SSVarNames.xsd + SSStrU.colon + SSStrU.valueString);
    jsonLDContext.put(SSVarNames.error,    SSVarNames.xsd + SSStrU.colon + SSStrU.valueBoolean);
    jsonLDContext.put(SSVarNames.errorMsg, SSVarNames.xsd + SSStrU.colon + SSStrU.valueString);
    jsonLDContext.put(SSVarNames.app,      app);
    jsonLDContext.put(SSVarNames.space,    space);

    return jsonLDContext;
  }
}

//@GET
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    ("/{type}")
//  @ApiOperation(
//    value = "retrieve json ld description for given type",
//    response = SSJSONLDRet.class)
//  public Response jsonLDGet(
//    @Context  
//      final HttpHeaders headers,
//    @PathParam(SSVarNames.type) 
//      final String type){
//    
//    final SSJSONLDPar par;
//    
//    try{
//      
//      par =
//        new SSJSONLDPar(
//          null,
//          type);
//      
//    }catch(Exception error){
//      return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
//    }
//    
//    return SSRestMainV2.handleRequest(headers, par, false, true).response;
//  }