/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package at.tugraz.sss.adapter.rest.v3;

import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.db.impl.*;
import at.tugraz.sss.servs.image.api.*;
import at.tugraz.sss.servs.image.datatype.SSImageProfilePictureSetPar;
import at.tugraz.sss.servs.image.datatype.SSImageProfilePictureSetRet;
import at.tugraz.sss.servs.image.impl.*;
import io.swagger.annotations.*;
import java.sql.*;
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

@Path("/images")
@Api( value = "images")
public class SSRESTImage{
  
  private final SSImageClientI imageServ = new SSImageImpl();
  private final SSDBSQLI          dbSQL        = new SSDBSQLMySQLImpl();
  
  @PostConstruct
  public void createRESTResource(){
  }
  
  @PreDestroy
  public void destroyRESTResource(){
  }
  
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
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = dbSQL.createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSImageProfilePictureSetPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(entity,  SSConf.sssUri),
            SSUri.get(file,    SSConf.sssUri),
            true,
            true);
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        return Response.status(200).entity(imageServ.imageProfilePictureSet(SSClientE.rest, par)).build();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
}
