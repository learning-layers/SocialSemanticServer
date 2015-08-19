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
package at.tugraz.sss.adapter.rest.v2.tag;

import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagEntitiesForTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagFrequsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsRemovePar;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagAddRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagEntitiesForTagsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagFrequsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsRemoveRet;
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

@Path("/tags")
@Api( value = "/tags")
public class SSRESTTag{
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("")
  @ApiOperation(
    value = "retrieve tag assignments",
    response = SSTagsGetRet.class)
  public Response tagsGet(
    @Context HttpHeaders headers){
    
    final SSTagsGetPar par;
    
    try{
      
      par =
        new SSTagsGetPar(
          null,
          null, //forUser
          null, //entities
          null, //labels
          null, //space
          null, //circles
          null, //startTime
          true); //withUserRestriction
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered")
  @ApiOperation(
    value = "retrieve tag assignments",
    response = SSTagsGetRet.class)
  public Response tagsGetFiltered(
    @Context 
      final HttpHeaders headers,
    
    final SSTagsGetRESTAPIV2Par input){
    
    final SSTagsGetPar par;
    
    try{
      par =
        new SSTagsGetPar(
          null,
          input.forUser, //forUser
          input.entities, //entities
          input.labels, //labels
          input.space, //space
          input.circles, //circles
          input.startTime, //startTime, 
          true); //withUserRestriction
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/frequs")
  @ApiOperation(
    value = "retrieve tag frequencies",
    response = SSTagFrequsGetRet.class)
  public Response tagFrequsGet(
    @Context
      final HttpHeaders headers){
    
    final SSTagFrequsGetPar par;
    
    try{
      par =
        new SSTagFrequsGetPar(
          null,
          null, //forUser
          null, //entities
          null, //labels
          null, //space
          null, //circles
          null, //startTime
          false, //useUsersEntities
          true); //withUserRestriction
          
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/filtered/frequs")
  @ApiOperation(
    value = "retrieve tag frequencies",
    response = SSTagFrequsGetRet.class)
  public Response tagFrequsGetFiltered(
    @Context 
      final HttpHeaders headers,
    
    final SSTagFrequsGetRESTAPIV2Par input){
    
    final SSTagFrequsGetPar par;
    
    try{
      par =
        new SSTagFrequsGetPar(
          null,
          input.forUser, //forUser
          input.entities, //entities
          input.labels, //labels
          input.space, //space
          input.circles, //circles
          input.startTime, //startTime
          input.useUsersEntities, //useUsersEntities
          true); //withUserRestriction
            
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/filtered/entities")
  @ApiOperation(
    value = "retrieve entities for tags (currently startTime is not used to retrieve entities)",
    response = SSTagEntitiesForTagsGetRet.class)
  public Response tagEntitiesForTagsGetFiltered(
    @Context
    final HttpHeaders headers,
    
    final SSTagEntitiesForTagsGetRESTAPIV2Par input){
    
    final SSTagEntitiesForTagsGetPar par;
    
    try{
      par =
        new SSTagEntitiesForTagsGetPar(
          null,
          input.forUser,
          input.labels,
          input.space,
          input.startTime, 
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
    @Path    ("/entities/{entity}")
  @ApiOperation(
    value = "remove tag, user, entity, space combinations for given entity",
    response = SSTagsRemoveRet.class)
  public Response tagsRemove(
    @Context 
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.entity) 
      final String entity,
    
    final SSTagsRemoveRESTAPIV2Par     input){
    
    final SSTagsRemovePar par;
    
    try{
      par =
        new SSTagsRemovePar(
          null,
          null,
          SSUri.get(entity, SSVocConf.sssUri), //entity
          input.label, //label
          input.space, //space
          input.circle, //circle
          true,
          true); 
      
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
    value = "add a tag for an entity within given space",
    response = SSTagAddRet.class)
  public Response tagAdd(
    @Context 
      final HttpHeaders headers,
    
    final SSTagAddRESTAPIV2Par input){
    
    final SSTagAddPar par;
    
    try{
      par =
        new SSTagAddPar(
          null,
          input.entity, //entity
          input.label, //label
          input.space, //space
          input.circle, //circle
          input.creationTime,  //creationTime
          true, //withUserRestriction
          true); //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}
