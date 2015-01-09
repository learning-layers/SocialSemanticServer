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

import at.kc.tugraz.socialserver.utils.SSFileExtU;
import at.kc.tugraz.socialserver.utils.SSJSONU;
import at.kc.tugraz.socialserver.utils.SSMimeTypeU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileDownloadPar;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.io.OutputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

@Path("")
@Api( value = "SSAdapterRESTFileDownload")
public class SSAdapterRESTFileDownload{
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path(SSStrU.slash + "fileDownload")
  @ApiOperation(
    value = "download a file via POST request",
    response = byte.class)
  public Response fileDownload(final SSFileDownloadPar input){
    
    StreamingOutput  stream = null;
    SSSocketCon      sSCon;
    
    try{
      sSCon = new SSSocketCon(SSRestMainV1.conf.ssConf.host, SSRestMainV1.conf.ssConf.port, SSJSONU.jsonStr(input));
      
      sSCon.writeRequFullToSS   ();
      sSCon.readMsgFullFromSS   ();
      sSCon.writeRequFullToSS   ();

      stream = new StreamingOutput(){

        @Override
        public void write(OutputStream out) throws IOException{
          
          byte[] bytes;

          while((bytes = sSCon.readFileChunkFromSS()).length > 0) {

            out.write               (bytes);
            out.flush               ();
          }
          
          out.close();
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
  
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path(SSStrU.slash + "fileDownloadGET")
  @ApiOperation(
    value = "download a file via GET request with query params",
    response = byte.class)
  public Response fileDownloadGET(
    @QueryParam("user")
    final String user,
    @QueryParam("key")
    final String key,
    @QueryParam("file")
    final String file) throws Exception{
    
    final String     input  = "{\"op\":\"fileDownload\",\"user\":\"" + user + "\",\"key\":\"" + key + "\",\"file\":\"" + file + "\"}";
    StreamingOutput  stream = null;
    SSSocketCon      sSCon;
    
    try{
      sSCon = new SSSocketCon(SSRestMainV1.conf.ssConf.host, SSRestMainV1.conf.ssConf.port, input);
      
      sSCon.writeRequFullToSS   ();
      sSCon.readMsgFullFromSS   ();
      sSCon.writeRequFullToSS   ();
      
      stream = new StreamingOutput(){
        
        @Override
        public void write(OutputStream out) throws IOException{
          
          byte[] bytes;
          
          while((bytes = sSCon.readFileChunkFromSS()).length > 0) {
            
            out.write               (bytes);
            out.flush               ();
          }
          
          out.close();
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

    String fileName  = SSStrU.removeTrailingSlash(file);
    
    fileName = fileName.substring(fileName.lastIndexOf(SSStrU.slash) + 1);
    
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
    return Response.
      ok(stream).
      header("Content-Disposition", "inline; filename=\"" + fileName + "\"").
      header("Content-Type", SSMimeTypeU.mimeTypeForFileExt(SSFileExtU.ext(fileName))).
      build();
    
//      "Content-Disposition", "attachment; filename=\"" + fileName + "\"").
  }
}