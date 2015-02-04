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
package at.kc.tugraz.ss.adapter.rest.v2;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSAppAddRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSAppStackLayoutCreateRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSAppStackLayoutUpdateRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSCircleCreateRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSCircleEntitiesAddRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSCircleUsersAddRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSFriendAddRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSLikeSetRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSTagAddRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSTagEditRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSTagEntitiesForTagsGetRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSTagFrequsGetRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSTagsGetRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSTagsRemoveRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSVideoAddRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSVideoAnnotationAddRESTAPIV2Par;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreatePar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.friend.datatypes.par.SSFriendUserAddPar;
import at.kc.tugraz.ss.friend.datatypes.par.SSFriendsUserGetPar;
import at.kc.tugraz.ss.friend.datatypes.ret.SSFriendUserAddRet;
import at.kc.tugraz.ss.friend.datatypes.ret.SSFriendsUserGetRet;
import at.kc.tugraz.ss.like.datatypes.par.SSLikeUserSetPar;
import at.kc.tugraz.ss.like.datatypes.ret.SSLikeUserSetRet;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserGetPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleCreateRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCirclesGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesAddRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersAddRet;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagAddPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagUserEditPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagUserEntitiesForTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagUserFrequsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsUserGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsUserRemovePar;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagAddRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagUserEditRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagUserEntitiesForTagsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagUserFrequsGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsUserGetRet;
import at.kc.tugraz.ss.service.tag.datatypes.ret.SSTagsUserRemoveRet;
import at.kc.tugraz.sss.app.datatypes.par.SSAppAddPar;
import at.kc.tugraz.sss.app.datatypes.par.SSAppsGetPar;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppAddRet;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppsGetRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutCreatePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutDeletePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutUpdatePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutsGetPar;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutCreateRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutDeleteRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutUpdateRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutsGetRet;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserAddPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserAnnotationAddPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideosUserGetPar;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideoUserAddRet;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideoUserAnnotationAddRet;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideosUserGetRet;
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

