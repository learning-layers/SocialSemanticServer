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

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.impl.fct.misc.SSCollMiscFct;
import at.kc.tugraz.ss.service.coll.impl.fct.sql.SSCollSQLFct;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.util.SSServCallerU;

public class SSCollEntryAddFct{
  
  public static SSUri addNewColl(
    final SSCollSQLFct          sqlFct,
    final SSCollUserEntryAddPar par) throws Exception{
    
    final Boolean isParentCollSharedOrPublic;
    
    switch(
      ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleMostOpenCircleTypeGet(
        new SSCircleMostOpenCircleTypeGetPar(
          par.user,
          par.coll,
          false))){
      case priv: isParentCollSharedOrPublic = false; break;
      default:   isParentCollSharedOrPublic = true;
    }
    
    par.entry = SSServCaller.vocURICreate();
    
    ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
      new SSEntityUpdatePar(
        par.user,
        par.entry,
        SSEntityE.coll, //type,
        par.label, //label
        null, //description,
        null, //creationTime,
        null, //read,
        false, //setPublic
        false, //withUserRestriction
        false)); //shouldCommit)
    
    sqlFct.addColl(par.entry);
    
    sqlFct.addCollToColl(
      par.user, 
      par.coll, 
      par.entry, 
      isParentCollSharedOrPublic, 
      false);
    
     SSServCallerU.handleCirclesFromEntityEntitiesAdd(
        par.user,
        par.coll,
        SSUri.asListWithoutNullAndEmpty(par.entry), //entities
        par.withUserRestriction);
     
    return par.entry;
  }
  
  public static SSUri addPublicColl(
    final SSCollSQLFct          sqlFct,
    final SSCollUserEntryAddPar par) throws Exception{
    
    
    if(!SSCircleE.equals(
      ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleMostOpenCircleTypeGet(
        new SSCircleMostOpenCircleTypeGetPar(
          par.user,
          par.entry,
          false)), 
      SSCircleE.pub)){
      
      throw new Exception("coll to add is not public");
    }
    
    switch(((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleMostOpenCircleTypeGet(
        new SSCircleMostOpenCircleTypeGetPar(
          par.user,
          par.coll,
          false))){
      case priv: break;
      default:   throw new Exception("cannot add shared or public coll to shared / public parent coll");
    }
    
    if(sqlFct.ownsUserColl(par.user, par.entry)){
      throw new Exception("coll is already followed by user");
    }
    
    if(SSCollMiscFct.ownsUserASubColl(sqlFct, par.user, par.entry)){
      throw new Exception("a sub coll is already followed");
    }
    
    sqlFct.addCollToColl(
      par.user, 
      par.coll, 
      par.entry, 
      false,
      true);
    
    return par.entry;
  }
  
  public static SSUri addCollEntry(
    final SSCollSQLFct          sqlFct, 
    final SSCollUserEntryAddPar par) throws Exception{
    
    ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
      new SSEntityUpdatePar(
        par.user,
        par.entry,
        null, //type,
        par.label, //label
        null, //description,
        null, //creationTime,
        null, //read,
        false, //setPublic
        false, //withUserRestriction
        false)); //shouldCommit)
        
    sqlFct.addCollEntry(par.coll, par.entry);
    
    SSServCallerU.handleCirclesFromEntityEntitiesAdd(
        par.user,
        par.coll,
        SSUri.asListWithoutNullAndEmpty(par.entry), //entities
        par.withUserRestriction);
    
    return par.entry;
  }
}
