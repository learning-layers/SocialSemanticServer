/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.sss.servs.common.impl;

import at.tugraz.sss.serv.container.api.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.reg.*;
import java.util.*;

public class SSGetParentEntities{
  
  protected static final List<SSServContainerI> servsForGetParentEntities = new ArrayList<>();    
  
  public void regServ(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsForGetParentEntities){
        
        if(servsForGetParentEntities.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForGetParentEntities.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
