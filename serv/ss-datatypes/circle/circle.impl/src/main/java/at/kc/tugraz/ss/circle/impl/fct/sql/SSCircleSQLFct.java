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
package at.kc.tugraz.ss.circle.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.circle.impl.fct.misc.SSCircleMiscFct;
import at.kc.tugraz.ss.datatypes.datatypes.SSCircleE;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sss.serv.err.datatypes.SSErr;
import sss.serv.err.datatypes.SSErrE;

public class SSCircleSQLFct extends SSDBSQLFct{
  
  public SSCircleSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  public List<SSUri> getCircleURIs(
    final Boolean withSystemCircles) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.isSystemCircle, withSystemCircles);
      
      resultSet = dbSQL.select(circleTable, wheres);
      
      return getURIsFromResult(resultSet, SSSQLVarU.circleId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getCircleURIsForUser(
    final SSUri   userUri,
    final Boolean withSystemCircles) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSUri>         circleUris  = new ArrayList<>();
      final Map<String, String> wheres      = new HashMap<>();
      final List<SSUri>         tmpCircleUris;
      
      where(wheres, SSSQLVarU.userId, userUri);
      
      resultSet = dbSQL.select(circleUsersTable, wheres);
      
      tmpCircleUris = getURIsFromResult(resultSet, SSSQLVarU.circleId);
      
      if(withSystemCircles){
        return tmpCircleUris;
      }else{
        
        for(SSUri circleUri : tmpCircleUris){
          
          if(!isSystemCircle(circleUri)){
            circleUris.add(circleUri);
          }
        }
        
        return circleUris;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addCircle(
    final SSUri     circleUri,
    final SSCircleE circleType,
    final Boolean   isSystemCircle) throws Exception{
    
    try{
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarU.circleId,       circleUri);
      insert(inserts, SSSQLVarU.circleType,     circleType);
      insert(inserts, SSSQLVarU.isSystemCircle, isSystemCircle);
      
      dbSQL.insert(circleTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addUserToCircleIfNotExists(
    final SSUri circleUri,
    final SSUri userUri) throws Exception{
    
    try{
      
      if(hasCircleUser(circleUri, userUri)){
        return;
      }
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarU.circleId, circleUri);
      insert(inserts, SSSQLVarU.userId,   userUri);
      
      dbSQL.insert(circleUsersTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addEntityToCircleIfNotExists(
    final SSUri circleUri,
    final SSUri entityUri) throws Exception{
    
    try{
      
      if(hasCircleEntity(circleUri, entityUri)){
        return;
      }
      
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarU.circleId, circleUri);
      insert(inserts, SSSQLVarU.entityId, entityUri);
      
      dbSQL.insert(circleEntitiesTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSEntity> getUsersForCircle(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String>   wheres    = new HashMap<>();
      
      where(wheres, SSSQLVarU.circleId, circleUri);
      
      resultSet = dbSQL.select(circleUsersTable, wheres);
      
      return SSEntity.get(getURIsFromResult(resultSet, SSSQLVarU.userId), SSEntityE.user);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSEntity> getEntitiesForCircle(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String>   wheres      = new HashMap<>();
      
      where(wheres, SSSQLVarU.circleId, circleUri);
      
      resultSet = dbSQL.select(circleEntitiesTable, wheres);
      
      return SSEntity.get(getURIsFromResult(resultSet, SSSQLVarU.entityId), SSEntityE.entity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSCircleE> getCircleTypesForEntity(
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>              tables            = new ArrayList<>();
      final Map<String, String>       wheres            = new HashMap<>();
      final List<String>              columns           = new ArrayList<>();
      final List<String>              tableCons         = new ArrayList<>();
      
      table    (tables,    circleEntitiesTable);
      table    (tables,    circleTable);
      column   (columns,   SSSQLVarU.circleType);
      where    (wheres,    SSSQLVarU.entityId, entityUri);
      tableCon (tableCons, circleTable, SSSQLVarU.circleId, circleEntitiesTable, SSSQLVarU.circleId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null);
      
      return SSCircleE.get(getStringsFromResult(resultSet, SSSQLVarU.circleType));
      
//        if(!SSCircleE.contains(circleTypes, circleType)){
//          circleTypes.add(circleType);
//        }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSCircleE> getCircleTypesCommonForUserAndEntity(
    final SSUri userUri,
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>              tables       = new ArrayList<>();
      final Map<String, String>       wheres       = new HashMap<>();
      final List<String>              columns      = new ArrayList<>();
      final List<String>              tableCons    = new ArrayList<>();
      
      table    (tables,    circleUsersTable);
      table    (tables,    circleEntitiesTable);
      table    (tables,    circleTable);
      column   (columns,   SSSQLVarU.circleType);
      where    (wheres,    SSSQLVarU.entityId, entityUri);
      where    (wheres,    SSSQLVarU.userId,   userUri);
      tableCon (tableCons, circleTable, SSSQLVarU.circleId, circleEntitiesTable, SSSQLVarU.circleId);
      tableCon (tableCons, circleTable, SSSQLVarU.circleId, circleUsersTable,    SSSQLVarU.circleId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null);
      
      return SSCircleE.get(getStringsFromResult(resultSet, SSSQLVarU.circleType));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSEntityCircle> getCirclesCommonForUserAndEntity(
    final SSUri   userUri,
    final SSUri   entityUri,
    final Boolean withSystemCircles) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSEntityCircle>      circles    = new ArrayList<>();
      final List<String>              tables     = new ArrayList<>();
      final Map<String, String>       wheres     = new HashMap<>();
      final List<String>              columns    = new ArrayList<>();
      final List<String>              tableCons  = new ArrayList<>();
      SSEntityCircle                  circleObj;
      
      table     (tables,  circleUsersTable);
      table     (tables,  circleEntitiesTable);
      table     (tables,  circleTable);
      table     (tables,  entityTable);
      column    (columns, SSSQLVarU.label);
      column    (columns, SSSQLVarU.description);
      column    (columns, SSSQLVarU.circleType);
      column    (columns, SSSQLVarU.isSystemCircle);
      column    (columns, circleTable,        SSSQLVarU.circleId);
      where     (wheres,  SSSQLVarU.entityId, entityUri);
      where     (wheres,  SSSQLVarU.userId,   userUri);
      
      if(!withSystemCircles){
        where     (wheres,  SSSQLVarU.isSystemCircle, false);
      }
      
      tableCon  (tableCons, circleTable, SSSQLVarU.circleId, circleEntitiesTable, SSSQLVarU.circleId);
      tableCon  (tableCons, circleTable, SSSQLVarU.circleId, circleUsersTable,    SSSQLVarU.circleId);
      tableCon  (tableCons, circleTable, SSSQLVarU.circleId, entityTable,         SSSQLVarU.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null);
      
      while(resultSet.next()){
        
        circleObj =
          SSEntityCircle.get(
            bindingStrToUri         (resultSet, SSSQLVarU.circleId),
            bindingStrToLabel       (resultSet, SSSQLVarU.label),
            SSCircleE.get           (bindingStr(resultSet, SSSQLVarU.circleType)),
            bindingStrToBoolean     (resultSet,SSSQLVarU.isSystemCircle));
        
        circleObj.description = bindingStrToTextComment (resultSet, SSSQLVarU.description);
        
        circleObj.users.addAll(getUsersForCircle(circleObj.id));
        
        circles.add(circleObj);
      }
      
      return circles;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public Boolean isSystemCircle(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where (wheres,    SSSQLVarU.circleId, circleUri);
      
      resultSet = dbSQL.select(circleTable, wheres);
      
      checkFirstResult(resultSet);
      
      return bindingStrToBoolean(resultSet, SSSQLVarU.isSystemCircle);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSEntityCircle getCircle(
    final SSUri                circleUri,
    final Boolean              withUsers,
    final Boolean              withEntities,
    final Boolean              withCircleRights) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        tables            = new ArrayList<>();
      final List<String>        columns           = new ArrayList<>();
      final List<String>        tableCons         = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      final SSEntityCircle      circleObj;
      
      table    (tables,    circleTable);
      table    (tables,    entityTable);
      column   (columns,   SSSQLVarU.label);
      column   (columns,   SSSQLVarU.description);
      column   (columns,   SSSQLVarU.circleId);
      column   (columns,   SSSQLVarU.circleType);
      column   (columns,   SSSQLVarU.isSystemCircle);
      where    (wheres,    SSSQLVarU.circleId, circleUri);
      tableCon (tableCons, circleTable,        SSSQLVarU.circleId, entityTable, SSSQLVarU.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null);
      
      checkFirstResult(resultSet);
      
      circleObj =
        SSEntityCircle.get(
          circleUri,
          bindingStrToLabel       (resultSet, SSSQLVarU.label),
          SSCircleE.get           (bindingStr(resultSet, SSSQLVarU.circleType)),
          bindingStrToBoolean     (resultSet,SSSQLVarU.isSystemCircle));
      
      circleObj.description = bindingStrToTextComment (resultSet, SSSQLVarU.description);
      
      if(withUsers){
        circleObj.users.addAll(getUsersForCircle(circleObj.id));
      }
      
      if(withEntities){
        circleObj.entities.addAll(getEntitiesForCircle(circleUri));
      }
      
      if(withCircleRights){
        circleObj.accessRights.addAll(SSCircleMiscFct.getCircleRights(circleObj.circleType));
      }
      
      return circleObj;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getCircleURIsForEntity(
    final SSUri   entityUri,
    final Boolean withSystemCircles) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String>   wheres      = new HashMap<>();
      final List<String>          columns     = new ArrayList<>();
      final List<SSUri>           circleUris  = new ArrayList<>();
      final List<SSUri>           tmpCircleUris;
      
      column (columns, SSSQLVarU.circleId);
      where  (wheres,  SSSQLVarU.entityId, entityUri);
      
      resultSet = dbSQL.select(circleEntitiesTable, columns, wheres, null, null);
      
      tmpCircleUris = getURIsFromResult(resultSet, SSSQLVarU.circleId);
      
      if(withSystemCircles){
        return tmpCircleUris;
      }else{
        
        for(SSUri circleUri : tmpCircleUris){
          
          if(!isSystemCircle(circleUri)){
            circleUris.add(circleUri);
          }
        }
        
        return circleUris;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSCircleE getTypeForCircle(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String>   wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.circleId, circleUri);
      
      resultSet = dbSQL.select(circleTable, wheres);
      
      checkFirstResult(resultSet);
      
      return SSCircleE.get(bindingStr(resultSet, SSSQLVarU.circleType));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public Boolean isUserInCircle(
    final SSUri          userUri,
    final SSUri          circleUri) throws Exception{
    
    try{
      return SSStrU.contains(getCircleURIsForUser(userUri, true), circleUri);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public Boolean isGroupCircle(
    final SSUri circleUri) throws Exception{
    
    try{
      return SSCircleE.equals(getTypeForCircle(circleUri), SSCircleE.group);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private Boolean hasCircleEntity(
    final SSUri circleUri,
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.circleId, circleUri);
      where(wheres, SSSQLVarU.entityId, entityUri);
      
      resultSet = dbSQL.select(circleEntitiesTable, wheres);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  private Boolean hasCircleUser(
    final SSUri circleUri,
    final SSUri userUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.circleId, circleUri);
      where(wheres, SSSQLVarU.userId,   userUri);
      
      resultSet = dbSQL.select(circleUsersTable, wheres);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri getPrivCircleURI(
    final SSUri     user) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        tables            = new ArrayList<>();
      final List<String>        columns           = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      final List<String>        tableCons         = new ArrayList<>();
      
      column   (columns,   SSSQLVarU.circleId);
      table    (tables,    circleTable);
      table    (tables,    entityTable);
      
      where(wheres, SSSQLVarU.circleType, SSCircleE.priv);
      where(wheres, SSSQLVarU.author,     user);
      
      tableCon (tableCons, circleTable, SSSQLVarU.circleId, entityTable, SSSQLVarU.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarU.circleId);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
        SSServErrReg.regErrThrow(new SSErr(SSErrE.entityDoesntExist), false);
        return null;
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri getPubCircleURI() throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.circleType, SSCircleE.pub);
      
      resultSet = dbSQL.select(circleTable, wheres);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarU.circleId);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
        SSServErrReg.regErrThrow(new SSErr(SSErrE.entityDoesntExist), false);
        return null;
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
