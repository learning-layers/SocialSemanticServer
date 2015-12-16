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
package at.tugraz.sss.adapter.rest.v2.friend;


import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.friend.datatypes.par.SSFriendAddPar;
import at.kc.tugraz.ss.friend.datatypes.par.SSFriendsGetPar;
import at.kc.tugraz.ss.friend.datatypes.ret.SSFriendAddRet;
import at.kc.tugraz.ss.friend.datatypes.ret.SSFriendsGetRet;
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSVarNames;
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

@Path("/friends")
@Api( value = "/friends") //, basePath = "/friends"
public class SSRESTFriend{
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("")
  @ApiOperation(
    value = "get friends",
    response = SSFriendsGetRet.class)
  public Response friendsGet(
    @Context 
      final HttpHeaders headers){
    
    final SSFriendsGetPar par;
    
    try{
      
      par =
        new SSFriendsGetPar(
          null);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{friend}")
  @ApiOperation(
    value = "add a friend",
    response = SSFriendAddRet.class)
  public Response friendAddPost(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.friend)
    final String friend){
    
    final SSFriendAddPar par;
    
    try{
      
      par =
        new SSFriendAddPar(
          null,
          SSUri.get(friend, SSVocConf.sssUri),  //friend
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}
