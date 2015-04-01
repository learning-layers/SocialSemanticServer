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
package at.tugraz.sss.serv;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSSocketCon{
  
  private  Socket                        sSSocket;
  private  DataInputStream               sSInFileChunk;
  private  DataOutputStream              sSOutFileChunk;
  private  BufferedWriter                sSOutRequFull;
  private  DataOutputStream              clientOutFileChunk;
  private  InputStreamReader             clientInMsgFull;
  private  DataInputStream               clientInFileChunk;
  private  OutputStreamWriter            testOut;
  private  InputStreamReader             testIn;
  
  public SSSocketCon(
    final String  host, 
    final Integer port) throws Exception{
    
    this.sSSocket             = new Socket           (InetAddress.getByName(host), port);
    this.sSInFileChunk        = new DataInputStream  (sSSocket.getInputStream());
    this.sSOutFileChunk       = new DataOutputStream (sSSocket.getOutputStream());
    this.sSOutRequFull        = new BufferedWriter   (new OutputStreamWriter(sSSocket.getOutputStream(), SSEncodingU.utf8));
    this.testIn               = new InputStreamReader (sSSocket.getInputStream(),  SSEncodingU.utf8);
  }

  public SSSocketCon(
    final Socket  clientSocket) throws Exception{
    
    this.clientInMsgFull     = new InputStreamReader  (clientSocket.getInputStream(), SSEncodingU.utf8);
    this.clientOutFileChunk  = new DataOutputStream   (clientSocket.getOutputStream ());
    this.clientInFileChunk   = new DataInputStream    (clientSocket.getInputStream());
    this.testOut             = new OutputStreamWriter (clientSocket.getOutputStream(), SSEncodingU.utf8);
  }
  
  public boolean writeFileChunkToClient(byte[] chunk, int chunkLength) throws Exception{
    
    if(SSObjU.isNull(chunk)){
      throw new Exception("chunk to send to client is null");
    }
    
    if(
      chunk.length == 0 ||
      chunkLength  <= 0){
      
      clientOutFileChunk.writeInt (-1);
      clientOutFileChunk.write    (new byte[0]);
      clientOutFileChunk.flush    ();
      return false;
    }
    
    clientOutFileChunk.writeInt (chunkLength);
    clientOutFileChunk.write    (chunk, 0, chunkLength);
    clientOutFileChunk.flush    ();
    return true;
  }
  
  public byte[] readFileChunkFromClient() throws IOException{
    
    int chunkLength = clientInFileChunk.readInt();
    
    if(chunkLength == -1){
      return new byte[0];
    }
    
    byte[] chunk = new byte[chunkLength];
    
    clientInFileChunk.readFully(chunk);
    
    return chunk;
  }
    
  public boolean writeFileChunkToSS(byte[] chunk) throws Exception{
    
    if(SSObjU.isNull(chunk)){
      throw new Exception("chunk to send to SSS is null");
    }
    
    if(chunk.length == 0){
      sSOutFileChunk.writeInt  (-1);
      sSOutFileChunk.write     (chunk);
      sSOutFileChunk.flush     ();
      return false;
    }
    
    sSOutFileChunk.writeInt  (chunk.length);
    sSOutFileChunk.write     (chunk);
    sSOutFileChunk.flush     ();
    return true;  
  }
  
  public boolean writeFileChunkToSS(byte[] chunk, int length) throws Exception{
    
    if(SSObjU.isNull(chunk)){
      throw new Exception("chunk to send to SSS is null");
    }
    
    if(
      chunk.length == 0 ||
      length      <= 0){
      
      sSOutFileChunk.writeInt  (-1);
      sSOutFileChunk.write     (new byte[0]);
      sSOutFileChunk.flush     (); 
      return false;
    }
    
    sSOutFileChunk.writeInt  (length);
    sSOutFileChunk.write     (chunk, 0, length);
    sSOutFileChunk.flush     ();
    
    return true;    
  }
  
  public byte[] readFileChunkFromSS() throws IOException{
    
    int chunkSize = sSInFileChunk.readInt();
    
    if(chunkSize == -1){
      return new byte[0];
    }
    
    byte[] chunk = new byte[chunkSize];
    
    sSInFileChunk.readFully(chunk);
    
    return chunk;
  }
  
  public void writeRequFullToSS(final String jsonRequest) throws IOException{
    
    sSOutRequFull.write(jsonRequest);
    sSOutRequFull.write(SSSocketU.endOfRequest);
    sSOutRequFull.flush();
  }
  
  public void writeMsgFullToClient(final String stringJSON) throws Exception{
    
    char[] chars    = new char[SSSocketU.socketTranmissionSize];
    int    index    = 0;
    int    length   = stringJSON.length();
    
    while(index + SSSocketU.socketTranmissionSize < length){
      
      stringJSON.getChars(index, index + SSSocketU.socketTranmissionSize, chars, 0);
      
      testOut.write(chars);
      testOut.flush();
      
      index += SSSocketU.socketTranmissionSize;
    }
    
    if(index < length){
      
      chars = new char[length - index];
      
      stringJSON.getChars(index, length, chars, 0);
      
      testOut.write(chars);
      testOut.flush();
    }else{
      testOut.write(SSSocketU.endOfRequest);
      testOut.flush();
    }
	}
    
  public String readMsgFullFromSS() throws Exception{
    
    char[]     buffer = new char [SSSocketU.socketTranmissionSize]; 
    String     string = new String();
    int        read;
    
    while((read = testIn.read(buffer)) != -1){
     
      string += String.valueOf(buffer, 0, read);
      
      if(buffer[read - 1] == SSSocketU.endOfRequest){
        return string.substring(0, string.length() - 1);
      }
    }
    
    return string;
  }
  
  public void closeCon(){
    
    if(
      sSSocket == null ||
      sSSocket.isClosed()){
      return;
    }
    
    try{
      sSSocket.close();
    }catch(Exception error){
      SSLogU.err("server data input stream close failed");
    }
  }
  
  public String readMsgFullFromClient() throws Exception{
    
    char[]      buffer   = new char[1];
    String      string   = new String();
    
    clientInMsgFull.read(buffer);
    
    while(buffer[0] != SSSocketU.endOfRequest){
      
      string += buffer[0];
      
       clientInMsgFull.read(buffer);
    }
    
    return string;
  }
  
  public void writeRetFullToClient(
    final SSServRetI  result) throws Exception{
  
    final Map<String, Object> ret       = new HashMap<>();
    
    ret.put(SSVarU.op,               SSStrU.toStr(result.op));
    ret.put(SSVarU.error,            false);
    ret.put(SSStrU.toStr(result.op), result);
    
//    if(sendJSONLD){
//      ret.put(SSJSONLDU.context, SSJSONLDU.jsonLDContext(result.jsonLDDesc()));
//    }
    
    writeMsgFullToClient(SSJSONU.jsonStr(ret) + SSSocketU.endOfRequest);
  }
  
  public void writeErrorFullToClient(
    final List<SSErrForClient> errors, 
    final SSMethU              op) throws Exception{
    
    writeMsgFullToClient(prepErrorToClient(errors, op));
  }
  
  public String prepErrorToClient(
    final List<SSErrForClient> errors, 
    final SSMethU              op) throws Exception{
    
    final Map<String, Object> ret           = new HashMap<>();
    final List<String>        errorMessages = SSErrForClient.messages           (errors);
    
    ret.put(SSVarU.op,                      SSStrU.toStr(op));
    ret.put(SSVarU.error,                   true);
    ret.put(SSVarU.errorMsg,                errorMessages);
    ret.put(SSVarU.errorClassNames,         SSErrForClient.classNames         (errors));
    ret.put(SSVarU.errorClassesWhereThrown, SSErrForClient.classesWhereThrown (errors));
    ret.put(SSVarU.errorMethodsWhereThrown, SSErrForClient.methodsWhereThrown (errors));
    ret.put(SSVarU.errorLinesWhereThrown,   SSErrForClient.linesWhereThrown   (errors));
    ret.put(SSVarU.errorThreadsWhereThrown, SSErrForClient.threadsWhereThrown (errors));
    
//    ret.put(SSJSONLDU.context, SSJSONLDU.jsonLDContext());
    ret.put(SSStrU.toStr(op), null);

    ret.put(SSVarU.id, null);
    
    if(!errors.isEmpty()){
      
      ret.put(SSVarU.message, errorMessages.get(0));
      
      for(SSErrForClient error : errors){
        
        if(error.id != null){
          ret.put(SSVarU.id, error.id);
          break;
        }
      }
    }
    
    
    return SSJSONU.jsonStr(ret) + SSSocketU.endOfRequest;
  }
}

