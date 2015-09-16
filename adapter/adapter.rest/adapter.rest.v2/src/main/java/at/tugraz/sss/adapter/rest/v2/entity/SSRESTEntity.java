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
package at.tugraz.sss.adapter.rest.v2.entity;

import at.tugraz.sss.servs.entity.datatypes.par.SSEntitySharePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityUsersGetPar;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityShareRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntityUsersGetRet;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityTypesGetPar;
import at.tugraz.sss.serv.SSEntityCopyPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntitiesGetRet;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityCopyRet;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityUpdateRet;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.sss.comment.datatypes.par.SSCommentsAddPar;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.servs.entity.datatypes.ret.SSEntityTypesGetRet;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
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

@Path("/entities")
@Api( value = "/entities") //, basePath = "/entities"
public class SSRESTEntity {
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/types")
  @ApiOperation(
    value = "retrieve available entity types",
    response = SSEntityTypesGetRet.class)
  public Response entityTypesGet(
    @Context 
      final HttpHeaders headers){
    
    final SSEntityTypesGetPar par;
    
    try{
      
      par =
        new SSEntityTypesGetPar(
          null); 
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "retrieve entities",
    response = SSEntitiesGetRet.class)
  @Path("")
  public Response entitiesAccessibleGet(
    @Context
    final HttpHeaders headers){
    
    final SSEntitiesGetPar par;
    
    try{
      
      par =
        new SSEntitiesGetPar(
          null,  //user
          null,  //entities
          null, //types
          null, //descPar
          true); //withUserRestriction
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered/{entities}")
  @ApiOperation(
    value = "retrieve entity/entities information for given ID(s) or encoded URI(s)",
    response = SSEntitiesGetRet.class)
  public Response entitiesGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.entities)
    final String entities,
    
    final SSEntitiesGetRESTAPIV2Par input){
    
    final SSEntityDescriberPar   descPar = new SSEntityDescriberPar(null);
    final SSEntitiesGetPar       par;
    
    try{
      
      descPar.circle            = input.circle;
      descPar.space             = input.tagSpace;
      descPar.setTags           = input.setTags;
      descPar.setOverallRating  = input.setOverallRating;
      descPar.setDiscs          = input.setDiscs;
      descPar.setUEs            = input.setUEs;
      descPar.setThumb          = input.setThumb;
      descPar.setFlags          = input.setFlags;
      descPar.setCircles        = input.setCircles;
      descPar.setProfilePicture = input.setProfilePicture;
      
      par =
        new SSEntitiesGetPar(
          null,
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(entities, SSStrU.comma), SSVocConf.sssUri), //entities
          null, //types
          descPar, //descPar
          true); //withUserRestriction
      
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
    value = "adds a new entity with given properties",
    response = SSEntityUpdateRet.class)
  public Response entityAdd(
    @Context
    final HttpHeaders headers,
    
    final SSEntityUpdateRESTAPIV2Par input){
    
    final SSEntityUpdatePar par;
    
    try{
      par =
        new SSEntityUpdatePar(
          null, //user
          null, //entity
          input.type, //type
          input.label,       //label
          input.description, //description
          input.creationTime, //creationTime
          input.read,  //read
          null, //setPublic
          true, //withUserRestriction,
          true); //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{entity}")
  @ApiOperation(
    value = "updates / adds given properties for given entity",
    response = SSEntityUpdateRet.class)
  public Response entityUpdate(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.entity)
    final String entity,
    
    final SSEntityUpdateRESTAPIV2Par input){
    
    final SSEntityUpdatePar par;
    
    try{
      par =
        new SSEntityUpdatePar(
          null, //user
          SSUri.get(entity, SSVocConf.sssUri), //entity
          input.type, //type
          input.label,       //label
          input.description, //description
          input.creationTime, //creationTime
          input.read,  //read
          null, //setPublic
          true, //withUserRestriction,
          true); //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{entity}/share")
  @ApiOperation(
    value = "share an entity with users, circles or set it public (make it accessible for everyone)",
    response = SSEntityShareRet.class)
  public Response entityShare(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.entity)
    final String entity,
    
    final SSEntityShareRESTAPIV2Par input){
    
    final SSEntitySharePar par;
    
    try{
      par =
        new SSEntitySharePar(
          null, //user
          SSUri.get(entity, SSVocConf.sssUri), //entity
          input.users,
          input.circles,
          input.setPublic, //setPublic,
          input.comment, //comment
          true, //withUserRestriction,
          true); //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{entity}/copy")
  @ApiOperation(
    value = "copy an entity",
    response = SSEntityCopyRet.class)
  public Response entityCopy(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.entity)
    final String entity,
    
    final SSEntityCopyRESTAPIV2Par input){
    
    final SSEntityCopyPar par;
    
    try{
      par =
        new SSEntityCopyPar(
          null, //user
          SSUri.get(entity, SSVocConf.sssUri), //entity
          input.targetEntity, //targetEntity
          input.forUsers,
          input.label,
          input.includeUsers,
          input.includeEntities,
          input.includeMetaSpecificToEntityAndItsEntities,
          input.includeOriginUser,
          input.entitiesToExclude,
          input.comment,
          true, //withUserRestriction,
          true); //shouldCommit
      
      par.appendUserNameToLabel = input.appendUserNameToLabel;
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "retrieve users who can access given entity",
    response = SSCircleEntityUsersGetRet.class)
  @Path("/{entity}/users")
  public Response entityUsersGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.entity)
    final String entity){
    
    final SSCircleEntityUsersGetPar par;
    
    try{
      
      par =
        new SSCircleEntityUsersGetPar(
          null,  //user
          SSUri.get(entity, SSVocConf.sssUri),  //entity
          false, //invokeEntityHandlers
          true); //withUserRestriction
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{entity}/comments")
  @ApiOperation(
    value = "add comments to the entity",
    response = SSCommentsAddPar.class)
  public Response commentsAdd(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.entity)
    final String entity,
    
    final SSCommentsAddRESTAPIV2Par input){
    
    final SSCommentsAddPar       par;
    
    try{
      
      par =
        new SSCommentsAddPar(
          null,
          SSUri.get(entity, SSVocConf.sssUri), //entity
          input.comments, //comments
          true, //withUserRestriction
          true);  //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}