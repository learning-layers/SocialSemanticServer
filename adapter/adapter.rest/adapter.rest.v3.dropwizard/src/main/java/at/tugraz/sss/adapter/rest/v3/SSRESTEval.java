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


import at.tugraz.sss.servs.entity.datatype.*;
import at.tugraz.sss.servs.db.api.*;
import at.tugraz.sss.servs.util.*;
import at.tugraz.sss.servs.db.impl.*;
import at.tugraz.sss.servs.eval.api.*;
import at.tugraz.sss.servs.eval.datatype.*;
import at.tugraz.sss.servs.eval.impl.*;
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

@Path("rest/eval")
@Api( value = "eval")
public class SSRESTEval{
  
  private final SSEvalClientI evalServ = new SSEvalImpl();
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
  @Path("/log")
  @ApiOperation(
    value = "log events for evaluation purposes",
    response = SSEvalLogRet.class)
  public Response evalLog(
    @Context
    final HttpHeaders headers,
    
    final SSEvalLogRESTPar input){
    
    final SSEvalLogPar       par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSEvalLogPar(
            new SSServPar(sqlCon),
            null,
            input.toolContext,
            input.type,
            input.entity,
            input.content,
            input.entities,
            input.users,
            null, //creationTime
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
        
        
        return Response.status(200).entity(evalServ.evalLog(SSClientE.rest, par)).build();
        
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
