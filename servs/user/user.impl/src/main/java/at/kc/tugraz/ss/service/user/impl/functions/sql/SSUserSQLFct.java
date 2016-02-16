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

import at.tugraz.sss.serv.util.SSSQLVarNames;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.reg.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import at.tugraz.sss.serv.db.api.SSCoreSQL;

public class SSUserSQLFct extends SSCoreSQL{
  
  public SSUserSQLFct(
    final SSDBSQLI dbSQL){
    
    super(dbSQL);
  }
  
  public boolean existsUser(
    final SSServPar servPar, 
    final String email) throws SSErr{
    
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
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri getUserURIForEmail(
    final SSServPar servPar, 
    final String    email) throws SSErr{
    
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
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      if(!existsFirstResult(resultSet)){   
        return null;
      }
      
      return bindingStrToUri(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUser getUser(
    final SSServPar servPar, 
    final SSUri user) throws SSErr{
    
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
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
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
    final SSServPar servPar, 
    final List<SSUri> userURIs) throws SSErr{
    
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
            servPar, 
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
    final SSServPar servPar, 
    final SSUri  user,
    final String email) throws SSErr{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userId,     user);
      insert(inserts, SSSQLVarNames.email,      email);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.userId, user);
      
      dbSQL.insertIfNotExists(servPar, SSSQLVarNames.userTable, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}