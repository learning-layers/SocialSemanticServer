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
package at.tugraz.sss.adapter.rest.v2;

import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSJSONU;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.kc.tugraz.ss.adapter.rest.conf.SSAdapterRestConf;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSJSONLDU;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSSocketU;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ws.rs.core.StreamingOutput;

public class SSRestMainV2 extends Application {
  
  public static SSAdapterRestConf conf;

  public SSRestMainV2() throws Exception{
   
    SSAdapterRestConf.instSet (SSFileU.dirCatalinaBase() + SSVocConf.dirNameConf + "layer.test.yaml");
    
    conf = SSAdapterRestConf.instGet();
    
    SSFileExtE.init    ();
    SSMimeTypeE.init   ();
    SSJSONLDU.init(conf.getJsonLD().uri);
  }
  
  @Override
  public Set<Class<?>> getClasses() {

    final Set<Class<?>> classes = new HashSet<>();
    
    classes.add(MultiPartFeature.class);

    return classes;
  }
  
  private static String getBearer(
    final HttpHeaders headers) throws Exception{
    
    String bearer = headers.getRequestHeader("authorization").get(0);
    return SSStrU.replaceAll(bearer, "Bearer ", SSStrU.empty);
  }
  
  public static SSRESTObject handleFileDownloadRequest(
    final HttpHeaders headers,
    SSRESTObject      restObj,
    final String      fileName,
    final Boolean     getKeyFromHeaders){
    
    final StreamingOutput stream;
    final SSSocketCon     sssCon;
    
    try{
      restObj =
        handleRequest(
          headers,
          restObj,
          true,  //keepSSSConnectionOpen
          getKeyFromHeaders); //getKeyFromHeaders
      
      if(restObj.response.getStatus() != 200){
        return restObj;
      }
      
      try{
        restObj.sssCon.writeRequFullToSS (restObj.sssRequestMessage);
      }catch(Exception error){
        
        restObj.response =
          Response.status(500).entity(
            getJSONStrForError(
              SSErrE.sssWriteFailed)).build();
        
        return restObj;
      }
     
      sssCon = restObj.sssCon;

      try{
        stream = new StreamingOutput(){

          @Override
          public void write(OutputStream out) throws IOException{

            byte[] bytes;
            
            while((bytes = sssCon.readFileChunkFromSS()).length > 0) {

              out.write               (bytes);
              out.flush               ();
            }

            out.close();
            
            try{
              sssCon.closeCon();
            }catch(Exception error){
              SSLogU.warn("socket connection not closed");
            }
          }
        };
      }catch(Exception error){
        
        restObj.response = 
          Response.status(500).entity(
            SSRestMainV2.getJSONStrForError(
              SSErrE.sssReadFailed)).build();
        
        return restObj;
      }
      
      restObj.response = 
        Response.ok(stream).
          header("Content-Disposition", "inline; filename=\"" + fileName + "\"").
          header("Content-Type", SSMimeTypeE.mimeTypeForFileExt(SSFileExtE.ext(fileName)).toString()).
          build();
      
      return restObj;
      
    }catch(Exception error){
      
      try{
        
        if(restObj.sssCon != null){
          restObj.sssCon.closeCon();
        }
      }catch(Exception error1){
        SSLogU.warn("socket connection not closed correctly");
      }
      
      restObj.response =
        Response.status(500).entity(
          SSRestMainV2.getJSONStrForError(
            SSErrE.restAdapterInternalError)).build();
      
      return restObj;
    }
  }
  
