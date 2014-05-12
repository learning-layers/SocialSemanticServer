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
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
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
  
  public SSDBSQLMySQLImpl(final SSConfA conf) throws Exception{
    
    super(conf);
    
    connectToMYSQL();
  }
  
  @Override
  public ResultSet select(
    final String table) throws Exception{
    return connector.prepareStatement("SELECT * FROM " + table).executeQuery();
  }
  
  @Override
  public ResultSet select(
    final String              table, 
    final Map<String, String> wheres) throws Exception{
    
    String                              query    = "SELECT * FROM " + table + " WHERE ";
    Iterator<Map.Entry<String, String>> iterator = wheres.entrySet().iterator();
    int                                 counter  = 1;
    PreparedStatement                   stmt;
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
    }
    
    query          = SSStrU.removeTrailingString(query, " AND ");
    stmt           = connector.prepareStatement(query);
    iterator       = wheres.entrySet().iterator();
    
    while(iterator.hasNext()){
      stmt.setObject(counter++, iterator.next().getValue());
    }
    
    return stmt.executeQuery();
  }
  
//  @Override
//  public ResultSet select(
//    final List<String>        tables,
//    final List<String>        columns,
//    final Map<String, String> wheres,
//    final String              tableConnection) throws Exception{
//    
//    String                              query   = "SELECT "; 
//    int                                 counter = 1;
//    PreparedStatement                   stmt;
//    Iterator<Map.Entry<String, String>> iterator;
//    
//    for(String columnName : columns){
//      query += columnName + SSStrU.comma;
//    }
//    
//    query = SSStrU.removeTrailingString(query, SSStrU.comma) + " FROM ";
//    
//    for(String tableName : tables){
//      query += tableName + SSStrU.comma;
//    }
//    
//    query         = SSStrU.removeTrailingString(query, SSStrU.comma) + " WHERE ";
//    iterator      = wheres.entrySet().iterator();
//    
//    while(iterator.hasNext()){
//      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
//    }
//    
//    query         += tableConnection;
//    stmt           = connector.prepareStatement(query);
//    iterator       = wheres.entrySet().iterator();
//    
//    while(iterator.hasNext()){
//      stmt.setObject(counter++, iterator.next().getValue());
//    }
//
//    return stmt.executeQuery();
//  }
  
  @Override
  public ResultSet select(
    final List<String>        tables, 
    final List<String>        columns, 
    final Map<String, String> wheres, 
    final List<String>        tableConnections) throws Exception{
    
    String                              query   = "SELECT DISTINCT "; //caution do not remove distinct here without checks
    Iterator<Map.Entry<String, String>> iterator;
    int                                 counter = 1;
    PreparedStatement                   stmt;
    
    for(String columnName : columns){
      query += columnName + SSStrU.comma;
    }
    
    query = SSStrU.removeTrailingString(query, SSStrU.comma) + " FROM ";
    
    for(String tableName : tables){
      query += tableName + SSStrU.comma;
    }
    
    query         = SSStrU.removeTrailingString(query, SSStrU.comma) + " WHERE ";
    iterator      = wheres.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
    }
    
    for(String tableConection : tableConnections){
      query += tableConection + " AND ";
    }
    
    query          = SSStrU.removeTrailingString(query, " AND ");
    stmt           = connector.prepareStatement(query);
    iterator       = wheres.entrySet().iterator();
    
    while(iterator.hasNext()){
      stmt.setObject(counter++, iterator.next().getValue());
    }

    return stmt.executeQuery();
  }
  
  @Override
  public ResultSet select(
    final List<String>        tables, 
    final List<String>        columns, 
    final Map<String, String> wheres, 
    final String              tableConnection,
    final String              orderByColumn, 
    final String              sortType) throws Exception{
    
    String                              query   = "SELECT ";
    int                                 counter = 1;
    PreparedStatement                   stmt;
    Iterator<Map.Entry<String, String>> iterator;
    
    for(String columnName : columns){
      query += columnName + SSStrU.comma;
    }
    
    query = SSStrU.removeTrailingString(query, SSStrU.comma) + " FROM ";
    
    for(String tableName : tables){
      query += tableName + SSStrU.comma;
    }
    
    query         = SSStrU.removeTrailingString(query, SSStrU.comma) + " WHERE ";
    iterator      = wheres.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
    }
    
    query         += tableConnection + " ORDER BY " + orderByColumn + SSStrU.blank + sortType;
    stmt           = connector.prepareStatement(query);
    iterator       = wheres.entrySet().iterator();
    
    while(iterator.hasNext()){
      stmt.setObject(counter++, iterator.next().getValue());
    }

    return stmt.executeQuery();
  }
  
  @Override
  public ResultSet select(
    final String              table, 
    final List<String>        columns, 
    final Map<String, String> wheres) throws Exception{
    
    String                              query   = "SELECT DISTINCT ";
    int                                 counter = 1;
    PreparedStatement                   stmt;
    Iterator<Map.Entry<String, String>> iterator;
    
    for(String certain : columns){
      query += certain + SSStrU.comma + SSStrU.blank;
    }
    
    query          = SSStrU.removeTrailingString(query, SSStrU.comma + SSStrU.blank) + " FROM " + table + " WHERE ";
    iterator       = wheres.entrySet().iterator();
    
    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
    }
    
    query          = SSStrU.removeTrailingString(query, " AND ");
    stmt           = connector.prepareStatement(query);
    iterator       = wheres.entrySet().iterator();
    
    while(iterator.hasNext()){
      stmt.setObject(counter++, iterator.next().getValue());
    }
    
    return stmt.executeQuery();
  }
  
  @Override
  public ResultSet select(
    String              table, 
    Map<String, String> wheres, 
    String              orderByColumn, 
    String              sortType) throws Exception{
   
    String                              query    = "SELECT * FROM " + table + " WHERE ";
    Iterator<Map.Entry<String, String>> iterator = wheres.entrySet().iterator();
    int                                 counter  = 1;
    PreparedStatement                   stmt;

    while(iterator.hasNext()){
      query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
    }
    
    query          = SSStrU.removeTrailingString(query, " AND ") + " ORDER BY " + orderByColumn + SSStrU.blank + sortType;
    stmt           = connector.prepareStatement(query);
    iterator       = wheres.entrySet().iterator();
    
    while(iterator.hasNext()){
      stmt.setObject(counter++, iterator.next().getValue());
    }
    
    return stmt.executeQuery();
  }
  
  @Override 
  public void insertIfNotExists(
    final String              table, 
    final Map<String, String> inserts,
    final Map<String, String> uniqueKeys) throws Exception{

    PreparedStatement stmt = null;
    
    try{
      String                              query    = "INSERT INTO " + table + SSStrU.bracketOpen;
      int                                 counter  = 1;
      Iterator<Map.Entry<String, String>> iterator = inserts.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.comma;
      }
      
      query          = SSStrU.removeTrailingString(query, SSStrU.comma) + SSStrU.bracketClose + " SELECT * FROM (SELECT ";
      iterator       = inserts.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += SSStrU.questionMark + "AS " + iterator.next().getKey() + SSStrU.comma;
      }
      
      query          = SSStrU.removeTrailingString(query, SSStrU.comma) + SSStrU.bracketClose + " AS tmp WHERE NOT EXISTS (SELECT ";
      iterator       = uniqueKeys.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.comma;
      }
      
      query          = SSStrU.removeTrailingString(query, SSStrU.comma) + " FROM " + table + " WHERE ";
      iterator       = uniqueKeys.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
      }
      
      query          = SSStrU.removeTrailingString(query, " AND ") + ") LIMIT 1";
      stmt           = connector.prepareStatement(query);
      iterator       = inserts.entrySet().iterator();
      
      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }
      
      iterator = uniqueKeys.entrySet().iterator();
      
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
  public void delete(
    final String table) throws Exception{
    
    PreparedStatement   stmt  = null;
    
    try{
      
      stmt = connector.prepareStatement("DELETE FROM " + table);
      
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
  public void updateIgnore(
    final String              table, 
    final Map<String, String> wheres, 
    final Map<String, String> values) throws Exception{
    
    PreparedStatement stmt    = null;
    
    try{
      String                              query    = "UPDATE IGNORE " + table + " SET ";
      int                                 counter  = 1;
      Iterator<Map.Entry<String, String>> iterator = values.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + SSStrU.comma;
      }
      
      query          = SSStrU.removeTrailingString(query, SSStrU.comma) + " WHERE ";
      iterator       = wheres.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
      }
      
      query          = SSStrU.removeTrailingString(query, " AND ");
      stmt           = connector.prepareStatement(query);
      iterator       = values.entrySet().iterator();
      
      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }
      
      iterator = wheres.entrySet().iterator();
      
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
  public void update(
    final String              table, 
    final Map<String, String> wheres, 
    final Map<String, String> updates) throws Exception{
    
    PreparedStatement stmt    = null;
    
    try{
      String                              query   = "UPDATE " + table + " SET ";
      int                                 counter = 1;
      Iterator<Map.Entry<String, String>> iterator = updates.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + SSStrU.comma;
      }
      
      query          = SSStrU.removeTrailingString(query, SSStrU.comma) + " WHERE ";
      iterator       = wheres.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
      }
      
      
      query          = SSStrU.removeTrailingString(query, " AND ");
      stmt           = connector.prepareStatement(query);
      iterator       = updates.entrySet().iterator();
      
      while(iterator.hasNext()){
        stmt.setObject(counter++, iterator.next().getValue());
      }
      
      iterator = wheres.entrySet().iterator();
      
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
  public void insert(
    final String              table, 
    final Map<String, String> inserts) throws Exception{
    
    PreparedStatement stmt    = null;
    
    try{
      String                              query    = "INSERT INTO " + table + SSStrU.bracketOpen;
      int                                 counter  = 1;
      Iterator<Map.Entry<String, String>> iterator = inserts.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.comma;
      }
      
      query           = SSStrU.removeTrailingString(query, SSStrU.comma) + SSStrU.bracketClose + " VALUES " + SSStrU.bracketOpen;
      iterator        = inserts.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += SSStrU.questionMark + SSStrU.comma;
        iterator.next();
      }
      
      query          = SSStrU.removeTrailingString(query, SSStrU.comma) + SSStrU.bracketClose;
      stmt           = connector.prepareStatement(query);
      iterator       = inserts.entrySet().iterator();
      
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
  public void delete(
    final String              table,
    final Map<String, String> deletes) throws Exception{
    
    PreparedStatement stmt    = null;
    
    try{
      String                              query    = "DELETE FROM " + table + " WHERE ";
      int                                 counter  = 1;
      Iterator<Map.Entry<String, String>> iterator = deletes.entrySet().iterator();
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
      }
      
      query          = SSStrU.removeTrailingString(query, " AND ");
      stmt           = connector.prepareStatement(query);
      iterator       = deletes.entrySet().iterator();
      
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
  public void deleteIgnore(
    final String              table,
    final Map<String, String> deletes) throws Exception{
    
    PreparedStatement stmt = null;
    
    try{
      String                              query    = "DELETE IGNORE FROM " + table + " WHERE ";
      Iterator<Map.Entry<String, String>> iterator = deletes.entrySet().iterator();
      int                                 counter  = 1;
      
      while(iterator.hasNext()){
        query += iterator.next().getKey() + SSStrU.equal + SSStrU.questionMark + " AND ";
      }
      
      query          = SSStrU.removeTrailingString(query, " AND ");
      stmt           = connector.prepareStatement(query);
      iterator       = deletes.entrySet().iterator();
      
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
  
  public Connection getConnection(){
    return connector;
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