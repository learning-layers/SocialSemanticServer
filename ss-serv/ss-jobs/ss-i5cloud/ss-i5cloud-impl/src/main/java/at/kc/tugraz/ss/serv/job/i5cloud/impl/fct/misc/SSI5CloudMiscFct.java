/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.job.i5cloud.impl.fct.misc;

import at.kc.tugraz.socialserver.utils.SSEncodingU;
import at.kc.tugraz.socialserver.utils.SSJSONU;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.Map;

public class SSI5CloudMiscFct{
  
  public static Map<String,String> readJSONResponse(final URLConnection con) throws Exception{
    
    InputStreamReader   inputReader = null;
    
    try{
      final char[]              buffer       = new char[1];
      String                    jsonResponse = new String(); 
      
      inputReader = new InputStreamReader (con.getInputStream(), SSEncodingU.utf8);

      inputReader.read(buffer);

      while(true){

        jsonResponse += buffer[0];

        if(inputReader.read(buffer) == -1){
          break;
        }
      }

      return SSJSONU.jsonMap(jsonResponse);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      if(inputReader!= null){
        inputReader.close();
      }
    }
//    String           jKey;
//    String           jValue;
//    String           xAuthToken = null;
//    String           expires    = null;
//    String           swiftUrl   = null;
//    
//    jp.nextToken();
//    
//    while(jp.nextToken() != SSJSONU.jsonEnd) {
//      
//      jKey = jp.getCurrentName();
//      
//      jp.nextToken();
//      
//      jValue = jp.getText();
//      
//      if(SSStrU.equals(jKey, "X-Auth-Token")){
//        xAuthToken = jValue;
//        continue;
//      }
//      
//      if(SSStrU.equals(jKey, "expires")){
//        expires = jValue;
//        continue;
//      }
//      
//      if(SSStrU.equals(jKey, "swift-url")){
//        swiftUrl = jValue;
//        continue;
//      }
//    }
//    
//    System.out.println(xAuthToken);
//    System.out.println(expires);
//    System.out.println(swiftUrl);
  }
}
