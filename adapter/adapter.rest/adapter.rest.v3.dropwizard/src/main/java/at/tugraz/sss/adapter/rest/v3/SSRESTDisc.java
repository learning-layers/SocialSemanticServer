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

import at.tugraz.sss.servs.disc.api.*;
import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.servs.disc.datatype.SSDiscEntryAcceptPar;
import at.tugraz.sss.servs.disc.datatype.SSDiscEntryAddFromClientPar;
import at.tugraz.sss.servs.disc.datatype.SSDiscEntryAddPar;
import at.tugraz.sss.servs.disc.datatype.SSDiscEntryUpdatePar;
import at.tugraz.sss.servs.disc.datatype.SSDiscGetPar;
import at.tugraz.sss.servs.disc.datatype.SSDiscTargetsAddPar;
import at.tugraz.sss.servs.disc.datatype.SSDiscUpdatePar;
import at.tugraz.sss.servs.disc.datatype.SSDiscsGetPar;
import at.tugraz.sss.servs.disc.datatype.SSDiscEntryAcceptRet;
import at.tugraz.sss.servs.disc.datatype.SSDiscEntryAddRet;
import at.tugraz.sss.servs.disc.datatype.SSDiscEntryUpdateRet;
import at.tugraz.sss.servs.disc.datatype.SSDiscGetRet;
import at.tugraz.sss.servs.disc.datatype.SSDiscTargetsAddRet;
import at.tugraz.sss.servs.disc.datatype.SSDiscUpdateRet;
import at.tugraz.sss.servs.disc.datatype.SSDiscsGetRet;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.servs.db.impl.*;
import at.tugraz.sss.servs.disc.datatype.*;
import at.tugraz.sss.servs.disc.impl.*;
import io.swagger.annotations.*;
import java.sql.*;
import javax.annotation.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("rest/discs")
@Api( value = "discs")
public class SSRESTDisc{
  
  private final SSDiscClientI discServ = new SSDiscImpl();
  private final SSDBSQLI          dbSQL        = new SSDBSQLMySQLImpl();
  
  @PostConstruct
  public void createRESTResource(){
  }
  
