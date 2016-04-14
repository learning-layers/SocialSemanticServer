/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.friend.impl;

import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSEntitySQLTableE;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.util.SSSQLVarNames;
import at.tugraz.sss.servs.db.api.SSDBSQLFctA;
import at.tugraz.sss.servs.db.api.SSDBSQLI;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.friend.datatype.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSFriendSQL extends SSDBSQLFctA{

  public SSFriendSQL(final SSDBSQLI dbSQL) {
    super(dbSQL);
  }

  public void addFriend(
    final SSServPar servPar,
    final SSUri         user,
    final SSUri         friend) throws SSErr{
    
    try{
      final Map<String, String> inserts     = new HashMap<>();
      final Map<String, String> uniqueKeys  = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userId,      user);
      insert(inserts, SSSQLVarNames.friendId,    friend);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.userId,   user);
      uniqueKey(uniqueKeys, SSSQLVarNames.friendId, friend);
      
      dbSQL.insertIfNotExists(servPar, SSFriendSQLTableE.friends, inserts, uniqueKeys);
      
      inserts.clear();
      uniqueKeys.clear();
      
      insert(inserts, SSSQLVarNames.userId,      friend);
      insert(inserts, SSSQLVarNames.friendId,    user);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.userId,   friend);
      uniqueKey(uniqueKeys, SSSQLVarNames.friendId, user);
      
      dbSQL.insertIfNotExists(servPar, SSFriendSQLTableE.friends, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public SSFriend getFriend(
    final SSServPar servPar,
    final SSUri   friend) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{

      if(friend == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<String>        columns   = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.id);

      where     (wheres,    SSSQLVarNames.id, friend);
      
      resultSet = dbSQL.select(servPar, SSEntitySQLTableE.entity, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return SSFriend.get(
        bindingStrToUri(resultSet, SSSQLVarNames.id));
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getFriends(
    final SSServPar servPar,
    final SSUri   user) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{

      if(user == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<String>        columns   = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.userId);
      column(columns, SSSQLVarNames.friendId);
      
      where     (wheres,    SSSQLVarNames.userId, user);
      
      resultSet = dbSQL.select(servPar, SSFriendSQLTableE.friends, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.friendId);
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