@Path("")
@Api( value = "/entities", basePath = "/entities")
public class SSRESTEntities {

  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "retrieve information on entities the user can access",
    response = SSCircleEntitiesGetRet.class)
  @Path("/entities")
  public Response entitiesGet(
    @Context
      HttpHeaders headers){
    
    final SSCircleEntitiesGetPar par;
    
    try{
      
      par =
        new SSCircleEntitiesGetPar(
          SSMethU.circleEntitiesGet,
          null,
          null,
          null, 
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleGETRequest(headers, par);
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/circles")
  @ApiOperation(
    value = "retrieve circles the user can access",
    response = SSCircleEntitiesGetRet.class)
  public Response circlesGet(
    @Context HttpHeaders headers){
    
    final SSCirclesGetPar par;
    
    try{
      
      par =
        new SSCirclesGetPar(
          SSMethU.circlesGet,
          null,
          null,
          null,
          null,
          false,
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleGETRequest(headers, par);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/circles")
  @ApiOperation(
    value = "create a circle and add users and entities to",
    response = SSCircleCreateRet.class)
  public Response circleAddPost(
    @Context HttpHeaders              headers,
    final SSCircleCreateRESTAPIV2Par input){
    
    final SSCircleCreatePar par;
    
    try{
      
      par =
        new SSCircleCreatePar(
          SSMethU.circleCreate,
          null,
          null,
          input.label,
          input.entities,
          input.users,
          input.description,
          false, 
          true,
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/apps")
  @ApiOperation(
    value = "retrieve apps",
    response = SSAppsGetRet.class)
  public Response appsGet(
    @Context HttpHeaders       headers){
    
    final SSAppsGetPar par;
    
    try{
      
      par =
        new SSAppsGetPar(
          SSMethU.appsGet,
          null, 
          null);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleGETRequest(headers, par);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/apps")
  @ApiOperation(
    value = "add an app",
    response = SSAppAddRet.class)
  public Response appAddPost(
    @Context HttpHeaders       headers,
    final SSAppAddRESTAPIV2Par input){
    
    final SSAppAddPar par;
    
    try{
      
      par =
        new SSAppAddPar(
          SSMethU.appAdd,
          null,
          null,
          input.label,
          input.descriptionShort,
          input.descriptionFunctional,
          input.descriptionTechnical,
          input.descriptionInstall,
          input.downloads,
          input.downloadIOS,
          input.downloadAndroid,
          input.fork,
          input.screenShots,
          input.videos);
      
       }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/friends")
  @ApiOperation(
    value = "get friends",
    response = SSFriendsUserGetRet.class)
  public Response friendsGet(
    @Context HttpHeaders          headers){
    
    final SSFriendsUserGetPar par;
    
    try{
      
      par =
        new SSFriendsUserGetPar(
          SSMethU.friendsGet,
          null,
          null);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleGETRequest(headers, par);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/friends")
  @ApiOperation(
    value = "add a friend",
    response = SSFriendUserAddRet.class)
  public Response friendAddPost(
    @Context HttpHeaders          headers,
    final SSFriendAddRESTAPIV2Par input){
    
    final SSFriendUserAddPar par;
    
    try{
      par =
        new SSFriendUserAddPar(
          SSMethU.friendAdd,
          null,
          null,
          input.friend);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/videos")
  @ApiOperation(
    value = "retrieve videos",
    response = SSVideosUserGetRet.class)
  public Response videosGet(
    @Context HttpHeaders headers){
    
    final SSVideosUserGetPar par;
    
    try{
      
      par =
        new SSVideosUserGetPar(
          SSMethU.videosGet,
          null,
          null,
          null,
          null);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleGETRequest(headers, par);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/videos")
  @ApiOperation(
    value = "add a video",
    response = SSVideoUserAddRet.class)
  public Response videoAddPost(
    @Context HttpHeaders         headers,
    final SSVideoAddRESTAPIV2Par input){
    
    final SSVideoUserAddPar par;
    
    try{
      
      par =
        new SSVideoUserAddPar(
          SSMethU.videoAdd,
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
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
  
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
    
    return SSRestMainV2.handleGETRequest(headers, par);
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
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{video}/videos/annotations")
  @ApiOperation(
    value = "add an annotation to a video",
    response = SSVideoUserAnnotationAddRet.class)
  public Response videoAnnotationAddPost(
    @Context HttpHeaders                   headers,
    @PathParam(SSVarU.video) String        video,
    final SSVideoAnnotationAddRESTAPIV2Par input){
    
    final SSVideoUserAnnotationAddPar par;
    
    try{
      
      par = 
        new SSVideoUserAnnotationAddPar(
          SSMethU.videoAnnotationAdd, 
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
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{user}/circles")
  @ApiOperation(
    value = "retrieve circles for a given user",
    response = SSCirclesGetRet.class)
  public Response circlesForUserGet(
    @Context HttpHeaders headers,
    @PathParam (SSVarU.user)
      String user){
    
    final SSCirclesGetPar par;
    
    try{
      
      par =
        new SSCirclesGetPar(
          SSMethU.circlesGet,
          null,
          null,
          SSUri.get(user, SSVocConf.sssUri),
          null,
          false,
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleGETRequest(headers, par);
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{user}/circles/{circle}")
  @ApiOperation(
    value = "retrieve a circle for a given user",
    response = SSCirclesGetRet.class)
  public Response circleForUserGet(
    @Context HttpHeaders headers,
    @PathParam (SSVarU.user)
      String user,
     @PathParam (SSVarU.circle)
      String circle){
    
    final SSCircleGetPar par;
    
    try{
      
      par =
        new SSCircleGetPar(
          SSMethU.circleGet,
          null,
          null,
          SSUri.get(user,   SSVocConf.sssUri),
          SSUri.get(circle, SSVocConf.sssUri),
          false,
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleGETRequest(headers, par);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{circle}/circles/users")
  @ApiOperation(
    value = "add given users to a user-generated circle",
    response = SSCircleUsersAddRet.class)
  public Response circleUsersAddPost(
    @Context HttpHeaders                       headers,
    @PathParam(SSVarU.circle) String           circle,
    final SSCircleUsersAddRESTAPIV2Par input){
    
    final SSCircleUsersAddPar par;
    
    try{
      par =
        new SSCircleUsersAddPar(
          SSMethU.circleUsersAdd,
          null,
          null,
          SSUri.get(circle, SSVocConf.sssUri),
          input.users, 
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{circle}/circles/entities")
  @ApiOperation(
    value = "add given entities to a user-generated circle",
    response = SSCircleEntitiesAddRet.class)
  public Response circleEntitiesAddPost(
    @Context HttpHeaders                          headers,
    @PathParam(SSVarU.circle) String              circle,
    final SSCircleEntitiesAddRESTAPIV2Par input){
    
    final SSCircleEntitiesAddPar par;
    
    try{
      
      par =
        new SSCircleEntitiesAddPar(
          SSMethU.circleEntitiesAdd,
          null,
          null,
          SSUri.get(circle, SSVocConf.sssUri),
          input.entities, 
          true,
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/appStackLayouts")
  @ApiOperation(
    value = "retrieve appStackLayouts",
    response = SSAppStackLayoutsGetRet.class)
  public Response appStackLayoutsGet(
    @Context HttpHeaders                     headers){
    
    final SSAppStackLayoutsGetPar par;
    
    try{
      par =
        new SSAppStackLayoutsGetPar(
          SSMethU.appStackLayoutsGet,
          null,
          null);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleGETRequest(headers, par);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/appStackLayouts")
  @ApiOperation(
    value = "create an arrangement of tiles within an app",
    response = SSAppStackLayoutCreateRet.class)
  public Response appStackLayoutCreatePost(
    @Context HttpHeaders                     headers,
    final SSAppStackLayoutCreateRESTAPIV2Par input){
    
    final SSAppStackLayoutCreatePar par;
    
    try{
      par =
        new SSAppStackLayoutCreatePar(
          SSMethU.appStackLayoutCreate,
          null,
          null,
          input.uuid,
          input.app,
          input.label,
          input.description);
    
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/appStackLayouts/{stack}")
  @ApiOperation(
    value = "delete an arrangement of tiles within an app",
    response = SSAppStackLayoutDeleteRet.class)
  public Response appStackLayoutDelete(
    @Context HttpHeaders              headers,
    @PathParam(SSVarU.stack) String   stack){
    
    final SSAppStackLayoutDeletePar par;
    
    try{
      par =
        new SSAppStackLayoutDeletePar(
          SSMethU.appStackLayoutDelete,
          null,
          null,
          SSUri.get(stack, SSVocConf.sssUri));
    
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleDELETERequest(headers, par);
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/appStackLayouts/{stack}")
  @ApiOperation(
    value = "update an arrangement of tiles within an app",
    response = SSAppStackLayoutUpdateRet.class)
  public Response appStackLayoutUpdate(
    @Context HttpHeaders                     headers,
    @PathParam(SSVarU.stack) String          stack,
    final SSAppStackLayoutUpdateRESTAPIV2Par input){
    
    final SSAppStackLayoutUpdatePar par;
    
    try{
      par =
        new SSAppStackLayoutUpdatePar(
          SSMethU.appStackLayoutUpdate,
          null,
          null,
          SSUri.get(stack, SSVocConf.sssUri),
          input.app, 
          input.label,
          input.description);
    
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handlePUTRequest(headers, par);
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/tags")
  @ApiOperation(
    value = "retrieve tag assignments",
    response = SSTagsUserGetRet.class)
  public Response tagsGet(
    @Context HttpHeaders headers){
    
    final SSTagsUserGetPar par;
    
    try{
      
      par =
        new SSTagsUserGetPar(
          SSMethU.tagsGet,
          null,
          null,
          null,
          null,
          null,
          null,
          null);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleGETRequest(headers, par);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/tags")
  @ApiOperation(
    value = "retrieve tag assignments",
    response = SSTagsUserGetRet.class)
  public Response tagsGetPost(
    @Context HttpHeaders headers,
    final SSTagsGetRESTAPIV2Par input){
    
    final SSTagsUserGetPar par;
    
    try{
      par =
        new SSTagsUserGetPar(
          SSMethU.tagsGet,
          null,
          null,
          input.forUser,
          input.entities,
          input.labels,
          input.space,
          input.startTime);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/tags/frequs")
  @ApiOperation(
    value = "retrieve tag frequencies",
    response = SSTagUserFrequsGetRet.class)
  public Response tagFrequsGet(
    @Context
      HttpHeaders headers){
    
    final SSTagUserFrequsGetPar par;
    
    try{
      par =
        new SSTagUserFrequsGetPar(
          SSMethU.tagFrequsGet,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          false);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleGETRequest(headers, par);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/tags/frequs")
  @ApiOperation(
    value = "retrieve tag frequencies",
    response = SSTagUserFrequsGetRet.class)
  public Response tagFrequsGetPost(
    @Context HttpHeaders headers,
    final SSTagFrequsGetRESTAPIV2Par input){
    
    final SSTagUserFrequsGetPar par;
    
    try{
      par =
        new SSTagUserFrequsGetPar(
          SSMethU.tagFrequsGet,
          null,
          null,
          input.forUser,
          input.entities,
          input.labels,
          input.space,
          input.startTime,
          input.useUsersEntities);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/tags/entities")
  @ApiOperation(
    value = "retrieve entities for tags (currently startTime is not used to retrieve entities)",
    response = SSTagUserEntitiesForTagsGetRet.class)
  public Response tagEntitiesGetPost(
     @Context HttpHeaders                     headers,
    final SSTagEntitiesForTagsGetRESTAPIV2Par input){
    
    final SSTagUserEntitiesForTagsGetPar par;
    
    try{
      par =
        new SSTagUserEntitiesForTagsGetPar(
          SSMethU.tagEntitiesForTagsGet,
          null,
          null,
          input.forUser,
          input.labels,
          input.space,
          input.startTime);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/tags/{entity}")
  @ApiOperation(
    value = "remove tag, user, entity, space combinations",
    response = SSTagsUserRemoveRet.class)
  public Response tagsRemoveDelete(
    @Context HttpHeaders               headers,
    @PathParam(SSVarU.entity) String   entity,
    final SSTagsRemoveRESTAPIV2Par     input){
    
    final SSTagsUserRemovePar par;
    
    try{
      par =
        new SSTagsUserRemovePar(
          SSMethU.tagsRemove,
          null,
          null,
          SSUri.get(entity, SSVocConf.sssUri),
          input.label,
          input.space);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleDELETERequest(headers, par);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/tags/{entity}")
  @ApiOperation(
    value = "add a tag for an entity within given space",
    response = SSTagAddRet.class)
  public Response tagAddPost(
    @Context HttpHeaders            headers,
    @PathParam(SSVarU.entity)String entity,
    final SSTagAddRESTAPIV2Par      input){
    
    final SSTagAddPar par;
    
    try{
      par =
        new SSTagAddPar(
          SSMethU.tagAdd,
          null,
          null,
          SSUri.get(entity, SSVocConf.sssUri),
          input.label,
          input.space,
          input.creationTime);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/tags/label/{tag}")
  @ApiOperation(
    value = "changes the label of the tag assigned to entities by given user",
    response = SSTagUserEditRet.class)
  public Response tagEditPut(
    @Context HttpHeaders            headers,
    @PathParam(SSVarU.tag) String   tag,
    final SSTagEditRESTAPIV2Par     input) throws Exception{
    
    final SSTagUserEditPar par;
    
    try{
      par =
        new SSTagUserEditPar(
          SSMethU.tagEdit,
          null,
          null,
          SSTagLabel.get(tag),
          input.label);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handlePUTRequest(headers, par);
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
//    return SSRestMainV2.handleGETRequest(headers, par);
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
//    return SSRestMainV2.handleGETRequest(headers, par);
//  }
}