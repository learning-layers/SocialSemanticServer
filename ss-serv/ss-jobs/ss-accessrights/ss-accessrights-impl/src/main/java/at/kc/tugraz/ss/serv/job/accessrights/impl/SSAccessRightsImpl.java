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
package at.kc.tugraz.ss.serv.job.accessrights.impl;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.job.accessrights.api.SSAccessRightsClientI;
import at.kc.tugraz.ss.serv.job.accessrights.api.SSAccessRightsServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSAccessRightsRightTypeE;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.SSCircle;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.par.SSAccessRightsUserAllowedIsPar;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.par.SSAccessRightsUserCircleCreatePar;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.par.SSAccessRightsUserCirclesGetPar;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.par.SSAccessRightsUserEntitiesToCircleAddPar;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.par.SSAccessRightsUserUsersToCircleAddPar;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.ret.SSAccessRightsUserCircleCreateRet;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.ret.SSAccessRightsUserEntitiesToCircleAddRet;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.ret.SSAccessRightsUserUsersToCircleAddRet;
import at.kc.tugraz.ss.serv.job.accessrights.impl.fct.sql.SSAccessRightsFct;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.lang.reflect.Method;
import java.util.*;

public class SSAccessRightsImpl extends SSServImplWithDBA implements SSAccessRightsClientI, SSAccessRightsServerI{

  private final SSAccessRightsFct sqlFct;

  public SSAccessRightsImpl(final SSServConfA conf, final SSDBSQLI dbSQL) throws Exception{

    super(conf, null, dbSQL);

    sqlFct = new SSAccessRightsFct(dbSQL);
  }
  
  /* SSServRegisterableImplI */
  @Override
  public List<SSMethU> publishClientOps() throws Exception{

    List<SSMethU> clientOps = new ArrayList<SSMethU>();

    Method[] methods = SSAccessRightsClientI.class.getMethods();

    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }

