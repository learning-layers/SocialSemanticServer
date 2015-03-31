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
package at.kc.tugraz.ss.circle.datatypes.par;

import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServErrReg;

public class SSCircleUserAddPar extends SSServPar{

  public SSUri circle = null;
  
  public SSCircleUserAddPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
    
      if(pars != null){
        circle       = (SSUri)        pars.get(SSVarU.circle);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
