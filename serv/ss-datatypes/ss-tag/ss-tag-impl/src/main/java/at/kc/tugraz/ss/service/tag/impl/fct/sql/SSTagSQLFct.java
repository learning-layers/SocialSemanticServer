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
package at.kc.tugraz.ss.service.tag.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSTagSQLFct extends SSDBSQLFct{
  
  public SSTagSQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }

  public void addTagAssIfNotExists(
    final SSUri       tagUri, 
    final SSUri       userUri,
    final SSUri       entityUri,
    final SSSpaceE    space,
    final Long        creationTime) throws Exception{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert    (inserts,    SSSQLVarU.userId,       userUri);
      insert    (inserts,    SSSQLVarU.entityId,     entityUri);
      insert    (inserts,    SSSQLVarU.tagId,        tagUri);
      insert    (inserts,    SSSQLVarU.tagSpace,     space);
      
      if(
        creationTime == null ||
        creationTime == 0){
        insert    (inserts,    SSSQLVarU.creationTime, SSDateU.dateAsLong());
      }else{
        insert    (inserts,    SSSQLVarU.creationTime, creationTime);
      }
      
      uniqueKey (uniqueKeys, SSSQLVarU.userId,       userUri);
      uniqueKey (uniqueKeys, SSSQLVarU.entityId,     entityUri);
      uniqueKey (uniqueKeys, SSSQLVarU.tagId,        tagUri);
      uniqueKey (uniqueKeys, SSSQLVarU.tagSpace,     space);
      
      dbSQL.insertIfNotExists(tagAssTable, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Boolean existsTagAss(
    final SSUri       userUri, 
    final SSUri       entityUri, 
    final SSUri       tagUri, 
    final SSSpaceE    space) throws Exception{

    ResultSet resultSet = null;
    
    try{
    
      final List<String>        columns  = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarU.tagId);
      column(columns, SSSQLVarU.userId);
      column(columns, SSSQLVarU.entityId);
      column(columns, SSSQLVarU.tagSpace);
      
      where(wheres, SSSQLVarU.userId,       userUri);
      where(wheres, SSSQLVarU.entityId,     entityUri);
      where(wheres, SSSQLVarU.tagId,        tagUri);
      where(wheres, SSSQLVarU.tagSpace,     space);
      
      resultSet = dbSQL.select(tagAssTable, columns, wheres, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void removeTagAsss(
    final SSUri       userUri,
    final SSUri       entityUri, 
    final SSTagLabel  tagURI, 
    final SSSpaceE    space) throws Exception{
    
    try{
      
      final Map<String, String> deletes = new HashMap<>();
      
      if(space != null){
        delete(deletes, SSSQLVarU.tagSpace, space);
      }
      
      if(userUri != null){
        delete(deletes, SSSQLVarU.userId,     userUri);
      }
      
      if(entityUri != null){
        delete(deletes, SSSQLVarU.entityId,    entityUri);
      }
      
      if(tagURI != null){
        delete(deletes, SSSQLVarU.tagId,      tagURI);
      }
      
      if(deletes.size() > 0){
        dbSQL.delete(tagAssTable, deletes);
      }else{
        dbSQL.delete(tagAssTable);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSTag> getTagAsss(
    final SSUri       userUri, 
    final SSUri       entity, 
    final SSSpaceE    tagSpace,
    final Long        startTime,
    final SSUri       tagURI) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres         = new HashMap<>();
      final List<SSTag>         tagAsss        = new ArrayList<>();
      final List<String>        tables         = new ArrayList<>();
      final List<String>        columns        = new ArrayList<>();
      final List<String>        tableCons      = new ArrayList<>();
//      final SSUri               tagURI         = getOrCreateTagURI(existsTagLabel(tagString), tagString);

      table    (tables,    tagAssTable);
      table    (tables,    entityTable);
      column   (columns,   SSSQLVarU.tagId);
      column   (columns,   SSSQLVarU.entityId);
      column   (columns,   SSSQLVarU.userId);
      column   (columns,   SSSQLVarU.tagSpace);
      column   (columns,   SSSQLVarU.label);
      column   (columns,   tagAssTable, SSSQLVarU.creationTime);
      tableCon (tableCons, tagAssTable, SSSQLVarU.tagId, entityTable, SSSQLVarU.id);
      
      if(userUri != null){
        where(wheres, SSSQLVarU.userId, userUri);
      }
      
      if(entity != null){
        where(wheres, SSSQLVarU.entityId, entity);
      }
      
      if(tagURI != null){
        where(wheres, SSSQLVarU.tagId, tagURI);
      }
      
      if(tagSpace != null){
        where(wheres, SSSQLVarU.tagSpace, tagSpace);
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null);
      
      while(resultSet.next()){
        
        //TODO dtheiler: use db for date restriction here
        if(
          startTime != null &&
          startTime != 0    &&
          bindingStrToLong(resultSet, SSSQLVarU.creationTime) < startTime){
          continue;
        }
        
        tagAsss.add(
          SSTag.get(
            bindingStrToUri  (resultSet, SSSQLVarU.tagId),
            bindingStrToUri  (resultSet, SSSQLVarU.entityId),
            bindingStrToUri  (resultSet, SSSQLVarU.userId),
            bindingStrToSpace(resultSet, SSSQLVarU.tagSpace),
            SSTagLabel.get   (bindingStr(resultSet, SSSQLVarU.label))));
      }
      
      return tagAsss;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getEntities(
    final SSUri       userUri, 
    final SSSpaceE    tagSpace,
    final SSUri       tagURI) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres       = new HashMap<>();
      final List<String>        columns      = new ArrayList<>();
      
      column(columns, SSSQLVarU.entityId);
      column(columns, SSSQLVarU.tagId);
      
      if(tagURI != null){
        where(wheres, SSSQLVarU.tagId, tagURI);
      }
      
      if(tagSpace != null){
        where(wheres, SSSQLVarU.tagSpace, tagSpace);
      }
      
      if(userUri != null){
        where(wheres, SSSQLVarU.userId, userUri);
      }
      
      resultSet = dbSQL.select(tagAssTable, columns, wheres, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarU.entityId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
