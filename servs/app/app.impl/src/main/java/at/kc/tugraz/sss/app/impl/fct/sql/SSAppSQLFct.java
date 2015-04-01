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
package at.kc.tugraz.sss.app.impl.fct.sql;

import at.tugraz.sss.serv.SSSQLVarU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.tugraz.sss.serv.SSDBSQLI;

import at.kc.tugraz.sss.app.datatypes.SSApp;
import at.tugraz.sss.serv.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSAppSQLFct extends SSDBSQLFct{

  public SSAppSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  public List<SSApp> getApps() throws Exception{
    
    ResultSet resultSet = null;
      
    try{
      final List<SSApp>         apps    = new ArrayList<>();
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarU.appId);
      column(columns, SSSQLVarU.descriptionShort);
      column(columns, SSSQLVarU.descriptionFunctional);
      column(columns, SSSQLVarU.descriptionTechnical);
      column(columns, SSSQLVarU.descriptionInstall);
      column(columns, SSSQLVarU.downloadIOS);
      column(columns, SSSQLVarU.downloadAndroid);
      column(columns, SSSQLVarU.fork);
      
      resultSet = dbSQL.select(appTable, columns, wheres, null, null, null);
      
      while(resultSet.next()){
        
        apps.add(
          SSApp.get(
            bindingStrToUri         (resultSet, SSSQLVarU.appId), 
            bindingStrToTextComment (resultSet, SSSQLVarU.descriptionShort), 
            bindingStrToTextComment (resultSet, SSSQLVarU.descriptionFunctional),  
            bindingStrToTextComment (resultSet, SSSQLVarU.descriptionTechnical),  
            bindingStrToTextComment (resultSet, SSSQLVarU.descriptionInstall),  
            bindingStrToUri         (resultSet, SSSQLVarU.downloadIOS), 
            bindingStrToUri         (resultSet, SSSQLVarU.downloadAndroid), 
            bindingStrToUri         (resultSet, SSSQLVarU.fork)));
      }
      
      return apps;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void createApp(
    final SSUri         app, 
    final SSTextComment descriptionShort, 
    final SSTextComment descriptionFunctional, 
    final SSTextComment descriptionTechnical, 
    final SSTextComment descriptionInstall, 
    final SSUri         downloadIOS, 
    final SSUri         downloadAndroid, 
    final SSUri         fork) throws Exception{
    
     try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert    (inserts,    SSSQLVarU.appId,     app);
      
      if(descriptionShort == null){
        insert    (inserts,    SSSQLVarU.descriptionShort,   SSStrU.empty);
      }else{
        insert    (inserts,    SSSQLVarU.descriptionShort,   descriptionShort);
      }
      
      if(descriptionFunctional == null){
        insert    (inserts,    SSSQLVarU.descriptionFunctional,     SSStrU.empty);
      }else{
        insert    (inserts,     SSSQLVarU.descriptionFunctional,     descriptionFunctional);
      }
      
      if(descriptionTechnical == null){
        insert    (inserts,    SSSQLVarU.descriptionTechnical,     SSStrU.empty);
      }else{
        insert    (inserts,     SSSQLVarU.descriptionTechnical,     descriptionTechnical);
      }
      
      if(descriptionInstall == null){
        insert    (inserts,    SSSQLVarU.descriptionInstall,     SSStrU.empty);
      }else{
        insert    (inserts,     SSSQLVarU.descriptionInstall,     descriptionInstall);
      }
      
      if(downloadIOS == null){
        insert    (inserts,    SSSQLVarU.downloadIOS,     SSStrU.empty);
      }else{
        insert    (inserts,     SSSQLVarU.downloadIOS,     downloadIOS);
      }
      
      if(downloadAndroid == null){
        insert    (inserts,    SSSQLVarU.downloadAndroid,     SSStrU.empty);
      }else{
        insert    (inserts,     SSSQLVarU.downloadAndroid,     downloadAndroid);
      }
      
      if(fork == null){
        insert    (inserts,    SSSQLVarU.fork,     SSStrU.empty);
      }else{
        insert    (inserts,     SSSQLVarU.fork,     fork);
      }
      
      uniqueKey (uniqueKeys, SSSQLVarU.appId,    app);
      
      dbSQL.insertIfNotExists(appTable, inserts, uniqueKeys);
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
  }
}