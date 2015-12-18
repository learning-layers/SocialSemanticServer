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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;

public abstract class SSDBSQLFctA extends SSDBFctA{
  
  protected final SSDBSQLI dbSQL;
  
  public SSDBSQLFctA(final SSDBSQLI dbSQL){
    
    super();
    
    this.dbSQL = dbSQL;
  }
  
  protected static void insert(
    final Map<String, String> inserts,
    final String              key,
    final Object              value) throws SSErr{
    
    inserts.put(key, value.toString());
  }
  
  protected static void insert(
    final Map<String, String> inserts,
    final String              key,
    final Enum                value) throws SSErr{
    
    inserts.put(key, value.toString());
  }
  
  protected static void update(
    final Map<String, String> updates,
    final String              key,
    final String              value) throws SSErr{
    
    updates.put(key, value);
  }
  
  protected static void update(
    final Map<String, String> updates,
    final String              key,
    final Enum                value) throws SSErr{
    
    updates.put(key, value.toString());
  }
  
  protected static void update(
    final Map<String, String> updates,
    final String              key,
    final Number              value) throws SSErr{
    
    updates.put(key, value.toString());
  }
  
  protected static void update(
    final Map<String, String> updates,
    final String              key,
    final Boolean             value) throws SSErr{
    
    updates.put(key, value.toString());
  }
  
  protected static void update(
    final Map<String, String> updates,
    final String              key,
    final SSEntityA           value) throws SSErr{
    
    updates.put(key, value.toString());
  }
  
  protected static void uniqueKey(
    final Map<String, String> uniqueKeys,
    final String              key,
    final Enum                value) throws SSErr{
    
    uniqueKeys.put(key, value.toString());
  }
  
  protected static void uniqueKey(
    final Map<String, String> uniqueKeys,
    final String              key,
    final String              value) throws SSErr{
    
    uniqueKeys.put(key, value);
  }
  
  protected static void uniqueKey(
    final Map<String, String> uniqueKeys,
    final String              key,
    final SSEntityA           value) throws SSErr{
    
    uniqueKeys.put(key, value.toString());
  }
  
  protected static void match(
    final List<String> matches,
    final String       key) throws SSErr{
    
    matches.add(key);
  }
  
  protected static void where(
    final Map<String, String> wheres,
    final String              key,
    final Object              value) throws SSErr{
    
    wheres.put(key, value.toString());
  }
  
  protected static void where(
    final Map<String, String> wheres,
    final String              table,
    final String              key,
    final SSEntityA           value) throws SSErr{
    
    wheres.put(table + SSStrU.dot + key, value.toString());
  }
  
  protected static void where(
    final Map<String, String> wheres,
    final String              table,
    final String              key,
    final Object              value) throws SSErr{
    
    wheres.put(table + SSStrU.dot + key, value.toString());
  }
  
  protected static void where(
    final Map<String, String> wheres,
    final String              key,
    final Enum                value) throws SSErr{
    
    wheres.put(key, value.toString());
  }
  
  protected static void where(
    final Map<String, String> wheres,
    final String              table,
    final String              key,
    final Enum                value) throws SSErr{
    
    wheres.put(table + SSStrU.dot + key, value.toString());
  }
  
  protected static void where(
    final MultivaluedMap<String, String> wheres,
    final String                         table,
    final String                         key,
    final Object                         value) throws SSErr{
    
    wheres.add(table + SSStrU.dot + key, value.toString());
  }
  
  protected static void table(
    final List<String> tables,
    final String       table) throws SSErr{
    
    tables.add(table);
  }
  
  protected static void column(
    final List<String> columns,
    final String       column) throws SSErr{
    
    columns.add(column);
  }
  
  protected static void column(
    final List<String> columns,
    final String       table,
    final String       key) throws SSErr{
    
    columns.add(table + SSStrU.dot + key);
  }
  
  protected static void tableCon(
    final List<String> tableCons,
    final String       table1,
    final String       key1,
    final String       table2,
    final String       key2) throws SSErr{
    
    tableCons.add(table1 + SSStrU.dot + key1 + SSStrU.equal + table2 + SSStrU.dot + key2);
  }
  
