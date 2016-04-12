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

public class SSGetUsersResources {
  
  protected static final List<SSGetUsersResourcesI> servsForGetUsersResources = new ArrayList<>();
  
  public void regServ(
    final SSGetUsersResourcesI servContainer, 
    final SSConfA              conf) throws SSErr{
    
    try{
      
      if(!conf.use){
        return;
      }
      
      synchronized(servsForGetUsersResources){
        
        if(servsForGetUsersResources.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForGetUsersResources.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void getUsersResources(
    final SSServPar                          servPar,
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      for(SSGetUsersResourcesI serv : servsForGetUsersResources){
        serv.getUsersResources(servPar, usersEntities);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}