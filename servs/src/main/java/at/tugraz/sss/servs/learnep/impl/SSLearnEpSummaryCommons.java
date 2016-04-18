 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.learnep.impl;

import at.tugraz.sss.servs.activity.datatype.SSActivity;
import at.tugraz.sss.servs.learnep.datatype.SSLearnEp;
import at.tugraz.sss.servs.learnep.datatype.SSLearnEpDailySummary;
import at.tugraz.sss.servs.learnep.datatype.SSLearnEpDailySummaryCopyLearnEpEntry;
import at.tugraz.sss.servs.learnep.datatype.SSLearnEpDailySummaryLearnEpContentEntry;
import at.tugraz.sss.servs.learnep.datatype.SSLearnEpDailySummaryReminderEntry;
import at.tugraz.sss.servs.learnep.datatype.SSLearnEpDailySummaryShareLearnEpEntry;
import at.tugraz.sss.servs.learnep.datatype.SSLearnEpEntity;
import at.tugraz.sss.servs.user.api.SSUserServerI;
import at.tugraz.sss.servs.user.datatype.SSUsersGetPar;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSEntitiesAccessibleGetPar;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.api.*;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.util.SSDateU;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.user.impl.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SSLearnEpSummaryCommons {

  private final SSEntityServerI entityServ;
  
  public SSLearnEpSummaryCommons(final SSEntityServerI entityServ){
    this.entityServ = entityServ;
  }
  
  public void learnEpContentActivity(
    final List<SSEntity>                     learnEps,
    final SSActivity                         act,
    final Map<String, SSLearnEpDailySummary> summaries) throws SSErr{
    
    try{
      
      SSLearnEpDailySummary                    dailySummary;
      SSLearnEpDailySummaryLearnEpContentEntry summaryEntry;
      SSLearnEp                                learnEp = null;
      
      for(SSEntity entity : act.entities){
        
        switch(entity.type){
          
          case learnEp:{
            
            learnEp = (SSLearnEp) SSStrU.get(learnEps, entity);
            break;
          }
          
          default:{
            continue;
          }
        }
      }
      
      if(learnEp == null){
        return;
      }
      
      for(SSEntity targetUser : learnEp.users){
        
        if(SSStrU.containsKey(summaries, targetUser)){
          dailySummary = summaries.get(SSStrU.toStr(targetUser));
        }else{
          dailySummary = new SSLearnEpDailySummary();
        }
        
        dailySummary.user              = targetUser;
        summaryEntry                   = new SSLearnEpDailySummaryLearnEpContentEntry();
        summaryEntry.originUserLabel   = SSLabel.get(SSStrU.removeEmailHost(act.author.label));
        summaryEntry.targetEntityLabel = learnEp.label;
        summaryEntry.activityType      = act.activityType;
        summaryEntry.learnEp           = learnEp;
        
        dailySummary.userSummaries.add(summaryEntry);
        
        summaries.put(SSStrU.toStr(targetUser), dailySummary);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void shareLearnEpWithUser(
    final SSActivity                         act,
    final Map<String, SSLearnEpDailySummary> summaries) throws SSErr{
    
    try{
      
      SSLearnEpDailySummary                  dailySummary;
      SSLearnEpDailySummaryShareLearnEpEntry summaryEntry;
      
      for(SSEntity targetUser : act.users){
        
        if(SSStrU.containsKey(summaries, targetUser)){
          dailySummary = summaries.get(SSStrU.toStr(targetUser));
        }else{
          dailySummary = new SSLearnEpDailySummary();
        }
        
        dailySummary.user              = targetUser;
        summaryEntry                   = new SSLearnEpDailySummaryShareLearnEpEntry();
        summaryEntry.originUserLabel   = SSLabel.get(SSStrU.removeEmailHost(act.author.label));
        summaryEntry.targetEntityLabel = act.entity.label;
        
        dailySummary.userSummaries.add(summaryEntry);
        
        summaries.put(SSStrU.toStr(targetUser), dailySummary);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void copyLearnEpForUser(
    final SSActivity                         act,
    final Map<String, SSLearnEpDailySummary> summaries) throws SSErr{
    
    try{
      
      try{
      
      SSLearnEpDailySummary                  dailySummary;
      SSLearnEpDailySummaryCopyLearnEpEntry  summaryEntry;
      
      for(SSEntity targetUser : act.users){
        
        if(SSStrU.containsKey(summaries, targetUser)){
          dailySummary = summaries.get(SSStrU.toStr(targetUser));
        }else{
          dailySummary = new SSLearnEpDailySummary();
        }
        
        dailySummary.user              = targetUser;
        summaryEntry                   = new SSLearnEpDailySummaryCopyLearnEpEntry();
        summaryEntry.originUserLabel   = SSLabel.get(SSStrU.removeEmailHost(act.author.label));
        summaryEntry.targetEntityLabel = act.entity.label;
        
        dailySummary.userSummaries.add(summaryEntry);
        
        summaries.put(SSStrU.toStr(targetUser), dailySummary);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void learnEpEntityReminders(
    final SSServPar                          servPar,
    final SSLearnEpSQL                       sql,
    final Map<String, SSLearnEpDailySummary> summaries) throws SSErr{
    
    try{
      
      final SSUserServerI               userServ     = new SSUserImpl();
      
      final List<SSEntity> allUsers =
        userServ.usersGet(
          new SSUsersGetPar(
            servPar,
            servPar.user,
            null, //users,
            null, //emails,
            false)); //invokeEntityHandlers
      
      final List<SSEntityE>                    types                        = new ArrayList<>();
      final List<SSUri>                        authors                      = new ArrayList<>();
      final long                               dateNow                      = new java.util.Date().getTime();
      final long                               dateOneMonthBack             = dateNow - SSDateU.dayInMilliSeconds * 30;
      final long                               dateThreeWeeksBack           = dateNow - SSDateU.dayInMilliSeconds * 21;
      final long                               dateTwoWeeksBack             = dateNow - SSDateU.dayInMilliSeconds * 14;
      final long                               dateOneWeekBack              = dateNow - SSDateU.dayInMilliSeconds * 7;
      SSEntitiesAccessibleGetPar               entitiesAccessibleGetPar;
      SSLearnEpDailySummary                    dailySummary;
      SSLearnEpDailySummaryReminderEntry       summaryEntry;
      
      types.add(SSEntityE.evernoteNote);
      types.add(SSEntityE.evernoteNotebook);
      types.add(SSEntityE.evernoteResource);
      types.add(SSEntityE.uploadedFile);
      types.add(SSEntityE.placeholder);
      types.add(SSEntityE.link);
      
      for(SSEntity user : allUsers){
        
        if(SSStrU.isEqual(user, SSConf.systemUserUri)){
          continue;
        }
        
        if(SSStrU.containsKey(summaries, user)){
          dailySummary = summaries.get(SSStrU.toStr(user));
        }else{
          dailySummary = new SSLearnEpDailySummary();
        }
        
        authors.clear();
        authors.add(user.id);
        
        dailySummary.user              = user;
        summaryEntry                   = new SSLearnEpDailySummaryReminderEntry();
        summaryEntry.originUserLabel   = SSLabel.get(SSStrU.removeEmailHost(user.label));
        
        entitiesAccessibleGetPar =
          new SSEntitiesAccessibleGetPar(
            servPar,
            user.id,
            types,
            authors,
            dateOneWeekBack, //startTime
            dateOneWeekBack + SSDateU.dayInMilliSeconds, //endTime,
            null, //descPar,
            false); //withUserRestriction
        
        entitiesAccessibleGetPar.pageSize = Integer.MAX_VALUE;
        
        summaryEntry.oneWeekAgoEntities.addAll(entityServ.entitiesAccessibleGet(entitiesAccessibleGetPar).entities);

        entitiesAccessibleGetPar =
          new SSEntitiesAccessibleGetPar(
            servPar,
            user.id,
            types,
            authors,
            dateTwoWeeksBack, //startTime
            dateTwoWeeksBack + SSDateU.dayInMilliSeconds, //endTime,
            null, //descPar,
            false); //withUserRestriction

        entitiesAccessibleGetPar.pageSize = Integer.MAX_VALUE;
        
        summaryEntry.twoWeeksAgoEntities.addAll(entityServ.entitiesAccessibleGet(entitiesAccessibleGetPar).entities);
        
        entitiesAccessibleGetPar =
          new SSEntitiesAccessibleGetPar(
            servPar,
            user.id,
            types,
            authors,
            dateThreeWeeksBack, //startTime
            dateThreeWeeksBack + SSDateU.dayInMilliSeconds, //endTime,
            null, //descPar,
            false); //withUserRestriction

        entitiesAccessibleGetPar.pageSize = Integer.MAX_VALUE;
        
        summaryEntry.threeWeeksAgoEntities.addAll(entityServ.entitiesAccessibleGet(entitiesAccessibleGetPar).entities);
        
        entitiesAccessibleGetPar =
          new SSEntitiesAccessibleGetPar(
            servPar,
            user.id,
            types,
            authors,
            dateOneMonthBack, //startTime
            dateOneMonthBack + SSDateU.dayInMilliSeconds, //endTime,
            null, //descPar,
            false); //withUserRestriction

        entitiesAccessibleGetPar.pageSize = Integer.MAX_VALUE;
        
        summaryEntry.fourWeeksAgoEntities.addAll(entityServ.entitiesAccessibleGet(entitiesAccessibleGetPar).entities);
        
        for(SSUri learnEpURI : sql.getLearnEpURIs(servPar, user.id)){
          
          for(SSUri learnEpVersionURI : sql.getLearnEpVersionURIs(servPar, learnEpURI)){
            
            for(SSEntity learnEpEntity : sql.getLearnEpVersionEntities(servPar, learnEpVersionURI)){
              
              SSStrU.remove(summaryEntry.oneWeekAgoEntities,    ((SSLearnEpEntity) learnEpEntity).entity);
              SSStrU.remove(summaryEntry.twoWeeksAgoEntities,   ((SSLearnEpEntity) learnEpEntity).entity);
              SSStrU.remove(summaryEntry.threeWeeksAgoEntities, ((SSLearnEpEntity) learnEpEntity).entity);
              SSStrU.remove(summaryEntry.fourWeeksAgoEntities,  ((SSLearnEpEntity) learnEpEntity).entity);
            }
          }
        }
        
        dailySummary.userSummaries.add(summaryEntry);
        
        summaries.put(SSStrU.toStr(user), dailySummary);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
