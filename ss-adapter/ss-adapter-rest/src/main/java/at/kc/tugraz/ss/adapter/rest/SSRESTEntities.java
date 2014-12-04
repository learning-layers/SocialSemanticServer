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
package at.kc.tugraz.ss.adapter.rest;

import at.kc.tugraz.socialserver.utils.SSEncodingU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserGetNewPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserGetNewRet;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import java.net.URLDecoder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/entities")
@Api( value = "entities", description = "SSS REST API for entities" )
public class SSRESTEntities {

  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{entity}")
  @ApiOperation(
    value = "retrieve entitiy information for given ID or encoded URI",
    response = SSEntityUserGetNewRet.class)
  public Response entityUserGetNew(
    @Context HttpHeaders headers,
    @PathParam (SSVarU.entity) String entity){
    
    try{
      
      String decodedURI;
      
      try{
        decodedURI = URLDecoder.decode(entity, SSEncodingU.utf8);
      }catch(Exception error){
        decodedURI = entity;
      }
      
      if(SSUri.isURI(decodedURI)){
        
        return SSRestMain.handleGETRequest(
          headers,
          new SSEntityUserGetNewPar(
            SSMethU.entityUserGetNew,
            null,
            decodedURI));
        
      }else{
        
        return SSRestMain.handleGETRequest(
          headers,
          new SSEntityUserGetNewPar(
            SSMethU.entityUserGetNew,
            null,
            SSRestMain.conf.vocConf.uriPrefix + decodedURI));
      }
    }catch(Exception error){
      return Response.status(422).build();
    }
  }
  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @ApiOperation(
//    value = "create a circle and add users and entities to",
//    response = SSEntityUserCircleCreateRet.class)
//  public Response entityCircleCreate(
//    @Context HttpHeaders              headers,
//    final SSEntityUserCircleCreatePar input){
//    
//    return SSRestMain.handlePOSTRequest(headers, input, SSMethU.entityCircleCreate);
//  }
}
