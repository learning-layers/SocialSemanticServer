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
package at.tugraz.sss.adapter.rest.v2.video;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/videos")
@Api( value = "/videos") //, basePath = "/videos"
public class SSRESTVideo{
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("")
  @ApiOperation(
    value = "retrieve videos",
    response = SSVideosUserGetRet.class)
  public Response videosGet(
    @Context HttpHeaders headers){
    
    final SSVideosUserGetPar par;
    
    try{
      
      par =
        new SSVideosUserGetPar(
          SSServOpE.videosGet,
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
    value = "add a video",
    response = SSVideoUserAddRet.class)
  public Response videoAdd(
    @Context HttpHeaders         headers,
    final SSVideoAddRESTAPIV2Par input){
    
    final SSVideoUserAddPar par;
    
    try{
      
      par =
        new SSVideoUserAddPar(
          SSServOpE.videoAdd,
          null, 
          null,
          input.uuid,
          input.link,
          input.forEntity, 
          input.genre,
          input.label, 
          input.description, 
          input.creationTime, 
          input.latitude,
          input.longitude,
          input.accuracy);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{video}/annotations")
  @ApiOperation(
    value = "add an annotation to a video",
    response = SSVideoUserAnnotationAddRet.class)
  public Response videoAnnotationAddPost(
    @Context 
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.video) 
      final String video,
    
    final SSVideoAnnotationAddRESTAPIV2Par input){
    
    final SSVideoUserAnnotationAddPar par;
    
    try{
      
      par =
        new SSVideoUserAnnotationAddPar(
          SSServOpE.videoAnnotationAdd,
          null,
          null,
          SSUri.get(video, SSVocConf.sssUri),
          input.timePoint,
          input.x,
          input.y,
          input.label,
          input.description);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}
