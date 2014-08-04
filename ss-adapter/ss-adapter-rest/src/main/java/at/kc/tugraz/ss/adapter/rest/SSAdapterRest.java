/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.adapter.rest;

import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSMimeTypeU;
import at.kc.tugraz.socialserver.utils.SSSocketU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSSystemU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.adapter.rest.conf.SSAdapterRestConf;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.conf.conf.SSConf;
import at.kc.tugraz.ss.serv.datatypes.SSClientPar;
import at.kc.tugraz.ss.serv.err.reg.SSErrForClient;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.ss.serv.serv.datatypes.err.SSDeployServiceOnNodeErr;
import com.sun.jersey.multipart.FormDataParam;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

@Path("/SSAdapterRest")
public class SSAdapterRest{
  
  private SSSocketCon     sSCon   = null;
  private int             read    = -1;
  private SSConf          conf    = null;
  
  public SSAdapterRest() throws Exception{
    
//    SSLogU.info("rest enter");
    SSAdapterRestConf.instSet (SSFileU.dirCatalinaBase() + SSSystemU.dirNameConf + "ss-adapter-rest-conf.yaml");
    
    /* util */
    SSMimeTypeU.init();
    SSJSONLDU.init(
      SSAdapterRestConf.instGet().getJsonLDConf().uri,
      SSAdapterRestConf.instGet().getVocConf().app,
      SSAdapterRestConf.instGet().getVocConf().space);
    
    /* json-ld */
//    SSJSONLD.inst.initServ(SSAdapterRestConf.instGet().getJsonLDConf());
    
    conf = SSAdapterRestConf.instGet().getSsConf();
  }
  
  @GET
  @Consumes(MediaType.TEXT_HTML)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "jsonLD" + SSStrU.slash + SSStrU.curlyBracketOpen + SSVarU.type + SSStrU.curlyBracketClose)
  public String jsonLD(@PathParam(SSVarU.type) String type){
    
    String jsonRequ = "{\"op\":\"" + SSMethU.jsonLD + "\",\"user\":\"" + "mailto:dummyUser" + "/\",\"type\":\"" + type + "\",\"key\":\"681V454J1P3H4W3B367BB79615U184N22356I3E\"}";
    
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.jsonLD);
  }
    
