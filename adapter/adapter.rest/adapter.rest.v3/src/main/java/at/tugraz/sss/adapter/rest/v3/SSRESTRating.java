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
import at.tugraz.sss.servs.rating.api.*;
import at.tugraz.sss.servs.rating.datatype.SSRatingOverallGetPar;
import at.tugraz.sss.servs.rating.datatype.SSRatingSetPar;
import at.tugraz.sss.servs.rating.datatype.SSRatingOverallGetRet;
import at.tugraz.sss.servs.rating.datatype.SSRatingSetRet;
import at.tugraz.sss.servs.entity.datatype.*;
import at.tugraz.sss.servs.db.api.*;
import at.tugraz.sss.servs.util.*;
import at.tugraz.sss.servs.db.impl.*;
import at.tugraz.sss.servs.rating.impl.*;
import io.swagger.annotations.*;
import java.sql.*;
import javax.annotation.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ratings")
@Api( value = "ratings")
public class SSRESTRating{

  private final SSRatingClientI ratingServ = new SSRatingImpl();
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
  @Path("/entities/{entity}/overall")
  @ApiOperation(
    value = "retrieve the overall rating (by all users) for given entity",
    response = SSRatingOverallGetRet.class)
  public Response ratingOverallGet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.entity)
    final String entity){
    
    final SSRatingOverallGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRatingOverallGetPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(entity, SSConf.sssUri),
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
        return Response.status(200).entity(ratingServ.ratingOverallGet(SSClientE.rest, par)).build();
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
  @Path("/entities/{entity}/value/{value}")
  @ApiOperation(
    value = "set the user's rating for given entity",
    response = SSRatingSetRet.class)
  public Response ratingSet(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.entity)
    final String entity,
    
    @PathParam(SSVarNames.value)
    final String value){
    
    final SSRatingSetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSRatingSetPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(entity, SSConf.sssUri),
            Integer.valueOf(value),
            true, //allowToRateAgain
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
        
        
        return Response.status(200).entity(ratingServ.ratingSet(SSClientE.rest, par)).build();
        
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