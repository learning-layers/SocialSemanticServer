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

import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSCoreSQL;

public class SSCommentSQLFct extends SSCoreSQL{
  
  public SSCommentSQLFct(
    final SSDBSQLI dbSQL,
    final SSUri    systemUserURI){
    
    super(dbSQL, systemUserURI);
  }
  
  public List<SSUri> getEntityURIsCommented(
    final SSUri forUser) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(forUser == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<String>        tables            = new ArrayList<>();
      final List<String>        columns           = new ArrayList<>();
      final List<String>        tableCons         = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      
      column(columns, SSSQLVarNames.commentsTable, SSSQLVarNames.entityId);
      
      table (tables, SSSQLVarNames.commentsTable);
      table (tables, SSSQLVarNames.entityTable);
      
      where   (wheres, SSSQLVarNames.entityTable, SSSQLVarNames.author, forUser);
      
      tableCon(tableCons, SSSQLVarNames.entityTable,  SSSQLVarNames.id, SSSQLVarNames.commentsTable,  SSSQLVarNames.commentId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.entityId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSTextComment> getComments(
    final SSUri entity,
    final SSUri forUser) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        tables            = new ArrayList<>();
      final List<String>        columns           = new ArrayList<>();
      final List<String>        tableCons         = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      
      column(columns, SSSQLVarNames.commentContent);
      
      table(tables, SSSQLVarNames.commentTable);
      table(tables, SSSQLVarNames.commentsTable);
      
      if(entity != null){
        where(wheres, SSSQLVarNames.commentsTable, SSSQLVarNames.entityId, entity);
      }
      
      if(forUser != null){
        
        column  (columns,   SSSQLVarNames.author);
        where   (wheres, SSSQLVarNames.entityTable, SSSQLVarNames.author, forUser);
        table   (tables, SSSQLVarNames.entityTable);
        tableCon(tableCons, SSSQLVarNames.entityTable,  SSSQLVarNames.id, SSSQLVarNames.commentTable,  SSSQLVarNames.commentId);
      }
      
      tableCon(tableCons, SSSQLVarNames.commentTable, SSSQLVarNames.commentId, SSSQLVarNames.commentsTable, SSSQLVarNames.commentId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return getTextCommentsFromResult(resultSet, SSSQLVarNames.commentContent);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addComment(
    final SSUri         entity,
    final SSUri         commentUri) throws Exception{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.entityId,  entity);
      insert(inserts, SSSQLVarNames.commentId, commentUri);
      
      dbSQL.insert(SSSQLVarNames.commentsTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void createComment(
    final SSUri         commentUri,
    final SSTextComment content) throws Exception{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.commentId,      commentUri);
      insert(inserts, SSSQLVarNames.commentContent, content);
      
      dbSQL.insert(SSSQLVarNames.commentTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}