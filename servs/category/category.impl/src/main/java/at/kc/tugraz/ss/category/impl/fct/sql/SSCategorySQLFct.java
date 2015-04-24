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
package at.kc.tugraz.ss.category.impl.fct.sql;

import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSSQLVarNames;
import at.kc.tugraz.ss.category.datatypes.SSCategory;
import at.kc.tugraz.ss.category.datatypes.SSCategoryLabel;
import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSUri;

import at.tugraz.sss.serv.SSServImplWithDBA;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCategorySQLFct extends SSDBSQLFct{
  
  public SSCategorySQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }
  
  public void addCategoryAssIfNotExists(
    final SSUri       categoryUri, 
    final SSUri       userUri,
    final SSUri       entityUri,
    final SSSpaceE    space,
    final Long        creationTime) throws Exception{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert    (inserts,    SSSQLVarNames.userId,        userUri);
      insert    (inserts,    SSSQLVarNames.entityId,      entityUri);
      insert    (inserts,    SSSQLVarNames.categoryId,    categoryUri);
      insert    (inserts,    SSSQLVarNames.categorySpace, space);
      
      if(
        creationTime == null ||
        creationTime == 0){
        insert    (inserts,    SSSQLVarNames.creationTime, SSDateU.dateAsLong());
      }else{
        insert    (inserts,    SSSQLVarNames.creationTime, creationTime);
      }
      
      uniqueKey (uniqueKeys, SSSQLVarNames.userId,        userUri);
      uniqueKey (uniqueKeys, SSSQLVarNames.entityId,      entityUri);
      uniqueKey (uniqueKeys, SSSQLVarNames.categoryId,    categoryUri);
      uniqueKey (uniqueKeys, SSSQLVarNames.categorySpace, space);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.categoryAssTable, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addCategoryIfNotExists(
    final SSUri           categoryUri, 
    final Boolean         isPredefined) throws Exception{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert    (inserts, SSSQLVarNames.categoryId,     categoryUri);
      
      if(isPredefined == null){
        insert    (inserts, SSSQLVarNames.isPredefined,   false);
      }else{
        insert    (inserts, SSSQLVarNames.isPredefined,   isPredefined);
      }
      
      uniqueKey (uniqueKeys, SSSQLVarNames.categoryId,  categoryUri);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.categoryTable, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<String> getCategories(
    final Boolean isPredefined) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns   = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        tables    = new ArrayList<>();
      final List<String>        tableCons = new ArrayList<>();
      
      column(columns, SSSQLVarNames.label);
      
      table(tables, SSSQLVarNames.categoryTable);
      table(tables, SSSQLVarNames.entityTable);
      
      if(isPredefined == null){
        where(wheres, SSSQLVarNames.isPredefined, false);
      }else{
        where(wheres, SSSQLVarNames.isPredefined, isPredefined);
      }
      
      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.categoryTable, SSSQLVarNames.categoryId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return getStringsFromResult(resultSet, SSSQLVarNames.label);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
    
  public void addCategoryAssIfNotExists(
    final SSUri       categoryUri, 
    final SSUri       userUri,
    final SSUri       entityUri,
    final SSSpaceE    space) throws Exception{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert    (inserts,    SSSQLVarNames.userId,         userUri);
      insert    (inserts,    SSSQLVarNames.entityId,       entityUri);
      insert    (inserts,    SSSQLVarNames.categoryId,     categoryUri);
      insert    (inserts,    SSSQLVarNames.categorySpace,  space);
      uniqueKey (uniqueKeys, SSSQLVarNames.userId,         userUri);
      uniqueKey (uniqueKeys, SSSQLVarNames.entityId,       entityUri);
      uniqueKey (uniqueKeys, SSSQLVarNames.categoryId,     categoryUri);
      uniqueKey (uniqueKeys, SSSQLVarNames.categorySpace,  space);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.categoryAssTable, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Boolean existsCategoryAss(
    final SSUri       userUri, 
    final SSUri       entityUri, 
    final SSUri       categoryUri, 
    final SSSpaceE    space) throws Exception{

    ResultSet resultSet = null;
    
    try{
    
      final List<String>        columns   = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.categoryId);
      column(columns, SSSQLVarNames.userId);
      column(columns, SSSQLVarNames.tagId);
      column(columns, SSSQLVarNames.tagSpace);
      
      where(wheres, SSSQLVarNames.userId,       userUri);
      where(wheres, SSSQLVarNames.entityId,     entityUri);
      where(wheres, SSSQLVarNames.tagId,        categoryUri);
      where(wheres, SSSQLVarNames.tagSpace,     space);
      
      resultSet = dbSQL.select(SSSQLVarNames.categoryAssTable, columns, wheres, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void removeCategoryAsss(
    final SSUri            userUri,
    final SSUri            entityUri, 
    final SSUri            categoryUri, 
    final SSSpaceE         space) throws Exception{
    
    try{
      
      final Map<String, String> deletes      = new HashMap<>();
      
      if(space != null){
        delete(deletes, SSSQLVarNames.tagSpace, space);
      }
      
      if(userUri != null){
        delete(deletes, SSSQLVarNames.userId,     userUri);
      }
      
      if(entityUri != null){
        delete(deletes, SSSQLVarNames.entityId,    entityUri);
      }
      
      if(categoryUri != null){
        delete(deletes, SSSQLVarNames.categoryId,      categoryUri);
      }
      
      if(deletes.size() > 0){
        dbSQL.delete(SSSQLVarNames.categoryAssTable, deletes);
      }else{
        dbSQL.delete(SSSQLVarNames.categoryAssTable);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSCategory> getCategoryAsss(
    final SSUri            userUri, 
    final SSUri            entity, 
    final SSSpaceE         categorySpace,
    final Long             startTime,
    final SSUri            categoryURI) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres         = new HashMap<>();
      final List<SSCategory>    categoryAsss   = new ArrayList<>();
      final List<String>        tables         = new ArrayList<>();
      final List<String>        columns        = new ArrayList<>();
      final List<String>        tableCons      = new ArrayList<>();
//      final SSUri               categoryURI    = getOrCreateCategoryURI(existsCategoryLabel(categoryLabel), categoryLabel);

      table    (tables, SSSQLVarNames.categoryAssTable);
      table    (tables, SSSQLVarNames.entityTable);
      column   (columns,   SSSQLVarNames.categoryId);
      column   (columns,   SSSQLVarNames.entityId);
      column   (columns,   SSSQLVarNames.userId);
      column   (columns,   SSSQLVarNames.categorySpace);
      column   (columns,   SSSQLVarNames.label);
      column   (columns, SSSQLVarNames.categoryAssTable, SSSQLVarNames.creationTime);
      tableCon (tableCons, SSSQLVarNames.categoryAssTable, SSSQLVarNames.categoryId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      if(userUri != null){
        where(wheres, SSSQLVarNames.userId, userUri);
      }
      
      if(entity != null){
        where(wheres, SSSQLVarNames.entityId, entity);
      }
      
      if(categoryURI != null){
        where(wheres, SSSQLVarNames.categoryId, categoryURI);
      }
      
      if(categorySpace != null){
        where(wheres, SSSQLVarNames.categorySpace, categorySpace);
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        //TODO dtheiler: use db for date restriction here
        if(
          startTime != null &&
          startTime != 0    &&
          bindingStrToLong(resultSet, SSSQLVarNames.creationTime) < startTime){
          continue;
        }
        
        categoryAsss.add(SSCategory.get(bindingStrToUri  (resultSet, SSSQLVarNames.categoryId),
            bindingStrToUri  (resultSet, SSSQLVarNames.entityId),
            bindingStrToUri  (resultSet, SSSQLVarNames.userId),
            bindingStrToSpace(resultSet, SSSQLVarNames.categorySpace),
            SSCategoryLabel.get   (bindingStr(resultSet, SSSQLVarNames.label))));
      }
      
      return categoryAsss;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getEntities(
    final SSUri            user, 
    final SSSpaceE         space,
    final SSUri            category) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres       = new HashMap<>();
      final List<String>        columns      = new ArrayList<>();
      
      if(space != null){
        where(wheres, SSSQLVarNames.categorySpace, space);
      }
      
      if(user != null){
        where(wheres, SSSQLVarNames.userId, user);
      }
      
      if(category != null){
        where(wheres, SSSQLVarNames.categoryId, category);
      }
      
      column(columns, SSSQLVarNames.entityId);
      
      resultSet = dbSQL.select(SSSQLVarNames.categoryAssTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.entityId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
