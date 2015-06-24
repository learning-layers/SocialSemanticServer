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
package at.tugraz.sss.adapter.rest.v2.learnep;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import com.wordnik.swagger.annotations.Api;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/learneps")
@Api( value = "/learneps") 
public class SSRESTLearnEp{
  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpsLockHold")
//  public String learnEpsLockHold(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpsLockHold);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpLockSet")
//  public String learnEpLockSet(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpLockSet);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpLockRemove")
//  public String learnEpLockRemove(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpLockRemove);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpsGet")
//  public String learnEpsGet(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpsGet);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpVersionsGet")
//  public String learnEpVersionsGet(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionsGet);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpVersionGet")
//  public String learnEpVersionGet(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionGet);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpVersionCurrentGet")
//  public String learnEpVersionCurrentGet(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionCurrentGet);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpVersionCurrentSet")
//  public String learnEpVersionCurrentSet(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionCurrentSet);
//  }
//    
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpVersionCreate")
//  public String learnEpVersionCreate(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionCreate);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpVersionAddCircle")
//  public String learnEpVersionAddCircle(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionAddCircle);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpVersionAddEntity")
//  public String learnEpVersionAddEntity(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionAddEntity);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpCreate")
//  public String learnEpCreate(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpCreate);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpVersionUpdateCircle")
//  public String learnEpVersionUpdateCircle(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionUpdateCircle);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpVersionUpdateEntity")
//  public String learnEpVersionUpdateEntity(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionUpdateEntity);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpVersionSetTimelineState")
//  public String learnEpVersionSetTimelineState(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionSetTimelineState);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpVersionGetTimelineState")
//  public String learnEpVersionGetTimelineState(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionGetTimelineState);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpVersionRemoveCircle")
//  public String learnEpVersionRemoveCircle(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionRemoveCircle);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpVersionRemoveEntity")
//  public String learnEpVersionRemoveEntity(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpVersionRemoveEntity);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "learnEpRemove")
//  public String learnEpRemove(String jsonRequ){
//    return SSRestMainV1.handleStandardJSONRESTCall(jsonRequ, SSServOpE.learnEpRemove);
//  }
}
