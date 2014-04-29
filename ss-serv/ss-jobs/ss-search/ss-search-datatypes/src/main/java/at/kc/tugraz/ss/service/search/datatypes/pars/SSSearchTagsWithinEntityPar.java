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
 package at.kc.tugraz.ss.service.search.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSTagLabel;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.ArrayList;
import java.util.List;

public class SSSearchTagsWithinEntityPar extends SSServPar{
  
  public SSUri             entityUri = null;
  public List<SSTagLabel>  tags      = new ArrayList<SSTagLabel>();
    
  public SSSearchTagsWithinEntityPar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      if(pars != null){
        entityUri        = (SSUri)            pars.get(SSVarU.entityUri);
        tags             = (List<SSTagLabel>) pars.get(SSVarU.tags);
      }
      
      if(clientPars != null){
        entityUri        = SSUri.get             (clientPars.get(SSVarU.entityUri));
        tags             = SSTagLabel.getDistinct(SSStrU.split(clientPars.get(SSVarU.tags), SSStrU.comma));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
