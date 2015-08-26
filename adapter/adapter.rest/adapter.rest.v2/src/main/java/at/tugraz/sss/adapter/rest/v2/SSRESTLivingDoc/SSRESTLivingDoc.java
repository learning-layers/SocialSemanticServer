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
package at.tugraz.sss.adapter.rest.v2.SSRESTLivingDoc;

import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocAddPar;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocGetPar;
import at.tugraz.sss.servs.livingdocument.datatype.par.SSLivingDocsGetPar;
import at.tugraz.sss.servs.livingdocument.datatype.ret.SSLivingDocAddRet;
import at.tugraz.sss.servs.livingdocument.datatype.ret.SSLivingDocGetRet;
import at.tugraz.sss.servs.livingdocument.datatype.ret.SSLivingDocsGetRet;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
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

@Path("/livingdocs")
@Api( value = "/livingdocs")
public class SSRESTLivingDoc {
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("")
  @ApiOperation(
    value = "retrieve living docs for a user",
    response = SSLivingDocsGetRet.class)
  public Response livingDocsGet(
    @Context
    final HttpHeaders headers){
    
    final SSLivingDocsGetPar par;
    
    try{
      
      par =
        new SSLivingDocsGetPar(
          null,
          true,  //withUserRestriction
          true); //invokeEntityHandlers
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
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
    
    final SSLivingDocsGetRESTAPIV2Par input){
    
    final SSLivingDocGetPar par;
    
    try{
      
      par =
        new SSLivingDocGetPar(
          null,
          SSUri.get(livingDoc, SSVocConf.sssUri),
          true,  //withUserRestriction
          true); //invokeEntityHandlers
      
      par.setUsers = input.setUsers;
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
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
    
    final SSLivingDocsGetRESTAPIV2Par input){
    
    final SSLivingDocsGetPar par;
    
    try{
      
      par =
        new SSLivingDocsGetPar(
          null,
          true,  //withUserRestriction
          true); //invokeEntityHandlers
      
      par.setUsers = input.setUsers;
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
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
    
    try{
      
      par =
        new SSLivingDocGetPar(
          null,
          SSUri.get(livingDoc, SSVocConf.sssUri),
          true,  //withUserRestriction
          true); //invokeEntityHandlers
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("")
  @ApiOperation(
    value = "add a living doc",
    response = SSLivingDocAddRet.class)
  public Response livingDocAdd(
    @Context
    final HttpHeaders headers,
    
    final SSLivingDocAddRESTAPIV2Par input){
    
    final SSLivingDocAddPar par;
    
    try{
      
      par =
        new SSLivingDocAddPar(
          null,
          input.uri,
          input.label,
          input.description,
          input.discussion,
          true,  //withUserRestriction
          true); //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}
