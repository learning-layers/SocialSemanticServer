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
package at.kc.tugraz.ss.serv.dataimport.impl.bitsandpieces;

import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSLinkU;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.auth.api.SSAuthServerI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSToolContextE;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportBitsAndPiecesPar;
import at.kc.tugraz.ss.serv.dataimport.impl.evernote.SSDataImportEvernoteNoteContentHandler;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthRegisterUserPar;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.kc.tugraz.ss.service.userevent.api.SSUEServerI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsGetPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSDataImportBitsAndPiecesEvernoteImporter {
  
  private static final ReentrantReadWriteLock  currentlyRunEvernoteImportsLock  = new ReentrantReadWriteLock();
  private static final Map<Thread, String>     currentlyRunEvernoteImports      = new HashMap<>();
  
  private final  SSFileRepoServerI       fileServ;
  private final  String                  localWorkPath;
  private final  List<String>            sharedNotebookGuids      = new ArrayList<>();
  private        SSEvernoteInfo          evernoteInfo             = null;
  private        SSUri                   userUri                  = null;
  private        List<SharedNotebook>    sharedNotebooks          = null;
  
  public SSDataImportBitsAndPiecesEvernoteImporter() throws Exception{
    this.localWorkPath   = SSCoreConf.instGet().getSss().getLocalWorkPath();
    this.fileServ        = (SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class);
  }
  
  public void handle(final SSDataImportBitsAndPiecesPar par) throws Exception{
    
    try{
  
      if(!addCurrentlyRunEvernoteImport(par.authToken, par.authEmail)){
        return;
      }
      
      SSLogU.info("start B&P evernote import for " +  par.authEmail);
      
      setBasicEvernoteInfo  (par);
      
      handleLinkedNotebooks ();
      setSharedNotebooks    ();
      handleNotebooks       ();
      handleNotes           ();
      handleResources       ();
      
      setUSN();
      
      SSLogU.info("end B&P evernote import for evernote account " + par.authEmail);
      
    }catch(Exception error){
      SSLogU.err("B&P evernote import failed for " + par.authEmail);
      SSServErrReg.regErrThrow(error);
    }finally{
      
      try{
        removeCurrentlyRunEvernoteImport(par.authToken);
      }catch(Exception error){
        SSLogU.warn("removing evernote import thread failed");
      }
    }
  }
  
  private void setBasicEvernoteInfo(final SSDataImportBitsAndPiecesPar par) throws Exception{
    
    final SSAuthServerI authServ = (SSAuthServerI) SSServReg.getServ(SSAuthServerI.class);
    
    if(par.authToken == null){
      par.authToken = SSServCaller.evernoteUsersAuthTokenGet(par.user);
    }
    
    evernoteInfo          = SSServCaller.evernoteNoteStoreGet (par.user, par.authToken);
    evernoteInfo.userName = SSLabel.get(evernoteInfo.userStore.getUser().getUsername());
    
    this.userUri         =
      authServ.authRegisterUser(
        new SSAuthRegisterUserPar(
          par.authEmail, //email
          "1234", //password
          SSLabel.get(par.authEmail),//evernoteInfo.userName,
          false, //updatePassword,
          false, //isSystemUser,
          false, //withUserRestriction,
          false)); //shouldCommit
    
    SSServCaller.evernoteUserAdd(
      this.userUri,
      par.authToken,
      false);
  }
  
  private void setUSN() throws Exception{
    
    final Integer usn;
    
    if(evernoteInfo.noteStoreSyncChunk.isSetChunkHighUSN()){
      usn = evernoteInfo.noteStoreSyncChunk.getChunkHighUSN();
    }else{
      if(evernoteInfo.noteStoreSyncChunk.isSetUpdateCount()){
        usn = evernoteInfo.noteStoreSyncChunk.getUpdateCount();
      }else{
        SSLogU.warn("couldnt set USN as Evernote didnt provide one");
        usn = 0;
      }
    }
      
    SSServCaller.evernoteUSNSet(
      this.userUri,
      evernoteInfo.authToken,
      usn,
      false);
  }
  
  private void setSharedNotebooks() throws Exception{
    
    sharedNotebooks     = SSServCaller.evernoteNotebooksSharedGet (evernoteInfo.noteStore);
    
    sharedNotebookGuids.clear();

    if(sharedNotebooks == null){
      return;
    }
    
    sharedNotebooks.stream().forEach((sharedNotebook)->{
      sharedNotebookGuids.add(sharedNotebook.getNotebookGuid());
    });
  }
  
  private void handleNotebooks() throws Exception{
    
    final List<Notebook> notebooks      = evernoteInfo.noteStoreSyncChunk.getNotebooks();
    SSUri                notebookUri;
    SSLabel              notebookLabel;
    
    if(notebooks == null){
      return;
    }
    
    for(Notebook notebook : notebooks){
      
      notebookUri      = getNormalOrSharedNotebookUri         (evernoteInfo.userName,    notebook, sharedNotebookGuids);
      notebookLabel    = getNormalOrSharedNotebookLabel       (notebook);
      
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
    
    ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
      new SSEntityUpdatePar(
        userUri,
        notebookUri,
        SSEntityE.evernoteNotebook, //type,
        notebookLabel, //label
        null, //description,
        notebookCreationTime, //creationTime,
        null, //read,
        false, //setPublic
        true, //withUserRestriction
        false)); //shouldCommit)
    
    ((SSEvalServerI) SSServReg.getServ(SSEvalServerI.class)).evalLog(
      new SSEvalLogPar(
        userUri, 
        SSToolContextE.evernoteImport, 
        SSEvalLogE.addNotebook, 
        notebookUri, 
        null,  //content
        null, //entities
        null,  //users
        false)); //shouldCommit
  }
  
  private void addNotebookUEs(
    final SSUri    notebookUri,
    final Notebook notebook) throws Exception{
    
    final List<SSEntity> existingCreationUEs =
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventsGet(
        new SSUEsGetPar(
          userUri, //user 
          userUri, //forUser
          notebookUri, //entity
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNotebookCreate), //types
          null, //startTime,
          null, //endTime, 
          true, //withUserRestriction, 
          false)); //invokeEntityHandlers
    
    if(existingCreationUEs.isEmpty()){
      
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventAdd(
        new SSUEAddPar(
          userUri,
          notebookUri,
          SSUEE.evernoteNotebookCreate,
          SSStrU.empty,
          notebook.getServiceCreated(),
          true, 
          false)); //shouldCommit
    }
    
    final List<SSEntity> existingUpdatingUEs =
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventsGet(
        new SSUEsGetPar(
          userUri, //user
          userUri, //forUser
          notebookUri, //entity
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNotebookUpdate), //types
          notebook.getServiceUpdated(),
          notebook.getServiceUpdated(),
          true, //withUserRestriction,
          false)); //invokeEntityHandlers
    
    if(existingUpdatingUEs.isEmpty()){
      
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventAdd(
        new SSUEAddPar(
          userUri,
          notebookUri,
          SSUEE.evernoteNotebookUpdate,
          SSStrU.empty,
          notebook.getServiceUpdated(),
          true,
          false)); //shouldCommit
    }
  }
  
  private void handleLinkedNotebooks() throws Exception{
    
    final List<LinkedNotebook> linkedNotebooks = evernoteInfo.noteStoreSyncChunk.getLinkedNotebooks();
    int                        timeCounter     = 1;
    SSUri                      notebookUri;
    Long                       creationTimeForLinkedNotebook;
    
    if(linkedNotebooks == null){
      return;
    }
    
    for(LinkedNotebook linkedNotebook : linkedNotebooks){
      
      notebookUri                   = getLinkedNotebookUri     (linkedNotebook);
      creationTimeForLinkedNotebook = new Date().getTime() - (SSDateU.dayInMilliSeconds * timeCounter);
      timeCounter++;
      
      addNotebook(
        notebookUri,
        getLinkedNotebookLabel(
          linkedNotebook),
        creationTimeForLinkedNotebook);
      
      addLinkedNotebookUEs(
        notebookUri,
        creationTimeForLinkedNotebook);
    }
  }
  
  private void addLinkedNotebookUEs(
    final SSUri notebookUri,
    final Long  creationTimeForLinkedNotebook) throws Exception {
    
    final List<SSEntity> existingUEs =
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventsGet(
        new SSUEsGetPar(
          userUri, //user
          userUri, //forUser
          notebookUri, //entity
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNotebookFollow), //types
          creationTimeForLinkedNotebook,
          creationTimeForLinkedNotebook,
          true, //withUserRestriction,
          false)); //invokeEntityHandlers
    
    if(!existingUEs.isEmpty()){
      return;
    }
    
    ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventAdd(
      new SSUEAddPar(
        userUri,
        notebookUri,
        SSUEE.evernoteNotebookFollow,
        SSStrU.empty,
        creationTimeForLinkedNotebook,
        true,
        false)); //shouldCommit
  }
  
  private void handleNotes() throws Exception{
    
    final List<Note>     notes = evernoteInfo.noteStoreSyncChunk.getNotes();
    Note                 noteWithContent;
    Notebook             notebook;
    SSUri                notebookUri;
    SSUri                noteUri;
    List<String>         noteTagNames;
    
    if(notes == null){
      return;
    }
    
    for(Note note : notes){
      
      noteUri          = getNormalOrSharedNoteUri        (evernoteInfo,           note);
      notebook         = SSServCaller.evernoteNotebookGet                         (evernoteInfo.noteStore, note.getNotebookGuid());
      noteWithContent  = SSServCaller.evernoteNoteGet                             (evernoteInfo.noteStore, note.getGuid(), true);
      notebookUri      = getNormalOrSharedNotebookUri    (evernoteInfo.userName,  notebook, sharedNotebookGuids);
      
      addNote(
        noteUri,
        getNoteLabel(
          note),
        note,
        notebookUri);
      
      noteTagNames = SSServCaller.evernoteNoteTagNamesGet(evernoteInfo.noteStore, note.getGuid());
      
      ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagsAdd(
        new SSTagsAddPar(
          userUri,
          SSTagLabel.get(noteTagNames), //labels
          SSUri.asListWithoutNullAndEmpty(noteUri), //entities
          SSSpaceE.sharedSpace, //space
          null, //circles
          note.getUpdated(), //creationTime
          true, //withUserRestriction
          false)); //shouldCommit
      
      for(String noteTag : noteTagNames){
        
        ((SSEvalServerI) SSServReg.getServ(SSEvalServerI.class)).evalLog(
          new SSEvalLogPar(
            userUri,
            SSToolContextE.evernoteImport,
            SSEvalLogE.addTag,
            noteUri,
            noteTag, //content
            SSUri.asListWithoutNullAndEmpty(notebookUri), //entities
            null, //users
            false)); //shouldCommit
      }
      
      addNoteUEs(
        note,
        noteUri);
      
      new SSDataImportEvernoteNoteContentHandler(
        fileServ,
        userUri,
        noteWithContent,
        noteUri,
        evernoteInfo.noteStore,
        localWorkPath).handleNoteContent();
    }
  }
  
  private void addNoteUEs(
    final Note         note,
    final SSUri        noteUri) throws Exception {
    
    final List<SSEntity> existingCreationUEs =
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventsGet(
        new SSUEsGetPar(
          userUri, //user
          userUri, //forUser
          noteUri, //entity
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNoteCreate), //types
          null,
          null,
          true, //withUserRestriction,
          false)); //invokeEntityHandlers
    
    if(existingCreationUEs.isEmpty()){
      
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventAdd(
        new SSUEAddPar(
          userUri,
          noteUri,
          SSUEE.evernoteNoteCreate,
          SSStrU.empty,
          note.getCreated(),
          true,
          false)); //shouldCommit
    }
    
    final List<SSEntity> existingUpdateUEs =
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventsGet(
        new SSUEsGetPar(
          userUri, //user
          userUri, //forUser
          noteUri, //entity
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNoteUpdate), //types
          note.getUpdated(),
          note.getUpdated(),
          true, //withUserRestriction,
          false)); //invokeEntityHandlers
    
    if(existingUpdateUEs.isEmpty()){
      
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventAdd(
        new SSUEAddPar(
          userUri,
          noteUri,
          SSUEE.evernoteNoteUpdate,
          SSStrU.empty,
          note.getUpdated(),
          true,
          false)); //shouldCommit
    }
    
    if(note.getDeleted() != 0L){
      
      final List<SSEntity> existingDeleteUEs =
        ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventsGet(
          new SSUEsGetPar(
            userUri, //user
            userUri, //forUser
            noteUri, //entity
            SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNoteDelete), //types
            null,
            null,
            true, //withUserRestriction,
            false)); //invokeEntityHandlers
      
      if(existingDeleteUEs.isEmpty()){
        
        ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventAdd(
          new SSUEAddPar(
            userUri,
            noteUri,
            SSUEE.evernoteNoteDelete,
            SSStrU.empty,
            note.getDeleted(),
            true,
            false)); //shouldCommit
      }
    }
    
    final NoteAttributes noteAttr = note.getAttributes();
    
    if(noteAttr == null){
      return;
    }
    
    if(noteAttr.getShareDate() != 0L){
      
      final List<SSEntity> existingShareUEs =
        ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventsGet(
          new SSUEsGetPar(
            userUri, //user
            userUri, //forUser
            noteUri, //entity
            SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNoteShare), //types
            noteAttr.getShareDate(),
            noteAttr.getShareDate(),
            true, //withUserRestriction,
            false)); //invokeEntityHandlers
      
      if(existingShareUEs.isEmpty()){
        
        ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventAdd(
          new SSUEAddPar(
            userUri,
            noteUri,
            SSUEE.evernoteNoteShare,
            SSStrU.empty,
            noteAttr.getShareDate(),
            true,
            false)); //shouldCommit
      }
    }
    
    if(noteAttr.getReminderDoneTime() != 0L){
      
      final List<SSEntity> existingReminderUEs =
        ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventsGet(
          new SSUEsGetPar(
            userUri, //user
            userUri, //forUser
            noteUri, //entity
            SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteReminderDone), //types
            noteAttr.getReminderDoneTime(),
            noteAttr.getReminderDoneTime(),
            true, //withUserRestriction,
            false)); //invokeEntityHandlers
      
      if(existingReminderUEs.isEmpty()){
        
        ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventAdd(
          new SSUEAddPar(
            userUri,
            noteUri,
            SSUEE.evernoteReminderDone,
            SSStrU.empty,
            noteAttr.getReminderDoneTime(),
            true,
            false)); //shouldCommit
      }
    }
    
    if(noteAttr.getReminderTime() != 0L){
      
      final List<SSEntity> existingReminder2UEs =
        ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventsGet(
          new SSUEsGetPar(
            userUri, //user
            userUri, //forUser
            noteUri, //entity
            SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteReminderCreate), //types
            noteAttr.getReminderTime(),
            noteAttr.getReminderTime(),
            true, //withUserRestriction,
            false)); //invokeEntityHandlers
      
      if(existingReminder2UEs.isEmpty()){
        
        ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventAdd(
          new SSUEAddPar(
            userUri,
            noteUri,
            SSUEE.evernoteReminderCreate,
            SSStrU.empty,
            noteAttr.getReminderTime(),
            true,
            false)); //shouldCommit
      }
    }
  }
  
  private void addNote(
    final SSUri   noteUri,
    final SSLabel noteLabel,
    final Note    note,
    final SSUri   notebookUri) throws Exception{
    
    final SSEntity nootebookEntity;
    
    try{
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          userUri,
          noteUri,
          SSEntityE.evernoteNote, //type,
          noteLabel, //label
          null, //description,
          note.getCreated(), //creationTime,
          null, //read,
          false, //setPublic
          true, //withUserRestriction
          false)); //shouldCommit)
     
      nootebookEntity =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
          new SSEntityGetPar(
            userUri,
            notebookUri, //entity
            true, //withUserRestriction
            null)); //descPar
      
      if(nootebookEntity == null){
        
        addNotebook(
          notebookUri,
          null,
          null);
      }
      
      SSServCaller.evernoteNoteAdd(
        this.userUri,
        notebookUri,
        noteUri,
        false);
      
      ((SSEvalServerI) SSServReg.getServ(SSEvalServerI.class)).evalLog(
        new SSEvalLogPar(
          userUri,
          SSToolContextE.evernoteImport,
          SSEvalLogE.addNote,
          noteUri,
          null, //content
          SSUri.asListWithoutNullAndEmpty(notebookUri), //entities
          SSUri.asListWithoutNullAndEmpty(), //users
          false)); //shouldCommit
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void handleResources() throws Exception{
    
    final List<Resource> resources = evernoteInfo.noteStoreSyncChunk.getResources();
    Resource             resourceWithContent;
    SSUri                resourceUri;
    Note                 note;
    SSUri                noteUri;
    
    if(resources == null){
      return;
    }
    
    for(Resource resource : resources){
      
      resourceWithContent = SSServCaller.evernoteResourceGet                  (evernoteInfo.noteStore, resource.getGuid(), false);
      
      try{
        if(SSFileExtE.isImageFileExt(SSMimeTypeE.fileExtForMimeType1(resourceWithContent.getMime()))){
          
          if(resourceWithContent.getAttributes().isSetSourceURL()){
            SSLogU.info("evernote image with source url ignored: " + resourceWithContent.getAttributes().getSourceURL());
            continue;
          }
          
          if(
            !resourceWithContent.isSetHeight() ||
            !resourceWithContent.isSetWidth()){
            
            SSLogU.info("evernote image resource height or width not set");
            continue;
          }
          
          if(
            resourceWithContent.getWidth()  <= 250||
            resourceWithContent.getHeight() <= 250){
            
            SSLogU.info("evernote image resource height or width < 250");
            continue;
          }
        }
      }catch(Exception error){
      }
      
      resourceWithContent = SSServCaller.evernoteResourceGet                  (evernoteInfo.noteStore, resource.getGuid(), true);
      resourceUri         = getResourceUri           (evernoteInfo, resource);
      note                = SSServCaller.evernoteNoteGet                      (evernoteInfo.noteStore, resource.getNoteGuid(), false);
      noteUri             = getNormalOrSharedNoteUri (evernoteInfo, note);
      
      addResource(
        resourceUri,
        getResourceLabel(
          resource,
          note),
        note.getUpdated(),
        noteUri);
      
      fileServ.fileAdd(
        new SSEntityFileAddPar(
          userUri,
          resourceWithContent.getData().getBody(), //fileBytes, 
          resourceWithContent.getData().getSize(), //fileLength
          SSMimeTypeE.fileExtForMimeType1(resourceWithContent.getMime()), //fileExt
          null, //file
          SSEntityE.file, //type,
          null, //label
          resourceUri, //entity
          true, //createThumb,
          resourceUri, //entityToAddThumbTo
          true, //removeExistingFilesForEntity
          true, //withUserRestriction
          false));//shouldCommit
      
      addResourceUEs(
        resourceUri,
        note.getUpdated());
    }
  }
  
  private void addResourceUEs(
    final SSUri resourceUri,
    final Long  resourceAddTime) throws Exception{
    
    final List<SSEntity> existingUEs =
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventsGet(
        new SSUEsGetPar(
          userUri, //user
          userUri, //forUser
          resourceUri, //entity
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteResourceAdd), //types
          resourceAddTime,
          resourceAddTime,
          true, //withUserRestriction,
          false)); //invokeEntityHandlers
    
    if(existingUEs.isEmpty()){
      
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventAdd(
        new SSUEAddPar(
          userUri,
          resourceUri,
          SSUEE.evernoteResourceAdd,
          SSStrU.empty,
          resourceAddTime,
          true,
          false)); //shouldCommit
    }
  }
  
  private void addResource(
    final SSUri   resourceUri,
    final SSLabel resourceLabel,
    final Long    resourceAddTime,
    final SSUri   noteUri) throws Exception{
    
    ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
      new SSEntityUpdatePar(
        userUri,
        resourceUri,
        SSEntityE.evernoteResource, //type,
        resourceLabel, //label
        null, //description,
        resourceAddTime, //creationTime,
        null, //read,
        false, //setPublic
        true, //withUserRestriction
        false)); //shouldCommit)
    
    SSServCaller.evernoteResourceAdd(
      this.userUri,
      noteUri,
      resourceUri,
      false);
    
    ((SSEvalServerI) SSServReg.getServ(SSEvalServerI.class)).evalLog(
      new SSEvalLogPar(
        userUri,
        SSToolContextE.evernoteImport,
        SSEvalLogE.addResource,
        resourceUri,
        null, //content
        SSUri.asListWithoutNullAndEmpty(noteUri), //entitites
        null,  //users
        false)); //shouldCommit
  }
  
  private SSUri getNormalOrSharedNotebookUri(SSLabel userName, Notebook notebook, List<String> sharedNotebookGuids) throws Exception{
    
    try{
      
      if(
        sharedNotebookGuids.contains   (notebook.getGuid()) &&
        !SSStrU.isEmpty                (notebook.getName())){
        return SSUri.get(createSharedNotebookUriStr(userName, notebook));
      }
      
    }catch(Exception error){}
    
    return getNotebookDefaultUri(notebook);
  }
  
  private SSUri getNormalOrSharedNoteUri(SSEvernoteInfo evernoteInfo, Note note) throws Exception {
    return SSUri.get(evernoteInfo.shardUri + "view/notebook/" + note.getGuid());
  }
  
  private SSUri getLinkedNotebookUri(LinkedNotebook linkedNotebook) throws Exception {
    return SSUri.get(linkedNotebook.getWebApiUrlPrefix() + "share/" + linkedNotebook.getShareKey());
  }
  
  private SSUri getResourceUri(SSEvernoteInfo evernoteInfo, Resource resource) throws Exception{
    return SSUri.get(evernoteInfo.shardUri + "res/" + resource.getGuid());
  }
  
  private String createSharedNotebookUriStr(SSLabel userName, Notebook notebook) throws Exception{
    
    //TODO dtheiler: check evernote environment to use here
    return SSLinkU.httpsEvernote + "pub/" + SSStrU.toStr(userName) + SSStrU.slash + notebook.getPublishing().getUri(); //7SSStrU.replaceAllBlanksSpecialCharactersDoubleDots(notebook.getName(), SSStrU.empty)
  }
  
  private SSUri getNotebookDefaultUri(Notebook notebook) throws Exception{
    
    if(
      notebook                  == null ||
      SSStrU.isEmpty(notebook.getGuid())){
      return SSUri.get(SSLinkU.httpsEvernote);
    }
    
    return SSUri.get(SSLinkU.httpsEvernote + "Home.action#b=" + notebook.getGuid());
  }
  
  private SSLabel getNormalOrSharedNotebookLabel(
    final Notebook notebook) throws Exception{
    
    try{
      final SSLabel tmpLabel = SSLabel.get(notebook.getName());
      
      if(tmpLabel == null){
        return getDefaultLabel();
      }else{
        return tmpLabel;
      }
    }catch(Exception error){
      return getDefaultLabel();
    }
  }
  
  private SSLabel getLinkedNotebookLabel(
    final LinkedNotebook linkedNotebook) throws Exception{
    
    try{
      final SSLabel tmpLabel = SSLabel.get(linkedNotebook.getShareName());
      
      if(tmpLabel == null){
        return getDefaultLabel();
      }else{
        return tmpLabel;
      }
    }catch(Exception error){
      return getDefaultLabel();
    }
  }
  
  public SSLabel getNoteLabel(
    final Note  note) throws Exception {
    
    try{
      final SSLabel tmpLabel = SSLabel.get(note.getTitle());
      
      if(tmpLabel == null){
        return getDefaultLabel();
      }else{
        return tmpLabel;
      }
    }catch(Exception error){
      return getDefaultLabel();
    }
  }
  
  private SSLabel getResourceLabel(
    final Resource resource,
    final Note     note) throws Exception{
    
    try{
      
      if(
        SSObjU.isNull (resource, resource.getAttributes()) ||
        SSStrU.isEmpty(resource.getAttributes().getFileName())){
        
        return getNoteLabel(note);
      }
      
      return SSLabel.get(resource.getAttributes().getFileName());
    }catch(Exception error){
      return getDefaultLabel();
    }
  }
  
  private SSLabel getDefaultLabel() throws Exception{
    return SSLabel.get("no label");
  }
  
  private Boolean addCurrentlyRunEvernoteImport(
    final String authToken,
    final String authEmail) throws Exception{
    
    try{
      
      if(!currentlyRunEvernoteImportsLock.isWriteLockedByCurrentThread()){
        currentlyRunEvernoteImportsLock.writeLock().lock();
      }
      
      if(currentlyRunEvernoteImports.containsValue(authToken)){
        SSLogU.warn("attempted to start B&P evernote import concurrently for " + authEmail);
        return false;
      }
      
      currentlyRunEvernoteImports.put(Thread.currentThread(), authToken);
      
      return true;
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      
      if(currentlyRunEvernoteImportsLock.isWriteLockedByCurrentThread()){
        currentlyRunEvernoteImportsLock.writeLock().unlock();
      }
    }
  }
  
  private void removeCurrentlyRunEvernoteImport(final String authToken) throws Exception{
    
    try{
      currentlyRunEvernoteImportsLock.writeLock().lock();
      
      if(authToken != null){
        currentlyRunEvernoteImports.remove(Thread.currentThread());
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(currentlyRunEvernoteImportsLock.isWriteLockedByCurrentThread()){
        currentlyRunEvernoteImportsLock.writeLock().unlock();
      }
    }
  }
}


