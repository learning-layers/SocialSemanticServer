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
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSNoResultFoundErr;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.user.api.SSUserGlobals;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSEntitySQLFct extends SSDBSQLFct{

  public SSEntitySQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }

  public SSEntity getEntity(
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      final Map<String, String> where = new HashMap<String, String>();
      
      where(where, SSSQLVarU.id, entityUri);
      
      resultSet = dbSQL.select(entityTable, where);
      
      checkFirstResult(resultSet);
      
      return SSEntity.get(
        entityUri,
        bindingStrToLabel      (resultSet, SSSQLVarU.label),
        bindingStrToLong       (resultSet, SSSQLVarU.creationTime),
        bindingStrToEntityType (resultSet, SSSQLVarU.type),
        bindingStrToUri        (resultSet, SSSQLVarU.author));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addEntityAtCreationTimeIfNotExists(
    final SSUri        entityUri, 
    final SSEntityA    label, 
    final Long         creationTime, 
    final SSEntityE    entityType, 
    final SSUri        authorUri) throws Exception{

    try{
      final Map<String, String> inserts     = new HashMap<String, String>();
      final Map<String, String> uniqueKeys  = new HashMap<String, String>();
      
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
      
      dbSQL.insertIfNotExists(entityTable, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addEntityIfNotExists(
    final SSUri        entityUri,
    final SSEntityA    label,
    final SSEntityE    entityType,
    final SSUri        authorUri) throws Exception{
    
    try{
      final Map<String, String> inserts    = new HashMap<String, String>();
      final Map<String, String> uniqueKeys = new HashMap<String, String>();
      
      uniqueKey (uniqueKeys,  SSSQLVarU.id,           entityUri);
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
      
      dbSQL.insertIfNotExists(entityTable, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void updateEntityLabelIfExists(
    final SSUri      entityUri,
    final SSLabel    label) throws Exception {
    
    try{
      final Map<String, String>  wheres   = new HashMap<String, String>();
      final Map<String, String>  updates  = new HashMap<String, String>();
      
      where(wheres, SSSQLVarU.id, entityUri);
      
      if(label == null){
        update (updates, SSSQLVarU.label, SSStrU.empty);
      }else{
        update (updates, SSSQLVarU.label, label);
      }
      
      dbSQL.updateIgnore(entityTable, wheres, updates);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void deleteEntityIfExists(
    final SSUri entityUri) throws Exception{
    
    try{
      final Map<String, String> deletes = new HashMap<String, String>();
      
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
      
      final Map<String, String> wheres      = new HashMap<String, String>();
      
      where(wheres, SSSQLVarU.userId, userUri);
      
      resultSet = dbSQL.select(circleUsersTable, wheres);
      
      return getURIsFromResult(resultSet, SSSQLVarU.circleId);
      
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public void addCircle(
    final SSUri     circleUri, 
    final SSCircleE circleType) throws Exception{
    
    try{

      final Map<String, String> inserts = new HashMap<String, String>();
      
      insert(inserts, SSSQLVarU.circleId,   circleUri);
      insert(inserts, SSSQLVarU.circleType, circleType);
      
      dbSQL.insert(circleTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public SSUri addOrGetPublicCircle() throws Exception{
    
    try{
      
      try{
        return getCirclePublicURI();
      }catch(SSNoResultFoundErr error){}

      final Map<String, String> inserts    = new HashMap<String, String>();
      final SSUri               circleUri  = createCircleURI();
      
      addEntityIfNotExists(
        circleUri, 
        SSLabel.get(SSStrU.empty), 
        SSEntityE.circle, 
        SSUri.get(SSUserGlobals.systemUserURI));

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
      
      final Map<String, String> inserts = new HashMap<String, String>();
      
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
      
      final Map<String, String> inserts = new HashMap<String, String>();
      
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
      
      final Map<String, String>   wheres    = new HashMap<String, String>();
      
      where(wheres, SSSQLVarU.circleId, circleUri);
      
      resultSet = dbSQL.select(circleUsersTable, wheres);
      
      return getURIsFromResult(resultSet, SSSQLVarU.userId);
      
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSCircleE> getCircleTypesForEntity(
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>              tables            = new ArrayList<String>();
      final Map<String, String>       wheres            = new HashMap<String, String>();
      final List<String>              columns           = new ArrayList<String>();
      final List<String>              tableCons         = new ArrayList<String>();
      
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
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSCircleE> getCircleTypesCommonForUserAndEntity(
    final SSUri userUri,
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>              tables       = new ArrayList<String>();
      final Map<String, String>       wheres       = new HashMap<String, String>();
      final List<String>              columns      = new ArrayList<String>();
      final List<String>              tableCons    = new ArrayList<String>();
      
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
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSEntityCircle> getCirclesCommonForUserAndEntity(
    final SSUri userUri,
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSEntityCircle>      circles    = new ArrayList<SSEntityCircle>();
      final List<String>              tables     = new ArrayList<String>();
      final Map<String, String>       wheres     = new HashMap<String, String>();
      final List<String>              columns    = new ArrayList<String>();
      final List<String>              tableCons  = new ArrayList<String>();
      
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
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSEntityCircle getCircle(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        tables            = new ArrayList<String>();
      final List<String>        columns           = new ArrayList<String>();
      final List<String>        tableCons         = new ArrayList<String>();
      final Map<String, String> wheres            = new HashMap<String, String>();
      
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
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSUri> getCircleURIsForEntity(
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String>   wheres      = new HashMap<String, String>();
      final List<String>          columns     = new ArrayList<String>();
      
      column (columns, SSSQLVarU.circleId);
      where  (wheres,  SSSQLVarU.entityId, entityUri);

      resultSet = dbSQL.select(circleEntitiesTable, columns, wheres);
      
      return getURIsFromResult(resultSet, SSSQLVarU.circleId);
      
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSUri> getCircleEntityURIs(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String>   wheres      = new HashMap<String, String>();
      
      where(wheres, SSSQLVarU.circleId, circleUri);
      
      resultSet = dbSQL.select(circleEntitiesTable, wheres);
      
      return getURIsFromResult(resultSet, SSSQLVarU.entityId);
      
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public SSCircleE getCircleType(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String>   wheres = new HashMap<String, String>();
      
      where(wheres, SSSQLVarU.circleId, circleUri);
      
      resultSet = dbSQL.select(circleTable, wheres);

      checkFirstResult(resultSet);
      
      return SSCircleE.get(bindingStr(resultSet, SSSQLVarU.circleType));
      
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public Boolean isUserInCircle(
    final SSUri          userUri, 
    final SSUri          circleUri) throws Exception{
  
    try{
      return SSUri.contains (getCircleURIsForUser(userUri), circleUri);
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
      return SSUri.equals(getEntity(circleUri).author, SSUri.get(SSUserGlobals.systemUserURI));
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
      
      final Map<String, String> wheres = new HashMap<String, String>();
      
      where(wheres, SSSQLVarU.circleId, circleUri);
      where(wheres, SSSQLVarU.entityId, entityUri);
      
      resultSet = dbSQL.select(circleEntitiesTable, wheres);
      
      return resultSet.first();
      
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private Boolean hasCircleUser(
    final SSUri circleUri,
    final SSUri userUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres = new HashMap<String, String>();
      
      where(wheres, SSSQLVarU.circleId, circleUri);
      where(wheres, SSSQLVarU.userId,   userUri);
      
      resultSet = dbSQL.select(circleUsersTable, wheres);
      
      return resultSet.first();
      
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private SSUri getCirclePublicURI() throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres = new HashMap<String, String>();

      where(wheres, SSSQLVarU.circleType, SSCircleE.pub);

      resultSet = dbSQL.select(circleTable, wheres);

      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarU.circleId);
      
    }catch(SSNoResultFoundErr error){
      throw error;
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
//      newValues = new HashMap<String, String>();
//      newValues.put(SSSQLVarU.label,    label.toString());
//      
//      parNamesAndValues = new HashMap<String, String>();
//      
//      parNamesAndValues.put(SSSQLVarU.id, entity.toString());
//      
//      dbSQL.dbSQLUpdateWhere(entityTable, parNamesAndValues, newValues);
//    }else{
//    
//      parNamesAndValues = new HashMap<String, String>();
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
//    final Map<String, String> where      = new HashMap<String, String>();
//    final List<String>        columns    = new ArrayList<String>();
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