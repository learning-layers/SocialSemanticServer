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
package at.tugraz.sss.servs.common.impl.tagcategory;

import at.kc.tugraz.ss.category.datatypes.SSCategory;
import at.kc.tugraz.ss.category.datatypes.SSCategoryLabel;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSUri;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class SSTagAndCategoryCommonSQL extends SSDBSQLFct{
  
  private final SSEntityE        metadataType;
  private final String           metadataIdSQLName;
  private final String           metadataSpaceSQLName;
  private final String           metadataSQLTableName;
  private final String           metadataAssSQLTableName;
  
  public SSTagAndCategoryCommonSQL(
    final SSDBSQLI    dbSQL,
    final SSEntityE   metadataType) throws Exception{
    
    super(dbSQL);
    
    this.metadataType = metadataType;
    
    switch(metadataType){
      
      case tag:{
        metadataIdSQLName       = SSSQLVarNames.tagId;
        metadataSpaceSQLName    = SSSQLVarNames.tagSpace;
        metadataAssSQLTableName = SSSQLVarNames.tagsTable;
        metadataSQLTableName    = null;
        break;
      }
      
      case category:{
        metadataIdSQLName       = SSSQLVarNames.categoryId;
        metadataSpaceSQLName    = SSSQLVarNames.categorySpace;
        metadataAssSQLTableName = SSSQLVarNames.categoriesTable;
        metadataSQLTableName    = SSSQLVarNames.categoryTable;
        break;
      }
      
      default:{
        throw new UnsupportedOperationException();
      }
    }
  }
  
  public List<SSUri> getEntities(
    final SSUri       userUri,
    final SSSpaceE    space,
    final SSUri       metadataURI) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres       = new HashMap<>();
      final List<String>        columns      = new ArrayList<>();
      
      column(columns, SSSQLVarNames.entityId);
      
      if(metadataURI != null){
        where(wheres, metadataIdSQLName, metadataURI);
      }
      
      if(space != null){
        where(wheres, metadataSpaceSQLName, space);
      }
      
      if(userUri != null){
        where(wheres, SSSQLVarNames.userId, userUri);
      }
      
      resultSet = dbSQL.select(metadataAssSQLTableName, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.entityId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<String> getMetadata(
    final Boolean isPredefined) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns   = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        tables    = new ArrayList<>();
      final List<String>        tableCons = new ArrayList<>();
      
      column(columns, SSSQLVarNames.label);
      
      table(tables, metadataSQLTableName);
      table(tables, SSSQLVarNames.entityTable);
      
      if(isPredefined == null){
        where(wheres, SSSQLVarNames.isPredefined, false);
      }else{
        where(wheres, SSSQLVarNames.isPredefined, isPredefined);
      }
      
      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, metadataSQLTableName, metadataIdSQLName);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return getStringsFromResult(resultSet, SSSQLVarNames.label);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSEntity> getMetadataAsss(
    final SSUri       userUri,
    final SSUri       entity,
    final SSSpaceE    space,
    final Long        startTime,
    final SSUri       metadataURI) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres         = new HashMap<>();
      final List<SSEntity>      metadataAsss   = new ArrayList<>();
      final List<String>        tables         = new ArrayList<>();
      final List<String>        columns        = new ArrayList<>();
      final List<String>        tableCons      = new ArrayList<>();
      
      column   (columns,   metadataIdSQLName);
      column   (columns,   SSSQLVarNames.entityId);
      column   (columns,   SSSQLVarNames.userId);
      column   (columns,   metadataSpaceSQLName);
      column   (columns,   SSSQLVarNames.label);
      column   (columns,   metadataAssSQLTableName, SSSQLVarNames.creationTime);
      column   (columns,   SSSQLVarNames.circleId);
      
      table    (tables, metadataAssSQLTableName);
      table    (tables, SSSQLVarNames.entityTable);
      
      tableCon (tableCons, metadataAssSQLTableName, metadataIdSQLName, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      if(userUri != null){
        where(wheres, SSSQLVarNames.userId, userUri);
      }
      
      if(entity != null){
        where(wheres, SSSQLVarNames.entityId, entity);
      }
      
      if(metadataURI != null){
        where(wheres, metadataIdSQLName, metadataURI);
      }
      
      if(space != null){
        where(wheres, metadataSpaceSQLName, space);
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
        
        switch(metadataType){
          
          case tag:{
            
            metadataAsss.add(
              SSTag.get(
                bindingStrToUri  (resultSet, metadataIdSQLName),
                bindingStrToUri  (resultSet, SSSQLVarNames.entityId),
                bindingStrToUri  (resultSet, SSSQLVarNames.userId),
                bindingStrToSpace(resultSet, metadataSpaceSQLName),
                SSTagLabel.get   (bindingStr(resultSet, SSSQLVarNames.label)),
                bindingStrToUri  (resultSet, SSSQLVarNames.circleId),
                bindingStrToLong (resultSet, SSSQLVarNames.creationTime)));
            break;
          }
          
          case category:{
            metadataAsss.add(
              SSCategory.get(
                bindingStrToUri     (resultSet, metadataIdSQLName),
                bindingStrToUri     (resultSet, SSSQLVarNames.entityId),
                bindingStrToUri     (resultSet, SSSQLVarNames.userId),
                bindingStrToSpace   (resultSet, metadataSpaceSQLName),
                SSCategoryLabel.get (bindingStr(resultSet, SSSQLVarNames.label)),
                bindingStrToUri     (resultSet, SSSQLVarNames.circleId),
                bindingStrToLong (resultSet, SSSQLVarNames.creationTime)));
            break;
          }
          
          default:{
            throw new UnsupportedOperationException();
          }
        }
      }
      
      return metadataAsss;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSEntity> getMetadataAsss(
    final List<SSUri> users,
    final List<SSUri> entities,
    final SSSpaceE    space,
    final Long        startTime,
    final List<SSUri> metadataURIs,
    final List<SSUri> circleURIs) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<MultivaluedMap<String, String>> wheres         = new ArrayList<>();
      final List<SSEntity>                       metadataAsss   = new ArrayList<>();
      final List<String>                         tables         = new ArrayList<>();
      final List<String>                         columns        = new ArrayList<>();
      final List<String>                         tableCons      = new ArrayList<>();
      
      column   (columns,   metadataIdSQLName);
      column   (columns,   SSSQLVarNames.entityId);
      column   (columns,   SSSQLVarNames.userId);
      column   (columns,   metadataSpaceSQLName);
      column   (columns,   SSSQLVarNames.label);
      column   (columns,   metadataAssSQLTableName, SSSQLVarNames.creationTime);
      column   (columns,   SSSQLVarNames.circleId);
      
      table    (tables,   metadataAssSQLTableName);
      table    (tables,   SSSQLVarNames.entityTable);
      
      tableCon (tableCons, metadataAssSQLTableName, metadataIdSQLName, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      if(
        users != null &&
        !users.isEmpty()){
        
        final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
        
        for(SSUri user : users){
          where(whereUsers, metadataAssSQLTableName, SSSQLVarNames.userId, user);
        }
        
        wheres.add(whereUsers);
      }
      
      if(
        entities != null &&
        !entities.isEmpty()){
        
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, metadataAssSQLTableName, SSSQLVarNames.entityId, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      if(
        metadataURIs != null &&
        !metadataURIs.isEmpty()){
        
        final MultivaluedMap<String, String> whereTags = new MultivaluedHashMap<>();
        
        for(SSUri metadataURI : metadataURIs){
          where(whereTags, metadataAssSQLTableName, metadataIdSQLName, metadataURI);
        }
        
        wheres.add(whereTags);
      }
      
      if(
        circleURIs != null &&
        !circleURIs.isEmpty()){
        
        final MultivaluedMap<String, String> whereCircles = new MultivaluedHashMap<>();
        
        for(SSUri circleURI : circleURIs){
          where(whereCircles, metadataAssSQLTableName, SSSQLVarNames.circleId, circleURI);
        }
        
        wheres.add(whereCircles);
      }
      
      if(space != null){
        
        final MultivaluedMap<String, String> whereTags = new MultivaluedHashMap<>();
        
        where(whereTags, metadataAssSQLTableName, metadataSpaceSQLName, space);
        
        wheres.add(whereTags);
      }
      
      if(wheres.isEmpty()){
        throw new Exception("wheres empty");
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
        
        switch(metadataType){
          
          case tag:{
            
            metadataAsss.add(
              SSTag.get(
                bindingStrToUri   (resultSet, metadataIdSQLName),
                bindingStrToUri   (resultSet, SSSQLVarNames.entityId),
                bindingStrToUri   (resultSet, SSSQLVarNames.userId),
                bindingStrToSpace (resultSet, metadataSpaceSQLName),
                SSTagLabel.get    (bindingStr(resultSet, SSSQLVarNames.label)),
                bindingStrToUri   (resultSet, SSSQLVarNames.circleId), 
                bindingStrToLong  (resultSet, SSSQLVarNames.creationTime)));
            break;
          }
          
          case category:{
            metadataAsss.add(
              SSCategory.get(
                bindingStrToUri     (resultSet, metadataIdSQLName),
                bindingStrToUri     (resultSet, SSSQLVarNames.entityId),
                bindingStrToUri     (resultSet, SSSQLVarNames.userId),
                bindingStrToSpace   (resultSet, metadataSpaceSQLName),
                SSCategoryLabel.get (bindingStr(resultSet, SSSQLVarNames.label)),
                bindingStrToUri     (resultSet, SSSQLVarNames.circleId),
                bindingStrToLong (resultSet, SSSQLVarNames.creationTime)));
            
            break;
          }
          
          default: throw new UnsupportedOperationException();
        }
      }
      
      return metadataAsss;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addMetadataIfNotExists(
    final SSUri           metadataURI, 
    final Boolean         isPredefined) throws Exception{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert    (inserts, metadataIdSQLName,     metadataURI);
      
      if(isPredefined == null){
        insert    (inserts, SSSQLVarNames.isPredefined,   false);
      }else{
        insert    (inserts, SSSQLVarNames.isPredefined,   isPredefined);
      }
      
      uniqueKey (uniqueKeys, metadataIdSQLName,  metadataURI);
      
      dbSQL.insertIfNotExists(metadataSQLTableName, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addMetadataAssIfNotExists1(
    final SSUri       metadataUri, 
    final SSUri       userUri,
    final SSUri       entityUri,
    final SSSpaceE    space,
    final SSUri       circleUri,
    final Long        creationTime) throws Exception{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert    (inserts,    SSSQLVarNames.userId,       userUri);
      insert    (inserts,    SSSQLVarNames.entityId,     entityUri);
      insert    (inserts,    metadataIdSQLName,          metadataUri);
      insert    (inserts,    metadataSpaceSQLName,       space);
      insert    (inserts,    SSSQLVarNames.circleId,     circleUri);
      
      if(
        creationTime == null ||
        creationTime == 0){
        insert    (inserts,    SSSQLVarNames.creationTime, SSDateU.dateAsLong());
      }else{
        insert    (inserts,    SSSQLVarNames.creationTime, creationTime);
      }
      
      uniqueKey (uniqueKeys, SSSQLVarNames.userId,       userUri);
      uniqueKey (uniqueKeys, SSSQLVarNames.entityId,     entityUri);
      uniqueKey (uniqueKeys, metadataIdSQLName,          metadataUri);
      uniqueKey (uniqueKeys, metadataSpaceSQLName,       space);
      uniqueKey (uniqueKeys, SSSQLVarNames.circleId,     circleUri);
      
      dbSQL.insertIfNotExists(metadataAssSQLTableName, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeMetadataAsss(
    final SSUri       userUri,
    final SSUri       entityUri, 
    final SSUri       metadataURI, 
    final SSSpaceE    space,
    final SSUri       circle) throws Exception{
    
    try{
      
      final Map<String, String> deletes = new HashMap<>();
      
      if(space != null){
        delete(deletes, metadataSpaceSQLName, space);
      }
      
      if(userUri != null){
        delete(deletes, SSSQLVarNames.userId,     userUri);
      }
      
      if(entityUri != null){
        delete(deletes, SSSQLVarNames.entityId,    entityUri);
      }
      
      if(metadataURI != null){
        delete(deletes, metadataIdSQLName,      metadataURI);
      }
      
      if(circle != null){
        delete(deletes, SSSQLVarNames.circleId,      circle);
      }
      
      if(deletes.size() > 0){
        dbSQL.delete(metadataAssSQLTableName, deletes);
      }else{
        dbSQL.delete(metadataAssSQLTableName);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}


//public List<SSTag> getTagAsss(
//    final SSUri       userUri, 
//    final SSUri       entity, 
//    final SSSpaceE    tagSpace,
//    final Long        startTime,
//    final SSUri       tagURI) throws Exception{
//    
//    ResultSet resultSet = null;
//    
//    try{
//      
//      final Map<String, String> wheres         = new HashMap<>();
//      final List<SSTag>         tagAsss        = new ArrayList<>();
//      final List<String>        tables         = new ArrayList<>();
//      final List<String>        columns        = new ArrayList<>();
//      final List<String>        tableCons      = new ArrayList<>();
////      final SSUri               tagURI         = getOrCreateTagURI(existsTagLabel(tagString), tagString);
//
//      column   (columns,   SSSQLVarNames.tagId);
//      column   (columns,   SSSQLVarNames.entityId);
//      column   (columns,   SSSQLVarNames.userId);
//      column   (columns,   SSSQLVarNames.tagSpace);
//      column   (columns,   SSSQLVarNames.label);
//      column   (columns,   SSSQLVarNames.tagAssTable, SSSQLVarNames.creationTime);
//      column   (columns,   SSSQLVarNames.circleId);
//      
//      table    (tables, SSSQLVarNames.tagAssTable);
//      table    (tables, SSSQLVarNames.entityTable);
//      
//      tableCon (tableCons, SSSQLVarNames.tagAssTable, SSSQLVarNames.tagId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
//      
//      if(userUri != null){
//        where(wheres, SSSQLVarNames.userId, userUri);
//      }
//      
//      if(entity != null){
//        where(wheres, SSSQLVarNames.entityId, entity);
//      }
//      
//      if(tagURI != null){
//        where(wheres, SSSQLVarNames.tagId, tagURI);
//      }
//      
//      if(tagSpace != null){
//        where(wheres, SSSQLVarNames.tagSpace, tagSpace);
//      }
//      
//      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
//      
//      while(resultSet.next()){
//        
//        //TODO dtheiler: use db for date restriction here
//        if(
//          startTime != null &&
//          startTime != 0    &&
//          bindingStrToLong(resultSet, SSSQLVarNames.creationTime) < startTime){
//          continue;
//        }
//        
//        tagAsss.add(
//          SSTag.get(bindingStrToUri  (resultSet, SSSQLVarNames.tagId),
//            bindingStrToUri  (resultSet, SSSQLVarNames.entityId),
//            bindingStrToUri  (resultSet, SSSQLVarNames.userId),
//            bindingStrToSpace(resultSet, SSSQLVarNames.tagSpace),
//            SSTagLabel.get   (bindingStr(resultSet, SSSQLVarNames.label)),
//            bindingStrToUri  (resultSet, SSSQLVarNames.circleId)));
//      }
//      
//      return tagAsss;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
//  
//  public List<SSTag> getTagAsss(
//    final List<SSUri> users, 
//    final List<SSUri> entities, 
//    final SSSpaceE    tagSpace,
//    final Long        startTime,
//    final List<SSUri> tagURIs, 
//    final List<SSUri> circleURIs) throws Exception{
//    
//    ResultSet resultSet = null;
//    
//    try{
//      
//      final List<MultivaluedMap<String, String>> wheres         = new ArrayList<>();
//      final List<SSTag>                          tagAsss        = new ArrayList<>();
//      final List<String>                         tables         = new ArrayList<>();
//      final List<String>                         columns        = new ArrayList<>();
//      final List<String>                         tableCons      = new ArrayList<>();
////      final SSUri               tagURI         = getOrCreateTagURI(existsTagLabel(tagString), tagString);
//
//      column   (columns,   SSSQLVarNames.tagId);
//      column   (columns,   SSSQLVarNames.entityId);
//      column   (columns,   SSSQLVarNames.userId);
//      column   (columns,   SSSQLVarNames.tagSpace);
//      column   (columns,   SSSQLVarNames.label);
//      column   (columns,   SSSQLVarNames.tagAssTable, SSSQLVarNames.creationTime);
//      column   (columns,   SSSQLVarNames.circleId);
//      
//      table    (tables, SSSQLVarNames.tagAssTable);
//      table    (tables,   SSSQLVarNames.entityTable);
//      
//      tableCon (tableCons, SSSQLVarNames.tagAssTable, SSSQLVarNames.tagId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
//      
//      if(
//        users != null &&
//        !users.isEmpty()){
//        
//        final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
//        
//        for(SSUri user : users){
//          where(whereUsers, SSSQLVarNames.tagAssTable, SSSQLVarNames.userId, user);
//        }
//        
//        wheres.add(whereUsers);
//      }
//      
//      if(
//        entities != null &&
//        !entities.isEmpty()){
//
//        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
//        
//        for(SSUri entity : entities){
//          where(whereEntities, SSSQLVarNames.tagAssTable, SSSQLVarNames.entityId, entity);
//        }
//        
//        wheres.add(whereEntities);
//      }
//      
//      if(
//        tagURIs != null &&
//        !tagURIs.isEmpty()){
//        
//        final MultivaluedMap<String, String> whereTags = new MultivaluedHashMap<>();
//        
//        for(SSUri tagURI : tagURIs){
//          where(whereTags, SSSQLVarNames.tagAssTable, SSSQLVarNames.tagId, tagURI);
//        }
//        
//        wheres.add(whereTags);
//      }
//      
//      if(
//        circleURIs != null &&
//        !circleURIs.isEmpty()){
//        
//        final MultivaluedMap<String, String> whereCircles = new MultivaluedHashMap<>();
//        
//        for(SSUri circleURI : circleURIs){
//          where(whereCircles, SSSQLVarNames.tagAssTable, SSSQLVarNames.circleId, circleURI);
//        }
//        
//        wheres.add(whereCircles);
//      }
//      
//      if(tagSpace != null){
//        
//        final MultivaluedMap<String, String> whereTags = new MultivaluedHashMap<>();
//        
//        where(whereTags, SSSQLVarNames.tagAssTable, SSSQLVarNames.tagSpace, tagSpace);
//        
//        wheres.add(whereTags);
//      }
//      
//      if(wheres.isEmpty()){
//        throw new Exception("wheres empty");
//      }
//      
//      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
//      
//      while(resultSet.next()){
//        
//        //TODO dtheiler: use db for date restriction here
//        if(
//          startTime != null &&
//          startTime != 0    &&
//          bindingStrToLong(resultSet, SSSQLVarNames.creationTime) < startTime){
//          continue;
//        }
//        
//        tagAsss.add(
//          SSTag.get(
//            bindingStrToUri  (resultSet, SSSQLVarNames.tagId),
//            bindingStrToUri  (resultSet, SSSQLVarNames.entityId),
//            bindingStrToUri  (resultSet, SSSQLVarNames.userId),
//            bindingStrToSpace(resultSet, SSSQLVarNames.tagSpace),
//            SSTagLabel.get   (bindingStr(resultSet, SSSQLVarNames.label)),
//            bindingStrToUri  (resultSet, SSSQLVarNames.circleId)));
//      }
//      
//      return tagAsss;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
//  
//  public List<SSUri> getEntities(
//    final SSUri       userUri, 
//    final SSSpaceE    tagSpace,
//    final SSUri       tagURI) throws Exception{
//    
//    ResultSet resultSet = null;
//    
//    try{
//      
//      final Map<String, String> wheres       = new HashMap<>();
//      final List<String>        columns      = new ArrayList<>();
//      
//      column(columns, SSSQLVarNames.entityId);
//      column(columns, SSSQLVarNames.tagId);
//      
//      if(tagURI != null){
//        where(wheres, SSSQLVarNames.tagId, tagURI);
//      }
//      
//      if(tagSpace != null){
//        where(wheres, SSSQLVarNames.tagSpace, tagSpace);
//      }
//      
//      if(userUri != null){
//        where(wheres, SSSQLVarNames.userId, userUri);
//      }
//      
//      resultSet = dbSQL.select(SSSQLVarNames.tagAssTable, columns, wheres, null, null, null);
//      
//      return getURIsFromResult(resultSet, SSSQLVarNames.entityId);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }