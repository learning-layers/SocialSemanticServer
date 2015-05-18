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

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubEntityAddPar;
import at.kc.tugraz.ss.circle.serv.SSCircleServ;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import at.kc.tugraz.sss.appstacklayout.api.SSAppStackLayoutClientI;
import at.kc.tugraz.sss.appstacklayout.api.SSAppStackLayoutServerI;
import at.kc.tugraz.sss.appstacklayout.datatypes.SSAppStackLayout;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutCreatePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutDeletePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutUpdatePar;
import at.kc.tugraz.sss.appstacklayout.datatypes.par.SSAppStackLayoutsGetPar;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutCreateRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutDeleteRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutUpdateRet;
import at.kc.tugraz.sss.appstacklayout.datatypes.ret.SSAppStackLayoutsGetRet;
import at.kc.tugraz.sss.appstacklayout.impl.fct.sql.SSAppStackLayoutSQLFct;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;

public class SSAppStackLayoutImpl extends SSServImplWithDBA implements SSAppStackLayoutClientI, SSAppStackLayoutServerI, SSEntityDescriberI{
  
  private final SSAppStackLayoutSQLFct sqlFct;
  
  public SSAppStackLayoutImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    this.sqlFct = new SSAppStackLayoutSQLFct(dbSQL);
  }
  
  @Override
  public SSEntity getUserEntity(final SSEntityDescriberPar par) throws Exception{
    
    try{
      return par.entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void appStackLayoutCreate(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSAppStackLayoutCreateRet.get(appStackLayoutCreate(parA), parA.op), parA.op);
  }

  @Override
  public SSUri appStackLayoutCreate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSAppStackLayoutCreatePar par               = new SSAppStackLayoutCreatePar(parA);
      final SSUri                     appStackLayoutUri;
      
      if(par.uuid != null){
        appStackLayoutUri = SSServCaller.vocURICreateFromId(par.uuid);
      }else{
        appStackLayoutUri = SSServCaller.vocURICreate();
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntityToPrivCircleAdd(
        par.user, 
        appStackLayoutUri, 
        SSEntityE.appStackLayout, 
        par.label, 
        par.description, 
        null, 
        false);
      
      
      ((SSCircleServerI)SSCircleServ.inst.serv()).circlePubEntityAdd(
        new SSCirclePubEntityAddPar(
          null, 
          null,
          par.user, 
          appStackLayoutUri, 
          false, 
          SSEntityE.appStackLayout, 
          par.label, 
          par.description, 
          null));
      
      if(par.app != null){
        
        ((SSCircleServerI)SSCircleServ.inst.serv()).circlePubEntityAdd(
          new SSCirclePubEntityAddPar(
            null,
            null,
            par.user,
            par.app,
            false,
            SSEntityE.entity,
            null,
            null,
            null));
      }
      
      sqlFct.createAppStackLayout(
        appStackLayoutUri,
        par.app);
      
      dbSQL.commit(par.shouldCommit);
      
      return appStackLayoutUri;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return appStackLayoutCreate(parA);
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
  public void appStackLayoutUpdate(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSAppStackLayoutUpdateRet.get(appStackLayoutUpdate(parA), parA.op), parA.op);
  }

  @Override
  public SSUri appStackLayoutUpdate(final SSServPar parA) throws Exception{
    
    try{
      
      final SSAppStackLayoutUpdatePar par               = new SSAppStackLayoutUpdatePar(parA);
      
      SSServCallerU.canUserReadEntity(par.user, par.stack);
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(par.app != null){
        
        ((SSCircleServerI)SSCircleServ.inst.serv()).circlePubEntityAdd(
          new SSCirclePubEntityAddPar(
            null,
            null,
            par.user,
            par.app,
            false,
            SSEntityE.entity,
            null,
            null,
            null));
        
        sqlFct.updateAppStackLayout(
          par.stack,
          par.app);
      }
      
      if(
        par.label != null || 
        par.description != null){
        
        SSServCaller.entityUpdate(
          par.user, 
          par.stack, 
          par.label, 
          par.description, 
          SSTextComment.asListWithoutNullAndEmpty(), 
          SSUri.asListWithoutNullAndEmpty(), 
          SSUri.asListWithoutNullAndEmpty(), 
          SSUri.asListWithoutNullAndEmpty(), 
          SSUri.asListWithoutNullAndEmpty(), 
          false);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.stack;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return appStackLayoutUpdate(parA);
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
  public void appStackLayoutsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSAppStackLayoutsGetRet.get(appStackLayoutsGet(parA), parA.op), parA.op);
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
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSAppStackLayoutDeleteRet.get(appStackLayoutDelete(parA), parA.op), parA.op);
  }
  
  @Override
  public Boolean appStackLayoutDelete(final SSServPar parA) throws Exception{
  
    try{
      final SSAppStackLayoutDeletePar par = new SSAppStackLayoutDeletePar(parA);

      SSServCallerU.canUserAllEntity(par.user, par.stack);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.deleteStack(par.stack);
    
      dbSQL.commit(par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return appStackLayoutDelete(parA);
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
}