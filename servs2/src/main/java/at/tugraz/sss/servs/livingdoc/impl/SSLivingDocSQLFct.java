/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.servs.livingdoc.impl;

import at.tugraz.sss.servs.livingdoc.datatype.SSLivingDocSQLTableE;
import at.tugraz.sss.servs.livingdoc.datatype.SSLivingDocument;
import at.tugraz.sss.serv.db.api.SSCoreSQL;
import at.tugraz.sss.serv.util.SSSQLVarNames;
import at.tugraz.sss.serv.errreg.SSServErrReg;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSLivingDocSQLFct extends SSCoreSQL{

  public SSLivingDocSQLFct(
    final SSDBSQLI dbSQL) {
    
    super(dbSQL);
  }
  
  public SSLivingDocument getLivingDoc(
    final SSServPar   servPar,
    final SSUri livingDoc) throws SSErr{
    
    ResultSet resultSet  = null;
    
    try{
      
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        columns   = new ArrayList<>();
      
      where(wheres, SSSQLVarNames.livingDocId, livingDoc);
      
      resultSet = dbSQL.select(servPar, SSLivingDocSQLTableE.livingdoc, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return SSLivingDocument.get(livingDoc);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public List<SSUri> getLivingDocURIs(
  final SSServPar   servPar) throws SSErr{
    
    ResultSet resultSet  = null;
    
    try{
      
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        columns   = new ArrayList<>();
      
      column(columns, SSSQLVarNames.livingDocId);
      
      resultSet = dbSQL.select(servPar, SSLivingDocSQLTableE.livingdoc, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.livingDocId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
   
  public List<SSUri> getLivingDocURIsForUser(
    final SSServPar   servPar,
    final SSUri           user) throws SSErr{
    
    ResultSet resultSet  = null;
    
    try{
      
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        columns   = new ArrayList<>();
      
      column(columns, SSSQLVarNames.livingDocId);
      
      where(wheres, SSSQLVarNames.userId, user);
      
      resultSet = dbSQL.select(servPar, SSLivingDocSQLTableE.livingdocusers, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.livingDocId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void createLivingDoc(
    final SSServPar   servPar,
    final SSUri    livingDocURI,
    final SSUri    user) throws SSErr{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert   (inserts,    SSSQLVarNames.livingDocId, livingDocURI);
      uniqueKey(uniqueKeys, SSSQLVarNames.livingDocId, livingDocURI);
      
      dbSQL.insertIfNotExists(servPar, SSLivingDocSQLTableE.livingdoc, inserts, uniqueKeys);
      
      addLivingDoc(servPar, livingDocURI, user);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addLivingDoc(
    final SSServPar   servPar,
    final SSUri    livingDocURI,
    final SSUri    user) throws SSErr{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userId,      user);
      insert(inserts, SSSQLVarNames.livingDocId, livingDocURI);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.userId,      user);
      uniqueKey(uniqueKeys, SSSQLVarNames.livingDocId, livingDocURI);
      
      dbSQL.insertIfNotExists(servPar, SSLivingDocSQLTableE.livingdocusers, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getLivingDocUserURIs(
    final SSServPar   servPar,
    final SSUri livingDoc) throws SSErr{

    ResultSet resultSet   = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();

      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.livingDocId, livingDoc);
      
      resultSet = dbSQL.select(servPar, SSLivingDocSQLTableE.livingdocusers, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.userId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void removeLivingDoc(
    final SSServPar   servPar, 
    final SSUri livingDoc) throws SSErr{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.livingDocId, livingDoc);
      
      dbSQL.deleteIgnore(servPar, SSLivingDocSQLTableE.livingdoc, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
