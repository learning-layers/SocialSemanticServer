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

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

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
        learnEpCircle     = (SSUri)    pars.get(SSVarU.learnEpCircle);
        label             = (SSLabel)  pars.get(SSVarU.label);
        xLabel            = (Float)    pars.get(SSVarU.xLabel);
        yLabel            = (Float)    pars.get(SSVarU.yLabel);
        xR                = (Float)    pars.get(SSVarU.xR);
        yR                = (Float)    pars.get(SSVarU.yR);
        xC                = (Float)    pars.get(SSVarU.xC);
        yC                = (Float)    pars.get(SSVarU.yC);
      }
      
//      if(clientPars != null){
//        learnEpCircle     = SSUri.get        (clientPars.get(SSVarU.learnEpCircle));
//        
//        try{
//          label             = SSLabel.get      (clientPars.get(SSVarU.label));
//        }catch(Exception error){}
//        
//        try{
//          xLabel            = Float.parseFloat (clientPars.get(SSVarU.xLabel));
//        }catch(Exception error){}
//        
//        try{
//          yLabel            = Float.parseFloat (clientPars.get(SSVarU.yLabel));
//        }catch(Exception error){}
//        
//        try{
//          xR                = Float.parseFloat (clientPars.get(SSVarU.xR));
//        }catch(Exception error){}
//        
//        try{
//          yR                = Float.parseFloat (clientPars.get(SSVarU.yR));
//        }catch(Exception error){}
//        
//        try{
//          xC                = Float.parseFloat (clientPars.get(SSVarU.xC));
//        }catch(Exception error){}
//        
//        try{
//          yC                = Float.parseFloat (clientPars.get(SSVarU.yC));
//        }catch(Exception error){}
//      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}