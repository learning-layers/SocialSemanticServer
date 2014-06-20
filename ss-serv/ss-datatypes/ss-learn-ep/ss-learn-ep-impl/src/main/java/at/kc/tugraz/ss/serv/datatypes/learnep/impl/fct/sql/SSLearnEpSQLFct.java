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

import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpCircle;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpEntity;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpTimelineState;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSLearnEpSQLFct extends SSDBSQLFct{
  
  protected final String               learnEpUserTable                    = "learnepuser";
  protected final String               learnEpVersionCirclesTable          = "learnepversioncircles";
  protected final String               learnEpVersionEntitiesTable         = "learnepversionentities";
  protected final String               learnEpVersionTable                 = "learnepversion";
  protected final String               learnEpVersionCurrentTable          = "learnepversioncurrent";
  protected final String               learnEpVersionsTable                = "learnepversions";
  protected final String               learnEpVersionTimelineStatesTable   = "learnepversiontimelinestates";
  protected final String               learnEpTimelineStateTable           = "learneptimelinestate";
  protected final String               learnEpCircleTable                  = "learnepcircle";
  protected final String               learnEpTable                        = "learnep";
  protected final String               learnEpEntityTable                  = "learnepentity";
  
  public SSLearnEpSQLFct(SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }
  
  public Boolean isLearnEp(SSUri entityUri) throws Exception{
    
    ResultSet           resultSet  = null;
    Map<String, String> selectPars = new HashMap<>();
    Boolean             isLearnEp  = false;
    
    selectPars.put(SSSQLVarU.learnEpId, entityUri.toString());
    
    try{
      
      resultSet  = dbSQL.select(learnEpTable, selectPars);
      isLearnEp  = resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return isLearnEp;
  }

  public Boolean isLearnEpVersion(SSUri entityUri) throws Exception{
    
    ResultSet           resultSet  = null;
    Map<String, String> selectPars = new HashMap<>();
    Boolean             isVersion  = false;
    
    selectPars.put(SSSQLVarU.learnEpVersionId, entityUri.toString());
    
    try{
      
      resultSet  = dbSQL.select(learnEpVersionTable, selectPars);
      isVersion  = resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return isVersion;
  }

  public Boolean isLearnEpCircle(SSUri entityUri) throws Exception{
    
    ResultSet           resultSet  = null;
    Map<String, String> selectPars = new HashMap<>();
    Boolean             isCircle   = false;
    
    selectPars.put(SSSQLVarU.learnEpCircleId, entityUri.toString());
    
    try{
      
      resultSet = dbSQL.select(learnEpCircleTable, selectPars);
      isCircle  = resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return isCircle;
  }

  public Boolean isLearnEpEntity(SSUri entityUri) throws Exception{
    
    ResultSet           resultSet  = null;
    Map<String, String> selectPars = new HashMap<>();
    Boolean             isEntity   = false;
    
    selectPars.put(SSSQLVarU.learnEpEntityId, entityUri.toString());
    
    try{
      
      resultSet = dbSQL.select(learnEpEntityTable, selectPars);
      isEntity  = resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return isEntity;
  }

  public Boolean isLearnEpTimelineState(SSUri entityUri) throws Exception{
    
    ResultSet           resultSet         = null;
    Map<String, String> selectPars        = new HashMap<>();
    Boolean             isTimelineState   = false;
    
    selectPars.put(SSSQLVarU.learnEpTimelineStateId, entityUri.toString());
    
    try{
      
      resultSet       = dbSQL.select(learnEpTimelineStateTable, selectPars);
      isTimelineState = resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return isTimelineState;
  }
  
  public List<SSLearnEp> getLearnEps(
    final SSUri user) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      final List<SSLearnEp>     learnEps  = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        columns   = new ArrayList<>();
      final List<String>        tables    = new ArrayList<>();
      final List<String>        tableCons = new ArrayList<>();
      
      column(columns, SSSQLVarU.learnEpId);
      column(columns, SSSQLVarU.label);
      
      table(tables, entityTable);
      table(tables, learnEpUserTable);
      
      where(wheres, SSSQLVarU.userId, user);
      
      tableCon(tableCons, entityTable, SSSQLVarU.id, learnEpUserTable, SSSQLVarU.learnEpId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      while(resultSet.next()){
        
        learnEps.add(
          SSLearnEp.get(
            user, 
            bindingStrToUri   (resultSet, SSSQLVarU.learnEpId), 
            bindingStrToLabel (resultSet, SSSQLVarU.label)));
      }
      
      return learnEps;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getLearnEpURIs(
    final SSUri user) throws Exception{
    
    ResultSet           resultSet  = null;
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.userId, user);
      
      resultSet = dbSQL.select(learnEpUserTable, wheres);
      
      return getURIsFromResult(resultSet, SSSQLVarU.learnEpId);
      
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
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.userId, user);
      
      resultSet = dbSQL.select(learnEpVersionCurrentTable, wheres);
      
      if(!resultSet.first()){
        throw new Exception("current learn ep version not set");
      }
      
      return bindingStrToUri(resultSet, SSSQLVarU.learnEpVersionId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSLearnEpVersion getLearnEpVersion(
    final SSUri learnEpVersionUri) throws Exception {
    
    ResultSet                  resultSet       = null;
    
    try{
      final Map<String, String>    wheres      = new HashMap<>();
      final List<String>           tables      = new ArrayList<>();
      final List<String>           columns     = new ArrayList<>();
      final List<String>           tableCons   = new ArrayList<>();
      
      column(columns, SSSQLVarU.creationTime);
      column(columns, SSSQLVarU.learnEpId);
      
      table(tables, entityTable);
      table(tables, learnEpVersionsTable);
      
      where(wheres, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
      
      tableCon(tableCons, entityTable, SSSQLVarU.id, learnEpVersionsTable, SSSQLVarU.learnEpVersionId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      if(!resultSet.first()){
        throw new Exception("learn ep version doesnt exist");
      }
      
      return SSLearnEpVersion.get(
          learnEpVersionUri,
          bindingStrToUri           (resultSet, SSSQLVarU.learnEpId),
          bindingStr                (resultSet, SSSQLVarU.creationTime),
          getLearnEpVersionEntities (learnEpVersionUri),
          getLearnEpVersionCircles  (learnEpVersionUri));
      
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
      
      final Map<String, String> wheres      = new HashMap<>();
      
      where(wheres, SSSQLVarU.learnEpId, learnEpUri);
      
      resultSet = dbSQL.select(learnEpVersionsTable, wheres);
      
      return getURIsFromResult(resultSet,  SSSQLVarU.learnEpVersionId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  private List<SSLearnEpCircle> getLearnEpVersionCircles(
    final SSUri learnEpVersionUri) throws Exception{
    
    ResultSet resultSet          = null;
    
    try{
      final List<SSLearnEpCircle>      learnEpCircles     = new ArrayList<>();
      final List<String>               columns            = new ArrayList<>();
      final List<String>               tables             = new ArrayList<>();
      final List<String>               tableCons          = new ArrayList<>();
      final Map<String, String>        wheres             = new HashMap<>();

      column(columns, learnEpCircleTable, SSSQLVarU.learnEpCircleId);
      column(columns, SSSQLVarU.label);
      column(columns, SSSQLVarU.xLabel);
      column(columns, SSSQLVarU.yLabel);
      column(columns, SSSQLVarU.xR);
      column(columns, SSSQLVarU.yR);
      column(columns, SSSQLVarU.xC);
      column(columns, SSSQLVarU.yC);
      
      where(wheres, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
      
      table(tables, entityTable);
      table(tables, learnEpVersionCirclesTable);
      table(tables, learnEpCircleTable);
      
      tableCon(tableCons, entityTable,                SSSQLVarU.id,              learnEpVersionCirclesTable, SSSQLVarU.learnEpCircleId);
      tableCon(tableCons, learnEpVersionCirclesTable, SSSQLVarU.learnEpCircleId, learnEpCircleTable,         SSSQLVarU.learnEpCircleId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      while(resultSet.next()){
        
        learnEpCircles.add(
          SSLearnEpCircle.get(
            bindingStrToUri   (resultSet, SSSQLVarU.learnEpCircleId),
            bindingStrToLabel (resultSet, SSSQLVarU.label),
            bindingStrToFloat (resultSet, SSSQLVarU.xLabel),
            bindingStrToFloat (resultSet, SSSQLVarU.yLabel),
            bindingStrToFloat (resultSet, SSSQLVarU.xR),
            bindingStrToFloat (resultSet, SSSQLVarU.yR),
            bindingStrToFloat (resultSet, SSSQLVarU.xC),
            bindingStrToFloat (resultSet, SSSQLVarU.yC)));
      }
      
      return learnEpCircles;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  private List<SSLearnEpEntity> getLearnEpVersionEntities(
    final SSUri learnEpVersionUri) throws Exception{
    
    ResultSet resultSet          = null;
    
    try{
      
      final List<SSLearnEpEntity>      learnEpEntities    = new ArrayList<>();
      final List<String>               columns            = new ArrayList<>();
      final List<String>               tables             = new ArrayList<>();
      final List<String>               tableCons          = new ArrayList<>();
      final Map<String, String>        wheres             = new HashMap<>();
    
      column(columns, learnEpEntityTable, SSSQLVarU.learnEpEntityId);
      column(columns, SSSQLVarU.entityId);
      column(columns, SSSQLVarU.x);
      column(columns, SSSQLVarU.y);

      table(tables, learnEpVersionEntitiesTable);
      table(tables, learnEpEntityTable);
      
      where(wheres, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
      
      tableCon(tableCons, learnEpVersionEntitiesTable, SSSQLVarU.learnEpEntityId, learnEpEntityTable, SSSQLVarU.learnEpEntityId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      while(resultSet.next()){
        
        learnEpEntities.add(
          SSLearnEpEntity.get(
            bindingStrToUri    (resultSet, SSSQLVarU.learnEpEntityId),
            bindingStrToUri    (resultSet, SSSQLVarU.entityId),
            bindingStrToFloat  (resultSet, SSSQLVarU.x),
            bindingStrToFloat  (resultSet, SSSQLVarU.y)));
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
      
      insert(inserts, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
      
      dbSQL.insert(learnEpVersionTable, inserts);
      
      inserts.clear();
      insert(inserts, SSSQLVarU.learnEpId,        learnEpUri);
      insert(inserts, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
      
      dbSQL.insert(learnEpVersionsTable, inserts);
      
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
      
      insert(inserts, SSSQLVarU.learnEpCircleId,    learnEpCircleUri);
      insert(inserts, SSSQLVarU.xLabel,             xLabel);
      insert(inserts, SSSQLVarU.yLabel,             yLabel);
      insert(inserts, SSSQLVarU.xR,                 xR);
      insert(inserts, SSSQLVarU.yR,                 yR);
      insert(inserts, SSSQLVarU.xC,                 xC);
      insert(inserts, SSSQLVarU.yC,                 yC);
      
      dbSQL.insert(learnEpCircleTable, inserts);
      
      inserts.clear();
      insert(inserts, SSSQLVarU.learnEpVersionId,   learnEpVersionUri);
      insert(inserts, SSSQLVarU.learnEpCircleId,    learnEpCircleUri);
      
      dbSQL.insert(learnEpVersionCirclesTable, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeEntity(SSUri entityUri) throws Exception{
    
    if(entityUri == null){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return;
    }
    
    Map<String, String> deletePars = new HashMap<>();
    
    try{
    
      deletePars.put(SSSQLVarU.learnEpEntityId, SSStrU.toStr(entityUri));
      
      dbSQL.delete(learnEpEntityTable, deletePars);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeCircle(SSUri circleUri) throws Exception{
    
    if(circleUri == null){
      SSServErrReg.regErrThrow(new Exception("circle uri null"));
      return;
    }
    
    Map<String, String> deletePars = new HashMap<>();
    
    try{
    
      deletePars.put(SSSQLVarU.learnEpCircleId, SSStrU.toStr(circleUri));
      
      dbSQL.delete(learnEpCircleTable, deletePars);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void updateCircle(
    SSUri      learnEpCircleUri,
    SSLabel label,
    Float      xLabel,
    Float      yLabel,
    Float      xR,
    Float      yR,
    Float      xC,
    Float      yC) throws Exception{
    
    Map<String, String> updatePars;
    Map<String, String> newValues;
    
    updatePars = new HashMap<>();
    newValues  = new HashMap<>();
    updatePars.put(SSSQLVarU.learnEpCircleId,   learnEpCircleUri.toString());
    newValues.put(SSSQLVarU.xLabel,             xLabel.toString());
    newValues.put(SSSQLVarU.yLabel,             yLabel.toString());
    newValues.put(SSSQLVarU.xR,                 xR.toString());
    newValues.put(SSSQLVarU.yR,                 yR.toString());
    newValues.put(SSSQLVarU.xC,                 xC.toString());
    newValues.put(SSSQLVarU.yC,                 yC.toString());
    
    dbSQL.update(learnEpCircleTable, updatePars, newValues);
  }
  
  public void updateEntity(
    SSUri    learnEpEntityUri,
    SSUri    entityUri,
    Float    x,
    Float    y) throws Exception{
    
    Map<String, String> updatePars;
    Map<String, String> newValues;
    
    updatePars = new HashMap<>();
    newValues = new HashMap<>();
    updatePars.put(SSSQLVarU.learnEpEntityId,   learnEpEntityUri.toString());
    newValues.put(SSSQLVarU.entityId,           entityUri.toString());
    newValues.put(SSSQLVarU.x,                  x.toString());
    newValues.put(SSSQLVarU.y,                  y.toString());
    
    dbSQL.update(learnEpEntityTable, updatePars, newValues);
  }
  
  public void addEntityToLearnEpVersion(
    final SSUri    learnEpEntityUri, 
    final SSUri    learnEpVersionUri,
    final SSUri    entityUri,
    final Float    x,
    final Float    y) throws Exception{
    
    try{
      final Map<String, String> inserts = new HashMap<>();

      insert(inserts, SSSQLVarU.learnEpEntityId,    learnEpEntityUri);
      insert(inserts, SSSQLVarU.entityId,           entityUri);
      insert(inserts, SSSQLVarU.x,                  x);
      insert(inserts, SSSQLVarU.y,                  y);

      dbSQL.insert(learnEpEntityTable, inserts);

      inserts.clear();
      insert(inserts, SSSQLVarU.learnEpVersionId,   learnEpVersionUri);
      insert(inserts, SSSQLVarU.learnEpEntityId,    learnEpEntityUri);

      dbSQL.insert(learnEpVersionEntitiesTable, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void createLearnEp(
    final SSUri    learnEpUri,
    final SSUri    user) throws Exception{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarU.learnEpId, learnEpUri);
      
      dbSQL.insert(learnEpTable, inserts);
      
      inserts.clear();
      insert(inserts, SSSQLVarU.userId,    user);
      insert(inserts, SSSQLVarU.learnEpId, learnEpUri);
      
      dbSQL.insert(learnEpUserTable, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void setLearnEpCurrentVersion(
    final SSUri user, 
    final SSUri learnEpVersionUri) throws Exception {
    
    ResultSet resultSet  = null;
    
    try{
      
      final Map<String, String> wheres  = new HashMap<>();
      final Map<String, String> updates = new HashMap<>();
      final Map<String, String> inserts = new HashMap<>();
      
      where(wheres, SSSQLVarU.userId, user);
      
      resultSet = dbSQL.select(learnEpVersionCurrentTable, wheres);
      
      if(resultSet.first()){
        
        wheres.clear();
        
        where  (wheres,  SSSQLVarU.userId,           user);
        update (updates, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
        
        dbSQL.update(learnEpVersionCurrentTable, wheres, updates);
      }else{
        
        insert(inserts, SSSQLVarU.userId,           user);
        insert(inserts, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
        
        dbSQL.insert(learnEpVersionCurrentTable, inserts);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri setLearnEpVersionTimelineState(
    final SSUri learnEpTimelineStateUri, 
    final SSUri learnEpVersionUri, 
    final Long  startTime, 
    final Long  endTime) throws Exception {
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      final Map<String, String> deletes = new HashMap<>();
      
      insert(inserts, SSSQLVarU.learnEpTimelineStateId, learnEpTimelineStateUri);
      insert(inserts, SSSQLVarU.startTime,              startTime);
      insert(inserts, SSSQLVarU.endTime,                endTime);
      
      dbSQL.insert(learnEpTimelineStateTable, inserts);
      
      delete(deletes, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
      
      dbSQL.delete(learnEpVersionTimelineStatesTable, deletes);
      
      inserts.clear();
      insert(inserts, SSSQLVarU.learnEpVersionId,       learnEpVersionUri);
      insert(inserts, SSSQLVarU.learnEpTimelineStateId, learnEpTimelineStateUri);
      
      dbSQL.insert(learnEpVersionTimelineStatesTable, inserts);
      
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
      
      column(columns, learnEpVersionTimelineStatesTable, SSSQLVarU.learnEpTimelineStateId);
      column(columns, SSSQLVarU.startTime);
      column(columns, SSSQLVarU.endTime);
      
      table(tables, learnEpTimelineStateTable);
      table(tables, learnEpVersionTimelineStatesTable);
      
      where(wheres, SSSQLVarU.learnEpVersionId, learnEpVersionUri);
      
      tableCon(tableCons, learnEpTimelineStateTable, SSSQLVarU.learnEpTimelineStateId, learnEpVersionTimelineStatesTable, SSSQLVarU.learnEpTimelineStateId);
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      if(!resultSet.first()){
        throw new Exception("timelinestate for version not set");
      }
      
      return SSLearnEpTimelineState.get(
        bindingStrToUri (resultSet, SSSQLVarU.learnEpTimelineStateId),
        learnEpVersionUri,
        bindingStrToLong(resultSet, SSSQLVarU.startTime),
        bindingStrToLong(resultSet, SSSQLVarU.endTime));
        
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
        return null;
      }finally{
        dbSQL.closeStmt(resultSet);
      }
  }
  
  public SSUri createLearnEpVersionUri() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objLearnEpVersion().toString()));
  }
  
  public SSUri createLearnEpCircleUri() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objLearnEpCircle().toString()));
  }
  
  public SSUri createLearnEpEntityUri() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objLearnEpEntity().toString()));
  }
  
  public SSUri createLearnEpUri() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objLearnEp().toString()));
  }
  
  public SSUri createLearnEpTimelineStateUri() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objLearnEpTimelineState().toString()));
  }
  
  private SSUri objLearnEpVersion() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityE.learnEpVersion.toString());
  }
  
  private SSUri objLearnEpCircle() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityE.learnEpCircle.toString());
  }
  
  private SSUri objLearnEpEntity() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityE.learnEpEntity.toString());
  }
  
  private SSUri objLearnEp() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityE.learnEp.toString());
  }
  
  private SSUri objLearnEpTimelineState() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityE.learnEpTimelineState.toString());
  }
}

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