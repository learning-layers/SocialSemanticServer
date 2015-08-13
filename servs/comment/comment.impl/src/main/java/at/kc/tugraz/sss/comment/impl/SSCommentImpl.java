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
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSSocketCon;
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
import at.kc.tugraz.sss.comment.datatypes.ret.SSCommentsGetRet;
import at.kc.tugraz.sss.comment.impl.fct.sql.SSCommentSQLFct;
import at.kc.tugraz.sss.comment.impl.fct.userrelationgather.SSCommentUserRelationGatherFct;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.caller.SSServCaller;
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
  
  private final SSCommentSQLFct sqlFct;
  
  public SSCommentImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    this.sqlFct = new SSCommentSQLFct(dbSQL);
  }
  
  @Override
  public void getUserRelations(
    final List<String>             allUsers, 
    final Map<String, List<SSUri>> userRelations) throws Exception{
    
    SSCommentUserRelationGatherFct.getUserRelations(
      allUsers, 
      userRelations);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{

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
  public SSUri commentsAdd(final SSCommentsAddPar par) throws Exception{
    
    try{
      
      if(par.entity == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      SSUri commentUri;
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          par.user,
          par.entity,
          null, //type,
          null, //label,
          null, //description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction,
          false)); //shouldCommit))
      
      for(SSTextComment content : par.comments){
        
        commentUri = SSServCaller.vocURICreate();
        
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            commentUri,
            SSEntityE.comment, //type,
            null, //label
            null, //description,
            null, //entitiesToAttach,
            null, //creationTime,
            null, //read,
            true, //setPublic
            false, //withUserRestriction
            false)); //shouldCommit)
        
        sqlFct.createComment(
          commentUri, 
          content);
        
        sqlFct.addComment(
          par.entity, 
          commentUri);
      }
      
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
  public void commentsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSCommentsGetPar par = (SSCommentsGetPar) parA.getFromJSON(SSCommentsGetPar.class);
    
    sSCon.writeRetFullToClient(SSCommentsGetRet.get(commentsGet(par)));
  }
  
  @Override
  public List<SSTextComment> commentsGet(final SSCommentsGetPar par) throws Exception{
    
    try{
      
      if(par.entity == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        if(!SSServCallerU.canUserRead(par.user, par.entity)){
          return new ArrayList<>();
        }
      }
      
      return sqlFct.getComments(par.entity, null);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> commentEntitiesGet(final SSCommentEntitiesGetPar par) throws Exception{
    
    try{
      
      if(par.user == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final List<SSUri>    entityURIs = sqlFct.getEntityURIsCommented(par.user);
      
      if(!par.withUserRestriction){
        return entityURIs;
      }

      final List<SSEntity> entities   = new ArrayList<>();
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entities,
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            par.user,
            entityURIs, //entities
            null, //types,
            null, //descPar,
            par.withUserRestriction)));
      
      return SSUri.getDistinctNotNullFromEntities(entities);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}