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

package at.tugraz.sss.servs.like.impl;

import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.util.SSSQLVarNames;
import at.tugraz.sss.servs.db.api.SSDBSQLI;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.servs.db.impl.SSCoreSQL;
import at.tugraz.sss.servs.like.datatype.*;

public class SSLikeSQL extends SSCoreSQL{

  public SSLikeSQL(
    final SSDBSQLI dbSQL){
    
    super(dbSQL);
  }
  
  public SSLikes getLikes(
    final SSServPar servPar,
    final SSUri user,
    final SSUri forUser, 
    final SSUri entity) throws SSErr{
    
    ResultSet resultSet = null;
    Integer   value;
    
    try{
    
      final SSLikes             likes     = SSLikes.get(0, 0, null);
      final List<String>        columns   = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.userId); //to be able to apply DISTINCT in select 
      column(columns, SSSQLVarNames.entityId);
      column(columns, SSSQLVarNames.value);
            
      if(forUser != null){
        where(wheres, SSSQLVarNames.userId, forUser);
      }
      
      where (wheres,    SSSQLVarNames.entityId, entity);
      
      resultSet = dbSQL.select(servPar, SSLikeSQLTableE.likes, columns, wheres, null, null, null);
      
      while(resultSet.next()){
        
        value = bindingStrToInteger(resultSet, SSSQLVarNames.value);
        
        if(value == 1){
          likes.likes++;
        }
        
        if(value == -1){
          likes.dislikes++;
        }
      }
      
      if(user != null){
        likes.like = getLike(servPar, user, entity);
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
    final SSServPar servPar,
    final SSUri   user,
    final SSUri   entity,
    final Integer value) throws SSErr{
    
    try{
      
      if(
        value != 1 && 
        value != 0 && 
        value != -1){
        throw new Exception("like value incorrect");
      }
      
      final Integer orignalValue = getLike(servPar, user, entity);
      
      if(orignalValue == null){
        
        final Map<String, String> inserts     = new HashMap<>();
        
        insert(inserts, SSSQLVarNames.userId,      user);
        insert(inserts, SSSQLVarNames.entityId,    entity);
        insert(inserts, SSSQLVarNames.value,       value);
        
        dbSQL.insert(servPar, SSLikeSQLTableE.likes, inserts);
      }else{
        
        final Map<String, String> updates     = new HashMap<>();
        final Map<String, String> wheres      = new HashMap<>();
        
        update(updates, SSSQLVarNames.value, value);
        
        where(wheres, SSSQLVarNames.userId,   user);
        where(wheres, SSSQLVarNames.entityId, entity);
        
        dbSQL.update(servPar, SSLikeSQLTableE.likes, wheres, updates);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Integer getLike(
    final SSServPar servPar,
    final SSUri   user,
    final SSUri   entity) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns   = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      
      column    (columns,   SSSQLVarNames.entityId);
      column    (columns,   SSSQLVarNames.value);
      
      where     (wheres,    SSSQLVarNames.entityId, entity);
      where     (wheres,    SSSQLVarNames.userId,   user);
      
      resultSet = dbSQL.select(servPar, SSLikeSQLTableE.likes, columns, wheres, null, null, null);
      
      while(resultSet.next()){
        return bindingStrToInteger(resultSet, SSSQLVarNames.value);
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
    final SSUri   user) throws SSErr{
    
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
