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
package at.tugraz.sss.servs.learnep.impl;

import at.kc.tugraz.ss.activity.datatypes.SSActivity;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpDailySummary;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpDailySummaryCopyLearnEpEntry;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpDailySummaryShareLearnEpEntry;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.SSLabel;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.util.SSStrU;
import java.util.Map;

public class SSLearnEpSummaryCommons {
  
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
}
