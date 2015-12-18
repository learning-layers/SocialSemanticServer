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

import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.tugraz.sss.serv.SSDateU;
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
import at.tugraz.sss.serv.SSEntityServerI;
import at.tugraz.sss.serv.SSClientE;
import at.tugraz.sss.serv.SSEntityGetPar;
import at.tugraz.sss.serv.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSServPar; 
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSQueryResultPage;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServRetI; 
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSEntitySharePar;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
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
  private final SSUserCommons userCommons;
  
  public SSMessageImpl(final SSConfA conf) throws SSErr{
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sqlFct      = new SSMessageSQLFct(dbSQL);
    this.entityServ  = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
    this.userCommons = new SSUserCommons();
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(
        par.setMessages &&
        SSStrU.equals(par.user, entity)){
        
        entity.messagesPage =
          new SSQueryResultPage(
            messagesGet(
              new SSMessagesGetPar(
                par.user,
                par.user, //forUser
                true, //includeRead,
                null, //startTime,
                par.withUserRestriction,
                false))); //invokeEntityHandlers
      }
      
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
  public SSMessage messageGet(final SSMessageGetPar par) throws SSErr{
    
    try{
      
      SSMessage message = sqlFct.getMessage(par.message);
      
      if(message == null){
        return null;
      }
      
      SSEntityDescriberPar descPar;
      
      if(par.invokeEntityHandlers){
        descPar         = new SSEntityDescriberPar(par.message);
        descPar.setRead = true;
      }else{
        descPar = null;
      }
      
      final SSEntity messageEntity =
        entityServ.entityGet(
          new SSEntityGetPar(
            par.user,
            par.message,
            par.withUserRestriction,
            descPar));
      
      message =
        SSMessage.get(
          message,
          messageEntity);
      
      if(par.invokeEntityHandlers){
        descPar = new SSEntityDescriberPar(message.id);
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
  public SSServRetI messagesGet(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSMessagesGetPar par = (SSMessagesGetPar) parA.getFromJSON(SSMessagesGetPar.class);
      
      return SSMessagesGetRet.get(messagesGet(par), SSDateU.dateAsLong());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> messagesGet(final SSMessagesGetPar par) throws SSErr{
    
    //TODO implement including message the user sent
    try{
      
      if(par.user == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        if(!SSStrU.equals(par.forUser, par.user)){
          par.forUser = par.user;
        }
      }
      
      final List<SSEntity>   result      = new ArrayList<>();
      final List<SSEntity>   messages    = new ArrayList<>();
      
      final List<SSUri>     messageURIs   = sqlFct.getMessageURIs(par.forUser, par.startTime);
      final SSMessageGetPar messageGetPar =
        new SSMessageGetPar(
          par.user,
          null, //message
          par.withUserRestriction,
          par.invokeEntityHandlers);
      
      for(SSUri messagURI : messageURIs){
        
        messageGetPar.message = messagURI;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          messages,
          messageGet(messageGetPar));
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
  public SSServRetI messageSend(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSMessageSendPar par        = (SSMessageSendPar) parA.getFromJSON(SSMessageSendPar.class);
      final SSUri            messageURI = messageSend(par);
      
      final SSMessageSendRet ret = SSMessageSendRet.get(messageURI);
      
      SSMessageActivityFct.messageSend(
        par.user,
        par.forUser,
        messageURI,
        par.message,
        par.shouldCommit);
      
      return ret;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri messageSend(final SSMessageSendPar par) throws SSErr{
    
    try{
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri message =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            SSVocConf.vocURICreate(),
            SSEntityE.message, //type,
            null, //label
            null,//description,
            null, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            false, //withUserRestriction
            false)); //shouldCommit)
      
      if(message == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sqlFct.sendMessage(
        message,
        par.user,
        par.forUser,
        par.message);
      
      entityServ.entityShare(
        new SSEntitySharePar(
          par.user,
          message,
          SSUri.asListNotNull(par.forUser),  //users
          null,  //circles
          false, //setPublic,
          null, //comment,
          par.withUserRestriction, //withUserRestriction,
          false)); //shouldCommit));
      
      dbSQL.commit(par.shouldCommit);
      
      return message;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}