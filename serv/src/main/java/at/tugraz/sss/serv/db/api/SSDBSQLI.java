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

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.*;
import java.sql.*;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;

public interface SSDBSQLI{

  public static final Integer entityLabelLength        = 255;

  public Connection createConnection() throws SSErr;
  public Integer    getMaxActive();
  public Integer    getActive();
  public void       closeCon  (final SSServPar servPar)   throws SSErr;
  
  public boolean rollBack(
    final SSServPar servPar,
    final boolean    shouldCommit) throws SSErr;

  public ResultSet select(
    final SSServPar servPar,
    final String query) throws SSErr;
  
  public ResultSet select(
    final SSDBSQLSelectPar par) throws SSErr;
  
  public ResultSet select(
    final SSServPar           servPar,
    final List<SSSQLTableI>   tables, 
    final List<String>        columns, 
    final Map<String, String> wheres,
    final List<String>        tableCons,
    final String              orderByColumn, 
    final String              sortType, 
    final Integer             limit) throws SSErr;
  
  public ResultSet select(
    final SSServPar           servPar,
    final SSSQLTableI         table, 
    final List<String>        columns, 
    final Map<String, String> wheres,
    final String              orderByColumn, 
    final String              sortType, 
    final Integer             limit) throws SSErr;
  
  public ResultSet selectLike(
    final SSServPar servPar,
    final List<SSSQLTableI>                    tables,
    final List<String>                         columns,
    final List<MultivaluedMap<String, String>> likes,
    final List<String>                         tableCons,
    final String                               orderByColumn, 
    final String                               sortType, 
    final Integer                              limit) throws SSErr;
  
  public void insert(
    final SSServPar servPar,
    final SSSQLTableI              table, 
    final Map<String, String> inserts) throws SSErr;
  
  public void insertIfNotExists(
    final SSServPar servPar,
    final SSSQLTableI              table, 
    final Map<String, String> inserts,
    final Map<String, String> uniqueKeys) throws SSErr;
  
  public void delete(
    final SSServPar   servPar,
    final SSSQLTableI table) throws SSErr;
  
  public void delete(
    final SSServPar servPar,
    final SSSQLTableI              table, 
    final Map<String, String> wheres) throws SSErr;
  
  public void deleteIgnore(
    final SSServPar           servPar,
    final SSSQLTableI         table, 
    final Map<String, String> wheres) throws SSErr;

  public void deleteIgnore(
    final SSServPar                            servPar,
    final SSSQLTableI                          table, 
    final List<MultivaluedMap<String, String>> wheres) throws SSErr;
  
  public void update(
    final SSServPar servPar,
    final SSSQLTableI              table, 
    final Map<String, String> wheres, 
    final Map<String, String> updates) throws SSErr;
  
  public void updateIgnore(
    final SSServPar servPar,
    final SSSQLTableI              table, 
    final Map<String, String> wheres, 
    final Map<String, String> updates) throws SSErr;
  
  public void closeStmt(
    final ResultSet resultSet) throws SSErr;
 
  public void startTrans(
    final SSServPar  servPar,
    final boolean    shouldCommit) throws SSErr;
  
  public void commit(
    final SSServPar  servPar,
    final boolean    shouldCommit) throws SSErr;
}

//  public void       insert         (String tableName, Map<String, String> parNamesWithValues)  throws SSErr;
//  public void       insertIgnore               (String tableName, Map<String, String> parNamesAndValues)   throws SSErr;
//  public ResultSet  query                      (String query)                                                                                     throws SSErr;


    
//  public ResultSet select(
//    final List<String>        tables, 
//    final List<String>        columns, 
//    final Map<String, String> wheres, 
//    final List<String>        tableCons) throws SSErr;

//  public ResultSet select(
//    final String tableName) throws SSErr;

//  public ResultSet select(
//    final String              table, 
//    final Map<String, String> wheres) throws SSErr;

//  public ResultSet select(
//    final String              tables, 
//    final Map<String, String> wheres, 
//    final String              orderByColumn, 
//    final String              sortType) throws SSErr;


//  public ResultSet select(
//    final List<String>        tables, 
//    final List<String>        columns, 
//    final Map<String, String> wheres, 
//    final String              tableCon,
//    final String              orderByColumn,
//    final String              sortType) throws SSErr;




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
//    final Integer                              limit) throws SSErr;
  
//  public ResultSet selectWithNumerics(
//    final List<String>                                           tables,
//    final List<String>                                           columns,
//    final List<MultivaluedMap<String, String>>                   wheres,
//    final MultivaluedMap<String, MultivaluedMap<String, String>> wheresNumbericComparision,
//    final List<String>                                           tableCons,
//    final String                                                 orderByColumn, 
//    final String                                                 sortType, 
//    final Integer                                                limit) throws SSErr;