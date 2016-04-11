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
package at.tugraz.sss.servs.disc.impl;

import at.tugraz.sss.serv.util.SSSQLVarNames;
import at.tugraz.sss.serv.datatype.*;

import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.servs.disc.datatype.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSDiscSQL extends SSCoreSQL {

  public SSDiscSQL(
    final SSDBSQLI dbSQLI){
    
    super(dbSQLI);
  }

  public List<SSUri> getDiscUserURIs(
    final SSServPar servPar,
    final SSUri disc) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{

      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns,  SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.discId, disc);
    
      resultSet = dbSQL.select(servPar, SSDiscSQLTableE.discuser, columns, wheres, null, null, null);

      return getURIsFromResult(resultSet, SSSQLVarNames.userId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getDiscURIs(
    final SSServPar servPar,
    final SSUri     userUri) throws SSErr{

    ResultSet resultSet = null;

    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.discId);
      
      if(userUri != null){
        where(wheres, SSSQLVarNames.userId, userUri);
      
        resultSet = dbSQL.select(servPar, SSDiscSQLTableE.discuser, columns, wheres, null, null, null);
      }else{
        resultSet = dbSQL.select(servPar, SSDiscSQLTableE.disc,     columns, wheres, null, null, null);
      }
      
      return getURIsFromResult(resultSet, SSSQLVarNames.discId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getDiscURIsForTarget(
    final SSServPar servPar,
    final SSUri forUser,
    final SSUri target) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      if(target == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        columns   = new ArrayList<>();
      final List<SSSQLTableI>   tables    = new ArrayList<>();
      final List<String>        tableCons = new ArrayList<>();      
      
      column    (columns,   SSDiscSQLTableE.disc,      SSSQLVarNames.discId);
      
      table(tables, SSDiscSQLTableE.disc);
      table(tables, SSDiscSQLTableE.disctargets);
      
      where     (wheres,    SSDiscSQLTableE.disctargets, SSSQLVarNames.targetId, target);
      
      tableCon  (tableCons, SSDiscSQLTableE.disc, SSSQLVarNames.discId, SSDiscSQLTableE.disctargets, SSSQLVarNames.discId);
      
      if(forUser != null){

        table     (tables,    SSDiscSQLTableE.discuser);
        
        where     (wheres,    SSSQLVarNames.userId,   forUser);
        
        tableCon  (tableCons, SSDiscSQLTableE.disc, SSSQLVarNames.discId, SSDiscSQLTableE.discuser, SSSQLVarNames.discId);
      }
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.discId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void createDisc(
    final SSServPar servPar,
    final SSUri         user,
    final SSUri         disc) throws SSErr{
    
    try{
      final Map<String, String> inserts =  new HashMap<>();
      
      insert(inserts, SSSQLVarNames.discId,      disc);
      
      dbSQL.insert(servPar, SSDiscSQLTableE.disc, inserts);
      
      addDisc(servPar, disc, user);

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addDiscTargets(
    final SSServPar servPar,
    final SSUri       disc, 
    final List<SSUri> targets) throws SSErr{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      for(SSUri target : targets){
      
        inserts.clear();
        
        insert(inserts, SSSQLVarNames.discId,       disc);
        insert(inserts, SSSQLVarNames.targetId,      target);
        
        uniqueKeys.clear();
        
        uniqueKey(uniqueKeys, SSSQLVarNames.discId,     disc);
        uniqueKey(uniqueKeys, SSSQLVarNames.targetId,   target);
      
        dbSQL.insertIfNotExists(servPar, SSDiscSQLTableE.disctargets, inserts, uniqueKeys);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addDisc(
    final SSServPar servPar,
    final SSUri    disc,
    final SSUri    user) throws SSErr{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.discId,   disc);
      insert(inserts, SSSQLVarNames.userId,   user);
      
      dbSQL.insert(servPar, SSDiscSQLTableE.discuser, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void addDiscEntry(
    final SSServPar servPar,
    final SSUri         discEntryUri, 
    final SSUri         discUri, 
    final SSTextComment content) throws SSErr{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      Integer                   discEntryCount;
      
      insert(inserts, SSSQLVarNames.discEntryId,      discEntryUri);
      insert(inserts, SSSQLVarNames.discEntryContent, content);
      insert(inserts, SSSQLVarNames.accepted,         false);
      
      dbSQL.insert(servPar, SSDiscSQLTableE.discentry, inserts);
      
      discEntryCount = getDiscEntryCount(servPar, discUri);
      discEntryCount++;
      
      inserts.clear();
      
      insert(inserts, SSSQLVarNames.discId,      discUri);
      insert(inserts, SSSQLVarNames.discEntryId, discEntryUri);
      insert(inserts, SSSQLVarNames.pos,         discEntryCount);
      
      dbSQL.insert(servPar, SSDiscSQLTableE.discentries, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Integer getDiscEntryCount(
    final SSServPar servPar,
    final SSUri discUri) throws SSErr{
    
    ResultSet resultSet      = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.discId);
      
      where(wheres, SSSQLVarNames.discId, discUri);
      
      resultSet = dbSQL.select(servPar, SSDiscSQLTableE.discentries, columns, wheres, null, null, null);
      
      resultSet.last();
      
      return resultSet.getRow();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public boolean isDisc(
    final SSServPar servPar,
    final SSUri     entityUri,
    final SSEntityE discType) throws SSErr {
    
    ResultSet resultSet   = null;
    
    try{
      final List<SSSQLTableI>   tables    = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        columns   = new ArrayList<>();
      final List<String>        tableCons = new ArrayList<>();
      
      table     (tables,    SSEntitySQLTableE.entity);
      table     (tables,    SSDiscSQLTableE.disc);      
      column    (columns,   SSSQLVarNames.discId);
      column    (columns,   SSSQLVarNames.type);
      where     (wheres,    SSSQLVarNames.discId, entityUri);
      where     (wheres,    SSSQLVarNames.type,   discType);
      tableCon  (tableCons, SSDiscSQLTableE.disc, SSSQLVarNames.discId, SSEntitySQLTableE.entity, SSSQLVarNames.id);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public boolean isDisc(
    final SSServPar servPar,
    final SSUri entityUri) throws SSErr {
    
    ResultSet resultSet   = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.discId);
      
      where(wheres, SSSQLVarNames.discId, entityUri);
      
      resultSet = dbSQL.select(servPar, SSDiscSQLTableE.disc, columns, wheres, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public boolean isDiscEntry(
    final SSServPar servPar,
    final SSUri entityUri) throws SSErr {
    
    ResultSet resultSet   = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.discEntryId);
      
      where(wheres, SSSQLVarNames.discEntryId, entityUri);
      
      resultSet = dbSQL.select(servPar, SSDiscSQLTableE.discentries, columns, wheres, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  private SSDisc getDiscWithoutEntries(
    final SSServPar servPar,
    final SSUri discUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<SSSQLTableI>   tables     = new ArrayList<>();
      final List<String>        columns    = new ArrayList<>();
      final List<String>        tableCons  = new ArrayList<>();
      final Map<String, String> wheres     = new HashMap<>();
      final SSDisc              discObj;
      
      column    (columns,   SSSQLVarNames.discId);
      column    (columns,   SSSQLVarNames.type);

      table     (tables, SSEntitySQLTableE.entity);
      table     (tables, SSDiscSQLTableE.disc);
      
      where     (wheres,    SSSQLVarNames.discId, discUri);
      
      tableCon  (tableCons, SSDiscSQLTableE.disc,        SSSQLVarNames.discId, SSEntitySQLTableE.entity, SSSQLVarNames.id);      
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      discObj =
        SSDisc.get(
          discUri,
          bindingStrToEntityType (resultSet, SSSQLVarNames.type),
          getDiscTargets(servPar, discUri));
      
      return discObj;
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  private List<SSEntity> getDiscTargets(
    final SSServPar servPar,
    final SSUri discUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns    = new ArrayList<>();
      final Map<String, String> wheres     = new HashMap<>();
      
      column(columns, SSSQLVarNames.targetId);
      
      where(wheres, SSSQLVarNames.discId, discUri);
      
      resultSet = dbSQL.select(servPar, SSDiscSQLTableE.disctargets, columns, wheres, null, null, null);
      
      return getEntitiesFromResult(resultSet, SSSQLVarNames.targetId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void deleteDisc(
    final SSServPar servPar,
    final SSUri discUri) throws SSErr{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.discId, discUri);
      
      dbSQL.delete(servPar, SSDiscSQLTableE.disc, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void unlinkDisc(
    final SSServPar servPar,
    final SSUri userUri, 
    final SSUri discUri) throws SSErr{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.userId, userUri);
      where(wheres, SSSQLVarNames.discId, discUri);
      
      dbSQL.delete(servPar, SSDiscSQLTableE.discuser, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getDiscEntryURIs(
    final SSServPar servPar,
    final SSUri disc) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns       = new ArrayList<>();
      final Map<String, String> wheres        = new HashMap<>();
      
      column(columns, SSSQLVarNames.discEntryId);
      
      where(wheres, SSSQLVarNames.discId, disc);
      
      resultSet = dbSQL.select(servPar, SSDiscSQLTableE.discentries, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.discEntryId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSDisc getDisc(
    final SSServPar servPar,
    final SSUri   discUri,
    final boolean setEntries) throws SSErr {
    
    ResultSet resultSet = null;
    
    try{
      
      final SSDisc              disc          = getDiscWithoutEntries(servPar, discUri);
      
      if(!setEntries){
        return disc;
      }
      
      final List<SSSQLTableI>   tables        = new ArrayList<>();
      final List<String>        columns       = new ArrayList<>();
      final List<String>        tableCons     = new ArrayList<>();
      final Map<String, String> wheres        = new HashMap<>();
      
      column(columns, SSSQLVarNames.discEntryContent);
      column(columns, SSSQLVarNames.pos);
      column(columns, SSSQLVarNames.type);
      column(columns, SSSQLVarNames.accepted);
      column(columns, SSDiscSQLTableE.discentries, SSSQLVarNames.discEntryId);
      
      table(tables, SSEntitySQLTableE.entity);
      table(tables, SSDiscSQLTableE.discentries);
      table(tables, SSDiscSQLTableE.discentry);
      
      where(wheres, SSSQLVarNames.discId, discUri);
      
      tableCon(tableCons, SSDiscSQLTableE.discentries,     SSSQLVarNames.discEntryId, SSEntitySQLTableE.entity, SSSQLVarNames.id);
      tableCon(tableCons, SSDiscSQLTableE.discentry,       SSSQLVarNames.discEntryId, SSEntitySQLTableE.entity, SSSQLVarNames.id);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        disc.entries.add( 
          SSDiscEntry.get(
            bindingStrToUri         (resultSet, SSSQLVarNames.discEntryId),
            bindingStrToEntityType  (resultSet, SSSQLVarNames.type),
            bindingStrToInteger     (resultSet, SSSQLVarNames.pos),
            bindingStrToTextComment (resultSet, SSSQLVarNames.discEntryContent), 
            bindingStrToBoolean     (resultSet, SSSQLVarNames.accepted)));
      }
      
      return disc;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public boolean ownsUserDisc(
    final SSServPar servPar,
    final SSUri user, 
    final SSUri disc) throws SSErr {
    
    ResultSet resultSet = null;
    
    try{

      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.discId);
      
      where(wheres, SSSQLVarNames.userId, user);
      where(wheres, SSSQLVarNames.discId, disc);
    
      resultSet = dbSQL.select(servPar, SSDiscSQLTableE.discuser, columns, wheres, null, null, null);

      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  } 

  public void acceptEntry(
    final SSServPar servPar,
    final SSUri entry) throws SSErr {
    
    try{
      final Map<String, String>  wheres   = new HashMap<>();
      final Map<String, String>  updates  = new HashMap<>();
      
      where(wheres, SSSQLVarNames.discEntryId, entry);
      
      update (updates, SSSQLVarNames.accepted, true);
      
      dbSQL.update(servPar, SSDiscSQLTableE.discentry, wheres, updates);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public SSUri getDiscForEntry(
    final SSServPar servPar,
    final SSUri entry) throws SSErr {
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.discId);
      
      where(wheres, SSSQLVarNames.discEntryId, entry);
      
      resultSet = dbSQL.select(servPar, SSDiscSQLTableE.discentries, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return bindingStrToUri(resultSet, SSSQLVarNames.discId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void updateEntryContent(
    final SSServPar servPar,
    final SSUri         entry, 
    final SSTextComment content) throws SSErr{
    
    try{
      final Map<String, String>  wheres   = new HashMap<>();
      final Map<String, String>  updates  = new HashMap<>();
      
      where(wheres, SSSQLVarNames.discEntryId, entry);
      
      update (updates, SSSQLVarNames.discEntryContent, content);
      
      dbSQL.update(servPar, SSDiscSQLTableE.discentry, wheres, updates);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void updateDiscContent(
    final SSServPar     servPar,
    final SSUri         disc, 
    final SSTextComment content) throws SSErr{
    
    try{
      final Map<String, String>  wheres   = new HashMap<>();
      final Map<String, String>  updates  = new HashMap<>();
      
      where(wheres, SSSQLVarNames.discId, disc);
      
      update (updates, SSDiscSQLTableE.disc.toString(), content);
      
      dbSQL.update(servPar, SSDiscSQLTableE.disc, wheres, updates);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}

//  public List<SSUri> getDiscURIs() throws SSErr{
//
//    ResultSet resultSet = null;
//
//    try{
//      resultSet = dbSQL.select(disc);
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
//    final SSUri targetUri) throws SSErr {
//  
//    ResultSet resultSet = null;
//
//    try{
//      final Map<String, String> wheres = new HashMap<>();
//      
//      where(wheres, SSSQLVarU.target, targetUri);
//      
//      resultSet = dbSQL.select(disc, wheres);
//      
//      return getURIsFromResult(resultSet, SSSQLVarU.discId);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }