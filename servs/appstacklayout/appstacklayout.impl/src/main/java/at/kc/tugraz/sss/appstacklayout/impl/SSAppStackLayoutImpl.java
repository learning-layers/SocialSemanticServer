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
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServImplWithDBA;

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
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServRetI; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSStrU;

public class SSAppStackLayoutImpl
extends SSServImplWithDBA
implements
  SSAppStackLayoutClientI,
  SSAppStackLayoutServerI,
  SSDescribeEntityI{
  
  private final SSAppStackLayoutSQLFct sql;
  
  public SSAppStackLayoutImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.getServImpl(), (SSDBNoSQLI) SSDBNoSQL.inst.getServImpl());
    
    this.sql = new SSAppStackLayoutSQLFct(dbSQL, SSVocConf.systemUserUri);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    switch(entity.type){
      
      case appStackLayout:{
      
        if(SSStrU.equals(entity, par.recursiveEntity)){
          return entity;
        }
        
        return SSAppStackLayout.get(
          appStackLayoutGet(
            new SSAppStackLayoutGetPar(
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
  public SSServRetI appStackLayoutCreate(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
    SSServCallerU.checkKey(parA);
    
    final SSAppStackLayoutCreatePar par = (SSAppStackLayoutCreatePar) parA.getFromJSON(SSAppStackLayoutCreatePar.class);
     
    return SSAppStackLayoutCreateRet.get(appStackLayoutCreate(par));
    
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri appStackLayoutCreate(final SSAppStackLayoutCreatePar par) throws SSErr{
    
    try{
      
      final SSUri appStackLayoutUri;
      
      if(par.uuid != null){
        appStackLayoutUri = SSVocConf.vocURICreateFromId(par.uuid);
      }else{
        appStackLayoutUri = SSVocConf.vocURICreate();
      }
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri appStackLayout =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            appStackLayoutUri,
            SSEntityE.appStackLayout, //type,
            par.label,
            par.description,
            null, //creationTime,
            null, //read,
            true, //setPublic
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(appStackLayout == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sql.createAppStackLayout(
        appStackLayout,
        par.app);
      
      dbSQL.commit(par.shouldCommit);
      
      return appStackLayout;
      
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
  public SSServRetI appStackLayoutUpdate(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
    SSServCallerU.checkKey(parA);
    
    final SSAppStackLayoutUpdatePar par = (SSAppStackLayoutUpdatePar) parA.getFromJSON(SSAppStackLayoutUpdatePar.class);
    
    return SSAppStackLayoutUpdateRet.get(appStackLayoutUpdate(par));
    
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri appStackLayoutUpdate(final SSAppStackLayoutUpdatePar par) throws SSErr{
    
    try{
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri appStackLayout =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.stack,
            null, //type
            par.label,
            par.description,
            null, //creationTime
            null, //read
            false, //setPublic
            false, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false));
      
      if(appStackLayout == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      if(par.app != null){
        
        sql.updateAppStackLayout(
          appStackLayout,
          par.app);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return appStackLayout;
      
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
  public SSAppStackLayout appStackLayoutGet(final SSAppStackLayoutGetPar par) throws SSErr{
    
    try{
      
      final SSAppStackLayout appStackLayout = sql.getAppStackLayout(par.stack);
      
      if(appStackLayout == null){
        return null;
      }
      
      final SSEntityServerI  entityServ           = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEntity         appStackLayoutEntity =
        entityServ.entityGet(
          new SSEntityGetPar(
            par.user,
            par.stack,
            par.withUserRestriction, //withUserRestriction,
            null)); //descPar)));
      
      if(appStackLayoutEntity == null){
        return null;
      }
      
      return SSAppStackLayout.get(
        appStackLayout,
        appStackLayoutEntity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI appStackLayoutsGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
    SSServCallerU.checkKey(parA);
    
    final SSAppStackLayoutsGetPar par = (SSAppStackLayoutsGetPar) parA.getFromJSON(SSAppStackLayoutsGetPar.class);
    
    return SSAppStackLayoutsGetRet.get(appStackLayoutsGet(par));

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> appStackLayoutsGet(final SSAppStackLayoutsGetPar par) throws SSErr{
    
    try{
      
      if(par.stacks.isEmpty()){
        par.stacks.addAll(sql.getStackURIs());
      }
      
      final List<SSEntity>         stacks = new ArrayList<>();
      final SSAppStackLayoutGetPar appStackLayoutGetPar =
        new SSAppStackLayoutGetPar(
          par.user,
          null, //stack
          par.withUserRestriction,
          par.invokeEntityHandlers);
      
      for(SSUri stackURI : par.stacks){
        
        appStackLayoutGetPar.stack = stackURI;

        SSEntity.addEntitiesDistinctWithoutNull(
          stacks,
          appStackLayoutGet(appStackLayoutGetPar));
      }
      
      return stacks;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI appStackLayoutDelete(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      
    SSServCallerU.checkKey(parA);
    
    final SSAppStackLayoutDeletePar par = (SSAppStackLayoutDeletePar) parA.getFromJSON(SSAppStackLayoutDeletePar.class);
    
    return SSAppStackLayoutDeleteRet.get(appStackLayoutDelete(par));
    
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean appStackLayoutDelete(final SSAppStackLayoutDeletePar par) throws SSErr{
    
    try{

      final SSEntity appStackLayout = 
        sql.getEntityTest(
          par.user, 
          par.stack, 
          par.withUserRestriction);
      
      if(appStackLayout == null){
        return false;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sql.deleteStack(par.stack);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return appStackLayoutDelete(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return false;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
}