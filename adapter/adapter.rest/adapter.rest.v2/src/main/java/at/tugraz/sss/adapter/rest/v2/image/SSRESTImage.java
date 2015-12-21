/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.sss.adapter.rest.v2.image;

import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.image.datatype.par.SSImageProfilePictureSetPar;
import at.tugraz.sss.servs.image.datatype.ret.SSImageProfilePictureSetRet;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/images")
@Api( value = "/images") //, basePath = "/entities"
public class SSRESTImage {
 
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/profile/picture/entity/{entity}/file/{file}")
  @ApiOperation(
    value = "set an entity's profile picture",
    response = SSImageProfilePictureSetRet.class)
  public Response imageProfilePictureSet(
    @Context 
      final HttpHeaders headers,
    
    @PathParam(SSVarNames.entity) 
      final String entity,
    
    @PathParam(SSVarNames.file) 
      final String file){
    
    final SSImageProfilePictureSetPar par;
    
    try{
      
      par =
        new SSImageProfilePictureSetPar(
          null, 
          SSUri.get(entity,  SSVocConf.sssUri),
          SSUri.get(file,    SSVocConf.sssUri), 
          true, 
          true);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}
