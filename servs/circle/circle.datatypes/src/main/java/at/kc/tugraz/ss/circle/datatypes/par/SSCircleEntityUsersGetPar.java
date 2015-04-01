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
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "entityUserEntityUsersGet request parameter")
public class SSCircleEntityUsersGetPar extends SSServPar{
  
  @ApiModelProperty( 
    required = true, 
    value = "entity to retrieve users for")  
  public SSUri        entity       = null;
    
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }

  public SSCircleEntityUsersGetPar(){}
  
  public SSCircleEntityUsersGetPar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        entity       = (SSUri)        pars.get(SSVarU.entity);
      }
      
      if(par.clientJSONObj != null){
        entity   = SSUri.get        (par.clientJSONObj.get(SSVarU.entity).getTextValue());
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String entity(){
    return SSStrU.removeTrailingSlash(entity);
  }
}