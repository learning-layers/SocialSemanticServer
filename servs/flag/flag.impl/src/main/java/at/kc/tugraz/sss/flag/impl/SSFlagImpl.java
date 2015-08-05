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

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.sss.flag.api.SSFlagClientI;
import at.kc.tugraz.sss.flag.api.SSFlagServerI;
import at.kc.tugraz.sss.flag.datatypes.ret.SSFlagsGetRet;
import at.kc.tugraz.sss.flag.datatypes.SSFlag;
import at.kc.tugraz.sss.flag.datatypes.SSFlagE;
import at.kc.tugraz.sss.flag.datatypes.par.SSFlagGetPar;
import at.kc.tugraz.sss.flag.datatypes.par.SSFlagsGetPar;
import at.kc.tugraz.sss.flag.datatypes.par.SSFlagsSetPar;
import at.kc.tugraz.sss.flag.datatypes.ret.SSFlagsSetRet;
import at.kc.tugraz.sss.flag.impl.fct.sql.SSFlagSQLFct;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSStrU;
import java.util.ArrayList;

public class SSFlagImpl 
extends 
  SSServImplWithDBA 
implements 
  SSFlagClientI, 
  SSFlagServerI, 
  SSDescribeEntityI{
  
  private final SSFlagSQLFct    sqlFct;
  private final SSEntityServerI entityServ;
  
  public SSFlagImpl(final SSConfA conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());

    this.sqlFct     = new SSFlagSQLFct(dbSQL);
    this.entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
     try{

       if(par.setFlags){
         
         entity.flags.addAll(
           flagsGet(
             new SSFlagsGetPar(
               par.user,
               SSUri.asListWithoutNullAndEmpty(entity.id), //entities,
               null, //types,
               null, //startTime,
               null, //endTime,
               par.withUserRestriction,  //withUserRestriction
               false)));  //invokeEntityHandlers
       }
       
       switch(entity.type){
         
         case flag:{
           
           if(SSStrU.equals(entity, par.recursiveEntity)){
             return entity;
           }
           
           final SSFlag flagEntity =
             SSFlag.get(
               flagGet(
                 new SSFlagGetPar(
                   par.user,
                   entity.id,
                   par.withUserRestriction,
                   false)),
               entity);
           
           return flagEntity;
         }
       }
      
      return entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void flagsSet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSFlagsSetPar par = (SSFlagsSetPar) parA.getFromJSON(SSFlagsSetPar.class);
    
    sSCon.writeRetFullToClient(SSFlagsSetRet.get(flagsSet(par)));
  }

  @Override
  public Boolean flagsSet(final SSFlagsSetPar par) throws Exception{
    
    try{
      
      for(SSUri entity : par.entities){

        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            par.user,
            entity,
            null, //type,
            null, //label
            null, //description,
            null, //entitiesToAttach,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //withUserRestriction
            false)); //shouldCommit)
      }      
      
      for(SSUri entity : par.entities){
        
        for(SSFlagE flagType : par.types){
          
          SSUri flagUri = SSServCaller.vocURICreate();
          
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              null,
              null,
              par.user,
              flagUri,
              SSEntityE.flag, //type,
              null, //label
              null, //description,
              null, //entitiesToAttach,
              null, //creationTime,
              null, //read,
              true, //setPublic
              false, //withUserRestriction
              false)); //shouldCommit)
          
          sqlFct.createFlag(
            flagUri,
            flagType,
            par.endTime,
            par.value);
          
          switch(flagType){
            
            case importance:
              
              final List<SSUri> existingFlagUris =
                sqlFct.getFlagURIs(
                  SSUri.asListWithoutNullAndEmpty(par.user),
                  SSUri.asListWithoutNullAndEmpty(entity),
                  SSFlagE.asListWithoutNullAndEmpty(flagType),
                  null, //startTime,
                  null); //endTime
              
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
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return flagsSet(par);
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
  public SSFlag flagGet(final SSFlagGetPar par) throws Exception{
    
    try{
      final SSEntityDescriberPar descPar;
      final SSFlag               flag;
      
      flag =
        SSFlag.get(
          sqlFct.getFlag(par.flag),
          entityServ.entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              par.flag,
              par.withUserRestriction,
              null)));
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, flag.entity.id)){
          return null;
        }
      }
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(par.flag);
      }else{
        descPar = null;
      }
      
      flag.entity =
        entityServ.entityGet(
          new SSEntityGetPar(
            null,
            null,
            par.user,
            flag.entity.id,
            par.withUserRestriction,
            descPar));
      
      flag.user =
        entityServ.entityGet(
          new SSEntityGetPar(
            null,
            null,
            par.user,
            flag.user.id,
            par.withUserRestriction,
            null));
      
      return flag;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void flagsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSFlagsGetPar par = (SSFlagsGetPar) parA.getFromJSON(SSFlagsGetPar.class);
    
    sSCon.writeRetFullToClient(SSFlagsGetRet.get(flagsGet(par)));
  }
  
  @Override
  public List<SSEntity> flagsGet(final SSFlagsGetPar par) throws Exception{
    
    try{
      
      final List<SSEntity>  flags = new ArrayList<>();
        
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.entities)){
          return new ArrayList<>();
        }
      }
      
      //TODO for flags which should be retrieved for user-entity combination and not only based on the entity, change here:
      final List<SSUri> flagURIs =
        sqlFct.getFlagURIs(
          SSUri.asListWithoutNullAndEmpty(), //        SSUri.asListWithoutNullAndEmpty(par.user),
          par.entities,
          par.types,
          par.startTime,
          par.endTime);
      
      for(SSUri flagURI : flagURIs){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          flags, 
          flagGet(
            new SSFlagGetPar(
              par.user, 
              flagURI, 
              par.withUserRestriction, 
              par.invokeEntityHandlers)));
      }

      return flags;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}