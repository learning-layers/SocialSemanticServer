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
package at.tugraz.sss.adapter.rest.v2.user;

import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUsersGetPar;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.service.user.datatypes.ret.SSUsersGetRet;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSUri;
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

@Path("/users")
@Api(value = "/users")
public class SSRESTUser{
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("")
  @ApiOperation(
    value = "retrieve users",
    response = SSUsersGetRet.class)
  public Response usersGet(
    @Context
    final HttpHeaders headers){
    
    final SSUsersGetPar par;
    
    try{
      
      par =
        new SSUsersGetPar(
          null,
          null, //users
          true); //invokeEntityHandlers
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{users}")
  @ApiOperation(
    value = "retrieve given users",
    response = SSUsersGetRet.class)
  public Response usersGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.users)
    final String users){
    
    final SSUsersGetPar par;
    
    try{
      
      par =
        new SSUsersGetPar(
          null,
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(users, SSStrU.comma), SSVocConf.sssUri), //users
          true); //invokeEntityHandlers
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered/{users}")
  @ApiOperation(
    value = "retieve given users",
    response = SSUsersGetRet.class)
  public Response usersFilteredGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.users)
    final String users,
    
    final SSUsersGetRESTAPIV2Par input){
    
    final SSUsersGetPar par;
    
    try{
      
      par =
        new SSUsersGetPar(
          null,
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(users, SSStrU.comma), SSVocConf.sssUri), //users
          true); //invokeEntityHandlers
      
      par.setFriends        = input.setFriends;
      par.setProfilePicture = input.setProfilePicture;
      par.setThumb          = input.setThumb;
      par.setMessages       = input.setMessages;
      par.setActivities     = input.setActivities;
      par.setCircles        = input.setCircles;
      par.setDiscs          = input.setDiscs;
      par.setColls          = input.setColls;
      par.setTags           = input.setTags;
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}
