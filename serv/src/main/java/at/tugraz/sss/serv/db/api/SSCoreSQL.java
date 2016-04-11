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
package at.tugraz.sss.serv.db.api;

import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.util.SSSQLVarNames;
import at.tugraz.sss.serv.errreg.SSServErrReg;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.SSCircle;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.par.SSDBSQLSelectPar;
import at.tugraz.sss.serv.datatype.enums.SSSearchOpE;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.datatype.enums.SSCircleRightE;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.enums.SSCircleE;
import at.tugraz.sss.serv.datatype.api.SSEntityA;
import at.tugraz.sss.serv.datatype.par.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class SSCoreSQL extends SSDBSQLFctA{
  
  public SSCoreSQL(final SSDBSQLI dbSQL){
    
    super(dbSQL);
  }
  
  public SSEntity getAuthorEntityFromResult(
    final SSServPar servPar,
    final ResultSet resultSet,
    final SSUri     systemUserURI) throws SSErr {
    
    try{
      
      return getEntityTest(
        servPar,
        systemUserURI,
        null, //user
        bindingStrToUri(resultSet, SSSQLVarNames.author),
        false); //withUserRestriction
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSEntity> getEntities(
    final SSServPar servPar,
    final SSLabel   label,
    final SSEntityE type) throws SSErr{
    
    ResultSet resultSet  = null;
    
    try{
      
      if(SSObjU.isNull(label, type)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<String>        columns  = new ArrayList<>();
      final Map<String, String> where    = new HashMap<>();
      final List<SSEntity>      entities = new ArrayList<>();
      
      column(columns, SSSQLVarNames.id);
      column(columns, SSSQLVarNames.label);
      column(columns, SSSQLVarNames.type);
      column(columns, SSSQLVarNames.creationTime);
      column(columns, SSSQLVarNames.author);
      column(columns, SSSQLVarNames.description);
      
      where(where, SSSQLVarNames.label, label);
      where(where, SSSQLVarNames.type,  type);
      
      resultSet = 
        dbSQL.select(
          servPar,
          SSEntitySQLTableE.entity, 
          columns, 
          where, 
          null, 
          null, 
          null);
      
      while(resultSet.next()){
        
        entities.add(
          SSEntity.get(
            bindingStrToUri        (resultSet, SSSQLVarNames.id),
            type,
            label, 
            bindingStrToTextComment   (resultSet, SSSQLVarNames.description),
            bindingStrToLong          (resultSet, SSSQLVarNames.creationTime), 
            bindingStrToAuthor        (resultSet, SSSQLVarNames.author)));
      }
      
      return entities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSUri> getEntityURIs(
    final SSServPar       servPar,
    final List<SSUri>     entities, 
    final List<SSEntityE> types, 
    final Long            startTime, 
    final Long            endTime) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      if(
        (entities == null || entities.isEmpty()) &&
        (types    == null || types.isEmpty())){
        
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<MultivaluedMap<String, String>>                   wheres         = new ArrayList<>();
      final MultivaluedMap<String, MultivaluedMap<String, String>> wheresNumeric  = new MultivaluedHashMap<>();
      final List<SSSQLTableI>                                      tables         = new ArrayList<>();
      final List<String>                                           columns        = new ArrayList<>();
      final List<String>                                           tableCons      = new ArrayList<>();
      
      column(columns, SSEntitySQLTableE.entity, SSSQLVarNames.id);
      
      table (tables, SSEntitySQLTableE.entity);
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSEntityE type : types){
          where(whereTypes, SSEntitySQLTableE.entity, SSSQLVarNames.type, type);
        }
        
        wheres.add(whereTypes);
      }
      
      if(
        entities != null &&
        !entities.isEmpty()){
        
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, SSEntitySQLTableE.entity, SSSQLVarNames.id, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      if(
        startTime != null &&
        startTime != 0){
        
        final List<MultivaluedMap<String, String>> greaterWheres           = new ArrayList<>();
        final MultivaluedMap<String, String>       whereNumbericStartTimes = new MultivaluedHashMap<>();
        
        wheresNumeric.put(SSStrU.greaterThan, greaterWheres);
        
        where(whereNumbericStartTimes, SSEntitySQLTableE.entity, SSSQLVarNames.creationTime, startTime);
        
        greaterWheres.add(whereNumbericStartTimes);
      }
      
      if(
        endTime != null &&
        endTime != 0){
        
        final List<MultivaluedMap<String, String>> lessWheres            = new ArrayList<>();
        final MultivaluedMap<String, String>       whereNumbericEndTimes = new MultivaluedHashMap<>();
        
        wheresNumeric.put(SSStrU.lessThan, lessWheres);
        
        where(whereNumbericEndTimes, SSEntitySQLTableE.entity, SSSQLVarNames.creationTime, endTime);
        
        lessWheres.add(whereNumbericEndTimes);
      }
      
      resultSet =
        dbSQL.select(
          new SSDBSQLSelectPar(
            servPar,
            tables, 
            columns,
            wheres, //orWheres
            null, //andWheres
            wheresNumeric, //numericWheres
            tableCons));
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSEntity> getEntities(
    final SSServPar       servPar,
    final List<SSUri>     entityURIs,
    final List<SSEntityE> types) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      if(
        (entityURIs == null || entityURIs.isEmpty()) &&
        (types      == null || types.isEmpty())){
        
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<String>                         columns    = new ArrayList<>();
      final List<SSSQLTableI>                    tables     = new ArrayList<>();
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
      
      table  (tables, SSEntitySQLTableE.entity);
      
      if(
        entityURIs != null &&
        !entityURIs.isEmpty()){
        
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entityURI : entityURIs){
          where(whereEntities, SSEntitySQLTableE.entity, SSSQLVarNames.id, entityURI);
        }
        
        wheres.add(whereEntities);
      }
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSEntityE type : types){
          where(whereTypes, SSEntitySQLTableE.entity, SSSQLVarNames.type, type);
        }
        
        wheres.add(whereTypes);
      }
      
      resultSet =
        dbSQL.select(
          new SSDBSQLSelectPar(
            servPar,
            tables,
            columns,
            wheres,
            null,
            null,
            tableCons));
      
      while(resultSet.next()){
        
        entity =
          SSEntity.get(
            bindingStrToUri        (resultSet, SSSQLVarNames.id),
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
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public void addUserToAdditionalAuthors(
    final SSServPar       servPar,
    final SSUri           userURI,
    final SSUri           entityURI, 
    final Long            creationTime) throws SSErr{
    
     try{
      
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userId,       userURI);
      insert(inserts, SSSQLVarNames.entityId,     entityURI);
      
      if(
        creationTime == null ||
        creationTime == 0){
        insert(inserts, SSSQLVarNames.creationTime, SSDateU.dateAsLong());
      }else{
        insert(inserts, SSSQLVarNames.creationTime, creationTime);
      }
      
      uniqueKey(uniqueKeys, SSSQLVarNames.userId,   userURI);
      uniqueKey(uniqueKeys, SSSQLVarNames.entityId, entityURI);
      
      dbSQL.insertIfNotExists(servPar, SSEntitySQLTableE.entityauthors, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addEntityToCircleIfNotExists(
    final SSServPar       servPar,
    final SSUri           circleUri,
    final SSUri           entityUri) throws SSErr{
    
    try{
      
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.circleId, circleUri);
      insert(inserts, SSSQLVarNames.entityId, entityUri);

      uniqueKey(uniqueKeys, SSSQLVarNames.circleId, circleUri);
      uniqueKey(uniqueKeys, SSSQLVarNames.entityId, entityUri);
      
      dbSQL.insertIfNotExists(servPar, SSEntitySQLTableE.circleentities, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addEntityIfNotExists(
    final SSServPar       servPar,
    final SSUri         entity, 
    final SSEntityE     entityType,
    final SSEntityA     label,
    final SSTextComment description,
    final SSUri         author,
    final Long          creationTime) throws SSErr{
    
    try{
      
      if(!existsEntity(servPar, entity)){
        
        final Map<String, String> inserts = new HashMap<>();
        
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
        
        if(author == null){
          insert(inserts, SSSQLVarNames.author, SSStrU.empty);
        }else{
          insert(inserts, SSSQLVarNames.author, author);
        }
        
        if(description == null){
          insert(inserts, SSSQLVarNames.description, SSStrU.empty);
        }else{
          insert(inserts, SSSQLVarNames.description, description);
        }
        
        dbSQL.insert(servPar, SSEntitySQLTableE.entity, inserts);
      }else{
        
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
        
        dbSQL.update(servPar, SSEntitySQLTableE.entity, wheres, updates);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void deleteEntityIfExists(
    final SSServPar       servPar,
    final SSUri entityUri) throws SSErr{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.id, entityUri);
      
      dbSQL.deleteIgnore(servPar, SSEntitySQLTableE.entity, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void removeAllEntities(
    final SSServPar       servPar) throws SSErr {
    
    try{
      dbSQL.delete(servPar, SSEntitySQLTableE.entity);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeAttachedEntities(
    final SSServPar       servPar,
    final SSUri       entity,
    final List<SSUri> attachments) throws SSErr{
    
    try{
      
      final List<MultivaluedMap<String, String>> wheres                = new ArrayList<>();
      final MultivaluedMap<String, String>       whereEntity           = new MultivaluedHashMap<>();
      final MultivaluedMap<String, String>       whereAttachedEntities = new MultivaluedHashMap<>();
      
      where(whereEntity, SSEntitySQLTableE.entityattachedentities, SSSQLVarNames.entityId, entity);
        
      wheres.add(whereEntity);
      
      for(SSUri attachment : attachments){
        where(whereAttachedEntities, SSEntitySQLTableE.entityattachedentities, SSSQLVarNames.attachedEntityId, attachment);
      }

      wheres.add(whereAttachedEntities);
      
      dbSQL.deleteIgnore(servPar, SSEntitySQLTableE.entityattachedentities, wheres);
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void attachEntities(
    final SSServPar       servPar,
    final SSUri       entity,
    final List<SSUri> entitiesToAttach) throws SSErr{
    
    try{

      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      for(SSUri entityToAttach : entitiesToAttach){
      
        inserts.clear();
        uniqueKeys.clear();
        
        insert(inserts, SSSQLVarNames.entityId,         entity);
        insert(inserts, SSSQLVarNames.attachedEntityId, entityToAttach);

        uniqueKey(uniqueKeys, SSSQLVarNames.entityId,          entity);
        uniqueKey(uniqueKeys, SSSQLVarNames.attachedEntityId,  entityToAttach);

        dbSQL.insertIfNotExists(servPar, SSEntitySQLTableE.entityattachedentities, inserts, uniqueKeys);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getAttachedEntities(
    final SSServPar       servPar,
    final SSUri entity) throws SSErr{
    
    ResultSet resultSet = null;
    
    if(entity == null){
      throw SSErr.get(SSErrE.parameterMissing);
    }
    
    try{
      
      final List<String>        columns          = new ArrayList<>();
      final List<SSSQLTableI>   tables           = new ArrayList<>();
      final Map<String, String> wheres           = new HashMap<>();
      final List<String>        tableCons        = new ArrayList<>();
      
      column(columns, SSSQLVarNames.id);

      table(tables, SSEntitySQLTableE.entity);
      table(tables, SSEntitySQLTableE.entityattachedentities);
      
      where(wheres, SSEntitySQLTableE.entityattachedentities, SSSQLVarNames.entityId, entity);
      
      tableCon(tableCons, SSEntitySQLTableE.entity, SSSQLVarNames.id, SSEntitySQLTableE.entityattachedentities, SSSQLVarNames.attachedEntityId);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public boolean getEntityRead(
    final SSServPar       servPar,
    final SSUri user, 
    final SSUri entity) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns   = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.entityId);
      
      where(wheres, SSSQLVarNames.userId,   user);
      where(wheres, SSSQLVarNames.entityId, entity);
      
      resultSet = dbSQL.select(servPar, SSEntitySQLTableE.entityreads, columns, wheres, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }

  public void setEntityRead(
    final SSServPar       servPar,
    final SSUri   user, 
    final SSUri   entity,
    final boolean read) throws SSErr{
    
    try{

      if(read){
      
        if(getEntityRead(servPar, user, entity)){
          return;
        }
        
        final Map<String, String> inserts    = new HashMap<>();
        final Map<String, String> uniqueKeys = new HashMap<>();

        insert(inserts, SSSQLVarNames.userId,       user);
        insert(inserts, SSSQLVarNames.entityId,     entity);

        uniqueKey(uniqueKeys, SSSQLVarNames.userId,   user);
        uniqueKey(uniqueKeys, SSSQLVarNames.entityId, entity);

        dbSQL.insertIfNotExists(servPar, SSEntitySQLTableE.entityreads, inserts, uniqueKeys);
      }else{
        final Map<String, String> wheres = new HashMap<>();
        
        where(wheres, SSSQLVarNames.userId,   user);
        where(wheres, SSSQLVarNames.entityId, entity);
        
        dbSQL.deleteIgnore(servPar, SSEntitySQLTableE.entityreads, wheres);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void addDownloads(
    final SSServPar       servPar,
    final SSUri         entity,
    final List<SSUri>   downloads) throws SSErr{
    
    try{
      
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      for(SSUri download : downloads){
        
        inserts.clear();
        uniqueKeys.clear();
        
        insert(inserts, SSSQLVarNames.entityId,       entity);
        insert(inserts, SSSQLVarNames.downloadId,     download);
        
        uniqueKey(uniqueKeys, SSSQLVarNames.entityId,    entity);
        uniqueKey(uniqueKeys, SSSQLVarNames.downloadId,  download);
        
        dbSQL.insertIfNotExists(servPar, SSEntitySQLTableE.entitydownloads, inserts, uniqueKeys);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getDownloads(
    final SSServPar       servPar,
    final SSUri entity) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns           = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      
      column(columns, SSSQLVarNames.downloadId);
      
      where(wheres, SSSQLVarNames.entityId, entity);
      
      resultSet = dbSQL.select(servPar, SSEntitySQLTableE.entitydownloads, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.downloadId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSUri> filterEntitiesByAllAuthors(
    final SSServPar   servPar,
    final List<SSUri> entityURIs,
    final List<SSUri> authorURIs) throws SSErr{
    
    try{
      
      if(
        entityURIs.isEmpty() ||
        authorURIs.isEmpty()){
        return entityURIs;
      }
      
      final List<SSUri> result = new ArrayList<>();
      
      result.addAll(
        filterEntitiesByAuthors(
          servPar,
          entityURIs,
          authorURIs));
      
      SSUri.addDistinctWithoutNull(
        result,
        filterEntitiesByAdditionalAuthors(
          servPar,
          entityURIs,
          authorURIs));
      
      return result;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private List<SSUri> filterEntitiesByAdditionalAuthors(
    final SSServPar       servPar,
    final List<SSUri>     entities,
    final List<SSUri>     authors) throws SSErr{
    
    ResultSet resultSet  = null;
    
    try{
      
      if(
        entities.isEmpty() || 
        authors.isEmpty()){
        SSServErrReg.regErrThrow(SSErrE.parameterMissing);
        return null;
      }
      
      final List<String>                         columns           = new ArrayList<>();
      final List<SSSQLTableI>                    tables            = new ArrayList<>();
      final List<String>                         tableCons         = new ArrayList<>();
      final List<MultivaluedMap<String, String>> orWheres          = new ArrayList<>();
      
      column(columns, SSSQLVarNames.entityId);
      
      table(tables, SSEntitySQLTableE.entityauthors);
      
      final MultivaluedMap<String, String> authorsPart = new MultivaluedHashMap<>();
      
      for(SSUri author : authors){
        where(authorsPart, SSEntitySQLTableE.entityauthors,        SSSQLVarNames.userId, author);
      }
      
      orWheres.add(authorsPart);
      
      final MultivaluedMap<String, String> entitiesPart = new MultivaluedHashMap<>();
      
      for(SSUri entity : entities){
        where(entitiesPart, SSEntitySQLTableE.entityauthors, SSSQLVarNames.entityId, entity);
      }
      
      orWheres.add(entitiesPart);
      
      resultSet =
        dbSQL.select(
          new SSDBSQLSelectPar(
            servPar, 
            tables, 
            columns,
            orWheres, 
            null, //andWheres, 
            null, //numbericWheres, 
            tableCons));
      
      return getURIsFromResult(resultSet, SSSQLVarNames.entityId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  private List<SSUri> filterEntitiesByAuthors(
    final SSServPar       servPar,
    final List<SSUri>     entities,
    final List<SSUri>     authors) throws SSErr{
    
    ResultSet resultSet  = null;
    
    try{
      
      if(
        entities.isEmpty() || 
        authors.isEmpty()){
        SSServErrReg.regErrThrow(SSErrE.parameterMissing);
        return null;
      }
      
      final List<String>                         columns           = new ArrayList<>();
      final List<SSSQLTableI>                    tables            = new ArrayList<>();
      final List<String>                         tableCons         = new ArrayList<>();
      final List<MultivaluedMap<String, String>> orWheres          = new ArrayList<>();
      
      column(columns, SSSQLVarNames.id);
      
      table(tables, SSEntitySQLTableE.entity);
      
      final MultivaluedMap<String, String> authorsPart = new MultivaluedHashMap<>();
      
      for(SSUri author : authors){
        where(authorsPart, SSEntitySQLTableE.entity,        SSSQLVarNames.author, author);
      }
      
      orWheres.add(authorsPart);
      
      final MultivaluedMap<String, String> entitiesPart = new MultivaluedHashMap<>();
      
      for(SSUri entity : entities){
        where(entitiesPart, SSEntitySQLTableE.entity, SSSQLVarNames.id, entity);
      }
      
      orWheres.add(entitiesPart);
      
      resultSet =
        dbSQL.select(
          new SSDBSQLSelectPar(
            servPar, 
            tables, 
            columns,
            orWheres, 
            null, //andWheres, 
            null, //numbericWheres, 
            tableCons));
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSUri> getAccessibleURIs(
    final SSServPar       servPar,
    final SSUri           systemUserURI,
    final SSUri           user,
    final List<SSEntityE> types,
    final Long            startTime,
    final Long            endTime) throws SSErr{
    
    ResultSet resultSet  = null;
    
    try{
      
      if(
        user == null ||
        SSStrU.isEqual(user, systemUserURI)){
        
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final String userStr                 = SSStrU.toStr(user);
      String       query                   = "select DISTINCT id from entity where ";
      String       typesQueryPart          = SSStrU.empty;
      String       creationTimeQueryPart   = SSStrU.empty;
      
      if(
        types != null &&
        !types.isEmpty()){
        
        typesQueryPart += SSStrU.bracketOpen;
          
        for(SSEntityE type : types){
          typesQueryPart += "type = '" + type.toString() + "' OR ";
        }
        
        typesQueryPart  = SSStrU.removeTrailingString(typesQueryPart, " OR ") + SSStrU.bracketClose;
      }
      
      if(
        startTime != null &&
        startTime != 0){

        creationTimeQueryPart += SSStrU.bracketOpen;
        
        creationTimeQueryPart += "creationTime > " + startTime;
      }
      
      if(
        endTime != null &&
        endTime != 0){

        if(!creationTimeQueryPart.isEmpty()){
          creationTimeQueryPart += " AND ";
        }else{
          creationTimeQueryPart += SSStrU.bracketOpen;
        }
        
        creationTimeQueryPart += "creationTime < " + endTime + SSStrU.bracketClose;
      }else{
        
        if(!creationTimeQueryPart.isEmpty()){
          creationTimeQueryPart += SSStrU.bracketClose;
        }
      }
      
      if(
        !typesQueryPart.isEmpty()    ||
        !creationTimeQueryPart.isEmpty()){
        
        if(!typesQueryPart.isEmpty()){
          query += typesQueryPart + " AND ";
        }
        
        if(!creationTimeQueryPart.isEmpty()){
          query += creationTimeQueryPart + " AND ";
        }
        
        query += SSStrU.bracketOpen;
      }
      
      query += "(type  = 'entity') OR ";
      query += "(type  = 'circle' AND id IN (select DISTINCT circleId from circle where circleType = 'pub' or circleType = 'pubCircle')) OR ";
      query += "(type  = 'circle' AND id IN (select DISTINCT circle.circleId from circle, circleusers where circleusers.userId = '" + userStr + "' and circle.circleId = circleusers.circleId)) OR ";
      query += "(type != 'entity' AND type != 'circle' AND id IN (select DISTINCT circleentities.entityId from circle, circleentities, circleusers where userId = '" + userStr + "' and circle.circleId = circleentities.circleId and circle.circleId = circleusers.circleId))";
      
      if(
        !typesQueryPart.isEmpty()     ||
        !creationTimeQueryPart.isEmpty()){
        
        query += SSStrU.bracketClose;
      }
      
      resultSet = dbSQL.select(servPar, query);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public SSEntity getEntityTest(
    final SSServPar       servPar,
    final SSUri           systemUserURI,
    final SSUri           user,
    final SSUri           entity,
    final boolean         withUserRestriction) throws SSErr{
    
    ResultSet resultSet  = null;
    
    try{
      
      if(
        user == null &&
        withUserRestriction){
        
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final String entityStr = entity.toString();
      final String userStr   = SSStrU.toStr(user);
      String       query;
      
      if(
        withUserRestriction &&
        !SSStrU.isEqual(userStr, systemUserURI)){
        
        query = "select DISTINCT id, type, label, description, author, creationTime from entity where ";
        query += "(id = '" + entityStr + "' AND type  = 'entity') OR ";
        query += "(id = '" + entityStr + "' AND type  = 'circle' AND id = (select DISTINCT circleId from circle where circleId = '" + entityStr + "' and (circleType = 'pub' or circleType = 'pubCircle'))) OR ";
        query += "(id = '" + entityStr + "' AND type  = 'circle' AND id = (select DISTINCT circle.circleId from circle, circleusers where circle.circleId = '" + entityStr + "' and circleusers.userId = '" + userStr + "' and circle.circleId = circleusers.circleId)) OR ";
        query += "(id = '" + entityStr + "' AND type != 'entity' AND type != 'circle' AND id = (select DISTINCT circleentities.entityId from circle, circleentities, circleusers where entityId = '" + entityStr + "' and userId = '" + userStr + "' and circle.circleId = circleentities.circleId and circle.circleId = circleusers.circleId))";
  
      }else{
        query = "select DISTINCT id, type, label, description, author, creationTime from entity where entity.id ='" + entityStr + "'";
      }
      
      resultSet = dbSQL.select(servPar, query);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return SSEntity.get(
        bindingStrToUri        (resultSet, SSSQLVarNames.id),
        bindingStrToEntityType (resultSet, SSSQLVarNames.type),
        bindingStrToLabel      (resultSet, SSSQLVarNames.label),
        bindingStrToTextComment(resultSet, SSSQLVarNames.description),
        bindingStrToLong       (resultSet, SSSQLVarNames.creationTime),
        bindingStrToAuthor     (resultSet, SSSQLVarNames.author));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public boolean isUserAuthor(
    final SSServPar   servPar,
    final SSUri       user, 
    final SSUri       entityURI) throws SSErr{
    
    try{
      
      if(SSObjU.isNull(user, entityURI)){
        return false;
      }
      
      final List<SSUri> entityURIs =
        filterEntitiesByAllAuthors(
          servPar,
          SSUri.asListNotNull(entityURI),
          SSUri.asListNotNull(user));
      
      if(entityURIs.isEmpty()){
        return false;
      }
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  public void setEntityColumns(
    final List<String> columns) throws SSErr{
    
    try{
      column(columns, SSEntitySQLTableE.entity, SSSQLVarNames.id);
      column(columns, SSEntitySQLTableE.entity, SSSQLVarNames.type);
      column(columns, SSEntitySQLTableE.entity, SSSQLVarNames.label);
      column(columns, SSEntitySQLTableE.entity, SSSQLVarNames.creationTime);
      column(columns, SSEntitySQLTableE.entity, SSSQLVarNames.author);
      column(columns, SSEntitySQLTableE.entity, SSSQLVarNames.description);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void setEntityTable(
    final List<SSSQLTableI> tables) throws SSErr{
    
    try{
      table(tables, SSEntitySQLTableE.entity);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public boolean existsEntity(
    final SSServPar       servPar,
    final SSUri entity) throws SSErr{
    
    ResultSet resultSet  = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> where   = new HashMap<>();
      
      column(columns, SSSQLVarNames.id);
      
      where(where, SSSQLVarNames.id, entity);
      
      resultSet = 
        dbSQL.select(
          servPar, 
          SSEntitySQLTableE.entity, 
          columns, 
          where, 
          null, 
          null, 
          null);
      
      return existsFirstResult(resultSet);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSEntity> getEntitiesForLabelsAndDescriptionsWithSQLLike(
    final SSServPar       servPar,
    final List<String> labelStrings,
    final List<String> descStrings,
    final SSSearchOpE  searchOp) throws SSErr{
  
    ResultSet resultSet = null;
    
    try{
      final List<SSEntity>                       entities  = new ArrayList<>();
      final List<MultivaluedMap<String, String>> likes     = new ArrayList<>();
      final List<String>                         columns   = new ArrayList<>();
      final List<SSSQLTableI>                    tables    = new ArrayList<>();
      final List<String>                         tableCons  = new ArrayList<>();
      SSEntity                                   entityObj;
      
      column (columns, SSSQLVarNames.id);
      column (columns, SSSQLVarNames.label);
      column (columns, SSSQLVarNames.type);
      column (columns, SSSQLVarNames.description);
      
      table(tables, SSEntitySQLTableE.entity);
      
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
              where(likeContents, SSEntitySQLTableE.entity, SSSQLVarNames.label, labelString);
            }
          }
          
          if(
            descStrings != null &&
            !descStrings.isEmpty()){
            
            for(String descString : descStrings){
              where(likeContents, SSEntitySQLTableE.entity, SSSQLVarNames.description, descString);
            }
          }
          
          likes.add(likeContents);
          break;
        }
        
        case and:{
          
          for(String labelString : labelStrings){
            
            likeContents = new MultivaluedHashMap<>();
            
            where(likeContents, SSEntitySQLTableE.entity, SSSQLVarNames.label, labelString);
            
            likes.add(likeContents);
          }
          
          for(String descString : descStrings){
            
            likeContents = new MultivaluedHashMap<>();
            
            where(likeContents, SSEntitySQLTableE.entity, SSSQLVarNames.description, descString);
            
            likes.add(likeContents);
          }
          
          break;
        }
      }
      
      resultSet = dbSQL.selectLike(servPar, tables, columns, likes, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        entityObj =
          SSEntity.get(
            bindingStrToUri        (resultSet, SSSQLVarNames.id),
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
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSUri> getEntitiesForLabelsWithLike(
    final SSServPar       servPar,
    final List<String> labels) throws SSErr{
  
    ResultSet resultSet = null;
    
    try{
      
      if(
        labels       == null ||
        labels.isEmpty()){
        
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<MultivaluedMap<String, String>> likes        = new ArrayList<>();
      final List<String>                         columns      = new ArrayList<>();
      final List<SSSQLTableI>                    tables       = new ArrayList<>();
      final List<String>                         tableCons    = new ArrayList<>();
      final MultivaluedMap<String, String>       likeContents = new MultivaluedHashMap<>();
      
      column (columns, SSSQLVarNames.id);
      
      table(tables, SSEntitySQLTableE.entity);
      
      for(String label : labels){
        where(likeContents, SSEntitySQLTableE.entity, SSSQLVarNames.label, label);
      }
      
      likes.add(likeContents);
      
      resultSet =
        dbSQL.selectLike(
          servPar, 
          tables,
          columns,
          likes,
          tableCons,
          null,
          null,
          null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSUri> getEntitiesForDescriptionsWithLike(
    final SSServPar       servPar,
    final List<String> descs) throws SSErr{
  
    ResultSet resultSet = null;
    
    try{
      
      if(
        descs       == null ||
        descs.isEmpty()){
        
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<MultivaluedMap<String, String>> likes        = new ArrayList<>();
      final List<String>                         columns      = new ArrayList<>();
      final List<SSSQLTableI>                    tables       = new ArrayList<>();
      final List<String>                         tableCons    = new ArrayList<>();
      final MultivaluedMap<String, String>       likeContents = new MultivaluedHashMap<>();
      
      column (columns, SSSQLVarNames.id);
      
      table(tables, SSEntitySQLTableE.entity);
      
      for(String desc : descs){
        where(likeContents, SSEntitySQLTableE.entity, SSSQLVarNames.description, desc);
      }
      
      likes.add(likeContents);
      
      resultSet =
        dbSQL.selectLike(
          servPar, 
          tables,
          columns,
          likes,
          tableCons,
          null,
          null,
          null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSUri> getEntitiesForLabelsWithMatch(
    final SSServPar       servPar,
    final List<SSUri>  entities,
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>                         columns   = new ArrayList<>();
      final List<SSSQLTableI>                    tables    = new ArrayList<>();
      final List<MultivaluedMap<String, String>> wheres    = new ArrayList<>();
      final List<String>                         matches   = new ArrayList<>();
      final List<String>                         tableCons = new ArrayList<>();
      
      column (columns, SSSQLVarNames.id);
      
      table(tables, SSEntitySQLTableE.entity);
      
      if(!entities.isEmpty()){
        
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, SSEntitySQLTableE.entity, SSSQLVarNames.id, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      match (matches, SSSQLVarNames.label);
      
      final SSDBSQLSelectPar selectPar =
        new SSDBSQLSelectPar(
          servPar, 
          tables,
          columns,
          wheres,
          null,
          null,
          tableCons);
       
      selectPar.matches.addAll   (matches);
      selectPar.requireds.addAll (requireds);
      selectPar.absents.addAll   (absents);
      selectPar.eithers.addAll   (eithers);
        
      resultSet = dbSQL.select(selectPar);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSUri> getEntitiesForDescriptionsWithMatch(
    final SSServPar    servPar,
    final List<SSUri>  entities,
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>                         columns   = new ArrayList<>();
      final List<SSSQLTableI>                    tables    = new ArrayList<>();
      final List<MultivaluedMap<String, String>> wheres    = new ArrayList<>();
      final List<String>                         matches   = new ArrayList<>();
      final List<String>                         tableCons = new ArrayList<>();
      
      column (columns, SSSQLVarNames.id);
      
      table(tables, SSEntitySQLTableE.entity);
      
      if(!entities.isEmpty()){
        
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, SSEntitySQLTableE.entity, SSSQLVarNames.id, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      match  (matches, SSSQLVarNames.description);
      
      final SSDBSQLSelectPar selectPar =
        new SSDBSQLSelectPar(
          servPar, 
          tables,
          columns,
          wheres,
          null,
          null,
          tableCons);
      
      selectPar.matches.addAll   (matches);
      selectPar.requireds.addAll (requireds);
      selectPar.absents.addAll   (absents);
      selectPar.eithers.addAll   (eithers);
      
      resultSet = dbSQL.select(selectPar);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
public List<SSUri> getCircleURIs(
    final SSServPar       servPar,
    final boolean withSystemCircles) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      
      where(wheres, SSSQLVarNames.isSystemCircle, withSystemCircles);
      
      resultSet = dbSQL.select(servPar, SSEntitySQLTableE.circle, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.circleId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public void addCircle(
    final SSServPar       servPar,
    final SSUri     circleUri,
    final SSCircleE circleType,
    final boolean   isSystemCircle) throws SSErr{
    
    try{
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.circleId,       circleUri);
      insert(inserts, SSSQLVarNames.circleType,     circleType);
      insert(inserts, SSSQLVarNames.isSystemCircle, isSystemCircle);
      
      dbSQL.insert(servPar, SSEntitySQLTableE.circle, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addUserToCircleIfNotExists(
    final SSServPar       servPar,
    final SSUri circleUri,
    final SSUri userUri) throws SSErr{
    
    try{
      
      if(hasCircleUser(servPar, circleUri, userUri)){
        return;
      }
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.circleId, circleUri);
      insert(inserts, SSSQLVarNames.userId,   userUri);
      
      dbSQL.insert(servPar, SSEntitySQLTableE.circleusers, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSEntity> getUsersForCircle(
    final SSServPar       servPar,
    final SSUri circleUri) throws SSErr{
    
    try{
      return SSEntity.get(getUserURIsForCircle(servPar, circleUri), SSEntityE.user);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSUri> getUserURIsForCircle(
    final SSServPar       servPar,
    final SSUri circleUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.circleId, circleUri);
      
      resultSet = dbSQL.select(servPar, SSEntitySQLTableE.circleusers, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.userId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSCircleE> getCircleTypesForEntity(
    final SSServPar       servPar,
    final SSUri entityUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSSQLTableI>         tables            = new ArrayList<>();
      final Map<String, String>       wheres            = new HashMap<>();
      final List<String>              columns           = new ArrayList<>();
      final List<String>              tableCons         = new ArrayList<>();
      
      table    (tables,    SSEntitySQLTableE.circleentities);
      table    (tables,    SSEntitySQLTableE.circle);
      column   (columns,   SSSQLVarNames.circleType);
      where    (wheres,    SSSQLVarNames.entityId, entityUri);
      tableCon (tableCons, SSEntitySQLTableE.circle, SSSQLVarNames.circleId, SSEntitySQLTableE.circleentities, SSSQLVarNames.circleId);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      return SSCircleE.get(getStringsFromResult(resultSet, SSSQLVarNames.circleType));
      
//        if(!SSCircleE.contains(circleTypes, circleType)){
//          circleTypes.add(circleType);
//        }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSUri> getCirclesCommonForUserAndEntity(
    final SSServPar       servPar,
    final SSUri userUri,
    final SSUri entityUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(userUri, entityUri)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<SSSQLTableI>         tables       = new ArrayList<>();
      final Map<String, String>       wheres       = new HashMap<>();
      final List<String>              columns      = new ArrayList<>();
      final List<String>              tableCons    = new ArrayList<>();
      
      table    (tables, SSEntitySQLTableE.circle);
      table    (tables, SSEntitySQLTableE.circleentities);
      table    (tables, SSEntitySQLTableE.circleusers);
      
      column   (columns,   SSEntitySQLTableE.circle, SSSQLVarNames.circleId);
      
      where    (wheres,    SSSQLVarNames.entityId, entityUri);
      where    (wheres,    SSSQLVarNames.userId,   userUri);
      
      tableCon (tableCons, SSEntitySQLTableE.circle, SSSQLVarNames.circleId, SSEntitySQLTableE.circleentities, SSSQLVarNames.circleId);
      tableCon (tableCons, SSEntitySQLTableE.circle, SSSQLVarNames.circleId, SSEntitySQLTableE.circleusers,    SSSQLVarNames.circleId);
      
      resultSet =
        dbSQL.select(
          servPar, 
          tables,
          columns,
          wheres,
          tableCons,
          null,
          null,
          null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.circleId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSCircleE> getCircleTypesCommonForUserAndEntity(
    final SSServPar       servPar,
    final SSUri userUri,
    final SSUri entityUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSSQLTableI>         tables       = new ArrayList<>();
      final Map<String, String>       wheres       = new HashMap<>();
      final List<String>              columns      = new ArrayList<>();
      final List<String>              tableCons    = new ArrayList<>();
      
      table    (tables, SSEntitySQLTableE.circleusers);
      table    (tables, SSEntitySQLTableE.circleentities);
      table    (tables, SSEntitySQLTableE.circle);
      
      column   (columns,   SSSQLVarNames.circleType);
      
      where    (wheres,    SSSQLVarNames.entityId, entityUri);
      where    (wheres,    SSSQLVarNames.userId,   userUri);
      
      tableCon (tableCons, SSEntitySQLTableE.circle, SSSQLVarNames.circleId, SSEntitySQLTableE.circleentities, SSSQLVarNames.circleId);
      tableCon (tableCons, SSEntitySQLTableE.circle, SSSQLVarNames.circleId, SSEntitySQLTableE.circleusers,    SSSQLVarNames.circleId);
      
      resultSet =
        dbSQL.select(
          servPar, 
          tables,
          columns,
          wheres,
          tableCons,
          null,
          null,
          null);
      
      return SSCircleE.get(getStringsFromResult(resultSet, SSSQLVarNames.circleType));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSUri> getCircleURIsCommonForUserAndEntity(
    final SSServPar       servPar,
    final SSUri   userUri,
    final SSUri   entityUri,
    final boolean withSystemCircles) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSSQLTableI>         tables     = new ArrayList<>();
      final Map<String, String>       wheres     = new HashMap<>();
      final List<String>              columns    = new ArrayList<>();
      final List<String>              tableCons  = new ArrayList<>();
      
      column    (columns, SSEntitySQLTableE.circle,        SSSQLVarNames.circleId);
      
      table     (tables,  SSEntitySQLTableE.circleusers);
      table     (tables,  SSEntitySQLTableE.circleentities);
      table     (tables,  SSEntitySQLTableE.circle);
      table     (tables,  SSEntitySQLTableE.entity);
      
      where     (wheres,  SSSQLVarNames.entityId, entityUri);
      where     (wheres,  SSSQLVarNames.userId,   userUri);
      
      if(!withSystemCircles){
        where     (wheres, SSEntitySQLTableE.circle, SSSQLVarNames.isSystemCircle, false);
      }
      
      tableCon  (tableCons, SSEntitySQLTableE.circle, SSSQLVarNames.circleId, SSEntitySQLTableE.circleentities, SSSQLVarNames.circleId);
      tableCon  (tableCons, SSEntitySQLTableE.circle, SSSQLVarNames.circleId, SSEntitySQLTableE.circleusers,    SSSQLVarNames.circleId);
      tableCon  (tableCons, SSEntitySQLTableE.circle, SSSQLVarNames.circleId, SSEntitySQLTableE.entity,         SSSQLVarNames.id);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.circleId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public boolean isSystemCircle(
    final SSServPar       servPar,
    final SSUri circleUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.isSystemCircle);
      
      where (wheres,    SSSQLVarNames.circleId, circleUri);
      
      resultSet = dbSQL.select(servPar, SSEntitySQLTableE.circle, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return false;
      }
      
      return bindingStrToBoolean(resultSet, SSSQLVarNames.isSystemCircle);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public SSCircle getCircle(
    final SSServPar            servPar,
    final SSUri                circleUri,
    final boolean              withUsers,
    final boolean              withEntities,
    final boolean              withCircleRights,
    final List<SSEntityE>      entityTypesToIncludeOnly) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSSQLTableI>   tables            = new ArrayList<>();
      final List<String>        columns           = new ArrayList<>();
      final List<String>        tableCons         = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      final SSCircle            circleObj;
      
      column   (columns,   SSSQLVarNames.circleId);
      column   (columns,   SSSQLVarNames.circleType);
      column   (columns,   SSSQLVarNames.isSystemCircle);
      
      table    (tables,    SSEntitySQLTableE.circle);
      table    (tables,    SSEntitySQLTableE.entity);
      
      where    (wheres,    SSSQLVarNames.circleId,     circleUri);
      
      tableCon (tableCons, SSEntitySQLTableE.circle,  SSSQLVarNames.circleId, SSEntitySQLTableE.entity, SSSQLVarNames.id);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      circleObj =
        SSCircle.get(
          circleUri,
          SSCircleE.get           (bindingStr(resultSet, SSSQLVarNames.circleType)),
          bindingStrToBoolean     (resultSet,SSSQLVarNames.isSystemCircle));
      
      if(withUsers){
        circleObj.users.addAll(getUsersForCircle(servPar, circleObj.id));
      }
      
      if(withEntities){
        
        circleObj.entities.addAll(
          getEntitiesForCircle(
            servPar, 
            circleUri,
            entityTypesToIncludeOnly));
      }
      
      if(withCircleRights){
        circleObj.accessRights.addAll(getCircleRights(servPar, circleObj.circleType));
      }
      
      return circleObj;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSUri> getCircleURIsForEntity(
    final SSServPar       servPar,
    final SSUri   entityUri,
    final boolean withSystemCircles) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String>   wheres      = new HashMap<>();
      final List<String>          columns     = new ArrayList<>();
      final List<SSUri>           circleUris  = new ArrayList<>();
      final List<SSUri>           tmpCircleUris;
      
      column (columns, SSSQLVarNames.circleId);
      where  (wheres,  SSSQLVarNames.entityId, entityUri);
      
      resultSet = dbSQL.select(servPar, SSEntitySQLTableE.circleentities, columns, wheres, null, null, null);
      
      tmpCircleUris = getURIsFromResult(resultSet, SSSQLVarNames.circleId);
      
      if(withSystemCircles){
        return tmpCircleUris;
      }else{
        
        for(SSUri circleUri : tmpCircleUris){
          
          if(!isSystemCircle(servPar, circleUri)){
            circleUris.add(circleUri);
          }
        }
        
        return circleUris;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public SSCircleE getTypeForCircle(
    final SSServPar       servPar,
    final SSUri circleUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      column(columns, SSSQLVarNames.circleType);
      
      where(wheres, SSSQLVarNames.circleId, circleUri);
      
      resultSet = dbSQL.select(servPar, SSEntitySQLTableE.circle, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return SSCircleE.get(bindingStr(resultSet, SSSQLVarNames.circleType));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public boolean isUserInCircle(
    final SSServPar       servPar,
    final SSUri          userUri,
    final SSUri          circleUri) throws SSErr{
    
    try{
      return SSStrU.contains(getCircleURIsForUser(servPar, userUri, true), circleUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  private boolean hasCircleUser(
    final SSServPar       servPar,
    final SSUri circleUri,
    final SSUri userUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.circleId, circleUri);
      where(wheres, SSSQLVarNames.userId,   userUri);
      
      resultSet = dbSQL.select(servPar, SSEntitySQLTableE.circleusers, columns, wheres, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public SSUri getPrivCircleURI(
    final SSServPar       servPar,
    final SSUri     user) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<SSSQLTableI>   tables            = new ArrayList<>();
      final List<String>        columns           = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      final List<String>        tableCons         = new ArrayList<>();
      
      column   (columns,   SSEntitySQLTableE.circle, SSSQLVarNames.circleId);
      
      table    (tables,    SSEntitySQLTableE.circle);
      table    (tables,    SSEntitySQLTableE.circleusers);
      
      where(wheres, SSSQLVarNames.circleType, SSCircleE.priv);
      where(wheres, SSSQLVarNames.userId,     user);
      
      tableCon (tableCons, SSEntitySQLTableE.circle, SSSQLVarNames.circleId, SSEntitySQLTableE.circleusers, SSSQLVarNames.circleId);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return bindingStrToUri(resultSet, SSSQLVarNames.circleId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public SSUri getPubCircleURI(
    final SSServPar       servPar) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      column(columns, SSSQLVarNames.circleType);
      
      where(wheres, SSSQLVarNames.circleType, SSCircleE.pub);
      
      resultSet = dbSQL.select(servPar, SSEntitySQLTableE.circle, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return bindingStrToUri(resultSet, SSSQLVarNames.circleId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public void removeUser(
    final SSServPar       servPar,
    final SSUri circle,
    final SSUri user) throws SSErr{
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.circleId, circle);
      where(wheres, SSSQLVarNames.userId, user);
      
      dbSQL.delete(servPar, SSEntitySQLTableE.circleusers, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeCircle(
    final SSServPar       servPar,
    final SSUri circle) throws SSErr{
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.circleId, circle);
      
      dbSQL.delete(servPar, SSEntitySQLTableE.circle, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getCircleURIsForUser(
    final SSServPar       servPar,
    final SSUri   userUri,
    final boolean withSystemCircles) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSUri>         circleUris  = new ArrayList<>();
      final List<String>        columns     = new ArrayList<>();
      final Map<String, String> wheres      = new HashMap<>();
      final List<SSUri>         tmpCircleUris;
      
      column(columns, SSSQLVarNames.circleId);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      
      resultSet = dbSQL.select(servPar, SSEntitySQLTableE.circleusers, columns, wheres, null, null, null);
      
      tmpCircleUris = getURIsFromResult(resultSet, SSSQLVarNames.circleId);
      
      if(withSystemCircles){
        return tmpCircleUris;
      }else{
        
        for(SSUri circleUri : tmpCircleUris){
          
          if(!isSystemCircle(servPar, circleUri)){
            circleUris.add(circleUri);
          }
        }
        
        return circleUris;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSEntity> getEntitiesForCircle(
    final SSServPar       servPar,
    final SSUri           circleUri,
    final List<SSEntityE> types) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      if(circleUri == null){
        throw new Exception("circle has to be given");
      }
      
      final List<MultivaluedMap<String, String>> wheres         = new ArrayList<>();
      final List<SSSQLTableI>                    tables         = new ArrayList<>();
      final List<String>                         columns        = new ArrayList<>();
      final List<String>                         tableCons      = new ArrayList<>();
      
      column(columns, SSEntitySQLTableE.circleentities, SSSQLVarNames.entityId);
      
      table    (tables, SSEntitySQLTableE.entity);
      table    (tables, SSEntitySQLTableE.circleentities);
      
      tableCon (tableCons, SSEntitySQLTableE.circleentities, SSSQLVarNames.entityId, SSEntitySQLTableE.entity, SSSQLVarNames.id);
      
      final MultivaluedMap<String, String> whereCircles = new MultivaluedHashMap<>();
      
      where(whereCircles, SSEntitySQLTableE.circleentities, SSSQLVarNames.circleId, circleUri);
      
      wheres.add(whereCircles);
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSEntityE type : types){
          where(whereTypes, SSEntitySQLTableE.entity, SSSQLVarNames.type, type);
        }
        
        wheres.add(whereTypes);
      }
      
      resultSet =
        dbSQL.select(
          new SSDBSQLSelectPar(
            servPar, 
            tables,
            columns,
            wheres,
            null,
            null,
            tableCons));
      
      return SSEntity.get(getURIsFromResult(resultSet, SSSQLVarNames.entityId),
        SSEntityE.entity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public void inviteUsers(
    final SSServPar       servPar,
    final SSUri        circle,
    final List<String> emails) throws SSErr {
    
//    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      for(String email : emails){
        
        inserts.clear();
        uniqueKeys.clear();
        
        insert    (inserts, SSSQLVarNames.circleId,  circle);
        insert    (inserts, SSSQLVarNames.inviteeId, email);
        
        uniqueKey(uniqueKeys, SSSQLVarNames.circleId,   circle);
        uniqueKey(uniqueKeys, SSSQLVarNames.inviteeId,  email);
        
        dbSQL.insertIfNotExists(servPar, SSEntitySQLTableE.circleinvitees, inserts, uniqueKeys);
      }
//    }catch(Exception sqlError){
//      SSServErrReg.regErrThrow(sqlError);
//    }
  }
  
  public List<String> getInvitedUsers(
    final SSServPar       servPar,
    final SSUri circle) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns     = new ArrayList<>();
      final Map<String, String> wheres      = new HashMap<>();
      
      column(columns, SSSQLVarNames.inviteeId);
      
      where(wheres, SSSQLVarNames.circleId, circle);
      
      resultSet = dbSQL.select(servPar, SSEntitySQLTableE.circleinvitees, columns, wheres, null, null, null);
      
      return getStringsFromResult(resultSet, SSSQLVarNames.inviteeId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public void changeCircleType(
    final SSServPar       servPar,
    final SSUri     circle,
    final SSCircleE type) throws SSErr{
    
    try{
      final Map<String, String>  wheres   = new HashMap<>();
      final Map<String, String>  updates  = new HashMap<>();
      
      where(wheres, SSSQLVarNames.circleId, circle);
      
      if(type != null){
        update (updates, SSSQLVarNames.circleType, type);
      }
      
      if(updates.isEmpty()){
        return;
      }
      
      dbSQL.update(servPar, SSEntitySQLTableE.circle, wheres, updates);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeEntityFromCircle(
    final SSServPar       servPar,
    final SSUri circle,
    final SSUri entity) throws SSErr{
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.circleId, circle);
      where(wheres, SSSQLVarNames.entityId, entity);
      
      dbSQL.delete(servPar, SSEntitySQLTableE.circleentities, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static List<SSCircleRightE> getCircleRights(
    final SSServPar       servPar,
    final SSCircleE circleType) throws SSErr{
    
    try{
      
      final List<SSCircleRightE> circleRights = new ArrayList<>();
      
      if(SSObjU.isNull(circleType)){
        throw new Exception("pars null");
      }
      
      switch(circleType){
        case priv: circleRights.add(SSCircleRightE.all);  break;
        case pub:  circleRights.add(SSCircleRightE.read); break;
        default:{
          circleRights.add(SSCircleRightE.read);
          circleRights.add(SSCircleRightE.edit);
          break;
        }
      }
      
      return circleRights;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

//private boolean hasCircleEntity(
//    final SSUri circleUri,
//    final SSUri entityUri) throws SSErr{
//
//    ResultSet resultSet = null;
//
//    try{
//      final List<String>          columns   = new ArrayList<>();
//      final Map<String, String>   wheres    = new HashMap<>();
//
//      column(columns, SSSQLVarNames.circleId);
//      column(columns, SSSQLVarNames.entityId);
//
//      where(wheres, SSSQLVarNames.circleId, circleUri);
//      where(wheres, SSSQLVarNames.entityId, entityUri);
//
//      resultSet = dbSQL.select(SSSQLVarNames.circleEntitiesTable, columns, wheres, null, null, null);
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

//  public List<SSEntity> getCircles(
//    final List<SSUri> circleURIs) throws SSErr{
//
//    ResultSet resultSet = null;
//
//    try{
//
//      final List<SSEntity> result = new ArrayList<>();
//
//      if(circleURIs == null){
//        throw SSErr.get(SSErrE.parameterMissing);
//      }
//
//      if(circleURIs.isEmpty()){
//        return result;
//      }
//
//      final List<String>                         columns    = new ArrayList<>();
//      final List<MultivaluedMap<String, String>> wheres     = new ArrayList<>();
//      final List<String>                         tables     = new ArrayList<>();
//      final List<String>                         tableCons  = new ArrayList<>();
//
//      column(columns, SSSQLVarNames.circleId);
//      column(columns, SSSQLVarNames.isSystemCircle);
//
//      final MultivaluedMap<String, String> whereCircles = new MultivaluedHashMap<>();
//
//      for(SSUri circleURI : circleURIs){
//        where(whereCircles, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, circleURI);
//      }
//
//      wheres.add(whereCircles);
//
//      table(tables, SSSQLVarNames.circleTable);
//
//      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
//
//      while(resultSet.next()){
//
//        result.add(
//          SSCircle.get(
//            bindingStrToUri(resultSet, SSSQLVarNames.circleId),
//            SSCircleE.get(bindingStr(resultSet, SSSQLVarNames.circleType)),
//            bindingStrToBoolean(resultSet, SSSQLVarNames.isSystemCircle)));
//      }
//
//      return result;
//
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }

//  public void entityAddOrUpdateLabel(SSUri entity, SSEntityA label) throws SSErr {
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

//  public boolean existsEntity(SSUri entityUri) throws SSErr{
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

//public List<SSUri> getEntityURIsUserCanAccess(
//    final SSUri           user,
//    final boolean         withSystemCircles,
//    final List<SSUri>     entities,
//    final List<SSEntityE> types,
//    final List<SSUri>     authors) throws SSErr{
//    
//    ResultSet resultSet = null;
//    
//    try{
//     
//      if(user == null){
//        throw SSErr.get(SSErrE.parameterMissing);
//      }
//      
//      final List<MultivaluedMap<String, String>> wheres         = new ArrayList<>();
//      final List<String>                         tables         = new ArrayList<>();
//      final List<String>                         columns        = new ArrayList<>();
//      final List<String>                         tableCons      = new ArrayList<>();
//      
//      column(columns, SSSQLVarNames.circleEntitiesTable,   SSSQLVarNames.entityId);
//      
//      table    (tables, SSSQLVarNames.circleUsersTable);
//      table    (tables, SSSQLVarNames.circleEntitiesTable);
//      table    (tables, SSEntitySQLTableE.entity);
//      
//      tableCon (tableCons, SSSQLVarNames.circleUsersTable,    SSSQLVarNames.circleId, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.circleId);
//      tableCon (tableCons, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.entityId, SSEntitySQLTableE.entity,         SSSQLVarNames.id);
//      
//      final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
//      
//      where(whereUsers, SSSQLVarNames.circleUsersTable, SSSQLVarNames.userId, user);
//      
//      wheres.add(whereUsers);
//      
//      if(
//        withSystemCircles != null &&
//        !withSystemCircles){
//        
//        table    (tables, SSSQLVarNames.circleTable);
//        tableCon (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleUsersTable,         SSSQLVarNames.circleId);
//        
//        final MultivaluedMap<String, String> whereIsSystemCircles = new MultivaluedHashMap<>();
//        
//        where(whereIsSystemCircles, SSSQLVarNames.circleTable, SSSQLVarNames.isSystemCircle, withSystemCircles);
//        
//        wheres.add(whereIsSystemCircles);
//      }
//      
//      if(
//        entities != null &&
//        !entities.isEmpty()){
//        
//        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
//        
//        for(SSUri entity : entities){
//          where(whereEntities, SSEntitySQLTableE.entity, SSSQLVarNames.id, entity);
//        }
//        
//        wheres.add(whereEntities);
//      }
//      
//      if(
//        authors != null &&
//        !authors.isEmpty()){
//        
//        final MultivaluedMap<String, String> whereAuthors = new MultivaluedHashMap<>();
//        
//        for(SSUri author : authors){
//          where(whereAuthors, SSEntitySQLTableE.entity, SSSQLVarNames.author, author);
//        }
//        
//        wheres.add(whereAuthors);
//      }
//      
//      if(
//        types != null &&
//        !types.isEmpty()){
//        
//        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
//        
//        for(SSEntityE type : types){
//          where(whereTypes, SSEntitySQLTableE.entity, SSSQLVarNames.type, type);
//        }
//        
//        wheres.add(whereTypes);
//      }
//      
//      resultSet =
//        dbSQL.select(
//          new SSDBSQLSelectPar(
//            tables,
//            columns,
//            wheres,
//            null,
//            null,
//            tableCons));
//      
//      return getURIsFromResult(resultSet, SSSQLVarNames.entityId);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }

//  //remove this entity service duplication
//  public void addEntity(
//    final SSUri         entity,
//    final SSEntityE     entityType,
//    final SSUri         authorUri) throws SSErr{
//    
//    final Map<String, String> inserts    = new HashMap<>();
//    
//    insert    (inserts,     SSSQLVarNames.id, entity);
//    
//    insert(inserts, SSSQLVarNames.creationTime, SSDateU.dateAsLong());
//    
//    insert(inserts, SSSQLVarNames.label, SSStrU.empty);
//    
//    if(entityType == null){
//      insert(inserts, SSSQLVarNames.type, SSEntityE.entity);
//    }else{
//      insert(inserts, SSSQLVarNames.type, entityType);
//    }
//    
//    if(authorUri == null){
//      insert(inserts, SSSQLVarNames.author, SSStrU.empty);
//    }else{
//      insert(inserts, SSSQLVarNames.author, authorUri);
//    }
//    
//    insert(inserts, SSSQLVarNames.description, SSStrU.empty);
//    
//    dbSQL.insert(SSEntitySQLTableE.entity, inserts);
//  }

//  public List<SSEntity> getEntitiesForCircle(
//    final SSUri circleUri) throws SSErr{
//
//    ResultSet resultSet = null;
//
//    try{
//      final List<String>          columns   = new ArrayList<>();
//      final Map<String, String>   wheres    = new HashMap<>();
//
//      column(columns, SSSQLVarU.entityId);
//      column(columns, SSSQLVarU.circleId);
//
//      where(wheres, SSSQLVarU.circleId, circleUri);
//
//      resultSet = dbSQL.select(circleEntitiesTable, columns, wheres, null, null, null);
//
//      return SSEntity.get(
//        getURIsFromResult(resultSet, SSSQLVarU.entityId),
//        SSEntityE.entity);
//
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }

//public List<SSEntity> getEntitiesForLabelsAndDescriptions(
//    final List<String> requireds,
//    final List<String> absents,
//    final List<String> eithers) throws SSErr{
//    
//    ResultSet resultSet = null;
//    
//    try{
//      final List<SSEntity>            entities  = new ArrayList<>();
//      final List<String>              columns   = new ArrayList<>();
//      final List<String>              matches   = new ArrayList<>();
//      SSEntity                        entityObj;
//      
//      column (columns, SSSQLVarNames.id);
//      column (columns, SSSQLVarNames.label);
//      column (columns, SSSQLVarNames.description);
//      column (columns, SSSQLVarNames.type);
//      match  (matches, SSSQLVarNames.label);
//      match  (matches, SSSQLVarNames.description);
//      
//      resultSet = dbSQL.select(SSEntitySQLTableE.entity, columns, matches, requireds, absents, eithers);
//      
//      while(resultSet.next()){
//      
//        entityObj =
//          SSEntity.get(
//            bindingStrToUri        (resultSet, SSSQLVarNames.id),
//            bindingStrToEntityType (resultSet, SSSQLVarNames.type),
//            bindingStrToLabel      (resultSet, SSSQLVarNames.label));
//        
//        entityObj.description = bindingStrToTextComment(resultSet, SSSQLVarNames.description);
//        
//        entities.add(entityObj);
//      }
//      
//      return entities;
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }

//public boolean isGroupOrPubCircleCircle(
//    final SSServPar       servPar,
//    final SSUri circleUri) throws SSErr{
//    
//    try{
//      
//      final SSCircleE circleType = getTypeForCircle(servPar, circleUri);
//      
//      switch(circleType){
//        
//        case pubCircle:
//        case group:{
//          return true;
//        }
//        
//        default: return false;
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return false;
//    }
//  }