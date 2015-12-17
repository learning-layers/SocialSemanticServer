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
package at.kc.tugraz.sss.comment.impl;

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSEntity;
import at.kc.tugraz.sss.comment.datatypes.par.SSCommentEntitiesGetPar;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.sss.comment.api.SSCommentClientI;
import at.kc.tugraz.sss.comment.api.SSCommentServerI;
import at.kc.tugraz.sss.comment.datatypes.par.SSCommentsAddPar;
import at.kc.tugraz.sss.comment.datatypes.par.SSCommentsGetPar;
import at.kc.tugraz.sss.comment.datatypes.ret.SSCommentsAddRet;
import at.kc.tugraz.sss.comment.datatypes.ret.SSCommentsGetRet;
import at.kc.tugraz.sss.comment.impl.fct.sql.SSCommentSQLFct;
import at.kc.tugraz.sss.comment.impl.fct.userrelationgather.SSCommentUserRelationGatherFct;
import at.tugraz.sss.serv.SSClientE;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServRetI; import at.tugraz.sss.serv.SSVarNames;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SSCommentImpl 
extends SSServImplWithDBA 
implements 
  SSCommentClientI, 
  SSCommentServerI, 
  SSDescribeEntityI,
  SSUserRelationGathererI{
  
  private final SSCommentSQLFct sql;
  
  public SSCommentImpl(final SSConfA conf) throws SSErr{

    super(conf, (SSDBSQLI) SSDBSQL.inst.getServImpl(), (SSDBNoSQLI) SSDBNoSQL.inst.getServImpl());

    this.sql = new SSCommentSQLFct(dbSQL, SSVocConf.systemUserUri);
  }
  
  @Override
  public void getUserRelations(
    final List<String>             allUsers,
    final Map<String, List<SSUri>> userRelations) throws SSErr{
    
    try{
      SSCommentUserRelationGatherFct.getUserRelations(
        allUsers,
        userRelations);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws SSErr{

    try{
      
      if(par.setComments){
        
        entity.comments.addAll(
          commentsGet(
            new SSCommentsGetPar(
              par.user, 
              entity.id,
              par.withUserRestriction)));
      }

      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI commentsAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSCommentsAddPar par = (SSCommentsAddPar) parA.getFromJSON(SSCommentsAddPar.class);
      
      return SSCommentsAddRet.get(commentsAdd(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri commentsAdd(final SSCommentsAddPar par) throws SSErr{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri entity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.entity,
            null, //type,
            null, //label,
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit))
      
      if(entity == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      SSUri commentUri;
      
      for(SSTextComment content : par.comments){
        
        commentUri =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              par.user,
              SSVocConf.vocURICreate(),
              SSEntityE.comment, //type,
              null, //label
              null, //description,
              null, //creationTime,
              null, //read,
              true, //setPublic
              true, //createIfNotExists,
              false, //withUserRestriction
              false)); //shouldCommit)
        
        if(commentUri == null){
          dbSQL.rollBack(par.shouldCommit);
          return null;
        }
        
        sql.createComment(
          commentUri, 
          content);
        
        sql.addComment(
          par.entity, 
          commentUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return commentsAdd(par);
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
  
  @Override
  public SSServRetI commentsGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSCommentsGetPar par = (SSCommentsGetPar) parA.getFromJSON(SSCommentsGetPar.class);
      
      return SSCommentsGetRet.get(commentsGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSTextComment> commentsGet(final SSCommentsGetPar par) throws SSErr{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        final SSEntity entity = 
          sql.getEntityTest(
            par.user, 
            par.entity, 
            par.withUserRestriction);
        
        if(entity == null){
          return new ArrayList<>();
        }
      }
      
      return sql.getComments(par.entity, null);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> commentEntitiesGet(final SSCommentEntitiesGetPar par) throws SSErr{
    
    try{
      
      if(par.user == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<SSUri> entityURIs = sql.getEntityURIsCommented(par.user);
      
      if(!par.withUserRestriction){
        return entityURIs;
      }

      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final List<SSEntity>  entities   = new ArrayList<>();
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entities,
        entityServ.entitiesGet(
          new SSEntitiesGetPar(
            par.user,
            entityURIs, //entities
            null, //descPar,
            par.withUserRestriction)));
      
      return SSUri.getDistinctNotNullFromEntities(entities);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}