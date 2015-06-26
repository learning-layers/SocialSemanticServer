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

import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.friend.api.SSFriendClientI;
import at.kc.tugraz.ss.friend.api.SSFriendServerI;
import at.kc.tugraz.ss.friend.datatypes.SSFriend;
import at.kc.tugraz.ss.friend.datatypes.par.SSFriendUserAddPar;
import at.kc.tugraz.ss.friend.datatypes.par.SSFriendsUserGetPar;
import at.kc.tugraz.ss.friend.datatypes.ret.SSFriendUserAddRet;
import at.kc.tugraz.ss.friend.datatypes.ret.SSFriendsUserGetRet;
import at.kc.tugraz.ss.friend.impl.fct.sql.SSFriendSQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.caller.SSServCallerU;
import java.util.ArrayList;
import java.util.List;

public class SSFriendImpl extends SSServImplWithDBA implements SSFriendClientI, SSFriendServerI{
  
  private final SSFriendSQLFct sqlFct;

  public SSFriendImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct = new SSFriendSQLFct(dbSQL);
  }
  
  @Override
  public void friendsGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSFriendsUserGetRet.get(friendsUserGet(parA), parA.op));
  }
  
  @Override
  public List<SSFriend> friendsUserGet(final SSServPar parA) throws Exception{
    
    try{
      final SSFriendsUserGetPar  par         = SSFriendsUserGetPar.get(parA);
      final List<SSFriend>       friends     = new ArrayList<>();
      
      for (SSFriend friend : sqlFct.getFriends(par.user)){
      
        friends.add(
          SSFriend.get(
            friend,
            ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
              new SSEntityGetPar(
                null,
                null,
                null,
                friend.id,  //entity
                null, //forUser
                null, //label
                null, //type
                false, //withUserRestriction
                false, //invokeEntityHandlers
              null, //descPar
                true))));
      }

      return friends;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void friendAdd(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSFriendUserAddRet.get(friendUserAdd(parA), parA.op));
  }
  
  @Override
  public SSUri friendUserAdd(final SSServPar parA) throws Exception{
    
    try{
      final SSFriendUserAddPar  par        = SSFriendUserAddPar.get(parA);
      
      dbSQL.startTrans(par.shouldCommit);
            
      sqlFct.addFriend(par.user, par.friend);
      
      dbSQL.commit(par.shouldCommit);
      
      return par.friend;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}