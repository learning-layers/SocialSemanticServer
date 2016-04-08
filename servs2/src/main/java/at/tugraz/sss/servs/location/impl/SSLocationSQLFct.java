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
package at.tugraz.sss.servs.location.impl;

import at.tugraz.sss.servs.location.datatype.SSLocationSQLTableE;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.servs.location.datatype.SSLocation;
import at.tugraz.sss.serv.util.SSSQLVarNames;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.servs.location.datatype.*;

public class SSLocationSQLFct extends SSCoreSQL{
  
  public SSLocationSQLFct(
    final SSDBSQLI dbSQL){
    
    super(dbSQL);
  }
  
  public void addLocation(
    final SSServPar  servPar,
    final SSUri      locationURI,
    final SSUri      entity, 
    final SSLocation location) throws SSErr{
    
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
      
      dbSQL.insert(servPar, SSLocationSQLTableE.location, inserts);
      
      inserts.clear();
      
      insert(inserts, SSSQLVarNames.entityId,           entity);
      insert(inserts, SSSQLVarNames.locationId,         locationURI);
      
      dbSQL.insert(servPar, SSEntitySQLTableE.entitylocations, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

   public SSLocation getLocation(
    final SSServPar  servPar,
     final SSUri location) throws SSErr{
   
    ResultSet resultSet = null;
    
    try{
      final List<String>         columns    = new ArrayList<>();              
      final Map<String, String>  wheres     = new HashMap<>();
      
      column(columns, SSLocationSQLTableE.location, SSSQLVarNames.locationId);
      column(columns, SSLocationSQLTableE.location, SSSQLVarNames.latitude);
      column(columns, SSLocationSQLTableE.location, SSSQLVarNames.longitude);
      column(columns, SSLocationSQLTableE.location, SSSQLVarNames.accuracy);
      
      resultSet = dbSQL.select(servPar, SSLocationSQLTableE.location, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
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
    final SSServPar  servPar,
    final SSUri forEntity) throws SSErr{
   
    ResultSet resultSet = null;
    
    try{
      final List<String>         columns    = new ArrayList<>();              
      final Map<String, String>  wheres     = new HashMap<>();

      column(columns, SSLocationSQLTableE.location, SSSQLVarNames.locationId);
        
      if(forEntity != null){
        
        final List<SSSQLTableI>    tables     = new ArrayList<>();
        final List<String>         tableCons  = new ArrayList<>();
      
        table(tables, SSLocationSQLTableE.location);
        table(tables, SSEntitySQLTableE.entitylocations);
        
        where(wheres, SSSQLVarNames.entityId, forEntity);
        
        tableCon(tableCons, SSLocationSQLTableE.location, SSSQLVarNames.locationId, SSEntitySQLTableE.entitylocations, SSSQLVarNames.locationId);
        
        resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      }else{
        resultSet = dbSQL.select(servPar, SSLocationSQLTableE.location, columns, wheres, null, null, null);
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
