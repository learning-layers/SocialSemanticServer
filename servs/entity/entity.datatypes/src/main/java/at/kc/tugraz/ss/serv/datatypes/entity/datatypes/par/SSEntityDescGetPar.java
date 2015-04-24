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
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
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
  
  public SSEntityDescGetPar(
    final SSUri   user, 
    final SSUri   entity,
    final Boolean getTags,
    final Boolean getOverallRating,
    final Boolean getDiscs,
    final Boolean getUEs,
    final Boolean getThumb,
    final Boolean getFlags,
    final Boolean getCircles){

    this.user             = user;
    this.entity           = entity;
    this.getTags          = getTags;
    this.getOverallRating = getOverallRating;
    this.getDiscs         = getDiscs;
    this.getUEs           = getUEs;
    this.getThumb         = getThumb;
    this.getFlags         = getFlags;
    this.getCircles       = getCircles;
  }
  
  public SSEntityDescGetPar(){}
    
  public SSEntityDescGetPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        entity           = (SSUri)   pars.get(SSVarNames.entity);
        getTags          = (Boolean) pars.get(SSVarNames.getTags);
        getOverallRating = (Boolean) pars.get(SSVarNames.getOverallRating);
        getDiscs         = (Boolean) pars.get(SSVarNames.getDiscs);
        getUEs           = (Boolean) pars.get(SSVarNames.getUEs);
        getThumb         = (Boolean) pars.get(SSVarNames.getThumb);
        getFlags         = (Boolean) pars.get(SSVarNames.getFlags);
        getCircles       = (Boolean) pars.get(SSVarNames.getCircles);
      }
      
      if(par.clientJSONObj != null){
        entity          = SSUri.get        (par.clientJSONObj.get(SSVarNames.entity).getTextValue());
        
        try{
          getTags            = par.clientJSONObj.get(SSVarNames.getTags).getBooleanValue();
        }catch(Exception error){}
        
        try{
          getOverallRating   = par.clientJSONObj.get(SSVarNames.getOverallRating).getBooleanValue();
        }catch(Exception error){}
        
        try{
          getDiscs        = par.clientJSONObj.get(SSVarNames.getDiscs).getBooleanValue();
        }catch(Exception error){}
        
        try{
          getUEs        = par.clientJSONObj.get(SSVarNames.getUEs).getBooleanValue();
        }catch(Exception error){}
        
        try{
          getThumb        = par.clientJSONObj.get(SSVarNames.getThumb).getBooleanValue();
        }catch(Exception error){}
        
        try{
          getFlags        = par.clientJSONObj.get(SSVarNames.getFlags).getBooleanValue();
        }catch(Exception error){}
        
        try{
          getCircles        = par.clientJSONObj.get(SSVarNames.getCircles).getBooleanValue();
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