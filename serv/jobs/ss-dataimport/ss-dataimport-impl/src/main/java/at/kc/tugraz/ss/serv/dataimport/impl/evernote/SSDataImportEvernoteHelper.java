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
package at.kc.tugraz.ss.serv.dataimport.impl.evernote;

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportEvernotePar;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.jobs.evernote.impl.helper.SSEvernoteHelper;
import at.kc.tugraz.ss.serv.jobs.evernote.impl.helper.SSEvernoteLabelHelper;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.Date;
import java.util.List;

public class SSDataImportEvernoteHelper {
  
  private final  SSEvernoteHelper        evernoteHelper;
  private final  String                  localWorkPath;
  private        SSEvernoteInfo          evernoteInfo             = null;
  private        SSUri                   userUri                  = null;
  private        List<SharedNotebook>    sharedNotebooks          = null;
  private        List<String>            sharedNotebookGuids      = null;
//  private        long                    april01                  = new Date().getTime() - SSDateU.dayInMilliSeconds * 109;
  
  public SSDataImportEvernoteHelper(final SSDBSQLI dbSQL) throws Exception{
    this.localWorkPath   = SSCoreConf.instGet().getSsConf().getLocalWorkPath();
    this.evernoteHelper  = new SSEvernoteHelper(dbSQL);
  }
  
  public void setBasicEvernoteInfo(final SSDataImportEvernotePar par) throws Exception{
    
    if(par.authToken == null){
      par.authToken = evernoteHelper.sqlFct.getAuthToken(par.user);
    }
    
    evernoteInfo          = SSServCaller.evernoteNoteStoreGet (par.user, par.authToken);
    evernoteInfo.userName = evernoteHelper.getUserName        (evernoteInfo);
    
    this.userUri         = 
      SSServCaller.authRegisterUser(
        par.user, 
        evernoteInfo.userName,
        par.authEmail,
        "1234", 
        false,
        false);
    
    evernoteHelper.sqlFct.addUserIfNotExists(this.userUri, par.authToken);
  }
  
  public void setUSN() throws Exception{
    
    evernoteHelper.sqlFct.setUSN(
      evernoteInfo.authToken, 
      evernoteInfo.noteStoreSyncChunk.getUpdateCount());
  }
  
  public void setSharedNotebooks() throws Exception{
    
    sharedNotebooks     = SSServCaller.evernoteNotebooksSharedGet (evernoteInfo.noteStore);
    sharedNotebookGuids = evernoteHelper.getSharedNotebookGuids   (sharedNotebooks);
  }
  
  public void handleNotebooks() throws Exception{
    
    final List<Notebook> notebooks      = evernoteInfo.noteStoreSyncChunk.getNotebooks();
    SSUri                notebookUri;
    SSLabel              notebookLabel;
    
    if(notebooks == null){
      return;
    }
    
    for(Notebook notebook : notebooks){
      
      notebookUri      = evernoteHelper.uriHelper.getNormalOrSharedNotebookUri      (evernoteInfo.userName,    notebook, sharedNotebookGuids);
      notebookLabel    = SSEvernoteLabelHelper.getNormalOrSharedNotebookLabel       (notebookUri, notebook);
      
      addNotebook(
        notebookUri, 
        notebookLabel,  
        notebook.getServiceCreated());
      
      addNotebookUEs(
        notebookUri, 
        notebook);
    }
  }
  
  private void addNotebook(
    final SSUri    notebookUri,
    final SSLabel  notebookLabel,
    final Long     notebookCreationTime) throws Exception{
    
    SSServCaller.entityEntityToPrivCircleAdd(
      userUri,
      notebookUri,
      SSEntityE.evernoteNotebook,
      notebookLabel,
      null,
      notebookCreationTime,
      false);
  }
  
  private void addNotebookUEs(
    final SSUri    notebookUri,
    final Notebook notebook) throws Exception{
    
    final List<SSUE> existingCreationUEs =
      SSServCaller.uEsGet(
        userUri,
        userUri,
        notebookUri,
        SSUEE.evernoteNotebookCreate,
        null,
        null);
    
    if(existingCreationUEs.isEmpty()){
      
      SSServCaller.uEAddAtCreationTime(
        userUri,
        notebookUri,
        SSUEE.evernoteNotebookCreate,
        SSStrU.empty,
        notebook.getServiceCreated(),
        false);
    }
    
    final List<SSUE> existingUpdatingUEs =
      SSServCaller.uEsGet(
        userUri,
        userUri,
        notebookUri,
        SSUEE.evernoteNotebookUpdate,
        notebook.getServiceUpdated(),
        notebook.getServiceUpdated());
    
    if(existingUpdatingUEs.isEmpty()){
      
      SSServCaller.uEAddAtCreationTime(
        userUri,
        notebookUri,
        SSUEE.evernoteNotebookUpdate,
        SSStrU.empty,
        notebook.getServiceUpdated(),
        false);
    }
  }
  
