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

import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSUri;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSFileSQLFct extends SSDBSQLFct{

  public SSFileSQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }
  
  public void addFile(
    final SSUri file) throws Exception{
    
    try{

      if(SSObjU.isNull(file)){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert   (inserts,    SSSQLVarNames.fileId, file);
      uniqueKey(uniqueKeys, SSSQLVarNames.fileId, file);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.filesTable, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getFileURIsAttachtedToEntity(
    final SSUri entity) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(entity)){
        throw new SSErr(SSErrE.parameterMissing);
      }

      final List<String>        columns    = new ArrayList<>();
      final List<String>        tables     = new ArrayList<>();
      final Map<String, String> wheres     = new HashMap<>();
      final List<String>        tableCons  = new ArrayList<>();
      
      column (columns, SSSQLVarNames.filesTable, SSSQLVarNames.fileId);

      table  (tables, SSSQLVarNames.filesTable);
      table  (tables, SSSQLVarNames.entitiesTable);

      where   (wheres, SSSQLVarNames.entitiesTable, SSSQLVarNames.entityId, entity);
      
      tableCon(tableCons, SSSQLVarNames.filesTable, SSSQLVarNames.fileId, SSSQLVarNames.entitiesTable, SSSQLVarNames.attachedEntityId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.fileId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}