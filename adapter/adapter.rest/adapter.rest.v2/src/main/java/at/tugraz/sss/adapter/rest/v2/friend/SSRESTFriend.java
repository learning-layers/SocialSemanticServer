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

import at.kc.tugraz.ss.friend.api.*;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.friend.datatypes.par.SSFriendAddPar;
import at.kc.tugraz.ss.friend.datatypes.par.SSFriendsGetPar;
import at.kc.tugraz.ss.friend.datatypes.ret.SSFriendAddRet;
import at.kc.tugraz.ss.friend.datatypes.ret.SSFriendsGetRet;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.conf.api.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.impl.api.*;
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

@Path("/friends")
@Api( value = "/friends") //, basePath = "/friends"
public class SSRESTFriend extends SSServImplStartA{
  
  public SSRESTFriend() {
    super(null);
  }
  
  public SSRESTFriend(final SSConfA conf) {
    super(conf);
  }
  
  @Override
  protected void finalizeImpl() throws Exception{
    destroy();
  }

  @PostConstruct
  public void createRESTResource(){
  }
  
  @PreDestroy
  public void destroyRESTResource(){
    try{
      finalizeImpl();
    }catch(Exception error2){
      SSLogU.err(error2);
    }
  }
  
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
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSFriendClientI friendServ = (SSFriendClientI) SSServReg.getClientServ(SSFriendClientI.class);
      
      return Response.status(200).entity(friendServ.friendsGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
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
          SSUri.get(friend, SSConf.sssUri),  //friend
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSFriendClientI friendServ = (SSFriendClientI) SSServReg.getClientServ(SSFriendClientI.class);
      
      return Response.status(200).entity(friendServ.friendAdd(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
}
