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
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSEntityCircleCreateRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSEntityEntitiesToCircleAddRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSEntityUsersToCircleAddRESTAPIV2Par;
import at.kc.tugraz.ss.datatypes.datatypes.SSCircleE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
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
    
    final SSEntityUserCirclesGetPar par;
    
    try{
      
      par =
        new SSEntityUserCirclesGetPar(
          SSMethU.entityUserCirclesGet,
          null,
          null,
          null,
          false);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMain.handleGETRequest(headers, par);
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
    
    final SSEntityUserCircleGetPar par;
    
    try{
      
      par =
        new SSEntityUserCircleGetPar(
          SSMethU.entityCircleGet,
          null,
          null,
          null,
          SSUri.get(SSEncodingU.decode(circle)),
          false);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMain.handleGETRequest(headers, par);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "create a circle and add users and entities to",
    response = SSEntityUserCircleCreateRet.class)
  public Response entityCircleCreate(
    @Context HttpHeaders              headers,
    final SSEntityCircleCreateRESTAPIV2Par input){
    
    final SSEntityUserCircleCreatePar par;
    
    try{
      
      par =
        new SSEntityUserCircleCreatePar(
          SSMethU.entityCircleCreate,
          null,
          null,
          input.label,
          input.entities,
          input.users,
          input.description,
          false,
          SSCircleE.group);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMain.handlePOSTRequest(headers, par, par.op);
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
    final SSEntityUsersToCircleAddRESTAPIV2Par input){
    
    final SSEntityUserUsersToCircleAddPar par;
    
    try{
      par =
        new SSEntityUserUsersToCircleAddPar(
          SSMethU.entityUsersToCircleAdd,
          null,
          null,
          SSUri.get(SSEncodingU.decode(circle)),
          input.users);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMain.handlePOSTRequest(headers, par, par.op);
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
    final SSEntityEntitiesToCircleAddRESTAPIV2Par input){
    
    final SSEntityUserEntitiesToCircleAddPar par;
    
    try{
      
      par =
        new SSEntityUserEntitiesToCircleAddPar(
          SSMethU.entityEntitiesToCircleAdd,
          null,
          null,
          SSUri.get(SSEncodingU.decode(circle)),
          input.entities);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMain.handlePOSTRequest(headers, par, par.op);
  }
}
