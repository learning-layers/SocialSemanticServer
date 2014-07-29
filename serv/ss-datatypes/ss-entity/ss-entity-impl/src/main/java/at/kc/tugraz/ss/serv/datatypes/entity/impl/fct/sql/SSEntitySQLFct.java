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
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSEntityDoesntExistErr;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSNoResultFoundErr;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return true;
      }catch(SSNoResultFoundErr error){
        return false;
      }
      
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
      
      where(where, SSSQLVarU.id, entityUri);
      
      resultSet = dbSQL.select(entityTable, where);
      
      checkFirstResult(resultSet);
      
      return SSEntity.get(
        entityUri,
        bindingStrToLabel      (resultSet, SSSQLVarU.label),
        bindingStrToLong       (resultSet, SSSQLVarU.creationTime),
        bindingStrToEntityType (resultSet, SSSQLVarU.type),
        bindingStrToUri        (resultSet, SSSQLVarU.author), 
        SSTextComment.get      (bindingStr(resultSet, SSSQLVarU.description)),
        null, 
        null, 
        null, 
        null);
      
    }catch(SSNoResultFoundErr error){
      throw new SSEntityDoesntExistErr();
    }catch(Exception error){
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
      
      where(where, SSSQLVarU.label, label);
      where(where, SSSQLVarU.type,  type);
      
      resultSet = dbSQL.select(entityTable, where);
      
      checkFirstResult(resultSet);
      
      return SSEntity.get(
        bindingStrToUri        (resultSet, SSSQLVarU.id),
        label,
        bindingStrToLong       (resultSet, SSSQLVarU.creationTime),
        type,
        bindingStrToUri        (resultSet, SSSQLVarU.author),
        SSTextComment.get      (bindingStr(resultSet, SSSQLVarU.description)),
        null,
        null,
        null, 
        null);
      
    }catch(SSNoResultFoundErr error){
      throw new SSEntityDoesntExistErr();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addEntityAtCreationTimeIfNotExists(
    final SSUri         entityUri, 
    final SSEntityA     label, 
    final Long          creationTime, 
    final SSEntityE     entityType, 
    final SSUri         authorUri,
    final SSTextComment description) throws Exception{

    try{
      final Map<String, String> inserts     = new HashMap<>();
      final Map<String, String> uniqueKeys  = new HashMap<>();
      
      uniqueKey (uniqueKeys,  SSSQLVarU.id,           entityUri);
      insert    (inserts,     SSSQLVarU.id,           entityUri);
      insert    (inserts,     SSSQLVarU.creationTime, creationTime);
      
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
      
      dbSQL.insertIfNotExists(entityTable, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addEntityIfNotExists(
    final SSUri         entityUri,
    final SSEntityA     label,
    final SSEntityE     entityType,
    final SSUri         authorUri,
    final SSTextComment description) throws Exception{
    
    try{
     
      if(existsEntity(entityUri)){
        return;
      }
      
      final Map<String, String> inserts    = new HashMap<>();
      
      insert    (inserts,     SSSQLVarU.id,           entityUri);
      insert    (inserts,     SSSQLVarU.creationTime, SSDateU.dateAsLong());
      
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
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Boolean updateEntity(
    final SSUri         entity, 
    final SSLabel       label, 
    final SSTextComment description) throws Exception{
    
    try{
      final Map<String, String>  wheres   = new HashMap<>();
      final Map<String, String>  updates  = new HashMap<>();
      
      if(!existsEntity(entity)){
        SSLogU.warn("entity not updated | doesnt exist");
        return false;
      }
      
      where(wheres, SSSQLVarU.id, entity);
      
      if(label != null){
        update (updates, SSSQLVarU.label, label);
      }
      
      if(description != null){
        update (updates, SSSQLVarU.description, description);
      }
      
      if(updates.isEmpty()){
        return true;
      }
      
      dbSQL.update(entityTable, wheres, updates);
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
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
    final SSUri userUri) throws Exception{
   
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres      = new HashMap<>();
      
      where(wheres, SSSQLVarU.userId, userUri);
      
      resultSet = dbSQL.select(circleUsersTable, wheres);
      
      return getURIsFromResult(resultSet, SSSQLVarU.circleId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void addCircle(
    final SSUri     circleUri, 
    final SSCircleE circleType) throws Exception{
    
    try{

      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarU.circleId,   circleUri);
      insert(inserts, SSSQLVarU.circleType, circleType);
      
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
      
      insert(inserts, SSSQLVarU.entityId,         entity);
      insert(inserts, SSSQLVarU.attachedEntityId, entityToAttach);
      
      dbSQL.insert(entitiesTable, inserts);
      
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
        
        attachedEntities.add(
          SSEntity.get(
            bindingStrToUri           (resultSet, SSSQLVarU.id),
            bindingStrToLabel         (resultSet, SSSQLVarU.label),
            bindingStrToLong          (resultSet, SSSQLVarU.creationTime),
            bindingStrToEntityType    (resultSet, SSSQLVarU.type),
            bindingStrToUri           (resultSet, SSSQLVarU.author),
            bindingStrToTextComment   (resultSet, SSSQLVarU.description),
            null, 
            null, 
            null, 
            null));
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
  
  public SSUri addOrGetPublicCircle() throws Exception{
    
    try{
      
      try{
        return getCirclePublicURI();
      }catch(SSEntityDoesntExistErr error){}

      final Map<String, String> inserts    = new HashMap<>();
      final SSUri               circleUri  = createCircleURI();
      
      addEntityIfNotExists(
        circleUri, 
        SSLabel.get(SSStrU.empty), 
        SSEntityE.circle, 
        SSVoc.systemUserUri,
        null);

      insert(inserts, SSSQLVarU.circleId,   circleUri);
      insert(inserts, SSSQLVarU.circleType, SSCircleE.pub);

      dbSQL.insert(circleTable, inserts);

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
  
  public List<SSUri> getCircleUserURIs(
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
    final SSUri userUri,
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSEntityCircle>      circles    = new ArrayList<>();
      final List<String>              tables     = new ArrayList<>();
      final Map<String, String>       wheres     = new HashMap<>();
      final List<String>              columns    = new ArrayList<>();
      final List<String>              tableCons  = new ArrayList<>();
      
      table     (tables,  circleUsersTable);
      table     (tables,  circleEntitiesTable);
      table     (tables,  circleTable);
      table     (tables,  entityTable);
      column    (columns, SSSQLVarU.label);
      column    (columns, SSSQLVarU.circleType);
      column    (columns, circleTable,        SSSQLVarU.circleId);
      where     (wheres,  SSSQLVarU.entityId, entityUri);
      where     (wheres,  SSSQLVarU.userId,   userUri);
      tableCon  (tableCons, circleTable, SSSQLVarU.circleId, circleEntitiesTable, SSSQLVarU.circleId);
      tableCon  (tableCons, circleTable, SSSQLVarU.circleId, circleUsersTable,    SSSQLVarU.circleId);
      tableCon  (tableCons, circleTable, SSSQLVarU.circleId, entityTable,         SSSQLVarU.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      while(resultSet.next()){
        
        circles.add(
          SSEntityCircle.get(
            bindingStrToUri         (resultSet, SSSQLVarU.circleId),
            bindingStrToLabel       (resultSet, SSSQLVarU.label),
            SSCircleE.get           (bindingStr(resultSet, SSSQLVarU.circleType)),
            null, 
            null, 
            null));
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
   
  public SSEntityCircle getCircle(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        tables            = new ArrayList<>();
      final List<String>        columns           = new ArrayList<>();
      final List<String>        tableCons         = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      
      table    (tables,    circleTable);
      table    (tables,    entityTable);
      column   (columns,   SSSQLVarU.label);
      column   (columns,   SSSQLVarU.circleId);
      column   (columns,   SSSQLVarU.circleType);
      where    (wheres,    SSSQLVarU.circleId, circleUri);
      tableCon (tableCons, circleTable,        SSSQLVarU.circleId, entityTable, SSSQLVarU.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      checkFirstResult(resultSet);
      
      return SSEntityCircle.get(
        bindingStrToUri   (resultSet, SSSQLVarU.circleId),
        bindingStrToLabel (resultSet, SSSQLVarU.label),
        SSCircleE.get     (bindingStr(resultSet, SSSQLVarU.circleType)),
        null,
        null,
        null);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getCircleURIsForEntity(
    final SSUri entityUri) throws Exception{
    
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

  public SSCircleE getCircleType(
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
      return SSStrU.contains(getCircleURIsForUser(userUri), circleUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public Boolean isGroupCircle(
    final SSUri circleUri) throws Exception{
    
    try{
      return SSCircleE.equals(getCircleType(circleUri), SSCircleE.group);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public Boolean isSystemCircle(
    final SSUri circleUri) throws Exception{
    
    try{
      return SSStrU.equals(getEntity(circleUri).author, SSVoc.systemUserUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSUri createCircleURI() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objCircle().toString()));
  }
  
  private SSUri objCircle() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityE.circle.toString());
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
  
  private SSUri getCirclePublicURI() throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();

      where(wheres, SSSQLVarU.circleType, SSCircleE.pub);

      resultSet = dbSQL.select(circleTable, wheres);

      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarU.circleId);
      
    }catch(SSNoResultFoundErr error){
      throw new SSEntityDoesntExistErr();
    }catch(Exception error){
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
      
      column (columns, SSSQLVarU.id);
      column (columns, SSSQLVarU.label);
      column (columns, SSSQLVarU.description);
      column (columns, SSSQLVarU.type);
      match  (matches, SSSQLVarU.label);
      match  (matches, SSSQLVarU.description);
      
      againsts.addAll(keywords);
      
      resultSet = dbSQL.select(entityTable, columns, matches, againsts);
      
      while(resultSet.next()){
      
        entities.add(
          SSEntity.get(
            bindingStrToUri        (resultSet, SSSQLVarU.id),
            bindingStrToLabel      (resultSet, SSSQLVarU.label),
            null, 
            bindingStrToEntityType (resultSet, SSSQLVarU.type),
            null,
            SSTextComment.get      (bindingStr(resultSet, SSSQLVarU.description)),
            null,
            null,
            null, 
            null));
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
      
      column (columns, SSSQLVarU.id);
      column (columns, SSSQLVarU.label);
      column (columns, SSSQLVarU.type);
      column (columns, SSSQLVarU.description);
      match  (matches, SSSQLVarU.label);
      
      againsts.addAll(keywords);
      
      resultSet = dbSQL.select(entityTable, columns, matches, againsts);
      
      while(resultSet.next()){
        
        entities.add(
          SSEntity.get(
            bindingStrToUri        (resultSet, SSSQLVarU.id),
            bindingStrToLabel      (resultSet, SSSQLVarU.label),
            null,
            bindingStrToEntityType (resultSet, SSSQLVarU.type),
            null,
            SSTextComment.get      (bindingStr(resultSet, SSSQLVarU.description)),
            null,
            null,
            null, 
            null));
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
      
      column (columns, SSSQLVarU.id);
      column (columns, SSSQLVarU.label);
      column (columns, SSSQLVarU.type);
      column (columns, SSSQLVarU.description);
      match  (matches, SSSQLVarU.description);
      
      againsts.addAll(keywords);
      
      resultSet = dbSQL.select(entityTable, columns, matches, againsts);
      
      while(resultSet.next()){
        
        entities.add(
          SSEntity.get(
            bindingStrToUri        (resultSet, SSSQLVarU.id),
            bindingStrToLabel      (resultSet, SSSQLVarU.label),
            null,
            bindingStrToEntityType (resultSet, SSSQLVarU.type),
            null,
            SSTextComment.get      (bindingStr(resultSet, SSSQLVarU.description)),
            null, 
            null,
            null, 
            null));
      }
      
      return entities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSTextComment> getComments(
    final SSUri entity) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
       if(!existsEntity(entity)){
        SSLogU.warn("comments not retrieved | entity doesnt exist");
        return new ArrayList<>();
      }
      
      final List<String>        tables            = new ArrayList<>();
      final List<String>        columns           = new ArrayList<>();
      final List<String>        tableCons         = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      
      column(columns, SSSQLVarU.commentContent);
      
      table(tables, commentTable);
      table(tables, commentsTable);
      
      where(wheres, SSSQLVarU.entityId, entity);
      
      tableCon(tableCons, commentTable, SSSQLVarU.commentId, commentsTable, SSSQLVarU.commentId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
    
      return getTextCommentsFromResult(resultSet, SSSQLVarU.commentContent);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void addComment(
    final SSUri         entity,
    final SSUri         commentUri) throws Exception{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarU.entityId,  entity);
      insert(inserts, SSSQLVarU.commentId, commentUri);
      
      dbSQL.insert(commentsTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void createComment(
    final SSUri         commentUri, 
    final SSTextComment content) throws Exception{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarU.commentId,      commentUri);
      insert(inserts, SSSQLVarU.commentContent, content);
      
      dbSQL.insert(commentTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
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