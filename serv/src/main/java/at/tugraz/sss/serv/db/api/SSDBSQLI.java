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
package at.tugraz.sss.serv.db.api;

import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.par.SSDBSQLSelectPar;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;

public interface SSDBSQLI{

  public static final Integer entityLabelLength        = 255;
  
  public Integer    getMaxActive();
  public Integer    getActive();
  public Connection getConnection();
  public void       closeCon  ()   throws SSErr;
  
  public Boolean rollBack(
    final Boolean shouldCommit);

  public ResultSet select(
    final String query) throws SSErr;
  
  public ResultSet select(
    final SSDBSQLSelectPar par) throws SSErr;
  
  public ResultSet select(
    final List<String>        tables, 
    final List<String>        columns, 
    final Map<String, String> wheres,
    final List<String>        tableCons,
    final String              orderByColumn, 
    final String              sortType, 
    final Integer             limit) throws SSErr;
  
  public ResultSet select(
    final String              table, 
    final List<String>        columns, 
    final Map<String, String> wheres,
    final String              orderByColumn, 
    final String              sortType, 
    final Integer             limit) throws SSErr;
  
  public ResultSet selectLike(
    final List<String>                         tables,
    final List<String>                         columns,
    final List<MultivaluedMap<String, String>> likes,
    final List<String>                         tableCons,
    final String                               orderByColumn, 
    final String                               sortType, 
    final Integer                              limit) throws SSErr;
  
  public void insert(
    final String              table, 
    final Map<String, String> inserts) throws SSErr;
  
  public void insertIfNotExists(
    final String              table, 
    final Map<String, String> inserts,
    final Map<String, String> uniqueKeys) throws SSErr;
  
  public void delete(
    final String table) throws Exception;
  
  public void delete(
    final String              table, 
    final Map<String, String> wheres) throws SSErr;
  
  public void deleteIgnore(
    final String              table, 
    final Map<String, String> wheres) throws SSErr;

  public void deleteIgnore(
    final String                               table, 
    final List<MultivaluedMap<String, String>> wheres) throws SSErr;
  
  public void update(
    final String              table, 
    final Map<String, String> wheres, 
    final Map<String, String> updates) throws SSErr;
  
  public void updateIgnore(
    final String              table, 
    final Map<String, String> wheres, 
    final Map<String, String> updates) throws SSErr;
  
  public void closeStmt(
    final ResultSet resultSet) throws SSErr;
 
  public void startTrans(
    final Boolean shouldCommit) throws SSErr;
  
  public void commit(
    final Boolean shouldCommit) throws SSErr;
}

//  public void       insert         (String tableName, Map<String, String> parNamesWithValues)  throws Exception;
//  public void       insertIgnore               (String tableName, Map<String, String> parNamesAndValues)   throws Exception;
//  public ResultSet  query                      (String query)                                                                                     throws Exception;


    
//  public ResultSet select(
//    final List<String>        tables, 
//    final List<String>        columns, 
//    final Map<String, String> wheres, 
//    final List<String>        tableCons) throws Exception;

//  public ResultSet select(
//    final String tableName) throws Exception;

//  public ResultSet select(
//    final String              table, 
//    final Map<String, String> wheres) throws Exception;

//  public ResultSet select(
//    final String              tables, 
//    final Map<String, String> wheres, 
//    final String              orderByColumn, 
//    final String              sortType) throws Exception;


//  public ResultSet select(
//    final List<String>        tables, 
//    final List<String>        columns, 
//    final Map<String, String> wheres, 
//    final String              tableCon,
//    final String              orderByColumn,
//    final String              sortType) throws Exception;




//  public ResultSet select(
//    final List<String>                         tables,
//    final List<String>                         columns,
//    final List<MultivaluedMap<String, String>> orWheres,
//    final List<MultivaluedMap<String, String>> andWheres,
//    final List<String>                         tableCons,
//    final String                               globalSearchOp,
//    final String                               localSearchOp,
//    final String                               orderByColumn,
//    final String                               sortType, 
//    final Integer                              limit) throws Exception;
  
//  public ResultSet selectWithNumerics(
//    final List<String>                                           tables,
//    final List<String>                                           columns,
//    final List<MultivaluedMap<String, String>>                   wheres,
//    final MultivaluedMap<String, MultivaluedMap<String, String>> wheresNumbericComparision,
//    final List<String>                                           tableCons,
//    final String                                                 orderByColumn, 
//    final String                                                 sortType, 
//    final Integer                                                limit) throws Exception;