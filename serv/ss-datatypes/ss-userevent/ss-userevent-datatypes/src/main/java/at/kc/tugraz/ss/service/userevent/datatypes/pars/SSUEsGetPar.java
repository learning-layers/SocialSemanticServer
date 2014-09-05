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
package at.kc.tugraz.ss.service.userevent.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "uesGetPar request parameter")
public class SSUEsGetPar extends SSServPar{
  
  @ApiModelProperty(
    required = false,
    value = "user to retrieve user events for")
  public SSUri           forUser        = null;
  
  @XmlElement
  public void setForUser(final String forUser) throws Exception{
    this.forUser = SSUri.get(forUser);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entity to retrieve user events for")
  public SSUri           entity         = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "user event type to retrieve")
  public SSUEE           type           = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "start timestamp for retrieving user events for")
  public Long            startTime      = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "end timestamp for retrieving user events for")
  public Long            endTime        = null;
  
  public SSUEsGetPar(){}
  
  public SSUEsGetPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        forUser    = (SSUri)    pars.get(SSVarU.forUser);
        entity     = (SSUri)    pars.get(SSVarU.entity);
        type       = (SSUEE)    pars.get(SSVarU.type);
        startTime  = (Long)     pars.get(SSVarU.startTime);
        endTime    = (Long)     pars.get(SSVarU.endTime);
      }
      
      if(par.clientJSONObj != null){
        
        try{
          forUser    = SSUri.get    (par.clientJSONObj.get(SSVarU.forUser).getTextValue());
        }catch(Exception error){}
        
        try{
          entity   = SSUri.get    (par.clientJSONObj.get(SSVarU.entity).getTextValue());
        }catch(Exception error){}
        
        try{
          type  = SSUEE.get (par.clientJSONObj.get(SSVarU.type).getTextValue());
        }catch(Exception error){}
        
        try{
          startTime  = par.clientJSONObj.get(SSVarU.startTime).getLongValue();
        }catch(Exception error){}
        
        try{
          endTime  = par.clientJSONObj.get(SSVarU.endTime).getLongValue();
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
  
  public String getType(){
    return SSStrU.toStr(type);
  }
}