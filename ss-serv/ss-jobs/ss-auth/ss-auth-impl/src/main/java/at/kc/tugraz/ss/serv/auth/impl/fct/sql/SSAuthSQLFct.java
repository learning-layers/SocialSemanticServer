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

import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSNoResultFoundErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSAuthSQLFct extends SSDBSQLFct{
  
  public SSAuthSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }

  public Boolean hasKey(
    final SSUri userUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final Map<String, String> wheres = new HashMap<String, String>();
      
      where(wheres, SSSQLVarU.userId, userUri);
      
      resultSet = dbSQL.select(authTable, wheres);
      
      checkFirstResult(resultSet);
      
      return true;
      
    }catch(SSNoResultFoundErr error){
      return false;
    }catch(Exception error){
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
      final Map<String, String> wheres = new HashMap<String, String>();
      
      where(wheres, SSSQLVarU.userId, userUri);
      
      resultSet = dbSQL.select(authTable, wheres);
      
      checkFirstResult(resultSet);
      
      return bindingStr(resultSet, SSSQLVarU.authKey);
      
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
      final Map<String, String> inserts = new HashMap<String, String>();
      
      insert(inserts, SSSQLVarU.userId,  userUri);
      insert(inserts, SSSQLVarU.authKey, authKey);
      
      dbSQL.insert(authTable, inserts);
      
      return authKey;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public List<String> getKeys() throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      resultSet = dbSQL.select(authTable);
      
      return getStringsFromResult(resultSet, SSSQLVarU.authKey);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
  
