/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSAccessRightsCircleTypeE;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.job.accessrights.api.SSAccessRightsClientI;
import at.kc.tugraz.ss.serv.job.accessrights.api.SSAccessRightsServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSAccessRightsRightTypeE;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.SSCircle;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.par.SSAccessRightsCircleUserAddPar;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.par.SSAccessRightsEntityCircleURIsGetPar;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.par.SSAccessRightsEntityMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.par.SSAccessRightsUserAllowedIsPar;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.par.SSAccessRightsUserCircleCreatePar;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.par.SSAccessRightsUserCircleTypesForEntityGetPar;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.par.SSAccessRightsUserCirclesGetPar;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.par.SSAccessRightsUserEntitiesToCircleAddPar;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.par.SSAccessRightsUserEntityCirclesGetPar;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.par.SSAccessRightsUserUsersToCircleAddPar;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.ret.SSAccessRightsUserCircleCreateRet;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.ret.SSAccessRightsUserEntitiesToCircleAddRet;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.ret.SSAccessRightsUserUsersToCircleAddRet;
import at.kc.tugraz.ss.serv.job.accessrights.impl.fct.sql.SSAccessRightsFct;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.*;

public class SSAccessRightsImpl extends SSServImplWithDBA implements SSAccessRightsClientI, SSAccessRightsServerI{

  private final SSAccessRightsFct sqlFct;
  private static SSUri publicCircleUri = null;

