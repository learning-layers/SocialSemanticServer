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
package at.kc.tugraz.ss.activity.impl.fct.sql;

import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.activity.datatypes.SSActivity;
import at.kc.tugraz.ss.activity.datatypes.SSActivityContent;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityContentE;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.tugraz.sss.serv.SSAuthor;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class SSActivitySQLFct extends SSDBSQLFct{

  public SSActivitySQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  public void addActivityContent(
    final SSUri               activity,
    final SSActivityContentE  contentType,
    final SSActivityContent   content) throws Exception{
    
    try{
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.activityId,     activity);
      insert(inserts, SSSQLVarNames.contentType,    contentType);
      insert(inserts, SSSQLVarNames.content,        content);
      
      dbSQL.insert(SSSQLVarNames.activityContentsTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSActivityContent> getActivityContents(
    final SSUri activity) throws Exception{
    
    ResultSet resultSet = null;
      
    try{
      final List<SSActivityContent>   contents       = new ArrayList<>();
      final Map<String, String>       wheres         = new HashMap<>();
      final List<String>              columns        = new ArrayList<>();
      
      column(columns, SSSQLVarNames.content);
      
      where(wheres, SSSQLVarNames.activityId, activity);
      
      resultSet = dbSQL.select(SSSQLVarNames.activityContentsTable, columns, wheres, null, null, null);
      
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
    final SSUri               author,
    final SSUri               activity, 
    final SSActivityE         type, 
    final SSUri               entity, 
    final List<SSUri>         users, 
    final List<SSUri>         entityUris, 
    final List<SSTextComment> textComments) throws Exception{
    
    try{
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.activityId,     activity);
      insert(inserts, SSSQLVarNames.activityType,   type);
      
      if(entity != null){
        insert(inserts, SSSQLVarNames.entityId,   entity);
      }else{
        insert(inserts, SSSQLVarNames.entityId,   SSStrU.empty);
      }
      
      if(!textComments.isEmpty()){
        insert(inserts, SSSQLVarNames.textComment,   textComments.get(0));
      }else{
        insert(inserts, SSSQLVarNames.textComment,   SSStrU.empty);
      }
      
      dbSQL.insert(SSSQLVarNames.activityTable, inserts);
      
      inserts.clear();
      insert(inserts, SSSQLVarNames.activityId,     activity);
      insert(inserts, SSSQLVarNames.userId,         author);
      
      dbSQL.insert(SSSQLVarNames.activityUsersTable, inserts);
        
      for(SSUri user : users){

        inserts.clear();
        insert(inserts, SSSQLVarNames.activityId,     activity);
        insert(inserts, SSSQLVarNames.userId,         user);
        
        dbSQL.insert(SSSQLVarNames.activityUsersTable, inserts);
      }
      
      for(SSUri entityUri : entityUris){

        inserts.clear();
        insert(inserts, SSSQLVarNames.activityId,     activity);
        insert(inserts, SSSQLVarNames.entityId,       entityUri);
        
        dbSQL.insert(SSSQLVarNames.activityEntitiesTable, inserts);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri>    getActivityURIs(
    final List<SSUri>       users,
    final List<SSUri>       entities,
    final List<SSActivityE> types,
    final Long              startTime,
    final Long              endTime,
    final Boolean           sortByTime,
    final Integer           limit,
    final Boolean           includeOnlyLastActivities) throws Exception{
    
    ResultSet resultSet = null;
      
    try{
      final List<SSUri>                                            result         = new ArrayList<>();
      final List<MultivaluedMap<String, String>>                   wheres         = new ArrayList<>();
      final MultivaluedMap<String, MultivaluedMap<String, String>> wheresNumeric  = new MultivaluedHashMap<>();
      final List<String>                                           tables         = new ArrayList<>();
      final List<String>                                           columns        = new ArrayList<>();
      final List<String>                                           tableCons      = new ArrayList<>();

      table    (tables, SSSQLVarNames.activityTable);
      table    (tables, SSSQLVarNames.entityTable);
      
      column   (columns, SSSQLVarNames.entityTable,   SSSQLVarNames.id);
      column   (columns, SSSQLVarNames.entityTable,   SSSQLVarNames.author);
      column   (columns, SSSQLVarNames.activityTable, SSSQLVarNames.activityType);
      column   (columns, SSSQLVarNames.activityTable, SSSQLVarNames.entityId);
      column   (columns, SSSQLVarNames.entityTable,   SSSQLVarNames.creationTime);
      column   (columns, SSSQLVarNames.activityTable, SSSQLVarNames.textComment);

      tableCon (tableCons, SSSQLVarNames.activityTable, SSSQLVarNames.activityId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      if(
        users != null &&
        !users.isEmpty()){
        
        table    (tables, SSSQLVarNames.activityUsersTable);
        tableCon (tableCons, SSSQLVarNames.activityUsersTable,    SSSQLVarNames.activityId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
        
        final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
        
        for(SSUri user : users){
          where(whereUsers, SSSQLVarNames.activityUsersTable, SSSQLVarNames.userId, user);
        }
        
        wheres.add(whereUsers);
      }
      
      if(
        entities != null &&
        !entities.isEmpty()){

        table    (tables, SSSQLVarNames.activityEntitiesTable);
        tableCon (tableCons, SSSQLVarNames.activityEntitiesTable, SSSQLVarNames.activityId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, SSSQLVarNames.activityEntitiesTable, SSSQLVarNames.entityId, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSActivityE type : types){
          where(whereTypes, SSSQLVarNames.activityTable, SSSQLVarNames.activityType, type);
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
        
        wheresNumeric.put(SSStrU.lessThan,    lessWheres);
        
        where(whereNumbericEndTimes, SSSQLVarNames.entityTable, SSSQLVarNames.creationTime, endTime);
        
        lessWheres.add(whereNumbericEndTimes);
      }
      
      if(!wheresNumeric.isEmpty()){
        
        if(sortByTime){
          resultSet = dbSQL.select(tables, columns, wheres, wheresNumeric, tableCons, SSSQLVarNames.creationTime, "DESC", limit);
        }else{
          resultSet = dbSQL.select(tables, columns, wheres, wheresNumeric, tableCons, null, null, limit);
        }
        
      }else{
        
        if(sortByTime){
          resultSet = dbSQL.select(tables, columns, wheres, tableCons, SSSQLVarNames.creationTime, "DESC", limit);
        }else{
          resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, limit);
        }
      }
      
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
      
//      SSActivity.get(
//            bindingStrToUri  (resultSet, SSSQLVarNames.id), 
//            type,
//            SSEntity.get(entity, SSEntityE.entity), 
//            getActivityContents(bindingStrToUri  (resultSet, SSSQLVarNames.id))));
      return result;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getActivityUsers(final SSUri activity) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
    
      final List<String>              tables         = new ArrayList<>();
      final List<String>              columns        = new ArrayList<>();
      final Map<String, String>       wheres         = new HashMap<>();
      final List<String>              tableCons      = new ArrayList<>();
      
      table    (tables, SSSQLVarNames.activityTable);
      table    (tables, SSSQLVarNames.activityUsersTable);
      column   (columns,   SSSQLVarNames.userId);
      where    (wheres, SSSQLVarNames.activityTable,        SSSQLVarNames.activityId, activity);
      tableCon (tableCons, SSSQLVarNames.activityTable,        SSSQLVarNames.activityId, SSSQLVarNames.activityUsersTable, SSSQLVarNames.activityId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.userId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getActivityEntities(final SSUri activity) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
    
      final List<String>              tables         = new ArrayList<>();
      final List<String>              columns        = new ArrayList<>();
      final Map<String, String>       wheres         = new HashMap<>();
      final List<String>              tableCons      = new ArrayList<>();
      
      table    (tables, SSSQLVarNames.activityTable);
      table    (tables, SSSQLVarNames.activityEntitiesTable);
      column   (columns, SSSQLVarNames.activityEntitiesTable,  SSSQLVarNames.entityId);
      where    (wheres, SSSQLVarNames.activityTable,          SSSQLVarNames.activityId, activity);
      tableCon (tableCons, SSSQLVarNames.activityTable,          SSSQLVarNames.activityId, SSSQLVarNames.activityEntitiesTable, SSSQLVarNames.activityId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.entityId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public SSActivity getActivity(final SSUri activity) throws Exception{
   
    ResultSet resultSet = null;
      
    try{
      final Map<String, String>                  wheres         = new HashMap<>();
      final List<String>                         tables         = new ArrayList<>();
      final List<String>                         columns        = new ArrayList<>();
      final List<String>                         tableCons      = new ArrayList<>();
      final SSActivity                           activityObj;
      final SSUri                                entity;

      column(columns, SSSQLVarNames.entityTable,   SSSQLVarNames.id);
      column(columns, SSSQLVarNames.activityTable, SSSQLVarNames.activityType);
      column(columns, SSSQLVarNames.activityTable, SSSQLVarNames.entityId);
      
      table(tables, SSSQLVarNames.activityTable);
      table(tables, SSSQLVarNames.entityTable);
      
      where(wheres, SSSQLVarNames.activityId, activity);
      
      tableCon(tableCons, SSSQLVarNames.activityTable, SSSQLVarNames.activityId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      checkFirstResult(resultSet);
      
      activityObj = 
        SSActivity.get(
          bindingStrToUri  (resultSet, SSSQLVarNames.id), 
          SSActivityE.get  (bindingStr(resultSet, SSSQLVarNames.activityType)),
          null,
          new ArrayList<>());
        
      entity = bindingStrToUri  (resultSet, SSSQLVarNames.entityId);
      
      if(entity != null){
        
        activityObj.entity =
          SSEntity.get(
            entity,
            SSEntityE.entity);
      }else{
        activityObj.entity = null;
      }
    
      return activityObj;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
