/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.adapter.rest.v2.appstacklayout;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutCreatePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutDeletePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutUpdatePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutsGetPar;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutCreateRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutDeleteRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutUpdateRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutsGetRet;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/appstacklayouts")
@Api( value = "/appstacklayouts") //, basePath = "/appstacklayouts"
public class SSRESTAppStackLayout{
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("")
  @ApiOperation(
    value = "retrieve appStackLayouts",
    response = SSAppStackLayoutsGetRet.class)
  public Response appStackLayoutsGet(
    @Context HttpHeaders                     headers){
    
    final SSAppStackLayoutsGetPar par;
    
    try{
      par =
        new SSAppStackLayoutsGetPar(
          SSServOpE.appStackLayoutsGet,
          null,
          null, 
          true); //invokeEntityHandlers
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("")
  @ApiOperation(
    value = "create an arrangement of tiles within an app",
    response = SSAppStackLayoutCreateRet.class)
  public Response appStackLayoutCreatePost(
    @Context HttpHeaders                     headers,
    final SSAppStackLayoutCreateRESTAPIV2Par input){
    
    final SSAppStackLayoutCreatePar par;
    
    try{
      par =
        new SSAppStackLayoutCreatePar(
          SSServOpE.appStackLayoutCreate,
          null,
          null,
          input.uuid,
          input.app,
          input.label,
          input.description, 
          true, //withUserRestriction, 
          true); //shouldCommit);
    
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{stack}")
  @ApiOperation(
    value = "delete an arrangement of tiles within an app",
    response = SSAppStackLayoutDeleteRet.class)
  public Response appStackLayoutDelete(
    @Context HttpHeaders              headers,
    @PathParam(SSVarNames.stack) String   stack){
    
    final SSAppStackLayoutDeletePar par;
    
    try{
      par =
        new SSAppStackLayoutDeletePar(
          SSServOpE.appStackLayoutDelete,
          null,
          null,
          SSUri.get(stack, SSVocConf.sssUri), //stack, 
          true, //withUserRestriction
          true); //shouldCommit
    
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{stack}")
  @ApiOperation(
    value = "update an arrangement of tiles within an app",
    response = SSAppStackLayoutUpdateRet.class)
  public Response appStackLayoutUpdate(
    @Context 
      HttpHeaders headers,
    
    @PathParam(SSVarNames.stack) 
      String stack,
    
    final SSAppStackLayoutUpdateRESTAPIV2Par input){
    
    final SSAppStackLayoutUpdatePar par;
    
    try{
      par =
        new SSAppStackLayoutUpdatePar(
          SSServOpE.appStackLayoutUpdate,
          null,
          null,
          SSUri.get(stack, SSVocConf.sssUri),
          input.app, 
          input.label,
          input.description, 
          true,  //withUserDescription
          true); //shouldCommit
    
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}
