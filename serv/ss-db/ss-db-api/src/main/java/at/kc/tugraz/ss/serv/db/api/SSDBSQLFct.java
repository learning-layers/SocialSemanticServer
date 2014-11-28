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
package at.kc.tugraz.ss.serv.db.api;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;
import sss.serv.err.datatypes.SSErr;
import sss.serv.err.datatypes.SSErrE;

public class SSDBSQLFct extends SSDBFct{

  protected        final SSDBSQLI dbSQL;
  protected static final String   circleTable                         = "circle";
  protected static final String   thumbnailsTable                     = "thumbnails";
  protected static final String   filesTable                          = "files";
  protected static final String   circleUsersTable                    = "circleusers";
  protected static final String   circleEntitiesTable                 = "circleentities";
  protected static final String   collTable                           = "coll";
  protected static final String   collRootTable                       = "collroot";
  protected static final String   collSpecialTable                    = "collspecial";
  protected static final String   collEntryPosTable                   = "collentrypos";
  protected static final String   collHierarchyTable                  = "collhierarchy";
  protected static final String   collUserTable                       = "colluser";
  protected static final String   entityTable                         = "entity";
  protected static final String   entityReadsTable                    = "entityreads";
  protected static final String   userTable                           = "user";
  protected static final String   entitiesTable                       = "entities";
  protected static final String   flagTable                           = "flag";
  protected static final String   flagsTable                          = "flags";
  protected static final String   uesTable                            = "ues";
  protected static final String   activityTable                       = "activity";
  protected static final String   activityUsersTable                  = "activityusers";
  protected static final String   activityEntitiesTable               = "activityentities";
  protected static final String   activityContentsTable               = "activitycontents";
  protected static final String   locationTable                       = "location";
  protected static final String   entityLocationsTable                = "entitylocations";
  protected static final String   tagAssTable                         = "tagass";
  protected static final String   categoryTable                       = "category";
  protected static final String   categoryAssTable                    = "categoryass";
  protected static final String   authTable                           = "auth";
  protected static final String   discTable                           = "disc";
  protected static final String   discUserTable                       = "discuser";
  protected static final String   discEntryTable                      = "discentry";
  protected static final String   discEntriesTable                    = "discentries";
  protected static final String   evernoteNoteTable                   = "evernotenote";
  protected static final String   evernoteResourceTable               = "evernoteresource";
  protected static final String   evernoteUserTable                   = "evernoteuser";
  protected static final String   commentsTable                       = "comments";
  protected static final String   commentTable                        = "comment";
  protected static final String   messageTable                        = "message";
  protected static final String   appTable                            = "app";
  protected static final String   downloadsTable                      = "downloads";
  protected static final String   friendsTable                        = "friends";

  //learn ep 
  protected static final String   learnEpUserTable                    = "learnepuser";
  protected static final String   learnEpVersionCirclesTable          = "learnepversioncircles";
  protected static final String   learnEpVersionEntitiesTable         = "learnepversionentities";
  protected static final String   learnEpVersionTable                 = "learnepversion";
  protected static final String   learnEpVersionCurrentTable          = "learnepversioncurrent";
  protected static final String   learnEpVersionsTable                = "learnepversions";
  protected static final String   learnEpVersionTimelineStatesTable   = "learnepversiontimelinestates";
  protected static final String   learnEpTimelineStateTable           = "learneptimelinestate";
  protected static final String   learnEpCircleTable                  = "learnepcircle";
  protected static final String   learnEpTable                        = "learnep";
  protected static final String   learnEpEntityTable                  = "learnepentity";
  
  //image
  protected static final String   imageTable                          = "image";
  
  //app stack
  protected static final String   appStackLayoutTable                 = "appstacklayout";
  protected static final String   appStackLayoutTileTable             = "appstacklayouttile";
  protected static final String   appStackLayoutTilesTable            = "appstacklayouttiles";

  //video
  protected static final String   videoTable                          = "video";
  protected static final String   videoAnnotationTable                = "videoannotation";
  protected static final String   videoAnnotationsTable               = "videoannotations";
  protected static final String   userVideosTable                     = "uservideos";
  
  public SSDBSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super();
    
