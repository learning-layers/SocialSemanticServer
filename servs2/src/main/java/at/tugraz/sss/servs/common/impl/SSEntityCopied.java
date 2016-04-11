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

public class SSEntityCopied {
  
  protected static final List<SSServContainerI> servsForEntityCopied = new ArrayList<>();
  
  public void regServ(
    final SSServContainerI servContainer) throws SSErr{
    
    try{
      
      if(!servContainer.conf.use){
        return;
      }
      
      synchronized(servsForEntityCopied){
        
        if(servsForEntityCopied.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForEntityCopied.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  public void entityCopied(
    final SSServPar         servPar,
    final SSEntityCopiedPar entityCopiedPar) throws SSErr{
    
    try{
      
      for(SSServContainerI entityHandler : servsForEntityCopied){
        ((SSEntityCopiedI) entityHandler.getServImpl()).entityCopied(servPar, entityCopiedPar);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}