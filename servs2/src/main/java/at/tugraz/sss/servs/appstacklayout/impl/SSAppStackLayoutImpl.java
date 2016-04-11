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
package at.tugraz.sss.servs.appstacklayout.impl;

import at.tugraz.sss.servs.activity.api.*;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.conf.api.SSConfA;

import at.tugraz.sss.servs.common.impl.SSUserCommons;
import at.tugraz.sss.servs.appstacklayout.api.SSAppStackLayoutClientI;
import at.tugraz.sss.servs.appstacklayout.api.SSAppStackLayoutServerI;
import at.tugraz.sss.servs.appstacklayout.datatype.SSAppStackLayout;
import at.tugraz.sss.servs.appstacklayout.datatype.SSAppStackLayoutCreatePar;
import at.tugraz.sss.servs.appstacklayout.datatype.SSAppStackLayoutDeletePar;
import at.tugraz.sss.servs.appstacklayout.datatype.SSAppStackLayoutGetPar;
import at.tugraz.sss.servs.appstacklayout.datatype.SSAppStackLayoutUpdatePar;
import at.tugraz.sss.servs.appstacklayout.datatype.SSAppStackLayoutsGetPar;
import at.tugraz.sss.servs.appstacklayout.datatype.SSAppStackLayoutCreateRet;
import at.tugraz.sss.servs.appstacklayout.datatype.SSAppStackLayoutDeleteRet;
import at.tugraz.sss.servs.appstacklayout.datatype.SSAppStackLayoutUpdateRet;
import at.tugraz.sss.servs.appstacklayout.datatype.SSAppStackLayoutsGetRet;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.servs.common.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 
import at.tugraz.sss.serv.impl.api.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.eval.api.*;

public class SSAppStackLayoutImpl
extends SSServImplA
implements
  SSAppStackLayoutClientI,
  SSAppStackLayoutServerI,
  SSDescribeEntityI{
  
  private final SSAppStackLayoutSQLFct                sql;
  private final SSUserCommons                         userCommons = new SSUserCommons();
  private final SSAppStackLayoutActAndLogFct          actAndLogFct;
  private final SSDBSQLI                              dbSQL;
  private final SSDBNoSQLI                            dbNoSQL;
  
  public SSAppStackLayoutImpl(final SSConfA conf) throws SSErr{
    
    super(conf);
    
    this.dbSQL         = (SSDBSQLI)   SSServReg.getServ(SSDBSQLI.class);
    this.dbNoSQL       = (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class);
    this.sql           = new SSAppStackLayoutSQLFct(dbSQL);
    this.actAndLogFct  =
      new SSAppStackLayoutActAndLogFct(
        (SSActivityServerI) SSServReg.getServ(SSActivityServerI.class),
        (SSEvalServerI)     SSServReg.getServ(SSEvalServerI.class));
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    switch(entity.type){
      
      case appStackLayout:{
      
        if(SSStrU.isEqual(entity, par.recursiveEntity)){
          return entity;
        }
        
        return SSAppStackLayout.get(
          appStackLayoutGet(
            new SSAppStackLayoutGetPar(
              servPar,
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
      userCommons.checkKeyAndSetUser(parA);
      
      final SSAppStackLayoutCreatePar par            = (SSAppStackLayoutCreatePar) parA.getFromClient(clientType, parA, SSAppStackLayoutCreatePar.class);
      final SSUri                     appStackLayout = appStackLayoutCreate(par);
      
      return SSAppStackLayoutCreateRet.get(appStackLayout);
      
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
        appStackLayoutUri = SSConf.vocURICreateFromId(par.uuid);
      }else{
        appStackLayoutUri = SSConf.vocURICreate();
      }
      
      final SSEntityServerI entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri appStackLayout =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sql.createAppStackLayout(
        par,
        appStackLayout,
        par.app);
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLogFct.createAppStackLayout(
        par,
        appStackLayout,
        par.shouldCommit);
      
      return appStackLayout;
      
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
  public SSServRetI appStackLayoutUpdate(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSAppStackLayoutUpdatePar par = (SSAppStackLayoutUpdatePar) parA.getFromClient(clientType, parA, SSAppStackLayoutUpdatePar.class);
      
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
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri appStackLayout =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      if(par.app != null){
        
        sql.updateAppStackLayout(
          par,
          appStackLayout,
          par.app);
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLogFct.updateAppStackLayout(
        par, 
        par.shouldCommit);
      
      return appStackLayout;
      
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
  public SSAppStackLayout appStackLayoutGet(final SSAppStackLayoutGetPar par) throws SSErr{
    
    try{
      
      final SSAppStackLayout appStackLayout = sql.getAppStackLayout(par, par.stack);
      
      if(appStackLayout == null){
        return null;
      }
      
      final SSEntityServerI  entityServ           = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSEntity         appStackLayoutEntity =
        entityServ.entityGet(
          new SSEntityGetPar(
            par,
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
    userCommons.checkKeyAndSetUser(parA);
    
    final SSAppStackLayoutsGetPar par = (SSAppStackLayoutsGetPar) parA.getFromClient(clientType, parA, SSAppStackLayoutsGetPar.class);
    
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
        par.stacks.addAll(sql.getStackURIs(par));
      }
      
      final List<SSEntity>         stacks = new ArrayList<>();
      final SSAppStackLayoutGetPar appStackLayoutGetPar =
        new SSAppStackLayoutGetPar(
          par,
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
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSAppStackLayoutDeletePar par    = (SSAppStackLayoutDeletePar) parA.getFromClient(clientType, parA, SSAppStackLayoutDeletePar.class);
      final boolean                   worked = appStackLayoutDelete(par);
      
      return SSAppStackLayoutDeleteRet.get(worked);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean appStackLayoutDelete(final SSAppStackLayoutDeletePar par) throws SSErr{
    
    try{

      final SSEntity appStackLayout = 
        sql.getEntityTest(
          par,
          SSConf.systemUserUri,
          par.user, 
          par.stack, 
          par.withUserRestriction);
      
      if(appStackLayout == null){
        return false;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.deleteStack(par, par.stack);
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLogFct.removeAppStackLayout(
        par,
        par.stack,
        par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
}