  public void handleLinkedNotebooks() throws Exception{

    final List<LinkedNotebook> linkedNotebooks = evernoteInfo.noteStoreSyncChunk.getLinkedNotebooks();
    int                        timeCounter     = 1;
    SSUri                      notebookUri;
    Long                       creationTimeForLinkedNotebook;
    
    if(linkedNotebooks == null){
      return;
    }
    
    for(LinkedNotebook linkedNotebook : linkedNotebooks){
      
      notebookUri                   = evernoteHelper.uriHelper.getLinkedNotebookUri     (linkedNotebook);
      creationTimeForLinkedNotebook = new Date().getTime() - (SSDateU.dayInMilliSeconds * timeCounter);
      timeCounter++;
      
      addNotebook(
        notebookUri, 
        SSEvernoteLabelHelper.getLinkedNotebookLabel(
          linkedNotebook, 
          notebookUri), 
        creationTimeForLinkedNotebook);
      
      addLinkedNotebookUEs(
        notebookUri, 
        creationTimeForLinkedNotebook);
    }
  }
  
  private void addLinkedNotebookUEs(
    final SSUri notebookUri,
    final Long  creationTimeForLinkedNotebook) throws Exception {
    
    final List<SSUE> existingUEs =
      SSServCaller.uEsGet(
        userUri,
        userUri,
        notebookUri,
        SSUEE.evernoteNotebookFollow,
        creationTimeForLinkedNotebook,
        creationTimeForLinkedNotebook);
    
    if(!existingUEs.isEmpty()){
      return;
    }
    
    SSServCaller.uEAddAtCreationTime(
      userUri,
      notebookUri,
      SSUEE.evernoteNotebookFollow,
      SSStrU.empty,
      creationTimeForLinkedNotebook,
      false);
  }
  
  public void handleNotes() throws Exception{
    
    final List<Note>     notes = evernoteInfo.noteStoreSyncChunk.getNotes();
    Note                 noteWithContent;
    Notebook             notebook;
    SSUri                notebookUri;
    SSUri                noteUri;
    
    if(notes == null){
      return;
    }
    
    for(Note note : notes){
      
      noteUri          = evernoteHelper.uriHelper.getNormalOrSharedNoteUri        (evernoteInfo,           note);
      notebook         = SSServCaller.evernoteNotebookGet                         (evernoteInfo.noteStore, note.getNotebookGuid());
      noteWithContent  = SSServCaller.evernoteNoteGet                             (evernoteInfo.noteStore, note.getGuid(), true);
      notebookUri      = evernoteHelper.uriHelper.getNormalOrSharedNotebookUri    (evernoteInfo.userName,  notebook, sharedNotebookGuids);
      
      addNote(
        noteUri, 
        SSEvernoteLabelHelper.getNoteLabel(
          note,         
          noteUri), 
        note, 
        notebookUri);
      
      SSServCaller.tagsAdd(
        userUri,
        noteUri,
        SSServCaller.evernoteNoteTagNamesGet(evernoteInfo.noteStore, note.getGuid()),
        SSSpaceE.privateSpace,
        note.getUpdated(),
        false);
      
      addNoteUEs(
        note,
        noteUri);
      
      new SSDataImportEvernoteNoteContentHandler(
        userUri,
        noteWithContent,
        noteUri,
        localWorkPath).handleNoteContent();
    }
  }
  
