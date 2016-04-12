/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.sss.servs.common.impl;

import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.common.datatype.SSEntityDescriberPar;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.conf.SSConfA;
import at.tugraz.sss.servs.common.api.*;
import java.util.*;

public class SSDescribeEntity {
  
  protected static final List<SSDescribeEntityI> servsForDescribeEntity = new ArrayList<>();
  
  public void regServ(
    final SSDescribeEntityI serv, 
    final SSConfA           conf) throws SSErr{
    
    try{
      
      if(!conf.use){
        return;
      }
      
      synchronized(servsForDescribeEntity){
        
        if(servsForDescribeEntity.contains(serv)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForDescribeEntity.add(serv);
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
      
      for(SSDescribeEntityI serv : servsForDescribeEntity){
        describedEntity = serv.describeEntity(servPar, describedEntity, descPar);
      }

      return describedEntity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
