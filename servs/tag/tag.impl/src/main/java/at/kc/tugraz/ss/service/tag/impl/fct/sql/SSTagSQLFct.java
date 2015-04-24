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

import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.tugraz.sss.serv.SSServErrReg;

import at.tugraz.sss.serv.SSServImplWithDBA;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

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
      
      insert    (inserts,    SSSQLVarNames.userId,       userUri);
      insert    (inserts,    SSSQLVarNames.entityId,     entityUri);
      insert    (inserts,    SSSQLVarNames.tagId,        tagUri);
      insert    (inserts,    SSSQLVarNames.tagSpace,     space);
      
      if(
        creationTime == null ||
        creationTime == 0){
        insert    (inserts,    SSSQLVarNames.creationTime, SSDateU.dateAsLong());
      }else{
        insert    (inserts,    SSSQLVarNames.creationTime, creationTime);
      }
      
      uniqueKey (uniqueKeys, SSSQLVarNames.userId,       userUri);
      uniqueKey (uniqueKeys, SSSQLVarNames.entityId,     entityUri);
      uniqueKey (uniqueKeys, SSSQLVarNames.tagId,        tagUri);
      uniqueKey (uniqueKeys, SSSQLVarNames.tagSpace,     space);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.tagAssTable, inserts, uniqueKeys);
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
      
      column(columns, SSSQLVarNames.tagId);
      column(columns, SSSQLVarNames.userId);
      column(columns, SSSQLVarNames.entityId);
      column(columns, SSSQLVarNames.tagSpace);
      
      where(wheres, SSSQLVarNames.userId,       userUri);
      where(wheres, SSSQLVarNames.entityId,     entityUri);
      where(wheres, SSSQLVarNames.tagId,        tagUri);
      where(wheres, SSSQLVarNames.tagSpace,     space);
      
      resultSet = dbSQL.select(SSSQLVarNames.tagAssTable, columns, wheres, null, null, null);
      
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
    final SSUri       tagURI, 
    final SSSpaceE    space) throws Exception{
    
    try{
      
      final Map<String, String> deletes = new HashMap<>();
      
      if(space != null){
        delete(deletes, SSSQLVarNames.tagSpace, space);
      }
      
      if(userUri != null){
        delete(deletes, SSSQLVarNames.userId,     userUri);
      }
      
      if(entityUri != null){
        delete(deletes, SSSQLVarNames.entityId,    entityUri);
      }
      
      if(tagURI != null){
        delete(deletes, SSSQLVarNames.tagId,      tagURI);
      }
      
      if(deletes.size() > 0){
        dbSQL.delete(SSSQLVarNames.tagAssTable, deletes);
      }else{
        dbSQL.delete(SSSQLVarNames.tagAssTable);
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

      table    (tables, SSSQLVarNames.tagAssTable);
      table    (tables, SSSQLVarNames.entityTable);
      column   (columns,   SSSQLVarNames.tagId);
      column   (columns,   SSSQLVarNames.entityId);
      column   (columns,   SSSQLVarNames.userId);
      column   (columns,   SSSQLVarNames.tagSpace);
      column   (columns,   SSSQLVarNames.label);
      column   (columns, SSSQLVarNames.tagAssTable, SSSQLVarNames.creationTime);
      tableCon (tableCons, SSSQLVarNames.tagAssTable, SSSQLVarNames.tagId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      if(userUri != null){
        where(wheres, SSSQLVarNames.userId, userUri);
      }
      
      if(entity != null){
        where(wheres, SSSQLVarNames.entityId, entity);
      }
      
      if(tagURI != null){
        where(wheres, SSSQLVarNames.tagId, tagURI);
      }
      
      if(tagSpace != null){
        where(wheres, SSSQLVarNames.tagSpace, tagSpace);
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        //TODO dtheiler: use db for date restriction here
        if(
          startTime != null &&
          startTime != 0    &&
          bindingStrToLong(resultSet, SSSQLVarNames.creationTime) < startTime){
          continue;
        }
        
        tagAsss.add(SSTag.get(bindingStrToUri  (resultSet, SSSQLVarNames.tagId),
            bindingStrToUri  (resultSet, SSSQLVarNames.entityId),
            bindingStrToUri  (resultSet, SSSQLVarNames.userId),
            bindingStrToSpace(resultSet, SSSQLVarNames.tagSpace),
            SSTagLabel.get   (bindingStr(resultSet, SSSQLVarNames.label))));
      }
      
      return tagAsss;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSTag> getTagAsss(
    final List<SSUri> users, 
    final List<SSUri> entities, 
    final SSSpaceE    tagSpace,
    final Long        startTime,
    final List<SSUri> tagURIs) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final List<MultivaluedMap<String, String>> wheres         = new ArrayList<>();
      final List<SSTag>                          tagAsss        = new ArrayList<>();
      final List<String>                         tables         = new ArrayList<>();
      final List<String>                         columns        = new ArrayList<>();
      final List<String>                         tableCons      = new ArrayList<>();
//      final SSUri               tagURI         = getOrCreateTagURI(existsTagLabel(tagString), tagString);

      table    (tables, SSSQLVarNames.tagAssTable);
      table    (tables, SSSQLVarNames.entityTable);
      column   (columns,   SSSQLVarNames.tagId);
      column   (columns,   SSSQLVarNames.entityId);
      column   (columns,   SSSQLVarNames.userId);
      column   (columns,   SSSQLVarNames.tagSpace);
      column   (columns,   SSSQLVarNames.label);
      column   (columns, SSSQLVarNames.tagAssTable, SSSQLVarNames.creationTime);
      tableCon (tableCons, SSSQLVarNames.tagAssTable, SSSQLVarNames.tagId, SSSQLVarNames.entityTable, SSSQLVarNames.id);
      
      if(
        users != null &&
        !users.isEmpty()){
        
        final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
        
        for(SSUri user : users){
          where(whereUsers, SSSQLVarNames.tagAssTable, SSSQLVarNames.userId, user);
        }
        
        wheres.add(whereUsers);
      }
      
      if(
        entities != null &&
        !entities.isEmpty()){

        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, SSSQLVarNames.tagAssTable, SSSQLVarNames.entityId, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      if(
        tagURIs != null &&
        !tagURIs.isEmpty()){
        
        final MultivaluedMap<String, String> whereTags = new MultivaluedHashMap<>();
        
        for(SSUri tagURI : tagURIs){
          where(whereTags, SSSQLVarNames.tagAssTable, SSSQLVarNames.tagId, tagURI);
        }
        
        wheres.add(whereTags);
      }
      
      if(tagSpace != null){
        
        final MultivaluedMap<String, String> whereTags = new MultivaluedHashMap<>();
        
        where(whereTags, SSSQLVarNames.tagAssTable, SSSQLVarNames.tagSpace, tagSpace);
        
        wheres.add(whereTags);
      }
      
      if(wheres.isEmpty()){
        throw new Exception("wheres empty");
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        //TODO dtheiler: use db for date restriction here
        if(
          startTime != null &&
          startTime != 0    &&
          bindingStrToLong(resultSet, SSSQLVarNames.creationTime) < startTime){
          continue;
        }
        
        tagAsss.add(SSTag.get(bindingStrToUri  (resultSet, SSSQLVarNames.tagId),
            bindingStrToUri  (resultSet, SSSQLVarNames.entityId),
            bindingStrToUri  (resultSet, SSSQLVarNames.userId),
            bindingStrToSpace(resultSet, SSSQLVarNames.tagSpace),
            SSTagLabel.get   (bindingStr(resultSet, SSSQLVarNames.label))));
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
      
      column(columns, SSSQLVarNames.entityId);
      column(columns, SSSQLVarNames.tagId);
      
      if(tagURI != null){
        where(wheres, SSSQLVarNames.tagId, tagURI);
      }
      
      if(tagSpace != null){
        where(wheres, SSSQLVarNames.tagSpace, tagSpace);
      }
      
      if(userUri != null){
        where(wheres, SSSQLVarNames.userId, userUri);
      }
      
      resultSet = dbSQL.select(SSSQLVarNames.tagAssTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.entityId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
