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
package at.kc.tugraz.sss.flag.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivEntityAddPar;
import at.kc.tugraz.ss.circle.serv.SSCircleServ;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import at.kc.tugraz.sss.flag.api.SSFlagClientI;
import at.kc.tugraz.sss.flag.api.SSFlagServerI;
import at.kc.tugraz.sss.flag.datatypes.ret.SSFlagsUserGetRet;
import at.kc.tugraz.sss.flag.datatypes.SSFlag;
import at.kc.tugraz.sss.flag.datatypes.SSFlagE;
import at.kc.tugraz.sss.flag.datatypes.par.SSFlagsGetPar;
import at.kc.tugraz.sss.flag.datatypes.par.SSFlagsUserGetPar;
import at.kc.tugraz.sss.flag.datatypes.par.SSFlagsUserSetPar;
import at.kc.tugraz.sss.flag.datatypes.ret.SSFlagsUserSetRet;
import at.kc.tugraz.sss.flag.impl.fct.sql.SSFlagSQLFct;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;

public class SSFlagImpl extends SSServImplWithDBA implements SSFlagClientI, SSFlagServerI, SSEntityDescriberI{
  
  private final SSFlagSQLFct sqlFct;
  
  public SSFlagImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    this.sqlFct = new SSFlagSQLFct(dbSQL);
  }
  
  @Override
  public SSEntity getUserEntity(final SSEntityDescriberPar par) throws Exception{
    
     try{

       if(par.setFlags){
        
        par.entity.flags.addAll(
          SSServCaller.flagsGet(
            par.user,
            SSUri.asListWithoutNullAndEmpty(par.entity.id),
            SSStrU.toStrWithoutEmptyAndNull(),
            null,
            null));
      }
      
      return par.entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void flagsSet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSFlagsUserSetRet.get(flagsUserSet(parA), parA.op));
  }

  @Override
  public Boolean flagsUserSet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSFlagsUserSetPar par = SSFlagsUserSetPar.get(parA);
      
      SSServCallerU.canUserEditEntities(par.user, par.entities);
      
      for(SSUri entity : par.entities){

        ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlePrivEntityAdd(
          new SSCirclePrivEntityAddPar(
            null,
            null,
            par.user,
            entity,
            SSEntityE.entity,
            null,
            null,
            null,
            false));
      }
      
      for(SSUri entity : par.entities){
       
        for(SSFlagE flag : par.types){
          
          SSUri flagUri = SSServCaller.vocURICreate();
          
          ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circlePrivEntityAdd(
            new SSCirclePrivEntityAddPar(
              null,
              null,
              par.user,
              flagUri,
              SSEntityE.flag,
              null,
              null,
              null,
              false));
            
          sqlFct.createFlag(
            flagUri,
            flag,
            par.endTime,
            par.value);
          
          switch(flag){
            
            case importance:
              
              final List<SSUri> existingFlagUris = sqlFct.getFlagURIs(par.user, flag, entity);
              
              for(SSUri existingFlagUri : existingFlagUris){
               
                sqlFct.deleteFlagAss(
                  null,
                  existingFlagUri,
                  null,
                  null);
              }
              
              break;
          }
          
          sqlFct.addFlagAssIfNotExists(
            par.user,
            flagUri,
            entity);
        }
      }
      
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return flagsUserSet(parA);
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
  public void flagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSFlagsUserGetRet.get(flagsUserGet(parA), parA.op));
  }
  
  @Override
  public List<SSFlag> flagsGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSFlagsGetPar par = new SSFlagsGetPar(parA);
      
      //TODO for flags which should be retrieved for user-entity combination and not only based on the entity, change here:
      return sqlFct.getFlags(
        SSUri.asListWithoutNullAndEmpty(), //        SSUri.asListWithoutNullAndEmpty(par.user),
        par.entities,
        par.types, 
        par.startTime,
        par.endTime);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSFlag> flagsUserGet(final SSServPar parA) throws Exception{
    
    try{
      
      final SSFlagsUserGetPar par = SSFlagsUserGetPar.get(parA);
      
      SSServCallerU.canUserReadEntities(par.user, par.entities);
      
      //TODO for flags which should be retrieved for user-entity combination and not only based on the entity, change here:
      return sqlFct.getFlags(
        SSUri.asListWithoutNullAndEmpty(), //        SSUri.asListWithoutNullAndEmpty(par.user),
        par.entities,
        par.types, 
        par.startTime,
        par.endTime);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}