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

import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.SSSQLVarNames;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.sss.flag.datatypes.SSFlag;
import at.kc.tugraz.sss.flag.datatypes.SSFlagE;
import at.tugraz.sss.serv.datatype.par.SSDBSQLSelectPar;
import at.tugraz.sss.serv.datatype.SSEntity;
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

public class SSFlagSQLFct extends SSCoreSQL{

  public SSFlagSQLFct(
    final SSDBSQLI dbSQL,
    final SSUri    systemUserURI){
    
    super(dbSQL, systemUserURI);
  }
  
  public SSFlag getFlag(
    final SSServPar servPar,
    final SSUri flag) throws SSErr {
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns        = new ArrayList<>();
      final List<String>        tables         = new ArrayList<>();
      final Map<String, String> wheres         = new HashMap<>();
      final List<String>        tableCons      = new ArrayList<>();
      Long                      endTimeForFlag = null;
      Integer                   value          = null;
      
      column   (columns, SSSQLVarNames.flagTable,      SSSQLVarNames.flagId);
      column   (columns, SSSQLVarNames.flagTable,      SSSQLVarNames.type);
      column   (columns, SSSQLVarNames.flagTable,      SSSQLVarNames.value);
      column   (columns, SSSQLVarNames.flagTable,      SSSQLVarNames.endTime);
      column   (columns, SSSQLVarNames.flagsTable,     SSSQLVarNames.userId);
      column   (columns, SSSQLVarNames.flagsTable,     SSSQLVarNames.entityId);
      
      table(tables, SSSQLVarNames.flagTable);
      table(tables, SSSQLVarNames.flagsTable);
      
      where(wheres, SSSQLVarNames.flagTable, SSSQLVarNames.flagId, flag);
      
      tableCon (tableCons, SSSQLVarNames.flagTable,  SSSQLVarNames.flagId, SSSQLVarNames.flagsTable, SSSQLVarNames.flagId);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null; 
      }
      
      try{
        endTimeForFlag = bindingStrToLong(resultSet, SSSQLVarNames.endTime);
      }catch(Exception error){/* Do nothing because of only JSON Jackson needs this */ }
      
      try{
        value = bindingStrToInteger(resultSet, SSSQLVarNames.value);
      }catch(Exception error){/* Do nothing because of only JSON Jackson needs this */ }
      
      return SSFlag.get(
        bindingStrToUri     (resultSet, SSSQLVarNames.flagId),
        SSEntity.get        (bindingStrToUri(resultSet, SSSQLVarNames.userId),   SSEntityE.entity),
        SSEntity.get        (bindingStrToUri(resultSet, SSSQLVarNames.entityId), SSEntityE.entity),
        SSFlagE.get         (bindingStr(resultSet, SSSQLVarNames.type)),
        endTimeForFlag,
        value);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getFlagURIs(
    final SSServPar servPar,
    final List<SSUri>   users,
    final List<SSUri>   entities,
    final List<SSFlagE> types,
    final Long          startTime,
    final Long          endTime) throws SSErr{
    
    ResultSet resultSet = null;
      
    try{
      final List<SSUri>                          flagURIs       = new ArrayList<>();
      final List<MultivaluedMap<String, String>> wheres         = new ArrayList<>();
      final List<String>                         tables         = new ArrayList<>();
      final List<String>                         columns        = new ArrayList<>();
      final List<String>                         tableCons      = new ArrayList<>();
      Long                                       timestamp;

      table    (tables, SSSQLVarNames.flagTable);
      table    (tables, SSSQLVarNames.flagsTable);
      table    (tables, SSSQLVarNames.entityTable);
      
      column   (columns, SSSQLVarNames.entityTable,   SSSQLVarNames.creationTime);
      column   (columns, SSSQLVarNames.flagTable,     SSSQLVarNames.type);
      column   (columns, SSSQLVarNames.flagsTable,    SSSQLVarNames.userId);
      column   (columns, SSSQLVarNames.flagsTable,    SSSQLVarNames.entityId);
      column   (columns, SSSQLVarNames.flagTable,     SSSQLVarNames.flagId);

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
      
      resultSet =
        dbSQL.select(
          new SSDBSQLSelectPar(
            servPar, 
            tables,
            columns,
            wheres,
            null,
            null,
            tableCons));
      
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
        
        SSUri.addDistinctWithoutNull(flagURIs, bindingStrToUri  (resultSet, SSSQLVarNames.flagId));
      }
      
      return flagURIs;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void createFlag(
    final SSServPar servPar,
    final SSUri   flag,
    final SSFlagE type,
    final Long    endTime,
    final Integer value) throws SSErr{
    
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
      
      dbSQL.insertIfNotExists(servPar, SSSQLVarNames.flagTable, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addFlagAssIfNotExists(
    final SSServPar servPar,
    final SSUri         user, 
    final SSUri         flag, 
    final SSUri         entity) throws SSErr{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert    (inserts,    SSSQLVarNames.userId,    user);
      insert    (inserts,    SSSQLVarNames.entityId,  entity);
      insert    (inserts,    SSSQLVarNames.flagId,    flag);
      uniqueKey (uniqueKeys, SSSQLVarNames.userId,    user);
      uniqueKey (uniqueKeys, SSSQLVarNames.entityId,  entity);
      uniqueKey (uniqueKeys, SSSQLVarNames.flagId,    flag);
      
      dbSQL.insertIfNotExists(servPar, SSSQLVarNames.flagsTable, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void deleteFlagAss(
    final SSServPar servPar,
    final SSUri   user,
    final SSUri   flagUri,
    final SSFlagE flag,
    final SSUri   entity) throws SSErr{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      if(user != null){
        where(wheres, SSSQLVarNames.userId,   user);
      }

      if(flagUri != null){
        where(wheres, SSSQLVarNames.flagId, flagUri);
      }
      
      if(entity != null){
        where(wheres, SSSQLVarNames.entityId, entity);
      }
      
      if(flag != null){
        where(wheres, SSSQLVarNames.type,     flag);
      }
      
      if(wheres.isEmpty()){
        SSLogU.warn("not setting any pars would delete the whole flag ass table content", null);
        return;
      }
      
      dbSQL.deleteIgnore(servPar, SSSQLVarNames.flagsTable, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}