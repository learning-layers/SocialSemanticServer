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

import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSJSONU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSMimeTypeU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.adapter.rest.conf.SSAdapterRestConf;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSClientPar;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSErrForClient;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import sss.serv.err.datatypes.SSErr;
import sss.serv.err.datatypes.SSErrE;

public class SSRestMainV1 extends Application {
  
  public static SSAdapterRestConf conf;

  public SSRestMainV1() throws Exception{
   
    ResourceConfig resourceConfig;
    
    resourceConfig = new ResourceConfig(SSAdapterRESTFileUpload.class);
    resourceConfig.register(MultiPartFeature.class);
    
    resourceConfig = new ResourceConfig(SSAdapterRESTFileReplace.class);
    resourceConfig.register(MultiPartFeature.class);
    
    //    SSLogU.info("rest enter");
    SSAdapterRestConf.instSet (SSFileU.dirCatalinaBase() + SSVocConf.dirNameConf + "ss-adapter-rest-conf.yaml");
    
    /* util */
    SSMimeTypeU.init();
    SSJSONLDU.init(
      SSAdapterRestConf.instGet().getJsonLDConf().uri);
    
    /* json-ld */
//    SSJSONLD.inst.initServ(SSAdapterRestConf.instGet().getJsonLDConf());
    
    conf = SSAdapterRestConf.instGet();
  }
  
  @Override
  public Set<Class<?>> getClasses() {

    final Set<Class<?>> classes = new HashSet<>();
    
    classes.add(MultiPartFeature.class);
    classes.add(SSAdapterRest.class);
    classes.add(SSAdapterRESTFile.class);
    classes.add(SSAdapterRESTFileDownload.class);
    classes.add(SSAdapterRESTFileUpload.class);
    classes.add(SSAdapterRESTFileReplace.class);

    return classes;
  }
  
  public static String handleStandardJSONRESTCall(
    final SSServPar   input, 
    final SSMethU     op){
    
    try{
      
      input.op = op;
      
      return handleStandardJSONRESTCall(SSJSONU.jsonStr(input), op);
    }catch(Exception error){
      SSServErrReg.regErr(error);
      return null;
    }
  }
  
  public static String handleStandardJSONRESTCall(
    final String      jsonRequ,
    final SSMethU     op){
    
    SSSocketCon       sSCon = null;
    String            readMsgFullFromSS;
    
    try{
      
      try{
        sSCon = new SSSocketCon(conf.ssConf.host, conf.ssConf.port, jsonRequ);
      }catch(Exception error){
        
        SSLogU.info("couldnt connect to " + conf.ssConf.host + " " + conf.ssConf.port.toString());
        throw error;
      }
      
      try{
        sSCon.writeRequFullToSS ();
      }catch(Exception error){
        
        SSLogU.info("couldnt write to " + conf.ssConf.host + " " + conf.ssConf.port.toString());
        throw error;
      }
      
      try{
        readMsgFullFromSS = sSCon.readMsgFullFromSS ();
      }catch(Exception error){
        
        SSLogU.info("couldnt read from " + conf.ssConf.host + " " + conf.ssConf.port.toString());
        throw error;
      }
      
      return checkAndHandleSSSNodeSwitch(readMsgFullFromSS, jsonRequ);
      
    }catch(Exception error){
      
      final List<SSErrForClient> errors = new ArrayList<>();
      
      try{
        
        errors.add(SSErrForClient.get(error));
        
        if(sSCon != null){
          return sSCon.prepErrorToClient (errors, op);
        }
        
        SSServErrReg.regErrThrow(error);
        
      }catch(Exception error1){
        SSServErrReg.regErr(error1, "writing error to client didnt work");
      }
    }finally{
      
      if(sSCon != null){
        sSCon.closeCon();
      }
    }
    
    return null;
  }
  
  private static String checkAndHandleSSSNodeSwitch(
    final String msgFullFromSS,
    final String clientJSONRequ) throws Exception{
    
    SSSocketCon sssNodeSocketCon = null;
    
    try{
      
      final SSClientPar clientPar = new SSClientPar (msgFullFromSS);
      
      if(!clientPar.useDifferentServiceNode){
        return msgFullFromSS;
      }
      
      sssNodeSocketCon =
        new SSSocketCon(
          clientPar.sssNodeHost,
          clientPar.sssNodePort,
          clientJSONRequ);
      
      sssNodeSocketCon.writeRequFullToSS();
      
      return sssNodeSocketCon.readMsgFullFromSS ();
      
    }catch(Exception error){
      SSServErrReg.regErr     (error);
      SSServErrReg.regErrThrow(new SSErr(SSErrE.deployingServiceOnNodeFailed));
      return null;
    }finally{
      if(sssNodeSocketCon != null){
        sssNodeSocketCon.closeCon();
      }
    }
  }
  
  public static String getBearer(
    final HttpHeaders headers) throws Exception{
    
    String bearer = headers.getRequestHeader("authorization").get(0);
    return SSStrU.replaceAll(bearer, "Bearer ", SSStrU.empty);
  }
  
