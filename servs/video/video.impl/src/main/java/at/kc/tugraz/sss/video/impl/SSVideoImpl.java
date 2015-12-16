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
package at.kc.tugraz.sss.video.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileAddRet;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServImplWithDBA;

import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.sss.video.api.SSVideoClientI;
import at.kc.tugraz.sss.video.api.SSVideoServerI;
import at.kc.tugraz.sss.video.datatypes.SSVideo;
import at.kc.tugraz.sss.video.datatypes.SSVideoAnnotation;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoAnnotationGetPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoAnnotationsGetPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoAnnotationsSetPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserAddFromClientPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserAddPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserAnnotationAddPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserGetPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideosUserGetPar;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideoAnnotationsSetRet;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideoUserAddRet;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideoUserAnnotationAddRet;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideosUserGetRet;
import at.kc.tugraz.sss.video.impl.fct.sql.SSVideoSQLFct;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.SSClientE;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityContext;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSPushEntitiesToUsersI;
import at.tugraz.sss.serv.SSPushEntitiesToUsersPar;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServRetI; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFilesGetPar;
import at.tugraz.sss.servs.location.api.SSLocationServerI;
import at.tugraz.sss.servs.location.datatype.par.SSLocationAddPar;
import java.util.Map;

