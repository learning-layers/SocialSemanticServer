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

import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSDBSQLFctA;
import at.tugraz.sss.serv.SSDBSQLI;
import at.kc.tugraz.sss.app.datatypes.SSApp;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSAppSQLFct extends SSDBSQLFctA{

  public SSAppSQLFct(final SSDBSQLI dbSQL) throws SSErr{
    super(dbSQL);
  }
  
  public SSApp getApp(
    final SSUri app) throws Exception{
    
    ResultSet resultSet = null;
      
    try{
      
      if(app == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.appId);
      column(columns, SSSQLVarNames.descriptionShort);
      column(columns, SSSQLVarNames.descriptionFunctional);
      column(columns, SSSQLVarNames.descriptionTechnical);
      column(columns, SSSQLVarNames.descriptionInstall);
      column(columns, SSSQLVarNames.downloadIOS);
      column(columns, SSSQLVarNames.downloadAndroid);
      column(columns, SSSQLVarNames.fork);
      
      where(wheres, SSSQLVarNames.appId, app);
      
      resultSet = dbSQL.select(SSSQLVarNames.appTable, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return SSApp.get(
        bindingStrToUri         (resultSet, SSSQLVarNames.appId),
        bindingStrToTextComment (resultSet, SSSQLVarNames.descriptionShort),
        bindingStrToTextComment (resultSet, SSSQLVarNames.descriptionFunctional),
        bindingStrToTextComment (resultSet, SSSQLVarNames.descriptionTechnical),
        bindingStrToTextComment (resultSet, SSSQLVarNames.descriptionInstall),
        bindingStrToUri         (resultSet, SSSQLVarNames.downloadIOS),
        bindingStrToUri         (resultSet, SSSQLVarNames.downloadAndroid),
        bindingStrToUri         (resultSet, SSSQLVarNames.fork));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getAppURIs() throws Exception{
    
    ResultSet resultSet = null;
      
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.appId);
      
      resultSet = dbSQL.select(SSSQLVarNames.appTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.appId);
      
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
      
      insert    (inserts,    SSSQLVarNames.appId,     app);
      
      if(descriptionShort == null){
        insert    (inserts,    SSSQLVarNames.descriptionShort,   SSStrU.empty);
      }else{
        insert    (inserts,    SSSQLVarNames.descriptionShort,   descriptionShort);
      }
      
      if(descriptionFunctional == null){
        insert    (inserts,    SSSQLVarNames.descriptionFunctional,     SSStrU.empty);
      }else{
        insert    (inserts,     SSSQLVarNames.descriptionFunctional,     descriptionFunctional);
      }
      
      if(descriptionTechnical == null){
        insert    (inserts,    SSSQLVarNames.descriptionTechnical,     SSStrU.empty);
      }else{
        insert    (inserts,     SSSQLVarNames.descriptionTechnical,     descriptionTechnical);
      }
      
      if(descriptionInstall == null){
        insert    (inserts,    SSSQLVarNames.descriptionInstall,     SSStrU.empty);
      }else{
        insert    (inserts,     SSSQLVarNames.descriptionInstall,     descriptionInstall);
      }
      
      if(downloadIOS == null){
        insert    (inserts,    SSSQLVarNames.downloadIOS,     SSStrU.empty);
      }else{
        insert    (inserts,     SSSQLVarNames.downloadIOS,     downloadIOS);
      }
      
      if(downloadAndroid == null){
        insert    (inserts,    SSSQLVarNames.downloadAndroid,     SSStrU.empty);
      }else{
        insert    (inserts,     SSSQLVarNames.downloadAndroid,     downloadAndroid);
      }
      
      if(fork == null){
        insert    (inserts,    SSSQLVarNames.fork,     SSStrU.empty);
      }else{
        insert    (inserts,     SSSQLVarNames.fork,     fork);
      }
      
      uniqueKey (uniqueKeys, SSSQLVarNames.appId,    app);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.appTable, inserts, uniqueKeys);
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
  }

  public void removeApps(final List<SSUri> apps) throws Exception {
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      for(SSUri app : apps){
      
        wheres.clear();
        
        where(wheres, SSSQLVarNames.appId, app);
      
        dbSQL.deleteIgnore(SSSQLVarNames.appTable, wheres);
      }
      
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
  }
}