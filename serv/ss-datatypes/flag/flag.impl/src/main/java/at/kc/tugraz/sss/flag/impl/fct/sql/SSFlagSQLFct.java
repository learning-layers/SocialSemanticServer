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
package at.kc.tugraz.sss.flag.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.sss.flag.datatypes.SSFlag;
import at.kc.tugraz.sss.flag.datatypes.SSFlagE;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSFlagSQLFct extends SSDBSQLFct{

  public SSFlagSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  public List<SSFlag> getFlags(
    final List<SSUri>   users,
    final List<SSUri>   entities,
    final List<SSFlagE> types,
    final Long          startTime,
    final Long          endTime) throws Exception{
    
    ResultSet resultSet = null;
      
    try{
      final List<SSFlag>              flags          = new ArrayList<>();
      final List<Map<String, String>> wheres         = new ArrayList<>();
      final List<String>              tables         = new ArrayList<>();
      final List<String>              columns        = new ArrayList<>();
      final List<String>              tableCons      = new ArrayList<>();
      Long                            timestamp;
      Long                            endTimeForFlag;
      Integer                         value;

      table    (tables,    flagTable);
      table    (tables,    flagsTable);
      table    (tables,    entityTable);
      
      column   (columns,   entityTable,   SSSQLVarU.id);
      column   (columns,   entityTable,   SSSQLVarU.creationTime);
      column   (columns,   flagTable,     SSSQLVarU.type);
      column   (columns,   flagTable,     SSSQLVarU.endTime);
      column   (columns,   flagTable,     SSSQLVarU.value);
      column   (columns,   flagsTable,    SSSQLVarU.userId);
      column   (columns,   flagsTable,    SSSQLVarU.entityId);

      tableCon (tableCons, flagTable,  SSSQLVarU.flagId, entityTable, SSSQLVarU.id);
      tableCon (tableCons, flagsTable, SSSQLVarU.flagId, flagTable,   SSSQLVarU.flagId);
      
      if(
        users != null &&
        !users.isEmpty()){

        final Map<String, String> whereUsers = new HashMap<>();
        
        for(SSUri user : users){
          where(whereUsers, flagsTable, SSSQLVarU.userId, user);
        }
        
        wheres.add(whereUsers);
      }
      
      if(
        entities != null &&
        !entities.isEmpty()){

        final Map<String, String> whereEntities = new HashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, flagsTable, SSSQLVarU.entityId, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final Map<String, String> whereTypes = new HashMap<>();
        
        for(SSFlagE type : types){
          where(whereTypes, flagTable, SSSQLVarU.type, type);
        }
        
        wheres.add(whereTypes);
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      while(resultSet.next()){
        
        timestamp = bindingStrToLong (resultSet, SSSQLVarU.creationTime);
        
        if(
          startTime != null &&
          timestamp < startTime){
          continue;
        }
        
        if(
          endTime != null &&
          timestamp > endTime){
          continue;
        }
        
        try{
          endTimeForFlag = bindingStrToLong(resultSet, SSSQLVarU.endTime);
        }catch(Exception error){
          endTimeForFlag = null;
        }
        
        try{
          value = bindingStrToInteger(resultSet, SSSQLVarU.value);
        }catch(Exception error){
          value = null;
        }
        
        flags.add(
          SSFlag.get(
            bindingStrToUri  (resultSet, SSSQLVarU.id),
            bindingStrToUri  (resultSet, SSSQLVarU.userId),
            bindingStrToUri  (resultSet, SSSQLVarU.entityId), 
            SSFlagE.get      (bindingStr(resultSet, SSSQLVarU.type)), 
            timestamp,
            endTimeForFlag,
            value));
      }
      
      return flags;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void createFlag(
    final SSUri   flag,
    final SSFlagE type,
    final Long    endTime,
    final Integer value) throws Exception{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert    (inserts,    SSSQLVarU.flagId,    flag);
      insert    (inserts,    SSSQLVarU.type,      type);
      
      if(endTime == null){
        insert    (inserts,    SSSQLVarU.endTime,   SSStrU.empty);
      }else{
        insert    (inserts,    SSSQLVarU.endTime,   endTime);
      }
      
      if(value == null){
        insert    (inserts,    SSSQLVarU.value,     SSStrU.empty);
      }else{
       insert    (inserts,     SSSQLVarU.value,     value); 
      }
      
      uniqueKey (uniqueKeys, SSSQLVarU.flagId,    flag);
      
      dbSQL.insertIfNotExists(flagTable, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addFlagAssIfNotExists(
    final SSUri         user, 
    final SSUri         flag, 
    final SSUri         entity) throws Exception{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert    (inserts,    SSSQLVarU.userId,    user);
      insert    (inserts,    SSSQLVarU.entityId,  entity);
      insert    (inserts,    SSSQLVarU.flagId,    flag);
      uniqueKey (uniqueKeys, SSSQLVarU.userId,    user);
      uniqueKey (uniqueKeys, SSSQLVarU.entityId,  entity);
      uniqueKey (uniqueKeys, SSSQLVarU.flagId,    flag);
      
      dbSQL.insertIfNotExists(flagsTable, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}