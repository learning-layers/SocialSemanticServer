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

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.serv.datatypes.err.SSServerServNotAvailableErr;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesDeletePar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryChangePosPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryDeletePar;
import java.util.ArrayList;
import java.util.List;

public class SSCollActivityFct{
  
  public static void removeCollEntry(final SSCollUserEntryDeletePar par) throws Exception{
    
    try{
      
      final List<SSUri> eventEntities = new ArrayList<>();
      
      eventEntities.add(par.coll);
      eventEntities.add(par.entry);

      SSServCaller.activityAdd(
        par.user,
        SSActivityE.removeCollEntry,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(eventEntities),
        SSTextComment.asListWithoutNullAndEmpty(),
        false);
      
    }catch(SSServerServNotAvailableErr error){
      SSLogU.warn("activityAdd failed | service down");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void removeCollEntries(
    final SSCollUserEntriesDeletePar par) throws Exception{
    
    try{
      
      final List<SSUri> eventEntities = new ArrayList<>();
      
      eventEntities.add   (par.coll);
      eventEntities.addAll(par.entries);

      SSServCaller.activityAdd(
        par.user,
        SSActivityE.removeCollEntry,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(eventEntities),
        SSTextComment.asListWithoutNullAndEmpty(),
        false);
      
    }catch(SSServerServNotAvailableErr error){
      SSLogU.warn("activityAdd failed | service down");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void addCollEntry(
    final SSCollUserEntryAddPar par) throws Exception{
    
    try{
      
      final List<SSUri> eventEntities = new ArrayList<>();
      
      eventEntities.add   (par.coll);
      eventEntities.add   (par.entry);

      SSServCaller.activityAdd(
        par.user,
        SSActivityE.addCollEntry,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(eventEntities),
        SSTextComment.asListWithoutNullAndEmpty(),
        false);
      
    }catch(SSServerServNotAvailableErr error){
      SSLogU.warn("activityAdd failed | service down");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addCollEntries(
    final SSCollUserEntriesAddPar par) throws Exception{
    
    try{
      
      final List<SSUri> eventEntities = new ArrayList<>();
      
      eventEntities.add   (par.coll);
      eventEntities.addAll(par.entries);

      SSServCaller.activityAdd(
        par.user,
        SSActivityE.addCollEntry,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(eventEntities),
        SSTextComment.asListWithoutNullAndEmpty(),
        false);
      
    }catch(SSServerServNotAvailableErr error){
      SSLogU.warn("activityAdd failed | service down");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void changeCollEntryPos(
    final SSCollUserEntryChangePosPar par) throws Exception{
    
     try{
      
      final List<SSUri> eventEntities = new ArrayList<>();
      
      final List<SSUri>    collEntries = new ArrayList<>();
      Integer              counter     = 0;
      
       while(counter < par.order.size()){
        collEntries.add(SSUri.get(par.order.get(counter++)));
        counter++;
      }
      
      eventEntities.add   (par.coll);
      eventEntities.addAll(collEntries);

      SSServCaller.activityAdd(
        par.user,
        SSActivityE.changeCollEntryPos,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(eventEntities),
        SSTextComment.asListWithoutNullAndEmpty(),
        false);
      
    }catch(SSServerServNotAvailableErr error){
      SSLogU.warn("activityAdd failed | service down");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
  }
}
