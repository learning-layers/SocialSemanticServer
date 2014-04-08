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
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.user.api.SSUserGlobals;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSUserSQLFct extends SSDBSQLFct{
  
  public SSUserSQLFct(SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }
  
  public SSUri createUserUri(SSLabelStr userLabel) throws Exception{
    return SSUri.get(objUser().toString() + userLabel);
  }

  public List<SSUri> userAll() throws Exception {
    
    final List<SSUri> users                            = new ArrayList<SSUri>();
    final Map<String, String> whereParNamesWithValues  = new HashMap<String, String>();
    ResultSet                 resultSet                = null;
    
    try{
      
      whereParNamesWithValues.put(SSSQLVarU.type, SSEntityEnum.user.toString());
      
      resultSet = dbSQL.selectAllWhere(entityTable, whereParNamesWithValues);

      while(resultSet.next()){
        users.add(bindingStrToUri(resultSet, SSSQLVarU.id));
      }
      
      return users;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public SSUri userSystem() throws Exception{
    return SSUri.get(objUser() + SSUserGlobals.systemUserLabel);
  }
   
  private SSUri objUser() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.user.toString());
  }  
}