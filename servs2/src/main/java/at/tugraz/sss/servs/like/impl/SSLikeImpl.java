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
package at.tugraz.sss.servs.like.impl;

import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.like.api.SSLikeClientI;
import at.tugraz.sss.servs.like.api.SSLikeServerI;
import at.tugraz.sss.servs.like.datatype.SSLikes;
import at.tugraz.sss.servs.like.datatype.SSLikeUserSetPar;
import at.tugraz.sss.servs.like.datatype.SSLikesUserGetPar;
import at.tugraz.sss.servs.like.datatype.SSLikeUserSetRet;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.entity.datatype.SSClientE;
import at.tugraz.sss.servs.entity.datatype.SSEntityUpdatePar;
import at.tugraz.sss.servs.common.api.SSDescribeEntityI;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.common.datatype.SSEntityDescriberPar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import at.tugraz.sss.servs.entity.datatype.SSServRetI; 
import at.tugraz.sss.servs.conf.*;
import at.tugraz.sss.servs.entity.impl.*;

public class SSLikeImpl 
extends SSEntityImpl 
implements 
  SSLikeClientI, 
  SSLikeServerI,
  SSDescribeEntityI{
  
  private final SSLikeActAndLog                       actAndLog   = new SSLikeActAndLog();
  private final SSLikeSQL                             sql         = new SSLikeSQL(dbSQL);
   
  public SSLikeImpl(){
    super(SSCoreConf.instGet().getLike());
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar            servPar,
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(par.setLikes){
        
        entity.likes =
          likesGet(
            new SSLikesUserGetPar(
              servPar, 
              par.user,
              entity.id,
              null,
              par.withUserRestriction));
      }
      
      return entity;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSLikes likesGet(final SSLikesUserGetPar par) throws SSErr{
    
    try{
      
      if(par.entity == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final SSEntity entity = 
        sql.getEntityTest(
          par, 
          SSConf.systemUserUri,
          par.user, 
          par.entity, 
          par.withUserRestriction);
      
      if(entity == null){
        return null;
      }
      
      if(par.withUserRestriction){
        
        if(
          par.forUser != null &&
          !SSStrU.isEqual(par.forUser, par.user)){
          throw SSErr.get(SSErrE.userNotAllowedToRetrieveForOtherUser);
        }
      }
      
      return sql.getLikes(
        par, 
        par.user, 
        par.forUser, 
        par.entity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI likeSet(final SSClientE clientType, final SSServPar parA) throws SSErr {
    
    try{
      userCommons.checkKeyAndSetUser(parA);
      
      final SSLikeUserSetPar par = (SSLikeUserSetPar) parA.getFromClient(clientType, parA, SSLikeUserSetPar.class);
      
      return SSLikeUserSetRet.get(likeSet(par));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
   
  @Override
  public SSUri likeSet(final SSLikeUserSetPar par) throws SSErr{
    
    try{

      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri entity =
        entityUpdate(
          new SSEntityUpdatePar(
            par, 
            par.user,
            par.entity,
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
        return null;
      }
      
      sql.like(
        par, 
        par.user, 
        par.entity, 
        par.value);
      
      dbSQL.commit(par, par.shouldCommit);
      
      actAndLog.setLike(
        par, 
        par.shouldCommit);
      
      return par.entity;
      
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