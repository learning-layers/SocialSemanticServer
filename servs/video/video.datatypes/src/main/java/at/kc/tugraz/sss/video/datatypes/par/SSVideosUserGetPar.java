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
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;

public class SSVideosUserGetPar extends SSServPar{
  
  public SSUri forEntity  = null;
  public SSUri forUser    = null;
  
  public SSVideosUserGetPar(
    final SSServOpE op,
    final String  key,
    final SSUri   user, 
    final SSUri   forEntity,
    final SSUri   forUser){
    
    super(op, key, user);
    
    this.forEntity = forEntity;
    this.forUser   = forUser;
  }
  
  public SSVideosUserGetPar(final SSServPar par) throws Exception{
    super(par);
    
    try{
      
      if(pars != null){
        forEntity           = (SSUri)   pars.get(SSVarNames.forEntity);
        forUser             = (SSUri)   pars.get(SSVarNames.forUser);
      }
      
      if(par.clientJSONObj != null){
        
         try{
          forUser          = SSUri.get        (par.clientJSONObj.get(SSVarNames.forUser).getTextValue());
        }catch(Exception error){}
        
        try{
          forEntity          = SSUri.get        (par.clientJSONObj.get(SSVarNames.forEntity).getTextValue());
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
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }
}