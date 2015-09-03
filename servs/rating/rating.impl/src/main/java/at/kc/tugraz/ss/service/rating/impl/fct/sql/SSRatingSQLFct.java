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

import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSUri;

import at.tugraz.sss.serv.SSServImplWithDBA;
import at.kc.tugraz.ss.service.rating.datatypes.SSRating;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSRatingSQLFct extends SSDBSQLFct{

  public SSRatingSQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }
  
  public List<SSUri> getEntitiesRated(
    final SSUri user) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.entityId);
      
      where(wheres, SSSQLVarNames.userId, user);
      
      resultSet = dbSQL.select(SSSQLVarNames.ratingsTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.entityId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void deleteRatingAss(
    final SSUri user,
    final SSUri entityUri) throws Exception{
    
    try{
      final Map<String, String> deletes = new HashMap<>();

      if(user != null){
        delete(deletes, SSSQLVarNames.userId, user);
      }

      if(entityUri != null){
        delete(deletes, SSSQLVarNames.entityId, entityUri);
      }

      if(deletes.isEmpty()){
        dbSQL.delete(SSSQLVarNames.ratingsTable);
      }else{
        dbSQL.delete(SSSQLVarNames.ratingsTable, deletes);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public Boolean hasUserRatedEntity(
    final SSUri   userUri, 
    final SSUri   entityUri) throws Exception{
    
    if(SSObjU.isNull(userUri, entityUri)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
    
    ResultSet                 resultSet   = null;
    
    final List<String>        columns = new ArrayList<>();
    final Map<String, String> wheres  = new HashMap<>();
    
    try{

      column(columns, SSSQLVarNames.entityId);
      
      where(wheres, SSSQLVarNames.userId,   userUri);
      where(wheres, SSSQLVarNames.entityId, entityUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.ratingsTable, columns, wheres, null, null, null);
      
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
    final Integer ratingValue, 
    final Boolean userRatedEntityBefore) throws Exception{
   
    if(userRatedEntityBefore){
      
      final HashMap<String, String> wheres      = new HashMap<>();
      final HashMap<String, String> updates      = new HashMap<>();
      
      where(wheres, SSSQLVarNames.userId,        userUri);
      where(wheres, SSSQLVarNames.entityId,      entityUri);
      
      update(updates, SSSQLVarNames.ratingValue, ratingValue);
      
      dbSQL.update(SSSQLVarNames.ratingsTable, wheres, updates);
    }else{
    
      final Map<String, String> inserts= new HashMap<>();

      insert(inserts, SSSQLVarNames.ratingId,     ratingUri);
      insert(inserts, SSSQLVarNames.userId,       userUri);
      insert(inserts, SSSQLVarNames.entityId,     entityUri);
      insert(inserts, SSSQLVarNames.ratingValue,  ratingValue);

      dbSQL.insert(SSSQLVarNames.ratingsTable, inserts);
    }
  }
  
  public List<SSRating> getUserRatings(
    final SSUri userUri) throws Exception{
    
    ResultSet resultSet   = null;
    
    try{
      
      final List<SSRating>          ratings     = new ArrayList<>();
      final List<String>            columns     = new ArrayList<>();
      final HashMap<String, String> wheres      = new HashMap<>();
      
      column(columns, SSSQLVarNames.ratingId);
      column(columns, SSSQLVarNames.ratingValue);
      column(columns, SSSQLVarNames.entityId);
      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.userId,   userUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.ratingsTable, columns, wheres, null, null, null);
      
      while(resultSet.next()){
        
        ratings.add(SSRating.get(bindingStrToUri     (resultSet, SSSQLVarNames.ratingId), 
            bindingStrToInteger (resultSet, SSSQLVarNames.ratingValue), 
            bindingStrToUri     (resultSet, SSSQLVarNames.entityId), 
            bindingStrToUri     (resultSet, SSSQLVarNames.userId), 
            0L));
      }
      
      return ratings;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public Integer getUserRating(
    final SSUri userUri, 
    final SSUri entityUri) throws Exception{
    
    if(SSObjU.isNull(userUri, entityUri)){
      throw new SSErr(SSErrE.parameterMissing);
    }
    
    ResultSet               resultSet   = null;
    
    final List<String>        columns     = new ArrayList<>();
    final Map<String, String> wheres      = new HashMap<>();
    Integer                   ratingValue = 0;
    int                       counter     = 0;
    
    column(columns, SSSQLVarNames.ratingId);
    column(columns, SSSQLVarNames.ratingValue);
    
    where   (wheres, SSSQLVarNames.userId,   userUri);
    where   (wheres, SSSQLVarNames.entityId, entityUri);
    
    try{
      resultSet = dbSQL.select(SSSQLVarNames.ratingsTable, columns, wheres, null, null, null);
      
      while(resultSet.next()){
        ratingValue += bindingStrToInteger(resultSet, SSSQLVarNames.ratingValue);
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
    
    final List<String>        columns      = new ArrayList<>();
    final Map<String, String> wheres       = new HashMap<>();
    Double                    ratingValue  = 0d;
    int                       counter      = 0;
    ResultSet                 resultSet    = null;
    
    column(columns, SSSQLVarNames.ratingId);
    column(columns, SSSQLVarNames.ratingValue);
    
    where(wheres, SSSQLVarNames.entityId, entityUri);
    
    try{
      resultSet = dbSQL.select(SSSQLVarNames.ratingsTable, columns, wheres, null, null, null);
      
      while(resultSet.next()){
        ratingValue += bindingStrToInteger(resultSet, SSSQLVarNames.ratingValue);
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
}

