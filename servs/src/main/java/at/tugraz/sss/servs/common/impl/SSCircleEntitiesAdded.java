/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.sss.servs.common.impl;

import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.common.datatype.SSPushEntitiesToUsersPar;
import at.tugraz.sss.servs.entity.datatype.SSCircle;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import java.util.*;

public class SSCircleEntitiesAdded {
 
  private final SSAddAffiliatedEntitiesToCircle addAffiliatedEntitiesToCircle = new SSAddAffiliatedEntitiesToCircle();
  private final SSPushEntitiesToUsers           pushEntitiesToUsers           = new SSPushEntitiesToUsers();
  
  public void circleEntitiesAdded(
    final SSServPar      servPar,
    final SSUri          user, 
    final SSCircle       circle,
    final List<SSEntity> entities,
    final boolean        withUserRestriction) throws SSErr{
    
    try{
      
      if(
        entities == null ||
        entities.isEmpty()){
        return;
      }
      
      final List<SSEntity>  entitiesToPushToUsers   = new ArrayList<>();
      final List<SSEntity>  addedAffiliatedEntities =
        addAffiliatedEntitiesToCircle.addAffiliatedEntitiesToCircle(
          servPar,
          user,
          circle.id,
          entities,
          new ArrayList<>(),
          withUserRestriction);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entitiesToPushToUsers,
        entities);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entitiesToPushToUsers,
        addedAffiliatedEntities);
      
      pushEntitiesToUsers.pushEntitiesToUsers(
        servPar,
        new SSPushEntitiesToUsersPar(
          user,
          entitiesToPushToUsers,
          SSUri.getDistinctNotNullFromEntities(circle.users),
          withUserRestriction));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
