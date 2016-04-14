/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.adapter.rest.v3;

import at.tugraz.sss.servs.entity.datatype.*;
import javax.ws.rs.*;
import org.glassfish.jersey.jackson.*;
import org.glassfish.jersey.media.multipart.*;
import org.glassfish.jersey.server.*;

@ApplicationPath("/rest")
public class SSRestMain extends ResourceConfig{

  public SSRestMain() throws SSErr{
    
    super(
      MultiPartFeature.class, 
      JacksonFeature.class, //register Jackson as JAXB mediator
      SSRESTJSONMapper.class, //register the object mapper jackson shall use
      SSRESTAuth.class,
      SSRESTActivity.class,
      SSRESTApp.class,
      SSRESTAppStackLayout.class,
      SSRESTCategory.class,
      SSRESTCircle.class,
      SSRESTColl.class,
      SSRESTDisc.class,
      SSRESTEntity.class,
      SSRESTEval.class,
      SSRESTFile.class,
      SSRESTFlag.class,
      SSRESTFriend.class,
      SSRESTImage.class,
      SSRESTLearnEp.class,
      SSRESTLike.class,
      SSRESTLivingDoc.class,
      SSRESTMessage.class,
      SSRESTRating.class,
      SSRESTRecomm.class,
      SSRESTSearch.class,
      SSRESTTag.class,
      SSRESTUser.class,
      SSRESTVideo.class, 
      SSRESTLink.class);
    
//    SwaggerConfig swaggerConfig = new SwaggerConfig( );
//    ConfigFactory.setConfig( swaggerConfig );
//    swaggerConfig.setApiVersion( "0.0.1" );
//    swaggerConfig.setBasePath( "http://localhost:8080/MyService/api" );
//    ScannerFactory.setScanner( new DefaultJaxrsScanner( ) );
//    ClassReaders.setReader( new DefaultJaxrsApiReader( ) );
  }
}

//public static SSRESTObject handleRequest(
//    final HttpHeaders      headers,
//    final SSServPar        par, 
//    final boolean          keepSSSConnectionOpen,
//    final boolean          getKeyFromHeaders){
//    
//    return handleRequest(
//      headers, 
//      new SSRESTObject(par), 
//      keepSSSConnectionOpen, 
//      getKeyFromHeaders);
//  }
//    
//  public static SSRESTObject handleRequest(
//    final HttpHeaders      headers,
//    SSRESTObject           restObj,
//    final boolean          keepSSSConnectionOpen,
//    final boolean          getKeyFromHeaders){
//    
//    final ObjectMapper sssJSONResponseMapper    = new ObjectMapper();
//    final JsonNode     sssJSONResponseRootNode;
//    
//    if(getKeyFromHeaders){
//      
//      try{
//        restObj.par.key = getBearer(headers);
//      }catch(Exception error){
//        
//        restObj.response = Response.status(401).build();
//        
//        return restObj;
//      }
//    }
//    
//    try{
//      restObj.sssRequestMessage = SSJSONU.jsonStr(restObj.par);
//    }catch(Exception error){
//      
//      restObj.response =
//        Response.status(500).entity(
//          getJSONStrForError(
//            SSErrE.sssJsonRequestEncodingFailed)).build();
//      
//      return restObj;
//    }
//    
//    try{
//      
//      try{
//        restObj.sssCon = new SSSocketCon(conf.sss.host, conf.sss.port);
//      }catch(Exception error){
//        
//        restObj.response =
//          Response.status(500).entity(
//            getJSONStrForError(
//              SSErrE.sssConnectionFailed)).build();
//        
//        return restObj;
//      }
//      
//      try{
//        restObj.sssCon.writeRequFullToSS (restObj.sssRequestMessage);
//      }catch(Exception error){
//        
//        restObj.response =
//          Response.status(500).entity(
//            getJSONStrForError(
//              SSErrE.sssWriteFailed)).build();
//        
//        return restObj;
//      }
//      
//      try{
//        restObj.sssResponseMessage = restObj.sssCon.readMsgFullFromSS();
//      }catch(Exception error){
//        
//        restObj.response =
//          Response.status(500).entity(
//            getJSONStrForError(
//              SSErrE.sssReadFailed)).build();
//        
//        return restObj;
//      }
//      
////      restObj.sssResponseMessage =
////        checkAndHandleSSSNodeSwitch(
////          restObj.sssResponseMessage,
////          restObj.sssRequestMessage);
//      
//      try{
//        sssJSONResponseRootNode = sssJSONResponseMapper.readTree(restObj.sssResponseMessage);
//        
//        if(sssJSONResponseRootNode.get(SSVarNames.error).getBooleanValue()){
//          
//          restObj.response =
//            Response.status(500).entity(
//              getJSONStrForError(
//                sssJSONResponseRootNode.get(SSVarNames.id).getTextValue(),
//                sssJSONResponseRootNode.get(SSVarNames.message).getTextValue())).build();
//          
//        }else{
//          
//          restObj.response =
//            Response.status(200).entity(
//              SSJSONU.jsonStr(
//                sssJSONResponseRootNode.get(restObj.par.op.toString()))).build();
//        }
//        
//      }catch(Exception error){
//        
//        restObj.response =
//          Response.status(500).entity(
//            getJSONStrForError(
//              SSErrE.sssResponseParsingFailed)).build();
//      }
//      
//      return restObj;
//      
//    }catch(Exception error){
//      
//      restObj.response =
//        Response.status(500).entity(
//          getJSONStrForError(
//            SSErrE.restAdapterInternalError)).build();
//      
//      return restObj;
//      
//    }finally{
//      
//      try{
//        if(
//          !keepSSSConnectionOpen &&
//          restObj.sssCon != null){
//          
//          restObj.sssCon.closeCon();
//        }
//      }catch(Exception error){
//        SSLogU.warn("socket connection not closed correctly");
//      }
//    }
//  }

