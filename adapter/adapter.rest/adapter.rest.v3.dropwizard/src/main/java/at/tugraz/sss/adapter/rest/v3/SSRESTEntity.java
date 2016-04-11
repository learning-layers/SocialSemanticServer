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
package at.tugraz.sss.adapter.rest.v3;

import at.tugraz.sss.servs.comment.datatype.SSCommentsAddRESTPar;
import at.tugraz.sss.serv.datatype.par.SSEntityCopyPar;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.servs.user.datatype.SSUserEntityUsersGetPar;
import at.tugraz.sss.servs.user.datatype.SSUserEntityUsersGetRet;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.servs.comment.api.*;
import at.tugraz.sss.servs.comment.datatype.*;
import at.tugraz.sss.servs.comment.impl.*;
import at.tugraz.sss.servs.db.impl.*;
import at.tugraz.sss.servs.entity.api.*;
import at.tugraz.sss.servs.entity.datatype.*;
import at.tugraz.sss.servs.entity.impl.*;
import at.tugraz.sss.servs.user.api.*;
import at.tugraz.sss.servs.user.impl.*;
import io.swagger.annotations.*;
import java.sql.*;
import javax.annotation.*;
import javax.ws.rs.Consumes;
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

@Path("rest/entities")
@Api( value = "entities")
public class SSRESTEntity{
  
  private final SSEntityClientI  entityServ  = new SSEntityImpl();
  private final SSUserClientI    userServ    = new SSUserImpl();
  private final SSCommentClientI commentServ = new SSCommentImpl();
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
  @Path("/accessible")
  @ApiOperation(
    value = "retrieve accessible entities",
    response = SSEntityTypesGetRet.class)
  public Response entitiesAccessibleGet(
    @Context
    final HttpHeaders headers){
    
    final SSEntitiesAccessibleGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      try{
        
        par =
          new SSEntitiesAccessibleGetPar(
            new SSServPar(sqlCon),
            null, //user
            null, //types
            null, //authors
            null, //startTime
            null, //endTime
            new SSEntityDescriberPar(null), //descPar
            true); //withUserRestriction
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.entitiesAccessibleGet(SSClientE.rest, par)).build();
        
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
  @Path("/{entity}/attach/entities/{entities}")
  @ApiOperation(
    value = "attach entities",
    response = SSEntityAttachEntitiesRet.class)
  public Response entitiesAttachPost(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.entity)
    final String entity,
      
    @PathParam(SSVarNames.entities)
    final String entities){
    
    final SSEntityAttachEntitiesPar par;
    Connection                      sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSEntityAttachEntitiesPar(
            new SSServPar(sqlCon),
            null, //user
            SSUri.get(entity, SSConf.sssUri), //entity
            SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(entities, SSStrU.comma), SSConf.sssUri), //entities
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
        
        
        return Response.status(200).entity(entityServ.entityEntitiesAttach(SSClientE.rest, par)).build();
        
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
  @Path("/filtered/accessible")
  @ApiOperation(
    value = "retrieve accessible entities",
    response = SSEntityTypesGetRet.class)
  public Response entitiesAccessibleFilteredGet(
    @Context
    final HttpHeaders headers,
    
    final SSEntitiesAccessibleGetRESTPar input){
    
    final SSEntitiesAccessibleGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        final SSEntityDescriberPar descPar = new SSEntityDescriberPar(null);
        
        descPar.setFlags = input.setFlags;
        descPar.setTags  = input.setTags;
        
        par =
          new SSEntitiesAccessibleGetPar(
            new SSServPar(sqlCon),
            null, //user
            input.types, //types
            input.authors, //authors
            input.startTime, //startTime
            input.endTime, //endTime
            descPar, //descPar
            true); //withUserRestriction
        
        par.pageSize   = input.pageSize;
        par.pagesID    = input.pagesID;
        par.pageNumber = input.pageNumber;
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.entitiesAccessibleGet(SSClientE.rest, par)).build();
        
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
  @Path("/types")
  @ApiOperation(
    value = "retrieve available entity types",
    response = SSEntityTypesGetRet.class)
  public Response entityTypesGet(
    @Context
    final HttpHeaders headers){
    
    final SSEntityTypesGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSEntityTypesGetPar(
            new SSServPar(sqlCon),
            null);
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.entityTypesGet(SSClientE.rest, par)).build();
        
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
  @Path("/filtered/{entities}")
  @ApiOperation(
    value = "retrieve entities information for given ID(s) or encoded URI(s)",
    response = SSEntitiesGetRet.class)
  public Response entitiesGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.entities)
    final String entities,
    