  public SSAccessRightsImpl(final SSServConfA conf, final SSDBSQLI dbSQL) throws Exception{

    super(conf, null, dbSQL);

    sqlFct = new SSAccessRightsFct(dbSQL);
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
  public List<SSUri> accessRightsEntityCircleURIsGet(final SSServPar parA) throws Exception{
    
    final SSAccessRightsEntityCircleURIsGetPar par = new SSAccessRightsEntityCircleURIsGetPar(parA);
    
    try{
      return sqlFct.getEntityCircleURIs(par.entityUri);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri accessRightsCirclePublicAdd(final SSServPar parA) throws Exception{
    
    try{
      
      dbSQL.startTrans(true);
      
      publicCircleUri = sqlFct.addPublicCircle();
      
      sqlFct.addCircleRight(publicCircleUri, SSAccessRightsRightTypeE.read);
      sqlFct.addCircleRight(publicCircleUri, SSAccessRightsRightTypeE.addMetadata);
      sqlFct.addCircleRight(publicCircleUri, SSAccessRightsRightTypeE.addEntityToCircle);
      
      dbSQL.commit(true);
      
      return publicCircleUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSUri accessRightsCircleURIPublicGet(final SSServPar parA) throws Exception{
    
    try{
      
      if(publicCircleUri == null){
        throw new Exception("public circle not initialized");
      }
      
      return publicCircleUri;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSCircle> accessRightsUserCirclesGet(final SSServPar parA) throws Exception{
    
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
          sqlFct.addCircleRight(circleUri, SSAccessRightsRightTypeE.edit);
          sqlFct.addCircleRight(circleUri, SSAccessRightsRightTypeE.read);
          sqlFct.addCircleRight(circleUri, SSAccessRightsRightTypeE.addMetadata);
          sqlFct.addCircleRight(circleUri, SSAccessRightsRightTypeE.addEntityToCircle);
          sqlFct.addCircleRight(circleUri, SSAccessRightsRightTypeE.addUserToCircle);
          sqlFct.addCircleRight(circleUri, SSAccessRightsRightTypeE.removeEntity);
          break;
        default: throw new Exception("circle type currently not supported");
      }
      
      for(SSUri entityUri : par.entityUris){
        sqlFct.addEntityToCircle(circleUri, entityUri);
      }
      
      sqlFct.addUserToCircle(circleUri, par.user);
      
      for(SSUri userUri : par.userUris){
        sqlFct.addUserToCircle(circleUri, userUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return circleUri;
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void accessRightsCircleUserAdd(final SSServPar parA) throws Exception{
    
    final SSAccessRightsCircleUserAddPar par = new SSAccessRightsCircleUserAddPar(parA);
  
    try{
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addUserToCircle(par.circleUri, par.user);
      
      dbSQL.commit(par.shouldCommit);
      
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public SSAccessRightsCircleTypeE accessRightsEntityMostOpenCircleTypeGet(final SSServPar parA) throws Exception{
    
    final SSAccessRightsEntityMostOpenCircleTypeGetPar par                = new SSAccessRightsEntityMostOpenCircleTypeGetPar(parA);
    SSAccessRightsCircleTypeE                          mostOpenCircleType = null;
    
    try{
      
      final List<SSUri> entityCircleUris = sqlFct.getEntityCircleURIs(par.entityUri);
      
      for(SSUri circleUri : entityCircleUris){
        
        switch(sqlFct.getCircleType(circleUri)){
          
          case pub:{
            return SSAccessRightsCircleTypeE.pub;
          }
          
          case priv:{
            
            if(
              mostOpenCircleType != null || 
              mostOpenCircleType != SSAccessRightsCircleTypeE.group){ 
              
              mostOpenCircleType = SSAccessRightsCircleTypeE.priv;
            }
            
            break;
          }   
          
          default:{
            mostOpenCircleType = SSAccessRightsCircleTypeE.group;
            break;
          }
        }
      }
      
      return mostOpenCircleType;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSAccessRightsCircleTypeE> accessRightsUserCircleTypesForEntityGet(final SSServPar parA) throws Exception{
    
    final SSAccessRightsUserCircleTypesForEntityGetPar par         = new SSAccessRightsUserCircleTypesForEntityGetPar(parA);
    final List<SSAccessRightsCircleTypeE>              circleTypes = new ArrayList<SSAccessRightsCircleTypeE>();
    SSAccessRightsCircleTypeE                          circleType;
    
    try{
      final List<SSUri> entityCircleUris = sqlFct.getEntityCircleURIs(par.entityUri);
      final List<SSUri> userCircleUris   = sqlFct.getUserCircleURIs  (par.user);
      
      for(SSUri circleUri : entityCircleUris){
        
        if(!SSUri.contains(userCircleUris, circleUri)){
          continue;
        }
        
        circleType = sqlFct.getCircleType(circleUri);
        
        if(!SSAccessRightsCircleTypeE.contains(circleTypes, circleType)){
          circleTypes.add(circleType);
        }
      }
      
      return circleTypes;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override 
  public List<SSCircle> accessRightsUserEntityCirclesGet(final SSServPar parA) throws Exception{
    
    final SSAccessRightsUserEntityCirclesGetPar par               = new SSAccessRightsUserEntityCirclesGetPar(parA);
    final List<SSCircle>                        userEntityCircles = new ArrayList<SSCircle>();
    
    try{
      final List<SSUri> userCircleURIs   = sqlFct.getUserCircleURIs   (par.user);
      final List<SSUri> entityCircleURIs = sqlFct.getEntityCircleURIs (par.entityUri);
      
      for(SSUri entityCircleUri : entityCircleURIs){
        
        if(!SSUri.contains(userCircleURIs, entityCircleUri)){
          continue;
        }
        
        userEntityCircles.add(sqlFct.getCircle(entityCircleUri));
      }

      return userEntityCircles;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean accessRightsUserEntitiesToCircleAdd(final SSServPar parA) throws Exception{
    
    final SSAccessRightsUserEntitiesToCircleAddPar par = new SSAccessRightsUserEntitiesToCircleAddPar(parA);
    
    try{
      final List<SSUri> userCircleURIs = sqlFct.getUserCircleURIs(par.user);
      
      if(!SSUri.contains(userCircleURIs, par.circleUri)){
        throw new Exception("user doesnt have access to circle");
      }
      
      if(!sqlFct.hasCircleRight(par.circleUri, SSAccessRightsRightTypeE.addEntityToCircle)){
        throw new Exception("circle has not enough rights to add entity to it");
      }
      
      //TODO dtheiler: check where this goes
//      for(SSUri entityUri : par.entityUris){
//        
//        SSServCaller.addEntity(
//          par.user,
//          entityUri,
//          SSLabelStr.get(SSUri.toStr(entityUri)),
//          SSEntityEnum.entity);
//      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri entityUri : par.entityUris){
        sqlFct.addEntityToCircle(par.circleUri, entityUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean accessRightsUserUsersToCircleAdd(final SSServPar parA) throws Exception{
    
    final SSAccessRightsUserUsersToCircleAddPar par = new SSAccessRightsUserUsersToCircleAddPar(parA);
    
    try{
      
      if(!sqlFct.hasCircleRight(par.circleUri, SSAccessRightsRightTypeE.addUserToCircle)){
        throw new Exception("circle has not enough rights to add user to it");
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(SSUri userUri : par.userUris){
        sqlFct.addUserToCircle(par.circleUri, userUri);
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
    }catch(Exception error){
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean accessRightsUserAllowedIs(final SSServPar parA) throws Exception{
   
    final SSAccessRightsUserAllowedIsPar par = new SSAccessRightsUserAllowedIsPar(parA);
    
    try{
      
      if(SSEntityEnum.equals(SSServCaller.entityTypeGet(par.entityUri), SSEntityEnum.entity)){
        return true;
      }
      
      final List<SSUri> entityCircleUris = sqlFct.getEntityCircleURIs(par.entityUri);
      
      if(entityCircleUris.isEmpty()){
        return true;
      }
      
      for(SSUri userCircleUri : sqlFct.getUserCircleURIs(par.user)){
        
        if(SSUri.contains(entityCircleUris, userCircleUri)){
          
          if(sqlFct.hasCircleRight(userCircleUri, par.accessRight)){
            return true;
          }
        }
      }
      
      return false;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}