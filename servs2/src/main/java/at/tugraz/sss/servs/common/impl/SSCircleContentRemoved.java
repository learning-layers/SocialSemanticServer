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

public class SSCircleContentRemoved {
 
  protected static final List<SSServContainerI> servsForCircleContentRemoved            = new ArrayList<>();
 
  public void regServ(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
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
      
      for(SSServContainerI serv : servsForCircleContentRemoved){
        ((SSCircleContentRemovedI) serv.getServImpl()).circleContentRemoved(servPar, circleContentRemovedPar);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
