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

import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSDBSQLI;

import at.kc.tugraz.sss.video.datatypes.SSVideo;
import at.kc.tugraz.sss.video.datatypes.SSVideoAnnotation;
import at.tugraz.sss.serv.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSVideoSQLFct extends SSDBSQLFct{

  public SSVideoSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  public void addVideoToUser(
    final SSUri         user,
    final SSUri         video) throws Exception{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userId,   user);
      insert(inserts, SSSQLVarNames.videoId,  video);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.userId,  user);
      uniqueKey(uniqueKeys, SSSQLVarNames.videoId, video);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.userVideosTable, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addVideo(
    final SSUri         video,
    final String        genre,
    final SSUri         forEntity,
    final SSUri         link) throws Exception{
    
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
      
      insert    (inserts,    SSSQLVarNames.videoId,     video);
      
      uniqueKey (uniqueKeys, SSSQLVarNames.videoId, video);
        
      dbSQL.insertIfNotExists(SSSQLVarNames.videoTable, inserts, uniqueKeys);
      
      if(forEntity != null){
        
        uniqueKeys.clear();
        inserts.clear();
        
        insert(inserts, SSSQLVarNames.entityId,          forEntity);
        insert(inserts, SSSQLVarNames.attachedEntityId,  video);
        
        uniqueKey(uniqueKeys, SSSQLVarNames.entityId,          forEntity);
        uniqueKey(uniqueKeys, SSSQLVarNames.attachedEntityId,  video);
        
        dbSQL.insertIfNotExists(SSSQLVarNames.entitiesTable, inserts, uniqueKeys);
      }
      
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
  }
  
  public void createAnnotation(
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
      
      dbSQL.insert(SSSQLVarNames.videoAnnotationTable, inserts);
      
      inserts.clear();
      
      insert    (inserts,    SSSQLVarNames.videoId,            video);
      insert    (inserts,    SSSQLVarNames.videoAnnotationId,  videoAnnotation);
      
      dbSQL.insert(SSSQLVarNames.videoAnnotationsTable, inserts);
      
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
  }
  
  public SSVideo getVideo(
    final SSUri user, 
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
      column(columns, SSSQLVarNames.entityTable,         SSSQLVarNames.creationTime);
      column(columns, SSSQLVarNames.entityTable,         SSSQLVarNames.label);
      column(columns, SSSQLVarNames.entityTable,         SSSQLVarNames.description);

      where(wheres, SSSQLVarNames.videoTable, SSSQLVarNames.videoId, videoUri);
      
      table(tables, SSSQLVarNames.entityTable);
      table(tables, SSSQLVarNames.videoTable);
      
      if(user != null){
        where    (wheres, SSSQLVarNames.userVideosTable, SSSQLVarNames.userId, user);
        table    (tables, SSSQLVarNames.userVideosTable);
        tableCon (tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.userVideosTable, SSSQLVarNames.videoId);
      }
      
      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.videoTable,      SSSQLVarNames.videoId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      checkFirstResult(resultSet);
      
      video =
        SSVideo.get(bindingStrToUri         (resultSet, SSSQLVarNames.videoId),
          bindingStr              (resultSet, SSSQLVarNames.genre),
          new ArrayList<>(),
          bindingStrToUri          (resultSet, SSSQLVarNames.link));
      
      video.creationTime = bindingStrToLong        (resultSet, SSSQLVarNames.creationTime);
      video.label        = bindingStrToLabel       (resultSet, SSSQLVarNames.label);
      video.description  = bindingStrToTextComment (resultSet, SSSQLVarNames.description);
      
      return video;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSVideo> getVideos(
    final SSUri   forUser,     
    final SSUri   forEntity) throws Exception{
    
    ResultSet resultSet = null;
      
    try{
      final List<SSVideo>         videos      = new ArrayList<>();
      final List<String>          columns     = new ArrayList<>();
      final List<String>          tables      = new ArrayList<>();
      final Map<String, String>   wheres      = new HashMap<>();
      final List<String>          tableCons   = new ArrayList<>();
      
      SSVideo video;
      
      column(columns, SSSQLVarNames.videoTable,          SSSQLVarNames.videoId);
      column(columns, SSSQLVarNames.videoTable,          SSSQLVarNames.genre);
      column(columns, SSSQLVarNames.videoTable,          SSSQLVarNames.link);
      column(columns, SSSQLVarNames.entityTable,         SSSQLVarNames.creationTime);
      column(columns, SSSQLVarNames.entityTable,         SSSQLVarNames.label);
      column(columns, SSSQLVarNames.entityTable,         SSSQLVarNames.description);

      table(tables, SSSQLVarNames.entityTable);
      table(tables, SSSQLVarNames.videoTable);
      
      if(forUser != null){
        where    (wheres, SSSQLVarNames.userVideosTable, SSSQLVarNames.userId, forUser);
        table    (tables, SSSQLVarNames.userVideosTable);
        tableCon (tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.userVideosTable, SSSQLVarNames.videoId);
      }
      
      if(forEntity != null){
        where    (wheres, SSSQLVarNames.entitiesTable, SSSQLVarNames.entityId, forEntity);
        table    (tables, SSSQLVarNames.entitiesTable);
        tableCon (tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.entitiesTable, SSSQLVarNames.attachedEntityId);
      }
      
      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.videoTable,      SSSQLVarNames.videoId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        video =  
          SSVideo.get(bindingStrToUri         (resultSet, SSSQLVarNames.videoId),
            bindingStr              (resultSet, SSSQLVarNames.genre),
            new ArrayList<>(),
            bindingStrToUri         (resultSet, SSSQLVarNames.link));
          
        video.creationTime = bindingStrToLong        (resultSet, SSSQLVarNames.creationTime);
        video.label        = bindingStrToLabel       (resultSet, SSSQLVarNames.label);
        video.description  = bindingStrToTextComment (resultSet, SSSQLVarNames.description);
        
        videos.add(video);
      }
      
      return videos;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public List<SSVideoAnnotation> getAnnotations(
    final SSUri video) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<SSVideoAnnotation>   annotations = new ArrayList<>();
      final List<String>              columns     = new ArrayList<>();
      final List<String>              tables      = new ArrayList<>();
      final Map<String, String>       wheres      = new HashMap<>();
      final List<String>              tableCons   = new ArrayList<>();
      SSVideoAnnotation annotation;
      
      column(columns, SSSQLVarNames.entityTable,          SSSQLVarNames.creationTime);
      column(columns, SSSQLVarNames.entityTable,          SSSQLVarNames.label);
      column(columns, SSSQLVarNames.entityTable,          SSSQLVarNames.description);
      column(columns, SSSQLVarNames.videoAnnotationTable, SSSQLVarNames.videoAnnotationId);
      column(columns, SSSQLVarNames.videoAnnotationTable, SSSQLVarNames.x);
      column(columns, SSSQLVarNames.videoAnnotationTable, SSSQLVarNames.y);
      column(columns, SSSQLVarNames.videoAnnotationTable, SSSQLVarNames.timePoint);
      
      table(tables, SSSQLVarNames.entityTable);
      table(tables, SSSQLVarNames.videoAnnotationTable);
      table(tables, SSSQLVarNames.videoAnnotationsTable);
      
      where(wheres, SSSQLVarNames.videoAnnotationsTable, SSSQLVarNames.videoId, video);
      
      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.videoAnnotationTable,  SSSQLVarNames.videoAnnotationId);
      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.videoAnnotationsTable, SSSQLVarNames.videoAnnotationId);

      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        annotation =
          SSVideoAnnotation.get(bindingStrToUri   (resultSet, SSSQLVarNames.videoAnnotationId),
            bindingStrToLong  (resultSet, SSSQLVarNames.timePoint),
            bindingStrToFloat (resultSet, SSSQLVarNames.x),
            bindingStrToFloat (resultSet, SSSQLVarNames.y));
      
        annotation.creationTime = bindingStrToLong        (resultSet, SSSQLVarNames.creationTime);
        annotation.label        = bindingStrToLabel       (resultSet, SSSQLVarNames.label);
        annotation.description  = bindingStrToTextComment (resultSet, SSSQLVarNames.description);
          
        annotations.add(annotation);
      }
      
      return annotations;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  
}