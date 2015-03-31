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
package at.kc.tugraz.ss.adapter.rest.v2.pars.app;

import at.tugraz.sss.serv.SSMethU;
import at.kc.tugraz.ss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.sss.app.datatypes.par.SSAppAddPar;
import at.kc.tugraz.sss.app.datatypes.par.SSAppsGetPar;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppAddRet;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppsGetRet;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/apps")
@Api( value = "/apps") //, basePath = "/apps"
public class SSRESTApps{
  
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
          SSMethU.appsGet,
          null, 
          null);
      
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
    value = "add an app",
    response = SSAppAddRet.class)
  public Response appAddPost(
    @Context HttpHeaders       headers,
    final SSAppAddRESTAPIV2Par input){
    
    final SSAppAddPar par;
    
    try{
      
      par =
        new SSAppAddPar(
          SSMethU.appAdd,
          null,
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
          input.videos);
      
       }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleRequest(headers, par, false, true).response;
  }
}
