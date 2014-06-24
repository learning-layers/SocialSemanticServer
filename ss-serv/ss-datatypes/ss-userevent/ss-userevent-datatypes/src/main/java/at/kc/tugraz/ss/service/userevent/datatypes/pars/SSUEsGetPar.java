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
 package at.kc.tugraz.ss.service.userevent.datatypes.pars;

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSUEsGetPar extends SSServPar{
  
  public SSUri           forUser        = null;
  public SSUri           entity         = null;
  public SSUEE           type           = null;
  public Long            startTime      = null;
  public Long            endTime        = null;
  
  public SSUEsGetPar(SSServPar par) throws Exception{

    super(par);
    
    try{
      
      if(pars != null){
        forUser    = (SSUri)    pars.get(SSVarU.forUser);
        entity     = (SSUri)    pars.get(SSVarU.entity);
        type       = (SSUEE)    pars.get(SSVarU.type);
        startTime  = (Long)     pars.get(SSVarU.startTime);
        endTime    = (Long)     pars.get(SSVarU.endTime);
      }
      
      if(clientPars != null){
        
        try{
          forUser    = SSUri.get    (clientPars.get(SSVarU.forUser));
        }catch(Exception error){}
        
        try{
          entity   = SSUri.get    (clientPars.get(SSVarU.entity));
        }catch(Exception error){}
        
        try{
          type  = SSUEE.get (clientPars.get(SSVarU.type));
        }catch(Exception error){}
        
        try{
          startTime  = Long.valueOf (clientPars.get(SSVarU.startTime));
        }catch(Exception error){}
        
        try{
          endTime  = Long.valueOf (clientPars.get(SSVarU.endTime));
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}