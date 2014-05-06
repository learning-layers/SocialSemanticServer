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
package at.kc.tugraz.ss.serv.dataimport.impl.fct.sql;

import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SSDataImportSQLFct extends SSDBSQLFct{
  
  public SSDataImportSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  public void addUserWithGroup(
    final String userName,
    final String password) throws Exception{
    
    final String userId;
    ResultSet    resultSet               = null;
    
    try{
      final Map<String, String>          whereParNamesWithValues = new HashMap<String, String>();
      final Statement                    statement               = dbSQL.getConnection().createStatement();
      
      statement.execute("INSERT INTO user (user_name,user_options,user_password,user_email,user_newpassword) values ('" + userName + "','','','','');");
      
      whereParNamesWithValues.put("user_name", userName);
      
      resultSet = dbSQL.select("user", whereParNamesWithValues);
      resultSet.first();
      userId    = resultSet.getString("user_id");
      
      statement.execute("UPDATE user SET user_password = MD5(CONCAT(" + userId + ", '-', MD5('" + password + "'))) WHERE user_name = '" + userName + "'");
      statement.execute("INSERT INTO user_groups (ug_user, ug_group) values ('" + userId + "','group')");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
