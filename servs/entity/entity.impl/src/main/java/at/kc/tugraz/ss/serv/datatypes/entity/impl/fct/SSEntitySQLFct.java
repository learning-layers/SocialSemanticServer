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
package at.kc.tugraz.ss.serv.datatypes.entity.impl.fct;

import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchOpE;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSSQLVarU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityA;
import at.tugraz.sss.serv.SSEntity;

import at.tugraz.sss.serv.SSImageE;
import at.tugraz.sss.serv.SSLocation;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;

public class SSEntitySQLFct extends SSDBSQLFct{

  public SSEntitySQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }
  
  public Boolean existsEntity(
    final SSUri entity) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> where   = new HashMap<>();
      
      column(columns, SSSQLVarU.id);
      
      where(where, SSSQLVarU.id, entity);
      
      resultSet = dbSQL.select(entityTable, columns, where, null, null, null);
      
      try{
        checkFirstResult(resultSet);
      }catch(Exception error){
        
        if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
          SSServErrReg.reset();
          return false;
        }
        
        throw error;
      }
      
      return true;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSEntity getEntity(
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> where   = new HashMap<>();
      final SSEntity            entityObj;
      
      column(columns, SSSQLVarU.id);
      column(columns, SSSQLVarU.type);
      column(columns, SSSQLVarU.label);
      column(columns, SSSQLVarU.creationTime);
      column(columns, SSSQLVarU.author);
      column(columns, SSSQLVarU.description);
      
      where(where, SSSQLVarU.id, entityUri);
      
      resultSet = dbSQL.select(entityTable, columns, where, null, null, null);
      
      checkFirstResult(resultSet);
      
      entityObj =
        SSEntity.get(
          entityUri,
          bindingStrToEntityType (resultSet, SSSQLVarU.type),
          bindingStrToLabel      (resultSet, SSSQLVarU.label));
      
      entityObj.creationTime = bindingStrToLong       (resultSet, SSSQLVarU.creationTime);
      entityObj.author       = bindingStrToUri        (resultSet, SSSQLVarU.author);
      entityObj.description  = bindingStrToTextComment(resultSet, SSSQLVarU.description);

      return entityObj;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
        SSServErrReg.regErrThrow(new SSErr(SSErrE.entityDoesntExist), false);
        return null;
      }
        
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSEntity getEntity(
    final SSLabel   label,
    final SSEntityE type) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> where   = new HashMap<>();
      final SSEntity            entityObj;
      
      column(columns, SSSQLVarU.id);
      column(columns, SSSQLVarU.label);
      column(columns, SSSQLVarU.type);
      column(columns, SSSQLVarU.creationTime);
      column(columns, SSSQLVarU.author);
      column(columns, SSSQLVarU.description);
      
      where(where, SSSQLVarU.label, label);
      where(where, SSSQLVarU.type,  type);
      
      resultSet = dbSQL.select(entityTable, columns, where, null, null, null);
      
      checkFirstResult(resultSet);
      
      entityObj =
        SSEntity.get(
          bindingStrToUri        (resultSet, SSSQLVarU.id),
          type,
          label);
      
      entityObj.creationTime = bindingStrToLong       (resultSet, SSSQLVarU.creationTime);
      entityObj.author       = bindingStrToUri        (resultSet, SSSQLVarU.author);
      entityObj.description  = bindingStrToTextComment(resultSet, SSSQLVarU.description);

      return entityObj;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
        SSServErrReg.regErrThrow(new SSErr(SSErrE.entityDoesntExist), false);
        return null;
      }
        
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getEntityURIs(
    final SSUri           author,
    final List<SSEntityE> types) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>                         columns    = new ArrayList<>();
      final List<String>                         tables     = new ArrayList<>();
      final List<String>                         tableCons  = new ArrayList<>();
      final List<MultivaluedMap<String, String>> wheres     = new ArrayList<>();
      
      column (columns, SSSQLVarU.id);
      
      table  (tables,  entityTable);
      
      if(author == null){
        throw new Exception("author has to be given");
      }
      
      final MultivaluedMap<String, String> whereAuthors = new MultivaluedHashMap<>();
      
      where(whereAuthors, entityTable, SSSQLVarU.author, author);
      
      wheres.add(whereAuthors);
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSEntityE type : types){
          where(whereTypes, entityTable, SSSQLVarU.type, type);
        }
        
        wheres.add(whereTypes);
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarU.id);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSEntity> getEntities(
    final List<SSUri>     entityURIs,
    final List<SSEntityE> types) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>                         columns    = new ArrayList<>();
      final List<String>                         tables     = new ArrayList<>();
      final List<String>                         tableCons  = new ArrayList<>();
      final List<MultivaluedMap<String, String>> wheres     = new ArrayList<>();
      final List<SSEntity>                       entities   = new ArrayList<>();
      SSEntity                                   entity;
      
      column (columns, SSSQLVarU.id);
      column (columns, SSSQLVarU.type);
      column (columns, SSSQLVarU.label);
      column (columns, SSSQLVarU.creationTime);
      column (columns, SSSQLVarU.author);
      column (columns, SSSQLVarU.description);
      
      table  (tables,  entityTable);
      
      if(
        entityURIs != null &&
        !entityURIs.isEmpty()){
        
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entityURI : entityURIs){
          where(whereEntities, entityTable, SSSQLVarU.id, entityURI);
        }
        
        wheres.add(whereEntities);
      }
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSEntityE type : types){
          where(whereTypes, entityTable, SSSQLVarU.type, type);
        }
        
        wheres.add(whereTypes);
      }
      
      if(wheres.isEmpty()){
        throw new Exception("at least one parameter has to be set");
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        entity =
          SSEntity.get(
            bindingStrToUri        (resultSet, SSSQLVarU.id),
            bindingStrToEntityType (resultSet, SSSQLVarU.type),
            bindingStrToLabel      (resultSet, SSSQLVarU.label));
        
        entity.creationTime = bindingStrToLong       (resultSet, SSSQLVarU.creationTime);
        entity.author       = bindingStrToUri        (resultSet, SSSQLVarU.author);
        entity.description  = bindingStrToTextComment(resultSet, SSSQLVarU.description);
        
        entities.add(entity);
      }
      
      return entities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addEntityIfNotExists(
    final SSUri         entity, 
    final SSEntityE     entityType,
    final SSEntityA     label,
    final SSTextComment description,
    final SSUri         authorUri,
    final Long          creationTime) throws Exception{
    
    if(!existsEntity(entity)){
      
      final Map<String, String> inserts    = new HashMap<>();
      
      insert    (inserts,     SSSQLVarU.id, entity);
      
      if(
        creationTime == null ||
        creationTime == 0){
        insert(inserts, SSSQLVarU.creationTime, SSDateU.dateAsLong());
      }else{
        insert(inserts, SSSQLVarU.creationTime, creationTime);
      }
      
      if(label == null){
        insert(inserts, SSSQLVarU.label, SSStrU.empty);
      }else{
        insert(inserts, SSSQLVarU.label, SSStrU.trim(label, SSDBSQLI.entityLabelLength));
      }
      
      if(entityType == null){
        insert(inserts, SSSQLVarU.type, SSEntityE.entity);
      }else{
        insert(inserts, SSSQLVarU.type, entityType);
      }
      
      if(authorUri == null){
        insert(inserts, SSSQLVarU.author, SSStrU.empty);
      }else{
        insert(inserts, SSSQLVarU.author, authorUri);
      }
      
      if(description == null){
        insert(inserts, SSSQLVarU.description, SSStrU.empty);
      }else{
        insert(inserts, SSSQLVarU.description, description);
      }
      
      dbSQL.insert(entityTable, inserts);
    }else{
      
      try{
        final Map<String, String>  wheres   = new HashMap<>();
        final Map<String, String>  updates  = new HashMap<>();
        
        where(wheres, SSSQLVarU.id, entity);
        
        if(label != null){
          update (updates, SSSQLVarU.label, label);
        }
        
        if(description != null){
          update (updates, SSSQLVarU.description, description);
        }
        
        if(updates.isEmpty()){
          return;
        }
        
        dbSQL.update(entityTable, wheres, updates);
        
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }
    }
  }
  
  public void deleteEntityIfExists(
    final SSUri entityUri) throws Exception{
    
    try{
      final Map<String, String> deletes = new HashMap<>();
      
      delete(deletes, SSSQLVarU.id, entityUri);
      
      dbSQL.deleteIgnore(entityTable, deletes);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void removeAllEntities() throws Exception {
    
    try{
      dbSQL.delete(entityTable);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addThumb(
    final SSUri entity,
    final SSUri thumb) throws Exception{
    
    try{

      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarU.entityId,   entity);
      insert(inserts, SSSQLVarU.thumbId, thumb);
      
      dbSQL.insert(thumbnailsTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void attachEntity(
    final SSUri entity,
    final SSUri entityToAttach) throws Exception{
    
    try{

      final Map<String, String> inserts = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarU.entityId,         entity);
      insert(inserts, SSSQLVarU.attachedEntityId, entityToAttach);
      
      uniqueKey(uniqueKeys, SSSQLVarU.entityId,          entity);
      uniqueKey(uniqueKeys, SSSQLVarU.attachedEntityId,  entityToAttach);
      
      dbSQL.insertIfNotExists(entitiesTable, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSEntity> getAttachedEntities(
    final SSUri entity) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<SSEntity>      attachedEntities = new ArrayList<>();
      final List<String>        columns          = new ArrayList<>();
      final List<String>        tables           = new ArrayList<>();
      final Map<String, String> wheres           = new HashMap<>();
      final List<String>        tableCons        = new ArrayList<>();
      SSEntity                  entityObj;
      
      column(columns, SSSQLVarU.id);
      column(columns, SSSQLVarU.label);
      column(columns, SSSQLVarU.creationTime);
      column(columns, SSSQLVarU.type);
      column(columns, SSSQLVarU.author);
      column(columns, SSSQLVarU.description);

      table(tables, entityTable);
      table(tables, entitiesTable);
      
      where(wheres, entitiesTable, SSSQLVarU.entityId, entity);
      
      tableCon(tableCons, entityTable, SSSQLVarU.id, entitiesTable, SSSQLVarU.attachedEntityId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        entityObj =
          SSEntity.get(
            bindingStrToUri           (resultSet, SSSQLVarU.id),
            bindingStrToEntityType    (resultSet, SSSQLVarU.type),
            bindingStrToLabel         (resultSet, SSSQLVarU.label));
        
        entityObj.creationTime = bindingStrToLong          (resultSet, SSSQLVarU.creationTime);
        entityObj.author       = bindingStrToUri           (resultSet, SSSQLVarU.author);
        entityObj.description  = bindingStrToTextComment   (resultSet, SSSQLVarU.description);

        attachedEntities.add(entityObj);
      }
      
      return attachedEntities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addFile(
    final SSUri entity,
    final SSUri file) throws Exception{
    
    try{

      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarU.entityId,   entity);
      insert(inserts, SSSQLVarU.fileId,     file);
      
      dbSQL.insert(filesTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getThumbs(final SSUri entity) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarU.thumbId);
      
      where(wheres, SSSQLVarU.entityId, entity);
      
      resultSet = dbSQL.select(thumbnailsTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarU.thumbId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getImages(
    final SSUri    forEntity,
    final SSImageE type) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns    = new ArrayList<>();
      final List<String>        tables     = new ArrayList<>();
      final Map<String, String> wheres     = new HashMap<>();
      final List<String>        tableCons  = new ArrayList<>();
      
      column (columns, imageTable, SSSQLVarU.imageId);
      table  (tables, imageTable);
      
      if(forEntity != null){
        where   (wheres,    entitiesTable, SSSQLVarU.entityId, forEntity);
        table   (tables,    entitiesTable);
        tableCon(tableCons, imageTable, SSSQLVarU.imageId, entitiesTable, SSSQLVarU.attachedEntityId);
      }
      
      if(type != null){
        where(wheres, imageTable, SSSQLVarU.type, type);
      }
      
      if(!tableCons.isEmpty()){
        resultSet = dbSQL.select(tables,     columns, wheres, tableCons, null, null, null);
      }else{
        resultSet = dbSQL.select(imageTable, columns, wheres, null, null, null);
      }
      
      return getURIsFromResult(resultSet, SSSQLVarU.imageId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getDownloads(final SSUri entity) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns           = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      
      column(columns, SSSQLVarU.downloadId);
      
      where(wheres, SSSQLVarU.entityId, entity);
      
      resultSet = dbSQL.select(downloadsTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarU.downloadId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
    
  public List<SSUri> getFiles(final SSUri entity) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns           = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      
      column(columns, SSSQLVarU.fileId);
      
      where(wheres, SSSQLVarU.entityId, entity);
      
      resultSet = dbSQL.select(filesTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarU.fileId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSEntity> getEntitiesForLabelsAndDescriptions(
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<SSEntity>            entities  = new ArrayList<>();
      final List<String>              columns   = new ArrayList<>();
      final List<String>              matches   = new ArrayList<>();
      SSEntity                        entityObj;
      
      column (columns, SSSQLVarU.id);
      column (columns, SSSQLVarU.label);
      column (columns, SSSQLVarU.description);
      column (columns, SSSQLVarU.type);
      match  (matches, SSSQLVarU.label);
      match  (matches, SSSQLVarU.description);
      
      resultSet = dbSQL.select(entityTable, columns, matches, requireds, absents, eithers);
      
      while(resultSet.next()){
      
        entityObj =
          SSEntity.get(
            bindingStrToUri        (resultSet, SSSQLVarU.id),
            bindingStrToEntityType (resultSet, SSSQLVarU.type),
            bindingStrToLabel      (resultSet, SSSQLVarU.label));
        
        entityObj.description = bindingStrToTextComment(resultSet, SSSQLVarU.description);
        
        entities.add(entityObj);
      }
      
      return entities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSEntity> getEntitiesForLabelsAndDescriptionsWithSQLLike(
    final List<String> labelStrings,
    final List<String> descStrings,
    final SSSearchOpE  searchOp) throws Exception{
  
    ResultSet resultSet = null;
    
    try{
      final List<SSEntity>                       entities  = new ArrayList<>();
      final List<MultivaluedMap<String, String>> likes     = new ArrayList<>();
      final List<String>                         columns   = new ArrayList<>();
      final List<String>                         tables    = new ArrayList<>();
      final List<String>                         tableCons  = new ArrayList<>();
      SSEntity                                   entityObj;
      
      column (columns, SSSQLVarU.id);
      column (columns, SSSQLVarU.label);
      column (columns, SSSQLVarU.type);
      column (columns, SSSQLVarU.description);
      
      table(tables, entityTable);
      
      if(searchOp == null){
        throw new Exception("search for labels search operation null");
      }
      
      MultivaluedMap<String, String> likeContents = new MultivaluedHashMap<>();
      
      switch(searchOp){
        
        case or:{
          
          if(
            labelStrings != null &&
            !labelStrings.isEmpty()){
            
            for(String labelString : labelStrings){
              where(likeContents, entityTable, SSSQLVarU.label, labelString);
            }
          }
          
          if(
            descStrings != null &&
            !descStrings.isEmpty()){
            
            for(String descString : descStrings){
              where(likeContents, entityTable, SSSQLVarU.description, descString);
            }
          }
          
          likes.add(likeContents);
          break;
        }
        
        case and:{
          
          for(String labelString : labelStrings){
            
            likeContents = new MultivaluedHashMap<>();
            
            where(likeContents, entityTable, SSSQLVarU.label, labelString);
            
            likes.add(likeContents);
          }
          
          for(String descString : descStrings){
            
            likeContents = new MultivaluedHashMap<>();
            
            where(likeContents, entityTable, SSSQLVarU.description, descString);
            
            likes.add(likeContents);
          }
          
          break;
        }
      }
      
      resultSet = dbSQL.selectLike(tables, columns, likes, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        entityObj =
          SSEntity.get(
            bindingStrToUri        (resultSet, SSSQLVarU.id),
            bindingStrToEntityType (resultSet, SSSQLVarU.type),
            bindingStrToLabel      (resultSet, SSSQLVarU.label));
         
         entityObj.description = bindingStrToTextComment(resultSet, SSSQLVarU.description);
         
         entities.add(entityObj);
      }      
      
      return entities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSEntity> getEntitiesForLabels(
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<SSEntity>            entities  = new ArrayList<>();
      final List<String>              columns   = new ArrayList<>();
      final List<String>              matches   = new ArrayList<>();
      SSEntity                        entityObj;
      
      column (columns, SSSQLVarU.id);
      column (columns, SSSQLVarU.label);
      column (columns, SSSQLVarU.type);
      column (columns, SSSQLVarU.description);
      match  (matches, SSSQLVarU.label);
      
      resultSet = dbSQL.select(entityTable, columns, matches, requireds, absents, eithers);
      
      while(resultSet.next()){
        
        entityObj =
          SSEntity.get(
            bindingStrToUri        (resultSet, SSSQLVarU.id),
            bindingStrToEntityType (resultSet, SSSQLVarU.type),
            bindingStrToLabel      (resultSet, SSSQLVarU.label));
         
         entityObj.description = bindingStrToTextComment(resultSet, SSSQLVarU.description);
         
         entities.add(entityObj);
      }      
      
      return entities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSEntity> getEntitiesForDescriptions(
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<SSEntity>            entities  = new ArrayList<>();
      final List<String>              columns   = new ArrayList<>();
      final List<String>              matches   = new ArrayList<>();
      SSEntity                        entityObj;
      
      column (columns, SSSQLVarU.id);
      column (columns, SSSQLVarU.label);
      column (columns, SSSQLVarU.type);
      column (columns, SSSQLVarU.description);
      match  (matches, SSSQLVarU.description);
      
      resultSet = dbSQL.select(entityTable, columns, matches, requireds, absents, eithers);
      
      while(resultSet.next()){
        
        entityObj =
          SSEntity.get(
            bindingStrToUri        (resultSet, SSSQLVarU.id),
            bindingStrToEntityType (resultSet, SSSQLVarU.type),
            bindingStrToLabel      (resultSet, SSSQLVarU.label));
         
         entityObj.description = bindingStrToTextComment(resultSet, SSSQLVarU.description);
         
         entities.add(entityObj);
      }
      
      return entities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public Boolean getEntityRead(
    final SSUri user, 
    final SSUri entity) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns   = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      
      column(columns, SSSQLVarU.entityId);
      
      where(wheres, SSSQLVarU.userId,   user);
      where(wheres, SSSQLVarU.entityId, entity);
      
      resultSet = dbSQL.select(entityReadsTable, columns, wheres, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void setEntityRead(
    final SSUri   user, 
    final SSUri   entity,
    final Boolean read) throws Exception{
    
    try{

      if(read){
      
        if(getEntityRead(user, entity)){
          return;
        }
        
        final Map<String, String> inserts    = new HashMap<>();
        final Map<String, String> uniqueKeys = new HashMap<>();

        insert(inserts, SSSQLVarU.userId,       user);
        insert(inserts, SSSQLVarU.entityId,     entity);

        uniqueKey(uniqueKeys, SSSQLVarU.userId,   user);
        uniqueKey(uniqueKeys, SSSQLVarU.entityId, entity);

        dbSQL.insertIfNotExists(entityReadsTable, inserts, uniqueKeys);
      }else{
        final Map<String, String> deletes = new HashMap<>();
        
        delete(deletes, SSSQLVarU.userId,   user);
        delete(deletes, SSSQLVarU.entityId, entity);
        
        dbSQL.deleteIgnore(entityReadsTable, deletes);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void addDownload(
    final SSUri   entity, 
    final SSUri   download) throws Exception{
    
    try{

      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarU.entityId,      entity);
      insert(inserts, SSSQLVarU.downloadId,     download);
      
      uniqueKey(uniqueKeys, SSSQLVarU.entityId,    entity);
      uniqueKey(uniqueKeys, SSSQLVarU.downloadId,  download);
      
      dbSQL.insertIfNotExists(downloadsTable, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
//  public void addImage(
//    final SSUri   entity, 
//    final SSUri   image) throws Exception{
//    
//    try{
//
//      final Map<String, String> inserts    = new HashMap<>();
//      final Map<String, String> uniqueKeys = new HashMap<>();
//      
//      insert(inserts, SSSQLVarU.entityId,      entity);
//      insert(inserts, SSSQLVarU.imageId,       image);
//
//      uniqueKey(uniqueKeys, SSSQLVarU.entityId,    entity);
//      uniqueKey(uniqueKeys, SSSQLVarU.imageId,     image);
//      
//      dbSQL.insertIfNotExists(entityImagesTable, inserts, uniqueKeys);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
  
  public void addImage(
    final SSUri    image,
    final SSImageE type) throws Exception{
    
    try{

      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarU.imageId,    image);
      insert(inserts, SSSQLVarU.type,       type);
      
      uniqueKey(uniqueKeys, SSSQLVarU.imageId, image);
      
      dbSQL.insertIfNotExists(imageTable, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void addLocation(
    final SSUri      locationURI,
    final SSUri      entity, 
    final SSLocation location) throws Exception{
    
    try{

      final Map<String, String> inserts    = new HashMap<>();
      
      insert(inserts, SSSQLVarU.locationId,        locationURI);
      insert(inserts, SSSQLVarU.latitude,          location.latitude);
      insert(inserts, SSSQLVarU.longitude,         location.longitude);
      
      if(location.accuracy == null){
        insert(inserts, SSSQLVarU.accuracy,         SSStrU.empty);
      }else{
        insert(inserts, SSSQLVarU.accuracy,         location.accuracy);
      }
      
      dbSQL.insert(locationTable, inserts);
      
      inserts.clear();
      
      insert(inserts, SSSQLVarU.entityId,           entity);
      insert(inserts, SSSQLVarU.locationId,         locationURI);
      
      dbSQL.insert(entityLocationsTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public List<SSLocation> getLocations(
    final SSUri forEntity) throws Exception{
   
    ResultSet resultSet = null;
    
    try{
      final List<SSLocation>     locations  = new ArrayList<>();
      final List<String>         columns    = new ArrayList<>();              
      final Map<String, String>  wheres     = new HashMap<>();

      column(columns, locationTable, SSSQLVarU.locationId);
      column(columns, locationTable, SSSQLVarU.latitude);
      column(columns, locationTable, SSSQLVarU.longitude);
      column(columns, locationTable, SSSQLVarU.accuracy);
        
      if(forEntity != null){
        
        final List<String>         tables     = new ArrayList<>();
        final List<String>         tableCons  = new ArrayList<>();
      
        table(tables, locationTable);
        table(tables, entityLocationsTable);
        
        where(wheres, SSSQLVarU.entityId, forEntity);
        
        tableCon(tableCons, locationTable, SSSQLVarU.locationId, entityLocationsTable, SSSQLVarU.locationId);
        
        resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      }else{
        resultSet = dbSQL.select(locationTable, columns, wheres, null, null, null);
      }
      
      while(resultSet.next()){
        
        locations.add(
          SSLocation.get(
            bindingStrToUri   (resultSet, SSSQLVarU.locationId), 
            bindingStrToDouble(resultSet, SSSQLVarU.latitude), 
            bindingStrToDouble(resultSet, SSSQLVarU.longitude),
            bindingStrToFloat (resultSet, SSSQLVarU.accuracy)));
      }      
      
      return locations;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}


//  public void entityAddOrUpdateLabel(SSUri entity, SSEntityA label) throws Exception {
//
//    Map<String, String>      parNamesAndValues;
//    HashMap<String, String>  newValues;
//
//    if(entityExists(entity)){
//      newValues = new HashMap<>();
//      newValues.put(SSSQLVarU.label,    label.toString());
//
//      parNamesAndValues = new HashMap<>();
//      
//      parNamesAndValues.put(SSSQLVarU.id, entity.toString());
//      
//      dbSQL.dbSQLUpdateWhere(entityTable, parNamesAndValues, newValues);
//    }else{
//    
//      parNamesAndValues = new HashMap<>();
//      
//      parNamesAndValues.put(SSSQLVarU.id,            entity.toString());
//      parNamesAndValues.put(SSSQLVarU.label,         label.toString());
//      parNamesAndValues.put(SSSQLVarU.creationTime,  SSDateU.dateAsNano().toString());
//      
//      dbSQL.dbSQLInsert(entityTable, parNamesAndValues);
//    }
//  }

//  public Boolean existsEntity(SSUri entityUri) throws Exception{
//    
//    if(entityUri == null){
//      SSServErrReg.regErrThrow(new Exception("entity null"));
//      return null;
//    }
//    
//    final Map<String, String> where      = new HashMap<>();
//    final List<String>        columns    = new ArrayList<>();
//    ResultSet                 resultSet  = null;
//    
//    columns.add (SSSQLVarU.id);
//    where.put   (SSSQLVarU.id, entityUri.toString());
//    
//    try{
//      resultSet = dbSQL.selectCertainDistinctWhere(entityTable, columns, where);
//      
//      return resultSet.first();
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }