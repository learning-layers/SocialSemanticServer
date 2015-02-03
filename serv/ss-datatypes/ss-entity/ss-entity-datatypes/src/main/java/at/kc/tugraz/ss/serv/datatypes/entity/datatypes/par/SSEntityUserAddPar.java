/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "entityUserAdd request parameter")
public class SSEntityUserAddPar extends SSServPar{
  
  @ApiModelProperty( 
    required = false, 
    value = "link to the entity (will be used for ID generation if set")
  public SSUri         link           = null;
  
  @XmlElement
  public void setLink(final String link) throws Exception{
    this.link = SSUri.get(link);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "type of the entity")
  public SSEntityE     type           = null;
  
  @XmlElement
  public void setType(final String type) throws Exception{
    this.type = SSEntityE.get(type);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "entity's title")
  public SSLabel       label          = null;
  
  @XmlElement
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "entity's description")
  public SSTextComment description    = null;
  
  @XmlElement
  public void setDescription(final String description) throws Exception{
    this.description = SSTextComment.get(description);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "creation time to be set when adding the entity")
  public Long          creationTime   = null;
  
  public SSEntityUserAddPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        link            = (SSUri)         pars.get(SSVarU.link);
        label           = (SSLabel)       pars.get(SSVarU.label);
        type            = (SSEntityE)     pars.get(SSVarU.type);
        description     = (SSTextComment) pars.get(SSVarU.description);
        creationTime    = (Long)          pars.get(SSVarU.creationTime);
      }
      
      if(par.clientJSONObj != null){
        
        try{
          link          = SSUri.get        (par.clientJSONObj.get(SSVarU.link).getTextValue());
        }catch(Exception error){}
        
        try{
          label            = SSLabel.get(par.clientJSONObj.get(SSVarU.label).getTextValue());
        }catch(Exception error){}
        
        try{
          type            = SSEntityE.get(par.clientJSONObj.get(SSVarU.type).getTextValue());
        }catch(Exception error){}
        
        try{
          description            = SSTextComment.get(par.clientJSONObj.get(SSVarU.description).getTextValue());
        }catch(Exception error){}
        
        try{
          creationTime        = par.clientJSONObj.get(SSVarU.creationTime).getLongValue();
        }catch(Exception error){}
      }      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  
  /* json getters */
  public String getLink(){
    return SSStrU.removeTrailingSlash(link);
  }
  
  public String getType(){
    return SSStrU.toStr(type);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  public String getDescription(){
    return SSStrU.toStr(description);
  }
}