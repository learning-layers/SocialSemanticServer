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
package at.tugraz.sss.serv.datatype.par;

import at.tugraz.sss.serv.util.SSJSONU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.reg.*;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import java.net.Socket;
  
@XmlRootElement
public class SSServPar{
  
  public String               op                  = null;
  public SSUri                user                = null;
  public String               key                 = null;
  
  @XmlElement
  public Boolean              withUserRestriction = true;

  @JsonIgnore
  public Socket               clientSocket        = null;
  
  @JsonIgnore
  public Map<String, Object>  pars                = null;
  
  @JsonIgnore
  public Boolean              shouldCommit        = true;

  @JsonIgnore
  public String               clientJSONRequ      = null;
  
  @JsonIgnore
  public JsonNode             clientJSONObj       = null;
  
  @XmlElement
  public void setUser(final String user) throws Exception{
    this.user = SSUri.get(user);
  }
  
  public String getOp(){
    return SSStrU.toStr(op);
  }
  
  public String getUser(){
    return SSStrU.removeTrailingSlash(user);
  }
  
  public SSServPar(){
  }
  
  public SSServPar(
    final Socket       clientSocket,
    final String       clientJSONRequ) throws Exception{
    
    this.clientSocket      = clientSocket;
    this.clientJSONRequ    = clientJSONRequ;
    
    this.op  = SSJSONU.getValueFromJSON(clientJSONRequ, SSVarNames.op);
    this.key = SSJSONU.getValueFromJSON(clientJSONRequ, SSVarNames.key);
    
    //TODO for anchient use of serv par
    try{
      this.user = SSUri.get(SSJSONU.getValueFromJSON(clientJSONRequ, SSVarNames.user));
    }catch(Exception error){}
    
    //TODO for anchient use of serv par
    clientJSONObj = new ObjectMapper().readTree(clientJSONRequ);
  }

  public SSServPar(
    final String               op,
    final Map<String, Object>  pars) throws Exception{
    
    this.op            = op;
    this.pars          = pars;
    
    if(
      this.op   == null ||
      this.pars == null){
      throw new Exception("op or pars is/are empty");
    }
    
    //TODO code below for anchient serv par use
    try{
      user = (SSUri) pars.get(SSVarNames.user);
    }catch(Exception error){}
    
    try{
      key = (String) pars.get(SSVarNames.key);
    }catch(Exception error){}
    
    try{
      shouldCommit = (Boolean) pars.get(SSVarNames.shouldCommit);
    }catch(Exception error){}
    
    try{
      withUserRestriction = (Boolean) pars.get(SSVarNames.withUserRestriction);
    }catch(Exception error){}
  }
  
  protected SSServPar(
    final String       op,
    final String       key,
    final SSUri        user){
    
    this.op     = op;
    this.key    = key;
    this.user   = user;
  }
  
//  //TODO constructor for anchient serv par use
//  protected SSServPar(final SSServPar par) throws Exception{
//    
//    this.op           = par.op;
//    this.user         = par.user;
//    this.key          = par.key;
//
//    if(par.shouldCommit != null){
//      this.shouldCommit = par.shouldCommit;
//    }
//    
//    if(par.withUserRestriction != null){
//      this.withUserRestriction = par.withUserRestriction;
//    }
//    
//    this.pars         = par.pars;
//  }
  
  public SSServPar getFromClient(
    final SSClientE clientType, 
    final SSServPar par,
    final Class     subClass) throws SSErr{
    
    try{
      
      switch(clientType){
        
        case rest:{
          return par;
        }
        
        case socket:{
          return getFromJSON(subClass);
        }
      }
      
      throw new UnsupportedOperationException();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  protected SSServPar getFromJSON(final Class subClass) throws SSErr{
    
    try{
      final SSServPar result = (SSServPar) SSJSONU.obj(clientJSONRequ, subClass);
      
      result.op                  = op;
      result.key                 = key;
      result.user                = user;
      result.withUserRestriction = withUserRestriction;
      
      return result;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
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
//          op = SSVarNames.get(jValue);
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
//    finally{
//      
//      if(jp != null){
//        jp.close();
//      }
//    }