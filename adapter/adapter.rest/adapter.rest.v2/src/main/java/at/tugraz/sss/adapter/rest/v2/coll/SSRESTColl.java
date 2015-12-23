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
package at.tugraz.sss.adapter.rest.v2.coll;

import at.tugraz.sss.conf.SSConf;
import at.kc.tugraz.ss.service.coll.api.SSCollClientI;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollCumulatedTagsGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesDeletePar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserHierarchyGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserEntityIsInGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserCumulatedTagsGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesDeleteRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserHierarchyGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserRootGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserEntityIsInGetRet;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.tugraz.sss.serv.conf.api.*;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.impl.api.*;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
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

@Path("/colls")
@Api( value = "/colls")
public class SSRESTColl extends SSServImplStartA{
  
  public SSRESTColl() {
    super(null);
  }
  
  public SSRESTColl(final SSConfA conf) {
    super(conf);
  }
  
  @Override
  protected void finalizeImpl() throws Exception{
    destroy();
  }

  @PostConstruct
  public void createRESTResource(){
  }
  
  @PreDestroy
  public void destroyRESTResource(){
    try{
      finalizeImpl();
    }catch(Exception error2){
      SSLogU.err(error2);
    }
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "retrieve a user's collection with entries",
    response = SSCollGetRet.class)
  @Path("/{coll}")
  public Response collGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.coll)
    final String coll){
    
    final SSCollGetPar par;
    
    try{
      
      par =
        new SSCollGetPar(
          null,  //user
          SSUri.get(coll, SSConf.sssUri), //entity
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
      final SSCollClientI collServ = (SSCollClientI) SSServReg.getClientServ(SSCollClientI.class);
      
      return Response.status(200).entity(collServ.collGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "retrieve the user's root collection",
    response = SSCollUserRootGetRet.class)
  @Path("/root")
  public Response collRootGet(
    @Context
    final HttpHeaders headers){
    
    final SSCollUserRootGetPar par;
    
    try{
      
      par =
        new SSCollUserRootGetPar(
          null,  //user
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
      final SSCollClientI collServ = (SSCollClientI) SSServReg.getClientServ(SSCollClientI.class);
      
      return Response.status(200).entity(collServ.collRootGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "retrieve the parent collection order for a user's collection",
    response = SSCollUserHierarchyGetRet.class)
  @Path("/{coll}/hierarchy")
  public Response collHierarchyGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.coll)
    final String coll){
    
    final SSCollUserHierarchyGetPar par;
    
    try{
      
      par =
        new SSCollUserHierarchyGetPar(
          null,  //user,
          SSUri.get(coll, SSConf.sssUri),
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
      final SSCollClientI collServ = (SSCollClientI) SSServReg.getClientServ(SSCollClientI.class);
      
      return Response.status(200).entity(collServ.collHierarchyGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "add a (new) collection or any other entity to given user's collection",
    response = SSCollUserEntryAddRet.class)
  @Path("/{coll}")
  public Response collEntryAdd(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.coll)
    final String coll,
    
    final SSCollEntryAddRESTAPIV2Par input){
    
    final SSCollUserEntryAddPar par;
    
    try{
      
      par =
        new SSCollUserEntryAddPar(
          null,  //user,
          SSUri.get(coll, SSConf.sssUri),
          input.entry,
          input.label,
          input.addNewColl,
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
      final SSCollClientI collServ = (SSCollClientI) SSServReg.getClientServ(SSCollClientI.class);
      
      return Response.status(200).entity(collServ.collEntryAdd(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "add existing collections or (new) entities to a collection",
    response = SSCollUserEntriesAddRet.class)
  @Path("/{coll}/entries")
  public Response collEntriesAdd(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.coll)
    final String coll,
    
    final SSCollEntriesAddRESTAPIV2Par input){
    
    final SSCollUserEntriesAddPar par;
    
    try{
      
      par =
        new SSCollUserEntriesAddPar(
          null,  //user,
          SSUri.get(coll, SSConf.sssUri),
          input.entries,
          input.labels,
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
      final SSCollClientI collServ = (SSCollClientI) SSServReg.getClientServ(SSCollClientI.class);
      
      return Response.status(200).entity(collServ.collEntriesAdd(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "retrieve all the user's collections given entity is in",
    response = SSCollsUserEntityIsInGetRet.class)
  @Path("/entries/{entity}/colls")
  public Response collsEntityIsInGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.entity)
    final String entity){
    
    final SSCollsUserEntityIsInGetPar par;
    
    try{
      
      par =
        new SSCollsUserEntityIsInGetPar(
          null,  //user,
          SSUri.get(entity, SSConf.sssUri),
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
      final SSCollClientI collServ = (SSCollClientI) SSServReg.getClientServ(SSCollClientI.class);
      
      return Response.status(200).entity(collServ.collsEntityIsInGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "retrieve the cumulated tags (and their frequencies) for all the sub collections and respective entities",
    response = SSCollUserCumulatedTagsGetRet.class)
  @Path("/{coll}/tags/cumulated")
  public Response collCumulatedTagsGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.coll)
    final String coll){
    
    final SSCollCumulatedTagsGetPar par;
    
    try{
      
      par =
        new SSCollCumulatedTagsGetPar(
          null,  //user,
          SSUri.get(coll, SSConf.sssUri),
          true); //withUserRestriction
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSCollClientI collServ = (SSCollClientI) SSServReg.getClientServ(SSCollClientI.class);
      
      return Response.status(200).entity(collServ.collCumulatedTagsGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "delete one or more entries from a collection",
    response = SSCollUserEntriesDeleteRet.class)
  @Path("/{coll}/entries/{entries}")
  public Response collEntriesDelete(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.coll)
    final String coll,
    
    @PathParam(SSVarNames.entries)
    final String entries){
    
    final SSCollUserEntriesDeletePar par;
    
    try{
      
      par =
        new SSCollUserEntriesDeletePar(
          null,  //user,
          SSUri.get(coll, SSConf.sssUri),
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(entries, SSStrU.comma), SSConf.sssUri),
          true, //withUserRestriction
          true); // shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSCollClientI collServ = (SSCollClientI) SSServReg.getClientServ(SSCollClientI.class);
      
      return Response.status(200).entity(collServ.collEntriesDelete(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
  }
}