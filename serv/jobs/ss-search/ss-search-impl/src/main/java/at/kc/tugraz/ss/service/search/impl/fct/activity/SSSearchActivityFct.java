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
package at.kc.tugraz.ss.service.search.impl.fct.activity;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.activity.datatypes.SSActivityContent;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityContentE;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchPar;
import java.util.ArrayList;
import java.util.List;
import sss.serv.err.datatypes.SSErr;

public class SSSearchActivityFct{

  public static void search(
    final SSSearchPar par) throws Exception{
    
    try{
      
      List<String> keywordsToStore = new ArrayList<>();
      
      final SSUri activityUri = 
        SSServCaller.activityAdd(
          par.user,
          SSActivityE.search,
          SSUri.asListWithoutNullAndEmpty(),
          SSUri.asListWithoutNullAndEmpty(par.entitiesToSearchWithin),
          SSTextComment.asListWithoutNullAndEmpty(),
          null,
          false);
      
      keywordsToStore.addAll(par.keywordsToSearchFor);
      keywordsToStore.addAll(SSStrU.distinctWithoutEmptyAndNull(par.labelsToSearchFor));
      keywordsToStore.addAll(par.misToSearchFor);
      
      keywordsToStore = SSStrU.distinctWithoutEmptyAndNull(keywordsToStore);
      
      if(!keywordsToStore.isEmpty()){
      
        SSServCaller.activityContentsAdd(
          par.user,
          activityUri,
          SSActivityContentE.keyword,
          SSActivityContent.get(keywordsToStore),
          false);
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