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
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
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
      final List<SSUser>        users  = new ArrayList<SSUser>();
      final Map<String, String> wheres  = new HashMap<String, String>();
      
      where(wheres, SSSQLVarU.type, SSEntityE.user);
      
      resultSet = dbSQL.select(entityTable, wheres);
      
      while(resultSet.next()){
        
        users.add(
          SSUser.get(
            bindingStrToUri   (resultSet, SSSQLVarU.id),
            bindingStrToLabel (resultSet, SSSQLVarU.label)));
      }
      
      return users;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public Boolean existsUser(final SSUri userUri) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      final Map<String, String> wheres = new HashMap<String, String>();
      
      where(wheres, SSSQLVarU.id,   userUri);
      where(wheres, SSSQLVarU.type, SSEntityE.user);
      
      resultSet = dbSQL.select(entityTable, wheres);
      
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri getUserURIForLabel(final SSLabel label) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      final Map<String, String> wheres = new HashMap<String, String>();
      
      where(wheres, SSSQLVarU.label,  label);
      where(wheres, SSSQLVarU.type,   SSEntityE.user);
      
      resultSet = dbSQL.select(entityTable, wheres);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarU.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
   
  public SSUser getUser(final SSUri userUri) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      final Map<String, String> wheres = new HashMap<String, String>();
      
      where(wheres, SSSQLVarU.id, userUri);
      
      resultSet = dbSQL.select(entityTable, wheres);
      
      checkFirstResult(resultSet);
      
      return SSUser.get(
        userUri, 
        bindingStrToLabel(resultSet, SSSQLVarU.label));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}