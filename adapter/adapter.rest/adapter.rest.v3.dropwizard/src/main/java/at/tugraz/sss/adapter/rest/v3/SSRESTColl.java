/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.adapter.rest.v3;

import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.coll.api.SSCollClientI;
import at.tugraz.sss.servs.coll.datatype.SSCollCumulatedTagsGetPar;
import at.tugraz.sss.servs.coll.datatype.SSCollGetPar;
import at.tugraz.sss.servs.coll.datatype.SSCollUserEntriesAddPar;
import at.tugraz.sss.servs.coll.datatype.SSCollUserEntriesDeletePar;
import at.tugraz.sss.servs.coll.datatype.SSCollUserEntryAddPar;
import at.tugraz.sss.servs.coll.datatype.SSCollUserHierarchyGetPar;
import at.tugraz.sss.servs.coll.datatype.SSCollUserRootGetPar;
import at.tugraz.sss.servs.coll.datatype.SSCollsUserEntityIsInGetPar;
import at.tugraz.sss.servs.coll.datatype.SSCollGetRet;
import at.tugraz.sss.servs.coll.datatype.SSCollUserCumulatedTagsGetRet;
import at.tugraz.sss.servs.coll.datatype.SSCollUserEntriesAddRet;
import at.tugraz.sss.servs.coll.datatype.SSCollUserEntriesDeleteRet;
import at.tugraz.sss.servs.coll.datatype.SSCollUserEntryAddRet;
import at.tugraz.sss.servs.coll.datatype.SSCollUserHierarchyGetRet;
import at.tugraz.sss.servs.coll.datatype.SSCollUserRootGetRet;
import at.tugraz.sss.servs.coll.datatype.SSCollsUserEntityIsInGetRet;
import at.tugraz.sss.servs.util.*;
import at.tugraz.sss.servs.entity.datatype.*;
import at.tugraz.sss.servs.db.api.*;
import at.tugraz.sss.servs.coll.datatype.*;
import at.tugraz.sss.servs.coll.impl.*;
import at.tugraz.sss.servs.db.impl.*;
import io.swagger.annotations.*;
import java.sql.*;
import javax.annotation.*;
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

@Path("rest/colls")
@Api( value = "colls")
public class SSRESTColl{
  
  private final SSCollClientI collServ = new SSCollImpl();
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
    value = "retrieve a user's collection with entries",
    response = SSCollGetRet.class)
  @Path("/{coll}")
  public Response collGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.coll)
    final String coll){
    
    final SSCollGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCollGetPar(
            new SSServPar(sqlCon),
            null,  //user
            SSUri.get(coll, SSConf.sssUri), //entity
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
        
        
        return Response.status(200).entity(collServ.collGet(SSClientE.rest, par)).build();
        
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
    value = "retrieve the user's root collection",
    response = SSCollUserRootGetRet.class)
  @Path("/root")
  public Response collRootGet(
    @Context
    final HttpHeaders headers){
    
    final SSCollUserRootGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCollUserRootGetPar(
            new SSServPar(sqlCon),
            null,  //user
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
        
        
        return Response.status(200).entity(collServ.collRootGet(SSClientE.rest, par)).build();
        
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
    value = "retrieve the parent collection order for a user's collection",
    response = SSCollUserHierarchyGetRet.class)
  @Path("/{coll}/hierarchy")
  public Response collHierarchyGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.coll)
    final String coll){
    
    final SSCollUserHierarchyGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCollUserHierarchyGetPar(
            new SSServPar(sqlCon),
            null,  //user,
            SSUri.get(coll, SSConf.sssUri),
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
        
        
        return Response.status(200).entity(collServ.collHierarchyGet(SSClientE.rest, par)).build();
        
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
    value = "add a (new) collection or any other entity to given user's collection",
    response = SSCollUserEntryAddRet.class)
  @Path("/{coll}")
  public Response collEntryAdd(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.coll)
    final String coll,
    
    final SSCollEntryAddRESTPar input){
    
    final SSCollUserEntryAddPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCollUserEntryAddPar(
            new SSServPar(sqlCon),
            null,  //user,
            SSUri.get(coll, SSConf.sssUri),
            input.entry,
            input.label,
            input.addNewColl,
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
        
        
        return Response.status(200).entity(collServ.collEntryAdd(SSClientE.rest, par)).build();
        
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
    value = "add existing collections or (new) entities to a collection",
    response = SSCollUserEntriesAddRet.class)
  @Path("/{coll}/entries")
  public Response collEntriesAdd(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.coll)
    final String coll,
    
    final SSCollEntriesAddRESTPar input){
    
    final SSCollUserEntriesAddPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCollUserEntriesAddPar(
            new SSServPar(sqlCon),
            null,  //user,
            SSUri.get(coll, SSConf.sssUri),
            input.entries,
            input.labels,
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
        
        
        return Response.status(200).entity(collServ.collEntriesAdd(SSClientE.rest, par)).build();
        
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
    value = "retrieve all the user's collections given entity is in",
    response = SSCollsUserEntityIsInGetRet.class)
  @Path("/entries/{entity}/colls")
  public Response collsEntityIsInGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.entity)
    final String entity){
    
    final SSCollsUserEntityIsInGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCollsUserEntityIsInGetPar(
            new SSServPar(sqlCon),
            null,  //user,
            SSUri.get(entity, SSConf.sssUri),
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
        
        
        return Response.status(200).entity(collServ.collsEntityIsInGet(SSClientE.rest, par)).build();
        
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
    value = "retrieve the cumulated tags (and their frequencies) for all the sub collections and respective entities",
    response = SSCollUserCumulatedTagsGetRet.class)
  @Path("/{coll}/tags/cumulated")
  public Response collCumulatedTagsGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.coll)
    final String coll){
    
    final SSCollCumulatedTagsGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCollCumulatedTagsGetPar(
            new SSServPar(sqlCon),
            null,  //user,
            SSUri.get(coll, SSConf.sssUri),
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
        
        
        return Response.status(200).entity(collServ.collCumulatedTagsGet(SSClientE.rest, par)).build();
        
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
  @ApiOperation(
    value = "delete one or more entries from a collection",
    response = SSCollUserEntriesDeleteRet.class)
  @Path("/{coll}/entries/{entries}")
  public Response collEntriesDelete(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.coll)
    final String coll,
    
    @PathParam(SSVarNames.entries)
    final String entries){
    
    final SSCollUserEntriesDeletePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCollUserEntriesDeletePar(
            new SSServPar(sqlCon),
            null,  //user,
            SSUri.get(coll, SSConf.sssUri),
            SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(entries, SSStrU.comma), SSConf.sssUri),
            true, //withUserRestriction
            true); // shouldCommit
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        
        
        return Response.status(200).entity(collServ.collEntriesDelete(SSClientE.rest, par)).build();
        
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