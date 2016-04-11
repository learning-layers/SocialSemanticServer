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
package at.tugraz.sss.servs.app.impl;

import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.par.SSEntityDownloadsAddPar;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.servs.app.api.SSAppServerI;
import at.tugraz.sss.servs.app.api.SSAppClientI;
import at.tugraz.sss.servs.app.datatype.SSApp;
import at.tugraz.sss.servs.app.datatype.SSAppAddPar;
import at.tugraz.sss.servs.app.datatype.SSAppGetPar;
import at.tugraz.sss.servs.app.datatype.SSAppsDeletePar;
import at.tugraz.sss.servs.app.datatype.SSAppsGetPar;
import at.tugraz.sss.servs.app.datatype.SSAppAddRet;
import at.tugraz.sss.servs.app.datatype.SSAppsDeleteRet;
import at.tugraz.sss.servs.app.datatype.SSAppsGetRet;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.servs.common.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.datatype.enums.SSImageE;
import at.tugraz.sss.serv.errreg.SSServErrReg;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.entity.impl.*;
import at.tugraz.sss.servs.image.api.SSImageServerI;
import at.tugraz.sss.servs.image.datatype.SSImageAddPar;
import at.tugraz.sss.servs.image.impl.*;
import at.tugraz.sss.servs.video.api.*;
import at.tugraz.sss.servs.video.datatype.*;
import at.tugraz.sss.servs.video.impl.*;

public class SSAppImpl
extends SSEntityImpl
implements
  SSAppClientI,
  SSAppServerI,
  SSDescribeEntityI{
  
  private final SSAppSQLFct       sql          = new SSAppSQLFct(dbSQL);
  private final SSAppActAndLogFct actAndLogFct = new SSAppActAndLogFct();
  
  public SSAppImpl(){
    super(SSCoreConf.instGet().getApp());
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      switch(entity.type){
        
        case app:{
          
          if(SSStrU.isEqual(entity, par.recursiveEntity)){
            return entity;
          }
          
          final SSApp app =
            appGet(
              new SSAppGetPar(
                servPar,
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
      userCommons.checkKeyAndSetUser(parA);
      
      final SSAppAddPar         par       = (SSAppAddPar) parA.getFromClient(clientType, parA, SSAppAddPar.class);
      final SSUri               app       = appAdd(par);
      final SSImageServerI      imageServ = new SSImageImpl();
      final SSVideoServerI      videoServ = new SSVideoImpl();
      
      if(!par.downloads.isEmpty()){
        
        entityDownloadsAdd(
          new SSEntityDownloadsAddPar(
            par,
            par.user,
            app,
            par.downloads,
            par.withUserRestriction,
            par.shouldCommit));
      }
      
      for(SSUri screenshot : par.screenShots){
        
        imageServ.imageAdd(
          new SSImageAddPar(
            par,
            par.user,
            null,  //uuid
            screenshot, //link
            SSImageE.screenShot, //image type
            app, //forEntity
            null, //file
            par.label, //label
            false, //createThumb
            false, //isImageToAddTheThumb
            false, //removeThumbsFromEntity
            par.withUserRestriction,
            par.shouldCommit));
      }
      
      for(SSUri video : par.videos){
        
        videoServ.videoAdd(
          new SSVideoUserAddPar(
            par,
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
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri app =
        entityUpdate(
          new SSEntityUpdatePar(
            par,
            par.user,
            SSConf.vocURICreate(), //entity,
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sql.createApp(
        par,
        app,
        par.descriptionShort,
        par.descriptionFunctional,
        par.descriptionTechnical,
        par.descriptionInstall,
        par.downloadIOS,
        par.downloadAndroid,
        par.fork);
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLogFct.createApp(
        par,
        app,
        par.shouldCommit);
      
      return app;
      
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
  public SSApp appGet(final SSAppGetPar par) throws SSErr{
    
    try{
      
      final SSEntityDescriberPar descPar;
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(par.app);
        
        descPar.setAttachedEntities = true;
      }else{
        descPar = null;
      }
      
      final SSApp app = sql.getApp(par, par.app);
      
      if(app == null){
        return null;
      }
      
      final SSEntity        appEntity  =
        entityGet(
          new SSEntityGetPar(
            par,
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
      userCommons.checkKeyAndSetUser(parA);
      
      final SSAppsGetPar par = (SSAppsGetPar) parA.getFromClient(clientType, parA, SSAppsGetPar.class);
      
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
      final List<SSUri>          appURIs  = sql.getAppURIs(par);
      final SSAppGetPar          appGetPar =
        new SSAppGetPar(
          par,
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
      userCommons.checkKeyAndSetUser(parA);
      
      final SSAppsDeletePar par    = (SSAppsDeletePar) parA.getFromClient(clientType, parA, SSAppsDeletePar.class);
      
      return SSAppsDeleteRet.get(appsDelete(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSUri> appsDelete(final SSAppsDeletePar par) throws SSErr {
    
    try{
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.removeApps(par, par.apps);
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLogFct.removeApps(
        par,
        par.apps,
        par.shouldCommit);
      
      return par.apps;
      
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
}