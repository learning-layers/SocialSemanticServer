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

public class SSEntityUserCircleGetPar extends SSServPar{
  
  public SSUri   circle                     = null;
  public SSUri   forUser                    = null;
  public Boolean withSystemCircles          = false;
  
  public SSEntityUserCircleGetPar(){}
  
  public SSEntityUserCircleGetPar(
    final SSMethU  op,
    final String   key,
    final SSUri    user,
    final SSUri    forUser,
    final SSUri    circle,
    final Boolean  withSystemCircles) throws Exception{
    
    super(op, key, user);
    
    this.circle            = circle;
    this.forUser           = forUser;
    this.withSystemCircles = withSystemCircles;
  }
  
  public SSEntityUserCircleGetPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        circle               = (SSUri)   pars.get(SSVarU.circle);
        forUser              = (SSUri)   pars.get(SSVarU.forUser);
        withSystemCircles    = (Boolean) pars.get(SSVarU.withSystemCircles);
      }
      
      if(par.clientJSONObj != null){
        
        circle            = SSUri.get(par.clientJSONObj.get(SSVarU.circle).getTextValue());
        
        try{
          forUser = SSUri.get(par.clientJSONObj.get(SSVarU.forUser).getTextValue());
        }catch(Exception error){}
        
        withSystemCircles = false;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
   /* json getters */
  public String getCircle() throws Exception{
    return SSStrU.removeTrailingSlash(circle);
  }
  
  public String getForUser() throws Exception{
    return SSStrU.removeTrailingSlash(forUser);
  }
}