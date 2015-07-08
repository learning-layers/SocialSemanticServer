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

import at.tugraz.sss.serv.SSDBSQLFct;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchOpE;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSSQLVarNames;
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
      
      column(columns, SSSQLVarNames.id);
      
      where(where, SSSQLVarNames.id, entity);
      
      resultSet = dbSQL.select(SSSQLVarNames.entityTable, columns, where, null, null, null);
      
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
      
      column(columns, SSSQLVarNames.id);
      column(columns, SSSQLVarNames.type);
      column(columns, SSSQLVarNames.label);
      column(columns, SSSQLVarNames.creationTime);
      column(columns, SSSQLVarNames.author);
      column(columns, SSSQLVarNames.description);
      
      where(where, SSSQLVarNames.id, entityUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.entityTable, columns, where, null, null, null);
      
      checkFirstResult(resultSet);
      
      entityObj =
        SSEntity.get(entityUri,
          bindingStrToEntityType (resultSet, SSSQLVarNames.type),
          bindingStrToLabel      (resultSet, SSSQLVarNames.label));
      
      entityObj.creationTime = bindingStrToLong       (resultSet, SSSQLVarNames.creationTime);
      entityObj.author       = bindingStrToAuthor     (resultSet, SSSQLVarNames.author);
      entityObj.description  = bindingStrToTextComment(resultSet, SSSQLVarNames.description);

      return entityObj;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
        SSServErrReg.reset();
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
      
      column(columns, SSSQLVarNames.id);
      column(columns, SSSQLVarNames.label);
      column(columns, SSSQLVarNames.type);
      column(columns, SSSQLVarNames.creationTime);
      column(columns, SSSQLVarNames.author);
      column(columns, SSSQLVarNames.description);
      
      where(where, SSSQLVarNames.label, label);
      where(where, SSSQLVarNames.type,  type);
      
      resultSet = dbSQL.select(SSSQLVarNames.entityTable, columns, where, null, null, null);
      
      checkFirstResult(resultSet);
      
      entityObj =
        SSEntity.get(bindingStrToUri        (resultSet, SSSQLVarNames.id),
          type,
          label);
      
      entityObj.creationTime = bindingStrToLong          (resultSet, SSSQLVarNames.creationTime);
      entityObj.author       = bindingStrToAuthor        (resultSet, SSSQLVarNames.author);
      entityObj.description  = bindingStrToTextComment   (resultSet, SSSQLVarNames.description);

      return entityObj;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
        SSServErrReg.reset();
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
      
      column (columns, SSSQLVarNames.id);
      
      table  (tables, SSSQLVarNames.entityTable);
      
      if(author == null){
        throw new Exception("author has to be given");
      }
      
      final MultivaluedMap<String, String> whereAuthors = new MultivaluedHashMap<>();
      
      where(whereAuthors, SSSQLVarNames.entityTable, SSSQLVarNames.author, author);
      
      wheres.add(whereAuthors);
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSEntityE type : types){
          where(whereTypes, SSSQLVarNames.entityTable, SSSQLVarNames.type, type);
        }
        
        wheres.add(whereTypes);
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
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
      
      column (columns, SSSQLVarNames.id);
      column (columns, SSSQLVarNames.type);
      column (columns, SSSQLVarNames.label);
      column (columns, SSSQLVarNames.creationTime);
      column (columns, SSSQLVarNames.author);
      column (columns, SSSQLVarNames.description);
      
      table  (tables, SSSQLVarNames.entityTable);
      
      if(
        (entityURIs == null || entityURIs.isEmpty()) &&
        (types      == null || types.isEmpty())){
        
        throw new Exception("at least one parameter has to be set");
      }
      
      if(
        entityURIs != null &&
        !entityURIs.isEmpty()){
        
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entityURI : entityURIs){
          where(whereEntities, SSSQLVarNames.entityTable, SSSQLVarNames.id, entityURI);
        }
        
        wheres.add(whereEntities);
      }
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSEntityE type : types){
          where(whereTypes, SSSQLVarNames.entityTable, SSSQLVarNames.type, type);
        }
        
        wheres.add(whereTypes);
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        entity =
          SSEntity.get(bindingStrToUri        (resultSet, SSSQLVarNames.id),
            bindingStrToEntityType (resultSet, SSSQLVarNames.type),
            bindingStrToLabel      (resultSet, SSSQLVarNames.label));
        
        entity.creationTime = bindingStrToLong        (resultSet, SSSQLVarNames.creationTime);
        entity.author       = bindingStrToAuthor      (resultSet, SSSQLVarNames.author);
        entity.description  = bindingStrToTextComment (resultSet, SSSQLVarNames.description);
        
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
  
  //TODO remove duplicate from circle service
  public void addEntityToCircleIfNotExists(
    final SSUri circleUri,
    final SSUri entityUri) throws Exception{
    
    try{
      
      if(hasCircleEntity(circleUri, entityUri)){
        return;
      }
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.circleId, circleUri);
      insert(inserts, SSSQLVarNames.entityId, entityUri);
      
      dbSQL.insert(SSSQLVarNames.circleEntitiesTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  //TODO remove duplicate from circle service
  private Boolean hasCircleEntity(
    final SSUri circleUri,
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      column(columns, SSSQLVarNames.entityId);
      
      where(wheres, SSSQLVarNames.circleId, circleUri);
      where(wheres, SSSQLVarNames.entityId, entityUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleEntitiesTable, columns, wheres, null, null, null);
      
      return resultSet.first();
      
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
      
      insert    (inserts,     SSSQLVarNames.id, entity);
      
      if(
        creationTime == null ||
        creationTime == 0){
        insert(inserts, SSSQLVarNames.creationTime, SSDateU.dateAsLong());
      }else{
        insert(inserts, SSSQLVarNames.creationTime, creationTime);
      }
      
      if(label == null){
        insert(inserts, SSSQLVarNames.label, SSStrU.empty);
      }else{
        insert(inserts, SSSQLVarNames.label, SSStrU.trim(label, SSDBSQLI.entityLabelLength));
      }
      
      if(entityType == null){
        insert(inserts, SSSQLVarNames.type, SSEntityE.entity);
      }else{
        insert(inserts, SSSQLVarNames.type, entityType);
      }
      
      if(authorUri == null){
        insert(inserts, SSSQLVarNames.author, SSStrU.empty);
      }else{
        insert(inserts, SSSQLVarNames.author, authorUri);
      }
      
      if(description == null){
        insert(inserts, SSSQLVarNames.description, SSStrU.empty);
      }else{
        insert(inserts, SSSQLVarNames.description, description);
      }
      
      dbSQL.insert(SSSQLVarNames.entityTable, inserts);
    }else{
      
      try{
        final Map<String, String>  wheres   = new HashMap<>();
        final Map<String, String>  updates  = new HashMap<>();
        
        where(wheres, SSSQLVarNames.id, entity);
        
        if(label != null){
          update (updates, SSSQLVarNames.label, label);
        }
        
        if(description != null){
          update (updates, SSSQLVarNames.description, description);
        }
        
        if(
          creationTime != null &&
          creationTime != 0){
          update(updates, SSSQLVarNames.creationTime, creationTime);
        }
        
        if(updates.isEmpty()){
          return;
        }
        
        dbSQL.update(SSSQLVarNames.entityTable, wheres, updates);
        
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }
    }
  }
  
  public void deleteEntityIfExists(
    final SSUri entityUri) throws Exception{
    
    try{
      final Map<String, String> deletes = new HashMap<>();
      
      delete(deletes, SSSQLVarNames.id, entityUri);
      
      dbSQL.deleteIgnore(SSSQLVarNames.entityTable, deletes);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void removeAllEntities() throws Exception {
    
    try{
      dbSQL.delete(SSSQLVarNames.entityTable);
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
      
      insert(inserts, SSSQLVarNames.entityId,         entity);
      insert(inserts, SSSQLVarNames.attachedEntityId, entityToAttach);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.entityId,          entity);
      uniqueKey(uniqueKeys, SSSQLVarNames.attachedEntityId,  entityToAttach);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.entitiesTable, inserts, uniqueKeys);
      
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
      
      column(columns, SSSQLVarNames.id);
      column(columns, SSSQLVarNames.label);
      column(columns, SSSQLVarNames.creationTime);
      column(columns, SSSQLVarNames.type);
      column(columns, SSSQLVarNames.author);
      column(columns, SSSQLVarNames.description);

      table(tables, SSSQLVarNames.entityTable);
      table(tables, SSSQLVarNames.entitiesTable);
      
      where(wheres, SSSQLVarNames.entitiesTable, SSSQLVarNames.entityId, entity);
      
      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.entitiesTable, SSSQLVarNames.attachedEntityId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        entityObj =
          SSEntity.get(bindingStrToUri           (resultSet, SSSQLVarNames.id),
            bindingStrToEntityType    (resultSet, SSSQLVarNames.type),
            bindingStrToLabel         (resultSet, SSSQLVarNames.label));
        
        entityObj.creationTime = bindingStrToLong          (resultSet, SSSQLVarNames.creationTime);
        entityObj.author       = bindingStrToAuthor        (resultSet, SSSQLVarNames.author);
        entityObj.description  = bindingStrToTextComment   (resultSet, SSSQLVarNames.description);

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
      
      insert(inserts, SSSQLVarNames.entityId,   entity);
      insert(inserts, SSSQLVarNames.fileId,     file);
      
      dbSQL.insert(SSSQLVarNames.filesTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
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
      
      column (columns, SSSQLVarNames.imageTable, SSSQLVarNames.imageId);
      table  (tables, SSSQLVarNames.imageTable);
      
      if(forEntity != null){
        where   (wheres, SSSQLVarNames.entitiesTable, SSSQLVarNames.entityId, forEntity);
        table   (tables, SSSQLVarNames.entitiesTable);
        tableCon(tableCons, SSSQLVarNames.imageTable, SSSQLVarNames.imageId, SSSQLVarNames.entitiesTable, SSSQLVarNames.attachedEntityId);
      }
      
      if(type != null){
        where(wheres, SSSQLVarNames.imageTable, SSSQLVarNames.type, type);
      }
      
      if(!tableCons.isEmpty()){
        resultSet = dbSQL.select(tables,     columns, wheres, tableCons, null, null, null);
      }else{
        resultSet = dbSQL.select(SSSQLVarNames.imageTable, columns, wheres, null, null, null);
      }
      
      return getURIsFromResult(resultSet, SSSQLVarNames.imageId);
      
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
      
      column(columns, SSSQLVarNames.downloadId);
      
      where(wheres, SSSQLVarNames.entityId, entity);
      
      resultSet = dbSQL.select(SSSQLVarNames.downloadsTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.downloadId);
      
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
      
      column(columns, SSSQLVarNames.fileId);
      
      where(wheres, SSSQLVarNames.entityId, entity);
      
      resultSet = dbSQL.select(SSSQLVarNames.filesTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.fileId);
      
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
      
      column (columns, SSSQLVarNames.id);
      column (columns, SSSQLVarNames.label);
      column (columns, SSSQLVarNames.description);
      column (columns, SSSQLVarNames.type);
      match  (matches, SSSQLVarNames.label);
      match  (matches, SSSQLVarNames.description);
      
      resultSet = dbSQL.select(SSSQLVarNames.entityTable, columns, matches, requireds, absents, eithers);
      
      while(resultSet.next()){
      
        entityObj =
          SSEntity.get(bindingStrToUri        (resultSet, SSSQLVarNames.id),
            bindingStrToEntityType (resultSet, SSSQLVarNames.type),
            bindingStrToLabel      (resultSet, SSSQLVarNames.label));
        
        entityObj.description = bindingStrToTextComment(resultSet, SSSQLVarNames.description);
        
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
      
      column (columns, SSSQLVarNames.id);
      column (columns, SSSQLVarNames.label);
      column (columns, SSSQLVarNames.type);
      column (columns, SSSQLVarNames.description);
      
      table(tables, SSSQLVarNames.entityTable);
      
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
              where(likeContents, SSSQLVarNames.entityTable, SSSQLVarNames.label, labelString);
            }
          }
          
          if(
            descStrings != null &&
            !descStrings.isEmpty()){
            
            for(String descString : descStrings){
              where(likeContents, SSSQLVarNames.entityTable, SSSQLVarNames.description, descString);
            }
          }
          
          likes.add(likeContents);
          break;
        }
        
        case and:{
          
          for(String labelString : labelStrings){
            
            likeContents = new MultivaluedHashMap<>();
            
            where(likeContents, SSSQLVarNames.entityTable, SSSQLVarNames.label, labelString);
            
            likes.add(likeContents);
          }
          
          for(String descString : descStrings){
            
            likeContents = new MultivaluedHashMap<>();
            
            where(likeContents, SSSQLVarNames.entityTable, SSSQLVarNames.description, descString);
            
            likes.add(likeContents);
          }
          
          break;
        }
      }
      
      resultSet = dbSQL.selectLike(tables, columns, likes, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        entityObj =
          SSEntity.get(bindingStrToUri        (resultSet, SSSQLVarNames.id),
            bindingStrToEntityType (resultSet, SSSQLVarNames.type),
            bindingStrToLabel      (resultSet, SSSQLVarNames.label));
         
         entityObj.description = bindingStrToTextComment(resultSet, SSSQLVarNames.description);
         
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
      
      column (columns, SSSQLVarNames.id);
      column (columns, SSSQLVarNames.label);
      column (columns, SSSQLVarNames.type);
      column (columns, SSSQLVarNames.description);
      match  (matches, SSSQLVarNames.label);
      
      resultSet = dbSQL.select(SSSQLVarNames.entityTable, columns, matches, requireds, absents, eithers);
      
      while(resultSet.next()){
        
        entityObj =
          SSEntity.get(bindingStrToUri        (resultSet, SSSQLVarNames.id),
            bindingStrToEntityType (resultSet, SSSQLVarNames.type),
            bindingStrToLabel      (resultSet, SSSQLVarNames.label));
         
         entityObj.description = bindingStrToTextComment(resultSet, SSSQLVarNames.description);
         
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
      
      column (columns, SSSQLVarNames.id);
      column (columns, SSSQLVarNames.label);
      column (columns, SSSQLVarNames.type);
      column (columns, SSSQLVarNames.description);
      match  (matches, SSSQLVarNames.description);
      
      resultSet = dbSQL.select(SSSQLVarNames.entityTable, columns, matches, requireds, absents, eithers);
      
      while(resultSet.next()){
        
        entityObj =
          SSEntity.get(bindingStrToUri        (resultSet, SSSQLVarNames.id),
            bindingStrToEntityType (resultSet, SSSQLVarNames.type),
            bindingStrToLabel      (resultSet, SSSQLVarNames.label));
         
         entityObj.description = bindingStrToTextComment(resultSet, SSSQLVarNames.description);
         
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
      
      column(columns, SSSQLVarNames.entityId);
      
      where(wheres, SSSQLVarNames.userId,   user);
      where(wheres, SSSQLVarNames.entityId, entity);
      
      resultSet = dbSQL.select(SSSQLVarNames.entityReadsTable, columns, wheres, null, null, null);
      
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

        insert(inserts, SSSQLVarNames.userId,       user);
        insert(inserts, SSSQLVarNames.entityId,     entity);

        uniqueKey(uniqueKeys, SSSQLVarNames.userId,   user);
        uniqueKey(uniqueKeys, SSSQLVarNames.entityId, entity);

        dbSQL.insertIfNotExists(SSSQLVarNames.entityReadsTable, inserts, uniqueKeys);
      }else{
        final Map<String, String> deletes = new HashMap<>();
        
        delete(deletes, SSSQLVarNames.userId,   user);
        delete(deletes, SSSQLVarNames.entityId, entity);
        
        dbSQL.deleteIgnore(SSSQLVarNames.entityReadsTable, deletes);
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
      
      insert(inserts, SSSQLVarNames.entityId,      entity);
      insert(inserts, SSSQLVarNames.downloadId,     download);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.entityId,    entity);
      uniqueKey(uniqueKeys, SSSQLVarNames.downloadId,  download);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.downloadsTable, inserts, uniqueKeys);
      
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
      
      insert(inserts, SSSQLVarNames.imageId,    image);
      insert(inserts, SSSQLVarNames.type,       type);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.imageId, image);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.imageTable, inserts, uniqueKeys);
      
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
      
      insert(inserts, SSSQLVarNames.locationId,        locationURI);
      insert(inserts, SSSQLVarNames.latitude,          location.latitude);
      insert(inserts, SSSQLVarNames.longitude,         location.longitude);
      
      if(location.accuracy == null){
        insert(inserts, SSSQLVarNames.accuracy,         SSStrU.empty);
      }else{
        insert(inserts, SSSQLVarNames.accuracy,         location.accuracy);
      }
      
      dbSQL.insert(SSSQLVarNames.locationTable, inserts);
      
      inserts.clear();
      
      insert(inserts, SSSQLVarNames.entityId,           entity);
      insert(inserts, SSSQLVarNames.locationId,         locationURI);
      
      dbSQL.insert(SSSQLVarNames.entityLocationsTable, inserts);
      
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

      column(columns, SSSQLVarNames.locationTable, SSSQLVarNames.locationId);
      column(columns, SSSQLVarNames.locationTable, SSSQLVarNames.latitude);
      column(columns, SSSQLVarNames.locationTable, SSSQLVarNames.longitude);
      column(columns, SSSQLVarNames.locationTable, SSSQLVarNames.accuracy);
        
      if(forEntity != null){
        
        final List<String>         tables     = new ArrayList<>();
        final List<String>         tableCons  = new ArrayList<>();
      
        table(tables, SSSQLVarNames.locationTable);
        table(tables, SSSQLVarNames.entityLocationsTable);
        
        where(wheres, SSSQLVarNames.entityId, forEntity);
        
        tableCon(tableCons, SSSQLVarNames.locationTable, SSSQLVarNames.locationId, SSSQLVarNames.entityLocationsTable, SSSQLVarNames.locationId);
        
        resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      }else{
        resultSet = dbSQL.select(SSSQLVarNames.locationTable, columns, wheres, null, null, null);
      }
      
      while(resultSet.next()){
        
        locations.add(SSLocation.get(bindingStrToUri   (resultSet, SSSQLVarNames.locationId), 
            bindingStrToDouble(resultSet, SSSQLVarNames.latitude), 
            bindingStrToDouble(resultSet, SSSQLVarNames.longitude),
            bindingStrToFloat (resultSet, SSSQLVarNames.accuracy)));
      }      
      
      return locations;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSEntity> getAccessibleEntityURIs(
    final SSUri           user,
    final Boolean         withSystemCircles,
    final List<SSEntityE> types) throws Exception{
    
    ResultSet resultSet = null;
    
    final List<MultivaluedMap<String, String>> wheres         = new ArrayList<>();
    final List<String>                         tables         = new ArrayList<>();
    final List<String>                         columns        = new ArrayList<>();
    final List<String>                         tableCons      = new ArrayList<>();
    
    column(columns, SSSQLVarNames.circleEntitiesTable,   SSSQLVarNames.entityId);
//      column(columns, circleUsersTable,      SSSQLVarU.circleId);
    
    table    (tables, SSSQLVarNames.circleUsersTable);
    table    (tables, SSSQLVarNames.circleEntitiesTable);
    table    (tables, SSSQLVarNames.entityTable);
    
    tableCon (tableCons, SSSQLVarNames.circleUsersTable,    SSSQLVarNames.circleId, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.circleId);
    tableCon (tableCons, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.entityId, SSSQLVarNames.entityTable,         SSSQLVarNames.id);
    
    if(user == null){
      throw new Exception("user has to be given");
    }
    
    final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
    
    where(whereUsers, SSSQLVarNames.circleUsersTable, SSSQLVarNames.userId, user);
    
    wheres.add(whereUsers);
    
    if(!withSystemCircles){
      
      table    (tables, SSSQLVarNames.circleTable);
      tableCon (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleUsersTable,         SSSQLVarNames.circleId);
      
      final MultivaluedMap<String, String> whereIsSystemCircles = new MultivaluedHashMap<>();
      
      where(whereIsSystemCircles, SSSQLVarNames.circleTable, SSSQLVarNames.isSystemCircle, withSystemCircles);
      
      wheres.add(whereIsSystemCircles);
    }
    
    if(
      types != null &&
      !types.isEmpty()){
      
      final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
      
      for(SSEntityE type : types){
        where(whereTypes, SSSQLVarNames.entityTable, SSSQLVarNames.type, type);
      }
      
      wheres.add(whereTypes);
    }
    
    resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
    
    return SSEntity.get(getURIsFromResult(resultSet, SSSQLVarNames.entityId), SSEntityE.entity);
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