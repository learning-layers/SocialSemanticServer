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
package at.kc.tugraz.ss.service.coll.impl.fct.op;

import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.impl.fct.sql.SSCollSQLFct;
import at.kc.tugraz.ss.service.coll.impl.fct.ue.SSCollUEFct;

public class SSCollEntryAddFct{
  
  public static SSUri addNewColl(
    final SSCollSQLFct          sqlFct,
    final SSCollUserEntryAddPar par) throws Exception{
    
    final Boolean isParentCollSharedOrPublic;
    
    switch(SSServCaller.entityMostOpenCircleTypeGet(par.coll)){
      case priv: isParentCollSharedOrPublic = false; break;
      default:   isParentCollSharedOrPublic = true;
    }
    
    par.collEntry = sqlFct.createCollURI();
    
    SSServCaller.entityAdd(
      par.user,
      par.collEntry,
      par.collEntryLabel,
      SSEntityE.coll, 
      false);
    
    sqlFct.createColl(par.collEntry);
    
    sqlFct.addCollToUserColl(
      par.user, 
      par.coll, 
      par.collEntry, 
      isParentCollSharedOrPublic, 
      false);
    
    for(SSEntityCircle entityUserCircle : SSServCaller.entityUserEntityCirclesGet(par.user, par.coll)){
      
      SSServCaller.entityEntitiesToCircleAdd(
        par.user,
        entityUserCircle.circleUri,
        par.collEntry,
        false);
    }
    
    return par.collEntry;
  }
  
  public static SSUri addPublicColl(
    final SSCollSQLFct          sqlFct,
    final SSCollUserEntryAddPar par) throws Exception{
    
    if(!SSCircleE.equals(SSServCaller.entityMostOpenCircleTypeGet(par.collEntry), SSCircleE.pub)){
      throw new Exception("coll to add is not public");
    }
    
    //TODO dtheiler: check whether to follow coll is [explicitly] shared [with user]
    
    switch(SSServCaller.entityMostOpenCircleTypeGet(par.coll)){
      case priv: break;
      default:   throw new Exception("cannot add shared or public coll to shared / public parent coll");
    }
    
    if(sqlFct.ownsUserColl(par.user, par.collEntry)){
      throw new Exception("coll is already followed by user");
    }
    
    if(sqlFct.ownsUserASubColl(par.user, par.collEntry)){
      throw new Exception("a sub coll is already followed");
    }
    
    sqlFct.addCollToUserColl(
      par.user, 
      par.coll, 
      par.collEntry, 
      false,
      true);
    
    return par.collEntry;
  }
  
  public static SSUri addCollEntry(
    final SSCollSQLFct          sqlFct, 
    final SSCollUserEntryAddPar par) throws Exception{
  
    SSServCaller.entityAdd(
      par.user,
      par.collEntry,
      par.collEntryLabel,
      SSEntityE.entity,
      false);
    
    sqlFct.addEntryToColl(par.coll, par.collEntry);
    
    for(SSEntityCircle circle : SSServCaller.entityUserEntityCirclesGet(par.user, par.coll)){
      
      SSServCaller.entityEntitiesToCircleAdd(
        par.user,
        circle.circleUri,
        par.collEntry,
        false);
    }
    
    SSCollUEFct.collUserEntryAdd(par);
    
    return par.collEntry;
  }
}
