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
package at.kc.tugraz.sss.app.impl;

import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityDownloadsAddPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServImplWithDBA;

import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.sss.app.api.SSAppServerI;
import at.kc.tugraz.sss.app.api.SSAppClientI;
import at.kc.tugraz.sss.app.datatypes.SSApp;
import at.kc.tugraz.sss.app.datatypes.par.SSAppAddPar;
import at.kc.tugraz.sss.app.datatypes.par.SSAppGetPar;
import at.kc.tugraz.sss.app.datatypes.par.SSAppsDeletePar;
import at.kc.tugraz.sss.app.datatypes.par.SSAppsGetPar;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppAddRet;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppsDeleteRet;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppsGetRet;
import at.kc.tugraz.sss.app.impl.fct.sql.SSAppSQLFct;
import at.kc.tugraz.sss.video.api.SSVideoServerI;
import at.kc.tugraz.sss.video.datatypes.par.SSVideoUserAddPar;
import at.tugraz.sss.serv.SSClientE;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSImageE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServRetI; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.servs.image.api.SSImageServerI;
import at.tugraz.sss.servs.image.datatype.par.SSImageAddPar;

public class SSAppImpl
extends SSServImplWithDBA
implements
  SSAppClientI,
  SSAppServerI,
  SSDescribeEntityI{
  
  private final SSAppSQLFct sqlFct;
  
  public SSAppImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.getServImpl(), (SSDBNoSQLI) SSDBNoSQL.inst.getServImpl());
    
    this.sqlFct = new SSAppSQLFct(dbSQL);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      switch(entity.type){
        
        case app:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          final SSApp app =
            appGet(
              new SSAppGetPar(
                par.user,
                entity.id,
                false));  //invokeEntityHandlers
          
          if(app == null){
            return entity;
          }
          
          return SSApp.get(
            app,
            entity);
        }
        
        default: return entity;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI appAdd(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSAppAddPar         par       = (SSAppAddPar) parA.getFromJSON(SSAppAddPar.class);
      final SSUri               app       = appAdd(par);
      final SSImageServerI      imageServ = (SSImageServerI) SSServReg.getServ(SSImageServerI.class);
      final SSVideoServerI      videoServ = (SSVideoServerI) SSServReg.getServ(SSVideoServerI.class);
      
      if(!par.downloads.isEmpty()){
        
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityDownloadsAdd(
          new SSEntityDownloadsAddPar(
            par.user,
            app,
            par.downloads,
            par.withUserRestriction,
            par.shouldCommit));
      }
      
      for(SSUri screenshot : par.screenShots){
        
        imageServ.imageAdd(
          new SSImageAddPar(
            par.user,
            null,  //uuid
            screenshot, //link
            SSImageE.screenShot, //image type
            app, //forEntity
            null, //file
            false, //createThumb
            false, //isImageToAddTheThumb
            false, //removeThumbsFromEntity
            par.withUserRestriction,
            par.shouldCommit));
      }
      
      for(SSUri video : par.videos){
        
        videoServ.videoAdd(
          new SSVideoUserAddPar(
            par.user,
            null, //uuid
            video, //link
            null, //videoType
            app,  //forEntity
            null, //genre
            null, //label
            null, //description
            null, //creationTime
            null, //file
            par.withUserRestriction,
            par.shouldCommit));
      }
      
      return SSAppAddRet.get(app);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri appAdd(final SSAppAddPar par) throws SSErr{
    
    try{
      
      final SSEntityServerI  entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri app =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            SSVocConf.vocURICreate(), //entity,
            SSEntityE.app, //type,
            par.label, //label,
            null, //description,
            null, //creationTime,
            null, //read,
            true, //setPublic
            true, //createIfNotExists,
            par.withUserRestriction, //withUserRestriction,
            false)); //shouldCommit
      
      if(app == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sqlFct.createApp(
        app,
        par.descriptionShort,
        par.descriptionFunctional,
        par.descriptionTechnical,
        par.descriptionInstall,
        par.downloadIOS,
        par.downloadAndroid,
        par.fork);
      
      dbSQL.commit(par.shouldCommit);
      
      return app;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return appAdd(par);
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
  public SSApp appGet(final SSAppGetPar par) throws SSErr{
    
    try{
      
      final SSEntityDescriberPar descPar;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(par.app);
        
        descPar.setAttachedEntities = true;
      }else{
        descPar = null;
      }
      
      final SSApp app = sqlFct.getApp(par.app);
      
      if(app == null){
        return null;
      }
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEntity        appEntity  =
        entityServ.entityGet(
          new SSEntityGetPar(
            par.user,
            par.app,
            par.withUserRestriction, //withUserRestriction,
            descPar));
      
      return SSApp.get(
        app,
        appEntity); //descPar)));
      
//      if(par.invokeEntityHandlers)
      //TODO handle
//        entity.downloads.addAll(
//          SSServCaller.entityDownloadURIsGet(
//            par.user,
//            app.id));
      
//        try{
//
//          entity.attachedEntities.addAll(
//            SSServCaller.videosUserGet(
//              par.user,
//              null,
//              app.id));
//
//        }catch(SSErr error){
//
//          switch(error.code){
//            case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
//            default: SSServErrReg.regErrThrow(error);
//          }
//        }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI appsGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSAppsGetPar par = (SSAppsGetPar) parA.getFromJSON(SSAppsGetPar.class);
      
      return SSAppsGetRet.get(appsGet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> appsGet(final SSAppsGetPar par) throws SSErr{
    
    try{
      final List<SSEntity>       apps     = new ArrayList<>();
      final List<SSUri>          appURIs  = sqlFct.getAppURIs();
      final SSAppGetPar          appGetPar =
        new SSAppGetPar(
          par.user,
          null, //app
          par.invokeEntityHandlers);
      
      for(SSUri appURI : appURIs){
        
        appGetPar.app = appURI;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          apps,
          appGet(appGetPar));
      }
      
      return apps;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI appsDelete(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      SSServCallerU.checkKey(parA);
      
      final SSAppsDeletePar par = (SSAppsDeletePar) parA.getFromJSON(SSAppsDeletePar.class);
      
      return SSAppsDeleteRet.get(appsDelete(par));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> appsDelete(final SSAppsDeletePar par) throws SSErr {
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.removeApps(par.apps);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.apps;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return appsDelete(par);
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
}