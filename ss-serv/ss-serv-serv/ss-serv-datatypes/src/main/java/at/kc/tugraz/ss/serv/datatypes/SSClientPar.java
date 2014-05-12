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
package at.kc.tugraz.ss.serv.datatypes;

import at.kc.tugraz.socialserver.utils.SSJSONU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import org.codehaus.jackson.JsonParser;

public class SSClientPar{
  
  private SSMethU              op                       = null;
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
      
      if(!SSStrU.contains(jsonResp, SSVarU.useDifferentServiceNode)){
        return;
      }
      
      jp = SSJSONU.jsonParser(jsonResp);
      
      jp.nextToken();
      
      while(jp.nextToken() != SSJSONU.jsonEnd) {
        
        jKey = jp.getCurrentName();
        
        jp.nextToken();
        
        jValue = jp.getText();
        
        if(SSStrU.equals(jKey, SSVarU.op)){
          op = SSMethU.get(jValue);
          continue;
        }
        
        if(SSStrU.equals(jKey, SSVarU.user)){
          user = SSUri.get(jValue);
          continue;
        }
        
        if(SSStrU.equals(jKey, SSVarU.useDifferentServiceNode)){
          useDifferentServiceNode = Boolean.valueOf(jValue);
          continue;
        }
        
        if(SSStrU.equals(jKey, SSVarU.sssNodeHost)){
          sssNodeHost = jValue;
          continue;
        }
        
        if(SSStrU.equals(jKey, SSVarU.sssNodePort)){
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
      SSServErrReg.regErrThrow(error);
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