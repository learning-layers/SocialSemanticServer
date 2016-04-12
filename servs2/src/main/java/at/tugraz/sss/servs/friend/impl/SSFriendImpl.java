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
package at.tugraz.sss.servs.friend.impl;

import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.common.api.SSDescribeEntityI;
import at.tugraz.sss.servs.friend.api.SSFriendClientI;
import at.tugraz.sss.servs.friend.api.SSFriendServerI;
import at.tugraz.sss.servs.friend.datatype.SSFriend;
import at.tugraz.sss.servs.friend.datatype.SSFriendAddPar;
import at.tugraz.sss.servs.friend.datatype.SSFriendGetPar;
import at.tugraz.sss.servs.friend.datatype.SSFriendsGetPar;
import at.tugraz.sss.servs.friend.datatype.SSFriendAddRet;
import at.tugraz.sss.servs.friend.datatype.SSFriendsGetRet;
import at.tugraz.sss.servs.entity.datatype.SSEntityGetPar;
import at.tugraz.sss.servs.user.datatype.SSUser;
import at.tugraz.sss.servs.entity.datatype.SSClientE;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.common.datatype.SSEntityDescriberPar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSServRetI; 
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.entity.impl.*;
import java.util.ArrayList;
import java.util.List;

public class SSFriendImpl
extends
  SSEntityImpl
implements
  SSFriendClientI,
  SSFriendServerI,
  SSDescribeEntityI{
  
  private final SSFriendSQL sql = new SSFriendSQL(dbSQL);
  
  public SSFriendImpl(){
    super(SSCoreConf.instGet().getFriend());
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(!par.setFriends){
        return entity;
      }
      
      switch(entity.type){
        
        case user: {
          
          final SSUser userEntity = SSUser.get(entity);
          
          userEntity.friends.addAll(
            friendsGet(
              new SSFriendsGetPar(
                servPar,
                par.user)));
          
          return userEntity;
        }
        
        default: return entity;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSFriend friendGet(final SSFriendGetPar par) throws SSErr{
    
    try{
      
      if(par.friend == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }

      return SSFriend.get(
        sql.getFriend(par, par.friend),
        entityGet(
          new SSEntityGetPar(
            par, 
            par.user, 
            par.friend, 
            par.withUserRestriction, //withUserRestriction, 
            null))); //descPar));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI friendsGet(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{

      userCommons.checkKeyAndSetUser(parA);
    
    final SSFriendsGetPar par = (SSFriendsGetPar) parA.getFromClient(clientType, parA, SSFriendsGetPar.class);
    
    return SSFriendsGetRet.get(friendsGet(par));
    
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEntity> friendsGet(final SSFriendsGetPar par) throws SSErr{
    
    try{
      
      final List<SSEntity> result = new ArrayList<>();
      
      for(SSUri friend : sql.getFriends(par, par.user)){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          result,
          friendGet(
            new SSFriendGetPar(
              par, 
              par.user, 
              friend)));
      }
      
      return result;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI friendAdd(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      
      userCommons.checkKeyAndSetUser(parA);
      
      final SSFriendAddPar par = (SSFriendAddPar) parA.getFromClient(clientType, parA, SSFriendAddPar.class);
      
      return SSFriendAddRet.get(friendAdd(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri friendAdd(final SSFriendAddPar par) throws SSErr{
    
    try{
      
      dbSQL.startTrans(par, par.shouldCommit);
            
      sql.addFriend(par, par.user, par.friend);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return par.friend;
      
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