/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.sss.servs.common.impl;

import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.conf.SSConfA;
import at.tugraz.sss.servs.common.api.*;
import java.util.*;

public class SSGetUserRelations {
  
  protected static final List<SSGetUserRelationsI> servsForGetUserRelations = new ArrayList<>();
  
  public void regServ(
    final SSGetUserRelationsI servContainer, 
    final SSConfA             conf) throws SSErr{
    
    try{
      
      if(!conf.use){
        return;
      }
      
      synchronized(servsForGetUserRelations){
        
        if(servsForGetUserRelations.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForGetUserRelations.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void getUserRelations(
    final SSServPar                    servPar,
    final List<String>                 allUsers,
    final Map<String, List<SSUri>>     userRelations) throws SSErr{
    
    try{
      for(SSGetUserRelationsI serv : servsForGetUserRelations){
        serv.getUserRelations(servPar, allUsers, userRelations);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
