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
package at.kc.tugraz.sss.notification.impl;

import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.sss.notification.api.SSNotificationClientI;
import at.kc.tugraz.sss.notification.api.SSNotificationServerI;
import at.kc.tugraz.sss.notification.datatypes.SSNotification;
import at.kc.tugraz.sss.notification.datatypes.par.SSNotificationTest1Par;
import at.kc.tugraz.sss.notification.datatypes.ret.SSNotificationTest1Ret;
import at.kc.tugraz.sss.notification.impl.fct.sql.SSNotificationSQLFct;

public class SSNotificationImpl extends SSServImplWithDBA implements SSNotificationClientI, SSNotificationServerI{
  
  private final SSNotificationSQLFct sqlFct;
  
  public SSNotificationImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{

    super(conf, null, dbSQL);

    this.sqlFct = new SSNotificationSQLFct(dbSQL);
  }

  @Override
  public void notificationTest1(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSNotificationTest1Ret.get(notificationTest1(parA), parA.op));
  }

  @Override
  public SSNotification notificationTest1(final SSServPar parA) throws Exception{
    
    try{
      
      final SSNotificationTest1Par par = new SSNotificationTest1Par(parA);
      
      return sqlFct.test1(par.entity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}