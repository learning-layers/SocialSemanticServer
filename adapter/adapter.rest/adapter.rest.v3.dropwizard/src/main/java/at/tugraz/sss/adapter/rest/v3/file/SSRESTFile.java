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
package at.tugraz.sss.adapter.rest.v3.file;

import at.tugraz.sss.servs.file.datatype.ret.SSFileDownloadRet;
import at.tugraz.sss.servs.file.datatype.ret.SSFileUploadRet;
import at.tugraz.sss.servs.file.datatype.par.SSFileUploadPar;
import at.tugraz.sss.servs.file.datatype.par.SSFileDownloadPar;
import at.tugraz.sss.servs.file.api.SSFileClientI;
import at.tugraz.sss.adapter.rest.v3.SSRESTCommons;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.SSMimeTypeE;
import at.tugraz.sss.serv.util.*;
import io.swagger.annotations.*;
import java.io.*;
import java.sql.*;
import javax.annotation.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.glassfish.jersey.media.multipart.*;

@Path("rest/files")
@Api( value = "files")
public class SSRESTFile{
  
  @PostConstruct
  public void createRESTResource(){
  }
  
  @PreDestroy
  public void destroyRESTResource(){
  }
  
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
      value = "description for the file",
      required = false)
    @FormDataParam(SSVarNames.description)
    final String description,
    
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
    
//    @ApiParam(
//      value = "file data",
//      required = true)
    @FormDataParam("file")
    final InputStream file){ //@FormDataParam("my_file") FormDataBodyPart body Then you can use body.getMediaType() //    @FormDataParam("file") FormDataContentDisposition fileDisposition){
    
    final SSFileUploadPar  par;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSFileUploadPar(
            new SSServPar(sqlCon),
            null,
            SSMimeTypeE.get   (mimeType),  //mimeType
            SSLabel.get       (label),  //label
            SSTextComment.get (description),  //description
            SSUri.get         (circle, SSConf.sssUri), //circle
            null, //clientSocket
            file, //fileInputStream
            SSClientE.rest, //clientType
            true); //shouldCommit
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        par.key = SSRESTCommons.getBearer(headers);
      }catch(Exception error){
        return Response.status(401).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSFileClientI fileServ = (SSFileClientI) SSServReg.getClientServ(SSFileClientI.class);
        
        return Response.status(200).entity(fileServ.fileUpload(SSClientE.rest, par)).build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
  }
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/{file}/download")
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
    final String file) throws SSErr{
    
    final SSFileDownloadPar par;
//    String                  fileName;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSFileDownloadPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(file, SSConf.sssUri), //file
            null, //clientSocket
            false, //isPublicDownload
            SSClientE.rest);  //clientTpype
        
//        fileName = SSStrU.removeTrailingSlash(par.file);
//        fileName = fileName.substring(fileName.lastIndexOf(SSStrU.slash) + 1);
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSFileClientI fileServ    = (SSFileClientI) SSServReg.getClientServ(SSFileClientI.class);
        final SSFileDownloadRet ret         = (SSFileDownloadRet) fileServ.fileDownload(SSClientE.rest, par);
        
        return Response.ok(ret.outputStream).
          header("Content-Disposition", "inline; filename=\"" + ret.label + SSStrU.dot + SSFileExtE.getFromStrToFormat(SSStrU.toStr(ret.file)) + "\"").
          header("Content-Type", SSMimeTypeE.mimeTypeForFileExt(SSFileExtE.ext(ret.file)).toString()).
          build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
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
    final String file) throws SSErr{
    
    final SSFileDownloadPar par;
//    String                  fileName;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSFileDownloadPar(
            new SSServPar(sqlCon),
            null,
            SSUri.get(file, SSConf.sssUri), //entity
            null, //clientSocket
            false, //isPublicDownload
            SSClientE.rest);  //clientTpype
        
        par.key  = key;
        
//        fileName = SSStrU.removeTrailingSlash(par.file);
//        fileName = fileName.substring(fileName.lastIndexOf(SSStrU.slash) + 1);
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSFileClientI fileServ    = (SSFileClientI) SSServReg.getClientServ(SSFileClientI.class);
        final SSFileDownloadRet ret         = (SSFileDownloadRet) fileServ.fileDownload(SSClientE.rest, par);
        
        return Response.ok(ret.outputStream).
          header("Content-Disposition", "inline; filename=\"" + ret.label + SSStrU.dot + SSFileExtE.getFromStrToFormat(SSStrU.toStr(ret.file)) + "\"").
          header("Content-Type", SSMimeTypeE.mimeTypeForFileExt(SSFileExtE.ext(ret.file)).toString()).
          build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
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
    final String file) throws SSErr{
    
    final SSFileDownloadPar par;
//    String                  fileName;
    Connection               sqlCon = null;
    
    try{
      
      try{
        sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
      
      try{
        
        par =
          new SSFileDownloadPar(
            new SSServPar(sqlCon),
            null, //user
            SSUri.get(file, SSConf.sssUri), //file
            null, //clientSocket
            true, //isPublicDownload
            SSClientE.rest);
        
//        fileName = SSStrU.removeTrailingSlash(par.file);
//        fileName = fileName.substring(fileName.lastIndexOf(SSStrU.slash) + 1);
        
      }catch(Exception error){
        return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
      }
      
      try{
        final SSFileClientI fileServ    = (SSFileClientI) SSServReg.getClientServ(SSFileClientI.class);
        final SSFileDownloadRet ret         = (SSFileDownloadRet) fileServ.fileDownload(SSClientE.rest, par);
        
        return Response.ok(ret.outputStream).
          header("Content-Disposition", "inline; filename=\"" + ret.label + SSStrU.dot + SSFileExtE.getFromStrToFormat(SSStrU.toStr(ret.file)) + "\"").
          header("Content-Type", SSMimeTypeE.mimeTypeForFileExt(SSFileExtE.ext(ret.file)).toString()).
          build();
        
      }catch(Exception error){
        return SSRESTCommons.prepareErrorResponse(error);
      }
    }finally{
      
      try{
        
        if(sqlCon != null){
          sqlCon.close();  
        }
      }catch(Exception error){
        SSLogU.err(error);
      }
    }
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
//  @Path("/{file}/replace")
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
//      return Response.status(422).entity(SSRESTCommons.prepareErrorJSON(error)).build();
//    }
//
//    return SSRestMainV2.handleFileUploadRequest(headers, restObj, fileHandle).response;
//  }