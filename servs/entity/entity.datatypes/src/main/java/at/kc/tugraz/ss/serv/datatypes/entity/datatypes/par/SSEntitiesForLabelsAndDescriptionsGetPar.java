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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par;

import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import java.util.ArrayList;
import java.util.List;

public class SSEntitiesForLabelsAndDescriptionsGetPar extends SSServPar{
  
  public List<String> requireds = new ArrayList<>();
  public List<String> absents   = new ArrayList<>();
  public List<String> eithers   = new ArrayList<>();
    
  public SSEntitiesForLabelsAndDescriptionsGetPar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        requireds       = (List<String>)            pars.get(SSVarU.requireds);
        absents         = (List<String>)            pars.get(SSVarU.absents);
        eithers         = (List<String>)            pars.get(SSVarU.eithers);
      }
      
    }catch(Exception error){
      throw new SSErr(SSErrE.servParCreationFailed);
    }
  }
}