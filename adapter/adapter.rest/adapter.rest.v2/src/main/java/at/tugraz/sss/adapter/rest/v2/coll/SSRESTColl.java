/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.tugraz.sss.adapter.rest.v2.coll;

import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollCumulatedTagsGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesDeletePar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserHierarchyGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserWithEntriesPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserEntityIsInGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserCumulatedTagsGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntriesDeleteRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserEntryAddRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserHierarchyGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollUserRootGetRet;
import at.kc.tugraz.ss.service.coll.datatypes.ret.SSCollsUserEntityIsInGetRet;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class SSRESTColl{
  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "collsEntityIsInGet")
//  @ApiOperation(
//    value = "retrieve all the user's collections given entity is in",
//    response = SSCollsUserEntityIsInGetRet.class)
//  public String collsEntityIsInGet(final SSCollsUserEntityIsInGetPar input){
//    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collsEntityIsInGet);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "collRootGet")
//  @ApiOperation(
//    value = "retrieve the user's root collection",
//    response = SSCollUserRootGetRet.class)
//  public String collRootGet(final SSCollUserRootGetPar input){
//    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collRootGet);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "collEntryAdd")
//  @ApiOperation(
//    value = "add a (new) collection or any other entity to given user's collection",
//    response = SSCollUserEntryAddRet.class)
//  public String collEntryAdd(final SSCollUserEntryAddPar input){
//    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collEntryAdd);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "collEntriesAdd")
//  @ApiOperation(
//    value = "add existing collections or (new) entities to a collection",
//    response = SSCollUserEntriesAddRet.class)
//  public String collEntriesAdd(final SSCollUserEntriesAddPar input){
//    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collEntriesAdd);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "collEntriesDelete")
//  @ApiOperation(
//    value = "delete one or more entries from a collection",
//    response = SSCollUserEntriesDeleteRet.class)
//  public String collEntriesDelete(final SSCollUserEntriesDeletePar input){
//    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collEntriesDelete);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "collWithEntries")
//  @ApiOperation(
//    value = "retrieve a user's collection with entries",
//    response = SSCollGetRet.class)
//  public String collWithEntries(final SSCollUserWithEntriesPar input){
//    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collWithEntries);
//  }
//  
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "collHierarchyGet")
//  @ApiOperation(
//    value = "retrieve the parent collection order for a user's collection",
//    response = SSCollUserHierarchyGetRet.class)
//  public String collHierarchyGet(final SSCollUserHierarchyGetPar input){
//    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collHierarchyGet);
//  }
//   
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    (SSStrU.slash + "collCumulatedTagsGet")
//  @ApiOperation(
//    value = "retrieve the cumulated tags (and their frequencies) for all the sub collections and respective entities",
//    response = SSCollUserCumulatedTagsGetRet.class)
//  public String collCumulatedTagsGet(final SSCollCumulatedTagsGetPar input){
//    return SSRestMainV1.handleStandardJSONRESTCall(input, SSServOpE.collCumulatedTagsGet);
//  }
}
