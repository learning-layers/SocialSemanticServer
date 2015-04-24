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

import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSDBSQLI;

import at.kc.tugraz.sss.flag.datatypes.SSFlag;
import at.kc.tugraz.sss.flag.datatypes.SSFlagE;
import at.tugraz.sss.serv.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

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
      final List<SSFlag>                         flags          = new ArrayList<>();
      final List<MultivaluedMap<String, String>> wheres         = new ArrayList<>();
      final List<String>                         tables         = new ArrayList<>();
      final List<String>                         columns        = new ArrayList<>();
      final List<String>                         tableCons      = new ArrayList<>();
      Long                                       timestamp;
      Long                                       endTimeForFlag;
      Integer                                    value;
      SSFlag                                     flagObj;

      table    (tables, SSSQLVarNames.flagTable);
      table    (tables, SSSQLVarNames.flagsTable);
      table    (tables, SSSQLVarNames.entityTable);
      
      column   (columns, SSSQLVarNames.entityTable,   SSSQLVarNames.id);
      column   (columns, SSSQLVarNames.entityTable,   SSSQLVarNames.creationTime);
      column   (columns, SSSQLVarNames.flagTable,     SSSQLVarNames.type);
      column   (columns, SSSQLVarNames.flagTable,     SSSQLVarNames.endTime);
      column   (columns, SSSQLVarNames.flagTable,     SSSQLVarNames.value);
      column   (columns, SSSQLVarNames.flagsTable,    SSSQLVarNames.userId);
      column   (columns, SSSQLVarNames.flagsTable,    SSSQLVarNames.entityId);

      tableCon (tableCons, SSSQLVarNames.flagTable,  SSSQLVarNames.flagId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      tableCon (tableCons, SSSQLVarNames.flagsTable, SSSQLVarNames.flagId, SSSQLVarNames.flagTable,   SSSQLVarNames.flagId);
      
      if(
        users != null &&
        !users.isEmpty()){

        final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
        
        for(SSUri user : users){
          where(whereUsers, SSSQLVarNames.flagsTable, SSSQLVarNames.userId, user);
        }
        
        wheres.add(whereUsers);
      }
      
      if(
        entities != null &&
        !entities.isEmpty()){

        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, SSSQLVarNames.flagsTable, SSSQLVarNames.entityId, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSFlagE type : types){
          where(whereTypes, SSSQLVarNames.flagTable, SSSQLVarNames.type, type);
        }
        
        wheres.add(whereTypes);
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        timestamp = bindingStrToLong (resultSet, SSSQLVarNames.creationTime);
        
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
          endTimeForFlag = bindingStrToLong(resultSet, SSSQLVarNames.endTime);
        }catch(Exception error){
          endTimeForFlag = null;
        }
        
        try{
          value = bindingStrToInteger(resultSet, SSSQLVarNames.value);
        }catch(Exception error){
          value = null;
        }
        
        flagObj = 
          SSFlag.get(bindingStrToUri  (resultSet, SSSQLVarNames.id),
            bindingStrToUri  (resultSet, SSSQLVarNames.userId),
            bindingStrToUri  (resultSet, SSSQLVarNames.entityId),
            SSFlagE.get      (bindingStr(resultSet, SSSQLVarNames.type)),
            endTimeForFlag,
            value);
              
        flagObj.creationTime = timestamp;
        
        flags.add(flagObj);
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
      
      insert    (inserts,    SSSQLVarNames.flagId,    flag);
      insert    (inserts,    SSSQLVarNames.type,      type);
      
      if(endTime == null){
        insert    (inserts,    SSSQLVarNames.endTime,   SSStrU.empty);
      }else{
        insert    (inserts,    SSSQLVarNames.endTime,   endTime);
      }
      
      if(value == null){
        insert    (inserts,    SSSQLVarNames.value,     SSStrU.empty);
      }else{
       insert    (inserts,     SSSQLVarNames.value,     value); 
      }
      
      uniqueKey (uniqueKeys, SSSQLVarNames.flagId,    flag);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.flagTable, inserts, uniqueKeys);
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
      
      insert    (inserts,    SSSQLVarNames.userId,    user);
      insert    (inserts,    SSSQLVarNames.entityId,  entity);
      insert    (inserts,    SSSQLVarNames.flagId,    flag);
      uniqueKey (uniqueKeys, SSSQLVarNames.userId,    user);
      uniqueKey (uniqueKeys, SSSQLVarNames.entityId,  entity);
      uniqueKey (uniqueKeys, SSSQLVarNames.flagId,    flag);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.flagsTable, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getFlagURIs(
    final SSUri   user,
    final SSFlagE flag,
    final SSUri   entity) throws Exception{
    
    ResultSet resultSet;
    
    try{
      
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        columns   = new ArrayList<>();
      final List<String>        tables    = new ArrayList<>();
      final List<String>        tableCons = new ArrayList<>();
      
      column(columns, SSSQLVarNames.flagTable, SSSQLVarNames.flagId);
      
      where(wheres, SSSQLVarNames.userId, user);
      
      table(tables, SSSQLVarNames.flagTable);
      table(tables, SSSQLVarNames.flagsTable);
      
      tableCon(tableCons, SSSQLVarNames.flagTable, SSSQLVarNames.flagId, SSSQLVarNames.flagsTable, SSSQLVarNames.flagId);
      
      if(entity != null){
        where(wheres, SSSQLVarNames.entityId, entity);
      }
      
      if(flag != null){
        where(wheres, SSSQLVarNames.type, flag);
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.flagId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  
  public void deleteFlagAss(
    final SSUri   user,
    final SSUri   flagUri,
    final SSFlagE flag,
    final SSUri   entity) throws Exception{
    
    try{
      final Map<String, String> deletes = new HashMap<>();
      
      if(user != null){
        delete(deletes, SSSQLVarNames.userId,   user);
      }

      if(flagUri != null){
        delete(deletes, SSSQLVarNames.flagId, flagUri);
      }
      
      if(entity != null){
        delete(deletes, SSSQLVarNames.entityId, entity);
      }
      
      if(flag != null){
        delete(deletes, SSSQLVarNames.type,     flag);
      }
      
      if(deletes.isEmpty()){
        SSLogU.warn("not setting any pars would delete the whole flag ass table content");
        return;
      }
      
      dbSQL.deleteIgnore(SSSQLVarNames.flagsTable, deletes);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}