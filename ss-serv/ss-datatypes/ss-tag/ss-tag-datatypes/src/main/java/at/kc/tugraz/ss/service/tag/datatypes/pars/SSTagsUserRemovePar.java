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
 package at.kc.tugraz.ss.service.tag.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSTagLabel;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSTagsUserRemovePar extends SSServPar{
  
  public SSUri        resource     = null;
  public SSTagLabel   tagString    = null;
  public SSSpaceEnum  space        = null;
      
  public SSTagsUserRemovePar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        resource   = (SSUri)        pars.get(SSVarU.resource);
        tagString  = (SSTagLabel)   pars.get(SSVarU.tagString);
        space      = (SSSpaceEnum)  pars.get(SSVarU.space);
      }
      
      if(clientPars != null){
        
        try{
          resource   = SSUri.get        (clientPars.get(SSVarU.resource));
        }catch(Exception error){}
        
        try{
          tagString  = SSTagLabel.get   (clientPars.get(SSVarU.tagString));
         }catch(Exception error){} 
        
        try{
          space      = SSSpaceEnum.get  (clientPars.get(SSVarU.space));
        }catch(Exception error){}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
