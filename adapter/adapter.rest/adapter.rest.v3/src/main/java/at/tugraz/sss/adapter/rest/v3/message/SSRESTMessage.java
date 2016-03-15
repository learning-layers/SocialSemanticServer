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
package at.tugraz.sss.adapter.rest.v3.message;

import at.kc.tugraz.ss.message.api.*;
import at.tugraz.sss.adapter.rest.v3.SSRESTCommons;
import at.kc.tugraz.ss.message.datatypes.par.SSMessageSendPar;
import at.kc.tugraz.ss.message.datatypes.par.SSMessagesGetPar;
import at.kc.tugraz.ss.message.datatypes.ret.SSMessageSendRet;
import at.kc.tugraz.ss.message.datatypes.ret.SSMessagesGetRet;
import at.tugraz.sss.adapter.rest.v3.SSRESTCommons;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
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

@Path("/messages")
@Api( value = "messages")
public class SSRESTMessage{
  
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
    value = "retrieve messages",
    response = SSMessagesGetRet.class)
  public Response messagesGetFiltered(
    @Context
    final HttpHeaders headers,
    
    final SSMessagesGetRESTPar input){
    
    final SSMessagesGetPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSMessagesGetPar(
            new SSServPar(sqlCon),
            null,
            null, //forUser
            input.includeRead,
            input.startTime,
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
        final SSMessageClientI messageServ = (SSMessageClientI) SSServReg.getClientServ(SSMessageClientI.class);
        
        return Response.status(200).entity(messageServ.messagesGet(SSClientE.rest, par)).build();
        
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
    value = "send a message to a user",
    response = SSMessageSendRet.class)
  public Response messageSend(
    @Context
    final HttpHeaders headers,
    
    final SSMessageSendRESTPar input){
    
    final SSMessageSendPar par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSMessageSendPar(
            new SSServPar(sqlCon),
            null,
            input.forUser,
            input.message,
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
        final SSMessageClientI messageServ = (SSMessageClientI) SSServReg.getClientServ(SSMessageClientI.class);
        
        return Response.status(200).entity(messageServ.messageSend(SSClientE.rest, par)).build();
        
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