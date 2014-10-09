/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.kc.tugraz.ss.adapter.rest;

import at.kc.tugraz.socialserver.utils.SSStrU;
import com.google.common.base.Charsets;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import org.apache.commons.io.IOUtils;

@Provider
public class SSAuthFilter implements ContainerRequestFilter{
  
  @Override
  public void filter(ContainerRequestContext request) throws IOException {

    final MultivaluedMap<String, String> headers             = request.getHeaders();
    final List<String>                   authorizationHeader = headers.get("authorization");
    
    if(
      authorizationHeader         != null &&
      authorizationHeader.get(0)  != null &&
      authorizationHeader.get(0).contains("Bearer ")){
      
      String json = 
        IOUtils.toString(
          request.getEntityStream(), 
          Charsets.UTF_8.toString());

      json = json.substring(0, json.length() - 1) + ",\"bearer\":\"" + SSStrU.replaceAll(authorizationHeader.get(0), "Bearer ", SSStrU.empty) + "\"}";
      
      request.setEntityStream(IOUtils.toInputStream(json));
    }
    
    
    
//    //We do allow wadl to be retrieve
//    if(method.equals("GET") && (path.equals("application.wadl") || path.equals("application.wadl/xsd0.xsd")){
//      return containerRequest;
//    }
//    
//    //Get the authentification passed in HTTP headers parameters
//    String auth = containerRequest.getHeaderValue("authorization");
//    
//    //If the user does not have the right (does not provide any HTTP Basic Auth)
//    if(auth == null){
//      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
//    }
//    
//    return containerRequest;
  }
}