//  public static SSRESTObject handleFileDownloadRequest(
//    final HttpHeaders headers,
//    SSRESTObject      restObj,
//    final String      fileName,
//    final boolean     getKeyFromHeaders){
//    
//    final StreamingOutput stream;
//    final SSSocketCon     sssCon;
//    
//    try{
//      restObj =
//        handleRequest(
//          headers,
//          restObj,
//          true,  //keepSSSConnectionOpen
//          getKeyFromHeaders); //getKeyFromHeaders
//      
//      if(restObj.response.getStatus() != 200){
//        return restObj;
//      }
//      
//      try{
//        restObj.sssCon.writeRequFullToSS (restObj.sssRequestMessage);
//      }catch(Exception error){
//        
//        restObj.response =
//          Response.status(500).entity(
//            getJSONStrForError(
//              SSErrE.sssWriteFailed)).build();
//        
//        return restObj;
//      }
//     
//      sssCon = restObj.sssCon;
//
//      try{
//        stream = new StreamingOutput(){
//
//          @Override
//          public void write(OutputStream out) throws IOException{
//
//            byte[] bytes;
//            
//            while((bytes = sssCon.readFileChunkFromSS()).length > 0) {
//
//              out.write               (bytes);
//              out.flush               ();
//            }
//
//            out.close();
//            
//            try{
//              sssCon.closeCon();
//            }catch(Exception error){
//              SSLogU.warn("socket connection not closed");
//            }
//          }
//        };
//      }catch(Exception error){
//        
//        restObj.response = 
//          Response.status(500).entity(
//            SSRestMainV2.getJSONStrForError(
//              SSErrE.sssReadFailed)).build();
//        
//        return restObj;
//      }
//      
//      restObj.response = 
//        Response.ok(stream).
//          header("Content-Disposition", "inline; filename=\"" + fileName + "\"").
//          header("Content-Type", SSMimeTypeE.mimeTypeForFileExt(SSFileExtE.ext(fileName)).toString()).
//          build();
//      
//      return restObj;
//      
//    }catch(Exception error){
//      
//      try{
//        
//        if(restObj.sssCon != null){
//          restObj.sssCon.closeCon();
//        }
//      }catch(Exception error1){
//        SSLogU.warn("socket connection not closed correctly");
//      }
//      
//      restObj.response =
//        Response.status(500).entity(
//          SSRestMainV2.getJSONStrForError(
//            SSErrE.restAdapterInternalError)).build();
//      
//      return restObj;
//    }
//  }
  
