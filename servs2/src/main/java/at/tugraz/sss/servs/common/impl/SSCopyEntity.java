/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.sss.servs.common.impl;

import at.tugraz.sss.servs.common.datatype.SSEntityCopyPar;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.conf.SSConfA;
import at.tugraz.sss.servs.common.api.*;
import java.util.*;

public class SSCopyEntity {
  
  protected static final List<SSCopyEntityI> servsForCopyEntity = new ArrayList<>();
  
  public void regServ(
    final SSCopyEntityI servContainer, 
    final SSConfA       conf) throws SSErr{
    
    try{
      
      if(!conf.use){
        return;
      }
      
      synchronized(servsForCopyEntity){
        
        if(servsForCopyEntity.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForCopyEntity.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  public void copyEntity(
    final SSEntityCopyPar entityCopyPar,
    final SSEntity        entity) throws SSErr{
    
    try{
      
      for(SSCopyEntityI serv : servsForCopyEntity){
        serv.copyEntity(entityCopyPar, entity, entityCopyPar);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