  protected static void checkFirstResult(
    final ResultSet resultSet) throws SSErr{
    
    try{
      
      if(!resultSet.first()){
        throw SSErr.get(SSErrE.sqlNoResultFound);
      }
      
    }catch(SQLException sqlError){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, sqlError);
    }catch(SSErr error){
      SSServErrReg.regErrThrow(error, false);
    }
  }
  
  protected static Boolean existsFirstResult(
    final ResultSet resultSet) throws SSErr {
    
    try{
      return resultSet.first();
    }catch(SQLException error){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, error);
      return false;
    }
  }
  
  protected static List<String> getStringsFromResult(
    final ResultSet resultSet,
    final String    key) throws SSErr{
    
    try{
      final List<String> strings = new ArrayList<>();
      
      while(resultSet.next()){
        strings.add(bindingStr(resultSet, key));
      }
      
      return strings;
      
    }catch(SQLException error){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, error);
      return null;
    }
  }
  
  protected static List<SSTextComment> getTextCommentsFromResult(
    final ResultSet resultSet,
    final String    key) throws SSErr {
    
    try{
      final List<SSTextComment> textComments = new ArrayList<>();
      
      while(resultSet.next()){
        textComments.add(bindingStrToTextComment(resultSet, key));
      }
      
      return textComments;
    }catch(SQLException error){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, error);
      return null;
    }
  }
  
  protected static List<SSUri> getURIsFromResult(
    final ResultSet resultSet,
    final String    key) throws SSErr {
    
    try{
      final List<SSUri> uris = new ArrayList<>();
      
      while(resultSet.next()){
        uris.add(bindingStrToUri(resultSet, key));
      }
      
      return uris;
    }catch(SQLException error){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, error);
      return null;
    }
  }
  
  protected static List<SSEntity> getEntitiesFromResult(
    final ResultSet resultSet,
    final String    key) throws SSErr {
    
    try{
      final List<SSEntity> entities = new ArrayList<>();
      
      while(resultSet.next()){
        
        entities.add(
          SSEntity.get(
            bindingStrToUri(resultSet, key),
            SSEntityE.entity));
      }
      
      return entities;
      
    }catch(SQLException error){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, error);
      return null;
    }
  }
  
  protected static SSEntity getEntityFromResult(
    final ResultSet resultSet,
    final String    key) throws SSErr {
    
    return SSEntity.get(
      bindingStrToUri(resultSet, key),
      SSEntityE.entity);
  }
  
  protected static SSAuthor bindingStrToAuthor(
    final ResultSet resultSet,
    final String    binding) throws SSErr {
    
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
    final SSEntityE type) throws SSErr {
    
    final String tmp = bindingStr(resultSet, binding);
    
    if(SSStrU.isEmpty(tmp)){
      return null;
    }
    
    return SSEntity.get(SSUri.get(tmp), type);
  }
  
  protected static SSUri bindingStrToUri(
    final ResultSet resultSet,
    final String    binding) throws SSErr {
    
    final String tmp = bindingStr(resultSet, binding);
    
    if(SSStrU.isEmpty(tmp)){
      return null;
    }
    
    return SSUri.get(tmp);
  }
  
  protected static SSSpaceE bindingStrToSpace(
    final ResultSet resultSet,
    final String    binding) throws SSErr {
    
    return SSSpaceE.get(bindingStr(resultSet, binding));
  }
  
  protected static String bindingStr(
    final ResultSet resultSet,
    final String    binding) throws SSErr{
    
    try{
      return resultSet.getString(binding);
      
    }catch(SQLException error){
      SSServErrReg.regErrThrow(SSErrE.sqlDefaultErr, error);
      return null;
    }
  }
  
  protected static SSLabel bindingStrToLabel(
    final ResultSet resultSet,
    final String    binding) throws SSErr{
    
    return SSLabel.get(bindingStr(resultSet, binding));
  }
  
  protected static Boolean bindingStrToBoolean(
    final ResultSet resultSet,
    final String    binding) throws SSErr{
    
    return Boolean.valueOf(bindingStr(resultSet, binding));
  }
  
  protected static SSTextComment bindingStrToTextComment(
    final ResultSet resultSet,
    final String    binding) throws SSErr{
    
    return SSTextComment.get(bindingStr(resultSet, binding));
  }
  
  protected static SSEntityE bindingStrToEntityType(
    final ResultSet resultSet,
    final String    binding) throws SSErr{
    
    return SSEntityE.get(bindingStr(resultSet, binding));
  }
  
  protected static Double bindingStrToDouble(
    final ResultSet resultSet,
    final String    binding) throws SSErr {
    
    return Double.parseDouble(bindingStr(resultSet, binding));
  }
  
  protected static Float bindingStrToFloat(
    final ResultSet resultSet,
    final String    binding) throws SSErr {
    
    return Float.parseFloat(bindingStr(resultSet, binding));
  }
  
  protected static Integer bindingStrToInteger(
    final ResultSet resultSet,
    final String    binding) throws SSErr{
    
    return Integer.parseInt(bindingStr(resultSet, binding));
  }
  
  protected static Long bindingStrToLong(
    final ResultSet resultSet,
    final String    binding) throws SSErr{
    
    return Long.parseLong(bindingStr(resultSet, binding));
  }
}