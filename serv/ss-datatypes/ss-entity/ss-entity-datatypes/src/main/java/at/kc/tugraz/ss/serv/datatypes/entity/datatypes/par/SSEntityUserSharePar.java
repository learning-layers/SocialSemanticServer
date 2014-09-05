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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;

@XmlRootElement
@ApiModel(value = "entityUserShare request parameter")
public class SSEntityUserSharePar extends SSServPar{
  
  @ApiModelProperty( 
    required = true, 
    value = "entity to be shared")
  public SSUri         entity          = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  @ApiModelProperty( 
    required = true, 
    value = "user to share the entity with")
  public List<SSUri>   users           = new ArrayList<>();
    
  @XmlElement
  public void setUsers(final String users) throws Exception{
    this.users = SSUri.get(SSStrU.splitDistinctWithoutEmptyAndNull(users, SSStrU.comma));
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "textual comment for sharing")
  public SSTextComment comment         = null;

  @XmlElement
  public void getComment(final String comment) throws Exception{
    this.comment = SSTextComment.get(comment);
  }

  public SSEntityUserSharePar(){}
    
  public SSEntityUserSharePar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        entity       = (SSUri)                   pars.get(SSVarU.entity);
        users        = (List<SSUri>)             pars.get(SSVarU.users);
        
        try{
          comment    = SSTextComment.get((String)pars.get(SSVarU.comment));
        }catch(Exception error){}
      }
      
      if(par.clientJSONObj != null){
        
        saveActivity = true;
        entity       = SSUri.get        (par.clientJSONObj.get(SSVarU.entity).getTextValue());
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.users)) {
            users.add(SSUri.get(objNode.getTextValue()));
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
  
  public String getComment(){
    return SSStrU.toStr(comment);
  }
}