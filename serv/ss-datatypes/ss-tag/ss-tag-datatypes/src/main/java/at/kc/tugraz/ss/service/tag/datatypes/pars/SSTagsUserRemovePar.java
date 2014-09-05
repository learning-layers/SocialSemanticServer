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
package at.kc.tugraz.ss.service.tag.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "tagsUserRemove request parameter")
public class SSTagsUserRemovePar extends SSServPar{
  
  @ApiModelProperty(
    required = false,
    value = "entity to consider removing tag assignments from")
  public SSUri        entity     = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  @ApiModelProperty(
    required = false,
    value = "label of the tag to consider when removing tag-assignments")
  public SSTagLabel   label      = null;
  
  @XmlElement
  public void setLabel(final String label) throws Exception{
    this.label = SSTagLabel.get(label);
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "access restriction (i.e. privateSpace, sharedSpace) for tag-assignments to be removed")
  public SSSpaceE     space      = null;
  
  public SSTagsUserRemovePar(){}
  
  public SSTagsUserRemovePar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        entity   = (SSUri)        pars.get(SSVarU.entity);
        
        try{
          label    = SSTagLabel.get((String) pars.get(SSVarU.label));
        }catch(Exception error){}
        
        space    = (SSSpaceE)     pars.get(SSVarU.space);
      }
      
      if(par.clientJSONObj != null){
        
        try{
          entity   = SSUri.get        (par.clientJSONObj.get(SSVarU.entity).getTextValue());
        }catch(Exception error){}
        
        try{
          label  = SSTagLabel.get   (par.clientJSONObj.get(SSVarU.label).getTextValue());
        }catch(Exception error){}
        
        try{
          space      = SSSpaceE.get  (par.clientJSONObj.get(SSVarU.space).getTextValue());
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
