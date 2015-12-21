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

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.kc.tugraz.ss.like.api.SSLikeClientI;
import at.kc.tugraz.ss.like.api.SSLikeServerI;
import at.kc.tugraz.ss.like.datatypes.SSLikes;
import at.kc.tugraz.ss.like.datatypes.par.SSLikeUserSetPar;
import at.kc.tugraz.ss.like.datatypes.par.SSLikesUserGetPar;
import at.kc.tugraz.ss.like.datatypes.ret.SSLikeUserSetRet;
import at.kc.tugraz.ss.like.impl.fct.sql.SSLikeSQLFct;
import at.tugraz.sss.serv.impl.api.SSEntityServerI;
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.conf.SSConfA;

import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.impl.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;

public class SSLikeImpl 
extends SSServImplWithDBA 
implements 
  SSLikeClientI, 
  SSLikeServerI,
  SSDescribeEntityI{
  
  private final SSLikeSQLFct     sql;
  private final SSEntityServerI  entityServ;
  private final SSUserCommons userCommons;
   
  public SSLikeImpl(final SSConfA conf) throws SSErr{
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql          = new SSLikeSQLFct(dbSQL, SSVocConf.systemUserUri);
    this.entityServ   = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
    this.userCommons  = new SSUserCommons();
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws SSErr{
    
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
  public SSLikes likesGet(final SSLikesUserGetPar par) throws SSErr{
    
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
  public SSServRetI likeSet(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLikeUserSetPar par = (SSLikeUserSetPar) parA.getFromJSON(SSLikeUserSetPar.class);
      
      return SSLikeUserSetRet.get(likeSet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
   
  @Override
  public SSUri likeSet(final SSLikeUserSetPar par) throws SSErr{
    
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