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
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSNoResultFoundErr;
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
  
  public SSTagSQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }

  public Boolean existsTagLabel(final SSTagLabel tagLabel) throws Exception{
   
    ResultSet resultSet = null;
    
    try{
      
      if(tagLabel == null){
        return false;
      }
      
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.label, tagLabel);
      where(wheres, SSSQLVarU.type,  SSEntityE.tag);
      
      resultSet = dbSQL.select(entityTable, wheres);
      
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri getOrCreateTagURI(
    final Boolean     exsitsTag, 
    final SSTagLabel  tagString) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      if(!exsitsTag){
        return createTagURI();
      }
      
      final Map<String, String>  wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.label, tagString);
      where(wheres, SSSQLVarU.type,  SSEntityE.tag);
      
      resultSet = dbSQL.select(entityTable, wheres);
      
      checkFirstResult(resultSet);
      
      return bindingStrToUri(resultSet, SSSQLVarU.id);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addTagAssIfNotExists(
    final SSUri       tagUri, 
    final SSUri       userUri,
    final SSUri       entityUri,
    final SSSpaceE    space) throws Exception{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert    (inserts,    SSSQLVarU.userId,    userUri);
      insert    (inserts,    SSSQLVarU.entityId,  entityUri);
      insert    (inserts,    SSSQLVarU.tagId,     tagUri);
      insert    (inserts,    SSSQLVarU.tagSpace,  space);
      uniqueKey (uniqueKeys, SSSQLVarU.userId,    userUri);
      uniqueKey (uniqueKeys, SSSQLVarU.entityId,  entityUri);
      uniqueKey (uniqueKeys, SSSQLVarU.tagId,     tagUri);
      uniqueKey (uniqueKeys, SSSQLVarU.tagSpace,  space);
      
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
    
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarU.userId,       userUri);
      where(wheres, SSSQLVarU.entityId,     entityUri);
      where(wheres, SSSQLVarU.tagId,        tagUri);
      where(wheres, SSSQLVarU.tagSpace,     space);
      
      resultSet = dbSQL.select(tagAssTable, wheres);
      
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
    final SSTagLabel  tagString, 
    final SSSpaceE    space) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      
      final Map<String, String> wheres  = new HashMap<>();
      final Map<String, String> deletes = new HashMap<>();
      SSUri                     tagURI  = null;
      
      if(tagString != null){
        
        try{
          where(wheres, SSSQLVarU.label, tagString);
          where(wheres, SSSQLVarU.type,  SSEntityE.tag);
          
          resultSet = dbSQL.select(entityTable, wheres);
          
          checkFirstResult(resultSet);
          
          tagURI = bindingStrToUri(resultSet, SSSQLVarU.id);
        }catch(SSNoResultFoundErr error){
          return;
        }catch(Exception error){
          SSServErrReg.regErrThrow(error);
          return;
        }finally{
          dbSQL.closeStmt(resultSet);
        }
      }
      
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
    final SSTagLabel  tagString, 
    final SSSpaceE    tagSpace) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      final Map<String, String> wheres         = new HashMap<>();
      final List<SSTag>         tagAsss        = new ArrayList<SSTag>();
      final List<String>        tables         = new ArrayList<>();
      final List<String>        columns        = new ArrayList<>();
      final List<String>        tableCons      = new ArrayList<>();
      final SSUri               tagURI         = getOrCreateTagURI(existsTagLabel(tagString), tagString);

      table    (tables,    tagAssTable);
      table    (tables,    entityTable);
      column   (columns,   SSSQLVarU.tagId);
      column   (columns,   SSSQLVarU.entityId);
      column   (columns,   SSSQLVarU.userId);
      column   (columns,   SSSQLVarU.tagSpace);
      column   (columns,   SSSQLVarU.label);
      tableCon (tableCons, tagAssTable, SSSQLVarU.tagId, entityTable, SSSQLVarU.id);
      
      if(userUri != null){
        where(wheres, SSSQLVarU.userId, userUri);
      }
      
      if(entity != null){
        where(wheres, SSSQLVarU.entityId, entity);
      }
      
      if(tagString != null){
        where(wheres, SSSQLVarU.tagId, tagURI);
      }
      
      if(tagSpace != null){
        where(wheres, SSSQLVarU.tagSpace, tagSpace);
      }
      
      resultSet = dbSQL.select(tables, columns, wheres, tableCons);
      
      while(resultSet.next()){
        
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
  
  public List<SSUri> getEntitiesForTagLabel(
    final SSUri       userUri, 
    final SSTagLabel  tagString, 
    final SSSpaceE    tagSpace) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(!existsTagLabel(tagString)){
        return new ArrayList<SSUri>();
      }
      
      final Map<String, String> wheres       = new HashMap<>();
      final List<String>        columns      = new ArrayList<>();
      
      where(wheres, SSSQLVarU.tagId, getOrCreateTagURI(true, tagString));
      
      if(tagSpace != null){
        where(wheres, SSSQLVarU.tagSpace, tagSpace);
      }
      
      if(userUri != null){
        where(wheres, SSSQLVarU.userId, userUri);
      }
      
      column(columns, SSSQLVarU.entityId);
      column(columns, SSSQLVarU.tagId);
      
      resultSet = dbSQL.select(tagAssTable, columns, wheres);
      
      return getURIsFromResult(resultSet, SSSQLVarU.entityId);
      
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
