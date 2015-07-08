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
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntitiesGetRet;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUpdateRet;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSStrU;
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
          SSServOpE.entitiesGet,
          null,  //key
          null,  //user
          null,  //entities 
          null, //forUser
          SSEntityE.asListWithoutNullAndEmpty(), //types
          true,  //invokeEntityHandlers
          new SSEntityDescriberPar(
            false, //setTags, 
            false, //setOverallRating, 
            false, //setDiscs, 
            false, //setUEs, 
            false, //setThumb, 
            false, //setFlags, 
            false), //setCircles), //descPar
          true, //withUserRestriction
          true); //logErr
      
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
    
    final SSEntitiesGetPar par;
    
    try{
      
      par =
        new SSEntitiesGetPar(
          SSServOpE.entitiesGet,
          null,
          null,
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(entities, SSStrU.comma), SSVocConf.sssUri), //entities
          null, //forUser,
          null, //types
          true, //invokeEntityHandlers
          new SSEntityDescriberPar(
            input.setTags,  //setTags,
            input.setOverallRating,  //setOverallRating,
            input.setDiscs,  //setDiscs,
            input.setUEs,  //setUEs,
            input.setThumb,  //setThumb,
            input.setFlags,  //setFlags,
            input.setCircles), //setCircles) //descPar
          true, //withUserRestriction
          true); //logErr
      
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
          SSServOpE.entityUpdate, 
          null, //key 
          null, //user
          SSUri.get(entity, SSVocConf.sssUri), //entity
          null, //uriAlternative
          input.type, //type
          input.label,       //label
          input.description, //description
          input.comments,    //comments
          null, //entitiesToAttach
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
          SSServOpE.entityShare, 
          null, //key 
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
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "retrieve users who can access given entity",
    response = SSCircleEntityUsersGetRet.class)
  @Path("/{entity}")
  public Response entityUsersGet(
    @Context
    final HttpHeaders headers){
    
    final SSCircleEntityUsersGetPar par;
    
    try{
      
      par =
        new SSCircleEntityUsersGetPar(
          SSServOpE.circleEntityUsersGet,
          null,  //key
          null,  //user
          null,  //entity
          false, //invokeEntityHandlers
          true); //withUserRestriction
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}