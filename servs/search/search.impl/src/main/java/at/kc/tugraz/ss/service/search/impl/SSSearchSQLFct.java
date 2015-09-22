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
package at.kc.tugraz.ss.service.search.impl;

import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSDBSQLSelectPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSSearchOpE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSUri;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class SSSearchSQLFct extends SSDBSQLFct{
  
  public SSSearchSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  public List<SSEntity> getEntitiesForLabelsAndDescriptionsWithSQLLike(
    final List<String> labelStrings,
    final List<String> descStrings,
    final SSSearchOpE  searchOp) throws Exception{
  
    ResultSet resultSet = null;
    
    try{
      final List<SSEntity>                       entities  = new ArrayList<>();
      final List<MultivaluedMap<String, String>> likes     = new ArrayList<>();
      final List<String>                         columns   = new ArrayList<>();
      final List<String>                         tables    = new ArrayList<>();
      final List<String>                         tableCons  = new ArrayList<>();
      SSEntity                                   entityObj;
      
      column (columns, SSSQLVarNames.id);
      column (columns, SSSQLVarNames.label);
      column (columns, SSSQLVarNames.type);
      column (columns, SSSQLVarNames.description);
      
      table(tables, SSSQLVarNames.entityTable);
      
      if(searchOp == null){
        throw new Exception("search for labels search operation null");
      }
      
      MultivaluedMap<String, String> likeContents = new MultivaluedHashMap<>();
      
      switch(searchOp){
        
        case or:{
          
          if(
            labelStrings != null &&
            !labelStrings.isEmpty()){
            
            for(String labelString : labelStrings){
              where(likeContents, SSSQLVarNames.entityTable, SSSQLVarNames.label, labelString);
            }
          }
          
          if(
            descStrings != null &&
            !descStrings.isEmpty()){
            
            for(String descString : descStrings){
              where(likeContents, SSSQLVarNames.entityTable, SSSQLVarNames.description, descString);
            }
          }
          
          likes.add(likeContents);
          break;
        }
        
        case and:{
          
          for(String labelString : labelStrings){
            
            likeContents = new MultivaluedHashMap<>();
            
            where(likeContents, SSSQLVarNames.entityTable, SSSQLVarNames.label, labelString);
            
            likes.add(likeContents);
          }
          
          for(String descString : descStrings){
            
            likeContents = new MultivaluedHashMap<>();
            
            where(likeContents, SSSQLVarNames.entityTable, SSSQLVarNames.description, descString);
            
            likes.add(likeContents);
          }
          
          break;
        }
      }
      
      resultSet = dbSQL.selectLike(tables, columns, likes, tableCons, null, null, null);
      
      while(resultSet.next()){
        
        entityObj =
          SSEntity.get(
            bindingStrToUri        (resultSet, SSSQLVarNames.id),
            bindingStrToEntityType (resultSet, SSSQLVarNames.type),
            bindingStrToLabel      (resultSet, SSSQLVarNames.label));
         
         entityObj.description = bindingStrToTextComment(resultSet, SSSQLVarNames.description);
         
         entities.add(entityObj);
      }      
      
      return entities;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getEntitiesForLabelsWithLike(
    final List<String> labels) throws Exception{
  
    ResultSet resultSet = null;
    
    try{
      
      if(
        labels       == null ||
        labels.isEmpty()){
        
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final List<MultivaluedMap<String, String>> likes        = new ArrayList<>();
      final List<String>                         columns      = new ArrayList<>();
      final List<String>                         tables       = new ArrayList<>();
      final List<String>                         tableCons    = new ArrayList<>();
      final MultivaluedMap<String, String>       likeContents = new MultivaluedHashMap<>();
      
      column (columns, SSSQLVarNames.id);
      
      table(tables, SSSQLVarNames.entityTable);
      
      for(String label : labels){
        where(likeContents, SSSQLVarNames.entityTable, SSSQLVarNames.label, label);
      }
      
      likes.add(likeContents);
      
      resultSet =
        dbSQL.selectLike(
          tables,
          columns,
          likes,
          tableCons,
          null,
          null,
          null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getEntitiesForDescriptionsWithLike(
    final List<String> descs) throws Exception{
  
    ResultSet resultSet = null;
    
    try{
      
      if(
        descs       == null ||
        descs.isEmpty()){
        
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final List<MultivaluedMap<String, String>> likes        = new ArrayList<>();
      final List<String>                         columns      = new ArrayList<>();
      final List<String>                         tables       = new ArrayList<>();
      final List<String>                         tableCons    = new ArrayList<>();
      final MultivaluedMap<String, String>       likeContents = new MultivaluedHashMap<>();
      
      column (columns, SSSQLVarNames.id);
      
      table(tables, SSSQLVarNames.entityTable);
      
      for(String desc : descs){
        where(likeContents, SSSQLVarNames.entityTable, SSSQLVarNames.description, desc);
      }
      
      likes.add(likeContents);
      
      resultSet =
        dbSQL.selectLike(
          tables,
          columns,
          likes,
          tableCons,
          null,
          null,
          null);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getEntitiesForLabelsWithMatch(
    final List<SSUri>  entities,
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>                         columns   = new ArrayList<>();
      final List<String>                         tables    = new ArrayList<>();
      final List<MultivaluedMap<String, String>> wheres    = new ArrayList<>();
      final List<String>                         matches   = new ArrayList<>();
      final List<String>                         tableCons = new ArrayList<>();
      
      column (columns, SSSQLVarNames.id);
      
      table(tables, SSSQLVarNames.entityTable);
      
      if(!entities.isEmpty()){
        
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, SSSQLVarNames.entityTable, SSSQLVarNames.id, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      match (matches, SSSQLVarNames.label);
      
      final SSDBSQLSelectPar selectPar =
        new SSDBSQLSelectPar(
          tables,
          columns,
          wheres,
          null,
          null,
          tableCons);
       
      selectPar.matches.addAll   (matches);
      selectPar.requireds.addAll (requireds);
      selectPar.absents.addAll   (absents);
      selectPar.eithers.addAll   (eithers);
        
      resultSet = dbSQL.select(selectPar);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getEntitiesForDescriptionsWithMatch(
    final List<SSUri>  entities,
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      final List<String>                         columns   = new ArrayList<>();
      final List<String>                         tables    = new ArrayList<>();
      final List<MultivaluedMap<String, String>> wheres    = new ArrayList<>();
      final List<String>                         matches   = new ArrayList<>();
      final List<String>                         tableCons = new ArrayList<>();
      
      column (columns, SSSQLVarNames.id);
      
      table(tables, SSSQLVarNames.entityTable);
      
      if(!entities.isEmpty()){
        
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, SSSQLVarNames.entityTable, SSSQLVarNames.id, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      match  (matches, SSSQLVarNames.description);
      
      final SSDBSQLSelectPar selectPar =
        new SSDBSQLSelectPar(
          tables,
          columns,
          wheres,
          null,
          null,
          tableCons);
      
      selectPar.matches.addAll   (matches);
      selectPar.requireds.addAll (requireds);
      selectPar.absents.addAll   (absents);
      selectPar.eithers.addAll   (eithers);
      
      resultSet = dbSQL.select(selectPar);
      
      return getURIsFromResult(resultSet, SSSQLVarNames.id);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getEntityURIsUserCanAccess(
    final SSUri           user,
    final Boolean         withSystemCircles,
    final List<SSUri>     entities,
    final List<SSEntityE> types,
    final List<SSUri>     authors) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
     
      if(user == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final List<MultivaluedMap<String, String>> wheres         = new ArrayList<>();
      final List<String>                         tables         = new ArrayList<>();
      final List<String>                         columns        = new ArrayList<>();
      final List<String>                         tableCons      = new ArrayList<>();
      
      column(columns, SSSQLVarNames.circleEntitiesTable,   SSSQLVarNames.entityId);
      
      table    (tables, SSSQLVarNames.circleUsersTable);
      table    (tables, SSSQLVarNames.circleEntitiesTable);
      table    (tables, SSSQLVarNames.entityTable);
      
      tableCon (tableCons, SSSQLVarNames.circleUsersTable,    SSSQLVarNames.circleId, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.circleId);
      tableCon (tableCons, SSSQLVarNames.circleEntitiesTable, SSSQLVarNames.entityId, SSSQLVarNames.entityTable,         SSSQLVarNames.id);
      
      final MultivaluedMap<String, String> whereUsers = new MultivaluedHashMap<>();
      
      where(whereUsers, SSSQLVarNames.circleUsersTable, SSSQLVarNames.userId, user);
      
      wheres.add(whereUsers);
      
      if(
        withSystemCircles != null &&
        !withSystemCircles){
        
        table    (tables, SSSQLVarNames.circleTable);
        tableCon (tableCons, SSSQLVarNames.circleTable, SSSQLVarNames.circleId, SSSQLVarNames.circleUsersTable,         SSSQLVarNames.circleId);
        
        final MultivaluedMap<String, String> whereIsSystemCircles = new MultivaluedHashMap<>();
        
        where(whereIsSystemCircles, SSSQLVarNames.circleTable, SSSQLVarNames.isSystemCircle, withSystemCircles);
        
        wheres.add(whereIsSystemCircles);
      }
      
      if(
        entities != null &&
        !entities.isEmpty()){
        
        final MultivaluedMap<String, String> whereEntities = new MultivaluedHashMap<>();
        
        for(SSUri entity : entities){
          where(whereEntities, SSSQLVarNames.entityTable, SSSQLVarNames.id, entity);
        }
        
        wheres.add(whereEntities);
      }
      
      if(
        authors != null &&
        !authors.isEmpty()){
        
        final MultivaluedMap<String, String> whereAuthors = new MultivaluedHashMap<>();
        
        for(SSUri author : authors){
          where(whereAuthors, SSSQLVarNames.entityTable, SSSQLVarNames.author, author);
        }
        
        wheres.add(whereAuthors);
      }
      
      if(
        types != null &&
        !types.isEmpty()){
        
        final MultivaluedMap<String, String> whereTypes = new MultivaluedHashMap<>();
        
        for(SSEntityE type : types){
          where(whereTypes, SSSQLVarNames.entityTable, SSSQLVarNames.type, type);
        }
        
        wheres.add(whereTypes);
      }
      
      resultSet =
        dbSQL.select(
          new SSDBSQLSelectPar(
            tables,
            columns,
            wheres,
            null,
            null,
            tableCons));
      
      return getURIsFromResult(resultSet, SSSQLVarNames.entityId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}

//public List<SSEntity> getEntitiesForLabelsAndDescriptions(
//    final List<String> requireds,
//    final List<String> absents,
//    final List<String> eithers) throws Exception{
//    
//    ResultSet resultSet = null;
//    
//    try{
//      final List<SSEntity>            entities  = new ArrayList<>();
//      final List<String>              columns   = new ArrayList<>();
//      final List<String>              matches   = new ArrayList<>();
//      SSEntity                        entityObj;
//      
//      column (columns, SSSQLVarNames.id);
//      column (columns, SSSQLVarNames.label);
//      column (columns, SSSQLVarNames.description);
//      column (columns, SSSQLVarNames.type);
//      match  (matches, SSSQLVarNames.label);
//      match  (matches, SSSQLVarNames.description);
//      
//      resultSet = dbSQL.select(SSSQLVarNames.entityTable, columns, matches, requireds, absents, eithers);
//      
//      while(resultSet.next()){
//      
//        entityObj =
//          SSEntity.get(
//            bindingStrToUri        (resultSet, SSSQLVarNames.id),
//            bindingStrToEntityType (resultSet, SSSQLVarNames.type),
//            bindingStrToLabel      (resultSet, SSSQLVarNames.label));
//        
//        entityObj.description = bindingStrToTextComment(resultSet, SSSQLVarNames.description);
//        
//        entities.add(entityObj);
//      }
//      
//      return entities;
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }