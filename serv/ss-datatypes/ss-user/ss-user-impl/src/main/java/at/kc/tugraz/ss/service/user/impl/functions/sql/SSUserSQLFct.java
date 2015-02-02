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

import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSUserSQLFct extends SSDBSQLFct{
  
  public SSUserSQLFct(SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }
  
  public List<SSUser> userAll() throws Exception {
    
    ResultSet resultSet = null;
    
    try{
      final List<SSUser>        users            = new ArrayList<>();
      final List<String>        columns          = new ArrayList<>();
      final List<String>        tables           = new ArrayList<>();
      final Map<String, String> wheres           = new HashMap<>();
      final List<String>        tableCons        = new ArrayList<>();
      
      where(wheres, SSSQLVarU.type, SSEntityE.user);
      
      column(columns, SSSQLVarU.id);
      column(columns, SSSQLVarU.label);
      column(columns, SSSQLVarU.email);

      table(tables, entityTable);
      table(tables, userTable);
      
      tableCon(tableCons, entityTable, SSSQLVarU.id, userTable, SSSQLVarU.userId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        users.add(
          SSUser.get(
            bindingStrToUri   (resultSet, SSSQLVarU.id),
            bindingStrToLabel (resultSet, SSSQLVarU.label),
            bindingStr        (resultSet, SSSQLVarU.email)));
      }
      
      return users;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public Boolean existsUser(final String email) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      final List<String>        columns          = new ArrayList<>();
      final List<String>        tables           = new ArrayList<>();
      final Map<String, String> wheres           = new HashMap<>();
      final List<String>        tableCons        = new ArrayList<>();
      
      column(columns, SSSQLVarU.id);

      table(tables, entityTable);
      table(tables, userTable);
      
      where(wheres, entityTable, SSSQLVarU.type,  SSEntityE.user);
      where(wheres, userTable,   SSSQLVarU.email, email);
      
      tableCon(tableCons, entityTable, SSSQLVarU.id, userTable, SSSQLVarU.userId);
      
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
      
      column(columns, SSSQLVarU.id);

      table(tables, entityTable);
      table(tables, userTable);
      
      where(wheres, userTable,   SSSQLVarU.email, email);
      
      tableCon(tableCons, entityTable, SSSQLVarU.id, userTable, SSSQLVarU.userId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarU.id);
      
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
      final List<String>       columns          = new ArrayList<>();
      final List<String>        tables           = new ArrayList<>();
      final Map<String, String> wheres           = new HashMap<>();
      final List<String>        tableCons        = new ArrayList<>();
      
      column(columns, SSSQLVarU.id);
      column(columns, SSSQLVarU.email);
      column(columns, SSSQLVarU.label);

      table(tables, entityTable);
      table(tables, userTable);
      
      where(wheres, entityTable, SSSQLVarU.id, user);
      
      tableCon(tableCons, entityTable, SSSQLVarU.id, userTable, SSSQLVarU.userId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      checkFirstResult(resultSet);
      
      return SSUser.get(
        user, 
        bindingStrToLabel(resultSet, SSSQLVarU.label),
        bindingStr(resultSet, SSSQLVarU.email));
      
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
      
      insert(inserts, SSSQLVarU.userId, user);
      insert(inserts, SSSQLVarU.email,  email);
      
      uniqueKey(uniqueKeys, SSSQLVarU.userId, user);
      
      dbSQL.insertIfNotExists(userTable, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}