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
package at.kc.tugraz.sss.comment.impl.fct.sql;

import at.kc.tugraz.sss.comment.datatypes.*;
import at.kc.tugraz.sss.comment.datatypes.par.*;
import at.tugraz.sss.serv.util.SSSQLVarNames;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCommentSQLFct extends SSCoreSQL{
  
  public SSCommentSQLFct(
    final SSDBSQLI dbSQL){
    
    super(dbSQL);
  }
  
  public List<SSUri> getEntityURIsCommented(
    final SSServPar servPar,
    final SSUri     forUser) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      if(forUser == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<SSSQLTableI>   tables            = new ArrayList<>();
      final List<String>        columns           = new ArrayList<>();
      final List<String>        tableCons         = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      
      column(columns, SSCommentSQLTableE.comments, SSSQLVarNames.entityId);
      
      table (tables, SSCommentSQLTableE.comments);
      table (tables, SSEntitySQLTableE.entity);
      
      where   (wheres, SSEntitySQLTableE.entity, SSSQLVarNames.author, forUser);
      
      tableCon(tableCons, SSEntitySQLTableE.entity,  SSSQLVarNames.id, SSCommentSQLTableE.comments,  SSSQLVarNames.commentId);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.entityId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSComment> getComments(
    final SSServPar servPar,
    final SSUri     entity,
    final SSUri     forUser) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<SSComment>     comments          = new ArrayList<>();
      final List<SSSQLTableI>   tables            = new ArrayList<>();
      final List<String>        columns           = new ArrayList<>();
      final List<String>        tableCons         = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      
      column(columns, SSCommentSQLTableE.comment, SSSQLVarNames.commentId);
      column(columns, SSSQLVarNames.commentContent);
      
      table(tables, SSCommentSQLTableE.comment);
      table(tables, SSCommentSQLTableE.comments);
      
      if(entity != null){
        where(wheres, SSCommentSQLTableE.comments, SSSQLVarNames.entityId, entity);
      }
      
      if(forUser != null){
        
        column   (columns,   SSSQLVarNames.author);
        where    (wheres,    SSEntitySQLTableE.entity,  SSSQLVarNames.author, forUser);
        tableCon (tableCons, SSEntitySQLTableE.entity,  SSSQLVarNames.id, SSCommentSQLTableE.comment,  SSSQLVarNames.commentId);
      }
      
      tableCon(tableCons, SSCommentSQLTableE.comment, SSSQLVarNames.commentId, SSCommentSQLTableE.comments, SSSQLVarNames.commentId);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        comments.add(
          SSComment.get(
            bindingStrToUri        (resultSet, SSSQLVarNames.commentId), 
            bindingStrToTextComment(resultSet, SSSQLVarNames.commentContent)));
      }
      
      return comments;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addComment(
    final SSServPar servPar,
    final SSUri         entity,
    final SSUri         commentUri) throws SSErr{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.entityId,  entity);
      insert(inserts, SSSQLVarNames.commentId, commentUri);
      
      dbSQL.insert(servPar, SSCommentSQLTableE.comments, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void createComment(
    final SSServPar servPar,
    final SSUri         commentUri,
    final SSTextComment content) throws SSErr{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.commentId,      commentUri);
      insert(inserts, SSSQLVarNames.commentContent, content);
      
      dbSQL.insert(servPar, SSCommentSQLTableE.comment, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}