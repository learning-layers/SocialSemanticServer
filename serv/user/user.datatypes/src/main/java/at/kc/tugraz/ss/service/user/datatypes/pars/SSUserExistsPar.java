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
package at.kc.tugraz.ss.service.user.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "userExists request parameter")
public class SSUserExistsPar extends SSServPar{
  
  @XmlElement
  @ApiModelProperty(
    required = true,
    value = "email address of the user" )
  public String email;
  
  public SSUserExistsPar(){}
    
  public SSUserExistsPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        email         = (String)  pars.get(SSVarU.email);
      }
      
      if(par.clientJSONObj != null){
        email     = par.clientJSONObj.get(SSVarU.email).getTextValue();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
