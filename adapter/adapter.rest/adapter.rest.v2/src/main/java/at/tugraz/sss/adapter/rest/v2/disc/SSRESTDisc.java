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
package at.tugraz.sss.adapter.rest.v2.disc;

import at.kc.tugraz.ss.serv.datatypes.entity.api.*;
import at.kc.tugraz.ss.service.disc.api.*;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.tugraz.sss.conf.SSConf;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAcceptPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAddFromClientPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAddPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryUpdatePar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscTargetsAddPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUpdatePar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryAcceptRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryUpdateRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscGetRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscTargetsAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUpdateRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscsGetRet;
import at.tugraz.sss.serv.conf.api.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.impl.api.*;
import at.tugraz.sss.serv.reg.*;
import io.swagger.annotations.*;
import javax.annotation.*;
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

@Path("/discs/discs")
@Api( value = "discs")
public class SSRESTDisc{
  
  @PostConstruct
  public void createRESTResource(){
    
  }
  
  @PreDestroy
  public void destroyRESTResource(){
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered")
  @ApiOperation(
    value = "retrieve discussions",
    response = SSDiscsGetRet.class)
  public Response discsGet(
    @Context
    final HttpHeaders headers,
    
    final SSDiscsGetRESTAPIV2Par input){
    
    final SSDiscsGetPar par;
    
    try{
      
      par =
        new SSDiscsGetPar(
          null, //user,
          input.setEntries, //setEntries,
          input.forUser, //forUser,
          null, //discs,
          null, //target,
          true, //withUserRestriction,
          true); //invokeEntityHandlers
      
      par.setCircleTypes      = input.setCircleTypes;
      par.setComments         = input.setComments;
      par.setLikes            = input.setLikes;
      par.setTags             = input.setTags;
      par.setAttachedEntities = input.setAttachedEntities;
      par.setReads            = input.setReads;
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSDiscClientI discServ = (SSDiscClientI) SSServReg.getClientServ(SSDiscClientI.class);
      
      return Response.status(200).entity(discServ.discsGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "add a textual comment/answer/opinion to a discussion [for given entity] or create a new discussion",
    response = SSDiscEntryAddRet.class)
  public Response discEntryAdd(
    @Context
    final HttpHeaders headers,
    
    final SSDiscEntryAddRESTAPIV2Par input){
    
    final SSDiscEntryAddPar par;
    
    try{
      
      par =
        new SSDiscEntryAddFromClientPar(
          input.disc, //disc
          input.targets, //targets,
          input.entry, //entry
          input.addNewDisc, //addNewDisc
          input.type, //type
          input.label, //label
          input.description, //description
          input.users, //users
          input.circles, //circles
          input.entities, //entities
          input.entityLabels); //entityLabels);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSDiscClientI discServ = (SSDiscClientI) SSServReg.getClientServ(SSDiscClientI.class);
      
      return Response.status(200).entity(discServ.discEntryAdd(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{disc}")
  @ApiOperation(
    value = "update discussion",
    response = SSDiscUpdateRet.class)
  public Response discUpdate(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.disc)
    final String disc,
    
    final SSDiscUpdateRESTAPIV2Par input){
    
    final SSDiscUpdatePar par;
    
    try{
      
      par =
        new SSDiscUpdatePar(
          null,
          SSUri.get(disc, SSConf.sssUri),
          input.label,
          input.content,
          input.entitiesToRemove,
          input.entitiesToAttach,
          input.entityLabels,
          input.read,
          true, //withUserRestriction,
          true);//shouldCommit);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSDiscClientI discServ = (SSDiscClientI) SSServReg.getClientServ(SSDiscClientI.class);
      
      return Response.status(200).entity(discServ.discUpdate(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/entries/{entry}")
  @ApiOperation(
    value = "update a textual comment/answer/opinion of a discussion",
    response = SSDiscEntryUpdateRet.class)
  public Response discEntryUpdate(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.entry)
    final String entry,
    
    final SSDiscEntryUpdateRESTAPIV2Par input){
    
    final SSDiscEntryUpdatePar par;
    
    try{
      
      par =
        new SSDiscEntryUpdatePar(
          null,
          SSUri.get(entry, SSConf.sssUri),
          input.content,
          input.entitiesToRemove,
          input.entitiesToAttach,
          input.entityLabels,
          true, //withUserRestriction,
          true);//shouldCommit);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSDiscClientI discServ = (SSDiscClientI) SSServReg.getClientServ(SSDiscClientI.class);
      
      return Response.status(200).entity(discServ.discEntryUpdate(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered/{disc}")
  @ApiOperation(
    value = "retrieve a discussion with its entries",
    response = SSDiscGetRet.class)
  public Response discWithEntriesGet(
    @Context
    final HttpHeaders  headers,
    
    @PathParam (SSVarNames.disc)
    final String disc,
    
    final SSDiscGetRESTAPIV2Par input){
    
    final SSDiscGetPar par;
    
    try{
      
      par =
        new SSDiscGetPar(
          null, //user
          SSUri.get(disc, SSConf.sssUri), //disc
          input.setEntries, //setEntries
          true, //withUserRestriction
          true); //invokeEntityHandlers
      
      par.setCircleTypes      = input.setCircleTypes;
      par.setComments         = input.setComments;
      par.setLikes            = input.setLikes;
      par.setTags             = input.setTags;
      par.setAttachedEntities = input.setAttachedEntities;
      par.setReads            = input.setReads;
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSDiscClientI discServ = (SSDiscClientI) SSServReg.getClientServ(SSDiscClientI.class);
      
      return Response.status(200).entity(discServ.discsGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/targets/{targets}")
  @ApiOperation(
    value = "retrieve discussions for certain entities, i.e., targets",
    response = SSDiscsGetRet.class)
  public Response discsForTargetsGet(
    @Context
    final HttpHeaders  headers,
    
    @PathParam (SSVarNames.targets)
    final String targets){
    
    final SSDiscsGetPar par;
    
    try{
      
      par =
        new SSDiscsGetPar(
          null, //user
          true, //setEntries
          null, //forUser
          null, //discs
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(targets, SSStrU.comma), SSConf.sssUri), //targets
          true, //withUserRestriction
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
      final SSDiscClientI discServ = (SSDiscClientI) SSServReg.getClientServ(SSDiscClientI.class);
      
      return Response.status(200).entity(discServ.discsGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered/targets/{targets}")
  @ApiOperation(
    value = "retrieve discussions for certain entities, i.e., targets",
    response = SSDiscsGetRet.class)
  public Response discsForTargetsFilteredGet(
    @Context
    final HttpHeaders  headers,
    
    @PathParam (SSVarNames.targets)
    final String targets,
    
    final SSDiscsForTargetsFilteredGetRESTAPIV2Par input){
    
    final SSDiscsGetPar par;
    
    try{
      
      par =
        new SSDiscsGetPar(
          null, //user
          true, //setEntries
          input.forUser, //forUser
          null, //discs
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(targets, SSStrU.comma), SSConf.sssUri), //targets
          true, //withUserRestriction
          true); //invokeEntityHandlers
      
      par.setCircleTypes      = input.setCircleTypes;
      par.setComments         = input.setComments;
      par.setLikes            = input.setLikes;
      par.setTags             = input.setTags;
      par.setAttachedEntities = input.setAttachedEntities;
      par.setReads            = input.setReads;
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSDiscClientI discServ = (SSDiscClientI) SSServReg.getClientServ(SSDiscClientI.class);
      
      return Response.status(200).entity(discServ.discsGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/entry/{entry}/accept")
  @ApiOperation(
    value = "accept a qa answer",
    response = SSDiscEntryAcceptRet.class)
  public Response discEntryAccept(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.entry)
    final String entry){
    
    final SSDiscEntryAcceptPar par;
    
    try{
      
      par =
        new SSDiscEntryAcceptPar(
          null, //user
          SSUri.get(entry, SSConf.sssUri), //entry
          true, //withUserRestriction,
          true); //shouldComit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSDiscClientI discServ = (SSDiscClientI) SSServReg.getClientServ(SSDiscClientI.class);
      
      return Response.status(200).entity(discServ.discEntryAccept(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{disc}/targets/{targets}")
  @ApiOperation(
    value = "add targets to a discussion",
    response = SSDiscTargetsAddRet.class)
  public Response discTargetsAdd(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.disc)
    final String disc,
    
    @PathParam (SSVarNames.targets)
    final String targets){
    
    final SSDiscTargetsAddPar par;
    
    try{
      
      par =
        new SSDiscTargetsAddPar(
          null, //user,
          SSUri.get(disc, SSConf.sssUri),
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(targets, SSStrU.comma), SSConf.sssUri),
          true, //withUserRestriction,
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
      final SSDiscClientI discServ = (SSDiscClientI) SSServReg.getClientServ(SSDiscClientI.class);
      
      return Response.status(200).entity(discServ.discTargetsAdd(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
}