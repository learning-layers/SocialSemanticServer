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
package at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.SSCircleE;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.datatypes.datatypes.SSImageE;
import at.kc.tugraz.ss.datatypes.datatypes.SSLocation;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.op.SSEntityMiscFct;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sss.serv.err.datatypes.SSErr;
import sss.serv.err.datatypes.SSErrE;

public class SSEntitySQLFct extends SSDBSQLFct{

  public SSEntitySQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }
  
  public Boolean existsEntity(
    final SSUri entity) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      final Map<String, String> where = new HashMap<>();
      
      where(where, SSSQLVarU.id, entity);
      
      resultSet = dbSQL.select(entityTable, where);
      
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
      
      final Map<String, String> where = new HashMap<>();
      final SSEntity            entityObj;
      
      where(where, SSSQLVarU.id, entityUri);
      
      resultSet = dbSQL.select(entityTable, where);
      
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
      
      final Map<String, String> where = new HashMap<>();
      final SSEntity            entityObj;
      
      where(where, SSSQLVarU.label, label);
      where(where, SSSQLVarU.type,  type);
      
      resultSet = dbSQL.select(entityTable, where);
      
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
  
  public List<SSUri> getEntities(
    final SSUri           user,
    final List<SSEntityE> types) throws Exception{
    
    final List<SSUri> entities = new ArrayList<>();
    
    for(SSEntityE type : types){
      entities.addAll(getEntities(user, type));
    }
    
    SSStrU.distinctWithoutNull2(entities);
    
    return entities;
  }
  
  public List<SSUri> getEntities(
    final SSUri     author,
    final SSEntityE type) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> where = new HashMap<>();
      
      where(where, SSSQLVarU.author, author);
      where(where, SSSQLVarU.type,   type);
      
      resultSet = dbSQL.select(entityTable, where);
      
      return getURIsFromResult(resultSet, SSSQLVarU.id);
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
        insert(inserts, SSSQLVarU.label, label);
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
  
  public List<SSUri> getCircleURIsForUser(
    final SSUri   userUri,
    final Boolean withSystemCircles) throws Exception{
   
    ResultSet resultSet = null;
    
    try{
      
      final List<SSUri>         circleUris  = new ArrayList<>();
      final Map<String, String> wheres      = new HashMap<>();
      final List<SSUri>         tmpCircleUris;
      
      where(wheres, SSSQLVarU.userId, userUri);
      
      resultSet = dbSQL.select(circleUsersTable, wheres);
      
      tmpCircleUris = getURIsFromResult(resultSet, SSSQLVarU.circleId);
      
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
      dbSQL.closeStmt(resultSet);
    }
  }

  public void addCircle(
    final SSUri     circleUri, 
    final SSCircleE circleType,
    final Boolean   isSystemCircle) throws Exception{
    
    try{

      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarU.circleId,       circleUri);
      insert(inserts, SSSQLVarU.circleType,     circleType);
      insert(inserts, SSSQLVarU.isSystemCircle, isSystemCircle);
      
      dbSQL.insert(circleTable, inserts);
      
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
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
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
  
  public SSUri addOrGetPrivCircleURI(
    final SSUri user) throws Exception{
    
    try{
      
      try{
        return getPrivCircleURI(user);
      }catch(SSErr error){
        
        switch(error.code){
          case entityDoesntExist: break;
          default: throw error;
        }
      }

      final SSUri               circleUri  = SSServCaller.vocURICreate("circle");
      
      addEntityIfNotExists(
        circleUri, 
        SSEntityE.circle, 
        null, 
        null, 
        SSVoc.systemUserUri, 
        null);

      addCircle(
        circleUri, 
        SSCircleE.priv,
        true);
      
      addUserToCircleIfNotExists(
        circleUri, 
        user);

      return circleUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
    
  public SSUri addOrGetPubCircleURI() throws Exception{
    
    try{
      
      try{
        return getPubCircleURI();
      }catch(SSErr error){
        
        switch(error.code){
          case entityDoesntExist: break;
          default: throw error;
        }
      }

      final Map<String, String> inserts    = new HashMap<>();
      final SSUri               circleUri  = SSServCaller.vocURICreate("circle");
      
      addEntityIfNotExists(
        circleUri, 
        SSEntityE.circle, 
        null, 
        null, 
        SSVoc.systemUserUri, 
        null);

      addCircle(
        circleUri, 
        SSCircleE.pub,
        true);

      return circleUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public void addUserToCircleIfNotExists(
    final SSUri circleUri, 
    final SSUri userUri) throws Exception{
    
    try{
      
      if(hasCircleUser(circleUri, userUri)){
        return;
      }
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarU.circleId, circleUri);
      insert(inserts, SSSQLVarU.userId,   userUri);
      
      dbSQL.insert(circleUsersTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addEntityToCircleIfNotExists(
    final SSUri circleUri,
    final SSUri entityUri) throws Exception{
    
    try{
      
      if(hasCircleEntity(circleUri, entityUri)){
        return;
      }
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarU.circleId, circleUri);
      insert(inserts, SSSQLVarU.entityId, entityUri);
      
      dbSQL.insert(circleEntitiesTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getUserURIsForCircle(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String>   wheres    = new HashMap<>();
      
      where(wheres, SSSQLVarU.circleId, circleUri);
      
      resultSet = dbSQL.select(circleUsersTable, wheres);
      
      return getURIsFromResult(resultSet, SSSQLVarU.userId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSCircleE> getCircleTypesForEntity(
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>              tables            = new ArrayList<>();
      final Map<String, String>       wheres            = new HashMap<>();
      final List<String>              columns           = new ArrayList<>();
      final List<String>              tableCons         = new ArrayList<>();
      
      table    (tables,    circleEntitiesTable);
      table    (tables,    circleTable);
      column   (columns,   SSSQLVarU.circleType);
      where    (wheres,    SSSQLVarU.entityId, entityUri);
      tableCon (tableCons, circleTable, SSSQLVarU.circleId, circleEntitiesTable, SSSQLVarU.circleId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons); //distinct
      
      return SSCircleE.get(getStringsFromResult(resultSet, SSSQLVarU.circleType));
      
//        if(!SSCircleE.contains(circleTypes, circleType)){
//          circleTypes.add(circleType);
//        }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSCircleE> getCircleTypesCommonForUserAndEntity(
    final SSUri userUri,
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>              tables       = new ArrayList<>();
      final Map<String, String>       wheres       = new HashMap<>();
      final List<String>              columns      = new ArrayList<>();
      final List<String>              tableCons    = new ArrayList<>();
      
      table    (tables,    circleUsersTable);
      table    (tables,    circleEntitiesTable);
      table    (tables,    circleTable);
      column   (columns,   SSSQLVarU.circleType);
      where    (wheres,    SSSQLVarU.entityId, entityUri);
      where    (wheres,    SSSQLVarU.userId,   userUri);
      tableCon (tableCons, circleTable, SSSQLVarU.circleId, circleEntitiesTable, SSSQLVarU.circleId);
      tableCon (tableCons, circleTable, SSSQLVarU.circleId, circleUsersTable,    SSSQLVarU.circleId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      return SSCircleE.get(getStringsFromResult(resultSet, SSSQLVarU.circleType));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSEntityCircle> getCirclesCommonForUserAndEntity(
    final SSUri   userUri,
    final SSUri   entityUri,
    final Boolean withSystemCircles) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSEntityCircle>      circles    = new ArrayList<>();
      final List<String>              tables     = new ArrayList<>();
      final Map<String, String>       wheres     = new HashMap<>();
      final List<String>              columns    = new ArrayList<>();
      final List<String>              tableCons  = new ArrayList<>();
      SSEntityCircle                  circleObj;
      
      table     (tables,  circleUsersTable);
      table     (tables,  circleEntitiesTable);
      table     (tables,  circleTable);
      table     (tables,  entityTable);
      column    (columns, SSSQLVarU.label);
      column    (columns, SSSQLVarU.description);
      column    (columns, SSSQLVarU.circleType);
      column    (columns, SSSQLVarU.isSystemCircle);
      column    (columns, circleTable,        SSSQLVarU.circleId);
      where     (wheres,  SSSQLVarU.entityId, entityUri);
      where     (wheres,  SSSQLVarU.userId,   userUri);
      
      if(!withSystemCircles){
        where     (wheres,  SSSQLVarU.isSystemCircle, false);
      }
      
      tableCon  (tableCons, circleTable, SSSQLVarU.circleId, circleEntitiesTable, SSSQLVarU.circleId);
      tableCon  (tableCons, circleTable, SSSQLVarU.circleId, circleUsersTable,    SSSQLVarU.circleId);
      tableCon  (tableCons, circleTable, SSSQLVarU.circleId, entityTable,         SSSQLVarU.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      while(resultSet.next()){
        
        circleObj =
          SSEntityCircle.get(
            bindingStrToUri         (resultSet, SSSQLVarU.circleId),
            bindingStrToLabel       (resultSet, SSSQLVarU.label),
            SSCircleE.get           (bindingStr(resultSet, SSSQLVarU.circleType)),
            bindingStrToBoolean     (resultSet,SSSQLVarU.isSystemCircle));

        circleObj.description = bindingStrToTextComment (resultSet, SSSQLVarU.description);
        
        circleObj.users.addAll(getUserURIsForCircle(circleObj.id));
        
        circles.add(circleObj);
      }
      
      return circles;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getThumbs(final SSUri entity) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres            = new HashMap<>();
      
      where(wheres, SSSQLVarU.entityId, entity);
      
      resultSet = dbSQL.select(thumbnailsTable, wheres);
      
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
      
      if(wheres.isEmpty()){
        resultSet = dbSQL.select(imageTable);
      }else{
        
        if(tableCons.isEmpty()){
          resultSet = dbSQL.select(imageTable, columns, wheres);
        }else{
          resultSet = dbSQL.select(tables, columns, wheres, tableCons);
        }
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
      
      final Map<String, String> wheres            = new HashMap<>();
      
      where(wheres, SSSQLVarU.entityId, entity);
      
      resultSet = dbSQL.select(downloadsTable, wheres);
      
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
      
      final Map<String, String> wheres            = new HashMap<>();
      
      where(wheres, SSSQLVarU.entityId, entity);
      
      resultSet = dbSQL.select(filesTable, wheres);
      
      return getURIsFromResult(resultSet, SSSQLVarU.fileId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public Boolean isSystemCircle(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where (wheres,    SSSQLVarU.circleId, circleUri);
      
      resultSet = dbSQL.select(circleTable, wheres);
      
      checkFirstResult(resultSet);
      
      return bindingStrToBoolean(resultSet, SSSQLVarU.isSystemCircle);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSEntityCircle getCircle(
    final SSUri                circleUri,
    final Boolean              withUsers,
    final Boolean              withEntities,
    final Boolean              withCircleRights) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        tables            = new ArrayList<>();
      final List<String>        columns           = new ArrayList<>();
      final List<String>        tableCons         = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      final SSEntityCircle      circleObj;
      
      table    (tables,    circleTable);
      table    (tables,    entityTable);
      column   (columns,   SSSQLVarU.label);
      column   (columns,   SSSQLVarU.description);
      column   (columns,   SSSQLVarU.circleId);
      column   (columns,   SSSQLVarU.circleType);
      column   (columns,   SSSQLVarU.isSystemCircle);
      where    (wheres,    SSSQLVarU.circleId, circleUri);
      tableCon (tableCons, circleTable,        SSSQLVarU.circleId, entityTable, SSSQLVarU.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      checkFirstResult(resultSet);
      
      circleObj =
        SSEntityCircle.get(
          circleUri,
          bindingStrToLabel       (resultSet, SSSQLVarU.label),
          SSCircleE.get           (bindingStr(resultSet, SSSQLVarU.circleType)),
          bindingStrToBoolean     (resultSet,SSSQLVarU.isSystemCircle));
      
      circleObj.description = bindingStrToTextComment (resultSet, SSSQLVarU.description);
      
      if(withUsers){
        circleObj.users.addAll(getUserURIsForCircle(circleObj.id));
      }
      
      if(withEntities){
        circleObj.entities.addAll(getCircleEntityURIs(circleUri));
      }

      if(withCircleRights){
        circleObj.accessRights.addAll(SSEntityMiscFct.getCircleRights(circleObj.circleType));
      }
      
      return circleObj;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getCircleURIsForEntity(
    final SSUri   entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String>   wheres      = new HashMap<>();
      final List<String>          columns     = new ArrayList<>();
      
      column (columns, SSSQLVarU.circleId);
      where  (wheres,  SSSQLVarU.entityId, entityUri);

      resultSet = dbSQL.select(circleEntitiesTable, columns, wheres);
      
      return getURIsFromResult(resultSet, SSSQLVarU.circleId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getCircleEntityURIs(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String>   wheres      = new HashMap<>();
      
      where(wheres, SSSQLVarU.circleId, circleUri);
      
      resultSet = dbSQL.select(circleEntitiesTable, wheres);
      
      return getURIsFromResult(resultSet, SSSQLVarU.entityId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public SSCircleE getTypeForCircle(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String>   wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.circleId, circleUri);
      
      resultSet = dbSQL.select(circleTable, wheres);

      checkFirstResult(resultSet);
      
      return SSCircleE.get(bindingStr(resultSet, SSSQLVarU.circleType));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public Boolean isUserInCircle(
    final SSUri          userUri, 
    final SSUri          circleUri) throws Exception{
  
    try{
      return SSStrU.contains(getCircleURIsForUser(userUri, true), circleUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public Boolean isGroupCircle(
    final SSUri circleUri) throws Exception{
    
    try{
      return SSCircleE.equals(getTypeForCircle(circleUri), SSCircleE.group);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private Boolean hasCircleEntity(
    final SSUri circleUri,
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.circleId, circleUri);
      where(wheres, SSSQLVarU.entityId, entityUri);
      
      resultSet = dbSQL.select(circleEntitiesTable, wheres);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  private Boolean hasCircleUser(
    final SSUri circleUri,
    final SSUri userUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.circleId, circleUri);
      where(wheres, SSSQLVarU.userId,   userUri);
      
      resultSet = dbSQL.select(circleUsersTable, wheres);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  private SSUri getPrivCircleURI(
    final SSUri     user) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        tables            = new ArrayList<>();
      final List<String>        columns           = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      final List<String>        tableCons         = new ArrayList<>();
      
      column   (columns,   SSSQLVarU.circleId);
      table    (tables,    circleTable);
      table    (tables,    entityTable);
      
      where(wheres, SSSQLVarU.circleType, SSCircleE.priv);
      where(wheres, SSSQLVarU.author,     user);
      
      tableCon (tableCons, circleTable, SSSQLVarU.circleId, entityTable, SSSQLVarU.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarU.circleId);
      
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
  
  private SSUri getPubCircleURI() throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();

      where(wheres, SSSQLVarU.circleType, SSCircleE.pub);

      resultSet = dbSQL.select(circleTable, wheres);

      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarU.circleId);
      
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

  public List<SSEntity> getEntitiesForLabelsAndDescriptions(
    final List<String> keywords) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<SSEntity>            entities  = new ArrayList<>();
      final List<String>              columns   = new ArrayList<>();
      final List<String>              matches   = new ArrayList<>();
      final List<String>              againsts  = new ArrayList<>();
      SSEntity                        entityObj;
      
      column (columns, SSSQLVarU.id);
      column (columns, SSSQLVarU.label);
      column (columns, SSSQLVarU.description);
      column (columns, SSSQLVarU.type);
      match  (matches, SSSQLVarU.label);
      match  (matches, SSSQLVarU.description);
      
      againsts.addAll(keywords);
      
      resultSet = dbSQL.select(entityTable, columns, matches, againsts);
      
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
    final List<String> keywords) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<SSEntity>            entities  = new ArrayList<>();
      final List<String>              columns   = new ArrayList<>();
      final List<String>              matches   = new ArrayList<>();
      final List<String>              againsts  = new ArrayList<>();
      SSEntity                        entityObj;
      
      column (columns, SSSQLVarU.id);
      column (columns, SSSQLVarU.label);
      column (columns, SSSQLVarU.type);
      column (columns, SSSQLVarU.description);
      match  (matches, SSSQLVarU.label);
      
      againsts.addAll(keywords);
      
      resultSet = dbSQL.select(entityTable, columns, matches, againsts);
      
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
    final List<String> keywords) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<SSEntity>            entities  = new ArrayList<>();
      final List<String>              columns   = new ArrayList<>();
      final List<String>              matches   = new ArrayList<>();
      final List<String>              againsts  = new ArrayList<>();
      SSEntity                        entityObj;
      
      column (columns, SSSQLVarU.id);
      column (columns, SSSQLVarU.label);
      column (columns, SSSQLVarU.type);
      column (columns, SSSQLVarU.description);
      match  (matches, SSSQLVarU.description);
      
      againsts.addAll(keywords);
      
      resultSet = dbSQL.select(entityTable, columns, matches, againsts);
      
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
      final Map<String, String>       wheres    = new HashMap<>();
      
      where(wheres, SSSQLVarU.userId,   user);
      where(wheres, SSSQLVarU.entityId, entity);
      
      resultSet = dbSQL.select(entityReadsTable, wheres);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void entityRead(
    final SSUri user, 
    final SSUri entity) throws Exception{
    
    try{

      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarU.entityId,     entity);
      insert(inserts, SSSQLVarU.userId,       user);

      uniqueKey(uniqueKeys, SSSQLVarU.entityId, entity);
      uniqueKey(uniqueKeys, SSSQLVarU.userId,   user);

      dbSQL.insertIfNotExists(entityReadsTable, inserts, uniqueKeys);
      
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
      
      if(forEntity != null){
        
        final List<String>         columns    = new ArrayList<>();              
        final List<String>         tables     = new ArrayList<>();
        final Map<String, String>  wheres     = new HashMap<>();
        final List<String>         tableCons  = new ArrayList<>();
      
        column(columns, locationTable, SSSQLVarU.locationId);
        column(columns, locationTable, SSSQLVarU.latitude);
        column(columns, locationTable, SSSQLVarU.longitude);
        column(columns, locationTable, SSSQLVarU.accuracy);
        
        table(tables, locationTable);
        table(tables, entityLocationsTable);
        
        where(wheres, SSSQLVarU.entityId, forEntity);
        
        tableCon(tableCons, locationTable, SSSQLVarU.locationId, entityLocationsTable, SSSQLVarU.locationId);
        
        resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      }else{
        resultSet = dbSQL.select(locationTable);
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