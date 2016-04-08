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
package at.tugraz.sss.servs.comment.impl;

import at.tugraz.sss.serv.entity.api.*;
import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.datatype.par.SSEntitiesGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.servs.comment.datatype.SSCommentEntitiesGetPar;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.servs.common.impl.SSUserCommons;
import at.tugraz.sss.servs.comment.api.SSCommentClientI;
import at.tugraz.sss.servs.comment.api.SSCommentServerI;
import at.tugraz.sss.servs.comment.datatype.SSCommentsAddPar;
import at.tugraz.sss.servs.comment.datatype.SSCommentsGetPar;
import at.tugraz.sss.servs.comment.datatype.SSCommentsAddRet;
import at.tugraz.sss.servs.comment.datatype.SSCommentsGetRet;
import at.tugraz.sss.servs.comment.impl.SSCommentSQLFct;
import at.tugraz.sss.servs.comment.impl.SSCommentUserRelationGatherFct;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 
import at.tugraz.sss.serv.util.*;
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
  
  private final SSCommentSQLFct  sql;
  private final SSUserCommons userCommons;
  
  public SSCommentImpl(final SSConfA conf) throws SSErr{

    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));

    this.sql         = new SSCommentSQLFct(dbSQL);
    this.userCommons = new SSUserCommons();
  }
  
  @Override
  public void getUserRelations(
    final SSServPar servPar,
    final List<String>             allUsers,
    final Map<String, List<SSUri>> userRelations) throws SSErr{
    
    try{
      SSCommentUserRelationGatherFct.getUserRelations(
        servPar,
        allUsers,
        userRelations);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar servPar,
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws SSErr{

    try{
      
      if(par.setComments){
        
        entity.comments.addAll(
          commentsGet(
            new SSCommentsGetPar(
              servPar,
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
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCommentsAddPar par = (SSCommentsAddPar) parA.getFromClient(clientType, parA, SSCommentsAddPar.class);
      
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
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri entity =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      SSUri commentUri;
      
      for(SSTextComment content : par.comments){
        
        commentUri =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              par,
              par.user,
              SSConf.vocURICreate(),
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
          dbSQL.rollBack(par, par.shouldCommit);
          return null;
        }
        
        sql.createComment(
          par, 
          commentUri, 
          content);
        
        sql.addComment(
          par, 
          par.entity, 
          commentUri);
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      return par.entity;
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI commentsGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSCommentsGetPar par = (SSCommentsGetPar) parA.getFromClient(clientType, parA, SSCommentsGetPar.class);
      
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
            par,
            SSConf.systemUserUri,
            par.user, 
            par.entity, 
            par.withUserRestriction);
        
        if(entity == null){
          return new ArrayList<>();
        }
      }
      
      return sql.getComments(par, par.entity, null);
      
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
      
      final List<SSUri> entityURIs = sql.getEntityURIsCommented(par, par.user);
      
      if(!par.withUserRestriction){
        return entityURIs;
      }

      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final List<SSEntity>  entities   = new ArrayList<>();
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entities,
        entityServ.entitiesGet(
          new SSEntitiesGetPar(
            par,
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