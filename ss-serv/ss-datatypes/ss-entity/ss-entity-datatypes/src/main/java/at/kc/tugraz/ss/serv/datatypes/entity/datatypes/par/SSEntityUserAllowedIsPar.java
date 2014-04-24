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

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityRightTypeE;

public class SSEntityUserAllowedIsPar extends SSServPar{

  public SSUri               entityUri    = null;
  public SSEntityRightTypeE  accessRight  = null;
  
  public SSEntityUserAllowedIsPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
    
      if(pars != null){
        entityUri       = (SSUri)               pars.get(SSVarU.entityUri);
        accessRight     = (SSEntityRightTypeE)  pars.get(SSVarU.accessRight);
      }
      
      if(clientPars != null){
        entityUri       = SSUri.get              (clientPars.get(SSVarU.entityUri));
        accessRight     = SSEntityRightTypeE.get (clientPars.get(SSVarU.accessRight));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
