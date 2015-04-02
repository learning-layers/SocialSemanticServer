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

import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSEntityUpdaterI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import at.kc.tugraz.sss.video.api.SSVideoClientI;
import at.kc.tugraz.sss.video.api.SSVideoServerI;
import at.kc.tugraz.sss.video.datatypes.SSVideo;
import at.kc.tugraz.sss.video.datatypes.SSVideoAnnotation;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserAddPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserAnnotationAddPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserGetPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideosUserGetPar;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideoUserAddRet;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideoUserAnnotationAddRet;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideosUserGetRet;
import at.kc.tugraz.sss.video.impl.fct.sql.SSVideoSQLFct;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;

public class SSVideoImpl 
extends SSServImplWithDBA 
implements 
  SSVideoClientI, 
  SSVideoServerI, 
  SSEntityDescriberI, 
  SSEntityUpdaterI, 
  SSEntityHandlerImplI{
  
  private final SSVideoSQLFct sqlFct;
  
  public SSVideoImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{

    super(conf, dbSQL);

    this.sqlFct = new SSVideoSQLFct(dbSQL);
  }
  
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSUri     userUri, 
    final SSEntityE entityType, 
    final SSUri     entityUri, 
    final Boolean   removeUserTags, 
    final Boolean   removeUserRatings, 
    final Boolean   removeFromUserColls, 
    final Boolean   removeUserLocations) throws Exception{
    
  }

  @Override
  public Boolean setUserEntityPublic(
    final SSUri     userUri, 
    final SSUri     entityUri, 
    final SSEntityE entityType, 
    final SSUri     publicCircleUri) throws Exception{
   
   return false; 
  }

  @Override
  public void shareUserEntity(
    final SSUri       user, 
    final List<SSUri> usersToShareWith, 
    final SSUri       entity, 
    final SSUri       circle,
    final SSEntityE   type,
    final Boolean     saveActivity) throws Exception{
    
    try{
      switch(type){
        
        case video:{
          
          for(SSUri userToShareWith : usersToShareWith){

            sqlFct.addVideoToUser(userToShareWith, entity);
            
            for(SSVideoAnnotation annotation : sqlFct.getAnnotations(entity)){
              
              try{
                SSServCallerU.canUserReadEntity(user, annotation.id);
              }catch(Exception error){
                SSServErrReg.reset();
                continue;
              }
              
              SSServCaller.circleEntitiesAdd(
                userToShareWith, 
                circle, 
                SSUri.asListWithoutNullAndEmpty(annotation.id), 
                false, 
                false, 
                false);
            }
          }
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public Boolean copyUserEntity(
    final SSUri       user, 
    final List<SSUri> users, 
    final SSUri       entity, 
    final List<SSUri> entitiesToExclude, 
    final SSEntityE   entityType) throws Exception{
    
    return false;
  }

  @Override
  public void shareUserEntityWithCircle(
    final SSUri     user, 
    final SSUri     circle,
    final SSUri     entity, 
    final SSEntityE type) throws Exception{

    switch(type){
      
      case video:{
      
        for(SSVideoAnnotation annotation : sqlFct.getAnnotations(entity)){
        
          try{
            SSServCallerU.canUserReadEntity(user, annotation.id);
          }catch(Exception error){
            SSServErrReg.reset();
            continue;
          }
          
          SSServCaller.circleEntitiesAdd(
            user, 
            circle, 
            SSUri.asListWithoutNullAndEmpty(annotation.id), 
            false, 
            false, 
            false);
        }
      }
    }
  }

  @Override
  public List<SSUri> getSubEntities(
    final SSUri     user, 
    final SSUri     entity, 
    final SSEntityE type) throws Exception{
    
    return null;
  }

  @Override
  public List<SSUri> getParentEntities(
    final SSUri     user, 
    final SSUri     entity, 
    final SSEntityE type) throws Exception{
    
    return null;
  }
  
  @Override
  public void updateEntity(
    final SSServPar parA) throws Exception{

    final SSEntityUpdatePar par = (SSEntityUpdatePar)parA;
    
    if(!par.videos.isEmpty()){

      for(SSUri video : par.videos){

        SSServCaller.videoUserAdd(
          par.user, 
          video, 
          par.entity, 
          null,
          false);
      }
    }
  }
  
  @Override
  public SSEntity getDescForEntity(
    final SSServPar parA,
    final SSEntity   desc) throws Exception{
    
    final SSEntityDescGetPar par = (SSEntityDescGetPar)parA;
      
    switch(desc.type){
      
      case video:{
        return SSServCaller.videoUserGet(par.user, desc.id);
      }
      
      default: return desc;
    }	
  }
  
  @Override
  public SSEntity getUserEntity(
    final SSUri              user,
    final SSEntity           entity) throws Exception{
    
    switch(entity.type){
      case video:
        return SSServCaller.videoUserGet(user, entity.id);
    }	
    
    return entity;
  }
  
  @Override
  public void videoAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSVideoUserAddRet.get(videoUserAdd(parA), parA.op), parA.op);
  }

  @Override
  public SSUri videoUserAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSVideoUserAddPar par      = new SSVideoUserAddPar(parA);
      final SSUri             videoUri;
      
      if(par.uuid != null){
        videoUri = SSServCaller.vocURICreateFromId(par.uuid);
      }else{
        
        if(par.link != null){
          videoUri = par.link;
        }else{
          videoUri = SSServCaller.vocURICreate();
        }
      }
      
//      if(par.link != null){
//        videoUri = par.link;
//      }else{
//
//        if(par.uuid != null){
//          videoUri = SSServCaller.vocURICreateFromId(par.uuid);
//        }else{
//          videoUri = SSServCaller.vocURICreate();
//        }
//      }
      
      if(par.forEntity != null){
        SSServCallerU.canUserReadEntity(par.user, par.forEntity);
      }
      
      if(par.link != null){
        SSServCallerU.canUserReadEntity(par.user, par.link);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntityToPrivCircleAdd(
        par.user,
        videoUri,
        SSEntityE.video, 
        par.label, 
        par.description, 
        par.creationTime, 
        false);
      
      if(par.forEntity != null){
        
        SSServCaller.entityEntityToPrivCircleAdd(
          par.user, 
          par.forEntity, 
          SSEntityE.entity, 
          null, 
          null, 
          null, 
          false);
      }
      
      if(par.link != null){
        
        SSServCaller.entityEntityToPrivCircleAdd(
          par.user, 
          par.link, 
          SSEntityE.entity, 
          null, 
          null, 
          null, 
          false);
      }
      
      sqlFct.addVideo(
        videoUri,
        par.genre,
        par.forEntity,
        par.link);
      
      sqlFct.addVideoToUser(
        par.user,
        videoUri);
      
      if(
        par.latitude  != null &&
        par.longitude != null){
        
        SSServCaller.entityLocationsAdd(
          par.user, 
          videoUri,
          par.latitude,
          par.longitude,
          par.accuracy,
          false);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return videoUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return videoUserAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void videoAnnotationAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSVideoUserAnnotationAddRet.get(videoUserAnnotationAdd(parA), parA.op), parA.op);
  }
  
  @Override
  public SSUri videoUserAnnotationAdd(final SSServPar parA) throws Exception{
  
    try{
      final SSVideoUserAnnotationAddPar    par           = new SSVideoUserAnnotationAddPar(parA);
      final SSUri                          annotationUri = SSServCaller.vocURICreate();

      SSServCallerU.canUserReadEntity(par.user, par.video);
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntityToPrivCircleAdd(
        par.user, 
        annotationUri, 
        SSEntityE.videoAnnotation, 
        par.label, 
        par.description, 
        null, 
        false);
       
      sqlFct.createAnnotation(
        par.video, 
        annotationUri, 
        par.x,
        par.y, 
        par.timePoint);
    
      dbSQL.commit(par.shouldCommit);
      
      return annotationUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA)){
          
          SSServErrReg.reset();
          
          return videoUserAnnotationAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSVideo videoUserGet(final SSServPar parA) throws Exception{
    
    try{
      final SSVideoUserGetPar      par         = new SSVideoUserGetPar(parA);
      final SSVideo                video;
      
      SSServCallerU.canUserReadEntity(par.user, par.video);
      
      video = sqlFct.getVideo(par.user, par.video);
        
      for(SSVideoAnnotation annotation : sqlFct.getAnnotations(video.id)){

        try{
          SSServCallerU.canUserReadEntity(par.user, annotation.id);
        }catch(Exception error){
          SSServErrReg.reset();
          continue;
        }
        
        video.annotations.add(annotation);
      }
      
      video.locations.addAll(
        SSServCaller.entityLocationsGet(
          par.user,
          video.id));
      
      return video;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void videosGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSVideosUserGetRet.get(videosUserGet(parA), parA.op), parA.op);
  }
  
  @Override
  public List<SSVideo> videosUserGet(final SSServPar parA) throws Exception{
    
    try{
      final SSVideosUserGetPar      par         = new SSVideosUserGetPar(parA);
      final List<SSVideo>           videos      = new ArrayList<>();
      
      if(par.forEntity != null){
        SSServCallerU.canUserReadEntity(par.user, par.forEntity);
      }
      
      for(SSVideo video : sqlFct.getVideos(par.forUser, par.forEntity)){
        
        for(SSVideoAnnotation annotation : sqlFct.getAnnotations(video.id)){
        
          try{
            SSServCallerU.canUserReadEntity(par.user, annotation.id);
          }catch(Exception error){
            SSServErrReg.reset();
            continue;
          }
          
          video.annotations.add(annotation);
        }
        
        video.locations.addAll(
          SSServCaller.entityLocationsGet(
            par.user,
            video.id));
        
        videos.add(video);
      }
      
      return videos;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}