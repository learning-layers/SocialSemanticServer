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
package at.kc.tugraz.ss.serv.db.impl;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.db.conf.SSDBSQLConf;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplDBA;
import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLTransactionRollbackException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class SSDBSQLMySQLImpl extends SSServImplDBA implements SSDBSQLI{
  
  public         Connection connector                = null;
  private static DataSource connectionPool           = null;
  private        Boolean    gotCon                   = false;
  private        Integer    numberTimesTriedToGetCon = 0;
  
  public Connection getConnection(){
    return connector;
  }
  
  public SSDBSQLMySQLImpl(final SSServConfA conf) throws Exception{
    
    super(conf);
    
    connectToMYSQL();
  }
  
  @Override
  protected void finalizeImpl() throws Exception{
    
    try{
      closeCon();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void startTrans(
    Boolean shouldCommit) throws Exception{
    
    if(!shouldCommit){
      return;
    }
    
    if(connector == null){
      SSLogU.warn("con null");
      return;
    }
    
    try{
      connector.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
      connector.setAutoCommit(false);
    }catch(SQLException error){
      SSServErrReg.regErrThrow(error);
    }
  }
  @Override
  public void closeCon() throws Exception{
    
    try{
      if(connector == null){
        return;
      }
      
      if(connector.isClosed()){
        connector = null;
        return;
      }

      connector.close();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
    
  @Override
  public void closeStmt(final ResultSet resultSet) throws Exception{
    
    try{
      
      if(
        resultSet                == null ||
        resultSet.isClosed()             ||
        resultSet.getStatement() == null ||
        resultSet.getStatement().isClosed()){
        return;
      }
      
      resultSet.getStatement().close();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public Boolean rollBack(final SSServPar parA){
    
    try{
      
      if(
        !parA.shouldCommit        ||
        connector == null         ||
        connector.getAutoCommit() ||
        connector.isClosed()){
        return false;
      }
      
      connector.rollback();
      
      if(parA.tryAgain){
        parA.tryAgain = false;
        return true;
      }
      
      return false;
    }catch(Exception error){
      SSServErrReg.regErr(error);
      return null;
    }
  }
  
  @Override
  public void commit(Boolean shouldCommit) throws Exception{
    
    if(!shouldCommit){
      return;
    }
    
    try{
      connector.commit();
      connector.setAutoCommit(true);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public ResultSet selectAll(
    String tableName) throws Exception{
    
    if(SSStrU.isEmpty(tableName)){
      SSServErrReg.regErrThrow(new Exception("table not spec"));
    }
    
    return connector.prepareStatement("SELECT * FROM " + tableName).executeQuery();
  }
  
  @Override
  public ResultSet selectAllWhere(
    String              tableName, 
    Map<String, String> whereParNamesWithValues) throws Exception{
    
    if(
      SSStrU.isEmpty(tableName) ||
      whereParNamesWithValues == null){
      
      SSServErrReg.regErrThrow(new Exception("table not spec or pars null"));
      return null;
    }
    
    String                              query   = "SELECT * FROM "; //wiki_revision_test WHERE UserID = ? AND Timestamp = ?";
    int                                 counter = 1;
    PreparedStatement                   stmt;
    Iterator<Map.Entry<String, String>> iterator;
    
    query        += tableName + " WHERE ";
    iterator      = whereParNamesWithValues.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
    }
    
    query          = SSStrU.removeTrailingString(query, " AND ");
    stmt           = connector.prepareStatement(query);
    iterator       = whereParNamesWithValues.entrySet().iterator();
    
    while(iterator.hasNext()){
      stmt.setObject(counter++, iterator.next().getValue());
    }
    
    return stmt.executeQuery();
  }
  
  @Override
  public ResultSet selectCertainWhere(
    List<String>        tableNames, 
    List<String>        columnNames, 
    Map<String, String> whereParNamesWithValues, 
    String              whereFixedRestriction) throws Exception{
    
    if(
      SSObjU.isNull(tableNames, columnNames, whereParNamesWithValues) ||
      tableNames.size()              <= 0    ||
      columnNames.size()             <= 0    ||
      SSStrU.isEmpty(whereFixedRestriction)){
      
      SSServErrReg.regErrThrow(new Exception("table not spec or pars null"));
      return null;
    }
    
    String                              query   = "SELECT "; // + "* FROM "; //wiki_revision_test WHERE UserID = ? AND Timestamp = ?";
    int                                 counter = 1;
    PreparedStatement                   stmt;
    Iterator<Map.Entry<String, String>> iterator;
    
    for(String columnName : columnNames){
      query += columnName + ",";
    }
    
    query          = SSStrU.removeTrailingString(query, ",");
    query         += " FROM ";
    
    for(String tableName : tableNames){
      query += tableName + ",";
    }
    
    query         = SSStrU.removeTrailingString(query, ",");
    query         += " WHERE ";
    iterator      = whereParNamesWithValues.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
    }
    
    query         += whereFixedRestriction;
    stmt           = connector.prepareStatement(query);
    iterator       = whereParNamesWithValues.entrySet().iterator();
    
    while(iterator.hasNext()){
      stmt.setObject(counter++, iterator.next().getValue());
    }

    return stmt.executeQuery();
  }
  
  @Override
  public ResultSet selectCertainDistinctWhere(
    List<String>        tableNames, 
    List<String>        columnNames, 
    Map<String, String> whereParNamesWithValues, 
    List<String>        whereFixedRestrictions) throws Exception{
    
    if(
      SSObjU.isNull(tableNames, columnNames, whereParNamesWithValues, whereFixedRestrictions) ||
      tableNames.size()              <= 0    ||
      columnNames.size()             <= 0    ||
      whereFixedRestrictions.size()  <= 0){
      
      SSServErrReg.regErrThrow(new Exception("table not spec or pars null"));
      return null;
    }
    
    String                              query   = "SELECT DISTINCT "; // + "* FROM "; //wiki_revision_test WHERE UserID = ? AND Timestamp = ?";
    int                                 counter = 1;
    PreparedStatement                   stmt;
    Iterator<Map.Entry<String, String>> iterator;
    
    for(String columnName : columnNames){
      query += columnName + ",";
    }
    
    query          = SSStrU.removeTrailingString(query, ",");
    query         += " FROM ";
    
    for(String tableName : tableNames){
      query += tableName + ",";
    }
    
    query         = SSStrU.removeTrailingString(query, ",");
    query         += " WHERE ";
    iterator      = whereParNamesWithValues.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
    }
    
    for(String whereFixedRestriction : whereFixedRestrictions){
      query         += whereFixedRestriction + " AND ";
    }
    
    query         = SSStrU.removeTrailingString(query, " AND ");
    stmt           = connector.prepareStatement(query);
    iterator       = whereParNamesWithValues.entrySet().iterator();
    
    while(iterator.hasNext()){
      stmt.setObject(counter++, iterator.next().getValue());
    }

    return stmt.executeQuery();
  }
  
  @Override
  public ResultSet selectCertainWhereOrderBy(
    List<String>        tableNames, 
    List<String>        columnNames, 
    Map<String, String> whereParNamesWithValues, 
    String              whereFixedRestriction,
    String              orderByColumn, 
    String              sortType) throws Exception{
    
    if(
      tableNames                     == null ||
      tableNames.size()              <= 0    ||
      columnNames                    == null ||
      columnNames.size()             <= 0    ||
      whereParNamesWithValues        == null ||
      whereParNamesWithValues.size() <= 0    ||
      SSStrU.isEmpty(whereFixedRestriction, orderByColumn, sortType)){
      
      SSServErrReg.regErrThrow(new Exception("table not spec or pars null"));
      return null;
    }
    
    String                              query   = "SELECT "; // + "* FROM "; //wiki_revision_test WHERE UserID = ? AND Timestamp = ?";
    int                                 counter = 1;
    PreparedStatement                   stmt;
    Iterator<Map.Entry<String, String>> iterator;
    
    for(String columnName : columnNames){
      query += columnName + ",";
    }
    
    query          = SSStrU.removeTrailingString(query, ",");
    query         += " FROM ";
    
    for(String tableName : tableNames){
      query += tableName + ",";
    }
    
    query         = SSStrU.removeTrailingString(query, ",");
    query         += " WHERE ";
    iterator      = whereParNamesWithValues.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
    }
    
    query         += whereFixedRestriction;
    query         += " ORDER BY " + orderByColumn + SSStrU.blank + sortType;
    stmt           = connector.prepareStatement(query);
    iterator       = whereParNamesWithValues.entrySet().iterator();
    
    while(iterator.hasNext()){
      stmt.setObject(counter++, iterator.next().getValue());
    }

    return stmt.executeQuery();
  }
  
  @Override
  public ResultSet selectCertainDistinctWhere(
    String              tableName, 
    List<String>        columnNames, 
    Map<String, String> wherePars) throws Exception{
    
    if(
      SSStrU.isEmpty   (tableName)    ||
      columnNames == null   ||
      wherePars   == null){
      SSServErrReg.regErrThrow(new Exception("table not spec, certain or pars null"));
      return null;
    }
    
    String                              query   = "SELECT DISTINCT "; //"* FROM "; //wiki_revision_test WHERE UserID = ? AND Timestamp = ?";
    int                                 counter = 1;
    PreparedStatement                   stmt;
    Iterator<Map.Entry<String, String>> iterator;
    
    for(String certain : columnNames){
      query += certain + SSStrU.comma + SSStrU.blank;
    }
    
    query          = SSStrU.removeTrailingString(query, SSStrU.comma + SSStrU.blank);
    query         += " FROM " + tableName + " WHERE ";
    iterator       = wherePars.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
    }
    
    query          = SSStrU.removeTrailingString(query, " AND ");
    stmt           = connector.prepareStatement(query);
    iterator       = wherePars.entrySet().iterator();
    
    while(iterator.hasNext()){
      stmt.setObject(counter++, iterator.next().getValue());
    }
    
    return stmt.executeQuery();
  }
  
  @Override
  public ResultSet selectAllWhereOrderBy(
    String              tableName, 
    Map<String, String> whereParNamesWithValues, 
    String              orderByColumn, 
    String              sortType) throws Exception{
   
    if(
      SSStrU.isEmpty(tableName) ||
      whereParNamesWithValues == null){
      SSServErrReg.regErrThrow(new Exception("table not spec or pars null"));
      return null;
    }
    
    String                              query   = "SELECT * FROM "; //wiki_revision_test WHERE UserID = ? AND Timestamp = ?";
    int                                 counter = 1;
    PreparedStatement                   stmt;
    Iterator<Map.Entry<String, String>> iterator;
    
    query        += tableName + " WHERE ";
    iterator      = whereParNamesWithValues.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
    }
    
    query          = SSStrU.removeTrailingString(query, " AND ");
    query         += " ORDER BY " + orderByColumn + SSStrU.blank + sortType;
    stmt           = connector.prepareStatement(query);
    iterator       = whereParNamesWithValues.entrySet().iterator();
    
    while(iterator.hasNext()){
      stmt.setObject(counter++, iterator.next().getValue());
    }
    
    return stmt.executeQuery();
  }
  
  @Override
  public void updateWhereIgnore(
    final String              table, 
    final Map<String, String> where, 
    final Map<String, String> values) throws Exception{
    
    if(
      SSStrU.isEmpty (table) ||
      SSObjU.isNull(where, values)){
      SSServErrReg.regErrThrow(new Exception("table not specified, pars null or newValues null"));
      return;
    }
    
    //    UPDATE table_name SET column1=value1,column2=value2,... WHERE some_column=some_value;
    String                              query   = "UPDATE IGNORE " + table + " SET ";
    int                                 counter = 1;
    PreparedStatement                   stmt    = null;
    Iterator<Map.Entry<String, String>> iterator;
    
    iterator      = values.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + SSStrU.comma;
    }
    
    query          = SSStrU.removeTrailingString(query, SSStrU.comma);
    query         += " WHERE ";
    iterator       = where.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
    }
    
    try{
      query          = SSStrU.removeTrailingString(query, " AND ");
      stmt           = connector.prepareStatement(query);
      iterator       = values.entrySet().iterator();

      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }

      iterator       = where.entrySet().iterator();

      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }

      stmt.executeUpdate();
      
    }catch(Exception error){
       SSServErrReg.regErrThrow(error);
    }finally{
      
      if(stmt != null){
        stmt.close();
      }
    }
  }
  
  @Override
  public void updateWhere(
    final String              table, 
    final Map<String, String> where, 
    final Map<String, String> values) throws Exception{
    
    if(
      SSStrU.isEmpty (table) ||
      SSObjU.isNull(where, values)){
      SSServErrReg.regErrThrow(new Exception("table not specified, pars null or newValues null"));
      return;
    }
    
    //    UPDATE table_name SET column1=value1,column2=value2,... WHERE some_column=some_value;
    String                              query   = "UPDATE " + table + " SET ";
    int                                 counter = 1;
    PreparedStatement                   stmt    = null;
    Iterator<Map.Entry<String, String>> iterator;
    
    iterator      = values.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + SSStrU.comma;
    }
    
    query          = SSStrU.removeTrailingString(query, SSStrU.comma);
    query         += " WHERE ";
    iterator       = where.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
    }
    
    try{
      query          = SSStrU.removeTrailingString(query, " AND ");
      stmt           = connector.prepareStatement(query);
      iterator       = values.entrySet().iterator();

      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }

      iterator       = where.entrySet().iterator();

      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }

      stmt.executeUpdate();
      
    }catch(Exception error){
       SSServErrReg.regErrThrow(error);
    }finally{
      
      if(stmt != null){
        stmt.close();
      }
    }
  }
  
  @Override
  public void insert(
    final String              table, 
    final Map<String, String> insert) throws Exception{
    
    if(
      SSStrU.isEmpty (table) ||
      SSObjU.isNull  (insert)){
      SSServErrReg.regErrThrow(new Exception("table not spec or pars null"));
      return;
    }
    
    String                              query   = "INSERT INTO ";
    int                                 counter = 1;
    PreparedStatement                   stmt    = null;
    Iterator<Map.Entry<String, String>> iterator;

    query         += table + SSStrU.bracketOpen;
    iterator       = insert.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.comma;
    }
    
    query          = SSStrU.removeTrailingString(query, SSStrU.comma);
    query         += SSStrU.bracketClose + " VALUES " + SSStrU.bracketOpen;
    iterator       = insert.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += SSStrU.questionMark + SSStrU.comma;
      iterator.next();
    }
    
    query          = SSStrU.removeTrailingString(query, SSStrU.comma);
    query         += SSStrU.bracketClose;
    
    try{
    
      stmt           = connector.prepareStatement(query);
      iterator       = insert.entrySet().iterator();

      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }
      
      stmt.executeUpdate();
    }catch(MySQLTransactionRollbackException error){
      throw new SSSQLDeadLockErr(error.getMessage());
    }finally{
      if(stmt != null){
        stmt.close();
      }
    }
  }
  
  @Override 
  public void insertWhereNotExists(
    final String              table, 
    final Map<String, String> insert,
    final Map<String, String> uniqueKey) throws Exception{

    if(
      SSStrU.isEmpty (table) ||
      SSObjU.isNull  (insert, uniqueKey) ||
      uniqueKey.size() != 1){
      SSServErrReg.regErrThrow(new Exception("table not spec or pars null"));
      return;
    }
    
    final String                        key     = uniqueKey.entrySet().iterator().next().getKey();
    final String                        value   = uniqueKey.get(key);
    String                              query   = "INSERT INTO ";
    int                                 counter = 1;
    PreparedStatement                   stmt    = null;
    Iterator<Map.Entry<String, String>> iterator;

    query         += table + SSStrU.bracketOpen;
    iterator       = insert.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.comma;
    }
    
    query          = SSStrU.removeTrailingString(query, SSStrU.comma);
    query         += SSStrU.bracketClose + " SELECT * FROM (SELECT ";
    iterator       = insert.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += SSStrU.questionMark + "AS " + iterator.next().getKey() + SSStrU.comma;
    }
    
    query          = SSStrU.removeTrailingString(query, SSStrU.comma);
    query         += SSStrU.bracketClose;
    query         += " AS tmp WHERE NOT EXISTS (SELECT " + key + " FROM " + table + " WHERE " + key + "='" + value + "') LIMIT 1";
    
    try{
    
      stmt           = connector.prepareStatement(query);
      iterator       = insert.entrySet().iterator();

      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }
      
      stmt.executeUpdate();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      if(stmt != null){
        stmt.close();
      }
    }
  }
  
  @Override
  public void deleteAll(
    String tableName) throws Exception{
    
    if(SSStrU.isEmpty (tableName)){
      SSServErrReg.regErrThrow(new Exception("table not spec"));
      return;
    }
    
    String              query = "DELETE FROM " + tableName;
    PreparedStatement   stmt  = null;
    
    try{
      stmt = connector.prepareStatement(query);
      stmt.executeUpdate();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(stmt != null){
        stmt.close();
      }
    }
  }
  
  @Override
  public void deleteWhere(
    final String              table, 
    final Map<String, String> where) throws Exception{

    if(
      SSStrU.isEmpty (table) ||
      SSObjU.isNull  (where)){
      SSServErrReg.regErrThrow(new Exception("table not spec or pars null"));
      return;
    }
    
    String                              query   = "DELETE FROM ";
    int                                 counter = 1;
    PreparedStatement                   stmt    = null;
    Iterator<Map.Entry<String, String>> iterator;
    
    query        += table + " WHERE ";
    iterator      = where.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
    }
    
    query          = SSStrU.removeTrailingString(query, " AND ");
    
    try{
      stmt           = connector.prepareStatement(query);
      iterator       = where.entrySet().iterator();

      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }

      stmt.executeUpdate();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(stmt != null){
        stmt.close();
      }
    }
  }
  
  @Override
  public void deleteWhereIgnore(
    final String              table, 
    final Map<String, String> where) throws Exception{

    if(
      SSStrU.isEmpty (table) ||
      SSObjU.isNull  (where)){
      SSServErrReg.regErrThrow(new Exception("table not spec or pars null"));
      return;
    }
    
    String                              query   = "DELETE IGNORE FROM ";
    int                                 counter = 1;
    PreparedStatement                   stmt    = null;
    Iterator<Map.Entry<String, String>> iterator;
    
    query        += table + " WHERE ";
    iterator      = where.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
    }
    
    query          = SSStrU.removeTrailingString(query, " AND ");
    
    try{
      stmt           = connector.prepareStatement(query);
      iterator       = where.entrySet().iterator();

      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }

      stmt.executeUpdate();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(stmt != null){
        stmt.close();
      }
    }
  }
  
  private void connectToMYSQL() throws Exception{
    
    if(SSObjU.isNull(connectionPool)){
      connectToMYSQLConnectionPool();
    }
    
    while(!gotCon && numberTimesTriedToGetCon < 5){
      
      try{
        connector = connectionPool.getConnection();
        connector.setAutoCommit(true);
        
        gotCon = true;
        
      }catch(MySQLNonTransientConnectionException error){
        
        SSLogU.info("no db conn available anymore... going to sleep for 3000 ms");
        
        numberTimesTriedToGetCon++;
        Thread.sleep(3000);
      }
    }
  }
  
  private void connectToMYSQLConnectionPool() throws Exception{
    
    Class.forName("com.mysql.jdbc.Driver");
    
    //    private static BoneCP     connectionPool   = null;
    //    BoneCPConfig config = new BoneCPConfig();
//    config.setJdbcUrl  ("jdbc:mysql://" + ((SSDBSQLConf)conf).host + SSStrU.colon + ((SSDBSQLConf)conf).port + SSStrU.slash + ((SSDBSQLConf)conf).schema); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
//    config.setUsername (((SSDBSQLConf)conf).username);
//    config.setPassword (((SSDBSQLConf)conf).password);
//    config.setMinConnectionsPerPartition(5);
//    config.setMaxConnectionsPerPartition(10);
//    config.setPartitionCount(4);
//    config.setLogStatementsEnabled(true);
//    config.setPoolStrategy("DEFAULT");
//    config.setAcquireRetryAttempts(1);
//    config.setAcquireRetryDelayInMs(1100);
//    config.setConnectionTimeoutInMs(1300);
//    
//    config.setTransactionRecoveryEnabled(false);
//    
//    connectionPool = new BoneCP(config);
    
    
    PoolProperties prop = new PoolProperties();
    prop.setUrl             ("jdbc:mysql://" + ((SSDBSQLConf)conf).host + SSStrU.colon + ((SSDBSQLConf)conf).port + SSStrU.slash + ((SSDBSQLConf)conf).schema); //+ "?autoReconnect=true"
    prop.setDriverClassName ("com.mysql.jdbc.Driver");
    prop.setUsername        (((SSDBSQLConf)conf).username);
    prop.setPassword        (((SSDBSQLConf)conf).password);
    prop.setFairQueue(true);
    prop.setTestWhileIdle(true);
    prop.setValidationInterval(30000);
    prop.setMaxActive(100);
    prop.setInitialSize(10);
    prop.setMinIdle(10);
    prop.setMaxWait(10000);
    prop.setRemoveAbandoned(false);
    prop.setMinEvictableIdleTimeMillis(30000);
    
    connectionPool = new DataSource(prop);
  }
}

