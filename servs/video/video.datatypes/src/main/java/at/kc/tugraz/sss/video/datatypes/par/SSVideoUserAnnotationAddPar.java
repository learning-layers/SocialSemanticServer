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
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;

public class SSVideoUserAnnotationAddPar extends SSServPar{
  
  public SSUri               video              = null;
  public Long                timePoint          = null;
  public Float               x                  = null;
  public Float               y                  = null;
  public SSLabel             label              = null;
  public SSTextComment       description        = null;
  
  public SSVideoUserAnnotationAddPar(
    final SSServOpE              op,
    final String               key,
    final SSUri                user,
    final SSUri                video,
    final Long                 timePoint,
    final Float                x,
    final Float                y,
    final SSLabel              label,
    final SSTextComment        description){
    
    super(op, key, user);
    
    this.video              = video;
    this.timePoint          = timePoint;
    this.x                  = x;
    this.y                  = y;
    this.label              = label;
    this.description        = description;
  }
  
  public SSVideoUserAnnotationAddPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        video               = (SSUri)          pars.get(SSVarU.video);
        timePoint           = (Long)           pars.get(SSVarU.timePoint);
        x                   = (Float)          pars.get(SSVarU.x);
        y                   = (Float)          pars.get(SSVarU.y);
        label               = (SSLabel)        pars.get(SSVarU.label);
        description         = (SSTextComment)  pars.get(SSVarU.description);
      }
      
      if(par.clientJSONObj != null){
        
        video               = SSUri.get(par.clientJSONObj.get(SSVarU.video).getTextValue());
        timePoint           =  par.clientJSONObj.get(SSVarU.timePoint).getLongValue();
        x                   =  par.clientJSONObj.get(SSVarU.x).getNumberValue().floatValue();
        y                   =  par.clientJSONObj.get(SSVarU.y).getNumberValue().floatValue();
        
        try{
          label =  SSLabel.get(par.clientJSONObj.get(SSVarU.label).getTextValue());
        }catch(Exception error){}
        
        try{
          description =  SSTextComment.get(par.clientJSONObj.get(SSVarU.description).getTextValue());
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  
  public String getVideo(){
    return SSStrU.removeTrailingSlash(video);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  public String getDescription(){
    return SSStrU.toStr(description);
  }
}