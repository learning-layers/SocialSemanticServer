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

import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityDescriberI;
import at.kc.tugraz.ss.serv.serv.api.SSEntityUpdaterI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.sss.video.api.SSVideoClientI;
import at.kc.tugraz.sss.video.api.SSVideoServerI;
import at.kc.tugraz.sss.video.datatypes.SSVideo;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoAddPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoAnnotationAddPar;
import at.kc.tugraz.sss.video.datatypes.par.SSVideosGetPar;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideoAddRet;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideoAnnotationAddRet;
import at.kc.tugraz.sss.video.datatypes.ret.SSVideosGetRet;
import at.kc.tugraz.sss.video.impl.fct.sql.SSVideoSQLFct;
import java.util.List;
import sss.serv.err.datatypes.SSErrE;

public class SSVideoImpl extends SSServImplWithDBA implements SSVideoClientI, SSVideoServerI, SSEntityDescriberI, SSEntityUpdaterI{
  
  private final SSVideoSQLFct sqlFct;
  
  public SSVideoImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{

    super(conf, null, dbSQL);

    this.sqlFct = new SSVideoSQLFct(dbSQL);
  }
  
  @Override
  public void updateEntity(final SSEntityUpdatePar par) throws Exception{

    if(!par.videos.isEmpty()){

      for(SSUri video : par.videos){
        
        sqlFct.addVideo(
          video, 
          null, 
          par.entity);
      }
    }
  }
  
  @Override
  public SSEntity getDescForEntity(
    final SSEntityDescGetPar par,
    final SSEntity           desc) throws Exception{
    
   /* if(par.getApps){
      
      desc.flags.addAll(
        SSServCaller.flagsGet(
          par.user,
          SSUri.asListWithoutNullAndEmpty(par.entity),
          SSStrU.toStrWithoutEmptyAndNull(),
          null,
          null));
    }
    */
	
    return desc;
  }
  
  @Override
  public void videoAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSVideoAddRet.get(videoAdd(parA), parA.op));
  }

  @Override
  public SSUri videoAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSVideoAddPar par      = new SSVideoAddPar(parA);
      final SSUri         videoUri;
      
      if(par.uuid != null){
        videoUri = SSServCaller.vocURICreate(par.uuid); 
      }else{
        videoUri = SSServCaller.vocURICreate(); 
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntityToPubCircleAdd(
        par.user, 
        videoUri, 
        SSEntityE.video, 
        par.label, 
        par.description, 
        par.creationTime, 
        false);
      
      if(par.forEntity != null){
        
        SSServCaller.entityEntityToPubCircleAdd(
          par.user, 
          par.forEntity, 
          SSEntityE.entity, 
          null, 
          null, 
          null, 
          false);
      }
      
      sqlFct.addVideo(
        videoUri,
        par.genre,
        par.forEntity);
      
      dbSQL.commit(par.shouldCommit);
      
      return videoUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return videoAdd(parA);
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
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSVideoAnnotationAddRet.get(videoAnnotationAdd(parA), parA.op));
  }
  
  @Override
  public SSUri videoAnnotationAdd(final SSServPar parA) throws Exception{
  
    try{
      final SSVideoAnnotationAddPar    par           = new SSVideoAnnotationAddPar(parA);
      final SSUri                      annotationUri = SSServCaller.vocURICreate();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntityToPubCircleAdd(
        par.user, 
        annotationUri, 
        SSEntityE.videoAnnotation, 
        par.label, 
        par.description, 
        null, 
        false);
       
      sqlFct.addAnnotation(
        par.video, 
        annotationUri, 
        par.x,
        par.y, 
        par.timePoint);
    
      dbSQL.commit(par.shouldCommit);
      
      return annotationUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return videoAnnotationAdd(parA);
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
  public void videosGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSVideosGetRet.get(videosGet(parA), parA.op));
  }
  
  @Override
  public List<SSVideo> videosGet(final SSServPar parA) throws Exception{
    
    try{
      final SSVideosGetPar par             = new SSVideosGetPar(parA);
      
      return sqlFct.getVideos(true, par.forEntity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}