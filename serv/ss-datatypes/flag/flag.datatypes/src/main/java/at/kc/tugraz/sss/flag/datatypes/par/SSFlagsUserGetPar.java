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
package at.kc.tugraz.sss.flag.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.sss.flag.datatypes.SSFlagE;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;

@XmlRootElement
@ApiModel(value = "flagsUserGet request parameter")
public class SSFlagsUserGetPar extends SSServPar{
  
  @ApiModelProperty( value = "entities", required = false )
  public List<SSUri>   entities       = new ArrayList<>();
  
  @XmlElement
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities);
  }
  
  @ApiModelProperty( value = "types", required = false )
  public List<SSFlagE> types          = new ArrayList<>();
  
  @XmlElement
  public void setTypes(final List<String> types) throws Exception{
    this.types = SSFlagE.get(types);
  }
  
  @XmlElement
  @ApiModelProperty( value = "startTime", required = false )
  public Long          startTime      = null;
  
  @XmlElement
  @ApiModelProperty( value = "endTime", required = false )
  public Long          endTime        = null;
  
  public SSFlagsUserGetPar(){}
  
  public SSFlagsUserGetPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        entities  = (List<SSUri>)              pars.get(SSVarU.entities);
        types     = SSFlagE.get((List<String>) pars.get(SSVarU.types));
        startTime = (Long)                     pars.get(SSVarU.startTime);
        endTime   = (Long)                     pars.get(SSVarU.endTime);
      }
      
      if(par.clientJSONObj != null){
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.entities)) {
            entities.add(SSUri.get(objNode.asText()));
          }
        }catch(Exception error){}
        
        try{
          for(final JsonNode objNode : par.clientJSONObj.get(SSVarU.types)) {
            types.add(SSFlagE.get(objNode.asText()));
          }
        }catch(Exception error){}
        
        try{
          startTime  = Long.valueOf (par.clientJSONObj.get(SSVarU.startTime).asText());
        }catch(Exception error){}
        
        try{
          endTime  = Long.valueOf (par.clientJSONObj.get(SSVarU.endTime).asText());
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
  
  public List<String> getTypes() throws Exception{
    return SSStrU.toStr(types);
  }
}