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
package at.tugraz.sss.servs.location.impl.sql;

import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.servs.location.datatype.SSLocation;
import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sss.servs.entity.sql.SSEntitySQL;

public class SSLocationSQLFct extends SSEntitySQL{
  
  public SSLocationSQLFct(
    final SSDBSQLI dbSQL,
    final SSUri    systemUserURI) throws Exception{
    
    super(dbSQL, systemUserURI);
  }
  
  public void addLocation(
    final SSUri      locationURI,
    final SSUri      entity, 
    final SSLocation location) throws Exception{
    
    try{

      final Map<String, String> inserts    = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.locationId,        locationURI);
      insert(inserts, SSSQLVarNames.latitude,          location.latitude);
      insert(inserts, SSSQLVarNames.longitude,         location.longitude);
      
      if(location.accuracy == null){
        insert(inserts, SSSQLVarNames.accuracy,         SSStrU.empty);
      }else{
        insert(inserts, SSSQLVarNames.accuracy,         location.accuracy);
      }
      
      dbSQL.insert(SSSQLVarNames.locationTable, inserts);
      
      inserts.clear();
      
      insert(inserts, SSSQLVarNames.entityId,           entity);
      insert(inserts, SSSQLVarNames.locationId,         locationURI);
      
      dbSQL.insert(SSSQLVarNames.entityLocationsTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

   public SSLocation getLocation(
     final SSUri location) throws Exception{
   
    ResultSet resultSet = null;
    
    try{
      final List<String>         columns    = new ArrayList<>();              
      final Map<String, String>  wheres     = new HashMap<>();
      
      column(columns, SSSQLVarNames.locationTable, SSSQLVarNames.locationId);
      column(columns, SSSQLVarNames.locationTable, SSSQLVarNames.latitude);
      column(columns, SSSQLVarNames.locationTable, SSSQLVarNames.longitude);
      column(columns, SSSQLVarNames.locationTable, SSSQLVarNames.accuracy);
      
      resultSet = dbSQL.select(SSSQLVarNames.locationTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      return SSLocation.get(
        bindingStrToUri   (resultSet, SSSQLVarNames.locationId),
        bindingStrToDouble(resultSet, SSSQLVarNames.latitude),
        bindingStrToDouble(resultSet, SSSQLVarNames.longitude),
        bindingStrToFloat (resultSet, SSSQLVarNames.accuracy));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
   
  public List<SSUri> getLocationURIs(
    final SSUri forEntity) throws Exception{
   
    ResultSet resultSet = null;
    
    try{
      final List<String>         columns    = new ArrayList<>();              
      final Map<String, String>  wheres     = new HashMap<>();

      column(columns, SSSQLVarNames.locationTable, SSSQLVarNames.locationId);
        
      if(forEntity != null){
        
        final List<String>         tables     = new ArrayList<>();
        final List<String>         tableCons  = new ArrayList<>();
      
        table(tables, SSSQLVarNames.locationTable);
        table(tables, SSSQLVarNames.entityLocationsTable);
        
        where(wheres, SSSQLVarNames.entityId, forEntity);
        
        tableCon(tableCons, SSSQLVarNames.locationTable, SSSQLVarNames.locationId, SSSQLVarNames.entityLocationsTable, SSSQLVarNames.locationId);
        
        resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      }else{
        resultSet = dbSQL.select(SSSQLVarNames.locationTable, columns, wheres, null, null, null);
      }
      
      return getURIsFromResult(resultSet, SSSQLVarNames.locationId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
}
