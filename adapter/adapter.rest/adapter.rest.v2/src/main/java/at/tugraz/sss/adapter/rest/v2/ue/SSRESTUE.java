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
package at.tugraz.sss.adapter.rest.v2.ue;

import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUECountGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEAddRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUECountGetRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEGetRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEsGetRet;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSVarNames;
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

@Path("/ues")
@Api(value = "/ues")
public class SSRESTUE{
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered")
  @ApiOperation(
    value = "retrieve user events for user, entity, time combination",
    response = SSUEsGetRet.class)
  public Response uEsGetFiltered(
    @Context 
      final HttpHeaders headers, 
    
    final SSUEsGetRESTAPIV2Par input){
    
    final SSUEsGetPar par;
    
    try{
      
      par =
        new SSUEsGetPar(
          SSServOpE.uEsGet,
          null,
          null,  
          input.forUser,
          input.entity, 
          input.types, 
          input.startTime,
          input.endTime);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{uE}")
  @ApiOperation(
    value = "retrieve given user event",
    response = SSUEGetRet.class)
  public Response uEGet(
    @Context 
      final HttpHeaders headers, 
    
    @PathParam(SSVarNames.uE) 
      final String uE){
    
    final SSUEGetPar par;
    
    try{
      
      par =
        new SSUEGetPar(
          SSServOpE.uEGet,
          null,
          null,  
          SSUri.get(uE, SSVocConf.sssUri));
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered/count")
  @ApiOperation(
    value = "retrieve the number of certain user events",
    response = SSUECountGetRet.class)
  public Response uECountGet(
    @Context 
      final HttpHeaders headers, 
    
    final SSUECountGetRESTAPIV2Par input){
    
    final SSUECountGetPar par;
    
    try{
      
      par =
        new SSUECountGetPar(
          SSServOpE.uECountGet,
          null,
          null,  
          input.forUser, 
          input.entity, 
          input.type, 
          input.startTime, 
          input.endTime);
      
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
    value = "adds a usage-based trace, i.e. user event, for entity, user combination",
    response = SSUEAddRet.class)
  public Response uEAdd(
    @Context 
      final HttpHeaders headers, 

    final SSUEAddRESTAPIV2Par input){
    
    final SSUEAddPar par;
    
    try{
      
      par =
        new SSUEAddPar(
          SSServOpE.uEAdd,
          null,
          null,  
          input.entity, //entity
          input.type,  //type
          input.content, //content
          null, //creationTime
          true); //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}
