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
package at.kc.tugraz.ss.adapter.rest.v2;

import at.kc.tugraz.socialserver.utils.SSFileExtE;
import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSJSONU;
import at.kc.tugraz.socialserver.utils.SSMimeTypeE;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.adapter.rest.conf.SSAdapterRestConf;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
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
import sss.serv.err.datatypes.SSErrE;

public class SSRestMainV2 extends Application {
  
  public static SSAdapterRestConf conf;

  public SSRestMainV2() throws Exception{
   
    SSAdapterRestConf.instSet (SSFileU.dirCatalinaBase() + SSVocConf.dirNameConf + "sss.adapter.rest.v2.conf.ld.yaml");
    
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
  
  public static SSRESTObject handleRequest(
    final HttpHeaders      headers,
    final SSServPar        par, 
    final Boolean          keepSSSConnectionOpen,
    final Boolean          getKeyFromHeaders){
    
    final SSRESTObject restObj                  = new SSRESTObject(par);
    final ObjectMapper sssJSONResponseMapper    = new ObjectMapper();
    final JsonNode     sssJSONResponseRootNode;
    
    if(getKeyFromHeaders){
    
      try{
        par.key = getBearer(headers);
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
        
        restObj.sssCon =
          new SSSocketCon(
            conf.ss.host,
            conf.ss.port,
            restObj.sssRequestMessage);
        
      }catch(Exception error){
        
        restObj.response =
          Response.status(500).entity(
            getJSONStrForError(
              SSErrE.sssConnectionFailed)).build();
        
        return restObj;
      }
      
      try{
        restObj.sssCon.writeRequFullToSS ();
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

        if(sssJSONResponseRootNode.get(SSVarU.error).getBooleanValue()){

          restObj.response =
            Response.status(500).entity(
              getJSONStrForError(
                sssJSONResponseRootNode.get(SSVarU.id).getTextValue(),
                sssJSONResponseRootNode.get(SSVarU.message).getTextValue())).build();

        }else{
          restObj.response =
            Response.status(200).entity(
              SSJSONU.jsonStr(sssJSONResponseRootNode.get(restObj.par.op.toString()))).build();
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
      
      if(
        !keepSSSConnectionOpen &&
        restObj.sssCon != null){
        
        restObj.sssCon.closeCon();
      }
    }
  }
  
  public static String getJSONStrForError(
    final SSErrE id){
    
    final Map<String, Object> jsonObj     = new HashMap<>();
    
    if(id == null){
      jsonObj.put(SSVarU.id,                      null);
    }else{
      jsonObj.put(SSVarU.id,                      id.toString());
    }
    
    if(id == null){
      jsonObj.put(SSVarU.message,               null);
    }else{
      jsonObj.put(SSVarU.message,               id.toString());
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