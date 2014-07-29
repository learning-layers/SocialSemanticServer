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

import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.service.disc.datatypes.SSDisc;
import at.kc.tugraz.ss.service.disc.datatypes.SSDiscEntry;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
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

      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.discId, disc);
    
      resultSet = dbSQL.select(discUserTable, wheres);

      return getURIsFromResult(resultSet, SSSQLVarU.userId);
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
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.userId, userUri);
      
      resultSet = dbSQL.select(discUserTable, wheres);
      
      return getURIsFromResult(resultSet, SSSQLVarU.discId);
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
      
      table     (tables,    discUserTable);
      table     (tables,    discTable);     
      column    (columns,   discUserTable,      SSSQLVarU.discId);
      where     (wheres,    SSSQLVarU.userId,   userUri);
      where     (wheres,    SSSQLVarU.entityId, targetUri);
      tableCon  (tableCons, discTable,          SSSQLVarU.discId, discUserTable, SSSQLVarU.discId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      return getURIsFromResult(resultSet, SSSQLVarU.discId);
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
      
      insert(inserts, SSSQLVarU.discId,      disc);
      insert(inserts, SSSQLVarU.entityId,    target);
      
      dbSQL.insert(discTable, inserts);
      
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
      
      insert(inserts, SSSQLVarU.discId,   disc);
      insert(inserts, SSSQLVarU.userId,   user);
      
      dbSQL.insert(discUserTable, inserts);
      
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
      
      insert(inserts, SSSQLVarU.discEntryId,      discEntryUri);
      insert(inserts, SSSQLVarU.discEntryContent, content);
      
      dbSQL.insert(discEntryTable, inserts);
      
      discEntryCount = getDiscEntryCount(discUri);
      discEntryCount++;
      
      inserts.clear();
      
      insert(inserts, SSSQLVarU.discId,      discUri);
      insert(inserts, SSSQLVarU.discEntryId, discEntryUri);
      insert(inserts, SSSQLVarU.pos,         discEntryCount);
      
      dbSQL.insert(discEntriesTable, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Integer getDiscEntryCount(
    final SSUri discUri) throws Exception{
    
    ResultSet resultSet      = null;
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.discId, discUri);
      
      resultSet = dbSQL.select(discEntriesTable, wheres);
      
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
      
      table     (tables,    entityTable);
      table     (tables,    discTable);      
      column    (columns,   SSSQLVarU.discId);
      column    (columns,   SSSQLVarU.type);
      where     (wheres,    SSSQLVarU.discId, entityUri);
      where     (wheres,    SSSQLVarU.type,   discType);
      tableCon  (tableCons, discTable,        SSSQLVarU.discId, entityTable, SSSQLVarU.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
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
      final Map<String, String> wheres =  new HashMap<>();
      
      where(wheres, SSSQLVarU.discId, entityUri);
      
      resultSet = dbSQL.select(discTable, wheres);
      
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
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.discEntryId, entityUri);
      
      resultSet = dbSQL.select(discEntriesTable, wheres);
      
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
      
      table     (tables, entityTable);
      table     (tables, discTable);
      column    (columns,   SSSQLVarU.label);
      column    (columns,   SSSQLVarU.author);
      column    (columns,   SSSQLVarU.discId);
      column    (columns,   SSSQLVarU.entityId);
      column    (columns,   SSSQLVarU.type);
      column    (columns,   SSSQLVarU.description);
      column    (columns,   SSSQLVarU.creationTime);
      where     (wheres,    SSSQLVarU.discId, discUri);
      tableCon  (tableCons, discTable,        SSSQLVarU.discId, entityTable, SSSQLVarU.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      checkFirstResult(resultSet);
      
      return SSDisc.get(
        discUri,
        bindingStrToLabel      (resultSet, SSSQLVarU.label),
        bindingStrToUri        (resultSet, SSSQLVarU.author),
        bindingStrToUri        (resultSet, SSSQLVarU.entityId),
        bindingStrToEntityType (resultSet, SSSQLVarU.type),
        null,
        SSTextComment.get(bindingStr(resultSet, SSSQLVarU.description)),
        bindingStrToLong(resultSet, SSSQLVarU.creationTime),
        null, 
        null,
        null);
      
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
      
      delete(deletes, SSSQLVarU.discId, discUri);
      
      dbSQL.delete(discTable, deletes);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void unlinkDisc(
    final SSUri userUri, 
    final SSUri discUri) throws Exception{
    
    try{
      final Map<String, String> deletes = new HashMap<>();
      
      delete(deletes, SSSQLVarU.userId, userUri);
      delete(deletes, SSSQLVarU.discId, discUri);
      
      dbSQL.delete(discUserTable, deletes);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getDiscEntryURIs(final SSUri disc) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns       = new ArrayList<>();
      final Map<String, String> wheres        = new HashMap<>();
      
      column(columns, SSSQLVarU.discEntryId);
      
      where(wheres, SSSQLVarU.discId, disc);
      
      resultSet = dbSQL.select(discEntriesTable, columns, wheres);
      
      return getURIsFromResult(resultSet, SSSQLVarU.discEntryId);
      
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
      
      table(tables, entityTable);
      table(tables, discEntriesTable);
      table(tables, discEntryTable);
      
      column(columns, SSSQLVarU.author);
      column(columns, SSSQLVarU.discEntryContent);
      column(columns, SSSQLVarU.pos);
      column(columns, SSSQLVarU.type);
      column(columns, SSSQLVarU.creationTime);
      column(columns, discEntriesTable, SSSQLVarU.discEntryId);
      
      where(wheres, SSSQLVarU.discId, discUri);
      
      tableCon(tableCons, discEntriesTable, SSSQLVarU.discEntryId, entityTable, SSSQLVarU.id);
      tableCon(tableCons, discEntryTable,   SSSQLVarU.discEntryId, entityTable, SSSQLVarU.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      while(resultSet.next()){
        
        discEntries.add(
          SSDiscEntry.get(
            bindingStrToUri        (resultSet, SSSQLVarU.discEntryId),
            bindingStrToEntityType (resultSet, SSSQLVarU.type),
            bindingStrToInteger    (resultSet, SSSQLVarU.pos),
            SSTextComment.get      (bindingStr(resultSet, SSSQLVarU.discEntryContent)),
            bindingStrToLong       (resultSet, SSSQLVarU.creationTime),
            null,
            bindingStrToUri        (resultSet, SSSQLVarU.author), 
            null));
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

      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.userId, user);
      where(wheres, SSSQLVarU.discId, disc);
    
      resultSet = dbSQL.select(discUserTable, wheres);

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

      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.userId, user);
    
      resultSet = dbSQL.select(discUserTable, wheres);

      return getStringsFromResult(resultSet, SSSQLVarU.discId);
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
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.discEntryId, entityUri);
      
      resultSet = dbSQL.select(discEntriesTable, wheres);
      
      return getStringsFromResult(resultSet, SSSQLVarU.discId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}