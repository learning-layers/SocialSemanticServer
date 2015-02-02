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

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
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
      
      column(columns, SSSQLVarU.userEventId);
      column(columns, SSSQLVarU.eventType);
      column(columns, SSSQLVarU.userId);
      column(columns, SSSQLVarU.entityId);
      column(columns, SSSQLVarU.content);
      
      where(wheres, SSSQLVarU.userEventId, ue);
      
      resultSet = dbSQL.select(uesTable, columns, wheres, null, null);
      
      checkFirstResult(resultSet);
      
      try{
        eventTypeFromDB = 
          SSUEE.get(
            bindingStr(resultSet, SSSQLVarU.eventType));
        
      }catch(Exception error){
        SSLogU.warn("user event type doesnt exist in current model " + bindingStr(resultSet, SSSQLVarU.eventType));
        eventTypeFromDB = null;
      }
      
      return SSUE.get(
        ue, 
        bindingStrToUri (resultSet, SSSQLVarU.userId), 
        eventTypeFromDB, 
        bindingStrToUri (resultSet, SSSQLVarU.entityId), 
        bindingStr      (resultSet, SSSQLVarU.content));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  //TODO dtheiler: include start and end date in sql query already
  public List<SSUE> getUEs(
    final SSUri       forUser,
    final SSUri       entity,
    final List<SSUEE> eventTypes,
    final Long        startTime,
    final Long        endTime) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>                         tables       = new ArrayList<>();
      final List<String>                         columns      = new ArrayList<>();
      final List<String>                         tableCons    = new ArrayList<>();
      final List<MultivaluedMap<String, String>> wheres       = new ArrayList<>();
      final List<SSUE>                           ues          = new ArrayList<>();
      SSUE                                       ueObj;
      SSUEE                                      eventTypeFromDB;
      Long                                       timestamp;
      
      column   (columns,   SSSQLVarU.userEventId);
      column   (columns,   SSSQLVarU.userId);
      column   (columns,   SSSQLVarU.entityId);
//      column   (columns,   SSSQLVarU.id);
      column   (columns,   SSSQLVarU.content);
      column   (columns,   SSSQLVarU.creationTime);
      column   (columns,   SSSQLVarU.eventType);
      
      table    (tables,    uesTable);
      table    (tables,    entityTable);
      
      tableCon (tableCons, uesTable, SSSQLVarU.userEventId, entityTable, SSSQLVarU.id);
      
      if(forUser != null){
                
        final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
        
        where(whereUsers, uesTable, SSSQLVarU.userId, forUser);
        
        wheres.add(whereUsers);
      }
      
      if(entity != null){
        
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        where(whereEntities, uesTable, SSSQLVarU.entityId, entity);
        
        wheres.add(whereEntities);
      }
      
      if(
        eventTypes != null &&
        !eventTypes.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSUEE eventType : eventTypes){
          where(whereTypes, uesTable, SSSQLVarU.eventType, eventType);
        }
        
        wheres.add(whereTypes);
      }

      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null);
      
      while(resultSet.next()){
        
        try{
          eventTypeFromDB = SSUEE.get(bindingStr(resultSet, SSSQLVarU.eventType));
        }catch(Exception error){
          SSLogU.warn("user event type doesnt exist in current model " + bindingStr(resultSet, SSSQLVarU.eventType));
          continue;
        }
        
        try{
          timestamp = bindingStrToLong(resultSet, SSSQLVarU.creationTime);
        }catch(Exception error){
          SSLogU.warn("timestamp isn't valid " + bindingStrToLong(resultSet, SSSQLVarU.creationTime));
          continue;
        }
        
        if(
          startTime != null &&
          startTime != 0    &&
          timestamp <= startTime){
          continue;
        }
        
        if(
          endTime != null &&
          endTime != 0    &&
          timestamp >= endTime){
          continue;
        }
        
        ueObj = 
         SSUE.get(
           bindingStrToUri       (resultSet, SSSQLVarU.userEventId), 
           bindingStrToUri       (resultSet, SSSQLVarU.userId), 
           eventTypeFromDB, 
           bindingStrToUri       (resultSet, SSSQLVarU.entityId), 
           bindingStr            (resultSet, SSSQLVarU.content));
        
        ueObj.creationTime = timestamp;

        ues.add(ueObj);
      }
      
      return ues;
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
      
      insert(inserts, SSSQLVarU.userEventId,   ue);
      insert(inserts, SSSQLVarU.userId,        user);
      insert(inserts, SSSQLVarU.entityId,      entity);
      insert(inserts, SSSQLVarU.eventType,     eventType);
      
      if(content == null){
        insert(inserts, SSSQLVarU.content,       content);
      }else{
        insert(inserts, SSSQLVarU.content,       SSStrU.empty);
      }

      dbSQL.insert(uesTable, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
