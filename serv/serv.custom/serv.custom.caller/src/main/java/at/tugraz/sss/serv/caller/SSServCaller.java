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
package at.tugraz.sss.serv.caller;

import at.kc.tugraz.socialserver.service.broadcast.datatypes.SSBroadcast;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.enums.SSBroadcastEnum;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSIDU;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSVarNames;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSServCaller {

  /* evernote */ 
  
  public static Resource evernoteResourceByHashGet(
    final SSUri           user,
    final NoteStoreClient noteStore,
    final String          noteGUID, 
    final String          resourceHash) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.noteStore,      noteStore);
    opPars.put(SSVarNames.noteGUID,       noteGUID);
    opPars.put(SSVarNames.resourceHash,   resourceHash);
    
    return (Resource) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteResourceByHashGet, opPars));
  }
    
  public static Boolean evernoteResourceAdd(
    final SSUri    user,
    final SSUri    note, 
    final SSUri    resource,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.note,           note);
    opPars.put(SSVarNames.resource,       resource);
    opPars.put(SSVarNames.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteResourceAdd, opPars));
  }
  
  public static Boolean evernoteUSNSet(
    final SSUri    user,
    final String   authToken, 
    final Integer  usn,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.authToken,      authToken);
    opPars.put(SSVarNames.usn,            usn);
    opPars.put(SSVarNames.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteUSNSet, opPars));
  }
    
  public static Boolean evernoteNoteAdd(
    final SSUri    user,
    final SSUri    notebook, 
    final SSUri    note,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.notebook,       notebook);
    opPars.put(SSVarNames.note,           note);
    opPars.put(SSVarNames.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNoteAdd, opPars));
  }
    
  public static Boolean evernoteUserAdd(
    final SSUri    user, 
    final String   authToken,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.authToken,      authToken);
    opPars.put(SSVarNames.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteUserAdd, opPars));
  }
  
  public static Resource evernoteResourceGet(
    final NoteStoreClient noteStore, 
    final String          resourceGUID,
    final Boolean         includeContent) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.noteStore,        noteStore);
    opPars.put(SSVarNames.resourceGUID,     resourceGUID);
    opPars.put(SSVarNames.includeContent,   includeContent);
    
    return (Resource) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteResourceGet, opPars));
  }
  
  public static List<SharedNotebook> evernoteNotebooksSharedGet(
    final NoteStoreClient noteStore) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();

    opPars.put(SSVarNames.noteStore,     noteStore);
      
    return (List<SharedNotebook>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNotebooksSharedGet, opPars));
  }
  
   public static Notebook evernoteNotebookGet(
    final NoteStoreClient noteStore,
    final String          notebookGUID) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.noteStore,     noteStore);
    opPars.put(SSVarNames.notebookGUID,  notebookGUID);
    
    return (Notebook) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNotebookGet, opPars));
  }
  
  public static Note evernoteNoteGet(
    final NoteStoreClient noteStore,
    final String          noteGUID,
    final Boolean         includeContent) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.noteStore,       noteStore);
    opPars.put(SSVarNames.noteGUID,        noteGUID);
    opPars.put(SSVarNames.includeContent,  includeContent);
    
    return (Note) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNoteGet, opPars));
  }
  
   public static List<String> evernoteNoteTagNamesGet(
    final NoteStoreClient noteStore, 
    final String          noteGUID) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.noteStore,      noteStore);
    opPars.put(SSVarNames.noteGUID,       noteGUID);
    
    return (List<String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNoteTagNamesGet, opPars));
  }
   
  public static Boolean broadcastAdd(
    final SSUri           user,
    final SSUri           entity,
    final SSBroadcastEnum type,
    final Object          content) throws Exception{
    
    final  Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,         user);
    opPars.put(SSVarNames.entity,       entity);
    opPars.put(SSVarNames.type,         type);
    opPars.put(SSVarNames.content,      content);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.broadcastAdd, opPars));
  }
  
  public static void broadcastUpdate() throws Exception{
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.broadcastUpdate, new HashMap<>()));
  } 
  
  public static List<SSBroadcast> broadcastsGet(
    final SSUri           user) throws Exception{
    
    final  Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,         user);
    
    return (List<SSBroadcast>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.broadcastsGet, opPars));
  }

  private static SSUri vocURIPrefixGet() throws Exception{
    return (SSUri) SSUri.get(SSVocConf.sssUri);
  }
  
  public static SSUri vocURICreate() throws Exception{
    return SSUri.get(vocURIPrefixGet() + SSIDU.uniqueID());
  }
  
  public static SSUri vocURICreateFromId(final String id) throws Exception{
    return SSUri.get(vocURIPrefixGet() + id);
  }
  
  public static SSUri vocURICreate(final SSFileExtE fileExt) throws Exception{
    return SSUri.get(vocURIPrefixGet() + SSIDU.uniqueID() + SSStrU.dot + fileExt.toString());
  }
  
  /* entity */
  
  public static List<SSUri> entityUserParentEntitiesGet(
    final SSUri      user, 
    final SSUri      entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.entity,    entity);
    
    return (List<SSUri>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entityUserParentEntitiesGet, opPars));
  }
  
  public static List<SSUri> entityUserSubEntitiesGet(
    final SSUri      user, 
    final SSUri      entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.entity,    entity);
    
    return (List<SSUri>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entityUserSubEntitiesGet, opPars));
  }
  
  public static void entityRemove(
    final SSUri   entity, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.entity,       entity);
    opPars.put(SSVarNames.shouldCommit, shouldCommit);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entityRemove, opPars));
  }
  
  /* modeling user event */

  public static void modelUEUpdate() throws Exception{
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.modelUEUpdate, new HashMap<>()));
  }
  
  /* data export */
  
  public static void dataExportUserRelations(
    final SSUri                     user) throws Exception {
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,                  user);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataExportUserRelations, opPars));
  }
  
  public static void dataExportUsersEntitiesTagsCategoriesTimestampsFile(
    final SSUri                     user,
    final List<SSUri>               users,
    final Boolean                   exportTags,
    final Boolean                   exportCategories,
    final String                    fileName) throws Exception {
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.fileName,              fileName);
    opPars.put(SSVarNames.users,                 users);
    opPars.put(SSVarNames.exportTags,            exportTags);
    opPars.put(SSVarNames.exportCategories,      exportCategories);
    opPars.put(SSVarNames.user,                  user);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataExportUsersEntitiesTagsCategoriesTimestampsFile, opPars));
  }
  
  public static void dataExportUserEntityTagsCategoriesTimestampsLine(
    final SSUri        user,
    final SSUri        forUser,
    final SSUri        entity,
    final List<String> tags,
    final List<String> categories,
    final String       fileName) throws Exception {
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,          user);
    opPars.put(SSVarNames.forUser,       forUser);
    opPars.put(SSVarNames.entity,        entity);
    opPars.put(SSVarNames.tags,          tags);
    opPars.put(SSVarNames.categories,    categories);
    opPars.put(SSVarNames.fileName,      fileName);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataExportUserEntityTagsCategoriesTimestampsLine, opPars));
  }
  
  /* data import */
  
  public static void dataImportAchso(
    final SSUri   user, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit,     shouldCommit);
    opPars.put(SSVarNames.user,             user);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataImportAchso, opPars));
  }
  
  public static Map<String, String> dataImportSSSUsersFromCSVFile( 
    final String fileName) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.fileName, fileName);
    
    return (Map<String, String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataImportSSSUsersFromCSVFile, opPars));
  }
  
  public static void dataImportMediaWikiUser(
    final SSUri   user,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,        user);
    opPars.put(SSVarNames.shouldCommit, shouldCommit);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataImportMediaWikiUser, opPars));
  }
  
  /* auth */
  
  public static void authUsersFromCSVFileAdd(
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit,      shouldCommit);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.authUsersFromCSVFileAdd, opPars));
  }
    
  /* i5Cloud */
  
  public static List<String> i5CloudAchsoSemanticAnnotationsSetGet(
    final List<String> ids) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.ids,   ids);
    
    return (List<String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.i5CloudAchsoSemanticAnnotationsSetGet, opPars));
  }
    
  public static String i5CloudAchsoVideoInformationGet() throws Exception{
    return (String) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.i5CloudAchsoVideoInformationGet, new HashMap<>()));
  }
    
  public static Map<String, String> i5CloudAuth() throws Exception{
    return (Map<String, String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.i5CloudAuth, new HashMap<>()));
  }
  
  public static void recommLoadUserRealms(
    final SSUri user) throws Exception{
    
    final Map<String, Object>  opPars           = new HashMap<>();
    
    opPars.put(SSVarNames.user,             user);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.recommLoadUserRealms, opPars));
  }
}