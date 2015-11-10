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
package at.tugraz.sss.serv;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class SSDBSQLFct extends SSDBFct{

  protected final SSDBSQLI dbSQL;
  
  public SSDBSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super();
    
    this.dbSQL = dbSQL;
  }
  
  protected static void insert(
    final Map<String, String> inserts,
    final String              key,
    final Object              value) throws Exception{
    
    try{
      inserts.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void insert(
    final Map<String, String> inserts,
    final String              key,
    final Enum                value) throws Exception{
    
    try{
      inserts.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void update(
    final Map<String, String> updates,
    final String              key,
    final String              value) throws Exception{
    
    try{
      updates.put(key, value);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void update(
    final Map<String, String> updates,
    final String              key,
    final Enum                value) throws Exception{
    
    try{
      updates.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void update(
    final Map<String, String> updates,
    final String              key,
    final Number              value) throws Exception{
    
    try{
      updates.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
   
  protected static void update(
    final Map<String, String> updates,
    final String              key,
    final Boolean             value) throws Exception{
    
    try{
      updates.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
    
  protected static void update(
    final Map<String, String> updates,
    final String              key,
    final SSEntityA           value) throws Exception{
    
    try{
      updates.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void uniqueKey(
    final Map<String, String> uniqueKeys,
    final String              key,
    final Enum                value) throws Exception{
    
    try{
      uniqueKeys.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void uniqueKey(
    final Map<String, String> uniqueKeys,
    final String              key,
    final String              value) throws Exception{
    
    try{
      uniqueKeys.put(key, value);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void uniqueKey(
    final Map<String, String> uniqueKeys,
    final String              key,
    final SSEntityA           value) throws Exception{
    
    try{
      uniqueKeys.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
   protected static void match(
     final List<String> matches, 
     final String       key) throws Exception{
     
     try{
       matches.add(key);
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
   }
   
  protected static void where(
    final Map<String, String> wheres,
    final String              key,
    final Object              value) throws Exception{
    
    try{
      wheres.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void where(
    final Map<String, String> wheres,
    final String              table, 
    final String              key,
    final SSEntityA           value) throws Exception{
    
    try{
      wheres.put(table + SSStrU.dot + key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void where(
    final Map<String, String> wheres,
    final String              table, 
    final String              key,
    final Object              value) throws Exception{
    
    try{
      wheres.put(table + SSStrU.dot + key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void where(
    final Map<String, String> wheres,
    final String              key,
    final Enum                value) throws Exception{
    
    try{
      wheres.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void where(
    final Map<String, String> wheres,
    final String              table,
    final String              key,
    final Enum                value) throws Exception{
    
    try{
      wheres.put(table + SSStrU.dot + key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void where(
    final MultivaluedMap<String, String> wheres,
    final String                         table,
    final String                         key,
    final Object                         value) throws Exception{
    
    try{
      wheres.add(table + SSStrU.dot + key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void table(
    final List<String> tables,
    final String       table) throws Exception{
    
    try{
      tables.add(table);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void column(
    final List<String> columns,
    final String       column) throws Exception{
    
    try{
      columns.add(column);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void column(
    final List<String> columns, 
    final String       table, 
    final String       key) throws Exception{
    
    try{
      columns.add(table + SSStrU.dot + key);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void tableCon(
    final List<String> tableCons,
    final String       table1, 
    final String       key1, 
    final String       table2, 
    final String       key2) throws Exception{
    
    try{
      tableCons.add(table1 + SSStrU.dot + key1 + SSStrU.equal + table2 + SSStrU.dot + key2);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void checkFirstResult(
    final ResultSet resultSet) throws Exception{
    
    try{
      
      if(!resultSet.first()){
        throw new SSErr(SSErrE.sqlNoResultFound);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error, false);
    }
  }
  
  protected static Boolean existsFirstResult(
    final ResultSet resultSet) throws Exception{
    
    return resultSet.first();
  }
  
  protected static List<String> getStringsFromResult(
    final ResultSet resultSet,
    final String    key) throws Exception{
    
    final List<String> strings = new ArrayList<>();
    
    while(resultSet.next()){
      strings.add(bindingStr(resultSet, key));
    }
    
    return strings;
  }
  
  protected static List<SSTextComment> getTextCommentsFromResult(
    final ResultSet resultSet,
    final String    key) throws Exception{
    
    final List<SSTextComment> textComments = new ArrayList<>();
    
    while(resultSet.next()){
      textComments.add(bindingStrToTextComment(resultSet, key));
    }
    
    return textComments;
  }
  
  protected static List<SSUri> getURIsFromResult(
    final ResultSet resultSet,
    final String    key) throws Exception{
    
    final List<SSUri> uris = new ArrayList<>();
    
    while(resultSet.next()){
      uris.add(bindingStrToUri(resultSet, key));
    }
    
    return uris;
  }
  
  protected static List<SSEntity> getEntitiesFromResult(
    final ResultSet resultSet,
    final String    key) throws Exception{
    
    final List<SSEntity> entities = new ArrayList<>();
    
    while(resultSet.next()){
      
      entities.add(
        SSEntity.get(
          bindingStrToUri(resultSet, key), 
          SSEntityE.entity));
    }
    
    return entities;
  }
  
  protected static SSEntity getEntityFromResult(
    final ResultSet resultSet,
    final String    key) throws Exception{
    
    return SSEntity.get(
      bindingStrToUri(resultSet, key),
      SSEntityE.entity);
  }
  
  protected static SSAuthor bindingStrToAuthor(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    final String tmp = bindingStr(resultSet, binding);
    
    if(SSStrU.isEmpty(tmp)){
      return null;
    }
    
    final SSAuthor author = SSAuthor.get(SSUri.get(tmp));
    
    author.author = SSAuthor.get(SSUri.get("http://sss.eu/system"), SSLabel.get("system"));
    
    return author;
  }
  
  protected static SSEntity bindingStrToEntity(
    final ResultSet resultSet, 
    final String    binding,
    final SSEntityE type) throws Exception{
    
    final String tmp = bindingStr(resultSet, binding);
    
    if(SSStrU.isEmpty(tmp)){
      return null;
    }
    
    return SSEntity.get(SSUri.get(tmp), type);
  }
    
  protected static SSUri bindingStrToUri(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    final String tmp = bindingStr(resultSet, binding);
    
    if(SSStrU.isEmpty(tmp)){
      return null;
    }
    
    return SSUri.get(tmp);
  }
  
  protected static SSSpaceE bindingStrToSpace(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return SSSpaceE.get(bindingStr(resultSet, binding));
  }
  
  protected static String bindingStr(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return resultSet.getString(binding);
  }
  
  protected static SSLabel bindingStrToLabel(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return SSLabel.get(bindingStr(resultSet, binding));
  }
  
  protected static Boolean bindingStrToBoolean(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return Boolean.valueOf(bindingStr(resultSet, binding));
  }
  
  protected static SSTextComment bindingStrToTextComment(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return SSTextComment.get(bindingStr(resultSet, binding));
  }
  
  protected static SSEntityE bindingStrToEntityType(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return SSEntityE.valueOf(bindingStr(resultSet, binding));
  }
  
  protected static Double bindingStrToDouble(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return Double.parseDouble(bindingStr(resultSet, binding));
  }
  
  protected static Float bindingStrToFloat(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return Float.parseFloat(bindingStr(resultSet, binding));
  }
  
  protected static  Integer bindingStrToInteger(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return Integer.parseInt(bindingStr(resultSet, binding));
  }
  
  protected static Long bindingStrToLong(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return Long.parseLong(bindingStr(resultSet, binding));
  }
  
  protected static void setEntityColumns(
    final List<String> columns) throws Exception{
    
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
  
  protected static void setEntityTable(
    final List<String> tables) throws Exception{
    
    try{
      table(tables, SSSQLVarNames.entityTable);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Boolean existsEntity(
    final SSUri entity) throws Exception{
    
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
      dbSQL.closeStmt(resultSet);
    }
  }
  
//  public SSEntity getEntity(
//    final SSUri entityUri) throws Exception{
//    
//    ResultSet resultSet  = null;
//    
//    try{
//      final List<String>        columns = new ArrayList<>();
//      final Map<String, String> where   = new HashMap<>();
//      
//      setEntityColumns(columns);
//      
//      where(where, SSSQLVarNames.id, entityUri);
//      
//      resultSet = 
//        dbSQL.select(
//          SSSQLVarNames.entityTable, 
//          columns, 
//          where, 
//          null, 
//          null, 
//          null);
//      
//      checkFirstResult(resultSet);
//      
//      return SSEntity.get(
//        bindingStrToUri        (resultSet, SSSQLVarNames.id),
//        bindingStrToEntityType (resultSet, SSSQLVarNames.type),
//        bindingStrToLabel      (resultSet, SSSQLVarNames.label),
//        bindingStrToTextComment(resultSet, SSSQLVarNames.description),
//        bindingStrToLong       (resultSet, SSSQLVarNames.creationTime),
//        bindingStrToAuthor     (resultSet, SSSQLVarNames.author));
//          
//    }catch(Exception error){
//      
//      if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
//        SSServErrReg.reset();
//        return null;
//      }
//        
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
  
  public SSEntity getEntityTest(
    final SSUri   user,
    final SSUri   entity,
    final Boolean withUserRestriction) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      if(
        user == null &&
        withUserRestriction){
        
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(entity == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final String entityStr = entity.toString();
      final String userStr   = SSStrU.toStr(user);
      String       query;
      
      if(
        withUserRestriction &&
        !SSStrU.equals(userStr, "http://sss.eu/system/")){
        
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
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getAccessibleURIs(
    final SSUri user) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      if(
        user == null ||
        SSStrU.equals(user, "http://sss.eu/system/")){
        
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final String userStr = SSStrU.toStr(user);
      String       query;
      
      query = "select DISTINCT id from entity where ";
      query += "(type = 'entity') OR ";
      query += "(type = 'circle' AND id IN (select DISTINCT circle.circleId from circle, circleusers where circleusers.userId = '" + userStr + "' and circle.circleId = circleusers.circleId)) OR ";
      query += "(type != 'entity' AND type != 'circle' AND id IN (select DISTINCT circleentities.entityId from circle, circleentities, circleusers where userId = '" + userStr + "' and circle.circleId = circleentities.circleId and circle.circleId = circleusers.circleId))";
      
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
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getEntityURIsUserCanAccess(
    final SSUri           user,
    final Boolean         withSystemCircles,
    final List<SSUri>     entities,
    final List<SSEntityE> types,
    final List<SSUri>     authors) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
     
      if(user == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final List<MultivaluedMap<String, String>> wheres         = new ArrayList<>();
      final List<String>                         tables         = new ArrayList<>();
      final List<String>                         columns        = new ArrayList<>();
      final List<String>                         tableCons      = new ArrayList<>();
      
      column(columns, SSSQLVarNames.circleEntitiesTable,   SSSQLVarNames.entityId);
      
      table    (tables, SSSQLVarNames.circleUsersTable);
      table    (tables, SSSQLVarNames.circleEntitiesTable);
      table    (tables, SSSQLVarNames.entityTable);
      
      tableCon (tableCons, SSSQLVarNames.circleUsersTable,    SSSQLVarNames.circleId, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.circleId);
      tableCon (tableCons, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.entityId, SSSQLVarNames.entityTable,         SSSQLVarNames.id);
      
      final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
      
      where(whereUsers, SSSQLVarNames.circleUsersTable, SSSQLVarNames.userId, user);
      
      wheres.add(whereUsers);
      
      if(
        withSystemCircles != null &&
        !withSystemCircles){
        
        table    (tables, SSSQLVarNames.circleTable);
        tableCon (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleUsersTable,         SSSQLVarNames.circleId);
        
        final MultivaluedMap<String, String> whereIsSystemCircles = new MultivaluedHashMap<>();
        
        where(whereIsSystemCircles, SSSQLVarNames.circleTable, SSSQLVarNames.isSystemCircle, withSystemCircles);
        
        wheres.add(whereIsSystemCircles);
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
      
      return getURIsFromResult(resultSet, SSSQLVarNames.entityId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public Boolean isUserAuthor(
    final SSUri   user, 
    final SSUri   entityURI,
    final Boolean withUserRestriction) throws Exception{
    
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
  
  public void removeEntityFromCircle(
    final SSUri circle,
    final SSUri entity) throws Exception{
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.circleId, circle);
      where(wheres, SSSQLVarNames.entityId, entity);
      
      dbSQL.delete(SSSQLVarNames.circleEntitiesTable, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}