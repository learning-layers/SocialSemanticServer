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
package at.kc.tugraz.ss.adapter.rest.v2.pars.disc;

import at.kc.tugraz.ss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserDiscURIsForTargetGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserEntryAddPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsUserAllGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserDiscURIsForTargetGetRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserEntryAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserWithEntriesRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscsUserAllGetRet;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSVarU;
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

@Path("/discs")
@Api( value = "/discs")
public class SSRESTDiscs{
 
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("")
  @ApiOperation(
    value = "retrieve all discussions",
    response = SSDiscsUserAllGetRet.class)
  public Response discsGet(
    @Context 
      final HttpHeaders headers){
    
    final SSDiscsUserAllGetPar par;
    
    try{
      
      par =
        new SSDiscsUserAllGetPar(
          SSServOpE.discsAllGet,
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
    value = "add a textual comment/answer/opinion to a discussion [for given entity] or create a new discussion",
    response = SSDiscUserEntryAddRet.class)
  public Response circleCreate(
    @Context 
      final HttpHeaders headers,
    
    final SSDiscEntryAddRESTAPIV2Par input){
    
    final SSDiscUserEntryAddPar par;
    
    try{
      
      par =
        new SSDiscUserEntryAddPar(
          SSServOpE.discEntryAdd,
          null,
          null,
          input.disc, //disc
          input.entity, //entity, 
          input.entry, //entry
          input.addNewDisc, //addNewDisc
          input.type, //type
          input.label, //label
          input.description, //description
          input.users, //users
          input.circles, //circles
          input.entities, //entities
          true); //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{disc}")
  @ApiOperation(
    value = "retrieve a discussion with its entries",
    response = SSDiscUserWithEntriesRet.class)
  public Response circleGet(
    @Context                    
      final HttpHeaders  headers,
    
    @PathParam (SSVarU.disc)  
      final String disc, 
    
    final SSDiscGetRESTAPIV2Par input){
    
    final SSDiscUserWithEntriesGetPar par;
    
    try{
      
      par =
        new SSDiscUserWithEntriesGetPar(
          SSServOpE.discWithEntriesGet, //op
          null, //key
          null, //user
          SSUri.get(disc, SSVocConf.sssUri), //disc
          10, //maxEntries
          input.includeComments); //includeComments
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/entities/{entity}")
  @ApiOperation(
    value = "retrieve discussions for a certain entity",
    response = SSDiscUserDiscURIsForTargetGetRet.class)
  public Response discURIsForTargetGet(
    @Context                    
      final HttpHeaders  headers,
    
    @PathParam (SSVarU.entity)  
      final String entity){
    
    final SSDiscUserDiscURIsForTargetGetPar par;
    
    try{
      
      par =
        new SSDiscUserDiscURIsForTargetGetPar(
          SSServOpE.discUserDiscURIsForTargetGet, //op
          null, //key
          null, //user
          SSUri.get(entity, SSVocConf.sssUri)); //entity
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}