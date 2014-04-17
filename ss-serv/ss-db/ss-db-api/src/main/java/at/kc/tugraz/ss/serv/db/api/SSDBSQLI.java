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
package at.kc.tugraz.ss.serv.db.api;

import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public interface SSDBSQLI{

  public Connection getConnection();
  
  public void       closeCon  () throws Exception;
  
  public Boolean rollBack(
    final SSServPar parA);

  public ResultSet selectCertainDistinctWhere(
    final String              tableName, 
    final List<String>        columns, 
    final Map<String, String> where) throws Exception;

  public ResultSet selectAllWhere(
    final String              table, 
    final Map<String, String> where) throws Exception;
  
  public void closeStmt(
    final ResultSet resultSet) throws Exception;
  
  public ResultSet selectCertainWhere(
    final List<String>        tableNames, 
    final List<String>        columnNames, 
    final Map<String, String> where, 
    final String              whereFixedRestriction) throws Exception;
  
  public ResultSet selectCertainDistinctWhere(
    final List<String>        tableNames, 
    final List<String>        columnNames, 
    final Map<String, String> whereParNamesWithValues, 
    final List<String>        whereFixedRestrictions) throws Exception;
  
  public ResultSet selectCertainWhereOrderBy(
    final List<String>        tableNames, 
    final List<String>        columnNames, 
    final Map<String, String> whereParNamesWithValues, 
    final String              whereFixedRestriction, 
    final String              orderByColumn, 
    final String              sortType) throws Exception;
  
  public void insert(
    final String              tableName, 
    final Map<String, String> insertPars) throws Exception;
  
  public void deleteWhere(
    final String              table, 
    final Map<String, String> where) throws Exception;
  
  public void deleteWhereIgnore(
    final String              table, 
    final Map<String, String> where) throws Exception;
  
  public void updateWhere(
    final String              table, 
    final Map<String, String> where, 
    final Map<String, String> values) throws Exception;
  
   public void updateWhereIgnore(
    final String              table, 
    final Map<String, String> where, 
    final Map<String, String> values) throws Exception;
  
  public void deleteAll(
    final String tableName) throws Exception;
 
  public ResultSet selectAll(
    final String tableName) throws Exception;
 
  public ResultSet selectAllWhereOrderBy(
    final String              tableName, 
    final Map<String, String> whereParNamesWithValues, 
    final String              orderByColumn, 
    final String              sortType) throws Exception;

  public void startTrans(
    final Boolean shouldCommit) throws Exception;
  
  public void commit(
    final Boolean shouldCommit) throws Exception;

  public void insertWhereNotExists(
    final String              table, 
    final Map<String, String> insert,
    final Map<String, String> uniqueKey) throws Exception;
}

//  public void       insert         (String tableName, Map<String, String> parNamesWithValues)  throws Exception;
//  public void       insertIgnore               (String tableName, Map<String, String> parNamesAndValues)   throws Exception;
//  public ResultSet  query                      (String query)                                                                                     throws Exception;