/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.recomm.impl.fct.sql;

import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSUri;

import at.tugraz.sss.serv.SSServImplWithDBA;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSRecommSQLFct extends SSDBSQLFct{
  
  public SSRecommSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  public Map<String, List<String>> getUserRealms() throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final Map<String, List<String>> userRealms = new HashMap<>();
      final List<String>              columns    = new ArrayList<>();
      final Map<String, String>       wheres     = new HashMap<>();
      
      column(columns, SSSQLVarNames.userId);
      column(columns, SSSQLVarNames.realm);
      
      resultSet = dbSQL.select(SSSQLVarNames.recommUserRealmsTable, columns, wheres, null, null, null);

      while(resultSet.next()){
        
        if(!userRealms.containsKey(bindingStr(resultSet, SSSQLVarNames.userId))){
          userRealms.put(bindingStr(resultSet, SSSQLVarNames.userId), new ArrayList());
        }else{
          
          if(userRealms.get(bindingStr(resultSet, SSSQLVarNames.userId)).contains(bindingStr(resultSet, SSSQLVarNames.realm))){
            continue;
          }
        }
        
        userRealms.get(bindingStr(resultSet, SSSQLVarNames.userId)).add(bindingStr(resultSet, SSSQLVarNames.realm));
      }
      
      return userRealms;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void addUserRealm(
    final SSUri  user, 
    final String realm) throws Exception{
    
    try{
      final Map<String, String> inserts =  new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userId,   user);
      insert(inserts, SSSQLVarNames.realm,    realm);
      
      dbSQL.insert(SSSQLVarNames.recommUserRealmsTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
