/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSMimeTypeU;
import at.kc.tugraz.socialserver.utils.SSSocketU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.adapter.rest.conf.SSAdapterRestConf;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.conf.conf.SSConf;
import at.kc.tugraz.ss.serv.err.reg.SSErrForClient;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.ss.service.user.api.SSUserGlobals;
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
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.lang3.ArrayUtils;

@Path("/SSAdapterRest")
public class SSAdapterRest{
  
  private SSSocketCon     sSCon   = null;
  private int             read    = -1;
  private SSConf          conf    = null;
  
  public SSAdapterRest() throws Exception{
    
//    SSLogU.info("rest enter");
    SSAdapterRestConf.instSet (SSFileU.dirCatalinaBase() + SSFileU.folderConf + "ss-adapter-rest-conf.yaml"); //"ss-adapter-rest-conf_domi.yaml" //"ss-adapter-rest-conf_newer.yaml"
    
    /**** utils ****/
    SSMimeTypeU.init();
    SSJSONLDU.init  (SSAdapterRestConf.instGet().getJsonLDConf().uri);
    
    /**** json-ld ****/
//    SSJSONLD.inst.initServ(SSAdapterRestConf.instGet().getJsonLDConf());
    
    conf = SSAdapterRestConf.instGet().getSsConf();
  }
  
  @GET
  @Consumes(MediaType.TEXT_HTML)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "jsonLD" + SSStrU.slash + SSStrU.curlyBracketOpen + SSVarU.entityType + SSStrU.curlyBracketClose)
  public String jsonLD(@PathParam(SSVarU.entityType) String entityType){
    
    String jsonRequ = "{\"op\":\"" + SSMethU.jsonLD + "\",\"user\":\"" + SSUserGlobals.systemUserURI + "/\",\"entityType\":\"" + entityType + "\",\"key\":\"681V454J1P3H4W3B367BB79615U184N22356I3E\"}";
    
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.jsonLD);
    
    
//    Map<String, Object> ret = new HashMap<String, Object>();
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
  }

  @GET
  @Consumes(MediaType.TEXT_HTML)
  @Produces(SSMimeTypeU.imagePng)
  @Path(SSStrU.slash + "fileThumbGet" + SSStrU.slash + SSStrU.curlyBracketOpen + SSVarU.id + SSStrU.curlyBracketClose)
  public Response fileThumbGet(@PathParam(SSVarU.id) String fileID){
    
    String     jsonRequ       = "{\"op\":\"" + SSMethU.fileThumbGet + "\",\"user\":\"http://eval.bp/user/dt/\",\"fileId\":\"" + fileID + "\",\"key\":\"681V454J1P3H4W3B367BB79615U184N22356I3E\"}";
    List<Byte> bytesFromSS   = new ArrayList<Byte>();
    String     imageString   = null;
    byte[]     bytes;
    Byte[]     nonPrimBytes;
    
    try{
      sSCon = new SSSocketCon(conf.host, conf.port, jsonRequ);
      
      sSCon.writeRequFullToSS   ();
      sSCon.readMsgFullFromSS   ();
      sSCon.writeRequFullToSS   ();
      
      while((bytes = sSCon.readFileChunkFromSS()).length > 0) {
        
        for(int counter = 0; counter < bytes.length; counter++){
          bytesFromSS.add(bytes[counter]);
        }
        
        sSCon.writeRequFullToSS ();
      }
      
      nonPrimBytes = bytesFromSS.toArray(new Byte[bytesFromSS.size()]);
      imageString  = "data:image/png;base64," + DatatypeConverter.printBase64Binary(ArrayUtils.toPrimitive(nonPrimBytes));

    }catch(Exception error){
      
      try{
        return Response.serverError().build();
      }catch(Exception error1){
        SSServErrReg.regErr(error1, "writing error to client didnt work");
      }
    }finally{
      sSCon.closeCon();
    }

    return Response.ok(imageString).build(); //non-streamed

    // uncomment line below to send streamed
    // return Response.ok(new ByteArrayInputStream(imageData)).build();
  }
  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "authCheckCred")
