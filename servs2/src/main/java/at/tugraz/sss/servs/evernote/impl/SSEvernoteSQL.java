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
package at.tugraz.sss.servs.evernote.impl;

import at.tugraz.sss.servs.evernote.datatype.*;
import at.tugraz.sss.serv.util.SSSQLVarNames;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.db.api.SSDBSQLFctA;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.errreg.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSEvernoteSQL extends SSDBSQLFctA {
  
  public SSEvernoteSQL(final SSDBSQLI dbSQL){
    super(dbSQL);
  }
  
  public void addUserIfNotExists(
    final SSServPar servPar,
    final SSUri  user,
    final String authToken) throws SSErr{
    
    try{
      final Map<String, String> inserts    =  new HashMap<>();
      final Map<String, String> uniqueKeys =  new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userId,      user);
      insert(inserts, SSSQLVarNames.authToken,   authToken);
      insert(inserts, SSSQLVarNames.usn,         0);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.userId, user);
      
      dbSQL.insertIfNotExists(servPar, SSEvernoteSQLTableE.evernoteuser, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void setUSN(
    final SSServPar servPar,
    final String  authToken,
    final Integer usn) throws SSErr{
    
    try{
      final Map<String, String> updates    =  new HashMap<>();
      final Map<String, String> wheres     =  new HashMap<>();
      
      where  (wheres,  SSSQLVarNames.authToken, authToken);
      update (updates, SSSQLVarNames.usn,       usn);
      
      dbSQL.update(servPar, SSEvernoteSQLTableE.evernoteuser, wheres, updates);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
    
  public String getAuthToken(
    final SSServPar servPar,
    final SSUri user) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.userId);
      column(columns, SSSQLVarNames.authToken);
      
      where(wheres, SSSQLVarNames.userId, user);
      
      resultSet = dbSQL.select(servPar, SSEvernoteSQLTableE.evernoteuser, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
        
      return bindingStr(resultSet, SSSQLVarNames.authToken);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addNoteIfNotExists(
    final SSServPar servPar,
    final SSUri notebookUri,
    final SSUri noteUri) throws SSErr{
    
    try{
      final Map<String, String> inserts    =  new HashMap<>();
      final Map<String, String> uniqueKeys =  new HashMap<>();
      
      insert(inserts, SSSQLVarNames.noteId,      noteUri);
      insert(inserts, SSSQLVarNames.notebookId,  notebookUri);

        uniqueKey(uniqueKeys, SSSQLVarNames.noteId, noteUri);
      
      dbSQL.insertIfNotExists(servPar, SSEvernoteSQLTableE.evernotenote, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void addResourceIfNotExists(
    final SSServPar servPar,
    final SSUri noteUri,
    final SSUri resourceUri) throws SSErr{
    
    try{
      final Map<String, String> inserts    =  new HashMap<>();
      final Map<String, String> uniqueKeys =  new HashMap<>();
      
      insert(inserts, SSSQLVarNames.entityId,    resourceUri);
      insert(inserts, SSSQLVarNames.noteId,      noteUri);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.entityId, resourceUri);
      
      dbSQL.insertIfNotExists(servPar, SSEvernoteSQLTableE.evernoteresource, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public SSEvernoteResource getResource(
    final SSServPar servPar,
    final SSUri resourceId) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.noteId);
      column(columns, SSSQLVarNames.entityId);
      
      where(wheres, SSSQLVarNames.entityId, resourceId);
      
      resultSet = dbSQL.select(servPar, SSEvernoteSQLTableE.evernoteresource, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
        
      return SSEvernoteResource.get(
        resourceId, 
        bindingStrToUri(resultSet, SSSQLVarNames.noteId));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public Integer getUSN(
    final SSServPar servPar,
    final String authToken) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres = new HashMap<>();
      
      column(columns, SSSQLVarNames.authToken);
      column(columns, SSSQLVarNames.usn);
      
      where(wheres, SSSQLVarNames.authToken, authToken);
      
      resultSet = dbSQL.select(servPar, SSEvernoteSQLTableE.evernoteuser, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return 0;
      }
      
      return bindingStrToInteger(resultSet, SSSQLVarNames.usn);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSEvernoteNote getNote(
    final SSServPar servPar,
    final SSUri noteUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.noteId);
      column(columns, SSSQLVarNames.notebookId);
      
      where(wheres, SSSQLVarNames.noteId, noteUri);
      
      resultSet = dbSQL.select(servPar, SSEvernoteSQLTableE.evernotenote, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return SSEvernoteNote.get(
        noteUri,
        bindingStrToUri(resultSet, SSSQLVarNames.notebookId));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}