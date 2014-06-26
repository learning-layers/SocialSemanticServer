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
package at.kc.tugraz.ss.service.rating.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class SSRatingSQLFct extends SSDBSQLFct{

  protected final String ratingAssTable = "ratingass";
  
  public SSRatingSQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }
  
public void deleteRatingAss(
    final SSUri userUri,
    final SSUri entityUri) throws Exception{
    
    final Map<String, String> whereParNamesWithValues = new HashMap<>();
    
    if(userUri != null){
      whereParNamesWithValues.put(SSSQLVarU.userId,   userUri.toString());
    }
    
    if(entityUri != null){
      whereParNamesWithValues.put(SSSQLVarU.entityId, entityUri.toString());
    }
    
    if(whereParNamesWithValues.isEmpty()){
      dbSQL.delete(ratingAssTable);
    }else{
      dbSQL.delete(ratingAssTable, whereParNamesWithValues);
    }
  }

  public Boolean hasUserRatedEntity(
    final SSUri   userUri, 
    final SSUri   entityUri) throws Exception{
    
    if(SSObjU.isNull(userUri, entityUri)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
     
    final Map<String, String> selectPars  = new HashMap<>();
    ResultSet                 resultSet   = null;
    
    selectPars.put(SSSQLVarU.userId,   userUri.toString());
    selectPars.put(SSSQLVarU.entityId, entityUri.toString());
    
    try{
      
      resultSet = dbSQL.select(ratingAssTable, selectPars);
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void rateEntityByUser(
    final SSUri   ratingUri, 
    final SSUri   userUri, 
    final SSUri   entityUri, 
    final Integer ratingValue) throws Exception{
   
    if(SSObjU.isNull(ratingUri, userUri, entityUri, ratingValue)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return;
    }
        
    final Map<String, String> insertPars = new HashMap<>();

    insertPars.put(SSSQLVarU.ratingId,     ratingUri.toString());
    insertPars.put(SSSQLVarU.userId,       userUri.toString());
    insertPars.put(SSSQLVarU.entityId,     entityUri.toString());
    insertPars.put(SSSQLVarU.ratingValue,  ratingValue.toString());
    
    dbSQL.insert(ratingAssTable, insertPars);
  }
  
  public Integer getUserRating(
    final SSUri userUri, 
    final SSUri entityUri) throws Exception{
    
    if(SSObjU.isNull(userUri, entityUri)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
    
    Integer                 ratingValue = 0;
    int                     counter     = 0;
    HashMap<String, String> selectPars  = new HashMap<>();
    ResultSet               resultSet   = null;
    
    selectPars.put(SSSQLVarU.userId,   userUri.toString());
    selectPars.put(SSSQLVarU.entityId, entityUri.toString());
    
    try{
      resultSet = dbSQL.select(ratingAssTable, selectPars);
      
      while(resultSet.next()){
        ratingValue += bindingStrToInteger(resultSet, SSSQLVarU.ratingValue);
        counter++;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    if(counter == 0){
      return ratingValue;
    }else{
      return ratingValue / counter;
    }    
  }

  public SSRatingOverall getOverallRating(final SSUri entityUri) throws Exception{
    
    if(SSObjU.isNull(entityUri)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
    
    Double                  ratingValue  = 0d;
    int                     counter      = 0;
    HashMap<String, String> selectPars   = new HashMap<>();
    ResultSet               resultSet    = null;
    
    selectPars.put(SSSQLVarU.entityId, entityUri.toString());
    
    try{
      resultSet = dbSQL.select(ratingAssTable, selectPars);
      
      while(resultSet.next()){
        ratingValue += bindingStrToInteger(resultSet, SSSQLVarU.ratingValue);
        counter++;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    if(counter != 0){
      ratingValue = ratingValue / counter;
    }  
    
    return SSRatingOverall.get(ratingValue, counter);
  }
  
  public SSUri createRatingUri() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objRating().toString()));
  }
  
  private SSUri objRating() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityE.rating.toString());
  }  
}

