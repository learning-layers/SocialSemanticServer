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

public class SSCollEntryAddFct{
  
  private final SSDBSQLI     dbSQL;
  private final SSCollSQLFct sqlFct;
  
  public SSCollEntryAddFct(final SSDBSQLI dbSQL) throws Exception{
   
    this.dbSQL  = dbSQL;
    this.sqlFct = new SSCollSQLFct(dbSQL);
  }
  
  public SSUri addNewColl(final SSCollUserEntryAddPar par) throws Exception{
    
    final Boolean isSharedOrPublicParentColl;
    
    par.collEntry = sqlFct.createCollURI();
    
    SSServCaller.addEntity(
      par.user,
      par.collEntry,
      par.collEntryLabel,
      SSEntityEnum.coll);
    
    dbSQL.startTrans(par.shouldCommit);
    
    sqlFct.createColl(par.collEntry);
    
    switch(SSServCaller.accessRightsEntityMostOpenCircleTypeGet(par.coll)){
      case priv: isSharedOrPublicParentColl = false; break;
      default:   isSharedOrPublicParentColl = true;
    }
    
    for(SSUri circleUri : SSServCaller.accessRightsEntityCirclesURIsGet(par.coll)){
      
      SSServCaller.accessRightsUserEntitiesToCircleAdd(
        par.user,
        circleUri,
        par.collEntry,
        false);
    }
    
    sqlFct.addCollToUserColl(par.user, par.coll, par.collEntry, isSharedOrPublicParentColl, false);
    
    dbSQL.commit(par.shouldCommit);
    
    return par.collEntry;
  }
  
  public SSUri addExistingColl(final SSCollUserEntryAddPar par) throws Exception{
    
    if(par.circleUri == null){
      throw new Exception("circle uri missing");
    }
    
    //TODO dtheiler: check whether to follow coll is [explicitly] shared [with user]
    
    if(!SSServCaller.accessRightsUserAllowedIs(par.user, par.collEntry, par.circleUri, SSAccessRightsRightTypeE.read)){
      throw new Exception("user cannot access to add coll");
    }
    
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
    
    sqlFct.addCollToUserColl(par.user, par.coll, par.collEntry, false, true);
    
    dbSQL.commit(par.shouldCommit);
    
    return par.collEntry;
  }
  
  public void addCollEntry(final SSCollUserEntryAddPar par) throws Exception{
    
    SSServCaller.addEntity(
      par.user,
      par.collEntry,
      par.collEntryLabel,
      SSEntityEnum.entity);
    
    dbSQL.startTrans(par.shouldCommit);
    
    for(SSUri circleUri : SSServCaller.accessRightsEntityCirclesURIsGet(par.coll)){
      
      SSServCaller.accessRightsUserEntitiesToCircleAdd(
        par.user,
        circleUri,
        par.collEntry,
        false);
    }
    
    sqlFct.addEntryToColl(par.coll, par.collEntry, par.collEntryLabel);
    
    dbSQL.commit(par.shouldCommit);
  }
}
