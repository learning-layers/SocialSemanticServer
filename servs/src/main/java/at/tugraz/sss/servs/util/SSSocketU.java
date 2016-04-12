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
package at.tugraz.sss.servs.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class SSSocketU {

  public static final Integer socketTranmissionSize = 10000;
  public static final char    endOfRequest          = '\0';
  
  public static String readFullString(
    final InputStreamReader streamReader) throws IOException {
    
    char[]      buffer   = new char[1];
    String      string   = new String();
    
    streamReader.read(buffer);
    
    while(buffer[0] != endOfRequest){
      
      string += buffer[0];
      
      streamReader.read(buffer);
    }
    
    return string;
  }
  
  public static void writeFullString(
    final OutputStreamWriter streamWriter,
    final String             string) throws IOException {
    
    char[] chars    = new char[socketTranmissionSize];
    int    index    = 0;
    int    length   = string.length();
    
    while(index + socketTranmissionSize < length){
      
      string.getChars(index, index + socketTranmissionSize, chars, 0);
      
      streamWriter.write(chars);
      streamWriter.flush();
      
      index += socketTranmissionSize;
    }
    
    if(index < length){
      
      chars = new char[length - index];
      
      string.getChars(index, length, chars, 0);
      
      streamWriter.write(chars);
      streamWriter.flush();
    }else{
      streamWriter.write(endOfRequest);
      streamWriter.flush();
    }
	}
  
  public static byte[] readByteChunk(
    final DataInputStream    dataInputStream) throws IOException{
    
    int chunkLength = dataInputStream.readInt();
    
    if(chunkLength == -1){
      return new byte[0];
    }
    
    byte[] chunk = new byte[chunkLength];
    
    dataInputStream.readFully(chunk);
    
    return chunk;
  }
  
  public static boolean writeByteChunk(
    final DataOutputStream dataOutputStream,
    final byte[]           chunk, 
    final int              chunkLength) throws IOException{
    
    if(
      chunk.length == 0 ||
      chunkLength  <= 0){
      
      dataOutputStream.writeInt (-1);
      dataOutputStream.write    (new byte[0]);
      dataOutputStream.flush    ();
      return false;
    }
    
    dataOutputStream.writeInt (chunkLength);
    dataOutputStream.write    (chunk, 0, chunkLength);
    dataOutputStream.flush    ();
    return true;
  }
}

//  public void writeRetFullToClient(
//    final Object    result, 
//    final SSServOpE op) throws SSErr{
//  
////    if(sendJSONLD){
////      ret.put(SSJSONLDU.context, SSJSONLDU.jsonLDContext(result.jsonLDDesc()));
////    }
//    
//    writeMsgFullToClient(prepRetFullToClient(result, op));
//  }

//    this.sSSocket             = new Socket           (InetAddress.getByName(host), port);
//    this.sSInFileChunk        = new DataInputStream  (sSSocket.getInputStream());
//    this.sSOutFileChunk       = new DataOutputStream (sSSocket.getOutputStream());
//    this.sSOutRequFull        = new BufferedWriter   (new OutputStreamWriter(sSSocket.getOutputStream(), SSEncodingU.utf8.toString()));
//    this.testIn               = new InputStreamReader (sSSocket.getInputStream(),  SSEncodingU.utf8.toString());
//    this.clientInMsgFull     = new InputStreamReader  (clientSocket.getInputStream(), SSEncodingU.utf8.toString());
//    this.clientOutFileChunk  = new DataOutputStream   (clientSocket.getOutputStream ());
//    this.clientInFileChunk   = new DataInputStream    (clientSocket.getInputStream());

//public void writeRequFullToSS(final String jsonRequest) throws IOException{
//    
//    sSOutRequFull.write(jsonRequest);
//    sSOutRequFull.write(SSSocketU.endOfRequest);
//    sSOutRequFull.flush();
//  }
//
//  public String readMsgFullFromSS() throws SSErr{
//    
//    char[]     buffer = new char [SSSocketU.socketTranmissionSize]; 
//    String     string = new String();
//    int        read;
//    
//    while((read = testIn.read(buffer)) != -1){
//     
//      string += String.valueOf(buffer, 0, read);
//      
//      if(buffer[read - 1] == SSSocketU.endOfRequest){
//        return string.substring(0, string.length() - 1);
//      }
//    }
//    
//    return string;
//  }

//public boolean writeFileChunkToSS(byte[] chunk) throws SSErr{
//    
//    if(SSObjU.isNull(chunk)){
//      throw new Exception("chunk to send to SSS is null");
//    }
//    
//    if(chunk.length == 0){
//      sSOutFileChunk.writeInt  (-1);
//      sSOutFileChunk.write     (chunk);
//      sSOutFileChunk.flush     ();
//      return false;
//    }
//    
//    sSOutFileChunk.writeInt  (chunk.length);
//    sSOutFileChunk.write     (chunk);
//    sSOutFileChunk.flush     ();
//    return true;  
//  }
//public boolean writeFileChunkToSS(byte[] chunk, int length) throws SSErr{
//    
//    if(SSObjU.isNull(chunk)){
//      throw new Exception("chunk to send to SSS is null");
//    }
//    
//    if(
//      chunk.length == 0 ||
//      length      <= 0){
//      
//      sSOutFileChunk.writeInt  (-1);
//      sSOutFileChunk.write     (new byte[0]);
//      sSOutFileChunk.flush     (); 
//      return false;
//    }
//    
//    sSOutFileChunk.writeInt  (length);
//    sSOutFileChunk.write     (chunk, 0, length);
//    sSOutFileChunk.flush     ();
//    
//    return true;    
//  }
//  
//  public byte[] readFileChunkFromSS() throws IOException{
//    
//    int chunkSize = sSInFileChunk.readInt();
//    
//    if(chunkSize == -1){
//      return new byte[0];
//    }
//    
//    byte[] chunk = new byte[chunkSize];
//    
//    sSInFileChunk.readFully(chunk);
//    
//    return chunk;
//  }

//public void closeCon(){
//    
//    if(
//      sSSocket == null ||
//      sSSocket.isClosed()){
//      return;
//    }
//    
//    try{
//      sSSocket.close();
//    }catch(Exception error){
//      SSLogU.err("server data input stream close failed");
//    }
//  }