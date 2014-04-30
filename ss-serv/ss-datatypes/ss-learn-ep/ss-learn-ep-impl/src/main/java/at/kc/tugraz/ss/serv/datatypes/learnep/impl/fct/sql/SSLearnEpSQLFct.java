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
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
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
    Map<String, String> selectPars = new HashMap<String, String>();
    Boolean             isLearnEp  = false;
    
    selectPars.put(SSSQLVarU.learnEpId, entityUri.toString());
    
    try{
      
      resultSet  = dbSQL.selectAllWhere(learnEpTable, selectPars);
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
    Map<String, String> selectPars = new HashMap<String, String>();
    Boolean             isVersion  = false;
    
    selectPars.put(SSSQLVarU.learnEpVersionId, entityUri.toString());
    
    try{
      
      resultSet  = dbSQL.selectAllWhere(learnEpVersionTable, selectPars);
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
    Map<String, String> selectPars = new HashMap<String, String>();
    Boolean             isCircle   = false;
    
    selectPars.put(SSSQLVarU.learnEpCircleId, entityUri.toString());
    
    try{
      
      resultSet = dbSQL.selectAllWhere(learnEpCircleTable, selectPars);
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
    Map<String, String> selectPars = new HashMap<String, String>();
    Boolean             isEntity   = false;
    
    selectPars.put(SSSQLVarU.learnEpEntityId, entityUri.toString());
    
    try{
      
      resultSet = dbSQL.selectAllWhere(learnEpEntityTable, selectPars);
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
    Map<String, String> selectPars        = new HashMap<String, String>();
    Boolean             isTimelineState   = false;
    
    selectPars.put(SSSQLVarU.learnEpTimelineStateId, entityUri.toString());
    
    try{
      
      resultSet       = dbSQL.selectAllWhere(learnEpTimelineStateTable, selectPars);
      isTimelineState = resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return isTimelineState;
  }
  
  public List<SSLearnEp> getLearnEpsForUser(SSUri user) throws Exception{
    
    List<SSLearnEp>     learnEps   = new ArrayList<SSLearnEp>();
    ResultSet           resultSet  = null;
    Map<String, String> selectPars = new HashMap<String, String>();
    
    selectPars.put(SSSQLVarU.userId, user.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(learnEpUserTable, selectPars);
      
      while(resultSet.next()){
        learnEps.add(
          SSLearnEp.get(
          user,
          bindingStrToUri   (resultSet, SSSQLVarU.learnEpId),
          null));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return learnEps;
  }
  
  public SSUri getLearnEpCurrentVersionUri(SSUri user) throws Exception {
   
    ResultSet                  resultSet                = null;
    Map<String, String>        selectPars               = new HashMap<String, String>();
    SSUri                      currentLearnEpVersionUri = null;
    
    selectPars.put(SSSQLVarU.userId, user.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(learnEpVersionCurrentTable, selectPars);
      
      if(!resultSet.first()){
        return null;
      }
      
      currentLearnEpVersionUri = bindingStrToUri(resultSet, SSSQLVarU.learnEpVersionId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return currentLearnEpVersionUri;
  }
  
  public SSLearnEpVersion getLearnEpVersion(SSUri learnEpVersionUri) throws Exception {
    
    ResultSet                  resultSet       = null;
    Map<String, String>        selectPars      = new HashMap<String, String>();
    SSLearnEpVersion           learnEpVersion  = null;
    
    selectPars.put(SSSQLVarU.learnEpVersionId, learnEpVersionUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(learnEpVersionsTable, selectPars);
      
      resultSet.first();
      
      learnEpVersion =
        SSLearnEpVersion.get(
        learnEpVersionUri,
        bindingStrToUri (resultSet, SSSQLVarU.learnEpId),
        null,
        null,
        null);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    if(learnEpVersion == null){
      return learnEpVersion;
    }
    
    learnEpVersion.circles   = getLearnEpVersionCircles  (learnEpVersionUri);
    learnEpVersion.entities  = getLearnEpVersionEntities (learnEpVersionUri);
    
    return learnEpVersion;
  }
  
  public List<SSLearnEpVersion> getLearnEpVersions(SSUri learnEpUri) throws Exception {
    
    List<SSLearnEpVersion>     learnEpVersions = new ArrayList<SSLearnEpVersion>();
    ResultSet                  resultSet       = null;
    Map<String, String>        selectPars      = new HashMap<String, String>();
    
    selectPars.put(SSSQLVarU.learnEpId, learnEpUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(learnEpVersionsTable, selectPars);
      
      while(resultSet.next()){
        
        learnEpVersions.add(
          SSLearnEpVersion.get(
          bindingStrToUri (resultSet, SSSQLVarU.learnEpVersionId),
          learnEpUri,
          null,
          null,
          null));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    for(SSLearnEpVersion learnEpVersion : learnEpVersions){
      learnEpVersion.circles   = getLearnEpVersionCircles      (learnEpVersion.learnEpVersionUri);
      learnEpVersion.entities  = getLearnEpVersionEntities     (learnEpVersion.learnEpVersionUri);
    }
    
    return learnEpVersions;
  }
  
  private List<SSLearnEpCircle> getLearnEpVersionCircles(SSUri learnEpVersionUri) throws Exception{
    
    List<SSLearnEpCircle>      learnEpCircles     = new ArrayList<SSLearnEpCircle>();
    List<SSUri>                learnEpCirclesUris = new ArrayList<SSUri>();
    ResultSet                  resultSet          = null;
    Map<String, String>        selectPars         = new HashMap<String, String>();
    
    selectPars.put(SSSQLVarU.learnEpVersionId, learnEpVersionUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(learnEpVersionCirclesTable, selectPars);
      
      while(resultSet.next()){
        learnEpCirclesUris.add(bindingStrToUri(resultSet, SSSQLVarU.learnEpCircleId));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    for(SSUri circleUri : learnEpCirclesUris){
      learnEpCircles.add(getLearnEpCircle(circleUri));
    }
    
    return learnEpCircles;
  }
  
  private List<SSLearnEpEntity> getLearnEpVersionEntities(SSUri learnEpVersionUri) throws Exception{
    
    List<SSLearnEpEntity>      learnEpEntities    = new ArrayList<SSLearnEpEntity>();
    List<SSUri>                learnEpEntityUris  = new ArrayList<SSUri>();
    ResultSet                  resultSet          = null;
    Map<String, String>        selectPars         = new HashMap<String, String>();
    
    selectPars.put(SSSQLVarU.learnEpVersionId, learnEpVersionUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(learnEpVersionEntitiesTable, selectPars);
      
      while(resultSet.next()){
        learnEpEntityUris.add(bindingStrToUri(resultSet, SSSQLVarU.learnEpEntityId));
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    for(SSUri entityUri : learnEpEntityUris){
      learnEpEntities.add(getLearnEpEntity(entityUri));
    }
    
    return learnEpEntities;
  }
  
  private SSLearnEpCircle getLearnEpCircle(SSUri circleUri) throws Exception {
    
    ResultSet                  resultSet     = null;
    Map<String, String>        selectPars    = new HashMap<String, String>();
    SSLearnEpCircle            learnEpCircle = null;
    
    selectPars.put(SSSQLVarU.learnEpCircleId, circleUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(learnEpCircleTable, selectPars);
      
      resultSet.first();
      
      learnEpCircle = 
        SSLearnEpCircle.get(
        circleUri,
        null,
        bindingStrToFloat(resultSet, SSSQLVarU.xLabel),
        bindingStrToFloat(resultSet, SSSQLVarU.yLabel),
        bindingStrToFloat(resultSet, SSSQLVarU.xR),
        bindingStrToFloat(resultSet, SSSQLVarU.yR),
        bindingStrToFloat(resultSet, SSSQLVarU.xC),
        bindingStrToFloat(resultSet, SSSQLVarU.yC));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return learnEpCircle;
  }
  
  private SSLearnEpEntity getLearnEpEntity(SSUri entityUri) throws Exception {
    
    ResultSet                  resultSet     = null;
    Map<String, String>        selectPars    = new HashMap<String, String>();
    SSLearnEpEntity            learnEpEntity = null;
    
    selectPars.put(SSSQLVarU.learnEpEntityId, entityUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(learnEpEntityTable, selectPars);
      
      resultSet.first();
      
      learnEpEntity =
        SSLearnEpEntity.get(
        entityUri,
        bindingStrToUri    (resultSet, SSSQLVarU.entityId),
        bindingStrToFloat  (resultSet, SSSQLVarU.x),
        bindingStrToFloat  (resultSet, SSSQLVarU.y));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return learnEpEntity;
  }
  
  public SSUri createLearnEpVersion(SSUri learnEpVersionUri, SSUri learnEpUri) throws Exception{
    
    Map<String, String> insertPars;
    
    insertPars = new HashMap<String, String>();
    insertPars.put(SSSQLVarU.learnEpVersionId, learnEpVersionUri.toString());
    
    dbSQL.insert(learnEpVersionTable, insertPars);
    
    insertPars = new HashMap<String, String>();
    insertPars.put(SSSQLVarU.learnEpId,        learnEpUri.toString());
    insertPars.put(SSSQLVarU.learnEpVersionId, learnEpVersionUri.toString());
    
    dbSQL.insert(learnEpVersionsTable, insertPars);
    
    return learnEpVersionUri;
  }
  
  public void addCircleToLearnEpVersion(
    SSUri      learnEpCircleUri,
    SSUri      learnEpVersionUri,
    SSLabel label,
    Float      xLabel,
    Float      yLabel,
    Float      xR,
    Float      yR,
    Float      xC,
    Float      yC) throws Exception{
    
    try{
      Map<String, String> insertPars;
      
      insertPars = new HashMap<String, String>();
      insertPars.put(SSSQLVarU.learnEpCircleId,    learnEpCircleUri.toString());
      insertPars.put(SSSQLVarU.xLabel,             xLabel.toString());
      insertPars.put(SSSQLVarU.yLabel,             yLabel.toString());
      insertPars.put(SSSQLVarU.xR,                 xR.toString());
      insertPars.put(SSSQLVarU.yR,                 yR.toString());
      insertPars.put(SSSQLVarU.xC,                 xC.toString());
      insertPars.put(SSSQLVarU.yC,                 yC.toString());
      
      dbSQL.insert(learnEpCircleTable, insertPars);
      
      insertPars = new HashMap<String, String>();
      insertPars.put(SSSQLVarU.learnEpVersionId,   learnEpVersionUri.toString());
      insertPars.put(SSSQLVarU.learnEpCircleId,    learnEpCircleUri.toString());
      
      dbSQL.insert(learnEpVersionCirclesTable, insertPars);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeEntity(SSUri entityUri) throws Exception{
    
    if(entityUri == null){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return;
    }
    
    Map<String, String> deletePars = new HashMap<String, String>();
    
    try{
    
      deletePars.put(SSSQLVarU.learnEpEntityId, SSUri.toStr(entityUri));
      
      dbSQL.deleteWhere(learnEpEntityTable, deletePars);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeCircle(SSUri circleUri) throws Exception{
    
    if(circleUri == null){
      SSServErrReg.regErrThrow(new Exception("circle uri null"));
      return;
    }
    
    Map<String, String> deletePars = new HashMap<String, String>();
    
    try{
    
      deletePars.put(SSSQLVarU.learnEpCircleId, SSUri.toStr(circleUri));
      
      dbSQL.deleteWhere(learnEpCircleTable, deletePars);
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
    
    updatePars = new HashMap<String, String>();
    newValues  = new HashMap<String, String>();
    updatePars.put(SSSQLVarU.learnEpCircleId,   learnEpCircleUri.toString());
    newValues.put(SSSQLVarU.xLabel,             xLabel.toString());
    newValues.put(SSSQLVarU.yLabel,             yLabel.toString());
    newValues.put(SSSQLVarU.xR,                 xR.toString());
    newValues.put(SSSQLVarU.yR,                 yR.toString());
    newValues.put(SSSQLVarU.xC,                 xC.toString());
    newValues.put(SSSQLVarU.yC,                 yC.toString());
    
    dbSQL.updateWhere(learnEpCircleTable, updatePars, newValues);
  }
  
  public void updateEntity(
    SSUri    learnEpEntityUri,
    SSUri    entityUri,
    Float    x,
    Float    y) throws Exception{
    
    Map<String, String> updatePars;
    Map<String, String> newValues;
    
    updatePars = new HashMap<String, String>();
    newValues = new HashMap<String, String>();
    updatePars.put(SSSQLVarU.learnEpEntityId,   learnEpEntityUri.toString());
    newValues.put(SSSQLVarU.entityId,           entityUri.toString());
    newValues.put(SSSQLVarU.x,                  x.toString());
    newValues.put(SSSQLVarU.y,                  y.toString());
    
    dbSQL.updateWhere(learnEpEntityTable, updatePars, newValues);
  }
  
  public void addEntityToLearnEpVersion(
    SSUri    learnEpEntityUri, 
    SSUri    learnEpVersionUri,
    SSUri    entityUri,
    Float    x,
    Float    y) throws Exception{
    
    Map<String, String> insertPars;
    
    insertPars = new HashMap<String, String>();
    insertPars.put(SSSQLVarU.learnEpEntityId,    learnEpEntityUri.toString());
    insertPars.put(SSSQLVarU.entityId,           entityUri.toString());
    insertPars.put(SSSQLVarU.x,                  x.toString());
    insertPars.put(SSSQLVarU.y,                  y.toString());
    
    dbSQL.insert(learnEpEntityTable, insertPars);
    
    insertPars = new HashMap<String, String>();
    insertPars.put(SSSQLVarU.learnEpVersionId,   learnEpVersionUri.toString());
    insertPars.put(SSSQLVarU.learnEpEntityId,    learnEpEntityUri.toString());
    
    dbSQL.insert(learnEpVersionEntitiesTable, insertPars);
  }
  
  public void createLearnEp(SSUri learnEpUri, SSUri user, SSSpaceE space) throws Exception{
    
    Map<String, String> insertPars;
    
    insertPars = new HashMap<String, String>();
    insertPars.put(SSSQLVarU.learnEpId,    learnEpUri.toString());
    
    dbSQL.insert(learnEpTable, insertPars);
    
    insertPars = new HashMap<String, String>();
    insertPars.put(SSSQLVarU.userId,       user.toString());
    insertPars.put(SSSQLVarU.learnEpId,    learnEpUri.toString());
    insertPars.put(SSSQLVarU.learnEpSpace, space.toString());
    
    dbSQL.insert(learnEpUserTable, insertPars);
  }
  
  public void setLearnEpCurrentVersion(SSUri user, SSUri learnEpVersionUri) throws Exception {
    
    Map<String, String> selectPars = new HashMap<String, String>();
    ResultSet           resultSet  = null;
    Map<String, String> updatePars;
    Map<String, String> insertPars;
    Map<String, String> newValues;
    
    selectPars.put(SSSQLVarU.userId, user.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(learnEpVersionCurrentTable, selectPars);
      
      if(resultSet.first()){
        
        updatePars = new HashMap<String, String>();
        newValues  = new HashMap<String, String>();
        updatePars.put (SSSQLVarU.userId,           user.toString());
        newValues.put  (SSSQLVarU.learnEpVersionId, learnEpVersionUri.toString());
        
        dbSQL.updateWhere(learnEpVersionCurrentTable, updatePars, newValues);
      }else{
        
        insertPars = new HashMap<String, String>();
        insertPars.put (SSSQLVarU.userId,           user.toString());
        insertPars.put (SSSQLVarU.learnEpVersionId, learnEpVersionUri.toString());
        
        dbSQL.insert(learnEpVersionCurrentTable, insertPars);
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
    
    if(SSObjU.isNull(learnEpTimelineStateUri, learnEpVersionUri,startTime, endTime)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
    
    final Map<String, String> insertPars = new HashMap<String, String>();
    final Map<String, String> deletePars = new HashMap<String, String>();
    
    try{
      insertPars.put(SSSQLVarU.learnEpTimelineStateId, learnEpTimelineStateUri.toString());
      insertPars.put(SSSQLVarU.startTime,              startTime.toString());
      insertPars.put(SSSQLVarU.endTime,                endTime.toString());
      
      dbSQL.insert(learnEpTimelineStateTable, insertPars);
      
      deletePars.put(SSSQLVarU.learnEpVersionId, learnEpVersionUri.toString());
        
      dbSQL.deleteWhere(learnEpVersionTimelineStatesTable, deletePars);
      
      insertPars.clear();
      insertPars.put(SSSQLVarU.learnEpVersionId,       learnEpVersionUri.toString());
      insertPars.put(SSSQLVarU.learnEpTimelineStateId, learnEpTimelineStateUri.toString());
      
      dbSQL.insert(learnEpVersionTimelineStatesTable, insertPars);
      
      return learnEpTimelineStateUri;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
    }
  }
  
  public SSLearnEpTimelineState getLearnEpVersionTimelineState(SSUri learnEpVersionUri) throws Exception{
    
    Map<String, String>    selectPars       = new HashMap<String, String>();
    ResultSet              resultSet        = null;
    SSLearnEpTimelineState timelineState    = null;
    SSUri                  timelineStateUri = null;
    
    selectPars.put(SSSQLVarU.learnEpVersionId, learnEpVersionUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(learnEpVersionTimelineStatesTable, selectPars);
      
      resultSet.first();
      
      timelineStateUri = bindingStrToUri(resultSet, SSSQLVarU.learnEpTimelineStateId);
      
    }catch(Exception error){
      SSServErrReg.regErr(new Exception("timeline state not available for version"));
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    selectPars = new HashMap<String, String>();
    selectPars.put(SSSQLVarU.learnEpTimelineStateId, SSStrU.toString(timelineStateUri));
    
    try{
      resultSet = dbSQL.selectAllWhere(learnEpTimelineStateTable, selectPars);
      
      resultSet.first();
      
      timelineState =
        SSLearnEpTimelineState.get(
        timelineStateUri,
        learnEpVersionUri,
        bindingStrToLong(resultSet, SSSQLVarU.startTime),
        bindingStrToLong(resultSet, SSSQLVarU.endTime));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }

    return timelineState;
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
