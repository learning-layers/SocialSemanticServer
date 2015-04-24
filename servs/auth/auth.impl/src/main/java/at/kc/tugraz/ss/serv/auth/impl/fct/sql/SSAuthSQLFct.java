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
package at.kc.tugraz.ss.serv.auth.impl.fct.sql;

import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSDBSQLI;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;

public class SSAuthSQLFct extends SSDBSQLFct{
  
  public SSAuthSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }

  public Boolean hasKey(
    final SSUri userUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.authTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
        SSServErrReg.reset();
        return false;
      }
        
      SSServErrReg.regErrThrow(error);
      return null;
      
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public String getKey(
    final SSUri userUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.authKey);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.authTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStr(resultSet, SSSQLVarNames.authKey);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri getUserForKey(
    final String key) throws Exception{
   
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.authKey, key);
      
      resultSet = dbSQL.select(SSSQLVarNames.authTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarNames.userId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public String addKey(
    final SSUri  userUri, 
    final String authKey) throws Exception{
    
     try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userId,  userUri);
      insert(inserts, SSSQLVarNames.authKey, authKey);
      
      dbSQL.insert(SSSQLVarNames.authTable, inserts);
      
      return authKey;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public List<String> getKeys() throws Exception{
    
    final List<String>        columns   = new ArrayList<>();
    final Map<String, String> wheres    = new HashMap<>();
    ResultSet                 resultSet = null;
    
    try{
      
      resultSet = dbSQL.select(SSSQLVarNames.authTable, columns, wheres, null, null, null);
      
      return getStringsFromResult(resultSet, SSSQLVarNames.authKey);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void removeKey(final SSUri user) throws Exception{
    
    try{
      final Map<String, String> deletes = new HashMap<>();
      
      delete(deletes, SSSQLVarNames.userId, user);
      
      dbSQL.deleteIgnore(SSSQLVarNames.authTable, deletes);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
  
