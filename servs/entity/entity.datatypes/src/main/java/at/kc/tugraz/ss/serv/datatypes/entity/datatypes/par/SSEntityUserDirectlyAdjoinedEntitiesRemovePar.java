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
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import at.tugraz.sss.serv.SSServErrReg;
@XmlRootElement
@ApiModel(value = "entityUserDirectlyAdjoinedEntitiesRemove request parameter")
public class SSEntityUserDirectlyAdjoinedEntitiesRemovePar extends SSServPar{
  
  @ApiModelProperty(
    required = true,
    value = "entity to be removed")
  public SSUri   entity              = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether the user's tags should be removed from the entity")
  public Boolean removeUserTags      = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether the user's ratings should be removed from the entity")
  public Boolean removeUserRatings   = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether the entity should be removed from all the user's collections")
  public Boolean removeFromUserColls = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether locations added by the user should be removed from the entity")
  public Boolean removeUserLocations = null;
  
  public SSEntityUserDirectlyAdjoinedEntitiesRemovePar(){}
    
  public SSEntityUserDirectlyAdjoinedEntitiesRemovePar(final SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        entity               = (SSUri)     pars.get(SSVarU.entity);
        removeUserTags       = (Boolean)   pars.get(SSVarU.removeUserTags);
        removeUserRatings    = (Boolean)   pars.get(SSVarU.removeUserRatings);
        removeFromUserColls  = (Boolean)   pars.get(SSVarU.removeFromUserColls);
        removeUserLocations  = (Boolean)   pars.get(SSVarU.removeUserLocations);
      }
      
      if(par.clientJSONObj != null){
        entity     = SSUri.get      (par.clientJSONObj.get(SSVarU.entity).getTextValue());
        
        try{
          removeUserTags   = par.clientJSONObj.get(SSVarU.removeUserTags).getBooleanValue();
        }catch(Exception error){}
        
        try{
          removeUserRatings   = par.clientJSONObj.get(SSVarU.removeUserRatings).getBooleanValue();
        }catch(Exception error){}
        
        try{
          removeFromUserColls   = par.clientJSONObj.get(SSVarU.removeFromUserColls).getBooleanValue();
        }catch(Exception error){}
        
        try{
          removeUserLocations   = par.clientJSONObj.get(SSVarU.removeUserLocations).getBooleanValue();
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
}