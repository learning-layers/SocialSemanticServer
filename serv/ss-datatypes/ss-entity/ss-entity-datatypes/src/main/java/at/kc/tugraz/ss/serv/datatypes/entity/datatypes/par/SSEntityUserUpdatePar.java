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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "entityUserUpdate request parameter")
public class SSEntityUserUpdatePar extends SSServPar{
  
  @XmlElement
  @ApiModelProperty( 
    required = true, 
    value = "entity to update")
  public SSUri               entity        = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "entity's updated name (optional)")
  public SSLabel             label         = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "entity's updated description (optional)")
  public SSTextComment       description   = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "new textual annotations for the entity (optional)")
  public List<SSTextComment> comments      = new ArrayList<>();
  
  public SSEntityUserUpdatePar(){}
  
  public SSEntityUserUpdatePar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        entity         = (SSUri)               pars.get(SSVarU.entity);
        label          = (SSLabel)             pars.get(SSVarU.label);
        description    = (SSTextComment)       pars.get(SSVarU.description);
        comments       = (List<SSTextComment>) pars.get(SSVarU.comments);
      }
      
      if(clientPars != null){
        entity       = SSUri.get      (clientPars.get(SSVarU.entity));
        
        try{
          label        = SSLabel.get (clientPars.get(SSVarU.label));
        }catch(Exception error){}
        
        try{
          description  = SSTextComment.get(clientPars.get(SSVarU.description));
        }catch(Exception error){}
        
        try{
          comments  = SSTextComment.get(SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.comments), SSStrU.comma));
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

  public String getDescription(){
    return SSStrU.toStr(description);
  }

  public List<String> getComments() throws Exception{
    return SSStrU.toStr(comments);
  }
}