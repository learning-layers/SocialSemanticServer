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
package at.kc.tugraz.ss.service.disc.datatypes.pars;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "discUserRemove request parameter")
public class SSDiscUserRemovePar extends SSServPar{
  
  @ApiModelProperty(
    required = true,
    value = "the disc to remove")
  public SSUri disc         = null;
  
  @XmlElement
  public void setDisc(final String disc) throws Exception{
    this.disc = SSUri.get(disc);
  }
  
  public SSDiscUserRemovePar(){}
  
  public SSDiscUserRemovePar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        disc        = (SSUri)       pars.get(SSVarU.disc);
      }
      
      if(par.clientJSONObj != null){
        disc        = SSUri.get     (par.clientJSONObj.get(SSVarU.disc).getTextValue());
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getDisc(){
    return SSStrU.removeTrailingSlash(disc);
  }
}
