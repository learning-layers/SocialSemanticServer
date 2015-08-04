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
package at.tugraz.sss.adapter.rest.v2.learnep;

import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpLockRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpLockSetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityAddPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCreatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentSetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCircleRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionEntityRemovePar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionTimelineStateGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionTimelineStateSetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionsGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpsGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpsLockHoldPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpCreateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpLockRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpLockSetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCircleAddRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionEntityAddRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCreateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCurrentGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCurrentSetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCircleUpdateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionEntityUpdateRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionCircleRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionEntityRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionTimelineStateGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionTimelineStateSetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpVersionsGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpsGetRet;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpsLockHoldRet;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSVarNames;
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

@Path("/learneps")
@Api( value = "/learneps") 
public class SSRESTLearnEp{
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("")
  @ApiOperation(
    value = "retrieve learning episodes for a user",
    response = SSLearnEpsGetRet.class)
  public Response learnEpsGet(
    @Context  
      final HttpHeaders headers){
    
    final SSLearnEpsGetPar par;
    
    try{
      
      par =
        new SSLearnEpsGetPar(
          SSServOpE.learnEpsGet,
          null, 
          null);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("{learnEp}/versions")
  @ApiOperation(
    value = "retrieve learning episode versions for given episode",
    response = SSLearnEpVersionsGetRet.class)
  public Response learnEpVersionsGet(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEp) 
      final String learnEp){
    
    final SSLearnEpVersionsGetPar par;
    
    try{
      
      par =
        new SSLearnEpVersionsGetPar(
          SSServOpE.learnEpVersionsGet,
          null, 
          null, 
          SSUri.get(learnEp, SSVocConf.sssUri));
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/versions/{learnEpVersion}")
  @ApiOperation(
    value = "retrieve given learning episode version",
    response = SSLearnEpVersionGetRet.class)
  public Response learnEpVersionGet(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpVersion) 
      final String learnEpVersion){
    
    final SSLearnEpVersionGetPar par;
    
    try{
      
      par =
        new SSLearnEpVersionGetPar(
          SSServOpE.learnEpVersionGet,
          null, 
          null, 
          SSUri.get(learnEpVersion, SSVocConf.sssUri));
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/versions/current")
  @ApiOperation(
    value = "retrieve current learning episode version",
    response = SSLearnEpVersionCurrentGetRet.class)
  public Response learnEpVersionCurrentGet(
    @Context  
      final HttpHeaders headers){
    
    final SSLearnEpVersionCurrentGetPar par;
    
    try{
      
      par =
        new SSLearnEpVersionCurrentGetPar(
          SSServOpE.learnEpVersionCurrentGet,
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
  @Path    ("/versions/current/{learnEpVersion}")
  @ApiOperation(
    value = "set current learning episode version",
    response = SSLearnEpVersionCurrentSetRet.class)
  public Response learnEpVersionCurrentSet(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpVersion) 
      final String learnEpVersion){
    
    final SSLearnEpVersionCurrentSetPar par;
    
    try{
      
      par =
        new SSLearnEpVersionCurrentSetPar(
          SSServOpE.learnEpVersionCurrentSet,
          null, 
          null, 
          SSUri.get(learnEpVersion, SSVocConf.sssUri), 
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("")
  @ApiOperation(
    value = "create learning episode",
    response = SSLearnEpCreateRet.class)
  public Response learnEpCreate(
    @Context  
      final HttpHeaders headers,
    
    final SSLearnEpCreateRESTAPIV2Par input){
    
    final SSLearnEpCreatePar par;
    
    try{
      
      par =
        new SSLearnEpCreatePar(
          SSServOpE.learnEpCreate,
          null, 
          null, 
          input.label, 
          input.description, 
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{learnEp}/versions")
  @ApiOperation(
    value = "create learning episode version",
    response = SSLearnEpVersionCreateRet.class)
  public Response learnEpVersionCreate(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEp) 
      final String learnEp){
    
    final SSLearnEpVersionCreatePar par;
    
    try{
      
      par =
        new SSLearnEpVersionCreatePar(
          SSServOpE.learnEpVersionCreate,
          null, 
          null, 
          SSUri.get(learnEp, SSVocConf.sssUri), 
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{learnEp}")
  @ApiOperation(
    value = "remove learning episode",
    response = SSLearnEpRemoveRet.class)
  public Response learnEpRemove(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEp) 
      final String learnEp){
    
    final SSLearnEpRemovePar par;
    
    try{
      
      par =
        new SSLearnEpRemovePar(
          SSServOpE.learnEpRemove,
          null, 
          null, 
          SSUri.get(learnEp, SSVocConf.sssUri), 
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
  @Path    ("/versions/{learnEpVersion}/circles")
  @ApiOperation(
    value = "add a circle to given learning episode version",
    response = SSLearnEpVersionCircleAddRet.class)
  public Response learnEpVersionCircleAdd(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpVersion) 
      final String learnEpVersion,
    
    final SSLearnEpVersionAddCircleRESTAPIV2Par input){
    
    final SSLearnEpVersionCircleAddPar par;
    
    try{
      
      par =
        new SSLearnEpVersionCircleAddPar(
          SSServOpE.learnEpVersionCircleAdd,
          null, 
          null, 
          SSUri.get(learnEpVersion, SSVocConf.sssUri),
          input.label, 
          input.xLabel, 
          input.yLabel, 
          input.xR, 
          input.yR, 
          input.xC, 
          input.yC, 
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/versions/{learnEpVersion}/entities")
  @ApiOperation(
    value = "add an entity to given learning episode version",
    response = SSLearnEpVersionEntityAddRet.class)
  public Response learnEpVersionEntityAdd(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpVersion) 
      final String learnEpVersion,
    
    final SSLearnEpVersionEntityAddRESTAPIV2Par input){
    
    final SSLearnEpVersionEntityAddPar par;
    
    try{
      
      par =
        new SSLearnEpVersionEntityAddPar(
          SSServOpE.learnEpVersionEntityAdd,
          null, 
          null, 
          SSUri.get(learnEpVersion, SSVocConf.sssUri),
          input.entity, 
          input.x, 
          input.y, 
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/circles/{learnEpCircle}")
  @ApiOperation(
    value = "update a circle",
    response = SSLearnEpVersionCircleUpdateRet.class)
  public Response learnEpVersionCircleUpdate(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpCircle) 
      final String learnEpCircle,
    
    final SSLearnEpVersionCircleUpdateRESTAPIV2Par input){
    
    final SSLearnEpVersionCircleUpdatePar par;
    
    try{
      
      par =
        new SSLearnEpVersionCircleUpdatePar(
          SSServOpE.learnEpVersionCircleUpdate,
          null, 
          null, 
          SSUri.get(learnEpCircle, SSVocConf.sssUri),
          input.label, 
          input.xLabel, 
          input.yLabel, 
          input.xR, 
          input.yR, 
          input.xC, 
          input.yC, 
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/entities/{learnEpEntity}")
  @ApiOperation(
    value = "update an entity",
    response = SSLearnEpVersionEntityUpdateRet.class)
  public Response learnEpVersionEntityUpdate(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpEntity) 
      final String learnEpEntity,
    
    final SSLearnEpVersionEntityUpdateRESTAPIV2Par input){
    
    final SSLearnEpVersionEntityUpdatePar par;
    
    try{
      
      par =
        new SSLearnEpVersionEntityUpdatePar(
          SSServOpE.learnEpVersionEntityUpdate,
          null, 
          null, 
          SSUri.get(learnEpEntity, SSVocConf.sssUri),
          input.entity, 
          input.x, 
          input.y, 
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/circles/{learnEpCircle}")
  @ApiOperation(
    value = "remove a circle",
    response = SSLearnEpVersionCircleRemoveRet.class)
  public Response learnEpVersionCircleRemove(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpCircle) 
      final String learnEpCircle){
    
    final SSLearnEpVersionCircleRemovePar par;
    
    try{
      
      par =
        new SSLearnEpVersionCircleRemovePar(
          SSServOpE.learnEpVersionCircleRemove,
          null, 
          null, 
          SSUri.get(learnEpCircle, SSVocConf.sssUri),
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/entities/{learnEpEntity}")
  @ApiOperation(
    value = "remove an entity",
    response = SSLearnEpVersionEntityRemoveRet.class)
  public Response learnEpVersionEntityRemove(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpEntity) 
      final String learnEpEntity){
    
    final SSLearnEpVersionEntityRemovePar par;
    
    try{
      
      par =
        new SSLearnEpVersionEntityRemovePar(
          SSServOpE.learnEpVersionEntityRemove,
          null, 
          null, 
          SSUri.get(learnEpEntity, SSVocConf.sssUri),
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/versions/{learnEpVersion}/timeline/state")
  @ApiOperation(
    value = "set timeline state for learning episode version",
    response = SSLearnEpVersionTimelineStateSetRet.class)
  public Response learnEpVersionTimelineStateSet(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpVersion) 
      final String learnEpVersion, 
    
    final SSLearnEpVersionTimelineStateSetRESTAPIV2Par input){
    
    final SSLearnEpVersionTimelineStateSetPar par;
    
    try{
      
      par =
        new SSLearnEpVersionTimelineStateSetPar(
          SSServOpE.learnEpVersionTimelineStateSet,
          null, 
          null, 
          SSUri.get(learnEpVersion, SSVocConf.sssUri),
          input.startTime, 
          input.endTime,
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/versions/{learnEpVersion}/timeline/state")
  @ApiOperation(
    value = "get timeline state for learning episode version",
    response = SSLearnEpVersionTimelineStateGetRet.class)
  public Response learnEpVersionTimelineStateGet(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpVersion) 
      final String learnEpVersion){
    
    final SSLearnEpVersionTimelineStateGetPar par;
    
    try{
      
      par =
        new SSLearnEpVersionTimelineStateGetPar(
          SSServOpE.learnEpVersionTimelineStateGet,
          null, 
          null, 
          SSUri.get(learnEpVersion, SSVocConf.sssUri));
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("{learnEps}/locks")
  @ApiOperation(
    value = "get lock information on given learning episodes",
    response = SSLearnEpsLockHoldRet.class)
  public Response learnEpsLockHold(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEps) 
      final String learnEps){
    
    final SSLearnEpsLockHoldPar par;
    
    try{
      
      par =
        new SSLearnEpsLockHoldPar(
          SSServOpE.learnEpsLockHold,
          null, 
          null, 
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(learnEps, SSStrU.comma), SSVocConf.sssUri));
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("{learnEp}/locks")
  @ApiOperation(
    value = "set lock on given learning episode",
    response = SSLearnEpLockSetRet.class)
  public Response learnEpLockSet(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEp) 
      final String learnEp){
    
    final SSLearnEpLockSetPar par;
    
    try{
      
      par =
        new SSLearnEpLockSetPar(
          SSServOpE.learnEpLockSet,
          null, 
          null,
          null, //forUser
          SSUri.get(learnEp, SSVocConf.sssUri), 
          true, 
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("{learnEp}/locks")
  @ApiOperation(
    value = "remove lock on given learning episode",
    response = SSLearnEpLockRemoveRet.class)
  public Response learnEpLockRemove(
    @Context  
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEp) 
      final String learnEp){
    
    final SSLearnEpLockRemovePar par;
    
    try{
      
      par =
        new SSLearnEpLockRemovePar(
          SSServOpE.learnEpLockRemove,
          null, 
          null,
          null, //forUser
          SSUri.get(learnEp, SSVocConf.sssUri), 
          true, 
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}