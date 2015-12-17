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
package at.tugraz.sss.adapter.socket;

import at.tugraz.sss.serv.SSSocketU;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSJSONU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServRetI;
import at.tugraz.sss.serv.SSVarNames;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSSocketAdapterU {
  
  public void writeRetFullToClient(
    final OutputStreamWriter   streamWriter,
    final SSServRetI           result) throws SSErr{
    
    //TODO fix when to send JSON LD Context back to client
//    if(sendJSONLD){
//      ret.put(SSJSONLDU.context, SSJSONLDU.jsonLDContext(result.jsonLDDesc()));
//    }
    
    try{
      final Map<String, Object> ret = new HashMap<>();
      
      ret.put(SSVarNames.op,           result.op);
      ret.put(SSVarNames.error,        false);
      ret.put(result.op,               result);
      
      SSSocketU.writeFullString(streamWriter,  SSJSONU.jsonStr(ret) + SSSocketU.endOfRequest);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void writeError(
    final OutputStreamWriter outputStreamWriter,
    final String             op) throws SSErr{
    
    try{
      final List<SSErr> errors       = SSServErrReg.getErrors();
      final String      errorString;
      
      if(errors.isEmpty()){
        
        errorString =
          getErrorString(
            op, //op
            null, //errorId
            null); //errorMessage
      }else{
        
        errorString =
          getErrorString(
            op, //op
            errors.get(0).id, //errorId
            errors.get(0).message); //errorMessage
      }
      
      SSSocketU.writeFullString(
        outputStreamWriter,
        errorString);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private String getErrorString(
    final String  op,
    final String  errorId,
    final String  errorMessage) throws SSErr{
    
    try{
      final Map<String, Object> ret = new HashMap<>();
      
      ret.put(SSVarNames.op,                    op);
      ret.put(SSVarNames.error,                 true);
//    ret.put(SSVarU.errorMsg,                errorMessages);
//    ret.put(SSVarU.errorClassNames,         SSErrForClient.classNames         (errors));
//    ret.put(SSVarU.errorClassesWhereThrown, SSErrForClient.classesWhereThrown (errors));
//    ret.put(SSVarU.errorMethodsWhereThrown, SSErrForClient.methodsWhereThrown (errors));
//    ret.put(SSVarU.errorLinesWhereThrown,   SSErrForClient.linesWhereThrown   (errors));
//    ret.put(SSVarU.errorThreadsWhereThrown, SSErrForClient.threadsWhereThrown (errors));
      
//    ret.put(SSJSONLDU.context, SSJSONLDU.jsonLDContext());
//    ret.put(SSStrU.toStr(op), null);
      
      ret.put(SSVarNames.id,      errorId);
      ret.put(SSVarNames.message, errorMessage);
      
      return SSJSONU.jsonStr(ret) + SSSocketU.endOfRequest;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}