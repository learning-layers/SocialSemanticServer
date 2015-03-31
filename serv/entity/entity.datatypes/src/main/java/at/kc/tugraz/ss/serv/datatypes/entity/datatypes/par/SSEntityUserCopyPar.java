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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;
import at.tugraz.sss.serv.SSServErrReg;
@XmlRootElement
@ApiModel(value = "entityUserCopy request parameter")
public class SSEntityUserCopyPar extends SSServPar{
  
  @ApiModelProperty(
    required = true,
    value = "entity to copy")
  public SSUri         entity             = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  @ApiModelProperty(
    required = true,
    value = "users to copy the entity for")
  public List<SSUri>   users              = new ArrayList<>();
  
  @XmlElement
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users);
  }
  
  @ApiModelProperty(
    required = false,
    value = "if set contains sub entities to exclude from copying")
  public List<SSUri>   entitiesToExclude  = new ArrayList<>();
  
  @XmlElement
  public void setEntitiesToExclude(final List<String> entitiesToExclude) throws Exception{
    this.entitiesToExclude = SSUri.get(entitiesToExclude);
  }
  
  @ApiModelProperty(
    required = false,
    value = "optional describing text")
  public SSTextComment comment            = null;
  
  @XmlElement
  public void setComment(final String comment) throws Exception{
    this.comment = SSTextComment.get(comment);
  }
  
  public SSEntityUserCopyPar(){}
    
  public SSEntityUserCopyPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        entity            = (SSUri)         pars.get(SSVarU.entity);
        users             = (List<SSUri>)   pars.get(SSVarU.users);
        entitiesToExclude = (List<SSUri>)   pars.get(SSVarU.entitiesToExclude);
        comment           = (SSTextComment) pars.get(SSVarU.comment);
      }
      
      if(par.clientJSONObj != null){
        entity       = SSUri.get (par.clientJSONObj.get(SSVarU.entity).getTextValue());
        
        for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.users)) {
          users.add(SSUri.get(objNode.getTextValue()));
        }
        
        for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.entitiesToExclude)) {
          entitiesToExclude.add(SSUri.get(objNode.getTextValue()));
        }
        
        try{
          comment = SSTextComment.get(par.clientJSONObj.get(SSVarU.comment).getTextValue());
        }catch(Exception error){}
      }
      
    }catch(Exception error){
      throw new SSErr(SSErrE.servParCreationFailed);
    }
  }
  
  /* json getters */
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public List<String> getEntitiesToExclude() throws Exception{
    return SSStrU.removeTrailingSlash(entitiesToExclude);
  }
  
  public String getComment(){
    return SSStrU.toStr(comment);
  }
}