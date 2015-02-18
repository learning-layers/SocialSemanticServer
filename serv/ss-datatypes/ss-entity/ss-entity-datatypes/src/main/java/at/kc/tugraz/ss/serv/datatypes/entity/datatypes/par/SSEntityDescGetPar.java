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
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "entityDescGet request parameter")
public class SSEntityDescGetPar extends SSServPar{
  
  @ApiModelProperty( 
    required = true, 
    value = "entity to get details for")
  public SSUri    entity            = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether tags for the entity should be delivered")
  public Boolean  getTags           = false;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether the overall rating for the entity should be delivered")
  public Boolean  getOverallRating  = false;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether the uris of discussions about the entity should be returned")
  public Boolean  getDiscs          = false;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether user events for given user and entity should be included")
  public Boolean  getUEs            = false;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether a thumbnail for files should be included")
  public Boolean  getThumb          = false;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether flags for this user and entity should be included")
  public Boolean  getFlags          = false;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether the entity's circles should be included")
  public Boolean  getCircles          = false;
  
  public SSEntityDescGetPar(){}
    
  public SSEntityDescGetPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        entity           = (SSUri)   pars.get(SSVarU.entity);
        getTags          = (Boolean) pars.get(SSVarU.getTags);
        getOverallRating = (Boolean) pars.get(SSVarU.getOverallRating);
        getDiscs         = (Boolean) pars.get(SSVarU.getDiscs);
        getUEs           = (Boolean) pars.get(SSVarU.getUEs);
        getThumb         = (Boolean) pars.get(SSVarU.getThumb);
        getFlags         = (Boolean) pars.get(SSVarU.getFlags);
        getCircles       = (Boolean) pars.get(SSVarU.getCircles);
      }
      
      if(par.clientJSONObj != null){
        entity          = SSUri.get        (par.clientJSONObj.get(SSVarU.entity).getTextValue());
        
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
          getThumb        = par.clientJSONObj.get(SSVarU.getThumb).getBooleanValue();
        }catch(Exception error){}
        
        try{
          getFlags        = par.clientJSONObj.get(SSVarU.getFlags).getBooleanValue();
        }catch(Exception error){}
        
        try{
          getCircles        = par.clientJSONObj.get(SSVarU.getCircles).getBooleanValue();
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
}