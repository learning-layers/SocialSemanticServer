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
package at.kc.tugraz.ss.serv.datatypes.learnep.impl.fct.sql;

import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpCircle;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpEntity;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpTimelineState;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServImplWithDBA;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;

public class SSLearnEpSQLFct extends SSDBSQLFct{
  
  public SSLearnEpSQLFct(SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }
  
  public SSLearnEp getLearnEp(
    final SSUri learnEpUri) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        columns   = new ArrayList<>();
      
      where(wheres, SSSQLVarNames.learnEpId, learnEpUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.learnEpTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      return SSLearnEp.get(learnEpUri);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getLearnEpUserURIs(
    final SSUri learnEp) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.learnEpId, learnEp);
    
      resultSet = dbSQL.select(SSSQLVarNames.learnEpUserTable, columns, wheres, null, null, null);

      return getURIsFromResult(resultSet, SSSQLVarNames.userId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getLearnEpURIsForUser(
    final SSUri           user) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        columns   = new ArrayList<>();
      
      column(columns, SSSQLVarNames.learnEpId);
      
      where(wheres, SSSQLVarNames.userId, user);
      
      resultSet = dbSQL.select(SSSQLVarNames.learnEpUserTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.learnEpId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri getLearnEpCurrentVersionURI(
    final SSUri user) throws Exception {
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns,  SSSQLVarNames.learnEpVersionId);
      
      where(wheres, SSSQLVarNames.userId, user);
      
      resultSet = dbSQL.select(SSSQLVarNames.learnEpVersionCurrentTable, columns, wheres, null, null, null);
      
      if(!resultSet.first()){
        throw new SSErr(SSErrE.learnEpCurrentVersionNotSet);
      }
      
      return bindingStrToUri(resultSet, SSSQLVarNames.learnEpVersionId);
    }catch(SSErr error){
      SSServErrReg.regErrThrow(error, false);
      return null;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSLearnEpVersion getLearnEpVersion(
    final SSUri   learnEpVersionUri) throws Exception {
    
    ResultSet                  resultSet       = null;
    
    try{
      final Map<String, String>    wheres      = new HashMap<>();
      final List<String>           tables      = new ArrayList<>();
      final List<String>           columns     = new ArrayList<>();
      final List<String>           tableCons   = new ArrayList<>();
      SSLearnEpVersion             learnEpVersion;
      
      column(columns, SSSQLVarNames.creationTime);
      column(columns, SSSQLVarNames.learnEpId);
      
      table(tables, SSSQLVarNames.entityTable);
      table(tables, SSSQLVarNames.learnEpVersionsTable);
      
      where(wheres, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
      
      tableCon(tableCons, SSSQLVarNames.entityTable, SSSQLVarNames.id, SSSQLVarNames.learnEpVersionsTable, SSSQLVarNames.learnEpVersionId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      if(!resultSet.first()){
        throw new Exception("learn ep version doesnt exist");
      }
      
      learnEpVersion =
        SSLearnEpVersion.get(
          learnEpVersionUri,
          bindingStrToUri               (resultSet, SSSQLVarNames.learnEpId),
          getLearnEpVersionEntities     (learnEpVersionUri),
          getLearnEpVersionCircles      (learnEpVersionUri),
          null);
      
      learnEpVersion.learnEpTimelineState = getLearnEpVersionTimelineState(learnEpVersionUri);
      
      return learnEpVersion;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getLearnEpVersionURIs(
    final SSUri learnEpUri) throws Exception {
    
    ResultSet resultSet       = null;

    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.learnEpVersionId);
      
      where(wheres, SSSQLVarNames.learnEpId, learnEpUri);
      
      resultSet = dbSQL.select(SSSQLVarNames.learnEpVersionsTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.learnEpVersionId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  private List<SSEntity> getLearnEpVersionCircles(
    final SSUri learnEpVersionUri) throws Exception{
    
    ResultSet resultSet          = null;
    
    try{
      final List<SSEntity>             learnEpCircles     = new ArrayList<>();
      final List<String>               columns            = new ArrayList<>();
      final List<String>               tables             = new ArrayList<>();
      final List<String>               tableCons          = new ArrayList<>();
      final Map<String, String>        wheres             = new HashMap<>();

      column(columns, SSSQLVarNames.learnEpCircleTable, SSSQLVarNames.learnEpCircleId);
      column(columns, SSSQLVarNames.xLabel);
      column(columns, SSSQLVarNames.yLabel);
      column(columns, SSSQLVarNames.xR);
      column(columns, SSSQLVarNames.yR);
      column(columns, SSSQLVarNames.xC);
      column(columns, SSSQLVarNames.yC);
      
      where(wheres, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
      
      table(tables, SSSQLVarNames.entityTable);
      table(tables, SSSQLVarNames.learnEpVersionCirclesTable);
      table(tables, SSSQLVarNames.learnEpCircleTable);
      
      tableCon(tableCons, SSSQLVarNames.learnEpVersionCirclesTable, SSSQLVarNames.learnEpCircleId, SSSQLVarNames.learnEpCircleTable, SSSQLVarNames.learnEpCircleId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        learnEpCircles.add(
          SSLearnEpCircle.get(
            bindingStrToUri   (resultSet, SSSQLVarNames.learnEpCircleId),
            bindingStrToFloat (resultSet, SSSQLVarNames.xLabel),
            bindingStrToFloat (resultSet, SSSQLVarNames.yLabel),
            bindingStrToFloat (resultSet, SSSQLVarNames.xR),
            bindingStrToFloat (resultSet, SSSQLVarNames.yR),
            bindingStrToFloat (resultSet, SSSQLVarNames.xC),
            bindingStrToFloat (resultSet, SSSQLVarNames.yC)));
      }
      
      return learnEpCircles;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSEntity> getLearnEpVersionEntities(
    final SSUri learnEpVersionUri) throws Exception{
    
    ResultSet resultSet          = null;
    
    try{
      
      final List<SSEntity>             learnEpEntities    = new ArrayList<>();
      final List<String>               columns            = new ArrayList<>();
      final List<String>               tables             = new ArrayList<>();
      final List<String>               tableCons          = new ArrayList<>();
      final Map<String, String>        wheres             = new HashMap<>();
    
      column(columns, SSSQLVarNames.learnEpEntityTable, SSSQLVarNames.learnEpEntityId);
      column(columns, SSSQLVarNames.entityId);
      column(columns, SSSQLVarNames.x);
      column(columns, SSSQLVarNames.y);

      table(tables, SSSQLVarNames.learnEpVersionEntitiesTable);
      table(tables, SSSQLVarNames.learnEpEntityTable);
      
      where(wheres, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
      
      tableCon(tableCons, SSSQLVarNames.learnEpVersionEntitiesTable, SSSQLVarNames.learnEpEntityId, SSSQLVarNames.learnEpEntityTable, SSSQLVarNames.learnEpEntityId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        learnEpEntities.add(
          SSLearnEpEntity.get(
            bindingStrToUri    (resultSet, SSSQLVarNames.learnEpEntityId),
            SSEntity.get(bindingStrToUri    (resultSet, SSSQLVarNames.entityId), SSEntityE.entity),
            bindingStrToFloat  (resultSet, SSSQLVarNames.x),
            bindingStrToFloat  (resultSet, SSSQLVarNames.y)));
      }
      
      return learnEpEntities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri createLearnEpVersion(
    final SSUri learnEpVersionUri,
    final SSUri learnEpUri) throws Exception{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
      
      dbSQL.insert(SSSQLVarNames.learnEpVersionTable, inserts);
      
      inserts.clear();
      insert(inserts, SSSQLVarNames.learnEpId,        learnEpUri);
      insert(inserts, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
      
      dbSQL.insert(SSSQLVarNames.learnEpVersionsTable, inserts);
      
      return learnEpVersionUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public void addCircleToLearnEpVersion(
    final SSUri      learnEpCircleUri,
    final SSUri      learnEpVersionUri,
    final SSLabel    label,
    final Float      xLabel,
    final Float      yLabel,
    final Float      xR,
    final Float      yR,
    final Float      xC,
    final Float      yC) throws Exception{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.learnEpCircleId,    learnEpCircleUri);
      insert(inserts, SSSQLVarNames.xLabel,             xLabel);
      insert(inserts, SSSQLVarNames.yLabel,             yLabel);
      insert(inserts, SSSQLVarNames.xR,                 xR);
      insert(inserts, SSSQLVarNames.yR,                 yR);
      insert(inserts, SSSQLVarNames.xC,                 xC);
      insert(inserts, SSSQLVarNames.yC,                 yC);
      
      dbSQL.insert(SSSQLVarNames.learnEpCircleTable, inserts);
      
      inserts.clear();
      insert(inserts, SSSQLVarNames.learnEpVersionId,   learnEpVersionUri);
      insert(inserts, SSSQLVarNames.learnEpCircleId,    learnEpCircleUri);
      
      dbSQL.insert(SSSQLVarNames.learnEpVersionCirclesTable, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void updateCircle(
    final SSUri      learnEpCircleUri,
    final Float      xLabel,
    final Float      yLabel,
    final Float      xR,
    final Float      yR,
    final Float      xC,
    final Float      yC) throws Exception{
    
    try{
      final Map<String, String> wheres  = new HashMap<>();
      final Map<String, String> updates = new HashMap<>();

      where(wheres, SSSQLVarNames.learnEpCircleId,   learnEpCircleUri);

      if(xLabel != null){
        update(updates, SSSQLVarNames.xLabel,             xLabel);
      }
      
      if(yLabel != null){
        update(updates, SSSQLVarNames.yLabel,             yLabel);
      }
      
      if(xR != null){
        update(updates, SSSQLVarNames.xR,                 xR);
      }
      
      if(yR != null){
        update(updates, SSSQLVarNames.yR,                 yR);
      }
      
      if(xC != null){
        update(updates, SSSQLVarNames.xC,                 xC);
      }
      
      if(yC != null){
        update(updates, SSSQLVarNames.yC,                 yC);
      }

      if(updates.isEmpty()){
        return;
      }
      
      dbSQL.update(SSSQLVarNames.learnEpCircleTable, wheres, updates);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void updateEntity(
    final SSUri    learnEpEntityUri,
    final Float    x,
    final Float    y) throws Exception{
    
    try{
      final Map<String, String> wheres  = new HashMap<>();
      final Map<String, String> updates = new HashMap<>();
      
      where(wheres ,  SSSQLVarNames.learnEpEntityId,   learnEpEntityUri);
      
      if(x != null){
        update(updates, SSSQLVarNames.x,                 x);
      }
      
      if(y != null){
        update(updates, SSSQLVarNames.y,                 y);
      }

      if(updates.isEmpty()){
        return;
      }
      
      dbSQL.update(SSSQLVarNames.learnEpEntityTable, wheres, updates);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addEntityToLearnEpVersion(
    final SSUri    learnEpEntityUri, 
    final SSUri    learnEpVersionUri,
    final SSUri    entityUri,
    final Float    x,
    final Float    y) throws Exception{
    
    try{
      final Map<String, String> inserts = new HashMap<>();

      insert(inserts, SSSQLVarNames.learnEpEntityId,    learnEpEntityUri);
      insert(inserts, SSSQLVarNames.entityId,           entityUri);
      insert(inserts, SSSQLVarNames.x,                  x);
      insert(inserts, SSSQLVarNames.y,                  y);

      dbSQL.insert(SSSQLVarNames.learnEpEntityTable, inserts);

      inserts.clear();
      insert(inserts, SSSQLVarNames.learnEpVersionId,   learnEpVersionUri);
      insert(inserts, SSSQLVarNames.learnEpEntityId,    learnEpEntityUri);

      dbSQL.insert(SSSQLVarNames.learnEpVersionEntitiesTable, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void createLearnEp(
    final SSUri    learnEpUri,
    final SSUri    user) throws Exception{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.learnEpId, learnEpUri);
      
      dbSQL.insert(SSSQLVarNames.learnEpTable, inserts);
      
      addLearnEp(learnEpUri, user);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addLearnEp(
    final SSUri    learnEpUri,
    final SSUri    user) throws Exception{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userId,    user);
      insert(inserts, SSSQLVarNames.learnEpId, learnEpUri);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.userId,    user);
      uniqueKey(uniqueKeys, SSSQLVarNames.learnEpId, learnEpUri);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.learnEpUserTable, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void deleteCurrentEpVersion(
    final SSUri learnEpVersion) throws Exception {
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.learnEpVersionId, learnEpVersion);
      
      dbSQL.delete(SSSQLVarNames.learnEpVersionCurrentTable, wheres);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  
  public void setLearnEpCurrentVersion(
    final SSUri user, 
    final SSUri learnEpVersionUri) throws Exception {
    
    ResultSet resultSet  = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      final Map<String, String> updates = new HashMap<>();
      final Map<String, String> inserts = new HashMap<>();
      
      column(columns, SSSQLVarNames.learnEpVersionId);
      
      where(wheres, SSSQLVarNames.userId, user);
      
      resultSet = dbSQL.select(SSSQLVarNames.learnEpVersionCurrentTable, columns, wheres, null, null, null);
      
      if(resultSet.first()){
        
        wheres.clear();
        
        where  (wheres,  SSSQLVarNames.userId,           user);
        update (updates, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
        
        dbSQL.update(SSSQLVarNames.learnEpVersionCurrentTable, wheres, updates);
      }else{
        
        insert(inserts, SSSQLVarNames.userId,           user);
        insert(inserts, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
        
        dbSQL.insert(SSSQLVarNames.learnEpVersionCurrentTable, inserts);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  //TODO improve
  public SSUri setLearnEpVersionTimelineState(
    final SSUri learnEpTimelineStateUri, 
    final SSUri learnEpVersionUri, 
    final Long  startTime, 
    final Long  endTime) throws Exception {
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      final Map<String, String> wheres = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.learnEpTimelineStateId, learnEpTimelineStateUri);
      insert(inserts, SSSQLVarNames.startTime,              startTime);
      insert(inserts, SSSQLVarNames.endTime,                endTime);
      
      dbSQL.insert(SSSQLVarNames.learnEpTimelineStateTable, inserts);
      
      where(wheres, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
      
      dbSQL.delete(SSSQLVarNames.learnEpVersionTimelineStatesTable, wheres);
      
      inserts.clear();
      insert(inserts, SSSQLVarNames.learnEpVersionId,       learnEpVersionUri);
      insert(inserts, SSSQLVarNames.learnEpTimelineStateId, learnEpTimelineStateUri);
      
      dbSQL.insert(SSSQLVarNames.learnEpVersionTimelineStatesTable, inserts);
      
      return learnEpTimelineStateUri;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSLearnEpTimelineState getLearnEpVersionTimelineState(
    final SSUri learnEpVersionUri) throws Exception{
    
    ResultSet resultSet        = null;
    
    try{
      
      final Map<String, String>    wheres      = new HashMap<>();
      final List<String>           columns     = new ArrayList<>();
      final List<String>           tables      = new ArrayList<>();
      final List<String>           tableCons   = new ArrayList<>();
      
      column(columns, SSSQLVarNames.learnEpVersionTimelineStatesTable, SSSQLVarNames.learnEpTimelineStateId);
      column(columns, SSSQLVarNames.startTime);
      column(columns, SSSQLVarNames.endTime);
      
      table(tables, SSSQLVarNames.learnEpTimelineStateTable);
      table(tables, SSSQLVarNames.learnEpVersionTimelineStatesTable);
      
      where(wheres, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
      
      tableCon(tableCons, SSSQLVarNames.learnEpTimelineStateTable, SSSQLVarNames.learnEpTimelineStateId, SSSQLVarNames.learnEpVersionTimelineStatesTable, SSSQLVarNames.learnEpTimelineStateId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      if(!resultSet.first()){
//        SSLogU.warn("no timeline state set for version " + learnEpVersionUri);
        return null;
      }
      
      return SSLearnEpTimelineState.get(bindingStrToUri (resultSet, SSSQLVarNames.learnEpTimelineStateId),
        learnEpVersionUri,
        bindingStrToLong(resultSet, SSSQLVarNames.startTime),
        bindingStrToLong(resultSet, SSSQLVarNames.endTime));
        
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
        return null;
      }finally{
        dbSQL.closeStmt(resultSet);
      }
  }

  public SSUri getLearnEpForVersion(
    final SSUri learnEpVersion) throws Exception{
    
    ResultSet resultSet;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.learnEpId);
      
      where(wheres, SSSQLVarNames.learnEpVersionId, learnEpVersion);
      
      resultSet = dbSQL.select(SSSQLVarNames.learnEpVersionsTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarNames.learnEpId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSUri getLearnEpVersionForCircle(
    final SSUri circle) throws Exception{
    
    ResultSet resultSet;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.learnEpVersionId);
      
      where(wheres, SSSQLVarNames.learnEpCircleId, circle);
      
      resultSet = dbSQL.select(SSSQLVarNames.learnEpVersionCirclesTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarNames.learnEpVersionId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSUri getLearnEpVersionForEntity(
    final SSUri entity) throws Exception{
    
    ResultSet resultSet;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.learnEpVersionId);
      
      where(wheres, SSSQLVarNames.learnEpEntityId, entity);
      
      resultSet = dbSQL.select(SSSQLVarNames.learnEpVersionEntitiesTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarNames.learnEpVersionId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public void deleteCircle(final SSUri learnEpCircle) throws Exception{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.learnEpCircleId, learnEpCircle);
      
      dbSQL.deleteIgnore(SSSQLVarNames.learnEpVersionCirclesTable, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void deleteEntity(final SSUri learnEpEntity) throws Exception{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.learnEpEntityId, learnEpEntity);
      
      dbSQL.deleteIgnore(SSSQLVarNames.learnEpVersionEntitiesTable, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public SSUri getEntity(
    final SSUri learnEpVersion, 
    final SSUri learnEpEntity) throws Exception{
    
    ResultSet resultSet;
    
    try{
      final List<String>        columns   = new ArrayList<>();
      final List<String>        tables    = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        tableCons = new ArrayList<>();
      
      column(columns, SSSQLVarNames.entityId);
      
      table(tables, SSSQLVarNames.learnEpEntityTable);
      table(tables, SSSQLVarNames.learnEpVersionEntitiesTable);
      
      where(wheres, SSSQLVarNames.learnEpVersionEntitiesTable, SSSQLVarNames.learnEpEntityId,  learnEpEntity);
      where(wheres, SSSQLVarNames.learnEpVersionEntitiesTable, SSSQLVarNames.learnEpVersionId, learnEpVersion);
      
      tableCon(tableCons, SSSQLVarNames.learnEpEntityTable, SSSQLVarNames.learnEpEntityId, SSSQLVarNames.learnEpVersionEntitiesTable, SSSQLVarNames.learnEpEntityId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarNames.entityId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public void removeLearnEpForUser(
    final SSUri user, 
    final SSUri learnEp) throws Exception{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.learnEpId, learnEp);
      where(wheres, SSSQLVarNames.userId,    user);

      dbSQL.delete(SSSQLVarNames.learnEpUserTable, wheres);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }    
  }
}


//      insert(inserts, SSSQLVarU.learnEpVersionId,   learnEpVersionUri);
//      insert(inserts, SSSQLVarU.learnEpCircleId,    learnEpCircleUri);
//      
//      dbSQL.insert(learnEpVersionCirclesTable, inserts);

//  private SSLearnEpCircle getLearnEpCircle(SSUri circleUri) throws Exception {
//    
//    ResultSet                  resultSet     = null;
//    Map<String, String>        selectPars    = new HashMap<>();
//    SSLearnEpCircle            learnEpCircle = null;
//    
//    selectPars.put(SSSQLVarU.learnEpCircleId, circleUri.toString());
//    
//    try{
//      resultSet = dbSQL.select(learnEpCircleTable, selectPars);
//      
//      resultSet.first();
//      
//      learnEpCircle = 
//        SSLearnEpCircle.get(
//        circleUri,
//        null,
//        bindingStrToFloat(resultSet, SSSQLVarU.xLabel),
//        bindingStrToFloat(resultSet, SSSQLVarU.yLabel),
//        bindingStrToFloat(resultSet, SSSQLVarU.xR),
//        bindingStrToFloat(resultSet, SSSQLVarU.yR),
//        bindingStrToFloat(resultSet, SSSQLVarU.xC),
//        bindingStrToFloat(resultSet, SSSQLVarU.yC));
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    return learnEpCircle;
//  }
  
//  private SSLearnEpEntity getLearnEpEntity(SSUri entityUri) throws Exception {
//    
//    ResultSet                  resultSet     = null;
//    Map<String, String>        selectPars    = new HashMap<>();
//    SSLearnEpEntity            learnEpEntity = null;
//    
//    selectPars.put(SSSQLVarU.learnEpEntityId, entityUri.toString());
//    
//    try{
//      resultSet = dbSQL.select(learnEpEntityTable, selectPars);
//      
//      resultSet.first();
//      
//      learnEpEntity =
//        SSLearnEpEntity.get(
//        entityUri,
//        bindingStrToUri    (resultSet, SSSQLVarU.entityId),
//        bindingStrToFloat  (resultSet, SSSQLVarU.x),
//        bindingStrToFloat  (resultSet, SSSQLVarU.y));
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    return learnEpEntity;
//  }

//  public Boolean isLearnEp(SSUri entityUri) throws Exception{
//    
//    ResultSet           resultSet  = null;
//    Map<String, String> selectPars = new HashMap<>();
//    Boolean             isLearnEp  = false;
//    
//    selectPars.put(SSSQLVarU.learnEpId, entityUri.toString());
//    
//    try{
//      
//      resultSet  = dbSQL.select(learnEpTable, selectPars);
//      isLearnEp  = resultSet.first();
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    return isLearnEp;
//  }
//
//  public Boolean isLearnEpVersion(SSUri entityUri) throws Exception{
//    
//    ResultSet           resultSet  = null;
//    Map<String, String> selectPars = new HashMap<>();
//    Boolean             isVersion  = false;
//    
//    selectPars.put(SSSQLVarU.learnEpVersionId, entityUri.toString());
//    
//    try{
//      
//      resultSet  = dbSQL.select(learnEpVersionTable, selectPars);
//      isVersion  = resultSet.first();
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    return isVersion;
//  }
//
//  public Boolean isLearnEpCircle(SSUri entityUri) throws Exception{
//    
//    ResultSet           resultSet  = null;
//    Map<String, String> selectPars = new HashMap<>();
//    Boolean             isCircle   = false;
//    
//    selectPars.put(SSSQLVarU.learnEpCircleId, entityUri.toString());
//    
//    try{
//      
//      resultSet = dbSQL.select(learnEpCircleTable, selectPars);
//      isCircle  = resultSet.first();
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    return isCircle;
//  }
//
//  public Boolean isLearnEpEntity(SSUri entityUri) throws Exception{
//    
//    ResultSet           resultSet  = null;
//    Map<String, String> selectPars = new HashMap<>();
//    Boolean             isEntity   = false;
//    
//    selectPars.put(SSSQLVarU.learnEpEntityId, entityUri.toString());
//    
//    try{
//      
//      resultSet = dbSQL.select(learnEpEntityTable, selectPars);
//      isEntity  = resultSet.first();
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    return isEntity;
//  }
//
//  public Boolean isLearnEpTimelineState(SSUri entityUri) throws Exception{
//    
//    ResultSet           resultSet         = null;
//    Map<String, String> selectPars        = new HashMap<>();
//    Boolean             isTimelineState   = false;
//    
//    selectPars.put(SSSQLVarU.learnEpTimelineStateId, entityUri.toString());
//    
//    try{
//      
//      resultSet       = dbSQL.select(learnEpTimelineStateTable, selectPars);
//      isTimelineState = resultSet.first();
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    return isTimelineState;
//  }

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
//      column(columns, SSSQLVarNames.learnEpId);
//      
//      where(wheres, SSSQLVarNames.userId,    user);
//      where(wheres, SSSQLVarNames.learnEpId, learnEp);
//    
//      resultSet = dbSQL.select(SSSQLVarNames.learnEpUserTable, columns, wheres, null, null, null);
//
//      return resultSet.first();
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  } 