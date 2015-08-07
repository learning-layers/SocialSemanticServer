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
package at.kc.tugraz.ss.service.disc.impl.fct.activity;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.tugraz.sss.serv.SSLogU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAddPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryAddRet;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;

public class SSDiscActivityFct{
  
  public static void discEntryAdd(
    final SSDiscEntryAddPar par,
    final SSDiscEntryAddRet ret, 
    final Boolean           shouldCommit) throws Exception{
    
    try{
      
      if(par.addNewDisc){
        
        for(SSUri target : par.targets){
          
          ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
            new SSActivityAddPar(
              null,
              null,
              par.user,
              SSActivityE.discussEntity,
              target, //entity
              null, //users
              SSUri.asListWithoutNullAndEmpty(ret.disc), //entities
              null, //comments
              null,
              shouldCommit));
        }
      }
      
      if(par.entry != null){
        
        ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
          new SSActivityAddPar(
            null,
            null,
            par.user,
            SSActivityE.addDiscEntry,
            ret.disc,
            null, //users,
            SSUri.asListWithoutNullAndEmpty(ret.entry), //entities
            SSTextComment.asListWithoutNullAndEmpty(par.entry), //comment
            null,
            shouldCommit));
      }
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}