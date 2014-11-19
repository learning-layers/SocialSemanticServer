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
package at.kc.tugraz.ss.adapter.rest;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserAddPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserAnnotationAddPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideosUserGetPar;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideoUserAddRet;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideoUserAnnotationAddRet;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideosUserGetRet;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Api( value = "video", description = "SSS REST API for videos" )
public class SSRESTVideo{
 
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash)
  @ApiOperation(
    value = "retrieve videos",
    response = SSVideosUserGetRet.class)
  public Response videosGet(
    @Context
      HttpHeaders headers){
//    @ApiParam(
//      required = false,
//      value = "entity to get videos for")
//    @PathParam ("forUser") String forUser,
//    @ApiParam(
//      required = false,
//      value = "user to get videos for")
//    @PathParam ("forEntity") String forEntity
    
    try{
      
      return SSRestMain.handleGETRequest(
        headers,
        new SSVideosUserGetPar(
          SSMethU.videosGet,
          null,
          null,
          null));
      
    }catch(Exception error){
      return Response.status(422).build();
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash)
  @ApiOperation(
    value = "add a video",
    response = SSVideoUserAddRet.class)
  public Response videoAdd(
    @Context HttpHeaders     headers,
    final SSVideoUserAddPar  input){
    
    return SSRestMain.handlePOSTRequest(headers, input, SSMethU.videoAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "annotation")
  @ApiOperation(
    value = "add an annotation to a video",
    response = SSVideoUserAnnotationAddRet.class)
  public Response videoAnnotationAdd(
    @Context HttpHeaders              headers,
    final SSVideoUserAnnotationAddPar input){
    
    return SSRestMain.handlePOSTRequest(headers, input, SSMethU.videoAnnotationAdd);
  }
}