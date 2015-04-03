/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.circle.datatypes.par;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;

@XmlRootElement
@ApiModel(value = "entityUserShare request parameter")
public class SSCircleEntitySharePar extends SSServPar{
  
  @ApiModelProperty( 
    required = true, 
    value = "entity to be shared")
  public SSUri         entity          = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "user to share the entity with")
  public List<SSUri>   users           = new ArrayList<>();
    
  @XmlElement
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "circles to share the entity with")
  public List<SSUri>   circles           = new ArrayList<>();
    
  @XmlElement
  public void setGroups(final List<String> circles) throws Exception{
    this.circles = SSUri.get(circles);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "textual comment for sharing")
  public SSTextComment comment         = null;

  @XmlElement
  public void getComment(final String comment) throws Exception{
    this.comment = SSTextComment.get(comment);
  }

  public SSCircleEntitySharePar(){}
    
  public SSCircleEntitySharePar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        entity       = (SSUri)                   pars.get(SSVarU.entity);
        users        = (List<SSUri>)             pars.get(SSVarU.users);
        circles      = (List<SSUri>)             pars.get(SSVarU.circles);
        comment      = (SSTextComment)           pars.get(SSVarU.comment);
      }
      
      if(par.clientJSONObj != null){
        
        entity       = SSUri.get        (par.clientJSONObj.get(SSVarU.entity).getTextValue());
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.users)) {
            users.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.circles)) {
            circles.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          comment     = SSTextComment.get(par.clientJSONObj.get(SSVarU.comment).getTextValue());
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /*json getters */
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public List<String> getCircles() throws Exception{
    return SSStrU.removeTrailingSlash(circles);
  }
  
  public String getComment(){
    return SSStrU.toStr(comment);
  }
}