//    Map<String, Object> ret = new HashMap<>();
//    
//    try{
//      ret.put(SSVarU.op,                  SSMethU.jsonLD);
//      ret.put(SSVarU.error,               false);
//      ret.put(SSVarU.jsonLD,              ((SSJSONLDImpl)SSJSONLD.inst.impl()).jsonLDDesc(entityType));
//      
//      ret.put(SSJSONLDU.context, SSJSONLDU.jsonLDContext());
//      
//      return Response.status(200).entity(SSJSONU.jsonStr(ret)).build();
//      
//    }catch (Exception error) {
//      
//      SSLogU.logError(error);
//        
//      try{
//        return Response.serverError().build();
//      }catch(Exception error1){
//        SSLogU.logError(error1, "writing error to client didnt work");
//      }
//    }
//    
//    return Response.ok(null).build();
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "categoriesPredefinedGet")
  public String categoriesPredefinedGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.categoriesPredefinedGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "flagsSet")
  public String flagsSet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.flagsSet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "flagsGet")
  public String flagsGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.flagsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "authCheckCred")
  public String authCheckCred(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.authCheckCred);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "dataImportEvernote")
  public String dataImportEvernote(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.dataImportEvernote);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "activitiesGet")
  public String activitiesGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.activitiesGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "search")
  public String search(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.search);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "systemVersionGet")
  public String systemVersionGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.systemVersionGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityPublicSet")
  public String entityPublicSet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityPublicSet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityGet")
  public String entityGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityCircleGet")
  public String entityCircleGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityCircleGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityCircleCreate")
  public String entityCircleCreate(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityCircleCreate);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityUsersToCircleAdd")
  public String entityUsersToCircleAdd(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityUsersToCircleAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityCopy")
  public String entityCopy(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityCopy);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityEntitiesToCircleAdd")
  public String entityEntitiesToCircleAdd(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityEntitiesToCircleAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityUserCirclesGet")
  public String entityUserCirclesGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityUserCirclesGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityEntityUsersGet")
  public String entityEntityUsersGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityEntityUsersGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collsEntityIsInGet")
  public String collsEntityIsInGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collsEntityIsInGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collsCouldSubscribeGet")
  public String collsCouldSubscribeGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collsCouldSubscribeGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "tagEdit")
  public String v(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.tagEdit);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collRootGet")
  public String collRootGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collRootGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collParentGet")
  public String collParentGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collParentGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collEntryAdd")
  public String collEntryAdd(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collEntryAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collEntriesAdd")
  public String collEntriesAdd(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collEntriesAdd);
  }  
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collEntryChangePos")
  public String collEntryChangePos(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collEntryChangePos);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collEntryDelete")
  public String collEntryDelete(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collEntryDelete);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collEntriesDelete")
  public String collEntriesDelete(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collEntriesDelete);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityShare")
  public String entityShare(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityShare);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collWithEntries")
  public String collWithEntries(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collWithEntries);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collsWithEntries")
  public String collsWithEntries(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collsWithEntries);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collHierarchyGet")
  public String collHierarchyGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collHierarchyGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collCumulatedTagsGet")
  public String collCumulatedTagsGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collCumulatedTagsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "discEntryAdd")
  public String discEntryAdd(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.discEntryAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "discURIsForTargetGet")
  public String discURIsForTargetGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.discURIsForTargetGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "discRemove")
  public String discRemove(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.discRemove);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "discWithEntriesGet")
  public String discWithEntriesGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.discWithEntriesGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "discsAllGet")
  public String discsAllGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.discsAllGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityDirectlyAdjoinedEntitiesRemove")
  public String entityDirectlyAdjoinedEntitiesRemove(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityDirectlyAdjoinedEntitiesRemove);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityDescGet")
  public String entityDescGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityDescGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityUpdate")
  public String entityUpdate(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityUpdate);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "fileExtGet")
  public String fileExtGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.fileExtGet);
  }
    
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "fileCanWrite")
  public String fileCanWrite(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.fileCanWrite);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "fileSetReaderOrWriter")
  public String fileSetReaderOrWriter(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.fileSetReaderOrWriter);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "fileUserFileWrites")
  public String fileUserFileWrites(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.fileUserFileWrites);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "fileWritingMinutesLeft")
  public String fileWritingMinutesLeft(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.fileWritingMinutesLeft);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpsGet")
  public String learnEpsGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.learnEpsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionsGet")
  public String learnEpVersionsGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.learnEpVersionsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionGet")
  public String learnEpVersionGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.learnEpVersionGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionCurrentGet")
  public String learnEpVersionCurrentGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.learnEpVersionCurrentGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionCurrentSet")
  public String learnEpVersionCurrentSet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.learnEpVersionCurrentSet);
  }
    
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionCreate")
  public String learnEpVersionCreate(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.learnEpVersionCreate);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionAddCircle")
  public String learnEpVersionAddCircle(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.learnEpVersionAddCircle);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionAddEntity")
  public String learnEpVersionAddEntity(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.learnEpVersionAddEntity);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpCreate")
  public String learnEpCreate(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.learnEpCreate);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionUpdateCircle")
  public String learnEpVersionUpdateCircle(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.learnEpVersionUpdateCircle);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionUpdateEntity")
  public String learnEpVersionUpdateEntity(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.learnEpVersionUpdateEntity);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionSetTimelineState")
  public String learnEpVersionSetTimelineState(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.learnEpVersionSetTimelineState);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionGetTimelineState")
  public String learnEpVersionGetTimelineState(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.learnEpVersionGetTimelineState);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionRemoveCircle")
  public String learnEpVersionRemoveCircle(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.learnEpVersionRemoveCircle);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "learnEpVersionRemoveEntity")
  public String learnEpVersionRemoveEntity(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.learnEpVersionRemoveEntity);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "locationAdd")
  public String locationAdd(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.locationAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "locationsGet")
  public String locationsGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.locationsGet);
  }  
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "modelUEResourceDetails")
  public String modelUEResourceDetails(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.modelUEResourceDetails);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "ratingOverallGet")
  public String ratingOverallGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.ratingOverallGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "ratingSet")
  public String ratingSet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.ratingSet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "recommTags")
  public String recommTags(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.recommTags);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "tagEntitiesForTagsGet")
  public String tagEntitiesForTagsGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.tagEntitiesForTagsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "tagsGet")
  public String tagsGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.tagsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "tagAdd")
  public String tagAdd(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.tagAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "tagFrequsGet")
  public String tagFrequsGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.tagFrequsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "tagsRemove")
  public String tagsRemove(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.tagsRemove);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "userAll")
  public String userAll(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.userAll);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "uEAdd")
  public String uEAdd(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.uEAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "uEsGet")
  public String uEsGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.uEsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "uEGet")
  public String uEGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.uEGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "uECountGet")
  public String uECountGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.uECountGet);
  }
  

