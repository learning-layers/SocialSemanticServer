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
package sss.serv.eval.impl.fct.sql;

import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSServImplWithDBA;

public class SSEvalSQLFct extends SSDBSQLFct{
  
  public SSEvalSQLFct(SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }
  
//  public List<SSUri> getLearnEpUserURIs(
//    final SSUri learnEp) throws Exception{
//    
//    ResultSet resultSet = null;
//    
//    try{
//      final List<String>        columns = new ArrayList<>();
//      final Map<String, String> wheres  = new HashMap<>();
//      
//      column(columns, SSSQLVarU.userId);
//      
//      where(wheres, SSSQLVarU.learnEpId, learnEp);
//    
//      resultSet = dbSQL.select(learnEpUserTable, columns, wheres, null, null, null);
//
//      return getURIsFromResult(resultSet, SSSQLVarU.userId);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
//  
//  public Boolean ownsUserLearnEp(
//    final SSUri user, 
//    final SSUri learnEp) throws Exception {
//    
//    ResultSet resultSet = null;
//    
//    try{
//
//      final List<String>        columns = new ArrayList<>();
//      final Map<String, String> wheres  = new HashMap<>();
//      
//      column(columns, SSSQLVarU.learnEpId);
//      
//      where(wheres, SSSQLVarU.userId,    user);
//      where(wheres, SSSQLVarU.learnEpId, learnEp);
//    
//      resultSet = dbSQL.select(learnEpUserTable, columns, wheres, null, null, null);
//
//      return resultSet.first();
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }  
//  
//  public SSLearnEp getLearnEpWithoutVersions(
//    final SSUri user, 
//    final SSUri learnEpUri) throws Exception{
//    
//    ResultSet resultSet  = null;
//    
//    try{
//      
//      final Map<String, String> wheres    = new HashMap<>();
//      final List<String>        columns   = new ArrayList<>();
//      final List<String>        tables    = new ArrayList<>();
//      final List<String>        tableCons = new ArrayList<>();
//      final SSLearnEp           learnEpObj;
//      
//      column(columns, SSSQLVarU.author);
//      column(columns, SSSQLVarU.label);
//      column(columns, SSSQLVarU.description);
//      
//      where(wheres, SSSQLVarU.learnEpId, learnEpUri);
//      where(wheres, SSSQLVarU.userId,    user);
//      
//      table(tables, learnEpUserTable);
//      table(tables, entityTable);
//      
//      tableCon(tableCons, entityTable, SSSQLVarU.id, learnEpUserTable, SSSQLVarU.learnEpId);
//      
//      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
//      
//      checkFirstResult(resultSet);
//      
//      learnEpObj =
//        SSLearnEp.get(
//          learnEpUri,
//          bindingStrToLabel(resultSet, SSSQLVarU.label),
//          user,
//          bindingStrToUri(resultSet, SSSQLVarU.author),
//          new ArrayList<>());
//      
//      learnEpObj.description = bindingStrToTextComment(resultSet, SSSQLVarU.description);
//
//      return learnEpObj;
//    
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
//  
//  public SSLearnEp getLearnEpWithVersions(
//    final SSUri           user,
//    final SSUri           learnEpUri,
//    final Boolean         withTimelineState) throws Exception{
//    
//    try{
//      final SSLearnEp learnEp = getLearnEpWithoutVersions(user, learnEpUri);
//      
//      for(SSUri learnEpVersionUri : getLearnEpVersionURIs(learnEpUri)){
//        
//        learnEp.versions.add(
//          getLearnEpVersion(
//            learnEpVersionUri,
//            withTimelineState));
//      }      
//
//      return learnEp;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
//  
//  public List<SSLearnEp> getLearnEps(
//    final SSUri           user) throws Exception{
//    
//    ResultSet resultSet  = null;
//    
//    try{
//      
//      final List<SSLearnEp>     learnEps  = new ArrayList<>();
//      final Map<String, String> wheres    = new HashMap<>();
//      final List<String>        columns   = new ArrayList<>();
//      final List<String>        tables    = new ArrayList<>();
//      final List<String>        tableCons = new ArrayList<>();
//      SSLearnEp                 learnEpObj;
//      
//      column(columns, SSSQLVarU.author);
//      column(columns, SSSQLVarU.learnEpId);
//      column(columns, SSSQLVarU.label);
//      column(columns, SSSQLVarU.description);
//      
//      table(tables, entityTable);
//      table(tables, learnEpUserTable);
//      
//      where(wheres, SSSQLVarU.userId, user);
//      
//      tableCon(tableCons, entityTable, SSSQLVarU.id, learnEpUserTable, SSSQLVarU.learnEpId);
//      
//      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
//      
//      while(resultSet.next()){
//        
//        learnEpObj =
//          SSLearnEp.get(
//            bindingStrToUri   (resultSet, SSSQLVarU.learnEpId),
//            bindingStrToLabel (resultSet, SSSQLVarU.label),
//            user,
//            bindingStrToUri(resultSet, SSSQLVarU.author),
//            new ArrayList<>());
//        
//        learnEpObj.description = bindingStrToTextComment(resultSet, SSSQLVarU.description);
//        
//        learnEps.add(learnEpObj);
//      }
//      
//      return learnEps;
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
//  
//  public List<SSUri> getLearnEpURIs(
//    final SSUri user) throws Exception{
//    
//    ResultSet           resultSet  = null;
//    
//    try{
//      final List<String>        columns = new ArrayList<>();
//      final Map<String, String> wheres  = new HashMap<>();
//      
//      column(columns,  SSSQLVarU.learnEpId);
//      
//      where(wheres, SSSQLVarU.userId, user);
//      
//      resultSet = dbSQL.select(learnEpUserTable, columns, wheres, null, null, null);
//      
//      return getURIsFromResult(resultSet, SSSQLVarU.learnEpId);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
//  
//  public SSUri getLearnEpCurrentVersionURI(
//    final SSUri user) throws Exception {
//    
//    ResultSet resultSet = null;
//    
//    try{
//      
//      final List<String>        columns = new ArrayList<>();
//      final Map<String, String> wheres  = new HashMap<>();
//      
//      column(columns,  SSSQLVarU.learnEpVersionId);
//      
//      where(wheres, SSSQLVarU.userId, user);
//      
//      resultSet = dbSQL.select(learnEpVersionCurrentTable, columns, wheres, null, null, null);
//      
//      if(!resultSet.first()){
//        SSServErrReg.regErrThrow(new SSErr(SSErrE.learnEpCurrentVersionNotSet);
//      }
//      
//      return bindingStrToUri(resultSet, SSSQLVarU.learnEpVersionId);
//    }catch(SSErr error){
//      SSServErrReg.regErrThrow(error, false);
//      return null;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
//  
//  public SSLearnEpVersion getLearnEpVersion(
//    final SSUri   learnEpVersionUri,
//    final Boolean withTimelineState) throws Exception {
//    
//    ResultSet                  resultSet       = null;
//    
//    try{
//      final Map<String, String>    wheres      = new HashMap<>();
//      final List<String>           tables      = new ArrayList<>();
//      final List<String>           columns     = new ArrayList<>();
//      final List<String>           tableCons   = new ArrayList<>();
//      SSLearnEpVersion             learnEpVersionObj;
//      
//      column(columns, SSSQLVarU.creationTime);
//      column(columns, SSSQLVarU.learnEpId);
//      
//      table(tables, entityTable);
//      table(tables, learnEpVersionsTable);
//      
//      where(wheres, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
//      
//      tableCon(tableCons, entityTable, SSSQLVarU.id, learnEpVersionsTable, SSSQLVarU.learnEpVersionId);
//      
//      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
//      
//      if(!resultSet.first()){
//        throw new Exception("learn ep version doesnt exist");
//      }
//      
//      learnEpVersionObj =
//        SSLearnEpVersion.get(
//          learnEpVersionUri,
//          bindingStrToUri               (resultSet, SSSQLVarU.learnEpId),
//          getLearnEpVersionEntities     (learnEpVersionUri),
//          getLearnEpVersionCircles      (learnEpVersionUri),
//          null);
//      
//      learnEpVersionObj.creationTime = bindingStrToLong(resultSet, SSSQLVarU.creationTime);
//      
//      if(withTimelineState){
//        learnEpVersionObj.learnEpTimelineState = getLearnEpVersionTimelineState(learnEpVersionUri);
//      }
//      
//      return learnEpVersionObj;
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
//  
//  public List<SSUri> getLearnEpVersionURIs(
//    final SSUri learnEpUri) throws Exception {
//    
//    ResultSet resultSet       = null;
//
//    try{
//      
//      final List<String>        columns = new ArrayList<>();
//      final Map<String, String> wheres  = new HashMap<>();
//      
//      column(columns, SSSQLVarU.learnEpVersionId);
//      
//      where(wheres, SSSQLVarU.learnEpId, learnEpUri);
//      
//      resultSet = dbSQL.select(learnEpVersionsTable, columns, wheres, null, null, null);
//      
//      return getURIsFromResult(resultSet, SSSQLVarU.learnEpVersionId);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
//  
//  private List<SSLearnEpCircle> getLearnEpVersionCircles(
//    final SSUri learnEpVersionUri) throws Exception{
//    
//    ResultSet resultSet          = null;
//    
//    try{
//      final List<SSLearnEpCircle>      learnEpCircles     = new ArrayList<>();
//      final List<String>               columns            = new ArrayList<>();
//      final List<String>               tables             = new ArrayList<>();
//      final List<String>               tableCons          = new ArrayList<>();
//      final Map<String, String>        wheres             = new HashMap<>();
//
//      column(columns, learnEpCircleTable, SSSQLVarU.learnEpCircleId);
//      column(columns, SSSQLVarU.label);
//      column(columns, SSSQLVarU.xLabel);
//      column(columns, SSSQLVarU.yLabel);
//      column(columns, SSSQLVarU.xR);
//      column(columns, SSSQLVarU.yR);
//      column(columns, SSSQLVarU.xC);
//      column(columns, SSSQLVarU.yC);
//      
//      where(wheres, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
//      
//      table(tables, entityTable);
//      table(tables, learnEpVersionCirclesTable);
//      table(tables, learnEpCircleTable);
//      
//      tableCon(tableCons, entityTable,                SSSQLVarU.id,              learnEpVersionCirclesTable, SSSQLVarU.learnEpCircleId);
//      tableCon(tableCons, learnEpVersionCirclesTable, SSSQLVarU.learnEpCircleId, learnEpCircleTable,         SSSQLVarU.learnEpCircleId);
//      
//      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
//      
//      while(resultSet.next()){
//        
//        learnEpCircles.add(
//          SSLearnEpCircle.get(
//            bindingStrToUri   (resultSet, SSSQLVarU.learnEpCircleId),
//            bindingStrToLabel (resultSet, SSSQLVarU.label),
//            bindingStrToFloat (resultSet, SSSQLVarU.xLabel),
//            bindingStrToFloat (resultSet, SSSQLVarU.yLabel),
//            bindingStrToFloat (resultSet, SSSQLVarU.xR),
//            bindingStrToFloat (resultSet, SSSQLVarU.yR),
//            bindingStrToFloat (resultSet, SSSQLVarU.xC),
//            bindingStrToFloat (resultSet, SSSQLVarU.yC)));
//      }
//      
//      return learnEpCircles;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
//  
//  private List<SSLearnEpEntity> getLearnEpVersionEntities(
//    final SSUri learnEpVersionUri) throws Exception{
//    
//    ResultSet resultSet          = null;
//    
//    try{
//      
//      final List<SSLearnEpEntity>      learnEpEntities    = new ArrayList<>();
//      final List<String>               columns            = new ArrayList<>();
//      final List<String>               tables             = new ArrayList<>();
//      final List<String>               tableCons          = new ArrayList<>();
//      final Map<String, String>        wheres             = new HashMap<>();
//    
//      column(columns, learnEpEntityTable, SSSQLVarU.learnEpEntityId);
//      column(columns, SSSQLVarU.entityId);
//      column(columns, SSSQLVarU.x);
//      column(columns, SSSQLVarU.y);
//
//      table(tables, learnEpVersionEntitiesTable);
//      table(tables, learnEpEntityTable);
//      
//      where(wheres, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
//      
//      tableCon(tableCons, learnEpVersionEntitiesTable, SSSQLVarU.learnEpEntityId, learnEpEntityTable, SSSQLVarU.learnEpEntityId);
//      
//      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
//      
//      while(resultSet.next()){
//        
//        learnEpEntities.add(
//          SSLearnEpEntity.get(
//            bindingStrToUri    (resultSet, SSSQLVarU.learnEpEntityId),
//            SSEntity.get(bindingStrToUri    (resultSet, SSSQLVarU.entityId), SSEntityE.entity),
//            bindingStrToFloat  (resultSet, SSSQLVarU.x),
//            bindingStrToFloat  (resultSet, SSSQLVarU.y)));
//      }
//      
//      return learnEpEntities;
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
//  
//  public SSUri createLearnEpVersion(
//    final SSUri learnEpVersionUri,
//    final SSUri learnEpUri) throws Exception{
//    
//    try{
//      final Map<String, String> inserts = new HashMap<>();
//      
//      insert(inserts, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
//      
//      dbSQL.insert(learnEpVersionTable, inserts);
//      
//      inserts.clear();
//      insert(inserts, SSSQLVarU.learnEpId,        learnEpUri);
//      insert(inserts, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
//      
//      dbSQL.insert(learnEpVersionsTable, inserts);
//      
//      return learnEpVersionUri;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
//  
//  public void addCircleToLearnEpVersion(
//    final SSUri      learnEpCircleUri,
//    final SSUri      learnEpVersionUri,
//    final SSLabel    label,
//    final Float      xLabel,
//    final Float      yLabel,
//    final Float      xR,
//    final Float      yR,
//    final Float      xC,
//    final Float      yC) throws Exception{
//    
//    try{
//      final Map<String, String> inserts = new HashMap<>();
//      
//      insert(inserts, SSSQLVarU.learnEpCircleId,    learnEpCircleUri);
//      insert(inserts, SSSQLVarU.xLabel,             xLabel);
//      insert(inserts, SSSQLVarU.yLabel,             yLabel);
//      insert(inserts, SSSQLVarU.xR,                 xR);
//      insert(inserts, SSSQLVarU.yR,                 yR);
//      insert(inserts, SSSQLVarU.xC,                 xC);
//      insert(inserts, SSSQLVarU.yC,                 yC);
//      
//      dbSQL.insert(learnEpCircleTable, inserts);
//      
//      inserts.clear();
//      insert(inserts, SSSQLVarU.learnEpVersionId,   learnEpVersionUri);
//      insert(inserts, SSSQLVarU.learnEpCircleId,    learnEpCircleUri);
//      
//      dbSQL.insert(learnEpVersionCirclesTable, inserts);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
//  
//  public void updateCircle(
//    final SSUri      learnEpCircleUri,
//    final Float      xLabel,
//    final Float      yLabel,
//    final Float      xR,
//    final Float      yR,
//    final Float      xC,
//    final Float      yC) throws Exception{
//    
//    try{
//      final Map<String, String> wheres  = new HashMap<>();
//      final Map<String, String> updates = new HashMap<>();
//
//      where(wheres, SSSQLVarU.learnEpCircleId,   learnEpCircleUri);
//
//      if(xLabel != null){
//        update(updates, SSSQLVarU.xLabel,             xLabel);
//      }
//      
//      if(yLabel != null){
//        update(updates, SSSQLVarU.yLabel,             yLabel);
//      }
//      
//      if(xR != null){
//        update(updates, SSSQLVarU.xR,                 xR);
//      }
//      
//      if(yR != null){
//        update(updates, SSSQLVarU.yR,                 yR);
//      }
//      
//      if(xC != null){
//        update(updates, SSSQLVarU.xC,                 xC);
//      }
//      
//      if(yC != null){
//        update(updates, SSSQLVarU.yC,                 yC);
//      }
//
//      if(updates.isEmpty()){
//        return;
//      }
//      
//      dbSQL.update(learnEpCircleTable, wheres, updates);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
//  
//  public void updateEntity(
//    final SSUri    learnEpEntityUri,
//    final SSUri    entityUri,
//    final Float    x,
//    final Float    y) throws Exception{
//    
//    try{
//      final Map<String, String> wheres  = new HashMap<>();
//      final Map<String, String> updates = new HashMap<>();
//      
//      where(wheres ,  SSSQLVarU.learnEpEntityId,   learnEpEntityUri);
//      
//      if(entityUri != null){
//        update(updates, SSSQLVarU.entityId,          entityUri);
//      }
//      
//      if(x != null){
//        update(updates, SSSQLVarU.x,                 x);
//      }
//      
//      if(y != null){
//        update(updates, SSSQLVarU.y,                 y);
//      }
//
//      if(updates.isEmpty()){
//        return;
//      }
//      
//      dbSQL.update(learnEpEntityTable, wheres, updates);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
//  
//  public void addEntityToLearnEpVersion(
//    final SSUri    learnEpEntityUri, 
//    final SSUri    learnEpVersionUri,
//    final SSUri    entityUri,
//    final Float    x,
//    final Float    y) throws Exception{
//    
//    try{
//      final Map<String, String> inserts = new HashMap<>();
//
//      insert(inserts, SSSQLVarU.learnEpEntityId,    learnEpEntityUri);
//      insert(inserts, SSSQLVarU.entityId,           entityUri);
//      insert(inserts, SSSQLVarU.x,                  x);
//      insert(inserts, SSSQLVarU.y,                  y);
//
//      dbSQL.insert(learnEpEntityTable, inserts);
//
//      inserts.clear();
//      insert(inserts, SSSQLVarU.learnEpVersionId,   learnEpVersionUri);
//      insert(inserts, SSSQLVarU.learnEpEntityId,    learnEpEntityUri);
//
//      dbSQL.insert(learnEpVersionEntitiesTable, inserts);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
//  
//  public void createLearnEp(
//    final SSUri    learnEpUri,
//    final SSUri    user) throws Exception{
//    
//    try{
//      final Map<String, String> inserts = new HashMap<>();
//      
//      insert(inserts, SSSQLVarU.learnEpId, learnEpUri);
//      
//      dbSQL.insert(learnEpTable, inserts);
//      
//      addLearnEp(learnEpUri, user);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
//  
//  public void addLearnEp(
//    final SSUri    learnEpUri,
//    final SSUri    user) throws Exception{
//    
//    try{
//      final Map<String, String> inserts = new HashMap<>();
//      
//      insert(inserts, SSSQLVarU.userId,    user);
//      insert(inserts, SSSQLVarU.learnEpId, learnEpUri);
//      
//      dbSQL.insert(learnEpUserTable, inserts);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
//  
//  public void setLearnEpCurrentVersion(
//    final SSUri user, 
//    final SSUri learnEpVersionUri) throws Exception {
//    
//    ResultSet resultSet  = null;
//    
//    try{
//      
//      final List<String>        columns = new ArrayList<>();
//      final Map<String, String> wheres  = new HashMap<>();
//      final Map<String, String> updates = new HashMap<>();
//      final Map<String, String> inserts = new HashMap<>();
//      
//      column(columns, SSSQLVarU.learnEpVersionId);
//      
//      where(wheres, SSSQLVarU.userId, user);
//      
//      resultSet = dbSQL.select(learnEpVersionCurrentTable, columns, wheres, null, null, null);
//      
//      if(resultSet.first()){
//        
//        wheres.clear();
//        
//        where  (wheres,  SSSQLVarU.userId,           user);
//        update (updates, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
//        
//        dbSQL.update(learnEpVersionCurrentTable, wheres, updates);
//      }else{
//        
//        insert(inserts, SSSQLVarU.userId,           user);
//        insert(inserts, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
//        
//        dbSQL.insert(learnEpVersionCurrentTable, inserts);
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
//  
//  public SSUri setLearnEpVersionTimelineState(
//    final SSUri learnEpTimelineStateUri, 
//    final SSUri learnEpVersionUri, 
//    final Long  startTime, 
//    final Long  endTime) throws Exception {
//    
//    try{
//      final Map<String, String> inserts = new HashMap<>();
//      final Map<String, String> deletes = new HashMap<>();
//      
//      insert(inserts, SSSQLVarU.learnEpTimelineStateId, learnEpTimelineStateUri);
//      insert(inserts, SSSQLVarU.startTime,              startTime);
//      insert(inserts, SSSQLVarU.endTime,                endTime);
//      
//      dbSQL.insert(learnEpTimelineStateTable, inserts);
//      
//      delete(deletes, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
//      
//      dbSQL.delete(learnEpVersionTimelineStatesTable, deletes);
//      
//      inserts.clear();
//      insert(inserts, SSSQLVarU.learnEpVersionId,       learnEpVersionUri);
//      insert(inserts, SSSQLVarU.learnEpTimelineStateId, learnEpTimelineStateUri);
//      
//      dbSQL.insert(learnEpVersionTimelineStatesTable, inserts);
//      
//      return learnEpTimelineStateUri;
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
//  
//  public SSLearnEpTimelineState getLearnEpVersionTimelineState(
//    final SSUri learnEpVersionUri) throws Exception{
//    
//    ResultSet resultSet        = null;
//    
//    try{
//      
//      final Map<String, String>    wheres      = new HashMap<>();
//      final List<String>           columns     = new ArrayList<>();
//      final List<String>           tables      = new ArrayList<>();
//      final List<String>           tableCons   = new ArrayList<>();
//      
//      column(columns, learnEpVersionTimelineStatesTable, SSSQLVarU.learnEpTimelineStateId);
//      column(columns, SSSQLVarU.startTime);
//      column(columns, SSSQLVarU.endTime);
//      
//      table(tables, learnEpTimelineStateTable);
//      table(tables, learnEpVersionTimelineStatesTable);
//      
//      where(wheres, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
//      
//      tableCon(tableCons, learnEpTimelineStateTable, SSSQLVarU.learnEpTimelineStateId, learnEpVersionTimelineStatesTable, SSSQLVarU.learnEpTimelineStateId);
//      
//      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
//      
//      if(!resultSet.first()){
////        SSLogU.warn("no timeline state set for version " + learnEpVersionUri);
//        return null;
//      }
//      
//      return SSLearnEpTimelineState.get(
//        bindingStrToUri (resultSet, SSSQLVarU.learnEpTimelineStateId),
//        learnEpVersionUri,
//        bindingStrToLong(resultSet, SSSQLVarU.startTime),
//        bindingStrToLong(resultSet, SSSQLVarU.endTime));
//        
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//        return null;
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//  }
//
//  public SSUri getLearnEpForVersion(
//    final SSUri learnEpVersion) throws Exception{
//    
//    ResultSet resultSet;
//    
//    try{
//      final List<String>        columns = new ArrayList<>();
//      final Map<String, String> wheres  = new HashMap<>();
//      
//      column(columns, SSSQLVarU.learnEpId);
//      
//      where(wheres, SSSQLVarU.learnEpVersionId, learnEpVersion);
//      
//      resultSet = dbSQL.select(learnEpVersionsTable, columns, wheres, null, null, null);
//      
//      checkFirstResult(resultSet);
//      
//      return bindingStrToUri(resultSet, SSSQLVarU.learnEpId);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
//  
//  public SSUri getLearnEpVersionForCircle(
//    final SSUri circle) throws Exception{
//    
//    ResultSet resultSet;
//    
//    try{
//      final List<String>        columns = new ArrayList<>();
//      final Map<String, String> wheres  = new HashMap<>();
//      
//      column(columns, SSSQLVarU.learnEpVersionId);
//      
//      where(wheres, SSSQLVarU.learnEpCircleId, circle);
//      
//      resultSet = dbSQL.select(learnEpVersionCirclesTable, columns, wheres, null, null, null);
//      
//      checkFirstResult(resultSet);
//      
//      return bindingStrToUri(resultSet, SSSQLVarU.learnEpVersionId);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
//  
//  public SSUri getLearnEpVersionForEntity(
//    final SSUri entity) throws Exception{
//    
//    ResultSet resultSet;
//    
//    try{
//      final List<String>        columns = new ArrayList<>();
//      final Map<String, String> wheres  = new HashMap<>();
//      
//      column(columns, SSSQLVarU.learnEpVersionId);
//      
//      where(wheres, SSSQLVarU.learnEpEntityId, entity);
//      
//      resultSet = dbSQL.select(learnEpVersionEntitiesTable, columns, wheres, null, null, null);
//      
//      checkFirstResult(resultSet);
//      
//      return bindingStrToUri(resultSet, SSSQLVarU.learnEpVersionId);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
//
//  public void deleteCircle(final SSUri learnEpCircle) throws Exception{
//    
//    try{
//      final Map<String, String> deletes = new HashMap<>();
//      
//      delete(deletes, SSSQLVarU.learnEpCircleId, learnEpCircle);
//      
//      dbSQL.deleteIgnore(learnEpVersionCirclesTable, deletes);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
//
//  public void deleteEntity(final SSUri learnEpEntity) throws Exception{
//    
//    try{
//      final Map<String, String> deletes = new HashMap<>();
//      
//      delete(deletes, SSSQLVarU.learnEpEntityId, learnEpEntity);
//      
//      dbSQL.deleteIgnore(learnEpVersionEntitiesTable, deletes);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }
}
