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
package at.tugraz.sss.servs.coll.impl;

import at.tugraz.sss.servs.db.impl.SSCoreSQL;
import at.tugraz.sss.servs.db.api.SSSQLTableI;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSEntitySQLTableE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.util.SSSQLVarNames;
import at.tugraz.sss.servs.entity.datatype.SSCircleE;
import at.tugraz.sss.servs.db.api.SSDBSQLI;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.coll.datatype.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCollSQL extends SSCoreSQL{

  public SSCollSQL(
    final SSDBSQLI dbSQL){
    
    super(dbSQL);
  }
  
  public SSUri addColl(
    final SSServPar servPar,
    final SSUri collUri) throws SSErr{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.collId, collUri);
      
      dbSQL.insert(servPar, SSCollSQLTableE.coll, inserts);
      
      return collUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public void addCollRoot(
    final SSServPar servPar,
    final SSUri   collUri, 
    final SSUri   userUri) throws SSErr{
    
    try{
      
      final Map<String, String> inserts = new HashMap<>();

      //add coll to coll root table
      insert (inserts, SSSQLVarNames.userId, userUri);
      insert (inserts, SSSQLVarNames.collId, collUri);
      
      dbSQL.insert(servPar, SSCollSQLTableE.collroot, inserts);
      
      //add coll to user coll table
      inserts.clear();
      insert(inserts, SSSQLVarNames.userId, userUri);
      insert(inserts, SSSQLVarNames.collId, collUri);
      
      dbSQL.insert(servPar, SSCollSQLTableE.colluser, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public boolean isCollRoot(
    final SSServPar servPar,
    final SSUri collUri) throws SSErr{
    
    ResultSet  resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.collId, collUri);
      
      resultSet = dbSQL.select(servPar, SSCollSQLTableE.collroot, columns, wheres, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  //TODO dtheiler: special coll could be merged with root coll table
  public void addCollSpecial(
    final SSServPar servPar,
    final SSUri   collUri, 
    final SSUri   userUri) throws SSErr{
    
    try{
      
      final Map<String, String> inserts = new HashMap<>();

      //add coll to special coll table
      insert(inserts, SSSQLVarNames.userId, userUri);
      insert(inserts, SSSQLVarNames.collId, collUri);
      
      dbSQL.insert(servPar, SSCollSQLTableE.collspecial, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public boolean isCollSpecial(
    final SSServPar servPar,
    final SSUri collUri) throws SSErr{
    
    ResultSet  resultSet               = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.collId, collUri);
      
      resultSet = dbSQL.select(servPar, SSCollSQLTableE.collspecial, columns, wheres, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
   public SSUri getSpecialCollURI(
     final SSServPar servPar,
     final SSUri userUri) throws SSErr{
    
     ResultSet  resultSet = null;
     
     try{
      
       final List<String>        columns = new ArrayList<>();
       final Map<String, String> wheres  = new HashMap<>();
       
       column(columns, SSSQLVarNames.collId);
       
       where(wheres, SSSQLVarNames.userId, userUri);
       
       resultSet = dbSQL.select(servPar, SSCollSQLTableE.collspecial, columns, wheres, null, null, null);
       
       if(!existsFirstResult(resultSet)){
        return null;
      }
       
       return bindingStrToUri(resultSet, SSSQLVarNames.collId);
       
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public boolean isColl(
    final SSServPar servPar,
    final SSUri entityUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.collId, entityUri);
      
      resultSet = dbSQL.select(servPar, SSCollSQLTableE.coll, columns, wheres, null, null, null);
      
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addCollEntry(
    final SSServPar servPar,
    final SSUri      collParent,
    final SSUri      collEntry) throws SSErr{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      //add coll entry to coll entry pos table
      Integer collEntryCount = getCollEntryCount(servPar, collParent);
      
      collEntryCount++;
      
      insert(inserts, SSSQLVarNames.collId,  collParent);
      insert(inserts, SSSQLVarNames.entryId, collEntry);
      insert(inserts, SSSQLVarNames.pos,     collEntryCount);
      
      dbSQL.insert(servPar, SSCollSQLTableE.collentrypos, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public Integer getCollEntryCount(
    final SSServPar servPar,
    final SSUri collUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.collId, collUri);
      
      resultSet = dbSQL.select(servPar, SSCollSQLTableE.collentrypos, columns, wheres, null, null, null);
      
      resultSet.last();
      
      return resultSet.getRow();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getPublicCollURIs(
    final SSServPar servPar) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
    
      final List<SSSQLTableI>   tables      = new ArrayList<>();
      final List<String>        columns     = new ArrayList<>();
      final Map<String, String> wheres      = new HashMap<>();
      final List<String>        tableCons   = new ArrayList<>();
      
      column(columns, SSSQLVarNames.id);
      column(columns, SSSQLVarNames.collId);
      column(columns, SSSQLVarNames.entityId);
      column(columns, SSSQLVarNames.circleType);
      column(columns, SSEntitySQLTableE.circle,          SSSQLVarNames.circleId);
      column(columns, SSEntitySQLTableE.circleentities,  SSSQLVarNames.circleId);
      
      table(tables, SSCollSQLTableE.coll);
      table(tables, SSEntitySQLTableE.entity);
      table(tables, SSEntitySQLTableE.circle);
      table(tables, SSEntitySQLTableE.circleentities);
      
      where (wheres,  SSSQLVarNames.circleType, SSCircleE.pub);
      
      tableCon(tableCons, SSEntitySQLTableE.entity,         SSSQLVarNames.id,       SSCollSQLTableE.coll,     SSSQLVarNames.collId);
      tableCon(tableCons, SSEntitySQLTableE.circleentities, SSSQLVarNames.circleId, SSEntitySQLTableE.circle, SSSQLVarNames.circleId);
      tableCon(tableCons, SSEntitySQLTableE.circleentities, SSSQLVarNames.entityId, SSCollSQLTableE.coll,     SSSQLVarNames.collId);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  } 
  
  public List<SSUri> getUserCollURIs(
    final SSServPar servPar,
    final SSUri userUri) throws SSErr{
     
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
    
      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      
      resultSet = dbSQL.select(servPar, SSCollSQLTableE.colluser, columns, wheres, null, null, null);      
      
      return getURIsFromResult(resultSet, SSSQLVarNames.collId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }  
  
  public boolean ownsUserColl(
    final SSServPar servPar,
    final SSUri userUri, 
    final SSUri collUri) throws SSErr {
    
    ResultSet resultSet = null;
    
    try{

      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      where(wheres, SSSQLVarNames.collId, collUri);
    
      resultSet = dbSQL.select(servPar, SSCollSQLTableE.colluser, columns, wheres, null, null, null);

      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }  
  
  public void addCollToColl(
    final SSServPar   servPar,
    final SSUri       user,
    final SSUri       collParent,
    final SSUri       collChild,
    final boolean     createdCollIsInSharedOrPublicCircle,
    final boolean     addedCollIsSharedOrPublic) throws SSErr{
    
    try{
      
      if(createdCollIsInSharedOrPublicCircle && addedCollIsSharedOrPublic){
        throw new Exception("cannot add to shared coll and add shared/public coll at the same time");
      }
      
      final Map<String, String> inserts = new HashMap<>();
      
      //add relation of coll parent to child coll to hierarchy table
      inserts.clear();
      insert(inserts, SSSQLVarNames.collParentId, collParent);
      insert(inserts, SSSQLVarNames.collChildId,  collChild);
      
      dbSQL.insert(servPar, SSCollSQLTableE.collhierarchy, inserts);
      
      //add coll child to coll parent in coll entry pos table
      final Integer collEntryCount = getCollEntryCount(servPar, collParent) + 1;
      
      inserts.clear();
      insert(inserts, SSSQLVarNames.collId,   collParent);
      insert(inserts, SSSQLVarNames.entryId,  collChild);
      insert(inserts, SSSQLVarNames.pos,      collEntryCount);
      
      dbSQL.insert(servPar, SSCollSQLTableE.collentrypos, inserts);
      
      //add child coll to user coll table
      inserts.clear();
      insert(inserts, SSSQLVarNames.userId,    user);
      insert(inserts, SSSQLVarNames.collId,    collChild);
      
      dbSQL.insert(servPar, SSCollSQLTableE.colluser, inserts);
      
      //add currently added coll to other users as well
      if(createdCollIsInSharedOrPublicCircle){
        
        inserts.clear();
        insert(inserts, SSSQLVarNames.collId, collChild);
        
        for(SSUri coUserUri : getCollUserURIs(servPar, collParent)){
          
          if(SSStrU.isEqual(coUserUri, user)){
            continue;
          }
          
          insert(inserts, SSSQLVarNames.userId, coUserUri);
          
          dbSQL.insert(servPar, SSCollSQLTableE.colluser, inserts);
        }
      }
      
      //add sub colls of shared / pub coll for this user as well
      if(addedCollIsSharedOrPublic){
        
        final List<SSUri> subCollUris = new ArrayList<>();
        
        SSCollCommon.getAllChildCollURIs(servPar, this, collChild, collChild, subCollUris);
        
        inserts.clear();
        insert(inserts, SSSQLVarNames.userId, user);
        
        for(SSUri subCollUri : subCollUris){
          
          insert(inserts, SSSQLVarNames.collId, subCollUri);
          
          dbSQL.insert(servPar, SSCollSQLTableE.colluser, inserts);
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void unlinkCollAndSubColls(
    final SSServPar servPar,
    final SSUri userUri,
    final SSUri parentCollUri,
    final SSUri collUri) throws SSErr{
    
    try{
      final Map<String, String> wheres     = new HashMap<>();
      final List<SSUri>         subCollUris = new ArrayList<>();
      
      //remove sub colls of followed coll from user coll table as well
      SSCollCommon.getAllChildCollURIs(servPar, this, collUri, collUri, subCollUris);
      
      wheres.clear();
      where(wheres, SSSQLVarNames.userId, userUri);
      
      for(SSUri subCollUri : subCollUris){
        
        where(wheres, SSSQLVarNames.collId, subCollUri);
        
        dbSQL.delete(servPar, SSCollSQLTableE.colluser, wheres);
      }
      
      //remove coll from user coll table
      wheres.clear();
      where(wheres, SSSQLVarNames.userId,     userUri);
      where(wheres, SSSQLVarNames.collId,     collUri);
      
      dbSQL.delete(servPar, SSCollSQLTableE.colluser, wheres);
      
      //remove coll from coll hierarchy table
      wheres.clear();
      where(wheres, SSSQLVarNames.collParentId, parentCollUri);
      where(wheres, SSSQLVarNames.collChildId,  collUri);
      
      dbSQL.delete(servPar, SSCollSQLTableE.collhierarchy, wheres);
      
      //remove coll from coll entries pos table
      wheres.clear();
      where(wheres, SSSQLVarNames.collId,   parentCollUri.toString());
      where(wheres, SSSQLVarNames.entryId,  collUri.toString());
      
      dbSQL.delete(servPar, SSCollSQLTableE.collentrypos, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getDirectChildCollURIs(
    final SSServPar servPar,
    final SSUri collUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collChildId);
      
      where(wheres, SSSQLVarNames.collParentId, collUri);
      
      resultSet = dbSQL.select(servPar, SSCollSQLTableE.collhierarchy, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.collChildId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getDirectParentCollURIs(
    final SSServPar servPar,
    final SSUri collUri) throws SSErr{

    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collParentId);
      
      where(wheres, SSSQLVarNames.collChildId, collUri);
      
      resultSet = dbSQL.select(servPar, SSCollSQLTableE.collhierarchy, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.collParentId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void removeCollAndUnlinkSubColls(
    final SSServPar servPar,
    final SSUri userUri,
    final SSUri collUri) throws SSErr{
    
    try{
      
      final Map<String, String> wheres             = new HashMap<>();
      final List<SSUri>         directChildCollURIs = getDirectChildCollURIs(servPar, collUri);
      
      //unlink all direct sub colls (and hence their sub colls as well)
      for(SSUri subCollUri : directChildCollURIs){
        unlinkCollAndSubColls(servPar, userUri, collUri, subCollUri);
      }
      
      wheres.clear();
      where(wheres, SSSQLVarNames.id, collUri);
      
      dbSQL.delete(servPar, SSEntitySQLTableE.entity, wheres);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeCollEntry(
    final SSServPar servPar,
    final SSUri collUri,
    final SSUri collEntryUri) throws SSErr{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.collId,  collUri);
      where(wheres, SSSQLVarNames.entryId, collEntryUri);
      
      dbSQL.delete(servPar, SSCollSQLTableE.collentrypos, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void updateCollEntriesPos(
    final SSServPar servPar,
    final SSUri         collUri, 
    final List<SSUri>   collEntries,
    final List<Integer> order) throws SSErr{
    
    try{
      final Map<String, String> wheres   = new HashMap<>();
      final Map<String, String> updates  = new HashMap<>();
      Integer                   counter  = 0;
      
      where(wheres, SSSQLVarNames.collId, collUri);
      
      while(counter < collEntries.size()){
        
        where  (wheres,  SSSQLVarNames.entryId, collEntries.get(counter));
        update (updates, SSSQLVarNames.pos,     order.get      (counter));
        
        counter++;
        
        dbSQL.update(servPar, SSCollSQLTableE.collentrypos, wheres, updates);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private SSColl getColl(
    final SSServPar servPar,
    final SSUri           collUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSSQLTableI>   tables     = new ArrayList<>();
      final List<String>        columns    = new ArrayList<>();
      final List<String>        tableCons  = new ArrayList<>();
      final Map<String, String> wheres     = new HashMap<>();
      
      where    (wheres,    SSSQLVarNames.collId, collUri);
      
      table    (tables,    SSCollSQLTableE.coll);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return SSColl.get(collUri);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSColl getCollWithEntries(
    final SSServPar servPar,
    final SSUri  collUri) throws SSErr{

    ResultSet  resultSet = null;
    
    try{
      
      final List<SSSQLTableI>   tables    = new ArrayList<>();
      final List<String>        columns   = new ArrayList<>();
      final List<String>        tableCons = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      final SSColl              coll      = getColl(servPar, collUri);
      
      column   (columns,   SSSQLVarNames.entryId);
      column   (columns,   SSSQLVarNames.pos);
      column   (columns,   SSSQLVarNames.type);
      
      table    (tables,    SSCollSQLTableE.collentrypos);
      table    (tables,    SSEntitySQLTableE.entity);
      
      where(wheres, SSCollSQLTableE.collentrypos, SSSQLVarNames.collId, collUri);
        
      tableCon (tableCons, SSCollSQLTableE.collentrypos, SSSQLVarNames.entryId, SSEntitySQLTableE.entity,       SSSQLVarNames.id);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, SSSQLVarNames.pos, "ASC", null);
      
      while(resultSet.next()){
        
        coll.entries.add(
          SSCollEntry.get(
            bindingStrToUri        (resultSet, SSSQLVarNames.entryId),
            bindingStrToEntityType (resultSet, SSSQLVarNames.type), 
            bindingStrToInteger    (resultSet, SSSQLVarNames.pos)));
      }
      
      return coll;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public SSUri getRootCollURI(
    final SSServPar servPar,
    final SSUri userUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();

      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      
      resultSet = dbSQL.select(servPar, SSCollSQLTableE.collroot, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }

      return bindingStrToUri(resultSet, SSSQLVarNames.collId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public boolean containsCollEntry(
    final SSServPar servPar,
    final SSUri collUri, 
    final SSUri collEntryUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.collId,  collUri);
      where(wheres, SSSQLVarNames.entryId, collEntryUri);
      
      resultSet = dbSQL.select(servPar, SSCollSQLTableE.collentrypos, columns, wheres, null, null, null);
      
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public boolean existsCollRootForUser(
    final SSServPar servPar,
    final SSUri userUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();

      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      
      resultSet  = dbSQL.select(servPar, SSCollSQLTableE.collroot, columns, wheres, null, null, null);
      
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getCollUserURIs(
    final SSServPar servPar,
    final SSUri collUri) throws SSErr{

    ResultSet resultSet   = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();

      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.collId, collUri);
      
      resultSet = dbSQL.select(servPar, SSCollSQLTableE.colluser, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.userId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getCollURIsContainingEntity(
    final SSServPar servPar,
    final SSUri entityUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.collId);
      
      where(wheres, SSSQLVarNames.entryId, entityUri);
      
      resultSet = dbSQL.select(servPar, SSCollSQLTableE.collentrypos, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.collId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}

//  public SSSpaceEnum getUserCollSpace(SSUri user, SSUri coll) throws SSErr{
//    
//    Map<String, String> selectPars    = new HashMap<>();
//    ResultSet           resultSet     = null;
//    SSSpaceEnum         userCollSpace = null;
//    
//    selectPars.put(SSSQLVarU.userId, user.toString());
//    selectPars.put(SSSQLVarU.collId, coll.toString());
//    
//    try{
//      resultSet = dbSQL.dbSQLSelectAllWhere(collUserTable, selectPars);
//      
//      resultSet.first();
//      
//      userCollSpace = SSSpaceEnum.get(resultSet.getString(SSSQLVarU.collSpace));
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.dbSQLCloseStmt(resultSet);
//    }
//    
//    return userCollSpace;
//  }

//private boolean ownsUserCollByHierarchy(SSUri userUri, SSUri collUri) throws SSErr{
//    
//    final List<String> collParents    = new ArrayList<>();
//    final List<String> newCollParents = new ArrayList<>();
//    
//    try{
//      
//      collParents.addAll(getCollDirectParentURIs(collUri));
//      
//      while(true){
//        
//        for(String parentCollUri : collParents){
//          
//          for(String tmpUri : getCollDirectParentURIs(SSUri.get(parentCollUri))){
//          
//            if(!newCollParents.contains(tmpUri)){
//              newCollParents.add(tmpUri);
//            }
//          }
//        }
//        
//        if(newCollParents.isEmpty()){
//          return false;
//        }
//        
//        for(String newCollParentUri : newCollParents){
//          
//          if(ownsUserColl(userUri, SSUri.get(newCollParentUri))){
//            return true;
//          }
//        }
//        
//        collParents.clear();
//        collParents.addAll(newCollParents);
//        newCollParents.clear();
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//  public SSSpaceEnum getCollSpace(SSUri userUri, SSUri collUri) throws SSErr{
//    
//    if(
//      userUri      == null ||
//      collUri      == null){
//      SSServErrReg.regErrThrow(new Exception("parameter(s) null"));
//      return null;
//    }
//        
//    Map<String, String> selectPars  = new HashMap<>();
//    ResultSet           resultSet   = null;
//    SSSpaceEnum         collSpace   = null;
//    
//    selectPars.put(SSSQLVarU.userId, userUri.toString());
//    selectPars.put(SSSQLVarU.collId, collUri.toString());
//    
//    try{
//      
//      resultSet = dbSQL.dbSQLSelectAllWhere(collUserTable, selectPars);
//      
//      if(!resultSet.first()){
//        SSServErrReg.regErrThrow(new Exception("coll-user combination does not exist"));
//      }
//
//      collSpace = bindingStrToSpace(resultSet, SSSQLVarU.collSpace);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.dbSQLCloseStmt(resultSet);
//    }
//    
//    return collSpace;
//  }

// public boolean isEntityInPrivateUserColl(SSUri user, SSUri entity) throws SSErr{
//
//    Map<String, String> selectPars                = new HashMap<>();
//    ResultSet           resultSet                 = null;
//    List<String>        parentCollUris            = new ArrayList<>();
//    boolean             isEntityInPrivateUserColl;
//    
//    if(isColl(entity)){
//
//      selectPars.put(SSSQLVarU.userId, user.toString());
//      selectPars.put(SSSQLVarU.collId, entity.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(resultSet.first()){
//          isEntityInPrivateUserColl = SSSpaceEnum.isPrivate(bindingStrToSpace(resultSet, SSSQLVarU.collSpace));
//          return isEntityInPrivateUserColl;
//        }else{
//          return false;
//        }
//        
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    selectPars = new HashMap<>();
//    selectPars.put(SSSQLVarU.entryId, entity.toString());
//    
//    try{
//      resultSet = dbSQL.selectAllWhere(collEntryPosTable, selectPars);
//      
//      while(resultSet.next()){
//        parentCollUris.add(resultSet.getString(SSSQLVarU.collId));
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    if(parentCollUris.isEmpty()){
//      return null;
//    }
//    
//    selectPars = new HashMap<>();
//    
//    for(String parentCollUri : parentCollUris){
//      
//      selectPars.put(SSSQLVarU.collId, parentCollUri.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(
//          resultSet.next() &&
//          SSSpaceEnum.isPrivate(bindingStrToSpace(resultSet, SSSQLVarU.collSpace))){
//          return true;
//        }
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    return false;
//  }

//public boolean isEntityInSharedOrFollowedUserColl(final SSUri user, SSUri entity) throws SSErr{
//
//    Map<String, String> selectPars;
//    ResultSet           resultSet                            = null;
//    List<String>        parentCollUris                       = new ArrayList<>();
//    boolean             isEntityInSharedOrFollowedUserColl;
//    
//    if(isColl(entity)){
//
//      selectPars = new HashMap<>();
//      selectPars.put(SSSQLVarU.userId, user.toString());
//      selectPars.put(SSSQLVarU.collId, entity.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(resultSet.first()){
//          isEntityInSharedOrFollowedUserColl = SSSpaceEnum.isSharedOrFollow(bindingStrToSpace(resultSet, SSSQLVarU.collSpace));
//          return isEntityInSharedOrFollowedUserColl;
//        }else{
//          return false;
//        }
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    selectPars = new HashMap<>();
//    selectPars.put(SSSQLVarU.entryId, entity.toString());
//    
//    try{
//      resultSet = dbSQL.selectAllWhere(collEntryPosTable, selectPars);
//      
//      while(resultSet.next()){
//        parentCollUris.add(resultSet.getString(SSSQLVarU.collId));
//      }
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    if(parentCollUris.isEmpty()){
//      return null;
//    }
//    
//    selectPars = new HashMap<>();
//    
//    for(String parentCollUri : parentCollUris){
//      
//      selectPars.put(SSSQLVarU.collId, parentCollUri.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(
//          resultSet.next() &&
//          SSSpaceEnum.isSharedOrFollow(bindingStrToSpace(resultSet, SSSQLVarU.collSpace))){
//          return true;
//        }
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    return false;
//  }

//  public boolean newIsEntityInPrivateUserColl(final SSUri userUri, final SSUri entityUri) throws SSErr{
//    
//    if(SSObjU.isNull(userUri, entityUri)){
//      SSServErrReg.regErrThrow(new Exception("pars null"));
//      return null;
//    }
//    
//    final List<String>        collUris;
//    SSColl                    coll;
//      
//    try{
//      collUris = getCollUrisContainingEntity(entityUri);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//    
//    if(collUris.isEmpty()){
//      return false;
//    }
//    
//    for(String collUri : collUris){
//      
//      try{
//        coll = getUserColl(userUri, SSUri.get(collUri));
//      }catch(Exception error){
//        continue;
//      }
//      
//      if(SSSpaceEnum.isPrivate(coll.space)){
//        return true;
//      }
//    }
//    
//    return false;
//  }
  
//  public boolean newIsEntityInSharedOrFollowedUserColl(final SSUri userUri, final SSUri entityUri) throws SSErr{
//    
//    if(SSObjU.isNull(userUri, entityUri)){
//      SSServErrReg.regErrThrow(new Exception("pars null"));
//      return null;
//    }
//    
//    final List<String>        collUris;
//    final List<String>        userCollUris;
//    SSColl                    coll;
//    
//    try{
//      userCollUris  = getAllUserCollURIs(userUri);
//      
//      if(userCollUris.isEmpty()){
//        return false;
//      }
//      
//      collUris      = getCollUrisContainingEntity(entityUri);
//      
//      if(collUris.isEmpty()){
//        return false;
//      }
//      
//      for(String userCollUri : userCollUris){
//        
//        if(!collUris.contains(userCollUri)){
//          continue;
//        }
//        
//        coll = getUserColl(userUri, SSUri.get(userCollUri));
//        
//        if(SSSpaceEnum.isSharedOrFollow(coll.space)){
//          return true;
//        }
//      }
//      
//      return false;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//  public void removeColl(
//    final SSUri collUri) throws SSErr{
//    
//    if(collUri == null){
//      SSServErrReg.regErrThrow(new Exception("colluri null"));
//      return;
//    }
//    
//    final List<String>        subCollUris = new ArrayList<>();
//    final Map<String, String> deletePars  = new HashMap<>();
//    
//    try{
//      
//      //retrieve all sub coll uris
//      getAllChildCollURIs(collUri.toString(), collUri.toString(), subCollUris);
//          
//      //remove all sub colls
//      for(String subCollUri : subCollUris){
//        
//        deletePars.clear();
//        deletePars.put(SSSQLVarU.collId, subCollUri);
//        
//        dbSQL.deleteWhere(collTable, deletePars);
//        
//        deletePars.clear();
//        deletePars.put(SSSQLVarU.entryId, subCollUri);
//        
//        dbSQL.deleteWhere(collEntryPosTable, deletePars);
//      }
//      
//      deletePars.clear();
//      deletePars.put(SSSQLVarU.collId, SSStrU.toStr(collUri));
//
//      dbSQL.deleteWhere(collTable, deletePars);
//      
//      deletePars.clear();
//      deletePars.put(SSSQLVarU.entryId, SSStrU.toStr(collUri));
//
//      dbSQL.deleteWhere(collEntryPosTable, deletePars);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }

//  public boolean ownsUserASubColl(
//    final SSUri       userUri, 
//    final SSUri       collUri,
//    final SSSpaceEnum space) throws SSErr{
//    
//    if(SSObjU.isNull(userUri, collUri, space)){
//      SSServErrReg.regErrThrow(new Exception("pars not okay"));
//      return null;
//    }
//        
//    final List<String> subCollUris    = new ArrayList<>();
//    
//    getAllChildCollURIs(collUri.toString(), collUri.toString(), subCollUris);
//    
//    for(String subCollUri : subCollUris){
//      
//      if(ownsUserColl(userUri, SSUri.get(subCollUri), space)){
//        return true;
//      }
//    }
//    
//    return false;
//  }
  
  //  public boolean ownsUserASuperColl(
//    final SSUri       userUri, 
//    final SSUri       collUri,
//    final SSSpaceEnum space) throws SSErr{
//    
//    if(SSObjU.isNull(userUri, collUri, space)){
//      SSServErrReg.regErrThrow(new Exception("pars not okay"));
//      return null;
//    }
//        
//    final List<String> superCollUris = new ArrayList<>();
//    
//    getAllParentCollURIs(collUri.toString(), collUri.toString(), superCollUris);
//    
//    for(String parentCollUri : superCollUris){
//      
//      if(ownsUserColl(userUri, SSUri.get(parentCollUri), space)){
//        return true;
//      }
//    }
//    
//    return false;
//  }
  
//  public boolean ownsUserASuperColl(
//    final SSUri userUri, 
//    final SSUri collUri) throws SSErr{
//    
//    if(SSObjU.isNull(userUri, collUri)){
//      SSServErrReg.regErrThrow(new Exception("pars not okay"));
//      return null;
//    }
//        
//    final List<String> superCollUris = new ArrayList<>();
//    
//    getAllParentCollURIs(collUri.toString(), collUri.toString(), superCollUris);
//    
//    for(String parentCollUri : superCollUris){
//      
//      if(ownsUserColl(userUri, SSUri.get(parentCollUri))){
//        return true;
//      }
//    }
//    
//    return false;
//  }

//  private boolean ownsUserColl(
//    final SSUri       userUri, 
//    final SSUri       collUri, 
//    final SSSpaceEnum space) throws SSErr{
//    
//    
//    if(SSObjU.isNull(userUri, collUri, space)){
//      SSServErrReg.regErrThrow(new Exception("pars not okay"));
//      return null;
//    }
//    
//    final Map<String, String> whereParNamesWithValues  = new HashMap<>();
//    ResultSet                 resultSet                = null;
//    
//    try{
//      whereParNamesWithValues.put(SSSQLVarU.userId,    userUri.toString());
//      whereParNamesWithValues.put(SSSQLVarU.collId,    collUri.toString());
//      whereParNamesWithValues.put(SSSQLVarU.collSpace, space.toString());
//      
//      resultSet = dbSQL.selectAllWhere(collUserTable, whereParNamesWithValues);
//      
//      return resultSet.first();
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    //select * from entity, circleUser, circleEntity, cirlce where 
//    //id = entityUri, circleUser = userUri, circleType = private and id=entityId, circle.circleId=circlecircleId
//    //select * from entity, circleUser, circleEntity, cirlce where id = entityUri, circleUser = userUri, circleType != private;
//  }
//  public List<SSUri> getAllSharedCollURIs() throws SSErr{
//    
//    final List<SSUri>         sharedCollURIs          = new ArrayList<>();
//    final List<String>        columnNames             = new ArrayList<>();
//    final Map<String, String> whereParNamesWithValues = new HashMap<>();
//    ResultSet                 resultSet               = null;
//    
//    //get all colls from user table where space is shared (distinct)
//    columnNames.add             (SSSQLVarU.collId);
//    whereParNamesWithValues.put (SSSQLVarU.collSpace, SSSpaceEnum.sharedSpace.toString());
//    
//    try{
//      resultSet = dbSQL.selectCertainDistinctWhere(collUserTable, columnNames, whereParNamesWithValues);
//
//      while(resultSet.next()){
//        sharedCollURIs.add(bindingStrToUri(resultSet, SSSQLVarU.collId));
//      }
//      
//      return sharedCollURIs;
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }

//  public void shareCollAndSubColls(
//    final SSUri collUri) throws SSErr{
//    
//    if(SSObjU.isNull(collUri)){
//      SSServErrReg.regErrThrow(new Exception("pars not okay"));
//      return;
//    }
//  
//    final Map<String, String> updatePars = new HashMap<>();
//    final Map<String, String> newValues  = new HashMap<>();
//    final List<String>        subCollUris = new ArrayList<>();
//    
//    getAllChildCollURIs(collUri.toString(), collUri.toString(), subCollUris);
//    
//    //set space in user coll table for each sub coll to shared
//    for(String subCollUri : subCollUris){
//      
//      updatePars.put(SSSQLVarU.collId,    subCollUri.toString());
//      newValues.put (SSSQLVarU.collSpace, SSSpaceEnum.sharedSpace.toString());
//      
//      dbSQL.updateWhere(collUserTable, updatePars, newValues);
//    }
//    
//    //set space in user coll table for coll to shared
//    updatePars.put(SSSQLVarU.collId,    collUri.toString());
//    newValues.put (SSSQLVarU.collSpace, SSSpaceEnum.sharedSpace.toString());
//    
//    dbSQL.updateWhere(collUserTable, updatePars, newValues);
//  }


//public SSColl getColl(
//    final SSUri collUri) throws SSErr{
//   
//    if(SSObjU.isNull(collUri)){
//      SSServErrReg.regErrThrow(new Exception("pars null"));
//      return null;
//    }
//    
//    final List<String>        tableNames              = new ArrayList<>();
//    final List<String>        columnNames             = new ArrayList<>();
//    final Map<String, String> whereParNamesWithValues = new HashMap<>();
//    SSColl                    coll                    = null;
//    ResultSet                 resultSet               = null;
//    
//    try{
//      tableNames.add              (collTable);
//      tableNames.add              (entityTable);
//      columnNames.add             (SSSQLVarU.collId);
//      columnNames.add             (SSSQLVarU.author);
//      columnNames.add             (SSSQLVarU.label);
//      whereParNamesWithValues.put (SSSQLVarU.collId, collUri.toString());
//      
//      resultSet =
//        dbSQL.selectCertainWhere(
//        tableNames,
//        columnNames,
//        whereParNamesWithValues,
//        SSSQLVarU.collId + SSStrU.equal + SSSQLVarU.id);
//      
//      resultSet.first();
//      
//      coll = 
//        SSColl.get(
//        collUri,
//        null, 
//        bindingStrToUri  (resultSet, SSSQLVarU.author), 
//        bindingStr       (resultSet, SSSQLVarU.label), 
//        null);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    return coll;
//  }