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
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;

@XmlRootElement
@ApiModel(value = "entityUserEntitiesToCircleAdd request parameter")
public class SSEntityUserEntitiesToCircleAddPar extends SSServPar{
  
  @ApiModelProperty(
    required = true,
    value = "circle to add entities to")
  public SSUri       circle  = null;
  
  @XmlElement
  public void setCircle(final String circle) throws Exception{
    this.circle = SSUri.get(circle);
  }
  
  @ApiModelProperty(
    required = true,
    value = "entities to add")
  public List<SSUri> entities = new ArrayList<>();
  
  @XmlElement
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities);
  }
  
  public SSEntityUserEntitiesToCircleAddPar(){}
  
  public SSEntityUserEntitiesToCircleAddPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
    
      if(pars != null){
        circle         = (SSUri)         pars.get(SSVarU.circle);
        entities       = (List<SSUri>)   pars.get(SSVarU.entities);
      }
      
      if(par.clientJSONObj != null){
        
        try{
          circle         = SSUri.get (par.clientJSONObj.get(SSVarU.circle).getTextValue());
        }catch(Exception error){}
        
        for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.entities)) {
          entities.add(SSUri.get(objNode.getTextValue()));
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
   /* json getters */
  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public String getCircle() throws Exception{
    return SSStrU.removeTrailingSlash(circle);
  }
}
