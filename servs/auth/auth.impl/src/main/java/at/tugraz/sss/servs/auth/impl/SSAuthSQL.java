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
package at.tugraz.sss.servs.auth.impl;

import at.tugraz.sss.servs.auth.datatype.SSAuthSQLTableE;
import at.tugraz.sss.serv.util.SSSQLVarNames;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.SSDBSQLFctA;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.reg.SSServErrReg;

public class SSAuthSQL extends SSDBSQLFctA{
  
  public SSAuthSQL(final SSDBSQLI dbSQL){
    super(dbSQL);
  }

  public boolean hasKey(
    final SSServPar servPar,
    final SSUri userUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      
      resultSet = dbSQL.select(servPar, SSAuthSQLTableE.auth, columns, wheres, null, null, null);
      
      return existsFirstResult(resultSet);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public String getKey(
    final SSServPar servPar,
    final SSUri userUri) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.authKey);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      
      resultSet = dbSQL.select(servPar, SSAuthSQLTableE.auth, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return bindingStr(resultSet, SSSQLVarNames.authKey);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri getUserForKey(
    final SSServPar servPar,
    final String key) throws SSErr{
   
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.authKey, key);
      
      resultSet = dbSQL.select(servPar, SSAuthSQLTableE.auth, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return bindingStrToUri(resultSet, SSSQLVarNames.userId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public String addKey(
    final SSServPar servPar,
    final SSUri  userUri, 
    final String authKey) throws SSErr{
    
     try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userId,  userUri);
      insert(inserts, SSSQLVarNames.authKey, authKey);
      
      dbSQL.insert(servPar, SSAuthSQLTableE.auth, inserts);
      
      return authKey;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public List<String> getKeys(
    final SSServPar servPar) throws SSErr{
    
    final List<String>        columns   = new ArrayList<>();
    final Map<String, String> wheres    = new HashMap<>();
    ResultSet                 resultSet = null;
    
    try{
      
      resultSet = dbSQL.select(servPar, SSAuthSQLTableE.auth, columns, wheres, null, null, null);
      
      return getStringsFromResult(resultSet, SSSQLVarNames.authKey);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void removeKey(
    final SSServPar servPar,
    final SSUri user) throws SSErr{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.userId, user);
      
      dbSQL.deleteIgnore(servPar, SSAuthSQLTableE.auth, wheres);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
  
