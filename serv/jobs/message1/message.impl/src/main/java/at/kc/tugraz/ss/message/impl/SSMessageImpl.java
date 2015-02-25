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

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.message.api.SSMessageClientI;
import at.kc.tugraz.ss.message.api.SSMessageServerI;
import at.kc.tugraz.ss.message.datatypes.SSMessage;
import at.kc.tugraz.ss.message.datatypes.par.SSMessageSendPar;
import at.kc.tugraz.ss.message.datatypes.par.SSMessagesGetPar;
import at.kc.tugraz.ss.message.datatypes.ret.SSMessageSendRet;
import at.kc.tugraz.ss.message.datatypes.ret.SSMessagesGetRet;
import at.kc.tugraz.ss.message.impl.fct.activity.SSMessageActivityFct;
import at.kc.tugraz.ss.message.impl.fct.sql.SSMessageSQLFct;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.ArrayList;
import java.util.List;

public class SSMessageImpl extends SSServImplWithDBA implements SSMessageClientI, SSMessageServerI{
  
  private final SSMessageSQLFct sqlFct;

  public SSMessageImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{
    super(conf, null, dbSQL);
    
    this.sqlFct = new SSMessageSQLFct(dbSQL);
  }
  
  @Override
  public void messagesGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSMessagesGetRet.get(messagesGet(parA), SSDateU.dateAsLong(), parA.op));
  }
  
  @Override
  public List<SSMessage> messagesGet(final SSServPar parA) throws Exception{
    
    try{
      final SSMessagesGetPar  par         = new SSMessagesGetPar(parA);
      final List<SSMessage>   messages    = new ArrayList<>();
      final List<SSMessage>   tmpMessages = sqlFct.getMessages(par.user, par.startTime);
      
      for(SSMessage message : tmpMessages){
        
        message.read = SSServCaller.entityReadGet(par.user, message.id);
        
        if(
          !par.includeRead &&
          message.read){
          continue;
        }
        
        messages.add(message);
      }

      return messages;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void messageSend(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSMessageSendRet.get(messageSend(parA), parA.op));
  }
  
  @Override
  public SSUri messageSend(final SSServPar parA) throws Exception{
    
    try{
      final SSMessageSendPar  par        = new SSMessageSendPar(parA);
      final SSUri             messageUri = SSServCaller.vocURICreate();
      
      dbSQL.startTrans(par.shouldCommit);
      
      SSServCaller.entityEntityToPrivCircleAdd(
        par.user,
        messageUri,
        SSEntityE.message,
        null,
        null,
        null,
        false);
   
      sqlFct.sendMessage(
        messageUri, 
        par.user, 
        par.forUser,
        par.message);
      
      SSServCaller.circleEntityShare(
        par.user,
        messageUri,
        SSUri.asListWithoutNullAndEmpty(par.forUser),
        SSUri.asListWithoutNullAndEmpty(),
        null,
        false);
      
      SSMessageActivityFct.messageSend(
        par, 
        messageUri, 
        par.message);
      
      dbSQL.commit(par.shouldCommit);
      
      return messageUri;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}