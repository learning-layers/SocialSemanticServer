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
package at.tugraz.sss.servs.video.impl;

import at.tugraz.sss.servs.activity.api.*;
import at.tugraz.sss.serv.datatype.par.SSCircleEntitiesAddPar;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.servs.file.api.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.servs.common.impl.SSUserCommons;
import at.tugraz.sss.servs.video.api.SSVideoClientI;
import at.tugraz.sss.servs.video.api.SSVideoServerI;
import at.tugraz.sss.servs.video.datatype.SSVideo;
import at.tugraz.sss.servs.video.datatype.SSVideoAnnotation;
import at.tugraz.sss.servs.video.datatype.SSVideoAnnotationGetPar;
import at.tugraz.sss.servs.video.datatype.SSVideoAnnotationsGetPar;
import at.tugraz.sss.servs.video.datatype.SSVideoAnnotationsSetPar;
import at.tugraz.sss.servs.video.datatype.SSVideoUserAddFromClientPar;
import at.tugraz.sss.servs.video.datatype.SSVideoUserAddPar;
import at.tugraz.sss.servs.video.datatype.SSVideoUserAnnotationAddPar;
import at.tugraz.sss.servs.video.datatype.SSVideoUserGetPar;
import at.tugraz.sss.servs.video.datatype.SSVideosUserGetPar;
import at.tugraz.sss.servs.video.datatype.SSVideoAnnotationsSetRet;
import at.tugraz.sss.servs.video.datatype.SSVideoUserAddRet;
import at.tugraz.sss.servs.video.datatype.SSVideoUserAnnotationAddRet;
import at.tugraz.sss.servs.video.datatype.SSVideosUserGetRet;
import at.tugraz.sss.serv.entity.api.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.datatype.par.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.entity.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.SSEntityContext;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.entity.api.SSPushEntitiesToUsersI;
import at.tugraz.sss.serv.datatype.par.SSPushEntitiesToUsersPar;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.par.SSServPar;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI;
import at.tugraz.sss.serv.entity.api.SSUsersResourcesGathererI;
import at.tugraz.sss.servs.eval.api.*;
import at.tugraz.sss.servs.file.datatype.*;
import at.tugraz.sss.servs.location.api.SSLocationServerI;
import at.tugraz.sss.servs.location.datatype.*;
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
  
  private final SSVideoSQLFct       sql;
  private final SSEntityServerI     entityServ;
  private final SSEntityServerI     circleServ;
  private final SSLocationServerI   locationServ;
  private final SSUserCommons       userCommons;
  private final SSVideoActAndLogFct actAndLocFct;
  
  public SSVideoImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql          = new SSVideoSQLFct(dbSQL);
    this.entityServ   = (SSEntityServerI)   SSServReg.getServ(SSEntityServerI.class);
    this.circleServ   = (SSEntityServerI)   SSServReg.getServ(SSEntityServerI.class);
    this.locationServ = (SSLocationServerI) SSServReg.getServ(SSLocationServerI.class);
    this.userCommons  = new SSUserCommons();
    
    this.actAndLocFct =
      new SSVideoActAndLogFct(
        (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class),
        (SSEvalServerI)     SSServReg.getServ(SSEvalServerI.class));
  }
  
  @Override
  public void getUsersResources(
    final SSServPar servPar,
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      final SSVideosUserGetPar videosGetPar =
        new SSVideosUserGetPar(
          servPar,
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
    }
  }
  
  @Override
  public List<SSEntity> addAffiliatedEntitiesToCircle(
    final SSServPar servPar,
    final SSAddAffiliatedEntitiesToCirclePar par) throws SSErr{
    
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
                  servPar, 
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
              servPar,
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
          servPar,
          par.user,
          par.circle, //circle
          SSUri.getDistinctNotNullFromEntities(affiliatedEntities), //entities
          false, //withUserRestriction
          false)); //shouldCommit
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities,
        SSServReg.inst.addAffiliatedEntitiesToCircle(
          servPar,
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
    final SSServPar servPar,
    final SSPushEntitiesToUsersPar par) throws SSErr {
    
    try{
      for(SSEntity entityToPush : par.entities){
        
        switch(entityToPush.type){
          case video: {
            
            for(SSUri userToPushTo : par.users){
              sql.addVideoToUser(servPar, userToPushTo, entityToPush.id);
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
    final SSServPar servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      switch(entity.type){
        
        case video:{
          
          if(SSStrU.isEqual(entity, par.recursiveEntity)){
            return entity;
          }
          
          return SSVideo.get(
            videoGet(
              new SSVideoUserGetPar(
                servPar,
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
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSVideoUserAddFromClientPar par      = (SSVideoUserAddFromClientPar) parA.getFromClient(clientType, parA, SSVideoUserAddFromClientPar.class);
      final SSUri                       videoURI = videoAdd(par);
      
      if(
        par.latitude  != null &&
        par.longitude != null){
        
        locationServ.locationAdd(
          new SSLocationAddPar(
            par,
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
        videoUri = SSConf.vocURICreateFromId(par.uuid);
      }else{
        
        if(par.link != null){
          videoUri = par.link;
        }else{
          videoUri = SSConf.vocURICreate();
        }
      }
      
      final SSFileServerI fileServ = (SSFileServerI) SSServReg.getServ(SSFileServerI.class);
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri video =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sql.addVideo(
        par,
        video,
        par.genre,
        par.link,
        par.type);
      
      if(par.forEntity != null){
        
        final SSUri forEntity =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              par,
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
          dbSQL.rollBack(par, par.shouldCommit);
          return null;
        }
        
        sql.addVideoToEntity(par, videoUri, par.forEntity);
      }
      
      if(par.file != null){
        
        final SSFileAddRet fileAddRet =
          fileServ.fileAdd(
            new SSEntityFileAddPar(
              par,
              par.user,
              null,  //fileBytes
              null,  //fileLength
              null,  //fileExt
              par.file, //file
              null, //type
              par.label, //label
              video, //entity,
              false, //createThumb
              null, //entityToAddThumbTo
              true, //removeExistingFilesForEntity
              par.withUserRestriction,
              par.shouldCommit));
        
        if(
          fileAddRet      == null ||
          fileAddRet.file == null){
          dbSQL.rollBack(par, par.shouldCommit);
          return null;
        }
      }
      
      sql.addVideoToUser(
        par,
        par.user,
        video);
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLocFct.createVideo(
        par, 
        video, 
        par.shouldCommit);
      
      return video;
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI videoAnnotationsSet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSVideoAnnotationsSetPar par = (SSVideoAnnotationsSetPar) parA.getFromClient(clientType, parA, SSVideoAnnotationsSetPar.class);
      
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
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri video =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      if(
        par.removeExisting &&
        par.withUserRestriction){
        
        for(SSUri annotation : sql.getAnnotations(par, par.video)){
          
          if(!sql.isUserAuthor(par, par.user, annotation)){
            continue;
          }
          
          sql.removeAnnotation(par, annotation);

          actAndLocFct.removeVideoAnnotation(
            par, 
            annotation, 
            par.shouldCommit);
        }
      }
      
      for(int counter = 0; counter < par.labels.size(); counter++){
        
        SSUri.addDistinctWithoutNull(
          annotations,
          videoAnnotationAdd(
            new SSVideoUserAnnotationAddPar(
              par,
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
      
      dbSQL.commit(par, par.shouldCommit);
      
      return annotations;
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI videoAnnotationAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSVideoUserAnnotationAddPar par = (SSVideoUserAnnotationAddPar) parA.getFromClient(clientType, parA, SSVideoUserAnnotationAddPar.class);
      
      return SSVideoUserAnnotationAddRet.get(videoAnnotationAdd(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri videoAnnotationAdd(final SSVideoUserAnnotationAddPar par) throws SSErr{
    
    try{
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri video =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      final SSUri annotation =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
            par.user,
            SSConf.vocURICreate(), //entity
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sql.createAnnotation(
        par,
        par.video,
        annotation,
        par.x,
        par.y,
        par.timePoint);
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLocFct.createVideoAnnotation(
        par, 
        annotation, 
        par.shouldCommit);
      
      return annotation;
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSVideo videoGet(final SSVideoUserGetPar par) throws SSErr{
    
    try{
      
      SSVideo video =
        sql.getVideo(
          par,
          par.video);
      
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
      
      final SSFileServerI fileServ = (SSFileServerI) SSServReg.getServ(SSFileServerI.class);
      
      final SSEntity videoEntity =
        entityServ.entityGet(
          new SSEntityGetPar(
            par,
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
            par,
            par.user,
            video.id,
            par.withUserRestriction)));
      
      for(SSEntity file :
        fileServ.filesGet(
          new SSEntityFilesGetPar(
            par,
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
          par,
          SSConf.systemUserUri,
          par.user,
          par.annotation,
          par.withUserRestriction);
      
      if(annotationEntity == null){
        return null;
      }
      
      final SSVideoAnnotation annotation = sql.getAnnotation(par, par.annotation);
      
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
          par,
          SSConf.systemUserUri,
          par.user,
          par.video,
          par.withUserRestriction);
      
      if(video == null){
        return null;
      }
      
      final List<SSEntity>          annotations           = new ArrayList<>();
      final List<SSUri>             annotationURIs        = sql.getAnnotations(par, par.video);
      final SSVideoAnnotationGetPar videoAnnotationGetPar =
        new SSVideoAnnotationGetPar(
          par,
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
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSVideosUserGetPar par = (SSVideosUserGetPar) parA.getFromClient(clientType, parA, SSVideosUserGetPar.class);
      
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
              par,
              SSConf.systemUserUri,
              par.user,
              par.forEntity,
              par.withUserRestriction);
          
          if(forEntity == null){
            return new ArrayList<>();
          }
        }
      }
      
      final List<SSEntity>    videos      = new ArrayList<>();
      final List<SSUri>       videoURIs   = sql.getVideoURIs(par, par.user, par.forEntity);
      final SSVideoUserGetPar videoGetPar =
        new SSVideoUserGetPar(
          par,
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