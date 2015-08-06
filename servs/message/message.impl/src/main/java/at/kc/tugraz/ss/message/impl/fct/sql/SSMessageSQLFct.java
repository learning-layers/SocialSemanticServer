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

import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.message.datatypes.SSMessage;
import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

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
      
      insert(inserts, SSSQLVarNames.messageId,      message);
      insert(inserts, SSSQLVarNames.userId,         user);
      insert(inserts, SSSQLVarNames.forEntityId,    forUser);
      insert(inserts, SSSQLVarNames.messageContent, messageContent);
      
      dbSQL.insert(SSSQLVarNames.messageTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public SSMessage getMessage(
    final SSUri   messageURI) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(messageURI == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final Map<String, String>                                    wheres         = new HashMap<>();
      final List<String>                                           columns        = new ArrayList<>();
      
      column    (columns, SSSQLVarNames.messageTable,       SSSQLVarNames.userId);
      column    (columns, SSSQLVarNames.messageTable,       SSSQLVarNames.messageId);
      column    (columns, SSSQLVarNames.messageTable,       SSSQLVarNames.messageContent);
      column    (columns, SSSQLVarNames.messageTable,       SSSQLVarNames.forEntityId);

      where(wheres, SSSQLVarNames.messageTable, SSSQLVarNames.messageId, messageURI);
      
      resultSet = dbSQL.select(SSSQLVarNames.messageTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
        
      return SSMessage.get(
        bindingStrToUri        (resultSet, SSSQLVarNames.messageId), 
        SSEntity.get           (bindingStrToUri(resultSet, SSSQLVarNames.userId),      SSEntityE.user), 
        SSEntity.get           (bindingStrToUri(resultSet, SSSQLVarNames.forEntityId), SSEntityE.user),
        bindingStrToTextComment(resultSet, SSSQLVarNames.messageContent));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getMessageURIs(
    final SSUri   targetUserURI,
    final Long    startTime) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(targetUserURI == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final List<String>                                           tables         = new ArrayList<>();
      final List<MultivaluedMap<String, String>>                   wheres         = new ArrayList<>();
      final MultivaluedMap<String, MultivaluedMap<String, String>> wheresNumeric  = new MultivaluedHashMap<>();
      final List<String>                                           columns        = new ArrayList<>();
      final List<String>                                           tableCons      = new ArrayList<>();
      
      column    (columns, SSSQLVarNames.messageTable,       SSSQLVarNames.messageId);

      table     (tables, SSSQLVarNames.messageTable);
      table     (tables, SSSQLVarNames.entityTable);     
      
      tableCon  (tableCons, SSSQLVarNames.messageTable, SSSQLVarNames.messageId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
       
      final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
      
      where(whereUsers, SSSQLVarNames.messageTable, SSSQLVarNames.forEntityId, targetUserURI);
      
      wheres.add(whereUsers);

      if(
        startTime != null &&
        startTime != 0){

        final List<MultivaluedMap<String, String>> greaterWheres           = new ArrayList<>();
        final MultivaluedMap<String, String>       whereNumbericStartTimes = new MultivaluedHashMap<>();

        wheresNumeric.put(SSStrU.greaterThan, greaterWheres);
        
        where(whereNumbericStartTimes, SSSQLVarNames.entityTable, SSSQLVarNames.creationTime, startTime);
        
        greaterWheres.add(whereNumbericStartTimes);
      }
      
      if(!wheresNumeric.isEmpty()){
        resultSet = dbSQL.select(tables, columns, wheres, wheresNumeric, tableCons, null, null, null);
      }else{
        resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      }
      
      return getURIsFromResult(resultSet, SSSQLVarNames.messageId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
