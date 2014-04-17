/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.op;

import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityRightTypeE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCircleCreatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.sql.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;

public class SSEntityUserCircleCreateFct{
  
  public static SSUri createCircleWithRights(
    final SSEntitySQLFct              sqlFct,
    final SSEntityUserCircleCreatePar par) throws Exception{
    
    try{
      final SSUri circleUri = sqlFct.createCircleURI();
      
      SSServCaller.entityAdd(
        par.user,
        circleUri,
        par.label,
        SSEntityEnum.circle,
        false);
      
      sqlFct.addCircle(circleUri, par.circleType);
      
      switch(par.circleType){
        case priv:
          sqlFct.addCircleRight(circleUri, SSEntityRightTypeE.all);
          break;
        case group:
          sqlFct.addCircleRight(circleUri, SSEntityRightTypeE.edit);
          sqlFct.addCircleRight(circleUri, SSEntityRightTypeE.read);
          sqlFct.addCircleRight(circleUri, SSEntityRightTypeE.addMetadata);
          sqlFct.addCircleRight(circleUri, SSEntityRightTypeE.addEntityToCircle);
          sqlFct.addCircleRight(circleUri, SSEntityRightTypeE.addUserToCircle);
          sqlFct.addCircleRight(circleUri, SSEntityRightTypeE.removeEntity);
          break;
        default: throw new Exception("circle type currently not supported");
      }
      
      return circleUri;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public static void updateEntities(final SSEntityUserCircleCreatePar par) throws Exception{
    
    try{
      
      for(SSUri entityUri : par.entityUris){
        
        SSServCaller.entityAdd(
          par.user,
          entityUri,
          SSLabelStr.get(SSUri.toStr(entityUri)),
          SSEntityEnum.entity,
          false);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void addEntitiesToCircle(
    final SSEntitySQLFct              sqlFct, 
    final SSEntityUserCircleCreatePar par, 
    final SSUri                       circleUri) throws Exception{
    
    try{
      for(SSUri entityUri : par.entityUris){
        sqlFct.addEntityToCircle(circleUri, entityUri);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void addUsersToCircle(
    final SSEntitySQLFct              sqlFct, 
    final SSEntityUserCircleCreatePar par, 
    final SSUri                       circleUri) throws Exception{
    
    try{
    
      sqlFct.addUserToCircle(circleUri, par.user);
      
      for(SSUri userUri : par.userUris){
        sqlFct.addUserToCircle(circleUri, userUri);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}