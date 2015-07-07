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

import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.circle.impl.fct.misc.SSCircleMiscFct;
import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSEntityA;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSTextComment;

public class SSCircleSQLFct extends SSDBSQLFct{
  
  public SSCircleSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  //remove this entity service duplication
  public void addEntity(
    final SSUri         entity,
    final SSEntityE     entityType,
    final SSUri         authorUri) throws Exception{
    
    final Map<String, String> inserts    = new HashMap<>();
    
    insert    (inserts,     SSSQLVarNames.id, entity);
    
    insert(inserts, SSSQLVarNames.creationTime, SSDateU.dateAsLong());
    
    insert(inserts, SSSQLVarNames.label, SSStrU.empty);
    
    if(entityType == null){
      insert(inserts, SSSQLVarNames.type, SSEntityE.entity);
    }else{
      insert(inserts, SSSQLVarNames.type, entityType);
    }
    
    if(authorUri == null){
      insert(inserts, SSSQLVarNames.author, SSStrU.empty);
    }else{
      insert(inserts, SSSQLVarNames.author, authorUri);
    }
    
    insert(inserts, SSSQLVarNames.description, SSStrU.empty);
    
    dbSQL.insert(SSSQLVarNames.entityTable, inserts);
  }
  
  public List<SSUri> getCircleURIs(
    final Boolean withSystemCircles) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      
      where(wheres, SSSQLVarNames.isSystemCircle, withSystemCircles);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.circleId);
      
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
      
      insert(inserts, SSSQLVarNames.circleId,       circleUri);
      insert(inserts, SSSQLVarNames.circleType,     circleType);
      insert(inserts, SSSQLVarNames.isSystemCircle, isSystemCircle);
      
      dbSQL.insert(SSSQLVarNames.circleTable, inserts);
      
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
      
      insert(inserts, SSSQLVarNames.circleId, circleUri);
      insert(inserts, SSSQLVarNames.userId,   userUri);
      
      dbSQL.insert(SSSQLVarNames.circleUsersTable, inserts);
      
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
      
      insert(inserts, SSSQLVarNames.circleId, circleUri);
      insert(inserts, SSSQLVarNames.entityId, entityUri);
      
      dbSQL.insert(SSSQLVarNames.circleEntitiesTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSEntity> getUsersForCircle(
    final SSUri circleUri) throws Exception{
    
    try{
      return SSEntity.get(getUserURIsForCircle(circleUri), SSEntityE.user);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSUri> getUserURIsForCircle(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.circleId, circleUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleUsersTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.userId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
//  public List<SSEntity> getEntitiesForCircle(
//    final SSUri circleUri) throws Exception{
//    
//    ResultSet resultSet = null;
//    
//    try{
//      final List<String>          columns   = new ArrayList<>();
//      final Map<String, String>   wheres    = new HashMap<>();
//      
//      column(columns, SSSQLVarU.entityId);
//      column(columns, SSSQLVarU.circleId);
//      
//      where(wheres, SSSQLVarU.circleId, circleUri);
//      
//      resultSet = dbSQL.select(circleEntitiesTable, columns, wheres, null, null, null);
//      
//      return SSEntity.get(
//        getURIsFromResult(resultSet, SSSQLVarU.entityId), 
//        SSEntityE.entity);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
  
  public List<SSCircleE> getCircleTypesForEntity(
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>              tables            = new ArrayList<>();
      final Map<String, String>       wheres            = new HashMap<>();
      final List<String>              columns           = new ArrayList<>();
      final List<String>              tableCons         = new ArrayList<>();
      
      table    (tables, SSSQLVarNames.circleEntitiesTable);
      table    (tables, SSSQLVarNames.circleTable);
      column   (columns,   SSSQLVarNames.circleType);
      where    (wheres,    SSSQLVarNames.entityId, entityUri);
      tableCon (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.circleId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return SSCircleE.get(getStringsFromResult(resultSet, SSSQLVarNames.circleType));
      
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
      
      table    (tables, SSSQLVarNames.circleUsersTable);
      table    (tables, SSSQLVarNames.circleEntitiesTable);
      table    (tables, SSSQLVarNames.circleTable);
      column   (columns,   SSSQLVarNames.circleType);
      where    (wheres,    SSSQLVarNames.entityId, entityUri);
      where    (wheres,    SSSQLVarNames.userId,   userUri);
      tableCon (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.circleId);
      tableCon (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleUsersTable,    SSSQLVarNames.circleId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return SSCircleE.get(getStringsFromResult(resultSet, SSSQLVarNames.circleType));
      
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
      
      table     (tables, SSSQLVarNames.circleUsersTable);
      table     (tables, SSSQLVarNames.circleEntitiesTable);
      table     (tables, SSSQLVarNames.circleTable);
      table     (tables, SSSQLVarNames.entityTable);
      column    (columns, SSSQLVarNames.label);
      column    (columns, SSSQLVarNames.description);
      column    (columns, SSSQLVarNames.circleType);
      column    (columns, SSSQLVarNames.isSystemCircle);
      column    (columns, SSSQLVarNames.circleTable,        SSSQLVarNames.circleId);
      where     (wheres,  SSSQLVarNames.entityId, entityUri);
      where     (wheres,  SSSQLVarNames.userId,   userUri);
      
      if(!withSystemCircles){
        where     (wheres,  SSSQLVarNames.isSystemCircle, false);
      }
      
      tableCon  (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.circleId);
      tableCon  (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleUsersTable,    SSSQLVarNames.circleId);
      tableCon  (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.entityTable,         SSSQLVarNames.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        circleObj =
          SSEntityCircle.get(bindingStrToUri         (resultSet, SSSQLVarNames.circleId),
            bindingStrToLabel       (resultSet, SSSQLVarNames.label),
            SSCircleE.get           (bindingStr(resultSet, SSSQLVarNames.circleType)),
            bindingStrToBoolean     (resultSet,SSSQLVarNames.isSystemCircle));
        
        circleObj.description = bindingStrToTextComment (resultSet, SSSQLVarNames.description);
        
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
      
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.isSystemCircle);
      
      where (wheres,    SSSQLVarNames.circleId, circleUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStrToBoolean(resultSet, SSSQLVarNames.isSystemCircle);
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
    final Boolean              withCircleRights,
    final List<SSEntityE>      entityTypesToIncludeOnly) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        tables            = new ArrayList<>();
      final List<String>        columns           = new ArrayList<>();
      final List<String>        tableCons         = new ArrayList<>();
      final Map<String, String> wheres            = new HashMap<>();
      final SSEntityCircle      circleObj;
      
      table    (tables, SSSQLVarNames.circleTable);
      table    (tables, SSSQLVarNames.entityTable);
      column   (columns,   SSSQLVarNames.label);
      column   (columns,   SSSQLVarNames.description);
      column   (columns,   SSSQLVarNames.circleId);
      column   (columns,   SSSQLVarNames.circleType);
      column   (columns,   SSSQLVarNames.isSystemCircle);
      where    (wheres,    SSSQLVarNames.circleId, circleUri);
      tableCon (tableCons, SSSQLVarNames.circleTable,        SSSQLVarNames.circleId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      checkFirstResult(resultSet);
      
      circleObj =
        SSEntityCircle.get(circleUri,
          bindingStrToLabel       (resultSet, SSSQLVarNames.label),
          SSCircleE.get           (bindingStr(resultSet, SSSQLVarNames.circleType)),
          bindingStrToBoolean     (resultSet,SSSQLVarNames.isSystemCircle));
      
      circleObj.description = bindingStrToTextComment (resultSet, SSSQLVarNames.description);
      
      if(withUsers){
        circleObj.users.addAll(getUsersForCircle(circleObj.id));
      }
      
      if(withEntities){
        
        circleObj.entities.addAll(
          getEntitiesForCircle(
            circleUri, 
            entityTypesToIncludeOnly));
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
      
      column (columns, SSSQLVarNames.circleId);
      where  (wheres,  SSSQLVarNames.entityId, entityUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleEntitiesTable, columns, wheres, null, null, null);
      
      tmpCircleUris = getURIsFromResult(resultSet, SSSQLVarNames.circleId);
      
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
      
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      column(columns, SSSQLVarNames.circleType);
      
      where(wheres, SSSQLVarNames.circleId, circleUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      return SSCircleE.get(bindingStr(resultSet, SSSQLVarNames.circleType));
      
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
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      column(columns, SSSQLVarNames.entityId);
      
      where(wheres, SSSQLVarNames.circleId, circleUri);
      where(wheres, SSSQLVarNames.entityId, entityUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleEntitiesTable, columns, wheres, null, null, null);
      
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
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.circleId, circleUri);
      where(wheres, SSSQLVarNames.userId,   userUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleUsersTable, columns, wheres, null, null, null);
      
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
      
      column   (columns,   SSSQLVarNames.circleId);
      table    (tables, SSSQLVarNames.circleTable);
      table    (tables, SSSQLVarNames.entityTable);
      
      where(wheres, SSSQLVarNames.circleType, SSCircleE.priv);
      where(wheres, SSSQLVarNames.author,     user);
      
      tableCon (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarNames.circleId);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
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
      final List<String>          columns   = new ArrayList<>();
      final Map<String, String>   wheres    = new HashMap<>();
      
      column(columns, SSSQLVarNames.circleId);
      column(columns, SSSQLVarNames.circleType);
      
      where(wheres, SSSQLVarNames.circleType, SSCircleE.pub);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarNames.circleId);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlNoResultFound)){
        return null;
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void removeEntity(
    final SSUri circle,
    final SSUri entity) throws Exception{
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.circleId, circle);
      where(wheres, SSSQLVarNames.entityId, entity);
      
      dbSQL.delete(SSSQLVarNames.circleEntitiesTable, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public List<SSUri> getCircleURIsForUser(
    final SSUri   userUri,
    final Boolean withSystemCircles) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<SSUri>         circleUris  = new ArrayList<>();
      final List<String>        columns     = new ArrayList<>();
      final Map<String, String> wheres      = new HashMap<>();
      final List<SSUri>         tmpCircleUris;
      
      column(columns, SSSQLVarNames.circleId);
      
      where(wheres, SSSQLVarNames.userId, userUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.circleUsersTable, columns, wheres, null, null, null);
      
      tmpCircleUris = getURIsFromResult(resultSet, SSSQLVarNames.circleId);
      
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
  
  public List<SSEntity> getEntitiesForCircle(
    final SSUri           circleUri,
    final List<SSEntityE> types) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<MultivaluedMap<String, String>> wheres         = new ArrayList<>();
      final List<String>                         tables         = new ArrayList<>();
      final List<String>                         columns        = new ArrayList<>();
      final List<String>                         tableCons      = new ArrayList<>();
      
      column(columns, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.entityId);
      
      table    (tables, SSSQLVarNames.entityTable);
      table    (tables, SSSQLVarNames.circleEntitiesTable);

      tableCon (tableCons, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.entityId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      if(circleUri == null){
        throw new Exception("circle has to be given");
      }
        
      final MultivaluedMap<String, String> whereCircles = new MultivaluedHashMap<>();
      
      where(whereCircles, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.circleId, circleUri);
      
      wheres.add(whereCircles);
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSEntityE type : types){
          where(whereTypes, SSSQLVarNames.entityTable, SSSQLVarNames.type, type);
        }
        
        wheres.add(whereTypes);
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      return SSEntity.get(getURIsFromResult(resultSet, SSSQLVarNames.entityId), 
        SSEntityE.entity);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
