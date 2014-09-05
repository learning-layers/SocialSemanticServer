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
 package at.kc.tugraz.ss.service.disc.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;

@XmlRootElement
@ApiModel(value = "discUserEntryAdd request parameter")
public class SSDiscUserEntryAddPar extends SSServPar{
  
  @ApiModelProperty( 
    required = false, 
    value = "discussion to add an entry for (optional in case of a new discussion)")
  public SSUri               disc           = null;
  
  @XmlElement
  public void setDisc(final String disc) throws Exception{
    this.disc = SSUri.get(disc);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "entity to start a discussion for (optional)")
  public SSUri               entity         = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "text for the comment/answer/opinion (optional in case of a new discussion)")
  public SSTextComment       entry          = null;
  
  @XmlElement
  public void setEntry(final String entry) throws Exception{
    this.entry = SSTextComment.get(entry);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether a new disc should be created (optional)")
  public Boolean             addNewDisc     = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "discussion type: disc, qa or chat (optional in case of an existing discussion)")
  public SSEntityE           type           = null;
  
  @ApiModelProperty( 
    required = false, 
    value = "discussion name (optional in case of an existing discussion)")
  public SSLabel             label          = null;
  
  @XmlElement
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "describes the discussion in more detail (optional, except in case of a new discussion of type qa)")
  public SSTextComment       description    = null;
    
  @XmlElement
  public void setDescription(final String description) throws Exception{
    this.description = SSTextComment.get(description);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "provides users to share this discussion with upon creation of a new discussion (optional, but works only for a new discussion)")
  public List<SSUri>         users          = new ArrayList<>();
  
  @XmlElement
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "provides entities to be attached either to corresponding discussion if new discussion to be added or to respective entry in the other case (optional)")
  public List<SSUri>         entities       = new ArrayList<>();
            
  @XmlElement
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities);
  }
  
  public SSDiscUserEntryAddPar(){}
    
  public SSDiscUserEntryAddPar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        disc          = (SSUri)              pars.get(SSVarU.disc);
        entity        = (SSUri)              pars.get(SSVarU.entity);
        entry         = (SSTextComment)      pars.get(SSVarU.entry);
        addNewDisc    = (Boolean)            pars.get(SSVarU.addNewDisc);
        type          = (SSEntityE)          pars.get(SSVarU.type);
        label         = (SSLabel)            pars.get(SSVarU.label);
        description   = (SSTextComment)      pars.get(SSVarU.description);
        users         = (List<SSUri>)        pars.get(SSVarU.users);
        entities      = (List<SSUri>)        pars.get(SSVarU.entities);
      }
      
      if(par.clientJSONObj != null){
        
        try{
          entity      = SSUri.get             (par.clientJSONObj.get(SSVarU.entity).asText());
        }catch(Exception error){}
        
        try{
          disc        = SSUri.get             (par.clientJSONObj.get(SSVarU.disc).asText());
        }catch(Exception error){}
        
        try{
          addNewDisc  = Boolean.valueOf       (par.clientJSONObj.get(SSVarU.addNewDisc).asText());
        }catch(Exception error){}
        
        try{
          entry     = SSTextComment.get(par.clientJSONObj.get(SSVarU.entry).asText());
        }catch(Exception error){}
        
        try{
          type     = SSEntityE.get(par.clientJSONObj.get(SSVarU.type).asText());
        }catch(Exception error){}
        
        try{
          label     = SSLabel.get(par.clientJSONObj.get(SSVarU.label).asText());
        }catch(Exception error){}
        
        try{
          description   = SSTextComment.get(par.clientJSONObj.get(SSVarU.description).asText());
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.users)) {
            users.add(SSUri.get(objNode.asText()));
          }
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.entities)) {
            entities.add(SSUri.get(objNode.asText()));
          }
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getDisc(){
    return SSStrU.removeTrailingSlash(disc);
  }

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }

  public String getEntry(){
    return SSStrU.toStr(entry);
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

  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }

  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
}
