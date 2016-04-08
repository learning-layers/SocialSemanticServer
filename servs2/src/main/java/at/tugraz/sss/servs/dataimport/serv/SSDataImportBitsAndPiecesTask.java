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
package at.tugraz.sss.servs.dataimport.serv;

import at.tugraz.sss.servs.dataimport.api.*;
import at.tugraz.sss.servs.dataimport.datatype.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import java.sql.*;

public class SSDataImportBitsAndPiecesTask implements Runnable{
  
  private final SSDataImportBitsAndPiecesPar par;
  
  public SSDataImportBitsAndPiecesTask(
    final SSDataImportBitsAndPiecesPar par){
    
    this.par = par;
  }
  
  @Override
  public void run() {
    handle();
  }
  
  private void handle(){
    
    Connection sqlCon = null;
    
    try{
      
      sqlCon = ((SSDBSQLI) SSServReg.getServ(SSDBSQLI.class)).createConnection();
      
      par.sqlCon = sqlCon;
      
      ((SSDataImportServerI) SSServReg.getServ(SSDataImportServerI.class)).dataImportBitsAndPieces(par);
      
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