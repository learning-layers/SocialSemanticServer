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
package at.tugraz.sss.adapter.rest.v2.activity;

import at.kc.tugraz.ss.activity.api.*;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivitiesGetPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityTypesGetPar;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivitiesGetRet;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivityTypesGetRet;
import at.tugraz.sss.adapter.rest.v2.*;
import at.tugraz.sss.serv.conf.api.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.impl.api.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.annotation.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/activities")
@Api( value = "/activities")
public class SSRESTActivity extends SSServImplStartA{
  
  public SSRESTActivity() {
    super(null);
  }
  
  public SSRESTActivity(final SSConfA conf) {
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
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered")
  @ApiOperation(
    value = "retrieve activities",
    response = SSActivitiesGetRet.class)
  public Response activitiesGetFiltered(
    @Context
    final HttpHeaders headers,
    
    final SSActivitiesGetRESTAPIV2Par input){
    
    final SSActivitiesGetPar par;
    
    try{
      
      par =
        new SSActivitiesGetPar(
          null,
          null, //activities
          input.types,
          input.users,
          input.entities,
          input.circles,
          input.startTime,
          input.endTime,
          input.includeOnlyLastActivities,
          true, //withUserRestriction
          true); //invokeEntityHandlers
      
    }catch(Exception error){
      return Response.status(422).build();
    }
//    System.out.println("activity end " + new Date().getTime());
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSActivityClientI activityServ = (SSActivityClientI) SSServReg.getClientServ(SSActivityClientI.class);
      
      return Response.status(200).entity(activityServ.activitiesGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/types")
  @ApiOperation(
    value = "retrieve available activity types",
    response = SSActivityTypesGetRet.class)
  public Response activityTypesGet(
    @Context
    final HttpHeaders headers){
    
    final SSActivityTypesGetPar par;
    
    try{
      
      par =
        new SSActivityTypesGetPar(
          null);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSActivityClientI activityServ = (SSActivityClientI) SSServReg.getClientServ(SSActivityClientI.class);
      
      return Response.status(200).entity(activityServ.activityTypesGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("")
  @ApiOperation(
    value = "add an activity",
    response = SSActivitiesGetRet.class)
  public Response activityAdd(
    @Context
    final HttpHeaders headers,
    
    final SSActivityAddRESTAPIV2Par input){
    
    final SSActivityAddPar par;
    
    try{
      
      par =
        new SSActivityAddPar(
          null,
          input.type,
          input.entity,
          input.users,
          input.entities,
          input.comments,
          input.creationTime,
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
      final SSActivityClientI activityServ = (SSActivityClientI) SSServReg.getClientServ(SSActivityClientI.class);
      
      return Response.status(200).entity(activityServ.activityAdd(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
}