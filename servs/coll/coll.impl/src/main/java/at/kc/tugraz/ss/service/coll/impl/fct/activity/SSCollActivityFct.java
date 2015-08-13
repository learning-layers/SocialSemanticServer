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
package at.kc.tugraz.ss.service.coll.impl.fct.activity;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.tugraz.sss.serv.SSLogU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesDeletePar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;

public class SSCollActivityFct{
  
  public static void removeCollEntries(
    final SSCollUserEntriesDeletePar par) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          par.user,
          SSActivityE.removeCollEntry,
          par.coll,
          SSUri.asListWithoutNullAndEmpty(),
          par.entries,
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          par.shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addCollEntry(
    final SSCollUserEntryAddPar par) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          par.user,
          SSActivityE.addCollEntry,
          par.coll,
          SSUri.asListWithoutNullAndEmpty(),
          SSUri.asListWithoutNullAndEmpty(par.entry),
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          par.shouldCommit));
      
    }catch(SSErr error){
      
      switch(error.code){
        case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
        default: SSServErrReg.regErrThrow(error);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addCollEntries(
    final SSCollUserEntriesAddPar par) throws Exception{
    
    try{
      
      ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityAdd(
        new SSActivityAddPar(
          par.user,
          SSActivityE.addCollEntry,
          par.coll,
          SSUri.asListWithoutNullAndEmpty(),
          par.entries,
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          par.shouldCommit));
      
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
