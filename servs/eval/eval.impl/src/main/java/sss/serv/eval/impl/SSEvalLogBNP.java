/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
* For a list of contributors see the AUTHORS file at the top-level directory of this distribution.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package sss.serv.eval.impl;

import at.kc.tugraz.ss.activity.api.SSActivityServerI;
import at.kc.tugraz.ss.activity.datatypes.SSActivity;
import at.kc.tugraz.ss.activity.datatypes.par.SSActivitiesGetPar;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.serv.datatype.par.SSCircleTypesGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.SSLearnEpServerI;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpVersionCurrentGetPar;
import at.kc.tugraz.ss.service.user.datatypes.*;
import at.tugraz.sss.serv.datatype.enums.SSCircleE;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.*;
import java.util.ArrayList;
import java.util.List;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSEvalLogBNP {
  
  public void log(
    final SSEvalLogPar     par,
    final SSEntityServerI  entityServ,
    final SSUser           originUser,
    final SSEntity         targetEntity,
    final List<SSEntity>   targetEntities,
    final List<SSEntity>   targetUsers){
    
    try{
      
      switch(par.type){
        
        //circle
        case createCircle:
        case addCircleEntities:
        case addCircleUsers:
        case removeCircle:
        case removeCircleUsers:
        case removeCircleEntities:{
          
          break;
        }
        
        //evernote & mail import
        case addNotebook:
        case addNote:
        case addResource:{
        
          break;
        }
        
        //organize area
        case addLearnEpVersionEntityFromRecommendedEntities:
        case removeLearnEpVersionCircle: 
        case removeLearnEpVersionEntity: 
        case addEntityToLearnEpVersion: 
        case addCircleToLearnEpVersion: 
        case addEntityToLearnEpCircle: 
        case removeEntityFromLearnEpCircle: 
        case removeLearnEpVersionCircleWithEntitites:
        case requestEditButton:  
        case clickLabelRecommendation:
        case releaseEditButton:{ 
        
          break;
        }
          
        //search tab
        case searchWithKeyword:{
          
          break;
        }
        
        //bit tab
        case clickTagRecommendation:
        case setImportance:{
          
          break;
        }

        //notification tab
        case setFilter:
        case removeFilter:{
          
          break;
        }
        
        //timeline
        case createPlaceholder:
        case clickJumpToDateButton:
        case executeJumpToDateButton:{
          
          break;
        }
        
        //menu bar
        case createLearnEp:
        case removeLearnEp:
        case clickHelpButton:
        case clickAffectButton:{
          
          break;
        }
      
        //episode tab
        case shareEntityWithUsers:
        case shareEntityWithCircles:
        case copyLearnEpForUser:{
         
          break;
        }

        //discussions
        case createDisc:
        case discussEntity: 
        case addDiscEntry: 
        case attachEntities: 
        case removeEntities:{
          
          break;
        }
        
        //global
        case clickTag:
        case clickBit:
        case likeEntity:
        case dislikeEntity:
        case downloadEntity:
        case addTag:
        case removeTag:
        case changeLabel:
        case changeDescription:{

          break;
        }
        
        //open, start and works events
        case openBitsAndPieces:
        case openDiscussionTool: 
        case openLivingDocuments: 
        case closeDiscussionTool: 
        case startDiscussionTool: 
        case startBitsAndPieces: 
        case worksInBitsAndPieces: 
        case worksInDiscussionTool:{
          
          break;
        }
        
        default: return;
      }
      
      final List<SSEntity>   notSelectedEntities = new ArrayList<>();
      final List<SSEntity>   activities          = new ArrayList<>();
      String                 logText             = new String();
      String                 selectBitsMeasure   = SSStrU.empty;
      final List<SSCircleE>  episodeSpaces       = new ArrayList<>();
      SSCircle               circle          = null;
      
      if(targetEntity != null){
        
        switch(targetEntity.type){
          
          case learnEp:{
            
            episodeSpaces.addAll(
              entityServ.circleTypesGet(
                new SSCircleTypesGetPar(
                  par,
                  originUser.id,
                  targetEntity.id,
                  true)));
            
            break;
          }
          
          case circle:{
            
            circle =
              entityServ.circleGet(
                new SSCircleGetPar(
                  par,
                  originUser.id, //user
                  targetEntity.id, //circle
                  null, //entityTypesToIncludeOnly
                  false,  //setTags
                  null,  //tagSpace
                  false,  //setEntities
                  false,  //setUsers
                  false,  //withUserRestriction
                  false)); //invokeEntityHandlers
          }
        }
      }
      
      switch(par.type){
        
        case clickBit:{
          
          if(targetEntity == null){
            break;
          }
          
          switch(targetEntity.type){
            
            case activity:{
              
              activities.addAll(
                ((SSActivityServerI) SSServReg.getServ(SSActivityServerI.class)).activitiesGet(
                  new SSActivitiesGetPar(
                    par, 
                    par.user, 
                    SSUri.asListNotNull(targetEntity.id),
                    null, //types, 
                    null, //users, 
                    null, //entities, 
                    null, //circles, 
                    null, //startTime,
                    null, //endTime, 
                    false, //includeOnlyLastActivities, 
                    false, //withUserRestriction, 
                    false))); //invokeEntityHandlers));
                  
              if(activities.isEmpty()){
                throw new Exception("activity doesnt exist");
              }
              
              targetEntities.addAll (((SSActivity)activities.get(0)).entities);
              targetUsers.addAll    (((SSActivity)activities.get(0)).users);
              break;
            }
          }
          
          break;
        }
      }
      
      //timestamp;tool context;user label;user email;log type;entity;entity type;entity label;content;tag type;entities' ids;entities' labels;
      //users' labels;episodespace;selected bits measure;not selected entities' ids;not selected entities' labels;circle type
      
      //time stamp
      
      if(par.creationTime != null){
        logText += par.creationTime;
      }else{
        logText += SSDateU.dateAsLong();
      }
      
      logText += SSStrU.semiColon;
      
      //tool context
      if(par.toolContext != null){
        logText += par.toolContext;
      }
      
      logText += SSStrU.semiColon;
      
      //user label
      logText += originUser.label;
      logText += SSStrU.semiColon;
      
      //user email
      logText += originUser.email;
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
        if(!activities.isEmpty()){
          logText += ((SSActivity)activities.get(0)).activityType;
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
        
        if(!episodeSpaces.isEmpty()){
          continue;
        }
        
        switch(entity.type){
          
          case learnEp:
            
            episodeSpaces.addAll(
              ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).circleTypesGet(
                new SSCircleTypesGetPar(
                  par, 
                  originUser.id,
                  targetEntity.id,
                  true)));
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
      for(SSCircleE space : episodeSpaces){
        logText += space.toString();
        logText += SSStrU.comma;
      }
      
      logText += SSStrU.semiColon;
      
      switch(par.type){
        
        case copyLearnEpForUser:{
          
          if(
            episodeSpaces.isEmpty() ||
            targetEntity  == null   ||
            targetEntities.isEmpty()){
            break;
          }
          
          try{
            
            final SSLearnEpVersion learnEpVersion =
              ((SSLearnEpServerI)SSServReg.getServ(SSLearnEpServerI.class)).learnEpVersionCurrentGet(
                new SSLearnEpVersionCurrentGetPar(
                  par, 
                  originUser.id, 
                  false, 
                  false));
            
            final Integer itemCount =
              learnEpVersion.learnEpCircles.size() +
              learnEpVersion.learnEpEntities.size();
            
            selectBitsMeasure = targetEntities.size() + SSStrU.slash + itemCount;
            
            for(SSEntity learnEpCircle : learnEpVersion.learnEpCircles){
              
              if(!SSStrU.contains(targetEntities, learnEpCircle)){
                notSelectedEntities.add(learnEpCircle);
              }
            }
            
            for(SSEntity entity : learnEpVersion.learnEpEntities){
              
              if(!SSStrU.contains(targetEntities, entity)){
                notSelectedEntities.add(entity);
              }
            }
          }catch(Exception error){
            SSLogU.warn("learn ep version couldnt be retrieved", error);
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
        logText += entity.id;
        logText += SSStrU.comma;
      }
      
      logText += SSStrU.semiColon;
      
      //circleType
      if(circle != null){
        logText += circle.circleType;
      }else{
        logText += SSStrU.empty;
      }
      
      logText += SSStrU.semiColon;
      
      logText = SSStrU.replaceAllLineFeedsWithTextualRepr(logText);
      
      SSLogU.trace(logText, false);
      
    }catch(Exception error){
      SSLogU.warn("eval logging failed", error);
    }
  }
}
