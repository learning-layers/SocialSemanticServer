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
package at.tugraz.sss.adapter.rest.v2.evallog;

import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.tugraz.sss.serv.conf.api.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.impl.api.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import io.swagger.annotations.*;
import javax.annotation.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sss.serv.eval.api.*;
import sss.serv.eval.datatypes.par.SSEvalLogPar;
import sss.serv.eval.datatypes.ret.SSEvalLogRet;

@Path("/eval/eval")
@Api( value = "eval")
public class SSRESTEval{
  
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
    
    final SSEvalLogRESTAPIV2Par input){
    
    final SSEvalLogPar       par;
    
    try{
      
      par =
        new SSEvalLogPar(
          null,
          input.toolContext,
          input.type,
          input.entity,
          input.content,
          input.entities,
          input.users,
          true); //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSEvalClientI evalServ = (SSEvalClientI) SSServReg.getClientServ(SSEvalClientI.class);
      
      return Response.status(200).entity(evalServ.evalLog(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
}
