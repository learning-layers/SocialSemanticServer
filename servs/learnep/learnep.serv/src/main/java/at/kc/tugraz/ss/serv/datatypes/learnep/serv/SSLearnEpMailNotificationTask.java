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

import at.kc.tugraz.ss.serv.datatypes.learnep.api.*;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpDailySummary;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpDailySummaryCopyLearnEpEntry;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpDailySummaryEntry;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpDailySummaryShareLearnEpEntry;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par.SSLearnEpDailySummaryGetPar;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpDailySummaryGetRet;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.util.SSStrU;
import at.tugraz.sss.servs.mail.SSMailServerI;
import at.tugraz.sss.servs.mail.datatype.par.SSMailSendPar;
import java.sql.*;
import java.util.Map;

public class SSLearnEpMailNotificationTask implements Runnable{
  
  @Override
  public void run() {
    
    Connection sqlCon = null;
    
    try{
      final SSMailServerI               mailServ                   = ((SSMailServerI)    SSServReg.getServ(SSMailServerI.class));
      final SSLearnEpServerI            learnEpServ                = ((SSLearnEpServerI) SSServReg.getServ(SSLearnEpServerI.class));
      final SSServPar                   servPar                    = new SSServPar(null);
      final SSLearnEpDailySummaryGetRet dailySummary;
      String                            shareLearnEpMailSummary;
      String                            copyLearnEpMailSummary;
      boolean                           sharingExists;
      boolean                           copyingExists;
      String                            mailSummary;
      
      sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      
      servPar.sqlCon = sqlCon;
      
      dailySummary =
        learnEpServ.learnEpDailySummaryGet(
          new SSLearnEpDailySummaryGetPar(
            servPar,
            SSConf.systemUserUri,
            new java.util.Date().getTime() - SSDateU.dayInMilliSeconds * 10));
      
      for(Map.Entry<String, SSLearnEpDailySummary> userSummary : dailySummary.summaries.entrySet()){
        
        mailSummary   = SSStrU.empty;
        sharingExists = false;
        copyingExists = false;
          
        shareLearnEpMailSummary = "Shared Learning Episodes" + SSStrU.backslashRBackslashN;
        shareLearnEpMailSummary += "-----------------------" + SSStrU.backslashRBackslashN;
        shareLearnEpMailSummary += SSStrU.backslashRBackslashN;
        
        copyLearnEpMailSummary = "Copied Learning Episodes" + SSStrU.backslashRBackslashN;
        copyLearnEpMailSummary += "-----------------------" + SSStrU.backslashRBackslashN;
        copyLearnEpMailSummary += SSStrU.backslashRBackslashN;
        
        for(SSLearnEpDailySummaryEntry summary : userSummary.getValue().userSummaries){
          
          if(summary instanceof SSLearnEpDailySummaryShareLearnEpEntry){
            
            shareLearnEpMailSummary += "* " + summary.originUserLabel + " shared " + summary.targetEntityLabel + " with me" + SSStrU.backslashRBackslashN;
            sharingExists            = true;
            continue;
          }
          
          if(summary instanceof SSLearnEpDailySummaryCopyLearnEpEntry){
            copyLearnEpMailSummary += "* " + summary.originUserLabel + " copied " + summary.targetEntityLabel + " for me" + SSStrU.backslashRBackslashN;
            sharingExists           = copyingExists;
            continue;
          }
        }

        if(sharingExists){
          mailSummary += shareLearnEpMailSummary + SSStrU.backslashRBackslashN;
        }
        
        if(copyingExists){
          mailSummary += copyLearnEpMailSummary;
        }        
        
        mailServ.mailSend(
          new SSMailSendPar(
            servPar,
            SSConf.systemUserUri,
            "dtheiler@know-center.at", //fromEmail,
            "the_didz@gmx.at", //toEmail,
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