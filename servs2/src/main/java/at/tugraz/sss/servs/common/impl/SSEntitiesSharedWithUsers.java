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

public class SSEntitiesSharedWithUsers {
  
  protected static final List<SSEntitiesSharedWithUsersI> servsForEntitiesSharedWithUsers = new ArrayList<>();
  
  public void regServ(
    final SSEntitiesSharedWithUsersI servContainer, 
    final SSConfA                    conf) throws SSErr{
    
    try{
      
      if(!conf.use){
        return;
      }
      
      synchronized(servsForEntitiesSharedWithUsers){
        
        if(servsForEntitiesSharedWithUsers.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForEntitiesSharedWithUsers.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void entitiesSharedWithUsers(
    final SSServPar                    servPar,
    final SSEntitiesSharedWithUsersPar par) throws SSErr{  
        
    try{
      
      for(SSEntitiesSharedWithUsersI serv : servsForEntitiesSharedWithUsers){
        serv.entitiesSharedWithUsers(servPar, par);
      }      
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
