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
package at.kc.tugraz.ss.adapter.rest.v1;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityUsersGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityPublicSetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityPublicSetRet;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitySharePar;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntityUsersGetRet;
import at.kc.tugraz.ss.circle.datatypes.ret.SSCircleEntityShareRet;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityCopyPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.ret.SSEntityCopyRet;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserCumulatedTagsGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesDeletePar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryChangePosPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryDeletePar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserHierarchyGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserParentGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserWithEntriesPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserCouldSubscribeGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserEntityIsInGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserWithEntriesPar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserCumulatedTagsGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesDeleteRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryChangePosRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryDeleteRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserHierarchyGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserParentGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserRootGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserWithEntriesRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserCouldSubscribeGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserEntityIsInGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserWithEntriesRet;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import sss.serv.eval.datatypes.par.SSEvalLogPar;
import sss.serv.eval.datatypes.ret.SSEvalLogRet;

@Path("")
@Api( value = "SSAdapterRest")
public class SSAdapterRest{
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityPublicSet")
  @ApiOperation(
    value = "set an entity public (make it accessible for everyone)",
    response = SSCircleEntityPublicSetRet.class)
  public String entityPublicSet(final SSCircleEntityPublicSetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.circleEntityPublicSet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityCopy")
  @ApiOperation(
    value = "copy an entity and hand it to a user",
    response = SSEntityCopyRet.class)
  public String entityCopy(final SSEntityCopyPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.entityCopy);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "entityEntityUsersGet")
  @ApiOperation(
    value = "retrieve users who can access given entity",
    response = SSCircleEntityUsersGetRet.class)
  public String entityEntityUsersGet(final SSCircleEntityUsersGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.circleEntityUsersGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collsEntityIsInGet")
  @ApiOperation(
    value = "retrieve all the user's collections given entity is in",
    response = SSCollsUserEntityIsInGetRet.class)
  public String collsEntityIsInGet(final SSCollsUserEntityIsInGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collsEntityIsInGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collsCouldSubscribeGet")
  @ApiOperation(
    value = "retrieve a list of all public collections given user could subscribe to",
    response = SSCollsUserCouldSubscribeGetRet.class)
  public String collsCouldSubscribeGet(final SSCollsUserCouldSubscribeGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collsCouldSubscribeGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collRootGet")
  @ApiOperation(
    value = "retrieve the user's root collection",
    response = SSCollUserRootGetRet.class)
  public String collRootGet(final SSCollUserRootGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collRootGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collParentGet")
  @ApiOperation(
    value = "retrieve the parent collection for given user's collection",
    response = SSCollUserParentGetRet.class)
  public String collParentGet(final SSCollUserParentGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collParentGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collEntryAdd")
  @ApiOperation(
    value = "add a (new) collection or any other entity to given user's collection",
    response = SSCollUserEntryAddRet.class)
  public String collEntryAdd(final SSCollUserEntryAddPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collEntryAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collEntriesAdd")
  @ApiOperation(
    value = "add existing collections or (new) entities to a collection",
    response = SSCollUserEntriesAddRet.class)
  public String collEntriesAdd(final SSCollUserEntriesAddPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collEntriesAdd);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collEntryChangePos")
  @ApiOperation(
    value = "change the sequential order of entries in a user's collection",
    response = SSCollUserEntryChangePosRet.class)
  public String collEntryChangePos(final SSCollUserEntryChangePosPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collEntryChangePos);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collEntryDelete")
  @ApiOperation(
    value = "delete an item from a user's collection",
    response = SSCollUserEntryDeleteRet.class)
  public String collEntryDelete(final SSCollUserEntryDeletePar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collEntryDelete);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collEntriesDelete")
  @ApiOperation(
    value = "delete one or more entries from a collection",
    response = SSCollUserEntriesDeleteRet.class)
  public String collEntriesDelete(final SSCollUserEntriesDeletePar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collEntriesDelete);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "circleEntityShare")
  @ApiOperation(
    value = "share an entity directly with given users",
    response = SSCircleEntityShareRet.class)
  public String circleEntityShare(final SSCircleEntitySharePar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.circleEntityShare);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collWithEntries")
  @ApiOperation(
    value = "retrieve a user's collection with entries",
    response = SSCollUserWithEntriesRet.class)
  public String collWithEntries(final SSCollUserWithEntriesPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collWithEntries);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collsWithEntries")
  @ApiOperation(
    value = "retrieve the user's collections with entries",
    response = SSCollsUserWithEntriesRet.class)
  public String collsWithEntries(final SSCollsUserWithEntriesPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collsWithEntries);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collHierarchyGet")
  @ApiOperation(
    value = "retrieve the parent collection order for a user's collection",
    response = SSCollUserHierarchyGetRet.class)
  public String collHierarchyGet(final SSCollUserHierarchyGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collHierarchyGet);
  }
   
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "collCumulatedTagsGet")
  @ApiOperation(
    value = "retrieve the cumulated tags (and their frequencies) for all the sub collections and respective entities",
    response = SSCollUserCumulatedTagsGetRet.class)
  public String collCumulatedTagsGet(final SSCollUserCumulatedTagsGetPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collCumulatedTagsGet);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path    (SSStrU.slash + "evalLog")
  @ApiOperation(
    value = "log events for evaluation purposes",
    response = SSEvalLogRet.class)
  public String evalLog(final SSEvalLogPar input){
    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.evalLog);
  }
}


//    Map<String, Object> ret = new HashMap<>();
//    
//    try{
//      ret.put(SSVarU.op,                  SSServOpE.jsonLD);
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