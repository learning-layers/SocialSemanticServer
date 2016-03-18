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
package at.tugraz.sss.servs.mail.serv;

import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.mail.SSMailServerI;
import at.tugraz.sss.servs.mail.datatype.par.SSMailSendPar;
import java.sql.*;

public class SSMailSendTask implements Runnable{
  
  @Override
  public void run(){
    handle();
  }
  
  private void handle() {
    
    Connection sqlCon = null;
    
    try{
      
      final SSServPar servPar = new SSServPar();
      
      sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      
      servPar.sqlCon = sqlCon;
      
      ((SSMailServerI) SSServReg.getServ(SSMailServerI.class)).mailSend(
        new SSMailSendPar(
          servPar,
          SSConf.systemUserUri,
          "dtheiler@know-center.at", //fromEmail,
          "the_didz@gmx.at", //toEmail,
          "Bits and Pieces test subject", //subject,
          "test content", //content,
          true, //withUserRestriction,
          true)); //shouldCommit
      
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