/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.service.disc.impl.fct.ue;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSUEEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAddPar;

public class SSDiscUEFct {

  public static void discCreate(final SSDiscEntryAddPar par, final SSUri discUri){
    
    if(!par.saveUE){
      return;
    }
    
    try{
      
      SSServCaller.ueAdd(
        par.user,
        par.target,
        SSUEEnum.discussEntity,
        SSUri.toStr(discUri),
        par.shouldCommit);
      
      SSServCaller.ueAdd(
        par.user,
        discUri,
        SSUEEnum.newDiscussionByDiscussEntity,
        SSUri.toStr(par.target),
        par.shouldCommit);
      
    }catch(Exception error){
      SSLogU.warn("storing ue failed");
    }
  }

  public static void discEntryAdd(final SSDiscEntryAddPar par, final SSUri discEntryUri){

     if(!par.saveUE){
      return;
    }
    
    try{
      
      SSServCaller.ueAdd(
        par.user,
        par.disc,
        SSUEEnum.addDiscussionComment,
        SSStrU.empty,
        par.shouldCommit);
      
    }catch(Exception error){
      SSLogU.warn("storing ue failed");
    }
  }
}
