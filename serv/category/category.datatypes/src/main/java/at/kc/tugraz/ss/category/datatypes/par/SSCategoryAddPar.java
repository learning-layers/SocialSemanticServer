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
package at.kc.tugraz.ss.category.datatypes.par;

import at.kc.tugraz.ss.category.datatypes.SSCategoryLabel;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "categoryAdd request parameter")
public class SSCategoryAddPar extends SSServPar{
  
  @ApiModelProperty(
    required = true,
    value = "entity to add category to")
  public SSUri        entity       = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  @ApiModelProperty(
    required = true,
    value = "label of the category to add")
  public SSCategoryLabel   label        = null;
  
  @XmlElement
  public void setLabel(final String label) throws Exception{
    this.label = SSCategoryLabel.get(label);
  }
  
  @XmlElement
  @ApiModelProperty(
    required = true,
    value = "access restriction for the category (i.e. privateSpace, sharedSpace)")
  public SSSpaceE     space        = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "timestamp for the category assignment to be created at in milliseconds")
  public Long         creationTime = null;
  
  public SSCategoryAddPar(){}
  
  public SSCategoryAddPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        entity       = (SSUri)                      pars.get(SSVarU.entity);
        label        = SSCategoryLabel.get((String) pars.get(SSVarU.label));
        space        = (SSSpaceE)                   pars.get(SSVarU.space);
        creationTime = (Long)                       pars.get(SSVarU.creationTime);
      }
      
      if(par.clientJSONObj != null){
        entity     = SSUri.get             (par.clientJSONObj.get(SSVarU.entity).getTextValue());
        label      = SSCategoryLabel.get   (par.clientJSONObj.get(SSVarU.label).getTextValue());
        space      = SSSpaceE.get          (par.clientJSONObj.get(SSVarU.space).getTextValue());
        
        try{
          creationTime = par.clientJSONObj.get(SSVarU.creationTime).getLongValue();
        }catch(Exception error){}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  public String getSpace(){
    return SSStrU.toStr(space);
  }
}
