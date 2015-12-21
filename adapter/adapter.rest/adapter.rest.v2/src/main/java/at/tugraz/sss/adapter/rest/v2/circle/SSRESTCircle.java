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

import at.kc.tugraz.ss.category.api.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleCreateRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesAddRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleTypeChangeRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersAddRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersInviteRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCirclesGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.api.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.SSCircleCreateFromClientPar;
import at.tugraz.sss.serv.datatype.par.SSCircleCreatePar;
import at.tugraz.sss.serv.datatype.par.SSCircleEntitiesAddPar;
import at.tugraz.sss.serv.datatype.par.SSCircleEntitiesRemoveFromClientPar;
import at.tugraz.sss.serv.datatype.par.SSCircleEntitiesRemovePar;
import at.tugraz.sss.serv.datatype.par.SSCircleGetPar;
import at.tugraz.sss.serv.datatype.par.SSCircleRemovePar;
import at.tugraz.sss.serv.datatype.par.SSCircleTypeChangePar;
import at.tugraz.sss.serv.datatype.par.SSCircleUsersAddPar;
import at.tugraz.sss.serv.datatype.par.SSCircleUsersInvitePar;
import at.tugraz.sss.serv.datatype.par.SSCircleUsersRemovePar;
import at.tugraz.sss.serv.datatype.par.SSCirclesGetPar;
import at.tugraz.sss.serv.reg.*;
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
          null, //user,
          null, //forUser,
          null, //entity
          null, //entityTypesToIncludeOnly
          true, //setEntities,
          true, //setUsers
          true,  //withUserRestriction
          false, //withSystemCircles
          true); //invokeEntityHandlers
      
      par.setThumb          = false;
      par.setProfilePicture = false;
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSEntityClientI entityServ = (SSEntityClientI) SSServReg.getClientServ(SSEntityClientI.class);
      
      return Response.status(200).entity(entityServ.circlesGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/users/{forUser}")
  @ApiOperation(
    value = "retrieve circles for user",
    response = SSCirclesGetRet.class)
  public Response circlesForUsersGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.forUser)
    final String forUser){
    
    final SSCirclesGetPar par;
    
    try{
      
      par =
        new SSCirclesGetPar(
          null, //user,
          SSUri.get(forUser, SSConf.sssUri), //forUser,
          null, //entity
          null, //entityTypesToIncludeOnly
          true, //setEntities,
          true, //setUsers
          true,  //withUserRestriction
          false, //withSystemCircles
          true); //invokeEntityHandlers
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSEntityClientI entityServ = (SSEntityClientI) SSServReg.getClientServ(SSEntityClientI.class);
      
      return Response.status(200).entity(entityServ.circlesGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered")
  @ApiOperation(
    value = "retrieve filtered circles",
    response = SSCirclesGetRet.class)
  public Response circlesFilteredGet(
    @Context
    final HttpHeaders headers,
    
    final SSCirclesGetRESTAPIV2Par input){
    
    final SSCirclesGetPar par;
    
    try{
      
      par =
        new SSCirclesGetPar(
          null, //user,
          input.forUser, //forUser,
          null, //entity
          input.entityTypesToIncludeOnly, //entityTypesToIncludeOnly
          true, //setEntities,
          true, //setUsers
          true,  //withUserRestriction
          false, //withSystemCircles
          input.invokeEntityHandlers); //invokeEntityHandlers
      
      par.setThumb          = input.setThumb;
      par.setProfilePicture = input.setProfilePicture;
      par.setTags           = input.setTags;
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSEntityClientI entityServ = (SSEntityClientI) SSServReg.getClientServ(SSEntityClientI.class);
      
      return Response.status(200).entity(entityServ.circlesGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered/users/{forUser}")
  @ApiOperation(
    value = "retrieve filtered circles for user",
    response = SSCirclesGetRet.class)
  public Response circlesFilteredForUserGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.forUser)
    final String forUser,
    
    final SSCirclesFilteredForUserGetRESTAPIV2Par input){
    
    final SSCirclesGetPar par;
    
    try{
      
      par =
        new SSCirclesGetPar(
          null, //user,
          SSUri.get(forUser, SSConf.sssUri), //forUser,
          null, //entity
          input.entityTypesToIncludeOnly, //entityTypesToIncludeOnly
          true, //setEntities,
          true, //setUsers
          true,  //withUserRestriction
          false, //withSystemCircles
          input.invokeEntityHandlers); //invokeEntityHandlers
      
      par.setThumb          = input.setThumb;
      par.setProfilePicture = input.setProfilePicture;
      par.setTags           = input.setTags;
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSEntityClientI entityServ = (SSEntityClientI) SSServReg.getClientServ(SSEntityClientI.class);
      
      return Response.status(200).entity(entityServ.circlesGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{circle}")
  @ApiOperation(
    value = "retrieve a circle",
    response = SSCircleGetRet.class)
  public Response circleGet(
    @Context
    final HttpHeaders  headers,
    
    @PathParam (SSVarNames.circle)
    final String circle){
    
    final SSCircleGetPar par;
    
    try{
      
      par =
        new SSCircleGetPar(
          null, //user
          SSUri.get(circle, SSConf.sssUri), //circle
          null, //entityTypesToIncludeOnly
          false, //setTags
          null, //circle
          true, //setEntities,
          true, //setUsers
          true,  //withUserRestriction
          true); //invokeEntityHandlers
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSEntityClientI entityServ = (SSEntityClientI) SSServReg.getClientServ(SSEntityClientI.class);
      
      return Response.status(200).entity(entityServ.circleGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered/{circle}")
  @ApiOperation(
    value = "retrieve a filtered circle",
    response = SSCircleGetRet.class)
  public Response circleFilteredGet(
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
          SSUri.get(circle, SSConf.sssUri), //circle
          input.entityTypesToIncludeOnly, //entityTypesToIncludeOnly
          input.setTags,
          input.tagSpace,
          true, //setEntities,
          true, //setUsers
          true,  //withUserRestriction
          input.invokeEntityHandlers); //invokeEntityHandlers
      
      par.setProfilePicture = input.setProfilePicture;
      par.setThumb          = input.setThumb;
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSEntityClientI entityServ = (SSEntityClientI) SSServReg.getClientServ(SSEntityClientI.class);
      
      return Response.status(200).entity(entityServ.circleGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
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
          SSUri.get(circle, SSConf.sssUri), //circle
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(users, SSStrU.comma), SSConf.sssUri),  //users
          true, //withUserRestriction
          true); //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSEntityClientI entityServ = (SSEntityClientI) SSServReg.getClientServ(SSEntityClientI.class);
      
      return Response.status(200).entity(entityServ.circleUsersAdd(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
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
          SSUri.get(circle, SSConf.sssUri), //circle
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(entities, SSStrU.comma), SSConf.sssUri),  //entities
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
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSEntityClientI entityServ = (SSEntityClientI) SSServReg.getClientServ(SSEntityClientI.class);
      
      return Response.status(200).entity(entityServ.circleEntitiesAdd(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
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
          SSUri.get(circle, SSConf.sssUri), //circle
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(entities, SSStrU.comma), SSConf.sssUri),  //entities
          input.removeCircleSpecificMetadata); //removeCircleSpecificMetadata
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSEntityClientI entityServ = (SSEntityClientI) SSServReg.getClientServ(SSEntityClientI.class);
      
      return Response.status(200).entity(entityServ.circleEntitiesRemove(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
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
      
      if(input.type == null){
        input.type = SSCircleE.group;
      }
      
      par =
        new SSCircleCreateFromClientPar(
          null,
          input.type,
          input.label, //label
          input.description, //description
          input.users, //users
          input.invitees, //invitees
          input.entities);//entities
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSEntityClientI entityServ = (SSEntityClientI) SSServReg.getClientServ(SSEntityClientI.class);
      
      return Response.status(200).entity(entityServ.circleCreate(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
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
          SSUri.get(circle, SSConf.sssUri), //circle
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(users, SSStrU.comma), SSConf.sssUri), //users
          true, //withUserRestriction
          true); //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSEntityClientI entityServ = (SSEntityClientI) SSServReg.getClientServ(SSEntityClientI.class);
      
      return Response.status(200).entity(entityServ.circleUsersRemove(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
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
          SSUri.get(circle, SSConf.sssUri), //circle
          true, //withUserRestriction
          true); //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSEntityClientI entityServ = (SSEntityClientI) SSServReg.getClientServ(SSEntityClientI.class);
      
      return Response.status(200).entity(entityServ.circleRemove(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
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
          SSUri.get(circle, SSConf.sssUri),
          SSStrU.splitDistinctWithoutEmptyAndNull(emails, SSStrU.comma),
          true,
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSEntityClientI entityServ = (SSEntityClientI) SSServReg.getClientServ(SSEntityClientI.class);
      
      return Response.status(200).entity(entityServ.circleUsersInvite(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{circle}/type/{type}")
  @ApiOperation(
    value = "change circle's type",
    response = SSCircleTypeChangeRet.class)
  public Response circleTypeChange(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.circle)
    final String circle,
    
    @PathParam(SSVarNames.type)
    final String type){
    
    final SSCircleTypeChangePar par;
    
    try{
      
      par =
        new SSCircleTypeChangePar(
          null,
          SSUri.get(circle, SSConf.sssUri),
          SSCircleE.get(type),
          true, //withUserRestriction
          true); //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSEntityClientI entityServ = (SSEntityClientI) SSServReg.getClientServ(SSEntityClientI.class);
      
      return Response.status(200).entity(entityServ.circleTypeChange(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
  }
}