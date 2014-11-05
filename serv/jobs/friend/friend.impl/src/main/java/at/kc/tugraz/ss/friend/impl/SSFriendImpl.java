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
package at.kc.tugraz.ss.friend.impl;

import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.friend.api.SSFriendClientI;
import at.kc.tugraz.ss.friend.api.SSFriendServerI;
import at.kc.tugraz.ss.friend.datatypes.SSFriend;
import at.kc.tugraz.ss.friend.datatypes.par.SSFriendUserAddPar;
import at.kc.tugraz.ss.friend.datatypes.par.SSFriendsUserGetPar;
import at.kc.tugraz.ss.friend.datatypes.ret.SSFriendUserAddRet;
import at.kc.tugraz.ss.friend.datatypes.ret.SSFriendsUserGetRet;
import at.kc.tugraz.ss.friend.impl.fct.sql.SSFriendSQLFct;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.ArrayList;
import java.util.List;

public class SSFriendImpl extends SSServImplWithDBA implements SSFriendClientI, SSFriendServerI{
  
  private final SSFriendSQLFct sqlFct;

  public SSFriendImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{
    super(conf, null, dbSQL);
    
    this.sqlFct = new SSFriendSQLFct(dbSQL);
  }
  
  @Override
  public void friendsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSFriendsUserGetRet.get(friendsUserGet(parA), parA.op));
  }
  
  @Override
  public List<SSFriend> friendsUserGet(final SSServPar parA) throws Exception{
    
    try{
      final SSFriendsUserGetPar  par         = new SSFriendsUserGetPar(parA);
      final List<SSFriend>       friends    = new ArrayList<>();
      
	  return friends;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void friendAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCaller.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSFriendUserAddRet.get(friendUserAdd(parA), parA.op));
  }
  
  @Override
  public SSUri friendUserAdd(final SSServPar parA) throws Exception{
    
    try{
      final SSFriendUserAddPar  par        = new SSFriendUserAddPar(parA);
      //final SSUri               messageUri = SSServCaller.vocURICreate();
      
      dbSQL.startTrans(par.shouldCommit);
            
      
      
      dbSQL.commit(par.shouldCommit);
      
      return SSServCaller.vocURICreate();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}