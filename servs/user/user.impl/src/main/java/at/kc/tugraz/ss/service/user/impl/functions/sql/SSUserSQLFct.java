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
package at.kc.tugraz.ss.service.user.impl.functions.sql;

import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSDBSQLSelectPar;
import at.tugraz.sss.serv.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import sss.servs.entity.sql.SSEntitySQL;

public class SSUserSQLFct extends SSEntitySQL{
  
  public SSUserSQLFct(
    final SSDBSQLI dbSQL,
    final SSUri    systemUserURI) throws Exception{
    
    super(dbSQL, systemUserURI);
  }
  
  public Boolean existsUser(final String email) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      final List<String>        columns          = new ArrayList<>();
      final List<String>        tables           = new ArrayList<>();
      final Map<String, String> wheres           = new HashMap<>();
      final List<String>        tableCons        = new ArrayList<>();
      
      column(columns, SSSQLVarNames.id);
      
      table(tables, SSSQLVarNames.entityTable);
      table(tables, SSSQLVarNames.userTable);
      
      where(wheres, SSSQLVarNames.entityTable, SSSQLVarNames.type,  SSEntityE.user);
      where(wheres, SSSQLVarNames.userTable,   SSSQLVarNames.email, email);
      
      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.userTable, SSSQLVarNames.userId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri getUserURIForEmail(final String email) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      final List<String>        columns          = new ArrayList<>();
      final List<String>        tables           = new ArrayList<>();
      final Map<String, String> wheres           = new HashMap<>();
      final List<String>        tableCons        = new ArrayList<>();
      
      column(columns, SSSQLVarNames.id);
      
      table(tables, SSSQLVarNames.entityTable);
      table(tables, SSSQLVarNames.userTable);
      
      where(wheres, SSSQLVarNames.userTable,   SSSQLVarNames.email, email);
      
      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.userTable, SSSQLVarNames.userId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUser getUser(final SSUri user) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      final List<String>        columns          = new ArrayList<>();
      final List<String>        tables           = new ArrayList<>();
      final Map<String, String> wheres           = new HashMap<>();
      final List<String>        tableCons        = new ArrayList<>();
      
      column(columns, SSSQLVarNames.id);
      column(columns, SSSQLVarNames.email);
      
      table(tables, SSSQLVarNames.entityTable);
      table(tables, SSSQLVarNames.userTable);
      
      where(wheres, SSSQLVarNames.entityTable, SSSQLVarNames.id, user);
      
      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.userTable, SSSQLVarNames.userId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      checkFirstResult(resultSet);
      
      return SSUser.get(
        user,
        bindingStr(resultSet, SSSQLVarNames.email));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getUserURIs(
    final List<SSUri> userURIs) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      final List<String>                         columns          = new ArrayList<>();
      final List<String>                         tables           = new ArrayList<>();
      final List<MultivaluedMap<String, String>> wheres           = new ArrayList<>();
      final List<String>                         tableCons        = new ArrayList<>();
      
      column(columns, SSSQLVarNames.id);
      
      table(tables, SSSQLVarNames.entityTable);
      table(tables, SSSQLVarNames.userTable);
      
      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.userTable, SSSQLVarNames.userId);
      
      if(
        userURIs != null &&
        !userURIs.isEmpty()){
        
        final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
        
        for(SSUri userURI : userURIs){
          where(whereUsers, SSSQLVarNames.entityTable, SSSQLVarNames.id, userURI);
        }
        
        wheres.add(whereUsers);
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
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addUser(
    final SSUri  user,
    final String email) throws Exception{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userId,     user);
      insert(inserts, SSSQLVarNames.email,      email);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.userId, user);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.userTable, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}