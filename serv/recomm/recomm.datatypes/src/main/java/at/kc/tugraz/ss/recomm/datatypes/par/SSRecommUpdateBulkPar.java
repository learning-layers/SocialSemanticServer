/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.recomm.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSRecommUpdateBulkPar extends SSServPar{
  
  public SSLabel  fileName     = null;
  
  public SSRecommUpdateBulkPar(
    final SSMethU             op,
    final String              key,
    final SSUri               user,
    final SSLabel             fileName){
    
    super(op, key, user);
    
    this.fileName = fileName;
  }
  
  public SSRecommUpdateBulkPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        fileName      = (SSLabel)    pars.get(SSVarU.fileName);
      }
      
      if(par.clientJSONObj != null){
        fileName    = SSLabel.get (par.clientJSONObj.get(SSVarU.fileName).getTextValue());
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getFileName(){
    return SSStrU.toStr(fileName);
  }
}
