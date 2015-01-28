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
package at.kc.tugraz.sss.appstacklayout.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSAppStackLayoutCreatePar extends SSServPar{
  
  public String              uuid             = null;
  public SSUri               link             = null;
  public SSUri               app              = null;
  public SSLabel             label            = null;
  public SSTextComment       description      = null;
  
  public SSAppStackLayoutCreatePar(
    final SSMethU        op,
    final String         key,
    final SSUri          user,
    final String         uuid,
    final SSUri          link,
    final SSUri          app,
    final SSLabel        label,
    final SSTextComment  description){
    
    super(op, key, user);
    
    this.uuid           = uuid;
    this.link           = link;
    this.app            = app;
    this.label          = label;
    this.description    = description;
  }
  
  public SSAppStackLayoutCreatePar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        
        uuid              = (String)          pars.get(SSVarU.uuid);
        link              = (SSUri)           pars.get(SSVarU.link);
        label             = (SSLabel)         pars.get(SSVarU.label);
        description       = (SSTextComment)   pars.get(SSVarU.description);
      }
      
      if(par.clientJSONObj != null){
        
        try{
          uuid =  par.clientJSONObj.get(SSVarU.uuid).getTextValue();
        }catch(Exception error){}
        
        try{
          link =  SSUri.get(par.clientJSONObj.get(SSVarU.link).getTextValue());
        }catch(Exception error){}
        
        try{
          app =  SSUri.get(par.clientJSONObj.get(SSVarU.app).getTextValue());
        }catch(Exception error){}
        
        try{
          label =  SSLabel.get      (par.clientJSONObj.get(SSVarU.label).getTextValue());
        }catch(Exception error){}
        
        try{
          description =  SSTextComment.get      (par.clientJSONObj.get(SSVarU.description).getTextValue());
        }catch(Exception error){}
      
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getApp(){
    return SSStrU.removeTrailingSlash(app);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public String getDescription(){
    return SSStrU.toStr(description);
  }
  
  public String getLink(){
    return SSStrU.removeTrailingSlash(link);
  }
}