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
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircleTypeE;
import java.util.ArrayList;
import java.util.List;

public class SSEntityCircleCreatePar extends SSServPar{

  public List<SSUri>               entityUris   = new ArrayList<SSUri>();
  public List<SSUri>               userUris     = new ArrayList<SSUri>();
  public SSEntityCircleTypeE       circleType   = null;
  public SSLabelStr                label        = null;
  public SSUri                     circleAuthor = null;
  
  public SSEntityCircleCreatePar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
    
      if(pars != null){
        circleType       = (SSEntityCircleTypeE)       pars.get(SSVarU.circleType);
        label            = (SSLabelStr)                pars.get(SSVarU.label);
        entityUris       = (List<SSUri>)               pars.get(SSVarU.entityUris);
        userUris         = (List<SSUri>)               pars.get(SSVarU.userUris);
        circleAuthor     = (SSUri)                     pars.get(SSVarU.circleAuthor);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
