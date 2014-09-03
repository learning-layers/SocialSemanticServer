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

import at.kc.tugraz.socialserver.utils.SSJSONU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSSocketU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileReplacePar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileReplaceRet;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/")
@Api( value = "SSAdapterRESTFileReplace", description = "SSS File Replace REST API" )
public class SSAdapterRESTFileReplace{
  
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @Path(SSStrU.slash + "fileReplace")
  @ApiOperation(
    value = "replace a file with a newer version",
    response = SSFileReplaceRet.class)
  public Response fileReplace(
    @FormDataParam(SSVarU.op)         final String op,
    @FormDataParam(SSVarU.user)       final String user,
    @FormDataParam(SSVarU.file)       final String file, 
    @FormDataParam(SSVarU.key)        final String key,
    
    @ApiParam(
      value = "file handle",
      required = true)
    @FormDataParam(SSVarU.fileHandle) 
    final InputStream fileHandle){

    Response     result = null;
    SSSocketCon  sSCon  = null;
    int          read;
    
    try{
      final SSFileReplacePar par    = new SSFileReplacePar();
      byte[]                 bytes  = new byte[SSSocketU.socketTranmissionSize];
      String                 returnMsg;
    
      par.op       = SSMethU.fileReplace;
      par.user     = SSUri.get(user);
      par.key      = key;
      par.file     = SSUri.get(file);
      
      sSCon = new SSSocketCon(SSRestMain.conf.host, SSRestMain.conf.port, SSJSONU.jsonStr(par));
      
      sSCon.writeRequFullToSS ();
      sSCon.readMsgFullFromSS();
      
      while ((read = fileHandle.read(bytes)) != -1){
        sSCon.writeFileChunkToSS  (bytes, read);
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
      
      if(sSCon != null){
        sSCon.closeCon();
      }
    }
    
    return result;
  }
}
