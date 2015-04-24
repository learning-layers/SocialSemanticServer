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
package sss.serv.eval.datatypes.par;

import at.tugraz.sss.serv.SSStrU;
import sss.serv.eval.datatypes.SSEvalLogE;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSToolContextE;
import at.tugraz.sss.serv.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;

@XmlRootElement
@ApiModel(value = "evalLog request parameter")
public class SSEvalLogPar extends SSServPar{

  public static SSEvalLogPar get(SSServPar parA){
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "context in tool where log was triggered")
  public SSToolContextE   toolContext  = null;
  
  @ApiModelProperty(
    required = false,
    value = "user to be logged")
  public SSUri            forUser      = null;

  @XmlElement
  public void setForUser(final String forUser){
    try{ this.forUser = SSUri.get(forUser); }catch(Exception error){}
  }
  
  @XmlElement
  @ApiModelProperty(
    required = true,
    value = "type of the log event")
  public SSEvalLogE       type         = null;

  @ApiModelProperty(
    required = false,
    value = "entity to be logged")
  public SSUri         entity       = null;
  
  @XmlElement
  public void setEntity(final String entity){
    try{ this.entity = SSUri.get(entity); }catch(Exception error){}
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "content to be logged")
  public String           content      = null;
  
  @ApiModelProperty(
    required = false,
    value = "entities to be logged")
  public List<SSUri>   entities     = new ArrayList<>();
  
  @XmlElement
  public void setEntities(final List<String> entities){
    try{ this.entities = SSUri.get(entities); }catch(Exception error){}
  }
  
  @ApiModelProperty(
    required = false,
    value = "users to be logged")
  public List<SSUri>   users        = new ArrayList<>();
  
  @XmlElement
  public void setUsers(final List<String> users){
    try{ this.users = SSUri.get(users); }catch(Exception error){}
  }
  
  public SSEvalLogPar(){}
  
  public SSEvalLogPar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        toolContext  = (SSToolContextE)  pars.get(SSVarNames.toolContext);
        forUser      = (SSUri)           pars.get(SSVarNames.forUser);
        type         = (SSEvalLogE)      pars.get(SSVarNames.type);
        entity       = (SSUri)           pars.get(SSVarNames.entity);
        content      = (String)          pars.get(SSVarNames.content);
        entities     = (List<SSUri>)     pars.get(SSVarNames.entities);
        users        = (List<SSUri>)     pars.get(SSVarNames.users);
      }
      
      if(par.clientJSONObj != null){
        
        type   = SSEvalLogE.valueOf(par.clientJSONObj.get(SSVarNames.type).getTextValue());
        
        try{ 
          toolContext   = SSToolContextE.valueOf(par.clientJSONObj.get(SSVarNames.toolContext).getTextValue());
        }catch(Exception error){}
          
        try{ 
          content   = par.clientJSONObj.get(SSVarNames.content).getTextValue();
        }catch(Exception error){}
        
        try{ 
          forUser   = SSUri.get(par.clientJSONObj.get(SSVarNames.forUser).getTextValue());
        }catch(Exception error){}
        
        try{
          entity   = SSUri.get(par.clientJSONObj.get(SSVarNames.entity).getTextValue());
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarNames.entities)) {
            entities.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarNames.users)) {
            users.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getToolContext(){
    return SSStrU.toStr(toolContext);
  }
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }
  
  public String getType(){
    return SSStrU.toStr(type);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
}