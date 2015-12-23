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
package at.tugraz.sss.adapter.rest.v2.like;

import at.kc.tugraz.ss.like.api.*;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.like.datatypes.par.SSLikeUserSetPar;
import at.kc.tugraz.ss.like.datatypes.ret.SSLikeUserSetRet;
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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/likes")
@Api( value = "/likes")
public class SSRESTLike extends SSServImplStartA{
  
  public SSRESTLike() {
    super(null);
  }
  
  public SSRESTLike(final SSConfA conf) {
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
  
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/entities/{entity}/value/{value}")
  @ApiOperation(
    value = "like / dislike / neutral an entity",
    response = SSLikeUserSetRet.class)
  public Response likeUpdate(
    @Context
    final HttpHeaders  headers,
    
    @PathParam (SSVarNames.entity)
    final String entity,
    
    @PathParam (SSVarNames.value)
    final Integer value){
    
    final SSLikeUserSetPar par;
    
    try{
      
      par =
        new SSLikeUserSetPar(
          null, //user
          SSUri.get(entity, SSConf.sssUri), //entity
          value, //value
          true, //withUserRestriction
          true);  //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSLikeClientI likeServ = (SSLikeClientI) SSServReg.getClientServ(SSLikeClientI.class);
      
      return Response.status(200).entity(likeServ.likeSet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return SSRestMainV2.prepareErrors();
    }
    
  }
}
