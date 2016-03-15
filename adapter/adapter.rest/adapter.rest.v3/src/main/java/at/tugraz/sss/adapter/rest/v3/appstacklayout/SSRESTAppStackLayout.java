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
package at.tugraz.sss.adapter.rest.v3.appstacklayout;

import at.kc.tugraz.sss.appstacklayout.api.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.adapter.rest.v3.SSRESTCommons;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.conf.SSConf;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutCreatePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutDeletePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutUpdatePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutsGetPar;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutCreateRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutDeleteRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutUpdateRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutsGetRet;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.*;
import io.swagger.annotations.*;
import java.sql.*;
import javax.annotation.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/appstacklayouts")
@Api( value = "appstacklayouts")
public class SSRESTAppStackLayout{
  
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
    value = "retrieve appStackLayouts",
    response = SSAppStackLayoutsGetRet.class)
  public Response appStackLayoutsAccessibleGet(
    @Context
    final HttpHeaders headers){
    
    final SSAppStackLayoutsGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        par =
          new SSAppStackLayoutsGetPar(
            new SSServPar(sqlCon),
            null,
            null, //stacks
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
        final SSAppStackLayoutClientI appStackLayoutServ = (SSAppStackLayoutClientI) SSServReg.getClientServ(SSAppStackLayoutClientI.class);
        
        return Response.status(200).entity(appStackLayoutServ.appStackLayoutsGet(SSClientE.rest, par)).build();
        
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
  @Path    ("/{stacks}")
  @ApiOperation(
    value = "retrieve given appStackLayouts",
    response = SSAppStackLayoutsGetRet.class)
  public Response appStackLayoutsGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.stacks)
    final String stacks){
    
    final SSAppStackLayoutsGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        par =
          new SSAppStackLayoutsGetPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(stacks, SSStrU.comma), SSConf.sssUri), //stacks
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
        final SSAppStackLayoutClientI appStackLayoutServ = (SSAppStackLayoutClientI) SSServReg.getClientServ(SSAppStackLayoutClientI.class);
        
        return Response.status(200).entity(appStackLayoutServ.appStackLayoutsGet(SSClientE.rest, par)).build();
        
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
    value = "create an arrangement of tiles within an app",
    response = SSAppStackLayoutCreateRet.class)
  public Response appStackLayoutCreatePost(
    @Context HttpHeaders                     headers,
    final SSAppStackLayoutCreateRESTPar input){
    
    final SSAppStackLayoutCreatePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        par =
          new SSAppStackLayoutCreatePar(
            new SSServPar(sqlCon),
            null,
            input.uuid,
            input.app,
            input.label,
            input.description,
            true, //withUserRestriction,
            true); //shouldCommit);
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSAppStackLayoutClientI appStackLayoutServ = (SSAppStackLayoutClientI) SSServReg.getClientServ(SSAppStackLayoutClientI.class);
        
        return Response.status(200).entity(appStackLayoutServ.appStackLayoutCreate(SSClientE.rest, par)).build();
        
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
  @Path    ("/{stack}")
  @ApiOperation(
    value = "delete an arrangement of tiles within an app",
    response = SSAppStackLayoutDeleteRet.class)
  public Response appStackLayoutDelete(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.stack)
    final String stack){
    
    final SSAppStackLayoutDeletePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        par =
          new SSAppStackLayoutDeletePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(stack, SSConf.sssUri), //stack,
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
        final SSAppStackLayoutClientI appStackLayoutServ = (SSAppStackLayoutClientI) SSServReg.getClientServ(SSAppStackLayoutClientI.class);
        
        return Response.status(200).entity(appStackLayoutServ.appStackLayoutDelete(SSClientE.rest, par)).build();
        
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
  @Path    ("/{stack}")
  @ApiOperation(
    value = "update an arrangement of tiles within an app",
    response = SSAppStackLayoutUpdateRet.class)
  public Response appStackLayoutUpdate(
    @Context
      HttpHeaders headers,
    
    @PathParam(SSVarNames.stack)
      String stack,
    
    final SSAppStackLayoutUpdateRESTPar input){
    
    final SSAppStackLayoutUpdatePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        par =
          new SSAppStackLayoutUpdatePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(stack, SSConf.sssUri),
            input.app,
            input.label,
            input.description,
            true,  //withUserDescription
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
        final SSAppStackLayoutClientI appStackLayoutServ = (SSAppStackLayoutClientI) SSServReg.getClientServ(SSAppStackLayoutClientI.class);
        
        return Response.status(200).entity(appStackLayoutServ.appStackLayoutUpdate(SSClientE.rest, par)).build();
        
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