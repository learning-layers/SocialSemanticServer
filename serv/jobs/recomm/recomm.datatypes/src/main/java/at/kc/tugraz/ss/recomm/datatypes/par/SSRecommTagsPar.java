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
package at.kc.tugraz.ss.recomm.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.JsonNode;

@XmlRootElement
@ApiModel(value = "recommTags request parameter")
public class SSRecommTagsPar extends SSServPar{
  
  @ApiModelProperty( 
    required = false, 
    value = "user to be considered to retrieve recommendations for")
  public SSUri         forUser    = null;
  
  @XmlElement
  public void setForUser(final String forUser) throws Exception{
    this.forUser = SSUri.get(forUser);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "resource to be considered to retrieve recommendations for")
  public SSUri         entity     = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "additional information to be taken into account")
  public List<String>  categories = new ArrayList<>();
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "number of tags to be returned")
  public Integer       maxTags    = 10;
  
  public SSRecommTagsPar(){}
    
  public SSRecommTagsPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      if(pars != null){
        this.forUser    =  (SSUri)         pars.get(SSVarU.forUser);
        this.entity     =  (SSUri)         pars.get(SSVarU.entity);
        this.categories =  (List<String>)  pars.get(SSVarU.categories);
        this.maxTags    =  (Integer)       pars.get(SSVarU.maxTags);
      }
      
      if(par.clientJSONObj != null){
        
        try{
          this.forUser   = SSUri.get         (par.clientJSONObj.get(SSVarU.forUser).asText());
        }catch(Exception error){}
        
        try{
          this.entity = SSUri.get         (par.clientJSONObj.get(SSVarU.entity).asText());
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.categories)) {
            categories.add(objNode.asText());
          }
        }catch(Exception error){}
        
        try{
          this.maxTags   = Integer.valueOf   (par.clientJSONObj.get(SSVarU.maxTags).asText());
        }catch(Exception error){}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
}
