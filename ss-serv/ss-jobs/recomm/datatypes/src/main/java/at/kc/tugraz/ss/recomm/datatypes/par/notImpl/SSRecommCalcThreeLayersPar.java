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

package at.kc.tugraz.ss.recomm.datatypes.par.notImpl;

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSRecommCalcThreeLayersPar extends SSServPar{

  public String  sampleName   = null;
  public Integer sampleCount  = null;
  public String  prefix       = null;
  public Boolean predictTags  = null;
    
  public SSRecommCalcThreeLayersPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      if(pars != null){
        this.sampleName   = (String)  pars.get(SSVarU.sampleName);
        this.sampleCount  = (Integer) pars.get(SSVarU.sampleCount);
        this.prefix       = (String)  pars.get(SSVarU.prefix);
        this.predictTags  = (Boolean) pars.get(SSVarU.predictTags);
      }
      
      if(clientPars != null){
        this.sampleName   = clientPars.get  (SSVarU.sampleName);
        this.sampleCount  = Integer.valueOf (clientPars.get(SSVarU.sampleCount));
        this.prefix       = clientPars.get  (SSVarU.prefix);
        this.predictTags  = Boolean.valueOf (clientPars.get(SSVarU.predictTags));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}