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
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
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
import at.tugraz.sss.serv.SSCircleContentChangedPar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.servs.location.api.SSLocationServerI;
import at.tugraz.sss.servs.location.datatype.par.SSLocationAddPar;

public class SSVideoImpl 
extends SSServImplWithDBA 
implements 
  SSVideoClientI, 
  SSVideoServerI, 
  SSEntityHandlerImplI{
  
  private final SSVideoSQLFct sqlFct;
  
  public SSVideoImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    this.sqlFct = new SSVideoSQLFct(dbSQL);
  }
  
  @Override
  public void circleContentChanged(final SSCircleContentChangedPar par) throws Exception{
    
    try{
      
      for(SSEntity entityToAdd : par.entitiesToAdd){
        
        switch(entityToAdd.type){
          
          case video:{
            
            for(SSVideoAnnotation annotation : sqlFct.getAnnotations(entityToAdd.id)){
              
              try{
                SSServCallerU.canUserReadEntity(par.user, annotation.id);
              }catch(Exception error){
                SSServErrReg.reset();
                continue;
              }
              
              ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleEntitiesAdd(
                new SSCircleEntitiesAddPar(
                  null,
                  null,
                  par.user,
                  par.circle,
                  SSUri.asListWithoutNullAndEmpty(annotation.id),
                  false,
                  false));
            }

            for(SSUri userToPushEntityTo : par.usersToPushEntitiesTo){
              sqlFct.addVideoToUser(userToPushEntityTo, entityToAdd.id);
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
  public void copyEntity(
    final SSUri       user, 
    final List<SSUri> users, 
    final SSUri       entity, 
    final List<SSUri> entitiesToExclude, 
    final SSEntityE   entityType) throws Exception{
    
  }
  
  @Override
  public List<SSUri> getSubEntities(
    final SSUri     user, 
    final SSUri     entity, 
    final SSEntityE type) throws Exception{
    
    return new ArrayList<>();
  }

  @Override
  public List<SSUri> getParentEntities(
    final SSUri     user, 
    final SSUri     entity, 
    final SSEntityE type) throws Exception{
    
    return new ArrayList<>();
  }
  
  @Override
  public SSEntity getUserEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      switch(entity.type){
        
        case video:{
          
          return SSVideo.get(
            videoGet(
              new SSVideoUserGetPar(
                null,
                null,
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
  public void videoAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSVideoUserAddPar par = (SSVideoUserAddPar) parA.getFromJSON(SSVideoUserAddPar.class);
    
    sSCon.writeRetFullToClient(SSVideoUserAddRet.get(videoAdd(par)));
  }

  @Override
  public SSUri videoAdd(final SSVideoUserAddPar par) throws Exception{
    
    try{
      
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
      
      dbSQL.startTrans(par.shouldCommit);
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          videoUri,
          SSEntityE.video, //type,
          par.label, //label
          par.description,//description,
          null, //entitiesToAttach,
          par.creationTime, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      if(par.forEntity != null){
        
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            par.user,
            par.forEntity,
            null, //type,
            null, //label
            null,//description,
            null, //entitiesToAttach,
            par.creationTime, //creationTime,
            null, //read,
            false, //setPublic
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      }      
      
      if(par.link != null){
        
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            par.user,
            par.link,
            null, //type,
            null, //label
            null,//description,
            null, //entitiesToAttach,
            par.creationTime, //creationTime,
            null, //read,
            false, //setPublic
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
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
        
        ((SSLocationServerI) SSServReg.getServ(SSLocationServerI.class)).locationAdd(
          new SSLocationAddPar(
            null, 
            null,
            par.user,
            videoUri,
            par.latitude,
            par.longitude,
            par.accuracy,
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return videoUri;
      
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
  public void videoAnnotationAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSVideoUserAnnotationAddPar par = (SSVideoUserAnnotationAddPar) parA.getFromJSON(SSVideoUserAnnotationAddPar.class);
    
    sSCon.writeRetFullToClient(SSVideoUserAnnotationAddRet.get(videoAnnotationAdd(par)));
  }
  
  @Override
  public SSUri videoAnnotationAdd(final SSVideoUserAnnotationAddPar par) throws Exception{
  
    try{
      final SSUri annotationUri = SSServCaller.vocURICreate();

      dbSQL.startTrans(par.shouldCommit);
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          par.video,
          null, //type,
          null, //label
          null,//description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          annotationUri,
          SSEntityE.videoAnnotation, //type,
          par.label, //label
          par.description,//description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
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
  public SSVideo videoGet(final SSVideoUserGetPar par) throws Exception{
    
    try{
      final SSVideo                video;
      final SSEntityDescriberPar   descPar;
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.video)){
          return null;
        }
      }
      
      if(par.invokeEntityHandlers){
        descPar              = new SSEntityDescriberPar(par.video);
        descPar.setLocations = true;
      }else{
        descPar = null;
      }
      
      video = 
        SSVideo.get(
          sqlFct.getVideo(par.user, par.video), 
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null, 
              null, 
              par.user,
              par.video,
              par.withUserRestriction, 
              descPar))); //descPar
      
      SSEntity.addEntitiesDistinctWithoutNull(
        video.annotations,
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            null,
            null,
            par.user,
            SSUri.getFromEntitites(sqlFct.getAnnotations(video.id)),
            null, //types
            null, //descPar
            par.withUserRestriction)));
      
      return video;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void videosGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSVideosUserGetPar par = (SSVideosUserGetPar) parA.getFromJSON(SSVideosUserGetPar.class);
    
    sSCon.writeRetFullToClient(SSVideosUserGetRet.get(videosGet(par)));
  }
  
  @Override
  public List<SSEntity> videosGet(final SSVideosUserGetPar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
      
        if(par.forEntity != null){
          
          if(!SSServCallerU.canUserRead(par.user, par.forEntity)){
            return new ArrayList<>();
          }
        }
      }
      
      final List<SSEntity> videos    = new ArrayList<>();
      final List<SSUri>    videoURIs = sqlFct.getVideoURIs(par.user, par.forEntity);
      
      for(SSUri videoURI : videoURIs){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          videos,
          videoGet(
            new SSVideoUserGetPar(
              null,
              null,
              par.user,
              videoURI,
              par.withUserRestriction,
              par.invokeEntityHandlers)));
      }
      
      return videos;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}