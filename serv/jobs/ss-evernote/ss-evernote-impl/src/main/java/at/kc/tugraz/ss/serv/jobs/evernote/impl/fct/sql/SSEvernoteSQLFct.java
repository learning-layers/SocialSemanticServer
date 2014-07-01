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
package at.kc.tugraz.ss.serv.jobs.evernote.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNote;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResource;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class SSEvernoteSQLFct extends SSDBSQLFct {
  
  public SSEvernoteSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }

  public void addNote(
    final SSUri notebookUri, 
    final SSUri noteUri) throws Exception{
    
    try{
      final Map<String, String> inserts =  new HashMap<>();
      
      insert(inserts, SSSQLVarU.noteId,      noteUri);
      insert(inserts, SSSQLVarU.notebookId,  notebookUri);
      
      dbSQL.insert(evernoteNoteTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void addResource(
    final SSUri noteUri,
    final SSUri resourceUri) throws Exception{
    
    try{
      final Map<String, String> inserts =  new HashMap<>();
      
      insert(inserts, SSSQLVarU.entityId,    resourceUri);
      insert(inserts, SSSQLVarU.noteId,      noteUri);
      
      dbSQL.insert(evernoteResourceTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public SSEvernoteNote getNote(
    final SSUri noteUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.noteId, noteUri);
      
      resultSet = dbSQL.select(evernoteNoteTable, wheres);
      
      if(!resultSet.next()){
        throw new Exception("evernote note doesnt exist");
      }
        
      return SSEvernoteNote.get(
        noteUri, 
        bindingStrToUri(resultSet, SSSQLVarU.notebookId));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSEvernoteResource getResource(
    final SSUri resourceId) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.entityId, resourceId);
      
      resultSet = dbSQL.select(evernoteResourceTable, wheres);
      
      if(!resultSet.next()){
        throw new Exception("evernote resource doesnt exist");
      }
        
      return SSEvernoteResource.get(
        resourceId, 
        bindingStrToUri(resultSet, SSSQLVarU.noteId));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}