//  public static SSRESTObject handleFileUploadRequest(
//    final HttpHeaders headers,
//    SSRESTObject      restObj,
//    final InputStream file){
//    
//    final ObjectMapper          sssJSONResponseMapper    = new ObjectMapper();
//    final JsonNode              sssJSONResponseRootNode;
//    byte[]                      bytes   = new byte[SSSocketU.socketTranmissionSize];
//    int                         read;
//    
//    try{
//      restObj =
//        handleRequest(
//          headers,
//          restObj,
//          true,  //keepSSSConnectionOpen
//          true); //getKeyFromHeaders
//      
//      if(restObj.response.getStatus() != 200){
//        return restObj;
//      }
//      
//      try{
//        
//        while((read = file.read(bytes)) != -1) {
//          restObj.sssCon.writeFileChunkToSS   (bytes, read);
//        }
//        
//        restObj.sssCon.writeFileChunkToSS(new byte[0], -1);
//      }catch(Exception error){
//        
//        restObj.response =
//          Response.status(500).entity(
//            SSRestMainV2.getJSONStrForError(
//              SSErrE.sssWriteFailed)).build();
//        
//        return restObj;
//      }
//      
//      try{
//        restObj.sssResponseMessage = restObj.sssCon.readMsgFullFromSS();
//      }catch(Exception error){
//        
//        restObj.response =
//          Response.status(500).entity(
//            getJSONStrForError(
//              SSErrE.sssReadFailed)).build();
//        
//        return restObj;
//      }
//      
////      restObj.sssResponseMessage =
////        checkAndHandleSSSNodeSwitch(
////          restObj.sssResponseMessage,
////          restObj.sssRequestMessage);
//
//      try{
//        sssJSONResponseRootNode = sssJSONResponseMapper.readTree(restObj.sssResponseMessage);
//
//        if(sssJSONResponseRootNode.get(SSVarNames.error).getBooleanValue()){
//
//          restObj.response =
//            Response.status(500).entity(
//              getJSONStrForError(
//                sssJSONResponseRootNode.get(SSVarNames.id).getTextValue(),
//                sssJSONResponseRootNode.get(SSVarNames.message).getTextValue())).build();
//
//        }else{
//          restObj.response =
//            Response.status(200).entity(
//              SSJSONU.jsonStr(
//                sssJSONResponseRootNode.get(restObj.par.op.toString()))).build();
//        }
//        
//      }catch(Exception error){
//        
//        restObj.response =
//          Response.status(500).entity(
//            getJSONStrForError(
//              SSErrE.sssResponseParsingFailed)).build();
//      }
//      
//      return restObj;
//      
//    }catch(Exception error){
//      
//      restObj.response =
//        Response.status(500).entity(
//          SSRestMainV2.getJSONStrForError(
//            SSErrE.restAdapterInternalError)).build();
//      
//      return restObj;
//      
//    }finally{
//      
//      try{
//        
//        if(restObj.sssCon != null){
//          restObj.sssCon.closeCon();
//        }
//        
//      }catch(Exception error1){
//        SSLogU.warn("socket connection not closed correctly");
//      }
//    }
//  }
//  @Override
//  public Set<Class<?>> getClasses() {
//
//    final Set<Class<?>> classes = new HashSet<>();
//    
//    classes.add(MultiPartFeature.class);
//
//    classes.add(SSRESTActivity.class);
//    classes.add(SSRESTApp.class);
//    classes.add(SSRESTAppStackLayout.class);
//    classes.add(SSRESTAuth.class);
//    classes.add(SSRESTCategory.class);
//    classes.add(SSRESTCircle.class);
//    classes.add(SSRESTColl.class);
//    classes.add(SSRESTDisc.class);
//    classes.add(SSRESTEntity.class);
//    classes.add(SSRESTEval.class);
//    classes.add(SSRESTFile.class);
//    classes.add(SSRESTFlag.class);
//    classes.add(SSRESTFriend.class);
//    classes.add(SSRESTImage.class);
//    classes.add(SSRESTLearnEp.class);
//    classes.add(SSRESTLike.class);
//    classes.add(SSRESTLivingDoc.class);
//    classes.add(SSRESTMessage.class);
//    classes.add(SSRESTRating.class);
//    classes.add(SSRESTRecomm.class);
//    classes.add(SSRESTSearch.class);
//    classes.add(SSRESTTag.class);
//    classes.add(SSRESTUE.class);
//    classes.add(SSRESTUser.class);
//    classes.add(SSRESTVideo.class);
//    
//    return classes;
//  }