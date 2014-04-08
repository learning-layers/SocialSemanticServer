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
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSUEEnum;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSUEsGetPar extends SSServPar{
  
  public SSUri           resource       = null;
  public SSUEEnum        eventType      = null;
  public Long            startTime      = null;
  public Long            endTime        = null;
  public SSUri           forUser        = null;
  
  public SSUEsGetPar(SSServPar par) throws Exception{

    super(par);
    
    try{
      
      if(pars != null){
        resource   = (SSUri)    pars.get(SSVarU.resource);
        eventType  = (SSUEEnum) pars.get(SSVarU.eventType);
        startTime  = (Long)     pars.get(SSVarU.startTime);
        forUser    = (SSUri)    pars.get(SSVarU.forUser);
        
        try{
          endTime  = (Long)     pars.get(SSVarU.endTime);
        }catch(Exception error){}
      }
      
      if(clientPars != null){
        
        try{
          forUser    = SSUri.get    (clientPars.get(SSVarU.forUser));
        }catch(Exception error){}
        
        try{
          resource   = SSUri.get    ((String)clientPars.get(SSVarU.resource));
        }catch(Exception error){}
        
        try{
          eventType  = SSUEEnum.get ((String)clientPars.get(SSVarU.eventType));
        }catch(Exception error){}
        
        try{
          startTime  = Long.valueOf ((String)clientPars.get(SSVarU.startTime));
        }catch(Exception error){}
        
        try{
          endTime  = Long.valueOf ((String)clientPars.get(SSVarU.endTime));
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}