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
package at.kc.tugraz.ss.message.impl;

import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.kc.tugraz.ss.message.api.SSMessageClientI;
import at.kc.tugraz.ss.message.api.SSMessageServerI;
import at.kc.tugraz.ss.message.datatypes.SSMessage;
import at.kc.tugraz.ss.message.datatypes.par.SSMessageGetPar;
import at.kc.tugraz.ss.message.datatypes.par.SSMessageSendPar;
import at.kc.tugraz.ss.message.datatypes.par.SSMessagesGetPar;
import at.kc.tugraz.ss.message.datatypes.ret.SSMessageSendRet;
import at.kc.tugraz.ss.message.datatypes.ret.SSMessagesGetRet;
import at.kc.tugraz.ss.message.impl.fct.activity.SSMessageActivityFct;
import at.kc.tugraz.ss.message.impl.fct.sql.SSMessageSQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitySharePar;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;

public class SSMessageImpl 
extends 
  SSServImplWithDBA 
implements 
  SSMessageClientI, 
  SSMessageServerI, 
  SSDescribeEntityI{
  
  private final SSMessageSQLFct  sqlFct;
  private final SSEntityServerI  entityServ;

  public SSMessageImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct     = new SSMessageSQLFct(dbSQL);
    this.entityServ = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      switch(entity.type){
        
        case message:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          return SSMessage.get(
            messageGet(
              new SSMessageGetPar(
                par.user,
                entity.id,
                par.withUserRestriction,
                false)),
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
  public SSMessage messageGet(final SSMessageGetPar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        if(!SSServCallerU.canUserRead(par.user, par.message)){
          return null;
        }
      }
      
      SSEntityDescriberPar descPar;
      
      if(par.invokeEntityHandlers){
        descPar         = new SSEntityDescriberPar(par.message);
        descPar.setRead = true;
      }else{
        descPar = null;
      }
      
      final SSMessage message = 
        SSMessage.get(
          sqlFct.getMessage(par.message),
          entityServ.entityGet(
            new SSEntityGetPar(
              par.user, 
              par.message, 
              par.withUserRestriction, 
              descPar)));
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(null);
      }else{
        descPar = null;
      }
      
      message.user =
        entityServ.entityGet(
          new SSEntityGetPar(
            par.user,
            message.user.id,
            par.withUserRestriction,
            descPar));
      
      message.forUser =
        entityServ.entityGet(
          new SSEntityGetPar(
            par.user,
            message.forUser.id,
            par.withUserRestriction,
            descPar));
      
      return message;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
    
  @Override
  public void messagesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSMessagesGetPar par = (SSMessagesGetPar) parA.getFromJSON(SSMessagesGetPar.class);
    
    sSCon.writeRetFullToClient(SSMessagesGetRet.get(messagesGet(par), SSDateU.dateAsLong()));
  }
  
  @Override
  public List<SSEntity> messagesGet(final SSMessagesGetPar par) throws Exception{
    
    try{
      
      if(par.user == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final List<SSEntity>   result      = new ArrayList<>();
      final List<SSEntity>   messages    = new ArrayList<>();
      
      final List<SSUri>   messageURIs = sqlFct.getMessageURIs(par.user, par.startTime);
      
      for(SSUri messagURI : messageURIs){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          messages,
          messageGet(
            new SSMessageGetPar(
              par.user,
              messagURI,
              par.withUserRestriction,
              par.invokeEntityHandlers)));
      }
      
      if(
        !par.invokeEntityHandlers ||
        par.includeRead){
        return messages;
      }
      
      messages.stream().filter((message)->(message.read == false)).forEach((message)->{
        result.add(message);
      });

      return result;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void messageSend(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    final SSMessageSendPar par        = (SSMessageSendPar) parA.getFromJSON(SSMessageSendPar.class);
    final SSUri            messageURI = messageSend(par);
      
    sSCon.writeRetFullToClient(SSMessageSendRet.get(messageURI));
    
    SSMessageActivityFct.messageSend(par, messageURI);
  }
  
  @Override
  public SSUri messageSend(final SSMessageSendPar par) throws Exception{
    
    try{
      final SSUri messageUri = SSServCaller.vocURICreate();
      
      dbSQL.startTrans(par.shouldCommit);
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          par.user,
          messageUri,
          SSEntityE.message, //type,
          null, //label
          null,//description,
          null, //creationTime,
          null, //read,
          false, //setPublic
          false, //withUserRestriction
          false)); //shouldCommit)
      
      sqlFct.sendMessage(
        messageUri,
        par.user,
        par.forUser,
        par.message);
      
      entityServ.entityShare(
        new SSEntitySharePar(
          par.user, 
          messageUri, 
          SSUri.asListWithoutNullAndEmpty(par.forUser),  //users
          null,  //circles
          false, //setPublic, 
          null, //comment, 
          par.withUserRestriction, //withUserRestriction, 
          false)); //shouldCommit));
      
      dbSQL.commit(par.shouldCommit);
      
      return messageUri;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}