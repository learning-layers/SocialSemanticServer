/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.sss.servs.common.impl;

import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.common.datatype.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.conf.SSConfA;
import at.tugraz.sss.servs.common.api.*;
import java.util.*;

public class SSAddAffiliatedEntitiesToCircle {
  
  protected static final List<SSAddAffiliatedEntitiesToCircleI> servsForAddAffiliatedEntitiesToCircle = new ArrayList<>();
  
  public void regServ(
    final SSAddAffiliatedEntitiesToCircleI servContainer, 
    final SSConfA                          conf) throws SSErr{
    
    try{
      
      if(!conf.use){
        return;
      }
      
      synchronized(servsForAddAffiliatedEntitiesToCircle){
        
        if(servsForAddAffiliatedEntitiesToCircle.contains(servContainer)){
          throw SSErr.get(SSErrE.servAlreadyRegistered);
        }
        
        servsForAddAffiliatedEntitiesToCircle.add(servContainer);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSEntity> addAffiliatedEntitiesToCircle(
    final SSServPar         servPar,
    final SSUri             user,
    final SSUri             circle,
    final List<SSEntity>    entities,
    final List<SSUri>       recursiveEntities,
    final boolean           withUserRestriction) throws SSErr{
    
    try{
      final List<SSEntity> addedAffiliatedEntities = new ArrayList<>();
      
      if(
        entities == null || 
        entities.isEmpty()){
        return addedAffiliatedEntities;
      }
      
      final SSAddAffiliatedEntitiesToCirclePar par =
        new SSAddAffiliatedEntitiesToCirclePar(
          user,
          circle,
          entities,
          recursiveEntities,
          withUserRestriction);
      
      for(SSAddAffiliatedEntitiesToCircleI serv : servsForAddAffiliatedEntitiesToCircle){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          addedAffiliatedEntities,
          serv.addAffiliatedEntitiesToCircle(servPar, par));
      }
      
      return addedAffiliatedEntities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
