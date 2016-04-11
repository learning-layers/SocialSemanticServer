/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.sss.adapter.socket;

import at.tugraz.sss.serv.errreg.SSServErrReg;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.impl.api.*;
import java.lang.reflect.*;
import java.util.*;

public class SSClientServs {
  
  protected static final Map<SSServImplA, Class>  classForServ  = new HashMap<>();
  protected static final Map<String, SSServImplA> servForOp     = new HashMap<>();
  
  public void regServ(
    final SSServImplA serv,
    final Class       clientServClass) throws SSErr{
    
    try{
      
      synchronized(servForOp){
        
        final Method[] methods = clientServClass.getMethods();
        
        for(Method method : methods){
          servForOp.put(method.getName(), serv);
        }
      }
      
      synchronized(classForServ){
        classForServ.put(serv, clientServClass);
      }
       
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public SSServImplA getServ(
    final String op) throws SSErr{
    
    try{
      
      return servForOp.get(op);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public Class getClass(final SSServImplA serv) throws SSErr{
    
    try{
      
      return classForServ.get(serv);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

//  public void regServ(
//    final SSServContainerI servContainer) throws SSErr{
//    
//    try{
//      
//      synchronized(servsForClientI){
//        
//        if(servsForClientI.containsKey(servContainer.servImplClientInteraceClass)){
//          throw new Exception("serv server interface already registered");
//        }
//        
//        if(servContainer.servImplClientInteraceClass == null){
//          SSLogU.warn("service container has no service client interface", null);
//          return;
//        }
//        
//        servsForClientI.put(servContainer.servImplClientInteraceClass, servContainer);
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
  
//  public SSServImplA getClientServ(final Class clientServClass) throws SSErr{
//    
//    try{
//      
//      final SSServContainerI serv = servsForClientI.get(clientServClass);
//      
//      if(serv == null){
//        throw SSErr.get(SSErrE.servInvalid);
//      }
//      
//      return serv.getServImpl();
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }