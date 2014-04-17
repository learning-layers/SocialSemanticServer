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
package at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircleTypeE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityRightTypeE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircle;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
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
    
    if(entityUri == null){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return null;
    }
    
    final Map<String, String> where      = new HashMap<String, String>();
    ResultSet                 resultSet  = null;

    where.put(SSSQLVarU.id, entityUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(entityTable, where);
      
      if(!resultSet.first()){
        throw new Exception("entity doesnt exist");
      }
      
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
    
    final Map<String, String> where            = new HashMap<String, String>();
    ResultSet                 resultSet        = null;
    
    where.put(SSSQLVarU.id, entityUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(entityTable, where);
      
      if(resultSet.first()){
        return bindingStrToLong(resultSet, SSSQLVarU.creationTime);
      }else{
        return 0L;
      }
      
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
    final SSEntityEnum entityType, 
    final SSUri        authorUri) throws Exception{

    if(SSObjU.isNull(entityUri, creationTime)){
      SSServErrReg.regErrThrow(new Exception("pars not okay"));
      return; 
    }
    
    final Map<String, String> insertPars = new HashMap<String, String>();
    final Map<String, String> uniqueKey  = new HashMap<String, String>();
    
    uniqueKey.put (SSSQLVarU.id,              SSUri.toStr(entityUri));
    insertPars.put(SSSQLVarU.id,              SSUri.toStr(entityUri));
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
    
    dbSQL.insertWhereNotExists(entityTable, insertPars, uniqueKey);
  }
  
  public void addEntityIfNotExists(
    SSUri        entityUri,
    SSEntityA    label,
    SSEntityEnum entityType,
    SSUri        authorUri) throws Exception{
    
    if(entityUri == null){
      throw new Exception("pars null");
    }
    
    final Map<String, String> insert    = new HashMap<String, String>();
    final Map<String, String> uniqueKey = new HashMap<String, String>();
    
    uniqueKey.put(SSSQLVarU.id,             SSUri.toStr(entityUri));
    insert.put   (SSSQLVarU.id,             SSUri.toStr(entityUri));
    insert.put   (SSSQLVarU.creationTime,   SSDateU.dateAsLong().toString());
    
    if(label == null){
      insert.put(SSSQLVarU.label,         SSStrU.empty);
    }else{
      insert.put(SSSQLVarU.label,         label.toString());
    }
    
    if(entityType == null){
      insert.put(SSSQLVarU.type,          SSEntityEnum.entity.toString());
    }else{
      insert.put(SSSQLVarU.type,          entityType.toString());
    }
    
    if(authorUri == null){
      insert.put(SSSQLVarU.author,        SSStrU.empty);
    }else{
      insert.put(SSSQLVarU.author,        SSUri.toStr(authorUri));
    }
    
    dbSQL.insertWhereNotExists(entityTable, insert, uniqueKey);
  }
  
  public SSLabelStr entityLabelGet(
    final SSUri entityUri) throws Exception {
   
    final Map<String, String> where      = new HashMap<String, String>();
    ResultSet                 resultSet  = null;
    
    where.put(SSSQLVarU.id, entityUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(entityTable, where);
      
      if(resultSet.first()){
        return bindingStrToLabel(resultSet, SSSQLVarU.label);
      }else{
        return SSLabelStr.get(SSStrU.empty);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri getEntityAuthor(
    final SSUri entityUri) throws Exception{
    
    if(entityUri == null){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return null;
    }
    
    final Map<String, String> where      = new HashMap<String, String>();
    ResultSet                 resultSet  = null;

    where.put(SSSQLVarU.id, entityUri.toString());
    
    try{
      
      resultSet = dbSQL.selectAllWhere(entityTable, where);
      
      if(resultSet.first()){
        return bindingStrToUri(resultSet, SSSQLVarU.author);
      }else{
        return null;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSEntityEnum getEntityType(
    final SSUri entityUri) throws Exception{
    
    if(entityUri == null){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return null;
    }
    
    final Map<String, String> where = new HashMap<String, String>();
    ResultSet                 resultSet  = null;
    
    try{
      
      where.put(SSSQLVarU.id, entityUri.toString());
      
      resultSet = dbSQL.selectAllWhere(entityTable, where);
      
      if(resultSet.first()){
        return bindingStrToEntityType(resultSet, SSSQLVarU.type);
      }else{
        return null;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
    
  public void entityLabelSet(
    final SSUri      entityUri,
    final SSLabelStr label) throws Exception {
    
    if(entityUri == null){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return;
    }
   
    final Map<String, String>  where   = new HashMap<String, String>();
    final Map<String, String>  values  = new HashMap<String, String>();
    
    where.put (SSSQLVarU.id,       entityUri.toString());
    
    if(label == null){
      values.put  (SSSQLVarU.label   , SSStrU.empty);
    }else{
      values.put  (SSSQLVarU.label   , SSLabelStr.toStr(label));
    }
    
    dbSQL.updateWhereIgnore(entityTable, where, values);
  }
  
  public void entityDeleteIgnore(
    final SSUri entityUri) throws Exception{
    
    if(entityUri == null){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return;
    }
      
    final Map<String, String> delete = new HashMap<String, String>();
    
    delete.put(SSSQLVarU.id, SSUri.toStr(entityUri));
    
    dbSQL.deleteWhereIgnore(entityTable, delete);
  }

  public void removeAllEntities() throws Exception {
    dbSQL.deleteAll(entityTable);
  }
  
    public List<SSUri> getUserCircleURIs(
    final SSUri userUri) throws Exception{
   
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(userUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String> where      = new HashMap<String, String>();
      final List<SSUri>         circleUris = new ArrayList<SSUri>();
      
      where.put(SSSQLVarU.userId, SSUri.toStr(userUri));
      
      resultSet = dbSQL.selectAllWhere(circleUsersTable, where);
      
      while(resultSet.next()){
        circleUris.add(bindingStrToUri(resultSet, SSSQLVarU.circleId));
      }
      
      return circleUris;
      
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public SSCircle getCircle(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(circleUri)){
        throw new Exception("pars not ok");
      }
      
      final List<String>        tableNames            = new ArrayList<String>();
      final List<String>        columnNames           = new ArrayList<String>();
      final Map<String, String> where                 = new HashMap<String, String>();
      
      tableNames.add  (circleTable);
      tableNames.add  (entityTable);
      columnNames.add (SSSQLVarU.label);
      columnNames.add (SSSQLVarU.circleId);
      columnNames.add (SSSQLVarU.circleType);
      
      where.put(SSSQLVarU.circleId, SSUri.toStr(circleUri));
      
      resultSet =
        dbSQL.selectCertainWhere(
          tableNames,
          columnNames,
          where,
          SSSQLVarU.circleId + SSStrU.equal + SSSQLVarU.id);
      
      resultSet.first();
      
      return SSCircle.get(
        bindingStrToUri   (resultSet, SSSQLVarU.circleId),
        bindingStrToLabel (resultSet, SSSQLVarU.label),
        SSEntityCircleTypeE.get(bindingStr(resultSet, SSSQLVarU.circleType)),
        null,
        null,
        null);
      
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public SSUri addCircle(
    final SSUri                     circleUri, 
    final SSEntityCircleTypeE circleType) throws Exception{
    
    try{

      if(SSObjU.isNull(circleUri, circleType)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String> insertPars = new HashMap<String, String>();
      
      insertPars.put(SSSQLVarU.circleId,   SSUri.toStr(circleUri));
      insertPars.put(SSSQLVarU.circleType, SSEntityCircleTypeE.toStr(circleType));
      
      dbSQL.insert(circleTable, insertPars);
      
      return circleUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSUri addPublicCircle() throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final Map<String, String> where = new HashMap<String, String>();

      where.put(SSSQLVarU.circleType, SSEntityCircleTypeE.pub.toString());

      resultSet = dbSQL.selectAllWhere(circleTable, where);

      if(resultSet.first()){
        return bindingStrToUri(resultSet, SSSQLVarU.circleId);
      }

      final Map<String, String> insert    = new HashMap<String, String>();
      final SSUri               circleUri = createCircleURI();

      insert.put(SSSQLVarU.circleId,   SSUri.toStr(circleUri));
      insert.put(SSSQLVarU.circleType, SSEntityCircleTypeE.pub.toString());

      dbSQL.insert(circleTable, insert);

      return circleUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public Boolean hasCircleRight(
    final SSUri                    circleUri,
    final SSEntityRightTypeE accessRight) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(circleUri, accessRight)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String> where = new HashMap<String, String>();
      
      where.put(SSSQLVarU.circleId,    SSUri.toStr(circleUri));
      where.put(SSSQLVarU.accessRight, SSEntityRightTypeE.all.toString());
      
      resultSet = dbSQL.selectAllWhere(circleRightsTable, where);
      
      if(resultSet.first()){
        return true;
      }
      
      where.put(SSSQLVarU.accessRight, SSEntityRightTypeE.toStr(accessRight));
      
      resultSet = dbSQL.selectAllWhere(circleRightsTable, where);
      
      return resultSet.first();
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public void addCircleRight(
    final SSUri              circleUri, 
    final SSEntityRightTypeE accessRight) throws Exception{
    
    try{

      if(SSObjU.isNull(circleUri, accessRight)){
        throw new Exception("pars not ok");
      }
      
      if(hasCircleRight(circleUri, accessRight)){
        return;
      }
      
      final Map<String, String> insertPars = new HashMap<String, String>();
      
      insertPars.put(SSSQLVarU.circleId,    SSUri.toStr(circleUri));
      insertPars.put(SSSQLVarU.accessRight, SSEntityRightTypeE.toStr(accessRight));
      
      dbSQL.insert(circleRightsTable, insertPars);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addUserToCircle(
    final SSUri circleUri, 
    final SSUri userUri) throws Exception{
    
    try{
      
      if(SSObjU.isNull(circleUri, userUri)){
        throw new Exception("pars not ok");
      }
      
      if(hasCircleUser(circleUri, userUri)){
        return;
      }
      
      final Map<String, String> insertPars = new HashMap<String, String>();
      
      insertPars.put(SSSQLVarU.circleId,      SSUri.toStr(circleUri));
      insertPars.put(SSSQLVarU.userId,        SSUri.toStr(userUri));
      
      dbSQL.insert(circleUsersTable, insertPars);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addEntityToCircle(
    final SSUri circleUri,
    final SSUri entityUri) throws Exception{
    
    try{
      
      if(SSObjU.isNull(circleUri, entityUri)){
        throw new Exception("pars not ok");
      }
      
      if(hasCircleEntity(circleUri, entityUri)){
        return;
      }
      
      final Map<String, String> insertPars = new HashMap<String, String>();
      
      insertPars.put(SSSQLVarU.circleId,      SSUri.toStr(circleUri));
      insertPars.put(SSSQLVarU.entityId,      SSUri.toStr(entityUri));
      
      dbSQL.insert(circleEntitiesTable, insertPars);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private Boolean hasCircleEntity(
    final SSUri circleUri,
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(circleUri, entityUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String> where = new HashMap<String, String>();
      
      where.put(SSSQLVarU.circleId, SSUri.toStr(circleUri));
      where.put(SSSQLVarU.entityId, SSUri.toStr(entityUri));
      
      resultSet = dbSQL.selectAllWhere(circleEntitiesTable, where);
      
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
      
      if(SSObjU.isNull(circleUri, userUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String> where = new HashMap<String, String>();
      
      where.put(SSSQLVarU.circleId, SSUri.toStr(circleUri));
      where.put(SSSQLVarU.userId,   SSUri.toStr(userUri));
      
      resultSet = dbSQL.selectAllWhere(circleUsersTable, where);
      
      return resultSet.first();
      
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  
  public SSUri createCircleURI() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objCircle().toString()));
  }
  
  private SSUri objCircle() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.circle.toString());
  } 
  
  public List<SSEntityRightTypeE> getCircleRights(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(circleUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String>             where  = new HashMap<String, String>();
      final List<SSEntityRightTypeE>  rights = new ArrayList<SSEntityRightTypeE>();
      
      where.put(SSSQLVarU.circleId,      SSUri.toStr(circleUri));
      
      resultSet = dbSQL.selectAllWhere(circleRightsTable, where);
      
      while(resultSet.next()){
        rights.add(SSEntityRightTypeE.get(bindingStr(resultSet, SSSQLVarU.accessRight)));
      }
      
      return rights;
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public List<SSUri> getCircleUserUris(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(circleUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String>   where    = new HashMap<String, String>();
      final List<SSUri>           userUris = new ArrayList<SSUri>();
      
      where.put(SSSQLVarU.circleId,      SSUri.toStr(circleUri));
      
      resultSet = dbSQL.selectAllWhere(circleUsersTable, where);
      
      while(resultSet.next()){
        userUris.add(bindingStrToUri(resultSet, SSSQLVarU.userId));
      }
      
      return userUris;
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public List<SSUri> getCircleEntityUris(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(circleUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String>   where       = new HashMap<String, String>();
      final List<SSUri>           entityUris  = new ArrayList<SSUri>();
      
      where.put(SSSQLVarU.circleId,      SSUri.toStr(circleUri));
      
      resultSet = dbSQL.selectAllWhere(circleEntitiesTable, where);
      
      while(resultSet.next()){
        entityUris.add(bindingStrToUri(resultSet, SSSQLVarU.entityId));
      }
      
      return entityUris;
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSUri> getEntityCircleURIs(
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(entityUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String>   where       = new HashMap<String, String>();
      final List<String>          columnNames = new ArrayList<String>();
      final List<SSUri>           circleUris  = new ArrayList<SSUri>();
      
      where.put(SSSQLVarU.entityId, SSUri.toStr(entityUri));
      
      columnNames.add(SSSQLVarU.circleId);

      resultSet = dbSQL.selectCertainDistinctWhere(circleEntitiesTable, columnNames, where);
      
      while(resultSet.next()){
        circleUris.add(bindingStrToUri(resultSet, SSSQLVarU.circleId));
      }
      
      return circleUris;
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public SSEntityCircleTypeE getCircleType(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(circleUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String>   where       = new HashMap<String, String>();
      
      where.put(SSSQLVarU.circleId,      SSUri.toStr(circleUri));
      
      resultSet = dbSQL.selectAllWhere(circleTable, where);

      resultSet.first();
      
      return SSEntityCircleTypeE.get(bindingStr(resultSet, SSSQLVarU.circleType));
      
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
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