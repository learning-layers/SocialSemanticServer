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
package at.kc.tugraz.ss.serv.db.api;

import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;

public interface SSDBSQLI{

  public static final Integer entityLabelLength        = 255;
  
  public Integer    getMaxActive() throws Exception;
  public Integer    getActive()    throws Exception;
  public Connection getConnection();
  public void       closeCon  ()   throws Exception;
  
  public Boolean rollBack(
    final SSServPar parA);

  public ResultSet select(
    final String tableName) throws Exception;
  
  public ResultSet select(
    final String              table, 
    final Map<String, String> wheres) throws Exception;
  
  public ResultSet select(
    final String              table, 
    final List<String>        columns, 
    final Map<String, String> where) throws Exception;
  
  public ResultSet select(
    final String              table, 
    final List<String>        columns, 
    final List<String>        matches, 
    final List<String>        requireds,
    final List<String>        absents,
    final List<String>        eithers) throws Exception;
  
  public ResultSet select(
    final List<String>        tables, 
    final List<String>        columns, 
    final List<String>        tableConnections) throws Exception;
    
  public ResultSet select(
    final List<String>        tables, 
    final List<String>        columns, 
    final Map<String, String> wheres, 
    final List<String>        tableCons) throws Exception;

  public ResultSet select(
    final List<String>                         tables,
    final List<String>                         columns,
    final List<MultivaluedMap<String, String>> wheres,
    final List<String>                         tableCons) throws Exception;

    public ResultSet select(
    final List<String>        tables, 
    final List<String>        columns, 
    final Map<String, String> wheres, 
    final String              tableCon,
    final String              orderByColumn,
    final String              sortType) throws Exception;
  
  public ResultSet select(
    final String              tables, 
    final Map<String, String> wheres, 
    final String              orderByColumn, 
    final String              sortType) throws Exception;
  
  public void insert(
    final String              table, 
    final Map<String, String> inserts) throws Exception;
  
  public void insertIfNotExists(
    final String              table, 
    final Map<String, String> inserts,
    final Map<String, String> uniqueKeys) throws Exception;
  
  public void delete(
    final String table) throws Exception;
  
  public void delete(
    final String              table, 
    final Map<String, String> deletes) throws Exception;
  
  public void deleteIgnore(
    final String              table, 
    final Map<String, String> deletes) throws Exception;

  public void update(
    final String              table, 
    final Map<String, String> wheres, 
    final Map<String, String> values) throws Exception;
  
  public void updateIgnore(
    final String              table, 
    final Map<String, String> wheres, 
    final Map<String, String> updates) throws Exception;

  
  public void closeStmt(
    final ResultSet resultSet) throws Exception;
  
 
  public void startTrans(
    final Boolean shouldCommit) throws Exception;
  
  public void commit(
    final Boolean shouldCommit) throws Exception;
}

//  public void       insert         (String tableName, Map<String, String> parNamesWithValues)  throws Exception;
//  public void       insertIgnore               (String tableName, Map<String, String> parNamesAndValues)   throws Exception;
//  public ResultSet  query                      (String query)                                                                                     throws Exception;