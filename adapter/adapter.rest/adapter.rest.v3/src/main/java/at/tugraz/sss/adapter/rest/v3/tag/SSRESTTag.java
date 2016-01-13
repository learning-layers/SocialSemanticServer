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
package at.tugraz.sss.adapter.rest.v3.tag;

import at.kc.tugraz.ss.service.tag.api.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.adapter.rest.v3.SSRestMain;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.conf.SSConf;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagEntitiesForTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagFrequsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsRemovePar;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagAddRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagEntitiesForTagsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagFrequsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsAddRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsRemoveRet;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.reg.*;
import io.swagger.annotations.*;
import javax.annotation.*;
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
@Api( value = "tags")
public class SSRESTTag{
  
  @PostConstruct
  public void createRESTResource(){
  }
  
  @PreDestroy
  public void destroyRESTResource(){
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "retrieve tag assignments",
    response = SSTagsGetRet.class)
  public Response tagsGet(
    @Context HttpHeaders headers){
    
    final SSTagsGetPar par;
    
    try{
      
      par =
        new SSTagsGetPar(
          null, //user
          null, //forUser
          null, //entities
          null, //labels
          null, //spaces
          null, //labelSearchOp
          null, //circles
          null, //startTime
          true); //withUserRestriction
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMain.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSTagClientI tagServ = (SSTagClientI) SSServReg.getClientServ(SSTagClientI.class);
      
      return Response.status(200).entity(tagServ.tagsGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMain.prepareErrors(error);
    }
    
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
    
    final SSTagsGetRESTPar input){
    
    final SSTagsGetPar par;
    
    try{
      par =
        new SSTagsGetPar(
          null,
          input.forUser, //forUser
          input.entities, //entities
          input.labels, //labels
          SSSearchOpE.or, //labelSearchOp
          SSSpaceE.asListWithoutNull(input.space), //spaces
          input.circles, //circles
          input.startTime, //startTime,
          true); //withUserRestriction
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMain.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSTagClientI tagServ = (SSTagClientI) SSServReg.getClientServ(SSTagClientI.class);
      
      return Response.status(200).entity(tagServ.tagsGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMain.prepareErrors(error);
    }
    
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
    
    try{
      par.key = SSRestMain.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSTagClientI tagServ = (SSTagClientI) SSServReg.getClientServ(SSTagClientI.class);
      
      return Response.status(200).entity(tagServ.tagFrequsGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMain.prepareErrors(error);
    }
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
    
    final SSTagFrequsGetRESTPar input){
    
    final SSTagFrequsGetPar par;
    
    try{
      par =
        new SSTagFrequsGetPar(
          null,
          input.forUser, //forUser
          input.entities, //entities
          input.labels, //labels
          SSSpaceE.asListWithoutNull(input.space), //spaces
          input.circles, //circles
          input.startTime, //startTime
          input.useUsersEntities, //useUsersEntities
          true); //withUserRestriction
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMain.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSTagClientI tagServ = (SSTagClientI) SSServReg.getClientServ(SSTagClientI.class);
      
      return Response.status(200).entity(tagServ.tagFrequsGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMain.prepareErrors(error);
    }
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
    
    final SSTagEntitiesForTagsGetRESTPar input){
    
    final SSTagEntitiesForTagsGetPar par;
    
    try{
      par =
        new SSTagEntitiesForTagsGetPar(
          null, //user
          input.forUser, //forUser
          null, //entities
          input.labels, //labels
          SSSearchOpE.or, //labelSearchOp
          SSSpaceE.asListWithoutNull(input.space), //spaces
          null, //circles
          input.startTime, //startTime,
          true); //withUserRestriction
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMain.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSTagClientI tagServ = (SSTagClientI) SSServReg.getClientServ(SSTagClientI.class);
      
      return Response.status(200).entity(tagServ.tagEntitiesForTagsGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMain.prepareErrors(error);
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/entities/{entities}")
  @ApiOperation(
    value = "add tags for entities",
    response = SSTagsAddRet.class)
  public Response tagsAdd(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.entities)
    final String entities,
    
    final SSTagsAddRESTPar     input){
    
    final SSTagsAddPar par;
    
    try{
      par =
        new SSTagsAddPar(
          null,
          input.labels, //labels
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(entities, SSStrU.comma), SSConf.sssUri), //entities
          input.space, //space
          input.circle, //circle
          input.creationTime,
          true, //withUserRestriction
          true); //commit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMain.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSTagClientI tagServ = (SSTagClientI) SSServReg.getClientServ(SSTagClientI.class);
      
      return Response.status(200).entity(tagServ.tagsAdd(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMain.prepareErrors(error);
    }
    
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
    
    final SSTagsRemoveRESTPar     input){
    
    final SSTagsRemovePar par;
    
    try{
      par =
        new SSTagsRemovePar(
          null,
          null,
          SSUri.get(entity, SSConf.sssUri), //entity
          input.label, //label
          input.space, //space
          input.circle, //circle
          true,
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMain.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSTagClientI tagServ = (SSTagClientI) SSServReg.getClientServ(SSTagClientI.class);
      
      return Response.status(200).entity(tagServ.tagsRemove(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMain.prepareErrors(error);
    }
    
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "add a tag for an entity within given space",
    response = SSTagAddRet.class)
  public Response tagAdd(
    @Context
    final HttpHeaders headers,
    
    final SSTagAddRESTPar input){
    
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
    
    try{
      par.key = SSRestMain.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSTagClientI tagServ = (SSTagClientI) SSServReg.getClientServ(SSTagClientI.class);
      
      return Response.status(200).entity(tagServ.tagAdd(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMain.prepareErrors(error);
    }
  }
}
