 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.recomm.serv.task;

import at.kc.tugraz.ss.recomm.api.SSRecommServerI;
import at.tugraz.sss.serv.util.SSLogU;
import at.kc.tugraz.ss.recomm.conf.SSRecommConf;
import at.kc.tugraz.ss.recomm.datatypes.par.SSRecommUpdateBulkPar;
import at.kc.tugraz.ss.recomm.serv.SSRecommServ;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.*;
import java.sql.*;

public class SSRecommUpdateBulkTask implements Runnable {
  
  private final SSRecommConf recommConf;
  
  public SSRecommUpdateBulkTask(
    final SSRecommConf recommConf) throws Exception{
    
    this.recommConf = recommConf;
  }
  
  @Override
  public void run() {
    handle();
  }
  
  private void handle(){
    
    Connection sqlCon = null;
    
    try{
      
      final SSServPar servPar = new SSServPar(null);
      
      sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      
      servPar.sqlCon = sqlCon;
      
      ((SSRecommServerI)SSRecommServ.inst.getServImpl()).recommUpdateBulk(
        new SSRecommUpdateBulkPar(
          servPar,
          SSConf.systemUserUri, //user
          recommConf.fileNameForRec, //realm
          null, //clientSocket
          null, //fileInputStream
          null));//clientType
      
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