/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package sss.serv.eval.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.SSActivity;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivityGetPar;
import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentGetPar;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSStrU;
import java.util.ArrayList;
import java.util.List;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSEvalLogBNP {
  
  public void log(
    final SSEvalLogPar     par,
    final SSEntity         originUser,
    final SSEntity         targetEntity,
    final List<SSEntity>   targetEntities,
    final List<SSEntity>   targetUsers){
    
    try{
      
      switch(par.type){
        
        case addNotebook: //server
        case addNote: //server
        case addResource: //server
        case copyLearnEpForUser: //server
        case shareLearnEpWithUser:  //server
        case removeLearnEpVersionCircle: //server
        case removeLearnEpVersionEntity: //server
        case addEntityToLearnEpVersion: //server
        case addCircleToLearnEpVersion: //server
        case addEntityToLearnEpCircle: //server
        case removeEntityFromLearnEpCircle: //server
        case removeLearnEpVersionCircleWithEntitites: //server
        case changeLabel:  //client | server
        case changeDescription: //client | server
        case addTag: //client | server
        case clickBit:  //client
        case clickTag:  //client
        case clickLabelRecommendation:   //client
        case clickTagRecommendation: //client
        case clickJumpToDateButton:   //client
        case clickAffectButton: //client
        case clickHelpButton:  //client
        case searchWithKeyword://client
        case readMessage: //client
        case sendMessage: //client
        case setImportance: //client
        case removeTag: //client
        case setFilter: //client
        case removeFilter: //client
        case executeJumpToDateButton: //client
        case requestEditButton:  //client
        case releaseEditButton:{ //client
          
          break;
        }
        
        default: return;
      }
      
      final List<SSEntity>   notSelectedEntities = new ArrayList<>();
      String                 logText             = new String();
      String                 selectBitsMeasure   = SSStrU.empty;
      SSActivity             activity            = null;
      SSCircleE              episodeSpace        = null;
      
      if(targetEntity != null){
        
        switch(targetEntity.type){
          
          case learnEp:
            
            episodeSpace =
              ((SSCircleServerI) SSServReg.getServ(SSCircleServerI.class)).circleMostOpenCircleTypeGet(
                new SSCircleMostOpenCircleTypeGetPar(
                  originUser.id,
                  targetEntity.id,
                  true));
            
            break;
        }
      }
      
      switch(par.type){
        
        case clickBit:{
          
          if(targetEntity == null){
            break;
          }
          
          switch(targetEntity.type){
            
            case activity:{
              
              activity =
                ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activityGet(
                  new SSActivityGetPar(
                    par.user,
                    targetEntity.id, //activity
                    false)); //invokeEntityHandlers
              
              targetEntities.addAll (activity.entities);
              targetUsers.addAll    (activity.users);
              break;
            }
          }
          
          break;
        }
      }
      
      //timestamp;tool context;user label;log type;entity;entity type;entity label;content;tag type;entities' ids;entities' labels;users' labels;episodespace;selected bits measure;not selected entities' ids;not selected entities' labels
      //time stamp
      logText += SSDateU.dateAsLong();
      logText += SSStrU.semiColon;
      
      //tool context
      if(par.toolContext != null){
        logText += par.toolContext;
      }
      
      logText += SSStrU.semiColon;
      
      //user
      if(originUser != null){
        logText += originUser.label;
      }
      
      logText += SSStrU.semiColon;
      
      // log type
      logText += par.type;
      logText += SSStrU.semiColon;
      
      // entity
      if(targetEntity != null){
        logText += targetEntity.id;
      }
      
      logText += SSStrU.semiColon;
      
      // entity type
      if(targetEntity != null){
        logText += targetEntity.type;
      }
      
      logText += SSStrU.semiColon;
      
      // entity label
      if(targetEntity != null){
        logText += targetEntity.label;
      }
      
      logText += SSStrU.semiColon;
      
      // content
      if(par.content != null){
        logText += par.content;
      }else{
        if(activity != null){
          logText += activity.activityType;
        }
      }
      
      logText += SSStrU.semiColon;
      
      // tag type
      switch(par.type){
        
        case addTag:{
          
          if(par.content != null){
            
            if(par.content.startsWith("*")){
              logText += 2;
            }else{
              logText += 1;
            }
          }
          
          break;
        }
      }
      
      logText += SSStrU.semiColon;
      
      // entities' ids
      for(SSEntity entity : targetEntities){
        
        logText += entity.id;
        logText += SSStrU.comma;
        
        if(episodeSpace != null){
          continue;
        }
        
        switch(entity.type){
          
          case learnEp:
            
            episodeSpace =
              ((SSCircleServerI)SSServReg.getServ(SSCircleServerI.class)).circleMostOpenCircleTypeGet(
                new SSCircleMostOpenCircleTypeGetPar(
                  originUser.id,
                  targetEntity.id,
                  true));
            break;
        }
      }
      
      logText += SSStrU.semiColon;
      
      // entities' labels
      for(SSEntity entity : targetEntities){
        logText += entity.label;
        logText += SSStrU.comma;
      }
      
      logText += SSStrU.semiColon;
      
      // users' labels
      for(SSEntity user : targetUsers){
        logText += user.label;
        logText += SSStrU.comma;
      }
      
      logText += SSStrU.semiColon;
      
      // episode space
      if(episodeSpace != null){
        logText += episodeSpace;
      }
      
      logText += SSStrU.semiColon;
      
      switch(par.type){
        
        case copyLearnEpForUser:{
          
          if(
            episodeSpace  == null ||
            targetEntity  == null ||
            targetEntities.isEmpty()){
            break;
          }
          
          try{
            
            final SSLearnEpVersion learnEpVersion =
              ((SSLearnEpServerI)SSServReg.getServ(SSLearnEpServerI.class)).learnEpVersionCurrentGet(
                new SSLearnEpVersionCurrentGetPar(
                  originUser.id, 
                  false, 
                  false));
            
            final Integer itemCount =
              learnEpVersion.learnEpCircles.size() +
              learnEpVersion.learnEpEntities.size();
            
            selectBitsMeasure = targetEntities.size() + SSStrU.slash + itemCount;
            
            for(SSEntity circle : learnEpVersion.learnEpCircles){
              
              if(!SSStrU.contains(targetEntities, circle)){
                notSelectedEntities.add(circle);
              }
            }
            
            for(SSEntity entity : learnEpVersion.learnEpEntities){
              
              if(!SSStrU.contains(targetEntities, entity)){
                notSelectedEntities.add(entity);
              }
            }
          }catch(Exception error){
            SSServErrReg.reset();
            SSLogU.warn("learn ep version couldnt be retrieved");
          }
          
          break;
        }
      }
      
      //selected bits measure
      logText += selectBitsMeasure;
      logText += SSStrU.semiColon;
      
      //not selected entities' ids
      for(SSEntity entity : notSelectedEntities){
        logText += entity.id;
        logText += SSStrU.comma;
      }
      
      logText += SSStrU.semiColon;
      
      //not selected entities' labels
      for(SSEntity entity : notSelectedEntities){
        logText += entity.label;
        logText += SSStrU.comma;
      }
      
      logText += SSStrU.semiColon;
      
      logText = SSStrU.replaceAllLineFeedsWithTextualRepr(logText);
      
      SSLogU.trace(logText, false);
      
    }catch(Exception error){
      SSServErrReg.reset();
      SSLogU.warn("eval logging failed");
    }
  }
}