  public static SSRESTObject handleFileUploadRequest(
    final HttpHeaders headers,
    SSRESTObject      restObj,
    final InputStream file){
    
    final ObjectMapper          sssJSONResponseMapper    = new ObjectMapper();
    final JsonNode              sssJSONResponseRootNode;
    byte[]                      bytes   = new byte[SSSocketU.socketTranmissionSize];
    int                         read;
    
    try{
      restObj =
        handleRequest(
          headers,
          restObj,
          true,  //keepSSSConnectionOpen
          true); //getKeyFromHeaders
      
      if(restObj.response.getStatus() != 200){
        return restObj;
      }
      
      try{
        
        while((read = file.read(bytes)) != -1) {
          restObj.sssCon.writeFileChunkToSS   (bytes, read);
        }
        
        restObj.sssCon.writeFileChunkToSS(new byte[0], -1);
      }catch(Exception error){
        
        restObj.response =
          Response.status(500).entity(
            SSRestMainV2.getJSONStrForError(
              SSErrE.sssWriteFailed)).build();
        
        return restObj;
      }
      
      try{
        restObj.sssResponseMessage = restObj.sssCon.readMsgFullFromSS();
      }catch(Exception error){
        
        restObj.response =
          Response.status(500).entity(
            getJSONStrForError(
              SSErrE.sssReadFailed)).build();
        
        return restObj;
      }
      
//      restObj.sssResponseMessage =
//        checkAndHandleSSSNodeSwitch(
//          restObj.sssResponseMessage,
//          restObj.sssRequestMessage);

      try{
        sssJSONResponseRootNode = sssJSONResponseMapper.readTree(restObj.sssResponseMessage);

        if(sssJSONResponseRootNode.get(SSVarNames.error).getBooleanValue()){

          restObj.response =
            Response.status(500).entity(
              getJSONStrForError(
                sssJSONResponseRootNode.get(SSVarNames.id).getTextValue(),
                sssJSONResponseRootNode.get(SSVarNames.message).getTextValue())).build();

        }else{
          restObj.response =
            Response.status(200).entity(
              SSJSONU.jsonStr(
                sssJSONResponseRootNode.get(restObj.par.op.toString()))).build();
        }
        
      }catch(Exception error){
        
        restObj.response =
          Response.status(500).entity(
            getJSONStrForError(
              SSErrE.sssResponseParsingFailed)).build();
      }
      
      return restObj;
      
    }catch(Exception error){
      
      restObj.response =
        Response.status(500).entity(
          SSRestMainV2.getJSONStrForError(
            SSErrE.restAdapterInternalError)).build();
      
      return restObj;
      
    }finally{
      
      try{
        
        if(restObj.sssCon != null){
          restObj.sssCon.closeCon();
        }
        
      }catch(Exception error1){
        SSLogU.warn("socket connection not closed correctly");
      }
    }
  }
  
  public static SSRESTObject handleRequest(
    final HttpHeaders      headers,
    final SSServPar        par, 
    final Boolean          keepSSSConnectionOpen,
    final Boolean          getKeyFromHeaders){
    
    return handleRequest(
      headers, 
      new SSRESTObject(par), 
      keepSSSConnectionOpen, 
      getKeyFromHeaders);
  }
    
