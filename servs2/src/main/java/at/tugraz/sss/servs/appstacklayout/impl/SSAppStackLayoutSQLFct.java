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
package at.tugraz.sss.servs.appstacklayout.impl;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.appstacklayout.datatype.SSAppStackLayoutSQLTableE;
import at.tugraz.sss.servs.appstacklayout.datatype.SSAppStackLayout;
import at.tugraz.sss.servs.util.SSSQLVarNames;
import at.tugraz.sss.servs.db.api.SSDBSQLI;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.servs.db.impl.SSCoreSQL;

public class SSAppStackLayoutSQLFct extends SSCoreSQL{

  public SSAppStackLayoutSQLFct(
    final SSDBSQLI dbSQL) {
    
    super(dbSQL);
  }
  
  public SSAppStackLayout getAppStackLayout(
    final SSServPar servPar,
    final SSUri stack) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      
      if(stack == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final List<String>           columns         = new ArrayList<>();
      final Map<String, String>    wheres          = new HashMap<>();
      
      column(columns, SSSQLVarNames.stackId);
      column(columns, SSSQLVarNames.app);
        
      where(wheres, SSSQLVarNames.stackId, stack);
      
      resultSet = dbSQL.select(servPar, SSAppStackLayoutSQLTableE.appstacklayout, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
        
      return SSAppStackLayout.get(
        bindingStrToUri         (resultSet, SSSQLVarNames.stackId),
        bindingStrToUri         (resultSet, SSSQLVarNames.app));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getStackURIs(
    final SSServPar servPar) throws SSErr{
    
    ResultSet resultSet = null;
      
    try{
      final List<String>           columns         = new ArrayList<>();
      final Map<String, String>    wheres          = new HashMap<>();
      
      column(columns, SSSQLVarNames.stackId);
        
      resultSet = dbSQL.select(servPar, SSAppStackLayoutSQLTableE.appstacklayout, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.stackId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void createAppStackLayout(
    final SSServPar servPar,
    final SSUri         stack,
    final SSUri         app) throws SSErr{
    
     try{
      final Map<String, String> inserts    = new HashMap<>();
      
      if(app != null){
        insert    (inserts,    SSSQLVarNames.app,     app);
      }else{
        insert    (inserts,    SSSQLVarNames.app,     SSStrU.empty);
      }
      
      insert    (inserts,    SSSQLVarNames.stackId,     stack);
      
      dbSQL.insert(servPar, SSAppStackLayoutSQLTableE.appstacklayout, inserts);
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
  }

  public void deleteStack(
    final SSServPar servPar,
    final SSUri stack) throws SSErr{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.stackId, stack);
      
      dbSQL.delete(servPar, SSAppStackLayoutSQLTableE.appstacklayout, wheres);
      
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
  }

  public void updateAppStackLayout(
    final SSServPar servPar,
    final SSUri stack, 
    final SSUri app) throws SSErr{
    
    try{
      final Map<String, String> updates    = new HashMap<>();
      final Map<String, String> wheres     = new HashMap<>();
      
      where(wheres, SSSQLVarNames.stackId, stack);
      
      if(app != null){
        update(updates,    SSSQLVarNames.app,     app);
      }
      
      dbSQL.updateIgnore(servPar, SSAppStackLayoutSQLTableE.appstacklayout, wheres, updates);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}