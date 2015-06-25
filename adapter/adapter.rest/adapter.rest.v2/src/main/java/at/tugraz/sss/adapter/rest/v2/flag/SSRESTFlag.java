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
package at.tugraz.sss.adapter.rest.v2.flag;

import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.sss.flag.datatypes.par.SSFlagsUserGetPar;
import at.kc.tugraz.sss.flag.datatypes.par.SSFlagsUserSetPar;
import at.kc.tugraz.sss.flag.datatypes.ret.SSFlagsUserGetRet;
import at.kc.tugraz.sss.flag.datatypes.ret.SSFlagsUserSetRet;
import at.tugraz.sss.serv.SSServOpE;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/flags")
@Api(value = "/flags")
public class SSRESTFlag{
 
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("")
  @ApiOperation(
    value = "set flags",
    response = SSFlagsUserSetRet.class)
  public Response flagsSet(
    @Context 
      final HttpHeaders headers, 
    
    final SSFlagsSetRESTAPIV2Par input){
    
    final SSFlagsUserSetPar par;
    
    try{
      
      par =
        new SSFlagsUserSetPar(
          SSServOpE.flagsSet,
          null,
          null,  
          input.entities,
          input.types, 
          input.value, 
          input.endTime, 
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/filtered")
  @ApiOperation(
    value = "retrieve flags",
    response = SSFlagsUserGetRet.class)
  public Response flagsGetFiltered(
    @Context 
      final HttpHeaders headers, 
    
    final SSFlagsGetRESTAPIV2Par input){
    
    final SSFlagsUserGetPar par;
    
    try{
      
      par =
        new SSFlagsUserGetPar(
          SSServOpE.flagsGet,
          null,
          null,  
          input.entities,
          input.types, 
          input.startTime, 
          input.endTime);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}