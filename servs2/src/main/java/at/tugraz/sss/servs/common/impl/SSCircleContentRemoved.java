/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.sss.servs.common.impl;

import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSCircleContentRemovedPar;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.conf.SSConfA;
import at.tugraz.sss.servs.common.api.*;
import java.util.*;

public class SSCircleContentRemoved {
 
  protected static final List<SSCircleContentRemovedI> servsForCircleContentRemoved            = new ArrayList<>();
 
  public void regServ(
    final SSCircleContentRemovedI servContainer, 
    final SSConfA                 conf) throws SSErr{
    
    try{
      
      if(!conf.use){
        return;
      }
      
      synchronized(servsForCircleContentRemoved){
        
        if(servsForCircleContentRemoved.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForCircleContentRemoved.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void circleContentRemoved(
    final SSServPar                 servPar,
    final SSCircleContentRemovedPar circleContentRemovedPar) throws SSErr{
    
    try{
      
      for(SSCircleContentRemovedI serv : servsForCircleContentRemoved){
         serv.circleContentRemoved(servPar, circleContentRemovedPar);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
