/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.service.filerepo.datatypes.pars;

import at.tugraz.sss.serv.SSMimeTypeE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;

@ApiModel(value = "file upload request parameter")
public class SSFileUploadPar extends SSServPar{
  
  @ApiModelProperty(
    value = "name",
    required = true)
  public SSLabel  label     = null;
  
  @XmlElement
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }
  
  @ApiModelProperty(
    value = "file mime type",
    required = true)
  public SSMimeTypeE   mimeType  = null;
  
  public SSFileUploadPar(){}
  
  public SSFileUploadPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        mimeType   = (SSMimeTypeE)     pars.get(SSVarNames.mimeType);
        label      = (SSLabel)    pars.get(SSVarNames.label);
      }
      
      if(par.clientJSONObj != null){
        mimeType = SSMimeTypeE.get(par.clientJSONObj.get(SSVarNames.mimeType).getTextValue());
        label    = SSLabel.get (par.clientJSONObj.get(SSVarNames.label).getTextValue());
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  public String getMimeType(){
    return SSStrU.toStr(mimeType);
  }
}
