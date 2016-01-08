 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.adapter.rest.v2.auth;

import at.kc.tugraz.ss.serv.auth.api.*;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthCheckCredPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthRegisterUserPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.ret.SSAuthCheckCredRet;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
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

@Path("/auth")
@Api( value = "auth")
public class SSRESTAuth{
  
  @PostConstruct
  public void createRESTResource(){
  }
  
  @PreDestroy
  public void destroyRESTResource(){
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "validate given OIDC authentication token (sent as header 'Authorization: Bearer yourToken') and retrieve the user's uri for given token ('only available for SSS 'oidcAuth')",
    response = SSAuthCheckCredRet.class)
  public Response checkCredWithAuthToken(
    @Context
    final HttpHeaders headers){
    
    final SSAuthCheckCredPar par;
    
    try{
      
      par =
        new SSAuthCheckCredPar(
          null,
          null);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSAuthClientI authServ = (SSAuthClientI) SSServReg.getClientServ(SSAuthClientI.class);
      
      return Response.status(200).entity(authServ.authCheckCred(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "retrieve sss authentication key and the user's uri for given user name and password (only available for SSS 'csvFileAuth')",
    response = SSAuthCheckCredRet.class)
  public Response checkCredWithUserNamePassword(
    final SSAuthCheckCredRESTAPIV2Par input){
    
    final SSAuthCheckCredPar par;
    
    try{
      
      par =
        new SSAuthCheckCredPar(
          input.label,
          input.password);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      final SSAuthClientI authServ = (SSAuthClientI) SSServReg.getClientServ(SSAuthClientI.class);
      
      return Response.status(200).entity(authServ.authCheckCred(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{email}")
  @ApiOperation(
    value = "",
    response = SSAuthCheckCredRet.class)
  public Response authRegisterUser(
    @PathParam(SSVarNames.email)
    final String email,
    
    final SSAuthRegisterUserRESTAPIV2Par input){
    
    final SSAuthRegisterUserPar par;
    
    try{
      
      par =
        new SSAuthRegisterUserPar(
          email,
          input.password,
          input.label,
          false, //updatePassword,
          false, //isSystemUser,
          false, //withUserRestriction,
          true); //shouldCommitsss
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      final SSAuthClientI authServ = (SSAuthClientI) SSServReg.getClientServ(SSAuthClientI.class);
      
      return Response.status(200).entity(authServ.authRegisterUser(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
  }
}