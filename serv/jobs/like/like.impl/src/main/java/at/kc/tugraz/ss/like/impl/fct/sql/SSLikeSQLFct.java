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
package at.kc.tugraz.ss.like.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.like.datatypes.SSLikes;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSLikeSQLFct extends SSDBSQLFct{

  public SSLikeSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  public SSLikes getLikes(
    final SSUri user,
    final SSUri forUser, 
    final SSUri entity) throws Exception{
    
    ResultSet resultSet = null;
    Integer   value;
    
    try{
    
      final SSLikes             likes     = SSLikes.get(0, 0, null);
      final List<String>        columns   = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      
      if(forUser != null){
        where(wheres, SSSQLVarU.userId, forUser);
      }
      
      column(columns, SSSQLVarU.entityId);
      column(columns, SSSQLVarU.value);
      
      where (wheres,    SSSQLVarU.entityId, entity);
      
      resultSet = dbSQL.select(likesTable, columns, wheres, null, null);
      
      while(resultSet.next()){
        
        value = bindingStrToInteger(resultSet, SSSQLVarU.value);
        
        if(value == 1){
          likes.likes++;
        }
        
        if(value == -1){
          likes.dislikes++;
        }
      }
      
      if(user != null){
        likes.like = getLike(user, entity);
      }
      
      return likes;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void like(
    final SSUri   user,
    final SSUri   entity,
    final Integer value) throws Exception{
    
    try{
      
      if(
        value != 1 && 
        value != 0 && 
        value != -1){
        throw new Exception("like value incorrect");
      }
      
      final Integer orignalValue = getLike(user, entity);
      
      if(orignalValue == null){
        
        final Map<String, String> inserts     = new HashMap<>();
        
        insert(inserts, SSSQLVarU.userId,      user);
        insert(inserts, SSSQLVarU.entityId,    entity);
        insert(inserts, SSSQLVarU.value,       value);
        
        dbSQL.insert(likesTable, inserts);
      }else{
        
        final Map<String, String> updates     = new HashMap<>();
        final Map<String, String> wheres      = new HashMap<>();
        
        update(updates, SSSQLVarU.value, value);
        
        where(wheres, SSSQLVarU.userId,   user);
        where(wheres, SSSQLVarU.entityId, entity);
        
        dbSQL.update(likesTable, wheres, updates);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Integer getLike(
    final SSUri   user,
    final SSUri   entity) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns   = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      
      column    (columns,   SSSQLVarU.entityId);
      column    (columns,   SSSQLVarU.value);
      
      where     (wheres,    SSSQLVarU.entityId, entity);
      where     (wheres,    SSSQLVarU.userId,   user);
      
      resultSet = dbSQL.select(likesTable, columns, wheres, null, null);
      
      while(resultSet.next()){
        return bindingStrToInteger(resultSet, SSSQLVarU.value);
      }
      
      return null;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
 /* public List<SSFriend> getFriends(
    final SSUri   user) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSFriend>      friends   = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      
      where     (wheres,    SSSQLVarU.userId, user);
      
      resultSet = dbSQL.select(friendsTable, wheres);
      
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
  
  */
}
