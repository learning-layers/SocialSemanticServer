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
package at.kc.tugraz.ss.adapter.rest.v2.pars.entity;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.like.datatypes.par.SSLikeUserSetPar;
import at.kc.tugraz.ss.like.datatypes.ret.SSLikeUserSetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserGetRet;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
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

@Path("/entities")
@Api( value = "/entities") //, basePath = "/entities"
public class SSRESTEntities {

  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{entity}")
  @ApiOperation(
    value = "retrieve entity information for given ID or encoded URI",
    response = SSEntityUserGetRet.class)
  public Response entityGet(
    @Context
      HttpHeaders headers,
    @PathParam (SSVarU.entity)
      String entity){
    
    final SSEntityUserGetPar par;
    
    try{
      
      par =
        new SSEntityUserGetPar(
          SSMethU.entityGet,
          null,
          null,
          SSUri.get(entity, SSVocConf.sssUri),
          null);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{entity}/likes")
  @ApiOperation(
    value = "like or dislike an entity",
    response = SSLikeUserSetRet.class)
  public Response likeSetPost(
    @Context HttpHeaders               headers,
    @PathParam(SSVarU.entity) String   entity,
    final SSLikeSetRESTAPIV2Par        input){
    
    final SSLikeUserSetPar par;
    
    try{
      par =
        new SSLikeUserSetPar(
          SSMethU.likeSet,
          null,
          null,
          SSUri.get(entity, SSVocConf.sssUri),
          input.value);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  //  @GET
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path("/tags/{user}")
//  @ApiOperation(
//    value = "retrieve tag assignments for user",
//    response = SSTagsUserGetRet.class)
//  public Response tagsForUserGet(
//    @Context 
//      HttpHeaders headers,
//    @PathParam(SSVarU.user)
//      String   user){
//    
//    final SSTagsUserGetPar par;
//    
//    try{
//      
//      par =
//        new SSTagsUserGetPar(
//          SSMethU.tagsGet,
//          null,
//          null,
//          SSUri.get(user, SSVocConf.sssUri),
//          null,
//          null,
//          null,
//          null);
//      
//    }catch(Exception error){
//      return Response.status(422).build();
//    }
//    
//    return SSRestMainV2.handleRequest(headers, par);
//  }
  
  //  @GET
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    ("/{user}/tags/frequs")
//  @ApiOperation(
//    value = "retrieve tag frequencies",
//    response = SSTagUserFrequsGetRet.class)
//  public Response tagFrequsForUserGet(
//    @Context
//      HttpHeaders headers, 
//    @PathParam(SSVarU.user)
//      String user){
//    
//    final SSTagUserFrequsGetPar par;
//    
//    try{
//      par =
//        new SSTagUserFrequsGetPar(
//          SSMethU.tagFrequsGet,
//          null,
//          null,
//          SSUri.get(user, SSVocConf.sssUri),
//          null,
//          null,
//          null,
//          null,
//          false);
//      
//    }catch(Exception error){
//      return Response.status(422).build();
//    }
//    
//    return SSRestMainV2.handleRequest(headers, par);
//  }
}