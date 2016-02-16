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

import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
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
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.entity.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.datatype.SSQueryResultPage;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.par.SSEntitySharePar;
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
    final SSServPar servPar,
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
                servPar, 
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
                servPar, 
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
      
      SSMessage message = sqlFct.getMessage(par, par.message);
      
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
            par,
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
            par,
            par.user,
            message.user.id,
            par.withUserRestriction,
            descPar));
      
      message.forUser =
        entityServ.entityGet(
          new SSEntityGetPar(
            par,
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
      
      final SSMessagesGetPar par = (SSMessagesGetPar) parA.getFromClient(clientType, parA, SSMessagesGetPar.class);
      
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
      
      final List<SSUri>     messageURIs   = sqlFct.getMessageURIs(par, par.forUser, par.startTime);
      final SSMessageGetPar messageGetPar =
        new SSMessageGetPar(
          par,
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
      
      final SSMessageSendPar par        = (SSMessageSendPar) parA.getFromClient(clientType, parA, SSMessageSendPar.class);
      final SSUri            messageURI = messageSend(par);
      
      final SSMessageSendRet ret = SSMessageSendRet.get(messageURI);
      
      SSMessageActivityFct.messageSend(
        par,
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
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri message =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
            par.user,
            SSConf.vocURICreate(),
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sqlFct.sendMessage(
        par, 
        message,
        par.user,
        par.forUser,
        par.message);
      
      entityServ.entityShare(
        new SSEntitySharePar(
          par,
          par.user,
          message,
          SSUri.asListNotNull(par.forUser),  //users
          null,  //circles
          false, //setPublic,
          null, //comment,
          par.withUserRestriction, //withUserRestriction,
          false)); //shouldCommit));
      
      dbSQL.commit(par, par.shouldCommit);
      
      return message;
      
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
}