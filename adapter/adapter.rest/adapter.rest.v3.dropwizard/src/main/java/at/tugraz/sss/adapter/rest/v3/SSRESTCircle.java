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
package at.tugraz.sss.adapter.rest.v3;

import at.tugraz.sss.servs.util.*;
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.entity.datatype.*;
import at.tugraz.sss.servs.db.api.*;
import at.tugraz.sss.servs.db.impl.*;
import at.tugraz.sss.servs.entity.api.*;
import at.tugraz.sss.servs.entity.impl.*;
import io.swagger.annotations.*;
import java.sql.*;
import javax.annotation.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("rest/circles")
@Api( value = "circles")
public class SSRESTCircle{
  
  private final SSEntityClientI entityServ = new SSEntityImpl();
  private final SSDBSQLI          dbSQL        = new SSDBSQLMySQLImpl();
  
  @PostConstruct
  public void createRESTResource(){
  }
  
  @PreDestroy
  public void destroyRESTResource(){
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "retrieve circles",
    response = SSCirclesGetRet.class)
  public Response circlesGet(
    @Context
    final HttpHeaders headers){
    
    final SSCirclesGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCirclesGetPar(
            new SSServPar(sqlCon),
            null, //user,
            null, //forUser,
            null, //entity
            null, //entityTypesToIncludeOnly
            true, //setEntities,
            true, //setUsers
            true,  //withUserRestriction
            false, //withSystemCircles
            true); //invokeEntityHandlers
        
        par.setThumb          = false;
        par.setProfilePicture = false;
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.circlesGet(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/users/{forUser}")
  @ApiOperation(
    value = "retrieve circles for user",
    response = SSCirclesGetRet.class)
  public Response circlesForUsersGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.forUser)
    final String forUser){
    
    final SSCirclesGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCirclesGetPar(
            new SSServPar(sqlCon),
            null, //user,
            SSUri.get(forUser, SSConf.sssUri), //forUser,
            null, //entity
            null, //entityTypesToIncludeOnly
            true, //setEntities,
            true, //setUsers
            true,  //withUserRestriction
            false, //withSystemCircles
            true); //invokeEntityHandlers
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.circlesGet(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered")
  @ApiOperation(
    value = "retrieve filtered circles",
    response = SSCirclesGetRet.class)
  public Response circlesFilteredGet(
    @Context
    final HttpHeaders headers,
    
    final SSCirclesGetRESTPar input){
    
    final SSCirclesGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCirclesGetPar(
            new SSServPar(sqlCon),
            null, //user,
            input.forUser, //forUser,
            null, //entity
            input.entityTypesToIncludeOnly, //entityTypesToIncludeOnly
            true, //setEntities,
            true, //setUsers
            true,  //withUserRestriction
            false, //withSystemCircles
            input.invokeEntityHandlers); //invokeEntityHandlers
        
        par.setThumb          = input.setThumb;
        par.setProfilePicture = input.setProfilePicture;
        par.setTags           = input.setTags;
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.circlesGet(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered/users/{forUser}")
  @ApiOperation(
    value = "retrieve filtered circles for user",
    response = SSCirclesGetRet.class)
  public Response circlesFilteredForUserGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.forUser)
    final String forUser,
    
    final SSCirclesFilteredForUserGetRESTPar input){
    
    final SSCirclesGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCirclesGetPar(
            new SSServPar(sqlCon),
            null, //user,
            SSUri.get(forUser, SSConf.sssUri), //forUser,
            null, //entity
            input.entityTypesToIncludeOnly, //entityTypesToIncludeOnly
            true, //setEntities,
            true, //setUsers
            true,  //withUserRestriction
            false, //withSystemCircles
            input.invokeEntityHandlers); //invokeEntityHandlers
        
        par.setThumb          = input.setThumb;
        par.setProfilePicture = input.setProfilePicture;
        par.setTags           = input.setTags;
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.circlesGet(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{circle}")
  @ApiOperation(
    value = "retrieve a circle",
    response = SSCircleGetRet.class)
  public Response circleGet(
    @Context
    final HttpHeaders  headers,
    
    @PathParam (SSVarNames.circle)
    final String circle){
    
    final SSCircleGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCircleGetPar(
            new SSServPar(sqlCon),
            null, //user
            SSUri.get(circle, SSConf.sssUri), //circle
            null, //entityTypesToIncludeOnly
            false, //setTags
            null, //circle
            true, //setEntities,
            true, //setUsers
            true,  //withUserRestriction
            true); //invokeEntityHandlers
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.circleGet(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered/{circle}")
  @ApiOperation(
    value = "retrieve a filtered circle",
    response = SSCircleGetRet.class)
  public Response circleFilteredGet(
    @Context
    final HttpHeaders  headers,
    
    @PathParam (SSVarNames.circle)
    final String circle,
    
    final SSCircleGetRESTPar input){
    
    final SSCircleGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCircleGetPar(
            new SSServPar(sqlCon),
            null, //user
            SSUri.get(circle, SSConf.sssUri), //circle
            input.entityTypesToIncludeOnly, //entityTypesToIncludeOnly
            input.setTags,
            input.tagSpace,
            true, //setEntities,
            true, //setUsers
            true,  //withUserRestriction
            input.invokeEntityHandlers); //invokeEntityHandlers
        
        par.setProfilePicture = input.setProfilePicture;
        par.setThumb          = input.setThumb;
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.circleGet(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{circle}/users/{users}")
  @ApiOperation(
    value = "add given users to a circle",
    response = SSCircleUsersAddRet.class)
  public Response circleUsersAdd(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.circle)
    final String circle,
    
    @PathParam (SSVarNames.users)
    final String users){
    
    final SSCircleUsersAddPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        par =
          new SSCircleUsersAddPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(circle, SSConf.sssUri), //circle
            SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(users, SSStrU.comma), SSConf.sssUri),  //users
            true, //withUserRestriction
            true); //shouldCommit
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.circleUsersAdd(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{circle}/entities/{entities}")
  @ApiOperation(
    value = "add given entities to a circle",
    response = SSCircleEntitiesAddRet.class)
  public Response circleEntitiesAdd(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.circle)
    final String circle,
    
    @PathParam (SSVarNames.entities)
    final String entities,
    
    final SSCircleEntitiesAddRESTPar input){
    
    final SSCircleEntitiesAddPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCircleEntitiesAddPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(circle, SSConf.sssUri), //circle
            SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(entities, SSStrU.comma), SSConf.sssUri),  //entities
            true,  //withUserRestriction
            true); //shouldCommit
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSServRetI addCircleEntitiesResult = entityServ.circleEntitiesAdd(SSClientE.rest, par);
        
        SSRESTCommons.addTags(
          par,
          par.user,
          input.tags,
          par.entities,
          par.circle);
        
        SSRESTCommons.addCategories(
          par,
          par.user,
          input.categories,
          par.entities,
          par.circle);
        
        return Response.status(200).entity(addCircleEntitiesResult).build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{circle}/entities/{entities}")
  @ApiOperation(
    value = "remove given entities from circle",
    response = SSCircleEntitiesRemoveRet.class)
  public Response circleEntitiesRemove(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.circle)
    final String circle,
    
    @PathParam(SSVarNames.entities)
    final String entities,
    
    final SSCircleEntitiesRemoveRESTPar input){
    
    final SSCircleEntitiesRemovePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCircleEntitiesRemoveFromClientPar(
            new SSServPar(sqlCon),
            SSUri.get(circle, SSConf.sssUri), //circle
            SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(entities, SSStrU.comma), SSConf.sssUri),  //entities
            input.removeCircleSpecificMetadata); //removeCircleSpecificMetadata
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        return Response.status(200).entity(entityServ.circleEntitiesRemove(SSClientE.rest, par)).build();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "create a circle and add users / entities",
    response = SSCircleCreateRet.class)
  public Response circleCreate(
    @Context
    final HttpHeaders headers,
    
    final SSCircleCreateRESTPar input){
    
    final SSCircleCreatePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        if(input.type == null){
          input.type = SSCircleE.group;
        }
        
        par =
          new SSCircleCreateFromClientPar(
            new SSServPar(sqlCon),
            null,
            input.type,
            input.label, //label
            input.description, //description
            input.users, //users
            input.invitees, //invitees
            input.entities);//entities
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.circleCreate(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{circle}/users/{users}")
  @ApiOperation(
    value = "remove users from circle",
    response = SSCircleUsersRemoveRet.class)
  public Response circleUsersRemove(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.circle)
    final String circle,
    
    @PathParam(SSVarNames.users)
    final String users){
    
    final SSCircleUsersRemovePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCircleUsersRemovePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(circle, SSConf.sssUri), //circle
            SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(users, SSStrU.comma), SSConf.sssUri), //users
            true, //withUserRestriction
            true); //shouldCommit
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.circleUsersRemove(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{circle}")
  @ApiOperation(
    value = "remove circle",
    response = SSCircleRemoveRet.class)
  public Response circleRemove(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.circle)
    final String circle){
    
    final SSCircleRemovePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCircleRemovePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(circle, SSConf.sssUri), //circle
            true, //withUserRestriction
            true); //shouldCommit
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.circleRemove(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{circle}/users/invite/{emails}")
  @ApiOperation(
    value = "invite users to a circle",
    response = SSCircleUsersInviteRet.class)
  public Response circleUsersInvite(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.circle)
    final String circle,
    
    @PathParam(SSVarNames.emails)
    final String emails){
    
    final SSCircleUsersInvitePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCircleUsersInvitePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(circle, SSConf.sssUri),
            SSStrU.splitDistinctWithoutEmptyAndNull(emails, SSStrU.comma),
            true,
            true);
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.circleUsersInvite(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{circle}/type/{type}")
  @ApiOperation(
    value = "change circle's type",
    response = SSCircleTypeChangeRet.class)
  public Response circleTypeChange(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.circle)
    final String circle,
    
    @PathParam(SSVarNames.type)
    final String type){
    
    final SSCircleTypeChangePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCircleTypeChangePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(circle, SSConf.sssUri),
            SSCircleE.get(type),
            true, //withUserRestriction
            true); //shouldCommit
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.circleTypeChange(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
}