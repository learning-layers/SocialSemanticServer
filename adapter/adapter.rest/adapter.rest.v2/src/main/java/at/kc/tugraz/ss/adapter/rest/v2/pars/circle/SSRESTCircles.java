/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.kc.tugraz.ss.adapter.rest.v2.pars.circle;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.adapter.rest.v2.pars.circle.SSCircleCreateRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.circle.SSCircleEntitiesAddRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.circle.SSCircleEntitiesRemoveRESTAPIV2Par;
import at.kc.tugraz.ss.adapter.rest.v2.pars.circle.SSCircleUsersAddRESTAPIV2Par;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreatePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleCreateRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesAddRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntitiesRemoveRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleUsersAddRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCirclesGetRet;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
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

@Path("/circles")
@Api( value = "/circles") //, basePath = "/circles"
public class SSRESTCircles{
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "retrieve information on entities the user can access",
    response = SSCircleEntitiesGetRet.class)
  @Path("/entities")
  public Response entitiesGet(
    @Context HttpHeaders headers){
    
    final SSCircleEntitiesGetPar par;
    
    try{
      
      par =
        new SSCircleEntitiesGetPar(
          SSMethU.circleEntitiesGet,
          null,  //key
          null,  //user
          null,  //forUser
          SSEntityE.asListWithoutNullAndEmpty(), //types
          true,  //withSystemCircles
          true); //invokeEntityHandlers
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/users/{user}")
  @ApiOperation(
    value = "retrieve circles for a given user",
    response = SSCirclesGetRet.class)
  public Response circlesForUserGet(
    @Context                   HttpHeaders headers,
    @PathParam (SSVarU.user)   String      user){
    
    final SSCirclesGetPar par;
    
    try{
      
      par =
        new SSCirclesGetPar(
          SSMethU.circlesGet,
          null,
          null,
          SSUri.get(user, SSVocConf.sssUri),
          null,
          SSEntityE.asListWithoutNullAndEmpty(),
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{circle}/users/{user}")
  @ApiOperation(
    value = "retrieve a circle for a given user",
    response = SSCirclesGetRet.class)
  public Response circleForUserGet(
    @Context                    HttpHeaders  headers,
    @PathParam (SSVarU.user)    String       user,
    @PathParam (SSVarU.circle)  String       circle){
    
    final SSCircleGetPar par;
    
    try{
      
      par =
        new SSCircleGetPar(
          SSMethU.circleGet,
          null,
          null,
          SSUri.get(user,   SSVocConf.sssUri),
          SSUri.get(circle, SSVocConf.sssUri),
          SSEntityE.asListWithoutNullAndEmpty(),
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{circle}/users")
  @ApiOperation(
    value = "add given users to a circle",
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
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{circle}/entities")
  @ApiOperation(
    value = "add given entities to a circle",
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
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{circle}/entities")
  @ApiOperation(
    value = "remove given entities from circle",
    response = SSCircleEntitiesRemoveRet.class)
  public Response circleEntitiesDelete(
    @Context HttpHeaders                          headers,
    @PathParam(SSVarU.circle) String              circle,
    final SSCircleEntitiesRemoveRESTAPIV2Par input){
    
    final SSCircleEntitiesRemovePar par;
    
    try{
      
      par =
        new SSCircleEntitiesRemovePar(
          SSMethU.circleEntitiesRemove,
          null,
          null,
          SSUri.get(circle, SSVocConf.sssUri), //circle
          input.entities, //entities
          true); //withUserRestriction
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("")
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
          SSEntityE.asListWithoutNullAndEmpty(),
          true);
      
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
    value = "create a circle and add users / entities",
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
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}
