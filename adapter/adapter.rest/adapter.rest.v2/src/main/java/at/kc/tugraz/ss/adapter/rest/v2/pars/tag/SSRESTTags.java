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
package at.kc.tugraz.ss.adapter.rest.v2.pars.tag;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagUserEditPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagUserEntitiesForTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagUserFrequsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsUserGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsUserRemovePar;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagAddRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagUserEditRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagUserEntitiesForTagsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagUserFrequsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsUserGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsUserRemoveRet;
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

@Path("/tags")
@Api( value = "/tags") //, basePath = "/tags"
public class SSRESTTags{
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("")
  @ApiOperation(
    value = "retrieve tag assignments",
    response = SSTagsUserGetRet.class)
  public Response tagsGet(
    @Context HttpHeaders headers){
    
    final SSTagsUserGetPar par;
    
    try{
      
      par =
        new SSTagsUserGetPar(
          SSMethU.tagsGet,
          null,
          null,
          null,
          null,
          null,
          null,
          null);
      
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
    value = "retrieve tag assignments",
    response = SSTagsUserGetRet.class)
  public Response tagsGetPost(
    @Context HttpHeaders headers,
    final SSTagsGetRESTAPIV2Par input){
    
    final SSTagsUserGetPar par;
    
    try{
      par =
        new SSTagsUserGetPar(
          SSMethU.tagsGet,
          null,
          null,
          input.forUser,
          input.entities,
          input.labels,
          input.space,
          input.startTime);
      
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
    response = SSTagUserFrequsGetRet.class)
  public Response tagFrequsGet(
    @Context
      HttpHeaders headers){
    
    final SSTagUserFrequsGetPar par;
    
    try{
      par =
        new SSTagUserFrequsGetPar(
          SSMethU.tagFrequsGet,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          false);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/frequs")
  @ApiOperation(
    value = "retrieve tag frequencies",
    response = SSTagUserFrequsGetRet.class)
  public Response tagFrequsGetPost(
    @Context HttpHeaders headers,
    final SSTagFrequsGetRESTAPIV2Par input){
    
    final SSTagUserFrequsGetPar par;
    
    try{
      par =
        new SSTagUserFrequsGetPar(
          SSMethU.tagFrequsGet,
          null,
          null,
          input.forUser,
          input.entities,
          input.labels,
          input.space,
          input.startTime,
          input.useUsersEntities);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/entities")
  @ApiOperation(
    value = "retrieve entities for tags (currently startTime is not used to retrieve entities)",
    response = SSTagUserEntitiesForTagsGetRet.class)
  public Response tagEntitiesGetPost(
     @Context HttpHeaders                     headers,
    final SSTagEntitiesForTagsGetRESTAPIV2Par input){
    
    final SSTagUserEntitiesForTagsGetPar par;
    
    try{
      par =
        new SSTagUserEntitiesForTagsGetPar(
          SSMethU.tagEntitiesForTagsGet,
          null,
          null,
          input.forUser,
          input.labels,
          input.space,
          input.startTime);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{entity}")
  @ApiOperation(
    value = "remove tag, user, entity, space combinations",
    response = SSTagsUserRemoveRet.class)
  public Response tagsRemoveDelete(
    @Context HttpHeaders               headers,
    @PathParam(SSVarU.entity) String   entity,
    final SSTagsRemoveRESTAPIV2Par     input){
    
    final SSTagsUserRemovePar par;
    
    try{
      par =
        new SSTagsUserRemovePar(
          SSMethU.tagsRemove,
          null,
          null,
          SSUri.get(entity, SSVocConf.sssUri),
          input.label,
          input.space);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{entity}")
  @ApiOperation(
    value = "add a tag for an entity within given space",
    response = SSTagAddRet.class)
  public Response tagAddPost(
    @Context HttpHeaders            headers,
    @PathParam(SSVarU.entity)String entity,
    final SSTagAddRESTAPIV2Par      input){
    
    final SSTagAddPar par;
    
    try{
      par =
        new SSTagAddPar(
          SSMethU.tagAdd,
          null,
          null,
          SSUri.get(entity, SSVocConf.sssUri),
          input.label,
          input.space,
          input.creationTime);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/label/{tag}")
  @ApiOperation(
    value = "changes the label of the tag assigned to entities by given user",
    response = SSTagUserEditRet.class)
  public Response tagEditPut(
    @Context HttpHeaders            headers,
    @PathParam(SSVarU.tag) String   tag,
    final SSTagEditRESTAPIV2Par     input) throws Exception{
    
    final SSTagUserEditPar par;
    
    try{
      par =
        new SSTagUserEditPar(
          SSMethU.tagEdit,
          null,
          null,
          SSTagLabel.get(tag),
          input.entity,
          input.label);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}
