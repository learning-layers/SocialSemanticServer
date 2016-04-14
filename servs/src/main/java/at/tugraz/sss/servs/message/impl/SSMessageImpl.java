 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.message.impl;

import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.util.SSDateU;
import at.tugraz.sss.servs.message.api.SSMessageClientI;
import at.tugraz.sss.servs.message.api.SSMessageServerI;
import at.tugraz.sss.servs.message.datatype.SSMessage;
import at.tugraz.sss.servs.message.datatype.SSMessageGetPar;
import at.tugraz.sss.servs.message.datatype.SSMessageSendPar;
import at.tugraz.sss.servs.message.datatype.SSMessagesGetPar;
import at.tugraz.sss.servs.message.datatype.SSMessageSendRet;
import at.tugraz.sss.servs.message.datatype.SSMessagesGetRet;

import at.tugraz.sss.servs.entity.datatype.SSClientE;
import at.tugraz.sss.servs.entity.datatype.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityUpdatePar;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import at.tugraz.sss.servs.common.api.SSDescribeEntityI;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.common.datatype.SSEntityDescriberPar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.entity.datatype.SSQueryResultPage;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSServRetI; 
import at.tugraz.sss.servs.entity.datatype.SSEntitySharePar;
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.entity.impl.*;
import java.util.ArrayList;
import java.util.List;

public class SSMessageImpl
extends SSEntityImpl
implements
  SSMessageClientI,
  SSMessageServerI,
  SSDescribeEntityI{
  
  private final SSMessageActAndLog                    actAndLog   = new SSMessageActAndLog();
  private final SSMessageSQL                          sql         = new SSMessageSQL(dbSQL);
  
  public SSMessageImpl(){
    super(SSCoreConf.instGet().getMessage());
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(
        par.setMessages &&
        SSStrU.isEqual(par.user, entity)){
        
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
          
          if(SSStrU.isEqual(entity, par.recursiveEntity)){
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
      
      SSMessage message     = sql.getMessage(par, par.message);
      
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
        entityGet(
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
        entityGet(
          new SSEntityGetPar(
            par,
            par.user,
            message.user.id,
            par.withUserRestriction,
            descPar));
      
      message.forUser =
        entityGet(
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
        
        if(!SSStrU.isEqual(par.forUser, par.user)){
          par.forUser = par.user;
        }
      }
      
      final List<SSEntity>   result      = new ArrayList<>();
      final List<SSEntity>   messages    = new ArrayList<>();
      
      final List<SSUri>     messageURIs   = sql.getMessageURIs(par, par.forUser, par.startTime);
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
      
      actAndLog.messageSend(
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
        entityUpdate(
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
      
      sql.sendMessage(
        par, 
        message,
        par.user,
        par.forUser,
        par.message);
      
      entityShare(
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