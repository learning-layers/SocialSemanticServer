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

import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.sss.video.datatypes.SSVideo;
import at.kc.tugraz.sss.video.datatypes.SSVideoAnnotation;
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
      
      insert(inserts, SSSQLVarU.userId,   user);
      insert(inserts, SSSQLVarU.videoId,  video);
      
      dbSQL.insert(userVideosTable, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addVideo(
    final SSUri         video,
    final String        genre,
    final SSUri         forEntity) throws Exception{
    
     try{
      final Map<String, String> inserts    = new HashMap<>();
      
      if(genre == null){
        insert    (inserts,    SSSQLVarU.genre,     SSStrU.empty);
      }else{
        insert    (inserts,    SSSQLVarU.genre,     genre);
      }
      
      insert    (inserts,    SSSQLVarU.videoId,     video);
      
      dbSQL.insert(videoTable, inserts);
      
      if(forEntity != null){
        
        inserts.clear();
        
        insert(inserts, SSSQLVarU.entityId, forEntity);
        insert(inserts, SSSQLVarU.videoId,  video);
        
        dbSQL.insert(entityVideosTable, inserts);
      }
      
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
  }
  
  public void addAnnotation(
    final SSUri video,
    final SSUri videoAnnotation,
    final Float x, 
    final Float y,
    final Long  timePoint) throws Exception{
   
    try{
      final Map<String, String> inserts    = new HashMap<>();
      
      if(x == null){
        insert    (inserts,    SSSQLVarU.x,     SSStrU.empty);
      }else{
        insert    (inserts,    SSSQLVarU.x,     x);
      }
      
      if(y == null){
        insert    (inserts,    SSSQLVarU.y,     SSStrU.empty);
      }else{
        insert    (inserts,    SSSQLVarU.y,     y);
      }
      
      if(timePoint == null){
        insert    (inserts,    SSSQLVarU.timePoint,     SSStrU.empty);
      }else{
        insert    (inserts,    SSSQLVarU.timePoint,     timePoint);
      }
      
      insert    (inserts,    SSSQLVarU.videoAnnotationId,  videoAnnotation);
      
      dbSQL.insert(videoAnnotationTable, inserts);
      
      inserts.clear();
      
      insert    (inserts,    SSSQLVarU.videoId,            video);
      insert    (inserts,    SSSQLVarU.videoAnnotationId,  videoAnnotation);
      
      dbSQL.insert(videoAnnotationsTable, inserts);
      
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
  }
  
  public List<SSVideo> getVideos(
    final SSUri   user,     
    final SSUri   forEntity) throws Exception{
    
    ResultSet resultSet = null;
      
    try{
      final List<SSVideo>         videos      = new ArrayList<>();
      final List<String>          columns     = new ArrayList<>();
      final List<String>          tables      = new ArrayList<>();
      final Map<String, String>   wheres      = new HashMap<>();
      final List<String>          tableCons   = new ArrayList<>();
      
      SSVideo video;
      
      column(columns, videoTable,          SSSQLVarU.videoId);
      column(columns, videoTable,          SSSQLVarU.genre);
      column(columns, entityTable,         SSSQLVarU.creationTime);
      column(columns, entityTable,         SSSQLVarU.label);
      column(columns, entityTable,         SSSQLVarU.description);

      table(tables, entityTable);
      table(tables, videoTable);
      
      if(user != null){
        where    (wheres,    userVideosTable, SSSQLVarU.userId, user);
        table    (tables,    userVideosTable);
        tableCon (tableCons, entityTable, SSSQLVarU.id, userVideosTable, SSSQLVarU.videoId);
      }
      
      if(forEntity != null){
        where    (wheres,    entityVideosTable, SSSQLVarU.entityId, forEntity);
        table    (tables,    entityVideosTable);
        tableCon (tableCons, entityTable, SSSQLVarU.id, entityVideosTable, SSSQLVarU.videoId);
      }
      
      tableCon(tableCons, entityTable, SSSQLVarU.id, videoTable,      SSSQLVarU.videoId);
      
      if(wheres.isEmpty()){
        resultSet = dbSQL.select(tables, columns, tableCons);
      }else{
        resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      }
      
      while(resultSet.next()){
        
        video =  
          SSVideo.get(
            bindingStrToUri         (resultSet, SSSQLVarU.videoId),
            bindingStr              (resultSet, SSSQLVarU.genre),
            new ArrayList<>());
          
        video.creationTime = bindingStrToLong        (resultSet, SSSQLVarU.creationTime);
        video.label        = bindingStrToLabel       (resultSet, SSSQLVarU.label);
        video.description  = bindingStrToTextComment (resultSet, SSSQLVarU.description);
        
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
      
      column(columns, entityTable,          SSSQLVarU.creationTime);
      column(columns, entityTable,          SSSQLVarU.label);
      column(columns, entityTable,          SSSQLVarU.description);
      column(columns, videoAnnotationTable, SSSQLVarU.videoAnnotationId);
      column(columns, videoAnnotationTable, SSSQLVarU.x);
      column(columns, videoAnnotationTable, SSSQLVarU.y);
      column(columns, videoAnnotationTable, SSSQLVarU.timePoint);
      
      table(tables, entityTable);
      table(tables, videoAnnotationTable);
      table(tables, videoAnnotationsTable);
      
      where(wheres, videoAnnotationsTable, SSSQLVarU.videoId, video);
      
      tableCon(tableCons, entityTable, SSSQLVarU.id, videoAnnotationTable,  SSSQLVarU.videoAnnotationId);
      tableCon(tableCons, entityTable, SSSQLVarU.id, videoAnnotationsTable, SSSQLVarU.videoAnnotationId);

      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      while(resultSet.next()){
        
        annotation =
          SSVideoAnnotation.get(
            bindingStrToUri   (resultSet, SSSQLVarU.videoAnnotationId),
            bindingStrToLong  (resultSet, SSSQLVarU.timePoint),
            bindingStrToFloat (resultSet, SSSQLVarU.x),
            bindingStrToFloat (resultSet, SSSQLVarU.y));
      
        annotation.creationTime = bindingStrToLong        (resultSet, SSSQLVarU.creationTime);
        annotation.label        = bindingStrToLabel       (resultSet, SSSQLVarU.label);
        annotation.description  = bindingStrToTextComment (resultSet, SSSQLVarU.description);
          
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