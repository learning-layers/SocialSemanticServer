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
package at.tugraz.sss.servs.friend.impl;

import at.kc.tugraz.ss.friend.datatypes.SSFriend;
import at.tugraz.sss.serv.util.SSSQLVarNames;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.db.api.SSDBSQLFctA;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
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
      
      dbSQL.insertIfNotExists(servPar, SSSQLVarNames.friendsTable, inserts, uniqueKeys);
      
      inserts.clear();
      uniqueKeys.clear();
      
      insert(inserts, SSSQLVarNames.userId,      friend);
      insert(inserts, SSSQLVarNames.friendId,    user);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.userId,   friend);
      uniqueKey(uniqueKeys, SSSQLVarNames.friendId, user);
      
      dbSQL.insertIfNotExists(servPar, SSSQLVarNames.friendsTable, inserts, uniqueKeys);
      
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
      
      resultSet = dbSQL.select(servPar, SSSQLVarNames.entityTable, columns, wheres, null, null, null);
      
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
      
      resultSet = dbSQL.select(servPar, SSSQLVarNames.friendsTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.friendId);
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
