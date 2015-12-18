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
package at.tugraz.sss.servs.mail.impl;

import at.tugraz.sss.serv.SSDBSQLFctA;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSMailSQLFct extends SSDBSQLFctA{

  public SSMailSQLFct(final SSDBSQLI dbSQL){
    super(dbSQL);
  }
  
  public Boolean existsMail(
    final SSUri  mail,
    final String hash,
    final String receiverEmail) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      if(
        mail == null &&
        hash == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> where   = new HashMap<>();
      
      column(columns, SSSQLVarNames.mailId);
      
      if(mail != null){
        where(where, SSSQLVarNames.mailId, mail);
      }
      
      if(hash != null){
        where(where, SSSQLVarNames.hash, hash);
      }
      
      if(receiverEmail != null){
        where(where, SSSQLVarNames.receiverEmail, receiverEmail);
      }
            
      resultSet = 
        dbSQL.select(
          SSSQLVarNames.mailTable, 
          columns, 
          where, 
          null, 
          null, 
          null);
      
      try{
        checkFirstResult(resultSet);
      }catch(Exception error){
        
        if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
          SSServErrReg.reset();
          return false;
        }
        
        throw error;
      }
      
      return true;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addMailIfNotExists(
    final SSUri         mail, 
    final String        receiverEmail,
    final String        hash) throws Exception{
    
    if(!existsMail(mail, hash, receiverEmail)){
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert    (inserts, SSSQLVarNames.mailId, mail);
      
      if(receiverEmail == null){
        insert(inserts, SSSQLVarNames.receiverEmail, SSStrU.empty);
      }else{
        insert(inserts, SSSQLVarNames.receiverEmail, receiverEmail);
      }
      
      if(hash == null){
        insert(inserts, SSSQLVarNames.hash, SSStrU.empty);
      }else{
        insert(inserts, SSSQLVarNames.hash, hash);
      }
      
      dbSQL.insert(SSSQLVarNames.mailTable, inserts);
    }else{
      
      if(mail == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      try{
        final Map<String, String>  wheres   = new HashMap<>();
        final Map<String, String>  updates  = new HashMap<>();
        
        where(wheres, SSSQLVarNames.mailId, mail);
        
        if(receiverEmail != null){
          update (updates, SSSQLVarNames.receiverEmail, receiverEmail);
        }
        
        if(hash != null){
          update (updates, SSSQLVarNames.hash, hash);
        }
        
        if(updates.isEmpty()){
          return;
        }
        
        dbSQL.update(SSSQLVarNames.mailTable, wheres, updates);
        
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }
    }
  }
}