  private void addNoteUEs(
    final Note         note,
    final SSUri        noteUri) throws Exception {
    
    final List<SSUE> existingCreationUEs =
      SSServCaller.uEsGet(
        userUri,
        userUri,
        noteUri,
        SSUEE.evernoteNoteCreate,
        null,
        null);
    
    if(existingCreationUEs.isEmpty()){
      
      SSServCaller.uEAddAtCreationTime(
        userUri,
        noteUri,
        SSUEE.evernoteNoteCreate,
        SSStrU.empty,
        note.getCreated(),
        false);
    }
    
    final List<SSUE> existingUpdateUEs =
      SSServCaller.uEsGet(
        userUri,
        userUri,
        noteUri,
        SSUEE.evernoteNoteUpdate,
        note.getUpdated(),
        note.getUpdated());
    
    if(existingUpdateUEs.isEmpty()){
      
      SSServCaller.uEAddAtCreationTime(
        userUri,
        noteUri,
        SSUEE.evernoteNoteUpdate,
        SSStrU.empty,
        note.getUpdated(),
        false);
    }

    if(note.getDeleted() != 0L){
      
      final List<SSUE> existingDeleteUEs = 
        SSServCaller.uEsGet(
          userUri,
          userUri,
          noteUri,
          SSUEE.evernoteNoteDelete,
          null,
          null);
      
      if(existingDeleteUEs.isEmpty()){
        
        SSServCaller.uEAddAtCreationTime(
          userUri,
          noteUri,
          SSUEE.evernoteNoteDelete,
          SSStrU.empty,
          note.getDeleted(),
          false);
      }
    }
    
    final NoteAttributes noteAttr = note.getAttributes();
    
    if(noteAttr == null){
      return;
    }
    
    if(noteAttr.getShareDate() != 0L){
  
      final List<SSUE> existingShareUEs =
        SSServCaller.uEsGet(
          userUri,
          userUri,
          noteUri,
          SSUEE.evernoteNoteShare,
          noteAttr.getShareDate(),
          noteAttr.getShareDate());
      
      if(existingShareUEs.isEmpty()){
        
        SSServCaller.uEAddAtCreationTime(
          userUri,
          noteUri,
          SSUEE.evernoteNoteShare,
          SSStrU.empty,
          noteAttr.getShareDate(),
          false);
      }
    }
    
    if(noteAttr.getReminderDoneTime() != 0L){
      
      final List<SSUE> existingReminderUEs = 
        SSServCaller.uEsGet(
          userUri,
          userUri,
          noteUri,
          SSUEE.evernoteReminderDone,
          noteAttr.getReminderDoneTime(),
          noteAttr.getReminderDoneTime());
      
      if(existingReminderUEs.isEmpty()){
       
        SSServCaller.uEAddAtCreationTime(
          userUri,
          noteUri,
          SSUEE.evernoteReminderDone,
          SSStrU.empty,
          noteAttr.getReminderDoneTime(),
          false);
      }
    }
    
    if(noteAttr.getReminderTime() != 0L){
      
      final List<SSUE> existingReminder2UEs = 
        SSServCaller.uEsGet(
          userUri,
          userUri,
          noteUri,
          SSUEE.evernoteReminderCreate,
          noteAttr.getReminderTime(),
          noteAttr.getReminderTime());
      
      if(existingReminder2UEs.isEmpty()){
        
        SSServCaller.uEAddAtCreationTime(
          userUri,
          noteUri,
          SSUEE.evernoteReminderCreate,
          SSStrU.empty,
          noteAttr.getReminderTime(),
          false);
      }
    }
  }
  
  private void addNote(
    final SSUri   noteUri,
    final SSLabel noteLabel,
    final Note    note,
    final SSUri   notebookUri) throws Exception{
    
    SSServCaller.entityEntityToPrivCircleAdd(
      userUri, 
      noteUri, 
      SSEntityE.evernoteNote, 
      noteLabel, 
      null,
      note.getCreated(),
      false);
    
    evernoteHelper.sqlFct.addNoteIfNotExists (notebookUri, noteUri);
  }
  
  public void handleResources() throws Exception{
    
    final List<Resource> resources = evernoteInfo.noteStoreSyncChunk.getResources();
    Resource             resourceWithContent;
    SSUri                resourceUri;
    Note                 note;
    SSUri                noteUri;
    
    if(resources == null){
      return;
    }
    
    for(Resource resource : resources){

      resourceWithContent = SSServCaller.evernoteResourceGet                  (evernoteInfo.noteStore, resource.getGuid(), true);
      resourceUri         = evernoteHelper.uriHelper.getResourceUri           (evernoteInfo, resource);
      note                = SSServCaller.evernoteNoteGet                      (evernoteInfo.noteStore, resource.getNoteGuid(), false);
      noteUri             = evernoteHelper.uriHelper.getNormalOrSharedNoteUri (evernoteInfo, note);
      
      addResource(
        resourceUri,
        SSEvernoteLabelHelper.getResourceLabel(
          resource,
          resourceUri),
        note.getUpdated(),
        noteUri);
      
      new SSDataImportEvernoteResourceContentHandler(
        userUri,
        resourceWithContent,
        resourceUri,
        localWorkPath).handleResourceContent();
      
      addResourceUEs(
        resourceUri, 
        note.getUpdated());
    }
  }
  
  private void addResourceUEs(
    final SSUri resourceUri,
    final Long  resourceAddTime) throws Exception{
    
    final List<SSUE> existingUEs =
      SSServCaller.uEsGet(
        userUri,
        userUri,
        resourceUri,
        SSUEE.evernoteResourceAdd,
        resourceAddTime,
        resourceAddTime);
    
    if(existingUEs.isEmpty()){
      
      SSServCaller.uEAddAtCreationTime(
        userUri,
        resourceUri,
        SSUEE.evernoteResourceAdd,
        SSStrU.empty,
        resourceAddTime,
        false);
    }
  }

  private void addResource(
    final SSUri   resourceUri,
    final SSLabel resourceLabel,
    final Long    resourceAddTime,
    final SSUri   noteUri) throws Exception{
    
    SSServCaller.entityEntityToPrivCircleAdd(
      userUri, 
      resourceUri, 
      SSEntityE.evernoteResource, 
      resourceLabel, 
      null, 
      resourceAddTime,
      false);
    
    evernoteHelper.sqlFct.addResourceIfNotExists(
      noteUri, 
      resourceUri);
  }
}