    final SSEntitiesGetRESTPar input){
    
    final SSEntityDescriberPar   descPar = new SSEntityDescriberPar(null);
    final SSEntitiesGetPar       par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        descPar.circle              = input.circle;
        descPar.space               = input.tagSpace;
        descPar.setTags             = input.setTags;
        descPar.setOverallRating    = input.setOverallRating;
        descPar.setDiscs            = input.setDiscs;
        descPar.setUEs              = input.setUEs;
        descPar.setThumb            = input.setThumb;
        descPar.setFlags            = input.setFlags;
        descPar.setCircles          = input.setCircles;
        descPar.setProfilePicture   = input.setProfilePicture;
        descPar.setAttachedEntities = input.setAttachedEntities;
        
        par =
          new SSEntitiesGetPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(entities, SSStrU.comma), SSConf.sssUri), //entities
            descPar, //descPar
            true); //withUserRestriction
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.entitiesGet(SSClientE.rest, par)).build();
        
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
  
  //TODO replace this call by a service to be created for placeholder functionality (i.e., b&p placeholders)
  @Deprecated
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "adds a new entity with given properties",
    response = SSEntityUpdateRet.class)
  public Response entityAdd(
    @Context
    final HttpHeaders headers,
    
    final SSEntityUpdateRESTPar input){
    
    final SSEntityUpdatePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        par =
          new SSEntityUpdatePar(
            new SSServPar(sqlCon),
            null, //user
            null, //entity
            input.type, //type
            input.label,       //label
            input.description, //description
            input.creationTime, //creationTime
            input.read,  //read
            false, //setPublic
            true, //createIfNotExists
            true, //withUserRestriction,
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
        
        
        return Response.status(200).entity(entityServ.entityUpdate(SSClientE.rest, par)).build();
        
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
  @Path("/{entity}")
  @ApiOperation(
    value = "updates / adds given properties for given entity",
    response = SSEntityUpdateRet.class)
  public Response entityUpdate(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.entity)
    final String entity,
    
    final SSEntityUpdateRESTPar input){
    
    final SSEntityUpdatePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        par =
          new SSEntityUpdatePar(
            new SSServPar(sqlCon),
            null, //user
            SSUri.get(entity, SSConf.sssUri), //entity
            input.type, //type
            input.label,       //label
            input.description, //description
            input.creationTime, //creationTime
            input.read,  //read
            false, //setPublic
            true,  //createIfNotExists
            true, //withUserRestriction,
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
        
        
        return Response.status(200).entity(entityServ.entityUpdate(SSClientE.rest, par)).build();
        
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
  @Path("/{entity}/share")
  @ApiOperation(
    value = "share an entity with users, circles or set it public (make it accessible for everyone)",
    response = SSEntityShareRet.class)
  public Response entityShare(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.entity)
    final String entity,
    
    final SSEntityShareRESTPar input){
    
    final SSEntitySharePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        par =
          new SSEntitySharePar(
            new SSServPar(sqlCon),
            null, //user
            SSUri.get(entity, SSConf.sssUri), //entity
            input.users,
            input.circles,
            input.setPublic, //setPublic,
            input.comment, //comment
            true, //withUserRestriction,
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
        
        
        return Response.status(200).entity(entityServ.entityShare(SSClientE.rest, par)).build();
        
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
  @Path("/{entity}/unpublicize")
  @ApiOperation(
    value = "remove an entity from public space",
    response = SSEntityUnpublicizeRet.class)
  public Response entityUnpublicize(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.entity)
    final String entity){
    
    final SSEntityUnpublicizePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        par =
          new SSEntityUnpublicizePar(
            new SSServPar(sqlCon),
            null, //user
            SSUri.get(entity, SSConf.sssUri), //entity
            true, //withUserRestriction,
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
        
        
        return Response.status(200).entity(entityServ.entityUnpublicize(SSClientE.rest, par)).build();
        
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
  @Path    ("/{entity}/copy")
  @ApiOperation(
    value = "copy an entity",
    response = SSEntityCopyRet.class)
  public Response entityCopy(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.entity)
    final String entity,
    
    final SSEntityCopyRESTPar input){
    
    final SSEntityCopyPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        par =
          new SSEntityCopyPar(
            new SSServPar(sqlCon),
            null, //user
            SSUri.get(entity, SSConf.sssUri), //entity
            input.targetEntity, //targetEntity
            input.forUsers,
            input.label,
            input.includeUsers,
            input.includeEntities,
            input.includeMetaSpecificToEntityAndItsEntities,
            input.includeOriginUser,
            input.entitiesToExclude,
            input.comment,
            true, //withUserRestriction,
            true); //shouldCommit
        
        par.appendUserNameToLabel = input.appendUserNameToLabel;
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(entityServ.entityCopy(SSClientE.rest, par)).build();
        
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
  @ApiOperation(
    value = "retrieve users who can access given entity",
    response = SSUserEntityUsersGetRet.class)
  @Path("/{entity}/users")
  public Response entityUsersGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.entity)
    final String entity){
    
    final SSUserEntityUsersGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSUserEntityUsersGetPar(
            new SSServPar(sqlCon),
            null,  //user
            SSUri.get(entity, SSConf.sssUri),  //entity
            false, //invokeEntityHandlers
            true); //withUserRestriction
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        return Response.status(200).entity(userServ.userEntityUsersGet(SSClientE.rest, par)).build();
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
  @Path("/{entity}/comments")
  @ApiOperation(
    value = "add comments to the entity",
    response = SSCommentsAddPar.class)
  public Response commentsAdd(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.entity)
    final String entity,
    
    final SSCommentsAddRESTPar input){
    
    final SSCommentsAddPar       par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCommentsAddPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(entity, SSConf.sssUri), //entity
            input.comments, //comments
            true, //withUserRestriction
            true);  //shouldCommit
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        return Response.status(200).entity(commentServ.commentsAdd(SSClientE.rest, par)).build();
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

//  @GET
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @ApiOperation(
//    value = "retrieve entities",
//    response = SSEntitiesGetRet.class)
//  @Path("/")
//  @Deprecated
//  public Response entitiesAccessibleGet(
//    @Context
//    final HttpHeaders headers){
//
//    final SSEntitiesGetPar par;
//
//    try{
//
//      par =
//        new SSEntitiesGetPar(
//          null,  //user
//          null,  //entities
//          null, //types
//          null, //descPar
//          true); //withUserRestriction
//
//    }catch(Exception error){
//      return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
//    }
//
//    return SSRestMainV2.handleRequest(headers, par, false, true).response;
//  }