//  public Boolean isSharedNootebook(SSUri notebookUri, SSLabel userName, Notebook notebook) {
//    return uriHelper.isSharedNotebookUri(userName, notebook, notebookUri);
//  }



//  public String getUserEmail(final SSEvernoteInfo evernoteInfo) throws Exception{
//    return evernoteInfo.userStore.getUser().getEmail();
//  }

//  private static SSLabel getLinkedNoteLabel(
//    final Note  note) throws Exception {
//
//    try{
//      final SSLabel tmpLabel = SSLabel.get(note.getTitle());
//
//      if(tmpLabel == null){
//        return getDefaultLabel();
//      }else{
//        return tmpLabel;
//      }
//    }catch(Exception error){
//      return getDefaultLabel();
//    }
//  }

//private static SSUri getLinkedNoteUri(LinkedNotebook linkedNotebook, Note note) throws Exception{
//    return SSUri.get(linkedNotebook.getWebApiUrlPrefix() + "share/view/" + linkedNotebook.getShareKey() + "?#n=" + note.getGuid());
//  }
//
//  private static Boolean isSharedNotebookUri(SSLabel userName, Notebook notebook, SSUri notebookUri){
//
//    String sharedNotebookUriStr;
//
//    try{
//      sharedNotebookUriStr = createSharedNotebookUriStr(userName, notebook);
//    }catch(Exception error){
//      return false;
//    }
//
//    if(
//      notebookUri == null ||
//      SSStrU.isEmpty(notebookUri.toString())){
//      return false;
//    }
//
//    return notebookUri.toString().equals(sharedNotebookUriStr);
//  }