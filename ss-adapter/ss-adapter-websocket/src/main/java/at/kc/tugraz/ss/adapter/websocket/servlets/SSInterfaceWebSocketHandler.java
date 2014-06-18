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
package at.kc.tugraz.ss.adapter.websocket.servlets;

import at.kc.tugraz.socialserver.utils.*;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.conf.conf.SSConf;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.err.reg.SSErrForClient;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import sun.misc.BASE64Decoder;

public class SSInterfaceWebSocketHandler extends MessageInbound{

  private SSConf                                  sSConf              = null;
  private SSSocketCon                             sSCon               = null;
  private WsOutbound                              wsOutbound          = null;
  private SSMethU                                 op                  = null;
  private Map<String, String>                     objJSON             = null;
  private CharBuffer                              clientMsg           = null;
  
  public SSInterfaceWebSocketHandler() throws Exception{
    
    SSCoreConf.instSet    (SSFileU.dirCatalinaHome() + SSSystemU.dirNameConf + SSSystemU.fileNameSSAdapterWebSocketConf);
    
    sSConf =  SSCoreConf.instGet().getSsConf();
  }
  
	@Override
	protected void onTextMessage(CharBuffer clientMsg) {
		
		wsOutbound = getWsOutbound();
    
		try{
      this.clientMsg = clientMsg;
      this.objJSON   = SSJSONU.jsonMap(clientMsg.toString());
      op             = SSMethU.get(objJSON.get(SSVarU.op));
      
      if(
        SSMethU.equals(op, SSMethU.fileReplace)  ||
        SSMethU.equals(op, SSMethU.fileDownload) ||
        SSMethU.equals(op, SSMethU.fileUpload)){
        
        handleSSFileRepoRequest();
        return;
      }
      
      sSCon = new SSSocketCon(sSConf.host, sSConf.port, clientMsg.toString());

      sSCon.writeRequFullToSS   ();
      writeStringToClient       (sSCon.readMsgFullFromSS());
      
    }catch(Exception error1){
      
      try{
        writeErrorToClient(error1);
      }catch(Exception error2){
        SSServErrReg.regErr(error2, "writing error to client didnt work");
      }
    }
  }
  
  @Override
  protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
    System.out.println("binary message");
  }
  
  protected void writeStringToClient(String clientResult) throws Exception{
    
    CharBuffer buffer = CharBuffer.allocate(clientResult.length());
    
    buffer.put(SSStrU.toStr(clientResult));
    buffer.position(0);
    
    wsOutbound.writeTextMessage(buffer);
  }
  
  protected void writeBytesToClient(byte[] bytes) throws Exception{
    
    wsOutbound.writeBinaryMessage(ByteBuffer.wrap(bytes));
    wsOutbound.flush();
  }
  
  private void writeErrorToClient(Exception error) throws Exception{
    
    final List<SSErrForClient> errors = new ArrayList<>();
    String              errorMsg;  
    CharBuffer          charBuffer;  
      
    errors.add(SSErrForClient.get(error));
    
    errorMsg   = sSCon.prepErrorToClient (errors, op);
    charBuffer = CharBuffer.allocate     (errorMsg.length());
    
    charBuffer.put      (errorMsg);
    charBuffer.position (0);
    
    wsOutbound.writeTextMessage(charBuffer);
  }
  
  private void handleSSFileRepoRequest() throws Exception{
    
    if(SSObjU.isNull(sSCon)){
      sSCon = new SSSocketCon(sSConf.host, sSConf.port, clientMsg.toString());
      
      sSCon.writeRequFullToSS   ();
      writeStringToClient       (sSCon.readMsgFullFromSS());
      return;
    }
    
    if(
      SSMethU.equals(op, SSMethU.fileReplace) ||
      SSMethU.equals(op, SSMethU.fileUpload)){
      
      BASE64Decoder dataUriDecoder = new BASE64Decoder();
      String        dataUriChunk;
    
      dataUriChunk = objJSON.get(SSVarU.dataUriChunk);

      if(SSStrU.equals(dataUriChunk, SSStrU.valueFinished)){

        sSCon.writeFileChunkToSS (new byte[0]);
        writeStringToClient      (sSCon.readMsgFullFromSS());
      }else{
        sSCon.writeFileChunkToSS  (dataUriDecoder.decodeBuffer(dataUriChunk));
        writeStringToClient       (sSCon.readMsgFullFromSS());
      }
    }
    
    if(SSMethU.equals(op, SSMethU.fileDownload)){
      
      sSCon.sSJsonRequ = clientMsg.toString();
      sSCon.writeRequFullToSS();

      writeBytesToClient(sSCon.readFileChunkFromSS());
    }
  }
}