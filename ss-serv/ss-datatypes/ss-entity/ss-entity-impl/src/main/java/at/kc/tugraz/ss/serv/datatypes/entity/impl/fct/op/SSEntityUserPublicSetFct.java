/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.op;

import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserPublicSetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.sql.SSEntitySQLFct;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;

public class SSEntityUserPublicSetFct{

  public static void addEntityToPublicCircle(
    final SSEntitySQLFct           sqlFct, 
    final SSEntityUserPublicSetPar par, 
    final SSUri                    publicCircleUri) throws Exception{
    
    try{
      sqlFct.addEntityToCircle(publicCircleUri, par.entityUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void setPublicByEntityHandlers(final SSEntityUserPublicSetPar par) throws Exception{
    
    try{
      final SSEntityEnum entityType = SSServCaller.entityTypeGet(par.entityUri);
      
      if(SSEntityEnum.equals(entityType, SSEntityEnum.entity)){
        return;
      }
      
      for(SSServA serv : SSServA.getServsManagingEntities()){
        
        if(((SSEntityHandlerImplI) serv.serv()).setUserEntityPublic(par, entityType)){
          return;
        }
      }
          
      throw new Exception("entity couldnt be set to be public by entity handlers");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
