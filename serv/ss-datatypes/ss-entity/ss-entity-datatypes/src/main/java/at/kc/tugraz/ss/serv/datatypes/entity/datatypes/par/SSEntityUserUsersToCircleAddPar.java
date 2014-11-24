/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;

@XmlRootElement
@ApiModel(value = "entityUserUsersToCircleAdd request parameter")
public class SSEntityUserUsersToCircleAddPar extends SSServPar{
  
  @ApiModelProperty(
    required = true,
    value = "circle to add users to")
  public SSUri       circle  = null;
  
  @XmlElement
  public void setCircle(final String circle) throws Exception{
    this.circle = SSUri.get(circle);
  }
  
  @ApiModelProperty(
    required = true,
    value = "users to add")
  public List<SSUri> users   = new ArrayList<>();
  
  @XmlElement
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users);
  }
  
  public SSEntityUserUsersToCircleAddPar(){}
     
  public SSEntityUserUsersToCircleAddPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
    
      if(pars != null){
        circle        = (SSUri)         pars.get(SSVarU.circle);
        users         = (List<SSUri>)   pars.get(SSVarU.users);
      }
      
      if(par.clientJSONObj != null){
        
        try{ 
          circle        = SSUri.get (par.clientJSONObj.get(SSVarU.circle).getTextValue()); 
        }catch(Exception error){}
        
        for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.users)) {
          users.add(SSUri.get(objNode.getTextValue()));
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public String getCircle() throws Exception{
    return SSStrU.removeTrailingSlash(circle);
  }
}