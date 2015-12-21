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
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.SSEntityCircle;
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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class SSCoreSQL extends SSDBSQLFctA{

  private final SSUri systemUserURI;
  
  public SSCoreSQL(
    final SSDBSQLI  dbSQL,
    final SSUri     systemUserURI){
    
    super(dbSQL);
    
    this.systemUserURI = systemUserURI;
  }
  
  public List<SSEntity> getEntities(
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
          SSSQLVarNames.entityTable, 
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
      
      if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
        SSServErrReg.reset();
        return null;
      }
        
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
    final List<SSUri>     entities, 
    final List<SSEntityE> types, 
    final List<SSUri>     authors,
    final Long            startTime, 
    final Long            endTime) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      if(
        (entities == null || entities.isEmpty()) &&
        (types    == null || types.isEmpty())    &&
        (authors  == null || authors.isEmpty())){
        
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<MultivaluedMap<String, String>>                   wheres         = new ArrayList<>();
      final MultivaluedMap<String, MultivaluedMap<String, String>> wheresNumeric  = new MultivaluedHashMap<>();
      final List<String>                                           tables         = new ArrayList<>();
      final List<String>                                           columns        = new ArrayList<>();
      final List<String>                                           tableCons      = new ArrayList<>();
      
      column(columns, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      table (tables, SSSQLVarNames.entityTable);
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSEntityE type : types){
          where(whereTypes, SSSQLVarNames.entityTable, SSSQLVarNames.type, type);
        }
        
        wheres.add(whereTypes);
      }
      
      if(
        entities != null &&
        !entities.isEmpty()){
        
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, SSSQLVarNames.entityTable, SSSQLVarNames.id, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      if(
        authors != null &&
        !authors.isEmpty()){
        
        final MultivaluedMap<String, String> whereAuthors = new MultivaluedHashMap<>();
        
        for(SSUri author : authors){
          where(whereAuthors, SSSQLVarNames.entityTable, SSSQLVarNames.author, author);
        }
        
        wheres.add(whereAuthors);
      }
      
      if(
        startTime != null &&
        startTime != 0){
        
        final List<MultivaluedMap<String, String>> greaterWheres           = new ArrayList<>();
        final MultivaluedMap<String, String>       whereNumbericStartTimes = new MultivaluedHashMap<>();
        
        wheresNumeric.put(SSStrU.greaterThan, greaterWheres);
        
        where(whereNumbericStartTimes, SSSQLVarNames.entityTable, SSSQLVarNames.creationTime, startTime);
        
        greaterWheres.add(whereNumbericStartTimes);
      }
      
      if(
        endTime != null &&
        endTime != 0){
        
        final List<MultivaluedMap<String, String>> lessWheres            = new ArrayList<>();
        final MultivaluedMap<String, String>       whereNumbericEndTimes = new MultivaluedHashMap<>();
        
        wheresNumeric.put(SSStrU.lessThan, lessWheres);
        
        where(whereNumbericEndTimes, SSSQLVarNames.entityTable, SSSQLVarNames.creationTime, endTime);
        
        lessWheres.add(whereNumbericEndTimes);
      }
      
      resultSet =
        dbSQL.select(
          new SSDBSQLSelectPar(
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
      
      resultSet =
        dbSQL.select(
          new SSDBSQLSelectPar(
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
  
  public void addEntityToCircleIfNotExists(
    final SSUri circleUri,
    final SSUri entityUri) throws SSErr{
    
    try{
      
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.circleId, circleUri);
      insert(inserts, SSSQLVarNames.entityId, entityUri);

      uniqueKey(uniqueKeys, SSSQLVarNames.circleId, circleUri);
      uniqueKey(uniqueKeys, SSSQLVarNames.entityId, entityUri);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.circleEntitiesTable, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addEntityIfNotExists(
    final SSUri         entity, 
    final SSEntityE     entityType,
    final SSEntityA     label,
    final SSTextComment description,
    final SSUri         author,
    final Long          creationTime) throws SSErr{
    
    try{
      
      if(!existsEntity(entity)){
        
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
        
        dbSQL.insert(SSSQLVarNames.entityTable, inserts);
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
        
        dbSQL.update(SSSQLVarNames.entityTable, wheres, updates);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void deleteEntityIfExists(
    final SSUri entityUri) throws SSErr{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.id, entityUri);
      
      dbSQL.deleteIgnore(SSSQLVarNames.entityTable, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void removeAllEntities() throws SSErr {
    
    try{
      dbSQL.delete(SSSQLVarNames.entityTable);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeAttachedEntities(
    final SSUri       entity,
    final List<SSUri> attachments) throws SSErr{
    
    try{
      
      final List<MultivaluedMap<String, String>> wheres                = new ArrayList<>();
      final MultivaluedMap<String, String>       whereEntity           = new MultivaluedHashMap<>();
      final MultivaluedMap<String, String>       whereAttachedEntities = new MultivaluedHashMap<>();
      
      where(whereEntity, SSSQLVarNames.entityAttachedEntitiesTable, SSSQLVarNames.entityId, entity);
        
      wheres.add(whereEntity);
      
      for(SSUri attachment : attachments){
        where(whereAttachedEntities, SSSQLVarNames.entityAttachedEntitiesTable, SSSQLVarNames.attachedEntityId, attachment);
      }

      wheres.add(whereAttachedEntities);
      
      dbSQL.deleteIgnore(SSSQLVarNames.entityAttachedEntitiesTable, wheres);
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void attachEntities(
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

        dbSQL.insertIfNotExists(SSSQLVarNames.entityAttachedEntitiesTable, inserts, uniqueKeys);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getAttachedEntities(
    final SSUri entity) throws SSErr{
    
    ResultSet resultSet = null;
    
    if(entity == null){
      throw SSErr.get(SSErrE.parameterMissing);
    }
    
    try{
      final List<String>        columns          = new ArrayList<>();
      final List<String>        tables           = new ArrayList<>();
      final Map<String, String> wheres           = new HashMap<>();
      final List<String>        tableCons        = new ArrayList<>();
      
      column(columns, SSSQLVarNames.id);

      table(tables, SSSQLVarNames.entityTable);
      table(tables, SSSQLVarNames.entityAttachedEntitiesTable);
      
      where(wheres, SSSQLVarNames.entityAttachedEntitiesTable, SSSQLVarNames.entityId, entity);
      
      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.entityAttachedEntitiesTable, SSSQLVarNames.attachedEntityId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
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
  
  public Boolean getEntityRead(
    final SSUri user, 
    final SSUri entity) throws SSErr{
    
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
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }

  public void setEntityRead(
    final SSUri   user, 
    final SSUri   entity,
    final Boolean read) throws SSErr{
    
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
        final Map<String, String> wheres = new HashMap<>();
        
        where(wheres, SSSQLVarNames.userId,   user);
        where(wheres, SSSQLVarNames.entityId, entity);
        
        dbSQL.deleteIgnore(SSSQLVarNames.entityReadsTable, wheres);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void addDownloads(
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
        
        dbSQL.insertIfNotExists(SSSQLVarNames.entityDownloadsTable, inserts, uniqueKeys);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getDownloads(
    final SSUri entity) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns           = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      
      column(columns, SSSQLVarNames.downloadId);
      
      where(wheres, SSSQLVarNames.entityId, entity);
      
      resultSet = dbSQL.select(SSSQLVarNames.entityDownloadsTable, columns, wheres, null, null, null);
      
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
  
  public SSEntity getEntityTest(
    final SSUri   user,
    final SSUri   entity,
    final Boolean withUserRestriction) throws SSErr{
    
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
        !SSStrU.equals(userStr, systemUserURI)){
        
        query = "select DISTINCT id, type, label, description, author, creationTime from entity where ";
        query += "(id = '" + entityStr + "' AND type  = 'entity') OR ";
        query += "(id = '" + entityStr + "' AND type  = 'circle' AND id = (select DISTINCT circleId from circle where circleId = '" + entityStr + "' and (circleType = 'pub' or circleType = 'pubCircle'))) OR ";
        query += "(id = '" + entityStr + "' AND type  = 'circle' AND id = (select DISTINCT circle.circleId from circle, circleusers where circle.circleId = '" + entityStr + "' and circleusers.userId = '" + userStr + "' and circle.circleId = circleusers.circleId)) OR ";
        query += "(id = '" + entityStr + "' AND type != 'entity' AND type != 'circle' AND id = (select DISTINCT circleentities.entityId from circle, circleentities, circleusers where entityId = '" + entityStr + "' and userId = '" + userStr + "' and circle.circleId = circleentities.circleId and circle.circleId = circleusers.circleId))";
  
      }else{
        query = "select DISTINCT id, type, label, description, author, creationTime from entity where entity.id ='" + entityStr + "'";
      }
      
      resultSet = dbSQL.select(query);
      
      checkFirstResult(resultSet);
      
      return SSEntity.get(
        bindingStrToUri        (resultSet, SSSQLVarNames.id),
        bindingStrToEntityType (resultSet, SSSQLVarNames.type),
        bindingStrToLabel      (resultSet, SSSQLVarNames.label),
        bindingStrToTextComment(resultSet, SSSQLVarNames.description),
        bindingStrToLong       (resultSet, SSSQLVarNames.creationTime),
        bindingStrToAuthor     (resultSet, SSSQLVarNames.author)); 
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
        SSServErrReg.reset();
        return null;
      }
        
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
  
  public Boolean isUserAuthor(
    final SSUri   user, 
    final SSUri   entityURI,
    final Boolean withUserRestriction) throws SSErr{
    
    try{
      
      if(SSObjU.isNull(user, entityURI)){
        return false;
      }
      
      final SSEntity entity =
        getEntityTest(
          user, 
          entityURI, 
          withUserRestriction);
        
      if(
        entity == null ||
        !SSStrU.equals(user, entity.author)){
        return false;
      }
      
      return true;
    }catch(Exception error){
      SSServErrReg.reset();
      return false;
    }
  }
  
  public List<SSUri> getAccessibleURIs(
    final SSUri           user,
    final List<SSEntityE> types,
    final List<SSUri>     authors,
    final Long            startTime,
    final Long            endTime) throws SSErr{
    
    ResultSet resultSet  = null;
    
    try{
      
      if(
        user == null ||
        SSStrU.equals(user, systemUserURI)){
        
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final String userStr                 = SSStrU.toStr(user);
      String       query                   = "select DISTINCT id from entity where ";
      String       authorsQueryPart        = SSStrU.empty;
      String       typesQueryPart          = SSStrU.empty;
      String       creationTimeQueryPart   = SSStrU.empty;
      
      if(
        authors != null &&
        !authors.isEmpty()){
        
        authorsQueryPart += SSStrU.bracketOpen;
        
        for(SSUri author : authors){
          authorsQueryPart += "author = '" + author.toString() + "' OR ";
        }
        
        authorsQueryPart  = SSStrU.removeTrailingString(authorsQueryPart, " OR ") + SSStrU.bracketClose;
      }
      
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
        !authorsQueryPart.isEmpty()  ||
        !typesQueryPart.isEmpty()    ||
        !creationTimeQueryPart.isEmpty()){
        
        if(!authorsQueryPart.isEmpty()){
          query += authorsQueryPart + " AND ";
        }
        
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
        !authorsQueryPart.isEmpty()   ||
        !typesQueryPart.isEmpty()     ||
        !creationTimeQueryPart.isEmpty()){
        
        query += SSStrU.bracketClose;
      }
      
      resultSet = dbSQL.select(query);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
        SSServErrReg.reset();
        return null;
      }
        
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
  
  public void setEntityColumns(
    final List<String> columns) throws SSErr{
    
    try{
      column(columns, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      column(columns, SSSQLVarNames.entityTable, SSSQLVarNames.type);
      column(columns, SSSQLVarNames.entityTable, SSSQLVarNames.label);
      column(columns, SSSQLVarNames.entityTable, SSSQLVarNames.creationTime);
      column(columns, SSSQLVarNames.entityTable, SSSQLVarNames.author);
      column(columns, SSSQLVarNames.entityTable, SSSQLVarNames.description);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void setEntityTable(
    final List<String> tables) throws SSErr{
    
    try{
      table(tables, SSSQLVarNames.entityTable);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Boolean existsEntity(
    final SSUri entity) throws SSErr{
    
    ResultSet resultSet  = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> where   = new HashMap<>();
      
      column(columns, SSSQLVarNames.id);
      
      where(where, SSSQLVarNames.id, entity);
      
      resultSet = 
        dbSQL.select(
          SSSQLVarNames.entityTable, 
          columns, 
          where, 
          null, 
          null, 
          null);
      
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
      
      try{
        dbSQL.closeStmt(resultSet);
      }catch(Exception sqlError){
        SSServErrReg.regErrThrow(sqlError);
      }
    }
  }
  
  public List<SSEntity> getEntitiesForLabelsAndDescriptionsWithSQLLike(
    final List<String> labelStrings,
    final List<String> descStrings,
    final SSSearchOpE  searchOp) throws SSErr{
  
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
      final List<String>                         tables       = new ArrayList<>();
      final List<String>                         tableCons    = new ArrayList<>();
      final MultivaluedMap<String, String>       likeContents = new MultivaluedHashMap<>();
      
      column (columns, SSSQLVarNames.id);
      
      table(tables, SSSQLVarNames.entityTable);
      
      for(String label : labels){
        where(likeContents, SSSQLVarNames.entityTable, SSSQLVarNames.label, label);
      }
      
      likes.add(likeContents);
      
      resultSet =
        dbSQL.selectLike(
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
      final List<String>                         tables       = new ArrayList<>();
      final List<String>                         tableCons    = new ArrayList<>();
      final MultivaluedMap<String, String>       likeContents = new MultivaluedHashMap<>();
      
      column (columns, SSSQLVarNames.id);
      
      table(tables, SSSQLVarNames.entityTable);
      
      for(String desc : descs){
        where(likeContents, SSSQLVarNames.entityTable, SSSQLVarNames.description, desc);
      }
      
      likes.add(likeContents);
      
      resultSet =
        dbSQL.selectLike(
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
    final List<SSUri>  entities,
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>                         columns   = new ArrayList<>();
      final List<String>                         tables    = new ArrayList<>();
      final List<MultivaluedMap<String, String>> wheres    = new ArrayList<>();
      final List<String>                         matches   = new ArrayList<>();
      final List<String>                         tableCons = new ArrayList<>();
      
      column (columns, SSSQLVarNames.id);
      
      table(tables, SSSQLVarNames.entityTable);
      
      if(!entities.isEmpty()){
        
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, SSSQLVarNames.entityTable, SSSQLVarNames.id, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      match (matches, SSSQLVarNames.label);
      
      final SSDBSQLSelectPar selectPar =
        new SSDBSQLSelectPar(
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
    final List<SSUri>  entities,
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>                         columns   = new ArrayList<>();
      final List<String>                         tables    = new ArrayList<>();
      final List<MultivaluedMap<String, String>> wheres    = new ArrayList<>();
      final List<String>                         matches   = new ArrayList<>();
      final List<String>                         tableCons = new ArrayList<>();
      
      column (columns, SSSQLVarNames.id);
      
      table(tables, SSSQLVarNames.entityTable);
      
      if(!entities.isEmpty()){
        
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, SSSQLVarNames.entityTable, SSSQLVarNames.id, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      match  (matches, SSSQLVarNames.description);
      
      final SSDBSQLSelectPar selectPar =
        new SSDBSQLSelectPar(
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
    final Boolean withSystemCircles) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      
      where(wheres, SSSQLVarNames.isSystemCircle, withSystemCircles);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleTable, columns, wheres, null, null, null);
      
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
    final SSUri     circleUri,
    final SSCircleE circleType,
    final Boolean   isSystemCircle) throws SSErr{
    
    try{
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.circleId,       circleUri);
      insert(inserts, SSSQLVarNames.circleType,     circleType);
      insert(inserts, SSSQLVarNames.isSystemCircle, isSystemCircle);
      
      dbSQL.insert(SSSQLVarNames.circleTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addUserToCircleIfNotExists(
    final SSUri circleUri,
    final SSUri userUri) throws SSErr{
    
    try{
      
      if(hasCircleUser(circleUri, userUri)){
        return;
      }
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.circleId, circleUri);
      insert(inserts, SSSQLVarNames.userId,   userUri);
      
      dbSQL.insert(SSSQLVarNames.circleUsersTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSEntity> getUsersForCircle(
    final SSUri circleUri) throws SSErr{
    
    try{
      return SSEntity.get(getUserURIsForCircle(circleUri), SSEntityE.user);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSUri> getUserURIsForCircle(
    final SSUri circleUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.circleId, circleUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleUsersTable, columns, wheres, null, null, null);
      
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
    final SSUri entityUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>              tables            = new ArrayList<>();
      final Map<String, String>       wheres            = new HashMap<>();
      final List<String>              columns           = new ArrayList<>();
      final List<String>              tableCons         = new ArrayList<>();
      
      table    (tables, SSSQLVarNames.circleEntitiesTable);
      table    (tables, SSSQLVarNames.circleTable);
      column   (columns,   SSSQLVarNames.circleType);
      where    (wheres,    SSSQLVarNames.entityId, entityUri);
      tableCon (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.circleId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
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
    final SSUri userUri,
    final SSUri entityUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(userUri, entityUri)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<String>              tables       = new ArrayList<>();
      final Map<String, String>       wheres       = new HashMap<>();
      final List<String>              columns      = new ArrayList<>();
      final List<String>              tableCons    = new ArrayList<>();
      
      table    (tables, SSSQLVarNames.circleTable);
      table    (tables, SSSQLVarNames.circleEntitiesTable);
      table    (tables, SSSQLVarNames.circleUsersTable);
      
      column   (columns,   SSSQLVarNames.circleTable, SSSQLVarNames.circleId);
      
      where    (wheres,    SSSQLVarNames.entityId, entityUri);
      where    (wheres,    SSSQLVarNames.userId,   userUri);
      
      tableCon (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.circleId);
      tableCon (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleUsersTable,    SSSQLVarNames.circleId);
      
      resultSet =
        dbSQL.select(
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
    final SSUri userUri,
    final SSUri entityUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>              tables       = new ArrayList<>();
      final Map<String, String>       wheres       = new HashMap<>();
      final List<String>              columns      = new ArrayList<>();
      final List<String>              tableCons    = new ArrayList<>();
      
      table    (tables, SSSQLVarNames.circleUsersTable);
      table    (tables, SSSQLVarNames.circleEntitiesTable);
      table    (tables, SSSQLVarNames.circleTable);
      
      column   (columns,   SSSQLVarNames.circleType);
      
      where    (wheres,    SSSQLVarNames.entityId, entityUri);
      where    (wheres,    SSSQLVarNames.userId,   userUri);
      
      tableCon (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.circleId);
      tableCon (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleUsersTable,    SSSQLVarNames.circleId);
      
      resultSet =
        dbSQL.select(
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
    final SSUri   userUri,
    final SSUri   entityUri,
    final Boolean withSystemCircles) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>              tables     = new ArrayList<>();
      final Map<String, String>       wheres     = new HashMap<>();
      final List<String>              columns    = new ArrayList<>();
      final List<String>              tableCons  = new ArrayList<>();
      
      column    (columns, SSSQLVarNames.circleTable,        SSSQLVarNames.circleId);
      
      table     (tables,  SSSQLVarNames.circleUsersTable);
      table     (tables,  SSSQLVarNames.circleEntitiesTable);
      table     (tables,  SSSQLVarNames.circleTable);
      table     (tables,  SSSQLVarNames.entityTable);
      
      where     (wheres,  SSSQLVarNames.entityId, entityUri);
      where     (wheres,  SSSQLVarNames.userId,   userUri);
      
      if(!withSystemCircles){
        where     (wheres, SSSQLVarNames.circleTable, SSSQLVarNames.isSystemCircle, false);
      }
      
      tableCon  (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.circleId);
      tableCon  (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleUsersTable,    SSSQLVarNames.circleId);
      tableCon  (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.entityTable,         SSSQLVarNames.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
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
  
  public Boolean isSystemCircle(
    final SSUri circleUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.isSystemCircle);
      
      where (wheres,    SSSQLVarNames.circleId, circleUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStrToBoolean(resultSet, SSSQLVarNames.isSystemCircle);
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
  
  public SSEntityCircle getCircle(
    final SSUri                circleUri,
    final Boolean              withUsers,
    final Boolean              withEntities,
    final Boolean              withCircleRights,
    final List<SSEntityE>      entityTypesToIncludeOnly) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        tables            = new ArrayList<>();
      final List<String>        columns           = new ArrayList<>();
      final List<String>        tableCons         = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      final SSEntityCircle      circleObj;
      
      column   (columns,   SSSQLVarNames.circleId);
      column   (columns,   SSSQLVarNames.circleType);
      column   (columns,   SSSQLVarNames.isSystemCircle);
      
      table    (tables,    SSSQLVarNames.circleTable);
      table    (tables,    SSSQLVarNames.entityTable);
      
      where    (wheres,    SSSQLVarNames.circleId,     circleUri);
      
      tableCon (tableCons, SSSQLVarNames.circleTable,  SSSQLVarNames.circleId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      checkFirstResult(resultSet);
      
      circleObj =
        SSEntityCircle.get(
          circleUri,
          SSCircleE.get           (bindingStr(resultSet, SSSQLVarNames.circleType)),
          bindingStrToBoolean     (resultSet,SSSQLVarNames.isSystemCircle));
      
      if(withUsers){
        circleObj.users.addAll(getUsersForCircle(circleObj.id));
      }
      
      if(withEntities){
        
        circleObj.entities.addAll(
          getEntitiesForCircle(
            circleUri,
            entityTypesToIncludeOnly));
      }
      
      if(withCircleRights){
        circleObj.accessRights.addAll(getCircleRights(circleObj.circleType));
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
    final SSUri   entityUri,
    final Boolean withSystemCircles) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String>   wheres      = new HashMap<>();
      final List<String>          columns     = new ArrayList<>();
      final List<SSUri>           circleUris  = new ArrayList<>();
      final List<SSUri>           tmpCircleUris;
      
      column (columns, SSSQLVarNames.circleId);
      where  (wheres,  SSSQLVarNames.entityId, entityUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleEntitiesTable, columns, wheres, null, null, null);
      
      tmpCircleUris = getURIsFromResult(resultSet, SSSQLVarNames.circleId);
      
      if(withSystemCircles){
        return tmpCircleUris;
      }else{
        
        for(SSUri circleUri : tmpCircleUris){
          
          if(!isSystemCircle(circleUri)){
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
    final SSUri circleUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      column(columns, SSSQLVarNames.circleType);
      
      where(wheres, SSSQLVarNames.circleId, circleUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
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
  
  public Boolean isUserInCircle(
    final SSUri          userUri,
    final SSUri          circleUri) throws SSErr{
    
    try{
      return SSStrU.contains(getCircleURIsForUser(userUri, true), circleUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public Boolean isGroupOrPubCircleCircle(
    final SSUri circleUri) throws SSErr{
    
    try{
      
      final SSCircleE circleType = getTypeForCircle(circleUri);
      
      switch(circleType){
        
        case pubCircle:
        case group:{
          return true;
        }
        
        default: return false;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private Boolean hasCircleUser(
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
      
      resultSet = dbSQL.select(SSSQLVarNames.circleUsersTable, columns, wheres, null, null, null);
      
      return resultSet.first();
      
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
  
  public SSUri getPrivCircleURI(
    final SSUri     user) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        tables            = new ArrayList<>();
      final List<String>        columns           = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      final List<String>        tableCons         = new ArrayList<>();
      
      column   (columns,   SSSQLVarNames.circleTable, SSSQLVarNames.circleId);
      
      table    (tables,    SSSQLVarNames.circleTable);
      table    (tables,    SSSQLVarNames.circleUsersTable);
      
      where(wheres, SSSQLVarNames.circleType, SSCircleE.priv);
      where(wheres, SSSQLVarNames.userId,     user);
      
      tableCon (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleUsersTable, SSSQLVarNames.circleId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarNames.circleId);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
        SSServErrReg.reset();
        return null;
      }
      
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
  
  public SSUri getPubCircleURI() throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      column(columns, SSSQLVarNames.circleType);
      
      where(wheres, SSSQLVarNames.circleType, SSCircleE.pub);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarNames.circleId);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
        SSServErrReg.reset();
        return null;
      }
      
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
    final SSUri circle,
    final SSUri user) throws SSErr{
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.circleId, circle);
      where(wheres, SSSQLVarNames.userId, user);
      
      dbSQL.delete(SSSQLVarNames.circleUsersTable, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeCircle(
    final SSUri circle) throws SSErr{
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.circleId, circle);
      
      dbSQL.delete(SSSQLVarNames.circleTable, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getCircleURIsForUser(
    final SSUri   userUri,
    final Boolean withSystemCircles) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSUri>         circleUris  = new ArrayList<>();
      final List<String>        columns     = new ArrayList<>();
      final Map<String, String> wheres      = new HashMap<>();
      final List<SSUri>         tmpCircleUris;
      
      column(columns, SSSQLVarNames.circleId);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleUsersTable, columns, wheres, null, null, null);
      
      tmpCircleUris = getURIsFromResult(resultSet, SSSQLVarNames.circleId);
      
      if(withSystemCircles){
        return tmpCircleUris;
      }else{
        
        for(SSUri circleUri : tmpCircleUris){
          
          if(!isSystemCircle(circleUri)){
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
    final SSUri           circleUri,
    final List<SSEntityE> types) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      if(circleUri == null){
        throw new Exception("circle has to be given");
      }
      
      final List<MultivaluedMap<String, String>> wheres         = new ArrayList<>();
      final List<String>                         tables         = new ArrayList<>();
      final List<String>                         columns        = new ArrayList<>();
      final List<String>                         tableCons      = new ArrayList<>();
      
      column(columns, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.entityId);
      
      table    (tables, SSSQLVarNames.entityTable);
      table    (tables, SSSQLVarNames.circleEntitiesTable);
      
      tableCon (tableCons, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.entityId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      final MultivaluedMap<String, String> whereCircles = new MultivaluedHashMap<>();
      
      where(whereCircles, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.circleId, circleUri);
      
      wheres.add(whereCircles);
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSEntityE type : types){
          where(whereTypes, SSSQLVarNames.entityTable, SSSQLVarNames.type, type);
        }
        
        wheres.add(whereTypes);
      }
      
      resultSet =
        dbSQL.select(
          new SSDBSQLSelectPar(
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
        
        dbSQL.insertIfNotExists(SSSQLVarNames.circleInviteesTable, inserts, uniqueKeys);
      }
//    }catch(Exception sqlError){
//      SSServErrReg.regErrThrow(sqlError);
//    }
  }
  
  public List<String> getInvitedUsers(
    final SSUri circle) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns     = new ArrayList<>();
      final Map<String, String> wheres      = new HashMap<>();
      
      column(columns, SSSQLVarNames.inviteeId);
      
      where(wheres, SSSQLVarNames.circleId, circle);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleInviteesTable, columns, wheres, null, null, null);
      
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
      
      dbSQL.update(SSSQLVarNames.circleTable, wheres, updates);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeEntityFromCircle(
    final SSUri circle,
    final SSUri entity) throws SSErr{
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.circleId, circle);
      where(wheres, SSSQLVarNames.entityId, entity);
      
      dbSQL.delete(SSSQLVarNames.circleEntitiesTable, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static List<SSCircleRightE> getCircleRights(
    final SSCircleE circleType) throws SSErr{
    
    try{
      
      final List<SSCircleRightE> circleRights = new ArrayList<>();
      
      if(SSObjU.isNull(circleType)){
        throw new Exception("pars null");
      }
      
      switch(circleType){
        case priv: circleRights.add(SSCircleRightE.all);  break;
        case pub:  circleRights.add(SSCircleRightE.read); break;
        default:   circleRights.add(SSCircleRightE.read); circleRights.add(SSCircleRightE.edit);
      }
      
      return circleRights;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

//private Boolean hasCircleEntity(
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
//          SSEntityCircle.get(
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

//  public Boolean existsEntity(SSUri entityUri) throws SSErr{
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
//    final Boolean         withSystemCircles,
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
//      table    (tables, SSSQLVarNames.entityTable);
//      
//      tableCon (tableCons, SSSQLVarNames.circleUsersTable,    SSSQLVarNames.circleId, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.circleId);
//      tableCon (tableCons, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.entityId, SSSQLVarNames.entityTable,         SSSQLVarNames.id);
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
//          where(whereEntities, SSSQLVarNames.entityTable, SSSQLVarNames.id, entity);
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
//          where(whereAuthors, SSSQLVarNames.entityTable, SSSQLVarNames.author, author);
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
//          where(whereTypes, SSSQLVarNames.entityTable, SSSQLVarNames.type, type);
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
//    dbSQL.insert(SSSQLVarNames.entityTable, inserts);
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
//      resultSet = dbSQL.select(SSSQLVarNames.entityTable, columns, matches, requireds, absents, eithers);
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