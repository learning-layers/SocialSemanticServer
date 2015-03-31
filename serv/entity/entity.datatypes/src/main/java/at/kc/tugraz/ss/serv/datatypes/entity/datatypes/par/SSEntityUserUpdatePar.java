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

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;
import at.tugraz.sss.serv.SSServErrReg;

@XmlRootElement
@ApiModel(value = "entityUserUpdate request parameter")
public class SSEntityUserUpdatePar extends SSServPar{
  
  @ApiModelProperty( 
    required = true, 
    value = "entity to update")
  public SSUri               entity        = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "entity's updated name (optional)")
  public SSLabel             label         = null;
  
  @XmlElement
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "entity's updated description (optional)")
  public SSTextComment       description   = null;
  
  @XmlElement
  public void setDescription(final String description) throws Exception{
    this.description = SSTextComment.get(description);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "new textual annotations for the entity (optional)")
  public List<SSTextComment> comments      = new ArrayList<>();
  
  @XmlElement
  public void setComments(final List<String> comments) throws Exception{
    this.comments = SSTextComment.get(comments);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether the user has read the entity")
   public Boolean read = null;
  
  public SSEntityUserUpdatePar(){}
  
  public SSEntityUserUpdatePar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        entity         = (SSUri)               pars.get(SSVarU.entity);
        label          = (SSLabel)             pars.get(SSVarU.label);
        description    = (SSTextComment)       pars.get(SSVarU.description);
        comments       = (List<SSTextComment>) pars.get(SSVarU.comments);
        read           = (Boolean)             pars.get(SSVarU.read);
      }
      
      if(par.clientJSONObj != null){
        entity       = SSUri.get      (par.clientJSONObj.get(SSVarU.entity).getTextValue());
        
        try{
          label        = SSLabel.get (par.clientJSONObj.get(SSVarU.label).getTextValue());
        }catch(Exception error){}
        
        try{
          description  = SSTextComment.get(par.clientJSONObj.get(SSVarU.description).getTextValue());
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.comments)) {
            comments.add(SSTextComment.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          read        = par.clientJSONObj.get(SSVarU.read).getBooleanValue();
        }catch(Exception error){}
      }
    }catch(Exception error){
      throw new SSErr(SSErrE.servParCreationFailed);
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