//  private void closeClientInputStream(){
//    
//    if(clientInMsgFull == null){
//      return;
//    }
//    
//    try{
//      clientInMsgFull.close();
//    }catch(Exception error){
//      SSLogU.logError(error, "client input stream close failed");
//    }
//  }
//  
//  private void closeClientOutputStream(){
//    
//    if(clientOutMsgFull == null){
//      return;
//    }
//    
//    try{
//      clientOutMsgFull.close();
//    }catch(Exception error){
//      SSLogU.logError(error, "client output stream close failed");
//    }
//  }
//  
//  private void closeServerDataOutputStream(){
//    
//    if(sSOutFileChunk == null){
//      return;
//    }
//    
//    try{
//      sSOutFileChunk.close();
//    }catch(Exception error){
//      SSLogU.logError(error, "server data output stream close failed");
//    }
//  }
//  
//  private void closeServerInputStream(){
//    
//    if(sSInMsgFull == null){
//      return;
//    }
//    
//    try{
//      sSInMsgFull.close();
//    }catch(Exception error){
//      SSLogU.logError(error, "server input stream close failed");
//    }
//  }
//  
//  private void closeServerOutputStream(){
//    
//    if(sSOutRequFull == null){
//      return;
//    }
//    
//    try{
//      sSOutRequFull.close();
//    }catch(Exception error){
//      SSLogU.logError(error, "server output stream close failed");
//    }
//  }
//  
//  private void closeClientDataOutputStream(){
//    
//    if(clientOutFileChunk == null){
//      return;
//    }
//    
//    try{
//      clientOutFileChunk.close();
//    }catch(Exception error){
//      SSLogU.logError(error, "client data output stream close failed");
//    }
//  }
//  
//  private void closeClientDataInputStream(){
//    
//    if(clientInFileChunk == null){
//      return;
//    }
//    
//    try{
//      clientInFileChunk.close();
//    }catch(Exception error){
//      SSLogU.logError(error, "client data input stream close failed");
//    }
//  }
//  
//  private void closeServerDataInputStream(){
//    
//    if(sSInFileChunk == null){
//      return;
//    }
//    
//    try{
//      sSInFileChunk.close();
//    }catch(Exception error){
//      SSLogU.logError(error, "server data input stream close failed");
//    }
//  }