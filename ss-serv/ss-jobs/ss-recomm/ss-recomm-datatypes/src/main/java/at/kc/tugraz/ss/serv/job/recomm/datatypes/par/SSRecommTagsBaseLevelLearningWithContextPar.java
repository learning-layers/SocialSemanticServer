/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.job.recomm.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSRecommTagsBaseLevelLearningWithContextPar extends SSServPar{
  
  public SSUri         forUser    = null;
  public SSUri         entityUri  = null;
  public Integer       maxTags   = 10;
  
  public SSRecommTagsBaseLevelLearningWithContextPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      if(pars != null){
        this.forUser   =  (SSUri)         pars.get(SSVarU.forUser);
        this.entityUri =  (SSUri)         pars.get(SSVarU.entityUri);
        this.maxTags   =  (Integer)       pars.get(SSVarU.maxTags);
      }
      
      if(clientPars != null){
        
        try{
          this.forUser   = SSUri.get         (clientPars.get(SSVarU.forUser));
        }catch(Exception error){}
        
        try{
          this.entityUri = SSUri.get         (clientPars.get(SSVarU.entityUri));
        }catch(Exception error){}
        
        try{
          this.maxTags   = Integer.valueOf   (clientPars.get(SSVarU.maxTags));
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
