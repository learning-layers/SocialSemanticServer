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
package at.tugraz.sss.adapter.rest.v2.app;

import at.kc.tugraz.sss.app.api.*;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.sss.app.datatypes.par.SSAppAddPar;
import at.kc.tugraz.sss.app.datatypes.par.SSAppsDeletePar;
import at.kc.tugraz.sss.app.datatypes.par.SSAppsGetPar;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppAddRet;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppsDeleteRet;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppsGetRet;
import at.tugraz.sss.serv.conf.api.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.impl.api.*;
import at.tugraz.sss.serv.reg.*;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/apps")
@Api( value = "/apps") //, basePath = "/apps"
public class SSRESTApp extends SSServImplStartA{
  
  public SSRESTApp() {
    super(null);
  }
  
  public SSRESTApp(final SSConfA conf) {
    super(conf);
  }
  
  @Override
  protected void finalizeImpl() throws Exception{
    finalizeThread(false);
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("")
  @ApiOperation(
    value = "retrieve apps",
    response = SSAppsGetRet.class)
  public Response appsGet(
    @Context HttpHeaders       headers){
    
    final SSAppsGetPar par;
    
    try{
      
      par =
        new SSAppsGetPar(
          null,
          true); //invokeEntityHandlers
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSAppClientI appServ = (SSAppClientI) SSServReg.getClientServ(SSAppClientI.class);
      
      return Response.status(200).entity(appServ.appsGet(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
    
    finally{
      try{
        finalizeImpl();
      }catch(Exception error2){
        SSLogU.err(error2);
      }
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("")
  @ApiOperation(
    value = "add an app",
    response = SSAppAddRet.class)
  public Response appAddPost(
    @Context HttpHeaders       headers,
    final SSAppAddRESTAPIV2Par input){
    
    final SSAppAddPar par;
    
    try{
      
      par =
        new SSAppAddPar(
          null,
          input.label,
          input.descriptionShort,
          input.descriptionFunctional,
          input.descriptionTechnical,
          input.descriptionInstall,
          input.downloads,
          input.downloadIOS,
          input.downloadAndroid,
          input.fork,
          input.screenShots,
          input.videos,
          true, //withUserRestriction,
          true); //shouldCommmit);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSAppClientI appServ = (SSAppClientI) SSServReg.getClientServ(SSAppClientI.class);
      
      return Response.status(200).entity(appServ.appAdd(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
    
    finally{
      try{
        finalizeImpl();
      }catch(Exception error2){
        SSLogU.err(error2);
      }
    }
  }
  
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    ("/{apps}")
  @ApiOperation(
    value = "remove apps",
    response = SSAppsDeleteRet.class)
  public Response appsDelete(
    @Context
    final HttpHeaders headers,
    
    @PathParam(SSVarNames.apps)
    final String apps){
    
    final SSAppsDeletePar par;
    
    try{
      
      par =
        new SSAppsDeletePar(
          null,
          SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(apps, SSStrU.comma), SSConf.sssUri),
          true, //withUserRestriction
          true); //shouldCommit
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    try{
      par.key = SSRestMainV2.getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    try{
      final SSAppClientI appServ = (SSAppClientI) SSServReg.getClientServ(SSAppClientI.class);
      
      return Response.status(200).entity(appServ.appsDelete(SSClientE.rest, par)).build();
      
    }catch(Exception error){
      return Response.status(500).build();
    }
    finally{
      try{
        finalizeImpl();
      }catch(Exception error2){
        SSLogU.err(error2);
      }
    }
  }
}