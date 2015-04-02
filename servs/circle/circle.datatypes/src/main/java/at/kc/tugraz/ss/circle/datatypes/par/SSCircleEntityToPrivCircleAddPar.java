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
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServErrReg;

public class SSCircleEntityToPrivCircleAddPar extends SSServPar{

  public SSUri           entity       = null;
  public SSEntityE       type         = null;
  public SSLabel         label        = null;
  public SSTextComment   description  = null;
  public Long            creationTime = null;
  
  public SSCircleEntityToPrivCircleAddPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
    
      if(pars != null){
        entity       = (SSUri)         pars.get(SSVarU.entity);
        type         = (SSEntityE)     pars.get(SSVarU.type);
        label        = (SSLabel)       pars.get(SSVarU.label);
        description  = (SSTextComment) pars.get(SSVarU.description);
        creationTime = (Long)          pars.get(SSVarU.creationTime);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}