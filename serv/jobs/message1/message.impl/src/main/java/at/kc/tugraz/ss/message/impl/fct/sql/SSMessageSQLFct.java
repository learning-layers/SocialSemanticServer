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
package at.kc.tugraz.ss.message.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.message.datatypes.SSMessage;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSMessageSQLFct extends SSDBSQLFct{

  public SSMessageSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }

  public void sendMessage(
    final SSUri         message,
    final SSUri         user,
    final SSUri         forUser, 
    final SSTextComment messageContent) throws Exception{
    
    try{
      final Map<String, String> inserts =  new HashMap<>();
      
      insert(inserts, SSSQLVarU.messageId,      message);
      insert(inserts, SSSQLVarU.userId,         user);
      insert(inserts, SSSQLVarU.forEntityId,    forUser);
      insert(inserts, SSSQLVarU.messageContent, messageContent);
      
      dbSQL.insert(messageTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public List<SSMessage> getMessages(
    final SSUri   user,
    final Long    startTime) throws Exception{
    
    ResultSet resultSet = null;
    Long      creationTime;
    
    try{
      
      final List<SSMessage>     messages  = new ArrayList<>();
      final List<String>        tables    = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        columns   = new ArrayList<>();
      final List<String>        tableCons = new ArrayList<>();
      
      table     (tables,    messageTable);
      table     (tables,    entityTable);     
      column    (columns,   messageTable,       SSSQLVarU.userId);
      column    (columns,   messageTable,       SSSQLVarU.messageId);
      column    (columns,   messageTable,       SSSQLVarU.messageContent);
      column    (columns,   entityTable,        SSSQLVarU.creationTime);
      
      where     (wheres,    SSSQLVarU.forEntityId, user);
      
      tableCon  (tableCons, messageTable, SSSQLVarU.messageId, entityTable, SSSQLVarU.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        creationTime = bindingStrToLong(resultSet, SSSQLVarU.creationTime);
          
        if(
          startTime    != null &&
          startTime    != 0    &&
          creationTime <= startTime){
          continue;
        }
        
        messages.add(
          SSMessage.get(
            bindingStrToUri(resultSet, SSSQLVarU.messageId), 
            bindingStrToUri(resultSet, SSSQLVarU.userId), 
            user, 
            bindingStrToTextComment(resultSet, SSSQLVarU.messageContent),
            creationTime));
      }
      
      return messages;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
