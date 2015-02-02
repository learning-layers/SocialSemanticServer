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
package at.kc.tugraz.ss.friend.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.friend.datatypes.SSFriend;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSFriendSQLFct extends SSDBSQLFct{

  public SSFriendSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }

  public void addFriend(
    final SSUri         user,
    final SSUri         friend) throws Exception{
    
    try{
      final Map<String, String> inserts     = new HashMap<>();
      final Map<String, String> uniqueKeys  = new HashMap<>();
      
      insert(inserts, SSSQLVarU.userId,      user);
      insert(inserts, SSSQLVarU.friendId,    friend);
      
      uniqueKey(uniqueKeys, SSSQLVarU.userId,   user);
      uniqueKey(uniqueKeys, SSSQLVarU.friendId, friend);
      
      dbSQL.insertIfNotExists(friendsTable, inserts, uniqueKeys);
      
      inserts.clear();
      uniqueKeys.clear();
      
      insert(inserts, SSSQLVarU.userId,      friend);
      insert(inserts, SSSQLVarU.friendId,    user);
      
      uniqueKey(uniqueKeys, SSSQLVarU.userId,   friend);
      uniqueKey(uniqueKeys, SSSQLVarU.friendId, user);
      
      dbSQL.insertIfNotExists(friendsTable, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public List<SSFriend> getFriends(
    final SSUri   user) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSFriend>      friends   = new ArrayList<>();
      final List<String>        columns   = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      
      column(columns, SSSQLVarU.userId);
      column(columns, SSSQLVarU.friendId);
      
      where     (wheres,    SSSQLVarU.userId, user);
      
      resultSet = dbSQL.select(friendsTable, columns, wheres, null, null);
      
      while(resultSet.next()){
        
        friends.add(
          SSFriend.get(
            bindingStrToUri(resultSet, SSSQLVarU.friendId)));
      }
      
      return friends;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
