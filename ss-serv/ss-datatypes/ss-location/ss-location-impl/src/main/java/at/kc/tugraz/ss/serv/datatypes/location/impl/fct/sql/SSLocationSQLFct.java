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
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
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

  private static final String            locationTable                       = "location";
  private static final String            locationsTable                      = "locations";
  
  public SSLocationSQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }
  
  public Boolean existsLocationString(String locationString) throws Exception{
   
    if(locationString == null){
      SSServErrReg.regErrThrow(new Exception("locationString null"));
      return null;
    }
    
    HashMap<String, String> selectPars         = new HashMap<String, String>();
    ResultSet               resultSet          = null;
    Boolean                 exists             = false;
    List<String>            tableNames         = new ArrayList<String>();
    List<String>            columnNames        = new ArrayList<String>();
    Map<String, String>     whereParsAndValues = new HashMap<String, String>();
    
    try{
      selectPars.put(SSSQLVarU.label, locationString);
      selectPars.put(SSSQLVarU.type,  SSEntityE.toStr(SSEntityE.location));
      
      tableNames.add  (locationTable);
      tableNames.add  (entityTable);
      columnNames.add (SSSQLVarU.locationId);
      columnNames.add (SSSQLVarU.id);
      columnNames.add (SSSQLVarU.label);
      
      whereParsAndValues.put(SSSQLVarU.label, locationString);
      
      resultSet =
        dbSQL.selectCertainWhere(
        tableNames,
        columnNames,
        whereParsAndValues,
        SSSQLVarU.locationId + SSStrU.equal + SSSQLVarU.id);
      
      exists = resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return exists;
  }
  
  public SSUri getOrCreateLocationUri(Boolean exsitsTag, String locationString) throws Exception{
    
    if(!exsitsTag){
      return createLocationURI();
    }
        
    ResultSet               resultSet          = null;
    List<String>            tableNames         = new ArrayList<String>();
    List<String>            columnNames        = new ArrayList<String>();
    Map<String, String>     whereParsAndValues = new HashMap<String, String>();
    
    try{
      
      tableNames.add  (locationTable);
      tableNames.add  (entityTable);
      columnNames.add (SSSQLVarU.tagId);
      columnNames.add (SSSQLVarU.id);
      columnNames.add (SSSQLVarU.label);
      
      whereParsAndValues.put(SSSQLVarU.label, locationString);
      
      resultSet =
        dbSQL.selectCertainWhere(
        tableNames,
        columnNames,
        whereParsAndValues,
        SSSQLVarU.locationId + SSStrU.equal + SSSQLVarU.id);
      
      resultSet.first();
      
      return bindingStrToUri(resultSet, SSSQLVarU.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void addLocation(SSUri locationUri) throws Exception{
    
    if(locationUri == null){
      SSServErrReg.regErrThrow(new Exception("locationUri null"));
      return;
    }
    
    final Map<String, String> insertPars  = new HashMap<String, String>();
    
    insertPars.put(SSSQLVarU.locationId, locationUri.toString());
    
    try{
      dbSQL.insert(locationTable, insertPars);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Boolean existsLocationAss(SSUri userUri, SSUri entityUri, SSUri locationUri) throws Exception{
   
    if(
      userUri        == null ||
      entityUri      == null ||
      locationUri    == null){
      SSServErrReg.regErrThrow(new Exception("pars not ok"));
      return null;
    }
    
    final HashMap<String, String> selectPars = new HashMap<String, String>();
    ResultSet                     resultSet  = null;
    Boolean                       exists     = null;
    
    try{
      selectPars.put(SSSQLVarU.locationId,   locationUri.toString());
      selectPars.put(SSSQLVarU.entityId,     entityUri.toString());
      selectPars.put(SSSQLVarU.userId,       userUri.toString());
      
      resultSet = dbSQL.selectAllWhere(locationsTable, selectPars);
      exists    = resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }

    return exists;
  }
  
  public void addLocationAss(SSUri userUri, SSUri entityUri, SSUri locationUri) throws Exception{
  
    if(
      userUri     == null ||
      entityUri   == null ||
      locationUri == null){
      SSServErrReg.regErrThrow(new Exception("pars not okay"));
      return;
    }
    
    final HashMap<String, String> insertPars = new HashMap<String, String>();
    
    insertPars.put(SSSQLVarU.locationId,   locationUri.toString());
    insertPars.put(SSSQLVarU.entityId,     entityUri.toString());
    insertPars.put(SSSQLVarU.userId,       userUri.toString());

    try{
      dbSQL.insert(locationsTable, insertPars);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public Boolean existsLocationForEntity(SSUri entity) throws Exception{
    
    ResultSet           resultSet               = null;
    Map<String, String> selectPars              = new HashMap<String, String>();
    Boolean             existsLocationForEntity = false;
    
    selectPars.put(SSSQLVarU.entryId, entity.toString());
    
    try{
      resultSet               = dbSQL.selectAllWhere(locationTable, selectPars);
      existsLocationForEntity = resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return existsLocationForEntity;
  }
  
  public void deleteLocationAss(
    final SSUri userUri,
    final SSUri entityUri) throws Exception{
    
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    
    if(userUri != null){
      whereParNamesWithValues.put(SSSQLVarU.userId,   userUri.toString());
    }
    
    if(entityUri != null){
      whereParNamesWithValues.put(SSSQLVarU.entityId, entityUri.toString());
    }
    
    if(whereParNamesWithValues.isEmpty()){
      dbSQL.deleteAll(locationsTable);
    }else{
      dbSQL.deleteWhere(locationsTable, whereParNamesWithValues);
    }
  }

  public List<String> getLocationAsss(SSUri userUri, SSUri entityUri, String locationString) throws Exception{
    
    if(SSObjU.isNull(userUri, entityUri, locationString)){
      SSServErrReg.regErrThrow(new Exception("pars not okay"));
      return null;
    }
    
    final List<String>        locations          = new ArrayList<String>();
    final List<String>        tableNames         = new ArrayList<String>();
    final List<String>        columnNames        = new ArrayList<String>();
    final Map<String, String> whereParsAndValues = new HashMap<String, String>();
    ResultSet                 resultSet          = null;
    SSUri                     locationUri        = null;
    
    try{
      
      if(locationString != null){
        
        if(!existsLocationString(locationString)){
          return locations;
        }
        
        locationUri = getOrCreateLocationUri(true, locationString);
      }
      
      tableNames.add (locationsTable);
      tableNames.add (entityTable);
      columnNames.add(SSSQLVarU.locationId);
      columnNames.add(SSSQLVarU.entityId);
      columnNames.add(SSSQLVarU.userId);
      columnNames.add(SSSQLVarU.label);
      
      if(userUri != null){
        whereParsAndValues.put(SSSQLVarU.userId, userUri.toString());
      }
      
      if(entityUri != null){
        whereParsAndValues.put(SSSQLVarU.entityId, entityUri.toString());
      }
      
      if(locationUri != null){
        whereParsAndValues.put(SSSQLVarU.locationId, locationUri.toString());
      }
      
      resultSet =
        dbSQL.selectCertainWhere(
        tableNames,
        columnNames,
        whereParsAndValues,
        SSSQLVarU.locationId + SSStrU.equal + SSSQLVarU.id);
      
      while(resultSet.next()){
        
        if(!locations.contains(bindingStr(resultSet, SSSQLVarU.label))){
          locations.add(bindingStr(resultSet, SSSQLVarU.label));
        }
      }  
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return locations;
  }
  
  public SSUri createLocationURI() throws Exception{
   return SSUri.get(SSIDU.uniqueID(objLocation().toString()));
  }
  
  private SSUri objLocation() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityE.location.toString());
  } 
}
