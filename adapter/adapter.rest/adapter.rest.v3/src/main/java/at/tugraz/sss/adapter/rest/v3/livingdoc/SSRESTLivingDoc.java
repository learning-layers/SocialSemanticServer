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
package at.tugraz.sss.adapter.rest.v3.livingdoc;

import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.adapter.rest.v3.SSRestMain;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.livingdocument.api.*;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocAddPar;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocGetPar;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocRemovePar;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocUpdatePar;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocsGetPar;
import at.tugraz.sss.servs.livingdocument.datatype.ret.SSLivingDocAddRet;
import at.tugraz.sss.servs.livingdocument.datatype.ret.SSLivingDocGetRet;
import at.tugraz.sss.servs.livingdocument.datatype.ret.SSLivingDocRemoveRet;
import at.tugraz.sss.servs.livingdocument.datatype.ret.SSLivingDocUpdateRet;
import at.tugraz.sss.servs.livingdocument.datatype.ret.SSLivingDocsGetRet;
import io.swagger.annotations.*;
import java.sql.*;
import javax.annotation.*;
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

@Path("/livingdocs")
@Api( value = "livingdocs")
public class SSRESTLivingDoc{
  
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
    value = "retrieve living docs for a user",
    response = SSLivingDocsGetRet.class)
  public Response livingDocsGet(
    @Context
    final HttpHeaders headers){
    
    final SSLivingDocsGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRestMain.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLivingDocsGetPar(
            new SSServPar(sqlCon),
            null,
            null, //forUser
            true,  //withUserRestriction
            true); //invokeEntityHandlers
        
      }catch(Exception error){
        return Response.status(422).entity(SSRestMain.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRestMain.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRestMain.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSLivingDocClientI ldServ = (SSLivingDocClientI) SSServReg.getClientServ(SSLivingDocClientI.class);
        
        return Response.status(200).entity(ldServ.livingDocsGet(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRestMain.prepareErrorResponse(error);
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
  @Path    ("/filtered/{livingDoc}")
  @ApiOperation(
    value = "retrieve living doc for a user",
    response = SSLivingDocGetRet.class)
  public Response livingDocFilteredGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.livingDoc)
    final String livingDoc,
    
    final SSLivingDocsGetRESTPar input){
    
    final SSLivingDocGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRestMain.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLivingDocGetPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(livingDoc, SSConf.sssUri),
            true,  //withUserRestriction
            true); //invokeEntityHandlers
        
        par.setUsers = input.setUsers;
        par.setDiscs = input.setDiscs;
        
      }catch(Exception error){
        return Response.status(422).entity(SSRestMain.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRestMain.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRestMain.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSLivingDocClientI ldServ = (SSLivingDocClientI) SSServReg.getClientServ(SSLivingDocClientI.class);
        
        return Response.status(200).entity(ldServ.livingDocGet(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRestMain.prepareErrorResponse(error);
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
  @Path    ("/filtered")
  @ApiOperation(
    value = "retrieve living docs for a user",
    response = SSLivingDocsGetRet.class)
  public Response livingDocsFilteredGet(
    @Context
    final HttpHeaders headers,
    
    final SSLivingDocsGetRESTPar input){
    
    final SSLivingDocsGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRestMain.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLivingDocsGetPar(
            new SSServPar(sqlCon),
            null,
            null, //forUser
            true,  //withUserRestriction
            true); //invokeEntityHandlers
        
        par.setUsers = input.setUsers;
        par.setDiscs = input.setDiscs;
        
      }catch(Exception error){
        return Response.status(422).entity(SSRestMain.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRestMain.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRestMain.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSLivingDocClientI ldServ = (SSLivingDocClientI) SSServReg.getClientServ(SSLivingDocClientI.class);
        
        return Response.status(200).entity(ldServ.livingDocsGet(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRestMain.prepareErrorResponse(error);
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
  @Path    ("/{livingDoc}")
  @ApiOperation(
    value = "retrieve a living doc",
    response = SSLivingDocGetRet.class)
  public Response livingDocGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.livingDoc)
    final String livingDoc){
    
    final SSLivingDocGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRestMain.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLivingDocGetPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(livingDoc, SSConf.sssUri),
            true,  //withUserRestriction
            true); //invokeEntityHandlers
        
      }catch(Exception error){
        return Response.status(422).entity(SSRestMain.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRestMain.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRestMain.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSLivingDocClientI ldServ = (SSLivingDocClientI) SSServReg.getClientServ(SSLivingDocClientI.class);
        
        return Response.status(200).entity(ldServ.livingDocsGet(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRestMain.prepareErrorResponse(error);
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
    value = "add a living doc",
    response = SSLivingDocAddRet.class)
  public Response livingDocAdd(
    @Context
    final HttpHeaders headers,
    
    final SSLivingDocAddRESTPar input){
    
    final SSLivingDocAddPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRestMain.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLivingDocAddPar(
            new SSServPar(sqlCon),
            null,
            input.uri,
            input.label,
            input.description,
            input.discussion,
            true,  //withUserRestriction
            true); //shouldCommit
        
      }catch(Exception error){
        return Response.status(422).entity(SSRestMain.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRestMain.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRestMain.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSLivingDocClientI ldServ = (SSLivingDocClientI) SSServReg.getClientServ(SSLivingDocClientI.class);
        
        return Response.status(200).entity(ldServ.livingDocAdd(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRestMain.prepareErrorResponse(error);
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
  @Path    ("/{livingDoc}")
  @ApiOperation(
    value = "remove a living doc",
    response = SSLivingDocRemoveRet.class)
  public Response livingDocRemove(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.livingDoc)
    final String livingDoc){
    
    final SSLivingDocRemovePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRestMain.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLivingDocRemovePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(livingDoc, SSConf.sssUri),
            true,  //withUserRestriction
            true); //shouldCommit
        
      }catch(Exception error){
        return Response.status(422).entity(SSRestMain.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRestMain.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRestMain.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSLivingDocClientI ldServ = (SSLivingDocClientI) SSServReg.getClientServ(SSLivingDocClientI.class);
        
        return Response.status(200).entity(ldServ.livingDocRemove(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRestMain.prepareErrorResponse(error);
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
  @Path    ("/{livingDoc}")
  @ApiOperation(
    value = "update a living doc",
    response = SSLivingDocUpdateRet.class)
  public Response livingDocUpdate(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.livingDoc)
    final String livingDoc,
    
    final SSLivingDocUpdateRESTPar input){
    
    final SSLivingDocUpdatePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRestMain.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLivingDocUpdatePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(livingDoc, SSConf.sssUri),
            input.label,
            input.description,
            input.discussion,
            true,  //withUserRestriction
            true); //shouldCommit
        
      }catch(Exception error){
        return Response.status(422).entity(SSRestMain.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRestMain.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRestMain.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSLivingDocClientI ldServ = (SSLivingDocClientI) SSServReg.getClientServ(SSLivingDocClientI.class);
        
        return Response.status(200).entity(ldServ.livingDocUpdate(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRestMain.prepareErrorResponse(error);
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
