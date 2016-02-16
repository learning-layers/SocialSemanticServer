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

import at.tugraz.sss.serv.util.SSSQLVarNames;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.tugraz.sss.conf.*;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.par.SSDBSQLSelectPar;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import at.tugraz.sss.serv.db.api.SSCoreSQL;

public class SSUESQLFct extends SSCoreSQL{

  public SSUESQLFct(
    final SSDBSQLI dbSQL){
    
    super(dbSQL);
  }

  public SSUE getUE(
    final SSServPar servPar,
    final SSUri ue) throws SSErr{
    
    ResultSet            resultSet  = null;
    
    try{
      final List<String>         columns   = new ArrayList<>();
      final List<String>         tables    = new ArrayList<>();
      final Map<String, String>  wheres    = new HashMap<>();
      final List<String>         tableCons = new ArrayList<>();
      
      setEntityColumns(columns);
      column(columns, SSSQLVarNames.uesTable, SSSQLVarNames.userEventId);
      column(columns, SSSQLVarNames.uesTable, SSSQLVarNames.eventType);
      column(columns, SSSQLVarNames.uesTable, SSSQLVarNames.userId);
      column(columns, SSSQLVarNames.uesTable, SSSQLVarNames.entityId);
      column(columns, SSSQLVarNames.uesTable, SSSQLVarNames.content);
      
      setEntityTable  (tables);
      table(tables, SSSQLVarNames.uesTable);
      
      where(wheres, SSSQLVarNames.uesTable, SSSQLVarNames.userEventId, ue);
      
      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.uesTable, SSSQLVarNames.userEventId);
      
      resultSet = 
        dbSQL.select(
          servPar,
          tables, 
          columns, 
          wheres, 
          tableCons, 
          null, 
          null,
          null);
        
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
//      try{
//        eventTypeFromDB = SSUEE.get(bindingStr(resultSet, SSSQLVarNames.eventType));
//      }catch(Exception error){
//        SSLogU.warn("user event type doesnt exist in current model " + bindingStr(resultSet, SSSQLVarNames.eventType));
//        eventTypeFromDB = null;
//      }
      
      return SSUE.get(
        bindingStrToUri              (resultSet, SSSQLVarNames.id),
        bindingStrToLabel            (resultSet, SSSQLVarNames.label),
        bindingStrToTextComment      (resultSet, SSSQLVarNames.description),
        bindingStrToLong             (resultSet, SSSQLVarNames.creationTime),
        getAuthorEntityFromResult    (servPar, resultSet, SSConf.systemUserUri),
        bindingStrToEntity           (resultSet, SSSQLVarNames.userId,   SSEntityE.user), 
        SSUEE.get(bindingStr         (resultSet, SSSQLVarNames.eventType)),
        bindingStrToEntity           (resultSet, SSSQLVarNames.entityId, SSEntityE.entity), 
        bindingStr                   (resultSet, SSSQLVarNames.content));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getUEURIs(
    final SSServPar servPar,
    final SSUri       forUser,
    final SSUri       entity,
    final List<SSUEE> eventTypes,
    final Long        startTime,
    final Long        endTime) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>                                           tables        = new ArrayList<>();
      final List<String>                                           columns       = new ArrayList<>();
      final List<String>                                           tableCons     = new ArrayList<>();
      final List<MultivaluedMap<String, String>>                   wheres        = new ArrayList<>();
      final MultivaluedMap<String, MultivaluedMap<String, String>> wheresNumeric = new MultivaluedHashMap<>();
      
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
      
      return getURIsFromResult(resultSet, SSSQLVarNames.userEventId);
      
//      while(resultSet.next()){
//        
//        try{
//          SSUEE.get(bindingStr(resultSet, SSSQLVarNames.eventType));
//        }catch(Exception error){
//          SSLogU.warn("user event type doesnt exist in current model " + bindingStr(resultSet, SSSQLVarNames.eventType));
//          continue;
//        }
//
//        userEventURIs.add(bindingStrToUri       (resultSet, SSSQLVarNames.userEventId));
//      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void addUE(
    final SSServPar servPar,
    final SSUri    ue, 
    final SSUri    user, 
    final SSUri    entity, 
    final SSUEE    eventType, 
    final String   content) throws SSErr{

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

      dbSQL.insert(servPar, SSSQLVarNames.uesTable, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}