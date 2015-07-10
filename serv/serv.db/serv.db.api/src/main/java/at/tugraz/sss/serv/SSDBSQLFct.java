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
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;

public class SSDBSQLFct extends SSDBFct{

  protected        final SSDBSQLI dbSQL;
  
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
    final SSEntityA           value) throws Exception{
    
    try{
      updates.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void delete(
    final Map<String, String> deletes,
    final String              key,
    final SSEntityA           value) throws Exception{
    
    try{
      deletes.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void delete(
    final Map<String, String> deletes,
    final String              key,
    final Enum                value) throws Exception{
    
    try{
      deletes.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void delete(
    final Map<String, String> deletes,
    final String              key,
    final String              value) throws Exception{
    
    try{
      deletes.put(key, value);
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
    final ResultSet result) throws Exception{
    
    try{
      
      if(!result.first()){
        throw new SSErr(SSErrE.sqlNoResultFound);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error, false);
    }
  }
  
  protected static List<String> getStringsFromResult(
    final ResultSet result,
    final String    key) throws Exception{
    
    final List<String> strings = new ArrayList<>();
    
    while(result.next()){
      strings.add(bindingStr(result, key));
    }
    
    return strings;
  }
  
  protected static List<SSTextComment> getTextCommentsFromResult(
    final ResultSet result,
    final String    key) throws Exception{
    
    final List<SSTextComment> textComments = new ArrayList<>();
    
    while(result.next()){
      textComments.add(bindingStrToTextComment(result, key));
    }
    
    return textComments;
  }
  
  protected static List<SSUri> getURIsFromResult(
    final ResultSet result,
    final String    key) throws Exception{
    
    final List<SSUri> uris = new ArrayList<>();
    
    while(result.next()){
      uris.add(bindingStrToUri(result, key));
    }
    
    return uris;
  }
  
  protected static SSAuthor bindingStrToAuthor(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    final String tmp = bindingStr(resultSet, binding);
    
    if(SSStrU.isEmpty(tmp)){
      return null;
    }
    
    return SSAuthor.get(SSUri.get(tmp));
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
}