//  public String authCheckCred(String jsonRequ){
//    return handleStandardJSONRESTCall(jsonRequ, SSMethU.authCheckCred);
//  }
  
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
  @Path    (SSStrU.slash + "collsUserEntityIsInGet")
  public String collsUserEntityIsInGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collsUserEntityIsInGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collUserRootGet")
  public String collUserRootGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collUserRootGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collUserParentGet")
  public String collUserParentGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collUserParentGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collUserEntryAdd")
  public String collUserEntryAdd(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collUserEntryAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collUserEntriesAdd")
  public String collUserEntriesAdd(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collUserEntriesAdd);
  }  
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collUserEntryChangePos")
  public String collUserEntryChangePos(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collUserEntryChangePos);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collUserEntryDelete")
  public String collUserEntryDelete(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collUserEntryDelete);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collUserEntriesDelete")
  public String collUserEntriesDelete(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collUserEntriesDelete);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collUserShare")
  public String collUserShare(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collUserShare);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collSharedAll")
  public String collSharedAll(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collSharedAll);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collUserWithEntries")
  public String collUserWithEntries(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collUserWithEntries);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collsUserWithEntries")
  public String collsUserWithEntries(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collsUserWithEntries);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collUserHierarchyGet")
  public String collUserHierarchyGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collUserHierarchyGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collUserCumulatedTagsGet")
  public String collUserCumulatedTagsGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.collUserCumulatedTagsGet);
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
  @Path    (SSStrU.slash + "discWithEntries")
  public String discWithEntries(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.discWithEntries);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "discsAll")
  public String discsAll(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.discsAll);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityTypeGet")
  public String entityTypeGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityTypeGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityUserDirectlyAdjoinedEntitiesRemove")
  public String entityUserDirectlyAdjoinedEntitiesRemove(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityUserDirectlyAdjoinedEntitiesRemove);
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
  @Path    (SSStrU.slash + "entityLabelSet")
  public String entityLabelSet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityLabelSet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityLabelGet")
  public String entityLabelGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.entityLabelGet);
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
  @Path    (SSStrU.slash + "ratingUserSet")
  public String ratingUserSet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.ratingUserSet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "scaffRecommTags")
  public String scaffRecommTags(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.scaffRecommTags);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "searchMIs")
  public String searchMIs(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.searchMIs);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "searchSolr")
  public String searchSolr(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.searchSolr);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "searchTags")
  public String searchTags(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.searchTags);
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
  @Path    (SSStrU.slash + "tagUserFrequsGet")
  public String tagUserFrequsGet(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.tagUserFrequsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "tagsUserRemove")
  public String tagsUserRemove(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.tagsUserRemove);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "userLogin")
  public String userLogin(String jsonRequ){
    return handleStandardJSONRESTCall(jsonRequ, SSMethU.userLogin);
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
            
            sSCon.writeRequFullToSS ();
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
    @FormDataParam(SSVarU.jsonRequ) String      jsonRequ,
    @FormDataParam(SSVarU.file)     InputStream file){
    
    Response result = null;
    byte[]   bytes  = new byte[SSSocketU.socketTranmissionSize];
    String   returnMsg;
    
    try{
      
      sSCon = new SSSocketCon(conf.host, conf.port, jsonRequ);
      
      sSCon.writeRequFullToSS ();
      sSCon.readMsgFullFromSS();
      
      while ((read = file.read(bytes)) != -1){
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
    @FormDataParam(SSVarU.jsonRequ) String      jsonRequ,
    @FormDataParam(SSVarU.file)     InputStream file){
    
    Response result = null;
    byte[]   bytes  = new byte[SSSocketU.socketTranmissionSize];
    String   resultMsg;
    
    try{
      
      sSCon = new SSSocketCon(conf.host, conf.port, jsonRequ);
      
      sSCon.writeRequFullToSS  ();
      sSCon.readMsgFullFromSS  ();

      while ((read = file.read(bytes)) != -1) {
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
  
  private String handleStandardJSONRESTCall(String jsonRequ, SSMethU op){
    
    String readMsgFullFromSS;
    
    try{
      sSCon = new SSSocketCon(conf.host, conf.port, jsonRequ);
      
      sSCon.writeRequFullToSS                     ();
      
      readMsgFullFromSS = sSCon.readMsgFullFromSS ();
      
      return readMsgFullFromSS;
    
    }catch(Exception error){
      
      final List<SSErrForClient> errors = new ArrayList<SSErrForClient>();
      
      try{
        
        errors.add(SSErrForClient.get(error));
        
        return sSCon.prepErrorToClient (errors, op);
      }catch(Exception error1){
        SSServErrReg.regErr(error1, "writing error to client didnt work");
      }
    }finally{
      sSCon.closeCon();
      
//      SSLogU.info("rest leave");
    }
    
    return null;
  }
}