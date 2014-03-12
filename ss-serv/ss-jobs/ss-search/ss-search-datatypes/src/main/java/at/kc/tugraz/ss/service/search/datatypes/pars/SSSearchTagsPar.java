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
 package at.kc.tugraz.ss.service.search.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSTagLabel;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.List;

public class SSSearchTagsPar extends SSServPar{
  
  public List<SSTagLabel> tags             = null;
  public String            searchOp         = null;
  public int               maxResultsPerTag = 0;
    
  public SSSearchTagsPar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      if(pars != null){
        searchOp         = (String)            pars.get(SSVarU.searchOp);
        tags             = (List<SSTagLabel>) pars.get(SSVarU.tags);
        maxResultsPerTag = (Integer)           pars.get(SSVarU.maxResultsPerTag);
      }
      
      if(clientPars != null){
        searchOp         = (String) clientPars.get(SSVarU.searchOp);
        tags             = SSTagLabel.getDistinct(SSStrU.splitDistinct((String)clientPars.get(SSVarU.tags), SSStrU.comma));
        maxResultsPerTag = Integer.valueOf((String)clientPars.get(SSVarU.maxResultsPerTag));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
