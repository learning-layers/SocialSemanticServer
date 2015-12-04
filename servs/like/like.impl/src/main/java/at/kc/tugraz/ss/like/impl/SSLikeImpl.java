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

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.like.api.SSLikeClientI;
import at.kc.tugraz.ss.like.api.SSLikeServerI;
import at.kc.tugraz.ss.like.datatypes.SSLikes;
import at.kc.tugraz.ss.like.datatypes.par.SSLikeUserSetPar;
import at.kc.tugraz.ss.like.datatypes.par.SSLikesUserGetPar;
import at.kc.tugraz.ss.like.datatypes.ret.SSLikeUserSetRet;
import at.kc.tugraz.ss.like.impl.fct.sql.SSLikeSQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.util.SSServCallerU;

public class SSLikeImpl 
extends SSServImplWithDBA 
implements 
  SSLikeClientI, 
  SSLikeServerI,
  SSDescribeEntityI{
  
  private final SSLikeSQLFct     sql;
  private final SSEntityServerI  entityServ;
   
  public SSLikeImpl(final SSConfA conf) throws SSErr{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sql        = new SSLikeSQLFct(dbSQL, SSVocConf.systemUserUri);
    this.entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setLikes){
        
        entity.likes =
          likesGet(
            new SSLikesUserGetPar(
              par.user,
              entity.id,
              null,
              par.withUserRestriction));
      }
      
      return entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSLikes likesGet(final SSLikesUserGetPar par) throws Exception{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final SSEntity entity = 
        sql.getEntityTest(
          par.user, 
          par.entity, 
          par.withUserRestriction);
      
      if(entity == null){
        return null;
      }
      
      if(par.withUserRestriction){
        
        if(
          par.forUser != null &&
          !SSStrU.equals(par.forUser, par.user)){
          throw SSErr.get(SSErrE.userNotAllowedToRetrieveForOtherUser);
        }
      }
      
      return sql.getLikes(
        par.user, 
        par.forUser, 
        par.entity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void likeSet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSLikeUserSetPar par = (SSLikeUserSetPar) parA.getFromJSON(SSLikeUserSetPar.class);
      
    sSCon.writeRetFullToClient(SSLikeUserSetRet.get(likeSet(par)));
  }
   
  @Override
  public SSUri likeSet(final SSLikeUserSetPar par) throws Exception{
    
    try{

      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri entity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.entity,
            null, //type,
            null, //label
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(entity == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sql.like(
        par.user, 
        par.entity, 
        par.value);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return likeSet(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}