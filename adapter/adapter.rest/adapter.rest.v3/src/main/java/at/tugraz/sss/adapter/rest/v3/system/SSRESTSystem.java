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
package at.tugraz.sss.adapter.rest.v3.system;

//@Path("/system")
//@Api(value = "/system")
public class SSRESTSystem{

  //TODO reimplement
//  @GET
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path    ("/version")
//  @ApiOperation(
//    value = "retrieve the version of the sss instance",
//    response = SSSystemVersionGetRet.class)
//  public Response systemVersionGet(
//    @Context 
//      final HttpHeaders headers){
//
//    final ObjectMapper          sssJSONResponseMapper    = new ObjectMapper();
//    final SSSystemVersionGetPar par;
//    final SSRESTObject          restObj;
//    final SSSystemVersionGetRet ret;
//    final JsonNode              sssJSONResponseRootNode;
//    
//    try{
//      
//      par = new SSSystemVersionGetPar();
//      
//      restObj = new SSRESTObject(par);
//      
//    }catch(Exception error){
//      return Response.status(422).build();
//    }
//
//    try{
//      try{
//        
//        restObj.sssCon =
//          new SSSocketCon(
//            conf.sss.host,
//            conf.sss.port);
//        
//      }catch(Exception error){
//        
//        restObj.response =
//          Response.status(500).entity(
//            getJSONStrForError(
//              SSErrE.sssConnectionFailed)).build();
//        
//        return restObj.response;
//      }
//      
//      try{
//        ret                        = new SSSystemVersionGetRet         (SSRestMainV2.conf.sss.version);
//        restObj.sssResponseMessage = restObj.sssCon.prepRetFullToClient(ret, ret.op);
//      }catch(Exception error){
//        
//        restObj.response =
//          Response.status(500).entity(
//            getJSONStrForError(
//              SSErrE.sssReadFailed)).build();
//        
//        return restObj.response;
//      }
//      
//      try{
//        
//        sssJSONResponseRootNode = sssJSONResponseMapper.readTree(restObj.sssResponseMessage);
//        
//        restObj.response =
//          Response.status(200).entity(
//            SSJSONU.jsonStr(sssJSONResponseRootNode.get(restObj.par.op.toString()))).build();
//        
//      }catch(Exception error){
//        
//        restObj.response =
//          Response.status(500).entity(
//            getJSONStrForError(
//              SSErrE.sssResponseParsingFailed)).build();
//      }
//      
//      return restObj.response;
//      
//    }catch(Exception error){
//      
//      restObj.response =
//        Response.status(500).entity(
//          getJSONStrForError(
//            SSErrE.restAdapterInternalError)).build();
//      
//      return restObj.response;
//      
//    }finally{
//      
//      try{
//        if(restObj.sssCon != null){
//          restObj.sssCon.closeCon();
//        }
//      }catch(Exception error){
//        SSLogU.warn("socket connection not closed correctly");
//      }
//    }
//  }
}