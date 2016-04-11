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

public class SSPushEntitiesToUsers {
  
  protected static final List<SSServContainerI> servsForPushEntitiesToUsers = new ArrayList<>();
  
  public void regServ(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsForPushEntitiesToUsers){
        
        if(servsForPushEntitiesToUsers.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForPushEntitiesToUsers.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void pushEntitiesToUsers(
    final SSServPar                servPar,
    final SSPushEntitiesToUsersPar par) throws SSErr{
    
    try{
      
      for(SSServContainerI serv : servsForPushEntitiesToUsers){
        ((SSPushEntitiesToUsersI) serv.getServImpl()).pushEntitiesToUsers(servPar, par);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
