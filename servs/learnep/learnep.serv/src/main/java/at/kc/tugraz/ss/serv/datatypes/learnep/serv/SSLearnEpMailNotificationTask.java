 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.serv.datatypes.learnep.serv;

import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.serv.datatypes.learnep.api.*;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpDailySummary;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpDailySummaryCopyLearnEpEntry;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpDailySummaryEntry;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpDailySummaryLearnEpContentEntry;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpDailySummaryReminderEntry;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpDailySummaryShareLearnEpEntry;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpDailySummaryGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpDailySummaryGetRet;
import at.kc.tugraz.ss.service.disc.api.SSDiscServerI;
import at.kc.tugraz.ss.service.disc.datatypes.SSDiscDailySummary;
import at.kc.tugraz.ss.service.disc.datatypes.SSDiscDailySummaryAddDiscEntryEntry;
import at.kc.tugraz.ss.service.disc.datatypes.SSDiscDailySummaryDiscEntityEntry;
import at.kc.tugraz.ss.service.disc.datatypes.SSDiscDailySummaryEntry;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscDailySummaryGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscDailySummaryGetRet;
import at.kc.tugraz.ss.service.user.api.SSUserServerI;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUsersGetPar;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSUri;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.util.SSStrU;
import at.tugraz.sss.servs.common.impl.serv.SSServCommons;
import at.tugraz.sss.servs.mail.SSMailServerI;
import at.tugraz.sss.servs.mail.datatype.par.SSMailSendPar;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SSLearnEpMailNotificationTask implements Runnable{
  
  private final SSServCommons servCommons = new SSServCommons();
  
  @Override
  public void run() {
    
    Connection sqlCon = null;
    
    try{
      final SSMailServerI mailServ = servCommons.getMailServ();
      
      if(mailServ == null){
        return;
      }
      
      final SSDiscServerI                      discServ                   = ((SSDiscServerI)    SSServReg.getServ(SSDiscServerI.class));
      final SSLearnEpServerI                   learnEpServ                = ((SSLearnEpServerI) SSServReg.getServ(SSLearnEpServerI.class));
      final SSUserServerI                      userServ                   = ((SSUserServerI)    SSServReg.getServ(SSUserServerI.class));
      final SSServPar                          servPar                    = new SSServPar(null);
      final List<String>                       usersInSummaries           = new ArrayList<>();
      final Map<String, Set<SSActivityE>>      learnEpsActTypes           = new HashMap<>();
      final Map<String, String>                learnEpLabels              = new HashMap<>();
      final Map<String, Integer>               learnEpsActCount           = new HashMap<>();
      final Map<String, Set<String>>           learnEpsActUsers           = new HashMap<>();
      final SSLearnEpDailySummaryGetRet        dailyLearnEpSummary;
      final SSDiscDailySummaryGetRet           dailyDiscSummary;
      String                                   shareLearnEpMailSummary;
      String                                   copyLearnEpMailSummary;
      String                                   learnEpActivitySummary;
      String                                   reminderSummary;
      String                                   addDiscEntryMailSummary;
      String                                   discEntityMailSummary;
      boolean                                  sharingExists;
      boolean                                  copyingExists;
      boolean                                  reminderExists;
      boolean                                  learnEpActivityExists;
      boolean                                  addDiscEntryExists;
      boolean                                  discEntityExists;
      String                                   mailSummary;
      String                                   userEmail;
      SSEntity                                 user;
      SSLearnEpDailySummary                    learnEpDailySummary;
      SSDiscDailySummary                       discDailySummary;
      SSLearnEpDailySummaryLearnEpContentEntry learnEpActSummaryEntry;
      SSLearnEpDailySummaryReminderEntry       reminderSummaryEntry;
      String                                   formattedActTypes;
      String                                   formattedActUsers;
      
      sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      
      servPar.sqlCon = sqlCon;
      
      dailyLearnEpSummary =
        learnEpServ.learnEpDailySummaryGet(
          new SSLearnEpDailySummaryGetPar(
            servPar,
            SSConf.systemUserUri,
            new java.util.Date().getTime() - SSDateU.dayInMilliSeconds * 1));
      
      dailyDiscSummary = 
        discServ.discDailySummaryGet(
          new SSDiscDailySummaryGetPar(
            servPar,
            SSConf.systemUserUri,
            new java.util.Date().getTime() - SSDateU.dayInMilliSeconds * 1));
      
      SSStrU.addDistinctNotNull(usersInSummaries, dailyLearnEpSummary.summaries.keySet());
      SSStrU.addDistinctNotNull(usersInSummaries, dailyDiscSummary.summaries.keySet());
      
      for(String userKey : usersInSummaries){
        
        mailSummary   = SSStrU.empty;
        user          =
          userServ.usersGet(
            new SSUsersGetPar(
              servPar,
              SSConf.systemUserUri,
              SSUri.asListNotNull(SSUri.get(userKey)), //users
              null, //emails
              false)).get(0); //invokeEntityHandlers
        
        userEmail             = ((SSUser) user).email;
        sharingExists         = false;
        copyingExists         = false;
        learnEpActivityExists = false;
        learnEpsActTypes.clear();
        learnEpLabels.clear();
        learnEpsActCount.clear();
        learnEpsActUsers.clear();
        reminderExists        = false;
        discEntityExists      = false;
        addDiscEntryExists    = false;
        
        learnEpDailySummary = dailyLearnEpSummary.summaries.get(userKey);
        
        if(learnEpDailySummary != null){
          
          shareLearnEpMailSummary  = "New Shared Learning Episodes"  + SSStrU.backslashRBackslashN;
          shareLearnEpMailSummary += "============================"  + SSStrU.backslashRBackslashN;
          shareLearnEpMailSummary += SSStrU.backslashRBackslashN;
          
          copyLearnEpMailSummary  = "New Copied Learning Episodes"  + SSStrU.backslashRBackslashN;
          copyLearnEpMailSummary += "============================"  + SSStrU.backslashRBackslashN;
          copyLearnEpMailSummary += SSStrU.backslashRBackslashN;
          
          learnEpActivitySummary  = "Learning Episode Activities"  + SSStrU.backslashRBackslashN;
          learnEpActivitySummary += "==========================="  + SSStrU.backslashRBackslashN;
          learnEpActivitySummary += SSStrU.backslashRBackslashN;
          
          reminderSummary  = "Bits Not Used In Episodes"  + SSStrU.backslashRBackslashN;
          reminderSummary += "========================="  + SSStrU.backslashRBackslashN;
          reminderSummary += SSStrU.backslashRBackslashN;
          
          for(SSLearnEpDailySummaryEntry summary : learnEpDailySummary.userSummaries){
            
            if(summary instanceof SSLearnEpDailySummaryReminderEntry){
              
              reminderSummaryEntry = (SSLearnEpDailySummaryReminderEntry) summary;
              
              if(!reminderSummaryEntry.oneWeekAgoEntities.isEmpty()){
                
                reminderSummary += "One Week Ago" + SSStrU.backslashRBackslashN;
                reminderSummary += "------------" + SSStrU.backslashRBackslashN;
                
                for(SSEntity entity : reminderSummaryEntry.oneWeekAgoEntities){
                  reminderSummary += entity.label + " (" + entity.type + ")" + SSStrU.backslashRBackslashN;
                }
                
                reminderExists   = true;
              }
              
              if(!reminderSummaryEntry.twoWeeksAgoEntities.isEmpty()){
                
                if(reminderExists){
                  reminderSummary +=  SSStrU.backslashRBackslashN;
                }
                
                reminderSummary += "Two Weeks Ago" + SSStrU.backslashRBackslashN;
                reminderSummary += "-------------" + SSStrU.backslashRBackslashN;
                
                for(SSEntity entity : reminderSummaryEntry.twoWeeksAgoEntities){
                  reminderSummary += entity.label + " (" + entity.type + ")" + SSStrU.backslashRBackslashN;
                }
                
                reminderExists   = true;
              }
              
              if(!reminderSummaryEntry.threeWeeksAgoEntities.isEmpty()){
                
                if(reminderExists){
                  reminderSummary +=  SSStrU.backslashRBackslashN;
                }
                
                reminderSummary += "Three Weeks Ago" + SSStrU.backslashRBackslashN;
                reminderSummary += "---------------" + SSStrU.backslashRBackslashN;
                
                for(SSEntity entity : reminderSummaryEntry.threeWeeksAgoEntities){
                  reminderSummary += entity.label + " (" + entity.type + ")" + SSStrU.backslashRBackslashN;
                }
                
                reminderExists   = true;
              }
              
              if(!reminderSummaryEntry.fourWeeksAgoEntities.isEmpty()){
                
                if(reminderExists){
                  reminderSummary +=  SSStrU.backslashRBackslashN;
                }
                
                reminderSummary += "Four Weeks Ago" + SSStrU.backslashRBackslashN;
                reminderSummary += "--------------" + SSStrU.backslashRBackslashN;
                
                for(SSEntity entity : reminderSummaryEntry.fourWeeksAgoEntities){
                  reminderSummary += entity.label + " (" + entity.type + ")" + SSStrU.backslashRBackslashN;
                }
                
                reminderExists   = true;
              }
              
              continue;
            }
            
            if(SSStrU.isEqual(summary.originUserLabel, SSStrU.removeEmailHost(userEmail))){
              continue;
            }
            
            if(summary instanceof SSLearnEpDailySummaryShareLearnEpEntry){
              
              shareLearnEpMailSummary += "* " + summary.originUserLabel + " shared \"" + summary.targetEntityLabel + "\" with me" + SSStrU.backslashRBackslashN;
              sharingExists            = true;
              continue;
            }
            
            if(summary instanceof SSLearnEpDailySummaryCopyLearnEpEntry){
              
              copyLearnEpMailSummary += "* " + summary.originUserLabel + " copied \"" + summary.targetEntityLabel + "\" for me" + SSStrU.backslashRBackslashN;
              copyingExists           = true;
              continue;
            }
            
            if(summary instanceof SSLearnEpDailySummaryLearnEpContentEntry){
             
              learnEpActSummaryEntry = (SSLearnEpDailySummaryLearnEpContentEntry) summary;
              
              if(!SSStrU.containsKey(learnEpsActTypes, learnEpActSummaryEntry.learnEp)){
                learnEpsActTypes.put(SSStrU.toStr(learnEpActSummaryEntry.learnEp), new HashSet<>());
              }
              
              if(!SSStrU.containsKey(learnEpsActCount, learnEpActSummaryEntry.learnEp)){
                learnEpsActCount.put(SSStrU.toStr(learnEpActSummaryEntry.learnEp), 0);
              }
              
              if(!SSStrU.containsKey(learnEpsActUsers, learnEpActSummaryEntry.learnEp)){
                learnEpsActUsers.put(SSStrU.toStr(learnEpActSummaryEntry.learnEp), new HashSet<>());
              }
              
              learnEpsActTypes.get(SSStrU.toStr(learnEpActSummaryEntry.learnEp)).add(learnEpActSummaryEntry.activityType);
              
              learnEpLabels.put(SSStrU.toStr(learnEpActSummaryEntry.learnEp), SSStrU.toStr(learnEpActSummaryEntry.learnEp.label));
              
              learnEpsActCount.put(SSStrU.toStr(learnEpActSummaryEntry.learnEp), learnEpsActCount.get(SSStrU.toStr(learnEpActSummaryEntry.learnEp)) + 1);
              
              learnEpsActUsers.get(SSStrU.toStr(learnEpActSummaryEntry.learnEp)).add(learnEpActSummaryEntry.originUserLabel.toString());
              
              learnEpActivityExists  = true;
              continue;
            }
          }
          
          if(sharingExists){
            mailSummary += shareLearnEpMailSummary + SSStrU.backslashRBackslashN;
          }
          
          if(copyingExists){
            mailSummary += copyLearnEpMailSummary + SSStrU.backslashRBackslashN;
          }
          
          if(learnEpActivityExists){
            
            mailSummary += learnEpActivitySummary;
            
            for(Map.Entry<String, Set<SSActivityE>> learnEpActTypes : learnEpsActTypes.entrySet()){
              
              formattedActTypes = learnEpActTypes.getValue().toString();
              formattedActTypes = SSStrU.replaceAll(formattedActTypes, "[", "(");
              formattedActTypes = SSStrU.replaceAll(formattedActTypes, "]", ")");
              formattedActTypes = SSStrU.replaceAll(formattedActTypes, SSActivityE.removeLearnEpVersionCircleWithEntitites.toString(), "removed circle with bits");
              formattedActTypes = SSStrU.replaceAll(formattedActTypes, SSActivityE.removeLearnEpVersionCircle.toString(), "removed circle");
              formattedActTypes = SSStrU.replaceAll(formattedActTypes, SSActivityE.addCircleToLearnEpVersion.toString(), "added circle");
              formattedActTypes = SSStrU.replaceAll(formattedActTypes, SSActivityE.addEntityToLearnEpCircle.toString(), "added bit to circle");
              formattedActTypes = SSStrU.replaceAll(formattedActTypes, SSActivityE.addEntityToLearnEpVersion.toString(), "added bit");
              formattedActTypes = SSStrU.replaceAll(formattedActTypes, SSActivityE.changeLearnEpVersionCircleLabel.toString(), "renamed circle");
              formattedActTypes = SSStrU.replaceAll(formattedActTypes, SSActivityE.removeLearnEpVersionEntity.toString(), "remove bit");
              
              formattedActUsers = learnEpsActUsers.get(learnEpActTypes.getKey()).toString();
              formattedActUsers = SSStrU.replaceAll(formattedActUsers, "[", "");
              formattedActUsers = SSStrU.replaceAll(formattedActUsers, "]", "");
              
              mailSummary += 
                "\"" 
                + learnEpLabels.get(learnEpActTypes.getKey()) 
                + "\""
                + ": " 
                + learnEpsActCount.get(learnEpActTypes.getKey())
                + " activities "
                + formattedActTypes 
                + " by "
                + formattedActUsers
                + SSStrU.backslashRBackslashN;
            }
            
            mailSummary += SSStrU.backslashRBackslashN;
          }
          
          if(reminderExists){
            mailSummary += reminderSummary + SSStrU.backslashRBackslashN;
          }
        }
        
        discDailySummary = dailyDiscSummary.summaries.get(userKey);
        
        if(discDailySummary != null){
        
          discEntityMailSummary  = "New Discussions" + SSStrU.backslashRBackslashN;
          discEntityMailSummary += "===============" + SSStrU.backslashRBackslashN;
          discEntityMailSummary += SSStrU.backslashRBackslashN;
          
          addDiscEntryMailSummary  = "New Discussions Answers" + SSStrU.backslashRBackslashN;
          addDiscEntryMailSummary += "=======================" + SSStrU.backslashRBackslashN;
          addDiscEntryMailSummary += SSStrU.backslashRBackslashN;
          
          for(SSDiscDailySummaryEntry summary : discDailySummary.userSummaries){
            
            if(SSStrU.isEqual(summary.originUserLabel, SSStrU.removeEmailHost(userEmail))){
              continue;
            }
            
            if(summary instanceof SSDiscDailySummaryDiscEntityEntry){
              
              switch(((SSDiscDailySummaryDiscEntityEntry) summary).targetEntity.type){
                
                case learnEp:{
                  discEntityMailSummary += "* " + summary.originUserLabel + " created \"" + ((SSDiscDailySummaryDiscEntityEntry) summary).discLabel + "\" for episode \"" + summary.targetEntity.label + "\"" + SSStrU.backslashRBackslashN;
                  discEntityExists       = true;
                  break;
                }
                
                default:{
                  continue;
                }
              }
            }
            
            if(summary instanceof SSDiscDailySummaryAddDiscEntryEntry){
              
              for(SSEntity discTarget : ((SSDiscDailySummaryAddDiscEntryEntry) summary).discTargets){
                
                switch(discTarget.type){
                  
                  case learnEp:{
                    addDiscEntryMailSummary += "* " + summary.originUserLabel + " added a new answer to \"" + summary.targetEntity.label + "\" for episode \"" + discTarget.label + "\"" + SSStrU.backslashRBackslashN;
                    addDiscEntryExists       = true;
                    break;
                  }
                  
                  default:{
                    continue;
                  }
                }
              }
            }
          }
          
           if(discEntityExists){
            mailSummary += discEntityMailSummary + SSStrU.backslashRBackslashN;
          }
           
          if(addDiscEntryExists){
            mailSummary += addDiscEntryMailSummary + SSStrU.backslashRBackslashN;
          }
        }
        
        SSLogU.info(userEmail + ":");
        SSLogU.info(mailSummary);
        
        mailServ.mailSend(
          new SSMailSendPar(
            servPar,
            SSConf.systemUserUri,
            "dtheiler@know-center.at", //fromEmail,
            userEmail, //toEmail,
            "Bits and Pieces Daily Update", //subject,
            mailSummary, //content,
            true, //withUserRestriction,
            true)); //shouldCommit
      }
      
    }catch(Exception error){
      SSLogU.err(error);
    }finally{
      
      if(sqlCon != null){
        
        try{
          sqlCon.close();
        }catch (SQLException error) {
          SSLogU.err(error);
        }
      }
    }
  }
}