  public static Response handleGETRequest(
    final HttpHeaders      headers,
    final SSServPar        par){
    
    try{
      par.key = getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    final String   response;
    
    try{
      response = handleStandardJSONRESTCall(par, par.op);
    }catch(Exception error){
      return Response.status(500).entity(getJSONStrForError(SSErrE.sssResponseFailed, "couldn't retrieve response from sss")).build();
    }
    
    try{
      final ObjectMapper mapper       = new ObjectMapper();
      final JsonNode     jsonRootNode = mapper.readTree(response);
      
      if(jsonRootNode.get(SSVarU.error).getBooleanValue()){
        return Response.status(500).entity(getJSONStrForError(jsonRootNode.get(SSVarU.id).getTextValue(), jsonRootNode.get(SSVarU.message).getTextValue())).build();
      }else{
        return Response.status(200).entity(SSJSONU.jsonStr(jsonRootNode.get(par.op.toString()))).build();
      }
      
    }catch(Exception error){
      return Response.status(500).entity(getJSONStrForError(SSErrE.sssResponseParseFailed, "couldn't parse json from sss")).build();
    }
  }
  
  public static Response handlePOSTRequest(
    final HttpHeaders      headers,
    final SSServPar        par){
    
    try{
      par.key = getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    return sendPOSTRequest(par);
  }
  
  public static Response sendPOSTRequest(
    final SSServPar        par){
    
    final String   response;
    
    try{
      response = handleStandardJSONRESTCall(par, par.op);
    }catch(Exception error){
      return Response.status(500).entity(getJSONStrForError(SSErrE.sssResponseFailed, "couldn't retrieve response from sss")).build();
    }
    
    try{
      final ObjectMapper mapper       = new ObjectMapper();
      final JsonNode     jsonRootNode = mapper.readTree(response);
      
      if(jsonRootNode.get(SSVarU.error).getBooleanValue()){
        return Response.status(500).entity(getJSONStrForError(jsonRootNode.get(SSVarU.id).getTextValue(), jsonRootNode.get(SSVarU.message).getTextValue())).build();
      }else{
        return Response.status(200).entity(SSJSONU.jsonStr(jsonRootNode.get(par.op.toString()))).build();
      }
      
    }catch(Exception error){
      return Response.status(500).entity(getJSONStrForError(SSErrE.sssResponseParseFailed, "couldn't parse json from sss")).build();
    }
  }
  
  public static Response handleDELETERequest(
    final HttpHeaders      headers,
    final SSServPar        par){
    
    try{
      par.key = getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    final String   response;
    
    try{
      response = handleStandardJSONRESTCall(par, par.op);
    }catch(Exception error){
      return Response.status(500).entity(getJSONStrForError(SSErrE.sssResponseFailed, "couldn't retrieve response from sss")).build();
    }
    
    try{
      final ObjectMapper mapper       = new ObjectMapper();
      final JsonNode     jsonRootNode = mapper.readTree(response);
      
      if(jsonRootNode.get(SSVarU.error).getBooleanValue()){
        return Response.status(500).entity(getJSONStrForError(jsonRootNode.get(SSVarU.id).getTextValue(), jsonRootNode.get(SSVarU.message).getTextValue())).build();
      }else{
        return Response.status(201).entity(SSJSONU.jsonStr(jsonRootNode.get(par.op.toString()))).build();
      }
      
    }catch(Exception error){
      return Response.status(500).entity(getJSONStrForError(SSErrE.sssResponseParseFailed, "couldn't parse json from sss")).build();
    }
  }
  
  public static Response handlePUTRequest(
    final HttpHeaders      headers,
    final SSServPar        par){
    
    try{
      par.key = getBearer(headers);
    }catch(Exception error){
      return Response.status(401).build();
    }
    
    final String   response;
    
    try{
      response = handleStandardJSONRESTCall(par, par.op);
    }catch(Exception error){
      return Response.status(500).entity(getJSONStrForError(SSErrE.sssResponseFailed, "couldn't retrieve response from sss")).build();
    }
    
    try{
      final ObjectMapper mapper       = new ObjectMapper();
      final JsonNode     jsonRootNode = mapper.readTree(response);
      
      if(jsonRootNode.get(SSVarU.error).getBooleanValue()){
        return Response.status(500).entity(getJSONStrForError(jsonRootNode.get(SSVarU.id).getTextValue(), jsonRootNode.get(SSVarU.message).getTextValue())).build();
      }else{
        return Response.status(201).entity(SSJSONU.jsonStr(jsonRootNode.get(par.op.toString()))).build();
      }
      
    }catch(Exception error){
      return Response.status(500).entity(getJSONStrForError(SSErrE.sssResponseParseFailed, "couldn't parse json from sss")).build();
    }
  }
  
  private static String getJSONStrForError(
    final SSErrE id, 
    final String message){
    
    final Map<String, Object> jsonObj     = new HashMap<>();
    
    if(id == null){
      jsonObj.put(SSVarU.id,                      null);
    }else{
      jsonObj.put(SSVarU.id,                      id.toString());
    }
    
    if(message == null){
      
      if(id == null){
        jsonObj.put(SSVarU.message,               null);
      }else{
        jsonObj.put(SSVarU.message,               id.toString());
      }
    }else{
      jsonObj.put(SSVarU.message,               message);
    }
    
    try{
      return SSJSONU.jsonStr(jsonObj);
    } catch(Exception error){
      return null;
    }
  }
  
  private static String getJSONStrForError(
    final String id, 
    final String message) throws Exception{
    
   final Map<String, Object> jsonObj     = new HashMap<>();
    
    if(id == null){
      jsonObj.put(SSVarU.id,                      null);
    }else{
      jsonObj.put(SSVarU.id,                      id);
    }
    
    if(message == null){
      
      if(id == null){
        jsonObj.put(SSVarU.message,               null);
      }else{
        jsonObj.put(SSVarU.message,               id);
      }
    }else{
      jsonObj.put(SSVarU.message,               message);
    }
    
    try{
      return SSJSONU.jsonStr(jsonObj);
    } catch(Exception error){
      return null;
    }
  }
}