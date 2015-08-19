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
package at.tugraz.sss.adapter.rest.v2.circle;

import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreateFromClientPar;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreatePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesRemoveFromClientPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersInvitePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleCreateRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesAddRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersAddRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersInviteRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCirclesGetRet;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSStrU;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
@Api( value = "/circles") //, basePath = "/circles"
public class SSRESTCircle{
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("")
  @ApiOperation(
    value = "retrieve circles",
    response = SSCirclesGetRet.class)
  public Response circlesGet(
    @Context 
      final HttpHeaders headers){
    
    final SSCirclesGetPar par;
    
    try{
      
      par =
        new SSCirclesGetPar(
          null,
          null, //entity
          null, //entityTypesToIncludeOnly
          true,  //withUserRestriction
          false, //withSystemCircles
          true); //invokeEntityHandlers
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered/{circle}")
  @ApiOperation(
    value = "retrieve a circle",
    response = SSCircleGetRet.class)
  public Response circleGet(
    @Context                    
      final HttpHeaders  headers,
    
    @PathParam (SSVarNames.circle)  
      final String circle, 
    
    final SSCircleGetRESTAPIV2Par input){
    
    final SSCircleGetPar par;
    
    try{
      
      par =
        new SSCircleGetPar(
          null, //user
          SSUri.get(circle, SSVocConf.sssUri), //circle
          input.entityTypesToIncludeOnly, //entityTypesToIncludeOnly
          input.setTags,
          input.tagSpace,
          true,  //withUserRestriction
          true); //invokeEntityHandlers
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{circle}/users/{users}")
  @ApiOperation(
    value = "add given users to a circle",
    response = SSCircleUsersAddRet.class)
  public Response circleUsersAdd(
    @Context 
      final HttpHeaders headers,
    
    @PathParam (SSVarNames.circle) 
      final String circle,
    
    @PathParam (SSVarNames.users) 
      final String users){
    
    final SSCircleUsersAddPar par;
    
    try{
      par =
        new SSCircleUsersAddPar(
          null,
          SSUri.get(circle, SSVocConf.sssUri), //circle
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(users, SSStrU.comma), SSVocConf.sssUri),  //users
          true, //withUserRestriction
          true); //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{circle}/entities/{entities}")
  @ApiOperation(
    value = "add given entities to a circle",
    response = SSCircleEntitiesAddRet.class)
  public Response circleEntitiesAdd(
    @Context 
      final HttpHeaders headers,
    
    @PathParam (SSVarNames.circle) 
      final String circle,
    
    @PathParam (SSVarNames.entities) 
      final String entities, 
    
    final SSCircleEntitiesAddRESTAPIV2Par input){
    
    final SSCircleEntitiesAddPar par;
    
    try{
      
      par =
        new SSCircleEntitiesAddPar(
          null,
          SSUri.get(circle, SSVocConf.sssUri), //circle
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(entities, SSStrU.comma), SSVocConf.sssUri),  //entities
          true,  //withUserRestriction
          true); //shouldCommit
      
      if(input.tags != null){
        par.tags.addAll(input.tags);
      }
      
      if(input.categories != null){
        par.categories.addAll(input.categories);
      }
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{circle}/entities/{entities}")
  @ApiOperation(
    value = "remove given entities from circle",
    response = SSCircleEntitiesRemoveRet.class)
  public Response circleEntitiesRemove(
    @Context 
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.circle) 
      final String circle,
    
    @PathParam(SSVarNames.entities) 
      final String entities, 
    
    final SSCircleEntitiesRemoveRESTAPIV2Par input){

    final SSCircleEntitiesRemovePar par;
    
    try{
      
      par =
        new SSCircleEntitiesRemoveFromClientPar(
          SSUri.get(circle, SSVocConf.sssUri), //circle
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(entities, SSStrU.comma), SSVocConf.sssUri),  //entities
          input.removeCircleSpecificMetadata); //removeCircleSpecificMetadata
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("")
  @ApiOperation(
    value = "create a circle and add users / entities",
    response = SSCircleCreateRet.class)
  public Response circleCreate(
    @Context 
      final HttpHeaders headers,
    
    final SSCircleCreateRESTAPIV2Par input){
    
    final SSCircleCreatePar par;
    
    try{
      
      par =
        new SSCircleCreateFromClientPar(
          null,
          input.label, //label
          input.description, //description
          input.users, //users
          input.invitees, //invitees
          input.entities);//entities
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{circle}/users/{users}")
  @ApiOperation(
    value = "remove users from circle",
    response = SSCircleUsersRemoveRet.class)
  public Response circleUsersRemove(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.circle)
    final String circle,
    
    @PathParam(SSVarNames.users)
    final String users){
    
    final SSCircleUsersRemovePar par;
    
    try{
      
      par =
        new SSCircleUsersRemovePar(
          null,
          SSUri.get(circle, SSVocConf.sssUri), //circle
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(users, SSStrU.comma), SSVocConf.sssUri), //users
          true, //withUserRestriction 
          true); //shouldCommit

    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{circle}")
  @ApiOperation(
    value = "remove circle",
    response = SSCircleRemoveRet.class)
  public Response circleRemove(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.circle)
    final String circle){
    
    final SSCircleRemovePar par;
    
    try{
      
      par =
        new SSCircleRemovePar(
          null,
          SSUri.get(circle, SSVocConf.sssUri), //circle
          true, //withUserRestriction 
          true); //shouldCommit

    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{circle}/users/invite/{emails}")
  @ApiOperation(
    value = "invite users to a circle",
    response = SSCircleUsersInviteRet.class)
  public Response circleUsersInvite(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.circle)
    final String circle,
    
    @PathParam(SSVarNames.emails)
    final String emails){
    
    final SSCircleUsersInvitePar par;
    
    try{
      
      par =
        new SSCircleUsersInvitePar(
          null, 
          SSUri.get(circle, SSVocConf.sssUri), 
          SSStrU.splitDistinctWithoutEmptyAndNull(emails, SSStrU.comma), 
          true, 
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}