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
package at.kc.tugraz.ss.service.userevent.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSUESQLFct extends SSDBSQLFct{

  protected static final String               uesTable = "ues";
  
  public SSUESQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }

  public SSUE getUE(SSUri ueUri) throws Exception{
    
    Map<String, String>  selectPars = new HashMap<String, String>();
    ResultSet            resultSet  = null;
    SSUE                 ue         = null;
    
    try{
      
      selectPars.put(SSSQLVarU.userEventId, ueUri.toString());
      
      resultSet = dbSQL.select(uesTable, selectPars);
      
      resultSet.first();
      
      ue = SSUE.get(
        ueUri, 
        bindingStrToUri        (resultSet, SSSQLVarU.userId), 
        SSUEE.get(bindingStr(resultSet, SSSQLVarU.eventType)), 
        bindingStrToUri        (resultSet, SSSQLVarU.entityId), 
        bindingStr             (resultSet, SSSQLVarU.content), 
        null);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return ue;
  }
  
  //TODO dtheiler: include start and end date in sql query already
  public List<SSUE> getUEs(
    final SSUri    forUserUri,
    final SSUri    entityUri,
    final SSUEE    eventType,
    final Long     startTime,
    final Long     endTime) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>         tables       = new ArrayList<String>();
      final List<String>         columns      = new ArrayList<String>();
      final List<String>         tableCons    = new ArrayList<String>();
      final Map<String, String>  wheres       = new HashMap<String, String>();
      final List<SSUE>           ues          = new ArrayList<SSUE>();
      
      SSUEE                      eventTypeFromDB;
      Long                       timestamp;
      
      table    (tables,    uesTable);
      table    (tables,    entityTable);
      column   (columns,   SSSQLVarU.userEventId);
      column   (columns,   SSSQLVarU.userId);
      column   (columns,   SSSQLVarU.entityId);
      column   (columns,   SSSQLVarU.id);
      column   (columns,   SSSQLVarU.content);
      column   (columns,   SSSQLVarU.creationTime);
      column   (columns,   SSSQLVarU.eventType);
      tableCon (tableCons, uesTable, SSSQLVarU.userEventId, entityTable, SSSQLVarU.id);
      
      if(forUserUri != null){
        where(wheres, SSSQLVarU.userId, forUserUri);
      }
      
      if(entityUri != null){
        where(wheres, SSSQLVarU.entityId, entityUri);
      }
      
      if(eventType != null){
        where(wheres, SSSQLVarU.eventType, eventType);
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      while(resultSet.next()){
        
        try{
          eventTypeFromDB = SSUEE.get(bindingStr(resultSet, SSSQLVarU.eventType));
          timestamp       = bindingStrToLong(resultSet, SSSQLVarU.creationTime);
        }catch(Exception error){
          SSLogU.warn("user event type doesnt exist in curren model or timestamp isn't valid " + bindingStr(resultSet, SSSQLVarU.eventType));
          continue;
        }
        
        if(
          startTime != null &&
          timestamp < startTime){
          continue;
        }
        
        if(
          endTime != null &&
          timestamp > endTime){
          continue;
        }
        
        ues.add(
          SSUE.get(
            bindingStrToUri       (resultSet, SSSQLVarU.userEventId),
            bindingStrToUri       (resultSet, SSSQLVarU.userId),
            eventTypeFromDB,
            bindingStrToUri       (resultSet, SSSQLVarU.entityId),
            bindingStr            (resultSet, SSSQLVarU.content),
            timestamp));
      }
      
      return ues;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void addUE(
    final SSUri    ueUri, 
    final SSUri    userUri, 
    final SSUri    entityUri, 
    final SSUEE eventType, 
    final String   content) throws Exception{

    if(SSObjU.isNull(ueUri, userUri, entityUri, eventType, content)){
      SSServErrReg.regErrThrow(new Exception("pars not okay"));
      return;
    }
    
    final Map<String, String> insert = new HashMap<String, String>();
    
    try{
      insert.put(SSSQLVarU.userEventId,   ueUri.toString());
      insert.put(SSSQLVarU.userId,        userUri.toString());
      insert.put(SSSQLVarU.entityId,      entityUri.toString());
      insert.put(SSSQLVarU.eventType,     eventType.toString());
      insert.put(SSSQLVarU.content,       content);

      dbSQL.insert(uesTable, insert);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
