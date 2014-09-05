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
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "entityUserCircleGet request parameter")
public class SSEntityUserCircleGetPar extends SSServPar{
  
  @ApiModelProperty(
    required = true,
    value = "circle the circle to retrieve")
  public SSUri   circle                     = null;
  
  @XmlElement
  public void setCircle(final String circle) throws Exception{
    this.circle = SSUri.get(circle);
  }
  
  @ApiModelProperty(
    required = false,
    value = "user for which the circle shall be retrieved")
  public SSUri   forUser                    = null;
  
  @XmlElement
  public void setForUser(final String forUser) throws Exception{
    this.forUser = SSUri.get(forUser);
  }
  
  public Boolean withSystemCircles          = false;
  
  public SSEntityUserCircleGetPar(){}
  
  public SSEntityUserCircleGetPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        forUser              = (SSUri)   pars.get(SSVarU.forUser);
        circle               = (SSUri)   pars.get(SSVarU.circle);
        withSystemCircles    = (Boolean) pars.get(SSVarU.withSystemCircles);
      }
      
      if(par.clientJSONObj != null){
        
        try{
          forUser = SSUri.get(par.clientJSONObj.get(SSVarU.forUser).asText());
        }catch(Exception error){}
        
        circle            = SSUri.get(par.clientJSONObj.get(SSVarU.circle).asText());
        withSystemCircles = false;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
   /* json getters */
  public String getCircle() throws Exception{
    return SSStrU.removeTrailingSlash(circle);
  }
  
  public String getForUser() throws Exception{
    return SSStrU.removeTrailingSlash(forUser);
  }
}