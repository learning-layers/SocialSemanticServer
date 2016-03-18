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
package at.tugraz.sss.adapter.rest.v3.learnep;

import at.kc.tugraz.ss.serv.datatypes.learnep.api.*;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.*;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.*;
import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.adapter.rest.v3.SSRESTCommons;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.*;
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

@Path("/learneps")
@Api( value = "learneps")
public class SSRESTLearnEp{
  
  @PostConstruct
  public void createRESTResource(){
  }
  
  @PreDestroy
  public void destroyRESTResource(){
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{learnEp}/structure/circles/entities")
  @ApiOperation(
    value = "retrieve the circle bit structure for episode",
    response = SSLearnEpCircleEntityStructureGetRet.class)
  public Response learnEpCircleEntityStructureGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEp)
    final String learnEp){
    
    final SSLearnEpCircleEntityStructureGetPar   par;
    Connection                                   sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpCircleEntityStructureGetPar(
            new SSServPar(sqlCon),
            null, //user
            SSUri.get(learnEp, SSConf.sssUri), //learnEp
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpCircleEntityStructureGet(SSClientE.rest, par)).build();
        
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
    value = "retrieve learning episodes for a user",
    response = SSLearnEpsGetRet.class)
  public Response learnEpsGet(
    @Context
    final HttpHeaders headers){
    
    final SSLearnEpsGetPar   par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpsGetPar(
            new SSServPar(sqlCon),
            null, //user
            null, //forUser
            true, //withUserRestriction
            true); //invokeEntityHandlers
        
        par.setCircleTypes      = true;
        par.setAttachedEntities = true;
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpsGet(SSClientE.rest, par)).build();
        
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
  @Path    ("/{learnEp}/versions")
  @ApiOperation(
    value = "retrieve learning episode versions for given episode",
    response = SSLearnEpVersionsGetRet.class)
  public Response learnEpVersionsGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEp)
    final String learnEp){
    
    final SSLearnEpVersionsGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpVersionsGetPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(learnEp, SSConf.sssUri),  //learnEp
            null, //learnEpVersions
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpVersionsGet(SSClientE.rest, par)).build();
        
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
  @Path    ("/versions/{learnEpVersion}")
  @ApiOperation(
    value = "retrieve given learning episode version",
    response = SSLearnEpVersionGetRet.class)
  public Response learnEpVersionGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpVersion)
    final String learnEpVersion){
    
    final SSLearnEpVersionGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpVersionGetPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(learnEpVersion, SSConf.sssUri),
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpVersionGet(SSClientE.rest, par)).build();
        
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
  @Path    ("/versions/current")
  @ApiOperation(
    value = "retrieve current learning episode version",
    response = SSLearnEpVersionCurrentGetRet.class)
  public Response learnEpVersionCurrentGet(
    @Context
    final HttpHeaders headers){
    
    final SSLearnEpVersionCurrentGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpVersionCurrentGetPar(
            new SSServPar(sqlCon),
            null,
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpVersionCurrentGet(SSClientE.rest, par)).build();
        
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
  @Path    ("/versions/current/{learnEpVersion}")
  @ApiOperation(
    value = "set current learning episode version",
    response = SSLearnEpVersionCurrentSetRet.class)
  public Response learnEpVersionCurrentSet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpVersion)
    final String learnEpVersion){
    
    final SSLearnEpVersionCurrentSetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpVersionCurrentSetPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(learnEpVersion, SSConf.sssUri),
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpVersionCurrentSet(SSClientE.rest, par)).build();
        
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
    value = "create learning episode",
    response = SSLearnEpCreateRet.class)
  public Response learnEpCreate(
    @Context
    final HttpHeaders headers,
    
    final SSLearnEpCreateRESTPar input){
    
    final SSLearnEpCreatePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpCreatePar(
            new SSServPar(sqlCon),
            null,
            input.label,
            input.description,
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpCreate(SSClientE.rest, par)).build();
        
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
  @Path    ("/{learnEp}/versions")
  @ApiOperation(
    value = "create learning episode version",
    response = SSLearnEpVersionCreateRet.class)
  public Response learnEpVersionCreate(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEp)
    final String learnEp){
    
    final SSLearnEpVersionCreatePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpVersionCreatePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(learnEp, SSConf.sssUri),
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpVersionCreate(SSClientE.rest, par)).build();
        
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
  @Path    ("/{learnEp}")
  @ApiOperation(
    value = "remove learning episode",
    response = SSLearnEpRemoveRet.class)
  public Response learnEpRemove(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEp)
    final String learnEp){
    
    final SSLearnEpRemovePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpRemovePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(learnEp, SSConf.sssUri),
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpRemove(SSClientE.rest, par)).build();
        
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
  @Path    ("/versions/{learnEpVersion}/circles")
  @ApiOperation(
    value = "add a circle to given learning episode version",
    response = SSLearnEpVersionCircleAddRet.class)
  public Response learnEpVersionCircleAdd(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpVersion)
    final String learnEpVersion,
    
    final SSLearnEpVersionAddCircleRESTPar input){
    
    final SSLearnEpVersionCircleAddPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpVersionCircleAddPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(learnEpVersion, SSConf.sssUri),
            input.label,
            input.xLabel,
            input.yLabel,
            input.xR,
            input.yR,
            input.xC,
            input.yC,
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpVersionCircleAdd(SSClientE.rest, par)).build();
        
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
  @Path    ("/versions/{learnEpVersion}/entities")
  @ApiOperation(
    value = "add an entity to given learning episode version",
    response = SSLearnEpVersionEntityAddRet.class)
  public Response learnEpVersionEntityAdd(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpVersion)
    final String learnEpVersion,
    
    final SSLearnEpVersionEntityAddRESTPar input){
    
    final SSLearnEpVersionEntityAddPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpVersionEntityAddPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(learnEpVersion, SSConf.sssUri),
            input.entity,
            input.x,
            input.y,
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpVersionEntityAdd(SSClientE.rest, par)).build();
        
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
  @Path    ("/circles/{learnEpCircle}")
  @ApiOperation(
    value = "update a circle",
    response = SSLearnEpVersionCircleUpdateRet.class)
  public Response learnEpVersionCircleUpdate(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpCircle)
    final String learnEpCircle,
    
    final SSLearnEpVersionCircleUpdateRESTPar input){
    
    final SSLearnEpVersionCircleUpdatePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpVersionCircleUpdatePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(learnEpCircle, SSConf.sssUri),
            input.label,
            input.description,
            input.xLabel,
            input.yLabel,
            input.xR,
            input.yR,
            input.xC,
            input.yC,
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpVersionCircleUpdate(SSClientE.rest, par)).build();
        
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
  @Path    ("/entities/{learnEpEntity}")
  @ApiOperation(
    value = "update an entity",
    response = SSLearnEpVersionEntityUpdateRet.class)
  public Response learnEpVersionEntityUpdate(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpEntity)
    final String learnEpEntity,
    
    final SSLearnEpVersionEntityUpdateRESTPar input){
    
    final SSLearnEpVersionEntityUpdatePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpVersionEntityUpdatePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(learnEpEntity, SSConf.sssUri),
            input.entity,
            input.x,
            input.y,
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpVersionEntityUpdate(SSClientE.rest, par)).build();
        
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
  @Path    ("/circles/{learnEpCircle}")
  @ApiOperation(
    value = "remove a circle",
    response = SSLearnEpVersionCircleRemoveRet.class)
  public Response learnEpVersionCircleRemove(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpCircle)
    final String learnEpCircle){
    
    final SSLearnEpVersionCircleRemovePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpVersionCircleRemovePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(learnEpCircle, SSConf.sssUri),
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpVersionCircleRemove(SSClientE.rest, par)).build();
        
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
  @Path    ("/entities/{learnEpEntity}")
  @ApiOperation(
    value = "remove an entity",
    response = SSLearnEpVersionEntityRemoveRet.class)
  public Response learnEpVersionEntityRemove(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEpEntity)
    final String learnEpEntity){
    
    final SSLearnEpVersionEntityRemovePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpVersionEntityRemovePar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(learnEpEntity, SSConf.sssUri),
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpVersionEntityRemove(SSClientE.rest, par)).build();
        
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
  @Path    ("/timelinestate")
  @ApiOperation(
    value = "set timeline state",
    response = SSLearnEpTimelineStateSetRet.class)
  public Response learnEpTimelineStateSet(
    @Context
    final HttpHeaders headers,
    
    final SSLearnEpTimelineStateSetRESTPar input){
    
    final SSLearnEpTimelineStateSetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpTimelineStateSetPar(
            new SSServPar(sqlCon),
            null,
            input.startTime,
            input.endTime,
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpTimelineStateSet(SSClientE.rest, par)).build();
        
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
  @Path    ("/timelinestate")
  @ApiOperation(
    value = "get timeline state",
    response = SSLearnEpTimelineStateGetRet.class)
  public Response learnEpTimelineStateGet(
    @Context
    final HttpHeaders headers){
    
    final SSLearnEpTimelineStateGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpTimelineStateGetPar(
            new SSServPar(sqlCon),
            null,
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpTimelineStateGet(SSClientE.rest, par)).build();
        
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
  @Path    ("/{learnEps}/locks")
  @ApiOperation(
    value = "get lock information on given learning episodes",
    response = SSLearnEpsLockHoldRet.class)
  public Response learnEpsLockHold(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEps)
    final String learnEps){
    
    final SSLearnEpsLockHoldPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpsLockHoldPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(learnEps, SSStrU.comma), SSConf.sssUri),
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpsLockHold(SSClientE.rest, par)).build();
        
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
  @Path    ("/{learnEp}/locks")
  @ApiOperation(
    value = "set lock on given learning episode",
    response = SSLearnEpLockSetRet.class)
  public Response learnEpLockSet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEp)
    final String learnEp){
    
    final SSLearnEpLockSetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpLockSetPar(
            new SSServPar(sqlCon),
            null,
            null, //forUser
            SSUri.get(learnEp, SSConf.sssUri),
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpLockSet(SSClientE.rest, par)).build();
        
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
  @Path    ("/{learnEp}/locks")
  @ApiOperation(
    value = "remove lock on given learning episode",
    response = SSLearnEpLockRemoveRet.class)
  public Response learnEpLockRemove(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.learnEp)
    final String learnEp){
    
    final SSLearnEpLockRemovePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSLearnEpLockRemovePar(
            new SSServPar(sqlCon),
            null,
            null, //forUser
            SSUri.get(learnEp, SSConf.sssUri),
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
        final SSLearnEpClientI learnEpServ = (SSLearnEpClientI) SSServReg.getClientServ(SSLearnEpClientI.class);
        
        return Response.status(200).entity(learnEpServ.learnEpLockRemove(SSClientE.rest, par)).build();
        
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