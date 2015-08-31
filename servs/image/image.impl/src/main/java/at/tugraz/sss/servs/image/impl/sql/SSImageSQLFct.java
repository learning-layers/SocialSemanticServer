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
package at.tugraz.sss.servs.image.impl.sql;

import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSImage;
import at.tugraz.sss.serv.SSImageE;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSImageSQLFct extends SSDBSQLFct{
  
  public SSImageSQLFct(SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }
  
  public void addImage(
    final SSUri    image,
    final SSImageE imageType,
    final SSUri    link) throws Exception{
    
    try{

      if(SSObjU.isNull(image, imageType)){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.imageId,    image);
      insert(inserts, SSSQLVarNames.type,       imageType);
      
      if(link != null){
        insert(inserts, SSSQLVarNames.link,       link);
      }else{
        insert(inserts, SSSQLVarNames.link,       SSStrU.empty);
      }
      
      uniqueKey(uniqueKeys, SSSQLVarNames.imageId, image);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.imageTable, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public SSImage getImage(
    final SSUri    image) throws Exception{
    
    if(image == null){
      throw new SSErr(SSErrE.parameterMissing);
    }
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns    = new ArrayList<>();
      final Map<String, String> wheres     = new HashMap<>();
      
      column (columns, SSSQLVarNames.imageTable, SSSQLVarNames.imageId);
      column (columns, SSSQLVarNames.imageTable, SSSQLVarNames.type);
      column (columns, SSSQLVarNames.imageTable, SSSQLVarNames.link);
      
      where   (wheres, SSSQLVarNames.imageTable, SSSQLVarNames.imageId, image);
      
      resultSet = dbSQL.select(SSSQLVarNames.imageTable, columns, wheres, null, null, null);
      
      checkFirstResult(resultSet);
      
      return SSImage.get(
        bindingStrToUri(resultSet, SSSQLVarNames.imageId),
        SSImageE.get(bindingStr(resultSet, SSSQLVarNames.type)),
        bindingStrToUri(resultSet, SSSQLVarNames.link));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getImages(
    final SSUri    forEntity,
    final SSImageE imageType) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns    = new ArrayList<>();
      final List<String>        tables     = new ArrayList<>();
      final Map<String, String> wheres     = new HashMap<>();
      final List<String>        tableCons  = new ArrayList<>();
      
      column (columns, SSSQLVarNames.imageTable, SSSQLVarNames.imageId);
      table  (tables, SSSQLVarNames.imageTable);
      
      if(forEntity != null){
        where   (wheres, SSSQLVarNames.entityImagesTable, SSSQLVarNames.entityId, forEntity);
        table   (tables, SSSQLVarNames.entityImagesTable);
        tableCon(tableCons, SSSQLVarNames.imageTable, SSSQLVarNames.imageId, SSSQLVarNames.entityImagesTable, SSSQLVarNames.imageId);
      }
      
      if(imageType != null){
        where(wheres, SSSQLVarNames.imageTable, SSSQLVarNames.type, imageType);
      }
      
      if(!tableCons.isEmpty()){
        resultSet = dbSQL.select(tables,     columns, wheres, tableCons, null, null, null);
      }else{
        resultSet = dbSQL.select(SSSQLVarNames.imageTable, columns, wheres, null, null, null);
      }
      
      return getURIsFromResult(resultSet, SSSQLVarNames.imageId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void addImageToEntity(
    final SSUri image,
    final SSUri entity) throws Exception{
    
    try{
      
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.entityId,       entity);
      insert(inserts, SSSQLVarNames.imageId,        image);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.entityId, entity);
      uniqueKey(uniqueKeys, SSSQLVarNames.imageId,  image);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.entityImagesTable, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeProfilePictures(final SSUri entity) throws Exception{
    
    try{
      final Map<String, String> deletes = new HashMap<>();
      
      delete(deletes, SSSQLVarNames.entityId, entity);
      
      dbSQL.deleteIgnore(SSSQLVarNames.entityProfilePicturesTable, deletes);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeImagesFromEntity(final List<SSUri> images) throws Exception{
    
    try{
      
      final Map<String, String> deletes = new HashMap<>();
      
      for(SSUri image : images){
      
        deletes.clear();
       
        delete(deletes, SSSQLVarNames.imageId, image);
        
        dbSQL.deleteIgnore(SSSQLVarNames.entityImagesTable, deletes);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addProfilePicture(
    final SSUri entity,
    final SSUri image) throws Exception{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.entityId,       entity);
      insert(inserts, SSSQLVarNames.imageId,        image);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.entityId, entity);
      uniqueKey(uniqueKeys, SSSQLVarNames.imageId,  image);
      
      dbSQL.insertIfNotExists(SSSQLVarNames.entityProfilePicturesTable, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getProfilePictures(
    final SSUri entity) throws Exception{
    
    ResultSet resultSet  = null;
    
    try{
      final List<String>        columns          = new ArrayList<>();
      final Map<String, String> wheres           = new HashMap<>();
      
      column(columns, SSSQLVarNames.imageId);
      
      where(wheres, SSSQLVarNames.entityProfilePicturesTable, SSSQLVarNames.entityId, entity);
      
      resultSet = dbSQL.select(SSSQLVarNames.entityProfilePicturesTable, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.imageId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

//  public List<SSUri> getImageFiles(
//    final SSUri image) throws Exception{
//    
//    ResultSet resultSet = null;
//    
//    try{
//      
//      final List<String>        columns           = new ArrayList<>();
//      final Map<String, String> wheres            = new HashMap<>();
//      
//      column(columns, SSSQLVarNames.fileId);
//      
//      where(wheres, SSSQLVarNames.entityId, image);
//      
//      resultSet = dbSQL.select(SSSQLVarNames.entityFilesTable, columns, wheres, null, null, null);
//      
//      return getURIsFromResult(resultSet, SSSQLVarNames.fileId);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }
}