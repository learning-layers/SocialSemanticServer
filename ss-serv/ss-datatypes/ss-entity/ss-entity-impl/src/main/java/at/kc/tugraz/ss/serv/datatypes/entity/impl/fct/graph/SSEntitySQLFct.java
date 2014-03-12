/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.graph;

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class SSEntitySQLFct extends SSDBSQLFct{

  public SSEntitySQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }

  public SSEntity getEntity(final SSUri entityUri) throws Exception{
    
    if(entityUri == null){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return null;
    }
    
    final Map<String, String> whereParNamesWithValues   = new HashMap<String, String>();
    ResultSet                 resultSet                 = null;

    whereParNamesWithValues.put(SSSQLVarU.id, entityUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(entityTable, whereParNamesWithValues);
      
      resultSet.first();
      
      return SSEntity.get(
        entityUri, 
        bindingStrToLabel(resultSet, SSSQLVarU.label), 
        bindingStrToLong(resultSet, SSSQLVarU.creationTime), 
        bindingStrToEntityType(resultSet, SSSQLVarU.type), 
        bindingStrToUri(resultSet, SSSQLVarU.author));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public Long getEntityCreationTime(final SSUri entityUri) throws Exception{
    
    if(entityUri == null){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return null;
    }
    
    //    return ((SSEntityHandlerImplI) SSServReg.servForEntityType(entityType).serv()).entityCreationTimeGet(entityUri);
    
    final Map<String, String> whereParNamesWithValues   = new HashMap<String, String>();
    ResultSet                 resultSet                 = null;
    Long                      creationTime              = 0L;
    
    whereParNamesWithValues.put(SSSQLVarU.id, entityUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(entityTable, whereParNamesWithValues);
      
      resultSet.first();
      
      creationTime = bindingStrToLong(resultSet, SSSQLVarU.creationTime);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return creationTime;
  }
  
  public Boolean existsEntity(SSUri entityUri) throws Exception{
    
    if(entityUri == null){
      SSServErrReg.regErrThrow(new Exception("entity null"));
      return null;
    }
    
    final Map<String, String> selectPars = new HashMap<String, String>();
    ResultSet                 resultSet  = null;
    Boolean                   exists     = false;
    
    selectPars.put(SSSQLVarU.id, entityUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(entityTable, selectPars);
      exists    = resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return exists;
  }
  
  public void addEntityAtCreationTime(
    final SSUri        entityUri, 
    final SSEntityA    label, 
    final Long         creationTime, 
    final SSEntityEnum entityType, 
    final SSUri        authorUri) throws Exception{

    if(SSObjU.isNull(entityUri, creationTime)){
      SSServErrReg.regErrThrow(new Exception("pars not okay"));
      return; 
    }
    
    final Map<String, String> insertPars = new HashMap<String, String>();
    
    insertPars.put(SSSQLVarU.id,              entityUri.toString());
    insertPars.put(SSSQLVarU.creationTime,    SSStrU.toString(creationTime));    
    
    if(label == null){
      insertPars.put(SSSQLVarU.label,         SSStrU.empty);
    }else{
      insertPars.put(SSSQLVarU.label,         label.toString());
    }
    
    if(entityType == null){
      insertPars.put(SSSQLVarU.type,          SSEntityEnum.entity.toString());
    }else{
      insertPars.put(SSSQLVarU.type,          entityType.toString());
    }
    
    if(label == null){
      insertPars.put(SSSQLVarU.author,         SSStrU.empty);
    }else{
      insertPars.put(SSSQLVarU.author,         authorUri.toString());
    }
    
    dbSQL.insert(entityTable, insertPars);
  }
  
  public void addEntity(
    SSUri        entityUri, 
    SSEntityA    label, 
    SSEntityEnum entityType,
    SSUri        authorUri) throws Exception{
   
    if(entityUri == null){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return;
    }
     
    final Map<String, String> insertPars = new HashMap<String, String>();

    insertPars.put(SSSQLVarU.id,             entityUri.toString());
    insertPars.put(SSSQLVarU.creationTime,   SSDateU.dateAsLong().toString());
    
    if(label == null){
      insertPars.put(SSSQLVarU.label,         SSStrU.empty);
    }else{
      insertPars.put(SSSQLVarU.label,         label.toString());
    }
    
    if(entityType == null){    
      insertPars.put(SSSQLVarU.type,          SSEntityEnum.entity.toString());
    }else{
      insertPars.put(SSSQLVarU.type,          entityType.toString());
    }
    
    if(authorUri == null){    
      insertPars.put(SSSQLVarU.author,        SSStrU.empty);
    }else{
      insertPars.put(SSSQLVarU.author,        authorUri.toString());
    }
    
    dbSQL.insert(entityTable, insertPars);
  }
  
  public SSLabelStr entityLabelGet(SSUri entityUri) throws Exception {
   
    final Map<String, String> selectPars = new HashMap<String, String>();
    ResultSet                 resultSet  = null;
    SSLabelStr                label      = null;
    
    selectPars.put(SSSQLVarU.id, entityUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(entityTable, selectPars);
      
      resultSet.first();
      
      label = bindingStrToLabel(resultSet, SSSQLVarU.label);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return label;
  }
  
  public SSUri getEntityAuthor(SSUri entityUri) throws Exception{
    
    if(entityUri == null){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return null;
    }
    
    final Map<String, String> selectPars = new HashMap<String, String>();
    ResultSet                 resultSet  = null;
    SSUri                     authorUri  = null;
    
    
    selectPars.put(SSSQLVarU.id, entityUri.toString());
    
    try{
      
      resultSet = dbSQL.selectAllWhere(entityTable, selectPars);
      
      resultSet.first();
      
      authorUri = bindingStrToUri(resultSet, SSSQLVarU.author);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
   
    return authorUri;
  }
  
  public SSEntityEnum getEntityType(SSUri entityUri) throws Exception{
    
    if(entityUri == null){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return null;
    }
    
    final Map<String, String> selectPars = new HashMap<String, String>();
    ResultSet                 resultSet  = null;
    SSEntityEnum              entityType = null;
    
    selectPars.put(SSSQLVarU.id, entityUri.toString());
    
    try{
      
      resultSet = dbSQL.selectAllWhere(entityTable, selectPars);
      
      resultSet.first();
      
      entityType = bindingStrToEntityType(resultSet, SSSQLVarU.type);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
   
    return entityType;
  }
    
  public void entityLabelSet(SSUri entityUri, SSLabelStr label) throws Exception {
    
    if(entityUri == null){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return;
    }
   
    final Map<String, String>  updatePars = new HashMap<String, String>();
    final Map<String, String>  newValues  = new HashMap<String, String>();
    
    updatePars.put (SSSQLVarU.id,       entityUri.toString());
    
    if(label == null){
      newValues.put  (SSSQLVarU.label   , SSStrU.empty);
    }else{
      newValues.put  (SSSQLVarU.label   , label.toString());
    }
    
    dbSQL.updateWhere(entityTable, updatePars, newValues);
  }
  
  public void entityRemove(SSUri entityUri) throws Exception{
    
    if(entityUri == null){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return;
    }
      
    final Map<String, String> deletePars = new HashMap<String, String>();
    
    deletePars.put(SSSQLVarU.id, entityUri.toString());
    
    dbSQL.deleteWhere(entityTable, deletePars);
  }

  public void removeAllEntities() throws Exception {
    dbSQL.deleteAll(entityTable);
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