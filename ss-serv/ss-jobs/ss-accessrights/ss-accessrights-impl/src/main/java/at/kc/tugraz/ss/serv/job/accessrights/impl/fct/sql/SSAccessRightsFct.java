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
package at.kc.tugraz.ss.serv.job.accessrights.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.SSAccessRightsCircleTypeE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSAccessRightsRightTypeE;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.SSCircle;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSAccessRightsFct extends SSDBSQLFct{
  
  protected static final String circleTable           = "circle";
  protected static final String circleRightsTable     = "circleRights";
  protected static final String circleUsersTable      = "circleUsers";
  protected static final String circleEntitiesTable   = "circleEntities";
  
  public SSAccessRightsFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }

  public List<SSUri> getUserCircleURIs(
    final SSUri userUri) throws Exception{
   
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(userUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String> where      = new HashMap<String, String>();
      final List<SSUri>         circleUris = new ArrayList<SSUri>();
      
      where.put(SSSQLVarU.userId, SSUri.toStr(userUri));
      
      resultSet = dbSQL.selectAllWhere(circleUsersTable, where);
      
      while(resultSet.next()){
        circleUris.add(bindingStrToUri(resultSet, SSSQLVarU.circleId));
      }
      
      return circleUris;
      
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public SSCircle getCircle(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(circleUri)){
        throw new Exception("pars not ok");
      }
      
      final List<String>        tableNames            = new ArrayList<String>();
      final List<String>        columnNames           = new ArrayList<String>();
      final Map<String, String> where                 =  new HashMap<String, String>();
      
      tableNames.add  (circleTable);
      tableNames.add  (entityTable);
      columnNames.add (SSSQLVarU.label);
      columnNames.add (SSSQLVarU.circleId);
      columnNames.add (SSSQLVarU.circleType);
      
      where.put(SSSQLVarU.circleId, SSUri.toStr(circleUri));
      
      resultSet = 
        dbSQL.selectCertainWhere(
          tableNames, 
          columnNames, 
          where, 
          SSSQLVarU.circleId + SSStrU.equal + SSSQLVarU.id);
      
      return SSCircle.get(
        bindingStrToUri   (resultSet, SSSQLVarU.circleId),
        bindingStrToLabel (resultSet, SSSQLVarU.label),
        SSAccessRightsCircleTypeE.get(bindingStr(resultSet, SSSQLVarU.circleType)),
        null,
        null,
        null);
      
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public SSUri addCircle(
    final SSUri                     circleUri, 
    final SSAccessRightsCircleTypeE circleType) throws Exception{
    
    try{

      if(SSObjU.isNull(circleUri, circleType)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String> insertPars = new HashMap<String, String>();
      
      insertPars.put(SSSQLVarU.circleId,   SSUri.toStr(circleUri));
      insertPars.put(SSSQLVarU.circleType, SSAccessRightsCircleTypeE.toStr(circleType));
      
      dbSQL.insert(circleTable, insertPars);
      
      return circleUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public Boolean hasCircleRight(
    final SSUri                    circleUri,
    final SSAccessRightsRightTypeE accessRight) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(circleUri, accessRight)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String> where = new HashMap<String, String>();
      
      where.put(SSSQLVarU.circleId,    SSUri.toStr(circleUri));
      where.put(SSSQLVarU.accessRight, SSAccessRightsRightTypeE.toStr(accessRight));
      
      resultSet = dbSQL.selectAllWhere(circleRightsTable, where);
      
      return resultSet.first();
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public void addCircleRight(
    final SSUri                    circleUri, 
    final SSAccessRightsRightTypeE accessRight) throws Exception{
    
    try{

      if(SSObjU.isNull(circleUri, accessRight)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String> insertPars = new HashMap<String, String>();
      
      insertPars.put(SSSQLVarU.circleId,      SSUri.toStr(circleUri));
      insertPars.put(SSSQLVarU.accessRight, SSAccessRightsRightTypeE.toStr(accessRight));
      
      dbSQL.insert(circleRightsTable, insertPars);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addUserToCircle(
    final SSUri circleUri, 
    final SSUri userUri) throws Exception{
    
    try{
      
      if(SSObjU.isNull(circleUri, userUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String> insertPars = new HashMap<String, String>();
      
      insertPars.put(SSSQLVarU.circleId,      SSUri.toStr(circleUri));
      insertPars.put(SSSQLVarU.entityId,      SSUri.toStr(userUri));
      
      dbSQL.insert(circleUsersTable, insertPars);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addEntityToCircle(
    final SSUri circleUri,
    final SSUri entityUri) throws Exception{
    
    try{
      
      if(SSObjU.isNull(circleUri, entityUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String> insertPars = new HashMap<String, String>();
      
      insertPars.put(SSSQLVarU.circleId,      SSUri.toStr(circleUri));
      insertPars.put(SSSQLVarU.entityId,      SSUri.toStr(entityUri));
      
      dbSQL.insert(circleEntitiesTable, insertPars);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public SSUri createCircleURI() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objCircle().toString()));
  }
  
  private SSUri objCircle() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.circle.toString());
  } 
  
  public List<SSAccessRightsRightTypeE> getCircleRights(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(circleUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String>             where  = new HashMap<String, String>();
      final List<SSAccessRightsRightTypeE>  rights = new ArrayList<SSAccessRightsRightTypeE>();
      
      where.put(SSSQLVarU.circleId,      SSUri.toStr(circleUri));
      
      resultSet = dbSQL.selectAllWhere(circleRightsTable, where);
      
      while(resultSet.next()){
        rights.add(SSAccessRightsRightTypeE.get(bindingStr(resultSet, SSSQLVarU.accessRight)));
      }
      
      return rights;
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public List<SSUri> getCircleUserUris(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(circleUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String>   where    = new HashMap<String, String>();
      final List<SSUri>           userUris = new ArrayList<SSUri>();
      
      where.put(SSSQLVarU.circleId,      SSUri.toStr(circleUri));
      
      resultSet = dbSQL.selectAllWhere(circleUsersTable, where);
      
      while(resultSet.next()){
        userUris.add(bindingStrToUri(resultSet, SSSQLVarU.userId));
      }
      
      return userUris;
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public List<SSUri> getCircleEntityUris(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(circleUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String>   where       = new HashMap<String, String>();
      final List<SSUri>           entityUris  = new ArrayList<SSUri>();
      
      where.put(SSSQLVarU.circleId,      SSUri.toStr(circleUri));
      
      resultSet = dbSQL.selectAllWhere(circleEntitiesTable, where);
      
      while(resultSet.next()){
        entityUris.add(bindingStrToUri(resultSet, SSSQLVarU.entityId));
      }
      
      return entityUris;
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public List<SSUri> getEntityCircleURIs(
    final SSUri entityUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(entityUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String>   where       = new HashMap<String, String>();
      final List<String>          columnNames = new ArrayList<String>();
      final List<SSUri>           circleUris  = new ArrayList<SSUri>();
      
      where.put(SSSQLVarU.entityId, SSUri.toStr(entityUri));
      
      columnNames.add(SSSQLVarU.circleId);

      resultSet = dbSQL.selectCertainDistinctWhere(circleEntitiesTable, columnNames, where);
      
      while(resultSet.next()){
        circleUris.add(bindingStrToUri(resultSet, SSSQLVarU.circleId));
      }
      
      return circleUris;
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public SSAccessRightsCircleTypeE getCircleType(
    final SSUri circleUri) throws Exception{
    
    ResultSet resultSet = null;
    
    try{
      
      if(SSObjU.isNull(circleUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String>   where       = new HashMap<String, String>();
      
      where.put(SSSQLVarU.circleId,      SSUri.toStr(circleUri));
      
      resultSet = dbSQL.selectAllWhere(circleTable, where);

      resultSet.first();
      
      return SSAccessRightsCircleTypeE.get(bindingStr(resultSet, SSSQLVarU.circleType));
      
    }catch(Exception error){
      dbSQL.closeStmt(resultSet);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
