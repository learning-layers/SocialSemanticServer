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

import at.tugraz.sss.serv.datatype.par.SSCircleAddEntitiesToCircleOfEntityPar;
import at.tugraz.sss.serv.datatype.par.SSCircleIsEntityPrivatePar;
import at.tugraz.sss.serv.datatype.par.SSCircleIsEntityPublicPar;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.impl.fct.misc.SSCollMiscFct;
import at.kc.tugraz.ss.service.coll.impl.fct.sql.SSCollSQLFct;

public class SSCollEntryAddFct{
  
  public static SSUri addNewColl(
    final SSEntityServerI       circleServ,
    final SSEntityServerI       entityServ,
    final SSCollSQLFct          sqlFct,
    final SSCollUserEntryAddPar par) throws Exception{
    
    final Boolean isParentCollSharedOrPublic;
    
    if(circleServ.circleIsEntityPrivate(new SSCircleIsEntityPrivatePar(par.user, par.coll))){
      isParentCollSharedOrPublic = false;
    }else{
      isParentCollSharedOrPublic = true;
    }
    
    final SSUri newColl = 
      entityServ.entityUpdate(
      new SSEntityUpdatePar(
        par.user,
          SSConf.vocURICreate(),
        SSEntityE.coll, //type,
        par.label, //label
        null, //description,
        null, //creationTime,
        null, //read,
        false, //setPublic
        true, //createIfNotExists
        false, //withUserRestriction
        false)); //shouldCommit)
    
    if(newColl == null){
      return null;
    }
    
    sqlFct.addColl(newColl);
    
    sqlFct.addCollToColl(
      par.user,
      par.coll,
      newColl,
      isParentCollSharedOrPublic,
      false);
    
    circleServ.circleAddEntitiesToCirclesOfEntity(
      new SSCircleAddEntitiesToCircleOfEntityPar(
        par.user,
        par.coll,
        SSUri.asListNotNull(newColl), //entities
        par.withUserRestriction,
        false, //invokeEntityHandlers,
        false)); //shouldCommit
      
    return newColl;
  }
  
  public static SSUri addPublicColl(
    final SSCollSQLFct          sqlFct,
    final SSEntityServerI       circleServ,
    final SSCollUserEntryAddPar par) throws Exception{
    
    if(!circleServ.circleIsEntityPublic(
      new SSCircleIsEntityPublicPar(
        par.user, 
        par.entry))){
      throw new Exception("coll to add is not public");
    }
    
    if(!circleServ.circleIsEntityPrivate(
      new SSCircleIsEntityPrivatePar(
        par.user, 
        par.entry))){
      
      throw new Exception("cannot add shared or public coll to shared / public parent coll");
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
    final SSEntityServerI       circleServ, 
    final SSEntityServerI       entityServ,
    final SSCollSQLFct          sqlFct, 
    final SSCollUserEntryAddPar par) throws Exception{
    
    final SSUri entry =
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          par.user,
          par.entry, //entity
          null, //type,
          par.label, //label
          null, //description,
          null, //creationTime,
          null, //read,
          false, //setPublic
          true, //createIfNotExists
          false, //withUserRestriction
          false)); //shouldCommit)
    
    if(entry == null){
      return null;
    }
    
    sqlFct.addCollEntry(par.coll, entry);
    
    circleServ.circleAddEntitiesToCirclesOfEntity(
      new SSCircleAddEntitiesToCircleOfEntityPar(
        par.user,
        par.coll,
        SSUri.asListNotNull(entry), //entities
        par.withUserRestriction,
        true, //invokeEntityHandlers,
        false)); //shouldCommit
    
    return entry;
  }
}
