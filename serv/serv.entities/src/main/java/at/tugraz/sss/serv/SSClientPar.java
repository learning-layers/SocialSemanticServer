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

import at.tugraz.sss.serv.SSJSONU;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import org.codehaus.jackson.JsonParser;

public class SSClientPar{
  
  private SSServOpE            op                       = null;
  private SSUri                user                     = null;
  private String               key                      = null;
  public  String               sssNodeHost              = null;
  public  Integer              sssNodePort              = null;
  public  Boolean              useDifferentServiceNode  = false;
    
  public SSClientPar(final String jsonResp) throws Exception{
    
    JsonParser jp = null;
    String     jKey;
    String     jValue;
    
    try{
      
      if(!SSStrU.contains(jsonResp, SSVarNames.useDifferentServiceNode)){
        return;
      }
      
      jp = SSJSONU.jsonParser(jsonResp);
      
      jp.nextToken();
      
      while(jp.nextToken() != SSJSONU.jsonEnd) {
        
        jKey = jp.getCurrentName();
        
        jp.nextToken();
        
        jValue = jp.getText();
        
        if(SSStrU.equals(jKey, SSVarNames.op)){
          op = SSServOpE.get(jValue);
          continue;
        }
        
        if(SSStrU.equals(jKey, SSVarNames.user)){
          user = SSUri.get(jValue);
          continue;
        }
        
        if(SSStrU.equals(jKey, SSVarNames.useDifferentServiceNode)){
          useDifferentServiceNode = Boolean.valueOf(jValue);
          continue;
        }
        
        if(SSStrU.equals(jKey, SSVarNames.sssNodeHost)){
          sssNodeHost = jValue;
          continue;
        }
        
        if(SSStrU.equals(jKey, SSVarNames.sssNodePort)){
          sssNodePort = Integer.valueOf(jValue);
          continue;
        }
      }
      
      if(SSObjU.isNull(this.op)){
        throw new Exception("op is empty");
      }
      
      if(
        useDifferentServiceNode &&
        SSObjU.isNull(sssNodeHost, sssNodePort)){
        throw new Exception("pars for service node change missing");
      }
    }catch(Exception error){
      throw error;
    }finally{
     
      if(jp != null){
        jp.close();
      }
    }
  }
}


//    ret.put(SSVarU.error,                   true);
//    ret.put(SSVarU.errorMsg,                SSErrForClient.messages           (errors));
//    ret.put(SSVarU.errorClassNames,         SSErrForClient.classNames         (errors));
//    ret.put(SSVarU.errorClassesWhereThrown, SSErrForClient.classesWhereThrown (errors));
//    ret.put(SSVarU.errorMethodsWhereThrown, SSErrForClient.methodsWhereThrown (errors));
//    ret.put(SSVarU.errorLinesWhereThrown,   SSErrForClient.linesWhereThrown   (errors));
//    ret.put(SSVarU.errorThreadsWhereThrown, SSErrForClient.threadsWhereThrown (errors));