  public static SSRESTObject handleRequest(
    final HttpHeaders      headers,
    SSRESTObject           restObj,
    final Boolean          keepSSSConnectionOpen,
    final Boolean          getKeyFromHeaders){
    
    final ObjectMapper sssJSONResponseMapper    = new ObjectMapper();
    final JsonNode     sssJSONResponseRootNode;
    
    if(getKeyFromHeaders){
    
      try{
        restObj.par.key = getBearer(headers);
      }catch(Exception error){

        restObj.response = Response.status(401).build();

        return restObj;
      }
    }
    
    try{
      restObj.sssRequestMessage = SSJSONU.jsonStr(restObj.par);
    }catch(Exception error){
      
      restObj.response = 
        Response.status(500).entity(
          getJSONStrForError(
            SSErrE.sssJsonRequestEncodingFailed)).build();
      
      return restObj;
    }
    
    try{

      try{
        restObj.sssCon = new SSSocketCon(conf.sss.host, conf.sss.port);
      }catch(Exception error){
        
        restObj.response =
          Response.status(500).entity(
            getJSONStrForError(
              SSErrE.sssConnectionFailed)).build();
        
        return restObj;
      }
      
      try{
        restObj.sssCon.writeRequFullToSS (restObj.sssRequestMessage);
      }catch(Exception error){
        
        restObj.response =
          Response.status(500).entity(
            getJSONStrForError(
              SSErrE.sssWriteFailed)).build();
        
        return restObj;
      }
      
      try{
        restObj.sssResponseMessage = restObj.sssCon.readMsgFullFromSS();
      }catch(Exception error){
        
        restObj.response =
          Response.status(500).entity(
            getJSONStrForError(
              SSErrE.sssReadFailed)).build();
        
        return restObj;
      }
      
//      restObj.sssResponseMessage =
//        checkAndHandleSSSNodeSwitch(
//          restObj.sssResponseMessage,
//          restObj.sssRequestMessage);

      try{
        sssJSONResponseRootNode = sssJSONResponseMapper.readTree(restObj.sssResponseMessage);

        if(sssJSONResponseRootNode.get(SSVarNames.error).getBooleanValue()){

          restObj.response =
            Response.status(500).entity(
              getJSONStrForError(
                sssJSONResponseRootNode.get(SSVarNames.id).getTextValue(),
                sssJSONResponseRootNode.get(SSVarNames.message).getTextValue())).build();

        }else{
          
          restObj.response =
            Response.status(200).entity(
              SSJSONU.jsonStr(
                sssJSONResponseRootNode.get(restObj.par.op.toString()))).build();
        }
        
      }catch(Exception error){
        
        restObj.response =
          Response.status(500).entity(
            getJSONStrForError(
              SSErrE.sssResponseParsingFailed)).build();
      }
      
      return restObj;
      
    }catch(Exception error){
      
      restObj.response =
        Response.status(500).entity(
          getJSONStrForError(
            SSErrE.restAdapterInternalError)).build();
      
      return restObj;
      
    }finally{
      
      try{
      if(
        !keepSSSConnectionOpen &&
        restObj.sssCon != null){
        
        restObj.sssCon.closeCon();
      }
      }catch(Exception error){
        SSLogU.warn("socket connection not closed correctly");
    }
  }
  }
  
  public static String getJSONStrForError(
    final SSErrE id){
    
    final Map<String, Object> jsonObj     = new HashMap<>();
    
    if(id == null){
      jsonObj.put(SSVarNames.id,                    null);
      jsonObj.put(SSVarNames.message,               null);
    }else{
      jsonObj.put(SSVarNames.id,                    id.toString());
      jsonObj.put(SSVarNames.message,               id.toString());
    }
    
    try{
      return SSJSONU.jsonStr(jsonObj);
    } catch(Exception error){
      return null;
    }
  }
  
  public static String getJSONStrForError(
    final String id,
    final String message) throws Exception{
    
    final Map<String, Object> jsonObj     = new HashMap<>();
    
    jsonObj.put(SSVarNames.id,                      id);
    
    if(message == null){
      jsonObj.put(SSVarNames.message,               id);
    }else{
      jsonObj.put(SSVarNames.message,               message);
    }
    
    try{
      return SSJSONU.jsonStr(jsonObj);
    } catch(Exception error){
      return null;
    }
  }
}

//  private static String checkAndHandleSSSNodeSwitch(
//    final String msgFullFromSS,
//    final String clientJSONRequ) throws Exception{
//    
//    SSSocketCon sssNodeSocketCon = null;
//    
//    try{
//      
//      final SSClientPar clientPar = new SSClientPar (msgFullFromSS);
//      
//      if(!clientPar.useDifferentServiceNode){
//        return msgFullFromSS;
//      }
//      
//      sssNodeSocketCon =
//        new SSSocketCon(
//          clientPar.sssNodeHost,
//          clientPar.sssNodePort,
//          clientJSONRequ);
//      
//      sssNodeSocketCon.writeRequFullToSS();
//      
//      return sssNodeSocketCon.readMsgFullFromSS ();
//      
//    }catch(Exception error){
//      SSServErrReg.regErr     (error);
//      SSServErrReg.regErrThrow(new SSErr(SSErrE.deployingServiceOnNodeFailed));
//      return null;
//    }finally{
//      if(sssNodeSocketCon != null){
//        sssNodeSocketCon.closeCon();
//      }
//    }
//  }