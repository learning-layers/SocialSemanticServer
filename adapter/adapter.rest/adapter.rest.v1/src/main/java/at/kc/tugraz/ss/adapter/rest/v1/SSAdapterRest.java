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
package at.kc.tugraz.ss.adapter.rest.v1;

import at.tugraz.sss.serv.SSLogU;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSSearchRESTAPIV1Par;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivitiesUserGetPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityTypesGetPar;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivitiesUserGetRet;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivityTypesGetRet;
import at.kc.tugraz.ss.activity.datatypes.ret.SSActivityUserAddRet;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSAppAddRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSAppStackLayoutCreateRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSAppStackLayoutsGetRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSAppsGetRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSAuthCheckCredRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSCircleCreateRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSCircleGetRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSCirclesGetRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSCircleEntitiesAddRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSCircleEntitiesRemoveRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSEntityGetRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSCircleUsersAddRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSFriendAddRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSFriendsGetRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSLikeSetRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSSystemVersionGetPar;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSSystemVersionGetRet;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSTagAddRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSTagEditRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSTagEntitiesForTagsGetRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSTagFrequsGetRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSTagsGetRESTAPIV1Par;
import at.kc.tugraz.ss.adapter.rest.v1.par.SSTagsRemoveRESTAPIV1Par;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesPredefinedGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesUserGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesUserRemovePar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryAddPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryUserEditPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryUserEntitiesForCategoriesGetPar;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryUserFrequsGetPar;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoriesPredefinedGetRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoriesUserGetRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoriesUserRemoveRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryAddRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryUserEditRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryUserEntitiesForCategoriesGetRet;
import at.kc.tugraz.ss.category.datatypes.ret.SSCategoryUserFrequsGetRet;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreatePar;
import at.kc.tugraz.ss.friend.datatypes.par.SSFriendUserAddPar;
import at.kc.tugraz.ss.friend.datatypes.par.SSFriendsUserGetPar;
import at.kc.tugraz.ss.friend.datatypes.ret.SSFriendUserAddRet;
import at.kc.tugraz.ss.friend.datatypes.ret.SSFriendsUserGetRet;
import at.kc.tugraz.ss.like.datatypes.par.SSLikeUserSetPar;
import at.kc.tugraz.ss.like.datatypes.ret.SSLikeUserSetRet;
import at.kc.tugraz.ss.message.datatypes.par.SSMessageSendPar;
import at.kc.tugraz.ss.message.datatypes.ret.SSMessageSendRet;
import at.kc.tugraz.ss.message.datatypes.ret.SSMessagesGetRet;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommResourcesPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommTagsPar;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommResourcesRet;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommTagsRet;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityUsersGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityPublicSetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityPublicSetRet;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitySharePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleCreateRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCirclesGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesAddRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntityUsersGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntityShareRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersAddRet;
import at.kc.tugraz.ss.message.datatypes.par.SSMessagesGetPar;
import at.kc.tugraz.ss.serv.auth.conf.SSAuthConf;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescsGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCopyPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityDescGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityDescsGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserAddRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserCopyRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserDirectlyAdjoinedEntitiesRemoveRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserGetRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityUserUpdateRet;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.pars.SSModelUEEntityDetailsPar;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.rets.SSModelUEResourceDetailsRet;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthCheckCredPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.ret.SSAuthCheckCredRet;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserCumulatedTagsGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesDeletePar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryChangePosPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryDeletePar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserHierarchyGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserParentGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserWithEntriesPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserCouldSubscribeGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserEntityIsInGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserWithEntriesPar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserCumulatedTagsGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesDeleteRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryChangePosRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryDeleteRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserHierarchyGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserParentGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserRootGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserWithEntriesRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserCouldSubscribeGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserEntityIsInGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserWithEntriesRet;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserDiscURIsForTargetGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserEntryAddPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserRemovePar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsUserAllGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserDiscURIsForTargetGetRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserEntryAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserRemoveRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserWithEntriesRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscsUserAllGetRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileWritingMinutesLeftPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileWritingMinutesLeftRet;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingOverallGetPar;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingUserSetPar;
import at.kc.tugraz.ss.service.rating.datatypes.ret.SSRatingOverallGetRet;
import at.kc.tugraz.ss.service.rating.datatypes.ret.SSRatingUserSetRet;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchPar;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchRet;
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
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserAllPar;
import at.kc.tugraz.ss.service.user.datatypes.ret.SSUserAllRet;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUECountGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsGetPar;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEAddRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUECountGetRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEGetRet;
import at.kc.tugraz.ss.service.userevent.datatypes.ret.SSUEsGetRet;
import at.kc.tugraz.sss.app.datatypes.par.SSAppAddPar;
import at.kc.tugraz.sss.app.datatypes.par.SSAppsGetPar;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppAddRet;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppsGetRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutCreatePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutsGetPar;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutCreateRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutsGetRet;
import at.kc.tugraz.sss.flag.datatypes.par.SSFlagsUserGetPar;
import at.kc.tugraz.sss.flag.datatypes.par.SSFlagsUserSetPar;
import at.kc.tugraz.sss.flag.datatypes.ret.SSFlagsUserGetRet;
import at.kc.tugraz.sss.flag.datatypes.ret.SSFlagsUserSetRet;
import at.tugraz.sss.serv.SSSocketCon;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

