/**
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
package at.kc.tugraz.sss.video.datatypes.par;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;

public class SSVideoUserAddPar extends SSServPar{
  
  public String                uuid             = null;
  public SSUri                 link             = null;
  public SSUri                 forEntity        = null;
  public String                genre            = null;
  public SSLabel               label            = null;
  public SSTextComment         description      = null;
  public Long                  creationTime     = null;
  public Double                latitude         = null;
  public Double                longitude        = null;
  public Float                 accuracy         = null;

  public SSVideoUserAddPar(
    final SSServOpE        op,
    final String         key,
    final SSUri          user,
    final String         uuid,
    final SSUri          link,
    final SSUri          forEntity,
    final String         genre,
    final SSLabel        label,
    final SSTextComment  description,
    final Long           creationTime,
    final Double         latitude,
    final Double         longitude,
    final Float          accuracy){
    
    super(op, key, user);
    
    this.uuid           = uuid;
    this.link           = link;
    this.forEntity      = forEntity;
    this.genre          = genre;
    this.label          = label;
    this.description    = description;
    this.creationTime   = creationTime;
    this.latitude       = latitude;
    this.longitude      = longitude;
    this.accuracy       = accuracy;
  }
  
  public SSVideoUserAddPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        
        uuid              = (String)          pars.get(SSVarNames.uuid);
        link              = (SSUri)           pars.get(SSVarNames.link);
        forEntity         = (SSUri)           pars.get(SSVarNames.forEntity);
        genre             = (String)          pars.get(SSVarNames.genre);
        label             = (SSLabel)         pars.get(SSVarNames.label);
        description       = (SSTextComment)   pars.get(SSVarNames.description);
        creationTime      = (Long)            pars.get(SSVarNames.creationTime);
        latitude          = (Double)          pars.get(SSVarNames.latitude);
        longitude         = (Double)          pars.get(SSVarNames.longitude);
        accuracy          = (Float)           pars.get(SSVarNames.accuracy);
      }
      
      if(par.clientJSONObj != null){
        
        try{
          uuid =  par.clientJSONObj.get(SSVarNames.uuid).getTextValue();
        }catch(Exception error){}
        
        try{
          link =  SSUri.get(par.clientJSONObj.get(SSVarNames.link).getTextValue());
        }catch(Exception error){}
        
        try{
          forEntity =  SSUri.get(par.clientJSONObj.get(SSVarNames.forEntity).getTextValue());
        }catch(Exception error){}
        
        try{
          genre =  par.clientJSONObj.get(SSVarNames.genre).getTextValue();
        }catch(Exception error){}
        
        try{
          label =  SSLabel.get      (par.clientJSONObj.get(SSVarNames.label).getTextValue());
        }catch(Exception error){}
        
        try{
          description =  SSTextComment.get      (par.clientJSONObj.get(SSVarNames.description).getTextValue());
        }catch(Exception error){}
        
        try{
          creationTime =  par.clientJSONObj.get(SSVarNames.creationTime).getLongValue();
        }catch(Exception error){}
      
        try{
          latitude =  par.clientJSONObj.get(SSVarNames.latitude).getDoubleValue();
        }catch(Exception error){}
        
        try{
          longitude =  par.clientJSONObj.get(SSVarNames.longitude).getDoubleValue();
        }catch(Exception error){}
        
        try{
          accuracy =  par.clientJSONObj.get(SSVarNames.accuracy).getNumberValue().floatValue();
        }catch(Exception error){}
        
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getForEntity(){
    return SSStrU.removeTrailingSlash(forEntity);
  }
  
  public String getLink(){
    return SSStrU.removeTrailingSlash(link);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public String getDescription(){
    return SSStrU.toStr(description);
  }
}