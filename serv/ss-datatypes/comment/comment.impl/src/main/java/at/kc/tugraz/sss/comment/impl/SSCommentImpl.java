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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.sss.comment.datatypes.par.SSCommentEntitiesCommentedGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityDescriberI;
import at.kc.tugraz.ss.serv.serv.api.SSEntityUpdaterI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.api.SSUserRelationGathererI;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.sss.comment.api.SSCommentClientI;
import at.kc.tugraz.sss.comment.api.SSCommentServerI;
import at.kc.tugraz.sss.comment.datatypes.par.SSCommentsGetPar;
import at.kc.tugraz.sss.comment.datatypes.par.SSCommentsUserGetPar;
import at.kc.tugraz.sss.comment.datatypes.ret.SSCommentsUserGetRet;
import at.kc.tugraz.sss.comment.impl.fct.sql.SSCommentSQLFct;
import at.kc.tugraz.sss.comment.impl.fct.userrelationgather.SSCommentUserRelationGatherFct;
import java.util.List;
import java.util.Map;

public class SSCommentImpl extends SSServImplWithDBA implements SSCommentClientI, SSCommentServerI, SSEntityUpdaterI, SSEntityDescriberI, SSUserRelationGathererI{
  
  private final SSCommentSQLFct sqlFct;
  
  public SSCommentImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{

    super(conf, null, dbSQL);

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
  public SSEntity getDescForEntity(
    final SSEntityDescGetPar par, 
    SSEntity                 desc) throws Exception{
    
    desc.comments.addAll(
      SSServCaller.commentsGet(
        par.user, 
        null, 
        desc.id));
    
    return desc;
  }
  
  @Override
  public void updateEntity(
    final SSEntityUpdatePar par) throws Exception{

    if(par.comments.isEmpty()){
      return;
    }
    
    try{
      
      SSUri commentUri;
      
      for(SSTextComment content : par.comments){
        
        commentUri = SSServCaller.vocURICreate();
        
        SSServCaller.entityEntityToPrivCircleAdd(
          par.user,
          commentUri,
          SSEntityE.comment,
          par.label,
          null,
          null,
          false);
        
        sqlFct.createComment (commentUri, content);
        sqlFct.addComment    (par.entity, commentUri);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void commentsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSCommentsUserGetRet.get(commentsUserGet(parA), parA.op));
  }
  
  @Override
  public List<SSTextComment> commentsUserGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSCommentsUserGetPar par = new SSCommentsUserGetPar(parA);
      
      if(par.entity != null){
        SSServCaller.entityUserCanRead(par.user, par.entity);
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