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

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

@XmlRootElement
public class SSServPar{
  
  @XmlElement 
  @ApiModelProperty( 
    value = "operation to be executed", 
    required = true)
  public        SSMethU              op            = null;
  
  @XmlElement 
  @ApiModelProperty( 
    value = "the user's identifier", 
    required = true)
  public        SSUri                user          = null;
  
  @XmlElement 
  @ApiModelProperty( 
    value = "the user's access tocken", 
    required = true)
  public String key                    = null;
  
  @JsonIgnore (value = true)
  public        Map<String, Object>  pars          = null;
  
//  @JsonIgnore (value = true)
//  public        Map<String, String>  clientPars    = null;
  
  @JsonIgnore (value = true)
  public        Boolean              shouldCommit  = true;
  
  @JsonIgnore (value = true)
  public        Boolean              saveUE        = true;
  
  @JsonIgnore (value = true)
  public        Boolean              saveActivity  = false;
  
  @JsonIgnore (value = true)
  public        Boolean              tryAgain      = true;
  
  @JsonIgnore (value = true)
  public        JsonNode             clientJSONObj = null;
  
  protected SSServPar(
    final SSMethU op,
    final String  key,
    final SSUri   user){
    
    this.op   = op;
    this.key  = key;
    this.user = user;
  }
  
  public SSServPar(final String jsonRequ) throws Exception{
    
    try{
      
      final ObjectMapper mapper       = new ObjectMapper();
      final JsonNode     jsonRootNode = mapper.readTree(jsonRequ);

      op   = SSMethU.get      (jsonRootNode.get(SSVarU.op).getTextValue());
      key  = jsonRootNode.get (SSVarU.key).getTextValue();
      
      try{
        user = SSUri.get        (jsonRootNode.get(SSVarU.user).getTextValue());
      }catch(Exception error){}
      
      if(
        SSObjU.isNull  (this.op)||
        SSStrU.isEmpty (this.key)){
        throw new Exception("op or key is empty");
      }
      
      this.clientJSONObj = jsonRootNode;
      
//      JsonParser jp = null;
//      String     jKey;
//      String     jValue;
//      clientPars = new HashMap<>();
//      
//      jp = SSJSONU.jsonParser(jsonRequ);
//      
//      jp.nextToken();
//      
//      while(jp.nextToken() != SSJSONU.jsonEnd) {
//        
//        jKey = jp.getCurrentName();
//        
//        jp.nextToken();
//        
//        jValue = jp.getText();
//        
//        if(SSStrU.equals(jKey, SSVarU.op)){
//          op = SSMethU.get(jValue);
//          continue;
//        }
//        
//        if(SSStrU.equals(jKey, SSVarU.user)){
//          user = SSUri.get(jValue);
//          clientPars.put(jKey, jValue);
//          continue;
//        }
//        
//        if(SSStrU.equals(jKey, SSVarU.key)){
//          key         = jValue;
//          clientPars.put(jKey, jValue);
//          continue;
//        }
//        
//        if(SSStrU.isEmpty(jValue)){
//          clientPars.put(jKey, null);
//        }else{
//          clientPars.put(jKey, jValue);
//        }
//      }
//      
//      if(
//        SSObjU.isNull  (this.op, this.user)||
//        SSStrU.isEmpty (this.key)){
//        throw new Exception("op, user or key is empty");
//      }
      
    }catch(Exception error){
      throw error;
    }
//    finally{
//      
//      if(jp != null){
//        jp.close();
//      }
//    }
  }
  
  public SSServPar(
    final SSMethU                      op,
    final Map<String, Object>          pars) throws Exception{
    
    this.op            = op;
    this.pars          = pars;
    
    try{
      user = (SSUri) pars.get(SSVarU.user);
    }catch(Exception error1){}
    
    try{
      key = (String) pars.get(SSVarU.key);
    }catch(Exception error2){}
    
    try{
      shouldCommit = (Boolean) pars.get(SSVarU.shouldCommit);
    }catch(Exception error3){}
    
    try{
      saveUE = (Boolean) pars.get(SSVarU.saveUE);
    }catch(Exception error4){}
    
    try{
      saveActivity = (Boolean) pars.get(SSVarU.saveActivity);
    }catch(Exception error5){}
    
    if(
      this.op   == null ||
      this.pars == null){
      throw new Exception("op or pars is/are empty");
    }
  }
  
  protected SSServPar(final SSServPar par) throws Exception{
    
    this.op           = par.op;
    this.user         = par.user;
    this.key          = par.key;

    if(par.shouldCommit != null){
      this.shouldCommit = par.shouldCommit;
    }
    
    if(par.tryAgain != null){
      this.tryAgain = par.tryAgain;
    }
    
    if(par.saveUE != null){
      this.saveUE = par.saveUE;
    }
    
    if(par.saveActivity != null){
      this.saveActivity = par.saveActivity;
    }
    
    this.pars         = par.pars;
//    this.clientPars   = par.clientPars;
  }
  
  protected SSServPar(){}
  
  /* json getters */
  public String getOp(){
    return SSStrU.toStr(op);
  }

  public String getUser(){
    return SSStrU.removeTrailingSlash(user);
  }

  public String getKey(){
    return key;
  }
}
//public class SSRequ {
//
//  public  String      op      = null;
// public  List<String>  pars    = new ArrayList<>();
//
//  public SSRequ(String requJSON) throws Exception{
//    
//    JSONObject objJSON = new JSONObject      (requJSON);
//    JSONArray  parsJSON;
//    
//    op       = objJSON.getString    (SSStrU.valueOp);
//    parsJSON = objJSON.getJSONArray (SSStrU.valuePars);
//      
//    for (int counter = 0; counter < parsJSON.length(); counter++){
//      
//      if(SSStrU.equals(parsJSON.getString(counter), SSStrU.valueNull)){
//        pars.add(null);
//        continue;
//      }
//      
//      pars.add(parsJSON.getString(counter));
//    }
//  }
//  
//  public SSRequ(List<String> opAndPars) throws Exception{
//
//    if(
//      SSObjU.isNull(opAndPars) ||
//      opAndPars.size() < 1){
//
//      SSLogU.logAndThrow(new Exception("sss parameter(s) not set"));
//    }
//    
//    this.op = opAndPars.get(0);
//    
//    this.pars.addAll(opAndPars);
//    this.pars.remove (0);
// }
//  
//  public SSRequ(String op, String ... pars) throws Exception{
//
//    if(
//      SSObjU.isNull (op)        ||
//      SSObjU.isNull ((Object) pars)){
//
//      SSLogU.logAndThrow(new Exception("sss parameter(s) not set"));
//    }
//    
//    this.op = op;
//    this.pars.addAll(Arrays.asList(pars));
// }