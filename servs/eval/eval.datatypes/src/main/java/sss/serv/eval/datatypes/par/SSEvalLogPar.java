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
import at.tugraz.sss.serv.SSVarU;
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
        toolContext  = (SSToolContextE)  pars.get(SSVarU.toolContext);
        forUser      = (SSUri)           pars.get(SSVarU.forUser);
        type         = (SSEvalLogE)      pars.get(SSVarU.type);
        entity       = (SSUri)           pars.get(SSVarU.entity);
        content      = (String)          pars.get(SSVarU.content);
        entities     = (List<SSUri>)     pars.get(SSVarU.entities);
        users        = (List<SSUri>)     pars.get(SSVarU.users);
      }
      
      if(par.clientJSONObj != null){
        
        type   = SSEvalLogE.valueOf(par.clientJSONObj.get(SSVarU.type).getTextValue());
        
        try{ 
          toolContext   = SSToolContextE.valueOf(par.clientJSONObj.get(SSVarU.toolContext).getTextValue());
        }catch(Exception error){}
          
        try{ 
          content   = par.clientJSONObj.get(SSVarU.content).getTextValue();
        }catch(Exception error){}
        
        try{ 
          forUser   = SSUri.get(par.clientJSONObj.get(SSVarU.forUser).getTextValue());
        }catch(Exception error){}
        
        try{
          entity   = SSUri.get(par.clientJSONObj.get(SSVarU.entity).getTextValue());
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.entities)) {
            entities.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.users)) {
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