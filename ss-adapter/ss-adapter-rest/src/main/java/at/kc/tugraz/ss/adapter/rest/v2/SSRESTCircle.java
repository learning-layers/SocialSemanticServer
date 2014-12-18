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
package at.kc.tugraz.ss.adapter.rest.v2;

import at.kc.tugraz.socialserver.utils.SSEncodingU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.adapter.rest.SSRestMain;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCircleCreatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCircleGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCirclesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserEntitiesToCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserUsersToCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserCircleCreateRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserCircleGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserCirclesGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserEntitiesToCircleAddRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserUsersToCircleAddRet;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import java.net.URLDecoder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/circles")
@Api( value = "circles", description = "SSS REST API for circles (groups)" )
public class SSRESTCircle {

  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "retrieve circles",
    response = SSEntityUserCirclesGetRet.class)
  public Response entityUserCirclesGet(
    @Context HttpHeaders headers){
    
    try{
      
      return SSRestMain.handleGETRequest(
        headers,
        new SSEntityUserCirclesGetPar(
          SSMethU.entityUserCirclesGet,
          null,
          null,
          false));
      
    }catch(Exception error){
      return Response.status(422).build();
    }
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{circle}")
  @ApiOperation(
    value = "retrieve a certain circle",
    response = SSEntityUserCircleGetRet.class)
  public Response entityCircleGet(
    @Context
      HttpHeaders headers,
    @PathParam(SSVarU.circle) 
      String   circle){
   
    try{
      
      return SSRestMain.handleGETRequest(
        headers,
        new SSEntityUserCircleGetPar(
          SSMethU.entityCircleGet,
          null,
          null,
          URLDecoder.decode(circle, SSEncodingU.utf8),
          false));
      
    }catch(Exception error){
      return Response.status(422).build();
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "create a circle and add users and entities to",
    response = SSEntityUserCircleCreateRet.class)
  public Response entityCircleCreate(
    @Context HttpHeaders              headers,
    final SSEntityUserCircleCreatePar input){
    
    return SSRestMain.handlePOSTRequest(headers, input, SSMethU.entityCircleCreate);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{circle}/users")
  @ApiOperation(
    value = "add given users to a user-generated circle",
    response = SSEntityUserUsersToCircleAddRet.class)
  public Response entityUsersToCircleAdd(
    @Context 
      HttpHeaders headers,
    @PathParam(SSVarU.circle) 
      String   circle,
    final SSEntityUserUsersToCircleAddPar input){
    
    try{
      input.setCircle(URLDecoder.decode(circle, SSEncodingU.utf8));
    }catch(Exception error){
      return Response.status(422).build();
    }
      
    return SSRestMain.handlePOSTRequest(headers, input, SSMethU.entityUsersToCircleAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{circle}/entities")
  @ApiOperation(
    value = "add given entities to a user-generated circle",
    response = SSEntityUserEntitiesToCircleAddRet.class)
  public Response entityEntitiesToCircleAdd(
    @Context 
      HttpHeaders headers,
    @PathParam(SSVarU.circle) 
      String   circle,
    final SSEntityUserEntitiesToCircleAddPar input){
    
    try{
     input.setCircle(URLDecoder.decode(circle, SSEncodingU.utf8));
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMain.handlePOSTRequest(headers, input, SSMethU.entityEntitiesToCircleAdd);
  }
}
