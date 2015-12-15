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
package at.tugraz.sss.adapter.socket;

import at.tugraz.sss.serv.SSEncodingU;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSJSONU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSServRetI;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.adapter.socket.SSSocketU;
import at.tugraz.sss.serv.SSVarNames;
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
  private  DataInputStream               clientInFileChunk;
  private  InputStreamReader             testIn;
  
  public SSSocketCon(
    final String  host, 
    final Integer port) throws Exception{
    
    this.sSSocket             = new Socket           (InetAddress.getByName(host), port);
    this.sSInFileChunk        = new DataInputStream  (sSSocket.getInputStream());
    this.sSOutFileChunk       = new DataOutputStream (sSSocket.getOutputStream());
    this.sSOutRequFull        = new BufferedWriter   (new OutputStreamWriter(sSSocket.getOutputStream(), SSEncodingU.utf8.toString()));
    this.testIn               = new InputStreamReader (sSSocket.getInputStream(),  SSEncodingU.utf8.toString());
  }

  public SSSocketCon(
    final Socket  clientSocket) throws Exception{
    
//    this.clientInMsgFull     = new InputStreamReader  (clientSocket.getInputStream(), SSEncodingU.utf8.toString());
    this.clientOutFileChunk  = new DataOutputStream   (clientSocket.getOutputStream ());
    this.clientInFileChunk   = new DataInputStream    (clientSocket.getInputStream());
    
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
  
//  public void writeRetFullToClient(
//    final Object    result, 
//    final SSServOpE op) throws Exception{
//  
////    if(sendJSONLD){
////      ret.put(SSJSONLDU.context, SSJSONLDU.jsonLDContext(result.jsonLDDesc()));
////    }
//    
//    writeMsgFullToClient(prepRetFullToClient(result, op));
//  }
  
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