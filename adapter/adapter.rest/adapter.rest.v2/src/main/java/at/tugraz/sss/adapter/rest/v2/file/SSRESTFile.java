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
package at.tugraz.sss.adapter.rest.v2.file;

import at.tugraz.sss.adapter.rest.v2.SSRESTObject;
import at.tugraz.sss.adapter.rest.v2.SSRestMainV2;
import at.tugraz.sss.conf.SSConf;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileDownloadPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUploadPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileUploadRet;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.util.SSMimeTypeE;
import at.tugraz.sss.serv.util.*;
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
import javax.ws.rs.QueryParam;
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
      value = "circle to consider for metadata visibility",
      required = false)
    @FormDataParam(SSVarNames.circle)
    final String circle,
    
    @ApiParam(
      value = "mimeType for the file",
      required = true)
    @FormDataParam(SSVarNames.mimeType)
    final String mimeType,
    
    @ApiParam(
      value = "file data",
      required = true)
    @FormDataParam("file") 
    final InputStream fileHandle){ //@FormDataParam("my_file") FormDataBodyPart body Then you can use body.getMediaType()
    
    final SSFileUploadPar  par;
    final SSRESTObject     restObj;
    
    try{
      
      par =
        new SSFileUploadPar(
          null,
          SSMimeTypeE.get(mimeType),  //mimeType
          SSLabel.get    (label),  //label
          SSUri.get(circle, SSConf.sssUri), //circle
          null, //sSCon
          true); //shouldCommit

      restObj = new SSRESTObject(par);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handleFileUploadRequest(headers, restObj, fileHandle).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("{file}/download")
  @ApiOperation(
    value = "download a file",
    response = byte.class)
  public Response fileDownload(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "file to be downloaded",
      required = true)
    @PathParam(SSVarNames.file)
    final String file) throws Exception{
    
    final SSFileDownloadPar par;
    final SSRESTObject      restObj;
    String                  fileName;
    
    try{
      
      par =
        new SSFileDownloadPar(
          null,
          SSUri.get(file, SSConf.sssUri), //entity
          null, //sScon
          false); //isPublicDownload
      
      fileName = SSStrU.removeTrailingSlash(par.file);
      fileName = fileName.substring(fileName.lastIndexOf(SSStrU.slash) + 1);
      
      restObj = new SSRESTObject(par);
      
    }catch(Exception error){
      return Response.status(422).build();
    }

    return SSRestMainV2.handleFileDownloadRequest(headers, restObj, fileName, true).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/download")
  @ApiOperation(
    value = "download a file with query params",
    response = byte.class)
  public Response fileDownloadQueryParam(
    @Context
    final HttpHeaders headers,
    
    @ApiParam(
      value = "authentication token",
      required = true)
    @QueryParam("key")
    final String key,
      
    @ApiParam(
      value = "file to be downloaded",
      required = true)
    @QueryParam("file")
    final String file) throws Exception{
    
    final SSFileDownloadPar par;
    final SSRESTObject      restObj;
    String                  fileName;
    
    try{
      
      par =
        new SSFileDownloadPar(
          null,
          SSUri.get(file, SSConf.sssUri), //entity
          null, //sSCon
          false); //isPublicDownload
      
      par.key  = key;
      
      fileName = SSStrU.removeTrailingSlash(par.file);
      fileName = fileName.substring(fileName.lastIndexOf(SSStrU.slash) + 1);
      
      restObj = new SSRESTObject(par);
      
    }catch(Exception error){
      return Response.status(422).build();
    }

    return SSRestMainV2.handleFileDownloadRequest(headers, restObj, fileName, false).response;
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/{file}/download/public/")
  @ApiOperation(
    value = "download a public file",
    response = byte.class)
  public Response fileDownloadPublic(
    @ApiParam(
      value = "file to be downloaded",
      required = true)
    @PathParam(SSVarNames.file)
    final String file) throws Exception{
    
    final SSFileDownloadPar par;
    final SSRESTObject      restObj;
    String                  fileName;
    
    try{
      
      par =
        new SSFileDownloadPar(
          null,
          SSUri.get(file, SSConf.sssUri), //entity
          null, //sSCon
          true); 
      
      fileName = SSStrU.removeTrailingSlash(par.file);
      fileName = fileName.substring(fileName.lastIndexOf(SSStrU.slash) + 1);
      
      restObj = new SSRESTObject(par);
      
    }catch(Exception error){
      return Response.status(422).build();
    }

    return SSRestMainV2.handleFileDownloadRequest(null, restObj, fileName, false).response;
  }
}

//    if(SSFileExtU.imageFileExts.contains(SSFileExtU.ext(fileName))){
//      
//      return Response.
//        ok(stream).
//        header("Content-Disposition", "inline; filename=\"" + fileName + "\"").
//        header("Content-Type", SSMimeTypeU.mimeTypeForFileExt(SSFileExtU.ext(fileName))).
//        build();
//    }
//    
//    if(SSFileExtU.imageFileExts.contains(SSFileExtU.ext(fileName))){
//      
//    }
    
//      "Content-Disposition", "attachment; filename=\"" + fileName + "\"").
//
//  @PUT
//  @Consumes(MediaType.MULTIPART_FORM_DATA)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path("{file}/replace")
//  @ApiOperation(
//    value = "replace a file",
//    response = SSFileReplaceRet.class)
//  public Response fileReplace(
//    @Context
//    final HttpHeaders headers,
//    
//    @ApiParam(
//      value = "entity to be replaced",
//      required = true)
//    @PathParam(SSVarNames.file)
//    final String file,
//    
//    @ApiParam(
//      value = "file data",
//      required = true)
//    @FormDataParam("file") 
//    final InputStream fileHandle){
//    
//    final SSFileReplacePar par;
//    final SSRESTObject     restObj;
//    
//    try{
//      
//      par =
//        new SSFileReplacePar(
//          null,
//          SSUri.get(file, SSConf.sssUri), //entity
//          null, //sSCon
//          true); //shouldCommit
//      
//      restObj = new SSRESTObject(par);
//        
//    }catch(Exception error){
//      return Response.status(422).build();
//    }
//    
//    return SSRestMainV2.handleFileUploadRequest(headers, restObj, fileHandle).response;
//  }