@Path("")
@Api( value = "SSAdapterRest")
public class SSAdapterRest{
  
  /* start calls with RESTful counterpart */
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "friendAdd")
  @ApiOperation(
    value = "add a friend",
    response = SSFriendUserAddRet.class)
  public String friendAdd(
    final SSFriendAddRESTAPIV1Par input){
    
    final SSFriendUserAddPar par = 
      new SSFriendUserAddPar(
        SSServOpE.friendAdd, 
        input.key, 
        input.user, 
        input.friend);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "friendsGet")
  @ApiOperation(
    value = "add a friend",
    response = SSFriendsUserGetRet.class)
  public String friendsGet(
    final SSFriendsGetRESTAPIV1Par input){
    
    final SSFriendsUserGetPar par =
      new SSFriendsUserGetPar(
        SSServOpE.friendsGet,
        input.key,
        input.user);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "appAdd")
  @ApiOperation(
    value = "add an app",
    response = SSAppAddRet.class)
  public String appAdd(
    final SSAppAddRESTAPIV1Par input){
    
    final SSAppAddPar par =
      new SSAppAddPar(
        SSServOpE.appAdd,
        input.key,
        input.user,
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
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "appsGet")
  @ApiOperation(
    value = "retrieve apps",
    response = SSAppsGetRet.class)
  public String appsGet(
    final SSAppsGetRESTAPIV1Par input){
    
    final SSAppsGetPar par = 
      new SSAppsGetPar(
        SSServOpE.appsGet, 
        input.key, 
        input.user);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "appStackLayoutCreate")
  @ApiOperation(
    value = "create an arrangement of tiles within an app",
    response = SSAppStackLayoutCreateRet.class)
  public String appStackLayoutCreate(
    final SSAppStackLayoutCreateRESTAPIV1Par input){
    
    final SSAppStackLayoutCreatePar par =
      new SSAppStackLayoutCreatePar(
        SSServOpE.appStackLayoutCreate,
        input.key,
        input.user,
        null, 
        input.app,
        input.label,
        input.description);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "appStackLayoutsGet")
  @ApiOperation(
    value = "retrieve appStackLayouts",
    response = SSAppStackLayoutsGetRet.class)
  public String appStackLayoutsGet(
    final SSAppStackLayoutsGetRESTAPIV1Par input){
    
    final SSAppStackLayoutsGetPar par = 
      new SSAppStackLayoutsGetPar(
        SSServOpE.appStackLayoutsGet, 
        input.key, 
        input.user);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "likeSet")
  @ApiOperation(
    value = "like or dislike an entity",
    response = SSLikeUserSetRet.class)
  public String likeSet(
    final SSLikeSetRESTAPIV1Par input){
    
    final SSLikeUserSetPar par = 
      new SSLikeUserSetPar(
        SSServOpE.likeSet, 
        input.key, 
        input.user, 
        input.entity, 
        input.value);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "search")
  @ApiOperation(
    value = "search for entities",
    response = SSSearchRet.class)
  public String search(
    final SSSearchRESTAPIV1Par input){
    
    final SSSearchPar par = 
      new SSSearchPar(
        SSServOpE.search, 
        input.key, 
        input.user, 
        input.includeTextualContent, 
        input.wordsToSearchFor, 
        input.includeTags, 
        input.tagsToSearchFor, 
        input.includeLabel, 
        input.labelsToSearchFor, 
        input.includeDescription, 
        input.descriptionsToSearchFor, 
        input.typesToSearchOnlyFor, 
        input.includeOnlySubEntities, 
        input.entitiesToSearchWithin, 
        input.extendToParents, 
        input.includeRecommendedResults, 
        input.provideEntries, 
        input.pagesID, 
        input.pageNumber, 
        input.minRating, 
        input.maxRating, 
        input.localSearchOp, 
        input.globalSearchOp);
      
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "authCheckCred")
  @ApiOperation(
    value = "retrieve the authentication key and user's uri for credentials",
    response = SSAuthCheckCredRet.class)
  public String authCheckCred(
    @Context HttpHeaders     headers,
    final SSAuthCheckCredRESTAPIV1Par input) throws Exception{
    
    final SSAuthCheckCredPar par = 
      new SSAuthCheckCredPar(
        SSServOpE.authCheckCred, 
        SSAuthConf.noAuthKey,
        null,
        input.label,
        input.password);
    
    try{
      par.key = SSRestMainV1.getBearer(headers);
    }catch(Exception error){}
    
    String result = SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
    
    SSLogU.debug(result);
    
    return result;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "circleEntitiesRemove")
  @ApiOperation(
    value = "retrieve circles the user is in",
    response = SSCircleEntitiesRemoveRet.class)
  public String circleEntitiesRemove(
    final SSCircleEntitiesRemoveRESTAPIV1Par input) throws Exception{
    
     final SSCircleEntitiesRemovePar par = 
      new SSCircleEntitiesRemovePar(
        SSServOpE.circleEntitiesRemove, 
        input.key, 
        input.user,
        input.circle,
        input.entities,
        true);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "circlesGet")
  @ApiOperation(
    value = "retrieve circles the user is in",
    response = SSCirclesGetRet.class)
  public String circlesGet(
    final SSCirclesGetRESTAPIV1Par input) throws Exception{
    
    final SSCirclesGetPar par = 
      new SSCirclesGetPar(
        SSServOpE.circlesGet, 
        input.key, 
        input.user,
        input.forUser, 
        null,  //entity
        input.entityTypesToIncludeOnly, //entityTypesToIncludeOnly
        true); //invokeEntityHandlers
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "circleGet")
  @ApiOperation(
    value = "retrieve a certain circle",
    response = SSCircleGetRet.class)
  public String circleGet(
    final SSCircleGetRESTAPIV1Par input) throws Exception{
    
    final SSCircleGetPar par = 
      new SSCircleGetPar(
        SSServOpE.circleGet, 
        input.key, 
        input.user, 
        input.forUser, 
        input.circle,
        input.entityTypesToIncludeOnly, //entityTypesToIncludeOnly
        true); //invokeEntityHandlers
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "circleCreate")
  @ApiOperation(
    value = "create a circle and add users and entities to",
    response = SSCircleCreateRet.class)
  public String circleCreate(
    final SSCircleCreateRESTAPIV1Par input){
    
    final SSCircleCreatePar par =
      new SSCircleCreatePar(
        SSServOpE.circleCreate,
        input.key,
        input.user,
        input.label,
        input.entities,
        input.users,
        input.description,
        false,
        true,
        true);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "circleUsersAdd")
  @ApiOperation(
    value = "add given users to a user-generated circle",
    response = SSCircleUsersAddRet.class)
  public String circleUsersAdd(
    final SSCircleUsersAddRESTAPIV1Par input){
    
    final SSCircleUsersAddPar par =
      new SSCircleUsersAddPar(
        SSServOpE.circleUsersAdd,
        input.key,
        input.user,
        input.circle,
        input.users,
        true);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "circleEntitiesAdd")
  @ApiOperation(
    value = "add given entities to a user-generated circle",
    response = SSCircleEntitiesAddRet.class)
  public String circleEntitiesAdd(
    final SSCircleEntitiesAddRESTAPIV1Par input){
    
    final SSCircleEntitiesAddPar par = 
      new SSCircleEntitiesAddPar(
        SSServOpE.circleEntitiesAdd, 
        input.key, 
        input.user, 
        input.circle, 
        input.entities,
        true, 
        true);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityGet")
  @ApiOperation(
    value = "retrieve information for given entity",
    response = SSEntityUserGetRet.class)
  public String entityGet(
    final SSEntityGetRESTAPIV1Par input){
    
    final SSEntityUserGetPar par = 
      new SSEntityUserGetPar(
        SSServOpE.entityGet, 
        input.key, 
        input.user, 
        input.entity, 
        null);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "tagAdd")
  @ApiOperation(
    value = "add a tag within for an entity within given space",
    response = SSTagAddRet.class)
  public String tagAdd(
    final SSTagAddRESTAPIV1Par input){
    
    final SSTagAddPar par =  
      new SSTagAddPar(
        SSServOpE.tagAdd, 
        input.key, 
        input.user, 
        input.entity, 
        input.label, 
        input.space, 
        input.creationTime);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "tagsRemove")
  @ApiOperation(
    value = "remove tag, user, entity, space combinations",
    response = SSTagsUserRemoveRet.class)
  public String tagsRemove(
    final SSTagsRemoveRESTAPIV1Par input){
    
    final SSTagsUserRemovePar par = 
      new SSTagsUserRemovePar(
        SSServOpE.tagsRemove, 
        input.key, 
        input.user, 
        input.entity, 
        input.label, 
        input.space);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "tagEdit")
  @ApiOperation(
    value = "changes the label of the tag assigned to entities by given user",
    response = SSTagUserEditRet.class)
  public String tagEdit(
    final SSTagEditRESTAPIV1Par input){
    
    final SSTagUserEditPar par = 
      new SSTagUserEditPar(
        SSServOpE.tagEdit, 
        input.key, 
        input.user, 
        input.tag, 
        input.entity, 
        input.label);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "tagsGet")
  @ApiOperation(
    value = "retrieve tag assignments",
    response = SSTagsUserGetRet.class)
  public String tagsGet(
    final SSTagsGetRESTAPIV1Par input){
    
    final SSTagsUserGetPar par =
      new SSTagsUserGetPar(
        SSServOpE.tagsGet,
        input.key,
        input.user,
        input.forUser,
        input.entities,
        input.labels,
        input.space,
        input.startTime);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "tagFrequsGet")
  @ApiOperation(
    value = "retrieve tag frequencies",
    response = SSTagUserFrequsGetRet.class)
  public String tagFrequsGet(
    final SSTagFrequsGetRESTAPIV1Par input){
    
    final SSTagUserFrequsGetPar par = 
      new SSTagUserFrequsGetPar(
        SSServOpE.tagFrequsGet, 
        input.key, 
        input.user, 
        input.forUser, 
        input.entities, 
        input.labels, 
        input.space, 
        input.startTime,
        input.useUsersEntities);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "tagEntitiesForTagsGet")
  @ApiOperation(
    value = "retrieve entities for tags (currently startTime is not used to retrieve entities)",
    response = SSTagUserEntitiesForTagsGetRet.class)
  public String tagEntitiesForTagsGet(
    final SSTagEntitiesForTagsGetRESTAPIV1Par input){
    
    final SSTagUserEntitiesForTagsGetPar par = 
      new SSTagUserEntitiesForTagsGetPar(
        SSServOpE.tagEntitiesForTagsGet, 
        input.key, 
        input.user, 
        input.forUser, 
        input.labels, 
        input.space, 
        input.startTime);
    
    return SSRestMainV1.handleStandardJSONRESTCall(par, par.op);
  }
  
  /* end calls with RESTful counterpart  */
  /* *************************************/
  /* *************************************/
  /* *************************************/
  /* *************************************/
  /* *************************************/
  /* *************************************/
  /* *************************************/
  /* *************************************/
  /* *************************************/
  /* *************************************/
  /* *************************************/
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "categoriesPredefinedGet")
  @ApiOperation(
    value = "get predefined categories",
    response = SSCategoriesPredefinedGetRet.class)
  public String categoriesPredefinedGet(final SSCategoriesPredefinedGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.categoriesPredefinedGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "flagsSet")
  @ApiOperation(
    value = "set flags",
    response = SSFlagsUserSetRet.class)
  public String flagsSet(final SSFlagsUserSetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.flagsSet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "flagsGet")
  @ApiOperation(
    value = "retrieve flags set",
    response = SSFlagsUserGetRet.class)
  public String flagsGet(final SSFlagsUserGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.flagsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityDescsGet")
  @ApiOperation(
    value = "retrieve more detailed information for given entities of a user",
    response = SSEntityDescsGetRet.class)
  public String entityDescsGet(final SSEntityDescsGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.entityDescsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "activitiesGet")
  @ApiOperation(
    value = "retrieve activities from within a certain time frame",
    response = SSActivitiesUserGetRet.class)
  public String activitiesGet(final SSActivitiesUserGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.activitiesGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "activityTypesGet")
  @ApiOperation(
    value = "retrieve available activity types",
    response = SSActivityTypesGetRet.class)
  public String activityTypesGet(final SSActivityTypesGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.activityTypesGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "activityAdd")
  @ApiOperation(
    value = "add an activity",
    response = SSActivityUserAddRet.class)
  public String activityAdd(final SSActivityAddPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.activityAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "systemVersionGet")
  @ApiOperation(
    value = "retrieve the version of the sss instance",
    response = SSSystemVersionGetRet.class)
  public String systemVersionGet(final SSSystemVersionGetPar input){

    try{
      return new SSSocketCon(
        SSRestMainV1.conf.ss.host,
        SSRestMainV1.conf.ss.port).prepRetFullToClient(
          new SSSystemVersionGetRet(
            SSRestMainV1.conf.ss.version,
            SSServOpE.systemVersionGet),
          SSServOpE.systemVersionGet);
      
    }catch(Exception error){
      SSLogU.err(error);
      return null;
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityPublicSet")
  @ApiOperation(
    value = "set an entity public (make it accessible for everyone)",
    response = SSCircleEntityPublicSetRet.class)
  public String entityPublicSet(final SSCircleEntityPublicSetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.circleEntityPublicSet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityAdd")
  @ApiOperation(
    value = "add an entity",
    response = SSEntityUserAddRet.class)
  public String entityAdd(final SSEntityUserAddPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.entityAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityCopy")
  @ApiOperation(
    value = "copy an entity and hand it to a user",
    response = SSEntityUserCopyRet.class)
  public String entityCopy(final SSEntityUserCopyPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.entityCopy);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityEntityUsersGet")
  @ApiOperation(
    value = "retrieve users who can access given entity",
    response = SSCircleEntityUsersGetRet.class)
  public String entityEntityUsersGet(final SSCircleEntityUsersGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.circleEntityUsersGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collsEntityIsInGet")
  @ApiOperation(
    value = "retrieve all the user's collections given entity is in",
    response = SSCollsUserEntityIsInGetRet.class)
  public String collsEntityIsInGet(final SSCollsUserEntityIsInGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collsEntityIsInGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collsCouldSubscribeGet")
  @ApiOperation(
    value = "retrieve a list of all public collections given user could subscribe to",
    response = SSCollsUserCouldSubscribeGetRet.class)
  public String collsCouldSubscribeGet(final SSCollsUserCouldSubscribeGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collsCouldSubscribeGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collRootGet")
  @ApiOperation(
    value = "retrieve the user's root collection",
    response = SSCollUserRootGetRet.class)
  public String collRootGet(final SSCollUserRootGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collRootGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collParentGet")
  @ApiOperation(
    value = "retrieve the parent collection for given user's collection",
    response = SSCollUserParentGetRet.class)
  public String collParentGet(final SSCollUserParentGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collParentGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collEntryAdd")
  @ApiOperation(
    value = "add a (new) collection or any other entity to given user's collection",
    response = SSCollUserEntryAddRet.class)
  public String collEntryAdd(final SSCollUserEntryAddPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collEntryAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collEntriesAdd")
  @ApiOperation(
    value = "add existing collections or (new) entities to a collection",
    response = SSCollUserEntriesAddRet.class)
  public String collEntriesAdd(final SSCollUserEntriesAddPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collEntriesAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collEntryChangePos")
  @ApiOperation(
    value = "change the sequential order of entries in a user's collection",
    response = SSCollUserEntryChangePosRet.class)
  public String collEntryChangePos(final SSCollUserEntryChangePosPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collEntryChangePos);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collEntryDelete")
  @ApiOperation(
    value = "delete an item from a user's collection",
    response = SSCollUserEntryDeleteRet.class)
  public String collEntryDelete(final SSCollUserEntryDeletePar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collEntryDelete);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collEntriesDelete")
  @ApiOperation(
    value = "delete one or more entries from a collection",
    response = SSCollUserEntriesDeleteRet.class)
  public String collEntriesDelete(final SSCollUserEntriesDeletePar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collEntriesDelete);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "circleEntityShare")
  @ApiOperation(
    value = "share an entity directly with given users",
    response = SSCircleEntityShareRet.class)
  public String circleEntityShare(final SSCircleEntitySharePar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.circleEntityShare);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collWithEntries")
  @ApiOperation(
    value = "retrieve a user's collection with entries",
    response = SSCollUserWithEntriesRet.class)
  public String collWithEntries(final SSCollUserWithEntriesPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collWithEntries);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collsWithEntries")
  @ApiOperation(
    value = "retrieve the user's collections with entries",
    response = SSCollsUserWithEntriesRet.class)
  public String collsWithEntries(final SSCollsUserWithEntriesPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collsWithEntries);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collHierarchyGet")
  @ApiOperation(
    value = "retrieve the parent collection order for a user's collection",
    response = SSCollUserHierarchyGetRet.class)
  public String collHierarchyGet(final SSCollUserHierarchyGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collHierarchyGet);
  }
   
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collCumulatedTagsGet")
  @ApiOperation(
    value = "retrieve the cumulated tags (and their frequencies) for all the sub collections and respective entities",
    response = SSCollUserCumulatedTagsGetRet.class)
  public String collCumulatedTagsGet(final SSCollUserCumulatedTagsGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collCumulatedTagsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "discEntryAdd")
  @ApiOperation(
    value = "add a textual comment/answer/opinion to a discussion [for given entity] or create a new discussion",
    response = SSDiscUserEntryAddRet.class)
  public String discEntryAdd(final SSDiscUserEntryAddPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.discEntryAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "discURIsForTargetGet")
  @ApiOperation(
    value = "retrieve discussions for a certain entity",
    response = SSDiscUserDiscURIsForTargetGetRet.class)
  public String discURIsForTargetGet(final SSDiscUserDiscURIsForTargetGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.discURIsForTargetGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "discRemove")
  @ApiOperation(
    value = "remove a discussion from given user",
    response = SSDiscUserRemoveRet.class)
  public String discRemove(final SSDiscUserRemovePar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.discRemove);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "discWithEntriesGet")
  @ApiOperation(
    value = "retrieve the discussion with its entries",
    response = SSDiscUserWithEntriesRet.class)
  public String discWithEntriesGet(final SSDiscUserWithEntriesGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.discWithEntriesGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "discsAllGet")
  @ApiOperation(
    value = "retrieve all discussions given user is allowed to see",
    response = SSDiscsUserAllGetRet.class)
  public String discsAllGet(final SSDiscsUserAllGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.discsAllGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityDirectlyAdjoinedEntitiesRemove")
  @ApiOperation(
    value = "remove certain attached attributes from an entity",
    response = SSEntityUserDirectlyAdjoinedEntitiesRemoveRet.class)
  public String entityDirectlyAdjoinedEntitiesRemove(final SSEntityUserDirectlyAdjoinedEntitiesRemovePar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.entityDirectlyAdjoinedEntitiesRemove);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityDescGet")
  @ApiOperation(
    value = "retrieve more detailed information for given entity",
    response = SSEntityDescGetRet.class)
  public String entityDescGet(final SSEntityDescGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.entityDescGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityUpdate")
  @ApiOperation(
    value = "updates/adds given properties for an entity",
    response = SSEntityUserUpdateRet.class)
  public String entityUpdate(final SSEntityUserUpdatePar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.entityUpdate);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "fileWritingMinutesLeft")
  @ApiOperation(
    value = "retrieve number of minutes left user is allowed to replace / re-upload a file",
    response = SSFileWritingMinutesLeftRet.class)
  public String fileWritingMinutesLeft(final SSFileWritingMinutesLeftPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.fileWritingMinutesLeft);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "modelUEResourceDetails")
  @ApiOperation(
    value = "retrieve automatically usage-based modeled details for given entity",
    response = SSModelUEResourceDetailsRet.class)
  public String modelUEResourceDetails(final SSModelUEEntityDetailsPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.modelUEResourceDetails);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "ratingOverallGet")
  @ApiOperation(
    value = "retrieve the overall rating (by all users) for given entity",
    response = SSRatingOverallGetRet.class)
  public String ratingOverallGet(final SSRatingOverallGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.ratingOverallGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "ratingSet")
  @ApiOperation(
    value = "set the user's rating for given entity",
    response = SSRatingUserSetRet.class)
  public String ratingSet(final SSRatingUserSetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.ratingSet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "recommTags")
  @ApiOperation(
    value = "retrieve tag recommendations based on user, entity, tag, time and category combinations",
    response = SSRecommTagsRet.class)
  public String recommTags(final SSRecommTagsPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.recommTags);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "recommResources")
  @ApiOperation(
    value = "retrieve resource recommendations based on user, entity, tag, time and category combinations",
    response = SSRecommResourcesRet.class)
  public String recommResources(final SSRecommResourcesPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.recommResources);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "userAll")
  @ApiOperation(
    value = "retrieve all users",
    response = SSUserAllRet.class)
  public String userAll(final SSUserAllPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.userAll);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "uEAdd")
  @ApiOperation(
    value = "add a usage-based trace, i.e. user event, for entity, user combination",
    response = SSUEAddRet.class)
  public String uEAdd(final SSUEAddPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.uEAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "uEsGet")
  @ApiOperation(
    value = "retrieve user events for user, entity, time combination",
    response = SSUEsGetRet.class)
  public String uEsGet(final SSUEsGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.uEsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "uEGet")
  @ApiOperation(
    value = "retrieve given user event",
    response = SSUEGetRet.class)
  public String uEGet(final SSUEGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.uEGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "uECountGet")
  @ApiOperation(
    value = "retrieve the number of certain user events",
    response = SSUECountGetRet.class)
  public String uECountGet(final SSUECountGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.uECountGet);
  }
  
//  @GET
//  @Consumes(MediaType.TEXT_HTML)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "jsonLD" + SSStrU.slash + SSStrU.curlyBracketOpen + SSVarU.type + SSStrU.curlyBracketClose)
//  @ApiOperation(
//    value = "retrieve the JSON-LD description for a datatype from within SSS",
//    response = Object.class) 
//  public String jsonLD(
//    @ApiParam(
//      value = "type",
//      required = true)
//    @PathParam(SSVarU.type) 
//    final SSJSONLDPar input){
//    
//    String jsonRequ = "{\"op\":\"" + SSServOpE.jsonLD + "\",\"user\":\"" + "mailto:dummyUser" + "/\",\"type\":\"" + input.type + "\",\"key\":\"681V454J1P3H4W3B367BB79615U184N22356I3E\"}";
//    
//    return handleStandardJSONRESTCall(jsonRequ, SSServOpE.jsonLD);
//  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "broadcastsGet")
  public String broadcastsGet(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.broadcastsGet);
  }

  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpLockHold")
  public String learnEpLockHold(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpLockHold);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpsLockHold")
  public String learnEpsLockHold(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpsLockHold);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpLockSet")
  public String learnEpLockSet(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpLockSet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpLockRemove")
  public String learnEpLockRemove(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpLockRemove);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpsGet")
  public String learnEpsGet(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionsGet")
  public String learnEpVersionsGet(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionGet")
  public String learnEpVersionGet(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionCurrentGet")
  public String learnEpVersionCurrentGet(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionCurrentGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionCurrentSet")
  public String learnEpVersionCurrentSet(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionCurrentSet);
  }
    
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionCreate")
  public String learnEpVersionCreate(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionCreate);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionAddCircle")
  public String learnEpVersionAddCircle(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionAddCircle);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionAddEntity")
  public String learnEpVersionAddEntity(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionAddEntity);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpCreate")
  public String learnEpCreate(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpCreate);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionUpdateCircle")
  public String learnEpVersionUpdateCircle(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionUpdateCircle);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionUpdateEntity")
  public String learnEpVersionUpdateEntity(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionUpdateEntity);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionSetTimelineState")
  public String learnEpVersionSetTimelineState(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionSetTimelineState);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionGetTimelineState")
  public String learnEpVersionGetTimelineState(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionGetTimelineState);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionRemoveCircle")
  public String learnEpVersionRemoveCircle(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionRemoveCircle);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionRemoveEntity")
  public String learnEpVersionRemoveEntity(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionRemoveEntity);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpRemove")
  public String learnEpRemove(String jsonRequ){
    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpRemove);
  }
  
  /* category */ 
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "categoryEdit")
  @ApiOperation(
    value = "changes the label of the category assigned to entities by given user",
    response = SSCategoryUserEditRet.class)
  public String categoryEdit(final SSCategoryUserEditPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.categoryEdit);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "categoryEntitiesForCategoriesGet")
  @ApiOperation(
    value = "retrieve entities for categories (currently startTime is not used to retrieve entities)",
    response = SSCategoryUserEntitiesForCategoriesGetRet.class)
  public String categoryEntitiesForCategoriesGet(final SSCategoryUserEntitiesForCategoriesGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.categoryEntitiesForCategoriesGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "categoriesGet")
  @ApiOperation(
    value = "retrieve category assignments",
    response = SSCategoriesUserGetRet.class)
  public String categoriesGet(final SSCategoriesUserGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.categoriesGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "categoryAdd")
  @ApiOperation(
    value = "add a category within for an entity within given space",
    response = SSCategoryAddRet.class)
  public String categoryAdd(final SSCategoryAddPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.categoryAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "categoryFrequsGet")
  @ApiOperation(
    value = "retrieve category frequencies",
    response = SSCategoryUserFrequsGetRet.class)
  public String categoryFrequsGet(final SSCategoryUserFrequsGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.categoryFrequsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "categoriesRemove")
  @ApiOperation(
    value = "remove category, user, entity, space combinations",
    response = SSCategoriesUserRemoveRet.class)
  public String categoriesRemove(final SSCategoriesUserRemovePar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.categoriesRemove);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "messageSend")
  @ApiOperation(
    value = "send a message to a user",
    response = SSMessageSendRet.class)
  public String messageSend(final SSMessageSendPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.messageSend);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "messagesGet")
  @ApiOperation(
    value = "retrieve messages for the user",
    response = SSMessagesGetRet.class)
  public String messagesGet(final SSMessagesGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.messagesGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "evalLog")
  @ApiOperation(
    value = "log events for evaluation purposes",
    response = SSMessagesGetRet.class)
  public String evalLog(final SSEvalLogPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.evalLog);
  }
}


//    Map<String, Object> ret = new HashMap<>();
//    
//    try{
//      ret.put(SSVarU.op,                  SSServOpE.jsonLD);
//      ret.put(SSVarU.error,               false);
//      ret.put(SSVarU.jsonLD,              ((SSJSONLDImpl)SSJSONLD.inst.impl()).jsonLDDesc(entityType));
//      
//      ret.put(SSJSONLDU.context, SSJSONLDU.jsonLDContext());
//      
//      return Response.status(200).entity(SSJSONU.jsonStr(ret)).build();
//      
//    }catch (Exception error) {
//      
//      SSLogU.logError(error);
//        
//      try{
//        return Response.serverError().build();
//      }catch(Exception error1){
//        SSLogU.logError(error1, "writing error to client didnt work");
//      }
//    }
//    
//    return Response.ok(null).build();

//@POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_OCTET_STREAM)
//  @Path(SSStrU.slash + "fileDownload")
//  public StreamingOutput fileDownload(String jsonRequ) throws Exception{
//  
//        sSCon = new SSSocketCon(conf.host, conf.port, jsonRequ);
//      
//      sSCon.writeRequFullToSS   ();
//      sSCon.readMsgFullFromSS   ();
//      sSCon.writeRequFullToSS   ();
//  
//  return new StreamingOutput() {
//    
//    public void write(OutputStream output) throws IOException, WebApplicationException {
//     
//      try {
//         byte[] bytes;
//          FileOutputStream fileOutputStream = null;
//          
//          try{
//            fileOutputStream = SSFileU.openFileForWrite("F:/daten/hugo.mp3");
//          }catch(Exception ex){
//            Logger.getLogger(SSAdapterRest.class.getName()).log(Level.SEVERE, null, ex);
//          }
//
//          while((bytes = sSCon.readFileChunkFromSS()).length > 0) {
//
//            output.write               (bytes);
//            output.flush               ();
//            
//            fileOutputStream.write(bytes);
//            
//            sSCon.writeRequFullToSS ();
//          }
//          
//          output.close();
//          fileOutputStream.close();
//      } catch (Exception e) {
//        throw new WebApplicationException(e);
//      }
//    }
//  };