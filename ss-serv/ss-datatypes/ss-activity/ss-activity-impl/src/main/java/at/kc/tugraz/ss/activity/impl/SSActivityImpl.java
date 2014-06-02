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
package at.kc.tugraz.ss.activity.impl;

import at.kc.tugraz.ss.activity.api.SSActivityClientI;
import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityAddPar;
import at.kc.tugraz.ss.activity.impl.fct.sql.SSActivitySQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.List;

public class SSActivityImpl extends SSServImplWithDBA implements SSActivityClientI, SSActivityServerI, SSEntityHandlerImplI{

  private final SSActivitySQLFct sqlFct;

  public SSActivityImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{

    super(conf, dbGraph, dbSQL);

    this.sqlFct = new SSActivitySQLFct(dbSQL);
  }
  
  /* SSEntityHandlerImplI */

  public List<SSUri> searchWithKeywordWithin(
    final SSUri         userUri,
    final SSUri         entityUri,
    final String        keyword,
    final SSEntityE     entityType) throws Exception{

    return null;
  }
      
  @Override
  public Boolean setUserEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri, 
    final SSEntityE      entityType,
    final SSUri          publicCircleUri) throws Exception{
    
    return null;
  }

  @Override
  public Boolean shareUserEntity(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          circleUri,
    final SSEntityE      entityType) throws Exception{

    try{
      return false;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean addEntityToCircle(
    final SSUri        userUri,
    final SSUri        circleUri,
    final SSUri        entityUri,
    final SSEntityE entityType) throws Exception{

    return false;
  }

  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSUri       userUri, 
    final SSEntityE   entityType,
    final SSUri       entityUri,
    final Boolean     removeUserTags,
    final Boolean     removeUserRatings,
    final Boolean     removeFromUserColls,
    final Boolean     removeUserLocations) throws Exception{

  }

  @Override
  public SSEntityDescA getDescForEntity(
    final SSEntityE       entityType,
    final SSUri           userUri,
    final SSUri           entityUri,
    final SSLabel         label,
    final Long            creationTime,
    final List<String>    tags,
    final SSEntityA       overallRating,
    final List<SSUri>     discUris,
    final SSUri           author) throws Exception{
    
    return SSEntityDesc.get(
      entityUri,
      label,
      creationTime,
      tags,
      overallRating,
      discUris,
      author);
  }

  /* SSActivityClientI */
//  @Override
//  public void collUserParentGet(SSSocketCon sSCon, SSServPar parA) throws Exception{
//
//    SSServCaller.checkKey(parA);
//
//    sSCon.writeRetFullToClient(SSCollUserParentGetRet.get(collUserParentGet(parA), parA.op));
//  }
  
   /*  SSActivityServerI */
  
  @Override
  public SSUri activityAdd(final SSServPar parA) throws Exception{

    try{
      final SSActivityAddPar par = new SSActivityAddPar(parA);
      
      final SSUri activityUri = sqlFct.createActivityURI();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityAdd(
        par.user,
        activityUri,
        SSLabel.get(SSActivityE.toStr(par.type)),
        SSEntityE.activity,
        false);
      
      sqlFct.addActivity(
        activityUri, 
        par.type, 
        par.users, 
        par.sourceEntities, 
        par.targetEntities, 
        par.comments);
      
      dbSQL.commit(par.shouldCommit);

      return activityUri;
    }catch(SSSQLDeadLockErr deadLockErr){

      if(dbSQL.rollBack(parA)){
        return activityAdd(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }

    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

