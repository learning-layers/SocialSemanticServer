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

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivEntityAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSEntity;
import at.kc.tugraz.sss.comment.datatypes.par.SSCommentEntitiesCommentedGetPar;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSEntityUpdaterI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSUserRelationGathererI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import at.kc.tugraz.sss.comment.api.SSCommentClientI;
import at.kc.tugraz.sss.comment.api.SSCommentServerI;
import at.kc.tugraz.sss.comment.datatypes.par.SSCommentsGetPar;
import at.kc.tugraz.sss.comment.datatypes.par.SSCommentsUserGetPar;
import at.kc.tugraz.sss.comment.datatypes.ret.SSCommentsUserGetRet;
import at.kc.tugraz.sss.comment.impl.fct.sql.SSCommentSQLFct;
import at.kc.tugraz.sss.comment.impl.fct.userrelationgather.SSCommentUserRelationGatherFct;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import java.util.List;
import java.util.Map;

public class SSCommentImpl 
extends SSServImplWithDBA 
implements 
  SSCommentClientI, 
  SSCommentServerI, 
  SSEntityUpdaterI, 
  SSEntityDescriberI, 
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
  public SSEntity getUserEntity(final SSEntityDescriberPar par) throws Exception{

    try{
      
      par.entity.comments.addAll(
        SSServCaller.commentsGet(
          par.user,
          null,
          par.entity.id));

      return par.entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void updateEntity(
    final SSServPar parA) throws Exception{

    final SSEntityUpdatePar par = (SSEntityUpdatePar) parA;
    
    if(par.comments.isEmpty()){
      return;
    }
    
    try{
      
      SSUri commentUri;
      
      for(SSTextComment content : par.comments){
        
        commentUri = SSServCaller.vocURICreate();
        
        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlePrivEntityAdd(
          new SSCirclePrivEntityAddPar(
            null,
            null,
            par.user,
            commentUri,
            SSEntityE.comment,
            par.label,
            null,
            null,
            false));
        
        sqlFct.createComment (commentUri, content);
        sqlFct.addComment    (par.entity, commentUri);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void commentsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSCommentsUserGetRet.get(commentsUserGet(parA), parA.op));
  }
  
  @Override
  public List<SSTextComment> commentsUserGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSCommentsUserGetPar par = new SSCommentsUserGetPar(parA);
      
      if(par.entity != null){
        SSServCallerU.canUserReadEntity(par.user, par.entity);
      }
      
      if(
        par.forUser != null &&
        !SSStrU.equals(par.user, par.forUser)){
        throw new Exception("user cannot access comments for user " + par.forUser);
      }
      
      return sqlFct.getComments(par.entity, par.forUser);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSTextComment> commentsGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSCommentsGetPar par = new SSCommentsGetPar(parA);
      
      return sqlFct.getComments(par.entity, par.forUser);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> commentEntitiesCommentedGet(final SSServPar parA) throws Exception{
    
    try{
      final SSCommentEntitiesCommentedGetPar par = new SSCommentEntitiesCommentedGetPar(parA);
      
      return sqlFct.getEntityURIsCommented(par.forUser);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}