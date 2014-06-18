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
package at.kc.tugraz.ss.serv.datatypes.location.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSLocationSQLFct extends SSDBSQLFct{

  public SSLocationSQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }
  
  public Boolean existsLocationString(
    final String locationString) throws Exception{
   
    ResultSet               resultSet          = null;
    
    try{
      final List<String>            tables         = new ArrayList<>();
      final List<String>            columns        = new ArrayList<>();
      final List<String>            tableCons      = new ArrayList<>();
      final Map<String, String>     wheres         = new HashMap<>();
      
      table  (tables,     locationTable);
      table  (tables,     entityTable);
      column (columns,    SSSQLVarU.locationId);
      column (columns,    SSSQLVarU.id);
      column (columns,    SSSQLVarU.label);
      where  (wheres,     SSSQLVarU.label, locationString);
      where  (wheres,     SSSQLVarU.type,  SSEntityE.location);
      tableCon(tableCons, entityTable,     SSSQLVarU.id, locationTable, SSSQLVarU.locationId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri getOrCreateLocationURI(
    final Boolean existsTag, 
    final String  locationString) throws Exception{
    
    if(!existsTag){
      return createLocationURI();
    }
        
    ResultSet resultSet = null;
    
    try{
      final List<String>            tables     = new ArrayList<>();
      final List<String>            columns    = new ArrayList<>();
      final List<String>            tableCons  = new ArrayList<>();
      final Map<String, String>     wheres     = new HashMap<>();
      
      table    (tables,    locationTable);
      table    (tables,    entityTable);
      column   (columns,   SSSQLVarU.tagId);
      column   (columns,   SSSQLVarU.id);
      column   (columns,   SSSQLVarU.label);
      where    (wheres,    SSSQLVarU.label, locationString);
      tableCon (tableCons, locationTable,   SSSQLVarU.locationId, entityTable, SSSQLVarU.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarU.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void addLocation(
    final SSUri locationUri) throws Exception{
    
    try{
      final Map<String, String> inserts  = new HashMap<>();
      
      insert(inserts, SSSQLVarU.locationId, locationUri);
      
      dbSQL.insert(locationTable, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Boolean existsLocationAss(
    final SSUri userUri, 
    final SSUri entityUri,
    final SSUri locationUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.locationId,   locationUri);
      where(wheres, SSSQLVarU.entityId,     entityUri);
      where(wheres, SSSQLVarU.userId,       userUri);
      
      resultSet = dbSQL.select(locationsTable, wheres);
      
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addLocationAss(
    final SSUri userUri, 
    final SSUri entityUri, 
    final SSUri locationUri) throws Exception{
  
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert (inserts, SSSQLVarU.locationId, locationUri);
      insert (inserts, SSSQLVarU.entityId,   entityUri);
      insert (inserts, SSSQLVarU.userId,     userUri);
      
      dbSQL.insert(locationsTable, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public Boolean existsLocationForEntity(
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.entityId, entityUri);
      
      resultSet = dbSQL.select(locationTable, wheres);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void deleteLocationAss(
    final SSUri userUri,
    final SSUri entityUri) throws Exception{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      if(userUri != null){
        where(wheres, SSSQLVarU.userId,   userUri);
      }
      
      if(entityUri != null){
        where(wheres, SSSQLVarU.entityId, entityUri);
      }
      
      if(wheres.isEmpty()){
        dbSQL.delete(locationsTable);
      }else{
        dbSQL.delete(locationsTable, wheres);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public List<String> getLocationAsss(
    final SSUri  userUri, 
    final SSUri  entityUri, 
    final String locationString) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        tables         = new ArrayList<>();
      final List<String>        columns        = new ArrayList<>();
      final List<String>        tableCons      = new ArrayList<>();
      final Map<String, String> wheres         = new HashMap<>();
      final SSUri               locationUri    = getOrCreateLocationURI(existsLocationString(locationString), locationString);
      
      table    (tables,    locationsTable);
      table    (tables,    entityTable);
      column   (columns,   SSSQLVarU.locationId);
      column   (columns,   SSSQLVarU.entityId);
      column   (columns,   SSSQLVarU.userId);
      column   (columns,   SSSQLVarU.label);
      tableCon (tableCons, locationsTable, SSSQLVarU.locationId, entityTable, SSSQLVarU.id);
      
      if(userUri != null){
        where(wheres, SSSQLVarU.userId, userUri);
      }
      
      if(entityUri != null){
        where(wheres, SSSQLVarU.entityId, entityUri);
      }
      
      if(locationUri != null){
        where(wheres, SSSQLVarU.locationId, locationUri);
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      return getStringsFromResult(resultSet, SSSQLVarU.label);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri createLocationURI() throws Exception{
   return SSUri.get(SSIDU.uniqueID(objLocation().toString()));
  }
  
  private SSUri objLocation() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityE.location.toString());
  } 
}
