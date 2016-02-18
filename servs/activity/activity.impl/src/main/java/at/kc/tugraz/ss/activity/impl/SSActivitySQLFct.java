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
package at.kc.tugraz.ss.activity.impl;

import at.kc.tugraz.ss.activity.datatypes.*;
import at.tugraz.sss.serv.util.SSSQLVarNames;
import at.tugraz.sss.serv.util.*;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityContentE;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.tugraz.sss.conf.*;
import at.tugraz.sss.serv.datatype.SSAuthor;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class SSActivitySQLFct extends SSCoreSQL{

  public SSActivitySQLFct(
    final SSDBSQLI dbSQL){
    
    super(dbSQL);
  }
  
  public void addActivityContent(
    final SSServPar servPar,
    final SSUri               activity,
    final SSActivityContentE  contentType,
    final SSActivityContent   content) throws SSErr{
    
    try{
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.activityId,     activity);
      insert(inserts, SSSQLVarNames.contentType,    contentType);
      insert(inserts, SSSQLVarNames.content,        content);
      
      dbSQL.insert(servPar, SSActivitySQLTableE.activitycontents, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSActivityContent> getActivityContents(
    final SSServPar servPar,
    final SSUri activity) throws SSErr{
    
    ResultSet resultSet = null;
      
    try{
      final List<SSActivityContent>   contents       = new ArrayList<>();
      final Map<String, String>       wheres         = new HashMap<>();
      final List<String>              columns        = new ArrayList<>();
      
      column(columns, SSSQLVarNames.content);
      
      where(wheres, SSSQLVarNames.activityId, activity);
      
      resultSet = dbSQL.select(servPar, SSActivitySQLTableE.activitycontents, columns, wheres, null, null, null);
      
      while(resultSet.next()){
        contents.add(SSActivityContent.get(bindingStr(resultSet, SSSQLVarNames.content)));
      }
      
      return contents;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addActivity(
    final SSServPar servPar,
    final SSUri               author,
    final SSUri               activity, 
    final SSActivityE         type, 
    final SSUri               entity, 
    final List<SSUri>         users, 
    final List<SSUri>         entityUris, 
    final List<SSTextComment> textComments) throws SSErr{
    
    try{
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.activityId,     activity);
      insert(inserts, SSSQLVarNames.activityType,   type);
      insert(inserts, SSSQLVarNames.entityId,       entity);
      
      if(!textComments.isEmpty()){
        insert(inserts, SSSQLVarNames.textComment,   textComments.get(0));
      }else{
        insert(inserts, SSSQLVarNames.textComment,   SSStrU.empty);
      }
      
      dbSQL.insert(servPar, SSActivitySQLTableE.activity, inserts);
      
      inserts.clear();
      insert(inserts, SSSQLVarNames.activityId,     activity);
      insert(inserts, SSSQLVarNames.userId,         author);
      
      dbSQL.insert(servPar, SSActivitySQLTableE.activityusers, inserts);
        
      for(SSUri user : users){

        if(SSStrU.isEqual(author, user)){
          continue;
        }
        
        inserts.clear();
        insert(inserts, SSSQLVarNames.activityId,     activity);
        insert(inserts, SSSQLVarNames.userId,         user);
        
        dbSQL.insert(servPar, SSActivitySQLTableE.activityusers, inserts);
      }
      
      inserts.clear();
      insert(inserts, SSSQLVarNames.activityId,     activity);
      insert(inserts, SSSQLVarNames.entityId,       entity);
      
      dbSQL.insert(servPar, SSActivitySQLTableE.activityentities, inserts);
      
      for(SSUri entityUri : entityUris){

        if(SSStrU.isEqual(entity, entityUri)){
          continue;
        }
        
        inserts.clear();
        insert(inserts, SSSQLVarNames.activityId,     activity);
        insert(inserts, SSSQLVarNames.entityId,       entityUri);
        
        dbSQL.insert(servPar, SSActivitySQLTableE.activityentities, inserts);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getActivityURIs(
    final SSServPar servPar,
    final List<SSUri>       users,
    final List<SSUri>       entities,
    final List<SSActivityE> types,
    final Long              startTime,
    final Long              endTime,
    final boolean           sortByTime,
    final Integer           limit,
    final boolean           includeOnlyLastActivities) throws SSErr{
    
    ResultSet resultSet = null;
      
    try{
      final List<SSUri>                                            result         = new ArrayList<>();
      final List<MultivaluedMap<String, String>>                   wheres         = new ArrayList<>();
      final MultivaluedMap<String, MultivaluedMap<String, String>> wheresNumeric  = new MultivaluedHashMap<>();
      final List<SSSQLTableI>                                      tables         = new ArrayList<>();
      final List<String>                                           columns        = new ArrayList<>();
      final List<String>                                           tableCons      = new ArrayList<>();
      final SSDBSQLSelectPar                                       selectPar;

      setEntityTable(tables);
      table    (tables, SSActivitySQLTableE.activity);
      
      column   (columns, SSEntitySQLTableE.entity,   SSSQLVarNames.id);
      column   (columns, SSEntitySQLTableE.entity,   SSSQLVarNames.author);
      column   (columns, SSEntitySQLTableE.entity,   SSSQLVarNames.creationTime);
      
      column   (columns, SSActivitySQLTableE.activity, SSSQLVarNames.activityType);
      column   (columns, SSActivitySQLTableE.activity, SSSQLVarNames.entityId);
      column   (columns, SSActivitySQLTableE.activity, SSSQLVarNames.textComment);

      tableCon (tableCons, SSActivitySQLTableE.activity, SSSQLVarNames.activityId, SSEntitySQLTableE.entity, SSSQLVarNames.id);
      
      if(
        users != null &&
        !users.isEmpty()){
        
        table    (tables, SSActivitySQLTableE.activityusers);
        tableCon (tableCons, SSActivitySQLTableE.activityusers,    SSSQLVarNames.activityId, SSEntitySQLTableE.entity, SSSQLVarNames.id);
        
        final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
        
        for(SSUri user : users){
          where(whereUsers, SSActivitySQLTableE.activityusers, SSSQLVarNames.userId, user);
        }
        
        wheres.add(whereUsers);
      }
      
      if(
        entities != null &&
        !entities.isEmpty()){

        table    (tables, SSActivitySQLTableE.activityentities);
        tableCon (tableCons, SSActivitySQLTableE.activityentities, SSSQLVarNames.activityId, SSEntitySQLTableE.entity, SSSQLVarNames.id);
      
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, SSActivitySQLTableE.activityentities, SSSQLVarNames.entityId, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSActivityE type : types){
          where(whereTypes, SSActivitySQLTableE.activity, SSSQLVarNames.activityType, type);
        }
        
        wheres.add(whereTypes);
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
      
      if(
        endTime != null &&
        endTime != 0){
        
        final List<MultivaluedMap<String, String>> lessWheres            = new ArrayList<>();
        final MultivaluedMap<String, String>       whereNumbericEndTimes = new MultivaluedHashMap<>();
        
        wheresNumeric.put(SSStrU.lessThan,    lessWheres);
        
        where(whereNumbericEndTimes, SSEntitySQLTableE.entity, SSSQLVarNames.creationTime, endTime);
        
        lessWheres.add(whereNumbericEndTimes);
      }
      
      selectPar =
        new SSDBSQLSelectPar(
          servPar, 
          tables,
          columns,
          wheres,
          null,
          wheresNumeric,
          tableCons);
      
      selectPar.limit = limit;
      
      if(sortByTime){
        
        selectPar.orderByColumn = SSSQLVarNames.creationTime;
        selectPar.sortType      = SSStrU.valueDESC;
      }
      
      resultSet = dbSQL.select(selectPar);
      
//      return getURIsFromResult(resultSet, SSSQLVarNames.id);
      
      final         List<String> latestActivities = new ArrayList<>();
      SSUri         entity;
      SSAuthor      author;
      SSActivityE   type;
      String        activityCombi;
        
      while(resultSet.next()){
        
        author = bindingStrToAuthor  (resultSet, SSSQLVarNames.author);
        entity = bindingStrToUri     (resultSet, SSSQLVarNames.entityId);
        type   = SSActivityE.get     (bindingStr(resultSet, SSSQLVarNames.activityType));
        
        if(
          includeOnlyLastActivities &&
          entity != null){
          
          activityCombi = SSStrU.toStr(author) + SSStrU.toStr(entity) + SSStrU.toStr(type);
          
          if(latestActivities.contains(activityCombi)){
            continue;
          }else{
            latestActivities.add(activityCombi);
          }
        }
        
        result.add(bindingStrToUri  (resultSet, SSSQLVarNames.id));
      }
      
      return result;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getActivityUsers(
    final SSServPar servPar,
    final SSUri activity) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
    
      final List<SSSQLTableI>         tables         = new ArrayList<>();
      final List<String>              columns        = new ArrayList<>();
      final Map<String, String>       wheres         = new HashMap<>();
      final List<String>              tableCons      = new ArrayList<>();
      
      column   (columns,   SSSQLVarNames.userId);
      
      table    (tables, SSActivitySQLTableE.activity);
      table    (tables, SSActivitySQLTableE.activityusers);
      
      where    (wheres, SSActivitySQLTableE.activity, SSSQLVarNames.activityId, activity);
      
      tableCon (tableCons, SSActivitySQLTableE.activity, SSSQLVarNames.activityId, SSActivitySQLTableE.activityusers, SSSQLVarNames.activityId);
      
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
      
      return getURIsFromResult(resultSet, SSSQLVarNames.userId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getActivityEntities(
    final SSServPar servPar,
    final SSUri activity) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
    
      final List<SSSQLTableI>         tables         = new ArrayList<>();
      final List<String>              columns        = new ArrayList<>();
      final Map<String, String>       wheres         = new HashMap<>();
      final List<String>              tableCons      = new ArrayList<>();
      
      column   (columns, SSActivitySQLTableE.activityentities,  SSSQLVarNames.entityId);

      table    (tables, SSActivitySQLTableE.activity);
      table    (tables, SSActivitySQLTableE.activityentities);
      
      where    (wheres, SSActivitySQLTableE.activity, SSSQLVarNames.activityId, activity);
      
      tableCon (tableCons, SSActivitySQLTableE.activity, SSSQLVarNames.activityId, SSActivitySQLTableE.activityentities, SSSQLVarNames.activityId);
      
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
      
      return getURIsFromResult(resultSet, SSSQLVarNames.entityId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public SSActivity getActivity(
    final SSServPar servPar,
    final SSUri activity) throws SSErr{
   
    ResultSet resultSet = null;
      
    try{
      final Map<String, String>                  wheres         = new HashMap<>();
      final List<SSSQLTableI>                    tables         = new ArrayList<>();
      final List<String>                         columns        = new ArrayList<>();
      final List<String>                         tableCons      = new ArrayList<>();
      
      setEntityColumns(columns);
      column(columns, SSActivitySQLTableE.activity, SSSQLVarNames.activityType);
      column(columns, SSActivitySQLTableE.activity, SSSQLVarNames.entityId);

      setEntityTable(tables);
      table(tables, SSActivitySQLTableE.activity);
      
      where(wheres, SSSQLVarNames.activityId, activity);
      
      tableCon(tableCons, SSActivitySQLTableE.activity, SSSQLVarNames.activityId, SSEntitySQLTableE.entity, SSSQLVarNames.id);
      
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
      
      return SSActivity.get(
        bindingStrToUri           (resultSet, SSSQLVarNames.id),
        bindingStrToLabel         (resultSet, SSSQLVarNames.label),
        bindingStrToTextComment   (resultSet, SSSQLVarNames.description),
        bindingStrToLong          (resultSet, SSSQLVarNames.creationTime),
        getAuthorEntityFromResult (servPar, resultSet, SSConf.systemUserUri),
        SSActivityE.get           (bindingStr(resultSet, SSSQLVarNames.activityType)),
        getEntityFromResult       (resultSet, SSSQLVarNames.entityId),
        null); //contents
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
