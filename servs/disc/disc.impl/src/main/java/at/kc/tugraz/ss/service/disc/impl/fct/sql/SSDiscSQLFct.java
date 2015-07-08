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
package at.kc.tugraz.ss.service.disc.impl.fct.sql;

import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSUri;

import at.tugraz.sss.serv.SSServImplWithDBA;
import at.kc.tugraz.ss.service.disc.datatypes.SSDisc;
import at.kc.tugraz.ss.service.disc.datatypes.SSDiscEntry;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSEntityA;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSDiscSQLFct extends SSDBSQLFct {

  public SSDiscSQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }

//  public List<SSUri> getDiscURIs() throws Exception{
//
//    ResultSet resultSet = null;
//
//    try{
//      resultSet = dbSQL.select(discTable);
//
//      return getURIsFromResult(resultSet, SSSQLVarU.discId);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
  
  //  public List<SSUri> getDiscURIsForTarget(
//    final SSUri targetUri) throws Exception {
//  
//    ResultSet resultSet = null;
//
//    try{
//      final Map<String, String> wheres = new HashMap<>();
//      
//      where(wheres, SSSQLVarU.target, targetUri);
//      
//      resultSet = dbSQL.select(discTable, wheres);
//      
//      return getURIsFromResult(resultSet, SSSQLVarU.discId);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
  
  public List<SSUri> getDiscUserURIs(
    final SSUri disc) throws Exception{
    
    ResultSet resultSet = null;
    
    try{

      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns,  SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.discId, disc);
    
      resultSet = dbSQL.select(SSSQLVarNames.discUserTable, columns, wheres, null, null, null);

      return getURIsFromResult(resultSet, SSSQLVarNames.userId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getDiscURIs(
    final SSUri userUri) throws Exception{

    ResultSet resultSet = null;

    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.discId);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.discUserTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.discId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getDiscURIs(
    final SSUri userUri,
    final SSUri targetUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        tables    = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        columns   = new ArrayList<>();
      final List<String>        tableCons = new ArrayList<>();
      
      table     (tables, SSSQLVarNames.discUserTable);
      table     (tables, SSSQLVarNames.discTable);     
      column    (columns, SSSQLVarNames.discUserTable,      SSSQLVarNames.discId);
      where     (wheres,    SSSQLVarNames.userId,   userUri);
      where     (wheres,    SSSQLVarNames.entityId, targetUri);
      tableCon  (tableCons, SSSQLVarNames.discTable,          SSSQLVarNames.discId, SSSQLVarNames.discUserTable, SSSQLVarNames.discId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.discId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void createDisc(
    final SSUri         user,
    final SSUri         disc,
    final SSUri         target) throws Exception{
    
    try{
      final Map<String, String> inserts =  new HashMap<>();
      
      insert(inserts, SSSQLVarNames.discId,      disc);
      insert(inserts, SSSQLVarNames.entityId,    target);
      
      dbSQL.insert(SSSQLVarNames.discTable, inserts);
      
      addDisc(disc, user);

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addDisc(
    final SSUri    disc,
    final SSUri    user) throws Exception{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.discId,   disc);
      insert(inserts, SSSQLVarNames.userId,   user);
      
      dbSQL.insert(SSSQLVarNames.discUserTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void addDiscEntry(
    final SSUri         discEntryUri, 
    final SSUri         discUri, 
    final SSTextComment content) throws Exception{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      Integer                   discEntryCount;
      
      insert(inserts, SSSQLVarNames.discEntryId,      discEntryUri);
      insert(inserts, SSSQLVarNames.discEntryContent, content);
      
      dbSQL.insert(SSSQLVarNames.discEntryTable, inserts);
      
      discEntryCount = getDiscEntryCount(discUri);
      discEntryCount++;
      
      inserts.clear();
      
      insert(inserts, SSSQLVarNames.discId,      discUri);
      insert(inserts, SSSQLVarNames.discEntryId, discEntryUri);
      insert(inserts, SSSQLVarNames.pos,         discEntryCount);
      
      dbSQL.insert(SSSQLVarNames.discEntriesTable, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Integer getDiscEntryCount(
    final SSUri discUri) throws Exception{
    
    ResultSet resultSet      = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.discId);
      
      where(wheres, SSSQLVarNames.discId, discUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.discEntriesTable, columns, wheres, null, null, null);
      
      resultSet.last();
      
      return resultSet.getRow();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public Boolean isDisc(
    final SSUri     entityUri,
    final SSEntityE discType) throws Exception {
    
    ResultSet resultSet   = null;
    
    try{
      final List<String>        tables    = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        columns   = new ArrayList<>();
      final List<String>        tableCons = new ArrayList<>();
      
      table     (tables, SSSQLVarNames.entityTable);
      table     (tables, SSSQLVarNames.discTable);      
      column    (columns,   SSSQLVarNames.discId);
      column    (columns,   SSSQLVarNames.type);
      where     (wheres,    SSSQLVarNames.discId, entityUri);
      where     (wheres,    SSSQLVarNames.type,   discType);
      tableCon  (tableCons, SSSQLVarNames.discTable,        SSSQLVarNames.discId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public Boolean isDisc(
    final SSUri entityUri) throws Exception {
    
    ResultSet resultSet   = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.discId);
      
      where(wheres, SSSQLVarNames.discId, entityUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.discTable, columns, wheres, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public Boolean isDiscEntry(
    final SSUri entityUri) throws Exception {
    
    ResultSet resultSet   = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.discEntryId);
      
      where(wheres, SSSQLVarNames.discEntryId, entityUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.discEntriesTable, columns, wheres, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSDisc getDiscWithoutEntries(
    final SSUri discUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        tables     = new ArrayList<>();
      final List<String>        columns    = new ArrayList<>();
      final List<String>        tableCons  = new ArrayList<>();
      final Map<String, String> wheres     = new HashMap<>();
      final SSDisc              discObj;
      
      table     (tables, SSSQLVarNames.entityTable);
      table     (tables, SSSQLVarNames.discTable);
      
      column    (columns,   SSSQLVarNames.discId);
      column    (columns,   SSSQLVarNames.entityId);
      column    (columns,   SSSQLVarNames.type);
      
      where     (wheres,    SSSQLVarNames.discId, discUri);
      
      tableCon  (tableCons, SSSQLVarNames.discTable,        SSSQLVarNames.discId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      checkFirstResult(resultSet);
      
      discObj =
        SSDisc.get(
          discUri,
          bindingStrToEntityType (resultSet, SSSQLVarNames.type),
          null, //label
          bindingStrToUri        (resultSet, SSSQLVarNames.entityId));
      
      return discObj;
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void deleteDisc(
    final SSUri discUri) throws Exception{
    
    try{
      final Map<String, String> deletes = new HashMap<>();
      
      delete(deletes, SSSQLVarNames.discId, discUri);
      
      dbSQL.delete(SSSQLVarNames.discTable, deletes);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void unlinkDisc(
    final SSUri userUri, 
    final SSUri discUri) throws Exception{
    
    try{
      final Map<String, String> deletes = new HashMap<>();
      
      delete(deletes, SSSQLVarNames.userId, userUri);
      delete(deletes, SSSQLVarNames.discId, discUri);
      
      dbSQL.delete(SSSQLVarNames.discUserTable, deletes);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getDiscEntryURIs(final SSUri disc) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns       = new ArrayList<>();
      final Map<String, String> wheres        = new HashMap<>();
      
      column(columns, SSSQLVarNames.discEntryId);
      
      where(wheres, SSSQLVarNames.discId, disc);
      
      resultSet = dbSQL.select(SSSQLVarNames.discEntriesTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.discEntryId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSDisc getDiscWithEntries(
    final SSUri   discUri) throws Exception {
    
    ResultSet resultSet = null;
    
    try{
      final List<SSEntityA>     discEntries   = new ArrayList<>();
      final List<String>        tables        = new ArrayList<>();
      final List<String>        columns       = new ArrayList<>();
      final List<String>        tableCons     = new ArrayList<>();
      final Map<String, String> wheres        = new HashMap<>();
      final SSDisc              disc          = getDiscWithoutEntries(discUri);
      SSDiscEntry               discEntryObj;
      
      table(tables, SSSQLVarNames.entityTable);
      table(tables, SSSQLVarNames.discEntriesTable);
      table(tables, SSSQLVarNames.discEntryTable);
      
      column(columns, SSSQLVarNames.discEntryContent);
      column(columns, SSSQLVarNames.pos);
      column(columns, SSSQLVarNames.type);
      column(columns, SSSQLVarNames.discEntriesTable, SSSQLVarNames.discEntryId);
      
      where(wheres, SSSQLVarNames.discId, discUri);
      
      tableCon(tableCons, SSSQLVarNames.discEntriesTable,     SSSQLVarNames.discEntryId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      tableCon(tableCons, SSSQLVarNames.discEntryTable,       SSSQLVarNames.discEntryId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        discEntryObj = 
          SSDiscEntry.get(bindingStrToUri         (resultSet, SSSQLVarNames.discEntryId),
            bindingStrToEntityType  (resultSet, SSSQLVarNames.type),
            bindingStrToInteger     (resultSet, SSSQLVarNames.pos),
            bindingStrToTextComment (resultSet, SSSQLVarNames.discEntryContent));
          
        discEntries.add(discEntryObj);
      }
      
      disc.entries.addAll(discEntries);
      
      return disc;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public Boolean ownsUserDisc(
    final SSUri user, 
    final SSUri disc) throws Exception {
    
    ResultSet resultSet = null;
    
    try{

      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.discId);
      
      where(wheres, SSSQLVarNames.userId, user);
      where(wheres, SSSQLVarNames.discId, disc);
    
      resultSet = dbSQL.select(SSSQLVarNames.discUserTable, columns, wheres, null, null, null);

      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  } 

  public List<String> getDiscURIsForUser(
    final SSUri user) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.discId);
      
      where(wheres, SSSQLVarNames.userId, user);
    
      resultSet = dbSQL.select(SSSQLVarNames.discUserTable, columns, wheres, null, null, null);

      return getStringsFromResult(resultSet, SSSQLVarNames.discId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public List<String> getDiscURIsContainingEntry(
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.discId);
      
      where(wheres, SSSQLVarNames.discEntryId, entityUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.discEntriesTable, columns, wheres, null, null, null);
      
      return getStringsFromResult(resultSet, SSSQLVarNames.discId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}