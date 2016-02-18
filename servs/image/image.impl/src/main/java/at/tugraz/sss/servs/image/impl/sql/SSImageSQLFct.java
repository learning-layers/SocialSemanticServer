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

import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.datatype.SSImage;
import at.tugraz.sss.serv.datatype.enums.SSImageE;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.util.SSSQLVarNames;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.db.api.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.servs.image.datatype.par.*;

public class SSImageSQLFct extends SSCoreSQL{
  
  public SSImageSQLFct(
    final SSDBSQLI dbSQL){
    
    super(dbSQL);
  }
  
  public void addImage(
    final SSServPar servPar,
    final SSUri    image,
    final SSImageE imageType,
    final SSUri    link) throws SSErr{
    
    try{

      if(SSObjU.isNull(image, imageType)){
        throw SSErr.get(SSErrE.parameterMissing);
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
      
      dbSQL.insertIfNotExists(servPar, SSImageSQLTableE.image, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public SSImage getImage(
    final SSServPar servPar,
    final SSUri    image) throws SSErr{
    
    if(image == null){
      throw SSErr.get(SSErrE.parameterMissing);
    }
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns    = new ArrayList<>();
      final Map<String, String> wheres     = new HashMap<>();
      
      column (columns, SSImageSQLTableE.image, SSSQLVarNames.imageId);
      column (columns, SSImageSQLTableE.image, SSSQLVarNames.type);
      column (columns, SSImageSQLTableE.image, SSSQLVarNames.link);
      
      where   (wheres, SSImageSQLTableE.image, SSSQLVarNames.imageId, image);
      
      resultSet = dbSQL.select(servPar, SSImageSQLTableE.image, columns, wheres, null, null, null);
      
      if(!existsFirstResult(resultSet)){
        return null;
      }
      
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
    final SSServPar servPar,
    final SSUri    forEntity,
    final SSImageE imageType) throws SSErr{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>        columns    = new ArrayList<>();
      final List<SSSQLTableI>   tables     = new ArrayList<>();
      final Map<String, String> wheres     = new HashMap<>();
      final List<String>        tableCons  = new ArrayList<>();
      
      column (columns, SSImageSQLTableE.image, SSSQLVarNames.imageId);
      table  (tables,  SSImageSQLTableE.image);
      
      if(forEntity != null){
        where    (wheres,    SSEntitySQLTableE.entityimages, SSSQLVarNames.entityId, forEntity);
        table    (tables,    SSEntitySQLTableE.entityimages);
        tableCon (tableCons, SSImageSQLTableE.image, SSSQLVarNames.imageId, SSEntitySQLTableE.entityimages, SSSQLVarNames.imageId);
      }
      
      if(imageType != null){
        where(wheres, SSImageSQLTableE.image, SSSQLVarNames.type, imageType);
      }
      
      if(!tableCons.isEmpty()){
        resultSet = dbSQL.select(servPar, tables,     columns, wheres, tableCons, null, null, null);
      }else{
        resultSet = dbSQL.select(servPar, SSImageSQLTableE.image, columns, wheres, null, null, null);
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
    final SSServPar servPar,
    final SSUri image,
    final SSUri entity) throws SSErr{
    
    try{
      
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.entityId,       entity);
      insert(inserts, SSSQLVarNames.imageId,        image);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.entityId, entity);
      uniqueKey(uniqueKeys, SSSQLVarNames.imageId,  image);
      
      dbSQL.insertIfNotExists(servPar, SSEntitySQLTableE.entityimages, inserts, uniqueKeys);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeProfilePictures(
    final SSServPar servPar,
    final SSUri entity) throws SSErr{
    
    try{
      final Map<String, String> wheres = new HashMap<>();
      
      where(wheres, SSSQLVarNames.entityId, entity);
      
      dbSQL.deleteIgnore(servPar, SSEntitySQLTableE.entityprofilepictures, wheres);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeImagesFromEntity(
    final SSServPar servPar,
    final List<SSUri> images) throws SSErr{
    
    try{
      
      final Map<String, String> wheres = new HashMap<>();
      
      for(SSUri image : images){
      
        wheres.clear();
       
        where(wheres, SSSQLVarNames.imageId, image);
        
        dbSQL.deleteIgnore(servPar, SSEntitySQLTableE.entityimages, wheres);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addProfilePicture(
    final SSServPar servPar,
    final SSUri entity,
    final SSUri image) throws SSErr{
    
    try{
      final Map<String, String> inserts    = new HashMap<>();
      final Map<String, String> uniqueKeys = new HashMap<>();
      
      insert(inserts, SSSQLVarNames.entityId,       entity);
      insert(inserts, SSSQLVarNames.imageId,        image);
      
      uniqueKey(uniqueKeys, SSSQLVarNames.entityId, entity);
      uniqueKey(uniqueKeys, SSSQLVarNames.imageId,  image);
      
      dbSQL.insertIfNotExists(servPar, SSEntitySQLTableE.entityprofilepictures, inserts, uniqueKeys);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public List<SSUri> getProfilePictures(
    final SSServPar servPar,
    final SSUri entity) throws SSErr{
    
    ResultSet resultSet  = null;
    
    try{
      final List<String>        columns          = new ArrayList<>();
      final Map<String, String> wheres           = new HashMap<>();
      
      column(columns, SSSQLVarNames.imageId);
      
      where(wheres, SSEntitySQLTableE.entityprofilepictures, SSSQLVarNames.entityId, entity);
      
      resultSet = dbSQL.select(servPar, SSEntitySQLTableE.entityprofilepictures, columns, wheres, null, null, null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.imageId);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

//  public List<SSUri> getImageFiles(
//    final SSUri image) throws SSErr{
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