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

import at.tugraz.sss.servs.search.datatype.SSSearchRESTPar;
import at.tugraz.sss.servs.search.datatype.SSSearchPar;
import at.tugraz.sss.servs.search.datatype.SSSearchRet;

import at.tugraz.sss.servs.entity.datatype.*;
import at.tugraz.sss.servs.db.api.*;
import at.tugraz.sss.servs.util.*;
import at.tugraz.sss.servs.db.impl.*;
import at.tugraz.sss.servs.search.api.*;
import at.tugraz.sss.servs.search.impl.*;
import io.swagger.annotations.*;
import java.sql.*;
import javax.annotation.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/search")
@Api( value = "search")
public class SSRESTSearch{
  
  private final SSSearchClientI searchServ = new SSSearchImpl();
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
    value = "search for entities",
    response = SSSearchRet.class)
  public Response search(
    @Context
    final HttpHeaders headers,
    
    final SSSearchRESTPar input){
    
    final SSSearchPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSSearchPar(
            new SSServPar(sqlCon),
            null,
            input.documentContentsToSearchFor,
            input.tagsToSearchFor,
            input.authorsToSearchFor,
            input.labelsToSearchFor,
            input.descriptionsToSearchFor,
            input.applyGlobalSearchOpBetweenLabelAndDescription,
            input.typesToSearchOnlyFor,
            input.includeRecommendedResults,
            input.pageSize,
            input.pagesID,
            input.pageNumber,
            input.minRating,
            input.maxRating,
            input.startTime,
            input.endTime,
            input.localSearchOp,
            input.globalSearchOp,
            input.orderByLabel,
            input.orderByCreationTime,
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
        return Response.status(200).entity(searchServ.search(SSClientE.rest, par)).build();
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
