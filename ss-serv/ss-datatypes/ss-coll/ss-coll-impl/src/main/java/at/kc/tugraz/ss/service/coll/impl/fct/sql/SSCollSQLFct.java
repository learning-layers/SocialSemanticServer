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
package at.kc.tugraz.ss.service.coll.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircleTypeE;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.coll.datatypes.SSColl;
import at.kc.tugraz.ss.service.coll.datatypes.SSCollEntry;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCollSQLFct extends SSDBSQLFct{

  public SSCollSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super(dbSQL);
  }
  
  public SSUri createColl(final SSUri collUri) throws Exception{
    
     if(collUri == null){
      SSServErrReg.regErrThrow(new Exception("colluri null"));
      return null;
    }
     
    final Map<String, String> insertPars = new HashMap<String, String>();
    
    insertPars.put(SSSQLVarU.collId, collUri.toString());
    
    dbSQL.insert(collTable, insertPars);
    
    return collUri;
  }
  
  public void addUserRootColl(
    final SSUri rootCollUri, 
    final SSUri userUri) throws Exception{
    
    if(SSObjU.isNull(rootCollUri, userUri)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return;
    }
    
    final Map<String, String> insertPars = new HashMap<String, String>();
        
    //add coll to coll root table
    insertPars.put(SSSQLVarU.userId, userUri.toString());
    insertPars.put(SSSQLVarU.collId, rootCollUri.toString());
    
    dbSQL.insert(collRootTable, insertPars);
    
    //add coll to user coll table
    insertPars.clear();
    insertPars.put(SSSQLVarU.userId,    userUri.toString());
    insertPars.put(SSSQLVarU.collId,    rootCollUri.toString());
    
    dbSQL.insert(collUserTable, insertPars);
  }
  
  public Boolean isRootColl(final SSUri entityUri) throws Exception{
    
    if(SSObjU.isNull(entityUri)){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return null;
    }
        
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    ResultSet                 resultSet               = null;
    
    whereParNamesWithValues.put(SSSQLVarU.collId, entityUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(collRootTable, whereParNamesWithValues);
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public Boolean isColl(final SSUri entityUri) throws Exception{
    
    if(SSObjU.isNull(entityUri)){
      SSServErrReg.regErrThrow(new Exception("entityUri null"));
      return null;
    }
    
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    ResultSet                 resultSet               = null;
    
    whereParNamesWithValues.put(SSSQLVarU.collId, entityUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(collTable, whereParNamesWithValues);
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void addEntryToColl(
    final SSUri      collParent, 
    final SSUri      collEntry, 
    final SSLabelStr entryLabel) throws Exception{
    
    if(SSObjU.isNull(collParent, collEntry, entryLabel)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return;
    }
    
    final Map<String, String> insertPars = new HashMap<String, String>();
    
    //add coll entry to coll entry pos table
    Integer collEntryCount = getCollEntryCount(collParent);
    
    collEntryCount++;
    
    insertPars.put(SSSQLVarU.collId,  collParent.toString());
    insertPars.put(SSSQLVarU.entryId, collEntry.toString());
    insertPars.put(SSSQLVarU.pos,     collEntryCount.toString());
    
    dbSQL.insert(collEntryPosTable, insertPars);
  }

  public Integer getCollEntryCount(SSUri coll) throws Exception{
    
    Map<String, String> selectPars     = new HashMap<String, String>();
    ResultSet           resultSet      = null;
    Integer             collEntryCount = 0;

    selectPars.put(SSSQLVarU.collId, coll.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(collEntryPosTable, selectPars);
      
      resultSet.last();
      
      collEntryCount = new Integer(resultSet.getRow());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return collEntryCount;
  }
  
  public List<SSColl> getAllPublicColls() throws Exception{
    
    final List<String>        tableNames    = new ArrayList<String>();
    final List<String>        columnNames   = new ArrayList<String>();
    final Map<String, String> where         = new HashMap<String, String>();
    final List<String>        whereFixed    = new ArrayList<String>();
    ResultSet                 resultSet     = null;
    final List<SSColl>        publicColls   = new ArrayList<SSColl>();
    
    try{
      tableNames.add              (collTable);
      tableNames.add              (entityTable);
      tableNames.add              (circleTable);
      tableNames.add              (circleEntitiesTable);
      
      columnNames.add             (SSSQLVarU.id);
      columnNames.add             (SSSQLVarU.collId);
      columnNames.add             (SSSQLVarU.author);
      columnNames.add             (SSSQLVarU.label);
      columnNames.add             (SSSQLVarU.entityId);
      columnNames.add             (circleTable         + SSStrU.dot + SSSQLVarU.circleId);
      columnNames.add             (circleEntitiesTable + SSStrU.dot + SSSQLVarU.circleId);
      columnNames.add             (SSSQLVarU.circleType);
      
      where.put                   (SSSQLVarU.circleType, SSEntityCircleTypeE.toStr(SSEntityCircleTypeE.pub));
        
      whereFixed.add(SSSQLVarU.id                                              + SSStrU.equal + SSSQLVarU.collId);
      whereFixed.add(SSSQLVarU.collId                                          + SSStrU.equal + SSSQLVarU.entityId);
      whereFixed.add(circleEntitiesTable + SSStrU.dot + SSSQLVarU.circleId     + SSStrU.equal + circleTable + SSStrU.dot + SSSQLVarU.circleId);
      
      resultSet =
        dbSQL.selectCertainDistinctWhere(
        tableNames,
        columnNames,
        where,
        whereFixed);
      
      while(resultSet.next()){
        
        publicColls.add(
          SSColl.get(
            bindingStrToUri(resultSet, SSSQLVarU.id), 
            null, 
            bindingStrToUri(resultSet, SSSQLVarU.author),
            bindingStr     (resultSet, SSSQLVarU.label),
            null));
      }
      
      return publicColls;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  } 
  
  public List<String> getAllUserCollURIs(
    final SSUri userUri) throws Exception{
     
    ResultSet resultSet = null;
    
    try{
      final Map<String, String> whereParNamesWithValues   = new HashMap<String, String>();
      final List<String>        userCollUris              = new ArrayList<String>();
    
      whereParNamesWithValues.put(SSSQLVarU.userId, userUri.toString());
      
      resultSet = dbSQL.selectAllWhere(collUserTable, whereParNamesWithValues);
      
      while(resultSet.next()){
        userCollUris.add(bindingStr(resultSet, SSSQLVarU.collId));
      }
      
      return userCollUris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }  
  
  public Boolean ownsUserColl(
    final SSUri userUri, 
    final SSUri collUri) throws Exception {
    
    ResultSet resultSet = null;
    
    try{

      final Map<String, String> where = new HashMap<String, String>();
      
      where.put(SSSQLVarU.userId, userUri.toString());
      where.put(SSSQLVarU.collId, collUri.toString());
    
      resultSet = dbSQL.selectAllWhere(collUserTable, where);

      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }  
  
  public Boolean ownsUserASubColl(
    final SSUri userUri, 
    final SSUri collUri) throws Exception{
    
    if(SSObjU.isNull(userUri, collUri)){
      SSServErrReg.regErrThrow(new Exception("pars not okay"));
      return null;
    }
        
    final List<String> subCollUris    = new ArrayList<String>();
    
    getAllChildCollURIs(collUri.toString(), collUri.toString(), subCollUris);
    
    for(String subCollUri : subCollUris){
      
      if(ownsUserColl(userUri, SSUri.get(subCollUri))){
        return true;
      }
    }
    
    return false;
  }
  
  public void addCollToUserColl(
    final SSUri       user, 
    final SSUri       collParent, 
    final SSUri       collChild, 
    final Boolean     createdCollIsInSharedOrPublicCircle,
    final Boolean     addedCollIsSharedOrPublic) throws Exception{
    
    if(SSObjU.isNull(user, collParent, collChild, createdCollIsInSharedOrPublicCircle, addedCollIsSharedOrPublic)){
      SSServErrReg.regErrThrow(new Exception("pars not ok"));
      return;
    }
    
    if(
      createdCollIsInSharedOrPublicCircle &&
      addedCollIsSharedOrPublic){
      SSServErrReg.regErrThrow(new Exception("cannot add to shared coll and add shared/public coll at the same time"));
      return;
    }
    
    final Map<String, String> insertPars = new HashMap<String, String>(); 
    
    //add relation of coll parent to child coll to hierarchy table
    insertPars.clear();
    insertPars.put(SSSQLVarU.collParentId, collParent.toString());
    insertPars.put(SSSQLVarU.collChildId,  collChild.toString());
    
    dbSQL.insert(collHierarchyTable, insertPars);
    
    //add coll child to coll parent in coll entry pos table
    final Integer collEntryCount = getCollEntryCount(collParent) + 1;
    
    insertPars.clear();
    insertPars.put(SSSQLVarU.collId,  collParent.toString());
    insertPars.put(SSSQLVarU.entryId, collChild.toString());
    insertPars.put(SSSQLVarU.pos,     collEntryCount.toString());
    
    dbSQL.insert(collEntryPosTable, insertPars);
    
    //add child coll to user coll table
    insertPars.clear();
    insertPars.put(SSSQLVarU.userId,    user.toString());
    insertPars.put(SSSQLVarU.collId,    collChild.toString());
    
    dbSQL.insert(collUserTable, insertPars);
    
    //add currently added coll to other users as well
    if(createdCollIsInSharedOrPublicCircle){

      insertPars.clear();
      insertPars.put(SSSQLVarU.collId,    collChild.toString());
        
      for(SSUri coUserUri : getCollUsers(collParent)){
        
        if(SSStrU.equals(coUserUri.toString(), user.toString())){
          continue;
        }
        
        insertPars.put(SSSQLVarU.userId, coUserUri.toString());
        
        dbSQL.insert(collUserTable, insertPars);
      }
    }
    
    //add sub colls of shared / pub coll for this user as well
    if(addedCollIsSharedOrPublic){
      
      final List<String> subCollUris = new ArrayList<String>();
      
      getAllChildCollURIs(collChild.toString(), collChild.toString(), subCollUris);
      
      insertPars.clear();
      insertPars.put(SSSQLVarU.userId,    user.toString());
      
      for(String subCollUri : subCollUris){
        
        insertPars.put(SSSQLVarU.collId, subCollUri);
        
        dbSQL.insert(collUserTable, insertPars);
      }
    }
  }

  public void unlinkUserCollAndSubColls(
    final SSUri userUri, 
    final SSUri parentCollUri, 
    final SSUri collUri) throws Exception{
    
    if(SSObjU.isNull(userUri, parentCollUri, collUri)){
      SSServErrReg.regErrThrow(new Exception("pars not okay"));
      return;
    }
    
    final Map<String, String> deletePars = new HashMap<String, String>(); 
    final List<String>       subCollUris = new ArrayList<String>();

    //remove sub colls of followed coll from user coll table as well
    getAllChildCollURIs(collUri.toString(), collUri.toString(), subCollUris);
    
    deletePars.clear();
    deletePars.put(SSSQLVarU.userId, userUri.toString());
    
    for(String subCollUri : subCollUris){
      
      deletePars.put(SSSQLVarU.collId, subCollUri);
      
      dbSQL.deleteWhere(collUserTable, deletePars);
    }
    
    //remove coll from user coll table
    deletePars.clear();
    deletePars.put(SSSQLVarU.userId,     userUri.toString());
    deletePars.put(SSSQLVarU.collId,     collUri.toString());
            
    dbSQL.deleteWhere(collUserTable, deletePars);
    
    //remove coll from coll hierarchy table
    deletePars.clear();
    deletePars.put(SSSQLVarU.collParentId, parentCollUri.toString());
    deletePars.put(SSSQLVarU.collChildId,  collUri.toString());
            
    dbSQL.deleteWhere(collHierarchyTable, deletePars);
    
    //remove coll from coll entries pos table
    deletePars.clear();
    deletePars.put(SSSQLVarU.collId,   parentCollUri.toString());
    deletePars.put(SSSQLVarU.entryId,  collUri.toString());
            
    dbSQL.deleteWhere(collEntryPosTable, deletePars);
  }

  public void getAllChildCollURIs(
    final String       startCollUri, 
    final String       currentCollUri, 
    final List<String> subCollUris) throws Exception{
    
    for(String directSubCollUri : getDirectChildCollURIs(currentCollUri)){
      getAllChildCollURIs(startCollUri, directSubCollUri, subCollUris);
    }
    
    if(startCollUri.equals(currentCollUri)){
      return;
    }
    
    if(!subCollUris.contains(currentCollUri)){
      subCollUris.add(currentCollUri);
    }
  }
  
  public void getAllParentCollURIs(
    final String       startCollUri, 
    final String       currentCollUri, 
    final List<String> parentCollUris) throws Exception{
    
    for(String directParentCollUri : getDirectParentCollURIs(currentCollUri)){
      getAllParentCollURIs(startCollUri, directParentCollUri, parentCollUris);
    }
    
    if(startCollUri.equals(currentCollUri)){
      return;
    }
    
    if(!parentCollUris.contains(currentCollUri)){
      parentCollUris.add(currentCollUri);
    }
  }
  
  private List<String> getDirectChildCollURIs(final String collUri) throws Exception{
    
    final Map<String, String> whereParNamesWithValues   = new HashMap<String, String>();
    final List<String>        directSubCollUris         = new ArrayList<String>();
    ResultSet                 resultSet                 = null;
    
    whereParNamesWithValues.put(SSSQLVarU.collParentId, collUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(collHierarchyTable, whereParNamesWithValues);
      
      while(resultSet.next()){
        directSubCollUris.add(resultSet.getString(SSSQLVarU.collChildId));
      }
      
      return directSubCollUris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  private List<String> getDirectParentCollURIs(final String collUri) throws Exception{
    
    final List<String>        directParentCollUris    = new ArrayList<String>();
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    ResultSet                 resultSet               = null;
    
    try{
      whereParNamesWithValues.put(SSSQLVarU.collChildId, collUri);
      
      resultSet = dbSQL.selectAllWhere(collHierarchyTable, whereParNamesWithValues);
      
      while(resultSet.next()){
        directParentCollUris.add(bindingStr(resultSet, SSSQLVarU.collParentId));
      }
      
      return directParentCollUris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void removeUserPrivateCollAndUnlinkSubColls(
    final SSUri userUri,
    final SSUri collUri) throws Exception{
    
    try{
      
      if(SSObjU.isNull(userUri, collUri)){
        throw new Exception("pars not ok");
      }
      
      final Map<String, String> deletePars = new HashMap<String, String>();
      final List<String>        directChildCollURIs;
      
      //retrieve all direct sub coll uris
      directChildCollURIs = getDirectChildCollURIs(collUri.toString());
      
      //unlink all direct sub colls (and hence their sub colls as well)
      for(String subCollUri : directChildCollURIs){
        unlinkUserCollAndSubColls(userUri, collUri, SSUri.get(subCollUri));
      }
      
      deletePars.clear();
      deletePars.put(SSSQLVarU.collId, SSUri.toStr(collUri));
      
      dbSQL.deleteWhere(collTable, deletePars);
      
      deletePars.clear();
      deletePars.put(SSSQLVarU.entryId, SSUri.toStr(collUri));
      
      dbSQL.deleteWhere(collEntryPosTable, deletePars);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void removeEntryFromColl(
    final SSUri collUri, 
    final SSUri collEntryUri) throws Exception{
    
    if(SSObjU.isNull(collUri, collEntryUri)){
      SSServErrReg.regErrThrow(new Exception("pars not ok"));
      return;
    }

    final Map<String, String> deletePars = new HashMap<String, String>();
    
    //remove coll entry from coll entry pos table
    deletePars.put(SSSQLVarU.collId,  collUri.toString());
    deletePars.put(SSSQLVarU.entryId, collEntryUri.toString());
      
    dbSQL.deleteWhere(collEntryPosTable, deletePars);
  }

  public void changeCollEntriesPos(
    final SSUri         collUri, 
    final List<SSUri>   collEntries, 
    final List<Integer> order) throws Exception{
  
    if(
      SSObjU.isNull(collUri, collEntries, order) ||
      collEntries.size() != order.size()){
      SSServErrReg.regErrThrow(new Exception("pars not okay"));
      return;
    }
    
    final Map<String, String> updatePars = new HashMap<String, String>();
    final Map<String, String> newValues  = new HashMap<String, String>();
    Integer                   counter    = 0;
    
    updatePars.put(SSSQLVarU.collId,  SSUri.toStr(collUri));
    
    while(counter < collEntries.size()){
      
      updatePars.put(SSSQLVarU.entryId, collEntries.get(counter).toString());
      newValues.put (SSSQLVarU.pos,     order.get      (counter).toString());
      
      counter++;
      
      dbSQL.updateWhere(collEntryPosTable, updatePars, newValues);
    }
  }
  
  private SSColl getColl(
    final SSUri                     collUri, 
    final List<SSEntityCircleTypeE> circleTypes) throws Exception{
   
    if(SSObjU.isNull(collUri, circleTypes)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
    
//    if(!ownsUserColl(userUri, collUri)){
//      SSServErrReg.regErrThrow(new Exception("user doesnt have access to coll"));
//      return null;
//    }
    
    final List<String>        tableNames              = new ArrayList<String>();
    final List<String>        columnNames             = new ArrayList<String>();
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    ResultSet                 resultSet               = null;
    
    try{
      tableNames.add              (collTable);
      tableNames.add              (entityTable);
      columnNames.add             (SSSQLVarU.collId);
      columnNames.add             (SSSQLVarU.author);
      columnNames.add             (SSSQLVarU.label);
      whereParNamesWithValues.put (SSSQLVarU.collId, collUri.toString());
      
      resultSet =
        dbSQL.selectCertainWhere(
        tableNames,
        columnNames,
        whereParNamesWithValues,
        SSSQLVarU.collId + SSStrU.equal + SSSQLVarU.id);
      
      resultSet.first();
      
      return SSColl.get(
        collUri,
        null,
        bindingStrToUri  (resultSet, SSSQLVarU.author),
        bindingStr       (resultSet, SSSQLVarU.label),
        circleTypes);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSColl getCollWithEntries(
    final SSUri                           collUri,
    final List<SSEntityCircleTypeE>       circleTypes) throws Exception{

    if(SSObjU.isNull(collUri, circleTypes)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
        
    final List<String>        tableNames              = new ArrayList<String>();
    final List<String>        columnNames             = new ArrayList<String>();
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    final SSColl              coll;
    ResultSet                 resultSet               = null;
    SSCollEntry               collEntry;
    
    try{
      
      coll = getColl(collUri, circleTypes);
      
      tableNames.add              (collEntryPosTable); 
      tableNames.add              (entityTable);
      columnNames.add             (SSSQLVarU.entryId);
      columnNames.add             (SSSQLVarU.pos);
      columnNames.add             (SSSQLVarU.label);
      columnNames.add             (SSSQLVarU.type);
      
      whereParNamesWithValues.put (SSSQLVarU.collId, coll.uri.toString());
        
      resultSet = 
        dbSQL.selectCertainWhereOrderBy(
        tableNames, 
        columnNames, 
        whereParNamesWithValues, 
        SSSQLVarU.entryId + SSStrU.equal + SSSQLVarU.id,
        SSSQLVarU.pos,
        "ASC");
      
      while(resultSet.next()){
        
        collEntry =
          SSCollEntry.get(
            bindingStrToUri        (resultSet, SSSQLVarU.entryId),
            bindingStr             (resultSet, SSSQLVarU.label),
            new ArrayList<SSEntityCircleTypeE>(),
            bindingStrToInteger    (resultSet, SSSQLVarU.pos),
            bindingStrToEntityType (resultSet, SSSQLVarU.type));
        
        coll.entries.add(collEntry);
      }
      
      return coll;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public SSUri getUserRootCollURI(final SSUri userUri) throws Exception{
    
    if(SSObjU.isNull(userUri)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
     
    final Map<String, String> selectPars      = new HashMap<String, String>();
    ResultSet                 resultSet       = null;
    
    try{
      
      selectPars.put(SSSQLVarU.userId, SSUri.toStr(userUri));
      
      resultSet = dbSQL.selectAllWhere(collRootTable, selectPars);

      if(resultSet.first()){
        return bindingStrToUri(resultSet, SSSQLVarU.collId);
      }
      
      throw new Exception("user root coll doesnt exist");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public SSUri getUserDirectParentCollURI(
    final SSUri userUri, 
    final SSUri childColl) throws Exception{
    
    if(isRootColl(childColl)){
      return childColl;
    }
    
    for(String parentCollUri : getDirectParentCollURIs(childColl.toString())){ 
      
      if(ownsUserColl(userUri, SSUri.get(parentCollUri))){
        return SSUri.get(parentCollUri);
      }
    }
    
    SSServErrReg.regErrThrow(new Exception("user doesnt own coll"));
    return null;
  }

  public Boolean containsEntry(
    final SSUri collUri, 
    final SSUri collEntryUri) throws Exception{
    
    if(SSObjU.isNull(collUri, collEntryUri)){
      SSServErrReg.regErrThrow(new Exception("parameter(s) null"));
      return null;
    }
    
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    ResultSet                 resultSet               = null;
    
    try{
      whereParNamesWithValues.put(SSSQLVarU.collId,  SSUri.toStr(collUri));
      whereParNamesWithValues.put(SSSQLVarU.entryId, SSUri.toStr(collEntryUri));
    
      resultSet = dbSQL.selectAllWhere(collEntryPosTable, whereParNamesWithValues);
      
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public Boolean existsUserRootColl(
    final SSUri userUri) throws Exception{
    
    if(SSObjU.isNull(userUri)){
      SSServErrReg.regErrThrow(new Exception("parameter(s) null"));
      return null;
    }
    
    final Map<String, String> selectPars         = new HashMap<String, String>();
    ResultSet                 resultSet          = null;
    
    try{
      selectPars.put(SSSQLVarU.userId, userUri.toString());
      
      resultSet  = dbSQL.selectAllWhere(collRootTable, selectPars);
      
      return resultSet.first();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri createCollURI() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objColl().toString()));
  }
  
  private static SSUri objColl() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.coll.toString());
  }  
  
  public List<SSUri> getCollUsers(
    final SSUri collUri) throws Exception{

    if(SSObjU.isNull(collUri)){
      SSServErrReg.regErrThrow(new Exception("parameter(s) null"));
      return null;
    }
    
    final Map<String, String> where       = new HashMap<String, String>();
    final List<SSUri>         users       = new ArrayList<SSUri>();
    ResultSet                 resultSet   = null;
    
    try{
      where.put(SSSQLVarU.collId, collUri.toString());
      
      resultSet = dbSQL.selectAllWhere(collUserTable, where);
      
      while(resultSet.next()){
        users.add(bindingStrToUri(resultSet, SSSQLVarU.userId));
      }
      
      return users;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<String> getCollUrisContainingEntity(final SSUri entityUri) throws Exception{
    
    if(SSObjU.isNull(entityUri)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
        
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    final List<String>        collUris                = new ArrayList<String>();
    ResultSet                 resultSet               = null;
    
    whereParNamesWithValues.put(SSSQLVarU.entryId, entityUri.toString());
    
    try{
      
      resultSet = dbSQL.selectAllWhere(collEntryPosTable, whereParNamesWithValues);
      
      while(resultSet.next()){
        collUris.add(bindingStr(resultSet, SSSQLVarU.collId));
      }
      
      return collUris;
    
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}

//  public SSSpaceEnum getUserCollSpace(SSUri user, SSUri coll) throws Exception{
//    
//    Map<String, String> selectPars    = new HashMap<String, String>();
//    ResultSet           resultSet     = null;
//    SSSpaceEnum         userCollSpace = null;
//    
//    selectPars.put(SSSQLVarU.userId, user.toString());
//    selectPars.put(SSSQLVarU.collId, coll.toString());
//    
//    try{
//      resultSet = dbSQL.dbSQLSelectAllWhere(collUserTable, selectPars);
//      
//      resultSet.first();
//      
//      userCollSpace = SSSpaceEnum.get(resultSet.getString(SSSQLVarU.collSpace));
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.dbSQLCloseStmt(resultSet);
//    }
//    
//    return userCollSpace;
//  }

//private Boolean ownsUserCollByHierarchy(SSUri userUri, SSUri collUri) throws Exception{
//    
//    final List<String> collParents    = new ArrayList<String>();
//    final List<String> newCollParents = new ArrayList<String>();
//    
//    try{
//      
//      collParents.addAll(getCollDirectParentURIs(collUri));
//      
//      while(true){
//        
//        for(String parentCollUri : collParents){
//          
//          for(String tmpUri : getCollDirectParentURIs(SSUri.get(parentCollUri))){
//          
//            if(!newCollParents.contains(tmpUri)){
//              newCollParents.add(tmpUri);
//            }
//          }
//        }
//        
//        if(newCollParents.isEmpty()){
//          return false;
//        }
//        
//        for(String newCollParentUri : newCollParents){
//          
//          if(ownsUserColl(userUri, SSUri.get(newCollParentUri))){
//            return true;
//          }
//        }
//        
//        collParents.clear();
//        collParents.addAll(newCollParents);
//        newCollParents.clear();
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//  public SSSpaceEnum getCollSpace(SSUri userUri, SSUri collUri) throws Exception{
//    
//    if(
//      userUri      == null ||
//      collUri      == null){
//      SSServErrReg.regErrThrow(new Exception("parameter(s) null"));
//      return null;
//    }
//        
//    Map<String, String> selectPars  = new HashMap<String, String>();
//    ResultSet           resultSet   = null;
//    SSSpaceEnum         collSpace   = null;
//    
//    selectPars.put(SSSQLVarU.userId, userUri.toString());
//    selectPars.put(SSSQLVarU.collId, collUri.toString());
//    
//    try{
//      
//      resultSet = dbSQL.dbSQLSelectAllWhere(collUserTable, selectPars);
//      
//      if(!resultSet.first()){
//        SSServErrReg.regErrThrow(new Exception("coll-user combination does not exist"));
//      }
//
//      collSpace = bindingStrToSpace(resultSet, SSSQLVarU.collSpace);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.dbSQLCloseStmt(resultSet);
//    }
//    
//    return collSpace;
//  }

// public Boolean isEntityInPrivateUserColl(SSUri user, SSUri entity) throws Exception{
//
//    Map<String, String> selectPars                = new HashMap<String, String>();
//    ResultSet           resultSet                 = null;
//    List<String>        parentCollUris            = new ArrayList<String>();
//    Boolean             isEntityInPrivateUserColl;
//    
//    if(isColl(entity)){
//
//      selectPars.put(SSSQLVarU.userId, user.toString());
//      selectPars.put(SSSQLVarU.collId, entity.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(resultSet.first()){
//          isEntityInPrivateUserColl = SSSpaceEnum.isPrivate(bindingStrToSpace(resultSet, SSSQLVarU.collSpace));
//          return isEntityInPrivateUserColl;
//        }else{
//          return false;
//        }
//        
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    selectPars = new HashMap<String, String>();
//    selectPars.put(SSSQLVarU.entryId, entity.toString());
//    
//    try{
//      resultSet = dbSQL.selectAllWhere(collEntryPosTable, selectPars);
//      
//      while(resultSet.next()){
//        parentCollUris.add(resultSet.getString(SSSQLVarU.collId));
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    if(parentCollUris.isEmpty()){
//      return null;
//    }
//    
//    selectPars = new HashMap<String, String>();
//    
//    for(String parentCollUri : parentCollUris){
//      
//      selectPars.put(SSSQLVarU.collId, parentCollUri.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(
//          resultSet.next() &&
//          SSSpaceEnum.isPrivate(bindingStrToSpace(resultSet, SSSQLVarU.collSpace))){
//          return true;
//        }
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    return false;
//  }

//public Boolean isEntityInSharedOrFollowedUserColl(final SSUri user, SSUri entity) throws Exception{
//
//    Map<String, String> selectPars;
//    ResultSet           resultSet                            = null;
//    List<String>        parentCollUris                       = new ArrayList<String>();
//    Boolean             isEntityInSharedOrFollowedUserColl;
//    
//    if(isColl(entity)){
//
//      selectPars = new HashMap<String, String>();
//      selectPars.put(SSSQLVarU.userId, user.toString());
//      selectPars.put(SSSQLVarU.collId, entity.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(resultSet.first()){
//          isEntityInSharedOrFollowedUserColl = SSSpaceEnum.isSharedOrFollow(bindingStrToSpace(resultSet, SSSQLVarU.collSpace));
//          return isEntityInSharedOrFollowedUserColl;
//        }else{
//          return false;
//        }
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    selectPars = new HashMap<String, String>();
//    selectPars.put(SSSQLVarU.entryId, entity.toString());
//    
//    try{
//      resultSet = dbSQL.selectAllWhere(collEntryPosTable, selectPars);
//      
//      while(resultSet.next()){
//        parentCollUris.add(resultSet.getString(SSSQLVarU.collId));
//      }
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    if(parentCollUris.isEmpty()){
//      return null;
//    }
//    
//    selectPars = new HashMap<String, String>();
//    
//    for(String parentCollUri : parentCollUris){
//      
//      selectPars.put(SSSQLVarU.collId, parentCollUri.toString());
//      
//      try{
//        resultSet = dbSQL.selectAllWhere(collUserTable, selectPars);
//        
//        if(
//          resultSet.next() &&
//          SSSpaceEnum.isSharedOrFollow(bindingStrToSpace(resultSet, SSSQLVarU.collSpace))){
//          return true;
//        }
//      }catch(Exception error){
//        SSServErrReg.regErrThrow(error);
//      }finally{
//        dbSQL.closeStmt(resultSet);
//      }
//    }
//    
//    return false;
//  }

//  public Boolean newIsEntityInPrivateUserColl(final SSUri userUri, final SSUri entityUri) throws Exception{
//    
//    if(SSObjU.isNull(userUri, entityUri)){
//      SSServErrReg.regErrThrow(new Exception("pars null"));
//      return null;
//    }
//    
//    final List<String>        collUris;
//    SSColl                    coll;
//      
//    try{
//      collUris = getCollUrisContainingEntity(entityUri);
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//    
//    if(collUris.isEmpty()){
//      return false;
//    }
//    
//    for(String collUri : collUris){
//      
//      try{
//        coll = getUserColl(userUri, SSUri.get(collUri));
//      }catch(Exception error){
//        continue;
//      }
//      
//      if(SSSpaceEnum.isPrivate(coll.space)){
//        return true;
//      }
//    }
//    
//    return false;
//  }
  
//  public Boolean newIsEntityInSharedOrFollowedUserColl(final SSUri userUri, final SSUri entityUri) throws Exception{
//    
//    if(SSObjU.isNull(userUri, entityUri)){
//      SSServErrReg.regErrThrow(new Exception("pars null"));
//      return null;
//    }
//    
//    final List<String>        collUris;
//    final List<String>        userCollUris;
//    SSColl                    coll;
//    
//    try{
//      userCollUris  = getAllUserCollURIs(userUri);
//      
//      if(userCollUris.isEmpty()){
//        return false;
//      }
//      
//      collUris      = getCollUrisContainingEntity(entityUri);
//      
//      if(collUris.isEmpty()){
//        return false;
//      }
//      
//      for(String userCollUri : userCollUris){
//        
//        if(!collUris.contains(userCollUri)){
//          continue;
//        }
//        
//        coll = getUserColl(userUri, SSUri.get(userCollUri));
//        
//        if(SSSpaceEnum.isSharedOrFollow(coll.space)){
//          return true;
//        }
//      }
//      
//      return false;
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }

//  public void removeColl(
//    final SSUri collUri) throws Exception{
//    
//    if(collUri == null){
//      SSServErrReg.regErrThrow(new Exception("colluri null"));
//      return;
//    }
//    
//    final List<String>        subCollUris = new ArrayList<String>();
//    final Map<String, String> deletePars  = new HashMap<String, String>();
//    
//    try{
//      
//      //retrieve all sub coll uris
//      getAllChildCollURIs(collUri.toString(), collUri.toString(), subCollUris);
//          
//      //remove all sub colls
//      for(String subCollUri : subCollUris){
//        
//        deletePars.clear();
//        deletePars.put(SSSQLVarU.collId, subCollUri);
//        
//        dbSQL.deleteWhere(collTable, deletePars);
//        
//        deletePars.clear();
//        deletePars.put(SSSQLVarU.entryId, subCollUri);
//        
//        dbSQL.deleteWhere(collEntryPosTable, deletePars);
//      }
//      
//      deletePars.clear();
//      deletePars.put(SSSQLVarU.collId, SSUri.toStr(collUri));
//
//      dbSQL.deleteWhere(collTable, deletePars);
//      
//      deletePars.clear();
//      deletePars.put(SSSQLVarU.entryId, SSUri.toStr(collUri));
//
//      dbSQL.deleteWhere(collEntryPosTable, deletePars);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }

//  public Boolean ownsUserASubColl(
//    final SSUri       userUri, 
//    final SSUri       collUri,
//    final SSSpaceEnum space) throws Exception{
//    
//    if(SSObjU.isNull(userUri, collUri, space)){
//      SSServErrReg.regErrThrow(new Exception("pars not okay"));
//      return null;
//    }
//        
//    final List<String> subCollUris    = new ArrayList<String>();
//    
//    getAllChildCollURIs(collUri.toString(), collUri.toString(), subCollUris);
//    
//    for(String subCollUri : subCollUris){
//      
//      if(ownsUserColl(userUri, SSUri.get(subCollUri), space)){
//        return true;
//      }
//    }
//    
//    return false;
//  }
  
  //  public Boolean ownsUserASuperColl(
//    final SSUri       userUri, 
//    final SSUri       collUri,
//    final SSSpaceEnum space) throws Exception{
//    
//    if(SSObjU.isNull(userUri, collUri, space)){
//      SSServErrReg.regErrThrow(new Exception("pars not okay"));
//      return null;
//    }
//        
//    final List<String> superCollUris = new ArrayList<String>();
//    
//    getAllParentCollURIs(collUri.toString(), collUri.toString(), superCollUris);
//    
//    for(String parentCollUri : superCollUris){
//      
//      if(ownsUserColl(userUri, SSUri.get(parentCollUri), space)){
//        return true;
//      }
//    }
//    
//    return false;
//  }
  
//  public Boolean ownsUserASuperColl(
//    final SSUri userUri, 
//    final SSUri collUri) throws Exception{
//    
//    if(SSObjU.isNull(userUri, collUri)){
//      SSServErrReg.regErrThrow(new Exception("pars not okay"));
//      return null;
//    }
//        
//    final List<String> superCollUris = new ArrayList<String>();
//    
//    getAllParentCollURIs(collUri.toString(), collUri.toString(), superCollUris);
//    
//    for(String parentCollUri : superCollUris){
//      
//      if(ownsUserColl(userUri, SSUri.get(parentCollUri))){
//        return true;
//      }
//    }
//    
//    return false;
//  }

//  private Boolean ownsUserColl(
//    final SSUri       userUri, 
//    final SSUri       collUri, 
//    final SSSpaceEnum space) throws Exception{
//    
//    
//    if(SSObjU.isNull(userUri, collUri, space)){
//      SSServErrReg.regErrThrow(new Exception("pars not okay"));
//      return null;
//    }
//    
//    final Map<String, String> whereParNamesWithValues  = new HashMap<String, String>();
//    ResultSet                 resultSet                = null;
//    
//    try{
//      whereParNamesWithValues.put(SSSQLVarU.userId,    userUri.toString());
//      whereParNamesWithValues.put(SSSQLVarU.collId,    collUri.toString());
//      whereParNamesWithValues.put(SSSQLVarU.collSpace, space.toString());
//      
//      resultSet = dbSQL.selectAllWhere(collUserTable, whereParNamesWithValues);
//      
//      return resultSet.first();
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    //select * from entity, circleUser, circleEntity, cirlce where 
//    //id = entityUri, circleUser = userUri, circleType = private and id=entityId, circle.circleId=circlecircleId
//    //select * from entity, circleUser, circleEntity, cirlce where id = entityUri, circleUser = userUri, circleType != private;
//  }
//  public List<SSUri> getAllSharedCollURIs() throws Exception{
//    
//    final List<SSUri>         sharedCollURIs          = new ArrayList<SSUri>();
//    final List<String>        columnNames             = new ArrayList<String>();
//    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
//    ResultSet                 resultSet               = null;
//    
//    //get all colls from user table where space is shared (distinct)
//    columnNames.add             (SSSQLVarU.collId);
//    whereParNamesWithValues.put (SSSQLVarU.collSpace, SSSpaceEnum.sharedSpace.toString());
//    
//    try{
//      resultSet = dbSQL.selectCertainDistinctWhere(collUserTable, columnNames, whereParNamesWithValues);
//
//      while(resultSet.next()){
//        sharedCollURIs.add(bindingStrToUri(resultSet, SSSQLVarU.collId));
//      }
//      
//      return sharedCollURIs;
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//  }

//  public void shareCollAndSubColls(
//    final SSUri collUri) throws Exception{
//    
//    if(SSObjU.isNull(collUri)){
//      SSServErrReg.regErrThrow(new Exception("pars not okay"));
//      return;
//    }
//  
//    final Map<String, String> updatePars = new HashMap<String, String>();
//    final Map<String, String> newValues  = new HashMap<String, String>();
//    final List<String>        subCollUris = new ArrayList<String>();
//    
//    getAllChildCollURIs(collUri.toString(), collUri.toString(), subCollUris);
//    
//    //set space in user coll table for each sub coll to shared
//    for(String subCollUri : subCollUris){
//      
//      updatePars.put(SSSQLVarU.collId,    subCollUri.toString());
//      newValues.put (SSSQLVarU.collSpace, SSSpaceEnum.sharedSpace.toString());
//      
//      dbSQL.updateWhere(collUserTable, updatePars, newValues);
//    }
//    
//    //set space in user coll table for coll to shared
//    updatePars.put(SSSQLVarU.collId,    collUri.toString());
//    newValues.put (SSSQLVarU.collSpace, SSSpaceEnum.sharedSpace.toString());
//    
//    dbSQL.updateWhere(collUserTable, updatePars, newValues);
//  }


//public SSColl getColl(
//    final SSUri collUri) throws Exception{
//   
//    if(SSObjU.isNull(collUri)){
//      SSServErrReg.regErrThrow(new Exception("pars null"));
//      return null;
//    }
//    
//    final List<String>        tableNames              = new ArrayList<String>();
//    final List<String>        columnNames             = new ArrayList<String>();
//    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
//    SSColl                    coll                    = null;
//    ResultSet                 resultSet               = null;
//    
//    try{
//      tableNames.add              (collTable);
//      tableNames.add              (entityTable);
//      columnNames.add             (SSSQLVarU.collId);
//      columnNames.add             (SSSQLVarU.author);
//      columnNames.add             (SSSQLVarU.label);
//      whereParNamesWithValues.put (SSSQLVarU.collId, collUri.toString());
//      
//      resultSet =
//        dbSQL.selectCertainWhere(
//        tableNames,
//        columnNames,
//        whereParNamesWithValues,
//        SSSQLVarU.collId + SSStrU.equal + SSSQLVarU.id);
//      
//      resultSet.first();
//      
//      coll = 
//        SSColl.get(
//        collUri,
//        null, 
//        bindingStrToUri  (resultSet, SSSQLVarU.author), 
//        bindingStr       (resultSet, SSSQLVarU.label), 
//        null);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }finally{
//      dbSQL.closeStmt(resultSet);
//    }
//    
//    return coll;
//  }