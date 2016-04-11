/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.sss.servs.common.impl;

import at.tugraz.sss.serv.errreg.SSServErrReg;
import at.tugraz.sss.serv.conf.api.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.servs.common.api.*;
import java.util.*;

public class SSPushEntitiesToUsers {
  
  protected static final List<SSPushEntitiesToUsersI> servsForPushEntitiesToUsers = new ArrayList<>();
  
  public void regServ(
    final SSPushEntitiesToUsersI servContainer, 
    final SSConfA                conf) throws SSErr{
    
    try{
      
      if(!conf.use){
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
      
      for(SSPushEntitiesToUsersI serv : servsForPushEntitiesToUsers){
         serv.pushEntitiesToUsers(servPar, par);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
