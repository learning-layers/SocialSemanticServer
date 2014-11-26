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
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportEvernotePar;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSErrForClient;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.jobs.evernote.impl.helper.SSEvernoteHelper;
import at.kc.tugraz.ss.serv.jobs.evernote.impl.helper.SSEvernoteLabelHelper;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import com.evernote.edam.error.EDAMErrorCode;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SSDataImportEvernoteHelper {
  
  private final  SSEvernoteHelper        evernoteHelper;
  private final  String                  localWorkPath;
  private        SSEvernoteInfo          evernoteInfo             = null;
//  private        SSLabel                 userName                 = null;
  private        SSUri                   userUri                  = null;
  private        List<SharedNotebook>    sharedNotebooks          = null;
  private        List<String>            sharedNotebookGuids      = null;
  private        long                    april01                  = new Date().getTime() - SSDateU.dayInMilliSeconds * 109;
  
  public SSDataImportEvernoteHelper(final SSDBSQLI dbSQL) throws Exception{
    
    this.localWorkPath   = SSCoreConf.instGet().getSsConf().getLocalWorkPath();
    this.evernoteHelper  = new SSEvernoteHelper(dbSQL);
  }
  
  public SSLabel setBasicEvernoteInfo(final SSDataImportEvernotePar par) throws Exception{
    
    if(par.authToken == null){
      par.authToken = evernoteHelper.sqlFct.getAuthToken(par.user);
    }
    
    this.evernoteInfo    = getNoteStore(par);
    
    final SSLabel userName = evernoteHelper.getUserName        (evernoteInfo);
    
    this.userUri         = 
      SSServCaller.authRegisterUser(
        par.user, 
        userName,
        par.authEmail, //userName + SSVoc.systemEmailPostFix, 
        "1234", 
        false,
        false);
    
    evernoteHelper.sqlFct.addUserIfNotExists(this.userUri, par.authToken);
    
    return userName;
//    this.userCircle      = SSServCaller.entityCircleURIPrivGet(userUri);
  }
  
  public void setSharedNotebooks() throws Exception{
    sharedNotebooks     = SSServCaller.evernoteNotebooksSharedGet (evernoteInfo.noteStore);
    sharedNotebookGuids = evernoteHelper.getSharedNotebookGuids   (sharedNotebooks);
  }
  
  public void handleNotebooks(
    final SSDataImportEvernotePar par,
    final SSLabel                 userName) throws Exception{
    
    SSUri                notebookUri;
    SSLabel              notebookLabel;
    
    for(Notebook notebook : getNotebooks()){
      
      notebookUri      = evernoteHelper.uriHelper.getNormalOrSharedNotebookUri      (userName,    notebook, sharedNotebookGuids);
//      isSharedNotebook = evernoteHelper.isSharedNootebook                           (notebookUri, userName, notebook);
      notebookLabel    = SSEvernoteLabelHelper.getNormalOrSharedNotebookLabel       (notebookUri, notebook);
      
      addNotebook(
        notebookUri, 
        notebookLabel,  
        notebook.getServiceCreated());
      
      addNotebookUEs(
        notebookUri, 
        notebook);
      
      handleNotes(
        notebookUri,
        notebookLabel,
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

    int                        timeCounter     = 1;
    SSUri                      notebookUri;
    Long                       creationTimeForLinkedNotebook;
    
    for(LinkedNotebook linkedNotebook : getLinkedNotebooks()){
      
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
      
//      for(Note note : SSServCaller.getEvernoteLinkedNotes(evernoteInfo.noteStore, linkedNotebook)){
//        
//        noteUri   = evernoteHelper.uriHelper.getLinkedNoteUri     (linkedNotebook, note);
//        noteLabel = evernoteHelper.labelHelper.getLinkedNoteLabel (note, noteUri);
//        
//        SSServCaller.addEntityAtCreationTime(
//          userUri,
//          noteUri,
//          noteLabel,
//          creationTimeForLinkedNotebook,
//          SSEntityEnum.evernoteNote);
//        
//        SSServCaller.uEAddAtCreationTime(
//          userUri,
//          noteUri,
//          SSUEEnum.evernoteNoteFollow,
//          SSStrU.empty,
//          creationTimeForLinkedNotebook,
//          shouldCommit);
//        
//        if(note.getResources() != null){
//          
//          for(Resource resource : note.getResources()){
//            
//            resourceUri   = SSUri.get     (linkedNotebook.getWebApiUrlPrefix() + "share/" + linkedNotebook.getShareKey() + "/res/" + resource.getGuid());
//            resourceLabel = SSLabelStr.get(SSStrU.toStr(resourceUri));
//            
//            SSServCaller.addEntityAtCreationTime(
//              userUri,
//              resourceUri,
//              resourceLabel,
//              note.getUpdated(),
//              SSEntityEnum.evernoteResource);
//            
//            SSServCaller.uEAddAtCreationTime(
//              userUri,
//              resourceUri,
//              SSUEEnum.evernoteResourceFollow,
//              SSStrU.empty,
//              creationTimeForLinkedNotebook,
//              shouldCommit);
//          }
//        }
//      }
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
  
  private void handleNotes(
    final SSUri                notebookUri, 
    final SSLabel              notebookLabel,
    final Notebook             notebook) throws Exception{
    
    final List<String> noteTags = new ArrayList<>();
    SSUri              noteUri;
    
    for(Note note : getNotes(notebook)){
      
//      if(note.getUpdated() < april01){
//        continue;
//      }
      
      noteUri =
        evernoteHelper.uriHelper.getNormalOrSharedNoteUri(
          evernoteInfo,
          note);
      
      addNote(
        noteUri, 
        SSEvernoteLabelHelper.getNoteLabel(
          note,         
          noteUri), 
        note, 
        notebookUri);
      
      noteTags.clear  ();
      
      noteTags.addAll (
        SSStrU.distinctWithoutEmptyAndNull(
          getNoteTags(note)));
      
      addNoteTags(
        note, 
        noteUri,
        notebookLabel,
        noteTags);
      
      addNoteAndTagUEs(
        note,
        noteUri,
        noteTags);
      
      new SSDataImportEvernoteNoteContentHandler(
        userUri,
        note,
        noteUri,
        localWorkPath).handleNoteContent();
      
      handleResources(
        noteUri,
        notebookLabel,
        note);
    }
  }
  
  private void addNoteAndTagUEs(
    final Note         note,
    final SSUri        noteUri,
    final List<String> noteTags) throws Exception {
    
    Boolean tagExists;
    
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
    
    final List<SSUE> existingTagUEs = new ArrayList<>();
    
    for(String tag : noteTags){
      
      existingTagUEs.clear();
      
      existingTagUEs.addAll(
        SSServCaller.uEsGet(
          userUri,
          userUri,
          noteUri,
          SSUEE.addPrivateTag,
          null,
          null));

      tagExists = false;
      
      for(SSUE ue : existingTagUEs){
        
        if(SSStrU.equals(ue.content, tag)){
          tagExists = true;
          break;
        }
      }
      
      if(!tagExists){
      
        SSServCaller.uEAddAtCreationTime(
          userUri,
          noteUri,
          SSUEE.addPrivateTag,
          tag,
          note.getUpdated(),
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
  
  private void addNoteTags(
    final Note         note, 
    final SSUri        noteUri,
    final SSLabel      notebookLabel,
    final List<String> noteTags) throws Exception{
    
    SSServCaller.tagsAdd(
      userUri,
      noteUri,
      noteTags,
      SSSpaceE.privateSpace,
      note.getUpdated(),
      false);
    
    if(!SSStrU.isEmpty(notebookLabel)){
      
      SSServCaller.tagAdd(
        userUri,
        noteUri,
        SSStrU.toStr(notebookLabel),
        SSSpaceE.privateSpace,
        null,
        false);
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
  
  private void handleResources(
    final SSUri                noteUri,
    final SSLabel              notebookLabel,
    final Note                 note) throws Exception{
    
    SSUri resourceUri;
    
    if(note.getResources() == null){
      return;
    }
    
    for(Resource resource : note.getResources()){

      resourceUri =
        evernoteHelper.uriHelper.getResourceUri(
          evernoteInfo,
          resource);
      
      addResource(
        resourceUri,
        SSEvernoteLabelHelper.getResourceLabel(
          resource,
          resourceUri),
        note.getUpdated(),
        noteUri);
      
      new SSDataImportEvernoteResourceContentHandler(
        userUri,
        resource,
        resourceUri,
        localWorkPath).handleResourceContent();
      
      addResourceTags(
        resourceUri,
        notebookLabel);
      
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
  
  private void addResourceTags(
    final SSUri   resourceUri,
    final SSLabel notebookLabel) throws Exception{
    
    if(!SSStrU.isEmpty(notebookLabel)){
      
      SSServCaller.tagAdd(
        userUri,
        resourceUri,
        SSStrU.toStr(notebookLabel),
        SSSpaceE.privateSpace,
        null,
        false);
    }    
  }
  
  
  private List<Notebook> getNotebooks() throws Exception{
    
    try{
      return SSServCaller.evernoteNotebooksGet(evernoteInfo.noteStore);
      
    }catch(Exception error){
      
      try{
        
        for(SSErrForClient serviceImplError : SSServErrReg.getServiceImplErrors()){
          
          if(
            serviceImplError.exception instanceof EDAMSystemException &&
            ((EDAMSystemException)serviceImplError.exception).getErrorCode().compareTo(EDAMErrorCode.RATE_LIMIT_REACHED) == 0){
            
            SSServErrReg.reset();
            
            SSLogU.info("import goes to sleep for " + ((EDAMSystemException)serviceImplError.exception).getRateLimitDuration() + " seconds for RATE EXCEPTION");
            
            Thread.sleep(((EDAMSystemException)serviceImplError.exception).getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
            
            return SSServCaller.evernoteNotebooksGet(evernoteInfo.noteStore);
          }
        }
        
        SSServErrReg.regErrThrow(error);
        return null;
        
      }catch(Exception error1){
        SSServErrReg.regErrThrow(error1);
        return null;
      }
    }
  }
  
  private List<Note> getNotes(
    final Notebook notebook) throws Exception{
    
    try{
      return SSServCaller.evernoteNotesGet(evernoteInfo.noteStore, notebook.getGuid());
      
    }catch(Exception error){
      
      try{
        
        for(SSErrForClient serviceImplError : SSServErrReg.getServiceImplErrors()){
          
          if(
            serviceImplError.exception instanceof EDAMSystemException &&
            ((EDAMSystemException)serviceImplError.exception).getErrorCode().compareTo(EDAMErrorCode.RATE_LIMIT_REACHED) == 0){
            
            SSServErrReg.reset();
            
            SSLogU.info("import goes to sleep for " + ((EDAMSystemException)serviceImplError.exception).getRateLimitDuration() + " seconds for RATE EXCEPTION");
            
            Thread.sleep(((EDAMSystemException)serviceImplError.exception).getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
            
            return SSServCaller.evernoteNotesGet(evernoteInfo.noteStore, notebook.getGuid());
          }
        }
        
        SSServErrReg.regErrThrow(error);
        return null;
        
      }catch(Exception error1){
        SSServErrReg.regErrThrow(error1);
        return null;
      }
    }
  }

  private List<LinkedNotebook> getLinkedNotebooks() throws Exception{
    
    try{
      return SSServCaller.evernoteNotebooksLinkedGet(evernoteInfo.noteStore);
      
    }catch(Exception error){
      
      try{
        
        for(SSErrForClient serviceImplError : SSServErrReg.getServiceImplErrors()){
          
          if(
            serviceImplError.exception instanceof EDAMSystemException &&
            ((EDAMSystemException)serviceImplError.exception).getErrorCode().compareTo(EDAMErrorCode.RATE_LIMIT_REACHED) == 0){
            
            SSServErrReg.reset();
            
            SSLogU.info("import goes to sleep for " + ((EDAMSystemException)serviceImplError.exception).getRateLimitDuration() + " seconds for RATE EXCEPTION");
            
            Thread.sleep(((EDAMSystemException)serviceImplError.exception).getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
            
            return SSServCaller.evernoteNotebooksLinkedGet(evernoteInfo.noteStore);
          }
        }
        
        SSServErrReg.regErrThrow(error);
        return null;
        
      }catch(Exception error1){
        SSServErrReg.regErrThrow(error1);
        return null;
      }
    }
  }

  private List<String> getNoteTags(
    final Note note) throws Exception{
    
    try{
      return evernoteInfo.noteStore.getNoteTagNames(note.getGuid());
      
    }catch(Exception error){
      
      try{
        
        if(
          error instanceof EDAMSystemException &&
          ((EDAMSystemException)error).getErrorCode().compareTo(EDAMErrorCode.RATE_LIMIT_REACHED) == 0){
          
          SSServErrReg.reset();
          
          SSLogU.info("import goes to sleep for " + ((EDAMSystemException)error).getRateLimitDuration() + " seconds for RATE EXCEPTION");
          
          Thread.sleep(((EDAMSystemException)error).getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
          
          return evernoteInfo.noteStore.getNoteTagNames(note.getGuid());
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }catch(Exception error1){
        SSServErrReg.regErrThrow(error1);
        return null;
      }
    }
  }

  private SSEvernoteInfo getNoteStore(
    final SSDataImportEvernotePar par) throws Exception{
   
    try{
      return SSServCaller.evernoteNoteStoreGet (par.user, par.authToken);
      
    }catch(Exception error){
      
      try{
        
        for(SSErrForClient serviceImplError : SSServErrReg.getServiceImplErrors()){
          
          if(
            serviceImplError.exception instanceof EDAMSystemException &&
            ((EDAMSystemException)serviceImplError.exception).getErrorCode().compareTo(EDAMErrorCode.RATE_LIMIT_REACHED) == 0){
            
            SSServErrReg.reset();
            
            SSLogU.info("import goes to sleep for " + ((EDAMSystemException)serviceImplError.exception).getRateLimitDuration() + " seconds for RATE EXCEPTION");
            
            Thread.sleep(((EDAMSystemException)serviceImplError.exception).getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
            
            return SSServCaller.evernoteNoteStoreGet (par.user, par.authToken);
          }
        }

        SSServErrReg.regErrThrow(error);
        return null;
        
      }catch(Exception error1){
        SSServErrReg.regErrThrow(error1);
        return null;
      }
    }
  }
}