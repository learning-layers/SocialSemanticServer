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
package at.kc.tugraz.sss.appstacklayout.impl;

import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityDescriberI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.serv.caller.SSServCallerU;
import at.kc.tugraz.sss.appstacklayout.api.SSAppStackLayoutClientI;
import at.kc.tugraz.sss.appstacklayout.api.SSAppStackLayoutServerI;
import at.kc.tugraz.sss.appstacklayout.datatypes.SSAppStackLayout;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutCreatePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutDeletePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutsGetPar;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutCreateRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutDeleteRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutsGetRet;
import at.kc.tugraz.sss.appstacklayout.impl.fct.sql.SSAppStackLayoutSQLFct;
import java.util.ArrayList;
import java.util.List;
import sss.serv.err.datatypes.SSErrE;

public class SSAppStackLayoutImpl extends SSServImplWithDBA implements SSAppStackLayoutClientI, SSAppStackLayoutServerI, SSEntityDescriberI{
  
  private final SSAppStackLayoutSQLFct sqlFct;
  
  public SSAppStackLayoutImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{

    super(conf, null, dbSQL);

    this.sqlFct = new SSAppStackLayoutSQLFct(dbSQL);
  }
  
  @Override
  public SSEntity getUserEntity(
    final SSUri              user,
    final SSEntity           entity) throws Exception{
    
    switch(entity.type){
      case appStackLayout:
      case appTile:
//        return SSServCaller.videoUserGet(user, entity.id);
    }
    
    return entity;
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
  public void appStackLayoutCreate(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    final SSUri userFromKey = SSServCaller.checkKey(parA);
    
    if(userFromKey != null){
      parA.user = userFromKey;
    }
    
    sSCon.writeRetFullToClient(SSAppStackLayoutCreateRet.get(appStackLayoutCreate(parA), parA.op));
  }

  @Override
  public SSUri appStackLayoutCreate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSAppStackLayoutCreatePar par               = new SSAppStackLayoutCreatePar(parA);
      final SSUri                     appStackLayoutUri = SSServCaller.vocURICreate();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntityToPrivCircleAdd(
        par.user, 
        appStackLayoutUri, 
        SSEntityE.appStackLayout, 
        par.label, 
        par.description, 
        null, 
        false);
      
      SSServCaller.entityEntityToPubCircleAdd(
        par.user, 
        appStackLayoutUri, 
        SSEntityE.appStackLayout, 
        par.label, 
        par.description, 
        null, 
        false);
      
      if(par.app != null){
        
        SSServCaller.entityEntityToPubCircleAdd(
          par.user,
          par.app,
          SSEntityE.entity,
          null,
          null,
          null,
          false);
      }
      
      sqlFct.createAppStackLayout(
        appStackLayoutUri,
        par.app);
      
      dbSQL.commit(par.shouldCommit);
      
      return appStackLayoutUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return appStackLayoutCreate(parA);
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
  public void appStackLayoutsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    final SSUri userFromKey = SSServCaller.checkKey(parA);
    
    if(userFromKey != null){
      parA.user = userFromKey;
    }
    
    sSCon.writeRetFullToClient(SSAppStackLayoutsGetRet.get(appStackLayoutsGet(parA), parA.op));
  }
  
  @Override
  public List<SSAppStackLayout> appStackLayoutsGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSAppStackLayoutsGetPar par             = new SSAppStackLayoutsGetPar(parA);
      final List<SSAppStackLayout>  appStackLayouts = new ArrayList<>();
      SSAppStackLayout              entity;
      
      for(SSAppStackLayout appStackLayout : sqlFct.getAppStackLayouts()){

        entity =
          SSAppStackLayout.get(
            appStackLayout,
            SSServCaller.entityDescGet(
              par.user,
              appStackLayout.id,
              true,
              true,
              false,
              false,
              false,
              false,
              false));
        
         appStackLayouts.add(entity);
      }
      
      return appStackLayouts;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void appStackLayoutDelete(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    final SSUri userFromKey = SSServCaller.checkKey(parA);
    
    if(userFromKey != null){
      parA.user = userFromKey;
    }
    
    sSCon.writeRetFullToClient(SSAppStackLayoutDeleteRet.get(appStackLayoutDelete(parA), parA.op));
  }
  
  @Override
  public Boolean appStackLayoutDelete(final SSServPar parA) throws Exception{
  
    try{
      final SSAppStackLayoutDeletePar par = new SSAppStackLayoutDeletePar(parA);

      SSServCallerU.canUserEditEntity(par.user, par.stack);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.deleteStack(par.stack);
    
      dbSQL.commit(par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        SSServErrReg.reset();
        
        if(dbSQL.rollBack(parA)){
          return appStackLayoutDelete(parA);
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
}