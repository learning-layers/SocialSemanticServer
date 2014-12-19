/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSEntityUserCirclesGetPar extends SSServPar{

  public SSUri   forUser             = null;
  public Boolean withSystemCircles   = false;
  
  public SSEntityUserCirclesGetPar(
    final SSMethU  op,
    final String   key,
    final SSUri    user,
    final SSUri    forUser,
    final Boolean  withSystemCircles) throws Exception{
    
    super(op, key, user);
    
    this.withSystemCircles = withSystemCircles;
    this.forUser           = forUser;
  }
  
  public SSEntityUserCirclesGetPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        withSystemCircles = (Boolean) pars.get(SSVarU.withSystemCircles);
        forUser           = (SSUri)   pars.get(SSVarU.forUser);
      }
      
      if(par.clientJSONObj != null){
        withSystemCircles = false;

        try{
          forUser                    = SSUri.get(par.clientJSONObj.get(SSVarU.forUser).getTextValue());
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
}
