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
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
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
@XmlRootElement
@ApiModel(value = "entityDescsGet request parameter")
public class SSEntityDescsGetPar extends SSServPar{
  
  
  @ApiModelProperty( required = false, value = "entities to get details for (optional if types is set)")
  public List<SSUri>     entities          = new ArrayList<>();
  
  @XmlElement
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities);
  }
  
  @ApiModelProperty(required = false, value = "types of entities (optional if entities is set)")
  public List<SSEntityE> types             = new ArrayList<>();
  
  @XmlElement
  public void setTypes(final List<String> types){
    try{ this.types = SSEntityE.get(types); }catch(Exception error){}
  }
  
  @XmlElement
  @ApiModelProperty( required = false, value = "whether tags for entities should be delivered (optional)")
  public Boolean         getTags           = false;
  
  @XmlElement
  @ApiModelProperty( required = false, value = "whether overall ratings for entities should be delivered (optional)")
  public Boolean         getOverallRating  = false;
  
  @XmlElement
  @ApiModelProperty(  required = false, value = "whether discussion uris for entities shall be included (optional)")
  public Boolean         getDiscs          = false;
  
  @XmlElement
  @ApiModelProperty( required = false, value = "whether user events for user's given/resulting entities shall be returned (optional)")
  public Boolean         getUEs            = false;
  
  @XmlElement
  @ApiModelProperty( required = false, value = "whether a thumbnail for files should be included (optional)")
  public Boolean         getThumb          = false;
  
  @XmlElement
  @ApiModelProperty(  required = false, value = "whether flags for the user's entities should be included (optional)")
  public Boolean         getFlags          = false;

  public SSEntityDescsGetPar(){}
  
  public SSEntityDescsGetPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        entities         = (List<SSUri>)     pars.get(SSVarU.entities);
        types            = (List<SSEntityE>) pars.get(SSVarU.types);
        getTags          = (Boolean)         pars.get(SSVarU.getTags);
        getOverallRating = (Boolean)         pars.get(SSVarU.getOverallRating);
        getDiscs         = (Boolean)         pars.get(SSVarU.getDiscs);
        getUEs           = (Boolean)         pars.get(SSVarU.getUEs);
        getThumb         = (Boolean)         pars.get(SSVarU.getThumb);
        getFlags         = (Boolean)         pars.get(SSVarU.getFlags);
      }
      
      if(par.clientJSONObj != null){
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.entities)) {
            entities.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.types)) {
            types.add(SSEntityE.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          getTags            = par.clientJSONObj.get(SSVarU.getTags).getBooleanValue();
        }catch(Exception error){}
        
        try{
          getOverallRating   = par.clientJSONObj.get(SSVarU.getOverallRating).getBooleanValue();
        }catch(Exception error){}
        
        try{
          getDiscs        = par.clientJSONObj.get(SSVarU.getDiscs).getBooleanValue();
        }catch(Exception error){}
        
        try{
          getUEs        = par.clientJSONObj.get(SSVarU.getUEs).getBooleanValue();
        }catch(Exception error){}
        
        try{
          getThumb        = false;
        }catch(Exception error){}
        
        try{
          getFlags        = par.clientJSONObj.get(SSVarU.getFlags).getBooleanValue();
        }catch(Exception error){}
      }
    }catch(Exception error){
      throw new SSErr(SSErrE.servParCreationFailed);
    }
  }
  
  /* json getters */
  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getTypes() throws Exception{
    return SSStrU.toStr(types);
  }
}