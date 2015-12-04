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
package at.kc.tugraz.ss.service.filerepo.impl.fct;

import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSUri;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sss.servs.entity.sql.SSEntitySQL;

public class SSFileSQLFct extends SSEntitySQL{

  public SSFileSQLFct(
    final SSDBSQLI dbSQL, 
    final SSUri systemUserURI) {
    
    super(dbSQL, systemUserURI);
  }
  
  public void addFile(
    final SSUri file) throws Exception{
    
    try{

      if(SSObjU.isNull(file)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert    (inserts,    SSSQLVarNames.fileId,   file);
      uniqueKey (uniqueKeys, SSSQLVarNames.fileId,   file);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.fileTable, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getEntityFiles(
    final SSUri entity) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns           = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      
      column(columns, SSSQLVarNames.fileId);
      
      where(wheres, SSSQLVarNames.entityId, entity);
      
      resultSet = dbSQL.select(SSSQLVarNames.entityFilesTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.fileId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void addFileToEntity(
    final SSUri file, 
    final SSUri entity) throws Exception {
    
    try{
      
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.entityId,      entity);
      insert(inserts, SSSQLVarNames.fileId,        file);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.entityId, entity);
      uniqueKey(uniqueKeys, SSSQLVarNames.fileId,   file);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.entityFilesTable, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public SSEntityE getFileType(final SSUri file) throws  Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns           = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      
      column(columns, SSSQLVarNames.type);
      
      where(wheres, SSSQLVarNames.id, file);
      
      resultSet = dbSQL.select(SSSQLVarNames.entityTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStrToEntityType(resultSet, SSSQLVarNames.type);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
