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
package at.kc.tugraz.ss.activity.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.ArrayList;
import java.util.List;

public class SSActivityAddPar extends SSServPar{

  public SSActivityE            type     = null;
  public List<SSUri>            users            = new ArrayList<SSUri>();
  public List<SSUri>            sourceEntities   = new ArrayList<SSUri>();
  public List<SSUri>            targetEntities   = new ArrayList<SSUri>();
  public List<SSTextComment>    comments         = new ArrayList<SSTextComment>();
  
  public SSActivityAddPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        
        type = (SSActivityE) pars.get(SSVarU.type);
          
        try{
          users             = (List<SSUri>)         pars.get(SSVarU.users);
        }catch(Exception error){}
        
        try{
          sourceEntities     = (List<SSUri>)         pars.get(SSVarU.sourceEntities);
        }catch(Exception error){}
        
        try{
          targetEntities     = (List<SSUri>)         pars.get(SSVarU.targetEntities);
        }catch(Exception error){}
        
        try{
          comments         = (List<SSTextComment>) pars.get(SSVarU.comments);
        }catch(Exception error){}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
