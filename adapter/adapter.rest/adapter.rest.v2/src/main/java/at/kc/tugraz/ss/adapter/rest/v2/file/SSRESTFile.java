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
package at.kc.tugraz.ss.adapter.rest.v2.file;

import at.kc.tugraz.ss.adapter.rest.v2.SSRESTObject;
import at.kc.tugraz.ss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileDownloadPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileReplacePar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUploadPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileReplaceRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileUploadRet;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSVarNames;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/files")
@Api( value = "/files")
public class SSRESTFile{
  
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/upload")
  @ApiOperation(
    value = "upload a file",
    response = SSFileUploadRet.class)
  public Response fileUpload(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "label for the file",
      required = true)
    @FormDataParam(SSVarNames.label)
    final String label,
    
    @ApiParam(
      value = "mimeType for the file",
      required = true)
    @FormDataParam(SSVarNames.mimeType)
    final String mimeType,
    
    @ApiParam(
      value = "file data",
      required = true)
    @FormDataParam("file") 
    final InputStream file){ //@FormDataParam("my_file") FormDataBodyPart body Then you can use body.getMediaType()
    
    final SSFileUploadPar  par;
    final SSRESTObject     restObj;
    
    try{
      
      par =
        new SSFileUploadPar(
          SSServOpE.fileUpload,
          null,
          null,
          SSMimeTypeE.get(mimeType),  //mimeType
          SSLabel.get(label),  //label
          null, //sSCon
          true); //shouldCommit
      
      restObj = new SSRESTObject(par);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleFileUploadRequest(headers, restObj, file).response;
  }
  
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/replace/{entity}")
  @ApiOperation(
    value = "replace a file",
    response = SSFileReplaceRet.class)
  public Response fileReplace(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "entity to be replaced",
      required = true)
    @PathParam(SSVarNames.entity)
    final String entity,
    
    @ApiParam(
      value = "file data",
      required = true)
    @FormDataParam("file") 
    final InputStream file){
    
    final SSFileReplacePar par;
    final SSRESTObject     restObj;
    
    try{
      
      par =
        new SSFileReplacePar(
          SSServOpE.fileReplace,
          null,
          null,
          SSUri.get(entity, SSVocConf.sssUri), //entity
          null, //sSCon
          true); //shouldCommit
      
      restObj = new SSRESTObject(par);
        
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleFileUploadRequest(headers, restObj, file).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/download/{entity}")
  @ApiOperation(
    value = "download a file",
    response = byte.class)
  public Response fileDownload(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "entity to be downloaded",
      required = true)
    @PathParam(SSVarNames.entity)
    final String entity) throws Exception{
    
    final SSFileDownloadPar par;
    String fileName;
    
    try{
      
      par =
        new SSFileDownloadPar(
          SSServOpE.fileDownload,
          null,
          null,
          SSUri.get(entity, SSVocConf.sssUri), //entity
          null); //shouldCommit
      
      fileName = SSStrU.removeTrailingSlash(par.file);
      fileName = fileName.substring(fileName.lastIndexOf(SSStrU.slash) + 1);
      
    }catch(Exception error){
      return Response.status(422).build();
    }

    return SSRestMainV2.handleFileDownloadRequest(headers, par, fileName);
  }
}

// @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_OCTET_STREAM)
//  @Path(SSStrU.slash + "fileDownload")
//  @ApiOperation(
//    value = "download a file via POST request",
//    response = byte.class)
//  public Response fileDownload(final SSFileDownloadPar input){
//    
//    StreamingOutput  stream = null;
//    SSSocketCon      sSCon;
//    
//    try{
//      sSCon = new SSSocketCon(SSRestMainV1.conf.sss.host, SSRestMainV1.conf.sss.port);
//      
//      sSCon.writeRequFullToSS   (SSJSONU.jsonStr(input));
//      sSCon.readMsgFullFromSS   ();
//      sSCon.writeRequFullToSS   (SSJSONU.jsonStr(input));
//
//      stream = new StreamingOutput(){
//
//        @Override
//        public void write(OutputStream out) throws IOException{
//          
//          byte[] bytes;
//
//          while((bytes = sSCon.readFileChunkFromSS()).length > 0) {
//
//            out.write               (bytes);
//            out.flush               ();
//          }
//          
//          out.close();
//        }
//      };
//    }catch(Exception error){
//      
//      try{
//        return Response.serverError().build();
//      }catch(Exception error1){
//        SSServErrReg.regErr(error1, "writing error to client didnt work");
//      }
//    }finally{
////      sSCon.closeCon();
//    }
//    
//    return Response.ok(stream).build();
//  }