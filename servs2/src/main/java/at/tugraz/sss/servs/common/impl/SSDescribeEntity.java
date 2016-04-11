/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.sss.servs.common.impl;

import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.servs.common.api.*;
import java.util.*;

public class SSDescribeEntity {
  
  protected static final List<SSServContainerI> servsForDescribeEntity = new ArrayList<>();
  
  public void regServ(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsForDescribeEntity){
        
        if(servsForDescribeEntity.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForDescribeEntity.add(servContainer);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public SSEntity describeEntity(
    final SSServPar            servPar,
    final SSUri                user,
    final SSEntity             entity,
    final SSEntityDescriberPar descPar,
    final boolean              withUserRestriction) throws SSErr{
    
    try{
      
      if(entity == null){
        return null;
      }
      
      if(descPar == null){
        return entity;
      }
        
      descPar.user                = user;
      descPar.withUserRestriction = withUserRestriction;
      
      SSEntity describedEntity = entity;
      
      for(SSServContainerI serv : servsForDescribeEntity){
        describedEntity = ((SSDescribeEntityI) serv.getServImpl()).describeEntity(servPar, describedEntity, descPar);
      }

      return describedEntity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
