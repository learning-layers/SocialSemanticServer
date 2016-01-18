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
package at.kc.tugraz.sss.video.impl.fct.sql;

import at.tugraz.sss.serv.util.SSSQLVarNames;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.sss.video.datatypes.SSVideo;
import at.kc.tugraz.sss.video.datatypes.SSVideoAnnotation;
import at.kc.tugraz.sss.video.datatypes.SSVideoE;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.db.api.SSCoreSQL;

public class SSVideoSQLFct extends SSCoreSQL{

  public SSVideoSQLFct(
    final SSDBSQLI dbSQL,
    final SSUri    systemUserURI){
    
    super(dbSQL, systemUserURI);
  }
  
  public void addVideoToUser(
    final SSServPar servPar,
    final SSUri         user,
    final SSUri         video) throws Exception{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userId,   user);
      insert(inserts, SSSQLVarNames.videoId,  video);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.userId,  user);
      uniqueKey(uniqueKeys, SSSQLVarNames.videoId, video);
      
      dbSQL.insertIfNotExists(servPar, SSSQLVarNames.videoUsersTable, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addVideo(
    final SSServPar servPar,
    final SSUri         video,
    final String        genre,
    final SSUri         link, 
    final SSVideoE      type) throws Exception{
    
     try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      if(genre == null){
        insert    (inserts,    SSSQLVarNames.genre,     SSStrU.empty);
      }else{
        insert    (inserts,    SSSQLVarNames.genre,     genre);
      }
      
      if(link == null){
        insert    (inserts,    SSSQLVarNames.link,     SSStrU.empty);
      }else{
        insert    (inserts,    SSSQLVarNames.link,     link);
      }
      
      if(type == null){
        insert    (inserts,    SSSQLVarNames.videoType,     SSVideoE.other);
      }else{
        insert    (inserts,    SSSQLVarNames.videoType,     type);
      }
      
      insert    (inserts,    SSSQLVarNames.videoId,     video);
      
      uniqueKey (uniqueKeys, SSSQLVarNames.videoId, video);
        
      dbSQL.insertIfNotExists(servPar, SSSQLVarNames.videoTable, inserts, uniqueKeys);
      
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
  }
  
  public void removeAnnotation(
    final SSServPar servPar,
    final SSUri annotation) throws Exception{
   
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres,    SSSQLVarNames.videoAnnotationId,   annotation);
      
      dbSQL.delete(servPar, SSSQLVarNames.videoAnnotationTable, wheres);
      
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
  }
  
  public void createAnnotation(
    final SSServPar servPar,
    final SSUri video,
    final SSUri videoAnnotation,
    final Float x, 
    final Float y,
    final Long  timePoint) throws Exception{
   
    try{
      final Map<String, String> inserts    = new HashMap<>();
      
      if(x == null){
        insert    (inserts,    SSSQLVarNames.x,     SSStrU.empty);
      }else{
        insert    (inserts,    SSSQLVarNames.x,     x);
      }
      
      if(y == null){
        insert    (inserts,    SSSQLVarNames.y,     SSStrU.empty);
      }else{
        insert    (inserts,    SSSQLVarNames.y,     y);
      }
      
      if(timePoint == null){
        insert    (inserts,    SSSQLVarNames.timePoint,     SSStrU.empty);
      }else{
        insert    (inserts,    SSSQLVarNames.timePoint,     timePoint);
      }
      
      insert    (inserts,    SSSQLVarNames.videoAnnotationId,  videoAnnotation);
      
      dbSQL.insert(servPar, SSSQLVarNames.videoAnnotationTable, inserts);
      
      inserts.clear();
      
      insert    (inserts,    SSSQLVarNames.videoId,            video);
      insert    (inserts,    SSSQLVarNames.videoAnnotationId,  videoAnnotation);
      
      dbSQL.insert(servPar, SSSQLVarNames.videoAnnotationsTable, inserts);
      
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
  }
  
  public SSVideo getVideo(
    final SSServPar servPar,
    final SSUri videoUri) throws Exception{
    
    ResultSet resultSet = null;
      
    try{
      final List<String>          columns     = new ArrayList<>();
      final List<String>          tables      = new ArrayList<>();
      final Map<String, String>   wheres      = new HashMap<>();
      final List<String>          tableCons   = new ArrayList<>();
      
      final SSVideo video;
      
      column(columns, SSSQLVarNames.videoTable,          SSSQLVarNames.videoId);
      column(columns, SSSQLVarNames.videoTable,          SSSQLVarNames.genre);
      column(columns, SSSQLVarNames.videoTable,          SSSQLVarNames.link);
      column(columns, SSSQLVarNames.videoTable,          SSSQLVarNames.videoType);

      where(wheres, SSSQLVarNames.videoTable, SSSQLVarNames.videoId, videoUri);
      
//      table(tables, SSSQLVarNames.entityTable);
      table(tables, SSSQLVarNames.videoTable);
      
//      if(user != null){
//        where    (wheres, SSSQLVarNames.videoUsersTable, SSSQLVarNames.userId, user);
//        table    (tables, SSSQLVarNames.videoUsersTable);
//        tableCon (tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.videoUsersTable, SSSQLVarNames.videoId);
//      }
      
//      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.videoTable,      SSSQLVarNames.videoId);
      
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
      
      video =
        SSVideo.get(
          bindingStrToUri         (resultSet, SSSQLVarNames.videoId),
          bindingStr              (resultSet, SSSQLVarNames.genre),
          null,
          bindingStrToUri         (resultSet, SSSQLVarNames.link),
          SSVideoE.get(bindingStr (resultSet, SSSQLVarNames.videoType)));
      
      return video;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getVideoURIs(
    final SSServPar servPar,
    final SSUri   forUser,     
    final SSUri   forEntity) throws Exception{
    
    ResultSet resultSet = null;
      
    try{
      final List<String>          columns     = new ArrayList<>();
      final List<String>          tables      = new ArrayList<>();
      final Map<String, String>   wheres      = new HashMap<>();
      final List<String>          tableCons   = new ArrayList<>();
      
      column(columns, SSSQLVarNames.videoTable,          SSSQLVarNames.videoId);
      column(columns, SSSQLVarNames.videoTable,          SSSQLVarNames.genre);
      column(columns, SSSQLVarNames.videoTable,          SSSQLVarNames.link);

      table(tables, SSSQLVarNames.entityTable);
      table(tables, SSSQLVarNames.videoTable);
      
      if(forUser != null){
        where    (wheres, SSSQLVarNames.videoUsersTable, SSSQLVarNames.userId, forUser);
        table    (tables, SSSQLVarNames.videoUsersTable);
        tableCon (tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.videoUsersTable, SSSQLVarNames.videoId);
      }
      
      if(forEntity != null){
        where    (wheres, SSSQLVarNames.entityVideosTable, SSSQLVarNames.entityId, forEntity);
        table    (tables, SSSQLVarNames.entityVideosTable);
        tableCon (tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.entityVideosTable, SSSQLVarNames.videoId);
      }
      
      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.videoTable,      SSSQLVarNames.videoId);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);

      return getURIsFromResult(resultSet, SSSQLVarNames.videoId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public SSVideoAnnotation getAnnotation(
    final SSServPar servPar,
    final SSUri annotation) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>              columns     = new ArrayList<>();
      final Map<String, String>       wheres      = new HashMap<>();
      
      column(columns, SSSQLVarNames.videoAnnotationTable, SSSQLVarNames.videoAnnotationId);
      column(columns, SSSQLVarNames.videoAnnotationTable, SSSQLVarNames.x);
      column(columns, SSSQLVarNames.videoAnnotationTable, SSSQLVarNames.y);
      column(columns, SSSQLVarNames.videoAnnotationTable, SSSQLVarNames.timePoint);
      
      where(wheres, SSSQLVarNames.videoAnnotationTable, SSSQLVarNames.videoAnnotationId, annotation);

      resultSet = dbSQL.select(servPar, SSSQLVarNames.videoAnnotationTable, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return
        SSVideoAnnotation.get(
          bindingStrToUri   (resultSet, SSSQLVarNames.videoAnnotationId),
          bindingStrToLong  (resultSet, SSSQLVarNames.timePoint),
          bindingStrToFloat (resultSet, SSSQLVarNames.x),
          bindingStrToFloat (resultSet, SSSQLVarNames.y));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getAnnotations(
    final SSServPar servPar,
    final SSUri video) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>              columns     = new ArrayList<>();
      final List<String>              tables      = new ArrayList<>();
      final Map<String, String>       wheres      = new HashMap<>();
      final List<String>              tableCons   = new ArrayList<>();
      
      column(columns, SSSQLVarNames.videoAnnotationTable, SSSQLVarNames.videoAnnotationId);
      
      table(tables, SSSQLVarNames.videoAnnotationTable);
      table(tables, SSSQLVarNames.videoAnnotationsTable);
      
      where(wheres, SSSQLVarNames.videoAnnotationsTable, SSSQLVarNames.videoId, video);
      
      tableCon(tableCons, SSSQLVarNames.videoAnnotationsTable, SSSQLVarNames.videoAnnotationId, SSSQLVarNames.videoAnnotationTable,  SSSQLVarNames.videoAnnotationId);

      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.videoAnnotationId);
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addVideoToEntity(
    final SSServPar servPar,
    final SSUri video,
    final SSUri entity) throws Exception{
    
    try{
      
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.entityId,       entity);
      insert(inserts, SSSQLVarNames.videoId,        video);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.entityId, entity);
      uniqueKey(uniqueKeys, SSSQLVarNames.videoId,  video);
      
      dbSQL.insertIfNotExists(servPar, SSSQLVarNames.entityVideosTable, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}