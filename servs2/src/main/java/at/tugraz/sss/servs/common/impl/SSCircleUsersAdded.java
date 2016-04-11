/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.sss.servs.common.impl;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.reg.*;
import java.util.*;

public class SSCircleUsersAdded {
  
  private final SSAddAffiliatedEntitiesToCircle addAffiliatedEntitiesToCircle = new SSAddAffiliatedEntitiesToCircle();
  private final SSPushEntitiesToUsers           pushEntitiesToUsers           = new SSPushEntitiesToUsers();
  
  public void circleUsersAdded(
    final SSServPar      servPar,
    final SSUri          user, 
    final SSCircle circle,
    final List<SSUri>    users, 
    final boolean        withUserRestriction) throws SSErr {
    
    try{
      
      if(
        users == null ||
        users.isEmpty()){
        return;
      }
      
      final List<SSEntity>  entitiesToPushToUsers   = new ArrayList<>();
      final List<SSEntity>  addedAffiliatedEntities =
        addAffiliatedEntitiesToCircle.addAffiliatedEntitiesToCircle(
          servPar,
          user,
          circle.id,
          circle.entities,
          new ArrayList<>(),
          withUserRestriction);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entitiesToPushToUsers,
        circle.entities);
      
      SSEntity.addEntitiesDistinctWithoutNull(
        entitiesToPushToUsers,
        addedAffiliatedEntities);
      
      pushEntitiesToUsers.pushEntitiesToUsers(
        servPar,
        new SSPushEntitiesToUsersPar(
          user,
          entitiesToPushToUsers,
          users,
          withUserRestriction));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
