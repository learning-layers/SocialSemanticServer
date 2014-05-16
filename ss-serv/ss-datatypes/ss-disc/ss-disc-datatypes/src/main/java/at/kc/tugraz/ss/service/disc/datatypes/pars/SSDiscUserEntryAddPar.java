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
 package at.kc.tugraz.ss.service.disc.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.service.disc.datatypes.enums.SSDiscE;

public class SSDiscUserEntryAddPar extends SSServPar{
  
  public SSUri               discUri      = null;
  public SSUri               targetUri    = null;
  public SSTextComment       content      = null;
  public Boolean             addNewDisc   = null;
  public SSDiscE             discType     = null;
  public SSLabel             discLabel    = null;
            
  public SSDiscUserEntryAddPar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        discUri     = (SSUri)              pars.get(SSVarU.discUri);
        targetUri   = (SSUri)              pars.get(SSVarU.targetUri);
        content     = (SSTextComment)      pars.get(SSVarU.content);
        addNewDisc  = (Boolean)            pars.get(SSVarU.addNewDisc);
        discType    = (SSDiscE)            pars.get(SSVarU.discType);
        discLabel   = (SSLabel)            pars.get(SSVarU.discLabel);
      }
      
      if(clientPars != null){
        
        try{
          targetUri      = SSUri.get             (clientPars.get(SSVarU.targetUri));
        }catch(Exception error){}
        
        try{
          discUri        = SSUri.get             (clientPars.get(SSVarU.discUri));
        }catch(Exception error){}
        
        try{
          addNewDisc  = Boolean.valueOf       (clientPars.get(SSVarU.addNewDisc));
        }catch(Exception error){}
        
        try{
          content     = SSTextComment.get(clientPars.get(SSVarU.content));
        }catch(Exception error){}
        
        try{
          discType     = SSDiscE.get(clientPars.get(SSVarU.discType));
        }catch(Exception error){}
        
        try{
          discLabel     = SSLabel.get(clientPars.get(SSVarU.discLabel));
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
