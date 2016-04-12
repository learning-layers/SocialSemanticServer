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
package at.tugraz.sss.servs.flag.impl;

import at.tugraz.sss.servs.db.impl.SSCoreSQL;
import at.tugraz.sss.servs.db.api.SSSQLTableI;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSEntitySQLTableE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.flag.datatype.SSFlagSQLTableE;
import at.tugraz.sss.servs.flag.datatype.SSFlagE;
import at.tugraz.sss.servs.flag.datatype.SSFlag;
import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.util.SSSQLVarNames;
import at.tugraz.sss.servs.db.api.SSDBSQLI;
import at.tugraz.sss.servs.db.datatype.SSDBSQLSelectPar;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class SSFlagSQLFct extends SSCoreSQL{

  public SSFlagSQLFct(
    final SSDBSQLI dbSQL){
    
    super(dbSQL);
  }
  
  public SSFlag getFlag(
    final SSServPar servPar,
    final SSUri     flag) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns        = new ArrayList<>();
      final List<SSSQLTableI>   tables         = new ArrayList<>();
      final Map<String, String> wheres         = new HashMap<>();
      final List<String>        tableCons      = new ArrayList<>();
      Long                      endTimeForFlag = null;
      Integer                   value          = null;
      
      column   (columns, SSFlagSQLTableE.flag,      SSSQLVarNames.flagId);
      column   (columns, SSFlagSQLTableE.flag,      SSSQLVarNames.type);
      column   (columns, SSFlagSQLTableE.flag,      SSSQLVarNames.value);
      column   (columns, SSFlagSQLTableE.flag,      SSSQLVarNames.endTime);
      column   (columns, SSFlagSQLTableE.flags,     SSSQLVarNames.userId);
      column   (columns, SSFlagSQLTableE.flags,     SSSQLVarNames.entityId);
      
      table(tables, SSFlagSQLTableE.flag);
      table(tables, SSFlagSQLTableE.flags);
      
      where(wheres, SSFlagSQLTableE.flag, SSSQLVarNames.flagId, flag);
      
      tableCon (tableCons, SSFlagSQLTableE.flag,  SSSQLVarNames.flagId, SSFlagSQLTableE.flags, SSSQLVarNames.flagId);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null; 
      }
      
      try{
        endTimeForFlag = bindingStrToLong(resultSet, SSSQLVarNames.endTime);
      }catch(Exception error){
        SSLogU.debug(error);
      }
      
      try{
        value = bindingStrToInteger(resultSet, SSSQLVarNames.value);
      }catch(Exception error){
        SSLogU.debug(error);
      }
      
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
      final List<SSSQLTableI>                    tables         = new ArrayList<>();
      final List<String>                         columns        = new ArrayList<>();
      final List<String>                         tableCons      = new ArrayList<>();
      Long                                       timestamp;

      table    (tables, SSFlagSQLTableE.flag);
      table    (tables, SSFlagSQLTableE.flags);
      table    (tables, SSEntitySQLTableE.entity);
      
      column   (columns, SSEntitySQLTableE.entity, SSSQLVarNames.creationTime);
      column   (columns, SSFlagSQLTableE.flag,     SSSQLVarNames.type);
      column   (columns, SSFlagSQLTableE.flags,    SSSQLVarNames.userId);
      column   (columns, SSFlagSQLTableE.flags,    SSSQLVarNames.entityId);
      column   (columns, SSFlagSQLTableE.flag,     SSSQLVarNames.flagId);

      tableCon (tableCons, SSFlagSQLTableE.flag,  SSSQLVarNames.flagId, SSEntitySQLTableE.entity, SSSQLVarNames.id);
      tableCon (tableCons, SSFlagSQLTableE.flags, SSSQLVarNames.flagId, SSFlagSQLTableE.flag,      SSSQLVarNames.flagId);
      
      if(
        users != null &&
        !users.isEmpty()){

        final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
        
        for(SSUri user : users){
          where(whereUsers, SSFlagSQLTableE.flags, SSSQLVarNames.userId, user);
        }
        
        wheres.add(whereUsers);
      }
      
      if(
        entities != null &&
        !entities.isEmpty()){

        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, SSFlagSQLTableE.flags, SSSQLVarNames.entityId, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSFlagE type : types){
          where(whereTypes, SSFlagSQLTableE.flag, SSSQLVarNames.type, type);
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
      
      dbSQL.insertIfNotExists(servPar, SSFlagSQLTableE.flag, inserts, uniqueKeys);
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
      
      dbSQL.insertIfNotExists(servPar, SSFlagSQLTableE.flags, inserts, uniqueKeys);
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
      
      dbSQL.deleteIgnore(servPar, SSFlagSQLTableE.flags, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}