    return clientOps;
  }

  @Override
  public List<SSMethU> publishServerOps() throws Exception{

    List<SSMethU> serverOps = new ArrayList<SSMethU>();

    Method[] methods = SSAccessRightsServerI.class.getMethods();

    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }

    return serverOps;
  }

  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSAccessRightsClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }

  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSAccessRightsServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }

  /* SSAccessRightsClientI */
  
  @Override
  public void accessRightsUserCircleCreate(final SSSocketCon sSCon, final SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSAccessRightsUserCircleCreateRet.get(accessRightsUserCircleCreate(par), par.op));
  }
  
  @Override
  public void accessRightsUserEntitiesToCircleAdd(final SSSocketCon sSCon, final SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSAccessRightsUserEntitiesToCircleAddRet.get(accessRightsUserEntitiesToCircleAdd(par), par.op));
  }
  
  @Override
  public void accessRightsUserUsersToCircleAdd(final SSSocketCon sSCon, final SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(SSAccessRightsUserUsersToCircleAddRet.get(accessRightsUserUsersToCircleAdd(par), par.op));
  }
  
  /* SSAccessRightsServerI */
  
  @Override
  public List<SSCircle> accessRightsUserCirclesGet(SSServPar parA) throws Exception{
    
    final SSAccessRightsUserCirclesGetPar par = new SSAccessRightsUserCirclesGetPar(parA);
    
    try{
      final List<SSUri>    circleUris = sqlFct.getUserCircleURIs(par.user);
      final List<SSCircle> circles    = new ArrayList<SSCircle>();
      SSCircle             circle;
      
      for(SSUri circleUri : circleUris){
        
        circle              = sqlFct.getCircle            (circleUri);
        circle.circleRights = sqlFct.getCircleRights      (circleUri);
        circle.userUris     = sqlFct.getCircleUserUris    (circleUri);
        circle.entityUris   = sqlFct.getCircleEntityUris  (circleUri);  
          
        circles.add(circle);
      }
      
      return circles;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri accessRightsUserCircleCreate(final SSServPar parA) throws Exception{
    
    final SSAccessRightsUserCircleCreatePar par = new SSAccessRightsUserCircleCreatePar(parA);
    
    try{
      final SSUri circleUri = sqlFct.createCircleURI();
      
      SSServCaller.addEntity(
        par.user, 
        circleUri, 
        par.label, 
        SSEntityEnum.circle);
      
      for(SSUri entityUri : par.entityUris){
        
        SSServCaller.addEntity(
          par.user,
          entityUri,
          SSLabelStr.get(SSUri.toStr(entityUri)),
          SSEntityEnum.entity);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addCircle(circleUri, par.circleType);
      
      switch(par.circleType){
        case priv:
          sqlFct.addCircleRight(circleUri, SSAccessRightsRightTypeE.all);
          break;
        case group:
          sqlFct.addCircleRight(circleUri, SSAccessRightsRightTypeE.read);
          sqlFct.addCircleRight(circleUri, SSAccessRightsRightTypeE.edit);
          sqlFct.addCircleRight(circleUri, SSAccessRightsRightTypeE.addMetadata);
          sqlFct.addCircleRight(circleUri, SSAccessRightsRightTypeE.addEntityToCircle);
          sqlFct.addCircleRight(circleUri, SSAccessRightsRightTypeE.addUserToCircle);
          sqlFct.addCircleRight(circleUri, SSAccessRightsRightTypeE.removeEntity);
          break;
        case pub:
          sqlFct.addCircleRight(circleUri, SSAccessRightsRightTypeE.read);
          sqlFct.addCircleRight(circleUri, SSAccessRightsRightTypeE.addMetadata);
          break;
        default: throw new Exception("circle type currently not supported");
      }
      
      for(SSUri entityUri : par.entityUris){
        sqlFct.addEntityToCircle(circleUri, entityUri);
      }
      
      for(SSUri userUri : par.userUris){
        sqlFct.addUserToCircle(circleUri, userUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return circleUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean accessRightsUserEntitiesToCircleAdd(final SSServPar parA) throws Exception{
    
    final SSAccessRightsUserEntitiesToCircleAddPar par = new SSAccessRightsUserEntitiesToCircleAddPar(parA);
    
    try{
      
      if(!SSAccessRightsRightTypeE.contains(sqlFct.getCircleRights(par.circleUri), SSAccessRightsRightTypeE.addEntityToCircle)){
        throw new Exception("user has not enough rights to add entity to circle");
      }
      
      for(SSUri entityUri : par.entityUris){
        
        SSServCaller.addEntity(
          par.user,
          entityUri,
          SSLabelStr.get(SSUri.toStr(entityUri)),
          SSEntityEnum.entity);
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri entityUri : par.entityUris){
        sqlFct.addEntityToCircle(par.circleUri, entityUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean accessRightsUserUsersToCircleAdd(final SSServPar parA) throws Exception{
    
    final SSAccessRightsUserUsersToCircleAddPar par = new SSAccessRightsUserUsersToCircleAddPar(parA);
    
    try{
      
      if(!SSAccessRightsRightTypeE.contains(sqlFct.getCircleRights(par.circleUri), SSAccessRightsRightTypeE.addUserToCircle)){
        throw new Exception("user has not enough rights to add user to circle");
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri userUri : par.userUris){
        sqlFct.addUserToCircle(par.circleUri, userUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean accessRightsUserAllowedIs(final SSServPar parA) throws Exception{
   
    final SSAccessRightsUserAllowedIsPar par = new SSAccessRightsUserAllowedIsPar(parA);
    
    try{
      
      final SSEntityEnum entityType = SSServCaller.entityTypeGet(par.entityUri);

      if(SSEntityEnum.equals(entityType, SSEntityEnum.entity)){
        return true;
      }
      
      final List<SSUri> entityCircleUris = sqlFct.getEntityCircleURIs(par.entityUri);
      
      if(entityCircleUris.isEmpty()){
        return true;
      }
      
      final List<SSUri> userCircleUris = sqlFct.getUserCircleURIs(par.user);
      
      if(par.circleUri != null){
        
        if(
          !SSUri.contains                   (entityCircleUris, par.circleUri) ||
          !SSUri.contains                   (userCircleUris,   par.circleUri) ||
          !SSAccessRightsRightTypeE.contains(sqlFct.getCircleRights(par.circleUri), par.accessRight)){
          return false;
        }
        
        return true;
      }
      
      for(SSUri userCircleUri : userCircleUris){
        
        if(
          SSUri.contains                   (entityCircleUris, userCircleUri) &&
          SSAccessRightsRightTypeE.contains(sqlFct.getCircleRights(userCircleUri), par.accessRight)){
          return true;
        }
      }
      
      return false;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}