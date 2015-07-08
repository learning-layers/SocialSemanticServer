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

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.sss.app.api.SSAppServerI;
import at.kc.tugraz.sss.app.api.SSAppClientI;
import at.kc.tugraz.sss.app.datatypes.SSApp;
import at.kc.tugraz.sss.app.datatypes.par.SSAppAddPar;
import at.kc.tugraz.sss.app.datatypes.par.SSAppsGetPar;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppAddRet;
import at.kc.tugraz.sss.app.datatypes.ret.SSAppsGetRet;
import at.kc.tugraz.sss.app.impl.fct.sql.SSAppSQLFct;
import at.tugraz.sss.serv.SSCircleContentChangedPar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSImage;
import at.tugraz.sss.serv.SSImageE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.servs.image.api.SSImageServerI;
import at.tugraz.sss.servs.image.datatype.par.SSImageAddPar;

public class SSAppImpl 
extends SSServImplWithDBA 
implements 
  SSAppClientI, 
  SSAppServerI, 
  SSEntityHandlerImplI{
  
  private final SSAppSQLFct sqlFct;
  
  public SSAppImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    this.sqlFct = new SSAppSQLFct(dbSQL);
  }
  
  @Override
  public SSEntity getUserEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    return entity;
  }
  
  @Override
  public void circleContentChanged(final SSCircleContentChangedPar par) throws Exception{
  }
  
  @Override
  public void copyEntity(
    final SSUri        user,
    final List<SSUri>  users,
    final SSUri        entity,
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception{
    
  }
  
  @Override
  public List<SSUri> getSubEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    return new ArrayList<>();
  }
  
  @Override
  public List<SSUri> getParentEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    return new ArrayList<>();
  }
  
  @Override
  public void appAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSAppAddRet.get(appAdd(parA), parA.op));
  }

  @Override
  public SSUri appAdd(final SSServPar parA) throws Exception{
    
    try{
      
      final SSAppAddPar par    = new SSAppAddPar(parA);
      final SSUri       appUri = SSServCaller.vocURICreate();
      
      dbSQL.startTrans(par.shouldCommit);

      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          appUri, //entity,
          null, //uriAlternative,
          SSEntityE.app, //type,
          par.label, //label,
          null, //description,
          null, //comments,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          true, //setPublic
          par.withUserRestriction, //withUserRestriction,
          false)); //shouldCommit
      
      for(SSUri screenShot : par.screenShots){
      
        ((SSImageServerI) SSServReg.getServ(SSImageServerI.class)).imageAdd(
          new SSImageAddPar(
            null, 
            null, 
            par.user, 
            screenShot, 
            SSImageE.screenShot, //imageType
            appUri, //entity, 
            par.withUserRestriction, 
            false)); //shouldCommit
      }
      
      //TODO handle below
//      for(SSUri download : par.downloads){
//      
//        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
//          new SSEntityUpdatePar(
//            null,
//            null,
//            par.user,
//            download, //entity,
//            null, //uriAlternative,
//            null, //type,
//            null, //label,
//            null, //description,
//            null, //comments,
//            null, //entitiesToAttach,
//            null, //creationTime,
//            null, //read,
//            true, //setPublic
//            true, //withUserRestriction,
//            false)); //shouldCommit
//      }    
//          par.videos, //videos,
      
      sqlFct.createApp(
        appUri,
        par.descriptionShort, 
        par.descriptionFunctional, 
        par.descriptionTechnical, 
        par.descriptionInstall, 
        par.downloadIOS,
        par.downloadAndroid,
        par.fork);
      
      dbSQL.commit(par.shouldCommit);
      
      return appUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return appAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void appsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSAppsGetRet.get(appsGet(parA), parA.op));
  }
  
  @Override
  public List<SSApp> appsGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSAppsGetPar par  = new SSAppsGetPar(parA);
      final List<SSApp>  apps = new ArrayList<>();
      SSApp              entity;
      
      for(SSApp app : sqlFct.getApps()){
        
        entity =
          SSApp.get(
            app,
            ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
              new SSEntityGetPar(
                null,
                null,
                par.user,
                app.id,
                null, //forUser,
                null, //label
                null, //type
                false, //withUserRestriction
                false, //invokeEntityHandlers,
                null, //descPar,
                true))); //logErr
        
        entity.downloads.addAll(
          SSServCaller.entityDownloadURIsGet(
            par.user, 
            app.id));
        
        entity.attachedEntities.addAll(
          SSServCaller.entityScreenShotsGet(
            par.user, 
            app.id));
        
        try{
          
          entity.attachedEntities.addAll(
            SSServCaller.videosUserGet(
              par.user,
              null,
              app.id));
          
        }catch(SSErr error){
          
          switch(error.code){
            case notServerServiceForOpAvailable: SSLogU.warn(error.getMessage()); break;
            default: SSServErrReg.regErrThrow(error);
          }
        }
         
         apps.add(entity);
      }
      
      return apps;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}