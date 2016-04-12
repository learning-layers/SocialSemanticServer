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

import at.tugraz.sss.servs.category.datatype.SSCategoryFrequsGetRESTPar;
import at.tugraz.sss.servs.category.datatype.SSCategoryAddRESTPar;
import at.tugraz.sss.servs.category.datatype.SSCategoriesPredefinedGetPar;
import at.tugraz.sss.servs.category.datatype.SSCategoryAddPar;
import at.tugraz.sss.servs.category.datatype.SSCategoryFrequsGetPar;
import at.tugraz.sss.servs.category.datatype.SSCategoriesPredefinedGetRet;
import at.tugraz.sss.servs.category.datatype.SSCategoryAddRet;
import at.tugraz.sss.servs.category.datatype.SSCategoryFrequsGetRet;

import at.tugraz.sss.servs.entity.datatype.*;
import at.tugraz.sss.servs.db.api.*;
import at.tugraz.sss.servs.util.*;
import at.tugraz.sss.servs.category.api.*;
import at.tugraz.sss.servs.category.impl.*;
import at.tugraz.sss.servs.db.impl.*;
import io.swagger.annotations.*;
import java.sql.*;
import javax.annotation.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/categories")
@Api( value = "categories")
public class SSRESTCategory{
  
  private final SSCategoryClientI categoryServ = new SSCategoryImpl();
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
  @Path("/predefined")
  @ApiOperation(
    value = "retrieve predefined categories",
    response = SSCategoriesPredefinedGetRet.class)
  public Response categoriesPredefinedGet(
    @Context
    final HttpHeaders headers){
    
    final SSCategoriesPredefinedGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSCategoriesPredefinedGetPar(
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
        return Response.status(200).entity(categoryServ.categoriesPredefinedGet(SSClientE.rest, par)).build();
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
    value = "add a category for an entity in given space",
    response = SSCategoryAddRet.class)
  public Response categoryAdd(
    @Context
    final HttpHeaders headers,
    
    final SSCategoryAddRESTPar input){
    
    final SSCategoryAddPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        par =
          new SSCategoryAddPar(
            new SSServPar(sqlCon),
            null,
            input.entity, //entity
            input.label, //label
            input.space, //space
            input.circle, //circle
            input.creationTime,  //creationTime
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
        
        
        return Response.status(200).entity(categoryServ.categoryAdd(SSClientE.rest, par)).build();
        
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
  @Path    ("/frequs")
  @ApiOperation(
    value = "retrieve category frequencies",
    response = SSCategoryFrequsGetRet.class)
  public Response categoryFrequsGet(
    @Context
    final HttpHeaders headers){
    
    final SSCategoryFrequsGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        par =
          new SSCategoryFrequsGetPar(
            new SSServPar(sqlCon),
            null, //user
            null, //forUser
            null, //entities
            null, //labels
            null, //spaces
            null, //circles
            null, //startTime
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
        
        
        return Response.status(200).entity(categoryServ.categoryFrequsGet(SSClientE.rest, par)).build();
        
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
  @Path    ("/filtered/frequs")
  @ApiOperation(
    value = "retrieve category frequencies",
    response = SSCategoryFrequsGetRet.class)
  public Response categoryFrequsGetFiltered(
    @Context
    final HttpHeaders headers,
    
    final SSCategoryFrequsGetRESTPar input){
    
    final SSCategoryFrequsGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        par =
          new SSCategoryFrequsGetPar(
            new SSServPar(sqlCon),
            null,
            input.forUser, //forUser
            input.entities, //entities
            input.labels, //labels
            SSSpaceE.asListWithoutNull(input.space), //spaces
            input.circles, //circles
            input.startTime, //startTime
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
        
        
        return Response.status(200).entity(categoryServ.categoryFrequsGet(SSClientE.rest, par)).build();
        
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