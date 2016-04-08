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
package at.tugraz.sss.servs.recomm.impl;

import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.util.SSSQLVarNames;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.db.api.SSCoreSQL;
import at.tugraz.sss.servs.recomm.datatype.*;

public class SSRecommSQL extends SSCoreSQL{
  
  public SSRecommSQL(
    final SSDBSQLI dbSQL) {
    
    super(dbSQL);
  }
  
  public Map<String, List<String>> getUserRealms(
    final SSServPar servPar) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final Map<String, List<String>> userRealms = new HashMap<>();
      final List<String>              columns    = new ArrayList<>();
      final Map<String, String>       wheres     = new HashMap<>();
      
      column(columns, SSSQLVarNames.userId);
      column(columns, SSSQLVarNames.realm);
      
      resultSet = dbSQL.select(servPar, SSRecommSQLTableE.recommuserrealms, columns, wheres, null, null, null);

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
    final SSServPar servPar,
    final SSUri     user, 
    final String    realm) throws SSErr{
    
    try{
      final Map<String, String> inserts =  new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userId,   user);
      insert(inserts, SSSQLVarNames.realm,    realm);
      
      dbSQL.insert(servPar, SSRecommSQLTableE.recommuserrealms, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
