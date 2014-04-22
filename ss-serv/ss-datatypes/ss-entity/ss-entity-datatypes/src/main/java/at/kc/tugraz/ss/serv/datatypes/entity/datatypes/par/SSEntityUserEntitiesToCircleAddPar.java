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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.ArrayList;
import java.util.List;

public class SSEntityUserEntitiesToCircleAddPar extends SSServPar{

  public SSUri       circleUri  = null;
  public List<SSUri> entityUris = new ArrayList<SSUri>();
  
  public SSEntityUserEntitiesToCircleAddPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
    
      if(pars != null){
        circleUri        = (SSUri)         pars.get(SSVarU.circleUri);
        entityUris       = (List<SSUri>)   pars.get(SSVarU.entityUris);
      }
      
      if(clientPars != null){
        circleUri        = SSUri.get(clientPars.get(SSVarU.circleUri));
        entityUris       = SSUri.getDistinct(SSStrU.split(clientPars.get(SSVarU.entityUris), SSStrU.comma));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
