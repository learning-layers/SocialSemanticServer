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

import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServErrReg;

public class SSLearnEpVersionUpdateCirclePar extends SSServPar{
  
  public SSUri         learnEpCircle     = null;
  public SSLabel       label             = null;
  public Float         xLabel            = null;
  public Float         yLabel            = null;
  public Float         xR                = null;
  public Float         yR                = null;
  public Float         xC                = null;
  public Float         yC                = null;
  
  public SSLearnEpVersionUpdateCirclePar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        learnEpCircle     = (SSUri)    pars.get(SSVarNames.learnEpCircle);
        label             = (SSLabel)  pars.get(SSVarNames.label);
        xLabel            = (Float)    pars.get(SSVarNames.xLabel);
        yLabel            = (Float)    pars.get(SSVarNames.yLabel);
        xR                = (Float)    pars.get(SSVarNames.xR);
        yR                = (Float)    pars.get(SSVarNames.yR);
        xC                = (Float)    pars.get(SSVarNames.xC);
        yC                = (Float)    pars.get(SSVarNames.yC);
      }
      
      if(par.clientJSONObj != null){
        learnEpCircle     = SSUri.get        (par.clientJSONObj.get(SSVarNames.learnEpCircle).getTextValue());
        
        try{
          label             = SSLabel.get      (par.clientJSONObj.get(SSVarNames.label).getTextValue());
        }catch(Exception error){}
        
        try{
          xLabel            = par.clientJSONObj.get(SSVarNames.xLabel).getNumberValue().floatValue();
        }catch(Exception error){}
        
        try{
          yLabel            = par.clientJSONObj.get(SSVarNames.yLabel).getNumberValue().floatValue();
        }catch(Exception error){}
        
        try{
          xR                = par.clientJSONObj.get(SSVarNames.xR).getNumberValue().floatValue();
        }catch(Exception error){}
        
        try{
          yR                = par.clientJSONObj.get(SSVarNames.yR).getNumberValue().floatValue();
        }catch(Exception error){}
        
        try{
          xC                = par.clientJSONObj.get(SSVarNames.xC).getNumberValue().floatValue();
        }catch(Exception error){}
        
        try{
          yC                = par.clientJSONObj.get(SSVarNames.yC).getNumberValue().floatValue();
        }catch(Exception error){}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}