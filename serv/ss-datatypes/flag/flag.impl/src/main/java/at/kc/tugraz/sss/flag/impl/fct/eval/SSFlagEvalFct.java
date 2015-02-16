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
package at.kc.tugraz.sss.flag.impl.fct.eval;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.sss.flag.datatypes.SSFlagE;
import at.kc.tugraz.sss.flag.datatypes.par.SSFlagsUserSetPar;
import java.util.ArrayList;
import sss.serv.eval.datatypes.SSEvalLogE;

public class SSFlagEvalFct{
  
  public static void flagsSet(final SSServPar parA) throws Exception{
    
    try{
      
      if(!SSCoreConf.instGet().getEvalConf().use){
        return;
      }
      
      final SSFlagsUserSetPar par = new SSFlagsUserSetPar(parA);
      
      if(!SSStrU.contains(par.types, SSFlagE.importance)){
        return;
      }
      
      for(SSUri entity : par.entities){
      
        SSServCaller.evalLog(
          par.user,
          null,
          par.user,
          SSEvalLogE.setImportance,
          SSServCaller.entityGet(entity),
          SSStrU.toStr(par.value),
          new ArrayList<>(),
          new ArrayList<>(),
          true);
      }
      
    }catch(Exception error){
      SSServErrReg.reset();
      
      SSLogU.warn("flagsSet eval logs failed");
    }
  }
}