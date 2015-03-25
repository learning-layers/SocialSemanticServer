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
package at.kc.tugraz.ss.adapter.rest.v2.pars.recomm;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUsersPar;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommUsersRet;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/recomm")
@Api( value = "/recomm", basePath = "/recomm")
public class SSRESTRecomm{
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/users/")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsers(
    @Context HttpHeaders          headers){
    
    final SSRecommUsersPar par;
    
    try{
      
      par =
        new SSRecommUsersPar(
          SSMethU.recommUsers,
          null,
          null, 
          null, //forUser
          null, //entity
          null, //categories
          10);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleGETRequest(headers, par);
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/users/user/{forUser}/entity/{entity}")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsersForUserEntity(
    @Context HttpHeaders               headers,
    @ApiParam(value = "user to be considered to retrieve recommendations for") 
    @PathParam(SSVarU.forUser) String  forUser, 
    @ApiParam(value = "resource to be considered to retrieve recommendations for") 
    @PathParam(SSVarU.entity) String  entity){
    
    final SSRecommUsersPar par;
    
    try{
      
      par =
        new SSRecommUsersPar(
          SSMethU.recommUsers,
          null,
          null, 
          SSUri.get(forUser, SSVocConf.sssUri), //forUser
          SSUri.get(entity,  SSVocConf.sssUri), //entity
          null, //categories
          10);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleGETRequest(headers, par);
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/users/user/{forUser}")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsersForUser(
    @Context HttpHeaders               headers,
    @ApiParam(value = "user to be considered to retrieve recommendations for")
    @PathParam(SSVarU.forUser) String  forUser){
    
    final SSRecommUsersPar par;
    
    try{
      
      par =
        new SSRecommUsersPar(
          SSMethU.recommUsers,
          null,
          null, 
          SSUri.get(forUser, SSVocConf.sssUri), //forUser
          null, //entity
          null, //categories
          10);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleGETRequest(headers, par);
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/users/entity/{entity}")
  @ApiOperation(
    value = "retrieve user recommendations",
    response = SSRecommUsersRet.class)
  public Response recommUsersForEntity(
    @Context HttpHeaders              headers,
    @ApiParam(value = "resource to be considered to retrieve recommendations for")
    @PathParam(SSVarU.entity) String  entity){
    
    final SSRecommUsersPar par;
    
    try{
      
      par =
        new SSRecommUsersPar(
          SSMethU.recommUsers,
          null,
          null, 
          null, //forUser
          SSUri.get(entity, SSVocConf.sssUri), //entity
          null, //categories
          10);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleGETRequest(headers, par);
  }
}