  @PreDestroy
  public void destroyRESTResource(){
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered")
  @ApiOperation(
    value = "retrieve discussions",
    response = SSDiscsGetRet.class)
  public Response discsGet(
    @Context
    final HttpHeaders headers,
    
    final SSDiscsGetRESTPar input){
    
    final SSDiscsGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSDiscsGetPar(
            new SSServPar(sqlCon),
            null, //user,
            input.setEntries, //setEntries,
            input.forUser, //forUser,
            null, //discs,
            null, //target,
            true, //withUserRestriction,
            true); //invokeEntityHandlers
        
        par.setCircleTypes      = input.setCircleTypes;
        par.setComments         = input.setComments;
        par.setLikes            = input.setLikes;
        par.setTags             = input.setTags;
        par.setAttachedEntities = input.setAttachedEntities;
        par.setReads            = input.setReads;
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(discServ.discsGet(SSClientE.rest, par)).build();
        
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
    value = "add a textual comment/answer/opinion to a discussion [for given entity] or create a new discussion",
    response = SSDiscEntryAddRet.class)
  public Response discEntryAdd(
    @Context
    final HttpHeaders headers,
    
    final SSDiscEntryAddRESTPar input){
    
    final SSDiscEntryAddPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSDiscEntryAddFromClientPar(
            new SSServPar(sqlCon),
            input.disc, //disc
            input.targets, //targets,
            input.entry, //entry
            input.addNewDisc, //addNewDisc
            input.type, //type
            input.label, //label
            input.description, //description
            input.users, //users
            input.circles, //circles
            input.entities, //entities
            input.entityLabels); //entityLabels);
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(discServ.discEntryAdd(SSClientE.rest, par)).build();
        
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
  @Path("/{disc}")
  @ApiOperation(
    value = "update discussion",
    response = SSDiscUpdateRet.class)
  public Response discUpdate(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.disc)
    final String disc,
    
    final SSDiscUpdateRESTPar input){
    
    final SSDiscUpdatePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSDiscUpdatePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(disc, SSConf.sssUri),
            input.label,
            input.content,
            input.entitiesToRemove,
            input.entitiesToAttach,
            input.entityLabels,
            input.read,
            true, //withUserRestriction,
            true);//shouldCommit);
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(discServ.discUpdate(SSClientE.rest, par)).build();
        
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
  @Path("/entries/{entry}")
  @ApiOperation(
    value = "update a textual comment/answer/opinion of a discussion",
    response = SSDiscEntryUpdateRet.class)
  public Response discEntryUpdate(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.entry)
    final String entry,
    
    final SSDiscEntryUpdateRESTPar input){
    
    final SSDiscEntryUpdatePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSDiscEntryUpdatePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(entry, SSConf.sssUri),
            input.content,
            input.entitiesToRemove,
            input.entitiesToAttach,
            input.entityLabels,
            true, //withUserRestriction,
            true);//shouldCommit);
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(discServ.discEntryUpdate(SSClientE.rest, par)).build();
        
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
  @Path("/filtered/{disc}")
  @ApiOperation(
    value = "retrieve a discussion with its entries",
    response = SSDiscGetRet.class)
  public Response discWithEntriesGet(
    @Context
    final HttpHeaders  headers,
    
    @PathParam (SSVarNames.disc)
    final String disc,
    
    final SSDiscGetRESTPar input){
    
    final SSDiscGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSDiscGetPar(
            new SSServPar(sqlCon),
            null, //user
            SSUri.get(disc, SSConf.sssUri), //disc
            input.setEntries, //setEntries
            true, //withUserRestriction
            true); //invokeEntityHandlers
        
        par.setCircleTypes      = input.setCircleTypes;
        par.setComments         = input.setComments;
        par.setLikes            = input.setLikes;
        par.setTags             = input.setTags;
        par.setAttachedEntities = input.setAttachedEntities;
        par.setReads            = input.setReads;
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(discServ.discGet(SSClientE.rest, par)).build();
        
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
  @Path("/targets/{targets}")
  @ApiOperation(
    value = "retrieve discussions for certain entities, i.e., targets",
    response = SSDiscsGetRet.class)
  public Response discsForTargetsGet(
    @Context
    final HttpHeaders  headers,
    
    @PathParam (SSVarNames.targets)
    final String targets){
    
    final SSDiscsGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSDiscsGetPar(
            new SSServPar(sqlCon),
            null, //user
            true, //setEntries
            null, //forUser
            null, //discs
            SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(targets, SSStrU.comma), SSConf.sssUri), //targets
            true, //withUserRestriction
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
        
        
        return Response.status(200).entity(discServ.discsGet(SSClientE.rest, par)).build();
        
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
  @Path("/filtered/targets/{targets}")
  @ApiOperation(
    value = "retrieve discussions for certain entities, i.e., targets",
    response = SSDiscsGetRet.class)
  public Response discsForTargetsFilteredGet(
    @Context
    final HttpHeaders  headers,
    
    @PathParam (SSVarNames.targets)
    final String targets,
    
    final SSDiscsForTargetsFilteredGetRESTPar input){
    
    final SSDiscsGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSDiscsGetPar(
            new SSServPar(sqlCon),
            null, //user
            true, //setEntries
            input.forUser, //forUser
            null, //discs
            SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(targets, SSStrU.comma), SSConf.sssUri), //targets
            true, //withUserRestriction
            true); //invokeEntityHandlers
        
        par.setCircleTypes      = input.setCircleTypes;
        par.setComments         = input.setComments;
        par.setLikes            = input.setLikes;
        par.setTags             = input.setTags;
        par.setAttachedEntities = input.setAttachedEntities;
        par.setReads            = input.setReads;
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(discServ.discsGet(SSClientE.rest, par)).build();
        
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
  @Path("/entry/{entry}/accept")
  @ApiOperation(
    value = "accept a qa answer",
    response = SSDiscEntryAcceptRet.class)
  public Response discEntryAccept(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.entry)
    final String entry){
    
    final SSDiscEntryAcceptPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSDiscEntryAcceptPar(
            new SSServPar(sqlCon),
            null, //user
            SSUri.get(entry, SSConf.sssUri), //entry
            true, //withUserRestriction,
            true); //shouldComit
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(discServ.discEntryAccept(SSClientE.rest, par)).build();
        
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
  @Path("/{disc}/targets/{targets}")
  @ApiOperation(
    value = "add targets to a discussion",
    response = SSDiscTargetsAddRet.class)
  public Response discTargetsAdd(
    @Context
    final HttpHeaders headers,
    
    @PathParam (SSVarNames.disc)
    final String disc,
    
    @PathParam (SSVarNames.targets)
    final String targets){
    
    final SSDiscTargetsAddPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSDiscTargetsAddPar(
            new SSServPar(sqlCon),
            null, //user,
            SSUri.get(disc, SSConf.sssUri),
            SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(targets, SSStrU.comma), SSConf.sssUri),
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
        
        
        return Response.status(200).entity(discServ.discTargetsAdd(SSClientE.rest, par)).build();
        
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