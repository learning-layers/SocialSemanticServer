/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.service.disc.impl.fct.sql;

import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSSQLVarU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLFct;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.disc.datatypes.SSDisc;
import at.kc.tugraz.ss.service.disc.datatypes.SSDiscEntry;
import at.kc.tugraz.ss.service.disc.datatypes.SSDiscEntryContent;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSDiscSQLFct extends SSDBSQLFct {

  protected final String               discTable                           = "disc";
  protected final String               discEntryTable                      = "discentry";
  protected final String               discEntriesTable                    = "discentries";
  
  public SSDiscSQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }

  public List<SSUri> getAllDiscUris() throws Exception {
    
    final List<SSUri>   discUris     = new ArrayList<SSUri>();
    ResultSet           resultSet = null;

    try{
      resultSet = dbSQL.selectAll(discTable);

      while(resultSet.next()){
        discUris.add(bindingStrToUri (resultSet, SSSQLVarU.discId));
      }

      return discUris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public List<SSUri> getDiscUrisForTarget(final SSUri targetUri) throws Exception {
  
    if(SSObjU.isNull(targetUri)){
      SSServErrReg.regErrThrow(new Exception("target null"));
      return null;
    }
    
    final List<SSUri>         discUris                = new ArrayList<SSUri>();
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    ResultSet                 resultSet               = null;
    
    whereParNamesWithValues.put(SSSQLVarU.target, targetUri.toString());
    
    try{
      
      resultSet = dbSQL.selectAllWhere(discTable, whereParNamesWithValues);
      
      while(resultSet.next()){
        discUris.add(SSUri.get(bindingStr(resultSet, SSSQLVarU.discId)));
      }

      return discUris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }

  public void createDisc(SSUri discUri, SSUri user, SSUri target, SSLabelStr label) throws Exception{
    
    Map<String, String> insertPars;
    
    insertPars = new HashMap<String,       String>();
    insertPars.put(SSSQLVarU.discId,       discUri.toString());
    insertPars.put(SSSQLVarU.target,       target.toString());
    
    dbSQL.insert(discTable, insertPars);
  }

  public void addDiscEntry(SSUri discEntryUri, SSUri user, SSUri discUri, SSDiscEntryContent content) throws Exception{
    
    Map<String, String> insertPars;
    Integer             discEntryCount;

    insertPars = new HashMap<String, String>();
    insertPars.put(SSSQLVarU.discEntryId,      discEntryUri.toString());
    insertPars.put(SSSQLVarU.discEntryContent, content.toString());
    
    dbSQL.insert(discEntryTable, insertPars);
    
    discEntryCount = getDiscEntryCount(discUri);
    discEntryCount++;

    insertPars = new HashMap<String, String>();
    insertPars.put(SSSQLVarU.discId,      discUri.toString());
    insertPars.put(SSSQLVarU.discEntryId, discEntryUri.toString());
    insertPars.put(SSSQLVarU.pos,         discEntryCount.toString());
    
    dbSQL.insert(discEntriesTable, insertPars);
  }
  
  public Integer getDiscEntryCount(SSUri discUri) throws Exception{
    
    Map<String, String> selectPars     = new HashMap<String, String>();
    ResultSet           resultSet      = null;
    Integer             discEntryCount = 0;
    
    selectPars.put(SSSQLVarU.discId, discUri.toString());
    
    try{
      resultSet = dbSQL.selectAllWhere(discEntriesTable, selectPars);
      
      resultSet.last();
      
      discEntryCount = resultSet.getRow();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return  discEntryCount;
  }
  
  public Boolean isDisc(SSUri entityUri) throws Exception {
    
    ResultSet           resultSet   = null;
    Boolean             isDisc      = false;
    Map<String, String> selectPars;
    
    selectPars = new HashMap<String, String>();
    selectPars.put(SSSQLVarU.discId, entityUri.toString());
    
    try{
      
      resultSet = dbSQL.selectAllWhere(discTable, selectPars);
      isDisc    = resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return isDisc;
  }
  
  public Boolean isDiscEntry(SSUri entityUri) throws Exception {
    
    ResultSet           resultSet   = null;
    Boolean             isDiscEntry = false;
    Map<String, String> selectPars;
    
    selectPars = new HashMap<String, String>();
    selectPars.put(SSSQLVarU.discEntryId, entityUri.toString());
    
    try{
      
      resultSet   = dbSQL.selectAllWhere(discEntriesTable, selectPars);
      isDiscEntry = resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      dbSQL.closeStmt(resultSet);
    }
    
    return isDiscEntry;
  }
  
  public SSDisc getDiscWithoutEntries(
    final SSUri discUri) throws Exception{
    
    if(SSObjU.isNull(discUri)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
    
    final List<String>        tableNames              = new ArrayList<String>();
    final List<String>        columNames              = new ArrayList<String>();
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    ResultSet                 resultSet               = null;
    
    try{
      tableNames.add(entityTable);
      tableNames.add(discTable);
      
      columNames.add(SSSQLVarU.label);
      columNames.add(SSSQLVarU.author);
      columNames.add(SSSQLVarU.discId);
      columNames.add(SSSQLVarU.target);
      
      whereParNamesWithValues.put(SSSQLVarU.discId, discUri.toString());
      
      resultSet = dbSQL.selectCertainWhere(
        tableNames,
        columNames,
        whereParNamesWithValues,
        SSSQLVarU.discId + SSStrU.equal + SSSQLVarU.id);
      
      resultSet.first();
      
      return
        SSDisc.get(
          discUri,
          bindingStrToLabel (resultSet, SSSQLVarU.label),
          bindingStrToUri   (resultSet, SSSQLVarU.author),
          bindingStrToUri   (resultSet, SSSQLVarU.target),
          null);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public void deleteDisc(final SSUri discUri) throws Exception{
    
    if(SSObjU.isNull(discUri)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return;
    }
    
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    
    whereParNamesWithValues.put(SSSQLVarU.discId, discUri.toString());
    
    dbSQL.deleteWhere(discTable, whereParNamesWithValues);
  }
  
  public SSDisc getDiscWithEntries(
    final SSUri   discUri) throws Exception {
    
    if(SSObjU.isNull(discUri)){
      SSServErrReg.regErrThrow(new Exception("pars null"));
      return null;
    }
    
    final List<SSDiscEntry>   discEntries             = new ArrayList<SSDiscEntry>();
    final List<String>        tableNames              = new ArrayList<String>();
    final List<String>        columNames              = new ArrayList<String>();
    final List<String>        whereFixedRestrictions  = new ArrayList<String>();
    final Map<String, String> whereParNamesWithValues = new HashMap<String, String>();
    final SSDisc              disc;
    ResultSet                 resultSet               = null;
    
    try{
      disc = getDiscWithoutEntries(discUri);
      
      tableNames.add(entityTable);
      tableNames.add(discEntriesTable);
      tableNames.add(discEntryTable);
      
      columNames.add(discEntriesTable + SSStrU.dot + SSSQLVarU.discEntryId);
      columNames.add(SSSQLVarU.author);
      columNames.add(SSSQLVarU.discEntryContent);
      columNames.add(SSSQLVarU.pos);
      columNames.add(SSSQLVarU.creationTime);
      
      whereParNamesWithValues.put (SSSQLVarU.discId, discUri.toString());
      whereFixedRestrictions.add  (discEntriesTable + SSStrU.dot + SSSQLVarU.discEntryId + SSStrU.equal + SSSQLVarU.id);
      whereFixedRestrictions.add  (discEntryTable   + SSStrU.dot + SSSQLVarU.discEntryId + SSStrU.equal + SSSQLVarU.id);
      
      resultSet = dbSQL.selectCertainDistinctWhere(
        tableNames,
        columNames,
        whereParNamesWithValues,
        whereFixedRestrictions);
      
      while(resultSet.next()){
        
        discEntries.add(
          SSDiscEntry.get(
            bindingStrToUri       (resultSet, SSSQLVarU.discEntryId),
            bindingStrToInteger   (resultSet, SSSQLVarU.pos),
            SSDiscEntryContent.get(bindingStr(resultSet, SSSQLVarU.discEntryContent)),
            bindingStrToUri       (resultSet, SSSQLVarU.author),
            bindingStrToLong      (resultSet, SSSQLVarU.creationTime)));
      }
      
      disc.entries = discEntries;
      
      return disc;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
  
  public SSUri createDiscUri() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objDisc().toString()));
  }
  
  public SSUri createDiscEntryUri() throws Exception{
    return SSUri.get(SSIDU.uniqueID(objDiscEntry().toString()));
  }
  
  private SSUri objDisc() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.disc.toString());
  }
  
  private SSUri objDiscEntry() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityEnum.discEntry.toString());
  }
}
