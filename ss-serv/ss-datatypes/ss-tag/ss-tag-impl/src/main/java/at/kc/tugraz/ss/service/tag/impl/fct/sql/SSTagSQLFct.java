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

import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSTagSQLFct extends SSDBSQLFct{
  
  protected final String               tagAssTable                         = "tagass";

  public SSTagSQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }

  public Boolean existsTagLabel(final SSTagLabel tagLabel) throws Exception{
   
    if(tagLabel == null){
      return false;
    }
    
    final Map<String, String>     whereParNamesWithValues = new HashMap<String, String>();
    ResultSet                     resultSet               = null;
    
    try{
      
      whereParNamesWithValues.put(SSSQLVarU.label, tagLabel.toString());
      whereParNamesWithValues.put(SSSQLVarU.type,  SSEntityE.tag.toString());
        
      resultSet = dbSQL.selectAllWhere(entityTable, whereParNamesWithValues);
     
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri getOrCreateTagUri(
    final Boolean     exsitsTag, 
    final SSTagLabel tagString) throws Exception{
    
    if(!exsitsTag){
      return createTagURI();
    }
    
    if(SSObjU.isNull(tagString)){
      SSServErrReg.regErrThrow(new Exception("tagstring null"));
      return null;
    }
        
    final Map<String, String>  whereParNamesWithValues = new HashMap<String, String>();
    ResultSet                  resultSet          = null;
    
    try{
      
      whereParNamesWithValues.put(SSSQLVarU.label, tagString.toString());
      whereParNamesWithValues.put(SSSQLVarU.type,  SSEntityE.tag.toString());
      
      resultSet = dbSQL.selectAllWhere(entityTable, whereParNamesWithValues);
        
      resultSet.first();
      
      return bindingStrToUri(resultSet, SSSQLVarU.id);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addTagAss(
    final SSUri       tagUri, 
    final SSUri       userUri,
    final SSUri       entityUri,
    final SSSpaceE space) throws Exception{
    
    if(SSObjU.isNull(tagUri, userUri, entityUri, space)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return;
    }
    
    final Map<String, String> insertPars = new HashMap<String, String>();
    
    try{
      insertPars.put(SSSQLVarU.userId,       userUri.toString());
      insertPars.put(SSSQLVarU.entityId,     entityUri.toString());
      insertPars.put(SSSQLVarU.tagId,        tagUri.toString());
      insertPars.put(SSSQLVarU.tagSpace,     space.toString());
      
      dbSQL.insert(tagAssTable, insertPars);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Boolean existsTagAss(
    final SSUri       userUri, 
    final SSUri       entityUri, 
    final SSUri       tagUri, 
    final SSSpaceE space) throws Exception{
    
    if(SSObjU.isNull(userUri, entityUri, tagUri, space)){
      return false;
    }
    
    final HashMap<String, String> selectPars = new HashMap<String, String>();
    ResultSet                     resultSet = null;
    
    try{
      selectPars.put(SSSQLVarU.userId,       userUri.toString());
      selectPars.put(SSSQLVarU.entityId,     entityUri.toString());
      selectPars.put(SSSQLVarU.tagId,        tagUri.toString());
      selectPars.put(SSSQLVarU.tagSpace,     space.toString());
      
      resultSet = dbSQL.selectAllWhere(tagAssTable, selectPars);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void removeTagAsss(
    final SSUri       user,
    final SSUri       entity, 
    final SSTagLabel  tagString, 
    final SSSpaceE tagSpace) throws Exception{
    
    final Map<String, String> selectPars = new HashMap<String, String>();
    final Map<String, String> deletePars = new HashMap<String, String>();
    ResultSet                 resultSet  = null;
    SSUri                     tagURI     = null;

    if(tagString != null){
      
      selectPars.put(SSSQLVarU.label,    tagString.toString());
      selectPars.put(SSSQLVarU.type,     SSEntityE.toStr(SSEntityE.tag));

      resultSet = dbSQL.selectAllWhere(entityTable, selectPars);
      
      try{
        
        if(!resultSet.first()){
          return;
        }
        
        tagURI = bindingStrToUri(resultSet, SSSQLVarU.id);
        
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
      }finally{
        dbSQL.closeStmt(resultSet);
      }
    }
    
    try{
      
      if(tagSpace != null){
        deletePars.put(SSSQLVarU.tagSpace,  tagSpace.toString());
      }

      if(user != null){
        deletePars.put(SSSQLVarU.userId,     user.toString());
      }
      
      if(entity != null){
        deletePars.put(SSSQLVarU.entityId,    entity.toString());
      }
      
      if(tagURI != null){
        deletePars.put(SSSQLVarU.tagId,      tagURI.toString());
      }
      
      if(deletePars.size() > 0){
        dbSQL.deleteWhere(tagAssTable, deletePars);
      }else{
        dbSQL.deleteAll(tagAssTable);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSTag> getTagAsss(
    final SSUri       user, 
    final SSUri       entity, 
    final SSTagLabel  tagString, 
    final SSSpaceE tagSpace) throws Exception{
    
    final Map<String, String> whereParsAndValues = new HashMap<String, String>();
    final List<SSTag>         tagAsss            = new ArrayList<SSTag>();
    final List<String>        tableNames         = new ArrayList<String>();
    final List<String>        columnNames        = new ArrayList<String>();
    ResultSet                 resultSet          = null;
    SSUri                     tagURI             = null;
    
    try{
      
      if(tagString != null){
        
        if(!existsTagLabel(tagString)){
          return tagAsss;
        }
        
        tagURI = getOrCreateTagUri(true, tagString);
      }
      
      tableNames.add (tagAssTable);
      tableNames.add (entityTable);
      columnNames.add(SSSQLVarU.tagId);
      columnNames.add(SSSQLVarU.entityId);
      columnNames.add(SSSQLVarU.userId);
      columnNames.add(SSSQLVarU.tagSpace);
      columnNames.add(SSSQLVarU.label);
      
      if(user != null){
        whereParsAndValues.put(SSSQLVarU.userId, user.toString());
      }
      
      if(entity != null){
        whereParsAndValues.put(SSSQLVarU.entityId, entity.toString());
      }
      
      if(tagURI != null){
        whereParsAndValues.put(SSSQLVarU.tagId, tagURI.toString());
      }
      
      if(tagSpace != null){
        whereParsAndValues.put(SSSQLVarU.tagSpace, tagSpace.toString());
      }
      
      resultSet =
        dbSQL.selectCertainWhere(
        tableNames,
        columnNames,
        whereParsAndValues,
        SSSQLVarU.tagId + SSStrU.equal + SSSQLVarU.id);
      
      while(resultSet.next()){
        
        tagAsss.add(
          SSTag.get(
          bindingStrToUri  (resultSet, SSSQLVarU.tagId),
          bindingStrToUri  (resultSet, SSSQLVarU.entityId),
          bindingStrToUri  (resultSet, SSSQLVarU.userId),
          bindingStrToSpace(resultSet, SSSQLVarU.tagSpace),
          SSTagLabel.get   (bindingStr(resultSet, SSSQLVarU.label))));
      }      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return tagAsss;
  }
  
  public List<SSUri> getEntitiesForTagLabel(
    final SSUri       user, 
    final SSTagLabel tagString, 
    final SSSpaceE tagSpace) throws Exception{
    
    if(!existsTagLabel(tagString)){
      return new ArrayList<SSUri>();
    }

    final List<SSUri>         entityUris              = new ArrayList<SSUri>();
    final List<String>        columNames              = new ArrayList<String>();
    final SSUri               tagURI                  = getOrCreateTagUri(true, tagString);
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    ResultSet                 resultSet               = null;
    
    whereParNamesWithValues.put(SSSQLVarU.tagId,    SSUri.toStr            (tagURI));
    
    if(tagSpace != null){
      whereParNamesWithValues.put(SSSQLVarU.tagSpace, SSSpaceE.toStr (tagSpace));
    }
    
    if(user != null){
      whereParNamesWithValues.put(SSSQLVarU.userId, user.toString()); 
    }
    
    columNames.add(SSSQLVarU.entityId);
    columNames.add(SSSQLVarU.tagId);
    
    try{
      resultSet = dbSQL.selectCertainDistinctWhere(tagAssTable, columNames, whereParNamesWithValues);
      
      while(resultSet.next()){
        entityUris.add(bindingStrToUri(resultSet, SSSQLVarU.entityId));
      }

      return entityUris;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  private SSUri createTagURI() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objTag().toString()));
  }
   
  private SSUri objTag() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityE.tag.toString());
  }
}
