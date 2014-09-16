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

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.ss.category.datatypes.SSCategory;
import at.kc.tugraz.ss.category.datatypes.SSCategoryLabel;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sss.serv.err.datatypes.SSErrE;

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
      
      insert    (inserts,    SSSQLVarU.userId,        userUri);
      insert    (inserts,    SSSQLVarU.entityId,      entityUri);
      insert    (inserts,    SSSQLVarU.categoryId,    categoryUri);
      insert    (inserts,    SSSQLVarU.categorySpace, space);
      
      if(
        creationTime == null ||
        creationTime == 0){
        insert    (inserts,    SSSQLVarU.creationTime, SSDateU.dateAsLong());
      }else{
        insert    (inserts,    SSSQLVarU.creationTime, creationTime);
      }
      
      uniqueKey (uniqueKeys, SSSQLVarU.userId,        userUri);
      uniqueKey (uniqueKeys, SSSQLVarU.entityId,      entityUri);
      uniqueKey (uniqueKeys, SSSQLVarU.categoryId,    categoryUri);
      uniqueKey (uniqueKeys, SSSQLVarU.categorySpace, space);
      
      dbSQL.insertIfNotExists(categoryAssTable, inserts, uniqueKeys);
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
      
      insert    (inserts, SSSQLVarU.categoryId,     categoryUri);
      
      if(isPredefined == null){
        insert    (inserts, SSSQLVarU.isPredefined,   false);
      }else{
        insert    (inserts, SSSQLVarU.isPredefined,   isPredefined);
      }
      
      uniqueKey (uniqueKeys, SSSQLVarU.categoryId,  categoryUri);
      
      dbSQL.insertIfNotExists(categoryTable, inserts, uniqueKeys);
      
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
      
      column(columns, SSSQLVarU.label);
      
      table(tables, categoryTable);
      table(tables, entityTable);
      
      if(isPredefined == null){
        where(wheres, SSSQLVarU.isPredefined, false);
      }else{
        where(wheres, SSSQLVarU.isPredefined, isPredefined);
      }
      
      tableCon(tableCons, entityTable, SSSQLVarU.id, categoryTable, SSSQLVarU.categoryId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      return getStringsFromResult(resultSet, SSSQLVarU.label);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
    
  public Boolean existsCategoryLabel(
    final SSCategoryLabel categoryLabel) throws Exception{
   
    ResultSet resultSet = null;
    
    try{
      
      if(categoryLabel == null){
        return false;
      }
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.label, categoryLabel);
      where(wheres, SSSQLVarU.type,  SSEntityE.category);
      
      resultSet = dbSQL.select(entityTable, wheres);
      
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri getOrCreateCategoryURI(
    final Boolean          exsitsCategory, 
    final SSCategoryLabel  categoryLabel) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      if(!exsitsCategory){
        return createCategoryURI();
      }
      
      final Map<String, String>  wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.label, categoryLabel);
      where(wheres, SSSQLVarU.type,  SSEntityE.category);
      
      resultSet = dbSQL.select(entityTable, wheres);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarU.id);
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
      
      insert    (inserts,    SSSQLVarU.userId,         userUri);
      insert    (inserts,    SSSQLVarU.entityId,       entityUri);
      insert    (inserts,    SSSQLVarU.categoryId,     categoryUri);
      insert    (inserts,    SSSQLVarU.categorySpace,  space);
      uniqueKey (uniqueKeys, SSSQLVarU.userId,         userUri);
      uniqueKey (uniqueKeys, SSSQLVarU.entityId,       entityUri);
      uniqueKey (uniqueKeys, SSSQLVarU.categoryId,     categoryUri);
      uniqueKey (uniqueKeys, SSSQLVarU.categorySpace,  space);
      
      dbSQL.insertIfNotExists(categoryAssTable, inserts, uniqueKeys);
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
    
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.userId,       userUri);
      where(wheres, SSSQLVarU.entityId,     entityUri);
      where(wheres, SSSQLVarU.tagId,        categoryUri);
      where(wheres, SSSQLVarU.tagSpace,     space);
      
      resultSet = dbSQL.select(categoryAssTable, wheres);
      
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
    final SSCategoryLabel  categoryLabel, 
    final SSSpaceE         space) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      final Map<String, String> wheres  = new HashMap<>();
      final Map<String, String> deletes = new HashMap<>();
      SSUri                     categoryUri  = null;
      
      if(categoryLabel != null){
        
        try{
          where(wheres, SSSQLVarU.label, categoryLabel);
          where(wheres, SSSQLVarU.type,  SSEntityE.category);
          
          resultSet = dbSQL.select(entityTable, wheres);
          
          checkFirstResult(resultSet);
          
          categoryUri = bindingStrToUri(resultSet, SSSQLVarU.id);
          
        }catch(Exception error){
          
          if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
            SSServErrReg.reset();
            return;
          }
          
          SSServErrReg.regErrThrow(error);
          return;
          
        }finally{
          dbSQL.closeStmt(resultSet);
        }
      }
      
      if(space != null){
        delete(deletes, SSSQLVarU.tagSpace, space);
      }
      
      if(userUri != null){
        delete(deletes, SSSQLVarU.userId,     userUri);
      }
      
      if(entityUri != null){
        delete(deletes, SSSQLVarU.entityId,    entityUri);
      }
      
      if(categoryUri != null){
        delete(deletes, SSSQLVarU.categoryId,      categoryUri);
      }
      
      if(deletes.size() > 0){
        dbSQL.delete(categoryAssTable, deletes);
      }else{
        dbSQL.delete(categoryAssTable);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSCategory> getCategoryAsss(
    final SSUri            userUri, 
    final SSUri            entity, 
    final SSCategoryLabel  categoryLabel, 
    final SSSpaceE         categorySpace,
    final Long             startTime) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres         = new HashMap<>();
      final List<SSCategory>    categoryAsss   = new ArrayList<>();
      final List<String>        tables         = new ArrayList<>();
      final List<String>        columns        = new ArrayList<>();
      final List<String>        tableCons      = new ArrayList<>();
      final SSUri               categoryURI    = getOrCreateCategoryURI(existsCategoryLabel(categoryLabel), categoryLabel);

      table    (tables,    categoryAssTable);
      table    (tables,    entityTable);
      column   (columns,   SSSQLVarU.categoryId);
      column   (columns,   SSSQLVarU.entityId);
      column   (columns,   SSSQLVarU.userId);
      column   (columns,   SSSQLVarU.categorySpace);
      column   (columns,   SSSQLVarU.label);
      column   (columns,   categoryAssTable, SSSQLVarU.creationTime);
      tableCon (tableCons, categoryAssTable, SSSQLVarU.categoryId, entityTable, SSSQLVarU.id);
      
      if(userUri != null){
        where(wheres, SSSQLVarU.userId, userUri);
      }
      
      if(entity != null){
        where(wheres, SSSQLVarU.entityId, entity);
      }
      
      if(categoryLabel != null){
        where(wheres, SSSQLVarU.categoryId, categoryURI);
      }
      
      if(categorySpace != null){
        where(wheres, SSSQLVarU.categorySpace, categorySpace);
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      while(resultSet.next()){
        
        //TODO dtheiler: use db for date restriction here
        if(
          startTime != null &&
          startTime != 0    &&
          bindingStrToLong(resultSet, SSSQLVarU.creationTime) < startTime){
          continue;
        }
        
        categoryAsss.add(
          SSCategory.get(
            bindingStrToUri  (resultSet, SSSQLVarU.categoryId),
            bindingStrToUri  (resultSet, SSSQLVarU.entityId),
            bindingStrToUri  (resultSet, SSSQLVarU.userId),
            bindingStrToSpace(resultSet, SSSQLVarU.categorySpace),
            SSCategoryLabel.get   (bindingStr(resultSet, SSSQLVarU.label))));
      }
      
      return categoryAsss;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getEntitiesForCategoryLabel(
    final SSUri            userUri, 
    final SSCategoryLabel  tagLabel, 
    final SSSpaceE         categorySpace) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(!existsCategoryLabel(tagLabel)){
        return new ArrayList<>();
      }
      
      final Map<String, String> wheres       = new HashMap<>();
      final List<String>        columns      = new ArrayList<>();
      
      where(wheres, SSSQLVarU.tagId, getOrCreateCategoryURI(true, tagLabel));
      
      if(categorySpace != null){
        where(wheres, SSSQLVarU.categorySpace, categorySpace);
      }
      
      if(userUri != null){
        where(wheres, SSSQLVarU.userId, userUri);
      }
      
      column(columns, SSSQLVarU.entityId);
      column(columns, SSSQLVarU.categoryId);
      
      resultSet = dbSQL.select(categoryAssTable, columns, wheres);
      
      return getURIsFromResult(resultSet, SSSQLVarU.entityId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  private SSUri createCategoryURI() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objCategory().toString()));
  }
   
  private SSUri objCategory() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityE.category.toString());
  }
}
