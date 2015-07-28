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

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.sss.appstacklayout.api.SSAppStackLayoutClientI;
import at.kc.tugraz.sss.appstacklayout.api.SSAppStackLayoutServerI;
import at.kc.tugraz.sss.appstacklayout.datatypes.SSAppStackLayout;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutCreatePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutDeletePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutGetPar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutUpdatePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutsGetPar;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutCreateRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutDeleteRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutUpdateRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutsGetRet;
import at.kc.tugraz.sss.appstacklayout.impl.fct.sql.SSAppStackLayoutSQLFct;
import at.tugraz.sss.serv.SSCircleContentChangedPar;
import at.tugraz.sss.serv.SSCircleContentRemovedPar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityCopiedPar;
import at.tugraz.sss.serv.SSEntityCopyPar;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;

public class SSAppStackLayoutImpl
extends SSServImplWithDBA
implements
  SSAppStackLayoutClientI,
  SSAppStackLayoutServerI,
  SSEntityHandlerImplI{
  
  private final SSAppStackLayoutSQLFct sqlFct;
  
  public SSAppStackLayoutImpl(final SSConfA conf) throws Exception{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct = new SSAppStackLayoutSQLFct(dbSQL);
  }
  
  @Override
  public SSEntity getUserEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws Exception{
    
    switch(entity.type){
      
      case appStackLayout:{
      
        return SSAppStackLayout.get(
          appStackLayoutGet(
            new SSAppStackLayoutGetPar(
              null,
              null,
              par.user,
              entity.id,
              par.withUserRestriction, 
              false)), //invokeEntityHandlers
          entity);
      }
      
      default: return entity;
    }
  }
  
  @Override
  public void copyEntity(
    final SSEntity                  entity,
    final SSEntityCopyPar           par) throws Exception{
    
  }
  
  @Override
  public void entityCopied(final SSEntityCopiedPar par) throws Exception{
    
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
  public void appStackLayoutCreate(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSAppStackLayoutCreatePar par = (SSAppStackLayoutCreatePar) parA.getFromJSON(SSAppStackLayoutCreatePar.class);
     
    sSCon.writeRetFullToClient(SSAppStackLayoutCreateRet.get(appStackLayoutCreate(par)));
  }
  
  @Override
  public SSUri appStackLayoutCreate(final SSAppStackLayoutCreatePar par) throws Exception{
    
    try{
      
      final SSUri appStackLayoutUri;
      
      if(par.uuid != null){
        appStackLayoutUri = SSServCaller.vocURICreateFromId(par.uuid);
      }else{
        appStackLayoutUri = SSServCaller.vocURICreate();
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(par.app != null){
        
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            par.user,
            par.app,
            null, //type,
            null, //label,
            null, //par.description,
            null, //entitiesToAttach,
            null, //creationTime,
            null, //read,
            false, //setPublic
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      }
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          appStackLayoutUri,
          SSEntityE.appStackLayout, //type,
          par.label,
          par.description,
          null, //entitiesToAttach,
          null, //creationTime,
          null, //read,
          true, //setPublic
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      sqlFct.createAppStackLayout(
        appStackLayoutUri,
        par.app);
      
      dbSQL.commit(par.shouldCommit);
      
      return appStackLayoutUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return appStackLayoutCreate(par);
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
  public void appStackLayoutUpdate(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSAppStackLayoutUpdatePar par = (SSAppStackLayoutUpdatePar) parA.getFromJSON(SSAppStackLayoutUpdatePar.class);
    
    sSCon.writeRetFullToClient(SSAppStackLayoutUpdateRet.get(appStackLayoutUpdate(par)));
  }
  
  @Override
  public SSUri appStackLayoutUpdate(final SSAppStackLayoutUpdatePar par) throws Exception{
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          par.user,
          par.stack,
          null, //type
          par.label,
          par.description,
          null, //entitiesToAttach
          null, //creationTime
          null, //read
          false, //setPublic
          par.withUserRestriction, //withUserRestriction
          false));
      
      if(par.app != null){
        
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            par.user,
            par.app,
            null, //type,
            null, //label,
            null, //par.description,
            null, //entitiesToAttach,
            null, //creationTime,
            null, //read,
            true, //setPublic
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
        
        sqlFct.updateAppStackLayout(
          par.stack,
          par.app);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.stack;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return appStackLayoutUpdate(par);
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
  public SSAppStackLayout appStackLayoutGet(final SSAppStackLayoutGetPar par) throws Exception{
    
    try{
      
      return SSAppStackLayout.get(
        sqlFct.getAppStackLayout(par.stack),
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
          new SSEntityGetPar(
            null,
            null,
            par.user,
            par.stack,
            par.withUserRestriction, //withUserRestriction,
            null))); //descPar)));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void appStackLayoutsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSAppStackLayoutsGetPar par = (SSAppStackLayoutsGetPar) parA.getFromJSON(SSAppStackLayoutsGetPar.class);
    
    sSCon.writeRetFullToClient(SSAppStackLayoutsGetRet.get(appStackLayoutsGet(par)));
  }
  
  @Override
  public List<SSEntity> appStackLayoutsGet(final SSAppStackLayoutsGetPar par) throws Exception{
    
    try{
      final List<SSUri>          stackURIs       = sqlFct.getStackURIs();
      final List<SSEntity>       stacks          = new ArrayList<>();
      
      for(SSUri stackURI : stackURIs){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          stacks,
          appStackLayoutGet(
            new SSAppStackLayoutGetPar(
              null,
              null,
              par.user,
              stackURI,
              par.withUserRestriction,
              par.invokeEntityHandlers)));
      }
      
      return stacks;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void appStackLayoutDelete(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSAppStackLayoutDeletePar par = (SSAppStackLayoutDeletePar) parA.getFromJSON(SSAppStackLayoutDeletePar.class);
    
    sSCon.writeRetFullToClient(SSAppStackLayoutDeleteRet.get(appStackLayoutDelete(par)));
  }
  
  @Override
  public Boolean appStackLayoutDelete(final SSAppStackLayoutDeletePar par) throws Exception{
    
    try{
      SSServCallerU.canUserAllEntity(par.user, par.stack);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.deleteStack(par.stack);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return appStackLayoutDelete(par);
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