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
package at.tugraz.sss.servs.flag.impl;

import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.servs.flag.api.SSFlagClientI;
import at.tugraz.sss.servs.flag.api.SSFlagServerI;
import at.tugraz.sss.servs.flag.datatype.SSFlagsGetRet;
import at.tugraz.sss.servs.flag.datatype.SSFlag;
import at.tugraz.sss.servs.flag.datatype.SSFlagE;
import at.tugraz.sss.servs.flag.datatype.SSFlagGetPar;
import at.tugraz.sss.servs.flag.datatype.SSFlagsGetPar;
import at.tugraz.sss.servs.flag.datatype.SSFlagsSetPar;
import at.tugraz.sss.servs.flag.datatype.SSFlagsSetRet;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.servs.common.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.SSEntityContext;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import java.util.List;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.errreg.SSServErrReg;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 
import java.util.ArrayList;
import java.util.Map;
import at.tugraz.sss.servs.common.api.SSGetUsersResourcesI;
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.entity.impl.*;

public class SSFlagImpl 
extends 
  SSEntityImpl 
implements 
  SSFlagClientI, 
  SSFlagServerI, 
  SSDescribeEntityI, 
  SSGetUsersResourcesI{
  
  private final SSFlagSQLFct sql  = new SSFlagSQLFct  (dbSQL);
  
  public SSFlagImpl(){
    super(SSCoreConf.instGet().getFlag());
  }
  
  @Override
  public void getUsersResources(
    final SSServPar                          servPar,
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      final SSFlagsGetPar flagsGetPar =
        new SSFlagsGetPar(
          servPar,
          null, //user
          null, //entities,
          null, //types,
          null, //startTime,
          null, //endTime,
          true, //withUserRestriction, //because flagsGet doesnt consider the user yet
          false);//invokeEntityHandlers
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        flagsGetPar.user = userID;
        
        for(SSEntity flag : flagsGet(flagsGetPar)){
          
          usersEntities.get(user).add(
            new SSEntityContext(
              ((SSFlag) flag).entity.id,
              SSEntityE.flag,
              null,
              null));
        }
      }
      
    }catch(Exception error){
      SSLogU.err(error);
    }
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar servPar,
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws SSErr{
    
     try{

       if(par.setFlags){
         
         entity.flags.addAll(
           flagsGet(
             new SSFlagsGetPar(
               servPar,
               par.user,
               SSUri.asListNotNull(entity.id), //entities,
               null, //types,
               null, //startTime,
               null, //endTime,
               par.withUserRestriction,  //withUserRestriction
               false)));  //invokeEntityHandlers
       }
       
       switch(entity.type){
         
         case flag:{
           
           if(SSStrU.isEqual(entity, par.recursiveEntity)){
             return entity;
           }
           
           final SSFlag flag =
             flagGet(
               new SSFlagGetPar(
                 servPar,
                 par.user,
                 entity.id,
                 par.withUserRestriction,
                 false));
           
           if(flag == null){
             return entity;
           }
           
           return SSFlag.get(
               flag,
               entity);
         }
       }
      
      return entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI flagsSet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSFlagsSetPar par = (SSFlagsSetPar) parA.getFromClient(clientType, parA, SSFlagsSetPar.class);
      
      return SSFlagsSetRet.get(flagsSet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public boolean flagsSet(final SSFlagsSetPar par) throws SSErr{
    
    try{
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      SSUri entity;
      SSUri flag;
      
      for(SSUri entityToAddFlag : par.entities){

        entity =
          entityUpdate(
            new SSEntityUpdatePar(
              par,
              par.user,
              entityToAddFlag,
              null, //type,
              null, //label
              null, //description,
              null, //creationTime,
              null, //read,
              false, //setPublic
              true, //createIfNotExists
              par.withUserRestriction, //withUserRestriction
              false)); //shouldCommit)
        
        if(entity == null){
          dbSQL.rollBack(par, par.shouldCommit);
          return false;
        }
        
        for(SSFlagE flagType : par.types){
          
          flag =
            entityUpdate(
              new SSEntityUpdatePar(
                par,
                par.user,
                SSConf.vocURICreate(),
                SSEntityE.flag, //type,
                null, //label
                null, //description,
                null, //creationTime,
                null, //read,
                true, //setPublic
                true, //createIfNotExist
                false, //withUserRestriction
                false)); //shouldCommit)
          
          if(flag == null){
            dbSQL.rollBack(par, par.shouldCommit);
            return false;
          }
          
          sql.createFlag(
            par,
            flag,
            flagType,
            par.endTime,
            par.value);
          
          switch(flagType){
            
            case importance:
              
              final List<SSUri> existingFlagUris =
                sql.getFlagURIs(
                  par,
                  SSUri.asListNotNull(par.user),
                  SSUri.asListNotNull(entity),
                  SSFlagE.asListWithoutNullAndEmpty(flagType),
                  null, //startTime,
                  null); //endTime
              
              for(SSUri existingFlagUri : existingFlagUris){
                
                sql.deleteFlagAss(
                  par,
                  null,
                  existingFlagUri,
                  null,
                  null);
              }
              
              break;
          }
          
          sql.addFlagAssIfNotExists(
            par,
            par.user,
            flag,
            entity);
        }
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
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
  
  @Override
  public SSFlag flagGet(final SSFlagGetPar par) throws SSErr{
    
    try{
      SSFlag                     flag    = sql.getFlag(par, par.flag);
      
      if(flag == null){
        return null;
      }
      
      final SSEntityGetPar entityGetPar =
        new SSEntityGetPar(
          par,
          par.user,
          par.flag,
          par.withUserRestriction,
          null); //descPar
        
      final SSEntity flagEntity = entityGet(entityGetPar);
      
      if(flagEntity == null){
        return null;
      }
      
      flag =
        SSFlag.get(
          flag,
          flagEntity);
      
      if(par.invokeEntityHandlers){
        entityGetPar.descPar = new SSEntityDescriberPar(par.flag);
      }else{
        entityGetPar.descPar = null;
      }
      
      entityGetPar.entity  = flag.entity.id;
      flag.entity          = entityGet(entityGetPar);
      
      if(flag.entity == null){
        return null;
      }
      
      entityGetPar.entity  = flag.user.id;
      entityGetPar.descPar = null;
      flag.user            = entityGet(entityGetPar);
      
      return flag;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI flagsGet(final SSClientE clientType, final SSServPar parA) throws SSErr{
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSFlagsGetPar par = (SSFlagsGetPar) parA.getFromClient(clientType, parA, SSFlagsGetPar.class);
      
      return SSFlagsGetRet.get(flagsGet(par));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> flagsGet(final SSFlagsGetPar par) throws SSErr{
    
    try{
      
      if(par.withUserRestriction){
        
        SSEntity enityEntity;
        
        for(SSUri entity : par.entities){

          enityEntity = 
            sql.getEntityTest(
              par,
              SSConf.systemUserUri,
              par.user, 
              entity, 
              par.withUserRestriction);
          
          if(enityEntity == null){
            return new ArrayList<>();
          }
        }
      }

      final List<SSEntity> flags = new ArrayList<>();
      
      //TODO for flags which should be retrieved for user-entity combination and not only based on the entity, change here:
      final List<SSUri> flagURIs =
        sql.getFlagURIs(
          par,
          SSUri.asListNotNull(), //        SSUri.asListWithoutNullAndEmpty(par.user),
          par.entities,
          par.types,
          par.startTime,
          par.endTime);
      
      final SSFlagGetPar flagGetPar =
        new SSFlagGetPar(
          par,
          par.user,
          null, //flag
          par.withUserRestriction,
          par.invokeEntityHandlers);
      
      for(SSUri flagURI : flagURIs){
        
        flagGetPar.flag = flagURI;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          flags,
          flagGet(flagGetPar));
      }

      return flags;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}