    this.dbSQL = dbSQL;
  }
  
  protected static void insert(
    final Map<String, String> inserts,
    final String              key,
    final Object              value) throws Exception{
    
    try{
      inserts.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void insert(
    final Map<String, String> inserts,
    final String              key,
    final Enum                value) throws Exception{
    
    try{
      inserts.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void update(
    final Map<String, String> updates,
    final String              key,
    final String              value) throws Exception{
    
    try{
      updates.put(key, value);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void update(
    final Map<String, String> updates,
    final String              key,
    final Number              value) throws Exception{
    
    try{
      updates.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
   
  protected static void update(
    final Map<String, String> updates,
    final String              key,
    final SSEntityA           value) throws Exception{
    
    try{
      updates.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void delete(
    final Map<String, String> deletes,
    final String              key,
    final SSEntityA           value) throws Exception{
    
    try{
      deletes.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void delete(
    final Map<String, String> deletes,
    final String              key,
    final Enum                value) throws Exception{
    
    try{
      deletes.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void delete(
    final Map<String, String> deletes,
    final String              key,
    final String              value) throws Exception{
    
    try{
      deletes.put(key, value);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void uniqueKey(
    final Map<String, String> uniqueKeys,
    final String              key,
    final Enum                value) throws Exception{
    
    try{
      uniqueKeys.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void uniqueKey(
    final Map<String, String> uniqueKeys,
    final String              key,
    final SSEntityA           value) throws Exception{
    
    try{
      uniqueKeys.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
   protected static void match(
     final List<String> matches, 
     final String       key) throws Exception{
     
     try{
       matches.add(key);
     }catch(Exception error){
       SSServErrReg.regErrThrow(error);
     }
   }
   
  protected static void where(
    final Map<String, String> wheres,
    final String              key,
    final Object              value) throws Exception{
    
    try{
      wheres.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void where(
    final Map<String, String> wheres,
    final String              table, 
    final String              key,
    final SSEntityA           value) throws Exception{
    
    try{
      wheres.put(table + SSStrU.dot + key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void where(
    final Map<String, String> wheres,
    final String              table, 
    final String              key,
    final String              value) throws Exception{
    
    try{
      wheres.put(table + SSStrU.dot + key, value);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void where(
    final Map<String, String> wheres,
    final String              key,
    final Enum                value) throws Exception{
    
    try{
      wheres.put(key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void where(
    final Map<String, String> wheres,
    final String              table,
    final String              key,
    final Enum                value) throws Exception{
    
    try{
      wheres.put(table + SSStrU.dot + key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void where(
    final MultivaluedMap<String, String> wheres,
    final String                         table,
    final String                         key,
    final Object                         value) throws Exception{
    
    try{
      wheres.add(table + SSStrU.dot + key, value.toString());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void table(
    final List<String> tables,
    final String       table) throws Exception{
    
    try{
      tables.add(table);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void column(
    final List<String> columns,
    final String       column) throws Exception{
    
    try{
      columns.add(column);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void column(
    final List<String> columns, 
    final String       table, 
    final String       key) throws Exception{
    
    try{
      columns.add(table + SSStrU.dot + key);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void tableCon(
    final List<String> tableCons,
    final String       table1, 
    final String       key1, 
    final String       table2, 
    final String       key2) throws Exception{
    
    try{
      tableCons.add(table1 + SSStrU.dot + key1 + SSStrU.equal + table2 + SSStrU.dot + key2);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static void checkFirstResult(
    final ResultSet result) throws Exception{
    
    try{
      
      if(!result.first()){
        throw new SSErr(SSErrE.sqlNoResultFound);
      }
      
    }catch(SSErr error){
      SSServErrReg.regErrThrow(error, false);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  protected static List<String> getStringsFromResult(
    final ResultSet result,
    final String    key) throws Exception{
    
    final List<String> strings = new ArrayList<>();
    
    while(result.next()){
      strings.add(bindingStr(result, key));
    }
    
    return strings;
  }
  
  protected static List<SSTextComment> getTextCommentsFromResult(
    final ResultSet result,
    final String    key) throws Exception{
    
    final List<SSTextComment> textComments = new ArrayList<>();
    
    while(result.next()){
      textComments.add(bindingStrToTextComment(result, key));
    }
    
    return textComments;
  }
  
  protected static List<SSUri> getURIsFromResult(
    final ResultSet result,
    final String    key) throws Exception{
    
    final List<SSUri> uris = new ArrayList<>();
    
    while(result.next()){
      uris.add(bindingStrToUri(result, key));
    }
    
    return uris;
  }
  
  protected static SSUri bindingStrToUri(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return SSUri.get(bindingStr(resultSet, binding));
  }
  
  protected static SSSpaceE bindingStrToSpace(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return SSSpaceE.get(bindingStr(resultSet, binding));
  }
  
  protected static String bindingStr(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return resultSet.getString(binding);
  }
  
  protected static SSLabel bindingStrToLabel(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return SSLabel.get(bindingStr(resultSet, binding));
  }
  
  protected static Boolean bindingStrToBoolean(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return Boolean.valueOf(bindingStr(resultSet, binding));
  }
  
  protected static SSTextComment bindingStrToTextComment(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return SSTextComment.get(bindingStr(resultSet, binding));
  }
  
  protected static SSEntityE bindingStrToEntityType(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return SSEntityE.valueOf(bindingStr(resultSet, binding));
  }
  
  protected static Double bindingStrToDouble(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return Double.parseDouble(bindingStr(resultSet, binding));
  }
  
  protected static Float bindingStrToFloat(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return Float.parseFloat(bindingStr(resultSet, binding));
  }
  
  protected static  Integer bindingStrToInteger(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return Integer.parseInt(bindingStr(resultSet, binding));
  }
  
  protected static Long bindingStrToLong(
    final ResultSet resultSet, 
    final String    binding) throws Exception{
    
    return Long.parseLong(bindingStr(resultSet, binding));
  }
}
