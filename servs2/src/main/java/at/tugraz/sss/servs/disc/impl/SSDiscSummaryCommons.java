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
package at.tugraz.sss.servs.disc.impl;

import at.tugraz.sss.servs.activity.datatype.SSActivity;
import at.tugraz.sss.servs.disc.api.SSDiscServerI;
import at.tugraz.sss.servs.disc.datatype.SSDisc;
import at.tugraz.sss.servs.disc.datatype.SSDiscDailySummary;
import at.tugraz.sss.servs.disc.datatype.SSDiscDailySummaryAddDiscEntryEntry;
import at.tugraz.sss.servs.disc.datatype.SSDiscDailySummaryDiscEntityEntry;
import at.tugraz.sss.servs.disc.datatype.SSDiscGetPar;
import at.tugraz.sss.servs.user.api.SSUserServerI;
import at.tugraz.sss.servs.user.datatype.SSUsersGetPar;
import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.SSLabel;
import at.tugraz.sss.serv.datatype.SSUri;
import at.tugraz.sss.serv.datatype.par.SSServPar;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.reg.SSServReg;
import at.tugraz.sss.serv.util.SSStrU;
import java.util.List;
import java.util.Map;

public class SSDiscSummaryCommons{
  
  public void discEntity(
    final SSServPar                          servPar,
    final SSDiscSQL                          sql,
    final SSActivity                         act,
    final Map<String, SSDiscDailySummary>    summaries) throws SSErr{
    
    try{
      final SSUserServerI userServ = (SSUserServerI) SSServReg.getServ(SSUserServerI.class);
      final SSEntity      disc     = 
        sql.getEntityTest(
          servPar, 
          SSConf.systemUserUri, 
          SSConf.systemUserUri, 
          act.entities.get(0).id, //entity
          false); //withUserRestriction
      
      final List<SSUri>   userURIs = 
        sql.getDiscUserURIs(
          servPar,
          act.entities.get(0).id);

      if(userURIs.isEmpty()){
        return;
      }
      
      final List<SSEntity> users =
        userServ.usersGet(
          new SSUsersGetPar(
            servPar,
            SSConf.systemUserUri,
            userURIs,
            null, //emails
            false)); //invokeEntityHandlers
      
      SSDiscDailySummary                  dailySummary;
      SSDiscDailySummaryDiscEntityEntry   summaryEntry;
      
      for(SSEntity targetUser : users){
        
        if(SSStrU.isEqual(targetUser, act.author.id)){
          continue;
        }
        
        if(SSStrU.containsKey(summaries, targetUser)){
          dailySummary = summaries.get(SSStrU.toStr(targetUser));
        }else{
          dailySummary = new SSDiscDailySummary();
        }
        
        dailySummary.user              = targetUser;
        summaryEntry                   = new SSDiscDailySummaryDiscEntityEntry();
        summaryEntry.originUserLabel   = SSLabel.get(SSStrU.removeEmailHost(act.author.label));
        summaryEntry.targetEntity      = act.entity;
        summaryEntry.discLabel         = disc.label;
        
        dailySummary.userSummaries.add(summaryEntry);
        
        summaries.put(SSStrU.toStr(targetUser), dailySummary);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addDiscEntry(
    final SSDiscServerI                      discServ,
    final SSServPar                          servPar,
    final SSDiscSQL                          sql,
    final SSActivity                         act,
    final Map<String, SSDiscDailySummary>    summaries) throws SSErr{
    
    try{
      final SSUserServerI userServ = (SSUserServerI) SSServReg.getServ(SSUserServerI.class);
      final List<SSUri>   userURIs = 
        sql.getDiscUserURIs(
          servPar,
          act.entity.id);

      if(userURIs.isEmpty()){
        return;
      }
      
      final SSDisc disc =
        discServ.discGet(
          new SSDiscGetPar(
            servPar,
            SSConf.systemUserUri,
            act.entity.id,
            false, //setEntries,
            false, //withUserRestriction,
            false)); //invokeEntityHandlers)
      
      final List<SSEntity> users =
        userServ.usersGet(
          new SSUsersGetPar(
            servPar,
            SSConf.systemUserUri,
            userURIs,
            null, //emails
            false)); //invokeEntityHandlers
      
      SSDiscDailySummary                  dailySummary;
      SSDiscDailySummaryAddDiscEntryEntry summaryEntry;
      
      for(SSEntity targetUser : users){
        
        if(SSStrU.isEqual(targetUser, act.author.id)){
          continue;
        }
        
        if(SSStrU.containsKey(summaries, targetUser)){
          dailySummary = summaries.get(SSStrU.toStr(targetUser));
        }else{
          dailySummary = new SSDiscDailySummary();
        }
        
        dailySummary.user              = targetUser;
        summaryEntry                   = new SSDiscDailySummaryAddDiscEntryEntry();
        summaryEntry.originUserLabel   = SSLabel.get(SSStrU.removeEmailHost(act.author.label));
        summaryEntry.targetEntity      = act.entity;
        summaryEntry.discTargets.addAll(disc.targets);
        
        dailySummary.userSummaries.add(summaryEntry);
        
        summaries.put(SSStrU.toStr(targetUser), dailySummary);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
