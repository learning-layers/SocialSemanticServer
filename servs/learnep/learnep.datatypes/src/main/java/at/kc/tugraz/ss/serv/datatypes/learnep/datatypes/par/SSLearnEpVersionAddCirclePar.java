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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par;

import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServErrReg;

public class SSLearnEpVersionAddCirclePar extends SSServPar{
  
  public SSUri      learnEpVersion    = null;
  public SSLabel    label             = null;
  public Float      xLabel            = null;
  public Float      yLabel            = null;
  public Float      xR                = null;
  public Float      yR                = null;
  public Float      xC                = null;
  public Float      yC                = null;
  
  public SSLearnEpVersionAddCirclePar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        learnEpVersion    = (SSUri)   pars.get(SSVarU.learnEpVersion);
        label             = (SSLabel)  pars.get(SSVarU.label);
        xLabel            = (Float)    pars.get(SSVarU.xLabel);
        yLabel            = (Float)    pars.get(SSVarU.yLabel);
        xR                = (Float)    pars.get(SSVarU.xR);
        yR                = (Float)    pars.get(SSVarU.yR);
        xC                = (Float)    pars.get(SSVarU.xC);
        yC                = (Float)    pars.get(SSVarU.yC);
      }
      
      if(par.clientJSONObj != null){
        learnEpVersion    = SSUri.get        (par.clientJSONObj.get(SSVarU.learnEpVersion).getTextValue());
        label             = SSLabel.get      (par.clientJSONObj.get(SSVarU.label).getTextValue());
        xLabel            = par.clientJSONObj.get(SSVarU.xLabel).getNumberValue().floatValue();
        yLabel            = par.clientJSONObj.get(SSVarU.yLabel).getNumberValue().floatValue();
        xR                = par.clientJSONObj.get(SSVarU.xR).getNumberValue().floatValue();
        yR                = par.clientJSONObj.get(SSVarU.yR).getNumberValue().floatValue();
        xC                = par.clientJSONObj.get(SSVarU.xC).getNumberValue().floatValue();
        yC                = par.clientJSONObj.get(SSVarU.yC).getNumberValue().floatValue();
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}