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
package at.kc.tugraz.ss.like.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivEntityAddPar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.kc.tugraz.ss.like.api.SSLikeClientI;
import at.kc.tugraz.ss.like.api.SSLikeServerI;
import at.kc.tugraz.ss.like.datatypes.SSLikes;
import at.kc.tugraz.ss.like.datatypes.par.SSLikeUserSetPar;
import at.kc.tugraz.ss.like.datatypes.par.SSLikesUserGetPar;
import at.kc.tugraz.ss.like.datatypes.ret.SSLikeUserSetRet;
import at.kc.tugraz.ss.like.impl.fct.sql.SSLikeSQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;

public class SSLikeImpl extends SSServImplWithDBA implements SSLikeClientI, SSLikeServerI{
  
  private final SSLikeSQLFct sqlFct;

  public SSLikeImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct = new SSLikeSQLFct(dbSQL);
  }
  
  @Override
  public void likeSet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSLikeUserSetRet.get(likeUserSet(parA), parA.op));
  }
   
  @Override
  public SSLikes likesUserGet(final SSServPar parA) throws Exception{
    
    try{
      final SSLikesUserGetPar par = new SSLikesUserGetPar(parA);
      
      SSServCallerU.canUserReadEntity(par.user, par.entity);
      
      if(
        par.forUser != null &&
        !SSStrU.equals(par.forUser, par.user)){
        throw new SSErr(SSErrE.userNotAllowedToRetrieveForOtherUser);
      }
      
      return sqlFct.getLikes(
        par.user, 
        par.forUser, 
        par.entity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri likeUserSet(final SSServPar parA) throws Exception{
    
    try{
      final SSLikeUserSetPar par    = SSLikeUserSetPar.get(parA);
      final SSEntity         entity = 
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
          new SSEntityGetPar(
            null,
            null,
            null,
            par.entity, //entity
            null,  //forUser
            null, //label
            null, //type
            true, //withUserRestriction
            false, //invokeEntityHandlers
            null, //descPar
            true)); //logErr
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(entity == null){
        
        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlePrivEntityAdd(
          new SSCirclePrivEntityAddPar(
            null,
            null,
            par.user,
            par.entity,
            SSEntityE.entity,
            null,
            null,
            null,
            false));
      }
      
      sqlFct.like(
        par.user, 
        par.entity, 
        par.value);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return likeUserSet(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}