//@POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_OCTET_STREAM)
//  @Path(SSStrU.slash + "fileDownload")
//  public StreamingOutput fileDownload(String jsonRequ) throws Exception{
//  
//        sSCon = new SSSocketCon(conf.host, conf.port, jsonRequ);
//      
//      sSCon.writeRequFullToSS   ();
//      sSCon.readMsgFullFromSS   ();
//      sSCon.writeRequFullToSS   ();
//  
//  return new StreamingOutput() {
//    
//    public void write(OutputStream output) throws IOException, WebApplicationException {
//     
//      try {
//         byte[] bytes;
//          FileOutputStream fileOutputStream = null;
//          
//          try{
//            fileOutputStream = SSFileU.openFileForWrite("F:/daten/hugo.mp3");
//          }catch(Exception ex){
//            Logger.getLogger(SSAdapterRest.class.getName()).log(Level.SEVERE, null, ex);
//          }
//
//          while((bytes = sSCon.readFileChunkFromSS()).length > 0) {
//
//            output.write               (bytes);
//            output.flush               ();
//            
//            fileOutputStream.write(bytes);
//            
//            sSCon.writeRequFullToSS ();
//          }
//          
//          output.close();
//          fileOutputStream.close();
//      } catch (Exception e) {
//        throw new WebApplicationException(e);
//      }
//    }
//  };

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path(SSStrU.slash + "fileDownload")
  public Response fileDownload(String jsonRequ){
    
    StreamingOutput  stream           = null;
    
    try{
      sSCon = new SSSocketCon(conf.host, conf.port, jsonRequ);
      
      sSCon.writeRequFullToSS   ();
      sSCon.readMsgFullFromSS   ();
      sSCon.writeRequFullToSS   ();

      stream = new StreamingOutput(){

        @Override
        public void write(OutputStream out) throws IOException{
          
          byte[] bytes;
//          FileOutputStream fileOutputStream = null;
          
//          try{
//            fileOutputStream = SSFileU.openFileForWrite("F:/daten/hugo.mp3");
//          }catch(Exception ex){
//            Logger.getLogger(SSAdapterRest.class.getName()).log(Level.SEVERE, null, ex);
//          }

          while((bytes = sSCon.readFileChunkFromSS()).length > 0) {

            out.write               (bytes);
            out.flush               ();
            
//            fileOutputStream.write(bytes);
            
//            sSCon.writeRequFullToSS ();
          }
          
          out.close();
//          fileOutputStream.close();
        }
      };
    }catch(Exception error){
      
      try{
        return Response.serverError().build();
      }catch(Exception error1){
        SSServErrReg.regErr(error1, "writing error to client didnt work");
      }
    }finally{
//      sSCon.closeCon();
    }
    
    return Response.ok(stream).build();
  }
  
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @Path(SSStrU.slash + "fileReplace")
  public Response fileReplace(
    @FormDataParam(SSVarU.jsonRequ)   String      jsonRequ,
    @FormDataParam(SSVarU.fileHandle) InputStream fileHandle){
    
    Response result = null;
    byte[]   bytes  = new byte[SSSocketU.socketTranmissionSize];
    String   returnMsg;
    
    try{
      
      sSCon = new SSSocketCon(conf.host, conf.port, jsonRequ);
      
      sSCon.writeRequFullToSS ();
      sSCon.readMsgFullFromSS();
      
      while ((read = fileHandle.read(bytes)) != -1){
        sSCon.writeFileChunkToSS  (bytes, read);
//        sSCon.readMsgFullFromSS   ();
      }
      
      sSCon.writeFileChunkToSS(new byte[0], -1);
      
      returnMsg = sSCon.readMsgFullFromSS();
      
      return Response.status(200).entity(returnMsg).build();
      
    }catch(Exception error){
      
      try{
        return Response.serverError().build();
      }catch(Exception error1){
        SSServErrReg.regErr(error1, "writing error to client didnt work");
      }
    }finally{
      sSCon.closeCon();
    }
    
    return result;
  }
  
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @Path(SSStrU.slash + "fileUpload")
  public Response fileUpload(
    @FormDataParam(SSVarU.jsonRequ)   String      jsonRequ,
    @FormDataParam(SSVarU.fileHandle) InputStream fileHandle){
    
    Response result = null;
    byte[]   bytes  = new byte[SSSocketU.socketTranmissionSize];
    String   resultMsg;
    
    try{
      
      sSCon = new SSSocketCon(conf.host, conf.port, jsonRequ);
      
      sSCon.writeRequFullToSS  ();
      sSCon.readMsgFullFromSS  ();

      while ((read = fileHandle.read(bytes)) != -1) {
        sSCon.writeFileChunkToSS   (bytes, read);
//        sSCon.readMsgFullFromSS    ();
      }

      sSCon.writeFileChunkToSS(new byte[0], -1);

      resultMsg = sSCon.readMsgFullFromSS();
      
      sSCon.closeCon();
      
      return Response.status(200).entity(resultMsg).build();
      
    }catch(Exception error){
      
      try{
        return Response.serverError().build();
      }catch(Exception error1){
        SSServErrReg.regErr(error1, "writing error to client didnt work");
      }
    }finally{
      sSCon.closeCon();
    }
    
    return result;
  }
  
  private String handleStandardJSONRESTCall(
    final String  jsonRequ,
    final SSMethU op){
    
    String      readMsgFullFromSS;
    
    try{
      
      try{
        sSCon = new SSSocketCon(conf.host, conf.port, jsonRequ);
      }catch(Exception error){
        
        SSLogU.info("couldnt connect to " + conf.host.toString() + " " + conf.port.toString());
        throw error;
      }
      
      try{
        sSCon.writeRequFullToSS ();
      }catch(Exception error){
        
        SSLogU.info("couldnt write to " + conf.host.toString() + " " + conf.port.toString());
        throw error;
      }
          
      try{
        readMsgFullFromSS = sSCon.readMsgFullFromSS ();
      }catch(Exception error){
        
        SSLogU.info("couldnt read from " + conf.host.toString() + " " + conf.port.toString());
        throw error;
      }
      
      return checkAndHandleSSSNodeSwitch(readMsgFullFromSS, jsonRequ);
    
    }catch(Exception error){
      
      final List<SSErrForClient> errors = new ArrayList<>();
      
      try{
        
        errors.add(SSErrForClient.get(error));
        
        return sSCon.prepErrorToClient (errors, op);
        
      }catch(Exception error1){
        SSServErrReg.regErr(error1, "writing error to client didnt work");
      }
    }finally{
      
      if(sSCon != null){
        sSCon.closeCon();
      }
    }
    
    return null;
  }
  
  private String checkAndHandleSSSNodeSwitch(
    final String msgFullFromSS, 
    final String clientJSONRequ) throws Exception{
    
    SSSocketCon sssNodeSocketCon = null;
    
    try{
      
      final SSClientPar clientPar = new SSClientPar (msgFullFromSS);
      
      if(!clientPar.useDifferentServiceNode){
        return msgFullFromSS;
      }
      
      sssNodeSocketCon =
        new SSSocketCon(
          clientPar.sssNodeHost,
          clientPar.sssNodePort,
          clientJSONRequ);
      
      sssNodeSocketCon.writeRequFullToSS();
      
      return sssNodeSocketCon.readMsgFullFromSS ();
      
    }catch(Exception error){
      SSServErrReg.regErr     (error);
      SSServErrReg.regErrThrow(new SSDeployServiceOnNodeErr());
      return null;
    }finally{
      if(sssNodeSocketCon != null){
        sssNodeSocketCon.closeCon();
      }
    }
  }
}