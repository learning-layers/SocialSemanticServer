/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.op;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircleTypeE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserSharePar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;

public class SSEntityUserShareFct{

  public static SSUri createNewCircleAndShare(final SSEntityUserSharePar par) throws Exception{
    
    try{
      
      if(!par.circleUris.isEmpty()){
        throw new UnsupportedOperationException("currenlty sharing with circles not possible");
      }
      
      return SSServCaller.entityUserCircleCreate(
        par.user,
        par.entityUri,
        par.userUris,
        SSEntityCircleTypeE.group,
        SSLabelStr.get(SSUri.toStr(par.user) + SSStrU.underline + SSUri.toStr(par.entityUri)),
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static SSUri useCircleAndShare(final SSEntityUserSharePar par) throws Exception{
    
    try{
      
      if(!par.circleUris.isEmpty()){
        throw new UnsupportedOperationException("currenlty sharing with circles not possible");
      }
      
      SSServCaller.entityUserEntitiesToCircleAdd(
        par.user, 
        par.entityCircleUri, 
        par.entityUri, 
        false);
      
      return SSServCaller.entityUserUsersToCircleAdd(
        par.user,
        par.entityCircleUri,
        par.userUris,
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public static void shareByEntityHandlers(final SSEntityUserSharePar par) throws Exception{
    
    try{
      final SSEntityEnum entityType = SSServCaller.entityTypeGet(par.entityUri);
      
      if(SSEntityEnum.equals(entityType, SSEntityEnum.entity)){
        return;
      }
      
      for(SSServA serv : SSServA.getServsManagingEntities()){
        
        if(((SSEntityHandlerImplI) serv.serv()).shareUserEntity(par, entityType)){
          return;
        }
      }
      
      throw new Exception("entity couldnt be shared through entity handlers");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}