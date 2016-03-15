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
package at.tugraz.sss.adapter.rest.v3.recomm;

import at.kc.tugraz.ss.recomm.api.*;
import at.kc.tugraz.ss.recomm.datatypes.par.*;
import at.kc.tugraz.ss.recomm.datatypes.ret.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.adapter.rest.v3.SSRESTCommons;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.*;
import io.swagger.annotations.*;
import java.io.*;
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
import org.glassfish.jersey.media.multipart.*;

@Path("rest/recomm")
@Api( value = "recomm")
public class SSRESTRecomm{
  
  @PostConstruct
  public void createRESTResource(){
  }
  
  @PreDestroy
  public void destroyRESTResource(){
  }
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/update")
  @ApiOperation(
    value = "add a new combination of user, entity, tags, categories to be used for recommendations",
    response = SSRecommUpdateRet.class)
  public Response recommUpdate(
    @Context
    final HttpHeaders headers,
    
    final SSRecommUpdateRESTPar input){
    
    final SSRecommUpdatePar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommUpdatePar(
            new SSServPar(sqlCon),
            null,
            input.realm,       //realm
            input.forUser,     //forUser
            input.entity,      //entity
            input.tags,        //tags
            input.categories); //categories
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommUpdate(SSClientE.rest, par)).build();
        
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
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/update/bulk/{realm}")
  @ApiOperation(
    value = "add a file containing user, entity, tag, category combinations to be used for recommendations",
    response = SSRecommUpdateBulkRet.class)
  public Response recommUpdateBulk(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "recomm realm the user wants to query",
      required = true)
    @PathParam(SSVarNames.realm)
    final String realm,
    
//    @ApiParam(
//      value = "data file containing: user1;entity1;;tag1,tag2;cat1,cat2;",
//      required = true)
    @FormDataParam("file")
    final InputStream file){
    
    final SSRecommUpdateBulkPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommUpdateBulkPar(
            new SSServPar(sqlCon),
            null, //user
            realm, //realm
            null, //clientSocket
            file, //fileInputStream
            SSClientE.rest); //clientType
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommUpdateBulk(SSClientE.rest, par)).build();
        
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
  @Path    ("/update/bulk/entities")
  @ApiOperation(
    value = "add tags, categories for the user's given entities to be used for recommendations",
    response = SSRecommUpdateRet.class)
  public Response recommUpdateBulkEntities(
    @Context
    final HttpHeaders headers,
    
    final SSRecommUpdateBulkEntitiesRESTPar input){
    
    final SSRecommUpdateBulkEntitiesPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommUpdateBulkEntitiesPar(
            new SSServPar(sqlCon),
            null,
            input.realm,         //realm
            input.forUser,       //forUser
            input.entities,      //entity
            input.tags,          //tags
            input.categories);   //categories
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommUpdateBulkEntities(SSClientE.rest, par)).build();
        
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
  @Path    ("/users")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsers(
    @Context
    final HttpHeaders headers){
    
    final SSRecommUsersPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommUsersPar(
            new SSServPar(sqlCon),
            null,
            null, //realm
            null,  //forUser
            null,  //entity
            null,  //categories
            10, //maxUsers
            false, //ignoreAccessRights
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
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommUsers(SSClientE.rest, par)).build();
        
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
  @Path    ("/users/realm/{realm}")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsers(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "recomm realm the user wants to query",
      required = true)
    @PathParam("realm")
    final String realm){
    
    final SSRecommUsersPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommUsersPar(
            new SSServPar(sqlCon),
            null,
            realm, //realm
            null,  //forUser
            null,  //entity
            null,  //categories
            10, //maxUsers
            false, //ignoreAccessRights
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
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommUsers(SSClientE.rest, par)).build();
        
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
  @Path    ("/users/ignoreaccessrights/realm/{realm}")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsersIgnoreAccessRights(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "recomm realm the user wants to query",
      required = true)
    @PathParam("realm")
    final String realm){
    
    final SSRecommUsersPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommUsersPar(
            new SSServPar(sqlCon),
            null,
            realm, //realm
            null,  //forUser
            null,  //entity
            null,  //categories
            10, //maxUsers
            true, //ignoreAccessRights
            true, //withUserRestriction
            false); //invokeEntityHandlers
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommUsers(SSClientE.rest, par)).build();
        
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
  @Path    ("/users/user/{forUser}/entity/{entity}")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsersForUserEntity(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "user to be considered to retrieve recommendations for",
      required = true)
    @PathParam(SSVarNames.forUser)
      String forUser,
    
    @ApiParam(
      value = "resource to be considered to retrieve recommendations for",
      required = true)
    @PathParam(SSVarNames.entity)
      String entity){
    
    final SSRecommUsersPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommUsersPar(
            new SSServPar(sqlCon),
            null,
            null,                                //realm
            SSUri.get(forUser, SSConf.sssUri), //forUser
            SSUri.get(entity,  SSConf.sssUri), //entity
            null, //categories
            10, //maxUsers
            false, //ignoreAccessRights
            true, // withUserRestriction
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
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommUsers(SSClientE.rest, par)).build();
        
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
  @Path    ("/users/realm/{realm}/user/{forUser}/entity/{entity}")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsersForUserEntity(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "recomm realm the user wants to query",
      required = true)
    @PathParam(SSVarNames.realm)
      String realm,
    
    @ApiParam(
      value = "user to be considered to retrieve recommendations for",
      required = true)
    @PathParam(SSVarNames.forUser)
      String forUser,
    
    @ApiParam(
      value = "resource to be considered to retrieve recommendations for",
      required = true)
    @PathParam(SSVarNames.entity)
      String entity){
    
    final SSRecommUsersPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommUsersPar(
            new SSServPar(sqlCon),
            null,
            realm,                                //realm
            SSUri.get(forUser, SSConf.sssUri), //forUser
            SSUri.get(entity,  SSConf.sssUri), //entity
            null, //categories
            10, //maxUsers
            false, //ignoreAccessRights
            true, // withUserRestriction
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
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommUsers(SSClientE.rest, par)).build();
        
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
  @Path    ("/users/ignoreaccessrights/realm/{realm}/user/{forUser}/entity/{entity}")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsersForUserEntityIgnoreAccessRights(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "recomm realm the user wants to query",
      required = true)
    @PathParam(SSVarNames.realm)
      String realm,
    
    @ApiParam(
      value = "user to be considered to retrieve recommendations for",
      required = true)
    @PathParam(SSVarNames.forUser)
      String forUser,
    
    @ApiParam(
      value = "resource to be considered to retrieve recommendations for",
      required = true)
    @PathParam(SSVarNames.entity)
      String entity){
    
    final SSRecommUsersPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommUsersPar(
            new SSServPar(sqlCon),
            null,
            realm,                                //realm
            SSUri.get(forUser, SSConf.sssUri), //forUser
            SSUri.get(entity,  SSConf.sssUri), //entity
            null, //categories
            10, //maxUsers
            true, //ignoreAccessRights
            true, // withUserRestriction
            false); //invokeEntityHandlers
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommUsers(SSClientE.rest, par)).build();
        
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
  @Path    ("/users/user/{forUser}")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsersForUser(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "user to be considered to retrieve recommendations for",
      required = true)
    @PathParam(SSVarNames.forUser)
    final String forUser){
    
    final SSRecommUsersPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommUsersPar(
            new SSServPar(sqlCon),
            null,
            null,                                //realm
            SSUri.get(forUser, SSConf.sssUri), //forUser
            null, //entity
            null, //categories
            10,  //maxUsers
            false, //ignoreAccessRights
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
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommUsers(SSClientE.rest, par)).build();
        
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
  @Path    ("/users/realm/{realm}/user/{forUser}")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsersForUser(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "recomm realm the user wants to query",
      required = true)
    @PathParam(SSVarNames.realm)
    final String realm,
    
    @ApiParam(
      value = "user to be considered to retrieve recommendations for",
      required = true)
    @PathParam(SSVarNames.forUser)
    final String forUser){
    
    final SSRecommUsersPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommUsersPar(
            new SSServPar(sqlCon),
            null,
            realm,                                //realm
            SSUri.get(forUser, SSConf.sssUri), //forUser
            null, //entity
            null, //categories
            10, //maxUsers,
            false, //ignoreAccessRights
            true, // withUserRestriction
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
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommUsers(SSClientE.rest, par)).build();
        
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
  @Path    ("/users/ignoreaccessrights/realm/{realm}/user/{forUser}")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsersForUserIgnoreAccessRights(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "recomm realm the user wants to query",
      required = true)
    @PathParam(SSVarNames.realm)
    final String realm,
    
    @ApiParam(
      value = "user to be considered to retrieve recommendations for",
      required = true)
    @PathParam(SSVarNames.forUser)
    final String forUser){
    
    final SSRecommUsersPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommUsersPar(
            new SSServPar(sqlCon),
            null,
            realm,                                //realm
            SSUri.get(forUser, SSConf.sssUri), //forUser
            null, //entity
            null, //categories
            10, //maxUsers,
            true, //ignoreAccessRights
            true, // withUserRestriction
            false); //invokeEntityHandlers
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommUsers(SSClientE.rest, par)).build();
        
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
  @Path    ("/users/entity/{entity}")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsersForEntity(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "resource to be considered to retrieve recommendations for",
      required = true)
    @PathParam(SSVarNames.entity)
    final String entity){
    
    final SSRecommUsersPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommUsersPar(
            new SSServPar(sqlCon),
            null,
            null, //realm
            null, //forUser
            SSUri.get(entity, SSConf.sssUri), //entity
            null, //categories
            10, //maxUsers
            false, //ignoreAccessRights
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
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommUsers(SSClientE.rest, par)).build();
        
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
  @Path    ("/users/realm/{realm}/entity/{entity}")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsersForEntity(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "recomm realm the user wants to query",
      required = true)
    @PathParam(SSVarNames.realm)
    final String realm,
    
    @ApiParam(
      value = "resource to be considered to retrieve recommendations for",
      required = true)
    @PathParam(SSVarNames.entity)
    final String entity){
    
    final SSRecommUsersPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommUsersPar(
            new SSServPar(sqlCon),
            null,
            realm, //realm
            null, //forUser
            SSUri.get(entity, SSConf.sssUri), //entity
            null, //categories
            10, //maxUsers
            false,  //ignoreAccessRights
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
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommUsers(SSClientE.rest, par)).build();
        
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
  @Path    ("/users/ignoreaccessrights/realm/{realm}/entity/{entity}")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsersForEntityIgnoreAccessRights(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "recomm realm the user wants to query",
      required = true)
    @PathParam(SSVarNames.realm)
    final String realm,
    
    @ApiParam(
      value = "resource to be considered to retrieve recommendations for",
      required = true)
    @PathParam(SSVarNames.entity)
    final String entity){
    
    final SSRecommUsersPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommUsersPar(
            new SSServPar(sqlCon),
            null,
            realm, //realm
            null, //forUser
            SSUri.get(entity, SSConf.sssUri), //entity
            null, //categories
            10, //maxUsers
            true,  //ignoreAccessRights
            true,  //withUserRestriction
            false); //invokeEntityHandlers
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommUsers(SSClientE.rest, par)).build();
        
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
  @Path    ("/filtered/resources")
  @ApiOperation(
    value = "retrieve resource recommendations",
    response = SSRecommResourcesRet.class)
  public Response recommResources(
    @Context
    final HttpHeaders headers,
    
    final SSRecommResourcesRESTPar input){
    
    final SSRecommResourcesPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommResourcesPar(
            new SSServPar(sqlCon),
            null,
            input.realm,  //realm
            input.forUser,  //forUser
            input.entity,  //entity
            input.categories,  //categories
            input.maxResources,    //maxResources
            input.typesToRecommOnly,  //typesToRecommendOnly
            input.setCircleTypes,  //setCircleTypes
            input.includeOwn, //includeOwn
            false, //ignoreAccessRights
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
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommResources(SSClientE.rest, par)).build();
        
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
  @Path    ("/filtered/tags")
  @ApiOperation(
    value = "retrieve tag recommendations based on user, entity, tag, time and category combinations",
    response = SSRecommTagsRet.class)
  public Response recommTags(
    @Context
    final HttpHeaders headers,
    
    final SSRecommTagsRESTPar input){
    
    final SSRecommTagsPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRecommTagsPar(
            new SSServPar(sqlCon),
            null,
            input.realm,  //realm
            input.forUser,  //forUser
            null,  //entities
            input.categories,  //categories
            input.maxTags,    //maxTags
            input.includeOwn, //includeOwn
            input.ignoreAccessRights, //ignoreAccessRights
            true); //withUserRestriction
        
        SSUri.addDistinctWithoutNull(par.entities, input.entity);
        SSUri.addDistinctWithoutNull(par.entities, input.entities);
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSRecommClientI recommServ = (SSRecommClientI) SSServReg.getClientServ(SSRecommClientI.class);
        
        return Response.status(200).entity(recommServ.recommTags(SSClientE.rest, par)).build();
        
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