//  @Override
//  public void insertIgnore(String tableName, Map<String, String> parNamesWithValues) throws Exception {
//   
//    String                              query   = "INSERT IGNORE INTO ";
//    int                                 counter = 1;
//    PreparedStatement                   stmt;
//    Iterator<Map.Entry<String, String>> iterator;
//    
//    if(
//      SSStrU.isEmpty (tableName) ||
//      SSObjU.isNull  (parNamesWithValues)){
//      SSLogU.logAndThrow(new Exception("table not spec or pars null"));
//    }
//    
//    query         += tableName + SSStrU.bracketOpen;
//    iterator       = parNamesWithValues.entrySet().iterator();
//    
//    while(iterator.hasNext()){
//      query += iterator.next().getKey() + SSStrU.comma;
//    }
//    
//    query          = SSStrU.removeTrailingString(query, SSStrU.comma);
//    query         += SSStrU.bracketClose + " VALUES " + SSStrU.bracketOpen;
//    iterator       = parNamesWithValues.entrySet().iterator();
//    
//    while(iterator.hasNext()){
//      query += SSStrU.questionMark + SSStrU.comma;
//      iterator.next();
//    }
//    
//    query          = SSStrU.removeTrailingString(query, SSStrU.comma);
//    query         += SSStrU.bracketClose;
//    stmt           = connector.prepareStatement(query);
//    iterator       = parNamesWithValues.entrySet().iterator();
//    
//    while(iterator.hasNext()){
//      stmt.setObject(counter++, iterator.next().getValue());
//    }
//    
//    stmt.executeUpdate();
//    stmt.close();
//    
////    "insert into table (id, name, age) values(1, "A", 19) on duplicate key update name=values(name), age=values(age)";
//  }

//@Override
//  public ResultSet query(String query) throws Exception{
//    
//    Statement   stmt    = connector.createStatement();
//    ResultSet   results = stmt.executeQuery(query);
//    
//    return results;
//  }

//    p.setJmxEnabled(false);
//    p.setTestWhileIdle(false);
//    p.setTestOnBorrow(false);
//    p.setValidationQuery("SELECT 1");
//    p.setTestOnReturn(false);
//    p.setValidationInterval(30000);
//    p.setTimeBetweenEvictionRunsMillis(30000);
//    p.setMaxActive(100);
//    p.setInitialSize(10);
//    p.setMaxWait(10000);
//    p.setRemoveAbandonedTimeout(60);
//    p.setMinEvictableIdleTimeMillis(30000);
//    p.setMinIdle(10);
//    p.setLogAbandoned(true);
//    p.setRemoveAbandoned(true);
//    p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");