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
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkEntitiesPar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdatePar;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUsersPar;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommUpdateBulkRet;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommUpdateRet;
import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommUsersRet;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import java.io.File;
import java.io.InputStream;
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
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/recomm")
@Api( value = "/recomm") //, basePath = "/recomm"
public class SSRESTRecomm{
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/update/")
  @ApiOperation(
    value = "add a new combination of user, entity, tags, categories to be used for recommendations",
    response = SSRecommUpdateRet.class)
  public Response recommUpdate(
    @Context HttpHeaders             headers,
    final SSRecommUpdateRESTAPIV2Par input){
    
    final SSRecommUpdatePar par;
    
    try{
      
      par =
        new SSRecommUpdatePar(
          SSMethU.recommUpdate,
          null,
          null,
          input.forUser,     //forUser
          input.entity,      //entity
          input.tags,        //tags
          input.categories); //categories
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
  
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/update/bulk")
  @ApiOperation(
    value = "add a file containing user, entity, tag, category combinations to be used for recommendations",
    response = SSRecommUpdateBulkRet.class)
  public Response recommUpdateBulk(
    @Context HttpHeaders                                   headers,
    @ApiParam(name = "fileName", value = "description", required = false)
    @FormDataParam("fileName") String fileName,
    @ApiParam(name = "file", value = "description", required = true)
    @FormDataParam("file") InputStream file){
      
//    @FormDataParam("additionalMetadata") FormDataContentDisposition fileData){
    
    
//     {"name":"additionalMetadata","in":"formData","description":"Additional data to pass to server","required":false,"type":"string"},
//	  {"name":"file","in":"formData","description":"file to upload","required":false,"type":"file"}
    Response     result = null;
    SSSocketCon  sSCon  = null;
    int          read;

    
//    try{
//      final SSRecommUpdateBulkPar par;
//      byte[]                bytes  = new byte[SSSocketU.socketTranmissionSize];
//      String                readMsgFullFromSS;
//      
//      par =
//        new SSRecommUpdateBulkPar(
//          SSMethU.recommUpdateBulk,
//          null,
//          null,
//          SSLabel.get("test"));
//      
//      try{
//        par.key = getBearer(headers);
//      }catch(Exception error){
//        return Response.status(401).build();
//      }
//      
//      sSCon = new SSSocketCon(SSRestMainV2.conf.ss.host, SSRestMainV2.conf.ss.port, SSJSONU.jsonStr(par));
//      
//      sSCon.writeRequFullToSS  ();
//      sSCon.readMsgFullFromSS  ();
//      
//      while ((read = fileHandle.read(bytes)) != -1) {
//        sSCon.writeFileChunkToSS   (bytes, read);
//      }
//      
//      sSCon.writeFileChunkToSS(new byte[0], -1);
//      
//      try{
//        readMsgFullFromSS = sSCon.readMsgFullFromSS ();
//      }catch(Exception error){
//        
//        SSLogU.info("couldnt read from " + SSRestMainV2.conf.ss.host + " " + SSRestMainV2.conf.ss.port.toString());
//        throw error;
//      }
//      
//      sSCon.closeCon();
//      
//      return Response.status(200).entity(readMsgFullFromSS).build();
//      
//    }catch(Exception error){
//      
//      try{
//        return Response.serverError().build();
//      }catch(Exception error1){
//        SSServErrReg.regErr(error1, "writing error to client didnt work");
//      }
//    }finally{
//      
//      if(sSCon != null){
//        sSCon.closeCon();
//      }
//    }
    
    return result;
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/update/bulk/entities")
  @ApiOperation(
    value = "add tags, categories for the user's given entities to be used for recommendations",
    response = SSRecommUpdateRet.class)
  public Response recommUpdateBulkEntities(
    @Context HttpHeaders                         headers,
    final SSRecommUpdateBulkEntitiesRESTAPIV2Par input){
    
    final SSRecommUpdateBulkEntitiesPar par;
    
    try{
      
      par =
        new SSRecommUpdateBulkEntitiesPar(
          SSMethU.recommUpdateBulkEntities,
          null,
          null,
          input.forUser,       //forUser
          input.entities,      //entity
          input.tags,          //tags
          input.categories);   //categories
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
  
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
