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
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSRecommSplitSamplePar extends SSServPar{

  public String  dataSet         = null;
  public String  sampleName      = null;
  public Integer sampleCount     = null; 
  public Integer userLevel       = null;
  public Integer resourceLevel   = null;
  public Integer tagLevel        = null;
    
  public SSRecommSplitSamplePar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      if(pars != null){
        this.dataSet        = (String)  pars.get(SSVarU.dataSet);
        this.sampleName     = (String)  pars.get(SSVarU.sampleName);
        this.sampleCount    = (Integer) pars.get(SSVarU.sampleCount);
        this.userLevel      = (Integer) pars.get(SSVarU.userLevel);
        this.resourceLevel  = (Integer) pars.get(SSVarU.resourceLevel);
        this.tagLevel       = (Integer) pars.get(SSVarU.tagLevel);
      }
      
      if(clientPars != null){
        this.dataSet        = clientPars.get  (SSVarU.dataSet);
        this.sampleName     = clientPars.get  (SSVarU.sampleName);
        this.sampleCount    = Integer.valueOf (clientPars.get(SSVarU.sampleCount));
        this.userLevel      = Integer.valueOf (clientPars.get(SSVarU.userLevel));
        this.resourceLevel  = Integer.valueOf (clientPars.get(SSVarU.resourceLevel));
        this.tagLevel       = Integer.valueOf (clientPars.get(SSVarU.tagLevel));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