public class SSVideoImpl
extends SSServImplWithDBA
implements
  SSVideoClientI,
  SSVideoServerI,
  SSDescribeEntityI,
  SSPushEntitiesToUsersI,
  SSAddAffiliatedEntitiesToCircleI,
  SSUsersResourcesGathererI{
  
  private final SSVideoSQLFct     sql;
  private final SSEntityServerI   entityServ;
  private final SSCircleServerI   circleServ;
  private final SSLocationServerI locationServ;
  
  public SSVideoImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sql          = new SSVideoSQLFct(dbSQL, SSVocConf.systemUserUri);
    this.entityServ   = (SSEntityServerI)   SSServReg.getServ(SSEntityServerI.class);
    this.circleServ   = (SSCircleServerI)   SSServReg.getServ(SSCircleServerI.class);
    this.locationServ = (SSLocationServerI) SSServReg.getServ(SSLocationServerI.class);
  }
  
  @Override
  public void getUsersResources(
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      final SSVideosUserGetPar videosGetPar =
        new SSVideosUserGetPar(
          null, //user
          null, //forEntity,
          true, //withUserRestriction
          false);//invokeEntityHandlers
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        videosGetPar.user = userID;
        
        for(SSEntity video : videosGet(videosGetPar)){
          
          usersEntities.get(user).add(
            new SSEntityContext(
              video.id,
              SSEntityE.video,
              null,
              null));
        }
      }
      
    }catch(Exception error){
      SSLogU.err(error);
      SSServErrReg.reset();
    }
  }
  
  @Override
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSAddAffiliatedEntitiesToCirclePar par) throws SSErr{
    
    try{
      
      final List<SSEntity> affiliatedEntities = new ArrayList<>();
      
      for(SSEntity entityAdded : par.entities){
        
        switch(entityAdded.type){
          
          case video:{
            
            if(SSStrU.contains(par.recursiveEntities, entityAdded)){
              continue;
            }else{
              SSUri.addDistinctWithoutNull(par.recursiveEntities, entityAdded.id);
            }
            
            for(SSEntity videoContentEntity :
              videoAnnotationsGet(
                new SSVideoAnnotationsGetPar(
                  par.user,
                  entityAdded.id,
                  par.withUserRestriction))){
              
              if(SSStrU.contains(par.recursiveEntities, videoContentEntity)){
                continue;
              }
              
              SSEntity.addEntitiesDistinctWithoutNull(
                affiliatedEntities,
                videoContentEntity);
            }
          }
        }
        
        for(SSEntity video :
          videosGet(
            new SSVideosUserGetPar(
              par.user,
              entityAdded.id,
              par.withUserRestriction,
              false))){
          
          if(SSStrU.contains(par.recursiveEntities, video)){
            continue;
          }
          
          SSEntity.addEntitiesDistinctWithoutNull(
            affiliatedEntities,
            video);
        }
      }
      
      if(affiliatedEntities.isEmpty()){
        return affiliatedEntities;
      }
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          par.user,
          par.circle, //circle
          SSUri.getDistinctNotNullFromEntities(affiliatedEntities), //entities
          false, //withUserRestriction
          false)); //shouldCommit
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities,
        SSServCallerU.handleAddAffiliatedEntitiesToCircle(
          par.user,
          par.circle,
          affiliatedEntities, //entities
          par.recursiveEntities,
          par.withUserRestriction));
      
      return affiliatedEntities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void pushEntitiesToUsers(
    final SSPushEntitiesToUsersPar par) throws SSErr {
    
    try{
      for(SSEntity entityToPush : par.entities){
        
        switch(entityToPush.type){
          case video: {
            
            for(SSUri userToPushTo : par.users){
              sql.addVideoToUser(userToPushTo, entityToPush.id);
            }
            
            break;
          }
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      switch(entity.type){
        
        case video:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          return SSVideo.get(
            videoGet(
              new SSVideoUserGetPar(
                par.user,
                entity.id,
                par.withUserRestriction,
                false)), //invokeEntityHandlers
            entity);
        }
      }
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI videoAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSVideoUserAddFromClientPar par      = (SSVideoUserAddFromClientPar) parA.getFromJSON(SSVideoUserAddFromClientPar.class);
      final SSUri                       videoURI = videoAdd(par);
      
      if(
        par.latitude  != null &&
        par.longitude != null){
        
        locationServ.locationAdd(
          new SSLocationAddPar(
            par.user,
            videoURI,
            par.latitude,
            par.longitude,
            par.accuracy,
            par.withUserRestriction, //withUserRestriction,
            par.shouldCommit)); //shouldCommit
      }
      
      return SSVideoUserAddRet.get(videoURI);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri videoAdd(final SSVideoUserAddPar par) throws SSErr{
    
    try{
      
      final SSUri videoUri;
      
      if(par.uuid != null){
        videoUri = SSVocConf.vocURICreateFromId(par.uuid);
      }else{
        
        if(par.link != null){
          videoUri = par.link;
        }else{
          videoUri = SSVocConf.vocURICreate();
        }
      }
      
      final SSFileRepoServerI fileServ = (SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri video =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            videoUri,
            SSEntityE.video, //type,
            par.label, //label
            par.description,//description,
            par.creationTime, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists,
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(video == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sql.addVideo(
        video,
        par.genre,
        par.link,
        par.type);
      
      if(par.forEntity != null){
        
        final SSUri forEntity =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              par.user,
              par.forEntity,
              null, //type,
              null, //label
              null,//description,
              null, //creationTime,
              null, //read,
              false, //setPublic
              true, //createIfNotExists
              par.withUserRestriction, //withUserRestriction
              false)); //shouldCommit)
        
        if(forEntity == null){
          dbSQL.rollBack(par.shouldCommit);
          return null;
        }
        
        sql.addVideoToEntity(videoUri, par.forEntity);
      }
      
      if(par.file != null){
        
        final SSFileAddRet fileAddRet =
          fileServ.fileAdd(
            new SSEntityFileAddPar(
              par.user,
              null,  //fileBytes
              null,  //fileLength
              null,  //fileExt
              par.file, //file
              null, //type
              null, //label
              video, //entity,
              false, //createThumb
              null, //entityToAddThumbTo
              true, //removeExistingFilesForEntity
              par.withUserRestriction,
              par.shouldCommit));
        
        if(
          fileAddRet      == null ||
          fileAddRet.file == null){
          dbSQL.rollBack(par.shouldCommit);
          return null;
        }
      }
      
      sql.addVideoToUser(
        par.user,
        video);
      
      dbSQL.commit(par.shouldCommit);
      
      return video;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return videoAdd(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI videoAnnotationsSet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSVideoAnnotationsSetPar par = (SSVideoAnnotationsSetPar) parA.getFromJSON(SSVideoAnnotationsSetPar.class);
      
      return SSVideoAnnotationsSetRet.get(videoAnnotationsSet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> videoAnnotationsSet(final SSVideoAnnotationsSetPar par) throws SSErr{
    
    try{
      
      final List<SSUri> annotations = new ArrayList<>();
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri video =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.video,
            null, //type,
            null, //label
            null,//description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            false, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(video == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      if(
        par.removeExisting &&
        par.withUserRestriction){
        
        for(SSUri annotation : sql.getAnnotations(par.video)){
          
          if(!sql.isUserAuthor(par.user, annotation, par.withUserRestriction)){
            continue;
          }
          
          sql.removeAnnotation(annotation);
        }
      }
      
      for(int counter = 0; counter < par.labels.size(); counter++){
        
        SSUri.addDistinctWithoutNull(
          annotations,
          videoAnnotationAdd(
            new SSVideoUserAnnotationAddPar(
              par.user,
              par.video,
              par.timePoints.get(counter),
              par.x.get(counter),
              par.y.get(counter),
              par.labels.get(counter),
              par.descriptions.get(counter),
              par.withUserRestriction,
              false))); //shouldCommit)
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return annotations;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return videoAnnotationsSet(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI videoAnnotationAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSVideoUserAnnotationAddPar par = (SSVideoUserAnnotationAddPar) parA.getFromJSON(SSVideoUserAnnotationAddPar.class);
      
      return SSVideoUserAnnotationAddRet.get(videoAnnotationAdd(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri videoAnnotationAdd(final SSVideoUserAnnotationAddPar par) throws SSErr{
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri video =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.video,
            null, //type,
            null, //label
            null,//description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            false, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(video == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      final SSUri annotation =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            SSVocConf.vocURICreate(), //entity
            SSEntityE.videoAnnotation, //type,
            par.label, //label
            par.description,//description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(annotation == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sql.createAnnotation(
        par.video,
        annotation,
        par.x,
        par.y,
        par.timePoint);
      
      dbSQL.commit(par.shouldCommit);
      
      return annotation;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return videoAnnotationAdd(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSVideo videoGet(final SSVideoUserGetPar par) throws SSErr{
    
    try{
      
      SSVideo video = sql.getVideo(par.video);
      
      if(video == null){
        return null;
      }
      
      SSEntityDescriberPar   descPar;
      
      if(par.invokeEntityHandlers){
        descPar              = new SSEntityDescriberPar(par.video);
        descPar.setLocations = true;
      }else{
        descPar = null;
      }
      
      final SSFileRepoServerI fileServ = (SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class);
      
      final SSEntity videoEntity =
        entityServ.entityGet(
          new SSEntityGetPar(
            par.user,
            par.video,
            par.withUserRestriction,
            descPar)); //descPar
      
      if(videoEntity == null){
        return null;
      }
      
      video =
        SSVideo.get(
          video,
          videoEntity);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        video.annotations,
        videoAnnotationsGet(
          new SSVideoAnnotationsGetPar(
            par.user,
            video.id,
            par.withUserRestriction)));
      
      for(SSEntity file :
        fileServ.filesGet(
          new SSEntityFilesGetPar(
            par.user,
            par.video,
            par.withUserRestriction,
            false))){ //invokeEntityHandlers
        
        video.file = file;
        break;
      }
      
      return video;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSVideoAnnotation videoAnnotationGet(final SSVideoAnnotationGetPar par) throws SSErr{
    
    try{
      
      final SSEntity annotationEntity =
        sql.getEntityTest(
          par.user,
          par.annotation,
          par.withUserRestriction);
      
      if(annotationEntity == null){
        return null;
      }
      
      final SSVideoAnnotation annotation = sql.getAnnotation(par.annotation);
      
      if(annotation == null){
        return null;
      }
      
      return SSVideoAnnotation.get(annotation, annotationEntity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> videoAnnotationsGet(final SSVideoAnnotationsGetPar par) throws SSErr{
    
    try{
      
      final SSEntity video =
        sql.getEntityTest(
          par.user,
          par.video,
          par.withUserRestriction);
      
      if(video == null){
        return null;
      }
      
      final List<SSEntity>          annotations           = new ArrayList<>();
      final List<SSUri>             annotationURIs        = sql.getAnnotations(par.video);
      final SSVideoAnnotationGetPar videoAnnotationGetPar =
        new SSVideoAnnotationGetPar(
          par.user,
          null, //annotation
          par.withUserRestriction);
      
      for(SSUri annotation : annotationURIs){
        
        videoAnnotationGetPar.annotation = annotation;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          annotations,
          videoAnnotationGet(videoAnnotationGetPar));
      }
      
      return annotations;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI videosGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      SSServCallerU.checkKey(parA);
      
      final SSVideosUserGetPar par = (SSVideosUserGetPar) parA.getFromJSON(SSVideosUserGetPar.class);
      
      return SSVideosUserGetRet.get(videosGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> videosGet(final SSVideosUserGetPar par) throws SSErr{
    
    try{
      
      if(par.withUserRestriction){
        
        if(par.forEntity != null){
          
          final SSEntity forEntity =
            sql.getEntityTest(
              par.user,
              par.forEntity,
              par.withUserRestriction);
          
          if(forEntity == null){
            return new ArrayList<>();
          }
        }
      }
      
      final List<SSEntity>    videos      = new ArrayList<>();
      final List<SSUri>       videoURIs   = sql.getVideoURIs(par.user, par.forEntity);
      final SSVideoUserGetPar videoGetPar =
        new SSVideoUserGetPar(
          par.user,
          null, //video
          par.withUserRestriction,
          par.invokeEntityHandlers);
      
      for(SSUri videoURI : videoURIs){
        
        videoGetPar.video = videoURI;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          videos,
          videoGet(videoGetPar));
      }
      
      return videos;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}