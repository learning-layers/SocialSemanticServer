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
package at.kc.tugraz.ss.service.userevent.impl.fct.sql;

import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServErrReg;

import at.tugraz.sss.serv.SSServImplWithDBA;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class SSUESQLFct extends SSDBSQLFct{

  public SSUESQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }

  public SSUE getUE(
    final SSUri ue) throws Exception{
    
    ResultSet            resultSet  = null;
    
    try{
      final List<String>         columns = new ArrayList<>();
      final Map<String, String>  wheres  = new HashMap<>();
      SSUEE                      eventTypeFromDB;
      
      column(columns, SSSQLVarNames.userEventId);
      column(columns, SSSQLVarNames.eventType);
      column(columns, SSSQLVarNames.userId);
      column(columns, SSSQLVarNames.entityId);
      column(columns, SSSQLVarNames.content);
      
      where(wheres, SSSQLVarNames.userEventId, ue);
      
      resultSet = dbSQL.select(SSSQLVarNames.uesTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      try{
        eventTypeFromDB = 
          SSUEE.get(bindingStr(resultSet, SSSQLVarNames.eventType));
        
      }catch(Exception error){
        SSLogU.warn("user event type doesnt exist in current model " + bindingStr(resultSet, SSSQLVarNames.eventType));
        eventTypeFromDB = null;
      }
      
      return SSUE.get(
        ue, 
        SSEntity.get(bindingStrToUri (resultSet, SSSQLVarNames.userId), SSEntityE.user), 
        eventTypeFromDB, 
        SSEntity.get(bindingStrToUri (resultSet, SSSQLVarNames.entityId), SSEntityE.entity), 
        bindingStr      (resultSet, SSSQLVarNames.content));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getUEURIs(
    final SSUri       forUser,
    final SSUri       entity,
    final List<SSUEE> eventTypes,
    final Long        startTime,
    final Long        endTime) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>                                           tables        = new ArrayList<>();
      final List<String>                                           columns       = new ArrayList<>();
      final List<String>                                           tableCons     = new ArrayList<>();
      final List<MultivaluedMap<String, String>>                   wheres        = new ArrayList<>();
      final MultivaluedMap<String, MultivaluedMap<String, String>> wheresNumeric = new MultivaluedHashMap<>();
      final List<SSUri>                                            userEventURIs = new ArrayList<>();
      
      column   (columns,   SSSQLVarNames.userEventId);
      column   (columns,   SSSQLVarNames.eventType);
      
      table    (tables, SSSQLVarNames.uesTable);
      table    (tables, SSSQLVarNames.entityTable);
      
      tableCon (tableCons, SSSQLVarNames.uesTable, SSSQLVarNames.userEventId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      if(forUser != null){
                
        final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
        
        where(whereUsers, SSSQLVarNames.uesTable, SSSQLVarNames.userId, forUser);
        
        wheres.add(whereUsers);
      }
      
      if(entity != null){
        
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        where(whereEntities, SSSQLVarNames.uesTable, SSSQLVarNames.entityId, entity);
        
        wheres.add(whereEntities);
      }
      
      if(
        eventTypes != null &&
        !eventTypes.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSUEE eventType : eventTypes){
          where(whereTypes, SSSQLVarNames.uesTable, SSSQLVarNames.eventType, eventType);
        }
        
        wheres.add(whereTypes);
      }
      
      if(
        startTime != null &&
        startTime != 0){
        
        final List<MultivaluedMap<String, String>> greaterWheres           = new ArrayList<>();
        final MultivaluedMap<String, String>       whereNumbericStartTimes = new MultivaluedHashMap<>();
        
        wheresNumeric.put(SSStrU.greaterThan, greaterWheres);
        
        where(whereNumbericStartTimes, SSSQLVarNames.entityTable, SSSQLVarNames.creationTime, startTime);
        
        greaterWheres.add(whereNumbericStartTimes);
      }
      
      if(
        endTime != null &&
        endTime != 0){
        
        final List<MultivaluedMap<String, String>> lessWheres            = new ArrayList<>();
        final MultivaluedMap<String, String>       whereNumbericEndTimes = new MultivaluedHashMap<>();
        
        wheresNumeric.put(SSStrU.lessThan, lessWheres);
        
        where(whereNumbericEndTimes, SSSQLVarNames.entityTable, SSSQLVarNames.creationTime, endTime);
        
        lessWheres.add(whereNumbericEndTimes);
      }
      
      if(!wheresNumeric.isEmpty()){
        resultSet = dbSQL.select(tables, columns, wheres, wheresNumeric, tableCons, null, null, null);
      }else{
        resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      }
      
      while(resultSet.next()){
        
        try{
          SSUEE.get(bindingStr(resultSet, SSSQLVarNames.eventType));
        }catch(Exception error){
          SSLogU.warn("user event type doesnt exist in current model " + bindingStr(resultSet, SSSQLVarNames.eventType));
          continue;
        }
        
        //TODO check whether really needed
//        try{
//          timestamp = bindingStrToLong(resultSet, SSSQLVarU.creationTime);
//        }catch(Exception error){
//          SSLogU.warn("timestamp isn't valid " + bindingStrToLong(resultSet, SSSQLVarU.creationTime));
//          continue;
//        }
        
        userEventURIs.add(bindingStrToUri       (resultSet, SSSQLVarNames.userEventId));
      }
      
      return userEventURIs;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void addUE(
    final SSUri    ue, 
    final SSUri    user, 
    final SSUri    entity, 
    final SSUEE    eventType, 
    final String   content) throws Exception{

    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userEventId,   ue);
      insert(inserts, SSSQLVarNames.userId,        user);
      insert(inserts, SSSQLVarNames.entityId,      entity);
      insert(inserts, SSSQLVarNames.eventType,     eventType);
      
      if(content != null){
        insert(inserts, SSSQLVarNames.content,       content);
      }else{
        insert(inserts, SSSQLVarNames.content,       SSStrU.empty);
      }

      dbSQL.insert(SSSQLVarNames.uesTable, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
