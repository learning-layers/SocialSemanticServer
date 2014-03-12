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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSTagLabel;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.ArrayList;
import java.util.List;

public class SSTagsAddPar extends SSServPar{
  
  public SSUri             resource       = null;
  public List<SSTagLabel>  tagStrings     = new ArrayList<SSTagLabel>();
  public SSSpaceEnum       space          = null;
  
  public SSTagsAddPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        this.tagStrings.addAll((List<SSTagLabel>)  pars.get(SSVarU.tagStrings));
        this.resource     =  (SSUri)               pars.get(SSVarU.resource);
        this.space        =  (SSSpaceEnum)         pars.get(SSVarU.space);
      }
      
      if(clientPars != null){
        
        resource   = SSUri.get        (clientPars.get(SSVarU.resource));
        space      = SSSpaceEnum.get  (clientPars.get(SSVarU.space));
        
        tagStrings.addAll(SSTagLabel.getDistinct(SSStrU.split(clientPars.get(SSVarU.tagStrings), SSStrU.comma)));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
