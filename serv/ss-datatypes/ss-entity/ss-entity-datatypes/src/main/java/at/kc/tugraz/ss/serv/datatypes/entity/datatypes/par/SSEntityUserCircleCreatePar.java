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
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "entityUserCircleCreate request parameter")
public class SSEntityUserCircleCreatePar extends SSServPar{
  
  @XmlElement
  @ApiModelProperty(
    required = true,
    value = "circle name")
  public SSLabel                label          = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "entities to add")
  public List<SSUri>            entities       = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "users to add")
  public List<SSUri>            users          = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "textual annotation")
  public SSTextComment          description    = null;

  public Boolean                isSystemCircle = null;
  public SSCircleE              type           = null;

  public SSEntityUserCircleCreatePar(){}
    
  public SSEntityUserCircleCreatePar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
    
      if(pars != null){
        type           = (SSCircleE)       pars.get(SSVarU.type);
        label          = (SSLabel)         pars.get(SSVarU.label);
        entities       = (List<SSUri>)     pars.get(SSVarU.entities);
        users          = (List<SSUri>)     pars.get(SSVarU.users);
        description    = (SSTextComment)   pars.get(SSVarU.description);
        isSystemCircle = (Boolean)         pars.get(SSVarU.isSystemCircle);
      }
      
      if(clientPars != null){
        isSystemCircle = false;
        type           = SSCircleE.group;
        label          = SSLabel.get         (clientPars.get(SSVarU.label));
        
        try{
          entities       = SSUri.get (SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.entities), SSStrU.comma));
        }catch(Exception error){}
        
        try{
          users         = SSUri.get (SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.users), SSStrU.comma));
        }catch(Exception error){}
        
        try{
          description         = SSTextComment.get(clientPars.get(SSVarU.description));
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public String getDescription() throws Exception{
    return SSStrU.toStr(description);
  }
  
  public String getLabel() throws Exception{
    return SSStrU.toStr(label);
  }
  
  public String getType() throws Exception{
    return SSStrU.toStr(type);
  }
}
