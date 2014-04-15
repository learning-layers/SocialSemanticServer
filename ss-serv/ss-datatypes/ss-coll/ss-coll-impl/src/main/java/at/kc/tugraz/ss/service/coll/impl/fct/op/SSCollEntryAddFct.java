 /**
  * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSAccessRightsRightTypeE;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.impl.fct.sql.SSCollSQLFct;
import at.kc.tugraz.ss.service.coll.impl.fct.ue.SSCollUEFct;

public class SSCollEntryAddFct{
  
  private final SSDBSQLI     dbSQL;
  private final SSCollSQLFct sqlFct;
  
  public SSCollEntryAddFct(final SSDBSQLI dbSQL) throws Exception{
   
    this.dbSQL  = dbSQL;
    this.sqlFct = new SSCollSQLFct(dbSQL);
  }
  
  public SSUri addNewColl(
    final SSCollUserEntryAddPar par) throws Exception{
    
    final Boolean isParentCollSharedOrPublic;
    
    switch(SSServCaller.accessRightsEntityMostOpenCircleTypeGet(par.coll)){
      case priv: isParentCollSharedOrPublic = false; break;
      default:   isParentCollSharedOrPublic = true;
    }
    
    par.collEntry = sqlFct.createCollURI();
    
    SSServCaller.addEntity(
      par.user,
      par.collEntry,
      par.collEntryLabel,
      SSEntityEnum.coll);
    
    dbSQL.startTrans(par.shouldCommit);
    
    sqlFct.createColl(par.collEntry);
    
    sqlFct.addCollToUserColl(
      par.user, 
      par.coll, 
      par.collEntry, 
      isParentCollSharedOrPublic, 
      false);
    
    dbSQL.commit(par.shouldCommit);
    
    for(SSUri circleUri : SSServCaller.accessRightsEntityCircleURIsGet(par.coll)){
      
      SSServCaller.accessRightsUserEntitiesToCircleAdd(
        par.user,
        circleUri,
        par.collEntry,
        true);
    }
    
    return par.collEntry;
  }
  
  public SSUri addExistingColl(final SSCollUserEntryAddPar par) throws Exception{
    
    if(!SSServCaller.accessRightsUserAllowedIs(par.user, par.collEntry, SSAccessRightsRightTypeE.read)){
      throw new Exception("user cannot access to add coll");
    }
    
    //TODO dtheiler: check whether to follow coll is [explicitly] shared [with user]
    
    switch(SSServCaller.accessRightsEntityMostOpenCircleTypeGet(par.coll)){
      case priv: break;
      default:   throw new Exception("cannot add shared or public coll to shared / public parent coll");
    }
    
    if(sqlFct.ownsUserColl(par.user, par.collEntry)){
      throw new Exception("coll is already followed by user");
    }
    
    if(sqlFct.ownsUserASubColl(par.user, par.collEntry)){
      throw new Exception("a sub coll is already followed");
    }
    
    dbSQL.startTrans(par.shouldCommit);
    
    sqlFct.addCollToUserColl(
      par.user, 
      par.coll, 
      par.collEntry, 
      false,
      true);
    
    dbSQL.commit(par.shouldCommit);
    
    return par.collEntry;
  }
  
  public SSUri addCollEntry(final SSCollUserEntryAddPar par) throws Exception{
  
    SSServCaller.addEntity(
      par.user,
      par.collEntry,
      par.collEntryLabel,
      SSEntityEnum.entity);
    
    dbSQL.startTrans(par.shouldCommit);
    
    sqlFct.addEntryToColl(par.coll, par.collEntry, par.collEntryLabel);
    
    dbSQL.commit(par.shouldCommit);
    
    for(SSUri circleUri : SSServCaller.accessRightsEntityCircleURIsGet(par.coll)){
      
      SSServCaller.accessRightsUserEntitiesToCircleAdd(
        par.user,
        circleUri,
        par.collEntry,
        true);
    }
    
    SSCollUEFct.collUserEntryAdd(par);
    
    return par.collEntry;
  }
}
