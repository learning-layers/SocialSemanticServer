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

import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.*;
import at.tugraz.sss.conf.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class SSLearnEpSQLFct extends SSCoreSQL{
  
  public SSLearnEpSQLFct(final SSDBSQLI dbSQL){
    super(dbSQL);
  }
  
  public SSLearnEp getLearnEp(
    final SSServPar servPar,
    final SSUri learnEpUri) throws SSErr{
    
    ResultSet resultSet  = null;
    
    try{
      
      final List<String>                          columns   = new ArrayList<>();
      final List<MultivaluedMap<String, String>>  wheres    = new ArrayList<>();
      final List<SSSQLTableI>                     tables    = new ArrayList<>();
      final List<String>                          tableCons = new ArrayList<>();
      
      column(columns, SSLearnEpSQLTableE.learnep, SSSQLVarNames.learnEpId);
      
      table(tables, SSLearnEpSQLTableE.learnep);
      
      final MultivaluedMap<String, String> whereLearnEp = new MultivaluedHashMap<>();
      
      where(whereLearnEp, SSLearnEpSQLTableE.learnep, SSSQLVarNames.learnEpId, learnEpUri);
      
      wheres.add(whereLearnEp);
      
      resultSet = 
        dbSQL.select(
          new SSDBSQLSelectPar(
            servPar, 
            tables, 
            columns, 
            wheres, 
            null, 
            null, 
            tableCons));
        
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return SSLearnEp.get(bindingStrToUri(resultSet, SSSQLVarNames.learnEpId));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getLearnEpUserURIs(
    final SSServPar servPar,
    final SSUri learnEp) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.userId);
      
      where(wheres, SSSQLVarNames.learnEpId, learnEp);
    
      resultSet = dbSQL.select(servPar, SSLearnEpSQLTableE.learnepuser, columns, wheres, null, null, null);

      return getURIsFromResult(resultSet, SSSQLVarNames.userId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getLearnEpURIs(
    final SSServPar servPar) throws SSErr{
    
    ResultSet resultSet  = null;
    
    try{
      
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        columns   = new ArrayList<>();
      
      column(columns, SSSQLVarNames.learnEpId);
      
      resultSet = dbSQL.select(servPar, SSLearnEpSQLTableE.learnep, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.learnEpId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getLearnEpURIs(
    final SSServPar servPar,
    final SSUri           user) throws SSErr{
    
    ResultSet resultSet  = null;
    
    try{
      
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        columns   = new ArrayList<>();
      
      column(columns, SSSQLVarNames.learnEpId);
      
      where(wheres, SSSQLVarNames.userId, user);
      
      resultSet = dbSQL.select(servPar, SSLearnEpSQLTableE.learnepuser, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.learnEpId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  
  public SSUri getLearnEpCurrentVersionURI(
    final SSServPar servPar,
    final SSUri user) throws SSErr {
    
    ResultSet resultSet = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns,  SSSQLVarNames.learnEpVersionId);
      
      where(wheres, SSSQLVarNames.userId, user);
      
      resultSet = dbSQL.select(servPar, SSLearnEpSQLTableE.learnepversioncurrent, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
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
    final SSServPar servPar,
    final SSUri   learnEpVersionUri,
    final Boolean setCircles,
    final Boolean setEntities) throws SSErr {
    
    ResultSet                  resultSet       = null;
    
    try{
      final Map<String, String>    wheres      = new HashMap<>();
      final List<SSSQLTableI>      tables      = new ArrayList<>();
      final List<String>           columns     = new ArrayList<>();
      final List<String>           tableCons   = new ArrayList<>();

      column(columns, SSSQLVarNames.learnEpVersionId);
      column(columns, SSSQLVarNames.learnEpId);
      
      table(tables, SSLearnEpSQLTableE.learnepversions);
      
      where(wheres, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
      
      resultSet = 
        dbSQL.select(
          servPar, 
          tables, 
          columns, 
          wheres, 
          tableCons, 
          null, 
          null, 
          null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      final SSLearnEpVersion learnEpVersion =
        SSLearnEpVersion.get(
          bindingStrToUri               (resultSet, SSSQLVarNames.learnEpVersionId),
          null,
          null,
          null,
          null,
          bindingStrToUri               (resultSet, SSSQLVarNames.learnEpId),
          null, //entities, 
          null); //circles, 
        
      
      if(setCircles){
        learnEpVersion.learnEpCircles.addAll(getLearnEpVersionCircles      (servPar, learnEpVersionUri));
      }
      
      if(setEntities){
        learnEpVersion.learnEpEntities.addAll(getLearnEpVersionEntities     (servPar, learnEpVersionUri));
      }
      
//      if(setTimelineState){
//        learnEpVersion.learnEpTimelineState = getLearnEpVersionTimelineState(learnEpVersionUri);
//      }
      
      return learnEpVersion;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getLearnEpVersionURIs(
    final SSServPar servPar,
    final SSUri learnEpUri) throws SSErr {
    
    ResultSet resultSet       = null;

    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.learnEpVersionId);
      
      where(wheres, SSSQLVarNames.learnEpId, learnEpUri);
      
      resultSet = dbSQL.select(servPar, SSLearnEpSQLTableE.learnepversions, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.learnEpVersionId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSEntity> getLearnEpVersionCircles(
    final SSServPar servPar,
    final SSUri learnEpVersionUri) throws SSErr{
    
    ResultSet resultSet          = null;
    
    try{
      final List<SSEntity>             learnEpCircles     = new ArrayList<>();
      final List<String>               columns            = new ArrayList<>();
      final List<SSSQLTableI>          tables             = new ArrayList<>();
      final List<String>               tableCons          = new ArrayList<>();
      final Map<String, String>        wheres             = new HashMap<>();
      
      setEntityColumns(columns);
      column(columns, SSLearnEpSQLTableE.learnepcircle, SSSQLVarNames.learnEpCircleId);
      column(columns, SSLearnEpSQLTableE.learnepcircle, SSSQLVarNames.xLabel);
      column(columns, SSLearnEpSQLTableE.learnepcircle, SSSQLVarNames.yLabel);
      column(columns, SSLearnEpSQLTableE.learnepcircle, SSSQLVarNames.xR);
      column(columns, SSLearnEpSQLTableE.learnepcircle, SSSQLVarNames.yR);
      column(columns, SSLearnEpSQLTableE.learnepcircle, SSSQLVarNames.xC);
      column(columns, SSLearnEpSQLTableE.learnepcircle, SSSQLVarNames.yC);
      
      where(wheres, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
      
      setEntityTable(tables);
      table(tables, SSLearnEpSQLTableE.learnepversioncircles);
      table(tables, SSLearnEpSQLTableE.learnepcircle);
      
      tableCon(tableCons, SSEntitySQLTableE.entity,                 SSSQLVarNames.id,              SSLearnEpSQLTableE.learnepcircle, SSSQLVarNames.learnEpCircleId);
      tableCon(tableCons, SSLearnEpSQLTableE.learnepversioncircles, SSSQLVarNames.learnEpCircleId, SSLearnEpSQLTableE.learnepcircle, SSSQLVarNames.learnEpCircleId);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        learnEpCircles.add(
          SSLearnEpCircle.get(
            bindingStrToUri          (resultSet, SSSQLVarNames.id),
            bindingStrToLabel        (resultSet, SSSQLVarNames.label),
            bindingStrToTextComment  (resultSet, SSSQLVarNames.description),
            bindingStrToLong         (resultSet, SSSQLVarNames.creationTime),
            getAuthorEntityFromResult(servPar, resultSet, SSConf.systemUserUri),
            bindingStrToFloat        (resultSet, SSSQLVarNames.xLabel),
            bindingStrToFloat        (resultSet, SSSQLVarNames.yLabel),
            bindingStrToFloat        (resultSet, SSSQLVarNames.xR),
            bindingStrToFloat        (resultSet, SSSQLVarNames.yR),
            bindingStrToFloat        (resultSet, SSSQLVarNames.xC),
            bindingStrToFloat        (resultSet, SSSQLVarNames.yC)));
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
    final SSServPar servPar,
    final SSUri learnEpVersionUri) throws SSErr{
    
    ResultSet resultSet          = null;
    
    try{
      
      final List<SSEntity>             learnEpEntities    = new ArrayList<>();
      final List<String>               columns            = new ArrayList<>();
      final List<SSSQLTableI>          tables             = new ArrayList<>();
      final List<String>               tableCons          = new ArrayList<>();
      final Map<String, String>        wheres             = new HashMap<>();
    
      column(columns, SSLearnEpSQLTableE.learnepentity, SSSQLVarNames.learnEpEntityId);
      column(columns, SSSQLVarNames.entityId);
      column(columns, SSSQLVarNames.x);
      column(columns, SSSQLVarNames.y);

      table(tables, SSLearnEpSQLTableE.learnepversionentities);
      table(tables, SSLearnEpSQLTableE.learnepentity);
      
      where(wheres, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
      
      tableCon(tableCons, SSLearnEpSQLTableE.learnepversionentities, SSSQLVarNames.learnEpEntityId, SSLearnEpSQLTableE.learnepentity, SSSQLVarNames.learnEpEntityId);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
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
    final SSServPar servPar,
    final SSUri learnEpVersionUri,
    final SSUri learnEpUri) throws SSErr{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
      
      dbSQL.insert(servPar, SSLearnEpSQLTableE.learnepversion, inserts);
      
      inserts.clear();
      insert(inserts, SSSQLVarNames.learnEpId,        learnEpUri);
      insert(inserts, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
      
      dbSQL.insert(servPar, SSLearnEpSQLTableE.learnepversions, inserts);
      
      return learnEpVersionUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public void addCircleToLearnEpVersion(
    final SSServPar servPar,
    final SSUri      learnEpCircleUri,
    final SSUri      learnEpVersionUri,
    final SSLabel    label,
    final Float      xLabel,
    final Float      yLabel,
    final Float      xR,
    final Float      yR,
    final Float      xC,
    final Float      yC) throws SSErr{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.learnEpCircleId,    learnEpCircleUri);
      insert(inserts, SSSQLVarNames.xLabel,             xLabel);
      insert(inserts, SSSQLVarNames.yLabel,             yLabel);
      insert(inserts, SSSQLVarNames.xR,                 xR);
      insert(inserts, SSSQLVarNames.yR,                 yR);
      insert(inserts, SSSQLVarNames.xC,                 xC);
      insert(inserts, SSSQLVarNames.yC,                 yC);
      
      dbSQL.insert(servPar, SSLearnEpSQLTableE.learnepcircle, inserts);
      
      inserts.clear();
      insert(inserts, SSSQLVarNames.learnEpVersionId,   learnEpVersionUri);
      insert(inserts, SSSQLVarNames.learnEpCircleId,    learnEpCircleUri);
      
      dbSQL.insert(servPar, SSLearnEpSQLTableE.learnepversioncircles, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void updateCircle(
    final SSServPar servPar,
    final SSUri      learnEpCircleUri,
    final Float      xLabel,
    final Float      yLabel,
    final Float      xR,
    final Float      yR,
    final Float      xC,
    final Float      yC) throws SSErr{
    
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
      
      dbSQL.update(servPar, SSLearnEpSQLTableE.learnepcircle, wheres, updates);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void updateEntity(
    final SSServPar servPar,
    final SSUri    learnEpEntityUri,
    final Float    x,
    final Float    y) throws SSErr{
    
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
      
      dbSQL.update(servPar, SSLearnEpSQLTableE.learnepentity, wheres, updates);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addEntityToLearnEpVersion(
    final SSServPar servPar,
    final SSUri    learnEpEntityUri, 
    final SSUri    learnEpVersionUri,
    final SSUri    entityUri,
    final Float    x,
    final Float    y) throws SSErr{
    
    try{
      final Map<String, String> inserts = new HashMap<>();

      insert(inserts, SSSQLVarNames.learnEpEntityId,    learnEpEntityUri);
      insert(inserts, SSSQLVarNames.entityId,           entityUri);
      insert(inserts, SSSQLVarNames.x,                  x);
      insert(inserts, SSSQLVarNames.y,                  y);

      dbSQL.insert(servPar, SSLearnEpSQLTableE.learnepentity, inserts);

      inserts.clear();
      insert(inserts, SSSQLVarNames.learnEpVersionId,   learnEpVersionUri);
      insert(inserts, SSSQLVarNames.learnEpEntityId,    learnEpEntityUri);

      dbSQL.insert(servPar, SSLearnEpSQLTableE.learnepversionentities, inserts);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void createLearnEp(
    final SSServPar servPar,
    final SSUri    learnEpUri,
    final SSUri    user) throws SSErr{
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.learnEpId, learnEpUri);
      
      dbSQL.insert(servPar, SSLearnEpSQLTableE.learnep, inserts);
      
      addLearnEp(servPar, learnEpUri, user);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addLearnEp(
    final SSServPar servPar,
    final SSUri    learnEpUri,
    final SSUri    user) throws SSErr{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userId,    user);
      insert(inserts, SSSQLVarNames.learnEpId, learnEpUri);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.userId,    user);
      uniqueKey(uniqueKeys, SSSQLVarNames.learnEpId, learnEpUri);
      
      dbSQL.insertIfNotExists(servPar, SSLearnEpSQLTableE.learnepuser, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void deleteCurrentEpVersion(
    final SSServPar servPar,
    final SSUri user,
    final SSUri learnEpVersion) throws SSErr {
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.userId,           user);
      where(wheres, SSSQLVarNames.learnEpVersionId, learnEpVersion);
      
      dbSQL.delete(servPar, SSLearnEpSQLTableE.learnepversioncurrent, wheres);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void setLearnEpCurrentVersion(
    final SSServPar servPar,
    final SSUri user, 
    final SSUri learnEpVersionUri) throws SSErr {
    
    ResultSet resultSet  = null;
    
    try{
      
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      final Map<String, String> updates = new HashMap<>();
      final Map<String, String> inserts = new HashMap<>();
      
      column(columns, SSSQLVarNames.learnEpVersionId);
      
      where(wheres, SSSQLVarNames.userId, user);
      
      resultSet = dbSQL.select(servPar, SSLearnEpSQLTableE.learnepversioncurrent, columns, wheres, null, null, null);
      
      if(resultSet.first()){
        
        wheres.clear();
        
        where  (wheres,  SSSQLVarNames.userId,           user);
        update (updates, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
        
        dbSQL.update(servPar, SSLearnEpSQLTableE.learnepversioncurrent, wheres, updates);
      }else{
        
        insert(inserts, SSSQLVarNames.userId,           user);
        insert(inserts, SSSQLVarNames.learnEpVersionId, learnEpVersionUri);
        
        dbSQL.insert(servPar, SSLearnEpSQLTableE.learnepversioncurrent, inserts);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void createTimelineState(
    final SSServPar servPar,
    final SSUri user,
    final SSUri timelineStateURI,
    final Long  startTime, 
    final Long  endTime) throws SSErr {
    
    try{
      final Map<String, String> inserts = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.userId,                  user);
      insert(inserts, SSSQLVarNames.learnEpTimelineStateId,  timelineStateURI);
      insert(inserts, SSSQLVarNames.startTime,               startTime);
      insert(inserts, SSSQLVarNames.endTime,                 endTime);
      
      dbSQL.insert(servPar, SSLearnEpSQLTableE.learneptimelinestate, inserts);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
    
  public void updateTimelineState(
    final SSServPar servPar,
    final SSUri timelineStateURI,
    final Long  startTime, 
    final Long  endTime) throws SSErr {
    
    try{
      final Map<String, String> updates = new HashMap<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      update(updates, SSSQLVarNames.startTime,              startTime);
      update(updates, SSSQLVarNames.endTime,                endTime);
      
      where(wheres, SSSQLVarNames.learnEpTimelineStateId, timelineStateURI);
      
      dbSQL.update(servPar, SSLearnEpSQLTableE.learneptimelinestate, wheres, updates);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public SSLearnEpTimelineState getTimelineState(
    final SSServPar servPar,
    final SSUri user) throws SSErr{
    
    ResultSet resultSet        = null;
    
    try{
      
      final Map<String, String>    wheres      = new HashMap<>();
      final List<String>           columns     = new ArrayList<>();
      
      column(columns, SSSQLVarNames.learnEpTimelineStateId);
      column(columns, SSSQLVarNames.startTime);
      column(columns, SSSQLVarNames.endTime);
      
      where(wheres, SSSQLVarNames.userId, user);
      
      resultSet = 
        dbSQL.select(
          servPar, 
          SSLearnEpSQLTableE.learneptimelinestate,
          columns, 
          wheres, 
          null, 
          null, 
          null);

      if(!resultSet.first()){
        return null;
      }
      
      return SSLearnEpTimelineState.get(
        bindingStrToUri (resultSet, SSSQLVarNames.learnEpTimelineStateId),
        user,
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
    final SSServPar servPar,
    final SSUri learnEpVersion) throws SSErr{
    
    ResultSet resultSet;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.learnEpId);
      
      where(wheres, SSSQLVarNames.learnEpVersionId, learnEpVersion);
      
      resultSet = dbSQL.select(servPar, SSLearnEpSQLTableE.learnepversions, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return bindingStrToUri(resultSet, SSSQLVarNames.learnEpId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSUri getLearnEpVersionForCircle(
    final SSServPar servPar,
    final SSUri circle) throws SSErr{
    
    ResultSet resultSet;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.learnEpVersionId);
      
      where(wheres, SSSQLVarNames.learnEpCircleId, circle);
      
      resultSet = dbSQL.select(servPar, SSLearnEpSQLTableE.learnepversioncircles, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return bindingStrToUri(resultSet, SSSQLVarNames.learnEpVersionId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSUri getLearnEpVersionForEntity(
    final SSServPar servPar,
    final SSUri entity) throws SSErr{
    
    ResultSet resultSet;
    
    try{
      final List<String>        columns = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.learnEpVersionId);
      
      where(wheres, SSSQLVarNames.learnEpEntityId, entity);
      
      resultSet = dbSQL.select(servPar, SSLearnEpSQLTableE.learnepversionentities, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return bindingStrToUri(resultSet, SSSQLVarNames.learnEpVersionId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public SSUri getLearnEpForEntity(
    final SSServPar servPar,
    final SSUri learnEpEntity) throws SSErr{
    
    ResultSet resultSet;
    
    try{
      final List<String>        columns   = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      final List<SSSQLTableI>   tables    = new ArrayList<>();
      final List<String>        tableCons = new ArrayList<>();
      
      column(columns, SSLearnEpSQLTableE.learnep, SSSQLVarNames.learnEpId);
      
      where(wheres, SSLearnEpSQLTableE.learnepversionentities, SSSQLVarNames.learnEpEntityId, learnEpEntity);
      
      table(tables, SSLearnEpSQLTableE.learnepversionentities);
      table(tables, SSLearnEpSQLTableE.learnepversions);
      table(tables, SSLearnEpSQLTableE.learnep);
      
      tableCon(tableCons, SSLearnEpSQLTableE.learnepversionentities, SSSQLVarNames.learnEpVersionId, SSLearnEpSQLTableE.learnepversions, SSSQLVarNames.learnEpVersionId);
      tableCon(tableCons, SSLearnEpSQLTableE.learnep,                SSSQLVarNames.learnEpId,        SSLearnEpSQLTableE.learnepversions, SSSQLVarNames.learnEpId);
      
      resultSet = 
        dbSQL.select(
          servPar, 
          tables,
          columns, 
          wheres, 
          tableCons, 
          null, 
          null, 
          null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return bindingStrToUri(resultSet, SSSQLVarNames.learnEpId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSUri getLearnEpForCircle(
    final SSServPar servPar,
    final SSUri learnEpCircle) throws SSErr{
    
    ResultSet resultSet;
    
    try{
      final List<String>        columns   = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      final List<SSSQLTableI>   tables    = new ArrayList<>();
      final List<String>        tableCons = new ArrayList<>();
      
      column(columns, SSLearnEpSQLTableE.learnep, SSSQLVarNames.learnEpId);
      
      where(wheres, SSLearnEpSQLTableE.learnepversioncircles, SSSQLVarNames.learnEpCircleId, learnEpCircle);
      
      table(tables, SSLearnEpSQLTableE.learnepversioncircles);
      table(tables, SSLearnEpSQLTableE.learnepversions);
      table(tables, SSLearnEpSQLTableE.learnep);
      
      tableCon(tableCons, SSLearnEpSQLTableE.learnepversioncircles,  SSSQLVarNames.learnEpVersionId, SSLearnEpSQLTableE.learnepversions, SSSQLVarNames.learnEpVersionId);
      tableCon(tableCons, SSLearnEpSQLTableE.learnep,                SSSQLVarNames.learnEpId,        SSLearnEpSQLTableE.learnepversions, SSSQLVarNames.learnEpId);
      
      resultSet = 
        dbSQL.select(
          servPar, 
          tables,
          columns, 
          wheres, 
          tableCons, 
          null, 
          null, 
          null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return bindingStrToUri(resultSet, SSSQLVarNames.learnEpId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public void deleteCircle(
    final SSServPar servPar,
    final SSUri learnEpCircle) throws SSErr{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.learnEpCircleId, learnEpCircle);
      
      dbSQL.deleteIgnore(servPar, SSLearnEpSQLTableE.learnepversioncircles, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public void deleteEntity(
    final SSServPar servPar,
    final SSUri learnEpEntity) throws SSErr{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.learnEpEntityId, learnEpEntity);
      
      dbSQL.deleteIgnore(servPar, SSLearnEpSQLTableE.learnepversionentities, wheres);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public SSUri getEntity(
    final SSServPar servPar,
    final SSUri learnEpVersion, 
    final SSUri learnEpEntity) throws SSErr{
    
    ResultSet resultSet;
    
    try{
      final List<String>        columns   = new ArrayList<>();
      final List<SSSQLTableI>   tables    = new ArrayList<>();
      final Map<String, String> wheres    = new HashMap<>();
      final List<String>        tableCons = new ArrayList<>();
      
      column(columns, SSSQLVarNames.entityId);
      
      table(tables, SSLearnEpSQLTableE.learnepentity);
      table(tables, SSLearnEpSQLTableE.learnepversionentities);
      
      where(wheres, SSLearnEpSQLTableE.learnepversionentities, SSSQLVarNames.learnEpEntityId,  learnEpEntity);
      where(wheres, SSLearnEpSQLTableE.learnepversionentities, SSSQLVarNames.learnEpVersionId, learnEpVersion);
      
      tableCon(tableCons, SSLearnEpSQLTableE.learnepentity, SSSQLVarNames.learnEpEntityId, SSLearnEpSQLTableE.learnepversionentities, SSSQLVarNames.learnEpEntityId);
      
      resultSet = dbSQL.select(servPar, tables, columns, wheres, tableCons, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
      return bindingStrToUri(resultSet, SSSQLVarNames.entityId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public void removeLearnEpForUser(
    final SSServPar servPar,
    final SSUri user, 
    final SSUri learnEp) throws SSErr{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.learnEpId, learnEp);
      where(wheres, SSSQLVarNames.userId,    user);

      dbSQL.delete(servPar, SSLearnEpSQLTableE.learnepuser, wheres);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }    
  }
}


//      insert(inserts, SSSQLVarU.learnEpVersionId,   learnEpVersionUri);
//      insert(inserts, SSSQLVarU.learnEpCircleId,    learnEpCircleUri);
//      
//      dbSQL.insert(learnEpVersionCirclesTable, inserts);

//  private SSLearnEpCircle getLearnEpCircle(SSUri circleUri) throws SSErr {
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
  
//  private SSLearnEpEntity getLearnEpEntity(SSUri entityUri) throws SSErr {
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

//  public Boolean isLearnEp(SSUri entityUri) throws SSErr{
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
//  public Boolean isLearnEpVersion(SSUri entityUri) throws SSErr{
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
//  public Boolean isLearnEpCircle(SSUri entityUri) throws SSErr{
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
//  public Boolean isLearnEpEntity(SSUri entityUri) throws SSErr{
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
//  public Boolean isLearnEpTimelineState(SSUri entityUri) throws SSErr{
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
//    final SSUri learnEp) throws SSErr {
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