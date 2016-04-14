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

package at.tugraz.sss.servs.message.impl;

import at.tugraz.sss.servs.db.api.SSSQLTableI;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSEntitySQLTableE;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.util.SSSQLVarNames;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.db.api.SSDBSQLFctA;
import at.tugraz.sss.servs.db.api.SSDBSQLI;
import at.tugraz.sss.servs.db.datatype.SSDBSQLSelectPar;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.message.datatype.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class SSMessageSQL extends SSDBSQLFctA{

  public SSMessageSQL(final SSDBSQLI dbSQL) {
    super(dbSQL);
  }

  public void sendMessage(
    final SSServPar servPar,
    final SSUri         message,
    final SSUri         user,
    final SSUri         forUser, 
    final SSTextComment messageContent) throws SSErr{
    
    try{
      final Map<String, String> inserts =  new HashMap<>();
      
      insert(inserts, SSSQLVarNames.messageId,      message);
      insert(inserts, SSSQLVarNames.userId,         user);
      insert(inserts, SSSQLVarNames.forEntityId,    forUser);
      insert(inserts, SSSQLVarNames.messageContent, messageContent);
      
      dbSQL.insert(servPar, SSMessageSQLTableE.message, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public SSMessage getMessage(
    final SSServPar servPar,
    final SSUri   messageURI) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      if(messageURI == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final Map<String, String>                                    wheres         = new HashMap<>();
      final List<String>                                           columns        = new ArrayList<>();
      
      column    (columns, SSMessageSQLTableE.message,       SSSQLVarNames.userId);
      column    (columns, SSMessageSQLTableE.message,       SSSQLVarNames.messageId);
      column    (columns, SSMessageSQLTableE.message,       SSSQLVarNames.messageContent);
      column    (columns, SSMessageSQLTableE.message,       SSSQLVarNames.forEntityId);

      where(wheres, SSMessageSQLTableE.message, SSSQLVarNames.messageId, messageURI);
      
      resultSet = dbSQL.select(servPar, SSMessageSQLTableE.message, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
        
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
    final SSServPar servPar,
    final SSUri   targetUserURI,
    final Long    startTime) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSSQLTableI>                                      tables         = new ArrayList<>();
      final List<MultivaluedMap<String, String>>                   wheres         = new ArrayList<>();
      final MultivaluedMap<String, MultivaluedMap<String, String>> wheresNumeric  = new MultivaluedHashMap<>();
      final List<String>                                           columns        = new ArrayList<>();
      final List<String>                                           tableCons      = new ArrayList<>();
      
      column    (columns, SSMessageSQLTableE.message,       SSSQLVarNames.messageId);

      table     (tables, SSMessageSQLTableE.message);
      table     (tables, SSEntitySQLTableE.entity);     
      
      tableCon  (tableCons, SSMessageSQLTableE.message, SSSQLVarNames.messageId, SSEntitySQLTableE.entity, SSSQLVarNames.id);
       
      if(targetUserURI != null){

        final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
        
        where(whereUsers, SSMessageSQLTableE.message, SSSQLVarNames.forEntityId, targetUserURI);
        
        wheres.add(whereUsers);
      }

      if(
        startTime != null &&
        startTime != 0){

        final List<MultivaluedMap<String, String>> greaterWheres           = new ArrayList<>();
        final MultivaluedMap<String, String>       whereNumbericStartTimes = new MultivaluedHashMap<>();

        wheresNumeric.put(SSStrU.greaterThan, greaterWheres);
        
        where(whereNumbericStartTimes, SSEntitySQLTableE.entity, SSSQLVarNames.creationTime, startTime);
        
        greaterWheres.add(whereNumbericStartTimes);
      }
      
      resultSet =
        dbSQL.select(
          new SSDBSQLSelectPar(
            servPar, 
            tables,
            columns,
            wheres,
            null,
            wheresNumeric,
            tableCons));
      
      return getURIsFromResult(resultSet, SSSQLVarNames.messageId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
